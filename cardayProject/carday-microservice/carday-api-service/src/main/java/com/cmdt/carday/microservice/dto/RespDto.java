package com.cmdt.carday.microservice.dto;

public class RespDto<T> {
	private String errorCode;
	private String errorMsg;
	private T result;
	
	public RespDto(){
		super();
	}
	
	public RespDto(String errorCode, String errorMsg){
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	
	public RespDto(T result){
		this.result = result;
	}
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	@Override
	public String toString() {
		return "RespDto [errorCode=" + errorCode + ", errorMsg=" + errorMsg + ", result=" + result + "]";
	}
	
}
