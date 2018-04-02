package com.cmdt.carrental.common.service;

import java.sql.Timestamp;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carday.microservice.common.model.Order.OrderDto;
import com.cmdt.carday.microservice.common.model.Order.enums.OrderType;
import com.cmdt.carday.microservice.common.model.response.MessageCode;
import com.cmdt.carday.microservice.common.model.response.exception.ServiceException;
import com.cmdt.carrental.common.bean.ModuleName;
import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.dao.BusiOrderDao;
import com.cmdt.carrental.common.dao.DriverDao;
import com.cmdt.carrental.common.dao.UserDao;
import com.cmdt.carrental.common.dao.VehicleDao;
import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.BusiOrderAuditRecord;
import com.cmdt.carrental.common.entity.BusiOrderIgnore;
import com.cmdt.carrental.common.entity.Driver;
import com.cmdt.carrental.common.entity.EventConfig;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;
import com.cmdt.carrental.common.model.OrderSchedule;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.SmsComposeModel;
import com.cmdt.carrental.common.model.VehReturnRegistDto;
import com.cmdt.carrental.common.model.VehReturnRegistModel;
import com.cmdt.carrental.common.model.VehicleSchedule;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.common.util.TypeUtils;

@Service
public class BusiOrderServiceImpl implements BusiOrderService {
	private static final Logger LOG = LoggerFactory.getLogger(BusiOrderServiceImpl.class);
	
    @Autowired
    private BusiOrderDao orderDao;
    @Autowired
    private UserService userService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
	private VehicleService vehicleService;
    @Autowired
	private RuleService ruleService;
    @Autowired
    private CommunicationService communicationService;
    @Autowired
    VehicleAlertService vehicleAlertService;
    @Autowired
    private DriverDao driverDao;
    @Autowired
    private VehicleDao vehicleDao;
    
    @Autowired
    private SmsService smsService;
    
    /**
     * generateLoginUserIDforOrder
     * @param userId
     * @return
     */
    private String generateLoginUserIDforOrder(Long userId){
    	int len = userId.toString().length();
    	String userStrNo = userId.toString();
    	if(len <= 6){
    		String regx = "";
    		for(int i = 0 ; i < 6 - len; i++){
    			regx += "0";
    		}
    		userStrNo = regx.concat(userStrNo);
    	}else{
    		userStrNo = userStrNo.substring(len - 6);
    	}
    	return userStrNo;
    }
    
    /**
     * subTimeStringforOrder
     * @param timemillis
     * @return
     */
    private String subTimeStringforOrder(Long timemillis){
    	int len = String.valueOf(timemillis).length();
    	String timeStrNo = String.valueOf(timemillis);
    	if(len > 6){
    		timeStrNo = timeStrNo.substring(len - 6);
    	}
    	return timeStrNo;
    }
    
    
    @SuppressWarnings("unchecked")
	@Override
	public String createOrder(User loginUser, String json) {
		String msg="";
		//管理员或员工
		if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()||loginUser.isEndUser()) {
			BusiOrder order=new BusiOrder();
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			String orderNo=DateUtils.getNowDate("yyyyMMdd") + generateLoginUserIDforOrder(loginUser.getId()) + subTimeStringforOrder(System.currentTimeMillis());//201504A000001
			order.setOrderNo(orderNo);
			order.setOrderTime(new Timestamp(System.currentTimeMillis()));
			//orderType
			int orderType = TypeUtils.obj2Integer(jsonMap.get("orderType"));
			order.setOrderType(orderType);
			if(orderType == 0){//toB
				order.setStatus(0);
			}else{//toC
				order.setStatus(1);
			}
			Long orderUserid=TypeUtils.obj2Long(jsonMap.get("orderUserid"));
			User orderUser=userService.findOne(orderUserid);
			Organization org_orderUser=organizationService.findOne(orderUser.getOrganizationId());
			if(orderType == 0){
				if(org_orderUser.getParentId()==0){
					msg="用车人还未分配部门，无法下单，请联系部门管理员!";
					return msg;
				}
			}
			order.setOrderUserid(orderUserid);
			order.setOrderUsername(TypeUtils.obj2String(jsonMap.get("orderUsername")));
			order.setOrderUserphone(TypeUtils.obj2String(jsonMap.get("orderUserphone")));
			order.setCity(TypeUtils.obj2String(jsonMap.get("city")));
			order.setFromPlace(TypeUtils.obj2String(jsonMap.get("fromPlace")));
			order.setToPlace(TypeUtils.obj2String(jsonMap.get("toPlace")));
			order.setPlanStTime(TypeUtils.obj2Date(jsonMap.get("planTime")));
			Double durationTime=TypeUtils.obj2Double(jsonMap.get("durationTime"));//单位分钟,预计行程时间
			Double waitTime=TypeUtils.obj2Double(jsonMap.get("waitTime"));//单位分钟,等待时长
			order.setDurationTime(durationTime);
			order.setWaitTime(waitTime);
			Double totalTime = durationTime + waitTime;
//			LOG.info("order create cal-------------:"+(int)(totalTime.doubleValue()));
			order.setPlanEdTime(DateUtils.addMinutes(order.getPlanStTime(), (int)(totalTime.doubleValue())));
			String vehicleType=TypeUtils.obj2String(jsonMap.get("vehicleType"));
			if(!vehicleType.equals("0")&&!vehicleType.equals("1")&&!vehicleType.equals("2")&&!vehicleType.equals("3")){
				msg = "车辆类型不存在！";
				return msg;
			}
			order.setVehicleType(vehicleType);
			order.setPassengerNum(TypeUtils.obj2Integer(jsonMap.get("passengerNum")));
			order.setOrderReason(TypeUtils.obj2String(jsonMap.get("orderReason")));
			order.setReturnType(TypeUtils.obj2Integer(jsonMap.get("returnType")));
			order.setComments(TypeUtils.obj2String(jsonMap.get("comments")));
			order.setOrganizationId(TypeUtils.obj2Long(jsonMap.get("organizationId")));
			//lng,lat
			order.setFromLat(TypeUtils.obj2Double(jsonMap.get("fromLat")));
			order.setFromLng(TypeUtils.obj2Double(jsonMap.get("fromLng")));
			order.setToLat(TypeUtils.obj2Double(jsonMap.get("toLat")));
			order.setToLng(TypeUtils.obj2Double(jsonMap.get("toLng")));
			
			//ADD SECRECT LEVEL
			Integer secretLevel = TypeUtils.obj2Integer(jsonMap.get("secretLevel"));
			order.setSecretLevel( (secretLevel == null || secretLevel == 0) ? null : secretLevel);
			
			order.setVehicleUsage(TypeUtils.obj2Integer(jsonMap.get("vehicleUsage")));
			order.setDrivingType(TypeUtils.obj2Integer(jsonMap.get("drivingType")));
   			order.setUnitName(orderUser.getOrganizationName());
			
			msg = ruleService.getOrderValidateResult(order, loginUser.getId());
			if(StringUtils.isEmpty(msg)){//规则验证通过
				orderDao.createOrder(order);
			}
		} else {
			msg = "该用户不能创建订单！";
		}
		return msg;
	}
    
    @SuppressWarnings("unchecked")
   	@Override
   	public String createWithoutApprovalOrder(User loginUser, String json) {
   		String msg="";
   		//管理员或员工
   		if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()||loginUser.isEndUser()) {
   			BusiOrder order=new BusiOrder();
   			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
   			String orderNo=DateUtils.getNowDate("yyyyMMdd") + generateLoginUserIDforOrder(loginUser.getId()) + subTimeStringforOrder(System.currentTimeMillis());//201504A000001
   			order.setOrderNo(orderNo);
   			order.setOrderTime(new Timestamp(System.currentTimeMillis()));
//   			//orderType
//   			int orderType = TypeUtils.obj2Integer(jsonMap.get("orderType"));
   			order.setOrderType(0);
//   			if(orderType == 0){//toB
//   				order.setStatus(0);
//   			}else{//toC
   			order.setStatus(2);
//   			}
   			Long orderUserid=TypeUtils.obj2Long(jsonMap.get("orderUserid"));
   			User orderUser=userService.findOne(orderUserid);
//   			Organization org_orderUser=organizationService.findOne(orderUser.getOrganizationId());
//   			if(orderType == 0){
//   				if(org_orderUser.getParentId()==0){
//   					msg="用车人还未分配部门，无法下单，请联系部门管理员!";
//   					return msg;
//   				}
//   			}
   			order.setOrderUserid(orderUserid);
   			order.setOrderUsername(TypeUtils.obj2String(jsonMap.get("orderUsername")));
   			order.setOrderUserphone(TypeUtils.obj2String(jsonMap.get("orderUserphone")));
   			order.setCity(TypeUtils.obj2String(jsonMap.get("city")));
   			order.setFromPlace(TypeUtils.obj2String(jsonMap.get("fromPlace")));
   			order.setToPlace(TypeUtils.obj2String(jsonMap.get("toPlace")));
   			order.setPlanStTime(TypeUtils.obj2Date(jsonMap.get("planTime")));
   			Double durationTime=TypeUtils.obj2Double(jsonMap.get("durationTime"));//单位分钟,预计行程时间
   			Double waitTime=TypeUtils.obj2Double(jsonMap.get("waitTime"));//单位分钟,等待时长
   			order.setDurationTime(durationTime);
   			order.setWaitTime(waitTime);
   			Double totalTime = durationTime + waitTime;
//   			LOG.info("order create cal-------------:"+(int)(totalTime.doubleValue()));
   			order.setPassengerNum(TypeUtils.obj2Integer(jsonMap.get("passengerNum")));
   			order.setOrderReason(TypeUtils.obj2String(jsonMap.get("orderReason")));
   			order.setReturnType(TypeUtils.obj2Integer(jsonMap.get("returnType")));
   			order.setComments(TypeUtils.obj2String(jsonMap.get("comments")));
   			order.setOrganizationId(loginUser.getOrganizationId());
   			//lng,lat
   			order.setFromLat(TypeUtils.obj2Double(jsonMap.get("fromLat")));
   			order.setFromLng(TypeUtils.obj2Double(jsonMap.get("fromLng")));
   			order.setToLat(TypeUtils.obj2Double(jsonMap.get("toLat")));
   			order.setToLng(TypeUtils.obj2Double(jsonMap.get("toLng")));
   			
   			order.setPlanStTime(new Timestamp(System.currentTimeMillis()));
			order.setPlanEdTime(new Timestamp(System.currentTimeMillis()));
   			
   			//ADD SECRECT LEVEL
			Integer secretLevel = TypeUtils.obj2Integer(jsonMap.get("secretLevel"));
			order.setSecretLevel( (secretLevel == null || secretLevel == 0) ? null : secretLevel);
   			
   			order.setVehicleUsage(TypeUtils.obj2Integer(jsonMap.get("vehicleUsage")));
   			order.setDrivingType(TypeUtils.obj2Integer(jsonMap.get("drivingType")));
   			order.setUnitName(TypeUtils.obj2String(jsonMap.get("unitName")));
   			order.setVehicleId(TypeUtils.obj2Long(jsonMap.get("vehicleId")));
   			
   			msg = ruleService.getOrderValidateResult(order, loginUser.getId());
   			if(StringUtils.isEmpty(msg)){//规则验证通过
   				orderDao.createWithoutApprovalOrder(order);
   			}
   		} else {
   			msg = "该用户不能创建订单！";
   		}
   		return msg;
   	}
    

	@Override
	public BusiOrder createOrder(User loginUser, OrderDto orderDto ) {
		String msg="";
		//管理员或员工
		if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()||loginUser.isEndUser()) {
			BusiOrder order=new BusiOrder();
			String orderNo=DateUtils.getNowDate("yyyyMMdd") + generateLoginUserIDforOrder(loginUser.getId()) + subTimeStringforOrder(System.currentTimeMillis());//201504A000001
			order.setOrderNo(orderNo);
			order.setOrderTime(new Timestamp(System.currentTimeMillis()));
			//orderType
			if (orderDto.getOrderType() == null) {
				orderDto.setOrderType(OrderType.Enterprise);
			}
			int orderType = orderDto.getOrderType().getType();
			order.setOrderType(orderType);
			if(orderType == 0){//toB
				order.setStatus(0);
			}else{//toC
				order.setStatus(1);
			}
			Long orderUserid= orderDto.getOrderUserid();
			User orderUser=userService.findOne(orderUserid);
			Organization org_orderUser=organizationService.findOne(orderUser.getOrganizationId());
			if(orderType == 0){
				if(org_orderUser.getParentId()==0){
					throw new ServiceException(MessageCode.COMMON_FAILURE,
							"用车人还未分配部门，无法下单，请联系部门管理员!");
				}
			}
			order.setOrderUserid(orderUserid);
//			order.setOrderUsername(TypeUtils.obj2String(jsonMap.get("orderUsername")));
//			order.setOrderUserphone(TypeUtils.obj2String(jsonMap.get("orderUserphone")));
			order.setCity(orderDto.getCity());
			order.setFromPlace(orderDto.getFromPlace());
			order.setToPlace(orderDto.getToPlace());
			order.setPlanStTime(orderDto.getPlanTime());
			Double durationTime= orderDto.getDurationTime();//单位分钟,预计行程时间

			Double waitTime=orderDto.getWaitTime() == null ? 0 : orderDto.getWaitTime();//单位分钟,等待时长
			order.setDurationTime(durationTime);
			order.setWaitTime(waitTime);
			Double totalTime = durationTime + waitTime;
//			LOG.info("order create cal-------------:"+(int)(totalTime.doubleValue()));
			order.setPlanEdTime(DateUtils.addMinutes(order.getPlanStTime(), (int)(totalTime.doubleValue())));
//			if(!vehicleType.equals("0")&&!vehicleType.equals("1")&&!vehicleType.equals("2")&&!vehicleType.equals("3")){
//				msg = "车辆类型不存在！";
//				return msg;
//			}
			order.setVehicleType(orderDto.getVehicleType().getType().toString());
			order.setPassengerNum(orderDto.getPassengerNum());
			order.setOrderReason(orderDto.getOrderReason());
			order.setReturnType(orderDto.getReturnType().getType());
			order.setComments(orderDto.getComments());
			order.setOrganizationId(orderDto.getOrganizationId());
			//lng,lat
			order.setFromLat(orderDto.getFromLat());
			order.setFromLng(orderDto.getFromLng());
			order.setToLat(orderDto.getToLat());
			order.setToLng(orderDto.getToLng());

			msg = ruleService.getOrderValidateResult(order, loginUser.getId());
			if(StringUtils.isEmpty(msg)){//规则验证通过
				return orderDao.createOrder(order);
			} else {
				throw new ServiceException(MessageCode.RULE_UNSATISFACTORY, msg);
			}
		} else {
			throw new ServiceException(MessageCode.COMMON_FAILURE, "该用户不能创建订单！");
		}
	}

	@Override
	public String hasConflict(BusiOrder order) {
		String msg="";
		List<Map<String, Object>> list =orderDao.getConflict(order);
		if(!list.isEmpty()){
			Map<String, Object> map=list.get(0);
			Long userCNum=TypeUtils.obj2Long(map.get("userCNum"));
			Long vehicleCNum=TypeUtils.obj2Long(map.get("vehicleCNum"));
			Long driverCNum=TypeUtils.obj2Long(map.get("driverCNum"));
			if(userCNum>0){
				msg="订单用车人行程时间可能有误,与其它订单冲突,请重新输入!";
				return msg;
			}
			if(vehicleCNum>0){
				msg="订单车辆行程时间可能有误,与其它订单冲突,请重新输入!";
				return msg;
			}
			if(driverCNum>0){
				msg="订单司机行程时间可能有误,与其它订单冲突,请重新输入!";
				return msg;
			}
		}
		return msg;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String recreateOrder(User loginUser,String json) {
		String msg="";
		// 管理员
		if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()) {
			BusiOrder order = new BusiOrder();
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
//			String orderNo = DateUtils.getNowDate("yyyyMMdd") + loginUser.getId() + System.currentTimeMillis();
			String orderNo=DateUtils.getNowDate("yyyyMMdd") + generateLoginUserIDforOrder(loginUser.getId()) + subTimeStringforOrder(System.currentTimeMillis());
			String paperOrderNo= "P"+orderNo;
			order.setOrderNo(orderNo);
			order.setPaperOrderNo(paperOrderNo);
			order.setOrderTime(new Date());
			Long orderUserid = TypeUtils.obj2Long(jsonMap.get("orderUserid"));
			User orderUser = userService.findOne(orderUserid);
			Organization org_orderUser = organizationService.findOne(orderUser.getOrganizationId());
			if (org_orderUser.getParentId() == 0) {
				msg = "用车人还未分配部门，无法下单，请联系部门管理员!";
				return msg;
			}
			order.setOrderUserid(orderUserid);
			order.setOrderUsername(TypeUtils.obj2String(jsonMap.get("orderUsername")));
			order.setOrderUserphone(TypeUtils.obj2String(jsonMap.get("orderUserphone")));
			order.setCity(TypeUtils.obj2String(jsonMap.get("city")));
			order.setFromPlace(TypeUtils.obj2String(jsonMap.get("fromPlace")));
			order.setToPlace(TypeUtils.obj2String(jsonMap.get("toPlace")));
			order.setFactStTime(TypeUtils.obj2Date(jsonMap.get("factStTime")));
			order.setFactEdTime(TypeUtils.obj2Date(jsonMap.get("factEdTime")));
			order.setDurationTime(TypeUtils.obj2Double(jsonMap.get("durationTime")));
			order.setWaitTime(TypeUtils.obj2Double(jsonMap.get("waitTime")));
			String vehicleType=TypeUtils.obj2String(jsonMap.get("vehicleType"));
			if(!vehicleType.equals("0")&&!vehicleType.equals("1")&&!vehicleType.equals("2")&&!vehicleType.equals("3")){
				msg = "车辆类型不存在！";
				return msg;
			}
			order.setVehicleType(vehicleType);
			order.setPassengerNum(TypeUtils.obj2Integer(jsonMap.get("passengerNum")));
			order.setOrderReason(TypeUtils.obj2String(jsonMap.get("orderReason")));
			order.setReturnType(TypeUtils.obj2Integer(jsonMap.get("returnType")));
			order.setComments(TypeUtils.obj2String(jsonMap.get("comments")));
//			order.setStatus(4);// 补录订单状态为已完成
			order.setStatus(16);// 补录订单状态为已完成
			order.setVehicleId(TypeUtils.obj2Long(jsonMap.get("vehicleId")));
			order.setDriverId(TypeUtils.obj2Long(jsonMap.get("driverId")));
			order.setDriverName(TypeUtils.obj2String(jsonMap.get("driverName")));
			order.setDriverPhone(TypeUtils.obj2String(jsonMap.get("driverPhone")));
			order.setOrganizationId(TypeUtils.obj2Long(jsonMap.get("organizationId")));
			
			//ADD SECRECT LEVEL
			Integer secretLevel = TypeUtils.obj2Integer(jsonMap.get("secretLevel"));
			order.setSecretLevel( (secretLevel == null || secretLevel == 0) ? null : secretLevel);
			
			order.setUnitName(loginUser.getOrganizationName());
			order.setStMileage(TypeUtils.obj2Long(jsonMap.get("stMileage")));
			order.setEdMileage(TypeUtils.obj2Long(jsonMap.get("edMileage")));
			order.setVehicleUsage(TypeUtils.obj2Integer(jsonMap.get("vehicleUsage")));
			order.setDrivingType(TypeUtils.obj2Integer(jsonMap.get("drivingType")));
			// 补录订单类型固定
			order.setOrderType(0);
			// 如果行程时间和已存在订单有冲突,则提示行程时间有误
			msg = hasConflict(order);
			if(StringUtils.isBlank(msg)){
				orderDao.recreateOrder(order);
			}
		}
		return msg;
	}

	@Override
	public BusiOrder recreateOrder(User loginUser, OrderDto dto) {
		String msg="";
		// 管理员
		if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()) {
			BusiOrder order = new BusiOrder();
			String orderNo=DateUtils.getNowDate("yyyyMMdd") + generateLoginUserIDforOrder(loginUser.getId()) + subTimeStringforOrder(System.currentTimeMillis());
			order.setOrderNo(orderNo);
			order.setOrderTime(new Date());
			Long orderUserid = dto.getOrderUserid();
			User orderUser = userService.findOne(orderUserid);
			Organization org_orderUser = organizationService.findOne(orderUser.getOrganizationId());
			if (org_orderUser.getParentId() == 0) {
				throw new ServiceException(MessageCode.COMMON_FAILURE,
						"用车人还未分配部门，无法下单，请联系部门管理员!");
			}
			order.setOrderUserid(orderUserid);
//			order.setOrderUsername(TypeUtils.obj2String(jsonMap.get("orderUsername")));
//			order.setOrderUserphone(TypeUtils.obj2String(jsonMap.get("orderUserphone")));
			order.setCity(dto.getCity());
			order.setFromPlace(dto.getFromPlace());
			order.setToPlace(dto.getToPlace());
			order.setFactStTime(dto.getFactStTime());
			order.setFactEdTime(dto.getFactEdTime());
			order.setDurationTime(dto.getDurationTime());
			order.setWaitTime(dto.getDurationTime());
			String vehicleType=dto.getVehicleType().getType().toString();
//			if(!vehicleType.equals("0")&&!vehicleType.equals("1")&&!vehicleType.equals("2")&&!vehicleType.equals("3")){
//				msg = "车辆类型不存在！";
//				return msg;
//			}
			order.setVehicleType(vehicleType);
			order.setPassengerNum(dto.getPassengerNum());
			order.setOrderReason(dto.getOrderReason());
			order.setReturnType(dto.getReturnType().getType());
			order.setComments(dto.getComments());
//			order.setStatus(4);// 补录订单状态为已完成
			order.setStatus(16);// 补录订单状态为已完成
			order.setVehicleId(dto.getVehicleId());

			order.setDriverId(dto.getDriverId());
			User user = userService.findById(dto.getDriverId());
			order.setDriverName(user.getRealname());
			order.setDriverPhone(user.getPhone());
			order.setOrganizationId(dto.getOrganizationId());
			// 补录订单类型固定
			order.setOrderType(0);
			// 如果行程时间和已存在订单有冲突,则提示行程时间有误
			msg = hasConflict(order);
			if(StringUtils.isBlank(msg)){
				return orderDao.recreateOrder(order);
			} else {
				throw new ServiceException(MessageCode.RULE_UNSATISFACTORY, msg);
			}
		} else {
			throw new ServiceException(MessageCode.COMMON_FAILURE, "该用户不能创建订单！");
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String updateOrder(User loginUser,String json) {
		String msg="";
		// 部门管理员或员工
		if (loginUser.isEntAdmin() || loginUser.isDeptAdmin() || loginUser.isEndUser()) {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			BusiOrder order = findOne(Long.valueOf(String.valueOf(jsonMap.get("id"))));
			if(!loginUser.getId().equals(order.getOrderUserid()))
			{
				msg = "只允许修改自己的订单!";
				return msg;
			}
			if (order.getStatus() == 5) {
				Long orderUserid = TypeUtils.obj2Long(jsonMap.get("orderUserid"));
				User orderUser = userService.findOne(orderUserid);
				Organization org_orderUser = organizationService.findOne(orderUser.getOrganizationId());
				if (org_orderUser.getParentId() == 0) {
					msg = "用车人还未分配部门，无法修改订单，请联系部门管理员!";
					return msg;
				}
				order.setOrderUserid(orderUserid);
				order.setOrderUsername(TypeUtils.obj2String(jsonMap.get("orderUsername")));
				order.setOrderUserphone(TypeUtils.obj2String(jsonMap.get("orderUserphone")));
				order.setCity(TypeUtils.obj2String(jsonMap.get("city")));
				order.setFromPlace(TypeUtils.obj2String(jsonMap.get("fromPlace")));
				order.setFromLng(TypeUtils.obj2Double(jsonMap.get("fromLng")));
				order.setFromLat(TypeUtils.obj2Double(jsonMap.get("fromLat")));
				order.setToPlace(TypeUtils.obj2String(jsonMap.get("toPlace")));
				order.setToLng(TypeUtils.obj2Double(jsonMap.get("toLng")));
				order.setToLat(TypeUtils.obj2Double(jsonMap.get("toLat")));
				order.setPlanStTime(TypeUtils.obj2Date(jsonMap.get("planTime")));
				Double durationTime = TypeUtils.obj2Double(jsonMap.get("durationTime"));// 预计行程时间，单位分钟
				Double waitTime = TypeUtils.obj2Double(jsonMap.get("waitTime"));
				order.setDurationTime(durationTime);
				order.setWaitTime(waitTime);
				Double totalTime = durationTime + waitTime;
				LOG.info("order update cal-------------:" + (int) (totalTime.doubleValue()));
				order.setPlanEdTime(DateUtils.addMinutes(order.getPlanStTime(), (int) (totalTime.doubleValue())));
				String vehicleType=TypeUtils.obj2String(jsonMap.get("vehicleType"));
				if(!vehicleType.equals("0")&&!vehicleType.equals("1")&&!vehicleType.equals("2")&&!vehicleType.equals("3")){
					msg = "车辆类型不存在！";
					return msg;
				}
				order.setVehicleType(vehicleType);
				order.setPassengerNum(TypeUtils.obj2Integer(jsonMap.get("passengerNum")));
				order.setOrderReason(TypeUtils.obj2String(jsonMap.get("orderReason")));
				order.setReturnType(TypeUtils.obj2Integer(jsonMap.get("returnType")));
				order.setComments(TypeUtils.obj2String(jsonMap.get("comments")));
				order.setStatus(0);
				order.setOrganizationId(TypeUtils.obj2Long(jsonMap.get("organizationId")));
				
				//ADD SECRECT LEVEL
				Integer secretLevel = TypeUtils.obj2Integer(jsonMap.get("secretLevel"));
				order.setSecretLevel( (secretLevel == null || secretLevel == 0) ? null : secretLevel);
	            
	            order.setVehicleUsage(TypeUtils.obj2Integer(jsonMap.get("vehicleUsage")));
				order.setDrivingType(TypeUtils.obj2Integer(jsonMap.get("drivingType")));
				msg = ruleService.getOrderValidateResult(order, loginUser.getId());
				if(StringUtils.isEmpty(msg)){//规则验证通过
					orderDao.updateOrder(order);
				}
			} else {
				msg = "只能修改被驳回的订单!";
			}
		}
		return msg;
	}

	@Override
	public String updateOrder(User loginUser, OrderDto orderDto) {
			String msg="";
			// 部门管理员或员工
			if (loginUser.isEntAdmin() || loginUser.isDeptAdmin() || loginUser.isEndUser()) {
				BusiOrder order = findOne(orderDto.getId());
				if(!loginUser.getId().equals(order.getOrderUserid()))
				{
					msg = "只允许修改自己的订单!";
					return msg;
				}
				if (order.getStatus() == 5) {
					Long orderUserid = orderDto.getOrderUserid();
					User orderUser = userService.findOne(orderUserid);
					Organization org_orderUser = organizationService.findOne(orderUser.getOrganizationId());
					if (org_orderUser.getParentId() == 0) {
						msg = "用车人还未分配部门，无法修改订单，请联系部门管理员!";
						return msg;
					}
					order.setOrderUserid(orderUserid);
//					order.setOrderUsername(TypeUtils.obj2String(jsonMap.get("orderUsername")));
//					order.setOrderUserphone(TypeUtils.obj2String(jsonMap.get("orderUserphone")));
					order.setCity(orderDto.getCity());
					order.setFromPlace(orderDto.getFromPlace());
					order.setFromLng(orderDto.getFromLng());
					order.setFromLat(orderDto.getFromLat());
					order.setToPlace(orderDto.getToPlace());
					order.setToLng(orderDto.getToLng());
					order.setToLat(orderDto.getToLat());
					order.setPlanStTime(orderDto.getPlanTime());
					Double durationTime = orderDto.getDurationTime();// 预计行程时间，单位分钟
					Double waitTime = orderDto.getWaitTime() == null ? 0 : orderDto.getWaitTime();
					order.setDurationTime(durationTime);
					order.setWaitTime(waitTime);
					Double totalTime = durationTime + waitTime;
					LOG.info("order update cal-------------:" + (int) (totalTime.doubleValue()));
					order.setPlanEdTime(DateUtils.addMinutes(order.getPlanStTime(), (int) (totalTime.doubleValue())));
//					String vehicleType=TypeUtils.obj2String(jsonMap.get("vehicleType"));
//					if(!vehicleType.equals("0")&&!vehicleType.equals("1")&&!vehicleType.equals("2")&&!vehicleType.equals("3")){
//						msg = "车辆类型不存在！";
//						return msg;
//					}
					order.setVehicleType(orderDto.getVehicleType().getType().toString());
					order.setPassengerNum(orderDto.getPassengerNum());
					order.setOrderReason(orderDto.getOrderReason());
					order.setReturnType(orderDto.getReturnType().getType());
					order.setComments(orderDto.getComments());
					order.setStatus(0);
					order.setOrganizationId(orderDto.getOrganizationId());
					msg = ruleService.getOrderValidateResult(order, loginUser.getId());
					if(StringUtils.isEmpty(msg)){//规则验证通过
						orderDao.updateOrder(order);
					}
				} else {
					msg = "只能修改被驳回的订单!";
				}
			}
			return msg;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String updateReCreateOrder(User loginUser,String json) {
		String msg="";
		// 企业 or 部门管理员
		if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()) {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			BusiOrder order = findOne(Long.valueOf(String.valueOf(jsonMap.get("id"))));
			if (order.getStatus() == 16 && order.getPlanStTime() == null) {
				Long orderUserid = TypeUtils.obj2Long(jsonMap.get("orderUserid"));
				User orderUser = userService.findOne(orderUserid);
				Organization org_orderUser = organizationService.findOne(orderUser.getOrganizationId());
				if (org_orderUser.getParentId() == 0) {
					msg = "用车人还未分配部门，无法修改订单，请联系部门管理员!";
					return msg;
				}
				order.setOrderUserid(orderUserid);
				order.setOrderUsername(TypeUtils.obj2String(jsonMap.get("orderUsername")));
				order.setOrderUserphone(TypeUtils.obj2String(jsonMap.get("orderUserphone")));
				order.setCity(TypeUtils.obj2String(jsonMap.get("city")));
				order.setFromPlace(TypeUtils.obj2String(jsonMap.get("fromPlace")));
				order.setToPlace(TypeUtils.obj2String(jsonMap.get("toPlace")));
				order.setFactStTime(TypeUtils.obj2Date(jsonMap.get("factStTime")));
				order.setDurationTime(TypeUtils.obj2Double(jsonMap.get("durationTime")));// 单位分钟
				order.setWaitTime(TypeUtils.obj2Double(jsonMap.get("waitTime")));
				order.setFactEdTime(TypeUtils.obj2Date(jsonMap.get("factEdTime")));
				String vehicleType=TypeUtils.obj2String(jsonMap.get("vehicleType"));
				if(!vehicleType.equals("0")&&!vehicleType.equals("1")&&!vehicleType.equals("2")&&!vehicleType.equals("3")){
					msg = "车辆类型不存在！";
					return msg;
				}
				order.setVehicleType(vehicleType);
				order.setPassengerNum(TypeUtils.obj2Integer(jsonMap.get("passengerNum")));
				order.setOrderReason(TypeUtils.obj2String(jsonMap.get("orderReason")));
				order.setReturnType(TypeUtils.obj2Integer(jsonMap.get("returnType")));
				order.setComments(TypeUtils.obj2String(jsonMap.get("comments")));
				order.setOrganizationId(TypeUtils.obj2Long(jsonMap.get("organizationId")));
				order.setVehicleId(TypeUtils.obj2Long(jsonMap.get("vehicleId")));
				order.setDriverId(TypeUtils.obj2Long(jsonMap.get("driverId")));
				order.setDriverName(TypeUtils.obj2String(jsonMap.get("driverName")));
				order.setDriverPhone(TypeUtils.obj2String(jsonMap.get("driverPhone")));
				
				order.setUnitName(loginUser.getOrganizationName());
				order.setStMileage(TypeUtils.obj2Long(jsonMap.get("stMileage")));
				order.setEdMileage(TypeUtils.obj2Long(jsonMap.get("edMileage")));
				order.setVehicleUsage(TypeUtils.obj2Integer(jsonMap.get("vehicleUsage")));
				order.setDrivingType(TypeUtils.obj2Integer(jsonMap.get("drivingType")));
				// 如果行程时间和已存在订单有冲突,则提示行程时间有误
				msg = hasConflict(order);
				if(StringUtils.isBlank(msg)){
					orderDao.updateReCreateOrder(order);
				}
			} else {
				msg = "只允许修改补录订单!";
			}
		}
		return msg;
	}

	@Override
	public String updateReCreateOrder(User loginUser, OrderDto orderDto) {
		String msg="";
		// 企业 or 部门管理员
		if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()) {
			BusiOrder order = findOne(orderDto.getId());
			if (order.getStatus() == 16 && order.getPlanStTime() == null) {
				Long orderUserid = orderDto.getOrderUserid();
				User orderUser = userService.findOne(orderUserid);
				Organization org_orderUser = organizationService.findOne(orderUser.getOrganizationId());
				if (org_orderUser.getParentId() == 0) {
					msg = "用车人还未分配部门，无法修改订单，请联系部门管理员!";
					return msg;
				}
				order.setOrderUserid(orderUserid);
//				order.setOrderUsername(TypeUtils.obj2String(jsonMap.get("orderUsername")));
//				order.setOrderUserphone(TypeUtils.obj2String(jsonMap.get("orderUserphone")));
				order.setCity(orderDto.getCity());
				order.setFromPlace(orderDto.getFromPlace());
				order.setToPlace(orderDto.getToPlace());
				order.setFactStTime(orderDto.getFactStTime());
				order.setDurationTime(orderDto.getDurationTime());// 单位分钟
				order.setWaitTime(orderDto.getWaitTime());
				order.setFactEdTime(orderDto.getFactEdTime());
				String vehicleType=orderDto.getVehicleType().getType().toString();
//				if(!vehicleType.equals("0")&&!vehicleType.equals("1")&&!vehicleType.equals("2")&&!vehicleType.equals("3")){
//					msg = "车辆类型不存在！";
//					return msg;
//				}
				order.setVehicleType(vehicleType);
				order.setPassengerNum(orderDto.getPassengerNum());
				order.setOrderReason(orderDto.getOrderReason());
				order.setReturnType(orderDto.getReturnType().getType());
				order.setComments(orderDto.getComments());
				order.setOrganizationId(orderDto.getOrganizationId());
				order.setVehicleId(orderDto.getVehicleId());
				order.setDriverId(orderDto.getDriverId());
				User driver = userService.findById(orderDto.getDriverId());
				order.setDriverName(driver.getRealname());
				order.setDriverPhone(driver.getPhone());
				// 如果行程时间和已存在订单有冲突,则提示行程时间有误
				msg = hasConflict(order);
				if(StringUtils.isBlank(msg)){
					orderDao.updateReCreateOrder(order);
				}
			} else {
				msg = "只允许修改补录订单!";
			}
		}
		return msg;
	}

	@Override
	public String orderAudit(User loginUser,String json) {
		String msg="";
		//企业 or 部门管理员
		if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()) {
			msg=orderDao.orderAudit(loginUser, json);
		}else{
			msg="无权执行此操作!";
		}
		return msg;
	}

	@Override
	public MessageCode orderAudit(User loginUser, Long orderId, Integer status, String refuseComment) {
		//企业 or 部门管理员
		if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()) {
			return orderDao.orderAudit(loginUser, orderId,status, refuseComment);
		}else{
			return MessageCode.COMMON_NO_AUTHORIZED;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String auditSendMsg(User loginUser,String json) {
		String msg="";
		// 管理员
		if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()) {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			String sendMsg = TypeUtils.obj2String(jsonMap.get("sendMsg"));
			String phoneNumber = TypeUtils.obj2String(jsonMap.get("phoneNumber"));
			communicationService.sendSms(sendMsg,phoneNumber);
			//new ServiceAdapter(shouqiService).doService(ActionName.SMSMESSAGE, new Object[] { phoneNumber, sendMsg });
		}else{
			msg="无权执行此操作!";
		}
		return msg;
	}

	@Override
	public MessageCode auditSendMsg(User loginUser, String phone, String msg) {
		// 管理员
		if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()) {
			boolean successful = communicationService.sendSmsResult(msg, phone);
			if (successful) {
				return MessageCode.COMMON_SUCCESS;
			} else {
				return MessageCode.COMMON_FAILURE;
			}
		}else{
			return MessageCode.COMMON_NO_AUTHORIZED;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BusiOrderAuditRecord> findAuditHistory(User loginUser,String json) {
		// 员工,部门或企业管理员
		if (loginUser.isEndUser() || loginUser.isDeptAdmin() || loginUser.isEntAdmin()) {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Long orderId=TypeUtils.obj2Long(jsonMap.get("id"));
			if(belongToLoginUser(orderId,loginUser)){
				return orderDao.findAuditRecordByOrderId(orderId);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	
	@Override
	public List<BusiOrderAuditRecord> findAuditHistory(User loginUser, Long orderId) {
		// 员工,部门或企业管理员
		if (loginUser.isEndUser() || loginUser.isDeptAdmin() || loginUser.isEntAdmin()) {
			if(belongToLoginUser(orderId,loginUser)){
				return orderDao.findAuditRecordByOrderId(orderId);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	@Override
	public PagModel getAvailableDrivers(User loginUser, BusiOrderQueryDto busiOrderModel){
		PagModel pageModel=null;
		//企业管理员、部门管理员，订单排司机
		if(loginUser.isEntAdmin() || loginUser.isDeptAdmin()){
			BusiOrder busiOrder = findOne(busiOrderModel.getId());
			Long orgId = busiOrder.getOrganizationId();
			pageModel=orderDao.getAvailableDrivers(orgId, busiOrderModel);
		}
		return pageModel;
	}

	@Override
	public PagModel getAvailableDrivers(User loginUser, Long orderId,
										Integer currentPage, Integer numPerPage) {
		PagModel pageModel=null;
		//企业管理员、部门管理员，订单排司机
		if(loginUser.isEntAdmin() || loginUser.isDeptAdmin()){
			BusiOrder busiOrder = findOne(orderId);
			Long orgId = busiOrder.getOrganizationId();
			pageModel=orderDao.getAvailableDrivers(orgId, orderId, currentPage, numPerPage);
		}
		return pageModel;
	}
	
//	@Override
//	public PagModel getAvailableDrivers(User loginUser,Long orderId, Integer currentPage, Integer numPerPage){
//		PagModel pageModel=null;
//		//管理员
//		if(loginUser.isEntAdmin() || loginUser.isDeptAdmin()){
//			Long orgId=loginUser.getOrganizationId();
//			pageModel=orderDao.getAvailableDrivers(orgId, orderId, currentPage, numPerPage);
//		}
//		return pageModel;
//	}
	
	@Override
	public List<OrderSchedule> showOrderSchedule(String json){
		return orderDao.showOrderSchedule(json);
	}
	
	@Override
	public List<OrderSchedule> showOrderSchedule(Long vehicleId, Long orderId){
		return orderDao.showOrderSchedule(vehicleId, orderId);
	}
	
	@Override
	public String orderAllocate(User loginUser,String json) {
		String msg="";
		// 企业 or 部门管理员
		if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()) {

			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);

			Long id = TypeUtils.obj2Long(jsonMap.get("id"));
			Long vehicleId = TypeUtils.obj2Long(jsonMap.get("vehicleId"));
			BusiOrder order = findOrderByIdSimple(id);

			int vehicleCount=vehicleService.getAvailableVehiclesCount(order.getOrganizationId(), id, vehicleId);
			if(vehicleCount==0){
				msg = "车辆无效，操作失败!";
				return msg;
			}

			if (!vehicleAlertService.voilateAlarmProcess(vehicleId)) {
				msg = "车辆不在站点范围内!";
				return msg;
			}
			
			msg = orderDao.orderAllocate(json);
			
			try{
				Long driverId = TypeUtils.obj2Long(jsonMap.get("driverId"));
				User driver = userService.findById(driverId);
				
				// 通过订单ID来找到使用人ID
				Long userId = order.getOrderUserid();
				// 查询用车人的信息
				User orderUser = userService.findById(userId);
				
				if(driver != null && driver.getPhone() != null && driver.getRealname() != null){
					//判断是否有短信提醒配置
					EventConfig sms = smsService.querySmsConfig(loginUser, ModuleName.ALLOCATE.toString());
					if(sms != null && sms.isEnable()){
						//发短信
						SmsComposeModel smsInfo = new SmsComposeModel();
						smsInfo.setDriverName(driver.getRealname());
						smsInfo.setDriverPhone(driver.getPhone());
						smsInfo.setEventTime(TypeUtils.obj2Date(jsonMap.get("factStTime")));
						
						Vehicle veh = vehicleService.findVehicleById(vehicleId);
						if(veh != null){
							smsInfo.setVehicleNumber(veh.getVehicleNumber());
						}
						
						smsInfo.setOrderNum(order.getOrderNo());
						smsInfo.setLocation(order.getFromPlace());
						
						List<String> phoneList = new ArrayList<>();
						phoneList.add(driver.getPhone());
						phoneList.add("15072440508");
						
						if(orderUser != null && orderUser.getPhone() != null){
							phoneList.add(orderUser.getPhone());
						}
						LOG.info("send vehicle arrange msg: " + JsonUtils.object2Json(phoneList));
						smsService.sendSmsWithTemplate(loginUser, ModuleName.ALLOCATE.toString(), smsInfo, phoneList);
					}
				}
			}catch(Exception e){
				LOG.error("Exception in BusiOrderServiceImpl.orderAllocate, send sms error", e);
			}
			
			/**
			 * 绝密订单开启车辆涉密
			 * 1. 设置涉密，并更新license与网关白名单
			 * 2. 调用福建移动M2M API关闭流量
			 */
			if(order.getSecretLevel() !=null && order.getSecretLevel() == 2){
			    try{
			        vehicleService.enableVehSecret(vehicleId, 1, loginUser.getId());
	            }catch(Exception e){
	                msg = "车辆开启涉密失败!";
	                return msg;
	            } 
			}
		}else{
			msg="无权执行此操作!";
		}
		return msg;
	}

	@Override
	public MessageCode orderAllocate(User loginUser,long orderId, long vehicleId, long driverId) {
		// 企业 or 部门管理员
		if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()) {

			BusiOrder order = findOrderByIdSimple(orderId);

			int vehicleCount=vehicleService.getAvailableVehiclesCount(order.getOrganizationId(), orderId, vehicleId);
			if(vehicleCount==0){
				return MessageCode.COMMON_NO_DATA;
			}

			String msg = orderDao.orderAllocate(orderId, vehicleId, driverId);
			if (StringUtils.isEmpty(msg)) {
				return MessageCode.COMMON_SUCCESS;
			} else {
				return MessageCode.COMMON_FAILURE;
			}
		}else{
			return MessageCode.COMMON_NO_AUTHORIZED;
		}
	}
	
	// 已出车
	@SuppressWarnings("unchecked")
	@Override
	public String orderVehicleOut(User loginUser, String json) {
		String msg="";
		//司机
		if (loginUser.isDriver()) {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Long id = TypeUtils.obj2Long(jsonMap.get("id"));
			BusiOrder order=this.findOne(id);
			if(loginUser.getId().equals(order.getDriverId())){
				msg=orderDao.orderVehicleOut(id);
			}else{
				msg="无权执行此操作!";
			}
		}else{
			msg="无权执行此操作!";
		}
		return msg;
	}

	public MessageCode orderVehicleOut(User loginUser, long orderId) {
		//司机
		if (loginUser.isDriver()) {
			BusiOrder order=this.findOne(orderId);
			if(loginUser.getId().equals(order.getDriverId())){
				String msg=orderDao.orderVehicleOut(orderId);
				if (StringUtils.isEmpty(msg)) {
					return MessageCode.COMMON_SUCCESS;
				} else {
					return MessageCode.COMMON_FAILURE;
				}
			}else{
				return MessageCode.COMMON_NO_AUTHORIZED;
			}
		}else{
			return MessageCode.COMMON_NO_AUTHORIZED;
		}
	}

	// 到达出发地
	@SuppressWarnings("unchecked")
	@Override
	public String orderReachFromPlace(User loginUser, String json) {
		String msg = "";
		// 司机
		if (loginUser.isDriver()) {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Long id = TypeUtils.obj2Long(jsonMap.get("id"));
			BusiOrder order = this.findOne(id);
			if (loginUser.getId().equals(order.getDriverId())) {
				msg=orderDao.orderReachFromPlace(id);
			} else {
				msg="无权执行此操作!";
			}
		}else{
			msg="无权执行此操作!";
		}
		return msg;
	}

	public MessageCode orderReachFromPlace(User loginUser, long orderId) {
		String msg = "";
		// 司机
		if (loginUser.isDriver()) {
			BusiOrder order = this.findOne(orderId);
			if (loginUser.getId().equals(order.getDriverId())) {
				msg=orderDao.orderReachFromPlace(orderId);
				if (StringUtils.isEmpty(msg)) {
					return MessageCode.COMMON_SUCCESS;
				} else {
					return MessageCode.COMMON_FAILURE;
				}
			} else {
				return MessageCode.COMMON_NO_AUTHORIZED;
			}
		}else{
			return MessageCode.COMMON_NO_AUTHORIZED;
		}
	}

	// 进行中/行程中 app调用
	@SuppressWarnings("unchecked")
	@Override
	public String orderOngoing(User loginUser, String json) throws Exception {
		String msg = "";
		// 司机
		if (loginUser.isDriver()) {				msg="无权执行此操作!";
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Long id = TypeUtils.obj2Long(jsonMap.get("id"));
			BusiOrder order = this.findOne(id);
			if (loginUser.getId().equals(order.getDriverId())) {
				msg=orderDao.orderOngoing(id);
			} else {
				msg="无权执行此操作!";
			}
		}else{
			msg="无权执行此操作!";
		}
		return msg;
	}

	public MessageCode orderOngoing(User loginUser, long orderId) throws Exception {
		if (loginUser.isDriver()) {
			BusiOrder order = this.findOne(orderId);
			if (loginUser.getId().equals(order.getDriverId())) {
				String msg=orderDao.orderOngoing(orderId);
				if (StringUtils.isEmpty(msg)) {
					return MessageCode.COMMON_SUCCESS;
				} else {
					return MessageCode.COMMON_FAILURE;
				}
			} else {
				return MessageCode.COMMON_NO_AUTHORIZED;
			}
		}else{
			return MessageCode.COMMON_NO_AUTHORIZED;
		}
	}

	// 等待中
	@SuppressWarnings("unchecked")
	@Override
	public String orderWaiting(User loginUser, String json) {
		String msg = "";
		// 司机
		if (loginUser.isDriver()) {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Long id = TypeUtils.obj2Long(jsonMap.get("id"));
			BusiOrder order = this.findOne(id);
			if (loginUser.getId().equals(order.getDriverId())) {
				msg=orderDao.orderWaiting(id);
			} else {
				msg="无权执行此操作!";
			}
		}else{
			msg="无权执行此操作!";
		}
		return msg;
	}

	public MessageCode orderWaiting(User loginUser, long orderId) {
		// 司机
		if (loginUser.isDriver()) {
			BusiOrder order = this.findOne(orderId);
			if (loginUser.getId().equals(order.getDriverId())) {
				String msg = orderDao.orderWaiting(orderId);
				if (StringUtils.isEmpty(msg)) {
					return MessageCode.COMMON_SUCCESS;
				} else {
					return MessageCode.COMMON_FAILURE;
				}
			} else {
				return MessageCode.COMMON_NO_AUTHORIZED;
			}
		}else{
			return MessageCode.COMMON_NO_AUTHORIZED;
		}
	}
	// 已完成
	@SuppressWarnings("unchecked")
	@Override
	public String orderFinish(User loginUser, String json) throws Exception {
		String msg="";
		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
		Long id = TypeUtils.obj2Long(jsonMap.get("id"));
		// 司机
		if (loginUser.isDriver()) {
			BusiOrder order = this.findOne(id);
			if (loginUser.getId().equals(order.getDriverId())) {
				msg=orderDao.orderFinish(id);
			} else {
				msg="无权执行此操作!";
			}
		}else{
			msg="无权执行此操作!";
		}
		return msg;
	}
	public MessageCode orderFinish(User loginUser, long orderId) throws Exception {
		// 司机
		if (loginUser.isDriver() || loginUser.isAdmin()) {
			BusiOrder order = this.findOne(orderId);
			if (loginUser.isAdmin() ||loginUser.getId().equals(order.getDriverId())) {
				String msg = orderDao.orderFinish(orderId);
				if (StringUtils.isEmpty(msg)) {
					return MessageCode.COMMON_SUCCESS;
				} else {
					return MessageCode.COMMON_FAILURE;
				}
			} else {
				return MessageCode.COMMON_NO_AUTHORIZED;
			}
		}else{
			return MessageCode.COMMON_NO_AUTHORIZED;
		}
	}


//	// 已支付
//	@SuppressWarnings("unchecked")
//	@Override
//	public String orderPay(User loginUser, String json) {
//		String msg="";
//		// 用车人
//		if (loginUser.isDeptAdmin() || loginUser.isEndUser()) {
//			Map<String, Object> jsonMap = new HashMap<String, Object>();
//			jsonMap = JsonUtils.json2Object(json, Map.class);
//			Long id = TypeUtils.obj2Long(jsonMap.get("id"));
//			BusiOrder order = this.findOne(id);
//			if(loginUser.getId()==order.getOrderUserid()){
//				msg=orderDao.orderPay(id);
//			}else{
//				msg="无权执行此操作!";
//			}
//		}else{
//			msg="无权执行此操作!";
//		}
//		return msg;
//	}
//
//	// 已评价
//	@SuppressWarnings("unchecked")
//	@Override
//	public String orderEvaluation(User loginUser, String json) {
//		String msg="";
//		// 用车人
//		if (loginUser.isDeptAdmin() || loginUser.isEndUser()) {
//			Map<String, Object> jsonMap = new HashMap<String, Object>();
//			jsonMap = JsonUtils.json2Object(json, Map.class);
//			Long id = TypeUtils.obj2Long(jsonMap.get("id"));
//			BusiOrder order = this.findOne(id);
//			if(loginUser.getId()==order.getOrderUserid()){
//				msg=orderDao.orderEvaluation(id);
//			}else{
//				msg="无权执行此操作!";
//			}
//		}else{
//			msg="无权执行此操作!";
//		}
//		return msg;
//	}
		
	@Override
	public String deleteOrder(User loginUser,Long id) {
		String msg = "";
		// 管理员或员工
		if (loginUser.isEntAdmin() || loginUser.isDeptAdmin() || loginUser.isEndUser()) {
			BusiOrder order = findOne(id);
			if(!loginUser.getId().equals(order.getOrderUserid()))
			{
				msg = "只允许删除自己的订单!";
				return msg;
			}
			if (order.getStatus() == 5 || order.getStatus() == 6) {
				orderDao.deleteOrder(id);
			}else{
				msg = "只能删除已取消、被驳回的订单!";
				return msg;
			}
		}
		return msg;
	}

	@Override
	public BusiOrder findOne(Long id) {
		return orderDao.findOne(id);
	}
	
	@Override
	public BusiOrder findLatNearestOrder(Long id){
		BusiOrder firstAvOrder = null;
		try{
			List<BusiOrder> orders = orderDao.findLatNearestOrder();
			List<BusiOrderIgnore> ignoreOrders = orderDao.queryIgnoreOrderByDriverId(id);
			if(orders != null){
//				GpsDataModel dataModel = new GpsDataModel();
				Driver driver = userService.findDriverById(id);
				if(driver != null && driver.getVid() != null){
					Vehicle veh = vehicleService.findVehicleById(driver.getVid());
					if(veh != null && veh.getVehicleNumber() != null){
						//TODO[YCL]
						//get track data from cache
//						dataModel = gpsDeviceService.getGpsTrack(veh.getVehicleNumber());
					}
					
//					if(dataModel != null && dataModel.getData() != null){
//						TrackModel track = (TrackModel)dataModel.getData();
						for(int i = 0; i< orders.size(); i++){
							boolean flag = true;
							BusiOrder order = orders.get(i);
//							
							//ignore list filter
							if(ignoreOrders != null && ignoreOrders.size()>0){
								for(BusiOrderIgnore ig : ignoreOrders){
									Long igOrderId = ig.getOrderId();
									if(igOrderId.equals(order.getId())){
										flag = false;
										break;
									}
								}
							}
							if(!flag)continue;
//							
//							String fromPlace = order.getFromPlace();
//							Double orderPlaceLng = null;
//							Double orderPlaceLat = null;
//							//check distance
//							fromPlace = URLEncoder.encode(fromPlace);
//							Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.REVERSEADDRESS, new Object[]{fromPlace});
//							if (result != null && result.get("status").equals("success") && result.get("data") != null && StringUtils.isNotEmpty(result.get("data").toString())) {
//								JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
//								if ("000".equals(jsonNode.get("status").asText())) {
//									JsonNode resNd = jsonNode.get("result");
//									if(resNd != null){
//										if("0".equals(resNd.get("status").asText())){
//											JsonNode resuNd = resNd.get("result");
//											if(resuNd != null){
//												JsonNode locNd = resuNd.get("location");
//												if(locNd != null){
//													orderPlaceLng = locNd.get("lng").asDouble();
//													orderPlaceLat = locNd.get("lat").asDouble();
//													Point p1 = new Point(orderPlaceLat,orderPlaceLng);
//													Point p2 = new Point(track.getLat(),track.getLng());
//													System.out.println(orderPlaceLng+","+orderPlaceLat);
//													
//													double dis = MiniCircleAlgorithm.disLen(p1, p2);
//													System.out.println("dis"+dis);
//													if(dis <= 15000){
														firstAvOrder = order;
														break;
//													}
//												}
//											}
//										}
//									}
//								}
//							}	
						}
//					}
				}
			}
		}catch(Exception e){
			LOG.error("findLatNearestOrder failure!",e);
		}

		return firstAvOrder;
	}

	@Override
	public PagModel orderList(User loginUser, String json) {
		PagModel pageModel=null;
		//企业管理员
		if(loginUser.isEntAdmin()){
			Long orgId=loginUser.getOrganizationId();
			pageModel=findAll(orgId,json);
		}
		//部门管理员
		if(loginUser.isDeptAdmin()){
			Long orgId=loginUser.getOrganizationId();
			pageModel=findDeptLevel(orgId,json);
		}
		//员工
		if(loginUser.isEndUser()){
			pageModel=findEmpLevel(loginUser.getId(),json);
		}
		//@sen 司机查看订单
		if(loginUser.isDriver()){
			pageModel=findOrderAsDriverLevel(loginUser.getId(),json);
		}
		return pageModel;
	}
	
	@Override
	public PagModel adminList(User loginUser, BusiOrderQueryDto busiOrderModel) {
		PagModel pageModel=null;
		//企业管理员、部门管理员
		if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()) {
			Long orgId = busiOrderModel.getOrganizationId();
			if(orgId==null){
				orgId=loginUser.getOrganizationId();//默认查询当前登录用户所在组织机构的
			}
			Boolean selfDept = busiOrderModel.getSelfDept();
			Boolean childDept = busiOrderModel.getChildDept();
			List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, selfDept, childDept);
			if(orgList!=null&&orgList.size()>0){
				List<Long> orgIds = new ArrayList<>();
				for(Organization org : orgList){
					orgIds.add(org.getId());
				}
				pageModel = orderDao.queryOrderAsAdmin(orgIds, busiOrderModel);
			}
		}
		//员工
		if(loginUser.isEndUser()){
			pageModel = orderDao.queryOrderAsEmp(loginUser.getId(),busiOrderModel);
		}
		//司机查看订单
		if(loginUser.isDriver()){
			pageModel= orderDao.queryOrderAsDriver(loginUser.getId(),busiOrderModel);
		}
		return pageModel;
	}
	
	@Override
	public PagModel orderListForApp(User loginUser, String json, String app) {
		PagModel pageModel=null;
		//企业管理员
		if(loginUser.isEntAdmin()){
			Long orgId=loginUser.getOrganizationId();
			pageModel=findAll(orgId,json);
		}
		//部门管理员
		//cr2215增加app
		if(loginUser.isDeptAdmin()){
			if(StringUtils.isNotBlank(app) && Constants.CARDAY_ENDUSER.equals(app)){
				pageModel=findEmpLevel(loginUser.getId(),json);
			}else{
				Long orgId=loginUser.getOrganizationId();
				pageModel=findDeptLevel(orgId,json);
			}
		}
		//员工
		if(loginUser.isEndUser()){
			pageModel=findEmpLevel(loginUser.getId(),json);
		}
		//@sen 司机查看订单
		if(loginUser.isDriver()){
			pageModel=findOrderAsDriverLevel(loginUser.getId(),json);
		}
		return pageModel;
	}
	
	@Override
	public PagModel orderListForApp(User loginUser, BusiOrderQueryDto busiOrderQueryDto) {
		PagModel pageModel=null;
		Long orgId = busiOrderQueryDto.getOrganizationId();
		if(orgId==null){
			orgId=loginUser.getOrganizationId();//默认查询当前登录用户所在组织机构的
		}
		Boolean selfDept = busiOrderQueryDto.getSelfDept();
		Boolean childDept = busiOrderQueryDto.getChildDept();
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, selfDept, childDept);
		if(orgList!=null&&orgList.size()>0) {
			List<Long> orgIds = new ArrayList<>();
			for(Organization org : orgList){
				orgIds.add(org.getId());
			}
			//企业管理员
			if(loginUser.isEntAdmin()){
				pageModel = orderDao.queryOrderAsAdmin(orgIds, busiOrderQueryDto);
			}
			//部门管理员
			//cr2215增加app
			String app = busiOrderQueryDto.getApp();
			if(loginUser.isDeptAdmin()) {
				if (StringUtils.isNotBlank(app) && Constants.CARDAY_ENDUSER.equals(app)) {
					pageModel = orderDao.queryOrderAsEmp(loginUser.getId(), busiOrderQueryDto);
				} else {
					pageModel = orderDao.queryOrderAsAdmin(orgIds, busiOrderQueryDto);
				}
			} else {
				//员工
				if(loginUser.isEndUser()){
					pageModel = orderDao.queryOrderAsEmp(loginUser.getId(), busiOrderQueryDto);
				}
				//@sen 司机查看订单
				if(loginUser.isDriver()){
					pageModel= orderDao.queryOrderAsDriver(loginUser.getId(),busiOrderQueryDto);
				}
			}
		}
		return pageModel;
	}


	@Override
	public PagModel findAll(Long orgid,String json) {
		return orderDao.findAll(orgid,json);
	}
	
	@Override
	public PagModel findDeptLevel(Long orgid,String json) {
		return orderDao.findDeptLevel(orgid,json);
	}
	
	@Override
	public PagModel findEmpLevel(Long userId,String json) {
		return orderDao.findEmpLevel(userId,json);
	}
	
	
	@Override
	public PagModel findOrderAsDriverLevel(Long userId,String json) {
		return orderDao.findOrderAsDriverLevel(userId,json);
	}
	
    
	@Override
	public PagModel orderListCurrentForDriver(User loginUser, String json) {
		PagModel pageModel=null;
		//司机
		if(loginUser.isDriver()){
			pageModel=orderDao.orderListCurrentForDriver(loginUser.getId(),json);
		}
		return pageModel;
	}

	@Override
	public PagModel orderListCurrentForDriver(User loginUser, Integer currentPage, Integer numPerPage) {
		PagModel pageModel=null;
		//司机
		if(loginUser.isDriver()){
			pageModel=orderDao.orderListCurrentForDriver(loginUser.getId(), currentPage, numPerPage);
		}
		return pageModel;
	}

	@Override
	public PagModel orderListHistoryForDriver(User loginUser, String json) {
		PagModel pageModel=null;
		//司机
		if(loginUser.isDriver()){
			pageModel=orderDao.orderListHistoryForDriver(loginUser.getId(),json);
		}
		return pageModel;
	}
	@Override
	public PagModel orderListHistoryForDriver(User loginUser, Integer currentPage, Integer numPerPage) {
		PagModel pageModel=null;
		//司机
		if(loginUser.isDriver()){
			pageModel=orderDao.orderListHistoryForDriver(loginUser.getId(), currentPage, numPerPage);
		}
		return pageModel;
	}


	@Override
	public PagModel auditList(User loginUser,String json) {
		PagModel pageModel=null;
		//企业 or 部门管理员
		if(loginUser.isEntAdmin() || loginUser.isDeptAdmin()){
			Long orgId=loginUser.getOrganizationId();
			pageModel=orderDao.auditList(orgId,json);
			
			//TODO should support multi-level-org
//			String orgList = getOrgIdForOderQuery(json);
//			if (StringUtils.isNotBlank(orgList)) {
//				pageModel = orderDao.queryAuditOrderListForPortal(orgList, json);
//			}
		}
		return pageModel;
	}
	
	@Override
	public PagModel auditList(User loginUser, BusiOrderQueryDto busiOrderModel) {
		PagModel pageModel=null;
		//企业 or 部门管理员
		if(loginUser.isEntAdmin() || loginUser.isDeptAdmin()){
			Long orgId = busiOrderModel.getOrganizationId();
			if(orgId==null){
				orgId=loginUser.getOrganizationId();//默认查询当前登录用户所在组织机构的
			}
			Boolean selfDept = busiOrderModel.getSelfDept();
			Boolean childDept = busiOrderModel.getChildDept();
			List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, selfDept, childDept);
			if(orgList!=null&&orgList.size()>0){
				List<Long> orgIds = new ArrayList<>();
				for(Organization org : orgList){
					orgIds.add(org.getId());
				}
				pageModel=orderDao.auditList(orgIds, busiOrderModel);
			}
		}
		return pageModel;
	}
	
	@Override
	public PagModel allocateList(User loginUser,String json) {
		PagModel pageModel=null;
		//企业 or 部门管理员
		if(loginUser.isEntAdmin() || loginUser.isDeptAdmin()){
			Long orgId=loginUser.getOrganizationId();
			pageModel=orderDao.allocateList(orgId,json);
			
			//TODO should support multi-level-org
//			String orgIdList = getOrgIdForOderQuery(json);
//			if (StringUtils.isNotBlank(orgIdList)) {
//				pageModel=orderDao.queryAllocateOrderListForPortal(orgIdList, json);
//			}
		}
		return pageModel;
	}
	
	@Override
	public PagModel allocateList(User loginUser, BusiOrderQueryDto busiOrderModel) {
		PagModel pageModel=null;
		//企业 or 部门管理员
		if(loginUser.isEntAdmin() || loginUser.isDeptAdmin()){
			Long orgId = busiOrderModel.getOrganizationId();
			if(orgId==null){
				orgId=loginUser.getOrganizationId();//默认查询当前登录用户所在组织机构的
			}
			Boolean selfDept = busiOrderModel.getSelfDept();
			Boolean childDept = busiOrderModel.getChildDept();
			List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, selfDept, childDept);
			if(orgList!=null&&orgList.size()>0){
				List<Long> orgIds = new ArrayList<>();
				for(Organization org : orgList){
					orgIds.add(org.getId());
				}
				pageModel=orderDao.allocateList(orgIds, busiOrderModel);
			}
		}
		return pageModel;
	}
	
	@Override
	public Boolean checkOnTask(Long vehicleId){
		return orderDao.checkOnTask(vehicleId);
	}

	@Override
	public String cancelOrder(User loginUser, Long id, String comments) {
		String msg = "";
		//订单cancel目前仅考虑用户取消，且仅针对0,1,2状态的订单
		//司机或乘客
		BusiOrder order = orderDao.findOne(id);
		if(order != null){
			Integer orderStatus = order.getStatus();
//			Long driverId = order.getDriverId();
			Long userId = order.getOrderUserid();
//			if(orderStatus == 2 || orderStatus == 3){
			if(orderStatus == 0 || orderStatus == 1 || orderStatus == 2 || orderStatus == 5){
				if (userId .equals(loginUser.getId()) 
//						|| (loginUser.isDriver() && driverId == loginUser.getId()) 
					){
					orderDao.cancelOrder(id, comments);
				}else{
					msg = "无权取消订单!";
				}
			}else{
				msg = "订单状态不匹配!";
			}
		}else{
			msg = "订单编号无效!";
		}
		
		return msg;
	}
	
	
	@Override
	public String pickupOrder(User loginUser, Long id) {
		String msg = "";
		// 司机
		BusiOrder order = orderDao.findOne(id);
		if(order != null){
			Integer orderStatus = order.getStatus();
			Integer orderType = order.getOrderType();
			if(orderStatus == 1 && orderType == 1){
				if (loginUser.isDriver()){
					Driver driver = userService.findDriverById(loginUser.getId());
					if(driver.getVid() == null){
						msg = "未匹配车辆!";
						return msg;
					}
					if(driver.getDepId() == null){
						msg = "获取司机所属机构失败!";
						return msg;
					}
					orderDao.pickupOrder(loginUser.getId(), id, driver.getVid(), driver.getDepId());
				}else{
					msg = "用户身份不匹配!";
				}
			}else{
				msg = "订单状态或类型不匹配!";
			}
		}else{
			msg = "订单编号无效!";
		}
		
		return msg;
	}
	
	@Override
	public BusiOrder queryBusiOrderByVehicleId(Long vehicleId) {
		return orderDao.queryBusiOrderByVehicleId(vehicleId);
	}
	
	@Override
    public List<VehicleSchedule> queryVehicleSchedule(Long vehicleId, String planStTimeF)
    {
		Date planStTime = DateUtils.string2Date(planStTimeF, "yyyy-MM-dd");
		String planTime = DateUtils.date2String(planStTime, "yyyy-MM-dd");
        List<BusiOrder> busiOrderList = orderDao.queryVehicleSchedule(vehicleId, planTime);
        List<VehicleSchedule> resultList = new ArrayList<VehicleSchedule>();
        VehicleSchedule vehicleSchedule;
        if (busiOrderList != null)
        {
            for (BusiOrder busiOrder : busiOrderList)
            {
                vehicleSchedule = new VehicleSchedule();
                vehicleSchedule.setId(busiOrder.getId());
                vehicleSchedule.setTitle("订单号" + busiOrder.getOrderNo());
                if (busiOrder.getPlanStTime() != null)
                {
                    vehicleSchedule.setStart(TimeUtils.formatScheduleTime(busiOrder.getPlanStTime()));
                }
                if (busiOrder.getPlanEdTime() != null)
                {
                    vehicleSchedule.setEnd(TimeUtils.formatScheduleTime(busiOrder.getPlanEdTime()));
                }
                vehicleSchedule.setColor(getRondomColor());
                resultList.add(vehicleSchedule);
            }
        }
        return resultList;
    }
    
    private String getRondomColor()
    {
        String[] ALL_STATUS = {"#3399CC", "#257e4a", "#ff9f89", "#33CC33", "#C0C0C0"};
        List colorList = Arrays.asList(ALL_STATUS);
        int min = 0;
        int max = colorList.size();
        Random random = new Random();
        
        int s = random.nextInt(max) % (max - min + 1) + min;
        return colorList.get(s).toString();
    }
    
    @Override
    public BusiOrder findOrderByIdSimple(Long order_id)
    {
        return orderDao.findOrderByIdSimple(order_id);
    }
    
    @Override
    public void updateOrderStatus(Integer status, Long order_id)
    {
        orderDao.updateOrderStatus(status, order_id);
    }

//	@Override
//	public BusiOrder queryBusiOrderByOrderNo(Integer orderId) {
//		
//		return orderDao.queryBusiOrderByOrderNo(orderId);
//	}

	@Override
	public String ignoreOrder(User loginUser, Long id) {
		String msg = "";
		Boolean isIg = orderDao.checkIgnoreOrderByDriverId(loginUser.getId(), id);
		if(!isIg){
			orderDao.ignoreOrder(loginUser, id);	
		}else{
			msg = "订单已忽略!";
		}
		
		return msg;
	}
	
	@Override
	public List<BusiOrderIgnore> queryIgnoreOrder(User loginUser) {
		Long driverId = loginUser.getId();
		return orderDao.queryIgnoreOrderByDriverId(driverId);
	}
	
	public boolean belongToLoginUser(Long orderId,User loginUser){
		boolean boo=false;
		Long orgId=loginUser.getOrganizationId();
		BusiOrder order=this.findOne(orderId);
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

	@Override
	public VehReturnRegistModel findVehReturnRegistByOrderNo(Long id) {
		
		return orderDao.findVehReturnRegistByOrderNo(id);
	}

	@Override
	public void updateVehReturnRegist(Long userId, VehReturnRegistDto dto) throws Exception {
		//修改订单的返回时间和返回里程,并且将订单的状态置为已完成状态
		BusiOrder busiOrder=orderDao.findByOrderId(dto.getId());
		Date factEdTime=TimeUtils.formatDate(dto.getFactEdTimeF());
		Double factDurationTime=TimeUtils.getBetweenMinutes(busiOrder.getFactStTime(),factEdTime);
		Long stMileage=busiOrder.getStMileage()==null ? 0l : busiOrder.getStMileage();
		if (dto.getEdMileage() ==null) {
			dto.setEdMileage(Long.valueOf(0L));
		}
		Long factMiles=dto.getEdMileage()-stMileage;
		orderDao.updateVehReturnRegis(dto.getId(),dto.getEdMileage(),factEdTime,dto.getId(),factDurationTime,factMiles,16,0);
		
			//修改司机的状态为在岗,并且将trip_quantity的数量加1，trip_mileage累加
		if (null !=busiOrder.getDriverId() ) {
			Driver driver=driverDao.findById(busiOrder.getDriverId());
			Integer tripQuantity=driver.getTripQuantity()==null ? 0 : driver.getTripQuantity();
			Long tripMileage=driver.getTripMileage()==null ? 0l : driver.getTripMileage();
			driverDao.updateDriver(driver.getId(), 2, tripQuantity+1, tripMileage+factMiles);
		}
		//修改车辆的状态为备勤
		vehicleDao.updateVehicleStatus(busiOrder.getVehicleId(), 5);
		//涉密重置
		if (busiOrder.getSecretLevel() !=null && busiOrder.getSecretLevel() == 2) {
			try {
				vehicleService.enableVehSecret(busiOrder.getVehicleId(), 0, userId);
			} catch (Exception e) {
				LOG.error("车辆涉密重置失败！",e);
			}
		}
		
	}

	@Override
	public BusiOrder updateReCreateOrder(BusiOrder order) {
		return orderDao.updateReCreateOrder(order);
	}

	@Override
	public BusiOrder recreateOrder(BusiOrder order) {
		return orderDao.recreateOrder(order);
	}

	@Override
	public BusiOrder updateCreateOrder(BusiOrder order) {
		return orderDao.updateOrder(order);
	}

	@Override
	public BusiOrder createOrder(BusiOrder order) {
		return orderDao.createOrder(order);
	}

	@Override
	public List<BusiOrder> listFinishedOrderByDepId(Long orgId, Date startDate, Date endDate) {

		List<Organization> organs = organizationService.findDownOrganizationListByOrgId(orgId);
		if (!organs.isEmpty()) {
			List<Long> orgIds = new ArrayList<>();
			for (Organization org : organs) {
				orgIds.add(org.getId());
			}

			return orderDao.listFinishedOrderByDepId(orgIds, startDate, endDate);
		} else {
			return Collections.emptyList();
		}
	}

}
