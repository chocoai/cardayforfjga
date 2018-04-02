package com.cmdt.carrental.quartz.job;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.cmdt.carrental.quartz.filter.VehicleBackFilter;
import com.cmdt.carrental.quartz.service.VehicleAlertJobService;
import com.cmdt.carrental.quartz.spring.SpringUtils;

public class VehicleBackAlertJob<T> extends QuartzJobBean implements Serializable{

	private static final long serialVersionUID = 5026903187314571238L;
	
	private Logger LOG = Logger.getLogger(VehicleBackAlertJob.class);
	
	private VehicleAlertJobService<T> vehicleAlertJobService;
	
	private VehicleBackFilter<T> vehicleBackFilter;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		LOG.info("Start execute a vehicleBackAlertJob to filter obd data.");
		if(vehicleAlertJobService == null){
			vehicleAlertJobService = SpringUtils.getBean("vehicleAlertJobServiceImpl");
		}
		if(vehicleBackFilter == null){
			vehicleBackFilter = SpringUtils.getBean("vehicleBackFilter");
		}
		vehicleAlertJobService.executeAlertData(vehicleBackFilter);
	}	
}
