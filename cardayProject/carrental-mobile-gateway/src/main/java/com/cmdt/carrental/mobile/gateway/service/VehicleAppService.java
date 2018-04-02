package com.cmdt.carrental.mobile.gateway.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.bean.DriveStatus;
import com.cmdt.carrental.common.cache.RedisService;
import com.cmdt.carrental.common.dao.VehicleDao;
import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.TripTraceModel;
import com.cmdt.carrental.common.model.VehicleLocationModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.service.BusiOrderService;
import com.cmdt.carrental.common.service.UserServiceImpl;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.mobile.gateway.common.Constants;
import com.cmdt.carrental.mobile.gateway.common.WsResponse;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.OrderVehicleList;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.QueryVehicleListDto;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.VehicleImeiTraceDto;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.VehicleLocationListDto;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.VehicleParamDto;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.VehicleStatusList;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.VehicleTraceDto;
import com.cmdt.carrental.mobile.gateway.model.response.vehicle.VehicleListResult;
import com.cmdt.carrental.mobile.gateway.model.response.vehicle.VehicleRealtimeResult;
import com.cmdt.carrental.mobile.gateway.model.response.vehicle.VehicleResult;
import com.cmdt.carrental.mobile.gateway.model.response.vehicle.VehicleTraceResult;
import com.cmdt.carrental.mobile.gateway.model.response.vehicle.VehicleTypeResult;
import com.cmdt.carrental.mobile.gateway.util.DateUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class VehicleAppService {
	
	private static final Logger LOG = Logger.getLogger(VehicleAppService.class);
	
	private static final String VEHICLE_PREFIX="VEHICLE_";
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
    private VehicleService vehicleService;
	
	@Autowired
    private RedisService redisService;
	
	@Autowired
    private ShouqiService shouqiService;
	
	@Autowired
	private VehicleDao vehicleDao;
	
	@Autowired
	private BusiOrderService busiOrderService;
	
	public PagModel queryVehicleList(QueryVehicleListDto dto) {
		User loginUser = userService.findById(dto.getUserId());
		PagModel pagModel= new PagModel();
    	String json = JsonUtils.object2Json(dto);
    	//租户管理员
//    	if(loginUser.isRentAdmin()){
//    		Long rentId = loginUser.getOrganizationId();
//    		pagModel= vehicleService.findPageListByRentAdmin(rentId,json);
//    	}
    	
    	//企业管理员
    	if(loginUser.isEntAdmin()){
    		Long entId = loginUser.getOrganizationId();
    		pagModel= vehicleService.findPageListByEntAdmin(entId,json);
    	}
    	
    	//部门管理员
    	if(loginUser.isDeptAdmin()){
    		Long deptId = loginUser.getOrganizationId();
    		pagModel= vehicleService.findPageListByDeptAdmin(deptId,json);
    	}
    	pagModel = vehicleTransform(pagModel);
    	return pagModel;
	}
	
	public List<VehicleTypeResult> queryVehicleType() {
		List<VehicleTypeResult> list = new ArrayList<VehicleTypeResult>();
		VehicleTypeResult vehicleType = new VehicleTypeResult();
		vehicleType.setId(0);
		vehicleType.setType("经济型");
		list.add(vehicleType);
		vehicleType = new VehicleTypeResult();
		vehicleType.setId(1);
		vehicleType.setType("舒适型");
		list.add(vehicleType);
		vehicleType = new VehicleTypeResult();
		vehicleType.setId(2);
		vehicleType.setType("商务型");
		list.add(vehicleType);
		vehicleType = new VehicleTypeResult();
		vehicleType.setId(3);
		vehicleType.setType("豪华型");
		list.add(vehicleType);
		return list;
	}
	
	public WsResponse queryObdLocationByImei(VehicleParamDto vehicleParam) {
		WsResponse wsResponse = new WsResponse();
		Long id = vehicleParam.getCarId();
		Vehicle vehicle = vehicleService.findVehicleById(id);
		VehicleRealtimeResult result = null;
		if(vehicle != null) {
			String deviceNumber = vehicle.getDeviceNumber();
			try{
				if(!StringUtils.isEmpty(deviceNumber)){
					result = getLocationByImeiWithAddress(deviceNumber);
//					//for ycl
//					result = getGPSLocationByImeiWithAddress(vehPlate);
					
					double latitude = result.getLatitude();
					double longitude = result.getLongitude();
					
					if(latitude > 0 && longitude > 0){
						result.setVehicleNumber(vehicle.getVehicleNumber());
						wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
			    		wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
						wsResponse.setResult(result);
					}else{
						wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			    		wsResponse.getMessages().add("车辆经纬度信息不存在");
					}
				}else{
					wsResponse.setStatus(Constants.API_STATUS_FAILURE);
		    		wsResponse.getMessages().add("车辆imei不存在");
				}
			}catch(Exception e){
				LOG.error("queryObdLocationByImei failed!", e);
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
	    		wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
	    		return wsResponse;
			}
		}else{
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add("该车辆不存在");
			LOG.info("该车辆不存在");
		}
    	return wsResponse;
	}
	
	public WsResponse findTripTraceDataByTimeRange(VehicleTraceDto vehicleParam) {
		WsResponse wsResponse = new WsResponse();
		List<VehicleTraceResult> traceList = new ArrayList<VehicleTraceResult>();
    	try{
    		String starttime = vehicleParam.getStartTime();
    		String endtime = vehicleParam.getEndTime();
    		if(!DateUtil.compareDate(starttime, endtime)) {
    			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
    			wsResponse.getMessages().add("startTime必须小于endTime");
    			return wsResponse;
    		}
    		Long carId = vehicleParam.getCarId();
    		Vehicle vehicle = vehicleService.findVehicleById(carId);
    		if(vehicle != null) {
	    		String imei = vehicle.getDeviceNumber();
	    		List<TripTraceModel> tripTraceModelList = new ArrayList<TripTraceModel>();
	    		//for gh
	    		JsonNode obdNode = findTripTraceDataByTimeRange(imei,starttime,endtime);
	    		if(obdNode != null){
	    			populatePoint(obdNode,tripTraceModelList);
	    			populateSpeedAndAddress(obdNode,tripTraceModelList);
	    		}
	    		//for ycl
//	    		GpsDataModel obdNode = gpsDeviceService.getGpsTrace(vehPlate,starttime,endtime);
//	    		if(obdNode != null){
//	    			populatePoint(obdNode,tripTraceModelList);
//	    		}
	    		
	    		//过滤掉经纬度为0.0的值
	    		List<TripTraceModel> resultList = new ArrayList<TripTraceModel>();
	    		if(tripTraceModelList != null && tripTraceModelList.size() > 0){
	    			for(TripTraceModel tripTraceModelVal : tripTraceModelList){
	    				if((!"0.0".equals(tripTraceModelVal.getLatitude()))&&(!"0.0".equals(tripTraceModelVal.getLongitude()))){
	    					resultList.add(tripTraceModelVal);
	    				}
	    			}
	    		}
	    		wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
	    		wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
	    		if(resultList.size() > 0) {
	    			traceList = vehicleTraceTransform(resultList);
	    		}
	    		wsResponse.setResult(traceList);
    		} else {
    			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
    			wsResponse.getMessages().add("该车辆不存在");
    		}
    	}catch(Exception e){
    		LOG.error("findTripTraceDataByTimeRange failed!", e);
    		wsResponse.setStatus(Constants.API_STATUS_FAILURE);
    		wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
    		return wsResponse;
    	}
		return wsResponse;
	}
	
	public WsResponse findVehicleHistoryGPSTrack(VehicleTraceDto vehicleParam) {
		WsResponse wsResponse = new WsResponse();
		List<VehicleTraceResult> traceList = new ArrayList<VehicleTraceResult>();
    	try{
    		String starttime = vehicleParam.getStartTime();
    		String endtime = vehicleParam.getEndTime();
    		if(!DateUtil.compareDate(starttime, endtime)) {
    			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
    			wsResponse.getMessages().add("开始时间必须小于结束时间");
    			return wsResponse;
    		}
    		
//    		Date currentDate = new Date();
//			//时间间隔验证
//			if(TimeUtils.isMoreThanCurrentTime(starttime,currentDate)){
//				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
//    			wsResponse.getMessages().add("开始时间大于当前时间");
//				return wsResponse;
//			}
//			
//			if(TimeUtils.isMoreThanCurrentTime(endtime,currentDate)){
//				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
//    			wsResponse.getMessages().add("结束时间大于当前时间");
//				return wsResponse;
//			}
    		
    		Long carId = vehicleParam.getCarId();
    		Vehicle vehicle = vehicleService.findVehicleById(carId);
    		if(vehicle != null) {
	    		String imei = vehicle.getDeviceNumber();
				
				Object[] params = new Object[] { imei, starttime, endtime };
				Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.FINDVEHICLEHISTORYGPSTRACK, params);
				if (result.get("data") != null) {
					JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
					if ("000".equals(jsonNode.get("status").asText())) {
						ArrayNode rows = (ArrayNode) jsonNode.get("result");
						if (rows != null) {
							int rowSize = rows.size();
							if (rowSize > 0) {
								for (int i = 0; i < rowSize; i++) {
									JsonNode vehicleHistoryTrackNode = rows.get(i);
									VehicleTraceResult vehicleHistoryTrack = new VehicleTraceResult();
									vehicleHistoryTrack.setTime(vehicleHistoryTrackNode.get("tracetime").asText());
									vehicleHistoryTrack.setLongitude(vehicleHistoryTrackNode.get("longitude").asText());
									vehicleHistoryTrack.setLatitude(vehicleHistoryTrackNode.get("latitude").asText());
									vehicleHistoryTrack.setSpeed(vehicleHistoryTrackNode.get("speed").asText());
									vehicleHistoryTrack.setAddress(vehicleHistoryTrackNode.get("address").asText());
									vehicleHistoryTrack.setDriveStatus(vehicleHistoryTrackNode.get("status").asText());
									traceList.add(vehicleHistoryTrack);
								}
							}
						}
					}
				}
	    		wsResponse.setResult(traceList);
	    		wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
	    		wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
    		} else {
    			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
    			wsResponse.getMessages().add("该车辆不存在");
    		}
    	}catch(Exception e){
    		LOG.error("findVehicleHistoryGPSTrack failed!", e);
    		wsResponse.setStatus(Constants.API_STATUS_FAILURE);
    		wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
    		return wsResponse;
    	}
		return wsResponse;
	}
	
	
	
	public WsResponse findVehicleHistoryGPSTrackByImei(VehicleImeiTraceDto vehicleParam) {
		WsResponse wsResponse = new WsResponse();
		List<VehicleTraceResult> traceList = new ArrayList<VehicleTraceResult>();
    	try{
    		String starttime = vehicleParam.getStartTime();
    		String endtime = vehicleParam.getEndTime();
    		String imei = vehicleParam.getImei();
    		if(!DateUtil.compareDate(starttime, endtime)) {
    			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
    			wsResponse.getMessages().add("开始时间必须小于结束时间");
    			return wsResponse;
    		}
    		
			Object[] params = new Object[] { imei, starttime, endtime };
			Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.FINDVEHICLEHISTORYGPSTRACK, params);
			if (result.get("data") != null) {
					JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
					if ("000".equals(jsonNode.get("status").asText())) {
						ArrayNode rows = (ArrayNode) jsonNode.get("result");
						if (rows != null) {
							int rowSize = rows.size();
							if (rowSize > 0) {
								for (int i = 0; i < rowSize; i++) {
									JsonNode vehicleHistoryTrackNode = rows.get(i);
									VehicleTraceResult vehicleHistoryTrack = new VehicleTraceResult();
									vehicleHistoryTrack.setTime(vehicleHistoryTrackNode.get("tracetime").asText());
									vehicleHistoryTrack.setLongitude(vehicleHistoryTrackNode.get("longitude").asText());
									vehicleHistoryTrack.setLatitude(vehicleHistoryTrackNode.get("latitude").asText());
									vehicleHistoryTrack.setSpeed(vehicleHistoryTrackNode.get("speed").asText());
									vehicleHistoryTrack.setAddress(vehicleHistoryTrackNode.get("address").asText());
									vehicleHistoryTrack.setDriveStatus(vehicleHistoryTrackNode.get("status").asText());
									traceList.add(vehicleHistoryTrack);
								}
							}
						}
				}
	    		wsResponse.setResult(traceList);
	    		wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
	    		wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
    		} 
    	}catch(Exception e){
    		LOG.error("findVehicleHistoryGPSTrack failed!", e);
    		wsResponse.setStatus(Constants.API_STATUS_FAILURE);
    		wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
    		return wsResponse;
    	}
		return wsResponse;
	}
	
	
	private PagModel vehicleTransform(PagModel p) {
		List<VehicleModel> vehicleList = p.getResultList();
		List<VehicleResult> resultList = new ArrayList<VehicleResult>();
		VehicleResult vehicle;
		for(VehicleModel vehicleModel : vehicleList){
			vehicle = new VehicleResult();
			vehicle.setCardId(vehicleModel.getId());
			vehicle.setCarNum(vehicleModel.getVehicleNumber());
			vehicle.setType(getVehicleType(vehicleModel.getVehicleType()));
			vehicle.setBrand(vehicleModel.getVehicleBrand());
			vehicle.setCarForm(vehicleModel.getVehicleFromName());
			vehicle.setDepartment(vehicleModel.getArrangedOrgName());
			vehicle.setCarType(vehicleModel.getVehicleModel());
			vehicle.setChassisNum(vehicleModel.getVehicleIdentification());
			vehicle.setFuelConsume(vehicleModel.getTheoreticalFuelCon());
			vehicle.setCarSeatNum(vehicleModel.getSeatNumber());
			vehicle.setCarColor(vehicleModel.getVehicleColor());
			vehicle.setEmissions(vehicleModel.getVehicleOutput());
			vehicle.setFuelNum(vehicleModel.getVehicleFuel());
			vehicle.setCity(vehicleModel.getCity());
			vehicle.setDriveType(vehicleModel.getLicenseType());
			String dStemp = DateUtil.dateToString(vehicleModel.getVehicleBuyTime());
			vehicle.setBuyTime(dStemp);
			dStemp = DateUtil.dateToString(vehicleModel.getInsuranceExpiredate());
			vehicle.setInsureDate(dStemp);
			vehicle.setCarport(vehicleModel.getParkingSpaceInfo());
			vehicle.setCarFor(vehicleModel.getVehiclePurpose());
			vehicle.setSpeedLimit(vehicleModel.getLimitSpeed());
			vehicle.setDeviceNum(vehicleModel.getDeviceNumber());
			vehicle.setSimcard(vehicleModel.getSimNumber());
			resultList.add(vehicle);
		}
		p.setResultList(resultList);
		return p;
	}
	
	public String getVehicleType(String typeId) {
		String vehicleType = "";
		switch (typeId) {
		case "1":
			vehicleType = "舒适型";
			break;
		case "2":
			vehicleType = "商务型";
			break;
		case "3":
			vehicleType = "豪华型";
			break;
		default:
			vehicleType = "经济型";
		}
		return vehicleType;
	}
	
	/**
     * 首汽接口findTripTraceDataByTimeRange(车辆地理数据汇总)
     * @param imei
     * @param starttime
     * @param endtime
     * @return
     * @throws IOException 
     * @throws JsonProcessingException 
     */
    public JsonNode findTripTraceDataByTimeRange(String imei,String starttime,String endtime) throws Exception{
		JsonNode obdNode = null;
		Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.FINDTRIPTRACEDATABYTIMERANGE, new Object[] { imei, starttime, endtime });
		if (result.get("data") != null) {
			JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
			if ("000".equals(jsonNode.get("status").asText())) {
				ArrayNode rows = (ArrayNode) jsonNode.get("result");
				if (rows != null && rows.size() > 0) {
					// 涉及到obd更换，只获得最新obd数据
					if (rows.size() == 1) {
						obdNode = rows.get(0);
					} else if (rows.size() == 2) {
						obdNode = rows.get(1);
					}
				}
			}
		}
		return obdNode;
    }
    
    /**
     * 填充经纬度
     * @throws IOException 
     * @throws JsonProcessingException 
     */
	public void populatePoint(JsonNode obdNode, List<TripTraceModel> list) throws Exception {
		JsonNode tracegeometryJsonNode = MAPPER.readTree(obdNode.get("tracegeometry").asText());
		JsonNode coordinatesNode = tracegeometryJsonNode.get("coordinates");
		if (coordinatesNode != null) {
			String coordinatesNodeTextVal = coordinatesNode.toString();
			if (StringUtils.isNotEmpty(coordinatesNodeTextVal)) {
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
	
	/**
     * 填充经纬度 by gps device
     * @throws IOException 
     * @throws JsonProcessingException 
     */
//	public void populatePoint(GpsDataModel obdNode, List<TripTraceModel> list) throws Exception {
//		Object obj = obdNode.getData();
//		if (obj != null) {
//			TraceModel trace = (TraceModel)obj;
//			Integer traceCount = trace.getCount();
//			if (traceCount > 0) {
//				for (int i = 0; i < traceCount; i++) {
//					TrackModel track = trace.getList().get(i);
//					TripTraceModel tripTraceModel = new TripTraceModel();
//					tripTraceModel.setLongitude(track.getLng()!=null?(String.valueOf(track.getLng())):null);
//					tripTraceModel.setLatitude(track.getLat()!=null?(String.valueOf(track.getLat())):null);
//					tripTraceModel.setTracetime(track.getgPSTime());
//					tripTraceModel.setSpeed(track.getSpeed()!=null?(String.valueOf(track.getSpeed())):null);
//					if(tripTraceModel.getSpeed() != null && Integer.valueOf(tripTraceModel.getSpeed()) > 0){
//						tripTraceModel.setStatus("运行中");
//					}else{
//						tripTraceModel.setStatus("停止");
//					}
//					list.add(tripTraceModel);
//				}
//			}
//		}
//	}
    
    /**
     * 填充tracetime,speed,以及百度address
     * @throws IOException 
     * @throws JsonProcessingException 
     */
    public void populateSpeedAndAddress(JsonNode obdNode,List<TripTraceModel> list) throws Exception{
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
				Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.FINDREALTIMEGEOLISTDETAILBYID, objArr);
				if (result.get("data") != null) {
					JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
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
									if(Integer.valueOf(tripTraceModel.getSpeed()) > 0){
										tripTraceModel.setStatus("运行中");
									}else{
										tripTraceModel.setStatus("停止");
									}
								}
							}
						}
					}
				}
			}
		}

    }
    
    public List<VehicleTraceResult> vehicleTraceTransform(List<TripTraceModel> list) {
    	List<VehicleTraceResult> resultList = new ArrayList<VehicleTraceResult>();
    	VehicleTraceResult result;
    	for(TripTraceModel model : list) {
    		result = new VehicleTraceResult();
    		result.setTime(model.getTracetime());
    		result.setLongitude(model.getLongitude());
    		result.setLatitude(model.getLatitude());
    		result.setSpeed(model.getSpeed());
    		result.setDriveStatus(model.getStatus());
    		result.setAddress(model.getAddress());
    		resultList.add(result);
    	}
    	return resultList;
    }
    
    /**
     * find vehicles with status by org id
     * @param dto
     * @return vehicle lsit
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
    public PagModel queryObdLocationList(VehicleStatusList dto) throws JsonParseException, JsonMappingException, IOException{

		PagModel pagModel = vehicleService.findOrgAndSubVehicles(dto.getOrgId(), dto.getSelfDept(), dto.getChildDept(),dto.getCurrentPage(),dto.getNumPerPage(),dto.getType(),dto.getVehicleNumber());
		List<VehicleModel> vehicleList = pagModel.getResultList();
			
    	List<VehicleListResult> vehicleResult = new ArrayList<VehicleListResult>();
		
    	Date currentDate = new Date();
		if(vehicleList != null && vehicleList.size() > 0){
			for(VehicleModel vehicleModel : vehicleList){
				//从redis中取obd数据
				VehicleListResult vehicle = new VehicleListResult();
				vehicle.setCarId(vehicleModel.getId());
				vehicle.setCarNum(vehicleModel.getVehicleNumber());
				vehicle.setType(vehicleModel.getVehicleType());
				vehicle.setBrand(vehicleModel.getVehicleBrand());
				vehicle.setCarForm(vehicleModel.getVehicleFromName());
				vehicle.setDepartment(vehicleModel.getCurrentuseOrgName()!=null&&vehicleModel.getCurrentuseOrgName().length()>0?vehicleModel.getCurrentuseOrgName():"未分配");
				
				String obdRedisData = redisService.get(VEHICLE_PREFIX+vehicleModel.getDeviceNumber());
				if(StringUtils.isNotEmpty(obdRedisData)){
					VehicleLocationModel vehicleLocationModel = MAPPER.readValue(obdRedisData, VehicleLocationModel.class);
					
					Date traceTime = DateUtils.string2Date(vehicleLocationModel.getTracetime(), "yyyy-MM-dd HH:mm:ss");
					long minutes = TimeUtils.timeBetween(traceTime,currentDate,Calendar.MINUTE);
					if(minutes<=10) {
						int speed = vehicleLocationModel.getSpeed();
    					if(speed > 0) {
    						vehicle.setDrivieStatus(DriveStatus.INTRANSIT.getIndex());
    					} else {
    						vehicle.setDrivieStatus(DriveStatus.STOP.getIndex());
    					}
					} else {
						vehicle.setDrivieStatus(DriveStatus.OFFLINE.getIndex());
					}
					
					//CR-1551过滤掉经纬度为0.0的值
					if(vehicleLocationModel.getLatitude() == 0d || vehicleLocationModel.getLongitude() == 0d){
						vehicle.setDrivieStatus(DriveStatus.OFFLINE.getIndex());
					}
				} else {
					vehicle.setDrivieStatus(DriveStatus.OFFLINE.getIndex());
				}
				vehicleResult.add(vehicle);
			}
		}
		
		pagModel.setResultList(vehicleResult);
		return pagModel;
    }
    
    public WsResponse queryObdLocationList(VehicleLocationListDto param) {
    	WsResponse wsResponse = new WsResponse();
    	Long userId = param.getUserId();
    	User loginUser = userService.findById(userId);
    	param.setFromOrgId("-1");
    	try{
    		String json = JsonUtils.object2Json(param);
    		
    		List<VehicleModel> vehicleList = null;
    		PagModel pagModel= new PagModel();
    		//租户管理员
//        	if(loginUser.isRentAdmin()){
//        		Long rentId = loginUser.getOrganizationId();
//        		pagModel= vehicleService.findPageListByRentAdmin(rentId,json);
//        	}
        	
        	//企业管理员
        	if(loginUser.isEntAdmin()){
        		Long entId = loginUser.getOrganizationId();
        		pagModel= vehicleService.findPageListByEntAdmin(entId,json);
        	}
        	
        	//部门管理员
        	if(loginUser.isDeptAdmin()){
        		Long deptId = loginUser.getOrganizationId();
        		pagModel= vehicleService.findPageListByDeptAdmin(deptId,json);
        	}
    		
        	vehicleList = pagModel.getResultList();
        	
        	VehicleListResult vehicle;
        	List<VehicleListResult> vehicleResult = new ArrayList<VehicleListResult>();
    		
        	Date currentDate = new Date();
    		if(vehicleList != null && vehicleList.size() > 0){
    			for(VehicleModel vehicleModel : vehicleList){
    				//从redis中取obd数据
    				vehicle = new VehicleListResult();
    				vehicle.setCarId(vehicleModel.getId());
    				vehicle.setCarNum(vehicleModel.getVehicleNumber());
    				vehicle.setType(vehicleModel.getVehicleType());
    				vehicle.setBrand(vehicleModel.getVehicleBrand());
    				vehicle.setCarForm(vehicleModel.getVehicleFromName());
    				vehicle.setDepartment(vehicleModel.getArrangedOrgName());
    				
    				String obdRedisData = redisService.get(VEHICLE_PREFIX+vehicleModel.getDeviceNumber());
    				if(StringUtils.isNotEmpty(obdRedisData)){
    					VehicleLocationModel vehicleLocationModel = MAPPER.readValue(obdRedisData, VehicleLocationModel.class);
    					
    					Date traceTime = DateUtils.string2Date(vehicleLocationModel.getTracetime(), "yyyy-MM-dd HH:mm:ss");
    					long minutes = TimeUtils.timeBetween(traceTime,currentDate,Calendar.MINUTE);
    					if(minutes<=10) {
    						int speed = vehicleLocationModel.getSpeed();
        					if(speed > 0) {
        						vehicle.setDrivieStatus(DriveStatus.INTRANSIT.getIndex());
        					} else {
        						vehicle.setDrivieStatus(DriveStatus.STOP.getIndex());
        					}
    					} else {
    						vehicle.setDrivieStatus(DriveStatus.OFFLINE.getIndex());
    					}
    					
//    					vehicleLocationModel.setId(vehicleModel.getId());
    					//CR-1551过滤掉经纬度为0.0的值
    					if(vehicleLocationModel.getLatitude() == 0d || vehicleLocationModel.getLongitude() == 0d){
//    						obdLocationList.add(vehicleLocationModel);
    						vehicle.setDrivieStatus(DriveStatus.OFFLINE.getIndex());
    					}
    				} else {
    					vehicle.setDrivieStatus(DriveStatus.OFFLINE.getIndex());
    				}
    				vehicleResult.add(vehicle);
    			}
    		}
    		wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
    		wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
    		pagModel.setResultList(vehicleResult);
    		
    		wsResponse.setResult(pagModel);
    	}catch(Exception e){
    		LOG.error("queryObdLocationList failed!", e);
    		wsResponse.setStatus(Constants.API_STATUS_FAILURE);
    		wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
    	}
    	return wsResponse;
    }
    
    public WsResponse queryVehicleInfoById(VehicleParamDto vehicleParam) {
    	WsResponse wsResponse = new WsResponse();
    	VehicleModel vehicleModel = vehicleService.findVehicleModelById(vehicleParam.getCarId());
    	if(vehicleModel == null) {
    		wsResponse.setStatus(Constants.API_STATUS_FAILURE);
    		wsResponse.getMessages().add("该车辆信息不存在");
    		return wsResponse;
    	}
    	wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
    	wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
    	VehicleResult vehicle = vehicleTransform(vehicleModel);
    	wsResponse.setResult(vehicle);
    	return wsResponse;
    }
    
    private VehicleResult vehicleTransform(VehicleModel vehicleModel) {
    	VehicleResult vehicle = new VehicleResult();
		vehicle.setCardId(vehicleModel.getId());
		vehicle.setCarNum(vehicleModel.getVehicleNumber());
		vehicle.setType(getVehicleType(vehicleModel.getVehicleType()));
		vehicle.setBrand(vehicleModel.getVehicleBrand());
		vehicle.setCarForm(vehicleModel.getVehicleFromName());
		vehicle.setDepartment(vehicleModel.getArrangedOrgName());
		vehicle.setCarType(vehicleModel.getVehicleModel());
		vehicle.setChassisNum(vehicleModel.getVehicleIdentification());
		vehicle.setFuelConsume(vehicleModel.getTheoreticalFuelCon());
		vehicle.setCarSeatNum(vehicleModel.getSeatNumber());
		vehicle.setCarColor(vehicleModel.getVehicleColor());
		vehicle.setEmissions(vehicleModel.getVehicleOutput());
		vehicle.setFuelNum(vehicleModel.getVehicleFuel());
		vehicle.setCity(vehicleModel.getCityName());
		vehicle.setProvince(vehicleModel.getProvinceName());
		vehicle.setDriveType(vehicleModel.getLicenseType());
		String dStemp=null;
		if (vehicleModel.getVehicleBuyTime()!=null) {
			dStemp = DateUtil.dateToString(vehicleModel.getVehicleBuyTime());
		}
		vehicle.setBuyTime(dStemp);
		String iStemp = DateUtil.dateToString(vehicleModel.getInsuranceExpiredate());
		vehicle.setInsureDate(iStemp);
		vehicle.setCarport(vehicleModel.getParkingSpaceInfo());
		vehicle.setCarFor(vehicleModel.getVehiclePurpose());
		vehicle.setSpeedLimit(vehicleModel.getLimitSpeed());
		vehicle.setDeviceNum(vehicleModel.getDeviceNumber());
		vehicle.setDeviceVendorNumber(vehicleModel.getDeviceVendorNumber());
		vehicle.setSimcard(vehicleModel.getSimNumber());
		vehicle.setSnNumber(vehicleModel.getSnNumber());
		vehicle.setIccidNumber(vehicleModel.getIccidNumber());
		//set driver info
		vehicle.setRealname(vehicleModel.getRealname());
		vehicle.setPhone(vehicleModel.getPhone());
		
    	return vehicle;
    }
    
    public VehicleRealtimeResult getLocationByImeiWithAddress(String deviceNumber) throws Exception {
    	VehicleRealtimeResult vehicleRealtimeResult = new VehicleRealtimeResult();
    	Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.GETLOCATIONBYIMEIWITHADDRESS, new Object[]{deviceNumber});
    	Date currentDate = new Date();
    	if (result.get("data") != null) {
    		JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
    		if ("000".equals(jsonNode.get("status").asText())) {
    			JsonNode resultNode = jsonNode.get("result");
    			LOG.info("imei:" + resultNode.get("imei"));
    			if(resultNode != null){
    				if(resultNode.get("traceTime") != null) {
    					vehicleRealtimeResult.setTime(resultNode.get("traceTime").asText());
    					Date traceTime = DateUtils.string2Date(vehicleRealtimeResult.getTime(), "yyyy-MM-dd HH:mm:ss");
    					long minutes = TimeUtils.timeBetween(traceTime,currentDate,Calendar.MINUTE);
    					if(resultNode.get("speed") != null && minutes <= 10) {
        					double speed = resultNode.get("speed").asDouble();
        					vehicleRealtimeResult.setSpeed(speed);
        					if(speed > 0) {
        						vehicleRealtimeResult.setDriveStatus(DriveStatus.INTRANSIT.getIndex());
        					} else {
        						vehicleRealtimeResult.setDriveStatus(DriveStatus.STOP.getIndex());
        					}
        				} else {
        					vehicleRealtimeResult.setDriveStatus(DriveStatus.OFFLINE.getIndex());
        				}
    				}else {
    					vehicleRealtimeResult.setDriveStatus(DriveStatus.OFFLINE.getIndex());
    				}
    				
    				
    				if(resultNode.get("longitude") != null) {
    					vehicleRealtimeResult.setLongitude(resultNode.get("longitude").asDouble());
    				}
    				if(resultNode.get("latitude") != null) {
    					vehicleRealtimeResult.setLatitude(resultNode.get("latitude").asDouble());
    				}
    				if(resultNode.get("address") != null) {
    					vehicleRealtimeResult.setAddress(resultNode.get("address").asText());
    				}
    			}
    		}
    	}
    	return vehicleRealtimeResult;
    }
    
//    public VehicleRealtimeResult getGPSLocationByImeiWithAddress(String vehPlate) throws Exception {
//    	VehicleRealtimeResult vehicleRealtimeResult = new VehicleRealtimeResult();
//    	GpsDataModel gpsModel = gpsDeviceService.getGpsTrack(vehPlate);
//    	if (gpsModel != null && gpsModel.getData() != null) {
//    		TrackModel track = (TrackModel)gpsModel.getData();
//    		vehicleRealtimeResult.setTime(track.getgPSTime());
//    		vehicleRealtimeResult.setLongitude(track.getLng());
//			vehicleRealtimeResult.setLatitude(track.getLat());
//			if(track.getSpeed() != null) {
//				vehicleRealtimeResult.setSpeed(track.getSpeed());
//				if(track.getSpeed() > 0) {
//					vehicleRealtimeResult.setDriveStatus(DriveStatus.INTRANSIT.getIndex());
//				} else {
//					vehicleRealtimeResult.setDriveStatus(DriveStatus.STOP.getIndex());
//				}
//			} else {
//				vehicleRealtimeResult.setDriveStatus(DriveStatus.OFFLINE.getIndex());
//			}
//    	}
//    	return vehicleRealtimeResult;
//    }
    
    
    public PagModel listAvailableVehicle(OrderVehicleList dto) throws Exception {

    	BusiOrder busiOrder = busiOrderService.findOne(dto.getOrderId());
    	if(busiOrder == null) {
    			throw new Exception("订单不存在！");
    	}else if(busiOrder.getStatus()!=1){
    		    throw new Exception("订单不可用！");
    	}
    	return vehicleService.listAvailableVehicleByOrder(dto.getOrgId(),busiOrder,dto.isSelfDept(),dto.isChildDept(),dto.getCurrentPage(),dto.getNumPerPage());
    }
    
    public WsResponse queryAvailableVehicleById(VehicleParamDto vehicleParam) {
    	WsResponse wsResponse = new WsResponse();
    	VehicleModel vehicleModel = null;
		vehicleModel = vehicleService.queryAvailableVehicleById(vehicleParam.getCarId());
		wsResponse.setStatus(Constants.STATUS_SUCCESS);
		wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
		wsResponse.setResult(vehicleModel);
    	return wsResponse;
    }
    
    private String cityIdTransform(String cityId) {
    	String cityName = "";
    	switch (cityId) {
		case "0":
			cityName = "北京";
			break;
		case "1":
			cityName = "上海";
			break;
		case "2":
			cityName = "武汉";
			break;
		case "3":
			cityName = "重庆";
			break;
		}
		return cityName;
    }
    
}
