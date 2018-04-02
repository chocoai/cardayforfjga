package com.cmdt.carday.microservice.model.request.alert;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AlertVehicleQueryDto {
	
	@Min(value=1)
	@NotNull(message="userId不能为空")
	private Long userId;
	
	@NotNull(message="车牌号不能为空")
	private String vehicleNumber;
	
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
