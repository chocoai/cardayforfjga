package com.cmdt.carrental.mobile.gateway.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carrental.common.util.Patterns;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UsageReportVehiclePropertyQueryDto {

	@NotNull
	private Long orgId;
	@NotNull
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD,message="starttime日期格式错误，应为yyyy-mm-dd!")
	private String starttime;
	@NotNull
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD,message="endtime日期格式错误，应为yyyy-mm-dd!")
	private String endtime;
	@Min(value=1)
	@NotNull(message="currentPage不能为空")
	private Integer currentPage;
	@Min(value=1)
	@NotNull(message="numPerPage不能为空")
	private Integer numPerPage;
	@NotNull
	private Boolean selfDept;
	@NotNull
	private Boolean childDept;
	
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
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
	
}
