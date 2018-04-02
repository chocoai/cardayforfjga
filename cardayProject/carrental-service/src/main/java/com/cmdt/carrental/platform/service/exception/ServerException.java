package com.cmdt.carrental.platform.service.exception;

public class ServerException extends RuntimeException {

	private static final long serialVersionUID = 7607640803750403555L;

	public ServerException() {
		super();
	}

	public ServerException(String message) {
		super(message);
	}

	public ServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServerException(Throwable cause) {
		super(cause);
	}
}