package com.cmdt.carrental.mobile.gateway.model.request.alert;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carrental.common.util.Patterns;
import com.cmdt.carrental.mobile.gateway.constraint.annotation.AlertOrgDailyStatisticDtoConstraint;
import com.cmdt.carrental.mobile.gateway.constraint.annotation.AlertType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@AlertOrgDailyStatisticDtoConstraint
public class AlertOrgDailyStatisticDto {
	
	@NotNull(message="AlertType不能为空")
	@AlertType
	private String type;
	
	private String from;
	
	private String to;
	
	@Digits(message="orgId格式错误,必须为数值", fraction = 0, integer = 8)
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
