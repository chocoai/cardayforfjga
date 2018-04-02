package service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.quartz.SuperTestCase;
import com.cmdt.carrental.quartz.service.VehicleAnnualInspectionJobService;

public class VehicleAnnualInspectionJobServiceCase extends SuperTestCase{
	
	@Autowired
	private VehicleAnnualInspectionJobService service;
	
	@Test
	public void testVehicleAnnualInspectionAlert() {
		service.excInspectionTimeAlert();
	}

}
