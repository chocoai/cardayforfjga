package com.cmdt.carrental.common.model;

public class AlertCountModel {
	
	
	private String alertType;
	private int value;
	
	
	public AlertCountModel(){
		super();
	}
	
	public AlertCountModel(String alertType,int value){
		this.alertType = alertType;
		this.value = value;
	}
	
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	

}
