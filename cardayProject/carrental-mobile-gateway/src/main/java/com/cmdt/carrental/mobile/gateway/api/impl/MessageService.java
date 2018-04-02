package com.cmdt.carrental.mobile.gateway.api.impl;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.mobile.gateway.api.inf.IMessageService;
import com.cmdt.carrental.mobile.gateway.common.Constants;
import com.cmdt.carrental.mobile.gateway.common.WsResponse;
import com.cmdt.carrental.mobile.gateway.model.MsgCountModel;
import com.cmdt.carrental.mobile.gateway.model.MsgDto;
import com.cmdt.carrental.mobile.gateway.model.MsgInfoModel;
import com.cmdt.carrental.mobile.gateway.service.MessageAppService;

public class MessageService implements IMessageService {

	private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);
	
	@Autowired
	private MessageAppService messageAppService;
	
	@Override
	@POST
	@Path("/loadMarker/{alarmId}")
	public Response loadMarkerPointsByAlarmId(@PathParam("alarmId")String alarmId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	@GET
	@Path("/loadMsgs/{userId}/{msgType}/{currentPage}/{pageSize}/{app}")
	public Response loadMsgs(@PathParam("userId") String userId, @PathParam("msgType") String msgType, @PathParam("currentPage") Integer currentPage, @PathParam("pageSize") Integer pageSize, @PathParam("app") String app) {
		WsResponse<MsgInfoModel> wsResponse = new WsResponse<MsgInfoModel>();
		try{
			MsgInfoModel statModel = messageAppService.loadMsgInfoByUser(userId, msgType, currentPage, pageSize, app);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setResult(statModel);
		}catch(Exception e){
			LOG.error("loadMsgs error",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
        return Response.ok( wsResponse ).build();
		
	}
	
	
	@Override
	@GET
	@Path("/loadMsgCount/{userId}/{app}")
	public Response loadMsgCount(@PathParam("userId") String userId, @PathParam("app") String app){
		WsResponse<MsgCountModel> wsResponse = new WsResponse<MsgCountModel>();
		try{
			MsgCountModel statModel = messageAppService.loadMsgCountByUser(userId, app);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setResult(statModel);
		}catch(Exception e){
			LOG.error("loadMsgCount error",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
        return Response.ok( wsResponse ).build();
	}


	@Override
	@POST
	@Path("/setMsgAsRead")
	public Response setMsgAsRead(MsgDto msgD) {
		WsResponse<String> wsResponse = new WsResponse<String>();
		try{
			String msg = messageAppService.setMsgAsRead(msgD.getMsgId(),msgD.getUserId());
			if("".equals(msg)){
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			}else{
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
				wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
				wsResponse.setResult(msg);
			}
		}catch(Exception e){
			LOG.error("setMsgAsRead error",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
        return Response.ok( wsResponse ).build();
	}
}
