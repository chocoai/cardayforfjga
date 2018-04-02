package com.cmdt.carrental.platform.service.model.request.vehicle;

import java.io.Serializable;

public class LocationDto implements Serializable {

	private static final long serialVersionUID = 1743981696758656015L;

	private Long userId;

	private Long orgId;

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
