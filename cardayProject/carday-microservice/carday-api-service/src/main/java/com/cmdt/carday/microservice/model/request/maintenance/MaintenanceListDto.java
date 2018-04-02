package com.cmdt.carday.microservice.model.request.maintenance;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MaintenanceListDto {
	
	private Boolean includeSelf=Boolean.FALSE;
	
	private Boolean includeChild=Boolean.FALSE;
	
	
	private Long userId;
	
	private String vehicleNumber;
	private String searchScope;
	
	@Min(value=1)
	@NotNull(message="CurrentPage不能为空")
	private int currentPage;
	
	@Min(value=1)
	@NotNull(message="numPerPage不能为空")
	private int numPerPage;
	
	@Min(value=1)
	@NotNull(message="deptId不能为空")
	private Long deptId;
	
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getSearchScope() {
		return searchScope;
	}
	public void setSearchScope(String searchScope) {
		this.searchScope = searchScope;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getNumPerPage() {
		return numPerPage;
	}
	public void setNumPerPage(int numPerPage) {
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
