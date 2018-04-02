package com.cmdt.carrental.mobile.gateway.model.request.vehicle;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class VehicleLocationListDto {
	
	@NotNull(message="userId不能为空")
	private Long userId;
	
	private String vehicleNumber;
	
	private String deptId;
	
	private String vehicleType;
	@NotEmpty(message="currentPage不能为空")
	private String currentPage;
	@NotEmpty(message="numPerPage不能为空")
	private String numPerPage;
	private String fromOrgId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(String numPerPage) {
		this.numPerPage = numPerPage;
	}

	public String getFromOrgId() {
		return fromOrgId;
	}

	public void setFromOrgId(String fromOrgId) {
		this.fromOrgId = fromOrgId;
	}
	
}
