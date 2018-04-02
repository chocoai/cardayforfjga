package com.cmdt.carrental.common.integration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.util.HttpClientUtil;
import com.cmdt.carrental.common.util.JsonUtils;

@Component
public class CommunicationService {
	
	private static final Logger LOG = LoggerFactory.getLogger(CommunicationService.class);
	
	@Value("${service.emailServiceUrl}")
	private String emailServiceUrl;
	
	@Value("${service.smsServiceUrl}")
	private String smsServiceUrl;
	
	@Value("${service.pushServiceUrl}")
	private String pushServiceUrl;
	
	
	public String sendSms(String msg,String recipient){
		try {
			Map<String,String> params = new HashMap<String,String>();
			params.put("msg",msg);
			params.put("recipient", recipient);
			params.put("vendor", Constants.SMS_VENDOR);
			params.put("customerid",Constants.SMS_CUSTOMER_ID);
			return HttpClientUtil.sendJsonPost(smsServiceUrl, params);
		} catch (Exception e) {
			LOG.error("Call communication service sendSms error, cause by :\n",e);
			return null;
		}
	}

	/**
	 * 带返回值的短信推送
	 * @param msg
	 * @param recipient
	 * @return 返回短信推送是否成功
	 */
	public boolean sendSmsResult(String msg,String recipient){
		try {
			Map<String,String> params = new HashMap<String,String>();
			params.put("msg",msg);
			params.put("recipient", recipient);
			params.put("vendor", Constants.SMS_VENDOR);
			params.put("customerid",Constants.SMS_CUSTOMER_ID);
			String sendResp = HttpClientUtil.sendJsonPost(smsServiceUrl, params);
			Map<String, String> respMap = JsonUtils.json2Object(sendResp, Map.class);
			if (!Constants.SMS_Publish_Response_Success.equals(respMap.get("statusCode"))) {
				// 只有 statusCode = "20000”才代表短信推送成功
				// {"statusCode":"20000","messages":["SMS send success"],"returnEntity":null}
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			LOG.error("Call communication service sendSms error, cause by :\n",e);
			return false;
		}
	}
	
	public String sendEmail(){
		return "";
	}
	
	public String sendPush(List<String> mobiles,String title,String content,String product){
		
		StringBuffer mobilesBuffer = new StringBuffer();
		for(String mobile : mobiles){
			if(mobile!=null&&mobile.trim().length()>0){
				mobilesBuffer.append(mobile).append(",");
			}
		}
		
		if(mobilesBuffer.length()>0){
		  mobilesBuffer.deleteCharAt(mobilesBuffer.length()-1);
		}else{
			return null;
		}

		try {
			Map<String,Object> params = new HashMap<String,Object>();
			LOG.info("COMM_Serv_PHONES:"+mobilesBuffer.toString());
			params.put("mobile",mobilesBuffer.toString());
			params.put("title", title);
			params.put("content", content);
			params.put("vendor", Constants.PUSH_VENDOR);
			params.put("app", product);
			String respStr = HttpClientUtil.sendPost(pushServiceUrl, new StringBuffer().append(JsonUtils.object2Json(params)));
			LOG.info("COMM_Serv_RESP:"+respStr);
			return respStr;
		} catch (Exception e) {
			LOG.error("Call communication service sendPush error, cause by :\n",e);
			return null;
		}
	}
	
}
