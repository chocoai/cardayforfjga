package service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.quartz.SuperTestCase;
import com.cmdt.carrental.quartz.service.VehicleMaintenanceJobService;

public class VehicleMaintenanceServiceCase extends SuperTestCase{
	
	@Autowired
	private VehicleMaintenanceJobService service;
	
	@Test
	public void testExcMaintenanceMileageAlert(){
//		service.excMaintenanceMileageAlert();
		service.excMaintenanceTimeAlert();
	}

}
