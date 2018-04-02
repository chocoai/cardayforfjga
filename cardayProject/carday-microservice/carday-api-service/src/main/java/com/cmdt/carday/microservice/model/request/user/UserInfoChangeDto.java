package com.cmdt.carday.microservice.model.request.user;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carday.microservice.common.Patterns;

import io.swagger.annotations.ApiModelProperty;

public class UserInfoChangeDto implements Serializable{


	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="登录用户ID",required=true)
    @NotNull(message="userId不能为空")
    @Min(value=1)
	private Long userId;
	
	@ApiModelProperty(value="电话号码，格式错误，应为以13,15,17,18开头的11位数字")
	@Pattern(regexp = Patterns.REG_PHONE,message="phone格式错误，应为以13,15,17,18开头的11位数字")
	private String phone;			//电话
	
	@ApiModelProperty(value="邮箱")
	private String email;			//邮箱
	
	@ApiModelProperty(value="用户真实姓名")
	private String realname;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

}
