package com.cmdt.carrental.platform.service.biz.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.Rent;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.service.MessageService;
import com.cmdt.carrental.common.service.RentService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.platform.service.model.request.message.MessageCreateDto;
import com.cmdt.carrental.platform.service.model.request.message.MessagePageDto;

@Service
public class PlatformSystemMessageService {

	private static final Logger LOG = LoggerFactory.getLogger(PlatformSystemMessageService.class);

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommunicationService commService;

	public PagModel getMessageByUser(User loginUser, MessagePageDto messagePageDto) {
		try{
			if (StringUtils.isBlank(messagePageDto.getType())) {
				messagePageDto.setType("all");
			}
	
			return this.messageService.getMessageByUser(loginUser, messagePageDto.getType(),
					messagePageDto.getCurrentPage(), messagePageDto.getNumPerPage());
		}catch(Exception e){
    		LOG.error("PlatformSystemMessageService getMessageByUser error : ",e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE); 
    	}
	}

	public void addSysMessage(User loginUser, MessageCreateDto messageCreateDto) {
		try{
			List list = new ArrayList();
			Message message = new Message();
			message.setMsg(messageCreateDto.getContent());
			message.setTitle(messageCreateDto.getTitle());
			message.setType(Message.MessageType.SYSTEM);
			message.setTime(new Date());
			message.setIsNew(Integer.valueOf(1));
			message.setOrgId(loginUser.getOrganizationId());
			list.add(message);
			this.messageService.saveMessages(list);
	
			List entList = this.userService.listUserListByOrgId(loginUser.getOrganizationId(), Constants.ENTER_MANAGER);
	
			List deptList = this.userService.listUserListByOrgId(loginUser.getOrganizationId(), Constants.DEPT_MANAGER);
	
			List employList = this.userService.listUserListByOrgId(loginUser.getOrganizationId(), Constants.EMPLOYEE);
	
			List driverList = this.userService.listUserListByOrgId(loginUser.getOrganizationId(), Constants.DRIVER);
			List managerList = new ArrayList();
			managerList.addAll(entList);
			managerList.addAll(deptList);
	
			List employAndDeptList = new ArrayList();
			employAndDeptList.addAll(employList);
			employAndDeptList.addAll(deptList);
	
			this.commService.sendPush(managerList, message.getTitle(), message.getMsg(), "CARDAY.admin");
			this.commService.sendPush(driverList, message.getTitle(), message.getMsg(), "CARDAY.driver");
			this.commService.sendPush(employAndDeptList, message.getTitle(), message.getMsg(), "CARDAY.enduser");
		}catch(Exception e){
    		LOG.error("PlatformSystemMessageService addSysMessage error : ",e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE); 
    	}
	}
	
	public List<Message> getLatestSystemMessage(Long organizationId){
		try{
	        return messageService.getLatestSystemMessage(organizationId);
	    }catch(Exception e){
	        LOG.error("SystemMessageApi getLatestSystemMessage error, cause by: ", e);
	        throw new ServerException(Constants.API_MESSAGE_FAILURE); 
	    }
	}

}
