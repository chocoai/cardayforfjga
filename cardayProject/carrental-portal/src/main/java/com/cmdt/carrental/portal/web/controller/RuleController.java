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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmdt.carrental.common.entity.Employee;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.RuleAddress;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.RuleData;
import com.cmdt.carrental.common.model.RuleEditData;
import com.cmdt.carrental.common.model.UserBindingData;
import com.cmdt.carrental.common.model.VehicleRuleData;
import com.cmdt.carrental.common.model.VehicleRuleDisplayModel;
import com.cmdt.carrental.common.model.VehicleRuleGetOnAndOffDisplayModel;
import com.cmdt.carrental.common.model.VehicleRuleSQLModel;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.RuleService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/rule")
public class RuleController {
	private static final Logger LOG = LoggerFactory.getLogger(RuleController.class);
	
	@Autowired
	RuleService ruleService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private OrganizationService organizationService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	/**
	 * 根据位置名查询用车位置
	 * 
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	//@RequiresPermissions("rule:list")
	@RequestMapping(value = "/findLocationByLocationName",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			String locationName = "";
			String currentPage = "";
			String numPerPage = "";
			if (StringUtils.isNotEmpty(json)) {
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				locationName = String.valueOf(jsonMap.get("locationName"));
				currentPage = String.valueOf(jsonMap.get("currentPage"));
				numPerPage = String.valueOf(jsonMap.get("numPerPage"));
			}

			PagModel location = null;
			location = ruleService.findLocationByLocationName(locationName,loginUser.getOrganizationId(),currentPage,numPerPage);
			map.put("status", "success");
			map.put("data", location);
		} catch (Exception e) {
			LOG.error("Station Controller find station by stationName error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 新增用车位置
	 * 
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
//	@RequiresPermissions("rule:create")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> create(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			RuleAddress ruleAddress = new RuleAddress();
			ruleAddress.setLocationName(String.valueOf(jsonMap.get("locationName")));
			ruleAddress.setCity(String.valueOf(jsonMap.get("city")));
			ruleAddress.setPosition(String.valueOf(jsonMap.get("position")));
			ruleAddress.setLongitude(String.valueOf(jsonMap.get("longitude")));
			ruleAddress.setLatitude(String.valueOf(jsonMap.get("latitude")));
			ruleAddress.setRadius(String.valueOf(jsonMap.get("radius")));
			if(loginUser.getOrganizationId() != null)
				ruleAddress.setOrganizationId(loginUser.getOrganizationId());
			 
			ruleAddress = ruleService.createLocation(ruleAddress);

			if (ruleAddress != null) {
				map.put("data", ruleAddress);
				map.put("status", "success");
			} else {
				map.put("status", "failure");
			}
		} catch (Exception e) {
			LOG.error("RuleController.create",e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * 修改用车位置信息
	 * 
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	//@RequiresPermissions("rule:update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(@CurrentUser User loginUser,String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			RuleAddress ruleAddress = ruleService.findLocation(Long.valueOf(String.valueOf(jsonMap.get("id"))));
			if (ruleAddress != null) {
				ruleAddress.setId(Long.valueOf(jsonMap.get("id").toString()));
				ruleAddress.setLocationName(String.valueOf(jsonMap.get("locationName")));
				ruleAddress.setCity(String.valueOf(jsonMap.get("city")));
				ruleAddress.setPosition(String.valueOf(jsonMap.get("position")));
				ruleAddress.setLongitude(String.valueOf(jsonMap.get("longitude")));
				ruleAddress.setLatitude(String.valueOf(jsonMap.get("latitude")));
				ruleAddress.setRadius(String.valueOf(jsonMap.get("radius")));
				if(loginUser.getOrganizationId() != null)
					ruleAddress.setOrganizationId(loginUser.getOrganizationId());
			}
			RuleAddress updateLocation = ruleService.updateLocation(ruleAddress);
			map.put("data", updateLocation);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("RuleController.update",e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 删除用车位置
	 * 
	 * @param id
	 * @return
	 */
	//@RequiresPermissions("rule:delete")
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(@CurrentUser User loginUser, @PathVariable("id") Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			RuleAddress ruleAddress = ruleService.findLocation(id);
			ruleService.deleteLocation(ruleAddress);
			map.put("status", "success");
			return map;
		} catch (Exception e) {
			LOG.error("RuleController.delete",e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * 规则列表查询
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/ruleList", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> ruleList(@CurrentUser User loginUser) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		//企业管理员
    		if(loginUser.isEntAdmin()){
    			Long entId = loginUser.getOrganizationId();
    			List<VehicleRuleDisplayModel> ruleList = ruleService.getRuleListByOrgId(entId);
    			map.put("data", ruleList);
    		}
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("RuleController.ruleList",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 规则移除
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/removeRule", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> removeRule(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		
    		//企业管理员
    		if(loginUser.isEntAdmin()){
    			Long ruleId = Long.valueOf(String.valueOf(jsonMap.get("ruleId")));
    			ruleService.removeRule(ruleId);
    		}
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("RuleController.removeRule",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 规则新增
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/addRule", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> addRule(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		RuleData ruleData = MAPPER.readValue(json,RuleData.class);
    		//企业管理员
    		if(loginUser.isEntAdmin()){
    			Long entId = loginUser.getOrganizationId();
    			ruleService.addRule(ruleData,entId);
    			map.put("status", "success");
    		}
    	}catch(Exception e){
    		LOG.error("RuleController.addRule",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 根据id查询rule
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/findRuleById", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> findRuleById(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		//企业管理员
    		if(loginUser.isEntAdmin()){
    			Long ruleId = Long.valueOf(String.valueOf(jsonMap.get("ruleId")));
    			Long entId = loginUser.getOrganizationId();
    			RuleEditData ruleEditData = ruleService.findRuleById(ruleId,entId);
    			map.put("data", ruleEditData);
    			map.put("status", "success");
    		}
    	}catch(Exception e){
    		LOG.error("RuleController.findRuleById",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * update rule
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/updateRule", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> updateRule(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		
    		//企业管理员
    		if(loginUser.isEntAdmin()){
    			RuleData ruleData = MAPPER.readValue(json,RuleData.class);
				ruleService.updateRule(ruleData);
				map.put("status", "success");
    		}
    	}catch(Exception e){
    		LOG.error("RuleController.updateRule",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
    
    
	/**
	 * 上车位置查询
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/getOnAddressListForAdd", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> getOnAddressListForAdd(@CurrentUser User loginUser) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	List<VehicleRuleGetOnAndOffDisplayModel> retList = new ArrayList<VehicleRuleGetOnAndOffDisplayModel>();
    	map.put("data", "");
    	try{
    		//企业管理员
    		if(loginUser.isEntAdmin()){
    			Long entId = loginUser.getOrganizationId();
    			List<VehicleRuleSQLModel> ruleList = ruleService.getVehicleRuleGetOnAndOffAddressByOrgId(entId);
    			if(ruleList != null && ruleList.size() > 0){
    				for(int i = 0 ; i < ruleList.size() ; i ++){
    					VehicleRuleSQLModel vehicleRuleSQLModel = ruleList.get(i);
    					VehicleRuleGetOnAndOffDisplayModel vehicleRuleGetOnAndOffDisplayModel = new VehicleRuleGetOnAndOffDisplayModel();
    					vehicleRuleGetOnAndOffDisplayModel.setName("getOnCheck");
    					vehicleRuleGetOnAndOffDisplayModel.setInputValue(vehicleRuleSQLModel.getAddress_id());
    					vehicleRuleGetOnAndOffDisplayModel.setBoxLabel(vehicleRuleSQLModel.getAddress_name()+"("+vehicleRuleSQLModel.getAddress_radius()+"km)");
    					if(i == 0){
    						vehicleRuleGetOnAndOffDisplayModel.setChecked(true);
    					}else {
    						vehicleRuleGetOnAndOffDisplayModel.setChecked(false);
						}
    					retList.add(vehicleRuleGetOnAndOffDisplayModel);
    				}
    			}
    		}
    		map.put("data", retList);
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("RuleController.getOnAddressListForAdd",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 下车位置查询
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/getOffAddressListForAdd", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> getOffAddressListForAdd(@CurrentUser User loginUser) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	List<VehicleRuleGetOnAndOffDisplayModel> retList = new ArrayList<VehicleRuleGetOnAndOffDisplayModel>();
    	map.put("data", "");
    	try{
    		//企业管理员
    		if(loginUser.isEntAdmin()){
    			Long entId = loginUser.getOrganizationId();
    			List<VehicleRuleSQLModel> ruleList = ruleService.getVehicleRuleGetOnAndOffAddressByOrgId(entId);
    			if(ruleList != null && ruleList.size() > 0){
    				for(int i = 0 ; i < ruleList.size() ; i ++){
    					VehicleRuleSQLModel vehicleRuleSQLModel = ruleList.get(i);
    					VehicleRuleGetOnAndOffDisplayModel vehicleRuleGetOnAndOffDisplayModel = new VehicleRuleGetOnAndOffDisplayModel();
    					vehicleRuleGetOnAndOffDisplayModel.setName("getOffCheck");
    					vehicleRuleGetOnAndOffDisplayModel.setInputValue(vehicleRuleSQLModel.getAddress_id());
    					vehicleRuleGetOnAndOffDisplayModel.setBoxLabel(vehicleRuleSQLModel.getAddress_name()+"("+vehicleRuleSQLModel.getAddress_radius()+"km)");
    					if(i == 0){
    						vehicleRuleGetOnAndOffDisplayModel.setChecked(true);
    					}else {
    						vehicleRuleGetOnAndOffDisplayModel.setChecked(false);
						}
    					retList.add(vehicleRuleGetOnAndOffDisplayModel);
    				}
    			}
    		}
    		map.put("data", retList);
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("RuleController.getOffAddressListForAdd",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 上车位置查询(merged,显示所有位置,已选中的上车位置已标注)
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/getOnAddressListForEdit", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> getOnAddressListForEdit(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	List<VehicleRuleGetOnAndOffDisplayModel> retList = new ArrayList<VehicleRuleGetOnAndOffDisplayModel>();
    	map.put("data", "");
    	try{
    		
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		Long ruleId = Long.valueOf(String.valueOf(jsonMap.get("ruleId")));
    		
    		//企业管理员
    		if(loginUser.isEntAdmin()){
    			Long entId = loginUser.getOrganizationId();
    			retList = ruleService.getMergedVehicleRuleAddressByOrgIdAndRuleId(entId, ruleId, 0);
    		}
    		map.put("data", retList);
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("RuleController.getOnAddressListForEdit",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 下车位置查询(merged,显示所有位置,已选中的下车位置已标注)
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/getOffAddressListForEdit", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> getOffAddressListForEdit(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	List<VehicleRuleGetOnAndOffDisplayModel> retList = new ArrayList<VehicleRuleGetOnAndOffDisplayModel>();
    	map.put("data", "");
    	try{
    		
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		Long ruleId = Long.valueOf(String.valueOf(jsonMap.get("ruleId")));
    		
    		//企业管理员
    		if(loginUser.isEntAdmin()){
    			Long entId = loginUser.getOrganizationId();
    			retList = ruleService.getMergedVehicleRuleAddressByOrgIdAndRuleId(entId, ruleId, 1);
    		}
    		map.put("data", retList);
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("RuleController.getOffAddressListForEdit",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
    
	/**
	 * 规则列表查询(用户已绑定规则)
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/ruleBindingList", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> ruleBindingList(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		Long userId = Long.valueOf(String.valueOf(jsonMap.get("userId")));
    		
    		List<VehicleRuleData> vehicleRuleDataList = ruleService.findRuleListByUserId(userId);
    		if(vehicleRuleDataList != null && vehicleRuleDataList.size() > 0){
    			List<Long> ids = new ArrayList<Long>();
    			for(VehicleRuleData vehicleRuleData : vehicleRuleDataList){
    				ids.add(vehicleRuleData.getRuleId());
    			}
    			
    			if(ids != null && ids.size() > 0){
    				List<VehicleRuleDisplayModel> retList = ruleService.getRuleListByIds(ids);
    				map.put("data", retList);
    			}
    		}
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("RuleController.ruleBindingList",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 规则列表查询(用户未绑定规则)
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/ruleNotBindingList", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> ruleNotBindingList(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		Long userId = Long.valueOf(String.valueOf(jsonMap.get("userId")));
    		
    		Map<Long,Integer> bindingRuleIds = new HashMap<Long,Integer>();//已绑定规则池
    		List<VehicleRuleData> vehicleRuleDataList = ruleService.findRuleListByUserId(userId);//用户已经绑定
    		if(vehicleRuleDataList != null && vehicleRuleDataList.size() > 0){
    			for(VehicleRuleData vehicleRuleData : vehicleRuleDataList){
    				bindingRuleIds.put(vehicleRuleData.getRuleId(), 1);
    			}
    		}
    		
    		Long orgId = 0l;
    		
    		//查询改员工对应的企业Id
    		User queryUser = userService.findById(userId);
    		
    		if(queryUser.isEntAdmin()){//企业管理员
    			orgId = queryUser.getOrganizationId();
    		}else{
    			Organization org_orderUser=organizationService.findOne(queryUser.getOrganizationId());//找到部门管理员对应的企业id
    			if(org_orderUser.getParentId() == 0){//员工未分配，还在企业节点
    				orgId = queryUser.getOrganizationId();
    			}else{//在部门一级，查询部门的企业id
    				orgId = org_orderUser.getParentId();
    			}
    		}
    		
    		List<VehicleRuleDisplayModel> filterList = new ArrayList<VehicleRuleDisplayModel>();//过滤后的规则
    		
    		List<VehicleRuleDisplayModel> ruleList = ruleService.getRuleListByOrgIdAndParents(orgId);//该企业下所有的规则
    		if(ruleList != null && ruleList.size() > 0){
    			
    			if(bindingRuleIds.size() > 0){//需要过滤
    				for(VehicleRuleDisplayModel displayModel : ruleList){
    					if(bindingRuleIds.get(displayModel.getRuleId()) == null){
    						filterList.add(displayModel);
    					}
    				}
    				map.put("data", filterList);
    			}else{//不需要过滤
    				map.put("data", ruleList);
    			}
    		}
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("RuleController.ruleNotBindingList",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 用户解绑规则
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/removeBindingRule", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> removeBindingRule(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
    		Long userId = Long.valueOf(String.valueOf(jsonMap.get("userId")));
    		Long ruleId = Long.valueOf(String.valueOf(jsonMap.get("ruleId")));
    		ruleService.removeBindingRule(userId,ruleId);
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("RuleController.removeBindingRule",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * 用户绑定规则
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/userBindingRule", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> userBindingRule(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		UserBindingData userBindingData = MAPPER.readValue(json,UserBindingData.class);
    		ruleService.userBindingRule(userBindingData);
    	    map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("RuleController.userBindingRule",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
	/**
	 * balance check
	 * @param loginUser
	 * @param json
	 * @return
	 */
    @RequestMapping(value = "/balanceCheck", method = RequestMethod.POST)
    @ResponseBody
    @SuppressWarnings("unchecked")
    public Map<String,Object> balanceCheck(@CurrentUser User loginUser) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Employee employee = userService.findEmployeeByUserId(loginUser.getId());
    		String msg = "";
    		if(employee != null){
    			double MonthLimitvalue = employee.getMonthLimitvalue().doubleValue(); 
    			if(!(MonthLimitvalue == -1 || MonthLimitvalue == -1.0)){// 月累计限制额度(-1:不限额度)
    				Double monthLimitLeft = employee.getMonthLimitLeft();
    				if(monthLimitLeft != null){
    					double monthLimitLeftVal = monthLimitLeft.doubleValue();
    					if(monthLimitLeftVal < 0){
    						msg = "用车额度小于0";
    					}
    				}
    			}
    		}
    		map.put("data", msg);
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("RuleController.balanceCheck",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
        
}
