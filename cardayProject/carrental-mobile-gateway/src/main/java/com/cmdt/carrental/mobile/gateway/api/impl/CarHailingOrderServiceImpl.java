package com.cmdt.carrental.mobile.gateway.api.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.mobile.gateway.api.inf.ICarHailingOrderService;
import com.cmdt.carrental.mobile.gateway.common.Constants;
import com.cmdt.carrental.mobile.gateway.common.WsResponse;
import com.cmdt.carrental.common.model.BusiOrderDto;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;
import com.cmdt.carrental.mobile.gateway.model.request.user.ResetPasswordDto;
import com.cmdt.carrental.mobile.gateway.model.request.user.UserDto;
import com.cmdt.carrental.mobile.gateway.service.BusiOrderAppService;
import com.cmdt.carrental.mobile.gateway.service.CarHailingOrderAppService;
import com.cmdt.carrental.mobile.gateway.service.UserAppService;

public class CarHailingOrderServiceImpl implements ICarHailingOrderService{
	
	private static final Logger LOG = Logger.getLogger(CarHailingOrderServiceImpl.class);
	
	@Autowired
	private CarHailingOrderAppService orderAppService;
	@Autowired
	private BusiOrderAppService busiOrderAppService;
	@Autowired
	private UserAppService userAppService;
	
	@Override
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createOrder(BusiOrderDto dto) {
		LOG.debug("CarHailingOrderServiceImpl.createOrder["+dto+"]");
		WsResponse<BusiOrderDto> wsResponse = new WsResponse<BusiOrderDto>();
		try{
			String msg = orderAppService.createOrder(dto);
			if(StringUtils.isBlank(msg)){
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
				wsResponse.setResult(dto);
			}else{
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
				wsResponse.getMessages().add(msg);
			}
		}catch(Exception e){
			LOG.error("CarHailingOrderServiceImpl.createOrder error",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}

	
	@Override
	@POST
	@Path("/cancel")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cancelOrder(BusiOrderDto dto) {
		LOG.debug("CarHailingOrderServiceImpl.cancelOrder["+dto+"]");
		WsResponse<BusiOrderDto> wsResponse = new WsResponse<BusiOrderDto>();
		try{
			String msg = orderAppService.cancelOrder(dto);
			if(StringUtils.isBlank(msg)){
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
				wsResponse.setResult(dto);
			}else{
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
				wsResponse.getMessages().add(msg);
			}
		}catch(Exception e){
			LOG.error("CarHailingOrderServiceImpl.cancelOrder error",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}

	
	@Override
	@POST
	@Path("/pickupOrder")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response pickupOrders(BusiOrderDto dto) {
		LOG.debug("CarHailingOrderServiceImpl.pickupOrders["+dto+"]");
		WsResponse<BusiOrderDto> wsResponse = new WsResponse<BusiOrderDto>();
		try{
			String msg = orderAppService.pickupOrder(dto);
			if(StringUtils.isBlank(msg)){
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
				wsResponse.setResult(dto);
			}else{
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
				wsResponse.getMessages().add(msg);
			}
		}catch(Exception e){
			LOG.error("CarHailingOrderServiceImpl.pickupOrders error",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}

	
	@Override
	@POST
	@Path("/startTrip")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response orderStart(BusiOrderDto dto) {
		WsResponse<BusiOrderDto> wsResponse = new WsResponse<BusiOrderDto>();
		try{
			busiOrderAppService.orderOngoing(dto);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
		}catch(Exception e){
			LOG.error("CarHailingOrderServiceImpl.orderStart error",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}

	
	@Override
	@POST
	@Path("/finishTrip")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response orderFinish(BusiOrderDto dto) {
		WsResponse<BusiOrderDto> wsResponse = new WsResponse<BusiOrderDto>();
		try{
			busiOrderAppService.orderFinish(dto);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
		}catch(Exception e){
			LOG.error("CarHailingOrderServiceImpl.orderFinish error",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}


	@Override
	@POST
	@Path("/regClient")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response regClient(UserDto dto) {
		WsResponse<UserDto> wsResponse = new WsResponse<UserDto>();
		try{
			String msg = userAppService.regClient(dto);
			if(StringUtils.isBlank(msg)){
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
				wsResponse.setResult(dto);
			}else{
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
				wsResponse.getMessages().add(msg);
			}
			
		}catch(Exception e){
			LOG.error("CarHailingOrderServiceImpl.regClient error",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
	
	
	@Override
	@POST
	@Path("/authCode")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response requestAuthCodeForReg(ResetPasswordDto resetPasswordDto) {
		WsResponse<ResetPasswordDto> wsResponse = new WsResponse<ResetPasswordDto>();
		String pattern = "^[1][0-9]{10}$";
		if(resetPasswordDto.getPhoneNumber().matches(pattern)) {
			String phoneNumber = resetPasswordDto.getPhoneNumber();
			if(userAppService.getVerifyCode(phoneNumber)){
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			}else{
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
				wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
			}
		} else {
			wsResponse.setStatus(Constants.API_STATUS_PARAM_ILLEGAL);
			wsResponse.getMessages().add(Constants.API_MESSAGE_PARAM_ILLEGAL);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@Override
	@POST
	@Path("/checkAuthCode")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response checkAuthCodeForReg(ResetPasswordDto resetPasswordDto) {
		WsResponse<ResetPasswordDto> wsResponse = new WsResponse<ResetPasswordDto>();
		String pattern = "^[1][0-9]{10}$";
		if(resetPasswordDto.getPhoneNumber().matches(pattern)) {
			String msg = userAppService.checkVerifyCodeForReg(resetPasswordDto).get("status").toString();
			switch(msg) {
				case "success" :
					wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
					wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
					break;
				case "codeExpired" :
					wsResponse.setStatus(Constants.API_STATUS_FAILURE);
					wsResponse.getMessages().add("验证码已过期");
					break;
				case "codeError" :
					wsResponse.setStatus(Constants.API_STATUS_FAILURE);
					wsResponse.getMessages().add("验证码错误");
					break;
			}
		} else {
			wsResponse.setStatus(Constants.API_STATUS_PARAM_ILLEGAL);
			wsResponse.getMessages().add(Constants.API_MESSAGE_PARAM_ILLEGAL);
		}
		return Response.ok( wsResponse ).build();
	}
	
	
	@Override
	@POST
	@Path("/getAvOrder")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAvOrders(BusiOrderQueryDto dto) {
		WsResponse<BusiOrder> wsResponse = new WsResponse<BusiOrder>();
		try{
			BusiOrder order = orderAppService.findAvOrder(dto);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setResult(order);
		}catch(Exception e){
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}


	@Override
	@POST
	@Path("/ignoreOrder")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response ignoreOrder(BusiOrderDto dto) {
		WsResponse<String> wsResponse = new WsResponse<String>();
		try{
			String msg = orderAppService.ignoreOrder(dto);
			if("".equals(msg)){
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			}else{
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
				wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
				wsResponse.setResult(msg);
			}
		}catch(Exception e){
			LOG.error("ignore order failure!",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
}
