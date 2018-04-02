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

import com.cmdt.carday.microservice.model.request.employee.EmployeeCreateDto;
import com.cmdt.carday.microservice.model.request.employee.EmployeeDelToDep;
import com.cmdt.carday.microservice.model.request.employee.EmployeeListUnallocatedDep;
import com.cmdt.carday.microservice.model.request.employee.EmployeeSearchDto;
import com.cmdt.carday.microservice.model.request.employee.EmployeeSetToDep;
import com.cmdt.carday.microservice.model.request.employee.EmployeeUpdateDto;


@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeApiTest extends BaseApiTest {
	
	@Test
	public void listEmployeeByOrgIdByPageInSearchTest() throws Exception{
		EmployeeSearchDto dto = new EmployeeSearchDto();
		dto.setCurrentPage(1);
		dto.setNumPerPage(10);
		dto.setUserId(4l);
		dto.setOrganizationId(54l);
		dto.setRealname("企业");
		dto.setPhone("5");
		dto.setSelfDept(true);
		dto.setChildDept(true);
		runPost("/employee/search", dto);
	}
	
	@Test
	public void listByDeptTest() throws Exception{
		runGet("/employee/listByDept/569", null);
	}
	
	@Test
	public void createEmployeeTest() throws Exception{
		Random r = new Random();
		
		EmployeeCreateDto dto = new EmployeeCreateDto();
		dto.setUserId(4l);
		dto.setUsername("empJUnitTest"+r.nextInt(100));
		dto.setPassword("123456");
		dto.setRoleId(6l);
		dto.setOrganizationId(1l);
		dto.setRealname("员工JunitTest");
		dto.setPhone("13501010"+r.nextInt(10)+r.nextInt(10)+r.nextInt(10));
		dto.setCity("武汉");
		dto.setMonthLimitvalue(-1d);
		dto.setOrderCustomer("0");
		dto.setOrderSelf("1");
		dto.setOrderApp("0");
		dto.setOrderWeb("0");
		runPost("/employee/create", dto);
	}
	
	@Test
	public void showUpdateFormTest() throws Exception{
		runGet("/employee/show/2520", null);
	}
	
	@Test
	public void updateEmployeeTest() throws Exception{
		Random r = new Random();
		
		EmployeeUpdateDto dto = new EmployeeUpdateDto();
		dto.setId(2520l);
		dto.setUserId(4l);
		dto.setRoleId(6l);
		dto.setOrganizationId(1l);
		dto.setRealname("员工JunitTest");
		dto.setPhone("13501010"+r.nextInt(10)+r.nextInt(10)+r.nextInt(10));
		dto.setCity("武汉"+r.nextInt(10));
		dto.setMonthLimitvalue(-1d);
		dto.setOrderCustomer("1");
		dto.setOrderSelf("0");
		dto.setOrderApp("1");
		dto.setOrderWeb("1");
		runPut("/employee/update", dto);
	}
	
	@Test
	public void deleteEmployeeTest() throws Exception{
		runDelete("/employee/delete/2530", null);
	}
	
	@Test
	public void downloadFileTest() throws Exception{
		runGet("/employee/loadTemplate", null);
	}
	
	@Test
	public void importEmplyeeTest() throws Exception{
		Map<String, String> map = new HashMap<>();
		map.put("userId", "4");
		runPost("/employee/import", null);
	}
	
	@Test
	public void listByDeptIdTest() throws Exception{
		EmployeeSearchDto dto = new EmployeeSearchDto();
		dto.setCurrentPage(1);
		dto.setNumPerPage(10);
		dto.setUserId(4l);
		dto.setOrganizationId(54l);
		dto.setRealname("企业");
		dto.setUsername("auto");
		dto.setPhone("5");
		dto.setRoleId(6l);
		runPost("/employee/listByDeptId", dto);
	}
	
	@Test
	public void listUnallocatedDepEmpTest() throws Exception{
		EmployeeListUnallocatedDep dto = new EmployeeListUnallocatedDep();
		dto.setCurrentPage(1);
		dto.setNumPerPage(10);
		dto.setUserId(4l);
		dto.setRealname("测");
		dto.setUsername("t");
		dto.setPhone("132");
		runPost("/employee/listUnallocatedDepEmp", dto);
	}
	
	@Test
	public void setEmployeeToDepTest() throws Exception{
		EmployeeSetToDep dto = new EmployeeSetToDep();
		dto.setIds("2520,2543");
		dto.setAllocateDepId(998l);
		runPut("/employee/setEmployeeToDep", dto);
	}
	
	@Test
	public void deleteEmployeeToDepTest() throws Exception{
		EmployeeDelToDep dto = new EmployeeDelToDep();
		dto.setUserId(4l);
		dto.setIds("2520,2543");
		runDelete("/employee/deleteEmployeeToDep", dto);
	}
	
}
