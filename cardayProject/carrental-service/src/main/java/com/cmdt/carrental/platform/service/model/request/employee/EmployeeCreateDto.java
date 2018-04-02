package com.cmdt.carrental.platform.service.model.request.employee;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class EmployeeCreateDto {
	
	@NotNull(message="userId不能为空")
	private Long userId;
	@NotNull(message="username不能为空")
	private String username;
	@NotNull(message="password不能为空")
	private String password;
	@NotNull(message="roleId不能为空")
	private Long roleId;
	@NotNull(message="organizationId不能为空")
	private Long organizationId;
	@NotNull(message="realname不能为空")
	private String realname;
	@NotNull(message="phone不能为空")
	private String phone;
	private String email;
	
	private String city;
	private Double monthLimitvalue;
	private String orderCustomer;
	private String orderSelf;
	private String orderApp;
	private String orderWeb;
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
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
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Double getMonthLimitvalue() {
		return monthLimitvalue;
	}
	public void setMonthLimitvalue(Double monthLimitvalue) {
		this.monthLimitvalue = monthLimitvalue;
	}
	public String getOrderCustomer() {
		return orderCustomer;
	}
	public void setOrderCustomer(String orderCustomer) {
		this.orderCustomer = orderCustomer;
	}
	public String getOrderSelf() {
		return orderSelf;
	}
	public void setOrderSelf(String orderSelf) {
		this.orderSelf = orderSelf;
	}
	public String getOrderApp() {
		return orderApp;
	}
	public void setOrderApp(String orderApp) {
		this.orderApp = orderApp;
	}
	public String getOrderWeb() {
		return orderWeb;
	}
	public void setOrderWeb(String orderWeb) {
		this.orderWeb = orderWeb;
	}

}
