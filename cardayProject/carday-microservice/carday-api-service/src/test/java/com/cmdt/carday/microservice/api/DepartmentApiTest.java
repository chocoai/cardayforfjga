package com.cmdt.carday.microservice.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cmdt.carday.microservice.model.request.department.DepartmentCreateDto;
import com.cmdt.carday.microservice.model.request.department.DepartmentCreditModel;
import com.cmdt.carday.microservice.model.request.department.DepartmentDeleteDto;
import com.cmdt.carday.microservice.model.request.department.DepartmentPageDto;
import com.cmdt.carday.microservice.model.request.department.DepartmentUpdateDto;


@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@RunWith(SpringRunner.class)
@SpringBootTest
public class DepartmentApiTest extends BaseApiTest {
	
	@Test
	public void showListTest() throws Exception{
		runGet("/department/list/4", null);
	}
	
	@Test
	public void showTreeTest() throws Exception{
		runGet("/department/tree/4", null);
	}
	
	@Test
	public void showTreeByOrgIdTest() throws Exception{
		runGet("/department/treeByOrgId/46", null);
	}
	
	@Test
	public void findOrganizationByOrgIdTest() throws Exception{
		runGet("/department/findOrganizationByOrgId/2", null);
	}
	
	@Test
	public void appendChildShowOrganizationByParentIdTest() throws Exception{
		runGet("/department/appendChild/show/2", null);
	}
	
	@Test
	public void createDepartmentTest() throws Exception{
		DepartmentCreateDto dto = new DepartmentCreateDto();
		dto.setName("子研发部");
		dto.setParentId(2L);
		runPost("/department/create", dto);
	}
	
	@Test
	public void updateOrganizationTest() throws Exception{
		Random r = new Random();
		
		DepartmentUpdateDto dto = new DepartmentUpdateDto();
		dto.setName("子研发部"+r.nextInt());
		dto.setParentId(2L);
		dto.setId(5221l);
		runPut("/department/update", dto);
	}
	
	@Test
	public void deleteDepartmentTest() throws Exception{
		DepartmentDeleteDto dto = new DepartmentDeleteDto();
		dto.setId(5222l);
		runDelete("/department/delete", dto);
	}
	
	@Test
	public void listDirectChildrenWithCountTest() throws Exception{
		DepartmentPageDto dto = new DepartmentPageDto();
		dto.setCurrentPage(1);
		dto.setNumPerPage(10);
		dto.setUserId(4l);
		runPost("/department/listDirectChildrenWithCount", dto);
	}
	
	@Test
	public void findTreeCreditByOrgIdTest() throws Exception{
		runGet("/department/findTreeCreditByOrgId/2", null);
	}
	
	@Test
	public void updateCreditTest() throws Exception{
		List<DepartmentCreditModel> modelList = new ArrayList<>();
		DepartmentCreditModel model = new DepartmentCreditModel();
		model.setAvailableCredit(99.9);
		model.setLimitedCredit(12.5);
		model.setOrgId(5221l);
		modelList.add(model);
		runPut("/department/updateCredit", modelList);
	}
	
	@Test
	public void findOrgByIdTest() throws Exception{
		runGet("/department/findOrgById/2", null);
	}
	
}
