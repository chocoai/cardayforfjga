package com.cmdt.carday.microservice.model.request.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carday.microservice.common.Patterns;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "VerificationCode", description = "手机验证码信息")
public class VerificationCodeDto {
	
	@NotNull(message="手机号不能为空")
	@Pattern(regexp = Patterns.REG_PHONE, message="phone格式错误，应为以13,15,17,18开头的11位数字")
	@ApiModelProperty("手机号")
	private String phoneNumber;
	@NotNull(message="验证码不能为空")
	@ApiModelProperty("验证码")
	private String code;
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

}
