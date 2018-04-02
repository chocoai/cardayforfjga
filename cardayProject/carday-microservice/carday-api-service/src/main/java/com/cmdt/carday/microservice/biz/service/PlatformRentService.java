package com.cmdt.carday.microservice.biz.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.entity.Rent;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carday.microservice.common.model.response.MessageCode;
import com.cmdt.carday.microservice.common.model.response.exception.ServiceException;
import com.cmdt.carrental.common.service.RentService;

@Service
public class PlatformRentService {

	private static final Logger LOG = LoggerFactory.getLogger(PlatformRentService.class);
	
	@Autowired
    private RentService rentService;
	
	public List<Rent> showRentList(User loginUser){
		 //超级管理员
  		 if(loginUser.isSuperAdmin()){
  			return  rentService.findAll();
       	 }else{
       		 throw new ServiceException(MessageCode.COMMON_NO_AUTHORIZED);
       	 }
	}
	
}
