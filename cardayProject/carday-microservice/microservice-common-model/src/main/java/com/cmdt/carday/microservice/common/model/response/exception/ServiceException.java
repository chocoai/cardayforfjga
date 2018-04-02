package com.cmdt.carday.microservice.common.model.response.exception;

import com.cmdt.carday.microservice.common.model.response.MessageCode;

public class ServiceException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8727135069045802601L;

	/**
	 * Creates a new instance of ServiceException.
	 *
	 * @param errorMessage
	 *            errorMessage
	 */
	public ServiceException(MessageCode code) {
		super(code);
	}

	public ServiceException(MessageCode code, String message) {
		super(code, message);
	}

	/**
	 * Creates a new instance of ServiceException.
	 *
	 * @param errorMessage
	 *            errorMessage
	 * @param error
	 *            error
	 */
	public ServiceException(MessageCode code, Throwable error) {
		super(code, error);
	}

}
