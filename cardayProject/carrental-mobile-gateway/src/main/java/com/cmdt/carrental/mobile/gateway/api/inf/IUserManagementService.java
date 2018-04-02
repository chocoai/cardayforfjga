package com.cmdt.carrental.mobile.gateway.api.inf;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cmdt.carrental.common.util.Patterns;
import com.cmdt.carrental.mobile.gateway.model.DriverDto;
import com.cmdt.carrental.mobile.gateway.model.LoginDto;
import com.cmdt.carrental.mobile.gateway.model.SetPasswordDto;
import com.cmdt.carrental.mobile.gateway.model.VerifyCodeDto;
import com.cmdt.carrental.mobile.gateway.model.request.user.ChangePasswordDto;
import com.cmdt.carrental.mobile.gateway.model.request.user.ChangeUserInfo;
import com.cmdt.carrental.mobile.gateway.model.request.user.ForgetPasswordDto;
import com.cmdt.carrental.mobile.gateway.model.request.user.ResetPasswordDto;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.VehicleParamDto;

@Produces(MediaType.APPLICATION_JSON)
public interface IUserManagementService {
	
	public Response login(@Valid @NotNull LoginDto login);
	
	public Response sendCode(@NotNull 
	                         @Pattern(regexp = Patterns.REG_MOBILE_PHONE,message="Phone number format error!")
	                         String phone);
	
	public Response verifyCode(@Valid @NotNull VerifyCodeDto verifyCode);
	
	public Response setPassword(@Valid @NotNull SetPasswordDto changePassword);
	
	public Response changePassword(@Valid @NotNull ChangePasswordDto changePassword);
	
	public Response forgetPassword(@Valid @NotNull ForgetPasswordDto forgetPasswordDto);
	
	public Response changeUserInfo(@Valid @NotNull ChangeUserInfo user);
	
	public Response getVerifyCode(@Valid @NotNull ResetPasswordDto resetPasswordDto);
	
	public Response checkVerifyCode(@Valid @NotNull ResetPasswordDto resetPasswordDto);

	public Response viewDriver(@Valid @NotNull DriverDto dto);
	
	public Response queryBusiOrderByVehicleId(@Valid @NotNull VehicleParamDto dto);
	
	public Response requestAuthCodeForReg(ResetPasswordDto resetPasswordDto);
	public Response checkAuthCodeForReg(ResetPasswordDto resetPasswordDto);
	
	public Response changePhoneByUsername(
			@Valid
			@NotNull
			@Digits(message="userId格式错误", fraction = 0, integer = 10)
			Long userId,
			@Valid
            @NotNull 
            @Pattern(regexp = Patterns.REG_MOBILE_PHONE,message="Phone number format error!")
            String phone
			);
}
