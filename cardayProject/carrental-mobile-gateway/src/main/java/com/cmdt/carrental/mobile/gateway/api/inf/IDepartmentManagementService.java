package com.cmdt.carrental.mobile.gateway.api.inf;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cmdt.carrental.mobile.gateway.model.request.department.DepartmentDto;

@Produces(MediaType.APPLICATION_JSON)
public interface IDepartmentManagementService {
	
	public Response queryDepartmentList(@PathParam("userId") Long userId);
	
	public Response getDepartment(@Valid @NotNull DepartmentDto dto);

}
