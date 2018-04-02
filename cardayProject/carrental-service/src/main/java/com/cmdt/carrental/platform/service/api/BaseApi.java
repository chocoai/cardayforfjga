package com.cmdt.carrental.platform.service.api;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.Message.MessageType;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.service.BusiOrderService;
import com.cmdt.carrental.common.service.MessageService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.common.WsResponse;

public class BaseApi {
	private static final Logger LOG = LoggerFactory.getLogger(BaseApi.class);

	@Autowired
	private UserService userService;
	@Autowired
    private BusiOrderService orderService;
	@Autowired
    private VehicleService vehicleService;
	@Autowired
    private CommunicationService communicationService;
    @Autowired
    private MessageService messageService;
    
    
	protected User getUserById(Long userId){
		return userService.findById(userId);
	}
	
	protected <T> Response buildWsResponse(){
		LOG.debug("BaseApi.buildWsResponse");
		//build response
		WsResponse<T> wsResponse = new WsResponse<T>();
		wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
		wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		return Response.ok( wsResponse ).build();
	}
	
	/**
	 * buildResponse
	 * @param c
	 * @return
	 */
	protected <T> Response buildWsResponse(T c){
		LOG.debug("BaseApi.buildWsResponse");
		//build response
		WsResponse<T> wsResponse = new WsResponse<T>();
		wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
		wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		wsResponse.setResult(c);
		return Response.ok( wsResponse ).build();
	}
	
	/**
	 * buildFailureResponse
	 * @return
	 */
	protected <T> Response buildFailureResponse(){
		LOG.debug("BaseApi.buildFailureResponse");
		//build response
		WsResponse<T> wsResponse = new WsResponse<T>();
		wsResponse.setStatus(Constants.API_STATUS_FAILURE);
		wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		return Response.ok( wsResponse ).build();
	}

	/**
	 * buildFailureResponse
	 * @return
	 */
	protected <T> Response buildFailureResponse(String msg){
		LOG.debug("BaseApi.buildFailureResponse");
		//build response
		WsResponse<T> wsResponse = new WsResponse<T>();
		wsResponse.setStatus(Constants.API_STATUS_FAILURE);
		wsResponse.getMessages().add(msg);
		return Response.ok( wsResponse ).build();
	}
	
	
	/**
	 * buildFailureResponse
	 * @param statusCode
	 * @param errorMsg
	 * @return
	 */
	protected <T> Response buildFailureResponse(String statusCode, String errorMsg){
		LOG.debug("BaseApi.buildFailureResponse()");
		//build response
		WsResponse<T> wsResponse = new WsResponse<T>();
		wsResponse.setStatus(statusCode);
		wsResponse.getMessages().add(errorMsg);
		return Response.ok( wsResponse ).build();
	}
	
}
