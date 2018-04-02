package com.cmdt.carrental.quartz.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.cmdt.carrental.common.cache.RedisService;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.OBDQueryDTO;
import com.cmdt.carrental.common.model.VehicleLocationModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleQueryDTO;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.quartz.service.VehicleMonitorService;
import com.cmdt.carrental.quartz.spring.SpringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;


public class VehicleMonitorJob extends QuartzJobBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Logger LOG = Logger.getLogger(VehicleMonitorJob.class);
	
	private RedisService redisService;
	
	private VehicleMonitorService vehicleMonitorService;
	
	private ShouqiService shouqiService;
	
	private VehicleService vehicleService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	private static final String VEHICLE_MONITOR_PREFIX="VEHICLE_";

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		Date currentDate = new Date();
		if(redisService == null){
			redisService = SpringUtils.getBean("redisService");
		}
		if(vehicleMonitorService == null){
			vehicleMonitorService = SpringUtils.getBean("vehicleMonitorServiceImpl");
		}
		if(shouqiService == null){
			shouqiService = SpringUtils.getBean("shouqiService");
		}
		if(vehicleService == null){
			vehicleService = SpringUtils.getBean("vehicleServiceImpl");
		}
		//获得所有的obd list
		List<VehicleQueryDTO> obdList = vehicleMonitorService.findVehicleList();
		List<OBDQueryDTO> obdQueryList = new ArrayList<OBDQueryDTO>();
		if(obdList != null && obdList.size() > 0){
			Map<String,String> vehicleMap = new HashMap<String,String>();
			int obdListSize = obdList.size();
			for(int i = 0 ; i < obdListSize ; i ++){
				VehicleQueryDTO vehicleModel = obdList.get(i);
			    vehicleMap.put(vehicleModel.getDeviceNumber(), vehicleModel.getVehicleNumber());
			    obdQueryList.add(new OBDQueryDTO(vehicleModel.getDeviceNumber()));
			}
			callGetLatestDatasByImeiService(obdQueryList,vehicleMap,currentDate);
		}
	}
	
	/**
	 * 查询OBD最新位置并放置于redis
	 */
	public void callGetLatestDatasByImeiService(List<OBDQueryDTO> obdList, Map<String, String> vehicleMap,Date currentDate) {
		
		
		LOG.info("--------callGetLatestDatasByImeiService start :");
		try {
			Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.GETLATESTDATASBYIMEI, new Object[]{obdList});
			if (result != null && result.get("status").equals("success") && result.get("data") != null && StringUtils.isNotEmpty(result.get("data").toString())) {
				JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
				if ("000".equals(jsonNode.get("status").asText())) {
					ArrayNode rows = (ArrayNode) jsonNode.get("result");
					if (rows != null && rows.size() > 0) {
						for (int i = 0; i < rows.size(); i++) {
							JsonNode latestDataJsonNode = rows.get(i);
							String imei = latestDataJsonNode.get("imei").asText();
							double longitude = latestDataJsonNode.get("longitude").asDouble();
							double latitude = latestDataJsonNode.get("latitude").asDouble();
							int speed = latestDataJsonNode.get("speed").asInt();
							String tracetime = latestDataJsonNode.get("traceTime").asText();
							String vehicleNumber = vehicleMap.get(imei);
							VehicleLocationModel vehicleLocationModel = new VehicleLocationModel();
							if (null != imei) {
								VehicleModel vehicleModel = vehicleService.findVehicleByImei(imei);
								vehicleLocationModel.setRealname(vehicleModel.getRealname());
								vehicleLocationModel.setPhone(vehicleModel.getPhone());
								vehicleLocationModel.setVehicleType(vehicleModel.getVehicleType());
							}
							vehicleLocationModel.setImei(imei);
							vehicleLocationModel.setVehiclenumber(vehicleNumber);
							vehicleLocationModel.setLatitude(latitude);
							vehicleLocationModel.setLongitude(longitude);
							vehicleLocationModel.setSpeed(speed);
							vehicleLocationModel.setTracetime(tracetime);
							if(latestDataJsonNode.get("bearing") != null){
								vehicleLocationModel.setBearing(latestDataJsonNode.get("bearing").asInt());
							}else{
								vehicleLocationModel.setBearing(0);
							}
							
							if (speed > 0) {
								vehicleLocationModel.setStatus("行驶");
							} else {
								vehicleLocationModel.setStatus("停止");
							}
							
							long minutes = TimeUtils.timeBetween(TimeUtils.formatDate(tracetime),currentDate,Calendar.MINUTE);
							if(minutes > 10){
								vehicleLocationModel.setStatus("离线");
							}
							
							LOG.info("--------begin put data into redis-----imei:---------" + imei);
							redisService.set(VEHICLE_MONITOR_PREFIX + imei,
									MAPPER.writeValueAsString(vehicleLocationModel));
							LOG.info("--------end put data into redis--------------");
						}
					}
				}
			}
		} catch (Exception e) {
			LOG.error("VehicleMonitorJob callGetLatestDatasByImeiService error, cause by\n",e);
		}

	}

}
