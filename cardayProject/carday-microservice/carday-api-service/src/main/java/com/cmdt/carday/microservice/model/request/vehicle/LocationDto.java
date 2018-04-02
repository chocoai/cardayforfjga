package com.cmdt.carday.microservice.model.request.vehicle;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

public class LocationDto implements Serializable {

	private static final long serialVersionUID = 1743981696758656015L;

	@ApiModelProperty(value="登录用户ID",required=true)
    @NotNull(message="userId不能为空")
	private Long userId;

	@ApiModelProperty(value="部门Id")
	private Long orgId;

	@ApiModelProperty(value="车牌号")
	private String vehicleNumber;

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
