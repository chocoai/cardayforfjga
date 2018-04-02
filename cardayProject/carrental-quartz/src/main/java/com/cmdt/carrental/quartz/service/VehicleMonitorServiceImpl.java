package com.cmdt.carrental.quartz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.dao.VehicleMonitorDao;
import com.cmdt.carrental.common.model.VehicleQueryDTO;

@Service
public class VehicleMonitorServiceImpl implements VehicleMonitorService{
	
	@Autowired
	private VehicleMonitorDao vehicleMonitorDao;
	
	@Override
	public List<VehicleQueryDTO> findVehicleList() {
		return vehicleMonitorDao.findVehicleList();
	}

}
