package com.cmdt.carrental.mobile.gateway.api.impl;

import com.cmdt.carrental.common.entity.*;
import com.cmdt.carrental.common.entity.Message.MessageType;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.model.*;
import com.cmdt.carrental.common.service.MessageService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.mobile.gateway.api.inf.IBusiOrderService;
import com.cmdt.carrental.mobile.gateway.common.Constants;
import com.cmdt.carrental.mobile.gateway.common.WsResponse;
import com.cmdt.carrental.mobile.gateway.service.BusiOrderAppService;
import com.cmdt.carrental.mobile.gateway.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class BusiOrderServiceImpl implements IBusiOrderService {
    private static final Logger LOG = LoggerFactory.getLogger(BusiOrderServiceImpl.class);

    @Autowired
    private BusiOrderAppService busiOrderAppService;

    @Autowired
    private CommunicationService communicationService;

    @Autowired
    private UserService userService;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private MessageService messageService;

    @Override
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createOrder(BusiOrderDto dto) {
        WsResponse<BusiOrderDto> wsResponse = new WsResponse<BusiOrderDto>();
        try {
//            // CR-3894 change by zhou yangang
//            // 判断员工是否有 月度余额
//            Employee employee = userService.findEmployeeByUserId(dto.getLoginUserId());
//            Double limitLeft = employee.getMonthLimitvalue();
//            // 月度余额 = -1 表示月度余额不限
//            // Double类型做比较，必须写1.0, 不能写1，否则 -1时进不去...
//            if (limitLeft == null || (limitLeft != -1 && limitLeft <= 0.0) ) {
//                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
//                wsResponse.getMessages().add("月度余额不足");
//            } else {

            String msg = busiOrderAppService.createOrder(dto);
            if (StringUtils.isBlank(msg)) {
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
                wsResponse.setResult(dto);
            } else {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(msg);
            }
//            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method createOrder error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/update")
    public Response updateOrder(BusiOrderDto dto) {
        WsResponse<BusiOrderDto> wsResponse = new WsResponse<BusiOrderDto>();
        try {
            String msg = busiOrderAppService.updateOrder(dto);
            if (StringUtils.isBlank(msg)) {
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
                wsResponse.setResult(dto);
            } else {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(msg);
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method updateOrder error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/view")
    public Response viewOrder(BusiOrderQueryDto dto) {
        WsResponse<BusiOrder> wsResponse = new WsResponse<BusiOrder>();
        try {
            if (dto.getId() == null) {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add("订单id不能为空!");
            } else {
                BusiOrder order = busiOrderAppService.viewOrder(dto);
                if (null != order) {
                    wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                    wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
                    wsResponse.setResult(order);
                } else {
                    wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                    wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
                }
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method viewOrder error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/delete")
    public Response deleteOrder(BusiOrderDto dto) {
        WsResponse<?> wsResponse = new WsResponse<>();
        try {
            String msg = busiOrderAppService.deleteOrder(dto);
            if (StringUtils.isBlank(msg)) {
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            } else {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(msg);
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method deleteOrder error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/recreate")
    public Response recreateOrder(BusiOrderDto dto) {
        WsResponse<BusiOrderDto> wsResponse = new WsResponse<BusiOrderDto>();
        try {
            String msg = busiOrderAppService.recreateOrder(dto);
            if (StringUtils.isBlank(msg)) {
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            } else {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(msg);
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method recreateOrder error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/recreateUpdate")
    public Response recreateUpdate(BusiOrderDto dto) {
        WsResponse<BusiOrderDto> wsResponse = new WsResponse<BusiOrderDto>();
        try {
            String msg = busiOrderAppService.recreateUpdate(dto);
            if (StringUtils.isBlank(msg)) {
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            } else {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(msg);
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method recreateUpdate error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/audit")
    public Response audit(BusiOrderDto dto) {
        WsResponse<BusiOrderDto> wsResponse = new WsResponse<BusiOrderDto>();
        try {
            String msg = busiOrderAppService.audit(dto);
            if (StringUtils.isBlank(msg)) {
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);

                sendPush(dto.getId(), dto.getStatus());
            } else {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(msg);
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method audit error:", e);
        }

        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/auditSendMsg")
    public Response auditSendMsg(BusiOrderDto dto) {
        WsResponse<BusiOrderDto> wsResponse = new WsResponse<BusiOrderDto>();
        try {
            String msg = busiOrderAppService.auditSendMsg(dto);
            if (StringUtils.isBlank(msg)) {
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            } else {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(msg);
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method auditSendMsg error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/auditHistory")
    public Response auditHistory(BusiOrderQueryDto dto) {
        WsResponse<List<BusiOrderAuditRecord>> wsResponse = new WsResponse<List<BusiOrderAuditRecord>>();
        try {
            if (dto.getId() == null) {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add("订单id不能为空!");
            } else {
                List<BusiOrderAuditRecord> list = busiOrderAppService.auditHistory(dto);
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
                wsResponse.setResult(list);
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method auditHistory error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/availableVehicles")
    public Response getAvailableVehicles(BusiOrderQueryDto dto) {
        WsResponse<PagModel> wsResponse = new WsResponse<PagModel>();
        try {
            Long loginUserId = dto.getLoginUserId();
            User loginUser = userService.findById(loginUserId);
            if (loginUser.isEntAdmin()) {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add("企业管理员不能查询此服务!");
            } else {
                if (dto.getId() == null) {
                    wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                    wsResponse.getMessages().add("订单id不能为空!");
                } else {
                    PagModel pagModel = busiOrderAppService.getAvailableVehicles(dto);
                    wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                    wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
                    wsResponse.setResult(pagModel);
                }
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method getAvailableVehicles error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/availableDrivers")
    public Response getAvailableDrivers(BusiOrderQueryDto dto) {
        WsResponse<PagModel> wsResponse = new WsResponse<PagModel>();
        try {
            if (dto.getId() == null) {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add("订单id不能为空!");
            } else {
                PagModel pagModel = busiOrderAppService.getAvailableDrivers(dto);
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
                wsResponse.setResult(pagModel);
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method getAvailableDrivers error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/allocate")
    public Response allocate(BusiOrderDto dto) {
        WsResponse<BusiOrderDto> wsResponse = new WsResponse<BusiOrderDto>();
        try {
            String msg = busiOrderAppService.allocate(dto);
            if (StringUtils.isBlank(msg)) {
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
                // 用车人进行提醒
                sendPush(dto.getId(), 91);
                // 司机进行提醒
                sendPush(dto.getId(), 95);

            } else {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(msg);
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method allocate error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/vehicleOut")
    public Response orderVehicleOut(BusiOrderDto dto) {
        WsResponse<BusiOrderDto> wsResponse = new WsResponse<BusiOrderDto>();
        try {
            String msg = busiOrderAppService.orderVehicleOut(dto);
            if (StringUtils.isBlank(msg)) {
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);

                sendPush(dto.getId(), 92);
            } else {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(msg);
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method orderVehicleOut error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/reachFromPlace")
    public Response orderReachFromPlace(BusiOrderDto dto) {
        WsResponse<BusiOrderDto> wsResponse = new WsResponse<BusiOrderDto>();
        try {
            String msg = busiOrderAppService.orderReachFromPlace(dto);
            if (StringUtils.isBlank(msg)) {
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
                sendPush(dto.getId(), 93);
            } else {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(msg);
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method orderReachFromPlace error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/ongoing")
    public Response orderOngoing(BusiOrderDto dto) {
        WsResponse<BusiOrderDto> wsResponse = new WsResponse<BusiOrderDto>();
        try {
            String msg = busiOrderAppService.orderOngoing(dto);
            if (StringUtils.isBlank(msg)) {
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            } else {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(msg);
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method orderOngoing error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/waiting")
    public Response orderWaiting(BusiOrderDto dto) {
        WsResponse<BusiOrderDto> wsResponse = new WsResponse<BusiOrderDto>();
        try {
            String msg = busiOrderAppService.orderWaiting(dto);
            if (StringUtils.isBlank(msg)) {
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            } else {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(msg);
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method orderWaiting error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/finish")
    public Response orderFinish(BusiOrderDto dto) {
        WsResponse<BusiOrderDto> wsResponse = new WsResponse<BusiOrderDto>();
        try {
            String msg = busiOrderAppService.orderFinish(dto);
            if (StringUtils.isBlank(msg)) {
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
                sendPush(dto.getId(), 94);
            } else {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(msg);
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method orderFinish error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/cancel")
    public Response orderCancel(BusiOrderDto dto) {
        WsResponse<BusiOrderDto> wsResponse = new WsResponse<BusiOrderDto>();
        try {
            String msg = busiOrderAppService.orderCancel(dto);
            if (StringUtils.isBlank(msg)) {
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            } else {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(msg);
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method orderCancel error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/list")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response orderList(BusiOrderQueryDto dto) {
        WsResponse<PagModel> wsResponse = new WsResponse<PagModel>();
        try {
            PagModel pagModel = busiOrderAppService.orderList(dto);
            wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
            wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            wsResponse.setResult(pagModel);
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method orderList error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/auditList")
    public Response auditList(BusiOrderQueryDto dto) {
        WsResponse<PagModel> wsResponse = new WsResponse<PagModel>();
        try {
            String status_arr = ",0,1,2,5,6,11,12,3,13,4,15,16,";
            String msg = "";
            if (dto.getStatus() != null) {
                if (status_arr.indexOf("," + dto.getStatus() + ",") == -1) {
                    msg = "status值有误!";
                }
            }
            if (StringUtils.isBlank(msg)) {
                PagModel pagModel = busiOrderAppService.auditList(dto);
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
                wsResponse.setResult(pagModel);
            } else {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(msg);
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method auditList error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/allocateList")
    public Response allocateList(BusiOrderQueryDto dto) {
        WsResponse<PagModel> wsResponse = new WsResponse<PagModel>();
        try {
            String status_arr = ",0,1,2,5,6,11,12,3,13,4,15,16,";
            String msg = "";
            if (dto.getStatus() != null) {
                if (status_arr.indexOf("," + dto.getStatus() + ",") == -1) {
                    msg = "status值有误!";
                }
            }
            if (StringUtils.isBlank(msg)) {
                PagModel pagModel = busiOrderAppService.allocateList(dto);
                wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
                wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
                wsResponse.setResult(pagModel);
            } else {
                wsResponse.setStatus(Constants.API_STATUS_FAILURE);
                wsResponse.getMessages().add(msg);
            }
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method allocateList error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/driver/currentList")
    public Response orderListCurrentForDriver(BusiOrderQueryDto dto) {
        WsResponse<PagModel> wsResponse = new WsResponse<PagModel>();
        try {
            PagModel pagModel = busiOrderAppService.orderListCurrentForDriver(dto);
            wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
            wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            wsResponse.setResult(pagModel);
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method orderListCurrentForDriver error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/driver/historyList")
    public Response orderListHistoryForDriver(BusiOrderQueryDto dto) {
        WsResponse<PagModel> wsResponse = new WsResponse<PagModel>();
        try {
            PagModel pagModel = busiOrderAppService.orderListHistoryForDriver(dto);
            wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
            wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            wsResponse.setResult(pagModel);
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method orderListHistoryForDriver error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/recreateOrderEmpList")
    public Response recreateOrderEmpList(@Valid @NotNull BusiOrderQueryDto dto) {
        WsResponse<List<EmployeeModel>> wsResponse = new WsResponse<List<EmployeeModel>>();
        try {
            List<EmployeeModel> emps = busiOrderAppService.recreateOrderEmpList(dto);
            wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
            wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            wsResponse.setResult(emps);
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method recreateOrderEmpList error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/recreateOrderDriverList")
    public Response recreateOrderDriverList(BusiOrderQueryDto dto) {
        WsResponse<List<DriverModel>> wsResponse = new WsResponse<List<DriverModel>>();
        try {
            List<DriverModel> drivers = busiOrderAppService.recreateOrderDriverList(dto);
            wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
            wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            wsResponse.setResult(drivers);
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method recreateOrderDriverList error:", e);
        }
        return Response.ok(wsResponse).build();
    }

    @Override
    @POST
    @Path("/recreateOrderVehicleList")
    public Response recreateOrderVehicleList(BusiOrderQueryDto dto) {
        WsResponse<List<VehicleModel>> wsResponse = new WsResponse<List<VehicleModel>>();
        try {
            List<VehicleModel> vehicles = busiOrderAppService.recreateOrderVehicleList(dto);
            wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
            wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
            wsResponse.setResult(vehicles);
        } catch (Exception e) {
            wsResponse.setStatus(Constants.API_STATUS_FAILURE);
            wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
            LOG.error("method recreateOrderVehicleList error:", e);
        }
        return Response.ok(wsResponse).build();
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
        BusiOrder order = busiOrderAppService.findOrderByIdSimple(orderId);
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
                msgContent = "您的订单" + order.getOrderNo() + "已于" + DateUtil.dateToString(new Date()) + "审核通过!";
                title = "订单审核通过提醒";
                break;
            case 5:// 审核不通过，给予不通过通知
                phoneNum = orderUser.getPhone();
                msgContent =
                        "您的订单" + order.getOrderNo() + "已于" + DateUtil.dateToString(new Date()) + "审核未通过,请前往我的行程查看详情!";
                title = "订单审核未通过提醒";
                break;
            case 91:// 管理员已经完成排车
                phoneNum = orderUser.getPhone();
                // vehicle = vehicleService.findVehicleById(vehicleId);
                driverUser = userService.findById(driverId);
                msgContent = "您的订单" + order.getOrderNo() + "已于" + DateUtil.dateToString(new Date()) + "完成排车,车牌号为 "
                        + vehicle.getVehicleNumber() + ",司机为" + driverUser.getRealname() + ", 手机号为 " + driverUser.getPhone()
                        + ", 请留意行程开始时间，并准时在出发地等待!";
                title = "排车提醒";
                break;
            case 92:// 司机已出车执行任务
                phoneNum = orderUser.getPhone();
                // vehicle = vehicleService.findVehicleById(vehicleId);
                driverUser = userService.findById(driverId);
                msgContent = "您的订单" + order.getOrderNo() + "司机" + driverUser.getRealname() + "已出车,车牌号"
                        + vehicle.getVehicleNumber() + ",手机号为" + driverUser.getPhone() + ", 预约用车时间为"
                        + DateUtil.dateToString(order.getPlanStTime()) + ",请留意行程开始时间并准时在出发地等待!";
                title = "出车提醒";
                break;
            case 93:// 司机已到达出发地
                phoneNum = orderUser.getPhone();
                // vehicle = vehicleService.findVehicleById(vehicleId);
                driverUser = userService.findById(driverId);
                msgContent = "您的订单" + order.getOrderNo() + "司机" + driverUser.getRealname() + "已到达出发地,车牌号"
                        + vehicle.getVehicleNumber() + ",手机号为" + driverUser.getPhone() + ", 预约用车时间为"
                        + DateUtil.dateToString(order.getPlanStTime()) + ",请留意行程开始时间并准时上车!";
                title = "到达出发地提醒";
                break;
            case 94:// 车到达目的地

                phoneNum = orderUser.getPhone();
                driverUser = userService.findById(driverId);
                msgContent = "您的订单" + order.getOrderNo() + "司机" + driverUser.getRealname() + "已经到达目的地，请完成支付!";
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
}
