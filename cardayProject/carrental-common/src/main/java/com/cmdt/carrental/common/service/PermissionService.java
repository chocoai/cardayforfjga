package com.cmdt.carrental.common.service;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cmdt.carrental.common.entity.Resource;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.util.JsonUtils;


@Service
public class PermissionService {

	private static final Logger LOG = LoggerFactory.getLogger(PermissionService.class);
	
	@Autowired
	public ResourceService resourceService;
	
	@Autowired
	public UserService userService;
	
	/**
	 * Find all permissions include menu and buttons.
	 * @return resource json string
	 */
	public String findWholePermissionTree(){
		LOG.info("find whole orgnization tree permission");
		List<Resource> menus = resourceService.findAll();
		return JsonUtils.object2Json(menus);
	}
	
	/**
	 * Find user's menu permissions.
	 * @param user
	 * @return user's menu json string.
	 */
	public List<Resource> findPermissionMenusByUser(User user){
		List<Resource> menus = resourceService.findMenus(userService.findPermissions(user.getUsername()));
//		String json = JsonUtils.object2Json(menus);
//		if(StringUtils.isEmpty(json)){
//			LOG.info("Login user '"+user.getUsername()+"' has no menu permission.");
//		}
		return menus;
	}
	
	/**
	 * Find user's button permissions.
	 * @param user
	 * @return user's button json string.
	 */
	public String findPermissionButtonByUser(User user){
		List<Resource> menus = resourceService.findButtons(userService.findPermissions(user.getUsername()));
		String json = JsonUtils.object2Json(menus);
		if(StringUtils.isEmpty(json)){
			LOG.info("Login user '"+user.getUsername()+"' has no button permission.");
		}
		return json;
	}

	/**
	 * Find user's no permission menus.
	 * @param user
	 * @return user's menu json string.
	 */
	public List<Resource> findNoPermissionMenusByUser(User user){
		//If user is a driver, he has no menu permission.
		if(user != null && user.isDriver()){
			LOG.info("A driver login, has no menu permission!");
			return resourceService.findMenus(null);
		}else if(user == null){
			LOG.error("No user information found, generate menu failed!");
			return Collections.emptyList();
		}
		//To find user's no permission menu.
		List<Resource> noPermissionMmenus = resourceService.findMenus(userService.findPermissions(user.getUsername()));
		List<Resource> menus = resourceService.findMenus(null);
		menus.removeAll(noPermissionMmenus);

		return menus;
//		String json = JsonUtils.object2Json(menus);
//		if(StringUtils.isEmpty(json)){
//			LOG.info("Login user '"+user.getUsername()+"' has all permission.");
//		}
//		return json;
	}
	
	/**
	 * Find user's no permission buttons.
	 * @param user
	 * @return user's button json string.
	 */
	public List<Resource> findNoPermissionButtonsByUser(User user){
		if(user == null){
			LOG.error("No user information found, generate button failed!");
			return Collections.emptyList();
		}
		//To find user's no permission buttons.
		List<Resource> noPermissionMmenus = resourceService.findButtons(userService.findPermissions(user.getUsername()));
		List<Resource> buttons = resourceService.findButtons(null);
		buttons.removeAll(noPermissionMmenus);
//		String json = JsonUtils.object2Json(buttons);
//		if(StringUtils.isEmpty(json)){
//			LOG.info("Login user '"+user.getUsername()+"' has all permission.");
//		}
		return buttons;
	}
}
