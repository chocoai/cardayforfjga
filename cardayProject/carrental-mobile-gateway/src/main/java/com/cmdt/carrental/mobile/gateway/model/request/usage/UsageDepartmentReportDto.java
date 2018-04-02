package com.cmdt.carrental.mobile.gateway.model.request.usage;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carrental.common.util.Patterns;
import com.cmdt.carrental.mobile.gateway.constraint.annotation.UsageType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UsageDepartmentReportDto {
	
	@NotNull
	private Long orgId;
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD,message="Start Date format error!")
	private String startTime;
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD,message="End Date format error!")
	private String endTime;
	@NotNull(message="Query Type不能为空")
	@UsageType
	private String queryType;
	@NotNull
	private Boolean selfDept;
	@NotNull
	private Boolean childDept;
	
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
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
