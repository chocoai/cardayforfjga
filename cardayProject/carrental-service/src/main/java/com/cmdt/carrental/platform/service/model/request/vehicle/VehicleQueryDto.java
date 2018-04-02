package com.cmdt.carrental.platform.service.model.request.vehicle;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public class VehicleQueryDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@NotNull(message="entId 不能为空")
	@Digits(message="entId只能为数字", fraction=1,integer=Integer.MAX_VALUE)
	private Long entId;
	
	private String vehicleNumber;
	
	private String entName;

	public Long getEntId() {
		return entId;
	}

	public void setEntId(Long entId) {
		this.entId = entId;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getEntName() {
		return entName;
	}

	public void setEntName(String entName) {
		this.entName = entName;
	}
	
}
