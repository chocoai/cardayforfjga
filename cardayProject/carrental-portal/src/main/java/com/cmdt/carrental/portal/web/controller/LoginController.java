package com.cmdt.carrental.portal.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.ResourceService;
import com.cmdt.carrental.common.service.UserService;

@Controller
public class LoginController extends BaseController {

	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	public ResourceService resourceService;

	@Autowired
	public UserService userService;
	@Autowired
	private OrganizationService orgService;

	@RequestMapping(value = "/login")
	// @ResponseBody
	public String showLoginForm(HttpServletRequest req, Model model) {
		LOG.info("Inside login");
		String exceptionClassName = (String) req.getAttribute("shiroLoginFailure");
		if (UnknownAccountException.class.getName().equals(exceptionClassName)
				&& !StringUtils.isEmpty(exceptionClassName)) {
			model.addAttribute("error", "用户名不存在!");
			LOG.info("Wrong username or password!");
			return "login";
		} else if (LockedAccountException.class.getName().equals(exceptionClassName)
				&& !StringUtils.isEmpty(exceptionClassName)) {
			model.addAttribute("error", "账号已锁定或失效，请联系管理员!");
			LOG.info("Account Lock!");
			return "login";
		} else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)
				&& !StringUtils.isEmpty(exceptionClassName)) {
			model.addAttribute("error", "用户名或密码错误!");
			LOG.info("Login Exception!");
			return "login";
		} else {
//			List<Resource> menus = resourceService
//					.findMenus(userService.findPermissions(getCurrentUser().getUsername()));
//			String json = JsonUtils.object2Json(menus);
//			req.getSession().setAttribute("menus", json);
			 //fix bug CR-1391 begin 	在浏览器中打开歌华登录页两次
			 Subject currentUser = SecurityUtils.getSubject(); 
			 if(currentUser.isAuthenticated()){
				 return "forward:/";
			 }
			 //fix bug end CR-1391
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
	
	/**
	 * specific logout for gehua
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/ghlogout")
	public String ghlogout(HttpServletRequest req, Model model) {
		LOG.info("Inside ghlogout");
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
			LOG.info("gh Logout from car rental portal");
			if (LOG.isDebugEnabled()) {
				LOG.debug("退出登录");
			}
		}
		return "ghlogin";
	}
	
	@RequestMapping(value = "/password/forgetPassword")
	public String forgetPassword(HttpServletRequest req,String flag) {
		if(flag.equals("gh")){
			return "gehua_login/gh_getpass_index";
		}else{
			return "getpass_index";
		}
	}

	@RequestMapping(value = "/password/changePassword", method = RequestMethod.POST)
	public String changePassword(String newPassword,String flag,HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		userService.changePassword(user.getId(), newPassword);
		// request.getSession().removeAttribute("user");
		if (flag.equals("gh")) {
			return "gehua_login/gh_getpass_resetSuccess";
		}else{
			return "getpass_resetSuccess";
		}
	}

	private Organization findRootOrg(Long orgId) {
		Organization org = orgService.findById(orgId);
		if (org.getParentId() != 0) {
			org = findRootOrg(org.getParentId());
		}
		return org;
	}

	@RequestMapping(value = "/validateUser", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> validateUser(String username, String password, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("status", true);
		try {
			User user = userService.findByUsername(username);
			if (user == null) {
				// check mobile when username is not valid
				user = userService.findByPhoneNumber(username);
				if (user == null) {
					user = userService.findByEmail(username);
					if (user == null) {
						result.put("status", false);
						result.put("data", "账户不存在");
					}
				}
			}
			if (user.getOrganizationId() != null) {
				Organization org = findRootOrg(user.getOrganizationId());
				if (!"3".equals(org.getStatus())) {
					result.put("status", false);
					result.put("data", "账户被锁定");
				}
			}
			if (Boolean.TRUE.equals(user.getLocked())) {
				result.put("status", false);
				result.put("data", "账户被锁定");
			}

			// check user org status
			if (user.getOrganizationId() != null) {
				Organization org = findRootOrg(user.getOrganizationId());
				if (!"3".equals(org.getStatus())) {
					result.put("status", false);
					result.put("data", "账户被锁定");
				}
			}

			if (Boolean.TRUE.equals(user.getLocked())) {
				result.put("status", false);
				result.put("data", "账户被锁定");
			}

			String loginPassword = new SimpleHash("md5", password, ByteSource.Util.bytes(user.getCredentialsSalt()), 2)
					.toHex();

			if (!loginPassword.equals(user.getPassword())) {
				result.put("status", false);
				result.put("data", "密码错误");
			}
		} catch (Exception e) {
			LOG.error("Captcha Controller checkVertificationCode error, cause by:", e);
			result.put("status", false);
		}
		return result;
	}
}