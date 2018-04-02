package com.cmdt.carrental.common.entity;

import java.io.Serializable;

public class RentOrg implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long retid;
	private Long orgid;
	public Long getRetid() {
		return retid;
	}
	public void setRetid(Long retid) {
		this.retid = retid;
	}
	public Long getOrgid() {
		return orgid;
	}
	public void setOrgid(Long orgid) {
		this.orgid = orgid;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

    
}
