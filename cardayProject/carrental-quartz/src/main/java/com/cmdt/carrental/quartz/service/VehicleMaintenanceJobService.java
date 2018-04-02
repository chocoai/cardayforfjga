package com.cmdt.carrental.quartz.service;

import java.util.List;
import java.util.Map;

import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.VehicleMaintenance;

public interface VehicleMaintenanceJobService<T> {
	
	public void excMaintenanceMileageAlert();
	public void excMaintenanceTimeAlert();
}
