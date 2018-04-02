package com.cmdt.carrental.common.dao;

import java.util.List;

import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleStasticModel;

public interface VehicleUsageReportDao {

	List<VehicleModel> findVehicleList();

	void saveOrUpdateVehicleStastic(VehicleStasticModel vehicleStasticModel,String currentDateVal);

	void updateVehicleStasticCurrentOwner(Long currentuseOrgId, String currentuseOrgName, String vehicleNumber,
			String deviceNumber, String currentDateVal);

	void cleanCurrentOwner(String vehicleNumber, String deviceNumber, String currentDateVal);

}
