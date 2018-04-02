package com.cmdt.carrental.platform.service.model.request.marker;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MarkerUnassignVehicleDto {
	
	@NotNull(message="markerId不能为空")
	private Long markerId;
	
	@NotNull(message="vehicleId不能为空")
	private String vehicleId;
	
	
	public Long getMarkerId() {
		return markerId;
	}
	public void setMarkerId(Long markerId) {
		this.markerId = markerId;
	}
	public String getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
}
