package com.cmdt.carrental.platform.service.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.cxf.phase.PhaseInterceptorChain;
import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.common.WsResponse;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

public class InvokeFaultExceptionMapper implements ExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception ex) {
		PhaseInterceptorChain.getCurrentMessage().getExchange().put(Constants.INTECEPTOR_SKIP_FLAG, Boolean.TRUE);
		WsResponse<?> wsResponse = new WsResponse<>();
		ResponseBuilder rb =null;
		Response r=null;
		/*if (ex instanceof ServerException) {
			rb=Response.status(Response.Status.BAD_REQUEST);
		}else  if(ex instanceof InvalidFormatException){
			rb=Response.status(Response.Status.INTERNAL_SERVER_ERROR);  
		}*/
		if(ex instanceof ServerException){
			rb=Response.status(Response.Status.OK);  
			rb.type(MediaType.APPLICATION_JSON_UTF8_VALUE);
			wsResponse.setStatus(Constants.STATUS_INNER_ERROR);
			wsResponse.getMessages().add(ex.getMessage());
			rb.entity(wsResponse);
			r = rb.build();
		}else if (ex instanceof InvalidFormatException) {
			rb=Response.status(Response.Status.BAD_REQUEST);  
			rb.type(MediaType.APPLICATION_JSON_UTF8_VALUE);
			wsResponse.setStatus(Constants.STATUS_INNER_ERROR);
			wsResponse.getMessages().add("参数输入错误！");
			rb.entity(wsResponse);
			r = rb.build();
		}
		return r;
		//rb.language(Locale.SIMPLIFIED_CHINESE);  
	}

}