package com.cmdt.carrental.platform.service.model.request.annual;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AnnualInspectionPortalDto {
	
	@Min(value=1)
	@NotNull(message="userId不能为空")
	private Long userId;
	
	private Long entId;
	
	@Min(value=1)
	@NotNull(message="CurrentPage不能为空")
	private int currentPage;
	
	@Min(value=1)
	@NotNull(message="numPerPage不能为空")
	private int numPerPage;
	
	private String vehicleNumber;
	
	@Min(value=1)
	@NotNull(message="deptId不能为空")
	private Long deptId;
	
	private List<Long> deptIdList;
	
	private String insuranceStatus;
	
	private String inspectionStatus;
	
	private boolean selfDept;
	
	private boolean childDept;
	
	public Long getEntId() {
		return entId;
	}
	public void setEntId(Long entId) {
		this.entId = entId;
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
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public String getInsuranceStatus() {
		return insuranceStatus;
	}
	public void setInsuranceStatus(String insuranceStatus) {
		this.insuranceStatus = insuranceStatus;
	}
	public String getInspectionStatus() {
		return inspectionStatus;
	}
	public void setInspectionStatus(String inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public boolean getSelfDept() {
		return selfDept;
	}
	public void setSelfDept(boolean selfDept) {
		this.selfDept = selfDept;
	}
	public boolean getChildDept() {
		return childDept;
	}
	public void setChildDept(boolean childDept) {
		this.childDept = childDept;
	}
	public List<Long> getDeptIdList() {
		return deptIdList;
	}
	public void setDeptIdList(List<Long> deptIdList) {
		this.deptIdList = deptIdList;
	}
}
