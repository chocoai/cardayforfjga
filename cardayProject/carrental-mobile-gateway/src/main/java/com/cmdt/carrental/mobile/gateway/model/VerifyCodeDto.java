package com.cmdt.carrental.mobile.gateway.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import com.cmdt.carrental.common.util.Patterns;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class VerifyCodeDto {
	
	@NotNull 
	@Pattern(regexp = Patterns.REG_MOBILE_PHONE,message="Phone number format error!")
	private String phone;
	@NotNull
	@Length(min=6,max=6,message="verify code must be six length number!")
	private String vercode;
	
	
}
