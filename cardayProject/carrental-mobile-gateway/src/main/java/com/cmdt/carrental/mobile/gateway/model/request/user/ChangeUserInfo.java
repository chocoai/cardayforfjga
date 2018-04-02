package com.cmdt.carrental.mobile.gateway.model.request.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ChangeUserInfo {

	@NotNull(message="userId不能为空")
	private Long userId;
	private String portrait;
//	@NotEmpty(message="realName不能为空")
	private String realName;
	private String userName;
//	@NotEmpty(message="mobile不能为空") @Pattern(regexp ="^1[358]\\d{9}$",message="mobile格式有误")
	private String mobile;
//	@NotEmpty(message="email不能为空") @Email
	@Email
	private String email;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getPortrait() {
		return portrait;
	}
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
