package com.cmdt.carday.microservice.api;




import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cmdt.carday.microservice.model.request.vehicle.VehicleAssignDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleCreateDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleListDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleMarkerPageDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleUpdateDto;

@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@RunWith(SpringRunner.class)
@SpringBootTest
public class VehicleApiTest extends BaseApiTest {
	
	@Test
	public void listTest() throws Exception{
		VehicleListDto vehicleListDto= new VehicleListDto();
		vehicleListDto.setUserId(1L);
		vehicleListDto.setDeptId(1L);
		vehicleListDto.setSelfDept(true);
		vehicleListDto.setChildDept(true);
		vehicleListDto.setCurrentPage(1);
		vehicleListDto.setNumPerPage(10);
		vehicleListDto.setArrangeEnt(-1L);
		vehicleListDto.setFromOrgId(-1L);
		vehicleListDto.setVehicleNumber("鄂A7QQ29");
		vehicleListDto.setVehicleType(-1L);
		runPost("/vehicle/list",vehicleListDto);
	}
	
	@Test
	public void createTest() throws Exception{
		VehicleCreateDto dto =new VehicleCreateDto();
		dto.setUserId(4L);
		dto.setVehicleNumber("鄂C14347");
		dto.setVehicleBrand("红色");
		dto.setVehicleIdentification("CDS12232222222222");
		dto.setVehicleOutput("3.5L");
		dto.setCurrentuseOrgId(2L);
		dto.setCurrentuseOrgName("市场部");
		dto.setVehicleBuyTime(new Date());
		dto.setInsuranceExpiredate(new Date());
		dto.setInspectionExpiredate(new Date());
		dto.setVehiclePurpose("生产用车");
		dto.setLimitSpeed(2);
		dto.setVehicleType("1");
		dto.setVehicleModel("Q5");
		dto.setTheoreticalFuelCon(3D);
		dto.setSeatNumber(5);
		dto.setVehicleFuel("90(京89)");
		dto.setProvince("420000");
		dto.setCity("420100");
		dto.setStartTime("09:00");
		dto.setEndTime("18:00");
		runPost("/vehicle/create",dto);
	}
	
	@Test
	public void findVehicleByIdTest() throws Exception{
		runGet("/vehicle/findVehicleById/111",null);
	}
	
	@Test
	public void updateTest() throws Exception{
		VehicleUpdateDto dto =new VehicleUpdateDto();
		dto.setVehicleBrand("宝马");
		dto.setVehicleColor("黑色");
		dto.setId(1350L);
		runPut("/vehicle/update",dto);
	}
	
	@Test
	public void vehicleAssigneOrgTest() throws Exception{
		VehicleAssignDto dto =new VehicleAssignDto();
		dto.setVehicleId(325L);
		dto.setOrgId(2L);
		runPost("/vehicle/vehicleAssigneOrg",dto);
	}
	
	@Test
	public void driverAllocateTest() throws Exception{
		runGet("/vehicle/driverAllocate/vehicleId/325/driverId/12", null);
	}
	
	@Test
	public void findVehicleAssignedMarkersTest() throws Exception{
		Map<String, String> params =new HashMap<String, String>();
		params.put("vehicleNumber", "111");
		params.put("currentPage", "1");
		params.put("numPerPage", "10");
	    runPost("/vehicle/findVehicleAssignedMarkers",params);
				
	}
	
	@Test
	public void findVehicleAvialiableMarkersTest() throws Exception{
		VehicleMarkerPageDto vehicleMarkerPageDto=new VehicleMarkerPageDto();
		vehicleMarkerPageDto.setCurrentPage(1);
		vehicleMarkerPageDto.setNumPerPage(10);
		vehicleMarkerPageDto.setUserId(153L);
		vehicleMarkerPageDto.setVehicleId(1350L);
		runPost("/vehicle/findVehicleAvialiableMarkers",vehicleMarkerPageDto);
	}
	
	@Test
	public void assignMarkersTest() throws Exception{
		Map<String, String> params =new HashMap<String, String>();
		params.put("vehicleId", "1273");
		params.put("markerIds", "12,13");
	    runPost("/vehicle/assignMarkers",params);
	}
	
	@Test
	public void unassignVehiclesTest() throws Exception{
		Map<String, String> params =new HashMap<String, String>();
		params.put("vehicleId", "1273");
		params.put("markerId", "12");
	    runPost("/vehicle/unassignMarkers",params);
	}
	
	@Test
	public void listDeptVehiclesTest() throws Exception{
		runGet("/vehicle/listDeptVehicle/1",null);
	}
	
	
}
