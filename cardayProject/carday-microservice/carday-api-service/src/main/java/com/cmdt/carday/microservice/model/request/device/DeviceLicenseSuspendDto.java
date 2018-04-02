package com.cmdt.carday.microservice.model.request.device;

import javax.validation.constraints.NotNull;

public class DeviceLicenseSuspendDto {
	@NotNull(message="userId不能为空")
	private Long userId;
	private String licenseNo;
	private Long deviceId;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
}
