package com.cmdt.carrental.platform.service.model.request.station;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StationUnAssignVehicleDto {
	@NotNull(message="stationId不能为空")
    private String stationId;
	
	@NotNull(message="vehicleId不能为空")
    private String vehicleId;

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	
}
