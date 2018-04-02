package com.cmdt.carrental.mobile.gateway.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * 
 * @author KevinPei
 *
 * All useful annotations:
    @Null	被注释的元素必须为 null
	@NotNull	被注释的元素必须不为 null
	@AssertTrue	被注释的元素必须为 true
	@AssertFalse	被注释的元素必须为 false
	@Min(value)	被注释的元素必须是一个数字，其值必须大于等于指定的最小值
	@Max(value)	被注释的元素必须是一个数字，其值必须小于等于指定的最大值
	@DecimalMin(value)	被注释的元素必须是一个数字，其值必须大于等于指定的最小值
	@DecimalMax(value)	被注释的元素必须是一个数字，其值必须小于等于指定的最大值
	@Size(max, min)	被注释的元素的大小必须在指定的范围内
	@Digits (integer, fraction)	被注释的元素必须是一个数字，其值必须在可接受的范围内
	@Past	被注释的元素必须是一个过去的日期
	@Future	被注释的元素必须是一个将来的日期
	@Pattern(value)	被注释的元素必须符合指定的正则表达式
	@Email	被注释的元素必须是电子邮箱地址
	@Length	被注释的字符串的大小必须在指定的范围内
	@NotEmpty	被注释的字符串的必须非空
	@Range	被注释的元素必须在合适的范围内
 *
 */

@JsonInclude(Include.NON_NULL)
public class User {
	@NotNull(message="Username can not be empty")  @Size(min = 1, max = 50)
	private String username;
	@NotNull @Pattern(regexp = "\\d+")
	private String password;
	@NotNull @Email
	private String email;
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString(){
		return "UserName:"+this.username+"  Password:"+this.password+" Email:"+this.email;
	}

}
