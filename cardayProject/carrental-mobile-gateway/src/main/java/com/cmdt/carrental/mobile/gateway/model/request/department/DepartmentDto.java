package com.cmdt.carrental.mobile.gateway.model.request.department;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DepartmentDto {
	
	@NotNull
	@Digits(message="orgId格式错误", fraction = 0, integer = 9)
	private long orgId;
	
	@Digits(integer=3, fraction=0)
	@DecimalMin(value="0", inclusive=true)
	private int startDepth;
	
	@Digits(integer=3, fraction=0)
	@DecimalMin(value="0", inclusive=true)
	private int endDepth;

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public int getStartDepth() {
		return startDepth;
	}

	public void setStartDepth(int startDepth) {
		this.startDepth = startDepth;
	}

	public int getEndDepth() {
		return endDepth;
	}

	public void setEndDepth(int endDepth) {
		this.endDepth = endDepth;
	}
	



}
