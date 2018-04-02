package com.cmdt.carrental.quartz.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.entity.VehicleMaintenance;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.UsageReportSQLModel;
import com.cmdt.carrental.common.service.VehicleMaintenanceServiceImpl;
import com.cmdt.carrental.common.util.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class VehicleTravelMileageServiceImpl implements VehicleTravelMileageService{
	
	private static final Logger LOG = LoggerFactory.getLogger(VehicleTravelMileageServiceImpl.class);
	
	@Autowired
    private ShouqiService shouqiService;
	
	@Autowired
	private VehicleMaintenanceServiceImpl vmService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	public void sysnVehicleTravelMileage() {
		//保养车辆集合
		List<VehicleMaintenance> vList = vmService.querySysnTravelMileageVehicleList();
		LOG.info("sysnVehicleTravelMileage+++++size:" + vList.size());
		LOG.info("begin sysnVehicleTravelMileageFromVehicleStastic");
		//调用shouqi接口同步行驶里程
		for(VehicleMaintenance vMaintenance : vList) {
			LOG.info("deviceNumber:" + vMaintenance.getDeviceNumber() + " to findTripPropertyDataByTimeRange..");
//			findTripPropertyDataByTimeRange(vMaintenance.getVehicleId(), vMaintenance.getDeviceNumber(), vMaintenance.getUpdateTimeF());
			sysnVehicleTravelMileageFromVehicleStastic(vMaintenance.getVehicleId(),vMaintenance.getVehicleNumber(),vMaintenance.getDeviceNumber(),vMaintenance.getCurTime());
		}
		LOG.info("end sysnVehicleTravelMileageFromVehicleStastic");
	}
	
	//fix bug CR-2459
	private void sysnVehicleTravelMileageFromVehicleStastic(Long vehicleId,String vehicleNumber, String deviceNumber, Date curTime) {
		Integer travelMileage = vmService.getVehicleMileageStasticByImeiAndBeginTime(curTime,deviceNumber,vehicleNumber);
		if(travelMileage != 0){
			float temp  = (float)travelMileage/1000;
			Long travelMileageVal = Long.valueOf(Math.round(temp));
			Date updateTime = DateUtils.getCurrentTime();
			modifyJobTravelMileageAndUpdateTime(vehicleId,travelMileageVal,updateTime);
		}
	}

	private void findTripPropertyDataByTimeRange(Long vehicleId, String imei, String starttime) {
		Long gapMileage = 0l;
		Date updateTime = DateUtils.getCurrentTime();
		String endtime = DateUtils.date2String(updateTime, "yyyy-MM-dd HH:mm:ss");
		LOG.info("imei:" + imei);
		LOG.info("starttime:" + starttime);
		LOG.info("endtime:" + endtime);
		Map<String, Object> result = new ServiceAdapter(shouqiService)
				.doService(ActionName.FINDTRIPPROPERTYDATABYTIMERANGE, new Object[] { imei, starttime, endtime });
		LOG.info("imei:" + imei + ";starttime:" + starttime + ";endtime:" + endtime + ";result==>" + result);
		if (result.get("data") != null) {
			JsonNode jsonNode;
			try {
				jsonNode = MAPPER.readTree(result.get("data").toString());
				if ("000".equals(jsonNode.get("status").asText())) {
					ArrayNode rows = (ArrayNode) jsonNode.get("result");
					if (rows != null && rows.size() > 0) {
						JsonNode dataNode = null;
						// 涉及到obd更换，只获得最新obd数据
						if (rows.size() == 1) {
							dataNode = rows.get(0);
						} else if (rows.size() == 2) {
							dataNode = rows.get(1);
						}
						gapMileage = dataNode.get("mileage").asLong();
						float temp  = (float)gapMileage/1000;
    					gapMileage = Long.valueOf(Math.round(temp));
						modifyJobUpdateTime(vehicleId, gapMileage, updateTime);
					}
				}
			} catch (JsonProcessingException e) {
				LOG.error("VehicleTravelMileageServiceImpl.findTripPropertyDataByTimeRange..", e);
			} catch (IOException e) {
				LOG.error("VehicleTravelMileageServiceImpl.findTripPropertyDataByTimeRange..", e);
			}
		}
	}
	
	private void modifyJobUpdateTime(Long vehicleId, Long gapMileage, Date updateTime) {
		vmService.modifyMaintenanceJobTime(gapMileage, updateTime, vehicleId);
	}
	
	private void modifyJobTravelMileageAndUpdateTime(Long vehicleId, Long travelMileage, Date updateTime) {
		vmService.modifyJobTravelMileageAndUpdateTime(travelMileage, updateTime, vehicleId);
	}

}
