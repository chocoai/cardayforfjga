package com.cmdt.carrental.mobile.gateway.api.impl;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.cmdt.carrental.mobile.gateway.api.inf.RestfulServiceExample;
import com.cmdt.carrental.mobile.gateway.common.Constants;
import com.cmdt.carrental.mobile.gateway.common.WsResponse;
import com.cmdt.carrental.mobile.gateway.model.User;

@Service("restfulServiceExample")
public class RestfulServiceExampleImpl implements RestfulServiceExample {
	
	public RestfulServiceExampleImpl(){
		System.out.println("RestfulServiceExampleImpl Service Init");
	}
	
	@POST
	@Path("/logon")
	@Consumes(MediaType.APPLICATION_JSON)  
	//@Produces(MediaType.APPLICATION_JSON)
	public Response logon(@Valid @NotNull User user){
		WsResponse<User> wsResponse = new WsResponse<>(Constants.STATUS_SUCCESS,user);
		return Response.ok( wsResponse ).build();
	} 
	
	@GET
	@Path("/queryData")
	public String queryData(){
		return "fuck off";
	}

}