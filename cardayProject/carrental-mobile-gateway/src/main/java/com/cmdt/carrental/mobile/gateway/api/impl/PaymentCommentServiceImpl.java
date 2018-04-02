package com.cmdt.carrental.mobile.gateway.api.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.Driver;
import com.cmdt.carrental.common.entity.Employee;
import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.Message.MessageType;
import com.cmdt.carrental.common.entity.PaymentComment;
import com.cmdt.carrental.common.entity.PaymentCommentForDriver;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.service.DriverService;
import com.cmdt.carrental.common.service.MessageService;
import com.cmdt.carrental.common.service.PaymentCommentService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.mobile.gateway.api.inf.IPaymentService;
import com.cmdt.carrental.mobile.gateway.common.Constants;
import com.cmdt.carrental.mobile.gateway.common.WsResponse;
import com.cmdt.carrental.mobile.gateway.model.PaymentCommentDto;
import com.cmdt.carrental.mobile.gateway.service.BusiOrderAppService;
import com.cmdt.carrental.mobile.gateway.service.UserAppService;
import com.cmdt.carrental.mobile.gateway.util.DateUtil;

public class PaymentCommentServiceImpl implements IPaymentService
{
    private static final Logger LOG = LoggerFactory.getLogger(PaymentCommentServiceImpl.class);
    
    @Autowired
    private BusiOrderAppService busiOrderAppService;
    
    @Autowired
    private UserAppService userAppService;
    
    @Autowired
    private PaymentCommentService paymentCommentService;
    
    @Autowired
    private DriverService driverService;
    
    @Autowired
    private CommunicationService communicationService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private VehicleService vehicleService;
    
    @Override
    @POST
    @Path("/calculateConsumer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response calculateConsumer(PaymentCommentDto paymentCommentDto)
    {
        WsResponse<String> wsResponse = new WsResponse<String>();
        try
        {
            // 判断目前订单状态是否可以进行消费提交
            BusiOrder order = busiOrderAppService.findOrderByIdSimple(paymentCommentDto.getOrderId());
            // 判断订单是否存在
            if (null == order || !(paymentCommentDto.getCash()).matches("^+?(0|[1-9][0-9]*)$"))
            {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(Constants.API_MESSAGE_PARAM_ILLEGAL);
                return Response.ok(wsResponse).build();
            }
            if (order.getStatus() != 4)
            {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(Constants.LIMIT_ORDER_STATUS_INCORRECT);
                return Response.ok(wsResponse).build();
            }
            
            // 进行消费扣除、
            Employee employee = userAppService.findEmployeeByUserId(order.getOrderUserid());
            employee.setMonthLimitLeft(employee.getMonthLimitLeft() - Integer.parseInt(paymentCommentDto.getCash()));
            
            // 更新Employee中的剩余额度信息
            userAppService.updateEmployee(employee);
            
            // 更新订单order表中的状态信息
            busiOrderAppService.updateOrderStatus(15, paymentCommentDto.getOrderId());
            
            // 增加payment_comment信息
            PaymentComment paymentComment = new PaymentComment();
            paymentComment.setOrderId(paymentCommentDto.getOrderId());
            paymentComment.setOrderNo(order.getOrderNo());
            paymentComment.setPaymentCash(Integer.parseInt(paymentCommentDto.getCash()));
            paymentComment.setUserId(order.getOrderUserid());
            paymentComment.setPaymentTime(new Date());
            paymentComment.setDriverId(order.getDriverId());
            paymentCommentService.addPaymentComment(paymentComment);
            
            // 支付完成进行消息推送
            sendPush(paymentCommentDto, order);
            
            wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
            return Response.ok(wsResponse).build();
        }
        catch (Exception e)
        {
            LOG.error("there are some wrongs in calculate Payment! " + e.getMessage());
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            return Response.ok(wsResponse).build();
        }
    }
    
    @Override
    @POST
    @Path("/submitCommentInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submitCommentInfo(PaymentCommentDto paymentCommentDto)
    {
        WsResponse<String> wsResponse = new WsResponse<String>();
        try
        {
            
            // 判断目前订单状态是否可以进行消费提交
            BusiOrder order = busiOrderAppService.findOrderByIdSimple(paymentCommentDto.getOrderId());
            // 判断订单是否存在
            if (null == order || null == paymentCommentDto.getCommentLevel()
                || "".equals(paymentCommentDto.getCommentLevel()))
            {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(Constants.API_MESSAGE_PARAM_ILLEGAL);
                return Response.ok(wsResponse).build();
            }
            if (order.getStatus() != 15)
            {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(Constants.LIMIT_ORDER_STATUS_INCORRECT);
                return Response.ok(wsResponse).build();
            }
            
            // 修改订单表中此条订单的状态信息
            busiOrderAppService.updateOrderStatus(16, paymentCommentDto.getOrderId());
            
            // 更新payment_comment表中的评论信息
            PaymentComment paymentComment = new PaymentComment();
            paymentComment.setOrderId(paymentCommentDto.getOrderId());
            paymentComment.setOrderNo(order.getOrderNo());
            paymentComment.setCommentString(paymentCommentDto.getComment());
            paymentComment.setCommentLevel(paymentCommentDto.getCommentLevel());
            paymentComment.setCommentTime(new Date());
            paymentCommentService.updatePaymentCommentForComment(paymentComment);
            
            // 更新司机的信用等级
            String[] commentLevels = paymentCommentDto.getCommentLevel().split(",");
            double total = 0;
            for (String level : commentLevels)
            {
                total += Double.parseDouble(level);
            }
            Driver driver = driverService.queryDriverCreditRatingById(order.getDriverId());
            driver.setCreditRating((total / commentLevels.length + driver.getRentNum() * driver.getCreditRating())
                / (driver.getRentNum() + 1));
            driver.setRentNum(driver.getRentNum() + 1);
            driver.setId(order.getDriverId());
            driverService.updateDriverCredit(driver);
            
            // 返回正确信息
            wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
            wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            return Response.ok(wsResponse).build();
        }
        catch (Exception e)
        {
            LOG.error("there are some wrongs in submit comment info! " + e.getMessage());
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            return Response.ok(wsResponse).build();
        }
    }
    
    @Override
    @POST
    @Path("/queryPaymentCommentInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response queryPaymentCommentInfo(PaymentCommentDto paymentCommentDto)
    {
        WsResponse<PaymentComment> wsResponse = new WsResponse<PaymentComment>();
        try
        {
            // 查询账单和评论记录
            PaymentComment paymentComment = new PaymentComment();
            paymentComment.setOrderId(paymentCommentDto.getOrderId());
            paymentComment = paymentCommentService.queryPaymentComnent(paymentComment);
            // 获得司机的综合信息
            paymentComment.setCreditRating(
                driverService.queryDriverCreditRatingById(paymentComment.getDriverId()).getCreditRating());
            
            wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
            wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            wsResponse.setResult(paymentComment);
            return Response.ok(wsResponse).build();
        }
        catch (Exception e)
        {
            LOG.error("there are some wrongs in querying paymentComment info! " + e.getMessage());
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            return Response.ok(wsResponse).build();
        }
        
    }
    
    @Override
    @POST
    @Path("/queryDriverCommentList")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response queryDriverCommentList(PaymentCommentDto paymentCommentDto)
    {
        WsResponse<PaymentCommentForDriver> wsResponse = new WsResponse<PaymentCommentForDriver>();
        try
        {
            // 查询账单和评论记录
            List<PaymentComment> commentList =
                paymentCommentService.quyeyPaymentCommentByDriverId(paymentCommentDto.getDriverId());
            PaymentCommentForDriver paymentCommentForDriver = new PaymentCommentForDriver();
            // 获得司机的综合信息
            paymentCommentForDriver.setPaymentCommentList(commentList);
            paymentCommentForDriver.setCreditRating(
                driverService.queryDriverCreditRatingById(paymentCommentDto.getDriverId()).getCreditRating());
            
            wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
            wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            wsResponse.setResult(paymentCommentForDriver);
            return Response.ok(wsResponse).build();
        }
        catch (Exception e)
        {
            LOG.error("there are some wrongs in querying queryDriverCommentList info! " + e.getMessage());
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            return Response.ok(wsResponse).build();
        }
        
    }
    
    @Override
    @POST
    @Path("/queryDriverCommentListPage")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response queryDriverCommentListPage(PaymentCommentDto paymentCommentDto)
    {
        WsResponse<PaymentCommentForDriver> wsResponse = new WsResponse<PaymentCommentForDriver>();
        try
        {
            // 查询账单和评论记录
            List<PaymentComment> commentList = paymentCommentService.quyeyPaymentCommentPageByDriverId(
                paymentCommentDto.getDriverId(), paymentCommentDto.getPageNum(), paymentCommentDto.getPageSize());
            PaymentCommentForDriver paymentCommentForDriver = new PaymentCommentForDriver();
            // 获得司机的综合信息
            paymentCommentForDriver.setPaymentCommentList(commentList);
            paymentCommentForDriver.setCreditRating(
                driverService.queryDriverCreditRatingById(paymentCommentDto.getDriverId()).getCreditRating());
            
            wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
            wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            wsResponse.setResult(paymentCommentForDriver);
            return Response.ok(wsResponse).build();
        }
        catch (Exception e)
        {
            LOG.error("there are some wrongs in querying queryDriverCommentList info! " + e.getMessage());
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            return Response.ok(wsResponse).build();
        }
        
    }
    
    /**
     * <一句话功能简述> <功能详细描述>
     * 
     * @param paymentCommentDto
     * @param order
     * @see [类、类#方法、类#成员]
     */
    private void sendPush(PaymentCommentDto paymentCommentDto, BusiOrder order)
    {
        try
        {
            Long userId = order.getOrderUserid();
            // 查询用车人的信息
            User orderUser = userService.findById(userId);
            Long driverId = order.getDriverId();
            // 查询司机信息
            User driverUser = userService.findById(driverId);
            Vehicle vehicle = vehicleService.findVehicleById(order.getVehicleId());
            
            /**
             * 用车付费成功，进行消息推送
             */
            // 消息推送
            String title = "";
            String msgContent = "";
            // 通过订单ID来找到使用人ID
            
            title = "支付提醒";
            msgContent = "您的订单" + order.getOrderNo() + "已于" + DateUtil.dateToString(new Date()) + "消费"
                + paymentCommentDto.getCash() + "元,请对司机进行评价!";
            communicationService.sendPush(Arrays.asList(orderUser.getPhone()),
                title,
                msgContent,
                com.cmdt.carrental.common.constants.Constants.CARDAY_ENDUSER);
            
            // 消息入库
            // String sql = "insert into
            // message(type,car_no,time,org_id,location,is_end,is_new,warning_id,msg,order_id,title) values
            // (?,?,?,?,?,?,?,?,?,?,?)";
            Message message = new Message();
            message.setType(MessageType.TRAVEL);
            message.setCarNo(vehicle.getVehicleNumber());
            message.setTime(new Date());
            message.setMsg(msgContent);
            message.setTitle(title);
            message.setOrderId(paymentCommentDto.getOrderId());
            message.setOrgId(orderUser.getOrganizationId());
            message.setIsNew(1);
            // 给司机进行消息推送
            // 通过订单ID来找到司机ID
            
            msgContent = "您的订单乘客已完成支付，支付金额为" + paymentCommentDto.getCash() + "元";
            title = "收到支付提醒";
            communicationService.sendPush(Arrays.asList(driverUser.getPhone()),
                title,
                msgContent,
                com.cmdt.carrental.common.constants.Constants.CARDAY_DRIVER);
            
            Message driverMessage = new Message();
            driverMessage.setType(MessageType.TASK);
            driverMessage.setCarNo(vehicle.getVehicleNumber());
            driverMessage.setTime(new Date());
            driverMessage.setMsg(msgContent);
            driverMessage.setTitle(title);
            driverMessage.setOrderId(paymentCommentDto.getOrderId());
            driverMessage.setOrgId(orderUser.getOrganizationId());
            driverMessage.setIsNew(1);
            messageService.saveMessages(Arrays.asList(message, driverMessage));
            
        }
        catch (Exception e)
        {
            LOG.error("there are some wrongs in sending and saving message! " + e.getMessage());
        }
    }
}
