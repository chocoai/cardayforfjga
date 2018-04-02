package com.cmdt.carrental.mobile.gateway.model.request.alert;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carrental.common.util.Patterns;
import com.cmdt.carrental.mobile.gateway.constraint.annotation.AlertOrgStaticDtoConstraint;
import com.cmdt.carrental.mobile.gateway.constraint.annotation.AlertType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@AlertOrgStaticDtoConstraint
public class AlertOrgStaticDto {
	@NotNull(message="AlertType不能为空")
	@AlertType
	private String type;
	
	@NotNull(message="From Date不能为空")
	@Pattern(regexp = Patterns.REG_DATE_FORMAT_SIX_DIG,message="From日期格式错误，应为YYYYMMDD!")
	private String from;
	
	@NotNull(message="To Date不能为空")
	@Pattern(regexp = Patterns.REG_DATE_FORMAT_SIX_DIG,message="To日期格式错误，应为YYYYMMDD!")
	private String to;
		
	@Min(value=1)
	@NotNull(message="orgId不能为空")
	private Long orgId;
	
	@NotNull
	private Boolean selfDept;
	
	@NotNull
	private Boolean childDept;
	

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
