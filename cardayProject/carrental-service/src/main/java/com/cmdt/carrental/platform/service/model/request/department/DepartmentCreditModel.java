package com.cmdt.carrental.platform.service.model.request.department;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DepartmentCreditModel {

	@NotNull(message="availableCredit不能为空")
	private Double availableCredit;
	@NotNull(message="limitedCredit不能为空")
	private Double limitedCredit;
	@NotNull(message="orgId不能为空")
	private Long orgId;

	public Double getAvailableCredit() {
		return availableCredit;
	}

	public void setAvailableCredit(Double availableCredit) {
		this.availableCredit = availableCredit;
	}

	public Double getLimitedCredit() {
		return limitedCredit;
	}

	public void setLimitedCredit(Double limitedCredit) {
		this.limitedCredit = limitedCredit;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
    
}
