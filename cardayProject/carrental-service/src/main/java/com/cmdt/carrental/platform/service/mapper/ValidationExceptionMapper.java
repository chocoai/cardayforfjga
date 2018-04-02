package com.cmdt.carrental.platform.service.mapper;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.cxf.phase.PhaseInterceptorChain;

import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.common.WsResponse;


public class ValidationExceptionMapper implements ExceptionMapper<javax.validation.ConstraintViolationException> {
	
	public ValidationExceptionMapper(){
	}

    public Response toResponse(javax.validation.ConstraintViolationException cex) {
       PhaseInterceptorChain.getCurrentMessage().getExchange().put(Constants.INTECEPTOR_SKIP_FLAG, Boolean.TRUE);
       WsResponse<?> wsResponse = new WsResponse<>();
       wsResponse.setStatus(Constants.STATUS_BAD_REQUEST);
       Set<ConstraintViolation<?>> validationErrors = cex.getConstraintViolations();
       Iterator<ConstraintViolation<?>> it = validationErrors.iterator();  
       while (it.hasNext()) {  
    	   ConstraintViolation<?> error = it.next();
    	   String[] propertyInfo = error.getPropertyPath().toString().split("\\.");
    	   String propertyName = propertyInfo!=null&&propertyInfo.length>0?propertyInfo[propertyInfo.length-1]:error.getPropertyPath().toString();
    	   wsResponse.getMessages().add(propertyName+" - "+error.getMessage());
       }
       return Response.status(400).entity(wsResponse).build(); 
       
    }
}