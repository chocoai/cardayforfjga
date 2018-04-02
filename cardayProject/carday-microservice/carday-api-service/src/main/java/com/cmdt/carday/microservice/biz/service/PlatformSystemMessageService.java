package com.cmdt.carday.microservice.biz.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carday.microservice.common.model.response.exception.ServiceException;
import com.cmdt.carday.microservice.model.request.message.MessageCreateDto;
import com.cmdt.carday.microservice.model.request.message.MessagePageDto;
import com.cmdt.carday.microservice.model.response.message.MsgCountModel;
import com.cmdt.carday.microservice.model.response.message.MsgDataModel;
import com.cmdt.carday.microservice.model.response.message.MsgInfoModel;
import com.cmdt.carrental.common.bean.messageCenter.MessageType;
import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carday.microservice.common.model.response.MessageCode;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.model.DateCountModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.service.BusiOrderService;
import com.cmdt.carrental.common.service.MessageService;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.util.BusinessConstants;

@Service
public class PlatformSystemMessageService {

	// private static final Logger LOG =
	// LoggerFactory.getLogger(PlatformSystemMessageService.class);

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommunicationService commService;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private BusiOrderService busiOrderService;

	public PagModel getMessageByUser(User loginUser, MessagePageDto messagePageDto) {

		if (null == messagePageDto.getMessageType()) {
			messagePageDto.setMessageType(MessageType.ALL);
		}

		return this.messageService.getMessageByUser(loginUser, messagePageDto.getMessageType().getType(),
				messagePageDto.getCurrentPage(), messagePageDto.getNumPerPage());

	}

	public void addSysMessage(User loginUser, MessageCreateDto messageCreateDto) {

		Message message = new Message();
		message.setMsg(messageCreateDto.getContent());
		message.setTitle(messageCreateDto.getTitle());
		message.setType(Message.MessageType.SYSTEM);
		message.setTime(new Date());
		message.setIsNew(Integer.valueOf(1));
		message.setOrgId(loginUser.getOrganizationId());

		this.messageService.saveMessages(Arrays.asList(message));

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

	}

	public List<Message> getLatestSystemMessage(Long organizationId) {

		return messageService.getLatestSystemMessage(organizationId);

	}

	public MsgInfoModel loadMsgInfoByUser(Long userId, MessageType messageType, Integer currentPage, Integer pageSize,
			String app) {
		MsgInfoModel model = new MsgInfoModel();
		List<MsgDataModel> msgDatas = new ArrayList<>();
		int total = 0;
		int read = 0;
		User user = userService.findById(userId);
		String msgType;
		if (null == messageType) {
			msgType = MessageType.ALL.getType();
		} else {
			msgType = messageType.getType();
		}
		// CR2215
		if ((!Constants.CARDAY_ENDUSER.equals(app) && !Constants.CARDAY_ADMIN.equals(app)
				&& !Constants.CARDAY_DRIVER.equals(app))
				&& ((user.isEndUser() && !Constants.CARDAY_ENDUSER.equals(app))
						|| (user.isDriver() && !Constants.CARDAY_DRIVER.equals(app))
						|| (user.isDeptAdmin()
								&& (!Constants.CARDAY_ADMIN.equals(app) || !Constants.CARDAY_ENDUSER.equals(app)))
						|| ((user.isEntAdmin() || user.isRentEnterpriseAdmin())
								&& !Constants.CARDAY_ADMIN.equals(app)))) {
			return model;
		}

		// 司机120
		// 用车人78
		if (user != null) {
			PagModel pgmodel = messageService.getMessageByUserForApp(user, msgType, currentPage, pageSize, app);
			List<Message> messageList = pgmodel.getResultList();
			if (!messageList.isEmpty()) {
				total = messageList.size();
				for (Message msg : messageList) {
					MsgDataModel dataModel = new MsgDataModel();
					dataModel.setMsgId(msg.getId());
					dataModel.setCarNo(msg.getCarNo());
					dataModel.setType(msg.getType());
					dataModel.setLocation(msg.getLocation());
					dataModel.setMsg(msg.getMsg());
					// 用户的消息在表read_message有数据，则为已读消息
					if (messageService.getReadMessageByUserId(user.getId(), msg.getId()) == 1) {
						read++;
						dataModel.setUnread(false);
					} else {
						dataModel.setUnread(true);
					}
					if (msg.getIsEnd() != null) {
						dataModel.setRelease(msg.getIsEnd() == 1 ? true : false);
					}
					dataModel.setTime(msg.getTime());
					dataModel.setWarningId(msg.getWarningId());
					if (msg.getOrgId() != null) {
						Organization org = organizationService.findById(msg.getOrgId());
						dataModel.setDepartment(org.getName());
					}
					if (msg.getOrderId() != null) {
						BusiOrder busiOrder = busiOrderService.findOne(msg.getOrderId());
						dataModel.setOrderNo(busiOrder.getOrderNo());
						dataModel.setFromPlace(busiOrder.getFromPlace());
						dataModel.setToPlace(busiOrder.getToPlace());
						dataModel.setUserName(busiOrder.getOrderUsername());
						dataModel.setUserPhone(busiOrder.getOrderUserphone());
						dataModel.setDriverName(busiOrder.getDriverName());
						dataModel.setDriverPhone(busiOrder.getDriverPhone());
						dataModel.setOrderPlanTime(busiOrder.getPlanStTime());
						if (busiOrder.getPlanStTime() == null) {
							dataModel.setOrderType("补录订单");
						} else {
							dataModel.setOrderType("预约订单");
						}
					}
					msgDatas.add(dataModel);
				}
			}

		}
		model.setTotal(Integer.toString(total));
		model.setUnread(Integer.toString(total - read));
		model.setCurrentPage(currentPage);
		model.setPageSize(pageSize);
		model.setMsgs(msgDatas);
		return model;
	}

	public MsgCountModel loadMsgCountByUser(Long userId, String app) {
		MsgCountModel model = new MsgCountModel();
		User user = userService.findById(userId);

		// CR2215
		if ((!Constants.CARDAY_ENDUSER.equals(app) && !Constants.CARDAY_ADMIN.equals(app)
				&& !Constants.CARDAY_DRIVER.equals(app))
				&& ((user.isEndUser() && !Constants.CARDAY_ENDUSER.equals(app))
						|| (user.isDriver() && !Constants.CARDAY_DRIVER.equals(app))
						|| (user.isDeptAdmin()
								&& (!Constants.CARDAY_ADMIN.equals(app) && !Constants.CARDAY_ENDUSER.equals(app)))
						|| ((user.isEntAdmin() || user.isRentEnterpriseAdmin())
								&& !Constants.CARDAY_ADMIN.equals(app)))) {
			return model;
		}

		if (user != null) {
			Long orgId = user.getOrganizationId();
			List<DateCountModel> models = messageService.getMsgCountByUser(user, orgId);
			if (!models.isEmpty()) {
				int total = 0;
				for (DateCountModel m : models) {
					int tmp = 0;
					if (BusinessConstants.MSG_TYPE_SYSTEM.equalsIgnoreCase(m.getDate())) {
						model.setSystem(m.getValue() + "");
						tmp = m.getValue();
					} else if (BusinessConstants.MSG_TYPE_TRIP.equalsIgnoreCase(m.getDate())) {
						model.setTrip(m.getValue() + "");
						tmp = m.getValue();
					} else if (BusinessConstants.MSG_TYPE_MAINTAIN.equalsIgnoreCase(m.getDate())) {
						tmp = m.getValue();
						// CR2215
						if (user.isDeptAdmin() && StringUtils.isNotBlank(app) && Constants.CARDAY_ENDUSER.equals(app)) {
							tmp = 0;
						}
						model.setMaintain(Integer.toString(tmp));
					} else {
						tmp = m.getValue();
						// CR2215
						if (user.isDeptAdmin() && StringUtils.isNotBlank(app) && Constants.CARDAY_ENDUSER.equals(app)) {
							tmp = 0;
						}
						model.setAbnormal(Integer.toString(tmp));
					}
					total = total + tmp;
				}
				model.setUnReadTotal(Integer.toString(total));
			}
		}
		return model;
	}

	public Boolean setMsgAsRead(Long userId,Long msgId){
			Message msgdata = messageService.findById(msgId);
			if(msgdata == null){
				throw new ServiceException(MessageCode.MESSAGE_CENTER_EXIST);
			}else{
				if(messageService.getReadMessageByUserId(userId, msgId) != 0){
					throw new ServiceException(MessageCode.MESSAGE_CENTER_READ);
				}else{
					messageService.saveReadMessageByUserId(userId, msgId);
				}
			}
			return true;
		}

	public Message queryMessageById(Long messageId) {
		return messageService.queryMessageById(messageId);
	}
}
