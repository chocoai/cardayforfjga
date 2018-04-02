package com.cmdt.carrental.common.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.bean.AlertType;
import com.cmdt.carrental.common.dao.VehicleAlertDao;
import com.cmdt.carrental.common.dao.VehicleAlertJobDao;
import com.cmdt.carrental.common.entity.EventConfig;
import com.cmdt.carrental.common.entity.Marker;
import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.entity.VehicleAlert;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.AlertCountModel;
import com.cmdt.carrental.common.model.AlertDiagramStatisticModel;
import com.cmdt.carrental.common.model.AlertJsonData;
import com.cmdt.carrental.common.model.AlertMetaData;
import com.cmdt.carrental.common.model.AlertReport;
import com.cmdt.carrental.common.model.AlertStatSQLModel;
import com.cmdt.carrental.common.model.CountModel;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.IsInMarker;
import com.cmdt.carrental.common.model.MarkerModel;
import com.cmdt.carrental.common.model.OBDQueryDTO;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.Point;
import com.cmdt.carrental.common.model.QueryAlertInfoModel;
import com.cmdt.carrental.common.model.RealtimeLatestDataModel;
import com.cmdt.carrental.common.model.StationModel;
import com.cmdt.carrental.common.model.TransGPStoAddress;
import com.cmdt.carrental.common.model.TransGPStoBaiduModel;
import com.cmdt.carrental.common.model.TripTraceModel;
import com.cmdt.carrental.common.model.VehicleAlertModel;
import com.cmdt.carrental.common.model.VehicleAlertQueryDTO;
import com.cmdt.carrental.common.model.VehicleHistoryTrack;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.util.BeanSortUtils;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.MessageTemplateUtil;
import com.cmdt.carrental.common.util.TimeUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * @author ZhaoBin
 *
 */
@Service
public class VehicleAlertServiceImpl implements VehicleAlertService {
	private static final Logger LOG = LoggerFactory.getLogger(VehicleAlertServiceImpl.class);
	@Autowired
	private VehicleAlertDao vehicleAlertDao;
	
	@Autowired
	private VehicleAlertJobDao vehicleAlertJobDao;
	
	@Autowired
	private ShouqiService shouqiService;
	
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private MessageService messageService;
	
    // @Autowired
    // private BaiduApi baiduApi;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	/**
	 * 
	 */
	@Override
	public List<VehicleAlert> findVehicleAlert(VehicleAlertQueryDTO vehicleAlertQueryDTO) {
		List<VehicleAlert> list = vehicleAlertDao.findVehicleAlert(vehicleAlertQueryDTO);
		if(list!=null && list.size()>0){
			List<Point> points = new ArrayList<Point>();
			for(VehicleAlert alertModel : list) {
				Point point = new Point();
				point.setLat(Double.valueOf(alertModel.getAlertLatitude()));
				point.setLon(Double.valueOf(alertModel.getAlertLongitude()));
				points.add(point);
			}
			list = transGPStoBaiduByGroup(list);
	//		String result = shouqiService.transGPStoBaidu(points);
	//		TransGPStoBaiduModel transGPStoBaiduModel = JsonUtils.json2Object(result, TransGPStoBaiduModel.class);
	//		if(transGPStoBaiduModel.getResult().size()>0 && transGPStoBaiduModel.getResult().size() == list.size()) {
	//			for(int i=0;i<list.size();i++) {
	//				list.get(i).setAlertLatitude(String.valueOf(transGPStoBaiduModel.getResult().get(i).getLat()));
	//				list.get(i).setAlertLongitude(String.valueOf(transGPStoBaiduModel.getResult().get(i).getLon()));
	//			}
	//		}
			
			}
		return list;
	}
	
	private List<VehicleAlert> transGPStoBaiduByGroup(List<VehicleAlert> pList) {
		LOG.info("pList.size:" + pList.size());
		List<VehicleAlert> rList = new ArrayList<VehicleAlert>();
		int length = pList.size();
		for(int i=0; i< (length / 100 + 1); i++) {
			int num = 100 * (i + 1);
			if (num > length) {
				num = length;
			}
			try {
				Point point;
				List<Point> points = new ArrayList<Point>();
				List<VehicleAlert> tempList = new ArrayList<VehicleAlert>();
				for(int j = 100 * i; j < num; j++) {
					tempList.add(pList.get(j));
					point = new Point();
					point.setLat(Double.valueOf(pList.get(j).getAlertLatitude()));
					point.setLon(Double.valueOf(pList.get(j).getAlertLongitude()));
					points.add(point);
				}
                // TransResponse<List<Point>> result = shouqiService.transGPStoBaidu(points);
				String resultJson =  shouqiService.transGPStoBaidu(points);
				TransGPStoBaiduModel  result = JsonUtils.json2Object(resultJson, TransGPStoBaiduModel.class);
				for(int m = 0; m<result.getResult().size(); m++) {
					VehicleAlert vAlert = new VehicleAlert();
					vAlert.setAlertTime(tempList.get(m).getAlertTime());
					vAlert.setAlertType(tempList.get(m).getAlertType());
					vAlert.setAlertLatitude(String.valueOf(result.getResult().get(m).getLat()));
					vAlert.setAlertLongitude(String.valueOf(result.getResult().get(m).getLon()));
					vAlert.setAlertPosition(tempList.get(m).getAlertPosition());
					rList.add(vAlert);
				}
			} catch (Exception e) {
				LOG.error("VehicleAlertServiceImpl.transformBaiduPoint, cause by:\n", e);
			}
		}
		return rList;
	}

	@Override
	public List<Marker> findMarker(String vehicleNumber) {
		return vehicleAlertDao.findMarker(vehicleNumber);
	}


	@Override
	public VehicleModel getVehicleByVehicleNumber(Long entId,String vehicleNumber) {
			
		return vehicleAlertDao.getVehicleByVehicleNumber(entId,vehicleNumber);
	}


	@Override
	public MarkerModel findMarker(Long markerModelId) {
		Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.FINDMARKERBYID,
				new Object[] { markerModelId });
		MarkerModel markerModel=null;
		try {
			if (result != null && result.get("status").equals("success") && result.get("data") != null) {
				JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
				if ("000".equals(jsonNode.get("status").asText())) {
					JsonNode jsonNode2=jsonNode.get("result");
					 markerModel = JsonUtils.json2Object(jsonNode2.toString(), MarkerModel.class);
				}
			}
		} catch (Exception e) {
			LOG.error("MarkerService findMarkerByName error, cause by:\n", e);
		}
		return markerModel;
	}


	@Override
	public void populatePoint(JsonNode obdNode, List<TripTraceModel> list) throws Exception {
		JsonNode tracegeometryJsonNode = MAPPER.readTree(obdNode.get("tracegeometry").asText());
		JsonNode coordinatesNode = tracegeometryJsonNode.get("coordinates");
		if (coordinatesNode != null) {
			String coordinatesNodeTextVal = coordinatesNode.toString();
			if (StringUtils.isNotEmpty(coordinatesNodeTextVal)&&coordinatesNodeTextVal.length()>2) {
				String[] coordinatesArr = coordinatesNodeTextVal.replace("],[", "#").replace("[", "").replace("]", "").split("#");
				if (coordinatesArr != null) {
					int eleLength = coordinatesArr.length;
					for (int i = 0; i < eleLength; i++) {
						String[] coordinatesValArr = coordinatesArr[i].split(",");
						TripTraceModel tripTraceModel = new TripTraceModel();
						tripTraceModel.setLongitude(coordinatesValArr[0]);
						tripTraceModel.setLatitude(coordinatesValArr[1]);
						list.add(tripTraceModel);
					}
				}
			}
		}
	}
	@Override
	public List<TripTraceModel> populateSpeedAndAddress(JsonNode obdNode, List<TripTraceModel> list) throws Exception {
		List<TripTraceModel> resultList = new ArrayList<TripTraceModel>();
		String idlistTextVal = obdNode.get("idlist").asText();
		String[] idlistArr = null;
		if (StringUtils.isNotEmpty(idlistTextVal)) {
			idlistArr = idlistTextVal.replace("},{", "#").replace("{", "").replace("[", "").replace("]", "").replace("}", "").split("#");
			int idListArrLength = idlistArr.length;
			if (idListArrLength > 0) {
				Object[] objArr = new Object[idListArrLength];
				for (int i = 0; i < idListArrLength; i++) {
					objArr[i] = idlistArr[i];
				}
				int length = list.size();
				for (int x = 0; x < (length / 200 + 1); x++) {
					int num = 200 * (x + 1);
					if (num > length) {
						num = length;
					}
					Object[] objArrTemp = new Object[num];
					List<TripTraceModel> tempList = new ArrayList<TripTraceModel>();
					int z = 0;
					for (int y = 200 * x; y < num; y++) {
						objArrTemp[z] = objArr[y];
						tempList.add(list.get(y));
						z++;
					}
					resultList.addAll(findrealTimegeoListDetailById(objArrTemp, tempList));
				}
			}
		}
		return resultList;
		
		
//		String idlistTextVal = obdNode.get("idlist").asText();
//		String[] idlistArr = null;
//		if (StringUtils.isNotEmpty(idlistTextVal)&&idlistTextVal.length()>4) {
//			idlistArr = idlistTextVal.replace("},{", "#").replace("{", "").replace("[", "").replace("]", "").replace("}", "").split("#");
//			int idListArrLength = idlistArr.length;
//			if (idListArrLength > 0) {
//				Object[] objArr = new Object[idListArrLength];
//				for (int i = 0; i < idListArrLength; i++) {
//					objArr[i] = idlistArr[i];
//				}
//				Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.FINDREALTIMEGEOLISTDETAILBYID, objArr);
//				if (result.get("data") != null) {
//					JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
//					if ("000".equals(jsonNode.get("status").asText())) {
//						ArrayNode rows = (ArrayNode) jsonNode.get("result");
//						if (rows != null) {
//							int rowSize = rows.size();
//							// 首汽trace接口返回数据与包装返回speed与address的接口返回数据size一致
//							if (list.size() == rowSize) {
//								for (int i = 0; i < rowSize; i++) {
//									JsonNode speedAndAddressNode = rows.get(i);
//									TripTraceModel tripTraceModel = list.get(i);
//									tripTraceModel.setTracetime(speedAndAddressNode.get("tracetime").asText());
//									tripTraceModel.setSpeed(speedAndAddressNode.get("speed").asText());
//									tripTraceModel.setAddress(speedAndAddressNode.get("address").asText());
//									if(Integer.valueOf(tripTraceModel.getSpeed()) > 0){
//										tripTraceModel.setStatus("运行中");
//									}else{
//										tripTraceModel.setStatus("停止");
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
	}
	private List<TripTraceModel> findrealTimegeoListDetailById(Object[] objArr, List<TripTraceModel> list) throws Exception{
		Map<String, Object> result = new ServiceAdapter(shouqiService)
				.doService(ActionName.FINDREALTIMEGEOLISTDETAILBYID, objArr);
		if (result.get("data") != null) {
			JsonNode jsonNode;
			jsonNode = MAPPER.readTree(result.get("data").toString());
			if ("000".equals(jsonNode.get("status").asText())) {
				ArrayNode rows = (ArrayNode) jsonNode.get("result");
				if (rows != null) {
					int rowSize = rows.size();
					// 首汽trace接口返回数据与包装返回speed与address的接口返回数据size一致
					if (list.size() == rowSize) {
						for (int i = 0; i < rowSize; i++) {
							JsonNode speedAndAddressNode = rows.get(i);
							TripTraceModel tripTraceModel = list.get(i);
							tripTraceModel.setTracetime(speedAndAddressNode.get("tracetime").asText());
							tripTraceModel.setSpeed(speedAndAddressNode.get("speed").asText());
							tripTraceModel.setAddress(speedAndAddressNode.get("address").asText());
							if (Integer.valueOf(tripTraceModel.getSpeed()) > 0) {
								tripTraceModel.setStatus("运行中");
							} else {
								tripTraceModel.setStatus("停止");
							}
						}
					}
				}
			}
		}
		return list;
	}
	
	@Override
	public List<VehicleHistoryTrack> findTripTraceDataByTimeRange(String imei, String starttime, String endtime) throws Exception {
		List<VehicleHistoryTrack> retList = new ArrayList<VehicleHistoryTrack>();

		Object[] params = new Object[] { imei, starttime, endtime };
		Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.FINDVEHICLEHISTORYTRACK, params);
		if (result.get("data") != null) {
			JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
			if ("000".equals(jsonNode.get("status").asText())) {
				ArrayNode rows = (ArrayNode) jsonNode.get("result");
				if (rows != null) {
					int rowSize = rows.size();
					if (rowSize > 0) {
						for (int i = 0; i < rowSize; i++) {
							JsonNode vehicleHistoryTrackNode = rows.get(i);
							VehicleHistoryTrack vehicleHistoryTrack = new VehicleHistoryTrack();
							vehicleHistoryTrack.setTracetime(vehicleHistoryTrackNode.get("tracetime").asText());
							vehicleHistoryTrack.setLongitude(vehicleHistoryTrackNode.get("longitude").asText());
							vehicleHistoryTrack.setLatitude(vehicleHistoryTrackNode.get("latitude").asText());
							vehicleHistoryTrack.setSpeed(vehicleHistoryTrackNode.get("speed").asText());
							vehicleHistoryTrack.setAddress(vehicleHistoryTrackNode.get("address").asText());
							vehicleHistoryTrack.setStatus(vehicleHistoryTrackNode.get("status").asText());
							retList.add(vehicleHistoryTrack);
						}
					}
				}
			}
		}
		
		return retList;
	}


/*	@Override
	public List<StationModel> findStation(String vehicleNumber) {
		return vehicleAlertDao.findStation(vehicleNumber);
	}
*/

	@Override
	public List<StationModel> findStation(String vehicleNumber) {
		return vehicleAlertDao.findStation(vehicleNumber);
	}
	
	
	public static double formatNumberDecimals(double number,int decimal){
		BigDecimal b = new BigDecimal(number);  
		double f = b.setScale(decimal,BigDecimal.ROUND_HALF_UP).doubleValue();
		return f;
	}

	@Override
	public PagModel findAlertByPage(int currentPage, int numPerPage, Long rentId, boolean isEnt) {
		return vehicleAlertDao.findAlertByPage(currentPage,numPerPage,rentId, isEnt);
	}
	
	@Override
	public List<VehicleAlert> findTodayAlert(Long orgId,boolean isPre) {
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, true, true);
		return vehicleAlertDao.findTodayAlert(orgId, orgList,isPre);
	}
	
	@Override
	public List<AlertCountModel> findAlertCountByType(Long orgId) {
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, true, true);
		return vehicleAlertDao.findAlertCountList(orgId,orgList);
	}	
	
	@Override
	public List<AlertStatSQLModel> getDayAlert(List<Organization> orgList, Date startDate, Date endDate,Long orgId,Boolean self){
		return vehicleAlertDao.getDayAlertStat(startDate,endDate,orgList,orgId,self);
	}
	
	@Override
	public AlertStatSQLModel getDeptAlert(Long orgId,List<Organization> orgList, Date startDate, Date endDate,Boolean self){
		
		List<AlertStatSQLModel> treeList = vehicleAlertDao.getDeptAlertStat(startDate,endDate,orgList,orgId,self);
		
		//首先筛选出根节点
		AlertStatSQLModel rootNode = null;
		   for(AlertStatSQLModel countModel : treeList){
					if(countModel.getOrgId().longValue() == orgId.longValue()){
						rootNode = countModel;
						rootNode.setRootNode(true);
						break;
					}
		}
		
		buildAlertStatTree(rootNode,treeList);
		
		return rootNode;
	}
	
	
	private void buildAlertStatTree(AlertStatSQLModel rootNode, List<AlertStatSQLModel> alertStatisticsList)
    {
		AlertStatSQLModel currentNode = null;
        for (AlertStatSQLModel countModel : alertStatisticsList)
        {
            if (countModel.getParentId().longValue() == rootNode.getOrgId().longValue())
            {
            	currentNode = countModel;
            	rootNode.getChildren().add(currentNode);
            	buildAlertStatTree(currentNode, alertStatisticsList);
            }
        }
    }
	
	public List<CountModel> statisticDailyAlertByTypeDepartmentTimeRanger(Long orgId , String alertType,String fromDate,String toDate,Boolean selfDept, Boolean childDept){
		 List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, selfDept, childDept);
		 if(orgList!=null&&orgList.size()>0){
			 List<Long> orgIdList = new ArrayList<>();
			 for(Organization org : orgList){
				orgIdList.add(org.getId());
			 }	
			return vehicleAlertJobDao.statisticDailyAlertByTypeAndTimeRanger(orgIdList,orgId,selfDept,alertType, fromDate, toDate);
		 }else{
			 return new ArrayList<CountModel>();
		 }
	}
	
	public PagModel getAlertByDepartmentAndTimeRange(List<Organization> orgList,Boolean self,Long orgId,String alertType,String fromDate,String toDate,int currentPage, int numPerPage){
		return vehicleAlertJobDao.queryAlertByOrgTypeAndTimeRange(alertType,self,orgId,fromDate, toDate, orgList, currentPage, numPerPage);
	}
	
	@Override
	public AlertReport queryVehicleAlertStatisticsTopX(Date startDay, Date endDay,Long orgId,Boolean selfDept, Boolean childDept,int topCount){
		
        AlertReport report = new AlertReport();
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId,Boolean.TRUE, childDept);
		
		if(orgList!=null&&orgList.size()>0){
			List<AlertDiagramStatisticModel> overspeedTreeList= vehicleAlertJobDao.queryVehicleAlertStatistics( startDay, endDay, orgList,AlertType.OVERSPEED.toString(),orgId,selfDept);
			List<AlertDiagramStatisticModel> outboundTreeList= vehicleAlertJobDao.queryVehicleAlertStatistics( startDay, endDay, orgList,AlertType.OUTBOUND.toString(),orgId,selfDept);
			List<AlertDiagramStatisticModel> backstationTreeList= vehicleAlertJobDao.queryVehicleAlertStatistics( startDay, endDay, orgList,AlertType.VEHICLEBACK.toString(),orgId,selfDept);	
			
			
			//首先筛选出根节点
			AlertDiagramStatisticModel overspeedRootNode = null;
			for(AlertDiagramStatisticModel staticModel : overspeedTreeList){
				if(staticModel.getOrgId().longValue() == orgId.longValue()){
					    overspeedRootNode = staticModel;
					    overspeedRootNode.setRootNode(true);
						break;
					}
			}
			
			AlertDiagramStatisticModel outboundRootNode = null;
			for(AlertDiagramStatisticModel staticModel : outboundTreeList){
				if(staticModel.getOrgId().longValue() == orgId.longValue()){
					    outboundRootNode = staticModel;
					    outboundRootNode.setRootNode(true);
						break;
					}
			}
			
			AlertDiagramStatisticModel backRootNode = null;
			for(AlertDiagramStatisticModel staticModel : backstationTreeList){
				if(staticModel.getOrgId().longValue() == orgId.longValue()){
					    backRootNode = staticModel;
					    backRootNode.setRootNode(true);
						break;
					}
			}
			
			buildAlertTree(overspeedRootNode, overspeedTreeList);
			buildAlertTree(outboundRootNode, outboundTreeList);
			buildAlertTree(backRootNode, backstationTreeList);
			
			long durationDays = TimeUtils.timeBetween(startDay, endDay, Calendar.DATE);
			
			
			List<AlertDiagramStatisticModel> overspeedModelList = new ArrayList<>();
			List<AlertDiagramStatisticModel> outboundModelList = new ArrayList<>();
			List<AlertDiagramStatisticModel> backModelList = new ArrayList<>();
			
			
			if(selfDept){
				overspeedModelList.add(overspeedRootNode);
				outboundModelList.add(outboundRootNode);
				backModelList.add(backRootNode);
			}
			
			if(childDept){
				overspeedModelList.addAll(overspeedRootNode.getChildren());
				outboundModelList.addAll(outboundRootNode.getChildren());
				backModelList.addAll(backRootNode.getChildren());
			}
			
			List<AlertDiagramStatisticModel> outboundMilesModelList = new ArrayList<>(outboundModelList);
			
			report = this.generateReport(BeanSortUtils.sort(overspeedModelList, "alertTotal", false, topCount),
					                     BeanSortUtils.sort(outboundModelList, "alertTotal", false, topCount), 
					                     BeanSortUtils.sort(backModelList, "alertTotal", false, topCount),
					                     BeanSortUtils.sort(outboundMilesModelList, "outboundKilosTotal", false, topCount),
					                     durationDays);
		}
		return report;
	}
	
	private AlertReport generateReport(List<AlertDiagramStatisticModel> overspeedModelList,List<AlertDiagramStatisticModel> outboundModelList,List<AlertDiagramStatisticModel> backModelList,List<AlertDiagramStatisticModel> outboundMilesModelList,long duration){
		
		AlertReport alertReport = new AlertReport();
		List<AlertJsonData> pieList = new ArrayList<AlertJsonData>();
		List<AlertJsonData> columnarList = new ArrayList<AlertJsonData>();
		int overSpeedTotal = 0;
		int outboundTotal = 0;
		int vehicleBackTotal = 0;
		double outboundKiloTotal = 0;
		double overSpeedTotalAvg = 0;
		double outboundTotalAvg = 0;
		double vehicleBackTotalAvg = 0;
		double outboundKiloTotalAvg = 0;
		
		int overSpeedTotalOrgCount = overspeedModelList.size();
		int outboundTotalOrgCount = outboundModelList.size();
		int vehicleBackTotalOrgCount = backModelList.size();
		int outboundMileTotalOrgCount = outboundMilesModelList.size();

		
		
		for(AlertDiagramStatisticModel overModel : overspeedModelList){
			overSpeedTotal += overModel.getAlertTotal();
		}
		
		for(AlertDiagramStatisticModel outModel : outboundModelList){
			outboundTotal += outModel.getAlertTotal();
		}

		for(AlertDiagramStatisticModel backModel : backModelList){
			vehicleBackTotal += backModel.getAlertTotal();
		}
		
        for(AlertDiagramStatisticModel outMileModel : outboundMilesModelList){
        	outboundKiloTotal +=outMileModel.getOutboundKilosTotal();
		}
		
        
        vehicleBackTotalAvg = (double)vehicleBackTotal/duration;
        overSpeedTotalAvg=(double)overSpeedTotal/duration;
        outboundTotalAvg=(double)outboundTotal/duration;
		outboundKiloTotalAvg = (double)outboundKiloTotal/duration;
		
		
		AlertJsonData overSpeedColumnData = new AlertJsonData();
		AlertJsonData overSpeedPieData = new AlertJsonData();
		List<AlertMetaData> overSpeedColumnDataList = new ArrayList<>();
		List<AlertMetaData> overSpeedPieDataList = new ArrayList<>();
		overSpeedColumnData.setAlertType(AlertType.OVERSPEED.toString());
		overSpeedPieData.setAlertType(AlertType.OVERSPEED.toString());
		if(overSpeedTotalOrgCount>0){
		    overSpeedColumnData.setAverageNumber(formatNumberDecimals(overSpeedTotalAvg/overSpeedTotalOrgCount,2)+"");
		    overSpeedPieData.setAverageNumber(formatNumberDecimals(overSpeedTotalAvg/overSpeedTotalOrgCount,2)+"");
	    }
		overSpeedColumnData.setDataList(overSpeedColumnDataList);
		overSpeedPieData.setDataList(overSpeedPieDataList);
		
		AlertJsonData vehicleBackColumnData = new AlertJsonData();
		AlertJsonData vehicleBackPieData = new AlertJsonData();
		List<AlertMetaData> vehicleBackColumnDataList = new ArrayList<>();
		List<AlertMetaData> vehicleBackPieDataList = new ArrayList<>();
		vehicleBackColumnData.setAlertType(AlertType.VEHICLEBACK.toString());
		vehicleBackPieData.setAlertType(AlertType.VEHICLEBACK.toString());
		if(vehicleBackTotalOrgCount>0){
			vehicleBackColumnData.setAverageNumber(formatNumberDecimals(vehicleBackTotalAvg/vehicleBackTotalOrgCount,2)+"");
			vehicleBackPieData.setAverageNumber(formatNumberDecimals(vehicleBackTotalAvg/vehicleBackTotalOrgCount,2)+"");
		}
		vehicleBackColumnData.setDataList(vehicleBackColumnDataList);
		vehicleBackPieData.setDataList(vehicleBackPieDataList);
		
		AlertJsonData outBoundColumnData = new AlertJsonData();
		AlertJsonData outBoundPieData = new AlertJsonData();
		List<AlertMetaData> outBoundColumnDataList = new ArrayList<>();
		List<AlertMetaData> outBoundPieDataList = new ArrayList<>();
		outBoundColumnData.setAlertType(AlertType.OUTBOUND.toString());
		outBoundPieData.setAlertType(AlertType.OUTBOUND.toString());
		if(outboundTotalOrgCount>0){
			outBoundColumnData.setAverageNumber(formatNumberDecimals(outboundTotalAvg/outboundTotalOrgCount,2)+"");
			outBoundPieData.setAverageNumber(formatNumberDecimals(outboundTotalAvg/outboundTotalOrgCount,2)+"");
		}
		outBoundColumnData.setDataList(outBoundColumnDataList);
		outBoundPieData.setDataList(outBoundPieDataList);
		
		AlertJsonData outBoundKiloColumnData = new AlertJsonData();
		AlertJsonData outBoundKiloPieData = new AlertJsonData();
		List<AlertMetaData>  outBoundKiloColumnDataList = new ArrayList<>();
		List<AlertMetaData>  outBoundKiloPieDataList = new ArrayList<>();
		outBoundKiloColumnData.setAlertType(AlertType.OUTBOUNDKILOS.toString());
		outBoundKiloPieData.setAlertType(AlertType.OUTBOUNDKILOS.toString());
		if(outboundMileTotalOrgCount>0){
			outBoundKiloColumnData.setAverageNumber(formatNumberDecimals(outboundKiloTotalAvg/outboundMileTotalOrgCount,2)+"");
			outBoundKiloPieData.setAverageNumber(formatNumberDecimals(outboundKiloTotalAvg/outboundMileTotalOrgCount,2)+"");
		}
		outBoundKiloColumnData.setDataList(outBoundKiloColumnDataList);
		outBoundKiloPieData.setDataList(outBoundKiloPieDataList);
		
		
		
		for(AlertDiagramStatisticModel overspeedModel : overspeedModelList){
			AlertMetaData osChildColumnMetaData = new AlertMetaData();
			osChildColumnMetaData.setData(formatNumberDecimals((double)overspeedModel.getAlertTotal()/duration,2)+"");
			osChildColumnMetaData.setName(overspeedModel.getOrgName());
			
			AlertMetaData osChildPieMetaData = new AlertMetaData();
			osChildPieMetaData.setData(overspeedModel.getAlertTotal()+"");
			osChildPieMetaData.setName(overspeedModel.getOrgName());
			if(overSpeedTotal>0){
				osChildPieMetaData.setPercent(formatNumberDecimals((double)overspeedModel.getAlertTotal()/overSpeedTotal*100,2)+"%");
			}
			overSpeedColumnDataList.add(osChildColumnMetaData);
			overSpeedPieDataList.add(osChildPieMetaData);
		}
		
		for(AlertDiagramStatisticModel backChildModel : backModelList){
			
			AlertMetaData backChildColumnMetaData = new AlertMetaData();
			backChildColumnMetaData.setData(formatNumberDecimals((double)backChildModel.getAlertTotal()/duration,2)+"");
			backChildColumnMetaData.setName(backChildModel.getOrgName());
			
			AlertMetaData backChildPieMetaData = new AlertMetaData();
			backChildPieMetaData.setData(backChildModel.getAlertTotal()+"");
			backChildPieMetaData.setName(backChildModel.getOrgName());
			if(vehicleBackTotal>0){
				backChildPieMetaData.setPercent(formatNumberDecimals((double)backChildModel.getAlertTotal()/vehicleBackTotal*100,2)+"%");
			}
			vehicleBackColumnDataList.add(backChildColumnMetaData);
			vehicleBackPieDataList.add(backChildPieMetaData);
			
		}

		for(AlertDiagramStatisticModel outboundChildModel : outboundModelList){
			
			AlertMetaData outboundChildColumnMetaData = new AlertMetaData();
			outboundChildColumnMetaData.setData(formatNumberDecimals((double)outboundChildModel.getAlertTotal()/duration,2)+"");
			outboundChildColumnMetaData.setName(outboundChildModel.getOrgName());
			
			AlertMetaData outBoundChildPieMetaData = new AlertMetaData();
			outBoundChildPieMetaData.setData(outboundChildModel.getAlertTotal()+"");
			outBoundChildPieMetaData.setName(outboundChildModel.getOrgName());
			if(outboundTotal>0){
				outBoundChildPieMetaData.setPercent(formatNumberDecimals((double)outboundChildModel.getAlertTotal()/outboundTotal*100,2)+"%");
			}
			
			outBoundColumnDataList.add(outboundChildColumnMetaData);
			outBoundPieDataList.add(outBoundChildPieMetaData);	
		}
		
		
        for(AlertDiagramStatisticModel outboundChildModel : outboundMilesModelList){
			
			AlertMetaData columnKiloChildMetaData = new AlertMetaData();
			columnKiloChildMetaData.setData(formatNumberDecimals((double)outboundChildModel.getOutboundKilosTotal()/duration,2)+"");
			columnKiloChildMetaData.setName(outboundChildModel.getOrgName());
			
			AlertMetaData pieKiloChildMetaData = new AlertMetaData();
			pieKiloChildMetaData.setData(outboundChildModel.getOutboundKilosTotal()+"");
			pieKiloChildMetaData.setName(outboundChildModel.getOrgName());
			if(outboundKiloTotal>0){
				pieKiloChildMetaData.setPercent(formatNumberDecimals((double)outboundChildModel.getOutboundKilosTotal()/outboundKiloTotal*100,2)+"%");
			}
			
			outBoundKiloColumnDataList.add(columnKiloChildMetaData);
			outBoundKiloPieDataList.add(pieKiloChildMetaData);
		}
		
		columnarList.add(overSpeedColumnData);
		columnarList.add(vehicleBackColumnData);
		columnarList.add(outBoundColumnData);
		columnarList.add(outBoundKiloColumnData);
		
		pieList.add(overSpeedPieData);
		pieList.add(vehicleBackPieData);
		pieList.add(outBoundPieData);
		pieList.add(outBoundKiloPieData);
		
		alertReport.setColumnarList(columnarList);
		alertReport.setPieList(pieList);
		return alertReport;
	}
	
	private void buildAlertTree(AlertDiagramStatisticModel rootNode, List<AlertDiagramStatisticModel> alertStatisticsList)
    {
		AlertDiagramStatisticModel currentNode = null;
        for (AlertDiagramStatisticModel alertDiagramStatisticModel : alertStatisticsList)
        {
            if (alertDiagramStatisticModel.getParent_id().longValue() == rootNode.getOrgId().longValue())
            {
            	currentNode = alertDiagramStatisticModel;
            	rootNode.getChildren().add(currentNode);
            	buildAlertTree(currentNode, alertStatisticsList);
            }
        }
    }
	
	@Override
	public PagModel findVehicleAlertInfo(QueryAlertInfoModel model) {
		List<Organization> list=organizationService.findDownOrganizationListByOrgId(model.getDeptId(),model.getIncludeSelf(),model.getIncludeChild());
		List<Long> orgIdList=new ArrayList<Long>();
		if(!list.isEmpty()){
			for (Organization organization : list) {
				orgIdList.add(organization.getId());
			}
			if (orgIdList.isEmpty()) {
				return null;
			}
		}
		return vehicleAlertDao.findVehicleAlertInfo(model,orgIdList);
	}

	

	//new old:statisticDailyAlertByTypeDepartmentTimeRanger
	@Override
	public List<CountModel> statAlertByType(String orgId, String alertType, String startDay, String endDay,
			Boolean selfDept, Boolean childDept) {
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(Long.parseLong(orgId), selfDept, childDept);
		if(orgList!=null&&orgList.size()>0){
			 List<Long> orgIdList = new ArrayList<>();
			 for(Organization org : orgList){
				orgIdList.add(org.getId());
			 }	
			return vehicleAlertJobDao.statisticDailyAlertByTypeAndTimeRanger(orgIdList,Long.parseLong(orgId),selfDept,alertType, startDay, endDay);
		}else{
			return new ArrayList<CountModel>(); 
		}
	}

	//new old:findAllVehicleAlert
	@Override
	public List<VehicleAlert> findAllVehicleAlertData(VehicleAlertQueryDTO vehicleAlertQueryDTO) {
		
		QueryAlertInfoModel model = new QueryAlertInfoModel();
		
		model.setAlertType(vehicleAlertQueryDTO.getAlertType());
		model.setDeptId(vehicleAlertQueryDTO.getOrganizationId().longValue());
		model.setStartTime(vehicleAlertQueryDTO.getStartTime());
		model.setEndTime(vehicleAlertQueryDTO.getEndTime());
		model.setIncludeSelf(vehicleAlertQueryDTO.getSelfDept());
		model.setIncludeChild(vehicleAlertQueryDTO.getChildDept());
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(model.getDeptId(),model.getIncludeSelf(),model.getIncludeChild());
		List<VehicleAlert> alertList = vehicleAlertDao.findAllVehicleAlert(model,orgList);
		return alertList;
		
		
//		List<VehicleAlert> alertList = new ArrayList<>();
//		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(Long.valueOf(String.valueOf(vehicleAlertQueryDTO.getOrganizationId())),vehicleAlertQueryDTO.getSelfDept(),vehicleAlertQueryDTO.getChildDept());
//		if(orgList!=null&&orgList.size()>0){
//			List<Long> orgIdList = new ArrayList<>();
//			 for(Organization org : orgList){
//				orgIdList.add(org.getId());
//			 }	
//			 alertList = vehicleAlertDao.findAllVehicleAlertData(orgIdList,vehicleAlertQueryDTO);
//		}
//		return alertList;
	}
	
	@Override
	public List<VehicleAlert> findAllVehicleAlert(QueryAlertInfoModel model) {
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(model.getDeptId(),model.getIncludeSelf(),model.getIncludeChild());
		List<VehicleAlert> alertList = vehicleAlertDao.findAllVehicleAlert(model,orgList);
		return alertList;
	}

	@Override
	public List<EventConfig> findAlarmConfig(Long entId) {
		return vehicleAlertDao.findAlarmConfig(entId);
	}

	@Override
	public void updateAlarmConfig(Long entId, String eventType, Boolean isEnable) {
		vehicleAlertDao.updateAlarmConfig(entId, eventType, isEnable);
	}

	@Override
	public boolean voilateAlarmProcess(Long vehicleId){
		EventConfig config = vehicleAlertJobDao.queryAlarmConfig(vehicleId,AlertType.VIOLATE.toString());
		if(config!=null&&config.isEnable()){
			Vehicle vehicle = vehicleService.findVehicleById(vehicleId);
			List<StationModel> stations = vehicleAlertJobDao.findStation(vehicleId);
			if(vehicle!=null&&StringUtils.isNotBlank(vehicle.getDeviceNumber())&&stations!=null&&stations.size()>0){
			    
			    //check obd realtime data
			    List<OBDQueryDTO> obdList = new ArrayList<>();
		        OBDQueryDTO obd = new OBDQueryDTO();
		        obd.setImei(vehicle.getDeviceNumber());
		        obdList.add(obd);
			    Map<String,RealtimeLatestDataModel> realtimeMap = getVehicleRealtimeData(obdList);
		        RealtimeLatestDataModel realtimeLatestData = realtimeMap.get(vehicle.getDeviceNumber());
		        
				if(geoFencing(vehicle.getDeviceNumber(),stations)){
					return true;
				}else{
				    if(realtimeLatestData != null){
				      //if out, generate voilate alarm and return false
	                    generateVoilateAlarm(vehicle,realtimeLatestData);
	                    return false;
				    }else{
				        return true;
				    }
				}
			}
		}
		//if no station restrict, return true direct
		return true;
	}
	
	public void generateVoilateAlarm(Vehicle vehicle, RealtimeLatestDataModel realtimeLatestData){
		VehicleAlertModel vehicleAlertModel = new VehicleAlertModel();
		if(realtimeLatestData.getSpeed() != 0)
			vehicleAlertModel.setAlertSpeed(realtimeLatestData.getSpeed().toString());
		if(realtimeLatestData.getTraceTime() != null)
			vehicleAlertModel.setAlertTime(new Timestamp(realtimeLatestData.getTraceTime().getTime()));
		vehicleAlertModel.setAlertType(AlertType.VIOLATE.toString());
		
		vehicleAlertModel.setCurrentUseOrgId(vehicle.getCurrentuseOrgId());
		vehicleAlertModel.setRentId(vehicle.getRentId());
		vehicleAlertModel.setEntId(vehicle.getEntId());
		vehicleAlertModel.setVehicleNumber(vehicle.getVehicleNumber());
		vehicleAlertModel.setVehicleType(vehicle.getVehicleType());
		
		//根据车牌号和行车时间查询当时的驾驶员,如果没有就查询默认司机
		DriverModel driverModel = vehicleAlertJobDao.findDriver(vehicle.getVehicleNumber(), realtimeLatestData.getTraceTime());
		if(null!=driverModel){
			 vehicleAlertModel.setDriverId(driverModel.getId());
		}else{
			driverModel = vehicleAlertJobDao.findDefaultDriverByVehicleNumber(vehicle.getVehicleNumber());
			if(null!=driverModel){
				 vehicleAlertModel.setDriverId(driverModel.getId());
			}
		}
		
	  //根据经度和纬度计算报警城市,地址
	  if(realtimeLatestData.getLatitude() != 0 && realtimeLatestData.getLatitude() != 0){
		String alertPosition = getPosition(realtimeLatestData.getLatitude(),realtimeLatestData.getLongitude());
		vehicleAlertModel.setAlertPosition(alertPosition);
		vehicleAlertModel.setAlertCity(getCityByAddress(alertPosition));
		vehicleAlertModel.setAlertLatitude(realtimeLatestData.getLatitude()+"");
		vehicleAlertModel.setAlertLongitude(realtimeLatestData.getLongitude()+"");
		}
	   vehicleAlertJobDao.create(vehicleAlertModel);
	   saveAlertToMessage(vehicleAlertModel);
	}
	
	public String getPosition(double latitude, double longitude) {
		try {
			TransGPStoAddress rResponse = shouqiService.getAddressByPoint(transGpsPoint(longitude,latitude));
			return rResponse.getResult();
		} catch (Exception e) {
			LOG.error("Vehicle alert service getPosition error, cause by\n", e);
			return null;
		}
	}
	
	private Point transGpsPoint(double lon, double lat){
		List<Point> list = new ArrayList<Point>();
		list.add(new Point(lon,lat));
		try{
		Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.TRANSGPSTOBAIDU, new Object[]{list});
		if (result.get("data") != null) {
    		JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
    		if (jsonNode.get("status").asInt() == 0) {
    			ArrayNode rows = (ArrayNode) jsonNode.get("result");
				if (rows != null && rows.size() > 0) {
					JsonNode resultNode = rows.get(rows.size()-1);
					return new Point(resultNode.get("lon").asDouble(),resultNode.get("lat").asDouble());
				}
    		}
		}
		}catch(Exception e){
			LOG.info("Trans GPS to baidu point error:"+e);
			return null;
		}
		return null;
	}
	
	public Map<String,RealtimeLatestDataModel> getVehicleRealtimeData(List<OBDQueryDTO> obdList) {
		Map<String,RealtimeLatestDataModel> obdDataMap = new HashMap<String,RealtimeLatestDataModel>();

		if (obdList != null && !obdList.isEmpty()) {
			Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.GETLATESTDATASBYIMEI,
					new Object[] { obdList });
			try {
				if (result != null && result.get("status").equals("success") && result.get("data") != null && StringUtils.isNotEmpty(result.get("data").toString())) {
					JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
					if ("000".equals(jsonNode.get("status").asText())) {

						ArrayNode rows = (ArrayNode) jsonNode.get("result");
						if (rows != null && rows.size() > 0) {
							for (int i = 0; i < rows.size(); i++) {
								JsonNode rowNode = rows.get(i);
								RealtimeLatestDataModel realtimeLatestDataModel = JsonUtils
										.json2Object(rowNode.toString(), RealtimeLatestDataModel.class);
								obdDataMap.put(realtimeLatestDataModel.getImei(),realtimeLatestDataModel);
							}
						}
						return obdDataMap;
					}
					return obdDataMap;
				}
				return obdDataMap;
			} catch (Exception e) {
				LOG.error("Abstract class Filter getVehicleRealtimeData failure, cause by\n", e);
				return obdDataMap;
			}
		}
		return obdDataMap;
	}
	
	
	public boolean geoFencing(String imei, List<StationModel> markers) {

		boolean isIn = false;

		List<Long> markerIds = new ArrayList<>();
		for (StationModel marker : markers) {
			markerIds.add(marker.getMarkerId());
		}

		try {
		    Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.GEOFENCING,
				new Object[] { imei, markerIds });
			// 首先判断是否越界
			if (result != null && result.get("status").equals("success") && result.get("data") != null) {
				JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
				if ("000".equals(jsonNode.get("status").asText())) {
					ArrayNode rows = (ArrayNode) jsonNode.get("result");
					if (rows != null && rows.size() > 0) {
						for (int i = 0; i < rows.size(); i++) {
							JsonNode rowNode = rows.get(i);
							IsInMarker isInMarker = JsonUtils.json2Object(rowNode.toString(), IsInMarker.class);
							if (isInMarker.isInMarker()) {
								isIn = true;
								break;
							}
						}
					}

				}
			} else {
				isIn = true;
				LOG.error("Error:VehicleAlertService validate marker failed!");
			}

		} catch (Exception e) {
			// if internet error, or interface error,set in marker as true
		    isIn = true;
			LOG.error("VehicleAlertService limit outbound data failure, cause by\n", e);
		}

		return isIn;
	}
	
	public String getCityByAddress(String address){

		if(StringUtils.isNotBlank(address)){

			//commented by unused
			//String pro=StringUtils.substring(address,0,address.indexOf("省")+1);
			String city=StringUtils.substring(address,address.indexOf("省")+1,address.indexOf("市")+1);
			return city;
		}else{
			return null;
		}

	}
	
	private void saveAlertToMessage(VehicleAlertModel alert){
		List<Message> messages = new ArrayList<>();
	    	Message msg = new Message();
	    	msg.setCarNo(alert.getVehicleNumber());
	    	msg.setIsNew(1);
	    	msg.setLocation(alert.getAlertPosition());
	    	msg.setOrgId(alert.getCurrentUseOrgId());
	    	msg.setTime(new java.sql.Date(alert.getAlertTime().getTime()));
	    	msg.setType(Message.MessageType.VIOLATE);
	    	msg.setWarningId(alert.getId());
            msg.setMsg(MessageTemplateUtil.composeMessageForAlert(alert));
	    	messages.add(msg);
	    messageService.saveMessages(messages);
	}

}
