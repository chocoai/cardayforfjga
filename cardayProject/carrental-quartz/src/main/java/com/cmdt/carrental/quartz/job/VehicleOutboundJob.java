package com.cmdt.carrental.quartz.job;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.cmdt.carrental.quartz.filter.VehicleOutboundFilter;
import com.cmdt.carrental.quartz.service.VehicleAlertJobService;
import com.cmdt.carrental.quartz.spring.SpringUtils;

public class VehicleOutboundJob<T> extends QuartzJobBean implements Serializable{

	private static final long serialVersionUID = 5026903187314571238L;
	
	private Logger LOG = Logger.getLogger(VehicleOutboundJob.class);
	
	private VehicleAlertJobService<T> vehicleAlertJobService;
	
	private VehicleOutboundFilter<T> vehicleOutboundFilter;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		LOG.info("Start execute a vehicleOutboundJob to filter obd data.");
		if(vehicleAlertJobService == null){
			vehicleAlertJobService = SpringUtils.getBean("vehicleAlertJobServiceImpl");
		}
		if(vehicleOutboundFilter == null){
			vehicleOutboundFilter = SpringUtils.getBean("vehicleOutboundFilter");
		}
		vehicleAlertJobService.executeAlertData(vehicleOutboundFilter);
	}	
}
