package com.cmdt.carrental.platform.service.mapper;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.cxf.phase.PhaseInterceptorChain;

import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.common.WsResponse;

public class InvokeClientExceptionMapper implements ExceptionMapper<ClientErrorException> {
	
	@Override
	public Response toResponse(ClientErrorException ex) {
		PhaseInterceptorChain.getCurrentMessage().getExchange().put(Constants.INTECEPTOR_SKIP_FLAG, Boolean.TRUE);
		WsResponse<?> wsResponse = new WsResponse<>();
		ResponseBuilder rb = Response.status(ex.getResponse().getStatus());  
		rb.type(Constants.API_HEADER_TYPE_JSON_UTF8);
		wsResponse.setStatus(Constants.STATUS_BAD_REQUEST);
		wsResponse.getMessages().add(ex.getMessage());
		rb.entity(wsResponse);  
		//rb.language(Locale.SIMPLIFIED_CHINESE);  
		Response r = rb.build();
		return r;
	}

}
