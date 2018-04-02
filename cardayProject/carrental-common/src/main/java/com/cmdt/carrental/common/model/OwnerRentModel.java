package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class OwnerRentModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long rentId; //租户
    private String name; //租户名称
    
    public OwnerRentModel(){
    	
    }
    
    public OwnerRentModel(Long rentId, String name) {
        this.rentId = rentId;
        this.name = name;
    }
    
	public Long getRentId() {
		return rentId;
	}
	public void setRentId(Long rentId) {
		this.rentId = rentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
