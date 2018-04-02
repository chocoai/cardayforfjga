package com.cmdt.carday.microservice.model.request.vehicle;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

public class DriverAllocateDto implements Serializable{
	
	private static final long serialVersionUID = 7771782959524751869L;
	
	@ApiModelProperty(value="车辆ID",required=true)
	@NotNull(message="vehicleId不能为空")
	private Long vehicleId;
	
	@ApiModelProperty(value="司机ID",required=true)
	@NotNull(message="driverId不能为空")
	private Long driverId;
	
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Long getDriverId() {
		return driverId;
	}
	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}
	

}
