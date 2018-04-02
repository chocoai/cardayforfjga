package com.cmdt.carday.microservice.model.request.maintenance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonInclude(Include.NON_NULL)
public class MaintenanceCreateDto {
	
	@Min(value=1)
	@NotNull(message="userId不能为空")
	private Long userId;
	
    private Long vehicleId;
    
    private String deviceNumber;
    
    @NotNull(message="curTime不能为空")
//	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD,message="curTime日期格式错误，应为yyyy-mm-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date curTime;
    
    private Long headerMaintenanceMileage;
    
    private Long maintenanceMileage;
   
    private String curTimeF;
    
    private int maintenanceTime;
    
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	
	public String getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public Long getHeaderMaintenanceMileage() {
		return headerMaintenanceMileage;
	}
	public void setHeaderMaintenanceMileage(Long headerMaintenanceMileage) {
		this.headerMaintenanceMileage = headerMaintenanceMileage;
	}
	public Long getMaintenanceMileage() {
		return maintenanceMileage;
	}
	public void setMaintenanceMileage(Long maintenanceMileage) {
		this.maintenanceMileage = maintenanceMileage;
	}
	public Date getCurTime() {
		return curTime;
	}
	public void setCurTime(Date curTime) {
		this.curTime = curTime;
	}
	public String getCurTimeF() {
		return curTimeF;
	}
	public void setCurTimeF(String curTimeF) {
		this.curTimeF = curTimeF;
	}
	public int getMaintenanceTime() {
		return maintenanceTime;
	}
	public void setMaintenanceTime(int maintenanceTime) {
		this.maintenanceTime = maintenanceTime;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
