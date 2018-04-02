package com.cmdt.carrental.rt.data.service.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.rt.data.service.biz.RealtimeDataService;


public class BaseApi {
	
    protected static final Logger LOG = LoggerFactory.getLogger(BaseApi.class);
	
	@Autowired
    protected RealtimeDataService dataService;
	   
//	/**
//	 * buildWsResponse
//	 * @return
//	 */
//	protected <T> Response buildWsResponse(){
//		LOG.debug("BaseApi.buildWsResponse");
//		//build response
//		WsResponse<T> wsResponse = new WsResponse<T>();
//		wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
//		wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
//		return Response.ok( wsResponse ).build();
//	}
//	
//	/**
//	 * buildResponse
//	 * @param c
//	 * @return
//	 */
//	protected <T> Response buildWsResponse(T c){
//		LOG.debug("BaseApi.buildWsResponse");
//		//build response
//		WsResponse<T> wsResponse = new WsResponse<T>();
//		wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
//		wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
//		wsResponse.setResult(c);
//		return Response.ok( wsResponse ).build();
//	}
//	
//	
//	/**
//	 * buildFailureResponse
//	 * @return
//	 */
//	protected <T> Response buildFailureResponse(){
//		LOG.debug("BaseApi.buildFailureResponse");
//		//build response
//		WsResponse<T> wsResponse = new WsResponse<T>();
//		wsResponse.setStatus(Constants.API_STATUS_FAILURE);
//		wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
//		return Response.ok( wsResponse ).build();
//	}
//	
//	
//	/**
//	 * buildFailureResponse
//	 * @param statusCode
//	 * @param errorMsg
//	 * @return
//	 */
//	protected <T> Response buildFailureResponse(String statusCode, String errorMsg){
//		LOG.debug("BaseApi.buildFailureResponse()");
//		//build response
//		WsResponse<T> wsResponse = new WsResponse<T>();
//		wsResponse.setStatus(statusCode);
//		wsResponse.getMessages().add(errorMsg);
//		return Response.ok( wsResponse ).build();
//	}
}
