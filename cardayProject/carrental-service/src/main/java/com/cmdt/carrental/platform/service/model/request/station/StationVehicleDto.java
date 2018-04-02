package com.cmdt.carrental.platform.service.model.request.station;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by zhengjun.jing on 6/7/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StationVehicleDto {
	@NotNull(message="stationId不能为空")
    private String stationId;
	
	@NotNull(message="vehicleIds不能为空")
    private String vehicleIds;

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

	public String getVehicleIds() {
		return vehicleIds;
	}

	public void setVehicleIds(String vehicleIds) {
		this.vehicleIds = vehicleIds;
	}
}
