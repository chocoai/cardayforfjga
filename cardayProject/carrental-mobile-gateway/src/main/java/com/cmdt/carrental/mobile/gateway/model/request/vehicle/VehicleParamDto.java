package com.cmdt.carrental.mobile.gateway.model.request.vehicle;

import javax.validation.constraints.NotNull;

public class VehicleParamDto {
	
	@NotNull(message="carId不能为空")
	private Long carId;

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}
	
}
