package com.cmdt.carrental.common.model;

import java.io.Serializable;
import java.util.List;


public class AllocateDepModel implements Serializable{

	private static final long serialVersionUID = 1L;
    private Long allocateDepId; //分配部门ID
    private String deptName; //分配部门名称
    private String ids; //需要分配部门的对象编号
    private Long[] idArray; //需要分配部门的对象编号  Array
    private List<Long> idsList; //id list
    
	public Long getAllocateDepId() {
		return allocateDepId;
	}
	public void setAllocateDepId(Long allocateDepId) {
		this.allocateDepId = allocateDepId;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public Long[] getIdArray() {
		return idArray;
	}
	public void setIdArray(Long[] idArray) {
		this.idArray = idArray;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public List<Long> getIdsList() {
		return idsList;
	}
	public void setIdsList(List<Long> idsList) {
		this.idsList = idsList;
	}
    
}
