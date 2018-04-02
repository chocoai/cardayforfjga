package com.cmdt.carrental.common.util;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RespResult<T> implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public int state;
    
	public String msg;
    
	public String beanStr;
    
	@JsonIgnore
    public T beanClass;

    public RespResult() {}

    public RespResult(int state, String msg, String beanStr, T beanClass) {
        this.state = state;
        this.msg = msg;
        this.beanStr = beanStr;
        this.beanClass = beanClass;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isOK(){
        return state == 200;
    }

    public T getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(T beanClass) {
        this.beanClass = beanClass;
    }


    public String getBeanStr() {
        return beanStr;
    }

    public void setBeanStr(String beanStr) {
        this.beanStr = beanStr;
    }
}