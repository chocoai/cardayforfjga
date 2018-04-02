package com.cmdt.carrental.quartz.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.bean.AlertType;
import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.dao.UserDao;
import com.cmdt.carrental.common.dao.VehicleAlertJobDao;
import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.IsInMarker;
import com.cmdt.carrental.common.model.MarkerModel;
import com.cmdt.carrental.common.model.OBDQueryDTO;
import com.cmdt.carrental.common.model.OutboundMarkerModel;
import com.cmdt.carrental.common.model.Point;
import com.cmdt.carrental.common.model.RealtimeLatestDataModel;
import com.cmdt.carrental.common.model.StationModel;
import com.cmdt.carrental.common.model.StationsDurationModel;
import com.cmdt.carrental.common.model.TransGPStoAddress;
import com.cmdt.carrental.common.model.TransGPStoBaiduModel;
import com.cmdt.carrental.common.model.UserModel;
import com.cmdt.carrental.common.model.VehicleAlertModel;
import com.cmdt.carrental.common.model.VehicleBackModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleQueryDTO;
import com.cmdt.carrental.common.service.MessageService;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.util.DistanceUtil;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.MessageTemplateUtil;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.quartz.filter.Filter;
import com.cmdt.carrental.quartz.util.DateUtils;
import com.cmdt.carrental.quartz.util.NumberFormatUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class VehicleAlertJobServiceImpl<T> implements VehicleAlertJobService<T> {
	private static final Logger LOG = LoggerFactory.getLogger(VehicleAlertJobServiceImpl.class);

	@Autowired
	private VehicleAlertJobDao vehicleAlertJobDao;

	@Autowired
	private ShouqiService shouqiService;

    // @Autowired
    // private BaiduApi baiduApi;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private CommunicationService commService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private OrganizationService organizationService;

	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 根据过滤规则过滤数据,并保存到数据库表busi_vehicle_alert中.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void executeAlertData(Filter filter) {
		List<VehicleAlertModel> filteredData = filter.doFilter();
		if (filteredData != null && !filteredData.isEmpty()){
			List<VehicleAlertModel> savedResult = vehicleAlertJobDao.create(filteredData);
			saveAlertToMessage(savedResult,false);
			//sendAlertPush(savedResult,false);
		}
			
	}

	@Override
	public List<VehicleQueryDTO> findVehicleList() {
		return vehicleAlertJobDao.findVehicleHasOrg();
	}

	@Override
	public List<VehicleQueryDTO> findVehicleListHasMarker() {
		return vehicleAlertJobDao.findVehicleHasMarker();
	}

	@Override
	public List<VehicleQueryDTO> findVehicleListHasStationAndTimeLimit() {
		return vehicleAlertJobDao.findVehicleListHasStationAndTimeLimit();
	}

	@Override
	public Map<String, String> toMap(List<VehicleQueryDTO> vehicleModelList) {
		Map<String, String> retMap = new HashMap<String, String>();
		for (VehicleQueryDTO vehicleModel : vehicleModelList) {
			retMap.put(vehicleModel.getDeviceNumber(), vehicleModel.getVehicleNumber());
		}
		return retMap;
	}

	@Override
	public List<OBDQueryDTO> toOBDList(List<VehicleQueryDTO> vehicleModelList) {
		List<OBDQueryDTO> obdList = new ArrayList<OBDQueryDTO>();
		for (VehicleQueryDTO vehicleModel : vehicleModelList) {
			obdList.add(new OBDQueryDTO(vehicleModel.getDeviceNumber()));
		}
		return obdList;
	}

	/**
	 * 过滤正常回站车辆数据，即产生异常回车数据
	 * 
	 * @param vehicleBackModel
	 * @param obdDataMap
	 * @return obdDataMap
	 */
	@Override
	public void limit(VehicleBackModel vehicleBackModel, Map<String, RealtimeLatestDataModel> obdDataMap) {

		List<StationModel> stations = vehicleBackModel.getStations();

		RealtimeLatestDataModel realtimeLatestDataModel = obdDataMap.get(vehicleBackModel.getDeviceNumber());

		//首先判断数据的有效性
		if (realtimeLatestDataModel!=null&&realtimeLatestDataModel.getLatitude() != 0 && realtimeLatestDataModel.getLongitude() != 0) {
			
			

			//再验证是否已经需要验证车辆归站
			//StationsDurationModel durationModel = vehicleAlertJobDao.findStationsDurationByVechileNumber(vehicleBackModel.getVehicleNumber());
			StationsDurationModel durationModel = new StationsDurationModel();
			durationModel.setStartTime(vehicleBackModel.getStartTime());
			durationModel.setEndTime(vehicleBackModel.getEndTime());
			durationModel.setVehicleNumber(vehicleBackModel.getVehicleNumber());
			LOG.info("begain check back alert for car : "+vehicleBackModel.getVehicleNumber());
			boolean needCheck = this.needCheckBack(durationModel,realtimeLatestDataModel.getTraceTime());
            if(needCheck){
            	VehicleAlertModel vehicleAlertModel = vehicleAlertJobDao.getLastAlertDataByType(vehicleBackModel.getVehicleNumber(), AlertType.VEHICLEBACK.toString());
				if (vehicleAlertModel != null) {
					
					if((realtimeLatestDataModel.getTraceTime().getTime() - vehicleAlertModel.getAlertTime().getTime())/(1000*60)<=60){
						//如果两次告警相差不到1小时，不重复发送
						obdDataMap.remove(vehicleBackModel.getDeviceNumber());
					}else{
					
						// 判断是否重复报警
						if (vehicleAlertModel.getAlertLatitude().equals(realtimeLatestDataModel.getLatitude() + "")
								&& vehicleAlertModel.getAlertLongitude().equals(realtimeLatestDataModel.getLongitude() + "")
								) {
							obdDataMap.remove(vehicleBackModel.getDeviceNumber());
						} else {
							Point realtimePoint = new Point(new Double(realtimeLatestDataModel.getLongitude()),
									new Double(realtimeLatestDataModel.getLatitude()));
							// 遍历所有站点,判断当前坐标是否在任一站点半径内
							for (StationModel stationModel : stations) {
								Point stationPoint = transBaiduToGpsPoint(new Double(stationModel.getLongitude()),new Double(stationModel.getLatitude()));
								//Point baiduRealtimePoint = transGpsPoint(new Double(realtimeLatestDataModel.getLongitude()),new Double(realtimeLatestDataModel.getLatitude()));
								// 如果当前车辆GPS坐标任一站点半径范围内, 则从报警数据集合中清除
								if (DistanceUtil.getDistance(stationPoint, realtimePoint) < new Double(stationModel.getRadius())) {
									obdDataMap.remove(vehicleBackModel.getDeviceNumber());
									break;
								}
							}
						}
					}
				} else {
					Point realtimePoint = new Point(new Double(realtimeLatestDataModel.getLongitude()),
							new Double(realtimeLatestDataModel.getLatitude()));
					// 遍历所有站点,判断当前坐标是否在任一站点半径内
					for (StationModel stationModel : stations) {
						Point stationPoint = transBaiduToGpsPoint(new Double(stationModel.getLongitude()),new Double(stationModel.getLatitude()));
//						Point stationPoint = new Point(new Double(stationModel.getLongitude()),
//								new Double(stationModel.getLatitude()));
						//Point baiduRealtimePoint = transGpsPoint(new Double(realtimeLatestDataModel.getLongitude()),new Double(realtimeLatestDataModel.getLatitude()));
						// 如果当前车辆GPS坐标任一站点半径范围内, 则从报警数据集合中清除
						if (DistanceUtil.getDistance(stationPoint, realtimePoint) < new Double(stationModel.getRadius())) {
							obdDataMap.remove(vehicleBackModel.getDeviceNumber());
							break;
						}
					}
				}
            }else{
            	obdDataMap.remove(vehicleBackModel.getDeviceNumber());
            }

		}else{
			obdDataMap.remove(vehicleBackModel.getDeviceNumber());
		}
	}


	/**
	 * @param outboundMarkerModel
	 * @param traceDataMap
	 * @return traceDataMap
	 */
	@Override
	public void limit(OutboundMarkerModel outboundMarkerModel, Map<String, RealtimeLatestDataModel> obdDataMap) {
		Map<String, VehicleAlertModel> releaseOutboundAlertMap = new HashMap<String, VehicleAlertModel>();

		RealtimeLatestDataModel realtimeLatestDataModel = obdDataMap.get(outboundMarkerModel.getDeviceNumber());
		String imei = outboundMarkerModel.getDeviceNumber();
		List<MarkerModel> markers = outboundMarkerModel.getMarkers();

		if (realtimeLatestDataModel!=null&&realtimeLatestDataModel.getLongitude() != 0 && realtimeLatestDataModel.getLatitude() != 0) {

			VehicleModel vehicleModel = new VehicleModel();
			vehicleModel.setVehicleNumber(outboundMarkerModel.getVehicleNumber());
			VehicleAlertModel vehicleAlertModel = vehicleAlertJobDao.getLatestOutboundData(vehicleModel);

			// 如果没有一个未关闭的Alert，那么必定是首次越界，则不对Obdlist做处理
			if (null != vehicleAlertModel) {
				if (vehicleAlertModel.getAlertLatitude().equals(realtimeLatestDataModel.getLatitude() + "")
						&& vehicleAlertModel.getAlertLongitude().equals(realtimeLatestDataModel.getLongitude() + "")
//						&& vehicleAlertModel.getAlertTime()
//								.equals(new Timestamp(realtimeLatestDataModel.getTraceTime().getTime()))
						) {
					// 如果是重复数据，直接判断为无用数据
					obdDataMap.remove(imei);
				} else {
					boolean isIn = this.geoFencing(imei, markers);
					if (isIn) {
						// 越界回归,越界回归更新告警
						VehicleAlertModel releaseOutbound = new VehicleAlertModel();

						String alertPosition = this.getPosition(realtimeLatestDataModel.getLatitude(),
								realtimeLatestDataModel.getLongitude());
						vehicleAlertModel.setAlertCity(this.getCityByPosition(realtimeLatestDataModel.getLatitude(),realtimeLatestDataModel.getLongitude()));
						//单位米
						Double distance = realtimeLatestDataModel.getMileage()
								- (vehicleAlertModel.getFirstOutboundKilos() != null&&vehicleAlertModel.getFirstOutboundKilos()>0 ? vehicleAlertModel.getFirstOutboundKilos()
										: 0);
						Integer minus = (int)Math.ceil((double)TimeUtils.getBetweenSeconds(TimeUtils.formatDate(vehicleAlertModel.getAlertTime()),
								TimeUtils.formatDate(realtimeLatestDataModel.getTraceTime())) / 60);
//						Integer totalMinus = Integer.parseInt(vehicleAlertModel.getOutboundMinutes() == null ? "0"
//								: vehicleAlertModel.getOutboundMinutes()) + minus;

						releaseOutbound.setId(vehicleAlertModel.getId());
						releaseOutbound.setCurrentUseOrgId(vehicleAlertModel.getCurrentUseOrgId());
						releaseOutbound.setOutboundMinutes(minus + "");
						releaseOutbound.setOutboundKilos(NumberFormatUtil.formatNumberDecimals(distance/1000, 2));
						releaseOutbound.setAlertLongitude(realtimeLatestDataModel.getLongitude() + "");
						releaseOutbound.setAlertLatitude(realtimeLatestDataModel.getLatitude() + "");
						releaseOutbound.setAlertPosition(alertPosition);
						releaseOutbound.setVehicleNumber(outboundMarkerModel.getVehicleNumber());
						releaseOutbound.setAlertTime(new Timestamp(realtimeLatestDataModel.getTraceTime().getTime()));
						releaseOutbound.setAlertType(vehicleAlertModel.getAlertType());
						releaseOutbound.setOutboundReleasetime(
								new Timestamp(realtimeLatestDataModel.getTraceTime().getTime()));

						releaseOutboundAlertMap.put(imei, releaseOutbound);
						processOutboundRealse(releaseOutboundAlertMap);
						obdDataMap.remove(imei);
					} else {
						// 越界持续数据，则更新当前的警报
						processOutBoundData(vehicleAlertModel, realtimeLatestDataModel);
						obdDataMap.remove(imei);
					}
				}
			}else{
				boolean isIn = this.geoFencing(imei, markers);
				if (isIn) {
					obdDataMap.remove(imei);
				}
			}
		} else {
			obdDataMap.remove(imei);
		}
	}

	public boolean geoFencing(String imei, List<MarkerModel> markers) {

		boolean isIn = false;

		List<Long> markerIds = new ArrayList<>();
		for (MarkerModel marker : markers) {
			markerIds.add(marker.getId());
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

	/**
	 * 更新解除报警时间
	 * 
	 * @param releaseOutboundAlertMap
	 */
	private void processOutboundRealse(Map<String, VehicleAlertModel> releaseOutboundAlertMap) {
		List<VehicleAlertModel> realseAlerts = new ArrayList<>();
		
		LOG.info("Begain Realse");
		
		for (Entry<String, VehicleAlertModel> entry : releaseOutboundAlertMap.entrySet()) {
		     vehicleAlertJobDao.relaseOutboundAlert(entry.getValue());
			 //messageService.updateOutBoundMsgStatus(entry.getValue().getId());
			 realseAlerts.add(entry.getValue());
		}
		if(realseAlerts.size()>0){
		 saveAlertToMessage(realseAlerts,true);
		 //sendAlertPush(realseAlerts,true);
		}
	}

	/**
	 * 计算越界数据,首次发生时间,越界时长,越界里程
	 */
	private void processOutBoundData(VehicleAlertModel vehicleAlertModel, RealtimeLatestDataModel realtimeLatestData) {
		String alertPosition = this.getPosition(realtimeLatestData.getLatitude(), realtimeLatestData.getLongitude());
		vehicleAlertModel.setAlertCity(this.getCityByPosition(realtimeLatestData.getLatitude(),realtimeLatestData.getLongitude()));
		Double distance = realtimeLatestData.getMileage()
				- (vehicleAlertModel.getFirstOutboundKilos() != null&&vehicleAlertModel.getFirstOutboundKilos()>0 ? vehicleAlertModel.getFirstOutboundKilos(): 0);
		Integer minus = (int)Math.ceil((double)TimeUtils.getBetweenSeconds(TimeUtils.formatDate(vehicleAlertModel.getAlertTime()),
				TimeUtils.formatDate(realtimeLatestData.getTraceTime())) / 60);
		vehicleAlertModel.setOutboundMinutes(minus + "");
		vehicleAlertModel.setOutboundKilos(NumberFormatUtil.formatNumberDecimals(distance/1000, 2));
		vehicleAlertModel.setAlertLongitude(realtimeLatestData.getLongitude() + "");
		vehicleAlertModel.setAlertLatitude(realtimeLatestData.getLatitude() + "");
		vehicleAlertModel.setAlertPosition(alertPosition);
		vehicleAlertJobDao.updateOutboundAlert(vehicleAlertModel);
	}

	private boolean needCheckBack(StationsDurationModel durationModel,Date traceTime) {
		boolean needCheck = false;
		if(traceTime !=null){
			Date startDate = DateUtils.string2Date(DateUtils.date2String(traceTime, DateUtils.FORMAT_YYYY_MM_DD) + " "
					+ durationModel.getStartTime() + ":00", DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
			Date endDate = DateUtils.string2Date(DateUtils.date2String(traceTime, DateUtils.FORMAT_YYYY_MM_DD) + " "
					+ durationModel.getEndTime() + ":00", DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
	
			if(traceTime.before(startDate)||traceTime.after(endDate)){
				needCheck = true;
			}
			
			LOG.info(durationModel.getVehicleNumber()+" Start:"+startDate+" End:"+endDate+" Current:"+traceTime+" "+needCheck);
		}
		return needCheck;
	}

	@Override
	public String getPosition(double latitude, double longitude) {
		try {
			TransGPStoAddress rResponse = shouqiService.getAddressByPoint(transGpsPoint(longitude,latitude));
			return rResponse.getResult();
		} catch (Exception e) {
			LOG.error("Vehicle alert service getPosition error, cause by\n", e);
			return null;
		}
	}

	@Override
	public VehicleModel getVehicle(String vehicleNumber) {
		return vehicleAlertJobDao.getVehicle(vehicleNumber);
	}

	@Override
	public String getCityByPosition(double latitude, double longitude) {
		try {
			TransGPStoAddress rResponse = shouqiService.getCityByPoint(transGpsPoint(longitude,latitude));
			return rResponse.getResult();
		} catch (Exception e) {
			LOG.error("Vehicle alert service getPosition error, cause by\n", e);
			return null;
		}
	}

	@Override
	public DriverModel findDriver(String vehicleNumber, Date traceTime) {
		return vehicleAlertJobDao.findDriver(vehicleNumber, traceTime);
	}

	@Override
	public List<MarkerModel> findMarker(String vehicleNumber) {
		if (StringUtils.isEmpty(vehicleNumber))
			return null;
		List<MarkerModel> markerList = vehicleAlertJobDao.findMarker(vehicleNumber);
		return markerList;
	}

	@Override
	public List<StationModel> findStation(String vehicleNumber) {
		if (StringUtils.isEmpty(vehicleNumber))
			return null;
		return vehicleAlertJobDao.findStation(vehicleNumber);
	}
	
	private void sendAlertPush(List<VehicleAlertModel> alertData,boolean isRelease){
		
		for(VehicleAlertModel alert : alertData){
			
			if(alert.getCurrentUseOrgId()!=null){
				
				List<Organization> orgList = organizationService.findUpOrganizationListByOrgId(alert.getCurrentUseOrgId());
				 List<Long> orgIdList = new ArrayList<>();
				 for(Organization org : orgList){
					orgIdList.add(org.getId());
				 }	
				
				
				List<UserModel> admins = userDao.listByOrgId(orgIdList);
				if(admins!=null&&admins.size()>0){
					
					List<String> mobiles = new ArrayList<>();
					for(UserModel admin : admins){
						mobiles.add(admin.getPhone());
					}
					
					if(mobiles.size()>0){
					  if(isRelease){
						  
						String res = commService.sendPush(mobiles,Constants.ALERT_NOTIFICATION_TITLES.get(alert.getAlertType()),MessageTemplateUtil.composeReleaseOutBoundAlertMsg(alert),Constants.CARDAY_ADMIN); 
						LOG.info("Send Realse MSG Result:"+res);
					  }else{
					    String res = commService.sendPush(mobiles,Constants.ALERT_NOTIFICATION_TITLES.get(alert.getAlertType()),MessageTemplateUtil.composeMessageForAlert(alert),Constants.CARDAY_ADMIN);  
					    LOG.info("Send Result:"+res);
					  }
					}
				}
			}
			
		}
	}
	
	private void saveAlertToMessage(List<VehicleAlertModel> alertData,boolean isRealse){
		List<Message> messages = new ArrayList<>();
	    for(VehicleAlertModel alert : alertData){
	    	Message msg = new Message();
	    	msg.setCarNo(alert.getVehicleNumber());
	    	if(alert.getAlertType().equals(AlertType.OUTBOUND.toString())){
	    	  if(isRealse)
	    		  msg.setIsEnd(1);
	    	  else
	    		  msg.setIsEnd(0);
	    	}
	    	msg.setIsNew(1);
	    	msg.setLocation(alert.getAlertPosition());
	    	msg.setOrgId(alert.getCurrentUseOrgId());
	    	msg.setTime(new java.sql.Date(alert.getAlertTime().getTime()));
	    	msg.setType(alert.getAlertType().equals(AlertType.OUTBOUND.toString())?Message.MessageType.OUTBOUND:(alert.getAlertType().equals(AlertType.OVERSPEED.toString())?Message.MessageType.OVERSPEED:Message.MessageType.VEHICLEBACK));
	    	msg.setWarningId(alert.getId());
	    	if(isRealse){
	    		 msg.setMsg(MessageTemplateUtil.composeReleaseOutBoundAlertMsg(alert));
	    	}else{
	    		 msg.setMsg(MessageTemplateUtil.composeMessageForAlert(alert));
	    	}
	    	messages.add(msg);
	    }
	    messageService.saveMessages(messages);
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
	
	private Point transBaiduToGpsPoint(double lon, double lat){
		List<Point> list = new ArrayList<Point>();
		list.add(new Point(lon,lat));
		try{
    		String resultJson = shouqiService.transBaidutoGPS(list);
    		TransGPStoBaiduModel  tResponse = JsonUtils.json2Object(resultJson, TransGPStoBaiduModel.class);
		  if(tResponse!=null&&tResponse.getResult()!=null&&tResponse.getResult().size()>0)
		     return tResponse.getResult().get(0);
		  else
			return null;
		}catch(Exception e){
			LOG.info("Trans baidu to GPS point error:"+e);
			return null;
		}
	}
	
	
	public DriverModel findDefaultDriverByVehicleNumber(String vehicleNumber){
		return vehicleAlertJobDao.findDefaultDriverByVehicleNumber(vehicleNumber);
	}
	
}
