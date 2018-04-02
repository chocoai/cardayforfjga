package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class OrganizationCreditModel implements Serializable{
	
	private static final long serialVersionUID = 8278371275055652977L;

	private Long orgId;
	
	private Double availabeCredit;
	
	private Double limitedCredit;

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Double getAvailabeCredit() {
		return availabeCredit;
	}

	public void setAvailabeCredit(Double availabeCredit) {
		this.availabeCredit = availabeCredit;
	}

	public Double getLimitedCredit() {
		return limitedCredit;
	}

	public void setLimitedCredit(Double limitedCredit) {
		this.limitedCredit = limitedCredit;
	}
	
	
}
