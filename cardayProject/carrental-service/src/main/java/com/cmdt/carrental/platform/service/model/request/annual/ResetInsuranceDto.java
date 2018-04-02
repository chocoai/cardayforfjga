package com.cmdt.carrental.platform.service.model.request.annual;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResetInsuranceDto {
	
	@Min(value=1)
	@NotNull(message="userId不能为空")
	private Long userId;
	
	private Long vehicleId;
	
	private String insuranceDueTime;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getInsuranceDueTime() {
		return insuranceDueTime;
	}
	public void setInsuranceDueTime(String insuranceDueTime) {
		this.insuranceDueTime = insuranceDueTime;
	}

}
