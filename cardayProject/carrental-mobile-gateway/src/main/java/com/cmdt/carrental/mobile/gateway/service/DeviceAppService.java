package com.cmdt.carrental.mobile.gateway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.bean.DriveStatus;
import com.cmdt.carrental.common.entity.Device;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.Point;
import com.cmdt.carrental.common.service.DeviceService;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.mobile.gateway.model.BindDeviceAndVehicleDto;
import com.cmdt.carrental.mobile.gateway.model.response.vehicle.VehicleRealtimeResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class DeviceAppService {

	private static final Logger LOG = Logger.getLogger(DeviceAppService.class);
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Autowired
    private ShouqiService shouqiService;
	
	@Autowired
    private DeviceService deviceService;
	
	@Autowired
    private VehicleService vehicleService;
	
	
	public Device getDeviceStaticInfo(String deviceNumber)throws Exception {
		Device device = deviceService.findDeviceByNo(deviceNumber);
		if(device != null){
			Vehicle veh = vehicleService.findVehicleById(device.getVehicleId());
			if(veh != null){
				device.setVehicleNumber(veh.getVehicleNumber());
			}
		}
		
		return device;
	}
	
	public VehicleRealtimeResult getDeviceRuntimeInfo(String deviceNumber) throws Exception {
    	VehicleRealtimeResult vehicleRealtimeResult = new VehicleRealtimeResult();
    	Device device = deviceService.findDeviceByNo(deviceNumber);
    	if(device == null){
    		return null;
    	}
    	
    	vehicleRealtimeResult.setImeiNumber(device.getImeiNumber());
    	vehicleRealtimeResult.setSnNumber(device.getSnNumber());
    	vehicleRealtimeResult.setIccidNumber(device.getIccidNumber());
    	vehicleRealtimeResult.setDeviceType(device.getDeviceType());
    	vehicleRealtimeResult.setDeviceVendor(device.getDeviceVendor());
    	
    	Long vehicleId = device.getVehicleId();
    	Vehicle vehicle = new Vehicle();
    	if (null != device.getVehicleId()) {
    		vehicle = vehicleService.findVehicleById(vehicleId);
    	}
    	vehicleRealtimeResult.setVehicleNumber(vehicle.getVehicleNumber());
    	vehicleRealtimeResult.setBindDeviceVin(vehicle.getVehicleIdentification());
    	
    	String devcieVendor = device.getDeviceVendor();
    	String deviceNo = device.getImeiNumber();
    	if ("DBJ".equalsIgnoreCase(devcieVendor)) {
    		deviceNo = device.getIccidNumber();
		} else if ("DH".equalsIgnoreCase(devcieVendor)) {
			deviceNo = device.getSnNumber();
		}
    	
    	String bindLicenseNumber = device.getLicenseNumber();
    	if (StringUtils.isNotBlank(bindLicenseNumber)) {
    		vehicleRealtimeResult.setBindLicenseNumber(bindLicenseNumber);
    		String resultLicense = shouqiService.queryLicenseByNumber(bindLicenseNumber);
    		Map<String, Object> licenseMap = JsonUtils.json2Object(resultLicense, Map.class);
    		if ("000".equals(String.valueOf(licenseMap.get("status")))) {
    			JsonNode jsonNode = MAPPER.readTree(resultLicense);
        		ArrayNode arrayNodes = (ArrayNode)jsonNode.get("result");
        		JsonNode node = arrayNodes.get(0);
        		if (null != node.get("status").asText()) {
        			vehicleRealtimeResult.setBindLicenseStatus(node.get("status").asText());
        		}
        		if (null != node.get("binding_date").asText()) {
        			vehicleRealtimeResult.setBindTime(node.get("binding_date").asText());
        		}
    		}
    	}
    	
    	Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.GETLOCATIONBYIMEIWITHADDRESS, new Object[]{deviceNo});
    	if (result.get("data") != null) {
    		JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
    		if ("000".equals(jsonNode.get("status").asText())) {
    			JsonNode resultNode = jsonNode.get("result");
    			LOG.debug("imei:" + resultNode.get("imei"));
    			if(resultNode != null){
    				if(resultNode.get("traceTime") != null) {
    					vehicleRealtimeResult.setTime(resultNode.get("traceTime").asText());
    				}
    				if(resultNode.get("speed") != null) {
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
    				if(resultNode.get("longitude") != null) {
    					vehicleRealtimeResult.setLongitude(resultNode.get("longitude").asDouble());
    				}
    				if(resultNode.get("latitude") != null) {
    					vehicleRealtimeResult.setLatitude(resultNode.get("latitude").asDouble());
    				}
    				
    				//trans GPS
    				if(vehicleRealtimeResult.getLongitude() != 0 && vehicleRealtimeResult.getLatitude() != 0){
    					Point p = transGpsPoint(vehicleRealtimeResult.getLongitude(),vehicleRealtimeResult.getLatitude());
    					if(p != null){
    						vehicleRealtimeResult.setLongitude(p.getLon());
    						vehicleRealtimeResult.setLatitude(p.getLat());
    					}
    				}
    				
    				if(resultNode.get("address") != null) {
    					vehicleRealtimeResult.setAddress(resultNode.get("address").asText());
    				}
    				if(resultNode.get("mileage") != null) {
    					vehicleRealtimeResult.setMileage(resultNode.get("mileage").asLong());
    				}
    			}
    		}
    	}
    	return vehicleRealtimeResult;
    }
	
	public Point transGpsPoint(double lon, double lat)throws Exception{
		List<Point> list = new ArrayList<Point>();
		list.add(new Point(lon,lat));
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
		return null;
	}
	
	public String bindDeviceAndVehicle(BindDeviceAndVehicleDto bindDeviceAndVehicleDto) {
		//校验设备是否已经绑定
		String msg = "";
		Long deviceId = Long.valueOf(bindDeviceAndVehicleDto.getDeviceId());
		Device device = deviceService.findDeviceById(deviceId);
		if (null == device) {
			msg = "该设备不存在!";
			return msg;
		}
		if (null != device.getVehicleId()) {
			msg = "该设备已经绑定!";
			return msg;
		}
		
		//校验车辆是否已经绑定
		Long vehicleId = Long.valueOf(bindDeviceAndVehicleDto.getVehicleId());
		Vehicle vehicle = vehicleService.findVehicleById(vehicleId);
		if (null == vehicle) {
			msg = "该车辆不存在!";
			return msg;
		}
		if (StringUtils.isNotBlank(vehicle.getDeviceNumber())) {
			msg = "该车辆已经绑定！";
			return msg;
		}
		
		String devcieVendor = device.getDeviceVendor();
		String deviceNumber = device.getImeiNumber();
		if ("DBJ".equalsIgnoreCase(devcieVendor)) {
			deviceNumber = device.getIccidNumber();
		} else if ("DH".equalsIgnoreCase(devcieVendor)) {
			deviceNumber = device.getSnNumber();
		}
		
		device.setVehicleId(vehicleId);
		vehicle.setSimNumber(device.getSimNumber());
		vehicle.setDeviceNumber(deviceNumber);
		if (!deviceService.bindDeviceAndVehicle(device, vehicle)) {
			msg = "绑定失败";
		}
		return msg;
	}
	
	public String unbindDeviceAndVehicle(BindDeviceAndVehicleDto bindDeviceAndVehicleDto) {
		//校验设备是否存在
		String msg = "";
		Long deviceId = Long.valueOf(bindDeviceAndVehicleDto.getDeviceId());
		Device device = deviceService.findDeviceById(deviceId);
		if (null == device) {
			msg = "该设备不存在!";
			return msg;
		}
		if (null == device.getVehicleId()) {
			msg = "该设备没有绑定车辆!";
			return msg;
		}
		
		//校验车辆是否存在
		Long vehicleId = Long.valueOf(bindDeviceAndVehicleDto.getVehicleId());
		Vehicle vehicle = vehicleService.findVehicleById(vehicleId);
		if (null == vehicle) {
			msg = "该车辆不存在!";
			return msg;
		}
		if (StringUtils.isBlank(vehicle.getDeviceNumber())) {
			msg = "该车辆没有绑定设备！";
			return msg;
		}
		
		device.setVehicleId(null);
		vehicle.setSimNumber(null);
		vehicle.setDeviceNumber(null);
		if (!deviceService.unbindDeviceAndVehicle(device, vehicle)) {
			msg = "解绑绑定失败";
		}
		return msg;
	}
}
