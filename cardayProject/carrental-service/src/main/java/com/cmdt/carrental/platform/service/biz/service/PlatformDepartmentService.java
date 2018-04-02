package com.cmdt.carrental.platform.service.biz.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.OrgAndParentOrgByOrgIdModel;
import com.cmdt.carrental.common.model.OrganizationModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.util.TreeNode;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.platform.service.model.request.department.DepartmentCreateDto;
import com.cmdt.carrental.platform.service.model.request.department.DepartmentCreditModel;
import com.cmdt.carrental.platform.service.model.request.department.DepartmentPageDto;
import com.cmdt.carrental.platform.service.model.request.department.DepartmentUpdateDto;

@Service
public class PlatformDepartmentService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PlatformDepartmentService.class);
	
    @Autowired
    private OrganizationService organizationService;
	
    /**
     * 
     * @param user
     * @return
     */
	public List<Organization> findDepartmentByUser(User user){
		 List<Organization> list = new ArrayList<Organization>();
		 
   		 //企业管理员
   		 if(null!=user&&user.isEntAdmin()){
   			list = organizationService.findByOrganizationId(user.getOrganizationId());
   		 }
   		 
   		 return list;
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public List<TreeNode> findDepartmentTreeByUser(User user){
		List<TreeNode> treeNodeList = new ArrayList<TreeNode>();
		//超级管理员
  		 if(user.isSuperAdmin()){
  			List<Organization> entlist = organizationService.findEntList();
  			if(entlist != null && entlist.size() > 0){
  				
  				
  				for(Organization ent : entlist){
  					List<Organization> deptList = organizationService.findByOrganizationId(ent.getId());
  					if(deptList != null && deptList.size() > 0){
  						TreeNode treeNode = organizationService.formatOrganizationList2Tree(deptList);
  						treeNodeList.add(treeNode);
  					}
  				}
  				
  				if(treeNodeList != null && treeNodeList.size() > 0){
  					treeNodeList.get(0).setChecked(true);
  				}
  			}
  		 }
  		 
  		 //企业管理员
  		 if(user.isAdmin()){
  			List<Organization> list = organizationService.findByOrganizationId(user.getOrganizationId());
	   		 if(list != null && list.size() > 0){
	   			TreeNode treeNodeData = organizationService.formatOrganizationList2Tree(list);
	   			treeNodeData.setChecked(true);
       	    }
  		 }
  		 
  		 return treeNodeList;
		
	}
	
	/**
	 * 
	 * @param parentId
	 * @return
	 */
	public Map<String,Object> prepareInfoByParentId(Long parentId){
		 Map<String,Object> map = new HashMap<String,Object>();
		 Organization parent = organizationService.findOne(parentId);
		 OrganizationModel child = new OrganizationModel();
	        if(parent != null){
		        child.setParentId(parentId);
		        child.setParentIds(parent.makeSelfAsParentIds());
		        map.put(Constants.DATA_STR, child);
		        map.put(Constants.STATUS_STR, Constants.API_STATUS_SUCCESS);
	        }else{
	        	 map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
	        }
	    return map;
	}
	
	public List<Organization> showList(User loginUser) {
		List<Organization> list = new ArrayList<Organization>();
		// 企业管理员
		if (loginUser.isEntAdmin()) {
			list = organizationService.findByOrganizationId(loginUser.getOrganizationId());
		}
		return list;
	}
	
	public List<TreeNode> showTree(User user) {
		if (null == user) {
			throw new ServerException(Constants.API_ERROR_MSG_USER_NOT_EXIST); 
		}
		return Arrays.asList(organizationService.formatOrganizationList2Tree(user.getOrganizationId()));
	}
	
	public List<TreeNode> showTreeByOrgId(Long orgId) {
		return Arrays.asList(organizationService.formatOrganizationList2Tree(orgId));
	}
	
	public OrgAndParentOrgByOrgIdModel findOrganizationByOrgId(Long orgId) {
		return organizationService.findOrganizationAndDirectOrganizationByOrgId(orgId);
	}
	
	public OrgAndParentOrgByOrgIdModel appendChildShowOrganizationByParentId(Long parentId) {
		return organizationService.appendChildShowOrganizationByParentId(parentId);
	}
	
	public void create(DepartmentCreateDto dto) throws Exception {
		OrgAndParentOrgByOrgIdModel model = new OrgAndParentOrgByOrgIdModel();
		PropertyUtils.copyProperties(model, dto);
		Organization organization = organizationService.appendChild(model);
		if (null == organization) {
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	public void updateOrganization(DepartmentUpdateDto dto) throws Exception {
		OrgAndParentOrgByOrgIdModel model = new OrgAndParentOrgByOrgIdModel();
		PropertyUtils.copyProperties(model, dto);
		
		if (!organizationService.checkResource(model.getId())) {
			OrgAndParentOrgByOrgIdModel oldModel = organizationService.findOrganizationAndDirectOrganizationByOrgId(model.getId());
			if(oldModel.getParentId().equals(model.getParentId())){
				//如果上级部门没有修改，则可以修改此部门（即只修改了部门名称）
				organizationService.updateAndCheckOrganization(model);
			}else{
				throw new ServerException("该部门有资源信息，无法修改上级部门！");
			}
		} else {
			organizationService.updateAndCheckOrganization(model);
		}
	}
	
	public void deleteOrganization(Long orgId) {
		if (!organizationService.checkResource(orgId)) {
			throw new ServerException("该部门有资源信息，删除失败！");
		} else {
			organizationService.dropOrganization(orgId);
		}
	}
	
	public PagModel listDirectChildrenWithCount(User user,DepartmentPageDto departmentDto){
		PagModel PagModel= new PagModel();
		
		int currentPage = 1;
    	int numPerPage = 10;
		
		if(departmentDto.getCurrentPage() != null){
    		currentPage = departmentDto.getCurrentPage();
    	}
    	if(departmentDto.getNumPerPage() != null){
    		numPerPage = departmentDto.getNumPerPage();
    	}
		
		//企业管理员
   		if(user.isEntAdmin()){
   			PagModel = organizationService.listDirectChildrenWithLevelCount(user.getOrganizationId(),currentPage,numPerPage);
   		 }
   		return PagModel;
	}

	public List<TreeNode> findTreeCreditByOrgId(Long orgId) {
		return Arrays.asList(organizationService.formatCreditTree(orgId));
	}
	
	public void updateCredit(List<DepartmentCreditModel> creditModels) {
		Map<String, Object> map = null;
		List<Map<String, Object>> creditList = new ArrayList<>();
		for (DepartmentCreditModel model : creditModels) {
			map = new HashMap<>();
			map.put("availableCredit", model.getAvailableCredit());
			map.put("limitedCredit", model.getLimitedCredit());
			map.put("orgId", model.getOrgId());
			creditList.add(map);
		}
		organizationService.batchUpdate(creditList);
	}
	
	public Organization findOrgById(Long orgId) {
		return organizationService.findById(orgId);
	}
	
	
	

}
