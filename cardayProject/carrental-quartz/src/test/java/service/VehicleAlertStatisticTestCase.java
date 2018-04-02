package service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.quartz.SuperTestCase;
import com.cmdt.carrental.quartz.service.VehicleAlertJobService;

public class VehicleAlertStatisticTestCase  extends SuperTestCase{

	@Autowired
    private VehicleAlertJobService vehicleAlertService;
	
	@Test
	public void testAlertStatisticJob(){
//		vehicleAlertService.doStatistics();
	}
}
