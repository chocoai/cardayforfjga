package com.cmdt.carday.microservice.biz.service;

import java.util.*;

import com.cmdt.carday.microservice.api.BaseApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.entity.Resource;
import com.cmdt.carrental.common.entity.Role;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.service.PermissionService;
import com.cmdt.carrental.common.service.ResourceService;
import com.cmdt.carrental.common.service.RoleService;
import com.cmdt.carrental.common.util.TreeNode;
import com.cmdt.carrental.common.util.TreeNodeView;
import com.cmdt.carday.microservice.common.Constants;
import com.cmdt.carday.microservice.model.request.resource.ResourceDto;

//@Service
public class PlatformResouceService {

	private static final Logger LOG = LoggerFactory.getLogger(PlatformResouceService.class);

	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	public PermissionService permissionService;

	/**
	 * [超级管理员]查询所有资源
	 * [普通管理员]查询当前拥有资源
	 * @param loginUser
	 * @return
	 */
	public TreeNode resourceList(User loginUser) {
    	List<Resource> resources =  new ArrayList<Resource>();
    	if (null != loginUser) {
    		//超级管理员
    		if(loginUser.isSuperAdmin()){
    			resources = resourceService.findByResourceId(1l);//最顶层“资源”节点ID为1
    		}
    		//租户或企业管理员
    		if(loginUser.isAdmin() || loginUser.isRentAdmin()){
    			Long roleId=loginUser.getRoleId();
    			Role role = roleService.findOne(roleId);
    			String[] resourceIds=role.getResourceIds().split(",");
    			for(String resourceId:resourceIds){
    				resources.addAll(resourceService.findByResourceId(Long.valueOf(resourceId)));
    			}
    			resources.add(0, resourceService.findOne(1l));//添加一个root资源节点
    		}
    	}
        return resourceService.formatList2Tree(resources);
    }
	
	public TreeNodeView showRoleResources(User loginUser,Long roleId){
		List<Resource> resources =  new ArrayList<Resource>();
		 //超级管理员
		 if(loginUser.isSuperAdmin()){
			 resources = resourceService.findResourcesByRoleId(roleId);//最顶层“资源”节点ID为1
		 } else {
		 	return null;
		 }

		 //判断resources第一个是否是root的根节点（resource id = 1），如果不是，则添加一个root资源节点
		 if (resources.get(0).getId() != 1L) {
			 resources.add(0, resourceService.findOne(1l));
		 }
		 return resourceService.formatList2TreeView(resources);
	}
	
	public Resource showAppendChildForm(Long parentId) {
		Resource parent = resourceService.findOne(parentId);
		Resource child = new Resource();
		child.setParentId(parentId);
		String parentIds=parent.makeSelfAsParentIds();
		if(parentIds.lastIndexOf(",")>0){
			parentIds=parentIds.substring(0, parentIds.lastIndexOf(","));
		}
		child.setParentIds(parentIds);
		return child;
	}
	
	public void create(ResourceDto resourceDto) {
    	Resource resource=new Resource();
    	resource.setName(resourceDto.getName());
    	resource.setParentId(resourceDto.getParentId());
    	resource.setParentIds(resourceDto.getParentIds());
    	resource.setPermission(resourceDto.getPermission());
    	resource.setType(resourceDto.getType());
//    	if(createResourceDto.getType().equals("menu")){
//    		resource.setType(Resource.ResourceType.menu);
//    	}else if(createResourceDto.getType().equals("button")){
//    		resource.setType(Resource.ResourceType.button);
//    	}
    	resource.setUrl(resourceDto.getUrl());
    	resource.setAvailable(resourceDto.getAvailable());
    	resource.setDescription(resourceDto.getDescription());
    	resourceService.createResource(resource);
    }
	
	public Resource showUpdateForm(Long id) {
//		Map<String,Object> map = new HashMap<String,Object>();
		return resourceService.findOne(id);
//		if(resource != null){
//			map.put(Constants.DATA_STR, resource);
//			map.put(Constants.MSG_STR, Constants.API_MESSAGE_SUCCESS);
//	        map.put(Constants.STATUS_STR, Constants.API_STATUS_SUCCESS);
//		}else{
//			map.put(Constants.MSG_STR, "数据为空");
//			map.put(Constants.STATUS_STR, Constants.API_STATUS_FAILURE);
//		}
//		return map;
	}
	
	public void update(ResourceDto resourceDto) {
		Resource resource=new Resource();
		resource.setId(resourceDto.getId());
		resource.setName(resourceDto.getName());
		resource.setParentId(resourceDto.getParentId());
		resource.setParentIds(resourceDto.getParentIds());
		resource.setPermission(resourceDto.getPermission());
		resource.setType(resourceDto.getType());
//    	if(createResourceDto.getType().equals("menu")){
//    		resource.setType(Resource.ResourceType.menu);
//    	}else if(createResourceDto.getType().equals("button")){
//    		resource.setType(Resource.ResourceType.button);
//    	}
		resource.setUrl(resourceDto.getUrl());
		resource.setAvailable(resourceDto.getAvailable());
    	resource.setDescription(resourceDto.getDescription());
		resourceService.updateResource(resource);
	}
	
	public void delete(User loginUser, Long id) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	if (null != loginUser) {
    		//超级管理员
        	if(loginUser.isSuperAdmin()){
        		resourceService.deleteResource(id);
        	}
    	}
    }
	
	public List<Resource> loadUserMenus(User user){
		if (null != user) {
			return permissionService.findPermissionMenusByUser(user);
		}
		return Collections.emptyList();
	}
	
	public List<Resource> loadUserNoPermissionMenus(User user){
		if (null != user) {
			return permissionService.findNoPermissionMenusByUser(user);
		}
		return Collections.emptyList();
	}
	
	public List<Resource> loadUserNoPermissionbuttons(User user){
		String json = null;
		if (null != user) {
			return permissionService.findNoPermissionButtonsByUser(user);
		}
		return Collections.emptyList();
	}
}