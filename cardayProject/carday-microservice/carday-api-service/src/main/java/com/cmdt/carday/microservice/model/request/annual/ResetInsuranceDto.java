package com.cmdt.carday.microservice.model.request.annual;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
@ApiModel(value = "ResetInsuranceDto", description = "信息描述")
public class ResetInsuranceDto {
	
	@ApiModelProperty("用户ID")
	@Min(value=1)
	@NotNull(message="userId不能为空")
	private Long userId;
	
	@ApiModelProperty("车辆ID")
	private Long vehicleId;
	
	@ApiModelProperty("保险到期时间")
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
