package com.cmdt.carday.microservice.common.model.response.exception;

import com.cmdt.carday.microservice.common.model.response.MessageCode;

public class DBException extends BaseException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8727135069045802601L;

	public DBException(MessageCode code) {
		super(code);
	}

	public DBException(MessageCode code, String message) {
		super(code, message);
	}

	/**
	 * Creates a new instance of DBException.
	 *
	 * @param errorMessage
	 *            errorMessage
	 * @param error
	 *            error
	 */
	public DBException(MessageCode code, Throwable error) {
		super(code, error);
	}
}
