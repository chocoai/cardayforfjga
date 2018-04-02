package com.cmdt.carday.microservice.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cmdt.carday.microservice.model.request.driver.DriverDelToDep;
import com.cmdt.carday.microservice.model.request.driver.DriverListByUnallocatedDep;
import com.cmdt.carday.microservice.model.request.driver.DriverListDto;
import com.cmdt.carday.microservice.model.request.driver.DriverSetToDep;

@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@RunWith(SpringRunner.class)
@SpringBootTest
public class DriverApiTest extends BaseApiTest {
	
	@Test
	public void listDriverTest() throws Exception{
		DriverListDto dto = new DriverListDto();
		dto.setCurrentPage(1);
		dto.setNumPerPage(10);
		dto.setUserId(4l);
		dto.setOrganizationId(1l);
		dto.setSelfDept(true);
		dto.setChildDept(true);
		dto.setRealname("司机");
		dto.setUsername("driver");
		dto.setPhone("3");
		runPost("/driver/list", dto);
	}
	
	@Test
	public void listByDeptTest() throws Exception{
		runGet("/driver/listByDept/569/3", null);
	}
	
	@Test
	public void createDriverTest() throws Exception{
		Random r = new Random();
		
		Map<String, String> map = new HashMap<>();
		map.put("userId", "4");
		map.put("driverUsername", "DriverJUnitTest"+r.nextInt());
		map.put("driverPassword", "123456");
		map.put("roleId", "7");
		map.put("organizationId", "1");
		map.put("realname", "司机TEST724");
		map.put("phone", "13232942"+r.nextInt(10)+r.nextInt(10)+r.nextInt(10));
		map.put("licenseNumber", r.nextInt(10)+"2222233332221211"+r.nextInt(10));
		map.put("licenseType", "C1");
		map.put("licenseBegintime", "2010-01-01");
		map.put("licenseExpiretime", "2020-01-01");
		map.put("depId", "-1");
		map.put("stationId", "-1");
		runPost("/driver/create", map);
	}
	
	@Test
	public void showUpdateFormTest() throws Exception{
		runGet("/driver/show/181", null);
	}
	
	@Test
	public void updateDriverTest() throws Exception{
		Map<String, String> map = new HashMap<>();
		map.put("userId", "4");
		map.put("id", "2379");
		map.put("roleId", "7");
		map.put("organizationId", "1");
		map.put("realname", "司机TEST724");
		map.put("phone", "15232940022");
		map.put("licenseNumber", "112220333322212113");
		map.put("licenseType", "A1");
		map.put("licenseBegintime", "2010-01-01");
		map.put("licenseExpiretime", "2020-01-01");
		map.put("depId", "-1");
		map.put("stationId", "-1");
		runPut("/driver/update", map);
	}
	
	@Test
	public void deleteDriverTest() throws Exception{
		runDelete("/driver/delete/2380", null);
	}
	
	@Test
	public void downloadFileTest() throws Exception{
		runGet("/driver/loadTemplate", null);
	}
	
	@Test
	public void importDriverTest() throws Exception{
		Map<String, String> map = new HashMap<>();
		map.put("userId", "4");
		runPost("/driver/import", null);
	}
	
	@Test
	public void getImageUrlTest() throws Exception{
		runGet("/driver/getImageUrl", null);
	}
	
	@Test
	public void listByDeptIdTest() throws Exception{
		DriverListDto dto = new DriverListDto();
		dto.setCurrentPage(1);
		dto.setNumPerPage(10);
		dto.setOrganizationId(2l);
		dto.setUserId(4l);
		dto.setRealname("司机");
		dto.setUsername("driver");
		dto.setPhone("3");
		runPost("/driver/listByDeptId", dto);
	}
	
	@Test
	public void listUnallocatedDepDriverTest() throws Exception{
		DriverListByUnallocatedDep dto = new DriverListByUnallocatedDep();
		dto.setCurrentPage(1);
		dto.setNumPerPage(10);
		dto.setUserId(4l);
		dto.setRealname("司机");
		dto.setUsername("driver");
		dto.setPhone("3");
		runPost("/driver/listUnallocatedDepDriver", dto);
	}
	
	@Test
	public void setDriverToDepTest() throws Exception{
		DriverSetToDep dto = new DriverSetToDep();
		dto.setAllocateDepId(999l);
		dto.setIds("2379");
		runPut("/driver/setDriverToDep", dto);
	}
	
	@Test
	public void deleteDriverToDepTest() throws Exception{
		DriverDelToDep dto = new DriverDelToDep();
		dto.setIds("2379");
		runDelete("/driver/deleteDriverToDep", dto);
	}
}
