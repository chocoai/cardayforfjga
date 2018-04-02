package com.cmdt.carrental.mobile.gateway.model;

public class UserInfo {
	
	private String username;
	private String realname;
	private Long roleType;
	private String entName;
	private String mobile;
	private String email;
	
	public UserInfo(String username, String realname, Long roleType, String entName, String mobile, String email) {
		super();
		this.username = username;
		this.realname = realname;
		this.roleType = roleType;
		this.entName = entName;
		this.mobile = mobile;
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public Long getRoleType() {
		return roleType;
	}
	public void setRoleType(Long roleType) {
		this.roleType = roleType;
	}
	public String getEntName() {
		return entName;
	}
	public void setEntName(String entName) {
		this.entName = entName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String toString(){
		return "UserName:"+this.username+"  realname:"+this.realname+" roleType:"+this.roleType+" entName:"+this.entName+" mobile:"+this.mobile+" Email:"+this.email;
	}

}
