package com.cmdt.carrental.platform.service.biz.service;


import java.sql.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.entity.DevcieCommandConfigRecord;
import com.cmdt.carrental.common.entity.Device;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.integration.DeviceCommandService;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.model.DeviceModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.service.DeviceService;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.platform.service.model.request.device.DeviceCreateDto;
import com.cmdt.carrental.platform.service.model.request.device.DeviceLicenseActiveDto;
import com.cmdt.carrental.platform.service.model.request.device.DeviceLicenseBindDto;
import com.cmdt.carrental.platform.service.model.request.device.DeviceLicenseReactiveDto;
import com.cmdt.carrental.platform.service.model.request.device.DeviceLicenseSuspendDto;
import com.cmdt.carrental.platform.service.model.request.device.DeviceLicenseTerminatedDto;
import com.cmdt.carrental.platform.service.model.request.device.DeviceLicenseUnbinddDto;
import com.cmdt.carrental.platform.service.model.request.device.DeviceListDto;
import com.cmdt.carrental.platform.service.model.request.device.DeviceUpdateDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class PlatformDeviceService {
	private static final Logger LOG = LoggerFactory.getLogger(PlatformDeviceService.class);
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	VehicleService vehicleService;
	
	@Autowired
	ShouqiService shouqiService;
	
	@Autowired
	private DeviceCommandService deviceCommandService;
	
	public PagModel deviceList(User loginUser, DeviceListDto deviceListDto) {
		PagModel pageModel = null;
		DeviceModel deviceModel = new DeviceModel();
		if (null!=loginUser) {
			deviceModel.setImeiNumber(deviceListDto.getImeiNumber());
			deviceModel.setVehicleNumber(deviceListDto.getVehicleNumber());
			deviceModel.setDeviceVendor(deviceListDto.getDeviceVendor());
			deviceModel.setDeviceStatus(deviceListDto.getDeviceStatus());
			deviceModel.setCurrentPage(deviceListDto.getCurrentPage());
			deviceModel.setNumPerPage(deviceListDto.getNumPerPage());
			pageModel = deviceService.deviceList(loginUser, deviceModel);
		}
		return pageModel;
	}
	
	public String create(User loginUser, DeviceCreateDto deviceCreateDto) {
		String msg = "";
		Device device = new Device();
		Vehicle vehicle = new Vehicle();
		
		//验证snNumber是否已经存在
		String snNumber = deviceCreateDto.getSnNumber();
		if (StringUtils.isNotBlank(snNumber) && !deviceService.snNumberIsValid(snNumber)) {
			msg = "该snNumber已经存在!";
			return msg;
		} else {
			device.setSnNumber(snNumber);
		}
		
		//验证imeiNumber是否已经存在
		String imeiNumber = deviceCreateDto.getImeiNumber();
		if (StringUtils.isNotBlank(imeiNumber) && !deviceService.imeiNumberIsValid(imeiNumber)) {
			msg = "该imeiNumber已经存在!";
			return msg;
		} else {
			device.setImeiNumber(imeiNumber);
		}
		
		//验证iccidNumber是否已经存在
		String iccidNumber = deviceCreateDto.getIccidNumber();
		if (StringUtils.isNotBlank(iccidNumber) && !deviceService.iccidNumberIsExist(iccidNumber)) {
			msg = "该iccidNumber已经存在!";
			return msg;
		} else {
			device.setIccidNumber(iccidNumber);
		}
		
		//验证SIM卡是否已经存在
		String simNumber = deviceCreateDto.getSimNumber();
		if (StringUtils.isNotBlank(simNumber) && !deviceService.simNumberIsValid(simNumber)) {
			msg = "该SIM卡已经存在!";
			return msg;
		} else {
			device.setSimNumber(simNumber);
		}
		
		//验证车牌号是否已经绑定
		Long vehicleId = deviceCreateDto.getVehicleId();
		if (null == vehicleId) {
			device.setVehicleId(null);
		} else {
			if (!deviceService.vehicleNumberIsValid(vehicleId)) {
				msg = "该车牌号已经绑定!";
				return msg;
			} else {
				device.setVehicleId(vehicleId);
				//更新车辆表的设备信息
				vehicle.setId(vehicleId);
				vehicle.setSimNumber(deviceCreateDto.getSimNumber());
				//DBJ的设备号是iccidNumber编号,DH设备号是snNumber,其它设备是imeiNumber
				if ("DBJ".equalsIgnoreCase(deviceCreateDto.getDeviceVendor())) {
					vehicle.setDeviceNumber(deviceCreateDto.getIccidNumber());
    			} else if ("DH".equalsIgnoreCase(snNumber)) {
    				vehicle.setDeviceNumber(snNumber);
    			} else {
    				vehicle.setDeviceNumber(imeiNumber);
    			}
			}	
		}
		
		device.setDeviceType(deviceCreateDto.getDeviceType());
		device.setDeviceModel(deviceCreateDto.getDeviceModel());
		device.setDeviceVendorNumber(deviceCreateDto.getDeviceVendorNumber());
		device.setDeviceVendor(deviceCreateDto.getDeviceVendor());
		device.setFirmwareVersion(deviceCreateDto.getFirmwareVersion());
		device.setSoftwareVersion(deviceCreateDto.getSoftwareVersion());
		device.setPurchaseTime(deviceCreateDto.getPurchaseTime());
		device.setMaintainExpireTime(deviceCreateDto.getMaintainExpireTime());
		device.setDeviceStatus(deviceCreateDto.getDeviceStatus());
		device.setDeviceBatch(deviceCreateDto.getDeviceBatch());
		device = deviceService.createDevice(device);
		if(device != null){
			msg = "新增设备成功！";
			vehicleService.updateVichileDevice(vehicle);
		}else{
			msg = "新增设备失败！";
			return msg;
		}
		
		String imei = device.getImeiNumber();
		// DBJ设备 使用ICCID,DH设备使用snNumber
		if ("DBJ".equalsIgnoreCase(device.getDeviceVendor())) {
			imei = device.getIccidNumber();
		} else if ("DH".equalsIgnoreCase(device.getDeviceVendor())) {
			imei = device.getSnNumber();
		}
		// 执行车辆限速下发
		msg = excuteSetLimitSpeed(String.valueOf(deviceCreateDto.getVehicleId()), imei,
				String.valueOf(deviceCreateDto.getLimitSpeed()), String.valueOf(deviceCreateDto.getLatestLimitSpeed()),
				loginUser.getId(), true);
		
		return msg;
	}
	
	public String update(User loginUser, DeviceUpdateDto deviceUpdateDto) {
		String msg = "";
		Device device = new Device();
		Long deviceId = deviceUpdateDto.getId();
		
		//车辆设备更新
		Vehicle vehicle = new Vehicle();
		Vehicle oldvehicle = new Vehicle();
		
		Long newVehicleId = deviceUpdateDto.getVehicleId();
		Long oldVehicleId = deviceService.findDeviceById(deviceId).getVehicleId();
		Device currentDevice = deviceService.findDeviceById(deviceId);
		
		//验证车牌号是否已经绑定
		if (null == newVehicleId) {
			device.setVehicleId(null);
		} else {
			if (!deviceService.vehicleNumberIsValid(newVehicleId) && (!currentDevice.getVehicleId().equals(newVehicleId))){
				msg = "该车牌号已经绑定!";
				return msg;
			} else {
				device.setVehicleId(newVehicleId);
			}
		}
		
		//验证SIM卡是否已经绑定
		String simNumber = deviceUpdateDto.getSimNumber();
		if (StringUtils.isBlank(simNumber)) {
			device.setSimNumber(null);
		} else {
			if (!deviceService.simNumberIsValid(simNumber) && (!currentDevice.getSimNumber().equals(simNumber))) {
				msg = "该simNumber卡已经绑定!";
				return msg;
			}
		}
		
		device.setId(deviceUpdateDto.getId());
		device.setSnNumber(deviceUpdateDto.getSnNumber());
		device.setImeiNumber(deviceUpdateDto.getImeiNumber());
		device.setDeviceType(deviceUpdateDto.getDeviceType());
		device.setDeviceModel(deviceUpdateDto.getDeviceModel());
		device.setDeviceVendorNumber(deviceUpdateDto.getDeviceVendorNumber());
		device.setDeviceVendor(deviceUpdateDto.getDeviceVendor());
		device.setFirmwareVersion(deviceUpdateDto.getFirmwareVersion());
		device.setSoftwareVersion(deviceUpdateDto.getSoftwareVersion());
		device.setPurchaseTime(deviceUpdateDto.getPurchaseTime());
		device.setMaintainExpireTime(deviceUpdateDto.getMaintainExpireTime());
		device.setIccidNumber(deviceUpdateDto.getIccidNumber());
		device.setSimNumber(deviceUpdateDto.getSimNumber());
		device.setDeviceStatus(deviceUpdateDto.getDeviceStatus());
		device.setDeviceBatch(deviceUpdateDto.getDeviceBatch());
		device = deviceService.updateDevice(device);
		
		//解绑，车辆设备清除
		String deviceVendor = deviceUpdateDto.getDeviceVendor();
		if (newVehicleId!=null && oldVehicleId==null) {
			vehicle.setId(newVehicleId);
			//DBJ的设备号是iccidNumber编号，DH设备号是snNumber，其它设备是imeiNumber
			if ("DBJ".equalsIgnoreCase(deviceVendor)) {
				vehicle.setDeviceNumber(deviceUpdateDto.getIccidNumber());
			} else if ("DH".equalsIgnoreCase(deviceVendor)) {
				vehicle.setDeviceNumber(deviceUpdateDto.getSnNumber());
			} else {
				vehicle.setDeviceNumber(deviceUpdateDto.getImeiNumber());
			}
			vehicle.setSimNumber(deviceUpdateDto.getSimNumber());
			vehicleService.updateVichileDevice(vehicle);
		 }else if (newVehicleId==null && oldVehicleId!=null) {
			vehicle.setId(oldVehicleId);
			vehicle.setDeviceNumber(null);
			vehicle.setSimNumber(null);
			vehicleService.updateVichileDevice(vehicle);
		} else if (newVehicleId!=null && !newVehicleId.equals(oldVehicleId)) {
				vehicle.setId(newVehicleId);
				//DBJ的设备号是iccidNumber编号，DH设备号是snNumber，其它设备是imeiNumber
				if ("DBJ".equalsIgnoreCase(deviceVendor)) {
					vehicle.setDeviceNumber(deviceUpdateDto.getIccidNumber());
				} else if ("DH".equalsIgnoreCase(deviceVendor)) {
					vehicle.setDeviceNumber(deviceUpdateDto.getSnNumber());
				} else {
					vehicle.setDeviceNumber(deviceUpdateDto.getImeiNumber());
				}
				vehicle.setSimNumber(deviceUpdateDto.getSimNumber());
				vehicleService.updateVichileDevice(vehicle);
				//更新原来的vehicle的设备信息
				oldvehicle.setId(oldVehicleId);
				oldvehicle.setDeviceNumber(null);
				oldvehicle.setSimNumber(null);
				vehicleService.updateVichileDevice(oldvehicle);
		}
		
		if(device != null){
			msg = "更新设备成功！";
		}else{
			msg = "更新设备失败！";
			return msg;
		}
		
		String imei = device.getImeiNumber();
		// DBJ设备 使用ICCID,DH设备使用snNumber
		if ("DBJ".equalsIgnoreCase(device.getDeviceVendor())) {
			imei = device.getIccidNumber();
		} else if ("DH".equalsIgnoreCase(device.getDeviceVendor())) {
			imei = device.getSnNumber();
		}
		// 执行车辆限速下发
		msg = excuteSetLimitSpeed(String.valueOf(deviceUpdateDto.getVehicleId()), imei,
				String.valueOf(deviceUpdateDto.getLimitSpeed()), String.valueOf(deviceUpdateDto.getLatestLimitSpeed()),
				loginUser.getId(), false);
		
		return msg;
	}
	
	@SuppressWarnings("unchecked")
	public String licenseBind(DeviceLicenseBindDto deviceLicenseBindDto) {
		String msg = "";
		try {
			Long userId = deviceLicenseBindDto.getUserId();
			String imeiNumber = deviceLicenseBindDto.getImeiNumber();
			String iccidNumber = deviceLicenseBindDto.getIccidNumber();
			String snNumber = deviceLicenseBindDto.getSnNumber();
			String devcieVendor = deviceLicenseBindDto.getDeviceVendor();
			String deviceNumber = imeiNumber;
			if ("DBJ".equalsIgnoreCase(devcieVendor)) {
				deviceNumber = iccidNumber;
			} else if ("DH".equalsIgnoreCase(devcieVendor)) {
				deviceNumber = snNumber;
			}
			
			//查询一条可用的license
			String license = shouqiService.queryLicense("NOT_IN_USE", "1");
			Map<String, Object> licenseMap = JsonUtils.json2Object(license, Map.class);
			String status = String.valueOf(licenseMap.get("status"));
			if (status.equals("000")) {
				JsonNode jsonNode = MAPPER.readTree(license);
	    		ArrayNode arrayNodes = (ArrayNode)jsonNode.get("result");
	    		JsonNode node = arrayNodes.get(0);
	    		String licenseNo = node.get("license_no").asText();
	    		//绑定license
	    		String bindRestult = shouqiService.bindLicense(licenseNo, deviceNumber, devcieVendor, userId.toString());
	    		Map<String, Object> licenseBind = JsonUtils.json2Object(bindRestult, Map.class);
	    		if (licenseBind.get("status").equals("000")) {
	    			Device device = new Device();
	        		device.setId(deviceLicenseBindDto.getDeviceId());
	        		device.setStartTime(null);
	        		device.setEndTime(null);
	        		device.setLicenseNumber(licenseNo);
	        		device.setLicenseStatus(null);
	        		deviceService.updateDeviceLicesnse(device);
	        		msg = "License绑定成功！";
	    		} else {
	    			msg = "license绑定失败!"+"  错误状态：" + licenseBind.get("status");
	    			LOG.error("DeviceController licenseBind error!,error status:",licenseBind.get("status"));
	    		}
			} else {
				msg = String.valueOf(licenseMap.get("message"));
			}
		} catch (Exception e) {
    		msg = "License绑定失败！";
    		LOG.error("DeviceController licenseBind error!,caused by:", e);
		}
		return msg;
	}
	
	@SuppressWarnings("unchecked")
	public String licenseActive(DeviceLicenseActiveDto deviceLicenseActiveDto) {
		String msg = "";
    	try{
    		String licenseNo = deviceLicenseActiveDto.getLicenseNo();
    		String startDate = deviceLicenseActiveDto.getStartDate()+" 00:00:00";
    		String endDate = deviceLicenseActiveDto.getEndDate()+" 23:59:59";
    		String userId = deviceLicenseActiveDto.getUserId().toString();
    		String activeRestult = shouqiService.activeLicense(licenseNo, startDate, endDate, userId);
    		Map<String, Object> licenseRet = JsonUtils.json2Object(activeRestult, Map.class);
    		String status = String.valueOf(licenseRet.get("status"));
    		if (status.equals("000")) {
        		Device device = new Device();
        		device.setLicenseNumber(licenseNo);
        		device.setId(deviceLicenseActiveDto.getDeviceId());
        		device.setStartTime(Date.valueOf(deviceLicenseActiveDto.getStartDate()));
        		device.setEndTime(Date.valueOf(deviceLicenseActiveDto.getEndDate()));
        		device.setLicenseStatus(null);
        		deviceService.updateDeviceLicesnse(device);
        		msg = "License激活成功！";
    		} else {
    			msg = "license激活失败!"+"  错误状态：" + licenseRet.get("status");
    			LOG.error("DeviceController licenseActive error!,error status:",licenseRet.get("status"));
    		}
    	}catch(Exception e){
    		msg = "License激活失败！";
    		LOG.error("DeviceController licenseActive error!,caused by:", e);
    	}
    	return msg;
	}
	
	@SuppressWarnings("unchecked")
	public String licenseSuspend(DeviceLicenseSuspendDto deviceLicenseSuspendDto) {
		String msg = "";
    	try{
    		String licenseNo = deviceLicenseSuspendDto.getLicenseNo();
    		String userId = deviceLicenseSuspendDto.getUserId().toString();
    		String suspendRestult = shouqiService.suspendLicense(licenseNo, userId);
    		Map<String, Object> licenseRet = JsonUtils.json2Object(suspendRestult, Map.class);
    		String status = String.valueOf(licenseRet.get("status"));
    		if (status.equals("000")) {		
        		Device device = new Device();
        		device.setId(deviceLicenseSuspendDto.getDeviceId());
        		device.setStartTime(null);
        		device.setEndTime(null);
        		device.setLicenseNumber(licenseNo);
        		device.setLicenseStatus(null);
        		deviceService.updateDeviceLicesnse(device);
        		msg = "License挂起成功！";
    		} else {
    			msg = "license挂起失败!"+"  错误状态：" + licenseRet.get("status");
    			LOG.error("DeviceController licenseSuspend error!,error status:",licenseRet.get("status"));
    		}
    	}catch(Exception e){
    		msg = "License挂起失败！";
    		LOG.error("DeviceController licenseSuspend error!,caused by:", e);
    	}
    	return msg;
	}
	
	@SuppressWarnings("unchecked")
	public String licenseReactive(DeviceLicenseReactiveDto deviceLicenseReactiveDto) {
		String msg = "";
    	try{
    		String licenseNo = deviceLicenseReactiveDto.getLicenseNo();
    		String startDate = deviceLicenseReactiveDto.getStartDate() + " 00:00:00";
    		String endDate = deviceLicenseReactiveDto.getEndDate() + " 23:59:59";
    		String userId = deviceLicenseReactiveDto.getUserId().toString();
    		String reactiveRestult = shouqiService.reactiveLicense(licenseNo, startDate, endDate, userId);
    		Map<String, Object> licenseRet = JsonUtils.json2Object(reactiveRestult, Map.class);
    		String status = String.valueOf(licenseRet.get("status"));
    		if (status.equals("000")) {
        		Device device = new Device();
        		device.setLicenseNumber(licenseNo);
        		device.setId(deviceLicenseReactiveDto.getDeviceId());
        		device.setStartTime(Date.valueOf(startDate));
        		device.setEndTime(Date.valueOf(endDate));
        		device.setLicenseStatus(null);
        		deviceService.updateDeviceLicesnse(device);
        		msg = "License重新激活成功！";
    		} else {
    			msg = "license重新激活失败!"+"  错误状态：" + licenseRet.get("status");
    			LOG.error("DeviceController licenseReactive error!,error status:",licenseRet.get("status"));
    		}
    	}catch(Exception e){
    		msg = "License重新激活失败！";
    		LOG.error("DeviceController licenseReactive error!,caused by:", e);
    	}
    	return msg;
	}
	
	@SuppressWarnings("unchecked")
	public String licenseTerminated(DeviceLicenseTerminatedDto devicelicenseTerminatedDto) {
		String msg = "";
    	try{
    		String licenseNo = devicelicenseTerminatedDto.getLicenseNo();
    		String userId = String.valueOf(devicelicenseTerminatedDto.getUserId());
    		String terminteRestult = shouqiService.terminatedLicense(licenseNo, userId);
    		Map<String, Object> licenseRet = JsonUtils.json2Object(terminteRestult, Map.class);
    		String status = String.valueOf(licenseRet.get("status"));
    		if (status.equals("000")) {
        		Device device = new Device();
        		device.setId(devicelicenseTerminatedDto.getDeviceId());
        		device.setStartTime(null);
        		device.setEndTime(null);
        		device.setLicenseNumber(null);
        		device.setLicenseStatus(null);
        		deviceService.updateDeviceLicesnse(device);
        		msg = "License终止成功！";
    		} else {
    			msg = "license终止失败!"+"  错误状态：" + licenseRet.get("status");
    			LOG.error("DeviceController licenseTerminated error!,error status:",licenseRet.get("status"));
    		}
    	}catch(Exception e){
    		msg = "License终止失败！";
    		LOG.error("DeviceController licenseTerminated error!,caused by:", e);
    	}
    	return msg;
	}
	
	@SuppressWarnings("unchecked")
	public String licenseUnbind(DeviceLicenseUnbinddDto deviceLicenseUnbinddDto) {
		String msg = "";
    	try{
    		String licenseNo = deviceLicenseUnbinddDto.getLicenseNo();
    		String userId = String.valueOf(deviceLicenseUnbinddDto.getUserId());
    		String terminteRestult = shouqiService.unbindLicense(licenseNo, userId);
    		Map<String, Object> licenseRet = JsonUtils.json2Object(terminteRestult, Map.class);
    		String status = String.valueOf(licenseRet.get("status"));
    		if (status.equals("000")) {	
        		Device device = new Device();
        		device.setId(deviceLicenseUnbinddDto.getDeviceId());
        		device.setStartTime(null);
        		device.setEndTime(null);
        		device.setLicenseNumber(null);
        		device.setLicenseStatus(null);
        		deviceService.updateDeviceLicesnse(device);
        		msg = "License解绑成功！";
    		} else {
    			msg = "license解绑失败!"+"  错误状态：" + licenseRet.get("status");
    			LOG.error("DeviceController licenseUnbind error!,error status:",licenseRet.get("status"));
    		}
    	}catch(Exception e){
    		msg = "License解绑失败！";
    		LOG.error("DeviceController licenseUnbind error!,caused by:", e);
    	}
    	return msg;
	}
	
	/**
     * 执行车辆限速下发
     * 
     * @param map	状态map
     * @param vehicleId	车辆ID
     * @param imei	IMEI	
     * @param limitSpeed	车辆限速
     * @param latestLimitSpeed	最新下发限速
     * @param userId	操作用户ID
     * @return
     */
    @SuppressWarnings("unchecked")
	private String excuteSetLimitSpeed(String vehicleId, 
    		String imei, String limitSpeed, String latestLimitSpeed, Long userId, boolean isCreate) {
    	String msg = "";
    	// 绑定设备的车辆存在,且车辆限速与最新下发设备速度不一致时,限制速度下发
    	if(StringUtils.isNotBlank(vehicleId)
    			&& StringUtils.isNotBlank(limitSpeed) && !limitSpeed.equals(latestLimitSpeed)) {
    		// 执行命令
    		String result = deviceCommandService.setSpeedLimitCommand(imei, Integer.valueOf(limitSpeed), 0);
    		
    		Map<String, Object> jsonRst = JsonUtils.json2Object(result, Map.class);
    		String statusCode = "";
    		if (jsonRst.get("status") != null) {
    			statusCode = (String) jsonRst.get("status");
    		}
    		
    		// 设备命令记录
    		DevcieCommandConfigRecord devcieCommandConfigRecord = new DevcieCommandConfigRecord();
    		devcieCommandConfigRecord.setDeviceNumber(imei);
    		devcieCommandConfigRecord.setCommandType("SET_LIMIT_SPEED");
    		devcieCommandConfigRecord.setCommandValue(limitSpeed);
    		java.util.Date utilDate = new java.util.Date();
    		devcieCommandConfigRecord.setCommandSendTime(utilDate);
    		devcieCommandConfigRecord.setUserId(userId);
    		
    		if ("000".equals(statusCode)) {
    			devcieCommandConfigRecord.setCommandExcuteStatus("excuting");
    			devcieCommandConfigRecord.setCommandSendStatus(statusCode);
    			if (isCreate) {
    				msg = "设备添加成功，下发限速成功！";
    			} else {
    				msg = "设备修改成功，下发限速成功！";
    			}
    		} else {
    			devcieCommandConfigRecord.setCommandExcuteStatus("failure");
    			devcieCommandConfigRecord.setCommandSendStatus(statusCode);
    			if (isCreate) {
    				msg = "添加成功，下发限速失败！";
    			} else {
    				msg = "修改成功，下发限速失败！";
    			}
    		}
    		// 插入 设备命令记录表
    		deviceService.addDeviceCommandConfigRecord(devcieCommandConfigRecord);
    	}
    	return msg;
    }
}