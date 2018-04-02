package com.cmdt.carday.microservice.common.model.response.exception;

import com.cmdt.carday.microservice.common.model.response.MessageCode;

public class ApiException extends BaseException {

	public ApiException(MessageCode status) {
		super(status);
	}
	private static final long serialVersionUID = 2192729676981073577L;
	public ApiException(MessageCode status, Throwable cause) {
		super(status, cause);
	}
	
	public ApiException(MessageCode status, String message){
		super(status, message);
	}
}
