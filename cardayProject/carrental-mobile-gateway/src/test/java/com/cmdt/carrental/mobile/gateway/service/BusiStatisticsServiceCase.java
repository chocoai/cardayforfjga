package com.cmdt.carrental.mobile.gateway.service;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.mobile.gateway.SuperTestCase;
import com.cmdt.carrental.mobile.gateway.model.DeptDataModel;
import com.cmdt.carrental.mobile.gateway.model.request.stat.DaysStatDto;

/**
 * if you want use unit test, comment cxf configuration in Spring-config.xml first.
 * @author KevinPei
 *
 */
public class BusiStatisticsServiceCase extends SuperTestCase{
	
	@Autowired
	private BusiStatisticsService service;
	
	@Test
	public void loadTop3DStat() throws Exception{
		DaysStatDto dto = new DaysStatDto();
		dto.setStartDate(1491637696929L);
		dto.setEndDate(1492156096929L);
		dto.setDataType("warning");
	//	dto.setUserId("4");
		List<DeptDataModel> models = service.loadTop3DStat(dto);
		
		for(DeptDataModel data : models){
			System.out.println(data);
		}
		
		Assert.assertTrue(true);
	}

}
