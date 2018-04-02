package com.cmdt.carrental.portal.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmdt.carrental.common.entity.Resource;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.service.ResourceService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
public class LoginController extends BaseController {

	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	public ResourceService resourceService;

	@Autowired
	public UserService userService;
	
	@RequestMapping(value = "/login")
	//@ResponseBody
	public String showLoginForm(HttpServletRequest req, Model model) {		
		LOG.info("Inside login");
		String exceptionClassName = (String) req.getAttribute("shiroLoginFailure");
		if(UnknownAccountException.class.getName().equals(exceptionClassName) && !StringUtils.isEmpty(exceptionClassName)){
			model.addAttribute("error", "用户名不存在!");
			LOG.info("Wrong username or password!");
			return "login";
		}else if(LockedAccountException.class.getName().equals(exceptionClassName) && !StringUtils.isEmpty(exceptionClassName)){
			model.addAttribute("error", "账号已锁定或失效，请联系管理员!");
			LOG.info("Account Lock!");
			return "login";
		}else if(IncorrectCredentialsException.class.getName().equals(exceptionClassName) && !StringUtils.isEmpty(exceptionClassName)){
			model.addAttribute("error", "用户名或密码错误!");
			LOG.info("Login Exception!");
			return "login";
		}else{
			List<Resource> menus = resourceService.findMenus(userService.findPermissions(getCurrentUser().getUsername()));
			String json = JsonUtils.object2Json(menus);
			req.getSession().setAttribute("menus", json);
		}
		LOG.info("Login success");
		return "login";
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest req, Model model) {
		LOG.info("Inside logout");
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
			LOG.info("Logout from car rental portal");
			if (LOG.isDebugEnabled()) {
				LOG.debug("退出登录");
			}
		}
		return "login";
	}
	@RequestMapping(value = "/password/forgetPassword")
	public String forgetPassword(HttpServletRequest req) {
		return "getpass_index";
	}
	@RequestMapping(value = "/password/changePassword", method = RequestMethod.POST)
	public String changePassword(String newPassword,HttpServletRequest request){
		User user=(User) request.getSession().getAttribute("user");
			userService.changePassword(user.getId(), newPassword);
//			request.getSession().removeAttribute("user");
			return "getpass_resetSuccess";
	}
	
}
