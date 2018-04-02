package com.cmdt.carrental.platform.service.model.request.user;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerificationCodeDto {
	
	@NotNull(message="手机号不能为空")
	private String phoneNumber;
	@NotNull(message="验证码不能为空")
	private String code;
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
