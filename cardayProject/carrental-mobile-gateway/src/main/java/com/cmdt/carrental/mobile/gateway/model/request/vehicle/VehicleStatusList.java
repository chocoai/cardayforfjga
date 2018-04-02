package com.cmdt.carrental.mobile.gateway.model.request.vehicle;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class VehicleStatusList {
	
	@NotNull
	private Long orgId;
	@NotNull(message="currentPage不能为空")
	@Min(value=0)
	private Integer currentPage;
	@NotNull(message="numPerPage不能为空")
	@Min(value=1)
	private Integer numPerPage; 
	@NotNull
	private Boolean selfDept;
	@NotNull 
	private Boolean childDept;
	@Min(value=0) @Max(value=3)
	private Integer type;

	private String vehicleNumber;
	
	
	
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Boolean getSelfDept() {
		return selfDept;
	}
	public void setSelfDept(Boolean selfDept) {
		this.selfDept = selfDept;
	}
	public Boolean getChildDept() {
		return childDept;
	}
	public void setChildDept(Boolean childDept) {
		this.childDept = childDept;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

}
