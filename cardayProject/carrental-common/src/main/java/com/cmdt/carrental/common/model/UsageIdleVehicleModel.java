package com.cmdt.carrental.common.model;

public class UsageIdleVehicleModel {
	
	private Integer id;
	private String vehicleNumber;
	private String vehicleBrand;
	private String vehicleModel;
	private String currentuseOrgName;//部门
	private String vehiclePurpose;
	private String idelInfo;//空闲时间段
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getVehicleBrand() {
		return vehicleBrand;
	}
	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}
	public String getVehicleModel() {
		return vehicleModel;
	}
	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}
	public String getCurrentuseOrgName() {
		return currentuseOrgName;
	}
	public void setCurrentuseOrgName(String currentuseOrgName) {
		this.currentuseOrgName = currentuseOrgName;
	}
	public String getVehiclePurpose() {
		return vehiclePurpose;
	}
	public void setVehiclePurpose(String vehiclePurpose) {
		this.vehiclePurpose = vehiclePurpose;
	}
	public String getIdelInfo() {
		return idelInfo;
	}
	public void setIdelInfo(String idelInfo) {
		this.idelInfo = idelInfo;
	}
	
    
}
