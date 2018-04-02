package com.cmdt.carrental.common.dao;

import java.util.List;

import com.cmdt.carrental.common.model.VehicleQueryDTO;

public interface VehicleMonitorDao {

	List<VehicleQueryDTO> findVehicleList();

}
