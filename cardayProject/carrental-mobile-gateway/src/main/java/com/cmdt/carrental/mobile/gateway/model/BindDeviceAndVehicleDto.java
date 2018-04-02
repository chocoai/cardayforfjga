package com.cmdt.carrental.mobile.gateway.model;

import javax.validation.constraints.NotNull;

public class BindDeviceAndVehicleDto {
	@NotNull
	private String deviceId;
	
	@NotNull
	private String vehicleId;

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
}
