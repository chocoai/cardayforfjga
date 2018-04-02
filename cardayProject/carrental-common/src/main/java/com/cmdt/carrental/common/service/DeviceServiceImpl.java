package com.cmdt.carrental.common.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmdt.carrental.common.dao.DeviceDao;
import com.cmdt.carrental.common.dao.VehicleDao;
import com.cmdt.carrental.common.entity.DevcieCommandConfigRecord;
import com.cmdt.carrental.common.entity.Device;
import com.cmdt.carrental.common.entity.DeviceStatus;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.model.DevcieCommandConfigRecordModel;
import com.cmdt.carrental.common.model.DeviceModel;
import com.cmdt.carrental.common.model.LicenseDTO;
import com.cmdt.carrental.common.model.LicenseResModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.util.JsonUtils;


@Service
public class DeviceServiceImpl implements DeviceService {
	@Autowired
    private DeviceDao deviceDao;
	
	@Autowired
    private ShouqiService shouqiService;
	
	@Autowired
    private VehicleDao vehicleDao;
	
	@Override
	public Device createDevice(Device device) {
		// TODO Auto-generated method stub
		return deviceDao.createDevice(device);
	}

	@Override
	public Boolean imeiNumberIsValid(String imeiNumber) {
		return deviceDao.imeiNumberIsValid(imeiNumber);
	}

	@Override
	public Boolean vehicleNumberIsValid(Long vehicleId) {
		// TODO Auto-generated method stub
		return deviceDao.vehicleNumberIsValid(vehicleId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public PagModel deviceList(User loginUser, DeviceModel deviceModel) {
		PagModel pageModel=null;
		//系统管理员
		if(loginUser.isSuperAdmin()){
			Long orgId=loginUser.getOrganizationId();
			pageModel=findAll(orgId, deviceModel);
			if(pageModel != null) {
				List<DeviceModel> deviceList = pageModel.getResultList();
				if(null!=deviceList && !deviceList.isEmpty()) {
					for(DeviceModel device : deviceList) {
						//调用shouqi接口查询license状态
						queryLicenseStatus(device);
					}
				}
			}
		}
		return pageModel;
	}
	
	private void queryLicenseStatus(DeviceModel device) {
		if(StringUtils.isNotEmpty(device.getLicenseNumber())) {
			String result = shouqiService.queryLicenseByNumber(device.getLicenseNumber());//模糊匹配
			LicenseResModel licenseResModel = JsonUtils.json2Object(result, LicenseResModel.class);
			if(licenseResModel.getResult()!=null && licenseResModel.getResult().size() > 0) {
				for(LicenseDTO temp : licenseResModel.getResult()) {
					if(device.getLicenseNumber().equals(temp.getLicense_no()) //条件筛选
							&& StringUtils.isNotEmpty(licenseResModel.getStatus())) {
						device.setLicenseStatus(temp.getStatus());
						break;
					}
				}
			}
		}
	}

	@Override
	public PagModel findAll(Long orgid, DeviceModel deviceModel) {
		return deviceDao.findAll(orgid, deviceModel);
	}

	@Override
	public Device updateDevice(Device device) {
		return deviceDao.updateDevice(device);
	}
	
	@Override
	public void updateLatestLimitSpeed(String imei, int latestLimitSpeed) {
		deviceDao.updateLatestLimitSpeed(imei, latestLimitSpeed);
	}

	@Override
	public Device findDeviceById(Long id) {
		return deviceDao.findDeviceById(id);
	}

	@Override
	public Device updateDeviceLicesnse(Device device) {
		return deviceDao.updateDeviceLicesnse(device);				 
	}

	@Override
	public Device findDeviceByNo(String devNo) {
		return deviceDao.findDeviceByNo(devNo);
	}
	
	@Override
	public Boolean simNumberIsValid(String simNumber) {
		List<Device> list = deviceDao.findDeviceBySimNumber(simNumber);
		if(list == null || list.size() == 0) {
			return true;
		}
		return false;
	}
	
	@Override
	public Boolean snNumberIsValid(String snNumber) {
		List<Device> list = deviceDao.findDeviceBySnNumber(snNumber);
		if(list == null || list.size() == 0) {
			return true;
		}
		return false;
	}

	@Override
	public Map<String,String> findDeviceStatusList(List<String> deviceNumberList) {
		Map<String,String> retMap = new HashMap<String,String>();
		
		List<DeviceStatus> licenseList = deviceDao.findLicenseListByImeiList(deviceNumberList);
		if(licenseList != null && licenseList.size() > 0){
			for(DeviceStatus deviceStatus : licenseList){
			    //return actual number according to devices from different vendor
				String actualImei = deviceStatus.getActualImei();
				String licenseNumber = deviceStatus.getLicenseNumber();
				if(StringUtils.isNotEmpty(licenseNumber)){
					String licenseStatus = getLicenseStatusByLicenseNo(licenseNumber);
					if(StringUtils.isNotEmpty(licenseStatus)){
						if("IN_USE".equals(licenseStatus)){
							if(StringUtils.isNotEmpty(actualImei)){
								retMap.put(actualImei, licenseStatus);
							}
						}
					}
				}
			}
		}
		return retMap;
	}
	
	/**
	 * 查询license状态
	 * @param licenseNo
	 * @return
	 */
	public String getLicenseStatusByLicenseNo(String licenseNo){
		//测试可以使用一下语句,直接将imei设置为IN_USE状态
//		return "IN_USE";
		String result = shouqiService.queryLicenseByNumber(licenseNo);
		if(StringUtils.isEmpty(result)){
			return "";
		}
		
		LicenseResModel licenseResModel = JsonUtils.json2Object(result, LicenseResModel.class);

		if(null ==licenseResModel){
			return "";
		}
		String status = licenseResModel.getStatus();
		if(StringUtils.isEmpty(status)){
			return "";
		}
		
		if("000".equals(status)){
			if(licenseResModel.getResult()!=null && licenseResModel.getResult().size() > 0) {
				LicenseDTO licenseDTO = licenseResModel.getResult().get(0);
				if(licenseDTO != null){
					String licenseStatus = licenseDTO.getStatus();
					if(StringUtils.isNotEmpty(licenseStatus)){
						return licenseStatus;
					}
				}
			}
		}
		return "";
	}

	@Override
	public DevcieCommandConfigRecord addDeviceCommandConfigRecord(DevcieCommandConfigRecord devcieCommandConfigRecord) {
		return deviceDao.addDeviceCommandConfigRecord(devcieCommandConfigRecord);
		
	}

	@Override
	public DevcieCommandConfigRecord updateDeviceCommandConfigRecord(DevcieCommandConfigRecord devcieCommadConfigRecord) {
		return deviceDao.updateDeviceCommandConfigRecord(devcieCommadConfigRecord);
		
	}

	@Override
	public DevcieCommandConfigRecordModel findLatestWaittingCommandByImei(String imeiNumber) {
		return deviceDao.findLatestWaittingCommandByImei(imeiNumber);
	}

	@Override
	public Boolean iccidNumberIsExist(String iccidNumber) {
		return deviceDao.iccidNumberIsExist(iccidNumber);
	}

	@Override
	@Transactional
	public Boolean bindDeviceAndVehicle(Device device, Vehicle vehicle) {
		Device retDevice = deviceDao.updateDeviceOfVehcleId(device);
		Vehicle retVehicle = vehicleDao.updateVehicleOfDevice(vehicle);
		return (null!=retDevice && null!=retVehicle);
	}

	@Override
	@Transactional
	public Boolean unbindDeviceAndVehicle(Device device, Vehicle vehicle) {
		Device retDevice = deviceDao.updateDeviceOfVehcleId(device);
		Vehicle retVehicle = vehicleDao.updateVehicleOfDevice(vehicle);
		return (null!=retDevice && null!=retVehicle);
	}
	
}
