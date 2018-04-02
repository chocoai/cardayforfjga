package com.cmdt.carrental.portal.web.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Controller
@RequestMapping("/usage/report")
public class UsageReportController extends BaseController{
	private static final Logger LOG = LoggerFactory.getLogger(UsageReportController.class);
	
	@Autowired
	UsageReportService usageReportService;
	
	@Autowired
	private ShouqiService shouqiService;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private VehicleService vehicleService;
	
	private static final String VEHICLE_PREFIX = "VEHICLE_";
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	private static DecimalFormat df = new DecimalFormat("0.00");

	/**
	 * 车辆使用统计(饼图以及柱状图,企业管理员查看)
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/getPieAndColumnarDataByDayRange", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> getPieAndColumnarDataByDayRange(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		String starttime = "";
    		String endtime = "";
    		if(jsonMap.get("starttime") != null){
    			starttime = String.valueOf(jsonMap.get("starttime"));
    		}
    		if(jsonMap.get("endtime") != null){
    			endtime = String.valueOf(jsonMap.get("endtime"));
    		}
    		
    		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
    			//如果不输入时间，取最近一个月时间作为开始和结束时间
    			Date endDate = new Date();
    	    	Date startDate = DateUtils.addDays(endDate,-29);
    	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
    	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
    		}
    		
    		//按输入日期区间进行统计
    		Date startDate = DateUtils.string2Date(starttime + " 00:00:00","yyyy-MM-dd HH:mm:ss");
    		Date endDate = DateUtils.string2Date(endtime + " 23:59:59","yyyy-MM-dd HH:mm:ss");
    		
    		//企业管理员
    		if(loginUser.isEntAdmin()){
				UsageReportModel usageReportModel = usageReportService.getPieAndColumnarDataByDayRange(loginUser.getOrganizationId(),startDate,endDate);
    			if(usageReportModel != null){
    				map.put("data", usageReportModel);
    			}
    		}
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("UsageReportController.getPieAndColumnarDataByDayRange",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 车辆使用统计(饼图以及柱状图 new,old:getPieAndColumnarDataByDayRange)
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/getPieAndColumnarData", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> getPieAndColumnarData(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		String starttime = "";
    		String endtime = "";
    		Long orgId = 0l;
    		Boolean selfDept = true;
    		Boolean childDept = true;
    		
    		if(jsonMap.get("starttime") != null){
    			starttime = String.valueOf(jsonMap.get("starttime"));
    		}
    		if(jsonMap.get("endtime") != null){
    			endtime = String.valueOf(jsonMap.get("endtime"));
    		}
    		if(jsonMap.get("orgId") != null){
    			orgId = Long.valueOf(String.valueOf(jsonMap.get("orgId")));
    		}
    		if(jsonMap.get("selfDept") != null){
    			selfDept = Boolean.valueOf(String.valueOf(jsonMap.get("selfDept")));
    		}
    		if(jsonMap.get("childDept") != null){
    			childDept = Boolean.valueOf(String.valueOf(jsonMap.get("childDept")));
    		}
    		
    		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
    			//如果不输入时间，取最近一个月时间作为开始和结束时间
    			Date endDate = new Date();
    	    	Date startDate = DateUtils.addDays(endDate,-29);
    	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
    	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
    		}
    		
    		//按输入日期区间进行统计
    		Date startDate = DateUtils.string2Date(starttime + " 00:00:00","yyyy-MM-dd HH:mm:ss");
    		Date endDate = DateUtils.string2Date(endtime + " 23:59:59","yyyy-MM-dd HH:mm:ss");
    		
			UsageReportModel usageReportModel = usageReportService.getPieAndColumnarData(orgId,selfDept,childDept,startDate,endDate);
			if(usageReportModel != null){
				map.put("data", usageReportModel);
			}
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("UsageReportController.getPieAndColumnarDataByDayRange",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 车辆使用统计(每辆车的总里程，总油耗，总行驶时长以及平均值,支持分页,企业管理员,部门管理员查看)
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/getVehiclePropertyDataByDayRange", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> getVehiclePropertyDataByDayRange(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		PagModel pagModel= new PagModel();
    		
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		String starttime = "";
    		String endtime = "";
    		if(jsonMap.get("starttime") != null){
    			starttime = String.valueOf(jsonMap.get("starttime"));
    		}
    		if(jsonMap.get("endtime") != null){
    			endtime = String.valueOf(jsonMap.get("endtime"));
    		}
    		
    		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
    			//如果不输入时间，取最近一个月时间作为开始和结束时间
    			Date endDate = new Date();
    	    	Date startDate = DateUtils.addDays(endDate,-29);
    	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
    	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
    		}
    		
    		//按输入日期区间进行统计
    		Date startDate = DateUtils.string2Date(starttime + " 00:00:00","yyyy-MM-dd HH:mm:ss");
    		Date endDate = DateUtils.string2Date(endtime + " 23:59:59","yyyy-MM-dd HH:mm:ss");
    		
    		//部门id
    		long deptId = 0l;
    		if(jsonMap.get("deptId") != null){
    			if(!"-1".equals(String.valueOf(jsonMap.get("deptId")))){//选择了部门
    				deptId = Long.valueOf(String.valueOf(jsonMap.get("deptId")));
    			}
    		}
    		
    		int currentPage = 1;
        	int numPerPage = 10;
    		
    		if(jsonMap.get("currentPage") != null){
	    		currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
	    	}
	    	if(jsonMap.get("numPerPage") != null){
	    		numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
	    	}
    		
    		//企业管理员
    		if(loginUser.isEntAdmin()){
    			long entId = loginUser.getOrganizationId().longValue();
    			pagModel = usageReportService.getVehiclePropertyDataByDayRangeAndEntAdmin(startDate,endDate,entId,deptId,currentPage,numPerPage);
    		}
    		
    		//部门管理员
    		if(loginUser.isDeptAdmin()){
    			//部门id
        		deptId = loginUser.getOrganizationId().longValue();
        		pagModel = usageReportService.getVehiclePropertyDataByDayRangeAndEntAdmin(startDate,endDate,0l,deptId,currentPage,numPerPage);
    		}
    		map.put("data", pagModel);
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("UsageReportController.getVehiclePropertyDataByDayRange",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 车辆使用统计(每辆车的总里程，总油耗，总行驶时长以及平均值,支持分页,企业管理员,部门管理员查看) new old:getVehiclePropertyDataByDayRange
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/getVehiclePropertyData", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> getVehiclePropertyData(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		String starttime = "";
    		String endtime = "";
    		Long orgId = 0l;
    		Boolean selfDept = true;
    		Boolean childDept = true;
    		int currentPage = 1;
        	int numPerPage = 10;
    		
    		if(jsonMap.get("starttime") != null){
    			starttime = String.valueOf(jsonMap.get("starttime"));
    		}
    		if(jsonMap.get("endtime") != null){
    			endtime = String.valueOf(jsonMap.get("endtime"));
    		}
    		if(jsonMap.get("orgId") != null){
    			orgId = Long.valueOf(String.valueOf(jsonMap.get("orgId")));
    		}
    		if(jsonMap.get("selfDept") != null){
    			selfDept = Boolean.valueOf(String.valueOf(jsonMap.get("selfDept")));
    		}
    		if(jsonMap.get("childDept") != null){
    			childDept = Boolean.valueOf(String.valueOf(jsonMap.get("childDept")));
    		}
    		if(jsonMap.get("currentPage") != null){
	    		currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
	    	}
	    	if(jsonMap.get("numPerPage") != null){
	    		numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
	    	}
    		
    		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
    			//如果不输入时间，取最近一个月时间作为开始和结束时间
    			Date endDate = new Date();
    	    	Date startDate = DateUtils.addDays(endDate,-29);
    	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
    	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
    		}
    		
    		//按输入日期区间进行统计
    		Date startDate = DateUtils.string2Date(starttime + " 00:00:00","yyyy-MM-dd HH:mm:ss");
    		Date endDate = DateUtils.string2Date(endtime + " 23:59:59","yyyy-MM-dd HH:mm:ss");
    		
    		PagModel pagModel = usageReportService.getVehiclePropertyData(startDate,endDate,orgId,selfDept,childDept,currentPage,numPerPage);
    		
    		map.put("data", pagModel);
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("UsageReportController.getVehiclePropertyDataByDayRange",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
    /**
	 * 车辆使用统计(每辆车的总里程，总油耗，总行驶时长以及平均值,支持分页,企业管理员,部门管理员查看)(导出)
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/exportVehiclePropertyDataByDayRange", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> exportVehiclePropertyDataByDayRange(@CurrentUser User loginUser,String json,HttpServletRequest request,HttpServletResponse response) {
    	List<String> retList = new ArrayList<String>();
    	
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		String starttime = "";
    		String endtime = "";
    		if(jsonMap.get("starttime") != null){
    			starttime = String.valueOf(jsonMap.get("starttime"));
    		}
    		if(jsonMap.get("endtime") != null){
    			endtime = String.valueOf(jsonMap.get("endtime"));
    		}
    		
    		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
    			//如果不输入时间，取最近一个月时间作为开始和结束时间
    			Date endDate = new Date();
    	    	Date startDate = DateUtils.addDays(endDate,-29);
    	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
    	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
    		}
    		
    		//按输入日期区间进行统计
    		Date startDate = DateUtils.string2Date(starttime + " 00:00:00","yyyy-MM-dd HH:mm:ss");
    		Date endDate = DateUtils.string2Date(endtime + " 23:59:59","yyyy-MM-dd HH:mm:ss");
    		
    		//部门id
    		long deptId = 0l;
    		if(jsonMap.get("deptId") != null){
    			if(!"-1".equals(String.valueOf(jsonMap.get("deptId")))){//选择了部门
    				deptId = Long.valueOf(String.valueOf(jsonMap.get("deptId")));
    			}
    		}
    		
    		//企业管理员
    		if(loginUser.isEntAdmin()){
    			long entId = loginUser.getOrganizationId().longValue();
    			retList = usageReportService.getAllVehiclePropertyDataByDayRange(startDate,endDate,entId,deptId);
    		}
    		
    		//部门管理员
    		if(loginUser.isDeptAdmin()){
    			//部门id
        		deptId = loginUser.getOrganizationId().longValue();
        		retList = usageReportService.getAllVehiclePropertyDataByDayRange(startDate,endDate,0l,deptId);
    		}
    		map.put("status", "success");
			map.put("data", retList);
			map.put("filename", "车辆使用统计.xls");
    		map.put("sheet", "车辆使用统计列表");
    		map.put("header", "序号,车牌号,车辆品牌,排量,燃油号,部门,总里程(千米),总耗油量(升),总行驶时长(小时),使用率,平均里程(千米/天),平均油耗量(升/天),平均行驶时长(小时/天)");
    		CsvUtil.exportExcel(map,request,response);
    	}catch(Exception e){
    		LOG.error("UsageReportController.exportVehiclePropertyDataByDayRange",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
    /**
	 * 车辆使用统计(每辆车的总里程，总油耗，总行驶时长以及平均值,支持分页,企业管理员,部门管理员查看)(导出)  new old:exportVehiclePropertyDataByDayRange
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/exportVehiclePropertyData", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> exportVehiclePropertyData(@CurrentUser User loginUser,String json,HttpServletRequest request,HttpServletResponse response) {
    	List<String> retList = new ArrayList<String>();
    	
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		String starttime = "";
    		String endtime = "";
    		Long orgId = 0l;
    		Boolean selfDept = true;
    		Boolean childDept = true;
    		
    		if(jsonMap.get("starttime") != null){
    			starttime = String.valueOf(jsonMap.get("starttime"));
    		}
    		if(jsonMap.get("endtime") != null){
    			endtime = String.valueOf(jsonMap.get("endtime"));
    		}
    		if(jsonMap.get("orgId") != null){
    			orgId = Long.valueOf(String.valueOf(jsonMap.get("orgId")));
    		}
    		if(jsonMap.get("selfDept") != null){
    			selfDept = Boolean.valueOf(String.valueOf(jsonMap.get("selfDept")));
    		}
    		if(jsonMap.get("childDept") != null){
    			childDept = Boolean.valueOf(String.valueOf(jsonMap.get("childDept")));
    		}
    		
    		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
    			//如果不输入时间，取最近一个月时间作为开始和结束时间
    			Date endDate = new Date();
    	    	Date startDate = DateUtils.addDays(endDate,-29);
    	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
    	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
    		}
    		
    		//按输入日期区间进行统计
    		Date startDate = DateUtils.string2Date(starttime + " 00:00:00","yyyy-MM-dd HH:mm:ss");
    		Date endDate = DateUtils.string2Date(endtime + " 23:59:59","yyyy-MM-dd HH:mm:ss");
    		
    		
        	retList = usageReportService.getAllVehiclePropertyData(startDate,endDate,orgId,selfDept,childDept);
        	
    		map.put("status", "success");
			map.put("data", retList);
			map.put("filename", "车辆使用统计.xls");
    		map.put("sheet", "车辆使用统计列表");
    		map.put("header", "序号,车牌号,车辆品牌,排量,燃油号,部门,总里程(千米),总耗油量(升),总行驶时长(小时),使用率,平均里程(千米/天),平均油耗量(升/天),平均行驶时长(小时/天)");
    		CsvUtil.exportExcel(map,request,response);
    	}catch(Exception e){
    		LOG.error("UsageReportController.exportVehiclePropertyDataByDayRange",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 车辆使用统计(曲线图,企业管理员,部门管理员查看查看)
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/getVehicleLinePropertyDataByDayRange", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> getVehicleLinePropertyDataByDayRange(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		UsageReportLineModel usageReportLineModel = new UsageReportLineModel();
    		
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		String starttime = "";
    		String endtime = "";
    		if(jsonMap.get("starttime") != null){
    			starttime = String.valueOf(jsonMap.get("starttime"));
    		}
    		if(jsonMap.get("endtime") != null){
    			endtime = String.valueOf(jsonMap.get("endtime"));
    		}
    		
    		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
    			//如果不输入时间，取最近一个月时间作为开始和结束时间
    			Date endDate = new Date();
    	    	Date startDate = DateUtils.addDays(endDate,-29);
    	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
    	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
    		}
    		
    		//按输入日期区间进行统计
    		Date startDate = DateUtils.string2Date(starttime + " 00:00:00","yyyy-MM-dd HH:mm:ss");
    		Date endDate = DateUtils.string2Date(endtime + " 23:59:59","yyyy-MM-dd HH:mm:ss");
    		
    		//部门id
    		long deptId = 0l;
    		if(jsonMap.get("deptId") != null){
    			if(!"-1".equals(String.valueOf(jsonMap.get("deptId")))){//选择了部门
    				deptId = Long.valueOf(String.valueOf(jsonMap.get("deptId")));
    			}
    		}
    		
    		//企业管理员
    		if(loginUser.isEntAdmin()){
    			long entId = loginUser.getOrganizationId().longValue();
    			usageReportLineModel = usageReportService.getVehicleLinePropertyDataByDayRange(startDate,endDate,entId,deptId);
    		}
    		
    		//部门管理员
    		if(loginUser.isDeptAdmin()){
    			//部门id
        		deptId = loginUser.getOrganizationId().longValue();
        		usageReportLineModel = usageReportService.getVehicleLinePropertyDataByDayRange(startDate,endDate,0l,deptId);
    		}
    		map.put("data", usageReportLineModel);
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("UsageReportController.getVehicleLinePropertyDataByDayRange",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 车辆使用统计(曲线图,企业管理员,部门管理员查看查看) new old:getVehicleLinePropertyDataByDayRange
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/getVehicleLinePropertyData", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> getVehicleLinePropertyData(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		String starttime = "";
    		String endtime = "";
    		Long orgId = 0l;
    		Boolean selfDept = true;
    		Boolean childDept = true;
    		
    		if(jsonMap.get("starttime") != null){
    			starttime = String.valueOf(jsonMap.get("starttime"));
    		}
    		if(jsonMap.get("endtime") != null){
    			endtime = String.valueOf(jsonMap.get("endtime"));
    		}
    		if(jsonMap.get("orgId") != null){
    			orgId = Long.valueOf(String.valueOf(jsonMap.get("orgId")));
    		}
    		if(jsonMap.get("selfDept") != null){
    			selfDept = Boolean.valueOf(String.valueOf(jsonMap.get("selfDept")));
    		}
    		if(jsonMap.get("childDept") != null){
    			childDept = Boolean.valueOf(String.valueOf(jsonMap.get("childDept")));
    		}
    		
    		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
    			//如果不输入时间，取最近一个月时间作为开始和结束时间
    			Date endDate = new Date();
    	    	Date startDate = DateUtils.addDays(endDate,-29);
    	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
    	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
    		}
    		
    		//按输入日期区间进行统计
    		Date startDate = DateUtils.string2Date(starttime + " 00:00:00","yyyy-MM-dd HH:mm:ss");
    		Date endDate = DateUtils.string2Date(endtime + " 23:59:59","yyyy-MM-dd HH:mm:ss");
    		
    		UsageReportLineModel usageReportLineModel = usageReportService.getVehicleLinePropertyData(startDate,endDate,orgId,selfDept,childDept);
    		
    		map.put("data", usageReportLineModel);
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("UsageReportController.getVehicleLinePropertyDataByDayRange",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 车辆使用统计(查询每辆车当日，昨日，本周，本月里程与油耗,以及总里程，总油耗)
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/getVehicleAllMileageAndFuleconsList", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> getVehicleAllMileageAndFuleconsList(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		UsageReportAllMileageAndFuleconsModel usageReportAllMileageAndFuleconsModel = new UsageReportAllMileageAndFuleconsModel();
    		
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		String vehicleNumber = String.valueOf(jsonMap.get("vehicleNumber"));
    		String deviceNumber = String.valueOf(jsonMap.get("deviceNumber"));
    		
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
    		map.put("data", usageReportAllMileageAndFuleconsModel);
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("UsageReportController.getVehicleAllMileageAndFuleconsList",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 车辆使用统计(查询每辆车当日，昨日，本周，本月里程与油耗,以及总里程，总油耗)仅通过车牌号查询
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/getVehicleAllMileageAndFuleconsListByVehNum", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> getVehicleAllMileageAndFuleconsListByVehNum(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		UsageReportAllMileageAndFuleconsModel usageReportAllMileageAndFuleconsModel = new UsageReportAllMileageAndFuleconsModel();
    		
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		String vehicleNumber = String.valueOf(jsonMap.get("vehicleNumber"));
    		
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
    		map.put("data", usageReportAllMileageAndFuleconsModel);
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("UsageReportController.getVehicleAllMileageAndFuleconsList",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 未使用车辆统计(车的公里数在查询日期内出现公里数增量为0的情况则为未使用,企业管理员查看)
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/getIdleVehicleListByDayRange", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> getIdleVehicleListByDayRange(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		PagModel pagModel= new PagModel();
    		
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		String starttime = "";
    		String endtime = "";
    		if(jsonMap.get("starttime") != null){
    			starttime = String.valueOf(jsonMap.get("starttime"));
    		}
    		if(jsonMap.get("endtime") != null){
    			endtime = String.valueOf(jsonMap.get("endtime"));
    		}
    		
    		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
    			//如果不输入时间，取最近一个月时间作为开始和结束时间
    			Date endDate = new Date();
    	    	Date startDate = DateUtils.addDays(endDate,-29);
    	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
    	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
    		}
    		
    		//按输入日期区间进行统计
    		Date startDate = DateUtils.string2Date(starttime + " 00:00:00","yyyy-MM-dd HH:mm:ss");
    		Date endDate = DateUtils.string2Date(endtime + " 23:59:59","yyyy-MM-dd HH:mm:ss");
    		
    		//部门id
    		long deptId = 0l;
    		if(jsonMap.get("deptId") != null){
    			if(!"-1".equals(String.valueOf(jsonMap.get("deptId")))){//选择了部门
    				deptId = Long.valueOf(String.valueOf(jsonMap.get("deptId")));
    			}
    		}
    		
    		int currentPage = 1;
        	int numPerPage = 10;
    		
    		if(jsonMap.get("currentPage") != null){
	    		currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
	    	}
	    	if(jsonMap.get("numPerPage") != null){
	    		numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
	    	}
    		
    		//企业管理员
    		if(loginUser.isEntAdmin()){
    			long entId = loginUser.getOrganizationId().longValue();
    			pagModel = usageReportService.getIdleVehicleListByDayRange(startDate,endDate,entId,deptId,currentPage,numPerPage);
    		}
    		
    		//部门管理员
    		if(loginUser.isDeptAdmin()){
    			//部门id
        		deptId = loginUser.getOrganizationId().longValue();
        		pagModel = usageReportService.getIdleVehicleListByDayRange(startDate,endDate,0l,deptId,currentPage,numPerPage);
    		}
    		map.put("data", pagModel);
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("UsageReportController.getIdleVehicleListByDayRange",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 未使用车辆统计(车的公里数在查询日期内出现公里数增量为0的情况则为未使用) new old:getIdleVehicleListByDayRange
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/getIdleVehicleList", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> getIdleVehicleList(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		String starttime = "";
    		String endtime = "";
    		Long orgId = 0l;
    		Boolean selfDept = true;
    		Boolean childDept = true;
    		int currentPage = 1;
        	int numPerPage = 10;
        	
        	if(jsonMap.get("starttime") != null){
    			starttime = String.valueOf(jsonMap.get("starttime"));
    		}
    		if(jsonMap.get("endtime") != null){
    			endtime = String.valueOf(jsonMap.get("endtime"));
    		}
    		if(jsonMap.get("orgId") != null){
    			orgId = Long.valueOf(String.valueOf(jsonMap.get("orgId")));
    		}
    		if(jsonMap.get("selfDept") != null){
    			selfDept = Boolean.valueOf(String.valueOf(jsonMap.get("selfDept")));
    		}
    		if(jsonMap.get("childDept") != null){
    			childDept = Boolean.valueOf(String.valueOf(jsonMap.get("childDept")));
    		}
    		if(jsonMap.get("currentPage") != null){
	    		currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
	    	}
	    	if(jsonMap.get("numPerPage") != null){
	    		numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
	    	}
    		
    		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
    			//如果不输入时间，取最近一个月时间作为开始和结束时间
    			Date endDate = new Date();
    	    	Date startDate = DateUtils.addDays(endDate,-29);
    	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
    	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
    		}
    		
    		//按输入日期区间进行统计
    		Date startDate = DateUtils.string2Date(starttime + " 00:00:00","yyyy-MM-dd HH:mm:ss");
    		Date endDate = DateUtils.string2Date(endtime + " 23:59:59","yyyy-MM-dd HH:mm:ss");
    		
    		PagModel pagModel = usageReportService.getIdleVehicleList(startDate,endDate,orgId,selfDept,childDept,currentPage,numPerPage);
    		map.put("data", pagModel);
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("UsageReportController.getIdleVehicleListByDayRange",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 未使用车辆统计,导出所有的车辆到EXCEL(车的公里数在查询日期内出现公里数增量为0的情况则为未使用)
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/exportIdleVehicleListByDayRange", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public void exportIdleVehicleListByDayRange(@CurrentUser User loginUser,String json,HttpServletRequest request,HttpServletResponse response) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{    		
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		String starttime = "";
    		String endtime = "";
    		if(jsonMap.get("starttime") != null){
    			starttime = String.valueOf(jsonMap.get("starttime"));
    		}
    		if(jsonMap.get("endtime") != null){
    			endtime = String.valueOf(jsonMap.get("endtime"));
    		}
    		
    		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
    			//如果不输入时间，取最近一个月时间作为开始和结束时间
    			Date endDate = new Date();
    	    	Date startDate = DateUtils.addDays(endDate,-29);
    	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
    	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
    		}
    		
    		//按输入日期区间进行统计
    		Date startDate = DateUtils.string2Date(starttime + " 00:00:00","yyyy-MM-dd HH:mm:ss");
    		Date endDate = DateUtils.string2Date(endtime + " 23:59:59","yyyy-MM-dd HH:mm:ss");
    		
    		//部门id
    		long deptId = 0l;
    		if(jsonMap.get("deptId") != null){
    			if(!"-1".equals(String.valueOf(jsonMap.get("deptId")))){//选择了部门
    				deptId = Long.valueOf(String.valueOf(jsonMap.get("deptId")));
    			}
    		}
    		
    		List<String> usageReportExport = null;
    		//企业管理员
    		if(loginUser.isEntAdmin()){
    			long entId = loginUser.getOrganizationId().longValue();
    			usageReportExport =  usageReportService.getIdleVehicleListByDayRangeALL(startDate,endDate,entId,deptId);
    		}
    		
    		//部门管理员
    		if(loginUser.isDeptAdmin()){
    			//部门id
        		deptId = loginUser.getOrganizationId().longValue();
        		usageReportExport =  usageReportService.getIdleVehicleListByDayRangeALL(startDate,endDate,0l,deptId);
    		}
    		
    		map.put("filename", "未用车辆列表"+starttime+"至"+endtime+".xls");
    		map.put("sheet", "未用车辆列表");
    		map.put("header", "车牌号,车辆品牌,车辆型号,所属部门,车辆用途,空闲时间段");
			
    		map.put("data", usageReportExport);
	    	map.put("status", "success");
	    	CsvUtil.exportExcel(map,request,response);
    	}catch(Exception e){
    		LOG.error("UsageReportController.exportIdleVehicleListByDayRange",e);
   		   map.put("status", "failure");
   	   }
    }
    
	/**
	 * 未使用车辆统计,导出所有的车辆到EXCEL(车的公里数在查询日期内出现公里数增量为0的情况则为未使用) new old:exportIdleVehicleListByDayRange
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/exportIdleVehicleList", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public void exportIdleVehicleList(@CurrentUser User loginUser,String json,HttpServletRequest request,HttpServletResponse response) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{    		
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		String starttime = "";
    		String endtime = "";
    		Long orgId = 0l;
    		Boolean selfDept = true;
    		Boolean childDept = true;
    		
    		if(jsonMap.get("starttime") != null){
    			starttime = String.valueOf(jsonMap.get("starttime"));
    		}
    		if(jsonMap.get("endtime") != null){
    			endtime = String.valueOf(jsonMap.get("endtime"));
    		}
    		if(jsonMap.get("orgId") != null){
    			orgId = Long.valueOf(String.valueOf(jsonMap.get("orgId")));
    		}
    		if(jsonMap.get("selfDept") != null){
    			selfDept = Boolean.valueOf(String.valueOf(jsonMap.get("selfDept")));
    		}
    		if(jsonMap.get("childDept") != null){
    			childDept = Boolean.valueOf(String.valueOf(jsonMap.get("childDept")));
    		}
    		
    		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
    			//如果不输入时间，取最近一个月时间作为开始和结束时间
    			Date endDate = new Date();
    	    	Date startDate = DateUtils.addDays(endDate,-29);
    	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
    	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
    		}
    		
    		//按输入日期区间进行统计
    		Date startDate = DateUtils.string2Date(starttime + " 00:00:00","yyyy-MM-dd HH:mm:ss");
    		Date endDate = DateUtils.string2Date(endtime + " 23:59:59","yyyy-MM-dd HH:mm:ss");
    		
    		List<String> usageReportExport =  usageReportService.getAllIdleVehicleList(startDate,endDate,orgId,selfDept,childDept);
    		
    		map.put("filename", "未用车辆列表"+starttime+"至"+endtime+".xls");
    		map.put("sheet", "未用车辆列表");
    		map.put("header", "车牌号,车辆品牌,车辆型号,所属部门,车辆用途,空闲时间段");
			
    		map.put("data", usageReportExport);
	    	map.put("status", "success");
	    	CsvUtil.exportExcel(map,request, response);
    	}catch(Exception e){
    		LOG.error("UsageReportController.exportIdleVehicleListByDayRange",e);
   		   map.put("status", "failure");
   	   }
    }
    
	/**
	 * 车辆行驶明细(企业管理员，部门管理员均可)
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/getDrivingDetailedReport", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> getDrivingDetailedReport(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	map.put("status", "success");
    	List<DrivingDetailedReportModel> retList = new ArrayList<DrivingDetailedReportModel>();
    	try{
    		if(StringUtils.isEmpty(json)){
    			map.put("status", "failure");
    			return map;
    		}
    		
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		//starttime 查询开始时间   endtime查询结束时间 
    		String starttime = "";
    		String endtime = "";
    		String vehicleNumber = "";
    		if(jsonMap.get("starttime") != null){
    			starttime = String.valueOf(jsonMap.get("starttime")) + " 00:00:00";
    		}else{
    			map.put("status", "failure");
    			return map;
    		}
    		
    		if(jsonMap.get("endtime") != null){
    			endtime = String.valueOf(jsonMap.get("endtime")) + " 23:59:59";
    		}else{
    			map.put("status", "failure");
    			return map;
    		}
    		
    		if(jsonMap.get("vehicleNumber") != null){
    			vehicleNumber = String.valueOf(jsonMap.get("vehicleNumber"));
    		}else{
    			map.put("status", "failure");
    			return map;
    		}
    			 
			VehicleModel vehicleModel = usageReportService.findVehicleListByVehicleNumber(vehicleNumber);
			if(vehicleModel != null){
				boolean privilegeFlag = usageReportService.getPrivilegeFlag(vehicleModel,loginUser.getOrganizationId());
				if(!privilegeFlag){
					map.put("status", "failure");
	    			return map;
				}
				
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
									map.put("data", retList);
								}
							}else{
								map.put("status", "failure");
							}
						}else{
							 map.put("status", "failure");
						}
					}else{
						map.put("status", "failure");
					}
				}else{
					map.put("status", "failure");
				}
			}else{
				 map.put("status", "failure");
			}
    	}catch(Exception e){
    		LOG.error("UsageReportController.getDrivingDetailedReport",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
    /**
     * 获得imei在realtime_data表中的ids
     */
    public Object[] getIdsByDeviceId(String imei,String starttime,String endtime){
    	Object[] ret = null;
		Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.FINDTRIPTRACEDATABYTIMERANGE, new Object[] { imei, starttime, endtime });
		if (result.get("data") != null) {
			JsonNode jsonNode;
			try {
				jsonNode = MAPPER.readTree(result.get("data").toString());
				if ("000".equals(jsonNode.get("status").asText())) {
					ArrayNode rows = (ArrayNode) jsonNode.get("result");
					if (rows != null && rows.size() > 0) {
						JsonNode obdNode = null;
						// 涉及到obd更换，只获得最新obd数据
						if (rows.size() == 1) {
							obdNode = rows.get(0);
						} else if (rows.size() == 2) {
							obdNode = rows.get(1);
						}
						
						String idlistTextVal = obdNode.get("idlist").asText();
						String[] idlistArr = null;
						if (StringUtils.isNotEmpty(idlistTextVal)) {
							idlistArr = idlistTextVal.replace("},{", "#").replace("{", "").replace("[", "").replace("]", "").replace("}", "").split("#");
							int idListArrLength = idlistArr.length;
							if (idListArrLength > 0) {
								ret = new Object[idListArrLength];
								for (int i = 0; i < idListArrLength; i++) {
									ret[i] = idlistArr[i];
								}
							}
						}
					}
				}
			} catch (Exception e) {
				LOG.error("UsageReportController.getIdsByDeviceId",e);
				e.printStackTrace();
			} 
		}
    	return ret;
    }
    
	/**
	 * 车辆行驶明细导出
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/exportDrivingDetailedReport", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> exportDrivingDetailedReport(@CurrentUser User loginUser,String json,HttpServletRequest request,HttpServletResponse response) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	map.put("status", "success");
    	List<String> retList = new ArrayList<String>();
    	try{
    		if(StringUtils.isEmpty(json)){
    			map.put("status", "failure");
    			return map;
    		}
    		
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		//starttime 查询开始时间   endtime查询结束时间 
    		String starttime = "";
    		String endtime = "";
    		String vehicleNumber = "";
    		
    		String jsonStartTime = String.valueOf(jsonMap.get("starttime"));
    		String jsonEndTime = String.valueOf(jsonMap.get("endtime"));
    		
    		if(jsonMap.get("starttime") != null){
    			starttime = jsonStartTime + " 00:00:00";
    		}else{
    			map.put("status", "failure");
    			return map;
    		}
    		
    		if(jsonMap.get("endtime") != null){
    			endtime = jsonEndTime + " 23:59:59";
    		}else{
    			map.put("status", "failure");
    			return map;
    		}
    		
    		if(jsonMap.get("vehicleNumber") != null){
    			vehicleNumber = String.valueOf(jsonMap.get("vehicleNumber"));
    		}else{
    			map.put("status", "failure");
    			return map;
    		}
    		
			VehicleModel vehicleModel = usageReportService.findVehicleListByVehicleNumber(vehicleNumber);
			if(vehicleModel != null){
				
				if(loginUser.isEntAdmin()){//企业管理员
					long entId = loginUser.getOrganizationId();
					if(vehicleModel.getEntId() != null){
						if(vehicleModel.getEntId().longValue() != entId){//企业管理员只能查看本企业的车
							map.put("status", "failure");
			    			return map;
						}
					}else{
						map.put("status", "failure");
		    			return map;
					}
				}
				
				if(loginUser.isDeptAdmin()){//部门管理员
					long deptId = loginUser.getOrganizationId();
					if(vehicleModel.getCurrentuseOrgId() == null){//车还在企业，没有分配给部门
							map.put("status", "failure");
			    			return map;
					}else{
						if(vehicleModel.getCurrentuseOrgId().longValue() != deptId){//部门管理员只能查看本部门的车
							map.put("status", "failure");
			    			return map;
						}
					}
				}
				
				
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
									map.put("data", retList);
								}
							}else{
								map.put("status", "failure");
							}
						}else{
							 map.put("status", "failure");
						}
					}else{
						map.put("status", "failure");
					}
				}else{
					map.put("status", "failure");
				}
			}else{
				 map.put("status", "failure");
			}
    		
    		map.put("filename", "车辆行驶明细查询-"+vehicleNumber+"-"+jsonStartTime+"-"+jsonEndTime+".xls");
    		map.put("sheet", DateUtils.getNowDate());
    		map.put("header", "车牌号,车辆品牌,车辆型号,所属部门,车辆用途,时间,明细,位置");
    		map.put("data", retList);
    		CsvUtil.exportExcel(map,request,response);
    	}catch(Exception e){
    		LOG.error("UsageReportController.exportDrivingDetailedReport",e);
   		   map.put("status", "failure");
   	   }
       return map;
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
	@RequestMapping(value = "/findVehicleStatus", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findVehicleStatus(@CurrentUser User loginUser,String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		
		int totalNumber = 0; //车辆总数
		int onLineNumber = 0;//在线
		int drivingNumber = 0;//行驶
		int stopNumber = 0;//停止
		int offLineNumber = 0;//离线
		
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			
			VehicleStatus model = new VehicleStatus();

			List<VehicleModel> vehicleList = null;
			Long orgId = loginUser.getOrganizationId();
			if (loginUser.isEntAdmin()) {
		    	   String deptId = String.valueOf(jsonMap.get("deptId"));
		    	   if(StringUtils.isNotEmpty(deptId)){
		    		   orgId = Long.valueOf(deptId);
		    		   vehicleList = usageReportService.findAllVehicleListByDeptId(orgId);
		    	   }else{
		    		   vehicleList = usageReportService.findAllVehicleListByEntId(orgId);
		    	   }
			} else if (loginUser.isDeptAdmin()) {
				vehicleList = usageReportService.findAllVehicleListByDeptId(orgId);
			}
			
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
		} catch (Exception e) {
			LOG.error("VehicleController.findVehicleStatus", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	
	/**
	 * 首页统计车辆状态(redis调用) new old:findVehicleStatus
	 * “车辆总数”、“在线”、“行驶”、“停止”、“离线”
	 * 
	 * 在线=车辆总数-离线；
	      在线=行驶+停止；
              车辆最近一条数据与当前时间超过10分钟的，状态为离线；
	 * @return
	 */
	@RequestMapping(value = "/findVehicleStatusData", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findVehicleStatusData(@CurrentUser User loginUser,String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		
		int totalNumber = 0; //车辆总数
		int onLineNumber = 0;//在线
		int drivingNumber = 0;//行驶
		int stopNumber = 0;//停止
		int offLineNumber = 0;//离线
		
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			
			VehicleStatus model = new VehicleStatus();

			List<VehicleModel> vehicleList = null;
			Long orgId = loginUser.getOrganizationId();
			
			if(jsonMap.get("orgId") != null){
				String orgIdVal =  String.valueOf(jsonMap.get("orgId"));
				if(!StringUtils.isEmpty(orgIdVal)){
					orgId = Long.valueOf(orgIdVal);
				}
			}
			
			vehicleList = usageReportService.findAllVehicleListByOrgId(orgId);
			
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
		} catch (Exception e) {
			LOG.error("VehicleController.findVehicleStatus", e);
			map.put("status", "failure");
		}
		return map;
	}
    
}
