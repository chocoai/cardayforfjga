package com.cmdt.carday.microservice.common.model.geo;

public class TransResponse<T>
{
    private Integer status;
    
    private String message;
    
    private T result;
    
    public Integer getStatus()
    {
        return status;
    }
    
    public void setStatus(Integer status)
    {
        this.status = status;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public T getResult()
    {
        return result;
    }
    
    public void setResult(T result)
    {
        this.result = result;
    }
    
}
