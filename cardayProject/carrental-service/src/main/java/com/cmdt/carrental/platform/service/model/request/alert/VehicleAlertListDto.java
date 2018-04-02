package com.cmdt.carrental.platform.service.model.request.alert;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.cmdt.carrental.platform.service.constraint.annotation.AlertType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class VehicleAlertListDto implements Serializable{
	
	private static final long serialVersionUID = -9184873459924062979L;

	private Long userId;
	
	private String vehicleNumber;
	
	private String vehicleType;
	
	private Long fromOrgId;
	
	@Min(value=1)
	@NotNull(message="deptId不能为空")
	private Long deptId;
	
	private String deptName;
	
	private Date endTime;
	
	private Date startTime;
	
	@Min(value=1)
	@NotNull(message="CurrentPage不能为空")
	private Integer currentPage;
	
	@Min(value=1)
	@NotNull(message="numPerPage不能为空")
	private Integer numPerPage;
	
	
	@NotNull(message="AlertType不能为空")
	@AlertType
	private String alertType;
	
	private Boolean includeSelf=Boolean.FALSE;
	
	private Boolean includeChild=Boolean.FALSE;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public Long getFromOrgId() {
		return fromOrgId;
	}

	public void setFromOrgId(Long fromOrgId) {
		this.fromOrgId = fromOrgId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
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

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
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
