package com.cmdt.carday.microservice.model.request.employee;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
@ApiModel(value = "EmployeeUpdateDto", description = "信息描述")
public class EmployeeUpdateDto {
	
	@ApiModelProperty("用户id")
	@NotNull(message="userId不能为空")
	private Long userId;
	@ApiModelProperty("员工ID")
	@NotNull(message="员工Id不能为空")
	private Long id;
	@ApiModelProperty("角色ID")
	@NotNull(message="roleId不能为空")
	private Long roleId;
	@ApiModelProperty("所属组织ID")
	@NotNull(message="organizationId不能为空")
	private Long organizationId;
	@ApiModelProperty("月累计限制额度(-1:不限额度)")
	private Double monthLimitvalue;
	@ApiModelProperty("手机号码")
	@NotNull(message="phone不能为空")
	private String phone;
	@ApiModelProperty("邮箱")
	private String email;
	@ApiModelProperty("员工姓名")
	@NotNull(message="realname不能为空")
	private String realname;
	@ApiModelProperty("常驻城市")
	private String city;
	@ApiModelProperty("代客户下单 0:不代客户下单 1：代客户下单")
	private String orderCustomer;
	@ApiModelProperty("员工自己下单 0:员工不自己下单 1：员工自己下单")
	private String orderSelf;
	@ApiModelProperty("App下单 0:App不可以下单 1：App可以下单")
	private String orderApp;
	@ApiModelProperty("Web下单 0:Web不可以下单 1：Web可以下单")
	private String orderWeb;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Double getMonthLimitvalue() {
		return monthLimitvalue;
	}
	public void setMonthLimitvalue(Double monthLimitvalue) {
		this.monthLimitvalue = monthLimitvalue;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
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
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	

}