package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class ObdStatusModel implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String deviceNumber;
	private String status;
	
	public String getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
