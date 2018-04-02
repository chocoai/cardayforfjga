package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class VehicleMaintainInfoModel implements Serializable{
	
	private static final long serialVersionUID = -3175569481526921731L;
	
	private Long deptId;
	
	private String vehicleNumber;
	
	private Integer currentPage;
	
	private Integer numPerPage;
	
	private String searchScope;
	
	private Boolean includeSelf=Boolean.FALSE;
	
	private Boolean includeChild=Boolean.FALSE;

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	
	public String getSearchScope() {
		return searchScope;
	}

	public void setSearchScope(String searchScope) {
		this.searchScope = searchScope;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(Integer numPerPage) {
		this.numPerPage = numPerPage;
	}

	public Boolean getIncludeSelf() {
		return includeSelf;
	}

	public void setIncludeSelf(Boolean includeSelf) {
		this.includeSelf = includeSelf;
	}

	public Boolean getIncludeChild() {
		return includeChild;
	}

	public void setIncludeChild(Boolean includeChild) {
		this.includeChild = includeChild;
	}

	
}
