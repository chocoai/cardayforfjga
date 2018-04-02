package service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.model.VehicleBackModel;
import com.cmdt.carrental.quartz.SuperTestCase;
import com.cmdt.carrental.quartz.filter.VehicleBackFilter;
import com.cmdt.carrental.quartz.service.VehicleAlertJobService;
import com.cmdt.carrental.quartz.spring.SpringUtils;

public class BackJobTestCase extends SuperTestCase{
	
	@Autowired
	private VehicleAlertJobService<VehicleBackModel> vehicleAlertService;
	
	@Autowired
	private VehicleBackFilter<VehicleBackModel> vehicleBackFilter;
	
	@Test
	public void testbackJob(){
		
		vehicleAlertService = SpringUtils.getBean("vehicleAlertJobServiceImpl");
		vehicleBackFilter = SpringUtils.getBean("vehicleBackFilter");
		vehicleAlertService.executeAlertData(vehicleBackFilter);
		
	}

}
