package com.cmdt.carrental.common.model;

public class UsageVehiclePropertyModel {
	
	private Integer id;
	private String vehicleNumber;
	private String vehicleBrand;
	private String vehicleOutput;
	private String vehicleFuel;
	private String currentuseOrgName;//部门
	private String totalMileage;
	private String totalFuelcons;
	private String totalDrivingtime;
	private String avgMileage;
	private String avgFuelcons;
	private String avgDrivingtime;
	private String usagePercent;//使用率
	
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
	public String getVehicleOutput() {
		return vehicleOutput;
	}
	public void setVehicleOutput(String vehicleOutput) {
		this.vehicleOutput = vehicleOutput;
	}
	public String getVehicleFuel() {
		return vehicleFuel;
	}
	public void setVehicleFuel(String vehicleFuel) {
		this.vehicleFuel = vehicleFuel;
	}
	public String getCurrentuseOrgName() {
		return currentuseOrgName;
	}
	public void setCurrentuseOrgName(String currentuseOrgName) {
		this.currentuseOrgName = currentuseOrgName;
	}
	public String getTotalMileage() {
		return totalMileage;
	}
	public void setTotalMileage(String totalMileage) {
		this.totalMileage = totalMileage;
	}
	public String getTotalFuelcons() {
		return totalFuelcons;
	}
	public void setTotalFuelcons(String totalFuelcons) {
		this.totalFuelcons = totalFuelcons;
	}
	public String getTotalDrivingtime() {
		return totalDrivingtime;
	}
	public void setTotalDrivingtime(String totalDrivingtime) {
		this.totalDrivingtime = totalDrivingtime;
	}
	public String getAvgMileage() {
		return avgMileage;
	}
	public void setAvgMileage(String avgMileage) {
		this.avgMileage = avgMileage;
	}
	public String getAvgFuelcons() {
		return avgFuelcons;
	}
	public void setAvgFuelcons(String avgFuelcons) {
		this.avgFuelcons = avgFuelcons;
	}
	public String getAvgDrivingtime() {
		return avgDrivingtime;
	}
	public void setAvgDrivingtime(String avgDrivingtime) {
		this.avgDrivingtime = avgDrivingtime;
	}
	public String getUsagePercent() {
		return usagePercent;
	}
	public void setUsagePercent(String usagePercent) {
		this.usagePercent = usagePercent;
	}

}
