package com.cmdt.carday.microservice.api.portal;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmdt.carday.microservice.api.BaseApi;
import com.cmdt.carday.microservice.biz.service.PlatformOrganizationService;
import com.cmdt.carday.microservice.common.model.response.WsResponse;
import com.cmdt.carday.microservice.model.request.organization.OrganizationByIdDto;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.model.OrgListModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@Api("/organization")
@SwaggerDefinition(info = @Info(title = "organization API", version = "1.0.0"))
@Validated
@RestController  
@RequestMapping("/organization")
public class OrganizationApi extends BaseApi {
	
	@Autowired
    private PlatformOrganizationService organizationService;
	
	/**
     * [超级管理员,租户管理员] 根据id查询企业信息
     * @param id
     * @return
     */
	@ApiOperation(value = "根据id查询企业信息", response = WsResponse.class)
    @PostMapping("/findById")
	public Organization findById(@RequestBody @Valid @NotNull OrganizationByIdDto organizationByIdDto){
		return organizationService.findById(
				getUserById(organizationByIdDto.getUserId()), organizationByIdDto.getId());
	}
	
	/**
     * 查询所属企业或部门下拉框list
     * [租户管理员]查询所属企业
     * [企业管理员]查询所属部门
     * @param loginUser
     * @param json
     * @return
     */
	@ApiOperation(value = "查询所属企业或部门下拉框list", response = WsResponse.class)
    @GetMapping("/findLowerLevelOrgList/{userId}")
	public List<OrgListModel> findLowerLevelOrgList(
			@ApiParam(value = "用户ID", required = true) @PathVariable("userId")Long userId){
		return organizationService.findLowerLevelOrgList(getUserById(userId));
	}
	
/*	@GET
	@Path("/list/{userId}")
	public Response showList(@PathParam("userId")Long userId){
		WsResponse<List<Organization>> wsResponse = new WsResponse<>();
		try{
			wsResponse.setResult(organizationService.showList(getUserById(userId)));
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("OrganizationApi showList by userId error, cause by: : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@GET
	@Path("/auditList/{userId}")
	public Response showAuditList(@PathParam("userId")Long userId){
		WsResponse<List<Organization>> wsResponse = new WsResponse<>();
		try{
			wsResponse.setResult(organizationService.showAuditList(getUserById(userId)));
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("OrganizationApi showAuditList by userId error, cause by: : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@POST
	@Path("/auditCheck")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response checkOrganization(@Valid @NotNull UpdateOrganizationStatusDto checkOrganizationDto){
		WsResponse<Object> wsResponse = new WsResponse<>();
		try{
			Map<String,Object> map = organizationService.checkOrganization(
					getUserById(checkOrganizationDto.getUserId()), checkOrganizationDto);
			wsResponse.getMessages().add(map.get(Constants.MSG_STR).toString());
			wsResponse.setStatus(map.get(Constants.STATUS_STR).toString());
		}catch(Exception e){
			LOG.error("OrganizationApi checkOrganization error, cause by: : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@POST
	@Path("/serviceActivate")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response activateOrganizationService(@Valid @NotNull ActivateOrganizationDto activateOrganizationDto){
		WsResponse<Object> wsResponse = new WsResponse<>();
		try{
			Map<String,Object> map = organizationService.activateOrganizationService(
					getUserById(activateOrganizationDto.getUserId()), activateOrganizationDto);
			wsResponse.getMessages().add(map.get(Constants.MSG_STR).toString());
			wsResponse.setStatus(map.get(Constants.STATUS_STR).toString());
		}catch(Exception e){
			LOG.error("OrganizationApi activateOrganizationService error, cause by: : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@POST
	@Path("/serviceTerminate")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response terminateOrganizationService(@Valid @NotNull UpdateOrganizationStatusDto updateOrganizationDto){
		WsResponse<Object> wsResponse = new WsResponse<>();
		try{
			Map<String,Object> map = organizationService.terminateOrganizationService(
					getUserById(updateOrganizationDto.getUserId()), updateOrganizationDto);
			wsResponse.getMessages().add(map.get(Constants.MSG_STR).toString());
			wsResponse.setStatus(map.get(Constants.STATUS_STR).toString());
		}catch(Exception e){
			LOG.error("OrganizationApi terminateOrganizationService error, cause by: : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@POST
	@Path("/listAuditByOrgName")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listAuditByOrgName(@Valid @NotNull OrganizationListByNameDto organizationListByNameDto){
		WsResponse<List<Organization>> wsResponse = new WsResponse<>();
		try{
			wsResponse.setResult(organizationService.listAuditByOrgName(
					getUserById(organizationListByNameDto.getUserId()), organizationListByNameDto.getName()));
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("OrganizationApi listAuditByOrgName by orgName error, cause by: : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@POST
	@Path("/listByOrgName")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response listByOrgName(@Valid @NotNull OrganizationListByNameDto organizationListByNameDto){
		WsResponse<List<Organization>> wsResponse = new WsResponse<>();
		try{
			wsResponse.setResult(organizationService.listOrgByName(
					getUserById(organizationListByNameDto.getUserId()), organizationListByNameDto.getName()));
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("OrganizationApi listByOrgName by orgName error, cause by: : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}*/
	
/*	@POST
	@Path("/appendChild")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createOrganization(@Valid @NotNull CreateOrganizationDto createOrganizationDto){
		WsResponse<Object> wsResponse = new WsResponse<>();
		try{
			organizationService.create(getUserById(createOrganizationDto.getUserId()), createOrganizationDto);
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("OrganizationApi createOrganization error, cause by: : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@POST
	@Path("/updateOrganization")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateOrganization(@Valid @NotNull UpdateOrganizationDto updateOrganizationDto){
		WsResponse<Object> wsResponse = new WsResponse<>();
		try{
			organizationService.update(getUserById(updateOrganizationDto.getUserId()), updateOrganizationDto);
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("OrganizationApi updateOrganization error, cause by: : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@POST
	@Path("/deleteOrganization")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteOrganization(@Valid @NotNull DeleteOrganizationDto deleteOrganizationDto){
		WsResponse<Object> wsResponse = new WsResponse<>();
		try{
			organizationService.delete(getUserById(deleteOrganizationDto.getUserId()), deleteOrganizationDto.getId());
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("OrganizationApi deleteOrganization error, cause by: : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}*/
	
	/*@GET
	@Path("/auditedList/{userId}")
	public Response showAuditedList(@PathParam("userId")Long userId){
		WsResponse<List<Organization>> wsResponse = new WsResponse<>();
		try{
			wsResponse.setResult(organizationService.showAuditedList(getUserById(userId)));
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("OrganizationApi showAuditedList by userId error, cause by: : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@GET
	@Path("/auditedByAdminList/{userId}")
	public Response showAuditedByAdminList(@PathParam("userId")Long userId){
		WsResponse<List<Organization>> wsResponse = new WsResponse<>();
		try{
			wsResponse.setResult(organizationService.showAuditedByAdminList(getUserById(userId)));
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("OrganizationApi showAuditedByAdminList by userId error, cause by: : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}*/
	
}
