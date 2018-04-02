package com.cmdt.carrental.mobile.gateway.api.inf;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cmdt.carrental.mobile.gateway.model.request.alert.AlertOrgDailyStatisticDto;
import com.cmdt.carrental.mobile.gateway.model.request.alert.AlertOrgDto;
import com.cmdt.carrental.mobile.gateway.model.request.alert.AlertOrgStaticDto;

@Produces(MediaType.APPLICATION_JSON)
public interface IAlertService {
	
	public Response statisticAlertByTypeAndTimeRanger(@Valid @NotNull AlertOrgStaticDto orgStatDto);
	
	public Response getAlertDetailsById(@Min(value=1) Long warningId);

	public Response statisticDailyAlertByTypeAndTimeRanger(@Valid @NotNull AlertOrgDailyStatisticDto orgStatDto);
	
	public Response QueryAlertByTypeOrgAndTimeRanger(@Valid @NotNull AlertOrgDto alertOrgDto);
}
