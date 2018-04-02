package com.cmdt.carrental.mobile.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.service.BusiOrderService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.model.BusiOrderDto;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;

@Service
public class CarHailingOrderAppService {
   
	@Autowired
	private BusiOrderService busiOrderService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * createOrder
	 * @param dto
	 * @return
	 */
	public String createOrder(BusiOrderDto dto){
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		//set order type as carhailing
		dto.setOrderType(1);
		String json=JsonUtils.object2Json(dto);
		return busiOrderService.createOrder(loginUser, json);
	}
	
	/**
	 * orderList
	 * @param dto
	 * @return
	 */
	public PagModel orderList(BusiOrderQueryDto dto){
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		String json=JsonUtils.object2Json(dto);
		return busiOrderService.orderList(loginUser, json);
	}
	
	/**
	 * cancelOrder
	 * @param dto
	 * @return
	 */
	public String cancelOrder(BusiOrderDto dto){
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		return busiOrderService.cancelOrder(loginUser, dto.getId(), dto.getComments());
	}
	
	
	/**
	 * pickupOrder
	 * @param dto
	 * @return
	 */
	public String pickupOrder(BusiOrderDto dto){
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		return busiOrderService.pickupOrder(loginUser, dto.getId());
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public String ignoreOrder(BusiOrderDto dto){
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		return busiOrderService.ignoreOrder(loginUser, dto.getId());
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public BusiOrder findAvOrder(BusiOrderQueryDto dto){
		Long loginUserId=dto.getLoginUserId();
		BusiOrder order=busiOrderService.findLatNearestOrder(loginUserId);
		return order;
	}
}
