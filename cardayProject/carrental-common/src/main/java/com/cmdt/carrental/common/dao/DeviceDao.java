package com.cmdt.carrental.common.dao;

import java.util.List;

import com.cmdt.carrental.common.entity.DevcieCommandConfigRecord;
import com.cmdt.carrental.common.entity.Device;
import com.cmdt.carrental.common.entity.DeviceStatus;
import com.cmdt.carrental.common.model.DevcieCommandConfigRecordModel;
import com.cmdt.carrental.common.model.DeviceModel;
import com.cmdt.carrental.common.model.PagModel;

public interface DeviceDao {
	public Device createDevice(Device device);
	public Boolean imeiNumberIsValid (String imeiNumber);
	public Boolean vehicleNumberIsValid (Long vehicleId);
	public Boolean iccidNumberIsExist(String iccidNumber);
	public PagModel findAll(Long orgId, DeviceModel device);
	public Device updateDevice(Device device);
	public Device findDeviceById(Long id);
	public Device updateDeviceLicesnse(Device device);
	public Device findDeviceByNo(String devNo);
	public List<Device> findDeviceBySimNumber(String simNumber);
	public List<Device> findDeviceBySnNumber(String snNumber);
	public List<DeviceStatus> findLicenseListByImeiList(List<String> deviceNumberList);
	public void updateLatestLimitSpeed(String imei, int latestLimitSpeed);
	public DevcieCommandConfigRecord addDeviceCommandConfigRecord(DevcieCommandConfigRecord deviceCommandConfigRecord);
	public DevcieCommandConfigRecord updateDeviceCommandConfigRecord(DevcieCommandConfigRecord deviceCommandConfigRecord);
	public DevcieCommandConfigRecordModel findLatestWaittingCommandByImei(String imeiNumber);
	public Device updateDeviceOfVehcleId(Device device);
}
