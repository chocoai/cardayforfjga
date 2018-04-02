package com.cmdt.carrental.mobile.gateway.api.impl;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.util.Patterns;
import com.cmdt.carrental.mobile.gateway.api.inf.IUserManagementService;
import com.cmdt.carrental.mobile.gateway.common.Constants;
import com.cmdt.carrental.mobile.gateway.common.WsResponse;
import com.cmdt.carrental.mobile.gateway.model.DriverDto;
import com.cmdt.carrental.mobile.gateway.model.LoginDto;
import com.cmdt.carrental.mobile.gateway.model.SetPasswordDto;
import com.cmdt.carrental.mobile.gateway.model.VerifyCodeDto;
import com.cmdt.carrental.mobile.gateway.model.request.user.ChangePasswordDto;
import com.cmdt.carrental.mobile.gateway.model.request.user.ChangeUserInfo;
import com.cmdt.carrental.mobile.gateway.model.request.user.ForgetPasswordDto;
import com.cmdt.carrental.mobile.gateway.model.request.user.ResetPasswordDto;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.VehicleParamDto;
import com.cmdt.carrental.mobile.gateway.service.TestService;
import com.cmdt.carrental.mobile.gateway.service.UserAppService;
import com.cmdt.carrental.mobile.gateway.util.WsUtil;

public class UserManagementService implements IUserManagementService {
	
	private static final Logger LOG = Logger.getLogger(UserManagementService.class);

	
	@Autowired
	private TestService testService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserAppService userAppService;
	
	@Override
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response login(LoginDto login) {
		WsResponse<?> wsResponse = new WsResponse<>();;
		boolean loginStatus = false;
		
		List<String> users = testService.querUser();
		
		for(String s : users){
			System.out.println(s);
		}
		
//		try{
//		int i = Integer.parseInt(login.getUsername());
//		}catch(Exception e){
//			throw new ServerException(e.getMessage());
//		}
//		
//		User user  = userService.findByUsername(login.getUsername());
//		if(null!=user&&user.getPassword()!=null){
//		   loginStatus = userService.isValidPassword(login.getPassword(), user);
//		}
//		
//		if(loginStatus){	
//			UserInfo userInfo = new UserInfo(user.getUsername(),user.getRoleName(),user.getRoleId(),user.getOrganizationName(),user.getPhone(),user.getEmail());
//			wsResponse = new WsResponse<>(Constants.STATUS_SUCCESS,userInfo);
//			return Response.ok( wsResponse ).build();
//		}
//		else{
//			wsResponse = new WsResponse<>(Constants.STATUS_FAILURE,login);
//			wsResponse.getMessages().add("用户名或密码错误！");
//		}
		
		return Response.ok( wsResponse ).build();
	}

	@Override
	@POST
	@Path("/sendCode/{phone}")
	public Response sendCode(@PathParam("phone")String phone) {
		WsResponse<?> wsResponse;
		int code = WsUtil.nextInt(100000, 999999);
		wsResponse = new WsResponse<>();
		// TODO send SMS to destination phone number
		// TODO Store verify code &　Phone number in DB
		return Response.ok( wsResponse ).build();
	}

	@Override
	@POST
	@Path("/verify")
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response verifyCode(VerifyCodeDto verifyCode) {
		WsResponse<?> wsResponse;
		//TODO query verify code by phone number from DB
		//TODO check code
		boolean flag = false;
		return Response.ok( new WsResponse<>()).build();
	}

	@Override
	@POST
	@Path("/setPassword")
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response setPassword(SetPasswordDto changePassword) {
/*		User user  = userService.findByUsername(changePassword.getUsername());
		userService.changePassword(user.getId(),changePassword.getPassword());*/
		return Response.ok().build();
	}

	@Override
	@POST
	@Path("/changePassword")
//	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response changePassword(ChangePasswordDto changePassword) {
		LOG.info("++++changePassword++++");
		WsResponse<ChangePasswordDto> wsResponse = new WsResponse<ChangePasswordDto>();
		wsResponse = userAppService.changePassword(changePassword);
		return Response.ok( wsResponse ).build();
	}
	
	
	@Override
	@POST
	@Path("/forgetPassword")
//	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response forgetPassword(ForgetPasswordDto forgetPasswordDto) {
		LOG.info("++++changePassword++++");
		WsResponse wsResponse = new WsResponse();
		wsResponse = userAppService.forgetPassword(forgetPasswordDto);
		return Response.ok( wsResponse ).build();
	}
	
	@Override
	@POST
	@Path("/changeUserInfo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response changeUserInfo(ChangeUserInfo user) {
		LOG.info("++++changeUserInfo++++");
		WsResponse<ChangeUserInfo> wsResponse = new WsResponse<ChangeUserInfo>();
		wsResponse.setStatus(Constants.STATUS_SUCCESS);
		Long userId = user.getUserId();
		String phone = user.getMobile();
		String email = user.getEmail();
		String realName = user.getRealName();
		wsResponse = userAppService.updateUserInfo(user);
		return Response.ok( wsResponse ).build();
	}
	
	@Override
	@POST
	@Path("/getVerifyCode")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response getVerifyCode(ResetPasswordDto resetPasswordDto) {
		WsResponse<ResetPasswordDto> wsResponse = new WsResponse<ResetPasswordDto>();
		String pattern = "^[1][0-9]{10}$";
		if(resetPasswordDto.getPhoneNumber().matches(pattern)) {
			String phoneNumber = resetPasswordDto.getPhoneNumber();
			if (userService.phoneIsValid(phoneNumber)) {
				//手机号码不存在
				wsResponse.setStatus(Constants.PHONENO_NOT_EXIST);
				wsResponse.getMessages().add("该手机号在系统中不存在");
				return Response.ok( wsResponse ).build();
			} 
			
			if(userAppService.getVerifyCode(phoneNumber)){
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			}else{
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
				wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
			}
		} else {
			wsResponse.setStatus(Constants.API_STATUS_PARAM_ILLEGAL);
			wsResponse.getMessages().add(Constants.API_MESSAGE_PARAM_ILLEGAL);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@Override
	@POST
	@Path("/checkVerifyCode")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response checkVerifyCode(ResetPasswordDto resetPasswordDto) {
		WsResponse<ResetPasswordDto> wsResponse = new WsResponse<ResetPasswordDto>();
		String pattern = "^[1][0-9]{10}$";
		if(resetPasswordDto.getPhoneNumber().matches(pattern)) {
			//String phoneNumber = resetPasswordDto.getPhoneNumber();
			//Map<String, Object> result = new HashMap<String, Object>();
			String msg = userAppService.checkVerifyCode(resetPasswordDto).get("status").toString();
			
			switch(msg) {
				case "success" :
					wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
					wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
					break;
				case "phoneIllegal" :
					wsResponse.setStatus(Constants.API_STATUS_FAILURE);
					wsResponse.getMessages().add("该手机号未被注册");
					break;
				case "codeExpired" :
					wsResponse.setStatus(Constants.API_STATUS_FAILURE);
					wsResponse.getMessages().add("验证码已过期");
					break;
				case "codeError" :
					wsResponse.setStatus(Constants.API_STATUS_FAILURE);
					wsResponse.getMessages().add("验证码错误");
					break;
			}
		} else {
			wsResponse.setStatus(Constants.API_STATUS_PARAM_ILLEGAL);
			wsResponse.getMessages().add(Constants.API_MESSAGE_PARAM_ILLEGAL);
		}
		return Response.ok( wsResponse ).build();
	}

	@Override
	@POST
	@Path("/view/driver")
	public Response viewDriver(DriverDto dto) {
		WsResponse<DriverModel> wsResponse = new WsResponse<DriverModel>();
		try{
			DriverModel driver=userAppService.viewDriver(dto);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setResult(driver);
		}catch(Exception e){
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@Override
	@POST
	@Path("/queryBusiOrderByVehicleId")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response queryBusiOrderByVehicleId(VehicleParamDto dto) {
		WsResponse wsResponse = new WsResponse();
		wsResponse = userAppService.queryBusiOrderByVehicleId(dto);
		return Response.ok( wsResponse ).build();
	}
	
	
	@Override
	@POST
	@Path("/authCode")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response requestAuthCodeForReg(ResetPasswordDto resetPasswordDto) {
		WsResponse<ResetPasswordDto> wsResponse = new WsResponse<ResetPasswordDto>();
		String pattern = "^[1][0-9]{10}$";
		if(resetPasswordDto.getPhoneNumber().matches(pattern)) {
			String phoneNumber = resetPasswordDto.getPhoneNumber();
			String msg = userAppService.getRegCode(phoneNumber);
			if(msg.equals(Constants.API_STATUS_SUCCESS)){
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			}else{
				wsResponse.getMessages().add(msg);
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
				wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
			}
		} else {
			wsResponse.setStatus(Constants.API_STATUS_PARAM_ILLEGAL);
			wsResponse.getMessages().add(Constants.API_MESSAGE_PARAM_ILLEGAL);
		}
		return Response.ok( wsResponse ).build();
	}
	
	
	
	@Override
	@POST
	@Path("/checkAuthCode")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response checkAuthCodeForReg(ResetPasswordDto resetPasswordDto) {
		WsResponse<ResetPasswordDto> wsResponse = new WsResponse<ResetPasswordDto>();
		String pattern = "^[1][0-9]{10}$";
		if(resetPasswordDto.getPhoneNumber().matches(pattern)) {
			String msg = userAppService.checkVerifyCodeForReg(resetPasswordDto).get("status").toString();
			switch(msg) {
				case "success" :
					wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
					wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
					break;
				case "codeExpired" :
					wsResponse.setStatus(Constants.API_STATUS_FAILURE);
					wsResponse.getMessages().add("验证码已过期");
					break;
				case "codeError" :
					wsResponse.setStatus(Constants.API_STATUS_FAILURE);
					wsResponse.getMessages().add("验证码错误");
					break;
			}
		} else {
			wsResponse.setStatus(Constants.API_STATUS_PARAM_ILLEGAL);
			wsResponse.getMessages().add(Constants.API_MESSAGE_PARAM_ILLEGAL);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@Override
	@POST
	@Path("/changePhone/{userid}/{phone}")
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response changePhoneByUsername(@PathParam("userid") Long userId, @PathParam("phone") String phone){
		WsResponse<String> wsResponse = new WsResponse<String>();
		String msg = userAppService.changePhoneByUsername(userId,phone);
		try{
			if(msg!=null&&!msg.equals(Constants.API_STATUS_SUCCESS)){
				wsResponse.getMessages().add(msg);
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
				wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
			}else{
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			}
		}catch(Exception e){
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
}
