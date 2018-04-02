package com.cmdt.carday.microservice.model.request.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

@ApiModel(value = "UserChangePassword", description = "新密码信息")
public class UserChangePasswDto implements Serializable{

	private static final long serialVersionUID = 1L;

	@ApiModelProperty("用户ID")
	@NotNull(message="userId不能为空")
	private Long userId;

	@ApiModelProperty("旧密码")
	@NotNull(message="oldPassword不能为空")
	private String oldPassword; //旧密码

	@ApiModelProperty("新密码")
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
