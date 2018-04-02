package com.cmdt.carrental.portal.web.controller;

import java.util.ArrayList;
import java.util.Date;
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

import com.cmdt.carrental.common.bean.AuditStatus;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.AuditInfoModel;
import com.cmdt.carrental.common.model.CreditHistoryDto;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.RecharageCreditDto;
import com.cmdt.carrental.common.model.RelatedOrganizationInfo;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;


@Controller
@RequestMapping("/organization")
public class OrganizationController {
	private static final Logger LOG = LoggerFactory.getLogger(OrganizationController.class);

    @Autowired
    private OrganizationService organizationService;
    
    /**
     * [系统运营管理员]查询用车企业
     * [业务运营管理员]查询租车公司
     * @return
     */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("organizationaudit:list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> showList(@CurrentUser User loginUser,String json) {
		LOG.info("Inside OrganizationController.showList");
    	Map<String,Object> map = new HashMap<String,Object>();
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		PagModel pagModel=organizationService.findEntList(jsonMap);
    		
       		map.put("data",pagModel);
    		map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController[---showList---]", e);
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
    		
    		if(loginUser.isSuperAdmin()){
    			 list = organizationService.findOrganizationAuditedByAdminId(loginUser.getOrganizationId());
    		}
	   		 
	   		 if(list != null && list.size() > 0){
       		   map.put("data",list);
       	     }
    		 map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController[---showAuditedList---]", e);
    		map.put("status", "failure");
    	}
        return map;
    }
    
	/**
     * [系统运营管理员,业务运营管理员] 根据id查询企业信息
     * @param id
     * @return
     */
//    @SuppressWarnings("unchecked")
//	@RequiresPermissions(value="organization:view")
//    @RequestMapping(value = "/findById", method = RequestMethod.GET)
//    @ResponseBody
//    public Map<String,Object> findById(@CurrentUser User loginUser,String json) {
//    	LOG.info("Inside OrganizationController.findById");
//    	Map<String,Object> map = new HashMap<String,Object>();
//    	map.put("data", "");
//    	try{
//    		Map<String, Object> jsonMap = new HashMap<String, Object>();
//			jsonMap = JsonUtils.json2Object(json, Map.class);
//			Organization organization = organizationService.findById(Long.valueOf(String.valueOf(jsonMap.get("id"))));
//			map.put("data", organization);
//    		map.put("status", "success");
//    	}catch(Exception e){
//    		LOG.error("OrganizationController[---findById---]", e);
//    		map.put("status", "failure");
//    	}
//        return map;
//    }
    
    /**
     * [系统运营管理员,业务运营管理员] 增加企业信息
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/addOrganization", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addOrganization(@CurrentUser User loginUser,String json) {
    	LOG.info("Inside OrganizationController.addOrganization");
    	Map<String,Object> map = new HashMap<String,Object>();
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
   			Organization organization = new Organization();
			organization.setName(TypeUtils.obj2String(jsonMap.get("name")));
   			organization.setShortname(TypeUtils.obj2String(jsonMap.get("shortname")));
   			organization.setAddress(TypeUtils.obj2String(jsonMap.get("address")));
   			organization.setIntroduction(TypeUtils.obj2String(jsonMap.get("introduction")));
			organization.setLinkman(TypeUtils.obj2String(jsonMap.get("linkman")));
			organization.setLinkmanPhone(TypeUtils.obj2String(jsonMap.get("linkmanPhone")));
			organization.setLinkmanEmail(TypeUtils.obj2String(jsonMap.get("linkmanEmail")));
			organization.setStartTime(TypeUtils.obj2DateFormat(jsonMap.get("startTime")));
			organization.setEndTime(TypeUtils.obj2DateFormat(jsonMap.get("endTime")));
			organization.setEnterprisesType(TypeUtils.obj2String(jsonMap.get("enterprisesType")));
			List<String> list=(List<String>)jsonMap.get("businessType");
			StringBuilder sb=new StringBuilder();
			for (String tr : list) {
				 sb.append(tr).append("/");
			}
			organization.setBusinessType(sb.deleteCharAt(sb.length()-1).toString());
			
			List<Organization> orgList = organizationService.findByOrganizationName(organization.getName());
			if(orgList != null && !orgList.isEmpty()){
				map.put("status", "failure");
			}else{
				organizationService.createOrganization(organization);
	    		map.put("status", "success");
			}
    	}catch(Exception e){
    		LOG.error("OrganizationController[---addOrganization---]", e);
    		map.put("status", "failure");
    	}
        return map;
    }
    /**
     * [系统运营管理员,业务运营管理员] 修改企业
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
//	@RequiresPermissions("organization:update")
    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> update(String json,@CurrentUser User loginUser) {
    	LOG.info("Inside OrganizationController.update");
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
   			Organization organization = organizationService.findById(Long.valueOf(String.valueOf(jsonMap.get("id"))));
			organization.setName(TypeUtils.obj2String(jsonMap.get("name")));
   			organization.setShortname(TypeUtils.obj2String(jsonMap.get("shortname")));
   			organization.setAddress(TypeUtils.obj2String(jsonMap.get("address")));
   			organization.setIntroduction(TypeUtils.obj2String(jsonMap.get("introduction")));
			organization.setLinkman(TypeUtils.obj2String(jsonMap.get("linkman")));
			organization.setLinkmanPhone(TypeUtils.obj2String(jsonMap.get("linkmanPhone")));
			organization.setLinkmanEmail(TypeUtils.obj2String(jsonMap.get("linkmanEmail")));
			organization.setStartTime(TypeUtils.obj2DateFormat(jsonMap.get("startTime")));
			organization.setEndTime(TypeUtils.obj2DateFormat(jsonMap.get("endTime")));
			String status=TypeUtils.obj2String(jsonMap.get("status"));
			organization.setStatus(status);
			List<String> list=(List<String>)jsonMap.get("businessType");
			StringBuilder sb=new StringBuilder();
			for (String tr : list) {
				 sb.append(tr).append("/");
			}
			
			List<Organization> orgList = organizationService.findByOrganizationName(organization.getName());
			if(orgList != null && !orgList.isEmpty()){
				for(Organization org : orgList){
					Long orgId = org.getId();
					if(orgId != null && orgId.longValue() != organization.getId().longValue()){
						map.put("status", "failure");
						return map;
					}
				}
			}
			
			organization.setBusinessType(sb.deleteCharAt(sb.length()-1).toString());
   			organizationService.updateOrganization(organization);
	        map.put("status", "success");
			
    	}catch(Exception e){
    		LOG.error("OrganizationController[---update---]", e);
   		 	map.put("status", "failure");
    	}
       return map;
    }
    /**
     * [系统运营管理员,业务运营管理员] 根据企业id号查看审核历史记录
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/showAuditInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> showAuditInfo(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    	    List<AuditInfoModel> list=organizationService.showAuditInfo(TypeUtils.obj2Integer(jsonMap.get("id")));
    	    map.put("data",list);
    		map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController[---showAuditInfo---]", e);
    		map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * [系统运营管理员,业务运营管理员] 
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/findRelatedRentCompany", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> findRelatedRentCompany(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		Long entId=TypeUtils.obj2Long(jsonMap.get("entId"));
    		List<RelatedOrganizationInfo> list=organizationService.findRelatedRentCompany(entId);
    	    map.put("data",list);
    		map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController[---findRelatedRentCompany---]", e);
    		map.put("status", "failure");
    	}
        return map;
    }
    /**
     * [系统运营管理员,业务运营管理员] 
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/updateRelatedRentCompany", method = {RequestMethod.POST})
    @ResponseBody
    public Map<String,Object> updateRelatedRentCompany(String json,Long entId,String unRelatedArray) {
    	List<Map<String, Object>> jsonMap = JsonUtils.json2Object(json, List.class);
    	Map<String,Object> map = new HashMap<String,Object>();
    	try{
    		organizationService.updateRelatedRentCompany(jsonMap,entId);
    		map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController[---updateRelatedRentCompany---]", e);
    		map.put("status", "failure");
    	}
        return map;
    }
    /**
     * [租户管理员] 审核企业
     * @param id,status,comments(可选)
     * @return
     */
    @SuppressWarnings("unchecked")
//	@RequiresPermissions("organizationaudit:check")
    @RequestMapping(value = "/audit/check", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> checkOrganization(@CurrentUser User loginUser,String json) {
    	LOG.info("checkOrganization["+json+"]");
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Long userId = loginUser.getId();
    		Date date=new Date();
   			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Organization organization = organizationService.findById(Long.valueOf(String.valueOf(jsonMap.get("id"))));
			organization.setComments("");
			String status = TypeUtils.obj2String(jsonMap.get("status"));
			//审核不通过
			if(AuditStatus.NOPASSED.getValue().equals(status)){
				organization.setComments(TypeUtils.obj2String(jsonMap.get("reason")));
				//继续，续约
			}else if (AuditStatus.INSERVICE.getValue().equals(status)) {
				if(jsonMap.get("endTime") != null){
					organization.setEndTime(TypeUtils.obj2DateFormat(jsonMap.get("endTime")));
				}
				if (jsonMap.get("startTime")!=null) {
					if (date.compareTo(DateUtils.string2Date(jsonMap.get("startTime").toString(), "yyyy/MM/dd"))<0) {
						organization.setStartTime(date);
					}
				}
			}else if(AuditStatus.EXPIRED.getValue().equals(status)){
				organization.setEndTime(date);
			}
			organization.setStatus(status);
	        organizationService.auditOrganization(organization,userId);
	        map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("OrganizationController[---checkOrganization---]", e);
    		map.put("status", "failure");
    	}
       return map;
    }
    //查看额度充值历史记录
    
	@RequestMapping(value = "/showCreditHistory", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> showCreditHistoryByOrgId(@CurrentUser User loginUser,String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			CreditHistoryDto dto= JsonUtils.json2Object(json, CreditHistoryDto.class);
			PagModel pagModel=organizationService.showCreditHistoryByOrgId(dto);
			map.put("status", "success");
			map.put("data", pagModel);
		} catch (Exception e) {
			LOG.error("OrganizationController.showCreditHistory failed, caused by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	//给企业充值额度
	@RequestMapping(value = "/recharageCredit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> recharageCredit(@CurrentUser User User,String json){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			RecharageCreditDto dto= JsonUtils.json2Object(json, RecharageCreditDto.class);
			boolean flag=organizationService.recharageCredit(dto,User);
			map.put("status", "success");
			map.put("data", flag);
		} catch (Exception e) {
			LOG.error("OrganizationController.recharageCredit failed, caused by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	@RequestMapping(value = "/findOrgCreditById/{orgId}", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findOrgCreditById(@PathVariable("orgId") Long orgId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("children", "");
		try {
			Organization organization = organizationService.findOrgCreditById(orgId);
			map.put("status", "success");
			map.put("data",organization);
		} catch (Exception e) {
			LOG.error("OrganizationController.findOrgCreditById failed, caused by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
}

