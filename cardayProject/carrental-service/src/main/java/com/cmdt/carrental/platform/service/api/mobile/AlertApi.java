package com.cmdt.carrental.platform.service.api.mobile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.model.VehicleAlertModel;
import com.cmdt.carrental.platform.service.biz.service.PlatformAlertStatisticService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.common.WsResponse;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.platform.service.model.request.alert.AlertCreateDto;
import com.cmdt.carrental.platform.service.model.request.alert.AlertOrgDailyStatisticDto;
import com.cmdt.carrental.platform.service.model.request.alert.AlertOrgDto;
import com.cmdt.carrental.platform.service.model.request.alert.AlertOrgStaticDto;
import com.cmdt.carrental.platform.service.model.response.alert.AlertDailyStatisticRetDto;
import com.cmdt.carrental.platform.service.model.response.alert.AlertOrgRetDto;
import com.cmdt.carrental.platform.service.model.response.alert.AlertOrgStatisticRetDto;

@Produces(MediaType.APPLICATION_JSON)
public class AlertApi{
	
	private static final Logger LOG = LoggerFactory.getLogger(AlertApi.class);
	
	@Autowired
	private PlatformAlertStatisticService alertStatisticService;
	
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
	
	@GET
	@Path("/getDetails/{warningId}")
	public VehicleAlertModel getAlertDetailsById(@PathParam("warningId")Long warningId){
		//WsResponse<VehicleAlertModel> wsResponse = new WsResponse<>();
		
		return alertStatisticService.getAlertById(warningId);
		
		/*try{
			wsResponse.setResult(alertStatisticService.getAlertById(warningId));
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("AlertServiceImpl.getDetails failed, error : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();*/
	}
	
	
	@GET
	@Path("/getNull/{warningId}")
	public void getAlertNull(@PathParam("warningId")Long warningId){
		//WsResponse<VehicleAlertModel> wsResponse = new WsResponse<>();
		
	}
	
	
	@GET
	@Path("/getString/{warningId}")
	public String getAlertString(@PathParam("warningId")String warningId){
		return warningId;
		
	}
	
	@GET
	@Path("/getResponse/{warningId}")
	public Response getAlertResponse(@PathParam("warningId")String warningId){
		WsResponse<Object> wsResponse = new WsResponse<>();
		wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
		wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
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

}
