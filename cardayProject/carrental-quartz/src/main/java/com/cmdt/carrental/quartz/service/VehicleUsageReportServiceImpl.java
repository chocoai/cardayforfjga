package com.cmdt.carrental.quartz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.dao.VehicleUsageReportDao;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleStasticModel;

@Service
public class VehicleUsageReportServiceImpl implements VehicleUsageReportService{

	@Autowired
	private VehicleUsageReportDao vehicleUsageReportDao;
	
	@Override
	public List<VehicleModel> findVehicleList() {
		return vehicleUsageReportDao.findVehicleList();
	}

	@Override
	public void saveOrUpdateVehicleStastic(VehicleStasticModel vehicleStasticModel,String currentDateVal) {
		vehicleUsageReportDao.saveOrUpdateVehicleStastic(vehicleStasticModel,currentDateVal);
	}

	@Override
	public void updateVehicleStasticCurrentOwner(Long currentuseOrgId, String currentuseOrgName, String vehicleNumber,
			String deviceNumber, String currentDateVal) {
		vehicleUsageReportDao.updateVehicleStasticCurrentOwner(currentuseOrgId,currentuseOrgName,vehicleNumber,deviceNumber,currentDateVal);
	}

	@Override
	public void cleanCurrentOwner(String vehicleNumber, String deviceNumber, String currentDateVal) {
		vehicleUsageReportDao.cleanCurrentOwner(vehicleNumber,deviceNumber,currentDateVal);
	}
	
	
}
