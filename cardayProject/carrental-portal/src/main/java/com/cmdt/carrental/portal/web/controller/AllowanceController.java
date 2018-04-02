package com.cmdt.carrental.portal.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmdt.carrental.common.model.AllowanceModel;
import com.cmdt.carrental.common.service.AllowanceService;
import com.cmdt.carrental.common.util.JsonUtils;

@Controller
@RequestMapping("/allowance")
public class AllowanceController extends BaseController{
	
	private static final Logger LOG = LoggerFactory.getLogger(AllowanceController.class);
	
	@Autowired
    private AllowanceService allowanceService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> findAll(){
		Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		 List<AllowanceModel> list = allowanceService.findAll();
    		 map.put("status", "success");
		     map.put("data", list);
    	}catch(Exception e){
    		 LOG.error("AllowanceController.findAll",e);
    		 map.put("status", "failure");
    	}
    	return map;
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");

		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);

			String id = String.valueOf(jsonMap.get("id"));
			String allowanceName = String.valueOf(jsonMap.get("allowanceName"));
  			String allowanceValue = String.valueOf(jsonMap.get("allowanceValue"));
  			
  			if(StringUtils.isEmpty(id)){
  				map.put("status", "failure");
 				map.put("msg", "id不能为空");
 				return map;
  			}
  			
  			if(StringUtils.isEmpty(allowanceName)){
  				map.put("status", "failure");
 				map.put("msg", "补贴名不能为空");
 				return map;
  			}
  			
  			if(StringUtils.isEmpty(allowanceValue)){
  				map.put("status", "failure");
 				map.put("msg", "补贴值不能为空");
 				return map;
  			}
  			
  			AllowanceModel allowanceModel = new AllowanceModel();
  			allowanceModel.setId(Long.valueOf(id));
  			allowanceModel.setAllowanceName(allowanceName);
  			allowanceModel.setAllowanceValue(Double.valueOf(allowanceValue));
  			allowanceService.update(allowanceModel);
  			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("AllowanceController.update", e);
			map.put("status", "failure");
		}
		return map;

	}
}
