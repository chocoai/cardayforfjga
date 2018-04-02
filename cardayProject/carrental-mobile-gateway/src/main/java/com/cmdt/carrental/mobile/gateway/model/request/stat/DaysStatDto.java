package com.cmdt.carrental.mobile.gateway.model.request.stat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.cmdt.carrental.mobile.gateway.constraint.annotation.StatisticType;

public class DaysStatDto {
	
	@NotNull
	private Long orgId;
	@StatisticType
	private String dataType;
	@Digits(message="startDate只能为数字", fraction = 0, integer = 14)
	private Long startDate;
	@Digits(message="endDate只能为数字", fraction = 0, integer = 14)
	private Long endDate;
	@NotNull
	private Boolean selfDept;
	@NotNull
	private Boolean childDept;
	
	

	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public Long getStartDate() {
		return startDate;
	}
	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}
	public Long getEndDate() {
		return endDate;
	}
	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
	@Override
	public String toString() {
		return "RT7DaysStatDto [orgId=" + orgId + ", dataType=" + dataType + ", startDate="
				+ startDate + ", endDate=" + endDate + "]";
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
	
}
