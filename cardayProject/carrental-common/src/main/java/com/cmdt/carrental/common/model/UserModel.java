package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class UserModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long id; //编号
    private Long organizationId; //所属公司
    private String organizationName;//所属公名称
    private String username; //用户名
    private Long roleId; //拥有的角色
    private String roleName; //拥有的角色名称
    private Long userCategory;//0:超级管理员  1:租户管理员 2:企业管理员 3.部门管理员  4：员工  5:司机
    private String realname;
    private String phone;
    private String email;
    private int currentPage;
    private int numPerPage;
    private Boolean locked = Boolean.FALSE;
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
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Long getUserCategory() {
		return userCategory;
	}
	public void setUserCategory(Long userCategory) {
		this.userCategory = userCategory;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealName(String realname) {
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
}
