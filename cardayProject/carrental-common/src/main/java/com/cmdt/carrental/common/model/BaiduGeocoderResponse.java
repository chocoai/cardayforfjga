package com.cmdt.carrental.common.model;

public class BaiduGeocoderResponse {
	private Integer status;
	private String message;
	private BaiduGeocoder result;
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public BaiduGeocoder getResult() {
		return result;
	}
	public void setResult(BaiduGeocoder result) {
		this.result = result;
	}
	
}
