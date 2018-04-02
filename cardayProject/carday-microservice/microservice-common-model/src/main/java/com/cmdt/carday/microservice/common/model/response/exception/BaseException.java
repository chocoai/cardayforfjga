package com.cmdt.carday.microservice.common.model.response.exception;

import com.cmdt.carday.microservice.common.model.response.MessageCode;

import java.util.ArrayList;
import java.util.List;

public class BaseException extends RuntimeException {

	/**
	 * Generated serial version ID.
	 */
	private static final long serialVersionUID = 4292562406632090198L;

	private MessageCode code = MessageCode.COMMON_UNKNOWN_ERROR;
	
	private List<String> messageParams = new ArrayList<>();

	public BaseException(MessageCode code) {
		super();
		this.code = code;
	}

	public BaseException(MessageCode code,String message) {
		super(message);
		this.code = code;
	}

	public BaseException(MessageCode code, Throwable cause) {
		super(code.getMsg(),cause);
		this.code = code;
	}


	public BaseException(MessageCode code, List<String> messageParams) {
		super();
		this.code = code;
		this.messageParams.addAll(messageParams);
	}

	public BaseException(Throwable cause) {
		super(cause);
	}

	public MessageCode getCode() {
		return code;
	}

	public void setCode(MessageCode code) {
		this.code = code;
	}

	public List<String> getMessageParams() {
		return messageParams;
	}

	public void setMessageParams(List<String> messageParams) {
		this.messageParams = messageParams;
	}
}