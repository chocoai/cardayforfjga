package com.cmdt.carrental.common.model;

public class BaiduResponse {
	private Integer status;
	private String message;
	private BaiduCoord[] result;

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

	public BaiduCoord[] getResult() {
		return result;
	}

	public void setResult(BaiduCoord[] result) {
		this.result = result;
	}

}
