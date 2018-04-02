package com.cmdt.carrental.mobile.gateway.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.cache.RedisService;
import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.AlertStatSQLModel;
import com.cmdt.carrental.common.model.UsageReportAppColumnarItemModel;
import com.cmdt.carrental.common.model.UsageReportAppColumnarModel;
//import com.cmdt.carrental.common.model.AlertStatSQLModel;
import com.cmdt.carrental.common.model.UsageReportAppLineItemModel;
import com.cmdt.carrental.common.model.UsageReportAppLineModel;
import com.cmdt.carrental.common.model.VehicleLocationModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleStatus;
import com.cmdt.carrental.common.service.BusiOrderService;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.UsageReportService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.service.VehicleAlertService;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.mobile.gateway.model.DateDataModel;
import com.cmdt.carrental.mobile.gateway.model.DeptDataModel;
import com.cmdt.carrental.mobile.gateway.model.VehicleStatModel;
import com.cmdt.carrental.mobile.gateway.model.request.stat.DaysStatDto;
import com.cmdt.carrental.mobile.gateway.model.request.usage.UsageDepartmentReportDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BusiStatisticsService {

	private static final Logger LOG = LoggerFactory.getLogger(BusiStatisticsService.class);
	
	private static final String VEHICLE_PREFIX="VEHICLE_";
    private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private VehicleAlertService vehicleAlertService;
	
	@Autowired
    private RedisService redisService;
	
	@Autowired
    private BusiOrderService orderService;
	
	@Autowired
	private UsageReportAppService usageReportService;
	
	@Autowired
	private UsageReportService reportService;
	
	@Autowired
	private OrganizationService organizationService;
	
	
	public List<DateDataModel> load7DaysStat(DaysStatDto data) throws Exception {
		LOG.debug("BusiStatisticsService.load7DaysStat("+data+")");
		List<DateDataModel> modelList = new ArrayList<DateDataModel>();
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(data.getStartDate());
		Date startDate = c.getTime();
		c.setTimeInMillis(data.getEndDate());
		Date endDate = c.getTime();
//		LOG.info("startDate-"+ startDate);
//		LOG.info("endDate-"+ endDate);
		if(Constants.USAGE_TYPE_MILE.equals(data.getDataType()) ||  Constants.USAGE_TYPE_FUEL.equals(data.getDataType())){
			UsageDepartmentReportDto dto = new UsageDepartmentReportDto();
			dto.setOrgId(data.getOrgId());
			dto.setQueryType(data.getDataType()); //mile:里程 fuel:油耗
			dto.setStartTime(DateUtils.date2String(startDate,"yyyy-MM-dd"));
			dto.setEndTime(DateUtils.date2String(endDate,"yyyy-MM-dd"));
			dto.setSelfDept(data.getSelfDept());
			dto.setChildDept(data.getChildDept());
			UsageReportAppLineModel model = usageReportService.getTendencyChartByDayRange(dto);
			
			List<UsageReportAppLineItemModel> reportModels = model.getDataList();
			if(reportModels != null && reportModels.size() > 0){
				for(UsageReportAppLineItemModel item : reportModels){
					DateDataModel dateModel = new DateDataModel();
					dateModel.setData(item.getData());
					dateModel.setDate(item.getDay());
					modelList.add(dateModel);
				}
			}
		}else{
			List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(data.getOrgId(), data.getSelfDept(), data.getChildDept());
			if(orgList!=null&&orgList.size()>0){
				List<AlertStatSQLModel> alertSqlModels = vehicleAlertService.getDayAlert(orgList, startDate, endDate,data.getOrgId(),data.getSelfDept());
				if(alertSqlModels != null && alertSqlModels.size() > 0){
					for(AlertStatSQLModel item : alertSqlModels){
						DateDataModel dateModel = new DateDataModel();
						dateModel.setData(item.getCt().toString());
						dateModel.setDate(item.getTm());
						modelList.add(dateModel);
					}
				}
			}
		}
		return modelList;
		
	}
	
	public List<DeptDataModel> loadTop3DStat(DaysStatDto data) throws Exception {
		LOG.info("BusiStatisticsService.loadTop3DStat("+data+")");
		List<DeptDataModel> modelList = new ArrayList<DeptDataModel>();
			
		if(Constants.USAGE_TYPE_MILE.equals(data.getDataType()) ||  Constants.USAGE_TYPE_FUEL.equals(data.getDataType())){
			UsageDepartmentReportDto dto = new UsageDepartmentReportDto();
			dto.setOrgId(data.getOrgId());
			dto.setQueryType(data.getDataType()); //mile:里程 fuel:油耗
			dto.setSelfDept(data.getSelfDept());
			dto.setChildDept(data.getChildDept());
			
			Calendar c = Calendar.getInstance();
			if(data.getStartDate() != null && data.getEndDate() != null){
				c.setTimeInMillis(data.getStartDate());
				Date startDate = c.getTime();
				c.setTimeInMillis(data.getEndDate());
				Date endDate = c.getTime();
				dto.setStartTime(DateUtils.date2String(startDate,DateUtils.FORMAT_YYYY_MM_DD));
				dto.setEndTime(DateUtils.date2String(endDate,DateUtils.FORMAT_YYYY_MM_DD));
			}else{
				Date startDate = c.getTime();
				dto.setStartTime(DateUtils.date2String(startDate,DateUtils.FORMAT_YYYY_MM_DD));
				dto.setEndTime(DateUtils.date2String(startDate,DateUtils.FORMAT_YYYY_MM_DD));
			}
			
			UsageReportAppColumnarModel model = usageReportService.getDepartmentColumnarChartByDayRange(dto);
			List<UsageReportAppColumnarItemModel> reportModels = model.getDataList();
			if(reportModels != null && reportModels.size() > 0){
				//根据Collections.sort重载方法来实现  
		        Collections.sort(reportModels,new Comparator<UsageReportAppColumnarItemModel>(){  
		            @Override  
		            public int compare(UsageReportAppColumnarItemModel b1, UsageReportAppColumnarItemModel b2) { 
		            	if(b1.getData() != null && !"".equals(b1.getData()) 
		            			&& b1.getData() != null && !"".equals(b1.getData())){
		            		Double bd1 = Double.parseDouble(b1.getData());
		            		Double bd2 = Double.parseDouble(b2.getData());
		            		if(bd1 < bd2){
		            			return 1;
		            		}else if(bd1 > bd2){
		            			return -1;
		            		}
		            	}	
		            	return 0;  
		            }  
		              
		        });  
				
				for(int i = 0 ; i < reportModels.size(); i++){
					if(i == 3){
						break;
					}
					UsageReportAppColumnarItemModel item = reportModels.get(i);
					DeptDataModel dateModel = new DeptDataModel();
					dateModel.setData(item.getData());
					dateModel.setDept(item.getDeptName());
					modelList.add(dateModel);
					
				}
			}
		}else{
			String starttime = "";
			String endtime = "";
			Calendar c = Calendar.getInstance();
			if(data.getStartDate() != null && data.getEndDate() != null){
				c.setTimeInMillis(data.getStartDate());
				Date startDate = c.getTime();
				c.setTimeInMillis(data.getEndDate());
				Date endDate = c.getTime();
				starttime = DateUtils.date2String(startDate,DateUtils.FORMAT_YYYY_MM_DD);
				endtime = DateUtils.date2String(endDate,DateUtils.FORMAT_YYYY_MM_DD);
			}
			
			if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
				//如果不输入时间，取最近一个月时间作为开始和结束时间
				Date startDate = new Date();
		    	starttime = DateUtils.date2String(startDate,DateUtils.FORMAT_YYYY_MM_DD);
		    	endtime = DateUtils.date2String(startDate,DateUtils.FORMAT_YYYY_MM_DD);
			}
			
			//按输入日期区间进行统计
			Date startDate = DateUtils.string2Date(starttime + " "+DateUtils.FORMAT_TIME_HH_MI_SS_START,DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
			Date endDate = DateUtils.string2Date(endtime + " "+DateUtils.FORMAT_TIME_HH_MI_SS_END,DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
			
			//构件树必须要根节点
			List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(data.getOrgId(), true , data.getChildDept());
			if(orgList!=null&&orgList.size()>0){
				AlertStatSQLModel tree = vehicleAlertService.getDeptAlert(data.getOrgId(),orgList, startDate, endDate,data.getSelfDept());
				
				//获取根节点和次级节点，并组装成List
				List<AlertStatSQLModel> alertSqlModels = new ArrayList<>();
				for(AlertStatSQLModel child :tree.getChildren()){
					alertSqlModels.add(new AlertStatSQLModel(child.getTm(),child.getcountToal()));
				}
				//根据self参数判断是都添加根节点
				if(data.getSelfDept()){
					alertSqlModels.add(new AlertStatSQLModel(tree.getTm(),tree.getcountToal()));
				}
				  
				 if(alertSqlModels != null && alertSqlModels.size() > 0){
					
					//根据Collections.sort重载方法来实现  
			        Collections.sort(alertSqlModels,new Comparator<AlertStatSQLModel>(){  
			            @Override  
			            public int compare(AlertStatSQLModel b1, AlertStatSQLModel b2) { 
			            	return b2.getCt().compareTo(b1.getCt());
			            }    
			        });  
	
					for(int i = 0 ; i< alertSqlModels.size(); i++){
						if(i == 3){
							break;
						}
						AlertStatSQLModel item = alertSqlModels.get(i);
						DeptDataModel dateModel = new DeptDataModel();
						dateModel.setData(item.getCt().toString());
						dateModel.setDept(item.getTm());
						modelList.add(dateModel);
					}
				}
			}
		}
				
		return modelList;
		
	}
	
	
	/**
	 * 
	 * @param userId
	 * @return
	 * @throws Exception 
	 */
	public VehicleStatModel loadVehicleStat(String userId) throws Exception {
		LOG.debug("BusiStatisticsService.loadVehicleStat("+userId+")");
		VehicleStatModel statModel = new VehicleStatModel();
		try{
		if(userId != null && !"".equals(userId)){
    		User user = userService.findById(Long.parseLong(userId));
    		Long orgId = user.getOrganizationId();
    		
    		//init vehs count
    		int totalVehs = 0;
        	int onlineVehs = 0;
        	int onTaskVehs = 0;
        	int idleVehs = 0;
    		
			List<VehicleModel> vehicleList = reportService.findAllVehicleListByOrgId(orgId);
			
			if(vehicleList != null && !vehicleList.isEmpty()){
				//set total quantity
				totalVehs = vehicleList.size();
				
				for(VehicleModel vehicleModel : vehicleList){
    				//从redis中取obd数据
    				String obdRedisData = redisService.get(VEHICLE_PREFIX+vehicleModel.getDeviceNumber());
    				if(StringUtils.isNotEmpty(obdRedisData)){
    					VehicleLocationModel vehicleLocationModel = MAPPER.readValue(obdRedisData, VehicleLocationModel.class);
    					vehicleLocationModel.setId(vehicleModel.getId());
    					//过滤掉经纬度为0.0的值
    					if(vehicleLocationModel.getLatitude()!= 0.0 && vehicleLocationModel.getLongitude() != 0.0){
    						onlineVehs++;
    					}
    				}
    				
    				//确认车辆订单信息
    				if(orderService.checkOnTask(vehicleModel.getId())){
    					onTaskVehs++;
    				}
    			};
				
				//set idle quantity
				idleVehs = totalVehs - onTaskVehs;
			}
			
    		//set vehs quantity
			statModel.setOwnCar(String.valueOf(totalVehs));
			statModel.setTotalCar(String.valueOf(totalVehs));
			statModel.setOnlineCar(String.valueOf(onlineVehs));
			statModel.setDrivingCar(String.valueOf(onTaskVehs));
			statModel.setStopCar(String.valueOf(idleVehs));
		}
    	}catch(Exception e){
    		throw e;
    	}
		
    	return statModel;
	}
	
	
	
	/**
	 * 首页统计车辆状态(redis调用)
	 * “车辆总数”、“在线”、“行驶”、“停止”、“离线”
	 * 
	 * 在线=车辆总数-离线；
	      在线=行驶+停止；
              车辆最近一条数据与当前时间超过10分钟的，状态为离线；
	 * @return
	 */
	public Map<String, Object> findVehicleStatus(String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		
		int totalNumber = 0; //车辆总数
		int onLineNumber = 0;//在线
		int drivingNumber = 0;//行驶
		int stopNumber = 0;//停止
		int offLineNumber = 0;//离线
		
		try {
			VehicleStatus model = new VehicleStatus();
			List<VehicleModel> vehicleList = null;
			
			if(userId != null && !"".equals(userId)){
	    		User loginUser = userService.findById(Long.parseLong(userId));
			
				Long orgId = loginUser.getOrganizationId();
				vehicleList = reportService.findAllVehicleListByOrgId(orgId);
				
				if(vehicleList != null && vehicleList.size() > 0){
					
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
				
				map.put("status", "success");
				map.put("data", model);
			}
		} catch (Exception e) {
			LOG.error("VehicleController.findVehicleStatus", e);
			map.put("status", "failure");
		}
		return map;
	}
}
