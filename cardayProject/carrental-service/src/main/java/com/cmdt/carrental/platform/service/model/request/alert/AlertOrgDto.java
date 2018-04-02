package com.cmdt.carrental.platform.service.model.request.alert;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carrental.platform.service.common.Patterns;
import com.cmdt.carrental.platform.service.constraint.annotation.AlertOrgDtoConstraint;
import com.cmdt.carrental.platform.service.constraint.annotation.AlertType;
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
	@NotNull(message="DepartmentId不能为空")
	private Long departmentId;
	
	@Min(value=1)
	@NotNull(message="PageSize不能为空")
	private int pageSize;
	
	@Min(value=1)
	@NotNull(message="CurrentPage不能为空")
	private int currentPage;


	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

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
	
}
