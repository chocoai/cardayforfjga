package com.cmdt.carrental.platform.service.biz.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.BusiOrderAuditRecord;
import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.entity.Message.MessageType;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.EmployeeModel;
import com.cmdt.carrental.common.model.OrderSchedule;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleSchedule;
import com.cmdt.carrental.common.service.BusiOrderService;
import com.cmdt.carrental.common.service.MessageService;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.common.model.BusiOrderDto;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PlatformOrderService {
	private static final Logger LOG = LoggerFactory.getLogger(PlatformOrderService.class);
	
    @Autowired
    private BusiOrderService busiOrderService;
    @Autowired
	private UserService userService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private CommunicationService communicationService;
    @Autowired
    private MessageService messageService;
	@Autowired
	public ShouqiService shouqiService;
    
    /**
     * orderList for App
     * @param user
     * @param OrderQueryDto
     * @return
     */
	public PagModel orderListForApp(User user, BusiOrderQueryDto dto){
		LOG.debug("PlatformOrderService.orderListForApp");
		String col_orderby=dto.getColOrderBy();
		String orderBySql=" order by t.plan_st_time desc";
		if(StringUtils.isBlank(col_orderby) || col_orderby.trim().equals("1")){
			dto.setColOrderBy(orderBySql);
		}
		//查询列表app专用
		return busiOrderService.orderListForApp(user, dto);
	}
	
	/**
     * adminList(原orderList) for portal
     * @param user
     * @param OrderQueryDto
     * @return
     */
	public PagModel orderListForPortal(User user, BusiOrderQueryDto dto) {
		LOG.debug("PlatformOrderService.orderListForPortal");
		//查询列表portal专用
		return busiOrderService.adminList(user, dto);
	}
	
	/**
	 * auditList
	 * @param user
	 * @param BusiOrderQueryDto
	 * @return
	 */
	public PagModel auditList(User user, BusiOrderQueryDto dto) {
		LOG.debug("PlatformOrderService.auditList");
		String status_arr=",0,1,2,5,6,11,12,3,13,4,15,16,";
    	String msg="";
    	if(dto.getStatus() != null){
			if (status_arr.indexOf("," + dto.getStatus() + ",") == -1) {
				msg = "status值有误!";
			}
    	}
		if(StringUtils.isNotBlank(msg)){
			throw new ServerException(msg);
		}
		return busiOrderService.auditList(user, dto);
	}
	
	/**
	 * allocateList
	 * @param user
	 * @param BusiOrderQueryDto
	 * @return
	 */
	public PagModel allocateList(User user, BusiOrderQueryDto dto) {
		LOG.debug("PlatformOrderService.arrangeList");
		String status_arr=",0,1,2,5,6,11,12,3,13,4,15,16,";
    	String msg="";
    	if(dto.getStatus() != null || status_arr.indexOf("," + dto.getStatus() + ",") == -1){
			msg = "status值有误!";
    	}
    	if(StringUtils.isNotBlank(msg)){
			throw new ServerException(msg);
		}
		return busiOrderService.allocateList(user, JsonUtils.object2Json(dto));
	}
	
	public PagModel orderListCurrentForDriver(User user, BusiOrderQueryDto dto){
		String json=JsonUtils.object2Json(dto);
		return busiOrderService.orderListCurrentForDriver(user, json);
	}
	
	public PagModel orderListHistoryForDriver(User user, BusiOrderQueryDto dto){
		String json=JsonUtils.object2Json(dto);
		return busiOrderService.orderListHistoryForDriver(user, json);
	}
	
	public List<EmployeeModel> recreateOrderEmpList(BusiOrderQueryDto dto) {
		Long orgId = dto.getOrganizationId();
		return userService.listEmployeeByOrgId(orgId);
	}
	
	public List<DriverModel> recreateOrderDriverList(BusiOrderQueryDto dto) {
		Long orgId = dto.getOrganizationId();
		return userService.listDriverByDepId(orgId);
	}

	public List<VehicleModel> recreateOrderVehicleList(BusiOrderQueryDto dto) {
		Long orgId = dto.getOrganizationId();
		return vehicleService.listDeptVehicle(orgId);
	}
	
	/**
	 * createOrder
	 * @param user
	 * @param BusiOrderDto
	 * @return
	 */
	public void createOrder(User user, BusiOrderDto dto) {
		LOG.debug("PlatformOrderService.createOrder");
		String msg=create(user,dto);
		if(StringUtils.isNotBlank(msg)){
			throw new ServerException(msg);
		}
	}
	
	public String create(User user, BusiOrderDto dto) {
		String msg = "";
		try{
			// 创建订单，必填字段，校验不能为空
			if (dto.getOrderUserid() == null) {
				msg = "用车人Id不能为空，请填写用车人Id！";
				return msg;
			}
			if (StringUtils.isBlank(dto.getCity())) {
				msg = "用车城市不能为空，请填写用车城市！";
				return msg;
			}
			if (StringUtils.isBlank(dto.getFromPlace())) {
				msg = "出发地不能为空，请填写出发地！";
				return msg;
			}
			if (StringUtils.isBlank(dto.getToPlace())) {
				msg = "目的地不能为空，请填写目的地！";
				return msg;
			}
			if (StringUtils.isBlank(dto.getPlanTime())) {
				msg = "预约用车时间不能为空，请填写预约用车时间！";
				return msg;
			}
			if (dto.getDurationTime() == null || dto.getDurationTime() <= 0) {
				msg = "预计行程时间必须大于0，请填写预计行程时间！";
				return msg;
			}
			if (StringUtils.isBlank(dto.getVehicleType())) {
				msg = "用车类型不能为空，请填写用车类型！";
				return msg;
			}
			if (dto.getPassengerNum() == null || dto.getPassengerNum() <= 0) {
				msg = "乘车人数必须大于0！";
				return msg;
			}
			if (StringUtils.isBlank(dto.getOrderReason())) {
				msg = "用车事由不能为空，请填写用车事由！";
				return msg;
			}
			if (dto.getReturnType() == null || (dto.getReturnType() != 0 && dto.getReturnType() != 1)) {
				msg = "是否往返无效,请选择是否往返！";
				return msg;
			}
			if (dto.getReturnType() == 0 && dto.getWaitTime() == null) {
				msg = "等待时长为空，请填写等待时长！";
				return msg;
			}
			Double[] fromPoint = transAddrToPoint(dto.getFromPlace());
			if (null != fromPoint) {
				dto.setFromLat(fromPoint[0]);
				dto.setFromLng(fromPoint[1]);
			}
			Double[] toPoint = transAddrToPoint(dto.getToPlace());
			if (null != toPoint) {
				dto.setToLat(toPoint[0]);
				dto.setToLng(toPoint[1]);
			}
			if (!user.getOrganizationId().equals(dto.getOrganizationId())) {
				msg = "登录用户组织Id和订单组织Id不一致！";
				return msg;
			}
			if (!user.getId().equals(dto.getOrderUserid())) {
				msg = "登录用户Id与用车人Id不一致！";
				return msg;
			}
			msg=busiOrderService.createOrder(user, JsonUtils.object2Json(dto));
		}catch(Exception e){
			throw new ServerException("createOrder failed.");
		}
		return msg;
	}
	
	public Double[] transAddrToPoint(String address) throws Exception{
		Double[] dou=null;
		Map<String, Object> result =new ServiceAdapter(shouqiService).doService(ActionName.REVERSEADDRESS, new Object[] { address });
		if (result.get("data") != null) {
			ObjectMapper MAPPER = new ObjectMapper();
			JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
			if ("000".equals(jsonNode.get("status").asText())) {
				JsonNode rs1 = (JsonNode) jsonNode.get("result");
				//{"status":0,"result":{"location":{"lng":121.61513124890567,"lat":31.250432343160256},"precise":1,"confidence":80,"level":"道路"}}
				if ("0".equals(rs1.get("status").asText())) {
					JsonNode rs2 = (JsonNode) rs1.get("result").get("location");
					dou=new Double[2];
					dou[0]=rs2.get("lat").asDouble();
					dou[1]=rs2.get("lng").asDouble();
				}
			}
		}
		return dou;
	}
	
	/**
	 * updateOrder
	 * @param orderId
	 * @return
	 */
	public void updateOrder(User user, BusiOrderDto dto) {
		LOG.debug("PlatformOrderService.updateOrder");
		String msg=update(user,dto);
		if(StringUtils.isNotBlank(msg)){
			throw new ServerException(msg);
		}
	}
	
	public String update(User user, BusiOrderDto dto) {
		String msg = "";
		try{
			// 修改订单，必填字段，校验不能为空
			if (dto.getOrderUserid() == null) {
				msg = "用车人Id不能为空，请填写用车人Id！";
				return msg;
			}
			if (StringUtils.isBlank(dto.getCity())) {
				msg = "用车城市不能为空，请填写用车城市！";
				return msg;
			}
			if (StringUtils.isBlank(dto.getFromPlace())) {
				msg = "出发地不能为空，请填写出发地！";
				return msg;
			}
			if (StringUtils.isBlank(dto.getToPlace())) {
				msg = "目的地不能为空，请填写目的地！";
				return msg;
			}
			if (StringUtils.isBlank(dto.getPlanTime())) {
				msg = "预约用车时间不能为空，请填写预约用车时间！";
				return msg;
			}
			if (dto.getDurationTime() == null || dto.getDurationTime() <= 0) {
				msg = "预计行程时间必须大于0，请填写预计行程时间！";
				return msg;
			}
			if (StringUtils.isBlank(dto.getVehicleType())) {
				msg = "用车类型不能为空，请填写用车类型！";
				return msg;
			}
			if (dto.getPassengerNum() == null || dto.getPassengerNum() <= 0) {
				msg = "乘车人数必须大于0！";
				return msg;
			}
			if (StringUtils.isBlank(dto.getOrderReason())) {
				msg = "用车事由不能为空，请填写用车事由！";
				return msg;
			}
			if (dto.getReturnType() == null || (dto.getReturnType() != 0 && dto.getReturnType() != 1)) {
				msg = "是否往返无效,请选择是否往返！";
				return msg;
			}
			if (dto.getReturnType() == 0 && dto.getWaitTime() == null) {
				msg = "等待时长为空，请填写等待时长！";
				return msg;
			}
			Double[] fromPoint = transAddrToPoint(dto.getFromPlace());
			if (null != fromPoint) {
				dto.setFromLat(fromPoint[0]);
				dto.setFromLng(fromPoint[1]);
			}
			Double[] toPoint = transAddrToPoint(dto.getToPlace());
			if (null != toPoint) {
				dto.setToLat(toPoint[0]);
				dto.setToLng(toPoint[1]);
			}
			BusiOrder busiOrder = busiOrderService.findOne(dto.getId());
			// 只有部门管理员、员工可以修改订单
			if (user.getUserCategory() != 3 && user.getUserCategory() != 4) {
				return "该用户没有修改订单的权限！";
			}
			if (!dto.getOrderUserid().equals(busiOrder.getOrderUserid())) {
				return "只能修改自己的订单！";
			}
			msg=busiOrderService.updateOrder(user, JsonUtils.object2Json(dto));
		}catch(Exception e){
			throw new ServerException("updateOrder failed.");
		}
		return msg;
	}
	
	/**
	 * recreateOrder
	 * @param user
	 * @param BusiOrderQueryDto
	 * @return
	 */
	public void recreateOrder(User user, BusiOrderDto dto) {
		LOG.debug("PlatformOrderService.recreateOrder");
		String msg=recreate(user,dto);
		if(StringUtils.isNotBlank(msg)){
			throw new ServerException(msg);
		}
	}
	
	public String recreate(User user, BusiOrderDto dto) {
		// 补录订单，必填字段，校验不能为空
		String msg = "";
		if (dto.getOrderUserid() == null) {
			msg = "用车人Id不能为空，请填写用车人Id！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getCity())) {
			msg = "用车城市不能为空，请填写用车城市！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getFromPlace())) {
			msg = "出发地不能为空，请填写出发地！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getToPlace())) {
			msg = "目的地不能为空，请填写目的地！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getFactStTime())) {
			msg = "实际开始时间不能为空，请填写实际开始时间！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getFactEdTime())) {
			msg = "实际结束时间不能为空，请填写实际结束时间！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getVehicleType())) {
			msg = "用车类型不能为空，请填写用车类型！";
			return msg;
		}
		if (dto.getPassengerNum() == null || dto.getPassengerNum() <= 0) {
			msg = "乘车人数必须大于0！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getOrderReason())) {
			msg = "用车事由不能为空，请填写用车事由！";
			return msg;
		}
		if (dto.getReturnType() == null || (dto.getReturnType() != 0 && dto.getReturnType() != 1)) {
			msg = "是否往返无效,请选择是否往返！";
			return msg;
		}
		if (dto.getReturnType() == 0 && dto.getWaitTime() == null) {
			msg = "等待时长为空，请填写等待时长！";
			return msg;
		}
		if (dto.getDriverId() == null) {
			msg = "司机Id为空，请填写司机Id！";
			return msg;
		}
		if (dto.getVehicleId() == null) {
			msg = "车辆Id为空，请填写车辆Id！";
			return msg;
		}
		if (!user.getOrganizationId().equals(dto.getOrganizationId())) {
			msg = "登录用户组织Id和订单组织Id不一致！";
			return msg;
		}
		if (!user.getId().equals(dto.getOrderUserid())) {
			msg = "登录用户Id与用车人Id不一致！";
			return msg;
		}
		return busiOrderService.recreateOrder(user, JsonUtils.object2Json(dto));
	}
	
	/**
	 * updateReCreateOrder
	 * @param user
	 * @param dto
	 * @return
	 */
	public void updateReCreateOrder(User user, BusiOrderDto dto) {
		LOG.debug("PlatformOrderService.updateReCreateOrder");
		String msg=updateReCreate(user,dto);
		if(StringUtils.isNotBlank(msg)){
			throw new ServerException(msg);
		}
	}
	
	public String updateReCreate(User user, BusiOrderDto dto) {
		// 补录订单修改，必填字段，校验不能为空
		String msg = "";
		if (dto.getOrderUserid() == null) {
			msg = "用车人Id不能为空，请填写用车人Id！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getCity())) {
			msg = "用车城市不能为空，请填写用车城市！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getFromPlace())) {
			msg = "出发地不能为空，请填写出发地！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getToPlace())) {
			msg = "目的地不能为空，请填写目的地！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getFactStTime())) {
			msg = "实际开始时间不能为空，请填写实际开始时间！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getFactEdTime())) {
			msg = "实际结束时间不能为空，请填写实际结束时间！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getVehicleType())) {
			msg = "用车类型不能为空，请填写用车类型！";
			return msg;
		}
		if (dto.getPassengerNum() == null || dto.getPassengerNum() <= 0) {
			msg = "乘车人数必须大于0！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getOrderReason())) {
			msg = "用车事由不能为空，请填写用车事由！";
			return msg;
		}
		if (dto.getReturnType() == null || (dto.getReturnType() != 0 && dto.getReturnType() != 1)) {
			msg = "是否往返无效,请选择是否往返！";
			return msg;
		}
		if (dto.getReturnType() == 0 && dto.getWaitTime() == null) {
			msg = "等待时长为空，请填写等待时长！";
			return msg;
		}
		if (dto.getDriverId() == null) {
			msg = "司机Id为空，请填写司机Id！";
			return msg;
		}
		if (dto.getVehicleId() == null) {
			msg = "车辆Id为空，请填写车辆Id！";
			return msg;
		}
		Long loginUserId = dto.getLoginUserId();
		User loginUser = userService.findById(loginUserId);
		BusiOrder busiOrder = busiOrderService.findOne(dto.getId());
		if (!dto.getOrderUserid().equals(busiOrder.getOrderUserid())) {
			return "只能修改自己的订单！";
		}
		// 只要部门管理员能修改补录订单
		if (loginUser.getUserCategory() != 3) {
			return "该用户没有修改补录订单的权限！";
		}
		return busiOrderService.updateReCreateOrder(user, JsonUtils.object2Json(dto));
	}
	
	/**
	 * deleteOrder
	 * @param user
	 * @param id
	 * @return
	 */
	public void deleteOrder(User user, Long id){
		String msg= busiOrderService.deleteOrder(user, id);
		if(StringUtils.isNotBlank(msg)){
			throw new ServerException(msg);
		}
	}
	
	/**
	 * viewOrder
	 * @param orderId
	 * @return
	 */
	public BusiOrder viewOrder(Long loginUserId,Long orderId) {
		LOG.debug("PlatformOrderService.findOneOrder");
		User loginUser=userService.findById(loginUserId);
		String msg="";
		if (orderId == null){
			msg="订单id不能为空!";
        }
		if(belongToLoginUser(orderId,loginUser)){
			BusiOrder order=busiOrderService.findOne(orderId);
			if (null == order){
				msg="未找到数据!";
	        }
			if(StringUtils.isNotBlank(msg)){
				throw new ServerException(msg);
			}
			return order;
		}else{
			return null;
		}
	}
	
	/**
	 * orderAudit
	 * @param user
	 * @param dto
	 * @return
	 */
	public void orderAudit(User user, BusiOrderDto dto) {
		LOG.debug("PlatformOrderService.orderAudit");
		String msg= audit(user, dto);
		if(StringUtils.isNotBlank(msg)){
			throw new ServerException(msg);
		}
	}
	
	public String audit(User user, BusiOrderDto dto) {
		String msg="";
		if(dto.getId()==null){
			msg="订单id不能为空!";
			return msg;
		}
		if(belongToLoginUser(dto.getId(),user)){
			msg=busiOrderService.orderAudit(user, JsonUtils.object2Json(dto));
		}else{
			msg=Constants.API_MESSAGE_ERROR_MSG_NO_PREMISSION;
		}
		if (StringUtils.isBlank(msg)){
			//进行消息推送和数据入库
			sendPush(dto.getId(), dto.getStatus());
		}
		return msg;
	}
	
	/**
	 * auditSendMsg
	 * @param user
	 * @param sendMsg
	 * @param phoneNumber
	 * @return
	 */
	public void auditSendMsg(User user, BusiOrderDto dto) {
		LOG.debug("PlatformOrderService.auditSendMsg");
		String msg= sendMsg(user, dto);
		if(StringUtils.isNotBlank(msg)){
			throw new ServerException(msg);
		}
	}
	
	public String sendMsg(User user, BusiOrderDto dto) {
		String msg="";
		if(StringUtils.isBlank(dto.getPhoneNumber())){
			msg="phone number不能为空!";
			return msg;
		}
		if(StringUtils.isBlank(dto.getSendMsg())){
			msg="sendMsg不能为空!";
			return msg;
		}
		String json=JsonUtils.object2Json(dto);
		msg=busiOrderService.auditSendMsg(user,json);
		return msg;
	}
	
	/**
	 * auditHistory
	 * @param user
	 * @param orderId
	 * @return
	 */
	public List<BusiOrderAuditRecord> auditHistory(User user, Long orderId) {
		LOG.debug("PlatformOrderService.auditHistory");
		return busiOrderService.findAuditHistory(user, orderId);
	}
	
	/**
	 * getAvailableDrivers
	 * @param loginUser
	 * @param orderId
	 * @param currentPage
	 * @param numPerPage
	 * @return
	 */
	public PagModel getAvailableDrivers(User loginUser, BusiOrderQueryDto dto){
		String msg="";
		if (dto.getId() == null)
        {
			msg="订单id不能为空!";
        }
		if(StringUtils.isNotBlank(msg)){
			throw new ServerException(msg);
		}
		return busiOrderService.getAvailableDrivers(loginUser,dto);
	}
	
	/**
	 * showOrderSchedule
	 * @param vehicleId
	 * @param orderId
	 * @return
	 */
	public List<OrderSchedule> showOrderSchedule(Long vehicleId, Long orderId){
		return busiOrderService.showOrderSchedule(vehicleId, orderId);
	}
	
	/**
	 * orderAllocate
	 * @param user
	 * @param dto
	 * @return
	 */
	public void orderAllocate(User user, BusiOrderDto dto){
		String msg= allocate(user, dto);
		if(StringUtils.isNotBlank(msg)){
			throw new ServerException(msg);
		}
	}
	
	public String allocate(User user, BusiOrderDto dto){
		String msg="";
		if(dto.getId()==null){
			msg="订单id不能为空!";
			return msg;
		}
		if(dto.getVehicleId()==null){
			msg="车辆id不能为空!";
			return msg;
		}
		if(dto.getDriverId()==null){
			msg="司机id不能为空!";
			return msg;
		}
		BusiOrder order=busiOrderService.findOne(dto.getId());
		if(order.getStatus()!=1){
			msg="排车订单id无效!";
			return msg;
		}
		msg=busiOrderService.orderAllocate(user, JsonUtils.object2Json(dto));
		if(StringUtils.isBlank(msg)){
            // 开始消息推送和入库
            // 用车人进行提醒
            sendPush(dto.getId(), 91);
            // 司机进行提醒
            sendPush(dto.getId(), 95);
		}
		return msg;
	}
	
	public void orderVehicleOut(User user, BusiOrderDto dto){
		String msg= vehicleOut(user, dto);
		if(StringUtils.isNotBlank(msg)){
			throw new ServerException(msg);
		}
	}
	
	public String vehicleOut(User user, BusiOrderDto dto){
		String msg="";
		if(dto.getId()==null){
			msg="订单id不能为空!";
			return msg;
		}
		String json=JsonUtils.object2Json(dto);
		msg=busiOrderService.orderVehicleOut(user,json);
		if (StringUtils.isBlank(msg)){
            sendPush(dto.getId(), 92);
		}
		return msg;
	}
	
	public void orderReachFromPlace(User user, BusiOrderDto dto){
		String msg= reachFromPlace(user, dto);
		if(StringUtils.isNotBlank(msg)){
			throw new ServerException(msg);
		}
	}
	
	public String reachFromPlace(User user, BusiOrderDto dto){
		String msg="";
		if(dto.getId()==null){
			msg="订单id不能为空!";
			return msg;
		}
		String json=JsonUtils.object2Json(dto);
		msg=busiOrderService.orderReachFromPlace(user,json);
		 if (StringUtils.isBlank(msg)){
             sendPush(dto.getId(), 93);
		 }
		return msg;
	}
	
	public void orderOngoing(User user, BusiOrderDto dto){
		String msg= ongoing(user, dto);
		if(StringUtils.isNotBlank(msg)){
			throw new ServerException(msg);
		}
	}
	
	public String ongoing(User user, BusiOrderDto dto) {
		String msg="";
		try{
			if(dto.getId()==null){
				msg="订单id不能为空!";
				return msg;
			}
			String json=JsonUtils.object2Json(dto);
			msg=busiOrderService.orderOngoing(user,json);
		}catch(Exception e){
			throw new ServerException("order Ongoing failed.");
		}
		return msg;
	}
	
	public void orderWaiting(User user, BusiOrderDto dto){
		String msg= waiting(user, dto);
		if(StringUtils.isNotBlank(msg)){
			throw new ServerException(msg);
		}
	}
	
	public String waiting(User user, BusiOrderDto dto){
		String msg="";
		if(dto.getId()==null){
			msg="订单id不能为空!";
			return msg;
		}
		String json=JsonUtils.object2Json(dto);
		msg=busiOrderService.orderWaiting(user,json);
		return msg;
	}
	
	public void orderFinish(User user, BusiOrderDto dto) {
		String msg= finish(user, dto);
		if(StringUtils.isNotBlank(msg)){
			throw new ServerException(msg);
		}
	}
	
	public String finish(User user, BusiOrderDto dto) {
		String msg="";
		try{
			if(dto.getId()==null){
				msg="订单id不能为空!";
				return msg;
			}
			String json=JsonUtils.object2Json(dto);
			msg=busiOrderService.orderFinish(user,json);
			if (StringUtils.isBlank(msg)){
	            sendPush(dto.getId(), 94);
			}
		}catch(Exception e){
			throw new ServerException("order finish failed.");
		}
		return msg;
	}
	
	/**
	 * cancelOrder
	 * @param user
	 * @param id
	 * @return
	 */
	public void orderCancel(User user, Long id){
		String msg=busiOrderService.cancelOrder(user, id, null);
		if(StringUtils.isNotBlank(msg)){
			throw new ServerException(msg);
		}
	}
	
	/**
	 * queryVehicleSchedule
	 * @param vehicleId
	 * @param planStTime
	 * @return
	 */
	public List<VehicleSchedule> queryVehicleSchedule(Long vehicleId, String planStTime){
		return busiOrderService.queryVehicleSchedule(vehicleId, planStTime);
	}
	
	public boolean belongToLoginUser(Long orderId,User loginUser){
		boolean boo=false;
		Long orgId=loginUser.getOrganizationId();
		BusiOrder order=busiOrderService.findOne(orderId);
		if(order.getOrderUserid().equals(loginUser.getId())){
			boo=true;
		}else{
			if(loginUser.isEntAdmin() || loginUser.isDeptAdmin()){
				Long deptId=order.getOrganizationId();//订单所在部门ID
				List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, true, true);
				List<Long> orgIds = new ArrayList<>();
				if(!orgList.isEmpty()){
					for(Organization org : orgList){
						orgIds.add(org.getId());
					}
				}
				if(deptId.equals(orgId)||orgIds.contains(deptId)){
					boo=true;
				}
			}else if(loginUser.isEndUser()){
				if(order.getOrderUserid().equals(loginUser.getId())){
					boo=true;
				}
			}else if(loginUser.isDriver()){
				if(order.getDriverId().equals(loginUser.getId())){
					boo=true;
				}
			}
		}
		return boo;
	}
	
	/**
	 * 处理完状态需要给用车人发送信息状态
	 * @param orderId
	 * @param status
	 */
	protected void sendPush(Long orderId, int status)
    {
		LOG.debug("sendPush(orderId:"+orderId+",status:"+status+")");
		
        boolean isOrderUser = true;
        
        // 获得这条订单信息
        BusiOrder order = busiOrderService.findOrderByIdSimple(orderId);
        // 通过订单ID来找到使用人ID
        Long userId = order.getOrderUserid();
        // 查询用车人的信息;
        User orderUser = userService.findById(userId);
        // 通过订单ID来找到车辆VEHICLE ID
        Long vehicleId = order.getVehicleId();
        Vehicle vehicle = vehicleService.findVehicleById(vehicleId);
        // 通过订单ID来找到司机DRIVER ID
        Long driverId = order.getDriverId();
        // 进行消息推送
        String msgContent = "";
        String title = "";
        String phoneNum = "";
        User driverUser = null;
        switch (status)
        {
            case 1:// 审核通过，给予通过通知
                phoneNum = orderUser.getPhone();
                msgContent = "您的订单" + order.getOrderNo() + "已于" + DateUtils.dateToString(new Date()) + "审核通过!";
                title = "订单审核通过提醒";
                break;
            case 5:// 审核不通过，给予不通过通知
                phoneNum = orderUser.getPhone();
                msgContent =
                    "您的订单" + order.getOrderNo() + "已于" + DateUtils.dateToString(new Date()) + "审核未通过,请前往我的行程查看详情!";
                title = "订单审核未通过提醒";
                break;
            case 91:// 管理员已经完成排车
                phoneNum = orderUser.getPhone();
                // vehicle = vehicleService.findVehicleById(vehicleId);
                driverUser = userService.findById(driverId);
                msgContent = "您的订单" + order.getOrderNo() + "已于" + DateUtils.dateToString(new Date()) + "完成排车,车牌号为 "
                    + vehicle.getVehicleNumber() + ",司机为" + driverUser.getUsername() + ", 手机号为 " + driverUser.getPhone()
                    + ", 请留意行程开始时间，并准时在出发地等待!";
                title = "排车提醒";
                break;
            case 92:// 司机已出车执行任务
                phoneNum = orderUser.getPhone();
                // vehicle = vehicleService.findVehicleById(vehicleId);
                driverUser = userService.findById(driverId);
                msgContent = "您的订单" + order.getOrderNo() + "司机" + driverUser.getUsername() + "已出车,车牌号"
                    + vehicle.getVehicleNumber() + ",手机号为" + driverUser.getPhone() + ", 预约用车时间为"
                    + DateUtils.dateToString(order.getPlanStTime()) + ",请留意行程开始时间并准时在出发地等待!";
                title = "出车提醒";
                break;
            case 93:// 司机已到达出发地
                phoneNum = orderUser.getPhone();
                // vehicle = vehicleService.findVehicleById(vehicleId);
                driverUser = userService.findById(driverId);
                msgContent = "您的订单" + order.getOrderNo() + "司机" + driverUser.getUsername() + "已到达出发地,车牌号"
                    + vehicle.getVehicleNumber() + ",手机号为" + driverUser.getPhone() + ", 预约用车时间为"
                    + DateUtils.dateToString(order.getPlanStTime()) + ",请留意行程开始时间并准时上车!";
                title = "到达出发地提醒";
                break;
            case 94:// 车到达目的地
                
                phoneNum = orderUser.getPhone();
                driverUser = userService.findById(driverId);
                msgContent = "您的订单" + order.getOrderNo() + "司机" + driverUser.getUsername() + "已经到达目的地，请完成支付!";
                title = "到达目的地提醒";
                break;
            case 95:// 司机被安排任务提醒
                isOrderUser = false;
                driverUser = userService.findById(driverId);
                // vehicle = vehicleService.findVehicleById(vehicleId);
                phoneNum = driverUser.getPhone();
                // msgContent = "您有一条新的任务，预约用车时间" + DateUtil.dateToString(order.getPlanStTime()) + "，车牌号"
                // + vehicle.getVehicleNumber() + "，出发地" + order.getFromPlace() + "，目的地" + order.getToPlace()
                // + "，乘客张先生，手机号" + orderUser.getPhone() + "，请留意行程开始时间并准时出发";
                msgContent = "您已被安排新的任务，请注意查看详情!";
                title = "任务提醒";
                break;
            default:
                break;
        }
        communicationService.sendPush(Arrays.asList(phoneNum),
            title,
            msgContent,
            isOrderUser ? com.cmdt.carrental.common.constants.Constants.CARDAY_ENDUSER
                : com.cmdt.carrental.common.constants.Constants.CARDAY_DRIVER);
        
        // 消息入库
        Message message = new Message();
        message.setType(isOrderUser ? MessageType.TRAVEL : MessageType.TASK);
        message.setTime(new Date());
        message.setTitle(title);
        message.setMsg(msgContent);
        message.setCarNo(null != vehicle ? vehicle.getVehicleNumber() : null);
        message.setOrderId(orderId);
        message.setIsNew(1);
        message.setOrgId(orderUser.getOrganizationId());
        messageService.saveMessages(Arrays.asList(message));
    }
	
	
	/**
	 * sendPush
	 * @param recievers
	 * @param title
	 * @param msgContent
	 * @param app
	 */
	protected void sendPush(List<String> recievers, String title, String msgContent, String app){
		LOG.debug("sendPush(title:"+title+",msgContent:"+msgContent+",app:"+app+")");
		communicationService.sendPush(recievers, title, msgContent, app);
	}
}
