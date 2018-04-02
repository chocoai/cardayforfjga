package com.cmdt.carrental.mobile.gateway.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.DateCountModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.service.BusiOrderService;
import com.cmdt.carrental.common.service.MessageService;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.util.BusinessConstants;
import com.cmdt.carrental.mobile.gateway.model.MsgCountModel;
import com.cmdt.carrental.mobile.gateway.model.MsgDataModel;
import com.cmdt.carrental.mobile.gateway.model.MsgInfoModel;

@Service
public class MessageAppService {

	private static final Logger LOG = LoggerFactory.getLogger(BusiStatisticsService.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationService orgService;
	
	@Autowired
	private MessageService messageService;
	@Autowired
	private BusiOrderService busiOrderService;
	
	
	public MsgCountModel loadMsgCountByUser(String userId, String app) throws Exception {
		LOG.debug("MessageAppService.loadMsgCountByUser("+userId+")");
		MsgCountModel model = new MsgCountModel();
		if(userId == null || "".equals(userId)){
			return model;
		}
		
		User user = userService.findById(Long.parseLong(userId));
		
		//CR2215
		if((!Constants.CARDAY_ENDUSER.equals(app) && !Constants.CARDAY_ADMIN.equals(app) && !Constants.CARDAY_DRIVER.equals(app))
				&& ((user.isEndUser() && !Constants.CARDAY_ENDUSER.equals(app))
				||	(user.isDriver() && !Constants.CARDAY_DRIVER.equals(app))
				||	(user.isDeptAdmin() && (!Constants.CARDAY_ADMIN.equals(app) && !Constants.CARDAY_ENDUSER.equals(app)))
				||	((user.isEntAdmin() || user.isRentEnterpriseAdmin()) && !Constants.CARDAY_ADMIN.equals(app))
//				||	((user.isAdmin()|| user.isSuperAdmin()) && !Constants.CARDAY_ADMIN.equals(app))
				)){
			return model;
		}
				
		if(user != null){
			Long orgId = user.getOrganizationId();
			List<DateCountModel> models = messageService.getMsgCountByUser(user, orgId);
			if(models != null && models.size() > 0){
				int total = 0;
				for(DateCountModel m : models){
					int tmp = 0;
					if(BusinessConstants.MSG_TYPE_SYSTEM.toUpperCase().equals(m.getDate())){
						model.setSystem(m.getValue()+"");
						tmp = m.getValue();
					}else if(BusinessConstants.MSG_TYPE_TRIP.toUpperCase().equals(m.getDate())){
						model.setTrip(m.getValue()+"");
						tmp = m.getValue();
					}else if(BusinessConstants.MSG_TYPE_MAINTAIN.toUpperCase().equals(m.getDate())){
						tmp = m.getValue();
						//CR2215
						if(user.isDeptAdmin() && StringUtils.isNotBlank(app) && Constants.CARDAY_ENDUSER.equals(app)){
							tmp = 0;
						}
						model.setMaintain(tmp+"");
					}else{
						tmp = m.getValue();
						//CR2215
						if(user.isDeptAdmin() && StringUtils.isNotBlank(app) && Constants.CARDAY_ENDUSER.equals(app)){
							tmp = 0;
						}
						model.setAbnormal(tmp+"");
					}
					total = total + tmp;
				}
				
				model.setUnReadTotal(total + "");
			}
		}
		
		return model;
		
	}
	
	
	public MsgInfoModel loadMsgInfoByUser(String userId, String msgType,Integer currentPage,Integer pageSize, String app) throws Exception {
		LOG.debug("MessageAppService.loadMsgInfoByUser("+userId+","+msgType+","+currentPage+","+pageSize+","+app+")");
		MsgInfoModel model = new MsgInfoModel();
		List<MsgDataModel> msgDatas = new ArrayList<MsgDataModel>();
		int total = 0;
		int unread = 0;
		int read=0;
		
		//check parameters
		if(userId == null || "".equals(userId)
				|| msgType == null || "".equals(msgType)
				|| currentPage == null || "".equals(currentPage)
				|| pageSize == null || "".equals(pageSize)){
			return model;
		}
		User user = userService.findById(Long.parseLong(userId));
		
		//CR2215
		if((!Constants.CARDAY_ENDUSER.equals(app) && !Constants.CARDAY_ADMIN.equals(app) && !Constants.CARDAY_DRIVER.equals(app))
				&& ((user.isEndUser() && !Constants.CARDAY_ENDUSER.equals(app))
				||	(user.isDriver() && !Constants.CARDAY_DRIVER.equals(app))
				||	(user.isDeptAdmin() && (!Constants.CARDAY_ADMIN.equals(app) || !Constants.CARDAY_ENDUSER.equals(app)))
				||	((user.isEntAdmin() || user.isRentEnterpriseAdmin()) && !Constants.CARDAY_ADMIN.equals(app))
//				||	((user.isAdmin()|| user.isSuperAdmin()) && !Constants.CARDAY_ADMIN.equals(app))
				)){
			return model;
		}
		
		//司机120
		//用车人78
		if(user != null){
//			Long orgId = user.getOrganizationId();
			PagModel pgmodel;
//			if("all".equals(msgType)){
//				pgmodel = messageService.getMessageByUser(user, null, currentPage, pageSize);
//			}else{
				pgmodel = messageService.getMessageByUserForApp(user, msgType, currentPage, pageSize, app);
//			}
			List<Message> messageList = pgmodel.getResultList();
			if(messageList != null && messageList.size() > 0){
				total = messageList.size();
				for(Message msg : messageList){
					MsgDataModel dataModel = new MsgDataModel();
					dataModel.setMsgId(msg.getId());
					dataModel.setCarNo(msg.getCarNo());
					dataModel.setType(msg.getType());
					dataModel.setLocation(msg.getLocation());
					dataModel.setMsg(msg.getMsg());
//					dataModel.setUnread(msg.getIsNew()==1?true:false);
//					if(msg.getIsNew()==1){
//						unread++;
//					}
					
//					//CR2215部门管理员登陆enduser app过滤('OVERSPEED','VEHICLEBACK','OUTBOUND','MAINTAIN')
//					if(user.isDeptAdmin() && StringUtils.isNotBlank(app) && Constants.CARDAY_ENDUSER.equals(app)){
//						if("'OVERSPEED','VEHICLEBACK','OUTBOUND','MAINTAIN'".contains(msg.getType().name())){
//							continue;
//						}
//					}
					
					//用户的消息在表read_message有数据，则为已读消息
					if(messageService.getReadMessageByUserId(user.getId(), msg.getId())==1){
						read++;
						dataModel.setUnread(false);
					}else{
						dataModel.setUnread(true);
					}
					if(msg.getIsEnd() != null){
						dataModel.setRelease(msg.getIsEnd() == 1?true:false);
					}
					dataModel.setTime(msg.getTime());
					dataModel.setWarningId(msg.getWarningId());
					if(msg.getOrgId() !=  null){
						Organization org = orgService.findById(msg.getOrgId());
						dataModel.setDepartment(org.getName());
					}
					if (msg.getOrderId()!= null) {
						BusiOrder busiOrder=busiOrderService.findOne(msg.getOrderId());
						dataModel.setOrderNo(busiOrder.getOrderNo());
						dataModel.setFromPlace(busiOrder.getFromPlace());
						dataModel.setToPlace(busiOrder.getToPlace());
						dataModel.setUserName(busiOrder.getOrderUsername());
						dataModel.setUserPhone(busiOrder.getOrderUserphone());
						dataModel.setDriverName(busiOrder.getDriverName());
						dataModel.setDriverPhone(busiOrder.getDriverPhone());
						dataModel.setOrderPlanTime(busiOrder.getPlanStTime());
						if(busiOrder.getPlanStTime()==null){
							dataModel.setOrderType("补录订单");
						}else{
							dataModel.setOrderType("预约订单");
						}
					}
					
					msgDatas.add(dataModel);
				}
//				//CR2215
//				total = msgDatas.size();
			}
			
		}
		
		model.setTotal(total + "");
		model.setUnread(total-read + "");
		model.setCurrentPage(currentPage);
		model.setPageSize(pageSize);
		model.setMsgs(msgDatas);
		
		return model;
	}
	
	public String setMsgAsRead(String msgId,Long userId) throws Exception {
		LOG.debug("MessageAppService.setMsgAsRead("+msgId+")");
		String msg = "";
		if(msgId != null && !"".equals(msgId)){
			Message msgdata = messageService.findById(Long.parseLong(msgId));
			if(msgdata == null){
				msg = "消息不存在！";
			}else{
				if(messageService.getReadMessageByUserId(userId, Long.parseLong(msgId)) != 0){
					msg = "消息已读取！";
				}else{
					messageService.saveReadMessageByUserId(userId, Long.parseLong(msgId));
				}
			}
		}else{
			msg = "消息编号无效！";
		}
		return msg;
	}
}
