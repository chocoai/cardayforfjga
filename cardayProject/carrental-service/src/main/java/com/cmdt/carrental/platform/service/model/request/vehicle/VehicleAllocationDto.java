package com.cmdt.carrental.platform.service.model.request.vehicle;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class VehicleAllocationDto implements Serializable{
	
	private static final long serialVersionUID = 5058997400081674988L;

	private Long vehicleId;
	
	private Long currentuseOrgId; // 分配车辆到所在的企业Id
	
	private String currentuseOrgName; // 分配车辆到所在的企业名称

	public Long getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public Long getCurrentuseOrgId() {
		return currentuseOrgId;
	}

	public void setCurrentuseOrgId(Long currentuseOrgId) {
		this.currentuseOrgId = currentuseOrgId;
	}

	public String getCurrentuseOrgName() {
		return currentuseOrgName;
	}

	public void setCurrentuseOrgName(String currentuseOrgName) {
		this.currentuseOrgName = currentuseOrgName;
	}

}
