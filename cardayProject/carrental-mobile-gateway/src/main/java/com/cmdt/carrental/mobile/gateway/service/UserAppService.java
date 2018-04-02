package com.cmdt.carrental.mobile.gateway.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.cache.RedisService;
import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.Employee;
import com.cmdt.carrental.common.entity.PhoneVerificationCode;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.service.BusiOrderService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.mobile.gateway.common.Constants;
import com.cmdt.carrental.mobile.gateway.common.WsResponse;
import com.cmdt.carrental.mobile.gateway.model.DriverDto;
import com.cmdt.carrental.mobile.gateway.model.request.user.ChangePasswordDto;
import com.cmdt.carrental.mobile.gateway.model.request.user.ChangeUserInfo;
import com.cmdt.carrental.mobile.gateway.model.request.user.ForgetPasswordDto;
import com.cmdt.carrental.mobile.gateway.model.request.user.ResetPasswordDto;
import com.cmdt.carrental.mobile.gateway.model.request.user.UserDto;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.VehicleParamDto;
import com.cmdt.carrental.mobile.gateway.util.PatternCheck;

@Service
public class UserAppService
{
    
	private static final Logger LOG = LoggerFactory.getLogger(UserAppService.class);
	
    @Autowired
    private UserService userService;
    
    @Autowired
    public ShouqiService shouqiService;
    
    @Autowired
    public RedisService redisService;
    
    @Autowired
    private BusiOrderService busiOrderService;
    
    @Autowired
    private CommunicationService commService;
    
    public WsResponse<ChangePasswordDto> changePassword(ChangePasswordDto changePassword)
    {
        WsResponse<ChangePasswordDto> wsResponse = new WsResponse<ChangePasswordDto>();
        Long userId = changePassword.getUserId();
        String oldPasswd = changePassword.getOldPasswd();
        User loginUser = userService.findOne(userId);
        if (loginUser == null)
        {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add("该用户不存在");
            return wsResponse;
        }
        if (userService.isValidPassword(oldPasswd, loginUser))
        {
            String newPasswd = changePassword.getNewPasswd();
            userService.changePassword(loginUser.getId(), newPasswd);
            wsResponse.setStatus(Constants.STATUS_SUCCESS);
            wsResponse.getMessages().add(Constants.MESSAGE_SUCCESS);
        }
        else
        {
            wsResponse.setStatus(Constants.API_MESSAGE_FAILURE);
            wsResponse.getMessages().add("旧密码不正确");
        }
        return wsResponse;
    }
    
    public WsResponse forgetPassword(ForgetPasswordDto forgetPasswordDto)
    {
        WsResponse wsResponse = new WsResponse();
        String mobile = forgetPasswordDto.getMobile();
        String password = forgetPasswordDto.getPassword();
        User user = userService.findByPhoneNumber(mobile);
        if (user != null)
        {
            userService.changePassword(user.getId(), password);
            wsResponse.setStatus(Constants.STATUS_SUCCESS);
            wsResponse.getMessages().add(Constants.MESSAGE_SUCCESS);
        }
        else
        {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add("该手机号未注册");
            return wsResponse;
        }
        return wsResponse;
    }
    
    @SuppressWarnings("unchecked")
    public Boolean getVerifyCode(String phoneNumber)
    {
    	Boolean retValue = false;
        int num = (int)((Math.random() * 9 + 1) * 100000);
        String randNum = String.valueOf(num);
		//send sms by sjjl
        
        String response = commService.sendSms("您在CarDay请求的验证码是: " + randNum, phoneNumber);
		Map<String, Object> map = JsonUtils.json2Object(response, Map.class);
        /*Map<String, Object> map = new ServiceAdapter(shouqiService).doService(ActionName.SMSMESSAGE,
            new Object[] {phoneNumber, "您在CarDay请求的验证码是: " + randNum});*/
        if (map.get("statusCode").equals("20000"))
        {
            PhoneVerificationCode code = new PhoneVerificationCode();
            code.setCode(randNum);
            code.setPhoneNumber(phoneNumber);
            code.setExpirationTime(new java.sql.Timestamp((DateUtils.addMinutes(new Date(), 1)).getTime()));
            if (userService.getCode(phoneNumber) != null)
            {
                userService.updateCode(code);
            }
            else
            {
                userService.saveCode(code);
            }
            retValue = true;
            // redisService.set(phoneNumber, randNum, 60);
        }
        else if (map.get("status").equals("failure"))
        {
//            System.out.println("短信发送失败！");
            retValue = false;
        }
        
        return retValue;
    }
    
    
    
    public String getRegCode(String phoneNumber)
    {

        int num = (int)((Math.random() * 9 + 1) * 100000);
        String randNum = String.valueOf(num);
        
        User user = userService.findByPhoneNumber(phoneNumber);
        
        if(null!=user){
        	return "手机号已被使用！";
        }
        
		//send sms by sjjl
        String response = commService.sendSms("您在CarDay请求的验证码是: " + randNum, phoneNumber);
		Map<String, Object> map = JsonUtils.json2Object(response, Map.class);
        if (map.get("statusCode").equals("20000"))
        {
            PhoneVerificationCode code = new PhoneVerificationCode();
            code.setCode(randNum);
            code.setPhoneNumber(phoneNumber);
            code.setExpirationTime(new java.sql.Timestamp((DateUtils.addMinutes(new Date(), 1)).getTime()));
            if (userService.getCode(phoneNumber) != null)
            {
                userService.updateCode(code);
            }
            else
            {
                userService.saveCode(code);
            }
           return Constants.API_STATUS_SUCCESS;
        }
        else if (map.get("status").equals("failure"))
        {
//            System.out.println("短信发送失败！");
           return "验证码发送失败！";
        }
        
        return Constants.API_STATUS_SUCCESS;
    }
    
    
    // 验证码验证
    public Map<String, Object> checkVerifyCode(ResetPasswordDto resetPasswordDto)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        User user = userService.findByPhoneNumber(resetPasswordDto.getPhoneNumber());
        if (user == null)
        {
            result.put("status", "phoneIllegal");
            result.put("error", "该手机号未被注册");
            return result;
        }
        
        PhoneVerificationCode phoneCode = userService.checkCode(resetPasswordDto.getPhoneNumber());
        // String phoneCode=redisService.get(phoneNumber);
        if (phoneCode != null && phoneCode.getCode().equals(resetPasswordDto.getVerifyCode()))
        {
            result.put("status", "success");
        }
        else if (phoneCode == null)
        {
            result.put("status", "codeExpired");
            result.put("error", "验证码已失效");
        }
        else
        {
            result.put("status", "codeError");
            result.put("error", "验证码错误");
        }
        return result;
    }
    
    // 验证码验证
    public Map<String, Object> checkVerifyCodeForReg(ResetPasswordDto resetPasswordDto)
    {
        Map<String, Object> result = new HashMap<String, Object>();
        PhoneVerificationCode phoneCode = userService.checkCode(resetPasswordDto.getPhoneNumber());
        // String phoneCode=redisService.get(phoneNumber);
        if (phoneCode != null && phoneCode.getCode().equals(resetPasswordDto.getVerifyCode()))
        {
            result.put("status", "success");
        }
        else if (phoneCode == null)
        {
            result.put("status", "codeExpired");
            result.put("error", "验证码已失效");
        }
        else
        {
            result.put("status", "codeError");
            result.put("error", "验证码错误");
        }
        return result;
    }
    
    
    public String changePhoneByUsername(Long userId,String phone){
    	
    	User user = userService.findByPhoneNumber(phone);
    	
    	if(user!=null){
    		return "手机号以被使用！";
    	}
    	
    	user = userService.findById(userId);
    	
    	if(user == null){
    		return "用户不存在！";
    	}
    	
    	if(user.getPhone().equals(phone)){
    		return "新号码和旧号码相同！";
    	}
    	
    	user.setPhone(phone);
    	userService.updateUser(user);
    	return Constants.API_STATUS_SUCCESS;
    }
    
    public WsResponse<ChangeUserInfo> updateUserInfo(ChangeUserInfo user)
    {
        WsResponse<ChangeUserInfo> wsResponse = new WsResponse<ChangeUserInfo>();
        // userId有效性
        User userVolidate = userService.findOne(user.getUserId());
        wsResponse.setStatus(Constants.API_STATUS_FAILURE);
        if (userVolidate == null)
        {
            wsResponse.getMessages().add("该用户不存在");
            return wsResponse;
        }
        
        // 手机号校验
        if (StringUtils.isNotEmpty(user.getMobile()))
        {
            if (!PatternCheck.isMobileNO(user.getMobile()))
            {
                wsResponse.getMessages().add("手机号格式错误");
                return wsResponse;
            }
            
            if (!user.getMobile().equals(userVolidate.getPhone()))
            {
                User temp = userService.findByPhoneNumber(user.getMobile());
                if (temp != null)
                {
                    wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                    wsResponse.getMessages().add("该联系电话已被使用");
                    return wsResponse;
                }
            }
        }
        
        // 邮箱校验
        if (StringUtils.isNotEmpty(user.getEmail()))
        {
            // if(!PatternCheck.isEmail(user.getEmail())) {
            // wsResponse.getMessages().add("邮箱格式错误");
            // return wsResponse;
            // }
            
            if (!user.getEmail().equals(userVolidate.getEmail()))
            {
                User temp = userService.findByEmail(user.getEmail());
                if (temp != null)
                {
                    wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                    wsResponse.getMessages().add("该邮箱已被使用");
                    return wsResponse;
                }
            }
        }
        
        int result =
            userService.updateUserInfoApp(user.getUserId(), user.getMobile(), user.getEmail(), user.getRealName());
        if (result == 1)
        {
            wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
            wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            User loginUser = userService.findOne(user.getUserId());
            user.setUserName(loginUser.getUsername());
            user.setRealName(loginUser.getRealname());
            user.setMobile(loginUser.getPhone());
            user.setEmail(loginUser.getEmail());
            wsResponse.setResult(user);
        }
        else
        {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
        }
        return wsResponse;
    }
    
    public DriverModel viewDriver(DriverDto dto)
    {
        Long id = dto.getId();
        return userService.findDriverModel(id);
    }
    
    public WsResponse queryBusiOrderByVehicleId(VehicleParamDto dto)
    {
        WsResponse wsResponse = new WsResponse();
        BusiOrder busiOrder = busiOrderService.queryBusiOrderByVehicleId(dto.getCarId());
        wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
        wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
        wsResponse.setResult(busiOrder);
        return wsResponse;
    }
    
    // TODO only for 2c registration
    public String regClient(UserDto dto)
    {
        String errormsg = "";
        User user = new User();
        user.setUsername(dto.getUserName());
        user.setPassword(dto.getPassword());
        // 4: enduser/passenger
        user.setRoleId(6l);
        if (dto.getOrganizationId() != null)
        {
            user.setOrganizationId(dto.getOrganizationId());
        }
        else
        {
            user.setOrganizationId(0l);
        }
        user.setRealname(dto.getRealName());
        user.setPhone(dto.getMobile());
        user.setEmail(dto.getEmail());
        
        if (!userService.usernameIsValid(user.getUsername()))
        {
            errormsg = "用户名已被使用";
            return errormsg;
        }
        
        if (!userService.phoneIsValid(user.getPhone()))
        {
            errormsg = "该联系电话已被使用";
            return errormsg;
        }
        
        if (!userService.emialIsValid(user.getEmail()))
        {
            errormsg = "该邮箱已被使用";
            return errormsg;
        }
        
        Employee emp = new Employee();
        emp.setCity(dto.getCity());
        // emp.setMonthLimitvalue(Double.valueOf(String.valueOf(jsonMap.get("monthLimitvalue"))));
        // emp.setOrderCustomer(String.valueOf(jsonMap.get("orderCustomer")));
        // emp.setOrderSelf(String.valueOf(jsonMap.get("orderSelf")));
        // emp.setOrderApp(String.valueOf(jsonMap.get("orderApp")));
        // emp.setOrderWeb(String.valueOf(jsonMap.get("orderWeb")));
        user = userService.createEmployee(user, emp);
        
        return errormsg;
    }
    
    public Employee findEmployeeByUserId(Long id)
    {
        return userService.findEmployeeByUserId(id);
    }
    
    public void updateEmployee(Employee employee)
    {
        userService.updateEmployee(employee);
    }
}
