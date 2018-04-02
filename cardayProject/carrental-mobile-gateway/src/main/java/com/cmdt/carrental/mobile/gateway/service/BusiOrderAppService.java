package com.cmdt.carrental.mobile.gateway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.BusiOrderAuditRecord;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.EmployeeModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.service.BusiOrderService;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.model.BusiOrderDto;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BusiOrderAppService {
   
	@Autowired
	private BusiOrderService busiOrderService;
	@Autowired
	private UserService userService;
    @Autowired
    private OrganizationService organizationService;
	@Autowired
    private VehicleService vehicleService;
	@Autowired
	public ShouqiService shouqiService;
	
	public String createOrder(BusiOrderDto dto) throws Exception{
		//创建订单，必填字段，校验不能为空
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
		if (StringUtils.isBlank(dto.getPlanTime())) {
			msg = "预约用车时间不能为空，请填写预约用车时间！";
			return msg;
		}
		if (dto.getDurationTime()==null || dto.getDurationTime()<=0) {
			msg = "预计行程时间必须大于0，请填写预计行程时间！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getVehicleType())) {
			msg = "用车类型不能为空，请填写用车类型！";
			return msg;
		}
		if (dto.getPassengerNum()==null||dto.getPassengerNum()<=0) {
			msg = "乘车人数必须大于0！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getOrderReason())) {
			msg = "用车事由不能为空，请填写用车事由！";
			return msg;
		}
		if (dto.getReturnType()==null||(dto.getReturnType()!=0&&dto.getReturnType()!=1)) {
			msg = "是否往返无效,请选择是否往返！";
			return msg;
		}
		if (dto.getReturnType()==0 && dto.getWaitTime()==null) {
			msg = "等待时长为空，请填写等待时长！";
			return msg;
		}
		Double[] fromPoint=transAddrToPoint(dto.getFromPlace());
		if(null!=fromPoint){
			dto.setFromLat(fromPoint[0]);
			dto.setFromLng(fromPoint[1]);
		}
		Double[] toPoint=transAddrToPoint(dto.getToPlace());
		if(null!=toPoint){
			dto.setToLat(toPoint[0]);
			dto.setToLng(toPoint[1]);
		}
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		if (!loginUser.getOrganizationId().equals(dto.getOrganizationId())) {
			msg = "登录用户组织Id和订单组织Id不一致！";
			return msg;
		}
		if (!loginUserId.equals(dto.getOrderUserid())) {
			msg = "登录用户Id与用车人Id不一致！";
			return msg;
		}
		String json=JsonUtils.object2Json(dto);
		return busiOrderService.createOrder(loginUser, json);
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
	
	public String updateOrder(BusiOrderDto dto) throws Exception{
		//修改订单，必填字段，校验不能为空
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
		if (StringUtils.isBlank(dto.getPlanTime())) {
			msg = "预约用车时间不能为空，请填写预约用车时间！";
			return msg;
		}
		if (dto.getDurationTime()==null || dto.getDurationTime()<=0) {
			msg = "预计行程时间必须大于0，请填写预计行程时间！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getVehicleType())) {
			msg = "用车类型不能为空，请填写用车类型！";
			return msg;
		}
		if (dto.getPassengerNum()==null||dto.getPassengerNum()<=0) {
			msg = "乘车人数必须大于0！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getOrderReason())) {
			msg = "用车事由不能为空，请填写用车事由！";
			return msg;
		}
		if (dto.getReturnType()==null||(dto.getReturnType()!=0&&dto.getReturnType()!=1)) {
			msg = "是否往返无效,请选择是否往返！";
			return msg;
		}
		if (dto.getReturnType()==0 && dto.getWaitTime()==null) {
			msg = "等待时长为空，请填写等待时长！";
			return msg;
		}
		Double[] fromPoint=transAddrToPoint(dto.getFromPlace());
		if(null!=fromPoint){
			dto.setFromLat(fromPoint[0]);
			dto.setFromLng(fromPoint[1]);
		}
		Double[] toPoint=transAddrToPoint(dto.getToPlace());
		if(null!=toPoint){
			dto.setToLat(toPoint[0]);
			dto.setToLng(toPoint[1]);
		}
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		BusiOrder busiOrder = busiOrderService.findOne(dto.getId());
		//只有部门管理员、员工可以修改订单
		if (loginUser.getUserCategory()!= 3 && loginUser.getUserCategory()!= 4) {
			return "该用户没有修改订单的权限！";
		}
		if (!dto.getOrderUserid().equals(busiOrder.getOrderUserid())) {
			return "orderUserid不允许被修改！";
		}
		String json=JsonUtils.object2Json(dto);
		return busiOrderService.updateOrder(loginUser,json);
	}
	
	public BusiOrder viewOrder(BusiOrderQueryDto dto){
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		if(belongToLoginUser(dto.getId(),loginUser)){
			BusiOrder order=busiOrderService.findOne(dto.getId());
			return order;
		}else{
			return null;
		}
	}
	
	public String deleteOrder(BusiOrderDto dto){
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		Long id=dto.getId();
		return busiOrderService.deleteOrder(loginUser,id);
	}
	
	public String recreateOrder(BusiOrderDto dto){
		//补录订单，必填字段，校验不能为空
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
		if (dto.getPassengerNum()==null||dto.getPassengerNum()<=0) {
			msg = "乘车人数必须大于0！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getOrderReason())) {
			msg = "用车事由不能为空，请填写用车事由！";
			return msg;
		}
		if (dto.getReturnType()==null||(dto.getReturnType()!=0&&dto.getReturnType()!=1)) {
			msg = "是否往返无效,请选择是否往返！";
			return msg;
		}
		if (dto.getReturnType()==0 && dto.getWaitTime()==null) {
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
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		if (!loginUser.getOrganizationId().equals(dto.getOrganizationId())) {
			msg = "登录用户组织Id和订单组织Id不一致！";
			return msg;
		}
		if (!loginUserId.equals(dto.getOrderUserid())) {
			msg = "登录用户Id与用车人Id不一致！";
			return msg;
		}
		String json=JsonUtils.object2Json(dto);
		return busiOrderService.recreateOrder(loginUser, json);
	}
	
	public String recreateUpdate(BusiOrderDto dto){
		//补录订单修改，必填字段，校验不能为空
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
		if (dto.getPassengerNum()==null||dto.getPassengerNum()<=0) {
			msg = "乘车人数必须大于0！";
			return msg;
		}
		if (StringUtils.isBlank(dto.getOrderReason())) {
			msg = "用车事由不能为空，请填写用车事由！";
			return msg;
		}
		if (dto.getReturnType()==null||(dto.getReturnType()!=0&&dto.getReturnType()!=1)) {
			msg = "是否往返无效,请选择是否往返！";
			return msg;
		}
		if (dto.getReturnType()==0 && dto.getWaitTime()==null) {
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
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		BusiOrder busiOrder = busiOrderService.findOne(dto.getId());
		if (!dto.getOrderUserid().equals(busiOrder.getOrderUserid())) {
			return "orderUserid不允许被修改！";
		}
		//只要部门管理员能修改补录订单
		if (loginUser.getUserCategory()!= 3) {
			return "该用户没有修改补录订单的权限！";
		}
		String json=JsonUtils.object2Json(dto);
		return busiOrderService.updateReCreateOrder(loginUser,json);
	}
	
	public String audit(BusiOrderDto dto){
		String msg="";
		if(dto.getId()==null){
			msg="订单id不能为空!";
			return msg;
		}
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		String json=JsonUtils.object2Json(dto);
		if(belongToLoginUser(dto.getId(),loginUser)){
			msg=busiOrderService.orderAudit(loginUser, json);
		}else{
			msg="无权执行此操作!";
		}
		return msg;
	}
	
	public String auditSendMsg(BusiOrderDto dto){
		String msg="";
		if(StringUtils.isBlank(dto.getPhoneNumber())){
			msg="phone number不能为空!";
			return msg;
		}
		if(StringUtils.isBlank(dto.getSendMsg())){
			msg="sendMsg不能为空!";
			return msg;
		}
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		String json=JsonUtils.object2Json(dto);
		busiOrderService.auditSendMsg(loginUser,json);
		return msg;
	}
	
	public List<BusiOrderAuditRecord> auditHistory(BusiOrderQueryDto dto){
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		String json=JsonUtils.object2Json(dto);
		return busiOrderService.findAuditHistory(loginUser,json);
	}
	
	public PagModel getAvailableVehicles(BusiOrderQueryDto dto){
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		return vehicleService.listAvailableVehicleByAdmin(loginUser, dto);
	}
	
	public PagModel getAvailableDrivers(BusiOrderQueryDto dto){
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		return busiOrderService.getAvailableDrivers(loginUser, dto);
	}
	
	public String allocate(BusiOrderDto dto){
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
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		String json=JsonUtils.object2Json(dto);
		msg=busiOrderService.orderAllocate(loginUser,json);
		return msg;
	}
	
	public String orderVehicleOut(BusiOrderDto dto){
		String msg="";
		if(dto.getId()==null){
			msg="订单id不能为空!";
			return msg;
		}
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		String json=JsonUtils.object2Json(dto);
		msg=busiOrderService.orderVehicleOut(loginUser,json);
		return msg;
	}
	
	public String orderReachFromPlace(BusiOrderDto dto){
		String msg="";
		if(dto.getId()==null){
			msg="订单id不能为空!";
			return msg;
		}
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		String json=JsonUtils.object2Json(dto);
		msg=busiOrderService.orderReachFromPlace(loginUser,json);
		return msg;
	}
	
	public String orderOngoing(BusiOrderDto dto) throws Exception {
		String msg="";
		if(dto.getId()==null){
			msg="订单id不能为空!";
			return msg;
		}
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		String json=JsonUtils.object2Json(dto);
		msg=busiOrderService.orderOngoing(loginUser,json);
		return msg;
	}
	
	public String orderWaiting(BusiOrderDto dto){
		String msg="";
		if(dto.getId()==null){
			msg="订单id不能为空!";
			return msg;
		}
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		String json=JsonUtils.object2Json(dto);
		msg=busiOrderService.orderWaiting(loginUser,json);
		return msg;
	}
	
	public String orderFinish(BusiOrderDto dto) throws Exception {
		String msg="";
		if(dto.getId()==null){
			msg="订单id不能为空!";
			return msg;
		}
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		String json=JsonUtils.object2Json(dto);
		msg=busiOrderService.orderFinish(loginUser,json);
		return msg;
	}
	
	public String orderCancel(BusiOrderDto dto){
		String msg="";
		if(dto.getId()==null){
			msg="订单id不能为空!";
			return msg;
		}
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		msg=busiOrderService.cancelOrder(loginUser,dto.getId(),dto.getComments());
		return msg;
	}
	
	public PagModel orderList(BusiOrderQueryDto dto){
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		String col_orderby=dto.getColOrderBy();
		if(StringUtils.isBlank(col_orderby)){
			dto.setColOrderBy(" order by t.plan_st_time desc");
		}else if(col_orderby.trim().equals("1")){
			dto.setColOrderBy(" order by t.plan_st_time desc");
		}
		return busiOrderService.orderListForApp(loginUser, dto);
	}
	
	public PagModel auditList(BusiOrderQueryDto dto){
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		return busiOrderService.auditList(loginUser, dto);
	}
	
	public PagModel allocateList(BusiOrderQueryDto dto){
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		return busiOrderService.allocateList(loginUser, dto);
	}
	
	public PagModel orderListCurrentForDriver(BusiOrderQueryDto dto){
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		String json=JsonUtils.object2Json(dto);
		return busiOrderService.orderListCurrentForDriver(loginUser, json);
	}
	
	public PagModel orderListHistoryForDriver(BusiOrderQueryDto dto){
		Long loginUserId=dto.getLoginUserId();
		User loginUser=userService.findById(loginUserId);
		String json=JsonUtils.object2Json(dto);
		return busiOrderService.orderListHistoryForDriver(loginUser, json);
	}
	
	public List<EmployeeModel> recreateOrderEmpList(BusiOrderQueryDto dto) {
		Long orgId = dto.getOrganizationId();
		return userService.listEmployeeByOrgId(orgId);
	}
	
	public List<DriverModel> recreateOrderDriverList(BusiOrderQueryDto dto) {
		Long orgId = dto.getOrganizationId();
		return userService.listDriverByDepId(orgId, "");
	}

	public List<VehicleModel> recreateOrderVehicleList(BusiOrderQueryDto dto) {
		Long orgId = dto.getOrganizationId();
		return vehicleService.listDeptVehicle(orgId);
	}
	
	public BusiOrder findOrderByIdSimple(Long order_id)
    {
        return busiOrderService.findOrderByIdSimple(order_id);
    }
	
    public void updateOrderStatus(Integer status, Long order_id)
    {
        busiOrderService.updateOrderStatus(status, order_id);
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
}
