package com.cmdt.carrental.common.model;

import java.util.Date;

public class VehicleAlertQueryDTO {
	
	private String vehicleNumber;
	private String vehicleType;	
	private String vehicleSource;
	private String organizationName;
	private Date startTime;	
	private Date endTime;
	private String alertType;
	private Integer currentPage;
	private Integer numPerPage;
	private Integer organizationId;
	Boolean selfDept = true;
	Boolean childDept = true;
	
	
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getVehicleSource() {
		return vehicleSource;
	}
	public void setVehicleSource(String vehicleSource) {
		this.vehicleSource = vehicleSource;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public Integer getNumPerPage() {
		return numPerPage;
	}
	public void setNumPerPage(Integer numPerPage) {
		this.numPerPage = numPerPage;
	}	
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public Boolean getSelfDept() {
		return selfDept;
	}
	public void setSelfDept(Boolean selfDept) {
		this.selfDept = selfDept;
	}
	public Boolean getChildDept() {
		return childDept;
	}
	public void setChildDept(Boolean childDept) {
		this.childDept = childDept;
	}
	
}
