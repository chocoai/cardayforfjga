package com.cmdt.carday.microservice.model.request.alert;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carday.microservice.common.Patterns;
import com.cmdt.carday.microservice.constraint.annotation.AlertType;

public class AlertDataByTypeDto implements Serializable{
	
	private static final long serialVersionUID = 83926205640172020L;
	
	@Min(value=1)
	@NotNull(message="userId不能为空")
	private Long userId;
	
	private Long orgId;
	
	@NotNull(message="AlertType不能为空")
	@AlertType
	private String alertType;

	@Min(value=1)
	@NotNull(message="CurrentPage不能为空")
	private int currentPage;
	
	@Min(value=1)
	@NotNull(message="numPerPage不能为空")
	private int numPerPage;
	
	@NotNull(message="startTime 不能为空")
	@Pattern(regexp = Patterns.REG_DATE_FORMAT_SIX_DIG,message="startTime格式错误，应为yyyyMMdd")
	private String startTime;
	
	@NotNull(message="endTime不能为空")
	@Pattern(regexp = Patterns.REG_DATE_FORMAT_SIX_DIG,message="endTime格式错误，应为yyyyMMdd")
	private String endTime;
	
	private Boolean includeSelf=Boolean.TRUE;
	
	private Boolean includeChild=Boolean.TRUE;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
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
