package com.cmdt.carrental.common.model;


public class VehicleStasticModel {

	private Long id;
	private String vehicleNumber;
    private String deviceNumber;
	private Long rentId;
	private String rentName;
	private Long entId;
	private String entName;
	private Long currentuseOrgId;
	private String currentuseOrgName;
	private Integer totalMileage;
	private Double totalFuelCons;
	private Integer totalDrivingTime;
	private java.sql.Timestamp lastUpdatedTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public Long getRentId() {
		return rentId;
	}
	public void setRentId(Long rentId) {
		this.rentId = rentId;
	}
	public String getRentName() {
		return rentName;
	}
	public void setRentName(String rentName) {
		this.rentName = rentName;
	}
	public Long getEntId() {
		return entId;
	}
	public void setEntId(Long entId) {
		this.entId = entId;
	}
	public String getEntName() {
		return entName;
	}
	public void setEntName(String entName) {
		this.entName = entName;
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
	public Integer getTotalMileage() {
		return totalMileage;
	}
	public void setTotalMileage(Integer totalMileage) {
		this.totalMileage = totalMileage;
	}
	public Double getTotalFuelCons() {
		return totalFuelCons;
	}
	public void setTotalFuelCons(Double totalFuelCons) {
		this.totalFuelCons = totalFuelCons;
	}
	public Integer getTotalDrivingTime() {
		return totalDrivingTime;
	}
	public void setTotalDrivingTime(Integer totalDrivingTime) {
		this.totalDrivingTime = totalDrivingTime;
	}
	public java.sql.Timestamp getLastUpdatedTime() {
		return lastUpdatedTime;
	}
	public void setLastUpdatedTime(java.sql.Timestamp lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}
	
	
}
