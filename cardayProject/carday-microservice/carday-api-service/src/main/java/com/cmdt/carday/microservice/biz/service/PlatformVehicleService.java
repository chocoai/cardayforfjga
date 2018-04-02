package com.cmdt.carday.microservice.biz.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cmdt.carday.microservice.common.Constants;
import com.cmdt.carday.microservice.common.model.response.exception.ServiceException;
import com.cmdt.carday.microservice.model.request.vehicle.LocationDto;
import com.cmdt.carday.microservice.model.request.vehicle.UnAssignedVehicleDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicelToCurrOrgDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleAllocationDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleAssignDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleCreateDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleHistoryTrackeDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleListDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleListMantainanceDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleMarkerPageDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleModelDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleQueryObdDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleSpeedLimitDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleTripTraceDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleUpdateDto;
import com.cmdt.carrental.common.cache.RedisService;
import com.cmdt.carrental.common.entity.DevcieCommandConfigRecord;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carday.microservice.common.model.response.MessageCode;
import com.cmdt.carrental.common.integration.DeviceCommandService;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.AllocateDepModel;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.HomePageMapModel;
import com.cmdt.carrental.common.model.ObdStatusModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.Point;
import com.cmdt.carrental.common.model.TimeRangeModel;
import com.cmdt.carrental.common.model.TransGPStoBaiduModel;
import com.cmdt.carrental.common.model.TripPropertyModel;
import com.cmdt.carrental.common.model.TripTraceModel;
import com.cmdt.carrental.common.model.VehMoniTreeStatusNode;
import com.cmdt.carrental.common.model.VehicelAssignModel;
import com.cmdt.carrental.common.model.VehicleAllocationModel;
import com.cmdt.carrental.common.model.VehicleAndOrderModel;
import com.cmdt.carrental.common.model.VehicleBrandModel;
import com.cmdt.carrental.common.model.VehicleEnterpriseModel;
import com.cmdt.carrental.common.model.VehicleHistoryTrack;
import com.cmdt.carrental.common.model.VehicleListForOrgDto;
import com.cmdt.carrental.common.model.VehicleListModel;
import com.cmdt.carrental.common.model.VehicleLocationModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleQueryDTO;
import com.cmdt.carrental.common.model.VehicleRoot;
import com.cmdt.carrental.common.model.VehicleStatusRoot;
import com.cmdt.carrental.common.model.VehicleTreeStatusModel;
import com.cmdt.carrental.common.service.BusiOrderService;
import com.cmdt.carrental.common.service.DeviceService;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.VehicleAnnualInspectionService;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.common.util.CsvUtil;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.common.util.TypeUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class PlatformVehicleService {
	private static final Logger LOG = LoggerFactory.getLogger(PlatformVehicleService.class);

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private ShouqiService shouqiService;

	@Autowired
	private RedisService redisService;

	@Autowired
	private OrganizationService orgService;

	@Autowired
	private BusiOrderService orderService;

	@Autowired
	private DeviceCommandService deviceCommandService;

	@Autowired
	private VehicleAnnualInspectionService vehicleAnnualInspectionService;

	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private OrganizationService organizationService;

	private static final String VEHICLE_PREFIX = "VEHICLE_";

	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	Map<String,Integer> provinceMap =new HashMap<String,Integer>();
	
	Map<Integer,List<Map<String, Integer>>> cityList =new HashMap<Integer,List<Map<String, Integer>>>();
	

	public PagModel listVehicle(User user, VehicleListDto dto)  {
		PagModel pageModel = null;
		try {
			VehicleListModel vModel = new VehicleListModel();
			BeanUtils.copyProperties(vModel, dto);
			vModel.setOrganizationId(user.getOrganizationId());
			// 企业管理员
			if (user.isEntAdmin()) {
				pageModel = vehicleService.getVehicleListByEnterAdmin(vModel);
			}
			if (user.isDeptAdmin()) {
				pageModel = vehicleService.getVehicleListByDeptAdmin(vModel);
			}
		} catch (Exception e) {
			LOG.error("PlatformUserService listVehicle error, cause by:", e);
		}
		return pageModel;
	}

	// 验证车牌号，车架号的唯一性
	private void isValid(String vehicleNumber, String vehicleIdentification, Long id) {
		if (!StringUtils.isBlank(vehicleNumber) && null != vehicleService.countByVehicleNumber(vehicleNumber, id)) {
			throw  new ServiceException(MessageCode.VEHICLE_NUMBER_EXIST);
		}
		if (!StringUtils.isBlank(vehicleIdentification)
				&& null != vehicleService.countByVehicleIdentification(vehicleIdentification, id)) {
			throw  new ServiceException(MessageCode.VEHICLE_IDENTIFICATION_EXIST);
		}
		
	}

	public Vehicle createVehicle(User user, VehicleCreateDto dto) throws Exception {
		
		 this.isValid(dto.getVehicleNumber(), dto.getVehicleIdentification(), null);
		
		if (user.isEntAdmin()) {
			// 查出企业下所有的组织机构
			Organization org = orgService.findById(dto.getCurrentuseOrgId());
			if (org.getParentIds().indexOf(user.getOrganizationId() + "") > -1) {
				dto.setCurrentuseOrgName(org.getName());
				Vehicle vehicle = new Vehicle();
				BeanUtils.copyProperties(vehicle, dto);
				vehicle.setEntId(user.getOrganizationId());
				vehicle.setEntName(user.getOrganizationName());
				return vehicleService.createVehicleByEntAdmin(vehicle);
			}
		}
		return null;
	}

	public List<VehicleEnterpriseModel> getVehicleOrigin(User user) {
		LOG.debug("Inside PlatformVehicleService.getVehicleOrigin.");
		List<VehicleEnterpriseModel> list = null;
		if (user.isEntAdmin()) {
			list = vehicleService.findAllVehiclefromByEnterId(user.getOrganizationId(), user.getOrganizationName());
		}
		if (user.isDeptAdmin()) {
			list = vehicleService.findListVehicleFromByDeptAdmin(user.getOrganizationId());
		}
		return list;
	}

	public Vehicle getVehicleById(Long id) {
		LOG.debug("Inside PlatformVehicleService.getVehicleById.");
		return vehicleService.findVehicleById(id);
	}

	public VehicleModel findVehicleModelById(Long id) {
		LOG.debug("Inside PlatformVehicleService.findVehicleModelById.");
		return vehicleService.findVehicleModelById(id);
	}

	public void vehicleAllocation(VehicleAllocationDto dto) throws Exception {
		LOG.debug("PlatformVehicleService.vehicleAllocation");
		VehicleAllocationModel vModel = new VehicleAllocationModel();
		BeanUtils.copyProperties(vModel, dto);
		vehicleService.vehicleAllocationByEntAdmin(vModel);
	}

	public void updateVehicle(VehicleUpdateDto dto) throws Exception {
		LOG.debug("PlatformVehicleService.updateVehicle");
		this.isValid(dto.getVehicleNumber(), dto.getVehicleIdentification(),
				dto.getId());
		Vehicle vehicle = new Vehicle();
		BeanUtils.copyProperties(vehicle, dto);
		vehicleService.updateVehicleByEntAdmin(vehicle);
		// 如果修改车辆的年检时间时，需要同步更新年检表
		if (null != vehicle.getInspectionExpiredate()) {
			vehicleAnnualInspectionService.updateInspection(vehicle.getInspectionExpiredate(), vehicle.getId());
		}
	}

	public ObdStatusModel queryObdByImei(String deviceNumber) throws Exception {
		LOG.debug("PlatformVehicleService.queryObdByImei");
		ObdStatusModel obdStatusModel = new ObdStatusModel();
		obdStatusModel.setDeviceNumber(deviceNumber);
		Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.QUERYOBDBYIMEI,
				new Object[] { deviceNumber });
		LOG.info("=============call service QueryObdByImei result:" + result);
		if (result.get("data") != null) {
			JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
			if ("000".equals(jsonNode.get("status").asText())) {
				ArrayNode rows = (ArrayNode) jsonNode.get("result");
				if (rows != null && rows.size() > 0) {
					JsonNode obdNode = rows.get(0);
					obdStatusModel.setStatus(obdNode.get("status").asText());
				}
			} else {
				obdStatusModel.setStatus("NOTFOUND");
			}
		} else {
			obdStatusModel.setStatus("NOTFOUND");
		}
		return obdStatusModel;
	}

	public List<VehicleLocationModel> queryObdLocationList(User user, VehicleQueryObdDto dto) throws Exception {
		LOG.debug("PlatformVehicleService.queryObdLocationList");
		VehicleListModel vModel = new VehicleListModel();
		BeanUtils.copyProperties(vModel, dto);
		vModel.setOrganizationId(user.getOrganizationId());
		List<VehicleLocationModel> obdLocationList = new ArrayList<VehicleLocationModel>();
		List<VehicleModel> vehicleList = new ArrayList<VehicleModel>();
		if (user.isEntAdmin()) {
			vehicleList = vehicleService.findAllVehicleListByEntAdmin(vModel);
		}
		if (user.isDeptAdmin()) {
			vehicleList = vehicleService.findAllVehicleListByDeptAdmin(vModel);
		}
		if (vehicleList != null && vehicleList.size() > 0) {
			for (VehicleModel vehicleModel : vehicleList) {
				// 从redis中取obd数据
				String obdRedisData = redisService.get(VEHICLE_PREFIX + vehicleModel.getDeviceNumber());
				if (StringUtils.isNotEmpty(obdRedisData)) {
					VehicleLocationModel vehicleLocationModel = MAPPER.readValue(obdRedisData,
							VehicleLocationModel.class);
					// GPS转百度坐标
					vehicleLocationModel = transGPStoBaidu(vehicleLocationModel);
					vehicleLocationModel.setId(vehicleModel.getId());
					vehicleLocationModel.setRealname(vehicleModel.getRealname());
					vehicleLocationModel.setPhone(vehicleModel.getPhone());
					// 过滤掉经纬度为0.0的值
					if (vehicleLocationModel.getLatitude() != 0.0 && vehicleLocationModel.getLongitude() != 0.0) {
						obdLocationList.add(vehicleLocationModel);
					}
				}
			}
		}
		return obdLocationList;
	}

	private VehicleLocationModel transGPStoBaidu(VehicleLocationModel vModel) {
		Point point = new Point();
		point.setLat(vModel.getLatitude());
		point.setLon(vModel.getLongitude());
		List<Point> points = new ArrayList<Point>();
		points.add(point);
		String result = shouqiService.transGPStoBaidu(points);
		TransGPStoBaiduModel transGPStoBaiduModel = JsonUtils.json2Object(result, TransGPStoBaiduModel.class);
		if (transGPStoBaiduModel.getResult().size() > 0) {
			vModel.setLatitude(transGPStoBaiduModel.getResult().get(0).getLat());
			vModel.setLongitude(transGPStoBaiduModel.getResult().get(0).getLon());
		}

		return vModel;
	}

	public HomePageMapModel queryObdLocationByName(User user, VehicleQueryObdDto dto) throws Exception {
		LOG.debug("PlatformVehicleService.queryObdLocationByName");
		int totalVehs = 0;
		int onlineVehs = 0;
		int onTaskVehs = 0;
		int idleVehs = 0;
		Long orgId = user.getOrganizationId();
		HomePageMapModel model = new HomePageMapModel();
		List<VehicleLocationModel> obdLocationList = new ArrayList<VehicleLocationModel>();
		model.setObdLocationList(obdLocationList);
		String name = dto.getName();
		List<VehicleModel> vehicleList = null;
		if (name != null && !"".equals(name)) {
			// for home page
			if ("AllVehs".equals(name)) {
				if (user.isEntAdmin()) {
					vehicleList = vehicleService.getVehicleModelByOrganization(orgId);
				} else if (user.isDeptAdmin()) {
					vehicleList = vehicleService.getVehicleModelByDept(orgId);
				}
			} else {
				// check whether name equals vehicle plate
				VehicleModel vehicleModel = vehicleService.findVehicleByPlate(name);
				if (vehicleModel != null) {
					vehicleList = new ArrayList<VehicleModel>();
					vehicleList.add(vehicleModel);
				} else {
					// name should be dept name
					Organization org = null;
					if (user.isEntAdmin()) {
						org = orgService.findByName(name, orgId);
					} else if (user.isDeptAdmin()) {
						org = orgService.findByDeptName(name, orgId);

					}
					if (org != null) {
						vehicleList = vehicleService.getVehicleModelByDept(org.getId());
					}
				}
			}
			// 过滤车辆绑定的license状态不正常的车辆
			if (vehicleList != null && !vehicleList.isEmpty()) {
				vehicleList = vehicleService.getInUsedVehicleListByDeviceNumber(vehicleList);
				LOG.info("vehicleList.size:" + vehicleList.size());
			}
			if (vehicleList != null && !vehicleList.isEmpty()) {
				// set total quantity
				totalVehs = vehicleList.size();
				for (VehicleModel vehicleModel : vehicleList) {
					// 从redis中取obd数据
					String obdRedisData = redisService.get(VEHICLE_PREFIX + vehicleModel.getDeviceNumber());
					if (StringUtils.isNotEmpty(obdRedisData)) {
						VehicleLocationModel vehicleLocationModel = MAPPER.readValue(obdRedisData,
								VehicleLocationModel.class);
						// GPS转百度坐标
						vehicleLocationModel = transGPStoBaidu(vehicleLocationModel);
						vehicleLocationModel.setId(vehicleModel.getId());
						// 过滤掉经纬度为0.0的值
						if (vehicleLocationModel.getLatitude() != 0.0 && vehicleLocationModel.getLongitude() != 0.0) {
							obdLocationList.add(vehicleLocationModel);
							// CR2399是否离线
							String tracetime = vehicleLocationModel.getTracetime();
							if (StringUtils.isNotEmpty(tracetime)) {
								Date currentDate = new Date();
								long minutes = TimeUtils.timeBetween(TimeUtils.formatDate(tracetime), currentDate,
										Calendar.MINUTE);
								if (minutes > 10) {// 车辆最近一条数据与当前时间超过10分钟的，状态为离线
									vehicleLocationModel.setStatus(
											com.cmdt.carrental.common.constants.Constants.VEHICLE_RUNTIME_STATUS_OFFLINE);
								}
							} else {// 无tracktime,无法判断上传时间，设置为离线
								vehicleLocationModel.setStatus(
										com.cmdt.carrental.common.constants.Constants.VEHICLE_RUNTIME_STATUS_OFFLINE);
							}
						}
					}
					// 确认车辆订单信息
					if (orderService.checkOnTask(vehicleModel.getId())) {
						onTaskVehs++;
					}
				}
				// set online quantity
				onlineVehs = obdLocationList.size();
				// set idle quantity
				idleVehs = totalVehs - onTaskVehs;
			}
		}
		// set vehs quantity
		model.setTotalVehs(totalVehs);
		model.setOnlineVehs(onlineVehs);
		model.setOnTaskVehs(onTaskVehs);
		model.setIdleVehs(idleVehs);
		return model;
	}

	public List<VehicleModel> listDeptVehicles(Long orgId) {
		List<Organization> orgList =organizationService.findDownOrganizationListByOrgId(orgId);
		if (!orgList.isEmpty()) {
			List<Long> orgIds = new ArrayList<>();
			for (Organization org : orgList) {
				orgIds.add(org.getId());
			}
			
			Organization organization =organizationService.findOne(orgId);
			Boolean isEnt = false;
			if(organization.getParentId().equals(0L)){
				isEnt=true;
			}
			
			return vehicleService.listDeptVehicles(orgId, orgIds,isEnt);
		}else{
			return null;
		}
		
	}

	public VehicleLocationModel queryObdLocationByIme(String deviceNumber) throws Exception {
		String obdRedisData = redisService.get(VEHICLE_PREFIX + deviceNumber);
		VehicleLocationModel vehicleLocationModel = null;
		if (StringUtils.isNotEmpty(obdRedisData)) {
			vehicleLocationModel = MAPPER.readValue(obdRedisData, VehicleLocationModel.class);
			// GPS转百度坐标
			vehicleLocationModel = transGPStoBaidu(vehicleLocationModel);
		}
		return vehicleLocationModel;
	}

	public VehicleLocationModel queryObdLocationByVehicleId(Long vehicleId) throws Exception {
		Vehicle vehicle = vehicleService.findVehicleById(vehicleId);
		String deviceNumber = vehicle.getDeviceNumber();
		String obdRedisData = redisService.get(VEHICLE_PREFIX + deviceNumber);
		VehicleLocationModel vehicleLocationModel = null;
		if (StringUtils.isNotEmpty(obdRedisData)) {
			vehicleLocationModel = MAPPER.readValue(obdRedisData, VehicleLocationModel.class);
		}
		return vehicleLocationModel;
	}

	public TripPropertyModel findTripPropertyDataByTimeRange(VehicleTripTraceDto dto) throws Exception {
		TripPropertyModel tripPropertyModel = null;
		Map<String, Object> result = new ServiceAdapter(shouqiService).doService(
				ActionName.FINDTRIPPROPERTYDATABYTIMERANGE,
				new Object[] { dto.getImei(), dto.getStarttime(), dto.getEndtime() });
		if (result.get("data") != null) {
			JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
			if ("000".equals(jsonNode.get("status").asText())) {
				ArrayNode rows = (ArrayNode) jsonNode.get("result");
				if (rows != null && rows.size() > 0) {
					int mileage = 0;
					double avgFule = 0.0d;
					JsonNode dataNode = null;
					// 涉及到obd更换，只获得最新obd数据
					if (rows.size() == 1) {
						dataNode = rows.get(0);
					} else if (rows.size() == 2) {
						dataNode = rows.get(1);
					}
					tripPropertyModel = new TripPropertyModel();
					if (null != dataNode) {
						mileage = dataNode.get("mileage").asInt();
						avgFule = dataNode.get("avgfuel").asDouble();
						tripPropertyModel.setDrivetime(dataNode.get("drivetime").asInt());
						tripPropertyModel.setStoptime(dataNode.get("stoptime").asInt());
					}
					// 米转换为公里
					DecimalFormat df = new DecimalFormat("0.00");
					tripPropertyModel.setMileage(Double.valueOf(df.format((double) mileage / 1000)));
					Double data1 = Double.valueOf(avgFule);
					Double data2 = Double.valueOf(0.0d);
					if (data1.equals(data2)) {
						// 理论油耗
						VehicleModel vehicleModel = vehicleService.findVehicleByImei(dto.getImei());
						if (vehicleModel != null) {
							tripPropertyModel.setFuel(Double.valueOf(
									df.format((double) mileage * vehicleModel.getTheoreticalFuelCon() / 100000)));
						} else {
							tripPropertyModel.setFuel(0.0);
						}
					} else {
						tripPropertyModel
								.setFuel(Double.valueOf(df.format(tripPropertyModel.getMileage() * avgFule / 100)));
					}
				}
			}
		}
		return tripPropertyModel;
	}

	public List<TripTraceModel> findTripTraceDataByTimeRange(VehicleTripTraceDto dto) throws Exception {
		List<TripTraceModel> tripTraceModelList = new ArrayList<TripTraceModel>();
		JsonNode obdNode = findTripTraceDataByTimeRange(dto.getImei(), dto.getStarttime(), dto.getEndtime());
		if (obdNode != null) {
			populatePoint(obdNode, tripTraceModelList);
			tripTraceModelList = populateSpeedAndAddressByGroup(obdNode, tripTraceModelList);
		}

		// 过滤掉经纬度为0.0的值
		List<TripTraceModel> resultList = new ArrayList<TripTraceModel>();
		if (tripTraceModelList != null && tripTraceModelList.size() > 0) {
			for (TripTraceModel tripTraceModelVal : tripTraceModelList) {
				if ((!"0.0".equals(tripTraceModelVal.getLatitude()))
						&& (!"0.0".equals(tripTraceModelVal.getLongitude()))) {
					resultList.add(tripTraceModelVal);
				}
			}
		}

		// 转换baidu坐标
		return vehicleService.transformBaiduPoint(resultList);
	}

	private List<TripTraceModel> findrealTimegeoListDetailById(Object[] objArr, List<TripTraceModel> list)
			throws Exception {
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

	private List<TripTraceModel> populateSpeedAndAddressByGroup(JsonNode obdNode, List<TripTraceModel> list)
			throws Exception {
		List<TripTraceModel> resultList = new ArrayList<TripTraceModel>();
		String idlistTextVal = obdNode.get("idlist").asText();
		String[] idlistArr = null;
		if (StringUtils.isNotEmpty(idlistTextVal)) {
			idlistArr = idlistTextVal.replace("},{", "#").replace("{", "").replace("[", "").replace("]", "")
					.replace("}", "").split("#");
			int idListArrLength = idlistArr.length;
			if (idListArrLength > 0) {
				Object[] objArr = new Object[idListArrLength];
				for (int i = 0; i < idListArrLength; i++) {
					objArr[i] = idlistArr[i];
				}
				int length = list.size();
				for (int x = 0; x < (length / 300 + 1); x++) {
					int num = 300 * (x + 1);
					int objectLenth = 300;
					if (num > length) {
						num = length;
						objectLenth = num % 300;
					}
					Object[] objArrTemp = new Object[objectLenth];
					List<TripTraceModel> tempList = new ArrayList<TripTraceModel>();
					int z = 0;
					for (int y = 300 * x; y < num; y++) {
						objArrTemp[z] = objArr[y];
						tempList.add(list.get(y));
						z++;
					}
					resultList.addAll(findrealTimegeoListDetailById(objArrTemp, tempList));
				}
			}
		}
		return resultList;
	}

	/**
	 * 首汽接口findTripTraceDataByTimeRange(车辆地理数据汇总)
	 * 
	 * @param imei
	 * @param starttime
	 * @param endtime
	 * @return
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public JsonNode findTripTraceDataByTimeRange(String imei, String starttime, String endtime) throws Exception {
		JsonNode obdNode = null;
		Map<String, Object> result = new ServiceAdapter(shouqiService)
				.doService(ActionName.FINDTRIPTRACEDATABYTIMERANGE, new Object[] { imei, starttime, endtime });
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
	 * 
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public void populatePoint(JsonNode obdNode, List<TripTraceModel> list) throws Exception {
		JsonNode tracegeometryJsonNode = MAPPER.readTree(obdNode.get("tracegeometry").asText());
		JsonNode coordinatesNode = tracegeometryJsonNode.get("coordinates");
		if (coordinatesNode != null) {
			String coordinatesNodeTextVal = coordinatesNode.toString();
			if (StringUtils.isNotEmpty(coordinatesNodeTextVal)) {
				String[] coordinatesArr = coordinatesNodeTextVal.replace("],[", "#").replace("[", "").replace("]", "")
						.split("#");
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
	 * 填充tracetime,speed,以及百度address
	 * 
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public void populateSpeedAndAddress(JsonNode obdNode, List<TripTraceModel> list) throws Exception {
		String idlistTextVal = obdNode.get("idlist").asText();
		String[] idlistArr = null;
		if (StringUtils.isNotEmpty(idlistTextVal)) {
			idlistArr = idlistTextVal.replace("},{", "#").replace("{", "").replace("[", "").replace("]", "")
					.replace("}", "").split("#");
			int idListArrLength = idlistArr.length;
			if (idListArrLength > 0) {
				Object[] objArr = new Object[idListArrLength];
				for (int i = 0; i < idListArrLength; i++) {
					objArr[i] = idlistArr[i];
				}
				Map<String, Object> result = new ServiceAdapter(shouqiService)
						.doService(ActionName.FINDREALTIMEGEOLISTDETAILBYID, objArr);
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
			}
		}

	}

	public PagModel listAvailableVehicle(User user, BusiOrderQueryDto dto) throws Exception {
		return vehicleService.listAvailableVehicleByAdmin(user, dto);
	}

	public List<VehicleRoot> listVehicleTree(User user) {
		boolean isEnt = false;
		// 企业管理员
		if (user.isEntAdmin()) {
			isEnt = true;
		}

		// 部门管理员
		if (user.isDeptAdmin()) {
			isEnt = false;
		}
		return vehicleService.queryVehicleTree(user.getOrganizationId(), isEnt);
	}

	public List<VehicleEnterpriseModel> listAvailableAssignedEnterprise(User user) {
		return vehicleService.listAvailableAssignedEnterprise(user.getOrganizationId());
	}

	public int vehicleAssigne(VehicleAssignDto dto) throws Exception {
		VehicelAssignModel model = new VehicelAssignModel();
		BeanUtils.copyProperties(model, dto);
		return vehicleService.vehicleAssigne(model);
	}

	public void vehicleRecover(VehicleAssignDto dto) throws Exception {
		VehicleAllocationModel model = new VehicleAllocationModel();
		BeanUtils.copyProperties(model, dto);
		vehicleService.vehicleRecover(model);
	}

	public List<VehicleHistoryTrack> findVehicleHistoryTrack(VehicleHistoryTrackeDto dto) throws Exception {
		List<VehicleHistoryTrack> retList = new ArrayList<>();
		VehicleModel vehicleModel = vehicleService.findVehicleByPlate(dto.getVehicleNumber());
		if (vehicleModel == null) {
			return retList;
		}
		String imei = vehicleModel.getDeviceNumber();
		List<TimeRangeModel> timeRangeModelList = TimeUtils.getSplitTimeList(dto.getStarttime(), dto.getEndtime());// 时间分割
		if (!timeRangeModelList.isEmpty()) {
			for (TimeRangeModel timeRangeModel : timeRangeModelList) {
				String starttimeVal = timeRangeModel.getStarttime();
				String endtimeVal = timeRangeModel.getEndtime();
				if (StringUtils.isNotEmpty(starttimeVal) && StringUtils.isNotEmpty(endtimeVal)) {
					Object[] params = new Object[] { imei, starttimeVal, endtimeVal };
					Map<String, Object> result = new ServiceAdapter(shouqiService)
							.doService(ActionName.FINDVEHICLEHISTORYTRACKWITHOUTADDRESS, params);
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
										vehicleHistoryTrack
												.setTracetime(vehicleHistoryTrackNode.get("tracetime").asText());
										vehicleHistoryTrack
												.setLongitude(vehicleHistoryTrackNode.get("longitude").asText());
										vehicleHistoryTrack
												.setLatitude(vehicleHistoryTrackNode.get("latitude").asText());
										vehicleHistoryTrack.setSpeed(vehicleHistoryTrackNode.get("speed").asText());
										vehicleHistoryTrack.setStatus(vehicleHistoryTrackNode.get("status").asText());
										retList.add(vehicleHistoryTrack);
									}
								}
							}
						}
					}
				}
			}
		}
		return retList;
	}

	public List<DriverModel> findAvailableDriversByVehicleId(Long vehicleId) {
		List<DriverModel> driverList = vehicleService.findAvailableDriversByVehicleId(vehicleId);
		if (!driverList.isEmpty()) {
			for (DriverModel driverModel : driverList) {
				String realnameAndPhone = driverModel.getRealname() + "  " + driverModel.getPhone();
				driverModel.setRealnameAndPhone(realnameAndPhone);
			}
		}
		return driverList;
	}

	public int driverAllocate(Long vehicleId,Long driverId) {
		return vehicleService.driverAllocate(vehicleId, driverId);
	}

	public List<VehicleStatusRoot> listVehicleStatusTree(User user) {
		long orgId = user.getOrganizationId();
		String entName = "";
		String deptName = "";
		boolean isEnt = false;
		// 企业管理员
		if (user.isEntAdmin()) {
			isEnt = true;
			entName = user.getOrganizationName();
		}
		// 部门管理员
		if (user.isDeptAdmin()) {
			isEnt = false;
			deptName = user.getOrganizationName();
		}
		return vehicleService.listVehicleStatusTree(orgId, isEnt, entName, deptName);
	}

	public VehicleModel findVehicleInfoByVehicleNumber(User user, String vehicleNumber) {
		return vehicleService.getVehicleInfoByVehicleNumber(vehicleNumber, user);
	}

	@SuppressWarnings("unchecked")
	public String updateSpeedLimitgetUserById(User user, VehicleSpeedLimitDto dto) {
		String msg = "";
		Vehicle vehModel = new Vehicle();
		vehModel.setLimitSpeed(dto.getLimitSpeed());
		vehModel.setVehicleNumber(dto.getVehicleNumber());
		// 车辆信息
		VehicleModel vehicle = vehicleService.findByVehicleNumber(vehModel.getVehicleNumber());

		// 车辆绑定设备存在时,限制速度下发
		if (StringUtils.isNotBlank(vehicle.getDeviceNumber())) {
			// imei
			String imei = vehicle.getDeviceNumber();
			String result = deviceCommandService.setSpeedLimitCommand(imei, vehModel.getLimitSpeed(), 0);
			Map<String, Object> jsonMap = JsonUtils.json2Object(result, Map.class);
			String statusCode = "";
			if (jsonMap.get("status") != null) {
				statusCode = (String) jsonMap.get("status");
			}

			// 设备命令记录
			DevcieCommandConfigRecord devcieCommandConfigRecord = new DevcieCommandConfigRecord();
			devcieCommandConfigRecord.setDeviceNumber(imei);
			devcieCommandConfigRecord.setCommandType("SET_LIMIT_SPEED");
			devcieCommandConfigRecord.setCommandValue(String.valueOf(vehModel.getLimitSpeed()));
			java.util.Date utilDate = new java.util.Date();
			devcieCommandConfigRecord.setCommandSendTime(utilDate);
			devcieCommandConfigRecord.setUserId(user.getId());
			if ("000".equals(statusCode)) {
				devcieCommandConfigRecord.setCommandExcuteStatus("excuting");
				devcieCommandConfigRecord.setCommandSendStatus(statusCode);
				msg = Constants.API_MESSAGE_SUCCESS;
			} else {
				devcieCommandConfigRecord.setCommandExcuteStatus("failure");
				devcieCommandConfigRecord.setCommandSendStatus(statusCode);
				msg = Constants.UPDATE_LIMIT_SPEED_ERROR;
			}
			// 插入 设备命令记录表
			deviceService.addDeviceCommandConfigRecord(devcieCommandConfigRecord);
		} else {
			msg = Constants.UPDATE_LIMIT_SPEED_NO_DEVICE;
		}
		return msg;
	}

	public List<VehicleModel> listVehicleAutoComplete(User loginUser, String vehicleNumber) {
		return vehicleService.findVehicleByVehicleNumber(loginUser, vehicleNumber);
	}

	public List<Vehicle> vehicleListMantainance(User loginUser, VehicleListMantainanceDto dto) {
		VehicleQueryDTO model = new VehicleQueryDTO();
		model.setVehicleNumber(dto.getVehicleNumber());
		model.setDeviceNumber(dto.getDeviceNumber());
		return vehicleService.findVehicleListInMantainance(loginUser, model);
	}

	public TripPropertyModel findTripPropertyDataByTimeRangeByName(VehicleHistoryTrackeDto dto) throws Exception {
		VehicleModel vehicle = vehicleService.findVehicleByPlate(dto.getVehicleNumber());
		if (vehicle == null) {
			return null;
		}
		String imei = vehicle.getDeviceNumber();
		TripPropertyModel tripPropertyModel = null;
		Map<String, Object> result = new ServiceAdapter(shouqiService).doService(
				ActionName.FINDTRIPPROPERTYDATABYTIMERANGE,
				new Object[] { imei, dto.getStarttime(), dto.getEndtime() });
		if (result.get("data") != null) {
			JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
			if ("000".equals(jsonNode.get("status").asText())) {
				ArrayNode rows = (ArrayNode) jsonNode.get("result");
				if (rows != null && rows.size() > 0) {
					int mileage = 0;
					double avgFule = 0.0d;
					JsonNode dataNode = null;
					// 涉及到obd更换，只获得最新obd数据
					if (rows.size() == 1) {
						dataNode = rows.get(0);
					} else if (rows.size() == 2) {
						dataNode = rows.get(1);
					}
					tripPropertyModel = new TripPropertyModel();
					if (null != dataNode) {
						mileage = dataNode.get("mileage").asInt();
						avgFule = dataNode.get("avgfuel").asDouble();
						tripPropertyModel.setDrivetime(dataNode.get("drivetime").asInt());
						tripPropertyModel.setStoptime(dataNode.get("stoptime").asInt());
					}
					// 米转换为公里
					DecimalFormat df = new DecimalFormat("0.00");
					tripPropertyModel.setMileage(Double.valueOf(df.format((double) mileage / 1000)));
					Double data1 = Double.valueOf(avgFule);
					Double data2 = Double.valueOf(0.0d);
					if (data1.equals(data2)) {
						// 理论油耗
						VehicleModel vehicleModel = vehicleService.findVehicleByImei(imei);
						if (vehicleModel != null) {
							tripPropertyModel.setFuel(Double.valueOf(
									df.format((double) mileage * vehicleModel.getTheoreticalFuelCon() / 100000)));
						} else {
							tripPropertyModel.setFuel(0.0);
						}
					} else {
						tripPropertyModel
								.setFuel(Double.valueOf(df.format(tripPropertyModel.getMileage() * avgFule / 100)));
					}
				}
			}
		}
		return tripPropertyModel;
	}

	public List<VehMoniTreeStatusNode> listVehMoniStatusTreeData(User loginUser, String vehicleNumber) {
		long orgId = loginUser.getOrganizationId();
		String orgName = organizationService.findById(orgId).getName();
		
		Map<String, Object> map = new HashMap<>();
		String type = "";
		if (loginUser.isEntAdmin()) {// 企业
			type = "0";
		} else if (loginUser.isRentEnterpriseAdmin()) {// 租车公司
			type = "1";
		} else {// 部门及子部门
			type = "2";
		}
		List<VehMoniTreeStatusNode> root = vehicleService.listvehicleMoniStatusTree(orgId, orgName, type);

		if (!StringUtils.isEmpty(vehicleNumber)) {
			// filter tree
			//map.put("children", filterTreeWithVehicleNumber(root, vehicleNumber));
			return filterTreeWithVehicleNumber(root, vehicleNumber);
		} else {
		//	map.put("children", root);
			return root;
		}
		//return map;
	}

	private static List<VehMoniTreeStatusNode> filterTreeWithVehicleNumber(List<VehMoniTreeStatusNode> tree,
			String vehicleNumber) {
		if (tree != null && StringUtils.isNotBlank(vehicleNumber)) {
			try {
				// 进行UTF转码
				// vehicleNumber = new String(vehicleNumber
				// .getBytes("iso8859-1"),"utf-8");
				// vehicleNumber = URLDecoder.decode(vehicleNumber, "UTF-8");
				// 过滤双引号
				String vehiclePlate = vehicleNumber.replace("\"", "").replace("\'", "").toUpperCase();

				// 过滤单引号
				// vehicleNumber = vehicleNumber.replace("\'", "");

				// 进行统一大写处理
				// vehicleNumber = vehicleNumber.toUpperCase();

				// root filter with vehicle number
				for (VehMoniTreeStatusNode root : tree) {
					filterChildrenNodes(root.getChildren(), vehiclePlate);
					filterNullChildrenNodes(root.getChildren());
				}
			} catch (Exception e) {
				LOG.error("filter tree exception happened!", e);
			}
		}

		return tree;
	}

	private static boolean filterChildrenNodes(List<VehMoniTreeStatusNode> nodes, String vehicleNumber) {
		boolean flag = false;
		if (nodes != null) {
			for (int i = 0; i < nodes.size(); i++) {
				VehMoniTreeStatusNode node = nodes.get(i);
				if (!node.getLeaf()) {
					if (!filterChildrenNodes(node.getChildren(), vehicleNumber)) {
						nodes.set(i, null);
					} else {
						flag = true;
					}
				} else {
					/*String viewType = node.getViewType();
					String[] tmps = viewType.split("_");
					if (tmps != null && tmps.length > 3) {
						viewType = tmps[2];
					}*/
					String vNumber=node.getVehicleNumber();
					if (!StringUtils.isNotBlank(vNumber) || !vNumber.contains(vehicleNumber)) {
						nodes.set(i, null);
					} else {
						flag = true;
					}
				}
			}
		}
		return flag;
	}

	/**
	 * filterNullChildrenNodes
	 * 
	 * @param nodes
	 */
	private static void filterNullChildrenNodes(List<VehMoniTreeStatusNode> nodes) {
		if (nodes != null && !nodes.isEmpty()) {
			List<VehMoniTreeStatusNode> nullTempProd = new ArrayList<>(1);
			nullTempProd.add(null);
			nodes.removeAll(nullTempProd);

			for (VehMoniTreeStatusNode node : nodes) {
				if (!node.getLeaf() && node.getChildren() != null) {
					filterNullChildrenNodes(node.getChildren());
				}
			}
		}
	}

	public PagModel listUnAssignedVehicle(User loginUser, UnAssignedVehicleDto dto) throws Exception {
		VehicleListForOrgDto vehModel = new VehicleListForOrgDto();
		BeanUtils.copyProperties(vehModel, dto);

		// 企业管理员
		if (loginUser.isEntAdmin()) {
			Long entId = loginUser.getOrganizationId();
			vehModel.setEntId(entId);
		}

		// 部门管理员
		if (loginUser.isDeptAdmin()) {
			Long deptId = loginUser.getOrganizationId();
			Organization org = orgService.findTopOrganization(deptId);
			vehModel.setEntId(org.getId());
		}
		return vehicleService.findUnAssignedVehicelListForOrg(vehModel);
	}

	public boolean addVehicelToCurrOrg(VehicelToCurrOrgDto dto) throws Exception {
		AllocateDepModel allocateDepModel = new AllocateDepModel();
		BeanUtils.copyProperties(allocateDepModel, dto);
		Organization org = orgService.findById(allocateDepModel.getAllocateDepId());
		// 如果分配id为企业，则不能分配给自己
		int msg = 0;
		if (org != null && org.getParentId() != 0) {
			String vehicelIds = allocateDepModel.getIds();
			if (StringUtils.isNotBlank(vehicelIds)) {
				String[] tempVehicelIds = vehicelIds.split(",");
				Long[] VehicelIdsArr = new Long[tempVehicelIds.length];
				for (int i = 0; i < tempVehicelIds.length; i++) {
					VehicelIdsArr[i] = TypeUtils.obj2Long(tempVehicelIds[i]);
				}
				allocateDepModel.setIdArray(VehicelIdsArr);
				allocateDepModel.setDeptName(org.getName());
				msg = vehicleService.updateCurrOrgToVehicle(allocateDepModel);
			}
		}
		return msg > 0 ? true : false;
	}

	public List<VehicleListForOrgDto> findVehicleListbyIds(String ids) {
		String[] tempIds = ids.split(",");
		Long[] VehicelIds = new Long[tempIds.length];
		for (int i = 0; i < tempIds.length; i++) {
			VehicelIds[i] = Long.parseLong(tempIds[i]);
		}
		return vehicleService.findVehicleListbyIds(VehicelIds);
	}

	public Map<String, Object> removeVehicleFromOrg(User loginUser, VehicelToCurrOrgDto dto) throws Exception {
		AllocateDepModel allocateDepModel = new AllocateDepModel();
		BeanUtils.copyProperties(allocateDepModel, dto);
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		// 如果当前为企业管理员,选中树节点也为企业，则不能移除车辆
		if (loginUser.isAdmin() && allocateDepModel.getAllocateDepId() != null
				&& allocateDepModel.getAllocateDepId().equals(loginUser.getOrganizationId())) {
			map.put("status", "failure");
		} else {
			String ids = allocateDepModel.getIds();
			if (StringUtils.isNotBlank(ids)) {
				String[] tempIds = ids.split(",");
				Long[] VehicelIds = new Long[tempIds.length];
				for (int i = 0; i < tempIds.length; i++) {
					VehicelIds[i] = Long.parseLong(tempIds[i]);
				}
				allocateDepModel.setIdArray(VehicelIds);
				List<VehicleAndOrderModel> unRemoveVehicleList = vehicleService
						.findUnRemoveVehicleList(allocateDepModel);
				if (!unRemoveVehicleList.isEmpty()) {
					map.put("data", unRemoveVehicleList);
					map.put("status", "failure");
				} else {
					map.put("status", "success");
				}
			} else {
				map.put("status", "failure");
			}
		}
		return map;
	}

	public List<VehicleBrandModel> listvehicleModel(VehicleModelDto dto) {
		boolean isEnt = false;
		List<VehicleBrandModel> vBrandList = new ArrayList<>();
		if (dto.getDeptId() != null) {
			Organization org = orgService.findById(dto.getDeptId());
			if (org.getParentId() == 0) {
				isEnt = true;
			}
			vBrandList = vehicleService.listvehicleModel(isEnt, dto.getDeptId(), dto.gettFlag());
		}
		return vBrandList;
	}

	public HomePageMapModel queryObdLocation(User loginUser, LocationDto dto) throws Exception {
		HomePageMapModel model = new HomePageMapModel();
		List<VehicleLocationModel> obdLocationList = new ArrayList<>();
		model.setObdLocationList(obdLocationList);

		Long orgId = 0l;
		String vehicleNumber = "";

		if (dto.getOrgId() != null) {
			orgId = dto.getOrgId();
		}

		if (!StringUtils.isBlank(dto.getVehicleNumber())) {
			vehicleNumber = dto.getVehicleNumber();
		}

		List<VehicleModel> vehicleList = new ArrayList<>();
		if (orgId.longValue() == 0 && "".equals(vehicleNumber)) {// 根节点查询
			orgId = loginUser.getOrganizationId();

			Organization organization = orgService.findById(orgId);
			String enterprisesType = organization.getEnterprisesType();// (0:租车公司，1:用车企业)
			if (StringUtils.isEmpty(enterprisesType)) {
				vehicleList = vehicleService.queryVehicleListByDept(orgId);
			} else {
				vehicleList = vehicleService.queryVehicleListByEntAndRent(orgId, enterprisesType);
			}
		} else {
			if (orgId.longValue() != 0) {// 部门或子部门查询
				vehicleList = vehicleService.queryVehicleListByDept(orgId);
			} else {// 按车牌查询
				VehicleModel vehicleModel = vehicleService.findVehicleByPlate(vehicleNumber);
				if (vehicleModel != null) {
					vehicleList.add(vehicleModel);
				}
			}
		}

		// 过滤车辆绑定的license状态不正常的车辆
		if (!vehicleList.isEmpty()) {
			vehicleList = vehicleService.getInUsedVehicleListByDeviceNumber(vehicleList);
		}

		if (!vehicleList.isEmpty()) {
			for (VehicleModel vehicleModel : vehicleList) {
				// 从redis中取obd数据
				String obdRedisData = redisService.get(VEHICLE_PREFIX + vehicleModel.getDeviceNumber());
				if (StringUtils.isNotEmpty(obdRedisData)) {
					VehicleLocationModel vehicleLocationModel = MAPPER.readValue(obdRedisData,
							VehicleLocationModel.class);
					// GPS转百度坐标
					vehicleLocationModel = transGPStoBaidu(vehicleLocationModel);
					vehicleLocationModel.setId(vehicleModel.getId());
					// 过滤掉经纬度为0.0的值
					if (vehicleLocationModel.getLatitude() != 0.0 && vehicleLocationModel.getLongitude() != 0.0) {
						obdLocationList.add(vehicleLocationModel);

						String tracetime = vehicleLocationModel.getTracetime();
						if (StringUtils.isNotEmpty(tracetime)) {
							Date currentDate = new Date();
							long minutes = TimeUtils.timeBetween(TimeUtils.formatDate(tracetime), currentDate,
									Calendar.MINUTE);
							if (minutes > 10) {// 车辆最近一条数据与当前时间超过10分钟的，状态为离线
								vehicleLocationModel.setStatus(
										com.cmdt.carrental.common.constants.Constants.VEHICLE_RUNTIME_STATUS_OFFLINE);
							}
						} else {// 无tracktime,无法判断上传时间，设置为离线
							vehicleLocationModel.setStatus(
									com.cmdt.carrental.common.constants.Constants.VEHICLE_RUNTIME_STATUS_OFFLINE);
						}
					}
				}
			}
		}
		return model;
	}
	
	
	public PagModel findVehicleAssignedMarkers(String vehicleNumber,Integer currentPage, Integer numPerPage){
		return vehicleService.findVehicleAssignedMarkers(vehicleNumber,currentPage,numPerPage);
	}
	
	public PagModel findVehicleAvialiableMarkers(User loginUser,VehicleMarkerPageDto vehicleMarkerPageDto) throws Exception{
		VehicleQueryDTO vehicleQueryDto=new VehicleQueryDTO();
		BeanUtils.copyProperties(vehicleQueryDto, vehicleMarkerPageDto);
		vehicleQueryDto.setOrganizationId(loginUser.getOrganizationId());
		return vehicleService.findVehicleAvialiableMarkers(vehicleQueryDto);
	}
	
	public void assignMarkers(Long vehicleId,String markerIds){
		vehicleService.assignMarkers(vehicleId, markerIds);
	}
	
	public void unassignMarkers(Long vehicleId,Long markerId){
		vehicleService.unassignMarkers(vehicleId,markerId);
	}

	public String batchImportVehicle(User loginUser,MultipartFile multiFile) throws Exception{
		String extension = org.springframework.util.StringUtils
				.getFilenameExtension(multiFile.getOriginalFilename());
    	if(null != extension && ("xls".equalsIgnoreCase(extension) 
				|| "xlsx".equalsIgnoreCase(extension) || "csv".equalsIgnoreCase(extension))) {
    		Long orgId = loginUser.getOrganizationId();
			String orgName = loginUser.getOrganizationName();

			List<Object[]> dataList = CsvUtil.importFile(multiFile);
			List<Vehicle> modelList = new ArrayList<Vehicle>();
			for (int i = 0, num = dataList.size(); i < num; i++) {
				Object[] dataObj = dataList.get(i);
				// 过滤表头数据
				if (dataObj.length < 20)
					continue;
				Vehicle model = new Vehicle();
				String cell0 = TypeUtils.obj2String(dataObj[0]);// 车牌号
				String cell1 = TypeUtils.obj2String(dataObj[1]);// 车辆类型
				String cell2 = TypeUtils.obj2String(dataObj[2]);// 车辆品牌
				String cell3 = TypeUtils.obj2String(dataObj[3]);// 车辆型号
				String cell4 = TypeUtils.obj2String(dataObj[4]);// 车架号
				String cell5 = TypeUtils.obj2String(dataObj[5]);// 车辆颜色
				String cell6 = TypeUtils.obj2String(dataObj[6]);// 车辆座位数
				String cell7 = TypeUtils.obj2String(dataObj[7]);// 排量
				String cell8 = TypeUtils.obj2String(dataObj[8]);// 燃油号
				String cell9 = TypeUtils.obj2String(dataObj[9]);// 车辆所属省
				String cell10 = TypeUtils.obj2String(dataObj[10]);// 车辆所属城市
				String cell11 = TypeUtils.obj2String(dataObj[11]);// 购买时间
				String cell12 = TypeUtils.obj2String(dataObj[12]);// 理论油耗
				String cell13 = TypeUtils.obj2String(dataObj[13]);// 保险到期日
				String cell14 = TypeUtils.obj2String(dataObj[14]);// 车位信息
				String cell15 = TypeUtils.obj2String(dataObj[15]);// 车辆用途
				String cell16 = TypeUtils.obj2String(dataObj[16]);// 限速
				String cell17 = TypeUtils.obj2String(dataObj[17]);// 营业开始时间
				String cell18 = TypeUtils.obj2String(dataObj[18]);// 营业结束时间
				String cell19 = TypeUtils.obj2String(dataObj[19]);// 年检到期日
				String cell20 = TypeUtils.obj2String(dataObj[20]);// 部门ID

				model.setVehicleNumber(cell0);
				model.setVehicleType(cell1);
				model.setVehicleBrand(cell2);
				model.setVehicleModel(cell3);
				model.setVehicleIdentification(cell4);
				model.setVehicleColor(cell5);
				if (StringUtils.isNotBlank(cell6)) {
					model.setSeatNumber(TypeUtils.obj2Integer(cell6));
				}
				model.setVehicleOutput(cell7);
				model.setVehicleFuel(cell8);
				model.setProvince(cell9);
				model.setCity(cell10);
				if (StringUtils.isNotBlank(cell11)) {
					model.setVehicleBuyTime(new java.sql.Date(TypeUtils.obj2DateFormat(cell11).getTime()));
				}
				if (StringUtils.isNotBlank(cell12)) {
					model.setTheoreticalFuelCon(TypeUtils.obj2Double(cell12));
				}
				if (StringUtils.isNotBlank(cell13)) {
					model.setInsuranceExpiredate(new java.sql.Date(TypeUtils.obj2DateFormat(cell13).getTime()));
				}
				model.setParkingSpaceInfo(cell14);
				model.setVehiclePurpose(cell15);
				if (StringUtils.isNotBlank(cell16)) {
					model.setLimitSpeed(TypeUtils.obj2Integer(cell16));
				}
				model.setStartTime(cell17);
				model.setEndTime(cell18);
				if (StringUtils.isNotBlank(cell19)) {
					model.setInspectionExpiredate(new java.sql.Date(TypeUtils.obj2DateFormat(cell19).getTime()));
				}
				if(StringUtils.isNotBlank(cell20)){
					model.setOrganizationId(cell20);
				}
				model.setEntId(orgId);
				model.setEntName(orgName);
				modelList.add(model);
			}

			int sucNum = 0;// 成功导入几条数据
			int failNum = 0;// 失败几条数据
			int total = modelList.size();
			StringBuilder sb = new StringBuilder();
			if (modelList != null && !modelList.isEmpty()) {
				int num = 0;
				for (Vehicle model : modelList) {
					num++;
					String errMsg = validateData(loginUser,model, num);
					if (StringUtils.isNotBlank(errMsg)) {
						sb.append(errMsg).append("<br />");
						failNum++;
					} else {
						vehicleService.insertVehicleByEntAdmin(model);
					}
				}
				sucNum = total - failNum;
			}

			String failureMsgVal = sb.toString();
			if (StringUtils.isEmpty(failureMsgVal)) {
				return "成功导入:" + sucNum + ",失败:" + failNum;
			} else {
				return "成功导入:" + sucNum + ",失败:" + failNum + ",详细如下:<br />" + failureMsgVal;
			}

    	}else {
    		throw new ServiceException(MessageCode.COMMON_UPLOAD_FILE_EXTENSION_ERROR);
    	}
	}
	
	private String validateData(User loginUser,Vehicle vehicle, Integer num) {
		
		String msg = "";

		// 判断是否有为空的数据
		if (StringUtils.isBlank(vehicle.getVehicleNumber())) {
			msg = "第" + num + "条数据:车牌号为空,导入失败!";
			return msg;
		} else if (null != vehicleService.findVehicleByPlate(vehicle.getVehicleNumber())) {
			msg = "第" + num + "条数据:车牌号已被使用,导入失败!";
			return msg;
		} else if(!vehicle.getVehicleNumber().matches("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$")){
			msg = "第" + num + "条数据:车牌号格式输入错误,导入失败!";
			return msg;
		}
		if (StringUtils.isBlank(vehicle.getVehicleBrand())) {
			msg = "第" + num + "条数据:车辆品牌为空,导入失败!";
			return msg;
		}
		if (StringUtils.isBlank(vehicle.getVehicleIdentification())) {
			msg = "第" + num + "条数据:车架号为空,导入失败!";
			return msg;
		} else if (vehicleService.countByVehicleIdentification(vehicle.getVehicleIdentification(),null)!=null) {
			msg = "第" + num + "条数据:车架号已被使用,导入失败!";
			return msg;
		} else if(!vehicle.getVehicleIdentification().matches("^[A-Z0-9]{17}$")){
			msg = "第" + num + "条数据:车架号格式输入错误,导入失败!";
			return msg;
		}
		
		if(StringUtils.isBlank(vehicle.getProvince()) && StringUtils.isNotBlank(vehicle.getCity())){
			msg = "第" + num + "条数据:车辆所属省为空，但车辆所属市不为空,导入失败!";
			return msg;
		}else if(StringUtils.isNotBlank(vehicle.getProvince()) && StringUtils.isBlank(vehicle.getCity())){
			msg = "第" + num + "条数据:车辆所属省不为空，但车辆所属市为空,导入失败!";
			return msg;
		}else if(StringUtils.isNotBlank(vehicle.getProvince()) && StringUtils.isNotBlank(vehicle.getCity())){
			Integer id=provinceMap.get(vehicle.getProvince());
			if(id!=null){
				List<Map<String, Integer>> cList=cityList.get(id);
				Integer cityId=null;
				for(Map<String, Integer> map:cList){
					 cityId=map.get(vehicle.getCity());
					if(cityId!=null){
						break;
					}
				}
				if(cityId==null){
					msg = "第" + num + "条数据:车辆所属市不属于当前省,导入失败!";
					return msg;
				}
				vehicle.setCity(String.valueOf(cityId));		
			}else{
				msg = "第" + num + "条数据:车辆所属省不存在,导入失败!";
				return msg;
			}
		}
		if (StringUtils.isNotBlank(vehicle.getVehicleOutput())) {
			if (!vehicle.getVehicleOutput().matches("^\\d+(\\.\\d+)?(L|T)$")) {
				msg = "第" + num + "条数据:排量格式输入错误(L或者T),导入失败!";
				return msg;
			}
		}
		if (null == vehicle.getInsuranceExpiredate()) {
			msg = "第" + num + "条数据:保期到期日为空,导入失败!";
			return msg;
		}
		if (null == vehicle.getInspectionExpiredate()) {
			msg = "第" + num + "条数据:年检到期日为空,导入失败!";
			return msg;
		}
		if (null == vehicle.getLimitSpeed()) {
			msg = "第" + num + "条数据:限速为空,导入失败!";
			return msg;
		}
		if (StringUtils.isBlank(vehicle.getVehicleType())) {
			msg = "第" + num + "条数据:车辆类型为空,导入失败!";
			return msg;
		}
		if (StringUtils.isBlank(vehicle.getVehicleModel())) {
			msg = "第" + num + "条数据:车辆型号为空,导入失败!";
			return msg;
		}
		if (null == vehicle.getTheoreticalFuelCon()) {
			msg = "第" + num + "条数据:理论油耗为空,导入失败!";
			return msg;
		}
		if (null == vehicle.getVehicleFuel()) {
			msg = "第" + num + "条数据:燃油号为空,导入失败!";
			return msg;
		}
		if (StringUtils.isBlank(vehicle.getStartTime())) {
			//vehicle.setStartTime("09:00");
		}else if(!vehicle.getStartTime().matches("^([0][0-9]|[1][0-9]|2[0-3]):([0-5][0-9])$")){
			msg = "第" + num + "条数据:开始运营时间格式错误,导入失败!";
			return msg;
		}
		if (StringUtils.isBlank(vehicle.getEndTime())) {
			//vehicle.setStartTime("18:00");
		}else if (!vehicle.getEndTime().matches("^([0][0-9]|[1][0-9]|2[0-3]):([0-5][0-9])$")) {
			msg = "第" + num + "条数据:结束运营时间格式错误,导入失败!";
			return msg;
		}
		
		if(StringUtils.isNotBlank(vehicle.getVehiclePurpose())){
			if(vehicle.getVehiclePurpose().trim().equals("生产用车") || vehicle.getVehiclePurpose().trim().equals("营销用车") ||
					vehicle.getVehiclePurpose().trim().equals("接待用车") || vehicle.getVehiclePurpose().trim().equals("会议用车")){
			}else{
				msg = "第" + num + "条数据:车辆用途不存在,导入失败!";
				return msg;
			}
		}
		
		if(StringUtils.isNotBlank(vehicle.getOrganizationId())){
			Organization organization=	orgService.findCurrOrgBelongOrg(vehicle.getOrganizationId(),loginUser.getOrganizationId());
			if(organization==null){
				msg = "第" + num + "条数据:部门ID不属于当前企业,导入失败!";
				return msg;
			}else{
				vehicle.setCurrentuseOrgId(organization.getId());
				vehicle.setCurrentuseOrgName(organization.getName());
			}
		}else{
			vehicle.setCurrentuseOrgId(null);
			vehicle.setCurrentuseOrgName(null);
		}
		return msg;
	}
	
	public VehicleLocationModel queryObdLocationByImei(String deviceNumber) throws Exception{
		String obdRedisData = redisService.get(VEHICLE_PREFIX + deviceNumber);
		VehicleLocationModel vehicleLocationModel = new VehicleLocationModel();
		if (StringUtils.isNotEmpty(obdRedisData)) {
			vehicleLocationModel = MAPPER.readValue(obdRedisData, VehicleLocationModel.class);
			// GPS转百度坐标
			vehicleLocationModel = transGPStoBaidu(vehicleLocationModel);
		}
		return vehicleLocationModel;
	}
}
