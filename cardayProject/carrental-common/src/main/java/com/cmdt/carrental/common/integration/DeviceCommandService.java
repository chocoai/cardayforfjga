package com.cmdt.carrental.common.integration;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.util.HttpClientUtil;

@Component
public class DeviceCommandService {

private static final Logger LOG = LoggerFactory.getLogger(DeviceCommandService.class);
	
	@Value("${service.deviceCommandSetSpeedUrl}")
	private String deviceCommandSetSpeedUrl;
	
	/**
	 * @param imei(设备编号)
	 * @param limitSpeed(限速阈值)
	 * @param interval(报警周期)
	 * @return JSON
	 */
	public String setSpeedLimitCommand(String imei,int limitSpeed,int interval){
		try {
			Map<String,String> params = new HashMap<String,String>();
			params.put("imei",imei);
			params.put("params", Constants.COMMAND_INSTRUCTION_SPEED+Constants.COMMAND_INSTRUCTION_EQUALS+limitSpeed+Constants.COMMAND_INSTRUCTION_COLON+interval);
			params.put("user_id", "WANHAN");
			params.put("command_key",Constants.COMMAND_KEY);
			return HttpClientUtil.sendJsonPost(deviceCommandSetSpeedUrl, params);
		} catch (Exception e) {
			LOG.error("Call Command service set Speed error, cause by :\n",e);
			return null;
		}
	}
	
}
