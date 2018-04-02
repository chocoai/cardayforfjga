package com.cmdt.carrental.platform.service.model.request.user;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class UserChangePasswDto implements Serializable{

	private static final long serialVersionUID = 1L;

	@NotNull(message="userId不能为空")
	private Long userId;
	
	@NotNull(message="oldPassword不能为空")
	private String oldPassword; //旧密码
	
	@NotNull(message="newPassword不能为空")
	private String newPassword; //新密码 

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
}
