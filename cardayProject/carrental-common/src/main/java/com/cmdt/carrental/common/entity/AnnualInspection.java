package com.cmdt.carrental.common.entity;

import java.sql.Date;

public class AnnualInspection {
	
	private Long id;
	private Long vehicleId;
	private Date inspectionExpiredate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Date getInspectionExpiredate() {
		return inspectionExpiredate;
	}
	public void setInspectionExpiredate(Date inspectionExpiredate) {
		this.inspectionExpiredate = inspectionExpiredate;
	}
	
}
