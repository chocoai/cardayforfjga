package com.cmdt.carrental.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.dao.MessageDao;
import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.DateCountModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.util.TypeUtils;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private OrganizationService organizationService;
	 
	@Override
	public List<DateCountModel> getMsgCountByUser(User user,Long orgId) {
		Organization topOrg=organizationService.findTopOrganization(user.getOrganizationId());
		List<Organization> list=organizationService.findDownOrganizationListByOrgId(user.getOrganizationId(),true,true);
		List<Long> orgIdList=null;
		if(!list.isEmpty()){
			orgIdList=new ArrayList<Long>();
			for (Organization organization : list) {
				orgIdList.add(organization.getId());
			}
		}else{
			return null;
		}
		return messageDao.getMsgCountByUser(user, orgId,orgIdList,topOrg);
	}

	@Override
	public PagModel getMessageByUser(User user, String msgType, Integer currentPage, Integer pageSize) {
		PagModel pagModel=null;
		Organization topOrg=organizationService.findTopOrganization(user.getOrganizationId());
		List<Organization> list=organizationService.findDownOrganizationListByOrgId(user.getOrganizationId(),true,true);
		List<Long> orgIdList=null;
		if(!list.isEmpty()){
			orgIdList=new ArrayList<Long>();
			for (Organization organization : list) {
				orgIdList.add(organization.getId());
			}
		}else{
			return null;
		}
		pagModel=messageDao.getMessageByUser(user, msgType, currentPage, pageSize,orgIdList,topOrg);
		
		/*List<Message> messageList = pagModel.getResultList();
		for (Message message : messageList) {
			if (null!=message.getOrderNo()&&"".equals(message.getOrderNo())) {
				BusiOrder busiOrder=busiOrderDao.queryBusiOrderByOrderNo(message.getOrderNo());
			}
		}*/
		return pagModel;
	}
	
	
	@Override
	public PagModel getMessageByUserForApp(User user, String msgType, Integer currentPage, Integer pageSize, String app) {
		PagModel pagModel=null;
		/*Organization topOrg=organizationService.findTopOrganization(user.getOrganizationId());
		List<Long> orgIdList=null;
		if (user.isEntAdmin()) {
			List<Organization> list=organizationService.findDownOrganizationListByOrgId(user.getOrganizationId(),true,true);
			if(!list.isEmpty()){
				orgIdList=new ArrayList<Long>();
				for (Organization organization : list) {
					orgIdList.add(organization.getId());
				}
			}
			if (orgIdList.isEmpty()) {
				return null;
			}
		}*/
		Organization topOrg=organizationService.findTopOrganization(user.getOrganizationId());
		List<Organization> list=organizationService.findDownOrganizationListByOrgId(user.getOrganizationId(),true,true);
		List<Long> orgIdList=null;
		if(!list.isEmpty()){
			orgIdList=new ArrayList<Long>();
			for (Organization organization : list) {
				orgIdList.add(organization.getId());
			}
		}else{
			return null;
		}
		
		pagModel=messageDao.getMessageByUserForApp(user, msgType, currentPage, pageSize, app,orgIdList,topOrg);
		/*List<Message> messageList = pagModel.getResultList();
		for (Message message : messageList) {
			if (null!=message.getOrderNo()&&"".equals(message.getOrderNo())) {
				BusiOrder busiOrder=busiOrderDao.queryBusiOrderByOrderNo(message.getOrderNo());
			}
		}*/
		return pagModel;
	}
	

	@Override
	public void setMsgAsRead(Long msgId) {
		messageDao.setMsgAsRead(msgId);	
	}

	@Override
	public Message findById(Long msgId) {
		return messageDao.findById(msgId);	
	}
	
	public void saveMessages(final List<Message> messages){
		messageDao.saveMessages(messages);//保存系统消息，以及消息推送
	}

	//供portal端查看系统消息
	@Override
	public PagModel findAllSysMessage(Map<String, Object> jsonMap) {
		Integer currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
    	Integer numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
    	String title =TypeUtils.obj2String(jsonMap.get("title"));
    	PagModel pagModel=messageDao.findAllSysMessage(title,currentPage,numPerPage);
		return pagModel;
	}

	@Override
	public List<Message> getLatestSystemMessage(Long organizationId) {
		Organization topOrg=organizationService.findTopOrganization(organizationId);
		return messageDao.getLatestSystemMessage(topOrg.getId());
	}
	@Override
	public void updateOutBoundMsgStatus(Long alertId){
		messageDao.updateOutBoundMsgStatus(alertId);
	}

	@Override
	public Integer getReadMessageByUserId(Long userId,Long messageId) {
		return messageDao.getReadMessageByUserId(userId,messageId);
	}

	@Override
	public void saveReadMessageByUserId(Long userId, Long messageId) {
		 messageDao.saveReadMessageByUserId(userId,messageId);
		
	}

	@Override
	public Message queryMessageById(Long messageId) {
		return messageDao.queryMessageById(messageId);
	}
}
