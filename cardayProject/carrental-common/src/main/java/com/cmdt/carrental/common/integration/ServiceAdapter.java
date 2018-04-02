package com.cmdt.carrental.common.integration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TypeUtils;

/**
 * 
 * @author ZhaoBin
 *
 */
public class ServiceAdapter {

	private static final Logger LOG = LoggerFactory.getLogger(ServiceAdapter.class);
	
	public ShouqiService shouqiService = null;
	private CarDayService carDayService;
	
	public ServiceAdapter(){
		
	}
	
	public ServiceAdapter(Object clazz){
		if(clazz instanceof ShouqiService){
			this.shouqiService = (ShouqiService)clazz;
		}else if(clazz instanceof CarDayService){
			this.carDayService = (CarDayService)clazz;
		}
	}
	
	public Map<String, Object> doService(ActionName actionName, Object[] params){
		Map<String, Object> map = new HashMap<>();
		Object data = null;
		if(shouqiService != null){
			data = shouqiService.action(actionName,params);
		}else{
			map.put("status", "failure");
			return map;
		}
		map.put("status", "success");
		map.put("data", data);
		LOG.info("ServiceAdaper doService success.");
		return map;
	}
	
	public String doCarDayService(ActionName action,Object[] params){
		return (String)carDayService.action(action, params);
	}
}
