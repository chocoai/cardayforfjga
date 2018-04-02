package com.cmdt.carrental.mobile.gateway.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;

import com.cmdt.carrental.mobile.gateway.common.Constants;
import com.cmdt.carrental.mobile.gateway.common.WsResponse;
import com.cmdt.carrental.mobile.gateway.exception.ServerException;

public class InvokeFaultExceptionMapper implements ExceptionMapper<ServerException> {

	@Override
	public Response toResponse(ServerException ex) {
		WsResponse<?> wsResponse = new WsResponse<>();
		ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);  
		//rb.type("application/json;charset=UTF-8");
		wsResponse.setStatus(Constants.STATUS_INNER_ERROR);
		ex.getMessage();
		wsResponse.getMessages().add(ex.getMessage());
		rb.entity(wsResponse);  
		//rb.language(Locale.SIMPLIFIED_CHINESE);  
		Response r = rb.build();  
		return r;
	}

}