package com.cmdt.carrental.portal.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.service.VehicleAnnualInspectionService;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/vehicleAnnualInspection")
public class VehicleAnnualInspectionController extends BaseController{
	private static final Logger LOG = LoggerFactory.getLogger(VehicleAnnualInspectionController.class);
	
	@Autowired
	private VehicleAnnualInspectionService annualInspectionService;
	@Autowired
	private UserService userService;
	
	@RequiresPermissions("annualInspection:list")
    @RequestMapping(value = "/listPage", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> list(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
	    	PagModel pagModel= new PagModel();
	    	
	    	Long entId = null;
	    	pagModel= annualInspectionService.listPage(entId, json);
	    	
	    	map.put("data", pagModel);
	    	map.put("status", "success");
    	}catch(Exception e){
    	   LOG.error("VehicleAnnualInspectionController.list error",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
	
	@RequiresPermissions("annualInspection:update")
    @RequestMapping(value = "/resetInsuranceTime", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> resetInsuranceTime(@CurrentUser User loginUser,String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Long id = TypeUtils.obj2Long(jsonMap.get("vehicleId"));
			boolean status = false;
			String insuranceDueTime = String.valueOf(jsonMap.get("insuranceDueTime"));
			// 企业管理员
			if (loginUser.isEntAdmin()) {
				status = annualInspectionService.resetInsuranceTime(id, insuranceDueTime);
			}
			if(status) {
				map.put("status", "success");
			} else {
				map.put("status", "failure");
			}
		} catch (Exception e) {
			LOG.error("VehicleAnnualInspectionController.resetInsuranceTime error",e);
			map.put("status", "failure");
		}
		return map;
	}
	
	@RequiresPermissions("annualInspection:update")
	@RequestMapping(value = "/resetInspectionTime", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> resetInspectionTime(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Long id = TypeUtils.obj2Long(jsonMap.get("id"));
			String inspectionNextTime = String.valueOf(jsonMap.get("inspectionNextTime"));
			boolean status = false;
			// 企业管理员
			if (loginUser.isEntAdmin()) {
				status = annualInspectionService.resetInspectionTime(id, inspectionNextTime);
			}
			if(status) {
				map.put("status", "success");
			} else {
				map.put("status", "failure");
			}
		} catch (Exception e) {
			LOG.error("VehicleAnnualInspectionController.resetInspectionTime error",e);
			map.put("status", "failure");
		}
		return map;
	}
}
