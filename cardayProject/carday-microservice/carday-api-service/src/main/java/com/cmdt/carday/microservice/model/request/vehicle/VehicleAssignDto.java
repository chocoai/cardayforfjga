package com.cmdt.carday.microservice.model.request.vehicle;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
public class VehicleAssignDto implements Serializable{
	
	private static final long serialVersionUID = 4023038998788159499L;
	
	@ApiModelProperty(value="车辆ID",required=true)
	@Min(value=1)
	@NotNull(message="vehicleId不能为空")
	private Long vehicleId;
	
	@ApiModelProperty(value="部门Id，车辆分配orgId为分配的部门id，车辆回收orgId为null")
	private Long orgId;
	
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

}
