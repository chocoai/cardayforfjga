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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.OrgListModel;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/organization")
public class OrganizationController {
	
	private static final Logger LOG = LoggerFactory.getLogger(OrganizationController.class);

    @Autowired
    private OrganizationService organizationService;
    
    /**
     * [超级管理员]查询所有企业
     * [租户管理员]查询租户下面的企业
     * @return
     */
    @RequiresPermissions("organizationaudit:list")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> showList(@CurrentUser User loginUser) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		List<Organization> list = new ArrayList<Organization>();
    		
    		 //超级管理员
	   		 if(loginUser.isSuperAdmin()){
	   			 list = organizationService.findEntList();
	   		 }
    		
//	   		租户管理员不存在,删除
//    		//租户管理员
//    		if(loginUser.isRentAdmin()){
//    			 list = organizationService.findOrganizationByRentId(loginUser.getOrganizationId());
//    		}
    		
	   		if(list != null && list.size() > 0){
       		   map.put("data",list);
       	    }
    		map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController.showList",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * [超级管理员]查询所有待审核企业
     * [租户管理员]查询租户下面所有待审核企业
     * @return
     */
    @RequiresPermissions("organizationaudit:list")
    @RequestMapping(value = "/audit/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> showAuditList(@CurrentUser User loginUser) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		List<Organization> list = new ArrayList<Organization>();
    		
    		 //超级管理员
	   		 if(loginUser.isSuperAdmin()){
	   			 list = organizationService.findAuditAll();
	   		 }
//    		租户管理员不存在,删除
//    		//租户管理员
//    		if(loginUser.isRentAdmin()){
//    			 list = organizationService.findOrganizationAuditByRentId(loginUser.getOrganizationId());
//    		}
	   		 
	   		 if(list != null && list.size() > 0){
       		   map.put("data",list);
       	     }
    		 map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController.showAuditList",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * [租户管理员] 审核企业
     * @param id,status,comments(可选)
     * @return
     */
   /* 
    * 租户管理员不存在,删除
    * 
    * @RequiresPermissions("organizationaudit:check")
    @RequestMapping(value = "/audit/check", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> checkOrganization(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		 //租户管理员
	   		 if(loginUser.isRentAdmin()){
	   			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				Organization organization = organizationService.findById(Long.valueOf(String.valueOf(jsonMap.get("id"))));
				if(organization != null){
					String status = String.valueOf(jsonMap.get("status"));
					organization.setStatus(status);
					organization.setComments("");
					//审核不通过
					if("3".equals(status)){
						if(jsonMap.get("comments") != null){
							organization.setComments(String.valueOf(jsonMap.get("comments")));
						}
					}
			        organizationService.checkOrganization(organization);
				}
		        map.put("status", "success");
	   		 }else{
	   			map.put("status", "failure"); 
	   		 }
    	}catch(Exception e){
    		LOG.error("OrganizationController.checkOrganization",e);
   		 map.put("status", "failure");
    	}
       return map;
    }*/
    
    
    /**
     * [租户管理员] 激活企业服务
     * @param id,status,startTime,endTime(可选)
     * @return
     */
    /*
     * 租户管理员不存在,删除
     * 
     * @RequiresPermissions("organization:activate")
    @RequestMapping(value = "/service/activate", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> activateOrganizationService(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		 //租户管理员
	   		 if(loginUser.isRentAdmin()){
	   			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				Organization organization = organizationService.findById(Long.valueOf(String.valueOf(jsonMap.get("id"))));
				if(organization != null){
					if(jsonMap.get("startTime") != null && jsonMap.get("endTime") != null){
						organization.setStartTime(TimeUtils.getDaytime(String.valueOf(jsonMap.get("startTime"))));
						organization.setEndTime(TimeUtils.getDaytime(String.valueOf(jsonMap.get("endTime"))));
					}
					String status = String.valueOf(jsonMap.get("status"));
					organization.setStatus(status);
					
			        organizationService.activateOrganizationService(organization);
				}
		        map.put("status", "success");
	   		 }else{
	   			map.put("status", "failure"); 
	   		 }
    	}catch(Exception e){
    		LOG.error("OrganizationController.activateOrganizationService",e);
   		 map.put("status", "failure");
    	}
       return map;
    }*/
    
    
    /**
     * [租户管理员] 中止企业服务
     * @param id,status
     * @return
     */
    /*
     * 租户管理员不存在,删除
     * 
     * @RequiresPermissions("organization:terminate")
    @RequestMapping(value = "/service/terminate", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> terminateOrganizationService(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		 //租户管理员
	   		 if(loginUser.isRentAdmin()){
	   			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				Organization organization = organizationService.findById(Long.valueOf(String.valueOf(jsonMap.get("id"))));
				if(organization != null){
					String status = String.valueOf(jsonMap.get("status"));
					organization.setStatus(status);
					
			        organizationService.updateOrganizationStatus(organization);
				}
		        map.put("status", "success");
	   		 }else{
	   			map.put("status", "failure"); 
	   		 }
    	}catch(Exception e){
    		LOG.error("OrganizationController.terminateOrganizationService",e);
   		 map.put("status", "failure");
    	}
       return map;
    }*/
    
    
    /**
     * [超级管理员,租户管理员] 查询待审核企业
     * @param name
     * @return
     */
    @RequiresPermissions("organizationaudit:view")
    @RequestMapping(value = "/listAuditByOrgName", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listAuditOrgByName(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		List<Organization> list = new ArrayList<Organization>();
    		
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
			//超级管理员
			if(loginUser.isSuperAdmin()){
	   			 list = organizationService.findAuditByOrganizationName(String.valueOf(jsonMap.get("name")));
	   		 }
//			租户管理员不存在,删除
//			//租户管理员
//			if(loginUser.isRentAdmin()){
//				list = organizationService.findAuditByOrganizationNameAndRentId(String.valueOf(jsonMap.get("name")),loginUser.getOrganizationId());
//			}
			
			if(list != null && list.size() > 0){
       		 map.put("data",list);
       	    }
    		 map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController.listAuditOrgByName",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    
    /**
     * [超级管理员,租户管理员] 查询根据机构名查询所有机构
     * @param name
     * @return
     */
    @RequiresPermissions("organization:view")
    @RequestMapping(value = "/listByOrgName", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listOrgByName(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		List<Organization> list = new ArrayList<Organization>();
    		
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
			//超级管理员
			if(loginUser.isSuperAdmin()){
	   			 list = organizationService.findByOrganizationName(String.valueOf(jsonMap.get("name")));
	   		 }
//			租户管理员不存在,删除
//			//租户管理员
//			if(loginUser.isRentAdmin()){
//				list = organizationService.findByOrganizationNameAndRentId(String.valueOf(jsonMap.get("name")),loginUser.getOrganizationId());
//			}
//			
			if(list != null && list.size() > 0){
       		 map.put("data",list);
       	    }
    		 map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController.listOrgByName",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * [超级管理员,租户管理员] 查询根据id查询企业
     * @param id
     * @return
     */
    @RequiresPermissions(value="organization:view")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> findById(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Organization organization = null;
    		
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		
			if(loginUser.isSuperAdmin() || loginUser.isRentAdmin() || loginUser.isAdmin()){
				organization = organizationService.findById(Long.valueOf(String.valueOf(jsonMap.get("id"))));
	   			 if(organization != null){
	        		 map.put("data",organization);
	        	 }
	   		 }
    		 map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController.findById",e);
    		 map.put("status", "failure");
    	}
        return map;
    }


    /**
     * [租户管理员]租户管理员创建企业
     * @return
     */
   /*
    * 租户管理员不存在,删除
    *  
    *  @RequiresPermissions("organization:create")
    @RequestMapping(value = "/appendChild", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> create(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
	    	Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Organization organization = new Organization();
			
			if(jsonMap.get("parentId") == null){
				organization.setParentId(Long.valueOf(0));
			}else{
				organization.setParentId(Long.valueOf(String.valueOf(jsonMap.get("parentId"))));
			}
			
			if(jsonMap.get("parentIds") == null){
				organization.setParentIds("0");
			}else{
				organization.setParentIds(String.valueOf(jsonMap.get("parentIds")));
			}
			
			//租户管理员
	   		if(loginUser.isRentAdmin()){
	   			organization.setName(String.valueOf(jsonMap.get("name")));
	   			organization.setStatus("0");//需要审核
	   			organization.setShortname(String.valueOf(jsonMap.get("shortname")));
				organization.setLinkman(String.valueOf(jsonMap.get("linkman")));
				organization.setLinkmanPhone(String.valueOf(jsonMap.get("linkmanPhone")));
				organization.setLinkmanEmail(String.valueOf(jsonMap.get("linkmanEmail")));
				organization.setVehileNum(Long.valueOf(String.valueOf(jsonMap.get("vehileNum"))));
				organization.setCity(String.valueOf(jsonMap.get("city")));
				organization.setStartTime(TimeUtils.getDaytime(String.valueOf(jsonMap.get("startTime"))));
				organization.setEndTime(TimeUtils.getDaytime(String.valueOf(jsonMap.get("endTime"))));
				organization.setAddress(String.valueOf(jsonMap.get("address")));
				organization.setIntroduction(String.valueOf(jsonMap.get("introduction")));
				organizationService.createOrganization(organization,loginUser.getOrganizationId());
	   		}
	        
	        map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController.create",e);
   		 map.put("status", "failure");
    	}
       return map;
    }*/
    

    /**
     * [租户管理员] 修改企业
     * @param id
     * @return
     */
   /* 
    * 租户管理员不存在,删除
    * 
    * @RequiresPermissions("organization:update")
    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> update(String json,@CurrentUser User loginUser) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		if(loginUser.isRentAdmin() || loginUser.isAdmin()){
    			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    			
    			//租户管理员
    	   		if(loginUser.isRentAdmin()){
    	   			Organization organization = organizationService.findById(Long.valueOf(String.valueOf(jsonMap.get("id"))));
    	   			if(organization != null){
    	   				organization.setName(String.valueOf(jsonMap.get("name")));
        	   			organization.setShortname(String.valueOf(jsonMap.get("shortname")));
        				organization.setLinkman(String.valueOf(jsonMap.get("linkman")));
        				organization.setLinkmanPhone(String.valueOf(jsonMap.get("linkmanPhone")));
        				organization.setLinkmanEmail(String.valueOf(jsonMap.get("linkmanEmail")));
        				organization.setVehileNum(Long.valueOf(String.valueOf(jsonMap.get("vehileNum"))));
        				organization.setCity(String.valueOf(jsonMap.get("city")));
        				organization.setStartTime(TimeUtils.getDaytime(String.valueOf(jsonMap.get("startTime"))));
        				organization.setEndTime(TimeUtils.getDaytime(String.valueOf(jsonMap.get("endTime"))));
        				organization.setAddress(String.valueOf(jsonMap.get("address")));
        				organization.setIntroduction(String.valueOf(jsonMap.get("introduction")));
    	   			}
    	   			if(organization != null){
        	   			organizationService.updateOrganization(organization);
        	   		}
    	   		}
    		}
	        map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController.update",e);
   		 map.put("status", "failure");
    	}
       return map;
    }*/

    /**
     * [租户管理员] 删除企业
     * @param id
     * @return
     */
    /*
     * 租户管理员不存在,删除
     * 
     * @RequiresPermissions("organization:delete")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> delete(@PathVariable("id") Long id,@CurrentUser User loginUser) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		if(loginUser.isRentAdmin()){
    			organizationService.deleteOrganization(id);
    		}
    		map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController.delete",e);
   		 	map.put("status", "failure");
    	}
       return map;
    }*/
    
    /**
     * 所属企业或部门下拉框list
     * [租户管理员]查询所属企业
     * [企业管理员]查询所属部门
     * @param loginUser
     * @param json
     * @return
     */
    @RequestMapping(value = "/findLowerLevelOrgList", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> findLowerLevelOrgList(@CurrentUser User loginUser) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		
    		List<OrgListModel> list = null;
//    		租户管理员不存在,删除
//	    	//租户管理员
//	    	if(loginUser.isRentAdmin()){
//	    		Long rentId = loginUser.getOrganizationId();
//	    		list = organizationService.findLowerLevelOrgByRentAdmin(rentId);
//	    		map.put("data", list);
//	    	}
	    	
	    	//企业管理员
	    	if(loginUser.isEntAdmin()){
	    		Long entId = loginUser.getOrganizationId();
	    		list = organizationService.findfindLowerLevelOrgByEntAdmin(entId);
	    		map.put("data", list);
	    	}
	    	
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController.findLowerLevelOrgList",e);
   		   map.put("status", "failure");
   	   }
       return map;
    }
    
    /**
     * [租户管理员]查询租户下面所有已经审核过的企业
     * @return
     */
    @RequestMapping(value = "/audited/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> showAuditedList(@CurrentUser User loginUser) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		List<Organization> list = new ArrayList<Organization>();
    		
    		//租户管理员
    		if(loginUser.isSuperAdmin()){
    			list = organizationService.findOrganizationAuditedByAdminId(loginUser.getOrganizationId());
    		}
	   		 
	   		 if(list != null && list.size() > 0){
       		   map.put("data",list);
       	     }
    		 map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController.showAuditedList",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * [admin]查询admin审核通过的企业  
     * 包括以下状态的企业： 2:待服务开通 3：服务中 4:服务到期 5： 服务暂停
     * @return
     */
    @RequestMapping(value = "/auditedByAdmin/list", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> showAuditedByAdminList(@CurrentUser User loginUser) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		List<Organization> list = new ArrayList<Organization>();
    		
    		//admin
    		if(loginUser.isSuperAdmin()){
    			 list = organizationService.findOrganizationAuditedByAdminId(loginUser.getOrganizationId());
    		}
	   		 
	   		 if(list != null && list.size() > 0){
       		   map.put("data",list);
       	     }
    		 map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController.showAuditedByAdminList",e);
    		 map.put("status", "failure");
    	}
        return map;
    }

}
