package com.cmdt.carrental.mobile.gateway.api.inf;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.cmdt.carrental.mobile.gateway.model.BindDeviceAndVehicleDto;
import com.cmdt.carrental.mobile.gateway.model.LimitSpeedDto;

public interface IDeviceService {
	
	public Response queryDeviceStaticData(@Valid @NotNull @PathParam("devNo") String devNo);
	public Response queryDeviceRealtimeData(@Valid @NotNull @PathParam("devNo") String devNo);
	public Response setLimitSpeed(@Valid @NotNull LimitSpeedDto dto);
	public Response bindDeviceAndVehicle(@Valid @NotNull BindDeviceAndVehicleDto bindDeviceAndVehicleDto);
	public Response unbindDeviceAndVehicle(@Valid @NotNull BindDeviceAndVehicleDto bindDeviceAndVehicleDto);
}
