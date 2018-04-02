package com.cmdt.carrental.common.model;

import java.util.List;

public class TransGPStoBaiduModel {
	private int status;
	private String message;
	private List<Point> result;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<Point> getResult() {
		return result;
	}
	public void setResult(List<Point> result) {
		this.result = result;
	}
}
