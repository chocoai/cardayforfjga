package com.cmdt.carrental.common.model;

public class UsageReportAppColumnarItemModel {

	private Long deptId;//x轴值(部门id)
	private String deptName;//x轴值(部门名)
	private String data;//y轴值
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	
	
	
	
}
