package com.cmdt.carrental.common.model;

public class VehicleAllocationModel {
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
