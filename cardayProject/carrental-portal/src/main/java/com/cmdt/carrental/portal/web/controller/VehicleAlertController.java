
package com.cmdt.carrental.portal.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmdt.carrental.common.bean.AlertType;
import com.cmdt.carrental.common.entity.Marker;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.VehicleAlert;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.model.AlertCountModel;
import com.cmdt.carrental.common.model.AlertDiagramStatisticModel;
import com.cmdt.carrental.common.model.AlertReport;
import com.cmdt.carrental.common.model.AlertStatisticModel;
import com.cmdt.carrental.common.model.CountModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.Point;
import com.cmdt.carrental.common.model.PositionAndStation;
import com.cmdt.carrental.common.model.QueryAlertInfoModel;
import com.cmdt.carrental.common.model.StationModel;
import com.cmdt.carrental.common.model.TransGPStoBaiduModel;
import com.cmdt.carrental.common.model.TripTraceModel;
import com.cmdt.carrental.common.model.VehicleAlertQueryDTO;
import com.cmdt.carrental.common.model.VehicleHistoryTrack;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleOutBoundModel;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.VehicleAlertService;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.common.util.CsvUtil;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.ExportFileBean;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;
import com.fasterxml.jackson.databind.JsonNode;

@Controller
@RequestMapping("/vehicleAlert")
public class VehicleAlertController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(VehicleAlertController.class);
	@Autowired
	private VehicleAlertService vehicleAlertService;
	@Autowired
	private ShouqiService shouqiService;
	@Autowired
	private VehicleService vehicleService;
	@Autowired
	private OrganizationService organizationService;
	


	/**
	 * 根据起止时间及车辆和部门信息查询超速报警(导出)
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/exportOverspeedAlertData", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public void exportOverspeedAlertData(@CurrentUser User loginUser,String json,HttpServletRequest request,HttpServletResponse response) {
    	Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			List<String> retList = new ArrayList<String>();
			if (StringUtils.isNotEmpty(json)) {
				VehicleAlertQueryDTO vehicleAlertQueryDTO = new VehicleAlertQueryDTO();
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				vehicleAlertQueryDTO.setStartTime(TypeUtils.obj2Date(jsonMap.get("startTime")));
				vehicleAlertQueryDTO.setEndTime(TypeUtils.obj2Date(jsonMap.get("endTime")));
				vehicleAlertQueryDTO.setAlertType(AlertType.OVERSPEED.toString());
				
				if(jsonMap.get("selfDept") != null){
					vehicleAlertQueryDTO.setSelfDept(Boolean.valueOf(String.valueOf(jsonMap.get("selfDept"))));;
	    		}
	    		if(jsonMap.get("childDept") != null){
	    			vehicleAlertQueryDTO.setChildDept(Boolean.valueOf(String.valueOf(jsonMap.get("childDept"))));
	    		}
	    		
	    		if(jsonMap.get("orgId") != null){
	    			vehicleAlertQueryDTO.setOrganizationId(Integer.parseInt(String.valueOf(jsonMap.get("orgId"))));
	    		}
				
				
		    	List<VehicleAlert> alertList = vehicleAlertService.findAllVehicleAlertData(vehicleAlertQueryDTO);
				if(alertList != null && alertList.size() > 0){
					for(VehicleAlert vehicleAlert : alertList){
						StringBuffer buffer = new StringBuffer();
						buffer.append(vehicleAlert.getVehicleNumber()).append(",");
						buffer.append(vehicleAlert.getVehicleTypeName(vehicleAlert.getVehicleType())).append(",");
						buffer.append("超速,");
						buffer.append(vehicleAlert.getAlertTime()).append(",");
						buffer.append(vehicleAlert.getAlertSpeed()).append("km/h,");
						buffer.append(vehicleAlert.getOverspeedPercent()).append(",");
						buffer.append(vehicleAlert.getAlertPosition()).append(",");
						buffer.append(vehicleAlert.getVehicleSource()).append(",");
						buffer.append(vehicleAlert.getCurrentuseOrgName()).append(",");
						buffer.append(vehicleAlert.getDriverName()).append(",");
						buffer.append(vehicleAlert.getDriverPhone());
						retList.add(buffer.toString());
					}
				}
			}
			map.put("status", "success");
			map.put("data", retList);
			map.put("filename", "超速报警列表.xls");
    		map.put("sheet", "超速报警列表");
    		map.put("header", "车牌号,车辆类型,异常类型,报警时间,实际速度,超速比例,超速位置,车辆来源,部门,司机姓名,司机电话");
    		CsvUtil.exportExcel(map,request,response);
		} catch (Exception e) {
			LOG.error("vehicleAlert Controller export overspeed alert error, cause by:", e);
			map.put("status", "failure");
		}
    }
    
    
    
    
    
    @RequestMapping(value = "/findVehicleAlertInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findVehicleAlertInfo(@CurrentUser User loginUser,String json) {
    	Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			QueryAlertInfoModel model = JsonUtils.json2Object(json, QueryAlertInfoModel.class);
			PagModel pagModel = vehicleAlertService.findVehicleAlertInfo(model);
			map.put("data",pagModel);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("vehicleAlert Controller findVehicleAlertInfo error, cause by:", e);
			map.put("status", "failure");
		}
    	return map;
    }

	
	/**
	 * 根据起止时间及车辆和部门信息查询(导出)
	 * 越界报警
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportOutboundAlertData", method = RequestMethod.GET)
	@ResponseBody
	public void exportOutboundAlertData(@CurrentUser User loginUser, String json,HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			List<String> retList = new ArrayList<String>();
			if (StringUtils.isNotEmpty(json)) {
				VehicleAlertQueryDTO vehicleAlertQueryDTO = new VehicleAlertQueryDTO();
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				vehicleAlertQueryDTO.setOrganizationId(TypeUtils.obj2Integer(jsonMap.get("orgId")));
				vehicleAlertQueryDTO.setStartTime(TypeUtils.obj2Date(jsonMap.get("startTime")));
				vehicleAlertQueryDTO.setEndTime(TypeUtils.obj2Date(jsonMap.get("endTime")));
				vehicleAlertQueryDTO.setAlertType(AlertType.OUTBOUND.toString());
				if(jsonMap.get("selfDept") != null){
					vehicleAlertQueryDTO.setSelfDept(Boolean.valueOf(String.valueOf(jsonMap.get("selfDept"))));;
	    		}
	    		if(jsonMap.get("childDept") != null){
	    			vehicleAlertQueryDTO.setChildDept(Boolean.valueOf(String.valueOf(jsonMap.get("childDept"))));
	    		}
				
		    	List<VehicleAlert> alertList = vehicleAlertService.findAllVehicleAlertData(vehicleAlertQueryDTO);
				if(alertList != null && alertList.size() > 0){
					for(VehicleAlert vehicleAlert : alertList){
						StringBuffer buffer = new StringBuffer();
						buffer.append(vehicleAlert.getVehicleNumber()).append(",");
						buffer.append(vehicleAlert.getVehicleTypeName(vehicleAlert.getVehicleType())).append(",");
						buffer.append("越界,");
						buffer.append(vehicleAlert.getFirstOutboundtime()).append(",");
						buffer.append(vehicleAlert.getOutboundReleasetime()).append(",");
						buffer.append(vehicleAlert.getOutboundMinutes()).append(",");
						buffer.append(vehicleAlert.getOutboundKilos()).append(",");
						buffer.append(vehicleAlert.getVehicleSource()).append(",");
						buffer.append(vehicleAlert.getCurrentuseOrgName()).append(",");
						buffer.append(vehicleAlert.getDriverName()).append(",");
						buffer.append(vehicleAlert.getDriverPhone());
						retList.add(buffer.toString());
					}
				}
			}
			map.put("status", "success");
			map.put("data", retList);
			map.put("filename", "越界报警列表.xls");
    		map.put("sheet", "越界报警列表");
    		map.put("header", "车牌号,车辆类型,异常类型,首次发生时间,解除时间,越界时长(分钟),越界里程(KM),车辆来源,部门,司机姓名,司机电话");
    		CsvUtil.exportExcel(map,request,response);
		} catch (Exception e) {
			LOG.error("vehicleAlert Controller export outbound alert error, cause by:", e);
			map.put("status", "failure");
		}
	}

	
    private StationModel transGPStoBaidu(StationModel stationModel) {
    	Point point = new Point();
		point.setLat(Double.valueOf(stationModel.getLatitude()));
		point.setLon(Double.valueOf(stationModel.getLongitude()));
		List<Point> points = new ArrayList<Point>();
		points.add(point);
		String result = shouqiService.transGPStoBaidu(points);
		TransGPStoBaiduModel transGPStoBaiduModel = JsonUtils.json2Object(result, TransGPStoBaiduModel.class);
		if(transGPStoBaiduModel.getResult().size()>0) {
			stationModel.setLatitude(String.valueOf(transGPStoBaiduModel.getResult().get(0).getLat()));
			stationModel.setLongitude(String.valueOf(transGPStoBaiduModel.getResult().get(0).getLon()));
		}
    	return stationModel;
    }
	
    
	/**
	 * 根据起止时间及车辆和部门信息查询(导出)
	 * 回车报警
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/exportVehiclebackAlertData", method = RequestMethod.GET)
	@ResponseBody
	public void exportVehiclebackAlertData(@CurrentUser User loginUser, String json,HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			List<String> retList = new ArrayList<String>();
			if (StringUtils.isNotEmpty(json)) {
				VehicleAlertQueryDTO vehicleAlertQueryDTO = new VehicleAlertQueryDTO();
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				vehicleAlertQueryDTO.setOrganizationId(TypeUtils.obj2Integer(jsonMap.get("orgId")));
				vehicleAlertQueryDTO.setStartTime(TypeUtils.obj2Date(jsonMap.get("startTime")));
				vehicleAlertQueryDTO.setEndTime(TypeUtils.obj2Date(jsonMap.get("endTime")));
				vehicleAlertQueryDTO.setAlertType(AlertType.VEHICLEBACK.toString());
				if(jsonMap.get("selfDept") != null){
					vehicleAlertQueryDTO.setSelfDept(Boolean.valueOf(String.valueOf(jsonMap.get("selfDept"))));;
	    		}
	    		if(jsonMap.get("childDept") != null){
	    			vehicleAlertQueryDTO.setChildDept(Boolean.valueOf(String.valueOf(jsonMap.get("childDept"))));
	    		}
	    		
		    	List<VehicleAlert> alertList = vehicleAlertService.findAllVehicleAlertData(vehicleAlertQueryDTO);
				if(alertList != null && alertList.size() > 0){
					for(VehicleAlert vehicleAlert : alertList){
						StringBuffer buffer = new StringBuffer();
						buffer.append(vehicleAlert.getVehicleNumber()).append(",");
						buffer.append(vehicleAlert.getVehicleTypeName(vehicleAlert.getVehicleType())).append(",");
						buffer.append("回车,");
						buffer.append(TimeUtils.formatDate(vehicleAlert.getAlertTime())).append(",");
						buffer.append(vehicleAlert.getAlertCity()).append(",");
						buffer.append(vehicleAlert.getAlertPosition()).append(",");
						buffer.append(vehicleAlert.getVehicleSource()).append(",");
						buffer.append(vehicleAlert.getCurrentuseOrgName()).append(",");
						buffer.append(vehicleAlert.getDriverName()).append(",");
						buffer.append(vehicleAlert.getDriverPhone());
						retList.add(buffer.toString());
					}
				}
			}
			map.put("status", "success");
			map.put("data", retList);
			map.put("filename", "回车报警列表.xls");
    		map.put("sheet", "回车报警列表");
    		map.put("header", "车牌号,车辆类型,异常类型,报警时间,城市,实际停车位置,车辆来源,部门,司机姓名,司机电话");
    		CsvUtil.exportExcel(map,request,response);
		} catch (Exception e) {
			LOG.error("vehicleAlert Controller export back alert error, cause by:", e);
			map.put("status", "failure");
		}
	}
	

	/**
	 * 根据车牌号查找对应的Geofence和车辆历史轨迹
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/findMarkerByVehicleNumber", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findMarkerByVehicleNumber(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Long entId = loginUser.getOrganizationId();
			VehicleOutBoundModel vehicleOutBoundModel = new VehicleOutBoundModel();
			if (StringUtils.isNotEmpty(json)) {
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				String vehicleNumber = TypeUtils.obj2String(jsonMap.get("vehicleNumber"));
				String startTime = TypeUtils.obj2String(jsonMap.get("startTime"));
				String endTime = TypeUtils.obj2String(jsonMap.get("endTime"));
				if ("".equals(endTime)) {
					Date currentDate = new Date();
					endTime=DateUtils.date2String(currentDate, DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
					startTime=DateUtils.date2String(DateUtils.addDays(currentDate, -1), DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
				}
				
				List<Marker> markerIdlist = vehicleAlertService.findMarker(vehicleNumber);
				// 根据车牌号查询设备号
				VehicleModel vehicleModel = vehicleAlertService.getVehicleByVehicleNumber(entId,vehicleNumber);
				// 查找车在某个时间段的轨迹
				List<VehicleHistoryTrack> retList  = vehicleAlertService.findTripTraceDataByTimeRange(vehicleModel.getDeviceNumber(),
						startTime, endTime);

				LOG.info("getGpsTrack size - after trans GPS to baidu:["+retList.size()+"]");
				vehicleOutBoundModel.setMarkers(markerIdlist);
				vehicleOutBoundModel.setTraceModels(retList);   		
			}
			map.put("status", "success");
			map.put("data", vehicleOutBoundModel);
		} catch (Exception e) {
			LOG.error("VehicleAlertController find vehicle back alert error, cause by:\n", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	
	/**
	 * 部门异常用车(列表) new old:findAlertByTypeDepartmentAndTimeRange
	 * @param loginUser
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/findAlertDataByType", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findAlertDataByType(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			PagModel pagModel= new PagModel();
			if (StringUtils.isNotEmpty(json)) {
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				String startDay = String.valueOf(jsonMap.get("startDay")).replace("-", "");
				String endDay = String.valueOf(jsonMap.get("endDay")).replace("-", "");
				Long orgId = Long.valueOf(String.valueOf(jsonMap.get("orgId")));
				String alertType = String.valueOf(jsonMap.get("alertType"));
				int currentPage = TypeUtils.obj2Integer(jsonMap.get("currentPage"));
				int numPerPage =TypeUtils.obj2Integer(jsonMap.get("numPerPage"));
				Boolean selfDept = true;
	    		Boolean childDept = true;
	    		if(jsonMap.get("selfDept") != null){
	    			selfDept = Boolean.valueOf(String.valueOf(jsonMap.get("selfDept")));
	    		}
	    		if(jsonMap.get("childDept") != null){
	    			childDept = Boolean.valueOf(String.valueOf(jsonMap.get("childDept")));
	    		}
				
				List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, selfDept , childDept);
				if(orgList!=null&&orgList.size()>0){
				  pagModel = vehicleAlertService.getAlertByDepartmentAndTimeRange(orgList,selfDept,orgId,alertType, startDay, endDay, currentPage, numPerPage);
				}
			}
			
			map.put("status", "success");
			map.put("data", pagModel);
	} catch (Exception e) {
		LOG.error("VehicleAlertController statistics daily report by department id and time range error, cause by:", e);
		map.put("status", "failure");
	}
		return map;
	}
	
	/**
	 * 该方法已废弃
	 * @param loginUser
	 * @param json
	 * @return
	 */
	/*@SuppressWarnings("unchecked")
	@RequestMapping(value = "/statAlertByTypeDepartmentAndTimeRange", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> statAlertByTypeDepartmentAndTimeRange(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
				Long orgId = loginUser.getOrganizationId();
				String startDay = "";
				String endDay = "";
				String alertType = "";
				if (StringUtils.isNotEmpty(json)) {
					Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
					startDay = String.valueOf(jsonMap.get("startDay")).replace("-", "");
					endDay = String.valueOf(jsonMap.get("endDay")).replace("-", "");
					alertType = String.valueOf(jsonMap.get("alertType"));
				}
				List<CountModel>  dailyReport = vehicleAlertService.statisticDailyAlertByTypeDepartmentTimeRanger(orgId.toString(), alertType, startDay, endDay);
				map.put("status", "success");
				map.put("data", dailyReport);
		} catch (Exception e) {
			LOG.error("VehicleAlertController statistics daily report by department id and time range error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}*/

	/**
	 *  部门异常用车(曲线图)  new old:statAlertByTypeDepartmentAndTimeRange
	 * @param loginUser
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/statAlertByType", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> statAlertByType(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
				Long orgId = 0l;
				String startDay = "";
				String endDay = "";
				String alertType = "";
				Boolean selfDept = true;
	    		Boolean childDept = true;
				if (StringUtils.isNotEmpty(json)) {
					Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
					orgId = Long.valueOf(String.valueOf(jsonMap.get("orgId")));
					startDay = String.valueOf(jsonMap.get("startDay")).replace("-", "");
					endDay = String.valueOf(jsonMap.get("endDay")).replace("-", "");
					alertType = String.valueOf(jsonMap.get("alertType"));
					
		    		if(jsonMap.get("selfDept") != null){
		    			selfDept = Boolean.valueOf(String.valueOf(jsonMap.get("selfDept")));
		    		}
		    		if(jsonMap.get("childDept") != null){
		    			childDept = Boolean.valueOf(String.valueOf(jsonMap.get("childDept")));
		    		}
				}
				
				List<CountModel>  dailyReport = vehicleAlertService.statisticDailyAlertByTypeDepartmentTimeRanger(orgId, alertType, startDay, endDay,selfDept,childDept);
				
				map.put("status", "success");
				map.put("data", dailyReport);
		} catch (Exception e) {
			LOG.error("VehicleAlertController statistics daily report by department id and time range error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 异常用车统计(饼图与柱状图) new old:findVehicleAlertStatistics
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/findVehicleAlertPieAndColumnarData", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findVehicleAlertPieAndColumnarData(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			
			String starttime = "";
    		String endtime = "";
    		Long orgId = 0l;
    		Boolean selfDept = true;
    		Boolean childDept = true;
			
    		if(jsonMap.get("startDay") != null){
    			starttime = String.valueOf(jsonMap.get("startDay"));
    		}
    		if(jsonMap.get("endDay") != null){
    			endtime = String.valueOf(jsonMap.get("endDay"));
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
			
    		//按输入日期区间进行统计
    		Date startDate = DateUtils.string2Date(starttime + " 00:00:00","yyyy-MM-dd HH:mm:ss");
    		Date endDate = DateUtils.string2Date(endtime + " 23:59:59","yyyy-MM-dd HH:mm:ss");
    		
    		AlertReport alertReport = vehicleAlertService.queryVehicleAlertStatisticsTopX(startDate,endDate,orgId,selfDept,childDept,5);

            if(alertReport != null){
            	map.put("data", alertReport);
            }
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleAlertController find vehicle alert statistics error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
//	@RequiresPermissions("alertcount:list")
	@RequestMapping(value = "/findVehicleAlertCountByDate", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findVehicleAlertCountByDate(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			List<VehicleAlert> alertList = new ArrayList();
//			json = "{\"vehicleNumber\":\"沪A1111\",\"startTime\":\"2016-01-01 00:00:00\",\"endTime\":\"2017-01-01 00:00:00\"}";
			if (StringUtils.isNotEmpty(json)) {
				VehicleAlertQueryDTO vehicleAlertQueryDTO = new VehicleAlertQueryDTO();
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				vehicleAlertQueryDTO.setVehicleNumber(TypeUtils.obj2String(jsonMap.get("vehicleNumber")));
				vehicleAlertQueryDTO.setStartTime(TypeUtils.obj2Date(jsonMap.get("startTime")));
				vehicleAlertQueryDTO.setEndTime(TypeUtils.obj2Date(jsonMap.get("endTime")));
				
				alertList = vehicleAlertService.findVehicleAlert(vehicleAlertQueryDTO);
			}
			map.put("status", "success");
			map.put("data", alertList);
		} catch (Exception e) {
			LOG.error("vehicleAlert Controller find overspeed alert error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	@SuppressWarnings("unchecked")
//	@RequiresPermissions("alertcount:list")
	@RequestMapping(value = "/findVehicleAlert", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findTodayVehicleAlert(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			List<VehicleAlert> vehicleAlert= null;
			Long orgId = loginUser.getOrganizationId();

			if (StringUtils.isNotEmpty(json)) {
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				boolean isPre = (boolean) jsonMap.get("isPre");
				vehicleAlert=vehicleAlertService.findTodayAlert(orgId,isPre);
			}
			
			map.put("status", "success");
			map.put("data", vehicleAlert);
		} catch (Exception e) {
			LOG.error("vehicleAlert Controller find overspeed alert error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
//	@RequiresPermissions("alertcount:list")
	@RequestMapping(value = "/findVehicleAlertByPage", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findVehicleAlertByPage(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			PagModel pagModel= new PagModel();
			Long rentId = loginUser.getOrganizationId();
	    	boolean isEnt=false;
	    	//企业管理员
	    	if(loginUser.isEntAdmin()){
	    		isEnt=true;
	    	}
	    	
	    	//部门管理员
	    	if(loginUser.isDeptAdmin()){
	    		isEnt=false;
	    	}
			if (StringUtils.isNotEmpty(json)) {
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				int currentPage = TypeUtils.obj2Integer(jsonMap.get("currentPage"));
				int numPerPage =TypeUtils.obj2Integer(jsonMap.get("numPerPage"));
				pagModel=vehicleAlertService.findAlertByPage(currentPage,numPerPage,rentId,isEnt);
			}
			map.put("status", "success");
			map.put("data", pagModel);
		} catch (Exception e) {
			LOG.error("vehicleAlert Controller find overspeed alert error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	
	@SuppressWarnings("unchecked")
//	@RequiresPermissions("alertcount:list")
	@RequestMapping(value = "/findVehicleAlertCount", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findVehicleAlertCount(@CurrentUser User loginUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			List<AlertStatisticModel> alertCountList = new ArrayList<AlertStatisticModel>();
			int outboundCount = 0;
			int overspeedCount = 0;
			int vehiclebackCount = 0;
			Long orgId = loginUser.getOrganizationId();

			List<AlertCountModel> countList = vehicleAlertService.findAlertCountByType(orgId);
			//count alert
			for(AlertCountModel model : countList){
					String val = model.getAlertType();
					switch(val){
	        			case "OUTBOUND":
	        				outboundCount = model.getValue();
	        				break;
	        			case "OVERSPEED":
	        				overspeedCount = model.getValue();
	        				break;
	        			case "VEHICLEBACK":
	        				vehiclebackCount = model.getValue();
	        				break;
	        		}
			}

			AlertStatisticModel alert_ob = new AlertStatisticModel();
			AlertStatisticModel alert_os = new AlertStatisticModel();
			AlertStatisticModel alert_vb = new AlertStatisticModel();
			alert_ob.setName(AlertType.OUTBOUND.toString());
			alert_ob.setValue(outboundCount);
			alert_os.setName(AlertType.OVERSPEED.toString());
			alert_os.setValue(overspeedCount);
			alert_vb.setName(AlertType.VEHICLEBACK.toString());
			alert_vb.setValue(vehiclebackCount);
			alertCountList.add(alert_ob);
			alertCountList.add(alert_os);
			alertCountList.add(alert_vb);
			
			map.put("status", "success");
			map.put("data", alertCountList);
		} catch (Exception e) {
			LOG.error("vehicleAlert Controller find overspeed alert error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/findStationByVehicleNumber", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findStationByVehicleNumber(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			List<StationModel> stationModels = null;
			if (StringUtils.isNotEmpty(json)) {
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				String vehicleNumber = TypeUtils.obj2String(jsonMap.get("vehicleNumber"));
				String longitude = TypeUtils.obj2String(jsonMap.get("longitude"));
				String latitude=TypeUtils.obj2String(jsonMap.get("latitude"));
				// 查找车牌号对应的所有Station
				stationModels = vehicleAlertService.findStation(vehicleNumber);
				StationModel sPosition=new StationModel(); //车的目前位置
				sPosition.setLatitude(latitude);
				sPosition.setLongitude(longitude);
				sPosition=transGPStoBaidu(sPosition);
				
				PositionAndStation positionAndStation =new PositionAndStation();
				positionAndStation.setLatitude(sPosition.getLatitude());
				positionAndStation.setLongitude(sPosition.getLongitude());
				positionAndStation.setStationModels(stationModels);
				map.put("status", "success");
				map.put("data", positionAndStation);
			}
		} catch (Exception e) {
			LOG.error("Marker Controller find vehicle back alert error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	    
	    
		/**
		 * 异常用车统计(三种类型汇总导出,三个sheet) new old:exportAllAlert
		 * @param json
		 * @return
		 */
	    @RequestMapping(value = "/exportAllAlertData", method = RequestMethod.GET)
	    @ResponseBody
	    @SuppressWarnings("unchecked")
	    public Map<String,Object> exportAllAlertData(@CurrentUser User loginUser,String json,HttpServletRequest request,HttpServletResponse response) {
	    	Map<String, Object> map = new HashMap<String, Object>();
	    	List<ExportFileBean> exportFileBeanList = new ArrayList<ExportFileBean>();
			try {
				if (StringUtils.isNotEmpty(json)) {
					VehicleAlertQueryDTO vehicleAlertQueryDTO = new VehicleAlertQueryDTO();
					Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
					vehicleAlertQueryDTO.setOrganizationId(TypeUtils.obj2Integer(jsonMap.get("orgId")));
					vehicleAlertQueryDTO.setStartTime(TypeUtils.obj2Date(jsonMap.get("startTime")));
					vehicleAlertQueryDTO.setEndTime(TypeUtils.obj2Date(jsonMap.get("endTime")));
					
					if(jsonMap.get("selfDept") != null){
						vehicleAlertQueryDTO.setSelfDept(Boolean.valueOf(String.valueOf(jsonMap.get("selfDept"))));;
		    		}
		    		if(jsonMap.get("childDept") != null){
		    			vehicleAlertQueryDTO.setChildDept(Boolean.valueOf(String.valueOf(jsonMap.get("childDept"))));
		    		}
		    		
			    	//超速
			    	vehicleAlertQueryDTO.setAlertType(AlertType.OVERSPEED.toString());
			    	List<String> overspeedList = new ArrayList<String>();
			    	List<VehicleAlert> overspeedAlertList = vehicleAlertService.findAllVehicleAlertData(vehicleAlertQueryDTO);
			    	ExportFileBean overspeedExportFileBean = new ExportFileBean();
					if(overspeedAlertList != null && overspeedAlertList.size() > 0){
						for(VehicleAlert vehicleAlert : overspeedAlertList){
							StringBuffer buffer = new StringBuffer();
							buffer.append(vehicleAlert.getVehicleNumber()).append(",");
							buffer.append(vehicleAlert.getVehicleTypeName(vehicleAlert.getVehicleType())).append(",");
							buffer.append("超速,");
							buffer.append(vehicleAlert.getAlertTime()).append(",");
							buffer.append(vehicleAlert.getAlertSpeed()).append("km/h,");
							buffer.append(vehicleAlert.getOverspeedPercent()).append(",");
							buffer.append(vehicleAlert.getAlertPosition()).append(",");
							buffer.append(vehicleAlert.getVehicleSource()).append(",");
							buffer.append(vehicleAlert.getCurrentuseOrgName()).append(",");
							buffer.append(vehicleAlert.getDriverName()).append(",");
							buffer.append(vehicleAlert.getDriverPhone());
							overspeedList.add(buffer.toString());
						}
					}
					overspeedExportFileBean.setSheet("超速报警列表");
					overspeedExportFileBean.setHeader("车牌号,车辆类型,异常类型,报警时间,实际速度,超速比例,超速位置,车辆来源,部门,司机姓名,司机电话");
					overspeedExportFileBean.setData(overspeedList);
					exportFileBeanList.add(overspeedExportFileBean);
					
					//越界
					vehicleAlertQueryDTO.setAlertType(AlertType.OUTBOUND.toString());
			    	List<String> outboundList = new ArrayList<String>();
			    	List<VehicleAlert> outboundAlertList = vehicleAlertService.findAllVehicleAlertData(vehicleAlertQueryDTO);
			    	if(outboundAlertList != null && outboundAlertList.size() > 0){
			    		ExportFileBean outboundExportFileBean = new ExportFileBean();
						for(VehicleAlert vehicleAlert : outboundAlertList){
							StringBuffer buffer = new StringBuffer();
							buffer.append(vehicleAlert.getVehicleNumber()).append(",");
							buffer.append(vehicleAlert.getVehicleTypeName(vehicleAlert.getVehicleType())).append(",");
							buffer.append("越界,");
							buffer.append(vehicleAlert.getFirstOutboundtime()).append(",");
							buffer.append(vehicleAlert.getOutboundReleasetime()).append(",");
							buffer.append(vehicleAlert.getOutboundMinutes()).append(",");
							buffer.append(vehicleAlert.getOutboundKilos()).append(",");
							buffer.append(vehicleAlert.getVehicleSource()).append(",");
							buffer.append(vehicleAlert.getCurrentuseOrgName()).append(",");
							buffer.append(vehicleAlert.getDriverName()).append(",");
							buffer.append(vehicleAlert.getDriverPhone());
							outboundList.add(buffer.toString());
						}
						outboundExportFileBean.setSheet("越界报警列表");
						outboundExportFileBean.setHeader("车牌号,车辆类型,异常类型,首次发生时间,解除时间,越界时长(分钟),越界里程(KM),车辆来源,部门,司机姓名,司机电话");
						outboundExportFileBean.setData(outboundList);
						exportFileBeanList.add(outboundExportFileBean);
					}
			    	
			    	//回车
			    	vehicleAlertQueryDTO.setAlertType(AlertType.VEHICLEBACK.toString());
			    	List<String> backList = new ArrayList<String>();
			    	List<VehicleAlert> backAlertList = vehicleAlertService.findAllVehicleAlertData(vehicleAlertQueryDTO);
					if(backAlertList != null && backAlertList.size() > 0){
						ExportFileBean backExportFileBean = new ExportFileBean();
						for(VehicleAlert vehicleAlert : backAlertList){
							StringBuffer buffer = new StringBuffer();
							buffer.append(vehicleAlert.getVehicleNumber()).append(",");
							buffer.append(vehicleAlert.getVehicleTypeName(vehicleAlert.getVehicleType())).append(",");
							buffer.append("回车,");
							buffer.append(vehicleAlert.getAlertTime()).append(",");
							buffer.append(vehicleAlert.getAlertCity()).append(",");
							buffer.append(vehicleAlert.getAlertPosition()).append(",");
							buffer.append(vehicleAlert.getVehicleSource()).append(",");
							buffer.append(vehicleAlert.getCurrentuseOrgName()).append(",");
							buffer.append(vehicleAlert.getDriverName()).append(",");
							buffer.append(vehicleAlert.getDriverPhone());
							backList.add(buffer.toString());
						}
						backExportFileBean.setSheet("回车报警列表");
						backExportFileBean.setHeader("车牌号,车辆类型,异常类型,报警时间,城市,实际停车位置,车辆来源,部门,司机姓名,司机电话");
						backExportFileBean.setData(backList);
						exportFileBeanList.add(backExportFileBean);
					}
					map.put("status", "success");
		    		CsvUtil.exportExcelWithMultiSheet("异常用车统计.xls",exportFileBeanList,map,request,response);
				}else{
					map.put("status", "failure");
				}
			} catch (Exception e) {
				LOG.error("vehicleAlert Controller export overspeed alert error, cause by:", e);
				map.put("status", "failure");
			}
			return map;
	    }
	    
	    
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/exportVehiclebackAlert", method = RequestMethod.GET)
		@ResponseBody
		public void exportVehiclebackAlert(@CurrentUser User loginUser, String json,HttpServletRequest request,HttpServletResponse response) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("data", "");
			try {
				List<String> retList = new ArrayList<String>();
				if (StringUtils.isNotEmpty(json)) {
					QueryAlertInfoModel model = JsonUtils.json2Object(json, QueryAlertInfoModel.class);

			    	List<VehicleAlert> alertList = vehicleAlertService.findAllVehicleAlert(model);
					if(alertList != null && alertList.size() > 0){
						for(VehicleAlert vehicleAlert : alertList){
							StringBuffer buffer = new StringBuffer();
							buffer.append(vehicleAlert.getVehicleNumber()).append(",");
							buffer.append(vehicleAlert.getVehicleTypeName(vehicleAlert.getVehicleType())).append(",");
							buffer.append("回车,");
							buffer.append(TimeUtils.formatDate(vehicleAlert.getAlertTime())).append(",");
							buffer.append(vehicleAlert.getAlertCity()).append(",");
							buffer.append(vehicleAlert.getAlertPosition()).append(",");
							buffer.append(vehicleAlert.getVehicleSource()).append(",");
							buffer.append(vehicleAlert.getCurrentuseOrgName()).append(",");
							buffer.append(vehicleAlert.getDriverName()).append(",");
							buffer.append(vehicleAlert.getDriverPhone());
							retList.add(buffer.toString());
						}
					}
				}
				map.put("status", "success");
				map.put("data", retList);
				map.put("filename", "回车报警列表.xls");
	    		map.put("sheet", "回车报警列表");
	    		map.put("header", "车牌号,车辆类型,异常类型,报警时间,城市,实际停车位置,车辆来源,部门,司机姓名,司机电话");
	    		CsvUtil.exportExcel(map,request,response);
			} catch (Exception e) {
				LOG.error("vehicleAlert Controller export back alert error, cause by:", e);
				map.put("status", "failure");
			}
		}
	    
	    
		/**
		 * 根据起止时间及车辆和部门信息查询(导出)
		 * 越界报警
		 * 
		 * @return
		 */
		@SuppressWarnings("unchecked")
		@RequestMapping(value = "/exportOutboundAlert", method = RequestMethod.GET)
		@ResponseBody
		public void exportOutboundAlert(@CurrentUser User loginUser, String json,HttpServletRequest request,HttpServletResponse response) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("data", "");
			try {
				List<String> retList = new ArrayList<String>();
				if (StringUtils.isNotEmpty(json)) {
					QueryAlertInfoModel model = JsonUtils.json2Object(json, QueryAlertInfoModel.class);

			    	List<VehicleAlert> alertList = vehicleAlertService.findAllVehicleAlert(model);
					if(alertList != null && alertList.size() > 0){
						for(VehicleAlert vehicleAlert : alertList){
							StringBuffer buffer = new StringBuffer();
							buffer.append(vehicleAlert.getVehicleNumber()).append(",");
							buffer.append(vehicleAlert.getVehicleTypeName(vehicleAlert.getVehicleType())).append(",");
							buffer.append("越界,");
							buffer.append(vehicleAlert.getFirstOutboundtime()).append(",");
							buffer.append(vehicleAlert.getOutboundReleasetime()).append(",");
							buffer.append(vehicleAlert.getOutboundMinutes()).append(",");
							buffer.append(vehicleAlert.getOutboundKilos()).append(",");
							buffer.append(vehicleAlert.getVehicleSource()).append(",");
							buffer.append(vehicleAlert.getCurrentuseOrgName()).append(",");
							buffer.append(vehicleAlert.getDriverName()).append(",");
							buffer.append(vehicleAlert.getDriverPhone());
							retList.add(buffer.toString());
						}
					}
				}
				map.put("status", "success");
				map.put("data", retList);
				map.put("filename", "越界报警列表.xls");
	    		map.put("sheet", "越界报警列表");
	    		map.put("header", "车牌号,车辆类型,异常类型,首次发生时间,解除时间,越界时长(分钟),越界里程(KM),车辆来源,部门,司机姓名,司机电话");
	    		CsvUtil.exportExcel(map,request,response);
			} catch (Exception e) {
				LOG.error("vehicleAlert Controller export outbound alert error, cause by:", e);
				map.put("status", "failure");
			}
		}

		 @RequestMapping(value = "/exportOverspeedAlert", method = RequestMethod.GET)
		    @ResponseBody
		    @SuppressWarnings("unchecked")
		    public void exportOverspeedAlert(@CurrentUser User loginUser,String json,HttpServletRequest request,HttpServletResponse response) {
		    	Map<String, Object> map = new HashMap<String, Object>();
				map.put("data", "");
				try {
					List<String> retList = new ArrayList<String>();
					if (StringUtils.isNotEmpty(json)) {
						QueryAlertInfoModel model = JsonUtils.json2Object(json, QueryAlertInfoModel.class);

				    	List<VehicleAlert> alertList = vehicleAlertService.findAllVehicleAlert(model);
						if(alertList != null && alertList.size() > 0){
							for(VehicleAlert vehicleAlert : alertList){
								StringBuffer buffer = new StringBuffer();
								buffer.append(vehicleAlert.getVehicleNumber()).append(",");
								buffer.append(vehicleAlert.getVehicleTypeName(vehicleAlert.getVehicleType())).append(",");
								buffer.append("超速,");
								buffer.append(vehicleAlert.getAlertTime()).append(",");
								buffer.append(vehicleAlert.getAlertSpeed()).append("km/h,");
								buffer.append(vehicleAlert.getOverspeedPercent()).append(",");
								buffer.append(vehicleAlert.getAlertPosition()).append(",");
								buffer.append(vehicleAlert.getVehicleSource()).append(",");
								buffer.append(vehicleAlert.getCurrentuseOrgName()).append(",");
								buffer.append(vehicleAlert.getDriverName()).append(",");
								buffer.append(vehicleAlert.getDriverPhone());
								retList.add(buffer.toString());
							}
						}
					}
					map.put("status", "success");
					map.put("data", retList);
					map.put("filename", "超速报警列表.xls");
		    		map.put("sheet", "超速报警列表");
		    		map.put("header", "车牌号,车辆类型,异常类型,报警时间,实际速度,超速比例,超速位置,车辆来源,部门,司机姓名,司机电话");
		    		CsvUtil.exportExcel(map,request,response);
				} catch (Exception e) {
					LOG.error("vehicleAlert Controller export overspeed alert error, cause by:", e);
					map.put("status", "failure");
				}
		    }
		 
		 @SuppressWarnings("unchecked")
		 @RequestMapping(value = "/alarm/config", method = RequestMethod.POST)
		 @ResponseBody
		 public Map<String, Object> updateAlarmConfig(@CurrentUser User loginUser, String json){
			 Map<String, Object> map = new HashMap<String, Object>();
			 Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			 try{
				 Boolean isEnable  = TypeUtils.obj2Bool(jsonMap.get("isEnable"));
				 String eventType = TypeUtils.obj2String(jsonMap.get("eventType"));
				 Long entId = organizationService.findEntIdByOrgId(loginUser.getOrganizationId());
				 vehicleAlertService.updateAlarmConfig(entId, eventType, isEnable);
				 map.put("status", "success");
				 map.put("data", "");
			 }catch (Exception e) {
				   LOG.error("vehicleAlert Controller query alarm config, cause by:", e);
				   map.put("status", "failure");
			 }
			 return map;
		 }
		 
		 @RequestMapping(value = "/alarm/query", method = RequestMethod.GET)
		 @ResponseBody
		 @SuppressWarnings("unchecked")
		 public Map<String, Object> queryAlarmConfig(@CurrentUser User loginUser,String json){
			 Map<String, Object> map = new HashMap<String, Object>();
			 map.put("data", "");
			 Long entId = organizationService.findEntIdByOrgId(loginUser.getOrganizationId());
			 try {
			   map.put("status", "success");
			   map.put("data", vehicleAlertService.findAlarmConfig(entId));
			 }catch (Exception e) {
			   LOG.error("vehicleAlert Controller query alarm config, cause by:", e);
			   map.put("status", "failure");
			 }
			 return map;
		 }

}

