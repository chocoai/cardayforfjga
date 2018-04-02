package com.cmdt.carrental.mobile.gateway.api.inf;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cmdt.carrental.mobile.gateway.model.request.stat.DaysStatDto;

@Produces(MediaType.APPLICATION_JSON)
public interface IStatisticService {
	
	public Response load7DaysMileage(@NotNull String username);
	
	public Response loadToYesMileage(@NotNull String username);
	
	public Response loadTop3DMileage(@NotNull String username);
	
    public Response load7DaysFuel(@NotNull String username);
	
	public Response loadToYesFuel(@NotNull String username);
	
	public Response loadTop3DFuel(@NotNull String username);

	//homepage
	public Response loadVehicleStat(@Valid @NotNull String userId);
	public Response load7DaysStat(@Valid @NotNull DaysStatDto data);
	public Response loadTop3DStat(@Valid @NotNull DaysStatDto data);
}
