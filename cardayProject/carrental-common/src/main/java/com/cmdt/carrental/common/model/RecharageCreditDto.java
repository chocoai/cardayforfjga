package com.cmdt.carrental.common.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecharageCreditDto implements Serializable {

	private static final long serialVersionUID = 3554831281087856593L;

	private Long orgId; // 企业编号

	private Integer creditValue;// 充值金额

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Integer getCreditValue() {
		return creditValue;
	}

	public void setCreditValue(Integer creditValue) {
		this.creditValue = creditValue;
	}

}
