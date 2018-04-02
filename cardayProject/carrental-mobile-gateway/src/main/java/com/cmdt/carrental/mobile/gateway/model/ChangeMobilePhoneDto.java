package com.cmdt.carrental.mobile.gateway.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carrental.common.util.Patterns;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ChangeMobilePhoneDto {
	
	@NotNull 
	private String username;
	@NotNull
	@Pattern(regexp = Patterns.REG_MOBILE_PHONE,message="Phone number format error!")
	private String phone;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
