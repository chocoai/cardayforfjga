package com.cmdt.carrental.portal.web.shiro.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.portal.Constants;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class SysUserFilter extends PathMatchingFilter {
	private static final Logger LOG = LoggerFactory.getLogger(SysUserFilter.class);
	
    @Autowired
    private UserService userService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

    	String username = (String)SecurityUtils.getSubject().getPrincipal();
        User user = null;
        try{
        	user = userService.findByUsername(username);
        	if(user!=null){
            	if(((HttpServletRequest)request).getSession().getAttribute(Constants.CURRENT_USER)==null){
    	         ((HttpServletRequest)request).getSession().setAttribute(Constants.CURRENT_USER, user);
            	}
    	        request.setAttribute(Constants.CURRENT_USER, user);
            }
        }catch(Exception e){
        	LOG.error("PathMatchingFilter query user error:",e);
        }
        return true;
    }
}
