package com.cmdt.carrental.common.exception;

public class CommonServiceException extends Exception {




	/**
	 * 
	 */
	private static final long serialVersionUID = -7412681381058995389L;

	/**
     * Creates a new instance of WSException.
     *
     * @param errorMessage errorMessage
     */
    public CommonServiceException(String errorMessage) {
        super(errorMessage);
    }
    
    /**
     * Creates a new instance of WSException.
     *
     * @param errorMessage errorMessage
     * @param error error
     */
    public CommonServiceException(String errorMessage, Throwable error) {
        super(errorMessage, error);
    }
}
