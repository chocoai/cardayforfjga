package com.cmdt.carday.microservice.model.request.driver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DriverCreateDto {
	
	private String driverUsername;
	
	private String driverPassword;
	
	private Long roleId;
	
	private Long organizationId;
	
	private String realname;
	
	private String phone;
	
	private String driverEmail;

	private String licenseNumber;
	
	private String sex;
	
	private String birthday;
	
	private String licenseType;
	
	private String licenseBegintime;
	
	private String licenseExpiretime;
	
	private Integer drivingYears; 
	
	private Long depId;
	
	private Long stationId;

	public String getDriverUsername() {
		return driverUsername;
	}

	public void setDriverUsername(String driverUsername) {
		this.driverUsername = driverUsername;
	}

	public String getDriverPassword() {
		return driverPassword;
	}

	public void setDriverPassword(String driverPassword) {
		this.driverPassword = driverPassword;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDriverEmail() {
		return driverEmail;
	}

	public void setDriverEmail(String driverEmail) {
		this.driverEmail = driverEmail;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public String getLicenseBegintime() {
		return licenseBegintime;
	}

	public void setLicenseBegintime(String licenseBegintime) {
		this.licenseBegintime = licenseBegintime;
	}

	public String getLicenseExpiretime() {
		return licenseExpiretime;
	}

	public void setLicenseExpiretime(String licenseExpiretime) {
		this.licenseExpiretime = licenseExpiretime;
	}

	public Integer getDrivingYears() {
		return drivingYears;
	}

	public void setDrivingYears(Integer drivingYears) {
		this.drivingYears = drivingYears;
	}

	public Long getDepId() {
		return depId;
	}

	public void setDepId(Long depId) {
		this.depId = depId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	
}
