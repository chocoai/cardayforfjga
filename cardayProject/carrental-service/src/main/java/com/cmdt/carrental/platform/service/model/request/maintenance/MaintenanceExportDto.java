package com.cmdt.carrental.platform.service.model.request.maintenance;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MaintenanceExportDto {
	@Min(value=1)
	@NotNull(message="userId不能为空")
	private Long userId;
	
	private String vehicleNumber;
	
	private Long deptId;
	
	private String Ids;
	
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public String getIds() {
		return Ids;
	}
	public void setIds(String ids) {
		Ids = ids;
	}
	
	
}
