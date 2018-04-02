package com.cmdt.carrental.portal.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.UserService;

public class UserRealm extends AuthorizingRealm {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private UserService userService;
    @Autowired
    private OrganizationService orgService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String)principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        try{
         authorizationInfo.setRoles(userService.findRoles(username));
         authorizationInfo.setStringPermissions(userService.findPermissions(username));
        }catch(Exception e){
        	LOG.error("UserRealm doGetAuthorizationInfo error:", e);
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

    	LOG.info("===========begin AuthenticationInfo=======================");
        String username = (String)token.getPrincipal(); //得到用户名
    //    String password=new String((char[])token.getCredentials());//得到密码

        User user = userService.findByUsername(username);

        if(user == null) {
        	LOG.info("===========Account Auth : username is not valid=======================");
            
            //check mobile when username is not valid
            user = userService.findByPhoneNumber(username);
            if(user == null) {
            	LOG.info("===========Account Auth : phonenumber is not valid=======================");
            
            	//check email when others are not valid
            	user = userService.findByEmail(username);
                if(user == null) {
                	LOG.info("===========Account Auth : email is not valid=======================");
                    throw new UnknownAccountException();//没找到帐号
                }
            } 
        }
        
        //check user org status
        if(user.getOrganizationId() != null){
        	Organization org = findRootOrg(user.getOrganizationId());
        	if(!"3".equals(org.getStatus())){
        		throw new LockedAccountException();//帐号锁定
        	}
        }

        if(Boolean.TRUE.equals(user.getLocked())) {
        	LOG.info("===========UnknownAccountException=======================");
            throw new LockedAccountException(); //帐号锁定
        }

        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user.getUsername(), //用户名
                user.getPassword(), //密码
                ByteSource.Util.bytes(user.getCredentialsSalt()),//salt=username+salt
                getName()  //realm name
        );
        LOG.info("===========success=======================");
        return authenticationInfo;
    }
    
    
    private Organization findRootOrg(Long orgId){
    	Organization org = orgService.findById(orgId);
    	if(org.getParentId() != 0){
    		org = findRootOrg(org.getParentId());
    	}
    	return org; 
    }

    @Override
    public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
    }

    @Override
    public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
    }

    @Override
    public void clearCache(PrincipalCollection principals) {
        super.clearCache(principals);
    }

    public void clearAllCachedAuthorizationInfo() {
        getAuthorizationCache().clear();
    }

    public void clearAllCachedAuthenticationInfo() {
        getAuthenticationCache().clear();
    }

    public void clearAllCache() {
        clearAllCachedAuthenticationInfo();
        clearAllCachedAuthorizationInfo();
    }

}
