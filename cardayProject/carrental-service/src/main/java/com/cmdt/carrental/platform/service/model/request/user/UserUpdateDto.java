package com.cmdt.carrental.platform.service.model.request.user;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carrental.platform.service.common.Patterns;

public class UserUpdateDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
    
	@NotNull(message="id 不能为空")
	private Long id ;
	
	private String realname; 		//真实名字
	
	@Digits(message="roleId必须为数字", fraction = 0, integer = Integer.MAX_VALUE)
	private Long roleId;			 //拥有的角色列表
	
	@Digits(message="organizationId必须为数字", fraction = 0, integer = Integer.MAX_VALUE)
	private Long organizationId; 	//所属企业
	
	@Pattern(regexp = Patterns.REG_PHONE,message="phone格式错误，应为以13,15,17,18开头的11位数字")
	private String phone;			//电话
	
	private String email;			//邮箱
	
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
	
}
