package com.cmdt.carrental.common.integration.model;

public class GpsDataModel {
	
	private String resultCode;
	private String resultMessage;
	private Object data;
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "GpsDataModel [resultCode=" + resultCode + ", resultMessage=" + resultMessage + ", data=" + data + "]";
	}
	
}
