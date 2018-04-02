package com.cmdt.carday.microservice.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cmdt.carday.microservice.model.request.marker.MarkerAssignVehicleDto;
import com.cmdt.carday.microservice.model.request.marker.MarkerCreateDto;
import com.cmdt.carday.microservice.model.request.marker.MarkerPageDto;
import com.cmdt.carday.microservice.model.request.marker.MarkerUnassignVehicleDto;
import com.cmdt.carday.microservice.model.request.marker.MarkerUpdateDto;
import com.cmdt.carday.microservice.model.request.marker.MarkerVehiclePageDto;

@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@RunWith(SpringRunner.class)
@SpringBootTest
public class MarkerApiTest extends BaseApiTest {
	
	@Test
	public void listMarkerTest() throws Exception{
		MarkerPageDto markerPageDto=new MarkerPageDto();
		markerPageDto.setUserId(153L);
		markerPageDto.setCurrentPage(1);
		markerPageDto.setNumPerPage(10);
		runPost("/marker/findByName",markerPageDto);
	}
	
	@Test
	public void createMarkerTest() throws Exception{
		MarkerCreateDto markerCreateDto=new MarkerCreateDto();
		markerCreateDto.setUserId(153L);
		markerCreateDto.setMarkerName("test123456");
		markerCreateDto.setPosition("江岸区");
		markerCreateDto.setCity("武汉市");
		markerCreateDto.setRegionId(420100L);
		markerCreateDto.setType("0");
		markerCreateDto.setPattern("[[{\"lng\":114.316649,\"lat\":30.58903},{\"lng\":114.310517,\"lat\":30.578777},{\"lng\":114.280415,\"lat\":30.597386},{\"lng\":114.278165,\"lat\":30.602941},{\"lng\":114.292105,\"lat\":30.612255},{\"lng\":114.264083,\"lat\":30.647072},{\"lng\":114.287168,\"lat\":30.66391},{\"lng\":114.331372,\"lat\":30.688747},{\"lng\":114.336575,\"lat\":30.680789},{\"lng\":114.341193,\"lat\":30.679475},{\"lng\":114.351553,\"lat\":30.689945},{\"lng\":114.369575,\"lat\":30.698586},{\"lng\":114.374764,\"lat\":30.704453},{\"lng\":114.397679,\"lat\":30.700554},{\"lng\":114.405418,\"lat\":30.691257},{\"lng\":114.36015,\"lat\":30.65928},{\"lng\":114.353307,\"lat\":30.640746},{\"lng\":114.337089,\"lat\":30.623431},{\"lng\":114.316649,\"lat\":30.58903},{\"lng\":114.316649,\"lat\":30.58903}]]!");
		markerCreateDto.setLongitude("114.316649");
		markerCreateDto.setLatitude("30.58903");
		
		runPost("/marker/create",markerCreateDto);
		
	}
	
	@Test
	public void findByIdTest() throws Exception{
		runGet("/marker/findMarkerById/701",null);
	}
	
	@Test
	public void updateMarkerTest() throws Exception{
		MarkerUpdateDto markerUpdateDto =new MarkerUpdateDto();
		markerUpdateDto.setId(701L);
		markerUpdateDto.setUserId(153L);
		markerUpdateDto.setMarkerName("testUpdate");
		runPut("/marker/update",markerUpdateDto);
	}
	
	@Test
	public void deleteMarkerTest() throws Exception{
		runDelete("/marker/delete/701",null);
	}
	
	@Test
	public void findMarkerAvialiableVehiclesTest() throws Exception{
		MarkerVehiclePageDto markerVehiclePageDto=new MarkerVehiclePageDto();
		markerVehiclePageDto.setUserId(153L);
		markerVehiclePageDto.setMarkerId(701L);
		markerVehiclePageDto.setNumPerPage(10);
		markerVehiclePageDto.setCurrentPage(1);
		runPost("/marker/findMarkerAvialiableVehicles",markerVehiclePageDto);
	}
	
	@Test
	public void findMarkerAssignedVehiclesTest() throws Exception{
		MarkerVehiclePageDto markerVehiclePageDto =new MarkerVehiclePageDto();
		markerVehiclePageDto.setUserId(153L);
		markerVehiclePageDto.setMarkerId(701L);
		markerVehiclePageDto.setNumPerPage(10);
		markerVehiclePageDto.setCurrentPage(1);
		runPost("/marker/findMarkerAssignedVehicles",markerVehiclePageDto);
	}
	
	@Test
	public void assignVehiclesTest() throws Exception{
		MarkerAssignVehicleDto markerAssignVehicleDto =new MarkerAssignVehicleDto();
		markerAssignVehicleDto.setMarkerId(701L);
		markerAssignVehicleDto.setVehicleIds("2695");
		runPost("/marker/assignVehicles",markerAssignVehicleDto);
	}
	
	@Test
	public void unassignVehiclesTest() throws Exception{
		MarkerUnassignVehicleDto markerUnassignVehicleDto =new MarkerUnassignVehicleDto();
		markerUnassignVehicleDto.setMarkerId(701L);
		markerUnassignVehicleDto.setVehicleId("2695");
		runPost("/marker/unassignVehicles",markerUnassignVehicleDto);
	}

}
