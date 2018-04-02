package com.cmdt.carrental.mobile.gateway.model.request.user;

import javax.validation.constraints.NotNull;

public class ResetPasswordDto {
	@NotNull(message = "phoneNumber不能为空")
	private String phoneNumber;
	private String verifyCode;
	private String password;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
