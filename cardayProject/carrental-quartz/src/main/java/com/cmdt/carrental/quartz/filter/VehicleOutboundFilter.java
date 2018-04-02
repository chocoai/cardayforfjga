package com.cmdt.carrental.quartz.filter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cmdt.carrental.common.bean.AlertType;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.MarkerModel;
import com.cmdt.carrental.common.model.OBDQueryDTO;
import com.cmdt.carrental.common.model.OutboundMarkerModel;
import com.cmdt.carrental.common.model.RealtimeLatestDataModel;
import com.cmdt.carrental.common.model.TripTraceDisplayModelItem;
import com.cmdt.carrental.common.model.VehicleAlertModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleQueryDTO;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.quartz.spring.SpringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Component 
@Scope("prototype")
public class VehicleOutboundFilter<T> extends Filter<T> {
	private Logger LOG = Logger.getLogger(VehicleOutboundFilter.class);

	private List<Condition<T>> conditions = new ArrayList<Condition<T>>();;

	private List<VehicleQueryDTO> vehicleQueryDTOList;

	private List<OBDQueryDTO> obdList;

	@SuppressWarnings("unchecked")
	public VehicleOutboundFilter() {
		super();
		if(shouqiService == null){
			shouqiService = SpringUtils.getBean("shouqiService");
		}
		if(vehicleAlertJobService == null){
			vehicleAlertJobService = SpringUtils.getBean("vehicleAlertJobServiceImpl");
		}
		// 获得所有的obd list
		vehicleQueryDTOList = vehicleAlertJobService.findVehicleListHasMarker();
		// 转换OBDQueryDTO
		obdList = vehicleAlertJobService.toOBDList(vehicleQueryDTOList);
		//
		for(VehicleQueryDTO vehicleQueryDTO : vehicleQueryDTOList){
			Condition<OutboundMarkerModel> condition = new Condition<OutboundMarkerModel>();
			OutboundMarkerModel restrictValue = new OutboundMarkerModel();
			restrictValue.setVehicleNumber(vehicleQueryDTO.getVehicleNumber());
			restrictValue.setDeviceNumber(vehicleQueryDTO.getDeviceNumber());
			
			List<MarkerModel> markers = vehicleAlertJobService.findMarker(vehicleQueryDTO.getVehicleNumber());
			restrictValue.setMarkers(markers);
			condition.setRestrictValue(restrictValue);
			conditions.add((Condition<T>) condition);
		}
		
	}

	@Override
	public List<VehicleAlertModel> doFilter() {
		LOG.info("VehicleOutboundFilter start doFilter...");

			Map<String,RealtimeLatestDataModel> obdDataMap = getVehicleRealtimeData(obdList);
			List<VehicleAlertModel> filteredData = new ArrayList<VehicleAlertModel>();
			if(obdDataMap == null || obdDataMap.isEmpty())
				return null;
			// 转换Map
			Map<String, String> vehicleMap = vehicleAlertJobService.toMap(vehicleQueryDTOList);
			if (conditions != null && !conditions.isEmpty()) {
				for (Condition<T> condition : conditions) {
					OutboundMarkerModel outboundMarkerModel = (OutboundMarkerModel) (condition.getRestrictValue());

					vehicleAlertJobService.limit(outboundMarkerModel, obdDataMap);
				}
				/**
				 * OBDDataMap  
				 */
				for(Map.Entry<String,RealtimeLatestDataModel> entry: obdDataMap.entrySet()){
					RealtimeLatestDataModel realtimeLatestData = entry.getValue();
					
					VehicleAlertModel vehicleAlertModel = new VehicleAlertModel();
					vehicleAlertModel.setAlertType(AlertType.OUTBOUND.toString());
					// 根据IMEI号查询车辆信息
					if (StringUtils.isNotEmpty(realtimeLatestData.getImei())&&realtimeLatestData.getLongitude()!=0&&realtimeLatestData.getLatitude()!=0) {
						VehicleModel vehicleModel = vehicleAlertJobService
								.getVehicle(vehicleMap.get(realtimeLatestData.getImei()));
						vehicleAlertModel.setCurrentUseOrgId(vehicleModel.getCurrentuseOrgId());
						vehicleAlertModel.setRentId(vehicleModel.getRentId());
						vehicleAlertModel.setEntId(vehicleModel.getEntId());
						vehicleAlertModel.setVehicleNumber(vehicleMap.get(realtimeLatestData.getImei()));
						vehicleAlertModel.setVehicleType(vehicleModel.getVehicleType());


						//根据车牌号和行车时间查询当时的驾驶员,如果没有就查询默认司机
						DriverModel driverModel = vehicleAlertJobService.findDriver(vehicleModel.getVehicleNumber(), realtimeLatestData.getTraceTime());
						if(null!=driverModel){
							 vehicleAlertModel.setDriverId(driverModel.getId());
						}else{
							driverModel = vehicleAlertJobService.findDefaultDriverByVehicleNumber(vehicleModel.getVehicleNumber());
							if(null!=driverModel){
								 vehicleAlertModel.setDriverId(driverModel.getId());
							}
						}
						
						String alertPosition = vehicleAlertJobService.getPosition(realtimeLatestData.getLatitude(),realtimeLatestData.getLongitude());
						vehicleAlertModel.setAlertPosition(alertPosition);
						vehicleAlertModel.setAlertCity(vehicleAlertJobService.getCityByPosition(realtimeLatestData.getLatitude(),realtimeLatestData.getLongitude()));
						vehicleAlertModel.setAlertLongitude(realtimeLatestData.getLongitude()+"");
						vehicleAlertModel.setAlertLatitude(realtimeLatestData.getLatitude()+"");
						vehicleAlertModel.setFirstOutboundtime(new Timestamp(realtimeLatestData.getTraceTime().getTime()));
						vehicleAlertModel.setFirstOutboundKilos((double)realtimeLatestData.getMileage());
						vehicleAlertModel.setFirstAlertLongitude(realtimeLatestData.getLongitude()+"");
						vehicleAlertModel.setFirstAlertLatitude(realtimeLatestData.getLatitude()+"");
						
						vehicleAlertModel.setAlertTime(new Timestamp(realtimeLatestData.getTraceTime().getTime()));
						filteredData.add(vehicleAlertModel);
					}
                    
				}
			} 
			
	    return filteredData;
	}

	@Override
	public List<VehicleAlertModel> doFilter(List<Condition<T>> conditions) {
		if (conditions != null && !conditions.isEmpty()) {
			this.conditions = conditions;
		}
		return doFilter();
	}

	//获取多个车辆在某一时间段内的轨迹数据
	@SuppressWarnings("unused")
	private Map<String, List<TripTraceDisplayModelItem>> getObdTraceDataMap(String startTime, String endTime) {
		Map<String, List<TripTraceDisplayModelItem>> retmap = new HashMap<String, List<TripTraceDisplayModelItem>>();
		for (OBDQueryDTO obd : obdList) {
			List<TripTraceDisplayModelItem> traceDataList = getVehicleTraceData(obd.getImei(), startTime, endTime);
			retmap.put(obd.getImei(), traceDataList);
		}
		return retmap;
	}

	/**
	 * 获取车辆在某一时间段内的轨迹数据.
	 */
	private List<TripTraceDisplayModelItem> getVehicleTraceData(String imei, String startTime, String endTime) {
		List<TripTraceDisplayModelItem> traceDataList = new ArrayList<TripTraceDisplayModelItem>();

		if (obdList != null && !obdList.isEmpty()) {
			Map<String, Object> result = new ServiceAdapter(shouqiService)
					.doService(ActionName.FINDTRIPTRACEDATABYTIMERANGE, new Object[] { obdList });
			try {
				if (result != null && result.get("status").equals("success") && result.get("data") != null) {
					JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
					if ("000".equals(jsonNode.get("status").asText())) {

						ArrayNode rows = (ArrayNode) jsonNode.get("result");
						if (rows != null && rows.size() > 0) {
							for (int i = 0; i < rows.size(); i++) {
								JsonNode rowNode = rows.get(0);
								TripTraceDisplayModelItem tripTraceDisplayModelItem = JsonUtils
										.json2Object(rowNode.toString(), TripTraceDisplayModelItem.class);
								traceDataList.add(tripTraceDisplayModelItem);
							}
						}
						return traceDataList;
					}
					return traceDataList;
				}
				return traceDataList;
			} catch (Exception e) {
				LOG.error("", e);
				return traceDataList;
			}
		}
		return traceDataList;
	}

	public List<Condition<T>> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition<T>> conditions) {
		this.conditions = conditions;
	}

}
