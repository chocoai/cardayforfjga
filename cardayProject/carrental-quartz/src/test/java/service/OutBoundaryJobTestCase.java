package service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.model.OutboundMarkerModel;
import com.cmdt.carrental.quartz.SuperTestCase;
import com.cmdt.carrental.quartz.filter.VehicleOutboundFilter;
import com.cmdt.carrental.quartz.service.VehicleAlertJobService;
import com.cmdt.carrental.quartz.spring.SpringUtils;

public class OutBoundaryJobTestCase extends SuperTestCase{
	
	@Autowired
	private VehicleAlertJobService<OutboundMarkerModel> vehicleAlertService;
	
	@Autowired
	private VehicleOutboundFilter<OutboundMarkerModel> vehicleOutboundFilter;
	
	@Test
	public void testOutBoundaryJob(){
		vehicleAlertService = SpringUtils.getBean("vehicleAlertJobServiceImpl");
		vehicleOutboundFilter = SpringUtils.getBean("vehicleOutboundFilter");
		vehicleAlertService.executeAlertData(vehicleOutboundFilter);
	}

}
