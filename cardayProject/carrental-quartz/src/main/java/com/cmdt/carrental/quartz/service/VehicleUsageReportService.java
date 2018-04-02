package com.cmdt.carrental.quartz.service;

import java.util.List;

import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleStasticModel;

public interface VehicleUsageReportService {
	
   List<VehicleModel> findVehicleList();
   
   void saveOrUpdateVehicleStastic(VehicleStasticModel vehicleStasticModel,String currentDateVal);

   void updateVehicleStasticCurrentOwner(Long currentuseOrgId, String currentuseOrgName, String vehicleNumber,
		String deviceNumber, String currentDateVal);

   void cleanCurrentOwner(String vehicleNumber,
			String deviceNumber, String currentDateVal);
	
}
