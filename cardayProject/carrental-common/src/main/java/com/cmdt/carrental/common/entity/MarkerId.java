package com.cmdt.carrental.common.entity;

import java.io.Serializable;

public class MarkerId implements Serializable {
	
	private static final long serialVersionUID = 5760869301006119800L;
	private Long id; //编号
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
    
}
