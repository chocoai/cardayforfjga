package com.cmdt.carrental.mobile.gateway.api.inf;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cmdt.carrental.common.model.BusiOrderDto;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;
import com.cmdt.carrental.mobile.gateway.model.request.user.ResetPasswordDto;
import com.cmdt.carrental.mobile.gateway.model.request.user.UserDto;

@Produces(MediaType.APPLICATION_JSON)
public interface ICarHailingOrderService {
	
	//------------------------------------------------
	// Order API
	//------------------------------------------------
	//create order by passenger
	public Response createOrder(@Valid @NotNull BusiOrderDto dto);
	//cancel order by passenger and driver
	public Response cancelOrder(@Valid @NotNull BusiOrderDto dto);
	//pickup order
	public Response pickupOrders(@Valid @NotNull BusiOrderDto dto);
	//order trip
	public Response orderStart(@Valid @NotNull BusiOrderDto dto);
	public Response orderFinish(@Valid @NotNull BusiOrderDto dto);
	public Response getAvOrders(@Valid @NotNull BusiOrderQueryDto dto);
	public Response ignoreOrder(@Valid @NotNull BusiOrderDto dto);
	
	//------------------------------------------------
	// User API
	//------------------------------------------------
	public Response regClient(@Valid @NotNull UserDto dto);
	public Response requestAuthCodeForReg(ResetPasswordDto resetPasswordDto);
	public Response checkAuthCodeForReg(ResetPasswordDto resetPasswordDto);
	
}
