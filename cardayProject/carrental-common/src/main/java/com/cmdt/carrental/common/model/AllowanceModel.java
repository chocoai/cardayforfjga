package com.cmdt.carrental.common.model;

public class AllowanceModel {
	
	Long id;
	String allowanceName;
	Double allowanceValue;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAllowanceName() {
		return allowanceName;
	}
	public void setAllowanceName(String allowanceName) {
		this.allowanceName = allowanceName;
	}
	public Double getAllowanceValue() {
		return allowanceValue;
	}
	public void setAllowanceValue(Double allowanceValue) {
		this.allowanceValue = allowanceValue;
	}
	
	

}
