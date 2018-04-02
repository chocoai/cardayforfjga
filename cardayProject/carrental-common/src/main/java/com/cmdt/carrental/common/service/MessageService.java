package com.cmdt.carrental.common.service;

import java.util.List;
import java.util.Map;

import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.DateCountModel;
import com.cmdt.carrental.common.model.PagModel;

public interface MessageService {
	public List<DateCountModel> getMsgCountByUser(User user,Long orgId);
	public PagModel getMessageByUser(User user, String msgType, Integer currentPage, Integer pageSize);
	public PagModel getMessageByUserForApp(User user, String msgType, Integer currentPage, Integer pageSize, String app);

	public void setMsgAsRead(Long msgId);
	
	public Message findById(Long msgId);
	
	public void saveMessages(final List<Message> messages);
	public PagModel findAllSysMessage(Map<String, Object> jsonMap);
	public List<Message> getLatestSystemMessage(Long organizationId); 
	public void updateOutBoundMsgStatus(Long alertId);
	public Integer getReadMessageByUserId(Long userId,Long messageId);
	public void saveReadMessageByUserId(Long userId,Long messageId);
	public Message queryMessageById(Long messageId);
	
}
