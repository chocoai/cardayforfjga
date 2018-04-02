package com.cmdt.carrental.common.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AuditInfoModel implements Serializable{
	private static final long serialVersionUID = -1984102504109513338L;
	
	private String realname;
	private String role;
	private String phone;
	private String status;
	private Date auditTime;
	private String refuseComments;
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public String getRefuseComments() {
		return refuseComments;
	}
	public void setRefuseComments(String refuseComments) {
		this.refuseComments = refuseComments;
	}
	
	
	

}
