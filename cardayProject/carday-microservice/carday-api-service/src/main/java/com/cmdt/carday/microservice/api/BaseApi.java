package com.cmdt.carday.microservice.api;

import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.service.BusiOrderService;
import com.cmdt.carrental.common.service.MessageService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseApi {
	private static final Logger LOG = LoggerFactory.getLogger(BaseApi.class);

	private static final ThreadLocal<Long> loginUser = new ThreadLocal<>();

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

	protected User getUserById(){
		return userService.findById(loginUser.get());
	}
	
	public static void setLoginUser(Long userId){
		loginUser.set(userId);
	}

	public static void removeLoginUser(){
		loginUser.remove();
	}
	
}
