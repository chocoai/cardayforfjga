package com.cmdt.carday.microservice.model.request.user;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carday.microservice.common.Patterns;

public class UserCreateDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@NotNull(message="username不能为空")
	private String username; 		//用户名
	
	@NotNull(message="password不能为空")
    private String password; 		//密码
    
	@NotNull(message="realname不能为空")
	private String realname; 		//真实名字
	
	@NotNull(message="roleId不能为空")
	@Digits(message="roleId必须为数字", fraction = 0, integer = Integer.MAX_VALUE)
	private Long roleId;			 //拥有的角色列表
	
	@NotNull(message="organizationId不能为空")
	@Digits(message="organizationId必须为数字", fraction = 0, integer = Integer.MAX_VALUE)
	private Long organizationId; 	//所属企业
	
	@NotNull(message="phone不能为空")
	@Pattern(regexp = Patterns.REG_PHONE,message="phone格式错误，应为以13,15,17,18开头的11位数字")
	private String phone;			//电话
	
	private String email;			//邮箱
	
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
	
	
}
