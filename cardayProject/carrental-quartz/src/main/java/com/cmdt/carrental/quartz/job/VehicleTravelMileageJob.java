package com.cmdt.carrental.quartz.job;

import java.io.Serializable;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.cmdt.carrental.quartz.service.VehicleTravelMileageService;
import com.cmdt.carrental.quartz.spring.SpringUtils;

public class VehicleTravelMileageJob extends QuartzJobBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Logger LOG = Logger.getLogger(VehicleTravelMileageJob.class);
	
	@Autowired
	private VehicleTravelMileageService vehicleTravelMileageService;
	
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		LOG.info("+++++VehicleTravelMileageJob.executeInternal..");
		if(vehicleTravelMileageService == null) {
			vehicleTravelMileageService = SpringUtils.getBean("vehicleTravelMileageServiceImpl");
		}
		vehicleTravelMileageService.sysnVehicleTravelMileage();
	}
	
}
