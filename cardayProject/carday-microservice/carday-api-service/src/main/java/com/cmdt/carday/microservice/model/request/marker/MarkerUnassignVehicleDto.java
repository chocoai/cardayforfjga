package com.cmdt.carday.microservice.model.request.marker;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
public class MarkerUnassignVehicleDto {
	
	@ApiModelProperty(value="地理围栏ID",required=true)
	@NotNull(message="markerId不能为空")
	private Long markerId;
	
	@ApiModelProperty(value="车辆ID",required=true)
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
