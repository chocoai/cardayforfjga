package com.cmdt.carrental.common.dao;

import java.util.List;

import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.DateCountModel;
import com.cmdt.carrental.common.model.PagModel;

public interface MessageDao {
	
	public List<DateCountModel> getMsgCountByUser(User user, Long orgId, List<Long> orgIdList, Organization topOrg);
	public PagModel getMessageByUser(User user, String msgType, Integer currentPage, Integer pageSize, List<Long> list, Organization topOrg);
	public PagModel getMessageByUserForApp(User user, String msgType, Integer currentPage, Integer pageSize, String app, List<Long> orgIdList, Organization topOrg);
	
	public void setMsgAsRead(Long msgId);
	public Message findById(Long msgId);
	public void saveMessages(List<Message> messages);
	public PagModel findAllSysMessage(String title,Integer currentPage, Integer numPerPage);
	public List<Message> getLatestSystemMessage(Long organizationId);
	public void updateOutBoundMsgStatus(Long alertId);
	public Integer getReadMessageByUserId(Long userId,Long messageId);
	public void saveReadMessageByUserId(Long userId, Long messageId);
	public Message queryMessageById(Long messageId);
}
