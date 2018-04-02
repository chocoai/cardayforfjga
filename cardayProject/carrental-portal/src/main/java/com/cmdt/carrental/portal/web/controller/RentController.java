package com.cmdt.carrental.portal.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmdt.carrental.common.entity.Rent;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.service.RentService;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/rent")
public class RentController {
	
	private static final Logger LOG = LoggerFactory.getLogger(RentController.class);

    @Autowired
    private RentService rentService;
    
    
    /**
     * [超级管理员] 查询所有租户
     * @return
     */
    @RequiresPermissions("rent:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> showList(@CurrentUser User loginUser) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		List<Rent> list = null;
    		
    		 //超级管理员
	   		 if(loginUser.isSuperAdmin()){
	   			 list = rentService.findAll();
	   			 if(list != null && list.size() > 0){
	        		 map.put("data",list);
	        	 }
	   		 }
    		 map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("RentController.showList",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
}
