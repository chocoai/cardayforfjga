package com.cmdt.carrental.common.model;

public class OrganizationModel {
	
    private Long parentId; //父编号
    private String parentIds; //父编号列表，如0,1,2
    
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
    
    
}
