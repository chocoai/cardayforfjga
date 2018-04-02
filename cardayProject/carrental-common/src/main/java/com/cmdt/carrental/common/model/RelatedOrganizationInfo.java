package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class RelatedOrganizationInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer rentId;
	private Integer entId;
	private String rentName;
	private String entName;
	private Integer vehicleNumber=Integer.valueOf(0);
	private Integer driverNumber=Integer.valueOf(0);
	private Integer realVehicleNumber=Integer.valueOf(0);
	private Integer realDriverNumber=Integer.valueOf(0);
	private String businessType;
	 private String status;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer getRealVehicleNumber() {
		return realVehicleNumber;
	}
	public void setRealVehicleNumber(Integer realVehicleNumber) {
		this.realVehicleNumber = realVehicleNumber;
	}
	public Integer getRealDriverNumber() {
		return realDriverNumber;
	}
	public void setRealDriverNumber(Integer realDriverNumber) {
		this.realDriverNumber = realDriverNumber;
	}
	private Boolean isRelated=Boolean.FALSE;
	
	public Boolean getIsRelated() {
		return isRelated;
	}
	public void setIsRelated(Boolean isRelated) {
		this.isRelated = isRelated;
	}
	public String getRentName() {
		return rentName;
	}
	public void setRentName(String rentName) {
		this.rentName = rentName;
	}
	public Integer getRentId() {
		return rentId;
	}
	public void setRentId(Integer rentId) {
		this.rentId = rentId;
	}
	public Integer getEntId() {
		return entId;
	}
	public void setEntId(Integer entId) {
		this.entId = entId;
	}
	public String getEntName() {
		return entName;
	}
	public void setEntName(String entName) {
		this.entName = entName;
	}
	public Integer getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(Integer vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public Integer getDriverNumber() {
		return driverNumber;
	}
	public void setDriverNumber(Integer driverNumber) {
		this.driverNumber = driverNumber;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	

}
