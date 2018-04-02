package com.cmdt.carrental.mobile.gateway.api.impl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.mobile.gateway.api.inf.IDepartmentManagementService;
import com.cmdt.carrental.mobile.gateway.common.Constants;
import com.cmdt.carrental.mobile.gateway.common.WsResponse;
import com.cmdt.carrental.mobile.gateway.model.request.department.DepartmentDto;
import com.cmdt.carrental.mobile.gateway.model.response.department.DepartmentModel;
import com.cmdt.carrental.mobile.gateway.model.response.department.DepartmentRetModel;
import com.cmdt.carrental.mobile.gateway.service.DepartmentAppService;

public class DepartmentManagementService implements IDepartmentManagementService{
	
	private static final Logger LOG = LoggerFactory.getLogger(DepartmentManagementService.class);
	
	@Autowired
	private DepartmentAppService departmentAppService;
	
	@GET
	@Path("/queryDepartmentList/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response queryDepartmentList(@PathParam("userId") Long userId) {
		WsResponse<List<DepartmentModel>> wsResponse = departmentAppService.showDepartmentList(userId);
		return Response.ok( wsResponse ).build();
	}
	
	@POST
	@Path("/getDepartment")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	@Override
	public Response getDepartment(DepartmentDto dto){
		WsResponse<List<DepartmentRetModel>> wsResponse = new WsResponse<>();
		try
        {
			wsResponse.setResult(departmentAppService.queryDepatmentList(dto));
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			
        }
        catch (Exception e)
        {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method getDepartment error:", e);
        }
        return Response.ok(wsResponse).build();
	}
	
	
}
