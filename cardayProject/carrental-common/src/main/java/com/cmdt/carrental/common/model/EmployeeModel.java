package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class EmployeeModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id; // 编号
	private Long organizationId; // 所属公司
	private String organizationName;// 名称
	private String username; // 用户名
	private Long roleId; // 拥有的角色
	private String roleName; // 拥有的角色名称
	private String userCategory;// 0:超级管理员 1:租户管理员 2:企业管理员 3.部门管理员 4：员工 5:司机
	private String realname;
	private String phone;
	private String email;
	private String city;// 常驻城市
	private Double monthLimitvalue;// 月累计限制额度(-1:不限额度)
	private Double monthLimitLeft;// 月使用后剩下的额度
	private String orderCustomer;// 代客户下单 0:不代客户下单 1：代客户下单
	private String orderSelf;// 员工自己下单 0:员工不自己下单 1：员工自己下单
	private String orderApp;// App下单 0:App不可以下单 1：App可以下单
	private String orderWeb;// Web下单 0:Web不可以下单 1：Web可以下单
	private int currentPage;
	private int numPerPage;
	private boolean selfDept;
	private boolean childDept;
	private Boolean locked = Boolean.FALSE;
	private String IDNumber;//身份证号

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getUserCategory() {
		return userCategory;
	}

	public void setUserCategory(String userCategory) {
		this.userCategory = userCategory;
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

	public Double getMonthLimitLeft() {
		return monthLimitLeft;
	}

	public void setMonthLimitLeft(Double monthLimitLeft) {
		this.monthLimitLeft = monthLimitLeft;
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

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}

	public boolean getSelfDept() {
		return selfDept;
	}

	public void setSelfDept(boolean selfDept) {
		this.selfDept = selfDept;
	}

	public boolean getChildDept() {
		return childDept;
	}

	public void setChildDept(boolean childDept) {
		this.childDept = childDept;
	}

	public String getIDNumber() {
		return IDNumber;
	}

	public void setIDNumber(String iDNumber) {
		IDNumber = iDNumber;
	}
	
}
