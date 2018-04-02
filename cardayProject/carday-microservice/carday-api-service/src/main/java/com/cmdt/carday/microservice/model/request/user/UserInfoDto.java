package com.cmdt.carday.microservice.model.request.user;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class UserInfoDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id; //编号
	
	@NotNull(message="username不能为空")
	private String username; 		//用户名
	
	@NotNull(message="password不能为空")
    private String password; 		//密码
    
	private String realname; 		//真实名字
	
	private Long roleId;			 //拥有的角色列表
	
	private Long organizationId; 	//组织id
	
	private String phone;			//电话
	
	private String email;			//邮箱
	
	private Long entId;      		//企业id
	
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
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getEntId() {
		return entId;
	}
	public void setEntId(Long entId) {
		this.entId = entId;
	}
	
}
