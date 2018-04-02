package com.cmdt.carrental.common.service;

import java.util.List;
import java.util.Map;

import com.cmdt.carrental.common.entity.DevcieCommandConfigRecord;
import com.cmdt.carrental.common.entity.Device;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.model.DevcieCommandConfigRecordModel;
import com.cmdt.carrental.common.model.DeviceModel;
import com.cmdt.carrental.common.model.PagModel;

public interface DeviceService {
	//新增设备
	public Device createDevice(Device device);
	public Boolean imeiNumberIsValid(String imeiNumber);
	public Boolean vehicleNumberIsValid(Long vehicleId);
	public PagModel deviceList(User loginUser, DeviceModel deviceModel);
	public PagModel findAll(Long orgid, DeviceModel deviceModel);
	public Device updateDevice(Device device);
	public Device findDeviceById(Long id);
	public Device updateDeviceLicesnse(Device device);
	
	//for device app
	public Device findDeviceByNo(String devNo);
	
	public Boolean simNumberIsValid(String simNumber);
	
	public Boolean snNumberIsValid(String snNumber);
	
	public Boolean iccidNumberIsExist(String iccidNumber);
	
	//根据imei从设备组中查询对应的liceseNumber,DBJ的imei字段为iccidNumber,非DBJ的imei字段为imeiNumber，然后通过liceseNumber调用首汽接口获得license状态
	public Map<String,String> findDeviceStatusList(List<String> deviceNumberList);
	
	public void updateLatestLimitSpeed(String imei, int latestLimitSpeed);
	
	public DevcieCommandConfigRecord addDeviceCommandConfigRecord(DevcieCommandConfigRecord devcieCommadConfigRecord);
	public DevcieCommandConfigRecord updateDeviceCommandConfigRecord(DevcieCommandConfigRecord devcieCommadConfigRecord);
	
	//根据imei查询，状态为WAITTING，最新的一条记录
	public DevcieCommandConfigRecordModel findLatestWaittingCommandByImei(String imeiNumber);
	
	//设备绑定车辆，分别更新车辆、设备表
	Boolean bindDeviceAndVehicle(Device device, Vehicle vehicle);
	Boolean unbindDeviceAndVehicle(Device device, Vehicle vehicle);
	
}
