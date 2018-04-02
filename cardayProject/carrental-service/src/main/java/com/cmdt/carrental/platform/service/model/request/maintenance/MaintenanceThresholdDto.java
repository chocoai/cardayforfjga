package com.cmdt.carrental.platform.service.model.request.maintenance;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MaintenanceThresholdDto {
	private Long id;
	private Long maintenanceMileage;
	
    private int thresholdMonth;
    
    private Long alertMileage;
    
    private String maintenanceDueTimeF;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMaintenanceMileage() {
		return maintenanceMileage;
	}

	public void setMaintenanceMileage(Long maintenanceMileage) {
		this.maintenanceMileage = maintenanceMileage;
	}

	public int getThresholdMonth() {
		return thresholdMonth;
	}

	public void setThresholdMonth(int thresholdMonth) {
		this.thresholdMonth = thresholdMonth;
	}

	public String getMaintenanceDueTimeF() {
		return maintenanceDueTimeF;
	}

	public void setMaintenanceDueTimeF(String maintenanceDueTimeF) {
		this.maintenanceDueTimeF = maintenanceDueTimeF;
	}

	public Long getAlertMileage() {
		return alertMileage;
	}

	public void setAlertMileage(Long alertMileage) {
		this.alertMileage = alertMileage;
	}
    
    
}
