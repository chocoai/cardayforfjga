package com.cmdt.carday.microservice.model.request.user;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carday.microservice.common.Patterns;

public class UserListDto implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@NotNull(message="userId不能为空")
	private Long userId;
	
	private Long organizationId;
	
	private String userIds; 
	
	private String userName;
	
	private String realname;
	
	@Pattern(regexp = Patterns.REG_PHONE,message="phone格式错误，应为以13,15,17,18开头的11位数字")
	private String phone;			//电话
	
	private String email;			//邮箱

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
