package com.cmdt.carrental.common.bean;

public enum OrganizationType {
	RENT_VEHICLE_ORGANIZAITON("0"),VEHICLE_USED_ORGANIZATION("1");
	private String value;
	private OrganizationType(String value){
		this.value=value;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
