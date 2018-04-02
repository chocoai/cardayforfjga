package com.cmdt.carrental.common.model;

public class OBDQueryDTO {
	private String imei;
	
	public OBDQueryDTO(){
		
	}
	public OBDQueryDTO(String imei){
		this.imei = imei;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}

}
