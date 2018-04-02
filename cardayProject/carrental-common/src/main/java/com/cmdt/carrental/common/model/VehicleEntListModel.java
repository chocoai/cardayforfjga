package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class VehicleEntListModel implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long entId;
    private String entName;
	public Long getEntId() {
		return entId;
	}
	public void setEntId(Long entId) {
		this.entId = entId;
	}
	public String getEntName() {
		return entName;
	}
	public void setEntName(String entName) {
		this.entName = entName;
	}
	
    
	
	
	
	

}
