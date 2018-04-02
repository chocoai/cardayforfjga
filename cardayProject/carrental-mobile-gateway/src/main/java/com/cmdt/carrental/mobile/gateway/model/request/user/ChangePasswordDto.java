package com.cmdt.carrental.mobile.gateway.model.request.user;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ChangePasswordDto {
	
	@NotNull(message="userid不能为空")
	private Long userId;
	@NotEmpty(message="oldPasswd不能为空")
	private String oldPasswd;
	@NotEmpty(message="newPwd不能为空")
	private String newPasswd;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getOldPasswd() {
		return oldPasswd;
	}
	public void setOldPasswd(String oldPasswd) {
		this.oldPasswd = oldPasswd;
	}
	public String getNewPasswd() {
		return newPasswd;
	}
	public void setNewPasswd(String newPasswd) {
		this.newPasswd = newPasswd;
	}
	
}
