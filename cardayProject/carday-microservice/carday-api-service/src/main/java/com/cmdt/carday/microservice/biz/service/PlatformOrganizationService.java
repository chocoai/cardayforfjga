package com.cmdt.carday.microservice.biz.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.bean.AuditStatus;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.AuditInfoModel;
import com.cmdt.carrental.common.model.OrgListModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.RelatedOrganizationInfo;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carday.microservice.common.model.response.MessageCode;
import com.cmdt.carday.microservice.common.model.response.exception.ServiceException;
import com.cmdt.carday.microservice.model.request.organization.CheckOrganizationDto;
import com.cmdt.carday.microservice.model.request.organization.CreateOrganizationDto;
import com.cmdt.carday.microservice.model.request.organization.OrganizationListDto;
import com.cmdt.carday.microservice.model.request.organization.UpdateOrganizationDto;
import com.cmdt.carday.microservice.model.request.organization.UpdateRelatedRentCompanyDto;

@Service
public class PlatformOrganizationService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PlatformOrganizationService.class);
	
	@Autowired
    private OrganizationService organizationService;
	
	
	public PagModel showList(OrganizationListDto dto){
		Map<String,Object> map = new HashMap<>();
		map.put("currentPage", dto.getCurrentPage());
		map.put("numPerPage", dto.getNumPerPage());
		map.put("organizationName", dto.getOrganizationName());
		map.put("status", dto.getStatus());
		map.put("organizationType", dto.getOrganizationType());
    	
    	return organizationService.findEntList(map);
    }
	
//	public List<Organization> showList(User loginUser) {
//    	List<Organization> list = new ArrayList<Organization>();
//    	
//    	//超级管理员
//    	if(null != loginUser && loginUser.isSuperAdmin()){
//    		list = organizationService.findEntList();
//    	}
////    	租户管理员不存在,删除
////    	//租户管理员
////    	if(null != loginUser && loginUser.isRentAdmin()){
////    		list = organizationService.findOrganizationByRentId(loginUser.getOrganizationId());
////    	}
//    	
//        return list;
//    }
	
	public List<Organization> showAuditList(User loginUser) {
    	List<Organization> list = new ArrayList<Organization>();
    	
    	//超级管理员
    	if(null != loginUser && loginUser.isSuperAdmin()){
    		list = organizationService.findOrganizationAuditedByAdminId(loginUser.getOrganizationId());
    	}
//    	租户管理员不存在,删除
//    	//租户管理员
//    	if(loginUser.isRentAdmin()){
//    		list = organizationService.findOrganizationAuditByRentId(loginUser.getOrganizationId());
//    	}
    	
        return list;
    }
	
    public Organization addOrganization(CreateOrganizationDto dto) {
    	Organization organization = new Organization();
    	organization.setName(dto.getName());
    	organization.setShortname(dto.getShortname());
    	organization.setAddress(dto.getAddress());
    	organization.setIntroduction(dto.getIntroduction());
    	organization.setLinkman(dto.getLinkman());
    	organization.setLinkmanPhone(dto.getLinkmanPhone());
    	organization.setLinkmanEmail(dto.getLinkmanEmail());
    	organization.setStartTime(TypeUtils.obj2DateFormat(dto.getStartTime()));
    	organization.setEndTime(TypeUtils.obj2DateFormat(dto.getEndTime()));
    	organization.setEnterprisesType(dto.getEnterprisesType());
    	List<String> list=(List<String>)dto.getBusinessType();
    	StringBuilder sb=new StringBuilder();
    	for (String tr : list) {
    		sb.append(tr).append("/");
    	}
    	organization.setBusinessType(sb.deleteCharAt(sb.length()-1).toString());
    	
    	List<Organization> orgList = organizationService.findByOrganizationName(organization.getName());
    	if(orgList != null && !orgList.isEmpty()){
    		throw new ServiceException(MessageCode.COMMON_FAILURE);
    	}else{
    		return organizationService.createOrganization(organization);
    	}
    }
    
    public void update(UpdateOrganizationDto dto) {
   			Organization organization = organizationService.findById(dto.getId());
			organization.setName(dto.getName());
   			organization.setShortname(dto.getShortname());
   			organization.setAddress(dto.getAddress());
   			organization.setIntroduction(dto.getIntroduction());
			organization.setLinkman(dto.getLinkman());
			organization.setLinkmanPhone(dto.getLinkmanPhone());
			organization.setLinkmanEmail(dto.getLinkmanEmail());
			organization.setStartTime(TypeUtils.obj2DateFormat(dto.getStartTime()));
			organization.setEndTime(TypeUtils.obj2DateFormat(dto.getEndTime()));
			String status=dto.getStatus();
			organization.setStatus(status);
			List<String> list=(List<String>)dto.getBusinessType();
			StringBuilder sb=new StringBuilder();
			for (String tr : list) {
				 sb.append(tr).append("/");
			}
			
			List<Organization> orgList = organizationService.findByOrganizationName(organization.getName());
			if(orgList != null && !orgList.isEmpty()){
				for(Organization org : orgList){
					Long orgId = org.getId();
					if(orgId != null && orgId.longValue() != organization.getId().longValue()){
						throw new ServiceException(MessageCode.COMMON_FAILURE);
					}
				}
			}
			
			organization.setBusinessType(sb.deleteCharAt(sb.length()-1).toString());
   			organizationService.updateOrganization(organization);
    }
    
    
    public List<AuditInfoModel> showAuditInfo(Integer id) {
    	return organizationService.showAuditInfo(id);
    }
	
    public List<RelatedOrganizationInfo> findRelatedRentCompany(Long entId) {
    	return organizationService.findRelatedRentCompany(entId);
    }
    
    public void updateRelatedRentCompany(List<UpdateRelatedRentCompanyDto> relatedRentCompanyList,Long entId) {
    	Map<String, Object> map = null;
		List<Map<String, Object>> list = new ArrayList<>();
		for (UpdateRelatedRentCompanyDto dto : relatedRentCompanyList) {
			map = new HashMap<>();
			if (null != dto.getEntId()) {
				map.put("entId", dto.getEntId());
			}
			if (null != dto.getRentId()) {
				map.put("rentId", dto.getRentId());
			}
			map.put("vehicleNumber", dto.getVehicleNumber());
			map.put("driverNumber", dto.getDriverNumber());
			list.add(map);
		}
		
    	organizationService.updateRelatedRentCompany(list,entId);
    }
    
    public void checkOrganization(CheckOrganizationDto dto) {
    	Date date=new Date();
    	Organization organization = organizationService.findById(dto.getId());
    	organization.setComments("");
    	String status = dto.getStatus();
    	//审核不通过
    	if(AuditStatus.NOPASSED.getValue().equals(status)){
    		organization.setComments(dto.getReason());
    		//继续，续约
    	}else if (AuditStatus.INSERVICE.getValue().equals(status)) {
    		if(dto.getEndTime() != null){
    			organization.setEndTime(TypeUtils.obj2DateFormat(dto.getEndTime()));
    		}
    		if (dto.getStartTime()!=null) {
    			if (date.compareTo(DateUtils.string2Date(dto.getStartTime().toString(), "yyyy/MM/dd"))<0) {
    				organization.setStartTime(date);
    			}
    		}
    	}else if(AuditStatus.EXPIRED.getValue().equals(status)){
    		organization.setEndTime(date);
    	}
    	organization.setStatus(status);
    	organizationService.auditOrganization(organization,dto.getUserId());
    }
	/*
	 * 租户管理员不存在,删除
	 * 
	 * public Map<String,Object> checkOrganization(User loginUser, UpdateOrganizationStatusDto checkOrganizationDto) {
		Map<String,Object> map = new HashMap<String,Object>();
		//租户管理员
		if(null != loginUser && loginUser.isRentAdmin()){
			Organization organization = organizationService.findById(checkOrganizationDto.getId());
			if(organization != null){
				String status = checkOrganizationDto.getStatus();
				organization.setStatus(status);
				organization.setComments("");
				//审核不通过
				if("3".equals(status)){
					if(checkOrganizationDto.getComments() != null){
						organization.setComments(checkOrganizationDto.getComments());
					}
				}
				organizationService.checkOrganization(organization);
			}
			map.put(Constants.MSG_STR, Constants.API_MESSAGE_SUCCESS);
			map.put(Constants.STATUS_STR, Constants.API_STATUS_SUCCESS);
		}else{
			map.put(Constants.MSG_STR, "用户非租户管理员");
			map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
		}
		return map;
    }*/

	/*
	 * 租户管理员不存在,删除
	 * 
	 * public Map<String,Object> activateOrganizationService(User loginUser, ActivateOrganizationDto activateOrganizationDto) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	//租户管理员
    	if(null != loginUser && loginUser.isRentAdmin()){
    		Organization organization = organizationService.findById(activateOrganizationDto.getId());
    		if(organization != null){
    			if(activateOrganizationDto.getStartTime() != null && activateOrganizationDto.getEndTime() != null){
    				organization.setStartTime(TimeUtils.getDaytime(activateOrganizationDto.getStartTime()));
    				organization.setEndTime(TimeUtils.getDaytime(activateOrganizationDto.getEndTime()));
    			}
    			String status = activateOrganizationDto.getStatus();
    			organization.setStatus(status);
    			
    			organizationService.activateOrganizationService(organization);
    		}
    		map.put(Constants.MSG_STR, Constants.API_MESSAGE_SUCCESS);
			map.put(Constants.STATUS_STR, Constants.API_STATUS_SUCCESS);
    	}else{
    		map.put(Constants.MSG_STR, "用户非租户管理员");
			map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
    	}
       return map;
    }*/
	
	/*
	 * 租户管理员不存在,删除
	 * 
	 * public Map<String,Object> terminateOrganizationService(User loginUser,UpdateOrganizationStatusDto updateOrganizationDto) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	//租户管理员
    	if(null != loginUser && loginUser.isRentAdmin()){
    		Organization organization = organizationService.findById(updateOrganizationDto.getId());
    		if(organization != null){
    			String status = updateOrganizationDto.getStatus();
    			organization.setStatus(status);
    			
    			organizationService.updateOrganizationStatus(organization);
    		}
    		map.put(Constants.MSG_STR, Constants.API_MESSAGE_SUCCESS);
    		map.put(Constants.STATUS_STR, Constants.API_STATUS_SUCCESS);
    	}else{
    		map.put(Constants.MSG_STR, "用户非租户管理员");
    		map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
    	}
       return map;
    }*/
	
	public List<Organization> listAuditByOrgName(User loginUser, String name) {
    	List<Organization> list = new ArrayList<Organization>();
    	
    	//超级管理员
    	if(null != loginUser && loginUser.isSuperAdmin()){
    		list = organizationService.findAuditByOrganizationName(name);
    	}
//    	租户管理员不存在,删除
//    	//租户管理员
//    	if(null != loginUser && loginUser.isRentAdmin()){
//    		list = organizationService.findAuditByOrganizationNameAndRentId(name, loginUser.getOrganizationId());
//    	}
        return list;
    }
	
	public List<Organization> listOrgByName(User loginUser, String name) {
    	List<Organization> list = new ArrayList<Organization>();
    	
    	//超级管理员
    	if(null != loginUser && loginUser.isSuperAdmin()){
    		list = organizationService.findByOrganizationName(name);
    	}
//    	租户管理员不存在,删除
//    	//租户管理员
//    	if(null != loginUser && loginUser.isRentAdmin()){
//    		list = organizationService.findByOrganizationNameAndRentId(name, loginUser.getOrganizationId());
//    	}
    	
        return list;
    }
	
	public Organization findById(User loginUser,Long id) {
    	Organization organization = new Organization();
    	if (null != loginUser) {
    		if(loginUser.isSuperAdmin() || loginUser.isRentAdmin() || loginUser.isAdmin()){
    			organization = organizationService.findById(id);
    			if(organization != null){
    				return organization;
    			}
    		}
    	}
        return null;
    }
	
	/*
	 * 租户管理员不存在,删除
	 * 
	 * public void create(User loginUser, CreateOrganizationDto organizationDto) {
    	Organization organization = new Organization();
    	
    	if(organizationDto.getParentId() == null){
    		organization.setParentId(Long.valueOf(0));
    	}else{
    		organization.setParentId(organizationDto.getParentId());
    	}
    	
    	if(organizationDto.getParentIds() == null){
    		organization.setParentIds("0");
    	}else{
    		organization.setParentIds(organizationDto.getParentIds());
    	}
    	
    	//租户管理员
    	if(null != loginUser && loginUser.isRentAdmin()){
    		organization.setName(organizationDto.getName());
    		organization.setStatus("0");//需要审核
    		organization.setShortname(organizationDto.getShortname());
    		organization.setLinkman(organizationDto.getLinkman());
    		organization.setLinkmanPhone(organizationDto.getLinkmanPhone());
    		organization.setLinkmanEmail(organizationDto.getLinkmanEmail());
    		organization.setVehileNum(organizationDto.getVehileNum());
    		organization.setCity(organizationDto.getCity());
    		organization.setStartTime(TimeUtils.getDaytime(organizationDto.getStartTime()));
    		organization.setEndTime(TimeUtils.getDaytime(organizationDto.getEndTime()));
    		organization.setAddress(organizationDto.getAddress());
    		organization.setIntroduction(organizationDto.getIntroduction());
    		organizationService.createOrganization(organization, loginUser.getOrganizationId());
    	}
    }*/
	
	/*
	 * 租户管理员不存在,删除
	 * 
	 * public void update(User loginUser, UpdateOrganizationDto organizationDto) {
		if (null != loginUser) {
			if(loginUser.isRentAdmin() || loginUser.isAdmin()){
				//租户管理员
				if(loginUser.isRentAdmin()){
					Organization organization = organizationService.findById(organizationDto.getId());
					if(organization != null){
						organization.setName(organizationDto.getName());
						organization.setShortname(organizationDto.getShortname());
						organization.setLinkman(organizationDto.getLinkman());
						organization.setLinkmanPhone(organizationDto.getLinkmanPhone());
						organization.setLinkmanEmail(organizationDto.getLinkmanEmail());
						organization.setVehileNum(organizationDto.getVehileNum());
						organization.setCity(organizationDto.getCity());
						organization.setStartTime(TimeUtils.getDaytime(organizationDto.getStartTime()));
						organization.setEndTime(TimeUtils.getDaytime(organizationDto.getEndTime()));
						organization.setAddress(organizationDto.getAddress());
						organization.setIntroduction(organizationDto.getIntroduction());
					}
					if(organization != null){
						organizationService.updateOrganization(organization);
					}
				}
			}
		}
    }*/
	
	/*	
	 * 租户管理员不存在,删除
	 * 
     * public void delete(User loginUser, Long id) {
		if(null != loginUser && loginUser.isRentAdmin()){
			organizationService.deleteOrganization(id);
		}
    }*/
	
	public List<OrgListModel> findLowerLevelOrgList(User loginUser) {
			
		List<OrgListModel> list = null;
//		租户管理员不存在,删除
//		//租户管理员
//		if(null != loginUser && loginUser.isRentAdmin()){
//			Long rentId = loginUser.getOrganizationId();
//			list = organizationService.findLowerLevelOrgByRentAdmin(rentId);
//		}
		
		//企业管理员
		if(null != loginUser && loginUser.isEntAdmin()){
			Long entId = loginUser.getOrganizationId();
			list = organizationService.findfindLowerLevelOrgByEntAdmin(entId);
		}
		
		return list;
	}
	
	public List<Organization> showAuditedList(User loginUser) {
    	List<Organization> list = new ArrayList<Organization>();
    	
    	//租户管理员
    	if(null != loginUser && loginUser.isSuperAdmin()){
    		list = organizationService.findOrganizationAuditedByAdminId(loginUser.getOrganizationId());
    	}
    	
        return list;
    }
	
	public List<Organization> showAuditedByAdminList(User loginUser) {
		List<Organization> list = new ArrayList<Organization>();
		
		//admin
		if(null != loginUser && loginUser.isSuperAdmin()){
			list = organizationService.findOrganizationAuditedByAdminId(loginUser.getOrganizationId());
		}
	   		 
        return list;
    }
}