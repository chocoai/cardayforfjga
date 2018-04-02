package com.cmdt.carrental.platform.service.model.request.vehicle;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class VehicleByVehicleNumberDto implements Serializable {
	
	private static final long serialVersionUID = -5548559929164114710L;
	
	@Min(value=1)
	@NotNull(message="userId不能为空")
	private Long userId;
	
	@NotNull(message="vehicleNumber不能为空")
	private String vehicleNumber; // 车牌号

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
}
