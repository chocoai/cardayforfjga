package com.cmdt.carrental.platform.service.model.request.vehicle;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public class DriverAllocateDto implements Serializable{
	
	private static final long serialVersionUID = 7771782959524751869L;
	
	@Digits(integer=Integer.MAX_VALUE, fraction = 0, message="输入值必须为数字")
	@NotNull(message="vehicleId不能为空")
	private Long vehicleId;
	
	@Digits(integer=Integer.MAX_VALUE, fraction = 0, message="输入值必须为数字")
	@NotNull(message="driverId不能为空")
	private Long driverId;
	
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Long getDriverId() {
		return driverId;
	}
	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}
	

}
