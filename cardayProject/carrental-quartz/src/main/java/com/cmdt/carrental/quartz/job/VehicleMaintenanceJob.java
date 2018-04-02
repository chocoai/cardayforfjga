package com.cmdt.carrental.quartz.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.Message.MessageType;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.VehicleMaintenance;
import com.cmdt.carrental.quartz.service.VehicleMaintenanceJobService;
import com.cmdt.carrental.quartz.spring.SpringUtils;

public class VehicleMaintenanceJob extends QuartzJobBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Logger LOG = Logger.getLogger(VehicleMaintenanceJob.class);
	
	@Autowired
	private VehicleMaintenanceJobService vehicleMaintenanceJobService;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		LOG.info("+++++VehicleMaintenanceJob++++++");
		if(vehicleMaintenanceJobService == null){
			vehicleMaintenanceJobService = SpringUtils.getBean("vehicleMaintenanceJobServiceImpl");
		}
		//保养里程到期提醒
		vehicleMaintenanceJobService.excMaintenanceMileageAlert();
		//保养日期到期提醒
		vehicleMaintenanceJobService.excMaintenanceTimeAlert();
	}
	
}
