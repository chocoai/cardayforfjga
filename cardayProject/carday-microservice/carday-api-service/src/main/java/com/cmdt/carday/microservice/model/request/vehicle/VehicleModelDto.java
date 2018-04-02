package com.cmdt.carday.microservice.model.request.vehicle;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public class VehicleModelDto implements Serializable {

	private static final long serialVersionUID = 6307398318902029416L;
	
	@Digits(fraction=0,integer=Integer.MAX_VALUE)
	@NotNull(message="deptId不能为空")
	private Long deptId;

	@NotNull(message="tFlag不能为空")
	private String tFlag;

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String gettFlag() {
		return tFlag;
	}

	public void settFlag(String tFlag) {
		this.tFlag = tFlag;
	}

}
