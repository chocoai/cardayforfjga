package com.cmdt.carrental.common.entity;

import java.io.Serializable;

public class UserInfo implements Serializable {
	
	private static final long serialVersionUID = 5760869301006119800L;
	private Long id; //编号
    private Long organizationId; //所属公司
    private String phone; //来电人电话
    private String realName; //来电人姓名
    private String company; //来电人单位
    
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
 
}
