package com.cmdt.carrental.mobile.gateway.api.inf;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cmdt.carrental.mobile.gateway.model.User;

//@Path("/restfulServiceExample")
@Produces(MediaType.APPLICATION_JSON)
public interface RestfulServiceExample {
	
	@POST
	@Path("/logon")
	public Response logon(@Valid @NotNull User user);
	
	@GET
	@Path("/queryData")
	public String queryData();
	
}