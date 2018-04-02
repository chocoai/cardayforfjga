package com.cmdt.carrental.mobile.gateway.api.impl;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.model.VehicleAlertModel;
import com.cmdt.carrental.common.service.VehicleAlertService;
import com.cmdt.carrental.mobile.gateway.api.inf.IAlertService;
import com.cmdt.carrental.mobile.gateway.common.Constants;
import com.cmdt.carrental.mobile.gateway.common.WsResponse;
import com.cmdt.carrental.mobile.gateway.model.request.alert.AlertCreateDto;
import com.cmdt.carrental.mobile.gateway.model.request.alert.AlertOrgDailyStatisticDto;
import com.cmdt.carrental.mobile.gateway.model.request.alert.AlertOrgDto;
import com.cmdt.carrental.mobile.gateway.model.request.alert.AlertOrgStaticDto;
import com.cmdt.carrental.mobile.gateway.model.response.alert.AlertDailyStatisticRetDto;
import com.cmdt.carrental.mobile.gateway.model.response.alert.AlertOrgRetDto;
import com.cmdt.carrental.mobile.gateway.model.response.alert.AlertOrgStatisticRetDto;
import com.cmdt.carrental.mobile.gateway.service.AlertStatisticService;

public class AlertServiceImpl implements IAlertService{
	
	private static final Logger LOG = LoggerFactory.getLogger(AlertServiceImpl.class);
	
	@Autowired
	private AlertStatisticService alertStatisticService;
	
	@Autowired
	private VehicleAlertService alertService;
	
	@POST
	@Path("/statByTypeAndTime")
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response statisticAlertByTypeAndTimeRanger(@Valid @NotNull AlertOrgStaticDto orgStatDto){
		WsResponse<AlertOrgStatisticRetDto> wsResponse = new WsResponse<>();
		try{
			wsResponse.setResult(alertStatisticService.statisticAlertByTypeAndTimeRanger(orgStatDto));
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("AlertServiceImpl.statByTypeAndTime failed, error : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@Override
	@GET
	@Path("/getDetails/{warningId}")
	public Response getAlertDetailsById(@PathParam("warningId")Long warningId){
		WsResponse<VehicleAlertModel> wsResponse = new WsResponse<>();
		try{
			wsResponse.setResult(alertStatisticService.getAlertById(warningId));
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("AlertServiceImpl.getDetails failed, error : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@POST
	@Path("/statDailyByTypeAndTime")
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response statisticDailyAlertByTypeAndTimeRanger(@Valid @NotNull AlertOrgDailyStatisticDto orgStatDto){
		WsResponse<AlertDailyStatisticRetDto> wsResponse = new WsResponse<>();
		try{
			wsResponse.setResult(alertStatisticService.statisticDailyAlertByTypeAndTimeRanger(orgStatDto));
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("AlertServiceImpl.statisticDailyAlertByTypeAndTimeRanger failed, error : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@POST
	@Path("/queryOrgAlert")
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response QueryAlertByTypeOrgAndTimeRanger(@Valid @NotNull AlertOrgDto alertOrgDto){
		WsResponse<AlertOrgRetDto> wsResponse = new WsResponse<>();
		try{
			wsResponse.setResult(alertStatisticService.queryAlertByTypeAndTimeRanger(alertOrgDto));
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("AlertServiceImpl.queryOrgAlert failed, error : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@POST
	@Path("/createAlert")
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response generateAlertByType(@Valid @NotNull AlertCreateDto alertCreateDto){
		WsResponse<String> wsResponse = new WsResponse<>();
		try{
			alertStatisticService.generateAlertByType(alertCreateDto);
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("AlertServiceImpl.generateAlertByType failed, error : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@GET
	@Path("/check/{vehicleId}")
	public Response checkViolateAlarm(@PathParam("vehicleId")Long vehicleId){
		WsResponse<Boolean> wsResponse = new WsResponse<>();
		try{
			wsResponse.setResult(alertService.voilateAlarmProcess(vehicleId));
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("AlertServiceImpl.checkViolateAlarm failed, error : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}

}
