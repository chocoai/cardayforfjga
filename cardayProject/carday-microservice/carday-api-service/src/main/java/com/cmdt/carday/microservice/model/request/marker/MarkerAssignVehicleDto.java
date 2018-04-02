package com.cmdt.carday.microservice.model.request.marker;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
public class MarkerAssignVehicleDto {

	@ApiModelProperty(value="地理围栏ID",required=true)
	@NotNull(message="markerId不能为空")
	private Long markerId;
	
	@ApiModelProperty(value="分配车辆ID，以逗号分隔",required=true)
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
