package com.cmdt.carrental.mobile.gateway.api.impl;

import java.util.Calendar;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.cmdt.carrental.common.entity.Employee;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.mobile.gateway.api.inf.ILoginService;
import com.cmdt.carrental.mobile.gateway.common.Constants;
import com.cmdt.carrental.mobile.gateway.common.WsResponse;
import com.cmdt.carrental.mobile.gateway.model.LoginDto;
import com.cmdt.carrental.mobile.gateway.model.LoginUserInfo;

public class LoginServiceImpl implements ILoginService {

	private static final Logger LOG = LoggerFactory.getLogger(LoginServiceImpl.class);
	
	@Value("${password.algorithmName}")
    private String algorithmName = "md5";
	@Value("${password.hashIterations}")
    private int hashIterations = 2;
	@Autowired
	private UserService userService;
	@Autowired
	OrganizationService organizationService;
	
	@Override
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(LoginDto loginDto) {
		WsResponse<?> wsResponse = new WsResponse<>();
		try{
			User user = null;
			String phone = loginDto.getMobile();
		
			if (!loginDto.getAppType().equals("CarRental")) {
				wsResponse.setStatus(Constants.API_STATUS_PARAM_ILLEGAL);
				wsResponse.getMessages().add(Constants.API_MESSAGE_PARAM_ILLEGAL);
				return Response.ok( wsResponse ).build();
			}
			
			if (!(loginDto.getPlatformType().equals("android")||loginDto.getPlatformType().equals("ios"))) {
				wsResponse.setStatus(Constants.API_STATUS_PARAM_ILLEGAL);
				wsResponse.getMessages().add(Constants.API_MESSAGE_PARAM_ILLEGAL);
				return Response.ok( wsResponse ).build();
			}
	
			if (userService.phoneIsValid(phone)) {
				//手机号码不存在
				wsResponse.setStatus(Constants.PHONENO_NOT_EXIST);
				wsResponse.getMessages().add("该手机号在系统中不存在");
				return Response.ok( wsResponse ).build();
			} else {
				user = userService.findByPhoneNumber(phone);
				Long entId = userService.getEntId(user.getOrganizationId());
				//组织机构状态：0:待审核  1:审核不通过 2:待服务开通 3：服务中 4:服务到期 5： 服务暂停
				//企业不在服务器的用户，不允许登录
				Organization organization = organizationService.findOne(entId);
				if (organization != null && !"3".equals(organization.getStatus())) {
					wsResponse.setStatus(Constants.PHONENO_LOGIN_PASSWORD_ERROR);
					wsResponse.getMessages().add("该用户所在企业不在服务中，请联系系统管理员！");
					return Response.ok( wsResponse ).build();
				}
				String loginPassword = new SimpleHash(
		                algorithmName,
		                loginDto.getPassword(),
		                ByteSource.Util.bytes(user.getCredentialsSalt()),
		                hashIterations).toHex();
				
				if (!loginPassword.equals(user.getPassword())) {
					//手机号存在，密码不正确
					wsResponse.setStatus(Constants.PHONENO_LOGIN_PASSWORD_ERROR);
					wsResponse.getMessages().add("密码不正确");
					return Response.ok( wsResponse ).build();
				} else {
					String accessToken = new SimpleHash(
			                algorithmName,
			                String.valueOf(Calendar.getInstance().getTimeInMillis()),
			                ByteSource.Util.bytes(user.getCredentialsSalt()),
			                hashIterations).toHex();
					userService.updateAccessToken(user.getId(), accessToken);
					
					//手机号存在，密码正确
					LoginUserInfo userInfo = new LoginUserInfo();
					WsResponse<LoginUserInfo> wsResponse1 = new WsResponse<LoginUserInfo>(Constants.STATUS_SUCCESS, userInfo);
					wsResponse1.getMessages().add("处理成功");
					userInfo.setUserId(user.getId());
					userInfo.setUserName(user.getUsername());
					userInfo.setRealName(user.getRealname());
					userInfo.setMobile(user.getPhone());
					userInfo.setEmail(user.getEmail());
					//新建司机没有角色，约定存入数据库roleId=-1,在roleId表中没有对应的记录，获取userCategory为null
					if (user.getUserCategory() == null) {
						user.setUserCategory(5L);
					}
					userInfo.setUserCategory(user.getUserCategory());
					userInfo.setAccessToken(accessToken);
					
					if(entId != null){
						String entName = userService.getEntName(user.getOrganizationId());
						Long deptId = userService.getDeptId(user.getId(), user.getOrganizationId(), user.getUserCategory());
						String deptName = userService.getDeptName(user.getId(), user.getOrganizationId(), user.getUserCategory());
						userInfo.setEntId(entId);
						userInfo.setEntName(entName);
						userInfo.setDeptId(deptId);
						userInfo.setDeptName(deptName);
					}
					
					//查找并判断此用户的月额度和剩余额度
					Employee employee = userService.findEmployeeByUserId(user.getId());
					if(null != employee){
					    userInfo.setMonthLimitvalue(employee.getMonthLimitvalue());
	                    userInfo.setMonthLimitLeft(employee.getMonthLimitLeft());
					}
					return Response.ok( wsResponse1 ).build();
				}
			}
		}catch(Exception e){
			LOG.error("login error",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
        return Response.ok( wsResponse ).build();
	}
}
