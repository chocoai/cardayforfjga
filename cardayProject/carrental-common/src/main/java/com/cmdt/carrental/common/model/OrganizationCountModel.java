package com.cmdt.carrental.common.model;

public class OrganizationCountModel {
	
    private Long id; //部门或子部门id
    private String name; //部门或子部门名
    private Integer personNum;//部门或子部门中的管理员或员工个数
    private Long parentId;
    private String parentIds;
    private String userCategory;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPersonNum() {
		return personNum;
	}
	public void setPersonNum(Integer personNum) {
		this.personNum = personNum;
	}
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
	public String getUserCategory() {
		return userCategory;
	}
	public void setUserCategory(String userCategory) {
		this.userCategory = userCategory;
	}
	@Override
	public String toString() {
		return "OrganizationCountModel [id=" + id + ", name=" + name + ", personNum=" + personNum + ", parentId="
				+ parentId + ", userCategory=" + userCategory + "]";
	}
	
	
	
	
}
