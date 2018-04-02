package com.cmdt.carday.microservice.model.request.station;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StationUnAssignVehicleDto {
	
	@ApiModelProperty(value="站点ID",required=true)
	@NotNull(message="stationId不能为空")
    private String stationId;
	
	@ApiModelProperty(value="车辆ID",required=true)
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
