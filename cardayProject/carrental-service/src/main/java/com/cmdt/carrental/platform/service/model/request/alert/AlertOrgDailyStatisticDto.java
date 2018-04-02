package com.cmdt.carrental.platform.service.model.request.alert;

import javax.validation.constraints.NotNull;

import com.cmdt.carrental.platform.service.constraint.annotation.AlertOrgDailyStatisticDtoConstraint;
import com.cmdt.carrental.platform.service.constraint.annotation.AlertType;
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
	
	private String entId;
	
	private String departmentId;

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

	public String getEntId() {
		return entId;
	}

	public void setEntId(String entId) {
		this.entId = entId;
	}

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

}
