package com.cmdt.carrental.portal.web.controller;

import java.util.ArrayList;
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

import com.cmdt.carrental.common.entity.RuleHoliday;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.service.RuleService;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/holiday")
public class HolidayController {
	private static final Logger LOG = LoggerFactory.getLogger(HolidayController.class);
   
	@Autowired
	RuleService ruleService;
	
	/**
	 * 通过年份查询节假日
	 * @param year optional
	 * @return
	 */
    @RequestMapping(value = "/getHolidayListListByYear", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> getHolidayListListByYear(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		String year = "";
			String currentPage = "";
			String numPerPage = "";
    		
    		if(!StringUtils.isEmpty(json)){
    			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
        		year = String.valueOf(jsonMap.get("year"));
				currentPage = String.valueOf(jsonMap.get("currentPage"));
				numPerPage = String.valueOf(jsonMap.get("numPerPage"));
    		}
    		
    		if(StringUtils.isEmpty(year)){
    			String currentTime = DateUtils.getNowDate();
    			year = currentTime.split("-")[0]; //当前年份
    		}
    		
    		PagModel holidayList = null;
    		
    		holidayList = ruleService.getHolidayListListByYear(year,currentPage,numPerPage);
    		map.put("data", holidayList);
	    	map.put("status", "success");
    	}catch(Exception e){
    	   LOG.error("HolidayController[---getHolidayListListByYear---]", e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	 /**
	 * 创建节假日
	 * @param ruleHoliday
	 */
    @RequestMapping(value = "/createRuleHoliday", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> createRuleHoliday(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		RuleHoliday ruleHoliday = new RuleHoliday();
    		ruleHoliday.setHolidayYear(String.valueOf(jsonMap.get("holidayYear")));
    		ruleHoliday.setHolidayType(String.valueOf(jsonMap.get("holidayType")));
    		ruleHoliday.setHolidayTime(String.valueOf(jsonMap.get("holidayTime")));
    		ruleHoliday.setAdjustHolidayTime(String.valueOf(jsonMap.get("adjustHolidayTime")));
    		ruleService.createRuleHoliday(ruleHoliday);
	    	map.put("status", "success");
    	}catch(Exception e){
    	   LOG.error("HolidayController[---createRuleHoliday---]", e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	 /**
	 * 根据id查询节假日
	 * @param id
	 */
    @RequestMapping(value = "/findRuleHolidayById", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> findRuleHolidayById(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		Long id = Long.valueOf(String.valueOf(jsonMap.get("id")));
    		RuleHoliday ruleHoliday = ruleService.findRuleHolidayById(id);
    		map.put("data", ruleHoliday);
	    	map.put("status", "success");
    	}catch(Exception e){
    	   LOG.error("HolidayController[---findRuleHolidayById---]", e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	 /**
	 * 修改节假日
	 * @param ruleHoliday
	 */
    @RequestMapping(value = "/updateRuleHoliday", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> updateRuleHoliday(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		RuleHoliday ruleHoliday = new RuleHoliday();
    		ruleHoliday.setId(Long.valueOf(String.valueOf(jsonMap.get("id"))));
    		ruleHoliday.setHolidayYear(String.valueOf(jsonMap.get("holidayYear")));
    		ruleHoliday.setHolidayType(String.valueOf(jsonMap.get("holidayType")));
    		ruleHoliday.setHolidayTime(String.valueOf(jsonMap.get("holidayTime")));
    		ruleHoliday.setAdjustHolidayTime(String.valueOf(jsonMap.get("adjustHolidayTime")));
    		ruleService.updateRuleHoliday(ruleHoliday);
	    	map.put("status", "success");
    	}catch(Exception e){
    	   LOG.error("HolidayController[---updateRuleHoliday---]", e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	 /**
	 * 移除节假日
	 * @param id
	 */
    @RequestMapping(value = "/removeRuleHoliday", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> removeRuleHoliday(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    	    Long id = Long.valueOf(String.valueOf(jsonMap.get("id")));
    		ruleService.removeRuleHoliday(id);
	    	map.put("status", "success");
    	}catch(Exception e){
    	   LOG.error("HolidayController[---removeRuleHoliday---]", e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
	
}
