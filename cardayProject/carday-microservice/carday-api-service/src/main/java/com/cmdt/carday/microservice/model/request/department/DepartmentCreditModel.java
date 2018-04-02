package com.cmdt.carday.microservice.model.request.department;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
@ApiModel(value = "DepartmentCreditModel", description = "信息描述")
public class DepartmentCreditModel {

	@ApiModelProperty("额度")
	@NotNull(message="availableCredit不能为空")
	private Double availableCredit;
	@ApiModelProperty("未分配额度")
	@NotNull(message="limitedCredit不能为空")
	private Double limitedCredit;
	@ApiModelProperty("组织id")
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
