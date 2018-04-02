package com.cmdt.carrental.platform.service.model.request.alert;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carrental.platform.service.common.Patterns;
import com.cmdt.carrental.platform.service.constraint.annotation.AlertOrgStaticDtoConstraint;
import com.cmdt.carrental.platform.service.constraint.annotation.AlertType;
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
	@NotNull(message="EntId不能为空")
	private Long entId;

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

	public Long getEntId() {
		return entId;
	}

	public void setEntId(Long entId) {
		this.entId = entId;
	}
		
}
