package com.cmdt.carrental.portal.web.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.cmdt.carday.microservice.common.model.response.MessageCode;
import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.BusiOrderAuditRecord;
import com.cmdt.carrental.common.entity.Driver;
import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.Message.MessageType;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.OrderSchedule;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehReturnRegistDto;
import com.cmdt.carrental.common.model.VehReturnRegistModel;
import com.cmdt.carrental.common.model.VehicleSchedule;
import com.cmdt.carrental.common.service.BusiOrderService;
import com.cmdt.carrental.common.service.MessageService;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.RuleService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/order")
public class BusiOrderController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(BusiOrderController.class);

	@Autowired
	private BusiOrderService orderService;

	@Autowired
	private UserService userService;

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private CommunicationService communicationService;

	@Autowired
	private MessageService messageService;
	
	@Autowired
    private OrganizationService organizationService;
	
	@Autowired
	private RuleService ruleService;

	/**
	 * [企业管理员]查询所在企业的订单列表 [部门管理员]查询所在部门的订单列表 [员工]查询自己创建的订单列表
	 * 
	 * @param json{"orderNo":"20161012513783247","orderTime":"2016-10-12",
	 *            "status":"","planTime":"2016-10-14"}
	 * @return
	 */
	// @RequiresPermissions(value = {"order:list"})
	// @RequestMapping(method = RequestMethod.POST)
	// @ResponseBody
	// public Map<String, Object> orderList(@CurrentUser User loginUser, String
	// json)
	// {
	// Map<String, Object> map = new HashMap<>();
	// map.put("data", "");
	// try
	// {
	// PagModel pageModel = orderService.orderList(loginUser, json);
	// map.put("status", "success");
	// map.put("data", pageModel);
	// }
	// catch (Exception e)
	// {
	// LOG.error("BusiOrderController.orderList error:", e);
	// map.put("status", "failure");
	// }
	// return map;
	// }

	/**
	 * [企业管理员]查询所在企业的订单列表 [部门管理员]查询所在部门的订单列表 [员工]查询自己创建的订单列表
	 * 
	 * @return
	 */
	@RequiresPermissions(value = { "order:list" })
	@RequestMapping(value = "/admin/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> adminList(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			BusiOrderQueryDto dto = JsonUtils.json2Object(json, BusiOrderQueryDto.class);
			PagModel pageModel = orderService.adminList(loginUser, dto);
			map.put("status", "success");
			map.put("data", pageModel);
			// will be replace with follow code
			// dto.setLoginUserId(loginUser.getId());
			// map=doRequest(ActionName.ORDERLISTFORPORTAL,new Object[]{dto});
		} catch (Exception e) {
			LOG.error("BusiOrderController.orderList error:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * [部门管理员]订单审核列表
	 * 
	 * @param json{"status":"0"}
	 * @return
	 */
	// @RequiresPermissions(value = {"orderaudit:list"})
	// @RequestMapping(value = "/audit/list", method = RequestMethod.POST)
	// @ResponseBody
	// public Map<String, Object> auditList(@CurrentUser User loginUser, String
	// json)
	// {
	// Map<String, Object> map = new HashMap<>();
	// map.put("data", "");
	// try
	// {
	// PagModel pageModel = orderService.auditList(loginUser, json);
	// map.put("status", "success");
	// map.put("data", pageModel);
	// }
	// catch (Exception e)
	// {
	// LOG.error("BusiOrderController.auditList error:", e);
	// map.put("status", "failure");
	// }
	// return map;
	// }

	/**
	 * [部门管理员]订单审核列表
	 * 
	 * @param json{"status":"0"}
	 * @return
	 */
	@RequiresPermissions(value = { "orderaudit:list" })
	@RequestMapping(value = "/auditOrder/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> auditList(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			BusiOrderQueryDto dto = JsonUtils.json2Object(json, BusiOrderQueryDto.class);
			PagModel pageModel = orderService.auditList(loginUser, dto);
			map.put("status", "success");
			map.put("data", pageModel);
			// will be replace with follow code
			// dto.setLoginUserId(loginUser.getId());
			// map=doRequest(ActionName.ORDERAUDITLIST,new Object[]{dto});
		} catch (Exception e) {
			LOG.error("BusiOrderController.auditOrderList error:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 根据订单id查询订单信息
	 * 
	 * @param json{"orderNo":}
	 * @return
	 */
	@RequiresPermissions(value = { "order:list" })
	@RequestMapping(value = "/{id}/queryBusiOrderByOrderNo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryBusiOrderByOrderNo(@CurrentUser User loginUser, @PathVariable("id") Long id) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			BusiOrder busiOrder = orderService.findOne(id);
			map.put("status", "success");
			map.put("data", busiOrder);
			// will be replace with follow code
			// map=doRequest(ActionName.ORDERVIEW,new Object[]{loginUser.getId(),id});
		} catch (Exception e) {
			LOG.error("BusiOrderController.queryBusiOrderByOrderNo error:", e);
			map.put("status", "failure");
		}
		return map;
	}
	/**
	 * 车辆回车单查看
	 * @param loginUser
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}/queryVehReturnRegistByOrderNo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryVehReturnRegistByOrderNo(@CurrentUser User loginUser, @PathVariable("id") Long id) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			VehReturnRegistModel vehReturnRegistModel = orderService.findVehReturnRegistByOrderNo(id);
			map.put("status", "success");
			map.put("data", vehReturnRegistModel);
		} catch (Exception e) {
			LOG.error("BusiOrderController.queryVehReturnRegistByOrderNo error:", e);
			map.put("status", "failure");
		}
		return map;
	}
	/**
	 * 车辆回车登记单修改
	 * @param loginUser
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/updateVehReturnRegist", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateVehReturnRegist(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			VehReturnRegistDto dto=JsonUtils.json2Object(json, VehReturnRegistDto.class);
			orderService.updateVehReturnRegist(loginUser.getId(),dto);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("BusiOrderController.updateVehReturnRegist error:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * [部门管理员]订单排车列表
	 * 
	 * @param json{"status":"1"}
	 * @return
	 */
	// @RequiresPermissions(value = {"orderarrange:list"})
	// @RequestMapping(value = "/arrange/list", method = RequestMethod.POST)
	// @ResponseBody
	// public Map<String, Object> allocateList(@CurrentUser User loginUser,
	// String json)
	// {
	// Map<String, Object> map = new HashMap<>();
	// map.put("data", "");
	// try
	// {
	// PagModel pageModel = orderService.allocateList(loginUser, json);
	// map.put("status", "success");
	// map.put("data", pageModel);
	// }
	// catch (Exception e)
	// {
	// LOG.error("BusiOrderController.allocateList error:", e);
	// map.put("status", "failure");
	// }
	// return map;
	// }

	/**
	 * [部门管理员]订单排车列表
	 * 
	 * @param json{"status":"1"}
	 * @return
	 */
	@RequiresPermissions(value = { "orderarrange:list" })
	@RequestMapping(value = "/allocate/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> allocateList(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			BusiOrderQueryDto dto = JsonUtils.json2Object(json, BusiOrderQueryDto.class);
			PagModel pageModel = orderService.allocateList(loginUser, dto);
			map.put("status", "success");
			map.put("data", pageModel);
			// will be replace with follow code
			// dto.setLoginUserId(loginUser.getId());
			// map=doRequest(ActionName.ORDERALLOCATELIST,new Object[]{dto});
		} catch (Exception e) {
			LOG.error("BusiOrderController.allocateList error:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * [部门管理员,员工]创建订单
	 * 
	 * @param json:
	 * @return map
	 */
	@RequiresPermissions(value = { "order:create" })
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
//	public Map<String, Object> create(@CurrentUser User loginUser, String json) {
//		Map<String, Object> map = new HashMap<>();
//		map.put("data", "");
//		try {
//			String msg = orderService.createOrder(loginUser, json);
//			if (StringUtils.isNotBlank(msg)) {
//				map.put("status", "failure");
//				map.put("data", msg);
//				return map;
//			}
//			map.put("status", "success");
//			// will be replace with follow code
//			// BusiOrderDto dto = JsonUtils.json2Object(json, BusiOrderDto.class);
//			// dto.setLoginUserId(loginUser.getId());
//			// dto.setPlanTime(DateUtils.date2String(TypeUtils.obj2Date(dto.getPlanTime()), "yyyy-MM-dd HH:mm:ss"));
//			// map=doRequest(ActionName.ORDERCREATE,new Object[]{dto});
//		} catch (Exception e) {
//			LOG.error("order.create error", e);
//			map.put("status", "failure");
//		}
//		return map;
//	}
	
    public ResponseEntity<Map<String,Object>> create(@CurrentUser User loginUser,HttpServletRequest request, HttpServletResponse response){
		HttpHeaders headers = new HttpHeaders(); 
    	headers.setContentType(MediaType.TEXT_HTML);
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	String msg = "";
    	try{
    		ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
			CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
			MultipartHttpServletRequest multipartRequest = commonsMultipartResolver
					.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
//			if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()||loginUser.isEndUser()) {
			if (true) {
				BusiOrder order=new BusiOrder();
				String orderNo=DateUtils.getNowDate("yyyyMMdd") + generateLoginUserIDforOrder(loginUser.getId()) + subTimeStringforOrder(System.currentTimeMillis());//201504A000001
				order.setOrderNo(orderNo);
				order.setOrderTime(new Timestamp(System.currentTimeMillis()));
				//orderType
				int orderType = TypeUtils.obj2Integer(multipartRequest.getParameter("orderType"));
				order.setOrderType(orderType);
				if(orderType == 0){//toB
					order.setStatus(0);
				}else{//toC
					order.setStatus(1);
				}
				Long orderUserid=TypeUtils.obj2Long(multipartRequest.getParameter("orderUserid"));
				User orderUser=userService.findOne(orderUserid);
				Organization org_orderUser=organizationService.findOne(orderUser.getOrganizationId());
				if(orderType == 0){
					if(org_orderUser.getParentId()==0){
						msg="用车人还未分配部门，无法下单，请联系部门管理员!";
						map.put("status", "failure");
						map.put("success", false);
						map.put("msg", msg);
						return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
					}
				}
				order.setOrderUserid(orderUserid);
				order.setOrderUsername(orderUser.getRealname());
				order.setOrderUserphone(orderUser.getPhone());
				order.setCity(TypeUtils.obj2String(multipartRequest.getParameter("city")));
				order.setFromPlace(TypeUtils.obj2String(multipartRequest.getParameter("fromPlace")));
				order.setToPlace(TypeUtils.obj2String(multipartRequest.getParameter("toPlace")));
				order.setPlanStTime(TypeUtils.obj2Date(multipartRequest.getParameter("planTime")));
				Double durationTime=TypeUtils.obj2Double(multipartRequest.getParameter("durationTime1"));//单位分钟,预计行程时间
				Double waitTime=TypeUtils.obj2Double(multipartRequest.getParameter("waitTime1"));//单位分钟,等待时长
				order.setDurationTime(durationTime);
				order.setWaitTime(waitTime);
				Double totalTime = durationTime + waitTime;
//				LOG.info("order create cal-------------:"+(int)(totalTime.doubleValue()));
				order.setPlanEdTime(DateUtils.addMinutes(order.getPlanStTime(), (int)(totalTime.doubleValue())));
				String vehicleType=TypeUtils.obj2String(multipartRequest.getParameter("vehicleType"));
				if(!vehicleType.equals("0")&&!vehicleType.equals("1")&&!vehicleType.equals("2")&&!vehicleType.equals("3")){
					msg = "车辆类型不存在！";
					map.put("status", "failure");
					map.put("success", false);
					map.put("msg", msg);
					return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
				}
				order.setVehicleType(vehicleType);
				order.setPassengerNum(TypeUtils.obj2Integer(multipartRequest.getParameter("passengerNum")));
				order.setOrderReason(TypeUtils.obj2String(multipartRequest.getParameter("orderReason")));
				order.setReturnType(TypeUtils.obj2Integer(multipartRequest.getParameter("returnType")));
				order.setComments(TypeUtils.obj2String(multipartRequest.getParameter("comments")));
				order.setOrganizationId(TypeUtils.obj2Long(multipartRequest.getParameter("organizationId")));
				//lng,lat
				order.setFromLat(TypeUtils.obj2Double(multipartRequest.getParameter("fromLat")));
				order.setFromLng(TypeUtils.obj2Double(multipartRequest.getParameter("fromLng")));
				order.setToLat(TypeUtils.obj2Double(multipartRequest.getParameter("toLat")));
				order.setToLng(TypeUtils.obj2Double(multipartRequest.getParameter("toLng")));
				
				//ADD SECRECT LEVEL
				Integer secretLevel = TypeUtils.obj2Integer(multipartRequest.getParameter("secretLevel"));
				order.setSecretLevel( (secretLevel == null || secretLevel == 0) ? null : secretLevel);
				
				String attachName=order.getOrderNo()+".jpg";
				//上传订单附件
				boolean boo=upload(attachName,multipartRequest);
    			if(boo){
	    			order.setOrderAttach(attachName);
    			}
    			
				order.setVehicleUsage(TypeUtils.obj2Integer(multipartRequest.getParameter("vehicleUsage")));
				order.setDrivingType(TypeUtils.obj2Integer(multipartRequest.getParameter("drivingType")));
	   			order.setUnitName(loginUser.getOrganizationName());
				
				msg = ruleService.getOrderValidateResult(order, loginUser.getId());
				if(StringUtils.isEmpty(msg)){//规则验证通过
					orderService.createOrder(order);
					map.put("status", "success");
					map.put("success", true);
				} else {
					map.put("status", "failure");
					map.put("success", false);
					map.put("msg", msg);
					return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
				}
			}
//    	else{
//				msg = "用户没有添加订单的权限!";
//    			map.put("status", "failure");
//    			map.put("success", false);
//    			map.put("msg", msg);
//    			return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
//    		}
    	}catch(Exception e){
    		 map.put("status", "failure");
    		 map.put("success", false);
    		 LOG.error("Failed to create due to unexpected error!", e);
    	}
    	return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
    }
	
	/**
	 * [部门管理员,员工]面审批订单
	 * 
	 * @param json:
	 * @return map
	 */
	@RequiresPermissions(value = { "order:create" })
	@RequestMapping(value = "/createWithoutApproval", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createWithoutApproval(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			String msg = orderService.createWithoutApprovalOrder(loginUser, json);
			if (StringUtils.isNotBlank(msg)) {
				map.put("status", "failure");
				map.put("data", msg);
				return map;
			}
			map.put("status", "success");
			// will be replace with follow code
			// BusiOrderDto dto = JsonUtils.json2Object(json, BusiOrderDto.class);
			// dto.setLoginUserId(loginUser.getId());
			// dto.setPlanTime(DateUtils.date2String(TypeUtils.obj2Date(dto.getPlanTime()), "yyyy-MM-dd HH:mm:ss"));
			// map=doRequest(ActionName.ORDERCREATE,new Object[]{dto});
		} catch (Exception e) {
			LOG.error("order.createWithoutApproval error", e);
			map.put("status", "failure");
		}
		return map;
	}

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
    
	/**
	 * [部门管理员,员工]补录订单
	 * 
	 * @param json:
	 * @return map
	 */
	@RequiresPermissions(value = { "orderrecreate:create" })
	@RequestMapping(value = "/recreate", method = RequestMethod.POST)
	@ResponseBody
//	public Map<String, Object> recreate(@CurrentUser User loginUser, String json) {
//		Map<String, Object> map = new HashMap<>();
//		map.put("data", "");
//		try {
//			String msg = orderService.recreateOrder(loginUser, json);
//			if (StringUtils.isNotBlank(msg)) {
//				map.put("data", msg);
//				map.put("status", "failure");
//			} else {
//				map.put("status", "success");
//			}
//			// will be replace with follow code
//			// BusiOrderDto dto = JsonUtils.json2Object(json,
//			// BusiOrderDto.class);
//			// dto.setLoginUserId(loginUser.getId());
//			// dto.setFactStTime(DateUtils.date2String(TypeUtils.obj2Date(dto.getFactStTime()), "yyyy-MM-dd HH:mm:ss"));
//			// dto.setFactEdTime(DateUtils.date2String(TypeUtils.obj2Date(dto.getFactEdTime()), "yyyy-MM-dd HH:mm:ss"));
//			// map=doRequest(ActionName.ORDERRECREATE,new Object[]{dto});
//		} catch (Exception e) {
//			LOG.error("BusiOrderController.recreate error:", e);
//			map.put("status", "failure");
//		}
//		return map;
//	}
	
    public ResponseEntity<Map<String,Object>> recreate(@CurrentUser User loginUser,HttpServletRequest request, HttpServletResponse response){
		HttpHeaders headers = new HttpHeaders(); 
    	headers.setContentType(MediaType.TEXT_HTML);
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	String msg = "";
    	try{
    		ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
			CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
			MultipartHttpServletRequest multipartRequest = commonsMultipartResolver
					.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
//			if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()) {
			if (true) {
				BusiOrder order = new BusiOrder();
//				String orderNo = DateUtils.getNowDate("yyyyMMdd") + loginUser.getId() + System.currentTimeMillis();
				String orderNo=DateUtils.getNowDate("yyyyMMdd") + generateLoginUserIDforOrder(loginUser.getId()) + subTimeStringforOrder(System.currentTimeMillis());
				String paperOrderNo= "P"+orderNo;
				order.setOrderNo(orderNo);
				order.setPaperOrderNo(paperOrderNo);
				order.setOrderTime(new Date());
				Long orderUserid = TypeUtils.obj2Long(multipartRequest.getParameter("orderUserid"));
				User orderUser = userService.findOne(orderUserid);
				Organization org_orderUser = organizationService.findOne(orderUser.getOrganizationId());
				if (org_orderUser.getParentId() == 0) {
					msg = "用车人还未分配部门，无法下单，请联系部门管理员!";
					map.put("status", "failure");
					map.put("success", false);
					map.put("msg", msg);
					return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
				}
				order.setOrderUserid(orderUserid);
				order.setOrderUsername(TypeUtils.obj2String(multipartRequest.getParameter("orderUsername")));
				order.setOrderUserphone(TypeUtils.obj2String(multipartRequest.getParameter("orderUserphone")));
				order.setCity(TypeUtils.obj2String(multipartRequest.getParameter("city")));
				order.setFromPlace(TypeUtils.obj2String(multipartRequest.getParameter("fromPlace")));
				order.setToPlace(TypeUtils.obj2String(multipartRequest.getParameter("toPlace")));
				order.setFactStTime(TypeUtils.obj2Date(multipartRequest.getParameter("factStTime")));
				order.setFactEdTime(TypeUtils.obj2Date(multipartRequest.getParameter("factEdTime")));
				order.setDurationTime(TypeUtils.obj2Double(multipartRequest.getParameter("durationTime")));
				order.setWaitTime(TypeUtils.obj2Double(multipartRequest.getParameter("waitTime")));
				String vehicleType=TypeUtils.obj2String(multipartRequest.getParameter("vehicleType"));
				if(!vehicleType.equals("0")&&!vehicleType.equals("1")&&!vehicleType.equals("2")&&!vehicleType.equals("3")){
					msg = "车辆类型不存在！";
					map.put("status", "failure");
					map.put("success", false);
					map.put("msg", msg);
					return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
				}
				order.setVehicleType(vehicleType);
				order.setPassengerNum(TypeUtils.obj2Integer(multipartRequest.getParameter("passengerNum")));
				order.setOrderReason(TypeUtils.obj2String(multipartRequest.getParameter("orderReason")));
				order.setReturnType(TypeUtils.obj2Integer(multipartRequest.getParameter("returnType")));
				order.setComments(TypeUtils.obj2String(multipartRequest.getParameter("comments")));
//				order.setStatus(4);// 补录订单状态为已完成
				order.setStatus(16);// 补录订单状态为已完成
				order.setVehicleId(TypeUtils.obj2Long(multipartRequest.getParameter("vehicleId")));
				order.setDriverId(TypeUtils.obj2Long(multipartRequest.getParameter("driverId")));
				order.setDriverName(TypeUtils.obj2String(multipartRequest.getParameter("driverName")));
				order.setDriverPhone(TypeUtils.obj2String(multipartRequest.getParameter("driverPhone")));
				order.setOrganizationId(TypeUtils.obj2Long(multipartRequest.getParameter("organizationId")));
				
				//ADD SECRECT LEVEL
				Integer secretLevel = TypeUtils.obj2Integer(multipartRequest.getParameter("secretLevel"));
				order.setSecretLevel( (secretLevel == null || secretLevel == 0) ? null : secretLevel);
				
				String attachName=order.getOrderNo()+".jpg";
				//上传订单附件
				boolean boo=upload(attachName,multipartRequest);
    			if(boo){
	    			order.setOrderAttach(attachName);
    			}
    			
				order.setUnitName(loginUser.getOrganizationName());
				order.setStMileage(TypeUtils.obj2Long(multipartRequest.getParameter("stMileage")));
				order.setEdMileage(TypeUtils.obj2Long(multipartRequest.getParameter("edMileage")));
				order.setVehicleUsage(TypeUtils.obj2Integer(multipartRequest.getParameter("vehicleUsage")));
				order.setDrivingType(TypeUtils.obj2Integer(multipartRequest.getParameter("drivingType")));
				// 补录订单类型固定
				order.setOrderType(0);
				// 如果行程时间和已存在订单有冲突,则提示行程时间有误
				msg = orderService.hasConflict(order);
				if(StringUtils.isBlank(msg)){
					orderService.recreateOrder(order);
					map.put("status", "success");
					map.put("success", true);
				} else {
					map.put("status", "failure");
					map.put("success", false);
					map.put("msg", msg);
					return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
				}
			}
//			else{
//    			map.put("status", "failure");
//    			map.put("success", false);
//    			return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
//    		}
    	}catch(Exception e){
    		 map.put("status", "failure");
    		 map.put("success", false);
    		 LOG.error("Failed to create due to unexpected error!", e);
    	}
    	return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
    }

	/**
	 * 【部门管理员，员工】订单结束
	 *
	 */
	@RequiresPermissions(value = { "order:update" })
	@RequestMapping(value = "/finish", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> finishOrder(@CurrentUser User loginUser, String json){
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			if(jsonMap.get("id") !=null)
			{
				MessageCode messageCode = orderService.orderFinish(loginUser, TypeUtils.obj2Long(jsonMap.get("id")));
				if(messageCode == MessageCode.COMMON_SUCCESS){
					map.put("status", "success");
				}
			}else{
				map.put("status", "failure");
			}
		}catch (Exception e){
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * [部门管理员,员工]修改订单
	 * 
	 * @return
	 */
//	@RequiresPermissions(value = { "order:update" })
	@RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> showUpdateForm(@CurrentUser User loginUser, @PathVariable("id") Long id) {
		Map<String, Object> map = new HashMap<>();
		BusiOrder order = orderService.findOne(id);
		if (order != null) {
			map.put("data", order);
			map.put("status", "success");
		} else {
			map.put("status", "failure");
		}
		// will be replace with follow code
//		Map<String, Object> map=doRequest(ActionName.ORDERVIEW,new Object[]{loginUser.getId(),id});
		return map;
	}

	/**
	 * [部门管理员,员工]修改订单
	 * 
	 * @param
	 * @return
	 */
	@RequiresPermissions(value = { "order:update" })
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
//	public Map<String, Object> update(@CurrentUser User loginUser, String json) {
//		Map<String, Object> map = new HashMap<>();
//		map.put("data", "");
//		try {
//			String msg = orderService.updateOrder(loginUser, json);
//			if (StringUtils.isNotBlank(msg)) {
//				map.put("data", msg);
//				map.put("status", "failure");
//			} else {
//				map.put("status", "success");
//			}
//			// will be replace with follow code
////			 BusiOrderDto dto = JsonUtils.json2Object(json, BusiOrderDto.class);
////			 dto.setLoginUserId(loginUser.getId());
////			 dto.setPlanTime(DateUtils.date2String(TypeUtils.obj2Date(dto.getPlanTime()), "yyyy-MM-dd HH:mm:ss"));
////			 map=doRequest(ActionName.ORDERUPDATE,new Object[]{dto});
//		} catch (Exception e) {
//			LOG.error("BusiOrderController.update error:", e);
//			map.put("status", "failure");
//		}
//		return map;
//	}

	public ResponseEntity<Map<String,Object>> update(@CurrentUser User loginUser,HttpServletRequest request, HttpServletResponse response){
    	HttpHeaders headers = new HttpHeaders(); 
    	headers.setContentType(MediaType.TEXT_HTML);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		String msg = "";
		try {
			ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
			CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
			MultipartHttpServletRequest multipartRequest = commonsMultipartResolver
					.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
//			if (loginUser.isEntAdmin() || loginUser.isDeptAdmin() || loginUser.isEndUser()) {
			if (true) {
				BusiOrder order = orderService.findOne(Long.valueOf(String.valueOf(multipartRequest.getParameter("id"))));
				if(!loginUser.getId().equals(order.getOrderUserid()))
				{
					msg = "只允许修改自己的订单!";
					map.put("status", "failure");
					map.put("success", false);
					map.put("msg", msg);
					return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
				}
				if (order.getStatus() == 5) {
					Long orderUserid = TypeUtils.obj2Long(multipartRequest.getParameter("orderUserid"));
					User orderUser = userService.findOne(orderUserid);
					Organization org_orderUser = organizationService.findOne(orderUser.getOrganizationId());
					if (org_orderUser.getParentId() == 0) {
						msg = "用车人还未分配部门，无法修改订单，请联系部门管理员!";
						map.put("status", "failure");
						map.put("success", false);
						map.put("msg", msg);
						return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
					}
					order.setOrderUserid(orderUserid);
					order.setOrderUsername(orderUser.getUsername());
					order.setOrderUserphone(orderUser.getPhone());
					order.setCity(TypeUtils.obj2String(multipartRequest.getParameter("city")));
					order.setFromPlace(TypeUtils.obj2String(multipartRequest.getParameter("fromPlace")));
					order.setFromLng(TypeUtils.obj2Double(multipartRequest.getParameter("fromLng")));
					order.setFromLat(TypeUtils.obj2Double(multipartRequest.getParameter("fromLat")));
					order.setToPlace(TypeUtils.obj2String(multipartRequest.getParameter("toPlace")));
					order.setToLng(TypeUtils.obj2Double(multipartRequest.getParameter("toLng")));
					order.setToLat(TypeUtils.obj2Double(multipartRequest.getParameter("toLat")));
					order.setPlanStTime(TypeUtils.obj2Date(multipartRequest.getParameter("planTime")));
					Double durationTime = TypeUtils.obj2Double(multipartRequest.getParameter("durationTime"));// 预计行程时间，单位分钟
					Double waitTime = TypeUtils.obj2Double(multipartRequest.getParameter("waitTime"));
					order.setDurationTime(durationTime);
					order.setWaitTime(waitTime);
					Double totalTime = durationTime + waitTime;
					LOG.info("order update cal-------------:" + (int) (totalTime.doubleValue()));
					order.setPlanEdTime(DateUtils.addMinutes(order.getPlanStTime(), (int) (totalTime.doubleValue())));
					String vehicleType=TypeUtils.obj2String(multipartRequest.getParameter("vehicleType"));
					if(!vehicleType.equals("0")&&!vehicleType.equals("1")&&!vehicleType.equals("2")&&!vehicleType.equals("3")){
						msg = "车辆类型不存在！";
						map.put("status", "failure");
						map.put("success", false);
						map.put("msg", msg);
						return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
					}
					order.setVehicleType(vehicleType);
					order.setPassengerNum(TypeUtils.obj2Integer(multipartRequest.getParameter("passengerNum")));
					order.setOrderReason(TypeUtils.obj2String(multipartRequest.getParameter("orderReason")));
					order.setReturnType(TypeUtils.obj2Integer(multipartRequest.getParameter("returnType")));
					order.setComments(TypeUtils.obj2String(multipartRequest.getParameter("comments")));
					order.setStatus(0);
					order.setOrganizationId(TypeUtils.obj2Long(multipartRequest.getParameter("organizationId")));
					
					//ADD SECRECT LEVEL
					Integer secretLevel = TypeUtils.obj2Integer(multipartRequest.getParameter("secretLevel"));
					order.setSecretLevel( (secretLevel == null || secretLevel == 0) ? null : secretLevel);
		            
					String attachName=order.getOrderNo()+".jpg";
					//上传订单附件
					boolean boo=upload(attachName,multipartRequest);
	    			if(boo){
		    			order.setOrderAttach(attachName);
	    			}
	    			
		            order.setVehicleUsage(TypeUtils.obj2Integer(multipartRequest.getParameter("vehicleUsage")));
					order.setDrivingType(TypeUtils.obj2Integer(multipartRequest.getParameter("drivingType")));
					msg = ruleService.getOrderValidateResult(order, loginUser.getId());
					if(StringUtils.isEmpty(msg)){//规则验证通过
						orderService.updateCreateOrder(order);
						map.put("status", "success");
						map.put("success", true);
					}
				} else {
					msg = "只能修改被驳回的订单!";
					map.put("status", "failure");
					map.put("success", false);
					map.put("msg", msg);
					return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
				}
			} 
//			else {
//				map.put("status", "failure");
//				map.put("success", false);
//				return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
//			}
		} catch (Exception e) {
			map.put("status", "failure");
			map.put("success", false);
			LOG.error("Failed to update due to unexpected error!", e);
		}
		return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
	}
	
	/**
	 * [部门管理员,员工]修改订单
	 * 
	 * @param
	 * @return
	 */
	@RequiresPermissions(value = { "orderrecreate:create" })
	@RequestMapping(value = "/recreateUpdate", method = RequestMethod.POST)
	@ResponseBody
//	public Map<String, Object> recreateUpdate(@CurrentUser User loginUser, String json) {
//		Map<String, Object> map = new HashMap<>();
//		map.put("data", "");
//		try {
//			 String msg = orderService.updateReCreateOrder(loginUser, json);
//			 if (StringUtils.isNotBlank(msg)) {
//			 map.put("data", msg);
//			 map.put("status", "failure");
//			 } else {
//			 map.put("status", "success");
//			 }
//			// will be replace with follow code
////			BusiOrderDto dto = JsonUtils.json2Object(json, BusiOrderDto.class);
////			dto.setLoginUserId(loginUser.getId());
////			dto.setFactStTime(DateUtils.date2String(TypeUtils.obj2Date(dto.getFactStTime()), "yyyy-MM-dd HH:mm:ss"));
////			dto.setFactEdTime(DateUtils.date2String(TypeUtils.obj2Date(dto.getFactEdTime()), "yyyy-MM-dd HH:mm:ss"));
////			map = doRequest(ActionName.ORDERRECREATEUPDATE, new Object[] { dto });
//		} catch (Exception e) {
//			LOG.error("BusiOrderController.recreateUpdate error:", e);
//			map.put("status", "failure");
//		}
//		return map;
//	}
	
	public ResponseEntity<Map<String,Object>> recreateUpdate(@CurrentUser User loginUser,HttpServletRequest request, HttpServletResponse response){
    	HttpHeaders headers = new HttpHeaders(); 
    	headers.setContentType(MediaType.TEXT_HTML);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		String msg = "";
		try {
			ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
			CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
			MultipartHttpServletRequest multipartRequest = commonsMultipartResolver
					.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
//			if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()) {
			if (true) {
				BusiOrder order = orderService.findOne(Long.valueOf(String.valueOf(multipartRequest.getParameter("id"))));
				if (order.getStatus() == 16 && order.getPlanStTime() == null) {
					Long orderUserid = TypeUtils.obj2Long(multipartRequest.getParameter("orderUserid"));
					User orderUser = userService.findOne(orderUserid);
					Organization org_orderUser = organizationService.findOne(orderUser.getOrganizationId());
					if (org_orderUser.getParentId() == 0) {
						msg = "用车人还未分配部门，无法修改订单，请联系部门管理员!";
						map.put("status", "failure");
						map.put("success", false);
						map.put("msg", msg);
					}
					order.setOrderUserid(orderUserid);
					order.setOrderUsername(TypeUtils.obj2String(multipartRequest.getParameter("orderUsername")));
					order.setOrderUserphone(TypeUtils.obj2String(multipartRequest.getParameter("orderUserphone")));
					order.setCity(TypeUtils.obj2String(multipartRequest.getParameter("city")));
					order.setFromPlace(TypeUtils.obj2String(multipartRequest.getParameter("fromPlace")));
					order.setToPlace(TypeUtils.obj2String(multipartRequest.getParameter("toPlace")));
					order.setFactStTime(TypeUtils.obj2Date(multipartRequest.getParameter("factStTime")));
					order.setDurationTime(TypeUtils.obj2Double(multipartRequest.getParameter("durationTime")));// 单位分钟
					order.setWaitTime(TypeUtils.obj2Double(multipartRequest.getParameter("waitTime")));
					order.setFactEdTime(TypeUtils.obj2Date(multipartRequest.getParameter("factEdTime")));
					String vehicleType=TypeUtils.obj2String(multipartRequest.getParameter("vehicleType"));
					if(!vehicleType.equals("0")&&!vehicleType.equals("1")&&!vehicleType.equals("2")&&!vehicleType.equals("3")){
						msg = "车辆类型不存在！";
						map.put("status", "failure");
						map.put("success", false);
						map.put("msg", msg);
					}
					order.setVehicleType(vehicleType);
					order.setPassengerNum(TypeUtils.obj2Integer(multipartRequest.getParameter("passengerNum")));
					order.setOrderReason(TypeUtils.obj2String(multipartRequest.getParameter("orderReason")));
					order.setReturnType(TypeUtils.obj2Integer(multipartRequest.getParameter("returnType")));
					order.setComments(TypeUtils.obj2String(multipartRequest.getParameter("comments")));
					order.setOrganizationId(TypeUtils.obj2Long(multipartRequest.getParameter("organizationId")));
					order.setVehicleId(TypeUtils.obj2Long(multipartRequest.getParameter("vehicleId")));
					order.setDriverId(TypeUtils.obj2Long(multipartRequest.getParameter("driverId")));
					order.setDriverName(TypeUtils.obj2String(multipartRequest.getParameter("driverName")));
					order.setDriverPhone(TypeUtils.obj2String(multipartRequest.getParameter("driverPhone")));
					
					String attachName=order.getOrderNo()+".jpg";
					//上传订单附件
					boolean boo=upload(attachName,multipartRequest);
	    			if(boo){
		    			order.setOrderAttach(attachName);
	    			}
	    			
					order.setUnitName(loginUser.getOrganizationName());
					order.setStMileage(TypeUtils.obj2Long(multipartRequest.getParameter("stMileage")));
					order.setEdMileage(TypeUtils.obj2Long(multipartRequest.getParameter("edMileage")));
					order.setVehicleUsage(TypeUtils.obj2Integer(multipartRequest.getParameter("vehicleUsage")));
					order.setDrivingType(TypeUtils.obj2Integer(multipartRequest.getParameter("drivingType")));
					// 如果行程时间和已存在订单有冲突,则提示行程时间有误
					msg = orderService.hasConflict(order);
					if(StringUtils.isBlank(msg)){
						orderService.updateReCreateOrder(order);
						map.put("status", "success");
						map.put("success", true);
					} else {
						map.put("status", "failure");
						map.put("success", false);
						map.put("msg", msg);
					}
				} else {
					msg = "只允许修改补录订单!";
					map.put("status", "failure");
					map.put("success", false);
					map.put("msg", msg);
				}
			}
//			else {
//				map.put("status", "failure");
//				map.put("success", false);
//			}
		} catch (Exception e) {
			map.put("status", "failure");
			map.put("success", false);
			LOG.error("Failed to update due to unexpected error!", e);
		}
		return new ResponseEntity<Map<String,Object>>(map,headers,HttpStatus.OK);
	}

    /**
     * 上传附件
     * @param attachName
     * @param request
     * @param response
     */
	public boolean upload(String attachName, MultipartHttpServletRequest multipartRequest) {
		boolean boo = false;
		try {
			multipartRequest.setCharacterEncoding("UTF-8");
			MultipartFile multiFile = multipartRequest.getFile("orderAttach");
			if (!multiFile.isEmpty()) {
				String savePath = "/opt/apache-tomcat/webapps/resources/upload/order";
//				String savePath =multipartRequest.getServletContext().getRealPath("resources/upload/order");
				File dir=new File(savePath);
				if(!dir.exists()){
					dir.mkdirs();
				}
				File dest = new File(savePath + File.separator + attachName);
				if (dest.exists()) {
					dest.delete();
				}
				multiFile.transferTo(dest);
				boo = true;
			}
		} catch (IOException e) {
			LOG.error("Failed to upload due to IO error!", e);
			return boo;
		} catch (Exception e) {
			LOG.error("Failed to upload due to unexpected error!", e);
			return boo;
		}
		return boo;
	}
	
	/**
	 * [部门管理员]订单审核
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions(value = { "orderaudit:update" })
	@RequestMapping(value = "/audit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> audit(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			 orderService.orderAudit(loginUser, json);
			 map.put("status", "success");
			 // 进行消息推送 和数据入库
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Long orderId = TypeUtils.obj2Long(jsonMap.get("id"));
			Integer status = TypeUtils.obj2Integer(jsonMap.get("status"));
//			String refuseComments = TypeUtils.obj2String(jsonMap.get("refuseComments"));
			 sendPush(orderId, status);
			// will be replace with follow code
//			BusiOrderDto dto = new BusiOrderDto();
//			dto.setLoginUserId(loginUser.getId());
//			dto.setId(orderId);
//			dto.setStatus(status);
//			dto.setRefuseComments(refuseComments);
//			map = doRequest(ActionName.ORDERAUDIT, new Object[] { dto });
		} catch (Exception e) {
			LOG.error("BusiOrder audit error!", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * [部门管理员]订单审核完成后发短消息
	 * 
	 * @param
	 * @return
	 */
	@RequiresPermissions(value = { "orderaudit:update" })
	@RequestMapping(value = "/auditSendMsg", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> auditSendMsg(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			orderService.auditSendMsg(loginUser, json);
			map.put("status", "success");
			// will be replace with follow code
//			BusiOrderDto dto = JsonUtils.json2Object(json, BusiOrderDto.class);
//			dto.setLoginUserId(loginUser.getId());
//			map = doRequest(ActionName.ORDERAUDITSENDMSG, new Object[] { dto });
		} catch (Exception e) {
			LOG.error("BusiOrderController.auditSendMsg error:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * [部门管理员,普通员工]查看订单审核历史记录
	 * 
	 * @param
	 * @return
	 */
	@RequiresPermissions(value = { "order:list" })
	@RequestMapping(value = "/auditHistory", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> auditHistory(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			List<BusiOrderAuditRecord> list = orderService.findAuditHistory(loginUser, json);
			map.put("data", list);
			map.put("status", "success");
			// will be replace with follow code
//			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
//			Long orderId=TypeUtils.obj2Long(jsonMap.get("id"));
//			map = doRequest(ActionName.ORDERAUDITHISTORY, new Object[] { loginUser.getId(), orderId});
		} catch (Exception e) {
			LOG.error("BusiOrderController.auditHistory error:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * [部门管理员]订单排车获取司机列表
	 * 
	 * @param
	 * @return
	 */
	@RequiresPermissions(value = { "orderarrange:update" })
	@RequestMapping(value = "/arrange/drivers", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getAvailableDrivers(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			BusiOrderQueryDto dto = JsonUtils.json2Object(json, BusiOrderQueryDto.class);
			PagModel pageModel = orderService.getAvailableDrivers(loginUser, dto);
			map.put("status", "success");
			map.put("data", pageModel);
			// will be replace with follow code
//			 dto.setLoginUserId(loginUser.getId());
//			 map=doRequest(ActionName.ORDERAVAILABLEDRIVERS,new Object[]{dto});
		} catch (Exception e) {
			LOG.error("BusiOrderController.getAvailableDrivers error:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * [部门管理员]查看订单排车时间表
	 * 
	 * @param
	 * @return
	 */
	@RequiresPermissions(value = { "orderarrange:update" })
	@RequestMapping(value = "/schedule", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> showOrderSchedule(String json) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			 List<OrderSchedule> list = orderService.showOrderSchedule(json);
			 map.put("data", list);
			 map.put("status", "success");
			// will be replace with follow code
//			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
//			Long id = TypeUtils.obj2Long(jsonMap.get("id"));
//			Long vehicleId = TypeUtils.obj2Long(jsonMap.get("vehicleId"));
//			map = doRequest(ActionName.ORDERSCHEDULE, new Object[] { vehicleId,id });
		} catch (Exception e) {
			LOG.error("BusiOrderController.showOrderSchedule error:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * [部门管理员]订单排车
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions(value = { "orderarrange:update" })
	@RequestMapping(value = "/arrange", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> allocate(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			String msg = orderService.orderAllocate(loginUser, json);
			if (StringUtils.isNotBlank(msg)) {
				map.put("data", msg);
				map.put("status", "failure");
			} else {
				map.put("status", "success");
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				Long id = TypeUtils.obj2Long(jsonMap.get("id"));
				// 开始消息0推送和入库
				// 用车人进行提醒
//				sendPush(id, 91);
				// 司机进行提醒
//				sendPush(id, 95);
			}	
			// will be replace with follow code
			// dto.setLoginUserId(loginUser.getId());
			// map=doRequest(ActionName.ORDERALLOCATE,new Object[]{dto});
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("exception in BusiOrderController.allocate: order arrange", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * [部门管理员,员工]删除订单
	 * 
	 * @return
	 */
	@RequiresPermissions(value = { "order:delete" })
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(@CurrentUser User loginUser, @PathVariable("id") Long id) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			String msg = orderService.deleteOrder(loginUser, id);
			if (StringUtils.isNotBlank(msg)) {
				map.put("data", msg);
				map.put("status", "failure");
			} else {
				map.put("status", "success");
			}
			// will be replace with follow code
//			 map=doRequest(ActionName.ORDERDELETE,new Object[]{loginUser.getId(),id});
		} catch (Exception e) {
			LOG.error("BusiOrderController.delete error:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * [用车人]订单取消 权限:谁创建谁取消
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions(value = { "order:create" })
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> orderCancel(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Long id = TypeUtils.obj2Long(jsonMap.get("id"));
			orderService.cancelOrder(loginUser, id, null);
			map.put("status", "success");
			// will be replace with follow code
//			 BusiOrderDto dto = JsonUtils.json2Object(json, BusiOrderDto.class);
//			 dto.setLoginUserId(loginUser.getId());
//			 map=doRequest(ActionName.ORDERCANCEL,new Object[]{dto});
		} catch (Exception e) {
			LOG.error("BusiOrderController.orderCancel error:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * [部门管理员]查看订单排车时间表
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions(value = { "orderarrange:update" })
	@RequestMapping(value = "/vehicleSchedule", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryVehicleSchedule(String json) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Long vehicleId = Long.parseLong(String.valueOf(jsonMap.get("vehicleId")));
			String planStTimeF = String.valueOf(jsonMap.get("planStTimeF"));
			List<VehicleSchedule> list = orderService.queryVehicleSchedule(vehicleId, planStTimeF);
			map.put("data", list);
			map.put("status", "success");
			// will be replace with follow code
			// map=doRequest(ActionName.ORDERVEHICLESCHEDULE,new Object[]{vehicleId,planStTimeF});
		} catch (Exception e) {
			LOG.error("BusiOrderController.queryVehicleSchedule error:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 处理完状态需要给用车人发送信息状态
	 * 
	 * @param orderId
	 * @param status
	 * @see [类、类#方法、类#成员]
	 */
	private void sendPush(Long orderId, int status) {
		boolean isOrderUser = true;

		// 获得这条订单信息
		BusiOrder order = orderService.findOrderByIdSimple(orderId);
		// 通过订单ID来找到使用人ID
		Long userId = order.getOrderUserid();
		// 查询用车人的信息;为什么不写一个关联查询呢？没有为什么因为我懒 哈哈哈哈哈哈哈哈哈！！！
		User orderUser = userService.findById(userId);
		// 通过订单ID来找到车辆VEHICLE ID、
		Long vehicleId = order.getVehicleId();
		Vehicle vehicle = vehicleService.findVehicleById(vehicleId);
		// 通过订单ID来找到司机DRIVER ID
		Long driverId = order.getDriverId();
		// 进行消息推送 这个发送模板是什么玩意！我也不知道。。。
		String msgContent = "";
		String title = "";
		String phoneNum = "";
		User driverUser = null;
		switch (status) {
		case 1:// 审核通过，给予通过通知
			phoneNum = orderUser.getPhone();
			msgContent = "您的订单" + order.getOrderNo() + "已于" + DateUtils.dateToString(new Date()) + "审核通过!";
			title = "订单审核通过提醒";
			break;
		case 5:// 审核不通过，给予不通过通知
			phoneNum = orderUser.getPhone();
			msgContent = "您的订单" + order.getOrderNo() + "已于" + DateUtils.dateToString(new Date()) + "审核未通过,请前往我的行程查看详情!";
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
			// msgContent = "您有一条新的任务，预约用车时间" +
			// DateUtil.dateToString(order.getPlanStTime()) + "，车牌号"
			// + vehicle.getVehicleNumber() + "，出发地" + order.getFromPlace() +
			// "，目的地" + order.getToPlace()
			// + "，乘客张先生，手机号" + orderUser.getPhone() + "，请留意行程开始时间并准时出发";
			msgContent = "您已被安排新的任务，请注意查看详情!";
			title = "任务提醒";
			break;
		default:
			break;
		}
		communicationService.sendPush(Arrays.asList(phoneNum), title, msgContent,
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
}
