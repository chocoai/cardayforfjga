package com.cmdt.carrental.platform.service.model.request.alert;

import com.cmdt.carrental.platform.service.constraint.annotation.AlertOrgDtoConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@AlertOrgDtoConstraint
public class AlertOrgPortalDto extends AlertOrgDto{
	
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
