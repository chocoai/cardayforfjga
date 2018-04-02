package com.cmdt.carday.geo.exception;

public class WSException extends Exception
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -7412681381058995389L;
    
    /**
     * Creates a new instance of WSException.
     *
     * @param errorMessage errorMessage
     */
    public WSException(String errorMessage)
    {
        super(errorMessage);
    }
    
    /**
     * Creates a new instance of WSException.
     *
     * @param errorMessage errorMessage
     * @param error error
     */
    public WSException(String errorMessage, Throwable error)
    {
        super(errorMessage, error);
    }
}
