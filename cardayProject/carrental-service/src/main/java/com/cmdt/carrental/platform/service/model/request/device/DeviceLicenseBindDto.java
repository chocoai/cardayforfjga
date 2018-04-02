package com.cmdt.carrental.platform.service.model.request.device;

import javax.validation.constraints.NotNull;

public class DeviceLicenseBindDto {
	@NotNull(message="userId不能为空")
	private Long userId;
	private String snNumber;
	private String imeiNumber;
	private String deviceVendor;
	private String iccidNumber;
	private Long deviceId;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getSnNumber() {
		return snNumber;
	}
	public void setSnNumber(String snNumber) {
		this.snNumber = snNumber;
	}
	public String getImeiNumber() {
		return imeiNumber;
	}
	public void setImeiNumber(String imeiNumber) {
		this.imeiNumber = imeiNumber;
	}
	public String getDeviceVendor() {
		return deviceVendor;
	}
	public void setDeviceVendor(String deviceVendor) {
		this.deviceVendor = deviceVendor;
	}
	public String getIccidNumber() {
		return iccidNumber;
	}
	public void setIccidNumber(String iccidNumber) {
		this.iccidNumber = iccidNumber;
	}
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
}
