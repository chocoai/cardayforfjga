package com.cmdt.carrental.platform.service.model.request.marker;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MarkerAssignVehicleDto {

	@NotNull(message="markerId不能为空")
	private Long markerId;
	
	@NotNull(message="vehicleIds不能为空")
	private String vehicleIds;
	
	
	public Long getMarkerId() {
		return markerId;
	}
	public void setMarkerId(Long markerId) {
		this.markerId = markerId;
	}
	public String getVehicleIds() {
		return vehicleIds;
	}
	public void setVehicleIds(String vehicleIds) {
		this.vehicleIds = vehicleIds;
	}

}
