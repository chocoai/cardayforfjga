package com.cmdt.carday.microservice.model.request.station;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by zhengjun.jing on 6/7/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StationVehicleDto {
	
	@ApiModelProperty(value="站点ID",required=true)
	@NotNull(message="stationId不能为空")
    private String stationId;
	
	@ApiModelProperty(value="分配车辆ID，以逗号分隔",required=true)
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
