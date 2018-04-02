package com.cmdt.carrental.mobile.gateway.model;

public class DeptDataModel {
	private String dept;
	private String data;
	public String getDept() {
		return dept;
	}
	public void setDept(String dept) {
		this.dept = dept;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "DeptDataModel [dept=" + dept + ", data=" + data + "]";
	}
	
	
}
