package com.cmdt.carrental.platform.service.model.request.user;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginDto {
	
	@NotNull(message="用户名不能为空")
	private String username;
	@NotNull(message="密码不能为空")
	private String password;
	
	public UserLoginDto(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public UserLoginDto() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	

}
