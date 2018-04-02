package com.cmdt.carrental.portal.web.controller;

import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cmdt.carrental.common.entity.PhoneVerificationCode;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.util.DateUtils;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;  
 
@Controller  
@RequestMapping("/code")  
public class CaptchaController {  
	
	private static final Logger LOG = LoggerFactory.getLogger(CaptchaController.class);
      
    @Autowired  
    private Producer captchaProducer = null;  
    
    @Autowired
	public ShouqiService shouqiService;
    
    @Autowired
	public UserService userService;
    
    @Autowired
    public CommunicationService commService;
  
    @RequestMapping(value = "/captcha-image", method = RequestMethod.GET)
    public ModelAndView getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {  
        HttpSession session = request.getSession();  
        
        response.setDateHeader("Expires", 0);  
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");  
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");  
        response.setHeader("Pragma", "no-cache");  
        response.setContentType("image/jpeg");  
        
        String capText = captchaProducer.createText();  
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);  
        
        BufferedImage bi = captchaProducer.createImage(capText);  
        ServletOutputStream out = response.getOutputStream();  
        ImageIO.write(bi, "jpg", out);  
        try {  
            out.flush();  
        } finally {  
            out.close();  
        }  
        return null;  
    }
    
    @RequestMapping(value = "/getVolidateCode", method = RequestMethod.GET)
    public ModelAndView getVolidateCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	HttpSession session = request.getSession();  
        String code = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY);
        LOG.info("验证码: " + code );
        PrintWriter pWriter = response.getWriter();
        pWriter.write(code);
        return null;
    }
    @RequestMapping(value = "/sendVertificationCode", method = RequestMethod.POST)
    @ResponseBody
	public Map<String,Object> sendVertificationCode(String phoneNumber){
		int num = (int)((Math.random()*9+1)*100000);
		String randNum=String.valueOf(num);
		
		Map<String,Object> map=new HashMap<String,Object>();
		try {
			//Send sms by sjjl
		   commService.sendSms("您在CarDay请求的验证码是: " + randNum, phoneNumber);
		   //new ServiceAdapter(shouqiService).doService(ActionName.SMSMESSAGE, new Object[] { phoneNumber,"您在CarDay请求的验证码是: "+randNum });
		   PhoneVerificationCode code=new PhoneVerificationCode();
		   code.setCode(randNum);
		   code.setPhoneNumber(phoneNumber);
		   code.setExpirationTime(new java.sql.Timestamp((DateUtils.addMinutes(new Date(), 1)).getTime()));
		   if (userService.getCode(phoneNumber)!=null) {
			   userService.updateCode(code);
		   }else{
			   userService.saveCode(code);
		   }
		   //   redisService.set(phoneNumber, randNum, 60);
		   LOG.info("验证码 : "+randNum);
		   map.put("status", "success");
		
		
//		   //Send sms by ali
//		   String respMsg = smsService.sendSms(phoneNumber, SmsType.AUTHCODE, "{\"authCode\":\""+randNum+"\"}");
//	    	if("".equals(respMsg)){
//	    		LOG.info("短信发送成功！");
//	    		PhoneVerificationCode code=new PhoneVerificationCode();
//	 		    code.setCode(randNum);
//	 		    code.setPhoneNumber(phoneNumber);
//	 		    code.setExpirationTime(new java.sql.Timestamp((DateUtils.addMinutes(new Date(), 1)).getTime()));
//	 		    if (userService.getCode(phoneNumber)!=null) {
//	 			   userService.updateCode(code);
//	 		    }else{
//	 			   userService.saveCode(code);
//	 		    }
//	 		    //   redisService.set(phoneNumber, randNum, 60);
//	 		    LOG.info("验证码 : "+randNum);
//	 		    map.put("status", "success");
//	    	}else{
//	    		LOG.debug("Send sms exception:", respMsg);
//				map.put("status", "failure");
//	    	}
	    	
		} catch (Exception e) {
			LOG.error("Captcha Controller sendVertificationCode error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
    
    @RequestMapping(value = "/checkVertificationCode", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> checkVertificationCode(String phoneNumber,String code,HttpServletRequest request){
    	Map<String, Object> result=new HashMap<String, Object>();
    	try {
    		User user=userService.findByPhoneNumber(phoneNumber);
    		if(user==null){
    			result.put("status", "phoneFailure");
        		result.put("error", "该手机号未被注册");
        		return result;
    		}
    		request.getSession().setAttribute("user", user);
    		PhoneVerificationCode phoneCode=userService.checkCode(phoneNumber);
    		//String phoneCode=redisService.get(phoneNumber);
    		
    		if(phoneCode!=null && phoneCode.getCode().equals(code)){
    			result.put("status", "success");
        	}else if(phoneCode==null){
        		result.put("status", "codeFailure");
        		result.put("error", "验证码已失效");
        	}else{
        		result.put("status", "codeFailure");
        		result.put("error", "验证码错误");
        	}
		} catch (Exception e) {
			LOG.error("Captcha Controller checkVertificationCode error, cause by:", e);
			result.put("status", "failure");
		}
    	return result;
    }
    @RequestMapping(value = "/resetPage", method = RequestMethod.GET)
    public String resetPage(String phoneNumber,HttpServletRequest req){
    	return "getpass_resetPassword";
    }
}

