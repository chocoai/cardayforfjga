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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmdt.carrental.common.entity.Resource;
import com.cmdt.carrental.common.entity.Role;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.service.PermissionService;
import com.cmdt.carrental.common.service.ResourceService;
import com.cmdt.carrental.common.service.RoleService;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/resource")
public class ResourceController {
	private static final Logger LOG = LoggerFactory.getLogger(ResourceController.class);
    @Autowired
    private ResourceService resourceService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
	public PermissionService permissionService;
    
    @ModelAttribute("types")
    public Resource.ResourceType[] resourceTypes() {
        return Resource.ResourceType.values();
    }
    /**
     * [超级管理员]查询所有资源
     * [普通管理员]查询当前拥有资源
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> resourceList(@CurrentUser User loginUser) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("root", "");
    	try{
    		 List<Resource> resources =  new ArrayList<Resource>();
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
    		 map.put("status", "success");
    	     map.put("root", resourceService.formatList2Tree(resources));
    	}catch(Exception e){
    		 LOG.error("ResourceController.resourceList error:",e);
    		 map.put("status", "failure");
    	}
        return map;
    }
    
    /**
     * [超级管理员]新增资源
     * @return
     */
    @RequiresPermissions("resource:create")
    @RequestMapping(value = "/{parentId}/appendChild", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> showAppendChildForm(@PathVariable("parentId") Long parentId) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
        Resource parent = resourceService.findOne(parentId);
        Resource child = new Resource();
        child.setParentId(parentId);
        String parentIds=parent.makeSelfAsParentIds();
        if(parentIds.lastIndexOf(",")>0){
        	parentIds=parentIds.substring(0, parentIds.lastIndexOf(","));
        }
        child.setParentIds(parentIds);
        map.put("data",child);
        return map;
    }

    /**
     * [超级管理员]新增资源
     * @param name,parentId,permission,type,url
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequiresPermissions("resource:create")
    @RequestMapping(value = "/{parentId}/appendChild", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> create(String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		Resource resource=new Resource();
    		resource.setName(String.valueOf(jsonMap.get("name")));
    		resource.setParentId(Long.valueOf(String.valueOf(jsonMap.get("parentId"))));
    		resource.setParentIds(String.valueOf(jsonMap.get("parentIds")));
    		resource.setPermission(String.valueOf(jsonMap.get("permission")));
    		resource.setType(String.valueOf(jsonMap.get("type")));
//    		if(jsonMap.get("type").equals("menu")){
//    			resource.setType(Resource.ResourceType.menu);
//    		}else if(jsonMap.get("type").equals("button")){
//    			resource.setType(Resource.ResourceType.button);
//    		}
    		resource.setUrl(String.valueOf(jsonMap.get("url")));
	        resourceService.createResource(resource);
	        map.put("status", "success");
		}catch(Exception e){
			 LOG.error("ResourceController.create error:",e);
			 map.put("status", "failure");
		}
        return map;
    }

    /**
     * [超级管理员]根据ID查询要修改的资源
     * @return
     */
    @RequiresPermissions("resource:update")
    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> showUpdateForm(@PathVariable("id") Long id) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	Resource resource=resourceService.findOne(id);
    	if(resource != null){
	    	map.put("data", resource);
	    	map.put("status", "success");
		}else{
			map.put("status", "failure");
		}
        return map;
    }

    /**
     * [超级管理员]修改资源
     * @param id,name,parentId,permission,type,url
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequiresPermissions("resource:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> update(String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		Resource resource=new Resource();
    		resource.setId(Long.valueOf(String.valueOf(jsonMap.get("id"))));
    		resource.setName(String.valueOf(jsonMap.get("name")));
    		resource.setParentId(Long.valueOf(String.valueOf(jsonMap.get("parentId"))));
    		resource.setParentIds(String.valueOf(jsonMap.get("parentIds")));
    		resource.setPermission(String.valueOf(jsonMap.get("permission")));
    		resource.setType(String.valueOf(jsonMap.get("type")));
//    		if(jsonMap.get("type").equals("menu")){
//    			resource.setType(Resource.ResourceType.menu);
//    		}else if(jsonMap.get("type").equals("button")){
//    			resource.setType(Resource.ResourceType.button);
//    		}
    		resource.setUrl(String.valueOf(jsonMap.get("url")));
	        resourceService.updateResource(resource);
	        map.put("status", "success");
		}catch(Exception e){
			 LOG.error("ResourceController.update error:",e);
			 map.put("status", "failure");
		}
        return map;
    }

    /**
     * [超级管理员]删除资源
     * @return
     */
    @RequiresPermissions("resource:delete")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> delete(@CurrentUser User loginUser,@PathVariable("id") Long id) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		//超级管理员
			if(loginUser.isSuperAdmin()){
				resourceService.deleteResource(id);
		        map.put("status", "success");
			}
			//普通管理员，不能删除资源
			if(loginUser.isAdmin()){
				map.put("status", "failure");
			}
		}catch(Exception e){
			 LOG.error("ResourceController.delete error:",e);
			 map.put("status", "failure");
		}
        return map;
    }
    
    /**
	 * Find user's menu permissions.
	 * @param user
	 * @return user's menu json string.
	 */
    @ResponseBody
    @RequestMapping(value = "/loadUserMenus", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String loadUserMenus(@CurrentUser User user){
		List<Resource> menus = permissionService.findPermissionMenusByUser(user);
		String json = JsonUtils.object2Json(menus);
		if(StringUtils.isEmpty(json)){
			LOG.info("Login user '"+user.getUsername()+"' has no menu permission.");
		}
		return json;
	}
    
    /**
	 * Find user's no permission menus.
	 * @param user
	 * @return user's menu json string.
	 */
    @ResponseBody
    @RequestMapping(value = "/loadUserNoPermissionMenus", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String loadUserNoPermissionMenus(@CurrentUser User user){
		List<Resource> menus = permissionService.findNoPermissionMenusByUser(user);
		String json = JsonUtils.object2Json(menus);
		if(StringUtils.isEmpty(json)){
			LOG.info("Login user '"+user.getUsername()+"' has all menu permission.");
		}
		return json;
	}
    
    /**
	 * Find user's no permission buttons.
	 * @param user
	 * @return user's button json string.
	 */
    @ResponseBody
    @RequestMapping(value = "/loadUserNoPermissionButtons", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String loadUserNoPermissionbuttons(@CurrentUser User user){
		List<Resource> menus = permissionService.findNoPermissionButtonsByUser(user);
		String json = JsonUtils.object2Json(menus);
		if(StringUtils.isEmpty(json)){
			LOG.info("Login user '"+user.getUsername()+"' has all button permission.");
		}
		return json;
	}
}
