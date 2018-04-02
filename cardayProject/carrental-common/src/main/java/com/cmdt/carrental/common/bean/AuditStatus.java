package com.cmdt.carrental.common.bean;

public enum AuditStatus {
	TOAUDITED("0"),NOPASSED("1"),NOACTIVATED("2"),INSERVICE("3"),EXPIRED("4"),SERVICESUSPEND("5");
	private String value;
	private AuditStatus(String value){
		this.value=value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	

}
