package com.cmdt.carrental.quartz.service;

import java.util.List;

import com.cmdt.carrental.common.model.VehicleQueryDTO;

public interface VehicleMonitorService {

	List<VehicleQueryDTO> findVehicleList();
	
}
