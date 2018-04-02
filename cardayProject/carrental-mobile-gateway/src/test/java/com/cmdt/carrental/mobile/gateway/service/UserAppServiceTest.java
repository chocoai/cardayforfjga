package com.cmdt.carrental.mobile.gateway.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.mobile.gateway.SuperTestCase;

public class UserAppServiceTest extends SuperTestCase{
	
	@Autowired
	private UserAppService appService;
	
	@Autowired
	private UserService userService;
	

	@Test
	public void findShortName(){
		User user = userService.findByUsername("ghyx_ent");
		System.out.println(user.getShortname());
		
		Assert.assertTrue(true);
	}
	

}
