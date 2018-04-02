package com.cmdt.carrental.platform.service.model.request.vehicle;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class VehicleSpeedLimitDto implements Serializable {

	private static final long serialVersionUID = -7967365833804159629L;

	@Min(value = 1)
	@NotNull(message = "userId不能为空")
	private Long userId;

	@Digits(fraction = 0, integer = Integer.MAX_VALUE)
	private Integer limitSpeed;

	@NotNull(message = "vehicleNumber不能为空")
	private String vehicleNumber;

	public Integer getLimitSpeed() {
		return limitSpeed;
	}

	public void setLimitSpeed(Integer limitSpeed) {
		this.limitSpeed = limitSpeed;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
