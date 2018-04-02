package com.cmdt.carrental.common.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.model.VehicleQueryDTO;

@Repository
public class VehicleMonitorDaoImpl implements VehicleMonitorDao{

	
	 @Autowired
	 private JdbcTemplate jdbcTemplate;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<VehicleQueryDTO> findVehicleList() {
		List<VehicleQueryDTO> retList = new ArrayList<VehicleQueryDTO>();
		
		List<VehicleQueryDTO> retTmpList = jdbcTemplate.query("select vehicle_number,device_number from busi_vehicle where device_number is not null", new BeanPropertyRowMapper(VehicleQueryDTO.class));
		if(retTmpList != null && retTmpList.size() > 0){
			retList.addAll(retTmpList);
		}
		return retList;
	}
}
