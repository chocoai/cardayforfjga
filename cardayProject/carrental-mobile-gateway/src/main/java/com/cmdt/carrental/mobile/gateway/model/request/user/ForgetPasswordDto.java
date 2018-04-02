package com.cmdt.carrental.mobile.gateway.model.request.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ForgetPasswordDto {

	@NotEmpty(message="mobile不能为空") @Pattern(regexp ="^1[358]\\d{9}$",message="mobile格式有误")
	private String mobile;
	@NotEmpty(message="password不能为空")
	private String password;
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
