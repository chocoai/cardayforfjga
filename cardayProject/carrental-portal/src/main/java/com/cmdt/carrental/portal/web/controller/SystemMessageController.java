package com.cmdt.carrental.portal.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Null;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.Message.MessageType;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.service.MessageService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.util.BusinessConstants;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/sysMessage")
public class SystemMessageController extends BaseController {
	
	private static final Logger LOG = LoggerFactory.getLogger(VehicleController.class);
	
	@Autowired
	private MessageService messageService;
	@Autowired
	private UserService userService;
	@Autowired
	private CommunicationService commService;
	
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> showMessages(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		Integer currentPage = TypeUtils.obj2Integer(jsonMap.get("currentPage"));
        	Integer numPerPage = TypeUtils.obj2Integer(jsonMap.get("numPerPage"));
        	//fjga 公告消息只展示系统消息
        	String type=BusinessConstants.MSG_TYPE_SYSTEM;
//        	if (!StringUtils.isNotBlank(TypeUtils.obj2String(jsonMap.get("type")))) {
//				type=BusinessConstants.MSG_TYPE_ALL;
//			}else{
//				type=TypeUtils.obj2String(jsonMap.get("type"));
//			}
        	PagModel pagModel=messageService.getMessageByUser(loginUser,type,currentPage,numPerPage);
    		map.put("data",pagModel);
    		map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("SystemMessageController[---showMessages---]", e);
    		map.put("status", "failure");
    	}
        return map;
    }
    //企业管理员添加系统公告，以及推送公告到当前orgId下的所有用户
	    @SuppressWarnings("unchecked")
		@RequestMapping(value = "/addSysMessage", method = RequestMethod.POST)
	    @ResponseBody
	    public Map<String,Object> addSysMessage(@CurrentUser User loginUser,String json) {
			LOG.info("Inside SystemMessageController.addSysMessage");       
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("data", "");
			try {
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				
				List<Message> list=new ArrayList<Message>();
				Message message=new Message();
				message.setMsg(TypeUtils.obj2String(jsonMap.get("content")));
				message.setTitle(TypeUtils.obj2String(jsonMap.get("title")));
				message.setType(MessageType.SYSTEM);
				message.setTime(new Date());
				message.setIsNew(Integer.valueOf(1));
				message.setOrgId(loginUser.getOrganizationId());
				list.add(message);
				messageService.saveMessages(list);
				
				//企业管理员
				List<String> entList=userService.listUserListByOrgId(loginUser.getOrganizationId(), Constants.ENTER_MANAGER);
				//部门管理员
				List<String> deptList=userService.listUserListByOrgId(loginUser.getOrganizationId(),Constants.DEPT_MANAGER);
				//员工
				List<String> employList=userService.listUserListByOrgId(loginUser.getOrganizationId(), Constants.EMPLOYEE);
				//司机
				List<String> driverList=userService.listUserListByOrgId(loginUser.getOrganizationId(), Constants.DRIVER);
				List<String> managerList=new ArrayList<String>();
				managerList.addAll(entList);
				managerList.addAll(deptList);
				List<String> employAndDeptList=new ArrayList<String>();
				employAndDeptList.addAll(employList);
				employAndDeptList.addAll(deptList);
				//TODO userlist split by usertype
				commService.sendPush(managerList, message.getTitle(), message.getMsg(), Constants.CARDAY_ADMIN);
				commService.sendPush(driverList, message.getTitle(), message.getMsg(), Constants.CARDAY_DRIVER);
				commService.sendPush(employAndDeptList,message.getTitle(), message.getMsg(), Constants.CARDAY_ENDUSER);
				map.put("status", "success");
			} catch (Exception e) {
				LOG.error("SystemMessageController[---addSysMessage---]", e);
				map.put("status", "failure");
			}
			return map;
	    }
		@RequestMapping(value = "/getLatestSystemMessage", method = RequestMethod.POST)
	    @ResponseBody
	    public Map<String,Object> getLatestSystemMessage(@CurrentUser User loginUser) {
	    	Map<String,Object> map = new HashMap<String,Object>();
	    	map.put("data", "");
	    	try{
	    		List<Message> message=messageService.getLatestSystemMessage(loginUser.getOrganizationId());
	    		map.put("data",message);
	    		map.put("status", "success");
	    	}catch(Exception e){
	    		LOG.error("SystemMessageController[---getLatestSystemMessage---]", e);
	    		map.put("status", "failure");
	    	}
	        return map;
	    }
}
