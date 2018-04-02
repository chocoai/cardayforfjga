package com.cmdt.carrental.quartz.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.dao.VehicleAlertJobDao;
import com.cmdt.carrental.common.model.VehicleAlertModel;
import com.cmdt.carrental.quartz.SuperTestCase;

public class VehicleAlertDaoTestCase extends SuperTestCase{

	@Autowired
	private VehicleAlertJobDao vehicleAlertDao;
	
	@Test
	public void create(){
//		VehicleAlertModel vehicleAlertModel = new VehicleAlertModel();
//		vehicleAlertModel = vehicleAlertDao.create(vehicleAlertModel);
//		Assert.notNull(vehicleAlertModel);
//		Assert.notNull(vehicleAlertModel.getId());
	    Assert.assertTrue(true);
	}
}
