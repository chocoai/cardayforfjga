package com.cmdt.carrental.common.entity;

import java.util.List;

public class VehicleAnnualInspectionDto {

	private Long entId;
	
	private int currentPage;
	
	private int numPerPage;
	
	private String vehicleNumber;
	
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
	public List<Long> getDeptIdList() {
		return deptIdList;
	}
	public void setDeptIdList(List<Long> deptIdList) {
		this.deptIdList = deptIdList;
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
}
