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
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.OBDQueryDTO;
import com.cmdt.carrental.common.model.RealtimeLatestDataModel;
import com.cmdt.carrental.common.model.StationModel;
import com.cmdt.carrental.common.model.VehicleAlertModel;
import com.cmdt.carrental.common.model.VehicleBackModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleQueryDTO;
import com.cmdt.carrental.quartz.spring.SpringUtils;

@Component 
@Scope("prototype")
public class VehicleBackFilter<T> extends Filter<T>{
	private Logger LOG = Logger.getLogger(VehicleBackFilter.class);
	
	private List<Condition<T>> conditions = new ArrayList<Condition<T>>();
	
	private List<VehicleQueryDTO> vehicleQueryDTOList;
	
	private List<OBDQueryDTO> obdList;
	
	private Map<String,List<StationModel>> stationsMap;
	
	@SuppressWarnings("unchecked")
	public VehicleBackFilter() {
		super();
		if(shouqiService == null){
			shouqiService = SpringUtils.getBean("shouqiService");
		}
		if(vehicleAlertJobService == null){
			vehicleAlertJobService = SpringUtils.getBean("vehicleAlertJobServiceImpl");
		}
		//获得所有绑定station的obd list
		vehicleQueryDTOList = vehicleAlertJobService.findVehicleListHasStationAndTimeLimit();
		//转换OBDQueryDTO
		obdList = vehicleAlertJobService.toOBDList(vehicleQueryDTOList);
		//
		stationsMap = new HashMap<>();
		
		for(VehicleQueryDTO vehicleQueryDTO : vehicleQueryDTOList){
			
			List<StationModel> stations = vehicleAlertJobService.findStation(vehicleQueryDTO.getVehicleNumber());
			if(stations!=null&&stations.size()>0){
				stationsMap.put(vehicleQueryDTO.getDeviceNumber(), stations);
				Condition<VehicleBackModel> condition = new Condition<VehicleBackModel>();
				VehicleBackModel restrictValue = new VehicleBackModel();
				restrictValue.setVehicleNumber(vehicleQueryDTO.getVehicleNumber());
				restrictValue.setDeviceNumber(vehicleQueryDTO.getDeviceNumber());
				restrictValue.setStartTime(vehicleQueryDTO.getStartTime());
				restrictValue.setEndTime(vehicleQueryDTO.getEndTime());
				
				restrictValue.setStations(stations);
				condition.setRestrictValue(restrictValue);
				conditions.add((Condition<T>) condition);
			}
		}
		
	}
	
	@Override
	public List<VehicleAlertModel> doFilter() {
		LOG.info("VehicleBackFilter start doFilter...");
		Map<String,RealtimeLatestDataModel> obdDataMap = getVehicleRealtimeData(obdList);
		if(obdDataMap == null || obdDataMap.isEmpty())
			return null;
		List<VehicleAlertModel> filteredData = new ArrayList<VehicleAlertModel>();
		//转换Map
		Map<String,String> vehicleMap = vehicleAlertJobService.toMap(vehicleQueryDTOList);
		if(conditions != null && !conditions.isEmpty()){
			//过滤出报警数据
			for(Condition<T> condition : conditions){
				VehicleBackModel vehicleBackModel = (VehicleBackModel)(condition.getRestrictValue());
				vehicleAlertJobService.limit(vehicleBackModel, obdDataMap);
			}
			//处理过滤出的报警数据并记录到数据库中
			for( Map.Entry<String,RealtimeLatestDataModel> entry: obdDataMap.entrySet()){
				RealtimeLatestDataModel realtimeLatestData = entry.getValue();
				VehicleAlertModel vehicleAlertModel = new VehicleAlertModel();
				if(realtimeLatestData.getSpeed() != 0)
					vehicleAlertModel.setAlertSpeed(realtimeLatestData.getSpeed().toString());
				if(realtimeLatestData.getTraceTime() != null)
					vehicleAlertModel.setAlertTime(new Timestamp(realtimeLatestData.getTraceTime().getTime()));
				vehicleAlertModel.setAlertType(AlertType.VEHICLEBACK.toString());
				
				vehicleAlertModel.setStations(stationsMap.get(realtimeLatestData.getImei()));
				
				if(StringUtils.isNotEmpty(realtimeLatestData.getImei())){
					VehicleModel vehicleModel = vehicleAlertJobService.getVehicle(vehicleMap.get(realtimeLatestData.getImei()));
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
					
				}
				
				//根据经度和纬度计算报警城市,地址
				if(realtimeLatestData.getLatitude() != 0 && realtimeLatestData.getLatitude() != 0){
					String alertPosition = vehicleAlertJobService.getPosition(realtimeLatestData.getLatitude(),realtimeLatestData.getLongitude());
					vehicleAlertModel.setAlertPosition(alertPosition);
					vehicleAlertModel.setAlertCity(vehicleAlertJobService.getCityByPosition(realtimeLatestData.getLatitude(),realtimeLatestData.getLongitude()));
					vehicleAlertModel.setAlertLatitude(realtimeLatestData.getLatitude()+"");
					vehicleAlertModel.setAlertLongitude(realtimeLatestData.getLongitude()+"");
					}
				
				if(vehicleAlertModel!=null&&vehicleAlertModel.getAlertType()!=null&&vehicleAlertModel.getAlertType().trim().length()>0)
				filteredData.add(vehicleAlertModel);
			}
			return filteredData;
		}else
			return filteredData;
	}
	
	@Override
	public List<VehicleAlertModel> doFilter(List<Condition<T>> conditions) {
		if(conditions != null && !conditions.isEmpty()){
			this.conditions = conditions;
		}
		return doFilter();
	}
	
	public List<Condition<T>> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition<T>> conditions) {
		this.conditions = conditions;
	}

}
