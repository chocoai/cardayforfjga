package com.cmdt.carrental.portal.web.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
public class IndexController {
	
	private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping("/")
    public String index(@CurrentUser User loginUser, Model model) {
    	LOG.info("Inside IndexController");
    	Subject subject = SecurityUtils.getSubject();
    	if(subject.isAuthenticated()){
    		LOG.info("User authenticated forward to index page.");
    		return "index";
    	}else{
    		LOG.info("User not authenticated forward to login page.");
    		return "login";
    	}
    }

}
