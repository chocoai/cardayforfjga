package com.cmdt.carrental.mobile.gateway.service;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.model.AlertJsonData;
import com.cmdt.carrental.common.model.AlertMetaData;
import com.cmdt.carrental.common.model.AlertReport;
import com.cmdt.carrental.common.service.VehicleAlertService;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.mobile.gateway.SuperTestCase;

public class JobAlertServiceTestCase extends SuperTestCase{
	
	@Autowired
	private VehicleAlertService service;
	
	@Test
	public void testAlertStatistic(){
		String startDay = "2017-06-27";
		String endDay = "2017-07-26";
		Long orgId = 238L;
		Date startDate = DateUtils.string2Date(startDay + " 00:00:00","yyyy-MM-dd HH:mm:ss");
		Date endDate = DateUtils.string2Date(endDay + " 23:59:59","yyyy-MM-dd HH:mm:ss");
		AlertReport report =service.queryVehicleAlertStatisticsTopX(startDate,endDate,orgId,true,true,5);
		
		List<AlertJsonData> pieList = report.getPieList();
		List<AlertJsonData> columnarList = report.getColumnarList();
		
		System.out.println("---------------------PIE-------------------------");
		for(AlertJsonData pieData: pieList){
			System.out.println(pieData.getAlertType()+"  ---   "+pieData.getAverageNumber());
			for(AlertMetaData meta : pieData.getDataList()){
				System.out.println(meta.getName()+" -- "+meta.getPercent()+" -- "+meta.getData());
			}
		}

		System.out.println("---------------------COLUMN-------------------------");
        for(AlertJsonData colData: columnarList){
        	System.out.println(colData.getAlertType()+"  ---   "+colData.getAverageNumber());
			for(AlertMetaData meta : colData.getDataList()){
				System.out.println(meta.getName()+" -- "+meta.getPercent()+" -- "+meta.getData());
			}
		}
        
        Assert.assertTrue(true);
	}
	
}
