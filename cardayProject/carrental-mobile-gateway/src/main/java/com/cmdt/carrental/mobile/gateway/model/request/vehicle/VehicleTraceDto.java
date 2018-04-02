package com.cmdt.carrental.mobile.gateway.model.request.vehicle;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class VehicleTraceDto {
	
	@NotNull(message="carId不能为空")
	private Long carId;
	@NotEmpty(message="startTime不能为空")
	private String startTime;
	@NotEmpty(message="endTime不能为空")
	private String endTime;
	public Long getCarId() {
		return carId;
	}
	public void setCarId(Long carId) {
		this.carId = carId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
