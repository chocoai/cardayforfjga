package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class VehicleCurrentUseModel implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long currentuseOrgId;
    private String currentuseOrgName;
	public Long getCurrentuseOrgId() {
		return currentuseOrgId;
	}
	public void setCurrentuseOrgId(Long currentuseOrgId) {
		this.currentuseOrgId = currentuseOrgId;
	}
	public String getCurrentuseOrgName() {
		return currentuseOrgName;
	}
	public void setCurrentuseOrgName(String currentuseOrgName) {
		this.currentuseOrgName = currentuseOrgName;
	}
	
	
	
	

}
