package service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.quartz.SuperTestCase;

public class CommunicationServiceTestCase extends SuperTestCase{

	@Autowired
	private CommunicationService commService;
	
	@Test
	public void sendSms(){
//		commService.sendSms("1234567890", "15072440508");
	}
	
	@Test
	public void sendPush(){
		/*List<String> adminPhone = new ArrayList<>();
		List<String> driverPhone = new ArrayList<>();
		List<String> enduserPhone = new ArrayList<>();
		
		adminPhone.add("13216161110");
		driverPhone.add("13865687670");
		enduserPhone.add("13810000005");
		
		commService.sendPush(adminPhone, "管理员推送消息","这是管理员测试消息", Constants.CARDAY_ADMIN);
		commService.sendPush(driverPhone, "司机推送消息","这是司机测试消息", Constants.CARDAY_DRIVER);
		commService.sendPush(enduserPhone, "用车人推送消息", "这是用车人测试消息", Constants.CARDAY_ENDUSER);*/
	}
	
}
