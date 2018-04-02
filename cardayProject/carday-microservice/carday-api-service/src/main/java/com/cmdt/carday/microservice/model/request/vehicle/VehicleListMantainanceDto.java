package com.cmdt.carday.microservice.model.request.vehicle;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class VehicleListMantainanceDto implements Serializable {

	private static final long serialVersionUID = -4188130748961360920L;
	
	@Min(value=1)
	@NotNull(message="userId不能为空")
	private Long userId;
	
	private String vehicleNumber;
	
	private String deviceNumber;//车架号

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	

}
