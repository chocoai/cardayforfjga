package com.cmdt.carrental.mobile.gateway.model.request.alert;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carrental.common.util.Patterns;
import com.cmdt.carrental.mobile.gateway.constraint.annotation.AlertOrgDtoConstraint;
import com.cmdt.carrental.mobile.gateway.constraint.annotation.AlertType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@AlertOrgDtoConstraint
public class AlertOrgDto{
	
	@NotNull(message="AlertType不能为空")
	@AlertType
	private String type;
	
	@NotNull(message="From Date不能为空")
	@Pattern(regexp = Patterns.REG_DATE_FORMAT_SIX_DIG,message="From Date format error!")
	private String from;
	
	@NotNull(message="To Date不能为空")
	@Pattern(regexp = Patterns.REG_DATE_FORMAT_SIX_DIG,message="To Date format error!")
	private String to;
		
	@Min(value=1)
	@NotNull(message="orgId不能为空")
	private Long orgId;
	
	@Min(value=1)
	@NotNull(message="PageSize不能为空")
	private int pageSize;
	
	@Min(value=1)
	@NotNull(message="CurrentPage不能为空")
	private int currentPage;
	
	@NotNull
	private Boolean selfDept;
	
	@NotNull
	private Boolean childDept;



	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
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

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
}
