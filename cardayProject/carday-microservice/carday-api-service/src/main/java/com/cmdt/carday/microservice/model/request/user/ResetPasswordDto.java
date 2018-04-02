package com.cmdt.carday.microservice.model.request.user;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "RestPassword", description = "重置密码信息")
public class ResetPasswordDto {
	@NotNull(message="登陆名不能为空")
	@ApiModelProperty("用户名")
	private String username;
	@NotNull(message="新密码不能为空")
	@Pattern(regexp = "([\\u4e00-\\u9fa5]|\\w)+",message = "密码大小写字母或数字")
	@Length(min=6,max = 18,message = "密码长度在6到18位之间")
	@ApiModelProperty(value = "新密码",notes = "密码为6~18位大小写字母或数字")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
