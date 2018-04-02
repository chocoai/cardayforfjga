package com.cmdt.carrental.platform.service.biz.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.entity.Rent;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.service.RentService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.exception.ServerException;

@Service
public class PlatformRentService {

	private static final Logger LOG = LoggerFactory.getLogger(PlatformRentService.class);
	
	@Autowired
    private RentService rentService;
	
	public List<Rent> showRentList(User loginUser){
		try{
			 //超级管理员
	  		 if(loginUser.isSuperAdmin()){
	  			return  rentService.findAll();
	       	 }
			return null;
		}catch(Exception e){
			LOG.error("PlatformRentService showRentList error, cause by: ", e);
        	throw new ServerException(Constants.API_MESSAGE_FAILURE); 
		}
	}
	
}
