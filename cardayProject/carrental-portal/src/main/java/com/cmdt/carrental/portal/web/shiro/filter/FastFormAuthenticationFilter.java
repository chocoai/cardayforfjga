package com.cmdt.carrental.portal.web.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.portal.Constants;

public class FastFormAuthenticationFilter extends FormAuthenticationFilter{
	
	 @Override
	 protected boolean onLoginSuccess(AuthenticationToken token,Subject subject,ServletRequest request,ServletResponse response) throws Exception{
		 WebUtils.issueRedirect(request, response, getSuccessUrl());
		 return false;
	 }
	
	 
	 @Override
	 protected boolean  onAccessDenied(ServletRequest request,ServletResponse response) throws Exception {
		 HttpServletRequest req = WebUtils.toHttp(request);
	        String xmlHttpRequest = req.getHeader("X-Requested-With");
	        if ( xmlHttpRequest != null )
	            if ( xmlHttpRequest.equalsIgnoreCase("XMLHttpRequest") )  {
	                HttpServletResponse res = WebUtils.toHttp(response);
	                User user = (User)((HttpServletRequest)request).getSession().getAttribute(Constants.CURRENT_USER);
	                if(user == null){
	                	res.addHeader("timeout", "true");
	                }
	                res.sendError(401);
	                return false;
	        }
	    
	        return super.onAccessDenied(request, response);
	    }	
}
