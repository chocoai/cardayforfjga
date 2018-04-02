package com.cmdt.carday.microservice.biz.service;

import java.io.File;
import java.text.DecimalFormat;
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

import com.cmdt.carday.microservice.common.Constants;
import com.cmdt.carday.microservice.model.request.usageReport.UsageReportDto;
import com.cmdt.carday.microservice.model.request.usageReport.UsageReportPageDto;
import com.cmdt.carday.microservice.model.request.usageReport.UsageReportVehDto;
import com.cmdt.carrental.common.cache.RedisService;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.DrivingDetailedReportModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.UsageReportAllMileageAndFuleconsModel;
import com.cmdt.carrental.common.model.UsageReportLineModel;
import com.cmdt.carrental.common.model.UsageReportModel;
import com.cmdt.carrental.common.model.VehicleLocationModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleStatus;
import com.cmdt.carrental.common.service.UsageReportService;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.common.util.CsvUtil;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.TimeUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;


@Service
public class PlatformUsageReportService {

	private static final Logger LOG = LoggerFactory.getLogger(PlatformUsageReportService.class);
	
	@Autowired
	UsageReportService usageReportService;
	
	@Autowired
	private ShouqiService shouqiService;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private VehicleService vehicleService;
	
	private static final String VEHICLE_PREFIX = "VEHICLE_";
	
	private static DecimalFormat df = new DecimalFormat("0.00");

	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * 车辆使用统计
	 * 饼状图，柱状图
	 * @param usageReportDto
	 * @return
	 */
	public UsageReportModel getPieAndColumnarData(UsageReportDto usageReportDto){
		Long orgId = usageReportDto.getOrgId();
		Boolean selfDept = usageReportDto.getSelfDept();
		Boolean childDept = usageReportDto.getChildDept();
		
		String starttime = usageReportDto.getStarttime();
		String endtime = usageReportDto.getEndtime();
		
		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
			//如果不输入时间，取最近一个月时间作为开始和结束时间
			Date endDate = new Date();
	    	Date startDate = DateUtils.addDays(endDate,-29);
	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
		}
		
		Date startDate = DateUtils.string2Date(starttime + " 00:00:00","yyyy-MM-dd HH:mm:ss");
		Date endDate = DateUtils.string2Date(endtime + " 23:59:59","yyyy-MM-dd HH:mm:ss");
		
		return  usageReportService.getPieAndColumnarData(orgId,selfDept,childDept,startDate,endDate);
	}
	
	/**
	 * 车辆使用统计
	 * 曲线图
	 * @param usageReportDto
	 * @return
	 */
	public UsageReportLineModel getVehicleLinePropertyData(UsageReportDto usageReportDto){
		Long orgId = usageReportDto.getOrgId();
		Boolean selfDept = usageReportDto.getSelfDept();
		Boolean childDept = usageReportDto.getChildDept();
		String starttime = usageReportDto.getStarttime();
		String endtime = usageReportDto.getEndtime();
		
		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
			//如果不输入时间，取最近一个月时间作为开始和结束时间
			Date endDate = new Date();
	    	Date startDate = DateUtils.addDays(endDate,-29);
	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
		}
		
		Date startDate = DateUtils.string2Date(starttime + Constants.API_TIME_MIN,Constants.API_DATE_YYYY_MM_DD_HH24_MI_SS);
		Date endDate = DateUtils.string2Date(endtime + Constants.API_TIME_MAX,Constants.API_DATE_YYYY_MM_DD_HH24_MI_SS);
		return usageReportService.getVehicleLinePropertyData(startDate,endDate,orgId,selfDept,childDept);
	}
	
	/**
	 * 车辆使用统计
	 * 车辆统计
	 * @param usageReportDto
	 * @return
	 */
	public PagModel getVehiclePropertyData(UsageReportPageDto usageReportPageDto){
		Long orgId = usageReportPageDto.getOrgId();
		Boolean selfDept = usageReportPageDto.getSelfDept();
		Boolean childDept = usageReportPageDto.getChildDept();
		String starttime = usageReportPageDto.getStarttime();
		String endtime = usageReportPageDto.getEndtime();
		
		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
			//如果不输入时间，取最近一个月时间作为开始和结束时间
			Date endDate = new Date();
	    	Date startDate = DateUtils.addDays(endDate,-29);
	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
		}
		
		Date startDate = DateUtils.string2Date(starttime + Constants.API_TIME_MIN,Constants.API_DATE_YYYY_MM_DD_HH24_MI_SS);
		Date endDate = DateUtils.string2Date(endtime + Constants.API_TIME_MAX,Constants.API_DATE_YYYY_MM_DD_HH24_MI_SS);
		Integer currentPage = usageReportPageDto.getCurrentPage();
		Integer numPerPage = usageReportPageDto.getNumPerPage();
		if(currentPage == null || numPerPage == null){
			currentPage = 1;
			numPerPage = 10;
		}
		return usageReportService.getVehiclePropertyData(startDate,endDate,orgId,selfDept,childDept,currentPage,numPerPage);
	}
	
	/**
	 * 车辆使用统计(导出)
	 * 车辆统计
	 * @param usageReportDto
	 * @return
	 * @throws Exception 
	 */
	public File generateVehiclePropertyDataFile(UsageReportDto usageReportDto) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> retList = new ArrayList<String>();
		
		Long orgId = usageReportDto.getOrgId();
		Boolean selfDept = usageReportDto.getSelfDept();
		Boolean childDept = usageReportDto.getChildDept();
		String starttime = usageReportDto.getStarttime();
		String endtime = usageReportDto.getEndtime();
		
		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
			//如果不输入时间，取最近一个月时间作为开始和结束时间
			Date endDate = new Date();
	    	Date startDate = DateUtils.addDays(endDate,-29);
	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
		}
		
		Date startDate = DateUtils.string2Date(starttime + Constants.API_TIME_MIN,Constants.API_DATE_YYYY_MM_DD_HH24_MI_SS);
		Date endDate = DateUtils.string2Date(endtime + Constants.API_TIME_MAX,Constants.API_DATE_YYYY_MM_DD_HH24_MI_SS);
		retList = usageReportService.getAllVehiclePropertyData(startDate,endDate,orgId,selfDept,childDept);
		map.put("data", retList);
		map.put("filename", "车辆使用统计.xls");
		map.put("sheet", "车辆使用统计列表");
		map.put("header", "序号,车牌号,车辆品牌,排量,燃油号,部门,总里程(千米),总耗油量(升),总行驶时长(小时),使用率,平均里程(千米/天),平均油耗量(升/天),平均行驶时长(小时/天)");
		return CsvUtil.exportExcel(map);
	}
	
	/**
	 * 未使用车辆统计
	 * @param usageReportDto
	 * @return
	 */
	public PagModel getIdleVehicleList(UsageReportPageDto usageReportPageDto){
		Long orgId = usageReportPageDto.getOrgId();
		Boolean selfDept = usageReportPageDto.getSelfDept();
		Boolean childDept = usageReportPageDto.getChildDept();
		String starttime = usageReportPageDto.getStarttime();
		String endtime = usageReportPageDto.getEndtime();
		
		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
			//如果不输入时间，取最近一个月时间作为开始和结束时间
			Date endDate = new Date();
	    	Date startDate = DateUtils.addDays(endDate,-29);
	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
		}
		
		Date startDate = DateUtils.string2Date(starttime + Constants.API_TIME_MIN,Constants.API_DATE_YYYY_MM_DD_HH24_MI_SS);
		Date endDate = DateUtils.string2Date(endtime + Constants.API_TIME_MAX,Constants.API_DATE_YYYY_MM_DD_HH24_MI_SS);
		Integer currentPage = usageReportPageDto.getCurrentPage();
		Integer numPerPage = usageReportPageDto.getNumPerPage();
		if(currentPage == null || numPerPage == null){
			currentPage = 1;
			numPerPage = 10;
		}
		return usageReportService.getIdleVehicleList(startDate,endDate,orgId,selfDept,childDept,currentPage,numPerPage);
	}
	
	/**
	 * 未使用车辆统计(导出)
	 * @param usageReportDto
	 * @return
	 * @throws Exception 
	 */
	public File generateIdleVehicleListFile(UsageReportDto usageReportDto) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> retList = new ArrayList<String>();
		
		Long orgId = usageReportDto.getOrgId();
		Boolean selfDept = usageReportDto.getSelfDept();
		Boolean childDept = usageReportDto.getChildDept();
		String starttime = usageReportDto.getStarttime();
		String endtime = usageReportDto.getEndtime();
		
		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
			//如果不输入时间，取最近一个月时间作为开始和结束时间
			Date endDate = new Date();
	    	Date startDate = DateUtils.addDays(endDate,-29);
	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
		}
		
		Date startDate = DateUtils.string2Date(starttime + Constants.API_TIME_MIN,Constants.API_DATE_YYYY_MM_DD_HH24_MI_SS);
		Date endDate = DateUtils.string2Date(endtime + Constants.API_TIME_MAX,Constants.API_DATE_YYYY_MM_DD_HH24_MI_SS);
		
		retList = usageReportService.getAllIdleVehicleList(startDate,endDate,orgId,selfDept,childDept);
		map.put("data", retList);
		map.put("filename", "未用车辆列表"+starttime+"至"+endtime+".xls");
		map.put("sheet", "未用车辆列表");
		map.put("header", "车牌号,车辆品牌,车辆型号,所属部门,车辆用途,空闲时间段");
		return CsvUtil.exportExcel(map);
	}
	
	/**
	 * 车辆行驶明细
	 * @param usageReportVehDto
	 * @return
	 */
	public List<DrivingDetailedReportModel> getDrivingDetailedReport(UsageReportVehDto usageReportVehDto){
		List<DrivingDetailedReportModel> retList = new ArrayList<DrivingDetailedReportModel>();
    	String starttime = usageReportVehDto.getStarttime() + Constants.API_TIME_MIN;
    	String endtime =  usageReportVehDto.getEndtime() + Constants.API_TIME_MAX;
    	String vehicleNumber = usageReportVehDto.getVehicleNumber();
        
        VehicleModel vehicleModel = usageReportService.findVehicleListByVehicleNumber(vehicleNumber);
		if(vehicleModel != null){
			String deviceNumber = vehicleModel.getDeviceNumber();
			if(!StringUtils.isEmpty(deviceNumber)){
				Object[] params = new Object[] { deviceNumber, starttime, endtime };
				Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.FINDVEHICLEDRIVINGDETAILLIST, params);
				if (result.get("data") != null) {
					JsonNode jsonNode;
					try {
						jsonNode = MAPPER.readTree(result.get("data").toString());
						if ("000".equals(jsonNode.get("status").asText())) {
							ArrayNode rows = (ArrayNode) jsonNode.get("result");
							if (rows != null) {
								int rowSize = rows.size();
								if (rowSize > 0) {
									String vehicleNumberVal = vehicleModel.getVehicleNumber();
									String vehicleBrandVal = vehicleModel.getVehicleBrand();
									String vehicleModelVal = vehicleModel.getVehicleModel();
									String currentuseOrgNameVal = vehicleModel.getCurrentuseOrgName();
									String vehiclePurposeVal = vehicleModel.getVehiclePurpose();
									
									for (int i = 0; i < rowSize; i++) {
										JsonNode drivingDetailedReportNode = rows.get(i);
										DrivingDetailedReportModel drivingDetailedReportModel = new DrivingDetailedReportModel();
										drivingDetailedReportModel.setVehicleNumber(vehicleNumberVal);
										drivingDetailedReportModel.setVehicleBrand(vehicleBrandVal);
										drivingDetailedReportModel.setVehicleModel(vehicleModelVal);
										drivingDetailedReportModel.setCurrentuseOrgName(currentuseOrgNameVal);
										drivingDetailedReportModel.setVehiclePurpose(vehiclePurposeVal);
										drivingDetailedReportModel.setTracetime(drivingDetailedReportNode.get("tracetime").asText());
										drivingDetailedReportModel.setDetail(drivingDetailedReportNode.get("detail").asText());
										drivingDetailedReportModel.setAddress(drivingDetailedReportNode.get("address").asText());
										retList.add(drivingDetailedReportModel);
									}
								}
							}
						}
					} catch (Exception e) {
						LOG.error("PlatformUsageReportService getDrivingDetailedReport error : ", e);
					} 
				}
			}
		}
		return retList;
	}
	
	/**
	 * 车辆行驶明细(导出)
	 * @param usageReportVehDto
	 * @return
	 * @throws Exception 
	 */
	public File generateDrivingDetailedReportFile(UsageReportVehDto usageReportVehDto) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> retList = new ArrayList<String>();
		
		String starttime = usageReportVehDto.getStarttime() + Constants.API_TIME_MIN;
    	String endtime =  usageReportVehDto.getEndtime() + Constants.API_TIME_MAX;
        String vehicleNumber = usageReportVehDto.getVehicleNumber();
        
        VehicleModel vehicleModel = usageReportService.findVehicleListByVehicleNumber(vehicleNumber);
		if(vehicleModel != null){
			String deviceNumber = vehicleModel.getDeviceNumber();
			
			if(!StringUtils.isEmpty(deviceNumber)){
				Object[] params = new Object[] { deviceNumber, starttime, endtime };
				Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.FINDVEHICLEDRIVINGDETAILLIST, params);
				if (result.get("data") != null) {
					JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
					if ("000".equals(jsonNode.get("status").asText())) {
						ArrayNode rows = (ArrayNode) jsonNode.get("result");
						if (rows != null) {
							int rowSize = rows.size();
							if (rowSize > 0) {
								String vehicleNumberVal = vehicleModel.getVehicleNumber();
								String vehicleBrandVal = vehicleModel.getVehicleBrand();
								String vehicleModelVal = vehicleModel.getVehicleModel();
								String currentuseOrgNameVal = vehicleModel.getCurrentuseOrgName();
								String vehiclePurposeVal = vehicleModel.getVehiclePurpose();
								
								for (int i = 0; i < rowSize; i++) {
									StringBuffer buffer = new StringBuffer();
									JsonNode drivingDetailedReportNode = rows.get(i);
									buffer.append(vehicleNumberVal).append(",");
									buffer.append(vehicleBrandVal).append(",");
									buffer.append(vehicleModelVal).append(",");
									buffer.append(currentuseOrgNameVal).append(",");
									buffer.append(vehiclePurposeVal).append(",");
									buffer.append(drivingDetailedReportNode.get("tracetime").asText()).append(",");
									String detail = drivingDetailedReportNode.get("detail").asText();
									if(StringUtils.isNoneEmpty(detail)){
										detail = detail.replace(",", ".");
									}
									buffer.append(detail).append(",");
									buffer.append(drivingDetailedReportNode.get("address").asText());
									retList.add(buffer.toString());
								}
							}
						}
					}
				}
			}
		}
		
		map.put("data", retList);
		map.put("filename", "车辆行驶明细查询-"+vehicleNumber+"-"+usageReportVehDto.getStarttime()+"-"+ usageReportVehDto.getEndtime()+".xls");
		map.put("sheet", DateUtils.getNowDate());
		map.put("header", "车牌号,车辆品牌,车辆型号,所属部门,车辆用途,时间,明细,位置");
		return CsvUtil.exportExcel(map);
	}
	
	/**
	 * 车辆信息汇总
	 * @param loginUser
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	public VehicleStatus findVehicleStatusData(User loginUser,Long oragId) throws Exception{

		int totalNumber = 0; //车辆总数
		int onLineNumber = 0;//在线
		int drivingNumber = 0;//行驶
		int stopNumber = 0;//停止
		int offLineNumber = 0;//离线
		
		VehicleStatus model = new VehicleStatus();
		Long orgId = oragId;
		if(orgId == null){
			orgId = loginUser.getOrganizationId();
		}
		
		List<VehicleModel>  vehicleList = usageReportService.findAllVehicleListByOrgId(orgId);
		
		if(vehicleList != null && !vehicleList.isEmpty()){
			totalNumber = vehicleList.size();//车辆总数
			Date currentDate = new Date();
			
			for (VehicleModel vehicleModel : vehicleList) {
				String imei = vehicleModel.getDeviceNumber();
				if(StringUtils.isNotEmpty(imei)){
					// 从redis中取obd数据
					String obdRedisData = redisService.get(VEHICLE_PREFIX + imei);
					if (StringUtils.isNotEmpty(obdRedisData)) {
						VehicleLocationModel vehicleLocationModel = MAPPER.readValue(obdRedisData,VehicleLocationModel.class);
						//是否离线
						String tracetime = vehicleLocationModel.getTracetime();
						if(StringUtils.isNotEmpty(tracetime)){
							long minutes = TimeUtils.timeBetween(TimeUtils.formatDate(tracetime),currentDate,Calendar.MINUTE);
							if(minutes > 10){//车辆最近一条数据与当前时间超过10分钟的，状态为离线
								offLineNumber ++; //离线
							}else{
								if(vehicleLocationModel.getSpeed() > 0){
									drivingNumber ++;//行驶
								}
							}
						}else{//无tracktime,无法判断上传时间，设置为离线
							offLineNumber ++; //离线
						}
					}else{//在redis中无上传数据，设置为离线
						offLineNumber ++; //离线
					}
				}else{//无imei,无法判断上传数据，设置为离线
					offLineNumber ++; //离线
				}
			}
			
			//在线=车辆总数-离线；
			if(totalNumber >= offLineNumber){
				onLineNumber = totalNumber - offLineNumber;
			}
			
			//停止=在线-行驶
			if(onLineNumber >= drivingNumber){
				stopNumber = onLineNumber - drivingNumber;
			}
			
		}
		
		model.setTotalNumber(totalNumber);
		model.setOnLineNumber(onLineNumber);
		model.setDrivingNumber(drivingNumber);
		model.setOffLineNumber(offLineNumber);
		model.setStopNumber(stopNumber);
		
		return model;	
	}
	
	public UsageReportAllMileageAndFuleconsModel getVehicleAllMileageAndFuleconsListByVehNum(User loginUser, String vehicleNumber) throws Exception{
		
		UsageReportAllMileageAndFuleconsModel usageReportAllMileageAndFuleconsModel = new UsageReportAllMileageAndFuleconsModel();
		
		VehicleModel vehicle = vehicleService.getVehicleInfoByVehicleNumber(vehicleNumber,loginUser);
		//should use accutal device number
		String deviceNumber = vehicle.getAccDeviceNumber();
		
		//企业管理员,部门管理员
		if(loginUser.isEntAdmin() || loginUser.isDeptAdmin()){
			//填充除了累计总油耗，累计里程的所有数据
			usageReportAllMileageAndFuleconsModel = usageReportService.getVehicleAllMileageAndFuleconsList(vehicleNumber,deviceNumber);
			
			//填充累计总油耗,累计里程(调用首汽接口)
			Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.GETLOCATIONBYIMEI, new Object[]{deviceNumber});
			if (result != null && result.get("status").equals("success") && result.get("data") != null && StringUtils.isNotEmpty(result.get("data").toString())) {
				JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
				if ("000".equals(jsonNode.get("status").asText())) {
					JsonNode resultNode = jsonNode.get("result");
					if(resultNode != null){
						if(resultNode.get("mileage") != null){
							String mileageVal = resultNode.get("mileage").asText();
							usageReportAllMileageAndFuleconsModel.setTotalMileage(df.format((double)Integer.valueOf(mileageVal)/1000));//米转换为千米
						}
						if(resultNode.get("fuelCons") != null){
							String fuelConsVal = resultNode.get("fuelCons").asText();
							if("0".equals(fuelConsVal) || "0.0".equals(fuelConsVal)){
								VehicleModel vehicleModel = usageReportService.findVehicleListByDeviceNumberAndImei(vehicleNumber, deviceNumber);
								if(vehicleModel != null){
									usageReportAllMileageAndFuleconsModel.setTotalFuelcons(df.format((double)Double.valueOf(usageReportAllMileageAndFuleconsModel.getTotalMileage())*vehicleModel.getTheoreticalFuelCon()/100));
								}else{
									usageReportAllMileageAndFuleconsModel.setTotalFuelcons("0.0");
								}
							}
						}
					}
				}
			}else{
				usageReportAllMileageAndFuleconsModel.setTotalMileage("0");
				usageReportAllMileageAndFuleconsModel.setTotalFuelcons("0");
			}
		}
		return usageReportAllMileageAndFuleconsModel;
	}
}
