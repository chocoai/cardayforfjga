package com.cmdt.carrental.quartz.job;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.cmdt.carrental.quartz.service.VehicleAnnualInspectionJobService;
import com.cmdt.carrental.quartz.spring.SpringUtils;

public class VehicleAnnualInspectionJob extends QuartzJobBean implements Serializable{
	
	private Logger LOG = Logger.getLogger(VehicleAnnualInspectionJob.class);
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private VehicleAnnualInspectionJobService service;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		LOG.info("++++VehicleAnnualInspectionJob++++");
		if(service == null) {
			service = SpringUtils.getBean("vehicleAnnualInspectionJobServiceImpl");
		}
		service.excInsuranceTimeAlert();
		service.excInspectionTimeAlert();
	}

}
