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
import com.cmdt.carday.microservice.model.request.organization.OrganizationByIdDto;


@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganizationApiTest extends BaseApiTest {
	
	@Test
	public void findByIdTest() throws Exception{
		OrganizationByIdDto dto = new OrganizationByIdDto();
		dto.setUserId(4l);
		dto.setId(511l);
		runPost("/organization/findById", dto);
	}
	
	@Test
	public void findLowerLevelOrgListTest() throws Exception{
		runGet("/organization/findLowerLevelOrgList/4", null);
	}
}
