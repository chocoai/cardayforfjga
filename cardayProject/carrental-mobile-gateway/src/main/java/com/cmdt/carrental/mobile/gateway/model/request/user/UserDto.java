package com.cmdt.carrental.mobile.gateway.model.request.user;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UserDto {

	@NotNull
    private String mobile;
	@NotNull
    private String password; //密码
	private Long organizationId;
    private String userName; //用户名
    private String realName;
    private String email;
    private String city;
//    private Long roleId; //默认员工角色
//    private Long userCategory;//默认4：员工
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Override
	public String toString() {
		return "UserDto [organizationId=" + organizationId + ", userName=" + userName + ", password=" + password
				+ ", realName=" + realName + ", mobile=" + mobile + ", email=" + email + ", city=" + city + "]";
	}
    
}
