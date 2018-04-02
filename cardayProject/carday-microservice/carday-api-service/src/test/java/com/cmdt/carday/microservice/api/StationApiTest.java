package com.cmdt.carday.microservice.api;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cmdt.carday.microservice.model.request.station.StationCreateDto;
import com.cmdt.carday.microservice.model.request.station.StationPageDto;
import com.cmdt.carday.microservice.model.request.station.StationUnAssignVehicleDto;
import com.cmdt.carday.microservice.model.request.station.StationUpdateDto;
import com.cmdt.carday.microservice.model.request.station.StationVehicleDto;
import com.cmdt.carday.microservice.model.request.station.StationVehiclePageDto;

@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir="target/generated-snippets")
@RunWith(SpringRunner.class)
@SpringBootTest
public class StationApiTest extends BaseApiTest {

	@Test
	public void findByStationNameTest() throws Exception{
		StationPageDto stationPageDto =new StationPageDto();
		stationPageDto.setCurrentPage(1);
		stationPageDto.setNumPerPage(10);
		stationPageDto.setStationName("光谷广场");
		stationPageDto.setUserId(81L);
		runPost("/station/findByStationName",stationPageDto);
	}
	
	@Test
	public void listByOrgIdTest() throws Exception{
		runGet("/station/149/listByOrgId",null);
	}
	
	@Test
	public void createTest() throws Exception{
		StationCreateDto stationCreateDto=new StationCreateDto();
		/*{"userId":"149","stationName":"test242","position":"武汉市洪山区光谷","city":"武汉市","longitude":"114.407793",
			"latitude":"30.514113","radius":"2.0","carNumber":"44","areaId":1}*/
		stationCreateDto.setUserId(149L);
		stationCreateDto.setStationName("testCreateStation");
		stationCreateDto.setPosition("武汉市洪山区光谷");
		stationCreateDto.setCity("武汉市");
		stationCreateDto.setLongitude("114.407793");
		stationCreateDto.setLatitude("30.514113");
		stationCreateDto.setRadius("2.0");
		stationCreateDto.setCarNumber("44");
		stationCreateDto.setAreaId(1L);
		runPost("/station/create",stationCreateDto);
	}
	
	@Test
	public void findByIdTest() throws Exception{
		runGet("/station/findStationById/533",null);
	}
	
	@Test
	public void updateTest() throws Exception{
		StationUpdateDto stationUpdateDto=new StationUpdateDto();
		/*{"id":491,"userId":149,"stationName":"test2452","city":"武汉市","areaId":420106,"position":"武汉市武昌区户部巷",
			"radius":"2.0","carNumber":"44","longitude":"114.305926","latitude":"30.553369"}*/
		stationUpdateDto.setId(533L);
		stationUpdateDto.setUserId(149L);
		stationUpdateDto.setStationName("testUpdateStation");
		stationUpdateDto.setPosition("武汉市武昌区户部巷");
		stationUpdateDto.setCity("武汉市");
		stationUpdateDto.setLongitude("114.407793");
		stationUpdateDto.setLatitude("30.514113");
		stationUpdateDto.setRadius("2.0");
		stationUpdateDto.setCarNumber("44");
		stationUpdateDto.setAreaId(420106L);
		runPut("/station/update",stationUpdateDto);
	}
	
	@Test
	public void deleteTest() throws Exception{
		runDelete("/station/delete/533",null);
	}
	
	@Test
	public void findStationAssignedVehiclesTest() throws Exception{
		StationVehiclePageDto stationVehiclePageDto=new StationVehiclePageDto();
		stationVehiclePageDto.setCurrentPage(1);
		stationVehiclePageDto.setNumPerPage(10);
		stationVehiclePageDto.setStationId(118L);
		stationVehiclePageDto.setUserId(4L);
		runPost("/station/findStationAssignedVehicles",stationVehiclePageDto);
	}
	
	@Test
	public void findStationAvailableVehiclesTest() throws Exception{
		StationVehiclePageDto stationVehiclePageDto=new StationVehiclePageDto();
		stationVehiclePageDto.setCurrentPage(1);
		stationVehiclePageDto.setNumPerPage(10);
		stationVehiclePageDto.setStationId(118L);
		stationVehiclePageDto.setUserId(4L);
		runPost("/station/findStationAvailableVehicles",stationVehiclePageDto);
	}
	
	@Test
	public void assignVehiclesTest() throws Exception{
		StationVehicleDto stationVehicleDto =new StationVehicleDto();
		stationVehicleDto.setStationId("118");
		stationVehicleDto.setVehicleIds("327,326");
		runPost("/station/assignVehicles",stationVehicleDto);
	}
	
	@Test
	public void unassignVehiclesTest() throws Exception{
		StationUnAssignVehicleDto stationUnAssignVehicleDto=new StationUnAssignVehicleDto();
		stationUnAssignVehicleDto.setStationId("118");
		stationUnAssignVehicleDto.setVehicleId("327");
		runPost("/station/unassignVehicles",stationUnAssignVehicleDto);
	}
	
}
