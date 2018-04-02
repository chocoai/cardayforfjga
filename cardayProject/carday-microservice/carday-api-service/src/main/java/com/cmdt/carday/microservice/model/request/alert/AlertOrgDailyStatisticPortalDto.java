package com.cmdt.carday.microservice.model.request.alert;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.cmdt.carday.microservice.constraint.annotation.AlertType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AlertOrgDailyStatisticPortalDto{
	
	@Digits(message="userId格式错误", fraction = 0, integer = 10)
	private Long userId;
	
	@NotNull(message="AlertType不能为空")
	@AlertType
	private String type;
	
	private String from;
	
	private String to;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
