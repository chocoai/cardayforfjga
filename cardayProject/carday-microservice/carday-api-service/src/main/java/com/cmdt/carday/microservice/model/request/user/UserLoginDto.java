package com.cmdt.carday.microservice.model.request.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "UserLogin", description = "用户登录Dto")
public class UserLoginDto {
	
	@NotNull(message="用户名不能为空")
	@ApiModelProperty("用户名")
	private String username;
	@NotNull(message="密码不能为空")
	@ApiModelProperty("密码")
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
