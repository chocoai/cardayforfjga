package com.cmdt.carrental.mobile.gateway.api.inf;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cmdt.carrental.mobile.gateway.model.MsgDto;

@Produces(MediaType.APPLICATION_JSON)
public interface IMessageService {

	public Response loadMarkerPointsByAlarmId(@NotNull String alarmId);
	
	public Response loadMsgs(@NotNull String userId, @NotNull String msgType, @NotNull Integer currentPage, @NotNull Integer pageSize, String app);

	public Response loadMsgCount(@NotNull String userId, String app);
	
	public Response setMsgAsRead(@Valid @NotNull MsgDto msgD);
	
}
