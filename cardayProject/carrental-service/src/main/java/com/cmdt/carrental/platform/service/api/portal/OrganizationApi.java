package com.cmdt.carrental.platform.service.api.portal;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.model.OrgListModel;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformOrganizationService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.platform.service.model.request.organization.OrganizationByIdDto;

@Produces(MediaType.APPLICATION_JSON)
public class OrganizationApi extends BaseApi {
	
	private static final Logger LOG = LoggerFactory.getLogger(OrganizationApi.class);
	
	@Autowired
    private PlatformOrganizationService organizationService;
	
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
	
	/**
     * [超级管理员,租户管理员] 根据id查询企业
     * @param id
     * @return
     */
	@POST
	@Path("/findById")
	@Consumes(MediaType.APPLICATION_JSON)
	public Organization findById(@Valid @NotNull OrganizationByIdDto organizationByIdDto){
		try{
			return organizationService.findById(
					getUserById(organizationByIdDto.getUserId()), organizationByIdDto.getId());
		}catch(Exception e){
			LOG.error("OrganizationApi findById by id error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
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
	
	/**
     * 所属企业或部门下拉框list
     * [租户管理员]查询所属企业
     * [企业管理员]查询所属部门
     * @param loginUser
     * @param json
     * @return
     */
	@GET
	@Path("/findLowerLevelOrgList/{userId}")
	public List<OrgListModel> findLowerLevelOrgList(@PathParam("userId")Long userId){
		try{
			return organizationService.findLowerLevelOrgList(getUserById(userId));
		}catch(Exception e){
			LOG.error("OrganizationApi findLowerLevelOrgList by userId error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
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
