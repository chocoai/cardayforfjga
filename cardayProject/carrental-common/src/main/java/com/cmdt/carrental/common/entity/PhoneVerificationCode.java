package com.cmdt.carrental.common.entity;

import java.io.Serializable;

public class PhoneVerificationCode implements Serializable{
	private static final long serialVersionUID = 1L;
	private String phoneNumber;
	private String code;
	private java.sql.Timestamp  expirationTime;
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public java.sql.Timestamp getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(java.sql.Timestamp expirationTime) {
		this.expirationTime = expirationTime;
	}
	
}
