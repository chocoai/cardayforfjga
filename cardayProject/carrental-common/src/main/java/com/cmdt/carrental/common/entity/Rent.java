package com.cmdt.carrental.common.entity;

import java.io.Serializable;

public class Rent implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id; //编号
    private String name; //租户名称
    private String linkman;//租户联系人真实姓名
    private String linkmanPhone;//租户联系人电话
    private String linkmanEmail;//租户联系人邮箱
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getLinkmanPhone() {
		return linkmanPhone;
	}
	public void setLinkmanPhone(String linkmanPhone) {
		this.linkmanPhone = linkmanPhone;
	}
	public String getLinkmanEmail() {
		return linkmanEmail;
	}
	public void setLinkmanEmail(String linkmanEmail) {
		this.linkmanEmail = linkmanEmail;
	}
    
    
}
