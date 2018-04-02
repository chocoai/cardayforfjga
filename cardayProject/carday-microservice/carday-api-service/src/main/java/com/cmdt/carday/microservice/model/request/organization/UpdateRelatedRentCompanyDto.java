package com.cmdt.carday.microservice.model.request.organization;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UpdateRelatedRentCompanyDto {
	
	private static final long serialVersionUID = 1L;
	
	private Long entId;
	private Long rentId;
	@NotNull(message="vehicleNumber不能为空")
    private Long vehicleNumber;
	@NotNull(message="driverNumber不能为空")
    private Long driverNumber;
    
	public Long getEntId() {
		return entId;
	}
	public void setEntId(Long entId) {
		this.entId = entId;
	}
	public Long getRentId() {
		return rentId;
	}
	public void setRentId(Long rentId) {
		this.rentId = rentId;
	}
	public Long getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(Long vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public Long getDriverNumber() {
		return driverNumber;
	}
	public void setDriverNumber(Long driverNumber) {
		this.driverNumber = driverNumber;
	}
}
