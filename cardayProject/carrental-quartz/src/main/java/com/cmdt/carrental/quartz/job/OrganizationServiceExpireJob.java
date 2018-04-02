package com.cmdt.carrental.quartz.job;

import java.io.Serializable;
import java.util.Date;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.quartz.spring.SpringUtils;


public class OrganizationServiceExpireJob extends QuartzJobBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Logger LOG = Logger.getLogger(OrganizationServiceExpireJob.class);
	
	 @Autowired
	 private OrganizationService organizationService;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		if(organizationService == null){
			organizationService = SpringUtils.getBean("organizationServiceImpl");
		}
		
		
		String nowDate = DateUtils.getNowDate();
    	organizationService.updateOrganizationServiceStatus(nowDate);
	}

}
