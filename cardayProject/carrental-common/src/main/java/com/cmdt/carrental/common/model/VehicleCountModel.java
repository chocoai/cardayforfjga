package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class VehicleCountModel implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer vehiclecountval;
	private String message;
	private Integer validflag;//0:可以新增  1:不可以新增
	private String failureMsg;//失败原因
	
	public Integer getVehiclecountval() {
		return vehiclecountval;
	}
	public void setVehiclecountval(Integer vehiclecountval) {
		this.vehiclecountval = vehiclecountval;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getValidflag() {
		return validflag;
	}
	public void setValidflag(Integer validflag) {
		this.validflag = validflag;
	}
	public String getFailureMsg() {
		return failureMsg;
	}
	public void setFailureMsg(String failureMsg) {
		this.failureMsg = failureMsg;
	}

}
