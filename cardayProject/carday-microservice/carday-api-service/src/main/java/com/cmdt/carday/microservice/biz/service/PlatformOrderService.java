package com.cmdt.carday.microservice.biz.service;

import com.cmdt.carday.microservice.common.api.GeoService;
import com.cmdt.carday.microservice.common.model.Order.OrderDto;
import com.cmdt.carday.microservice.common.model.Order.enums.OrderReturnType;
import com.cmdt.carday.microservice.common.model.Order.enums.OrderStatus;
import com.cmdt.carday.microservice.common.model.Order.enums.OrderType;
import com.cmdt.carday.microservice.common.model.Order.enums.VehicleType;
import com.cmdt.carday.microservice.common.model.geo.BaiduLocation;
import com.cmdt.carday.microservice.common.model.response.MessageCode;
import com.cmdt.carday.microservice.common.model.response.exception.ServiceException;
import com.cmdt.carday.microservice.model.request.order.AllocateDto;
import com.cmdt.carday.microservice.model.request.order.AuditSendMsgDto;
import com.cmdt.carday.microservice.model.request.order.OrderAuditDto;
import com.cmdt.carday.microservice.model.response.order.VehicleScheduleDto;
import com.cmdt.carrental.common.entity.*;
import com.cmdt.carrental.common.entity.Message.MessageType;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.*;
import com.cmdt.carrental.common.service.*;
import com.cmdt.carrental.common.util.DateUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PlatformOrderService {
    private static final Logger LOG = LogManager.getLogger(PlatformOrderService.class);

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
    private ShouqiService shouqiService;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private GeoService geoService;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * orderList for App
     *
     * @param user
     * @return
     */
    public PagModel orderListForApp(User user, BusiOrderQueryDto dto) {
        LOG.debug("PlatformOrderService.orderListForApp");
        String col_orderby = dto.getColOrderBy();
        String orderBySql = " order by t.plan_st_time desc";
        if (StringUtils.isBlank(col_orderby) || col_orderby.trim().equals("1")) {
            dto.setColOrderBy(orderBySql);
        }
        //查询列表app专用
        return busiOrderService.orderListForApp(user, dto);
    }

    /**
     * adminList(原orderList) for portal
     *
     * @param user
     * @return
     */
    public PagModel orderListForPortal(User user, BusiOrderQueryDto dto) {
        LOG.debug("PlatformOrderService.orderListForPortal");
        //查询列表portal专用
        return busiOrderService.adminList(user, dto);
    }

    /**
     * auditList
     *
     * @param user
     * @return
     */
    public PagModel auditList(User user, BusiOrderQueryDto dto) {
        LOG.debug("PlatformOrderService.auditList");
        String status_arr = ",0,1,2,5,6,11,12,3,13,4,15,16,";
        String msg = "";
        if (dto.getStatus() != null) {
            if (status_arr.indexOf("," + dto.getStatus() + ",") == -1) {
                throw new ServiceException(MessageCode.COMMON_PARAMETER_ERROR, "status值有误!");
            }
        }
        return busiOrderService.auditList(user, dto);
    }

    /**
     * allocateList
     *
     * @param user
     * @return
     */
    public PagModel allocateList(User user, BusiOrderQueryDto dto) {
        LOG.debug("PlatformOrderService.allocateList");
        String status_arr = ",0,1,2,5,6,11,12,3,13,4,15,16,";
        if (dto.getStatus() != null && status_arr.indexOf("," + dto.getStatus() + ",") == -1) {
            String msg = "status值有误!";
            throw new ServiceException(MessageCode.COMMON_PARAMETER_ERROR, msg);
        }
        return busiOrderService.allocateList(user, dto);
    }

    public PagModel orderListCurrentForDriver(User user, Integer currentPage, Integer numPerPage) {
        return busiOrderService.orderListCurrentForDriver(user, currentPage, numPerPage);
    }

    public PagModel orderListHistoryForDriver(User user, Integer currentPage, Integer numPerPage) {
        return busiOrderService.orderListHistoryForDriver(user, currentPage, numPerPage);
    }

    public List<EmployeeModel> recreateOrderEmpList(Long orgId) {
        return userService.listEmployeeByOrgId(orgId);
    }

    public List<DriverModel> recreateOrderDriverList(Long orgId) {
        return userService.listDriverByDepId(orgId);
    }

    public List<VehicleModel> recreateOrderVehicleList(Long orgId) {
        return vehicleService.listDeptVehicle(orgId);
    }

    /**
     * createOrder
     *
     * @param user
     * @return
     */
    public OrderDto createOrder(User user, OrderDto dto) throws Exception {
        LOG.debug("PlatformOrderService.createOrder");
        return create(user, dto);
    }

    public OrderDto create(User user, OrderDto dto) throws Exception {
        String msg = "";
        // 创建订单，必填字段，校验不能为空
        if (dto.getOrderUserid() == null) {
            msg = "用车人Id不能为空，请填写用车人Id！";
        }
        if (StringUtils.isBlank(dto.getCity())) {
            msg = "用车城市不能为空，请填写用车城市！";
        }
        if (StringUtils.isBlank(dto.getFromPlace())) {
            msg = "出发地不能为空，请填写出发地！";
        }
        if (StringUtils.isBlank(dto.getToPlace())) {
            msg = "目的地不能为空，请填写目的地！";
        }
        if (dto.getPlanTime() == null) {
            msg = "预约用车时间不能为空，请填写预约用车时间！";
        }
        if (dto.getDurationTime() == null || dto.getDurationTime() <= 0) {
            msg = "预计行程时间必须大于0，请填写预计行程时间！";
        }
        if (dto.getVehicleType() == null) {
            msg = "用车类型不能为空，请填写用车类型！";
        }
        if (dto.getPassengerNum() == null || dto.getPassengerNum() <= 0) {
            msg = "乘车人数必须大于0！";
        }
        if (StringUtils.isBlank(dto.getOrderReason())) {
            msg = "用车事由不能为空，请填写用车事由！";
        }
        if (dto.getReturnType() == null) {
            msg = "是否往返无效,请选择是否往返！";
        }
        if (dto.getReturnType() == OrderReturnType.Round_Trip &&
                dto.getWaitTime() == null) {
            msg = "等待时长为空，请填写等待时长！";
        }
        if (StringUtils.isNotEmpty(msg)) {
            throw new ServiceException(MessageCode.COMMON_PARAMETER_ERROR, msg);
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

        if (user == null) {
            throw new ServiceException(MessageCode.COMMON_PARAMETER_ERROR, "用户不存在！");
        } else {

            if (!user.getOrganizationId().equals(dto.getOrganizationId())) {
                throw new ServiceException(MessageCode.COMMON_PARAMETER_ERROR, "登录用户组织Id和订单组织Id不一致！");
            }
            if (!user.getId().equals(dto.getOrderUserid())) {
                throw new ServiceException(MessageCode.COMMON_PARAMETER_ERROR, "登录用户Id与用车人Id不一致！");
            }
        }
        BusiOrder orderModel = busiOrderService.createOrder(user, dto);
        return getOrderDtoFromModel(orderModel);
    }

    private OrderDto getOrderDtoFromModel(BusiOrder orderModel) {
        OrderDto dto = new OrderDto();

        dto.setId(orderModel.getId());
        dto.setOrderNo(orderModel.getOrderNo());
        dto.setCity(orderModel.getCity());
        dto.setFromPlace(orderModel.getFromPlace());
        dto.setFromLat(orderModel.getFromLat());
        dto.setFromLng(orderModel.getFromLng());
        dto.setToPlace(orderModel.getToPlace());
        dto.setToLat(orderModel.getToLat());
        dto.setToLng(orderModel.getToLng());
        dto.setPlanTime(orderModel.getPlanStTime());
        dto.setOrderReason(orderModel.getOrderReason());
        dto.setOrganizationId(orderModel.getOrganizationId());
        dto.setOrderUserid(orderModel.getOrderUserid());
        dto.setOrderType(OrderType.create(orderModel.getOrderType()));
        dto.setPassengerNum(orderModel.getPassengerNum());
        dto.setReturnType(OrderReturnType.create(orderModel.getReturnType()));
        dto.setStatus(OrderStatus.create(orderModel.getStatus()));
        dto.setVehicleType(VehicleType.createVehicleType(Integer.valueOf(orderModel.getVehicleType())));
        dto.setWaitTime(orderModel.getWaitTime());
        dto.setComments(orderModel.getComments());

        // 补录订单的属性
        dto.setVehicleId(orderModel.getVehicleId());
        dto.setDriverId(orderModel.getDriverId());
        dto.setFactStTime(orderModel.getFactStTime());
        dto.setFactEdTime(orderModel.getFactEdTime());

        return dto;
    }

    public Double[] transAddrToPoint(String address) throws Exception {
        Double[] dou = null;
        BaiduLocation point = geoService.getPointByAddress(address);
        if (point != null) {
            dou = new Double[2];
            dou[0] = point.getLat();
            dou[1] = point.getLng();
        }
//        Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.REVERSEADDRESS, new Object[]{address});
//        if (result.get("data") != null) {
//            ObjectMapper MAPPER = new ObjectMapper();
//            JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
//            if ("000".equals(jsonNode.get("status").asText())) {
//                JsonNode rs1 = (JsonNode) jsonNode.get("result");
//                {"status":0,"result":{"location":{"lng":121.61513124890567,"lat":31.250432343160256},"precise":1,"confidence":80,"level":"道路"}}
//                if ("0".equals(rs1.get("status").asText())) {
//                    JsonNode rs2 = (JsonNode) rs1.get("result").get("location");
//                    dou = new Double[2];
//                    dou[0] = rs2.get("lat").asDouble();
//                    dou[1] = rs2.get("lng").asDouble();
//                }
//            }
//        }

        return dou;
    }

    /**
     * updateOrder
     *
     * @return
     */
    public void updateOrder(User user, OrderDto dto) {
        LOG.debug("PlatformOrderService.updateOrder");
        String msg = update(user, dto);
        if (StringUtils.isNotBlank(msg)) {
            throw new ServiceException(MessageCode.COMMON_PARAMETER_ERROR, msg);
        }
    }

    public String update(User user, OrderDto dto) {
        String msg = "";
        try {
            // 修改订单，必填字段，校验不能为空
            if (dto.getId() == null) {
                msg = "订单不能为空，请填写订单Id！";
                return msg;
            }
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
            if (dto.getPlanTime() == null) {
                msg = "预约用车时间不能为空，请填写预约用车时间！";
                return msg;
            }
            if (dto.getDurationTime() == null || dto.getDurationTime() <= 0) {
                msg = "预计行程时间必须大于0，请填写预计行程时间！";
                return msg;
            }
            if (null == dto.getVehicleType()) {
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
            if (dto.getReturnType() == null) {
                msg = "是否往返无效,请选择是否往返！";
                return msg;
            }
            if (dto.getReturnType() == OrderReturnType.Round_Trip && dto.getWaitTime() == null) {
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

            if (null == busiOrder) {
                return "该订单不存在！";
            }

            if (null == user) {
                return "该用户不存在！";
            }

            // 只有部门管理员、员工可以修改订单
            if (user.getUserCategory() != 3 && user.getUserCategory() != 4) {
                return "该用户没有修改订单的权限！";
            }
            if (!dto.getOrderUserid().equals(busiOrder.getOrderUserid())) {
                return "只能修改自己的订单！";
            }
            msg = busiOrderService.updateOrder(user, dto);
        } catch (Exception e) {
            throw new ServiceException(MessageCode.COMMON_FAILURE);
        }
        return msg;
    }

    /**
     * recreateOrder
     *
     * @param user
     * @param dto
     * @return
     */
    public OrderDto recreateOrder(User user, OrderDto dto) {
        LOG.debug("PlatformOrderService.recreateOrder");
        if (user == null) {
            throw new ServiceException(MessageCode.COMMON_DATA_NOT_EXIST, "用户不存在");
        }
        return recreate(user, dto);
    }

    public OrderDto recreate(User user, OrderDto dto) {
        // 补录订单，必填字段，校验不能为空
        String msg = "";
        if (dto.getOrderUserid() == null) {
            msg = "用车人Id不能为空，请填写用车人Id！";
        }
        if (StringUtils.isBlank(dto.getCity())) {
            msg = "用车城市不能为空，请填写用车城市！";
        }
        if (StringUtils.isBlank(dto.getFromPlace())) {
            msg = "出发地不能为空，请填写出发地！";
        }
        if (StringUtils.isBlank(dto.getToPlace())) {
            msg = "目的地不能为空，请填写目的地！";
        }
        if (dto.getFactStTime() == null) {
            msg = "实际开始时间不能为空，请填写实际开始时间！";
        }
        if (dto.getFactEdTime() == null) {
            msg = "实际结束时间不能为空，请填写实际结束时间！";
        }
        if (dto.getVehicleType() == null) {
            msg = "用车类型不能为空，请填写用车类型！";
        }
        if (dto.getPassengerNum() == null || dto.getPassengerNum() <= 0) {
            msg = "乘车人数必须大于0！";
        }
        if (StringUtils.isBlank(dto.getOrderReason())) {
            msg = "用车事由不能为空，请填写用车事由！";
        }
        if (dto.getReturnType() == null) {
            msg = "是否往返无效,请选择是否往返！";
        }
        if (dto.getReturnType() == OrderReturnType.Round_Trip && dto.getWaitTime() == null) {
            msg = "等待时长为空，请填写等待时长！";
        }
        if (dto.getDriverId() == null) {
            msg = "司机Id为空，请填写司机Id！";
        } else if (userService.findById(dto.getDriverId()) == null) {
            msg = "司机不存在";
        }
        if (dto.getVehicleId() == null) {
            msg = "车辆Id为空，请填写车辆Id！";
        } else if (vehicleService.findVehicleById(dto.getVehicleId()) == null) {
            msg = "车辆不存在";
        }
        if (!user.getOrganizationId().equals(dto.getOrganizationId())) {
            msg = "登录用户组织Id和订单组织Id不一致！";
        }
        if (StringUtils.isNotEmpty(msg)) {
            throw new ServiceException(MessageCode.COMMON_PARAMETER_ERROR, msg);
        }
        BusiOrder orderModel = busiOrderService.recreateOrder(user, dto);
        return getOrderDtoFromModel(orderModel);
    }

    /**
     * updateReCreateOrder
     *
     * @param user
     * @param dto
     * @return
     */
    public MessageCode updateReCreateOrder(User user, OrderDto dto) {
        LOG.debug("PlatformOrderService.updateReCreateOrder");
        String msg = updateReCreate(user, dto);
        if (StringUtils.isNotBlank(msg)) {
            throw new ServiceException(MessageCode.COMMON_PARAMETER_ERROR, msg);
        } else {
            return MessageCode.COMMON_SUCCESS;
        }
    }

    public String updateReCreate(User loginUser, OrderDto dto) {
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
        if (null == dto.getFactStTime()) {
            msg = "实际开始时间不能为空，请填写实际开始时间！";
            return msg;
        }
        if (null == dto.getFactEdTime()) {
            msg = "实际结束时间不能为空，请填写实际结束时间！";
            return msg;
        }
        if (null == dto.getVehicleType()) {
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
        if (dto.getReturnType() == null) {
            msg = "是否往返无效,请选择是否往返！";
            return msg;
        }
        if (dto.getReturnType() == OrderReturnType.Round_Trip && dto.getWaitTime() == null) {
            msg = "等待时长为空，请填写等待时长！";
            return msg;
        }
        if (dto.getDriverId() == null) {
            msg = "司机Id为空，请填写司机Id！";
            return msg;
        } else if (userService.findById(dto.getDriverId()) == null) {
            return "司机不存在";
        }
        if (dto.getVehicleId() == null) {
            msg = "车辆Id为空，请填写车辆Id！";
            return msg;
        } else if (vehicleService.findVehicleById(dto.getVehicleId()) == null) {
            return "车辆不存在";
        }
//        Long loginUserId = dto.getLoginUserId();
//        User loginUser = userService.findById(loginUserId);
        BusiOrder busiOrder = busiOrderService.findOne(dto.getId());
        if (!dto.getOrderUserid().equals(busiOrder.getOrderUserid())) {
            return "只能修改自己的订单！";
        }
        // 只要部门管理员能修改补录订单
        if (loginUser.getUserCategory() != 3) {
            return "该用户没有修改补录订单的权限！";
        }
        return busiOrderService.updateReCreateOrder(loginUser, dto);
    }

    /**
     * deleteOrder
     *
     * @param user
     * @param id
     * @return
     */
    public void deleteOrder(User user, Long id) {
        String msg = busiOrderService.deleteOrder(user, id);
        if (StringUtils.isNotBlank(msg)) {
            throw new ServiceException(MessageCode.COMMON_FAILURE, msg);
        }
    }

    /**
     * viewOrder
     *
     * @param orderId
     * @return
     */
    public BusiOrder viewOrder(Long loginUserId, Long orderId) {
        LOG.debug("PlatformOrderService.findOneOrder");
        User loginUser = userService.findById(loginUserId);
        String msg = "";
        if (orderId == null) {
            throw new ServiceException(MessageCode.COMMON_PARAMETER_ERROR, "订单id不能为空!");
        }
        if (belongToLoginUser(orderId, loginUser)) {
            BusiOrder order = busiOrderService.findOne(orderId);
            if (null == order) {
                throw new ServiceException(MessageCode.COMMON_NO_DATA);
            }
            return order;
        } else {
            return null;
        }
    }

    /**
     * orderAudit
     *
     * @param user
     * @param dto
     * @return
     */
    public void orderAudit(User user, OrderAuditDto dto) {
        LOG.debug("PlatformOrderService.orderAudit");
        if (dto.getOrderId() == null) {
            throw new ServiceException(MessageCode.COMMON_PARAMETER_ERROR, "订单id不能为空!");
        }
        if (belongToLoginUser(dto.getOrderId(), user)) {
            MessageCode result = busiOrderService.orderAudit(user, dto.getOrderId(), dto.getStatus(), dto.getRefuseComments());
            if (result != MessageCode.COMMON_SUCCESS) {
                throw new ServiceException(MessageCode.COMMON_FAILURE);
            }
        } else {
            throw new ServiceException(MessageCode.COMMON_NO_AUTHORIZED);
        }
        //进行消息推送和数据入库
        sendPush(dto.getOrderId(), dto.getStatus());
    }

    /**
     * auditSendMsg
     *
     * @param user
     * @param dto
     * @return
     */
    public MessageCode auditSendMsg(User user, AuditSendMsgDto dto) {
        LOG.debug("PlatformOrderService.auditSendMsg");
        if (StringUtils.isBlank(dto.getPhone())) {
            throw new ServiceException(MessageCode.COMMON_PARAMETER_ERROR, "phone number不能为空!");
        }
        if (StringUtils.isBlank(dto.getMsg())) {
            throw new ServiceException(MessageCode.COMMON_PARAMETER_ERROR, "sendMsg不能为空!");
        }
        return busiOrderService.auditSendMsg(user, dto.getPhone(), dto.getMsg());
    }

    /**
     * auditHistory
     *
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
     *
     * @param loginUser
     * @return
     */
    public PagModel getAvailableDrivers(User loginUser, Long orderId,
                                        Integer currentPage, Integer numPerPage) {
        return busiOrderService.getAvailableDrivers(loginUser, orderId, currentPage, numPerPage);
    }

    /**
     * showOrderSchedule
     *
     * @param vehicleId
     * @param orderId
     * @return
     */
    public List<OrderSchedule> showOrderSchedule(Long vehicleId, Long orderId) {
        return busiOrderService.showOrderSchedule(vehicleId, orderId);
    }

    /**
     * orderAllocate
     *
     * @param user
     * @param dto
     * @return
     */
    public MessageCode orderAllocate(User user, AllocateDto dto) {
        if (dto.getOrderId() == null) {
            throw new ServiceException(MessageCode.COMMON_PARAMETER_ERROR, "订单id不能为空!");
        }
        if (dto.getVehicleId() == null) {
            throw new ServiceException(MessageCode.COMMON_PARAMETER_ERROR, "车辆id不能为空!");
        }
        if (dto.getDriverId() == null) {
            throw new ServiceException(MessageCode.COMMON_PARAMETER_ERROR, "司机id不能为空!");
        }
        BusiOrder order = busiOrderService.findOne(dto.getOrderId());
        if (order.getStatus() != 1) {
            throw new ServiceException(MessageCode.COMMON_PARAMETER_ERROR, "排车订单id无效!");
        }
        MessageCode resultCode = busiOrderService.orderAllocate(user, dto.getOrderId(), dto.getVehicleId(), dto.getDriverId());
        if (resultCode == MessageCode.COMMON_SUCCESS) {
            // 开始消息推送和入库
            // 用车人进行提醒
            sendPush(dto.getOrderId(), 91);
            // 司机进行提醒
            sendPush(dto.getOrderId(), 95);
        }
        return resultCode;
    }

    public MessageCode orderVehicleOut(User user, Long orderId) {
        MessageCode resultCode = busiOrderService.orderVehicleOut(user, orderId);
        if (resultCode == MessageCode.COMMON_SUCCESS) {
            sendPush(orderId, 92);
        }

        return resultCode;
    }

    public MessageCode orderReachFromPlace(User user, Long orderId) {
        MessageCode resultCode = busiOrderService.orderReachFromPlace(user, orderId);
        if (resultCode == MessageCode.COMMON_SUCCESS) {
            sendPush(orderId, 93);
        }

        return resultCode;
    }

    public MessageCode orderOngoing(User user, Long orderId) throws Exception {
        return busiOrderService.orderOngoing(user, orderId);
    }

    public MessageCode orderWaiting(User user, Long orderId) {
        return busiOrderService.orderWaiting(user, orderId);
    }

    public MessageCode orderFinish(User user, Long orderId) throws Exception {
        MessageCode resultCode = busiOrderService.orderFinish(user, orderId);
        if (resultCode == MessageCode.COMMON_SUCCESS) {
            sendPush(orderId, 94);
        }

        return resultCode;
    }

    /**
     * cancelOrder
     *
     * @param user
     * @param id
     * @return
     */
    public MessageCode orderCancel(User user, Long id) {
        String msg = busiOrderService.cancelOrder(user, id, null);
        if (StringUtils.isNotBlank(msg)) {
            return MessageCode.COMMON_FAILURE;
        } else {
            return MessageCode.COMMON_SUCCESS;
        }
    }

    /**
     * queryVehicleSchedule
     *
     * @param vehicleId
     * @param planStTime
     * @return
     */
    public List<VehicleSchedule> queryVehicleSchedule(Long vehicleId, String planStTime) {
        return busiOrderService.queryVehicleSchedule(vehicleId, planStTime);
    }

    public boolean belongToLoginUser(Long orderId, User loginUser) {
        boolean boo = false;
        Long orgId = loginUser.getOrganizationId();
        BusiOrder order = busiOrderService.findOne(orderId);
        if (order.getOrderUserid().equals(loginUser.getId())) {
            boo = true;
        } else {
            if (loginUser.isEntAdmin() || loginUser.isDeptAdmin()) {
                Long deptId = order.getOrganizationId();//订单所在部门ID
                List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, true, true);
                List<Long> orgIds = new ArrayList<>();
                if (!orgList.isEmpty()) {
                    for (Organization org : orgList) {
                        orgIds.add(org.getId());
                    }
                }
                if (deptId.equals(orgId) || orgIds.contains(deptId)) {
                    boo = true;
                }
            } else if (loginUser.isEndUser()) {
                if (order.getOrderUserid().equals(loginUser.getId())) {
                    boo = true;
                }
            } else if (loginUser.isDriver()) {
                if (order.getDriverId().equals(loginUser.getId())) {
                    boo = true;
                }
            }
        }
        return boo;
    }

    /**
     * 处理完状态需要给用车人发送信息状态
     *
     * @param orderId
     * @param status
     */
    protected void sendPush(Long orderId, int status) {
        LOG.debug("sendPush(orderId:" + orderId + ",status:" + status + ")");

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
        switch (status) {
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
     *
     * @param recievers
     * @param title
     * @param msgContent
     * @param app
     */
    protected void sendPush(List<String> recievers, String title, String msgContent, String app) {
        LOG.debug("sendPush(title:" + title + ",msgContent:" + msgContent + ",app:" + app + ")");
        communicationService.sendPush(recievers, title, msgContent, app);
    }

    public PagModel getAvailableVehicles(User user, Long orderId, Integer currentPage, Integer numPerPage) {
        PagModel pagModel = vehicleService.listAvailableVehicleByAdmin(user, orderId, currentPage, numPerPage);
        if (pagModel == null) {
            throw new ServiceException(MessageCode.COMMON_NO_DATA);
        } else {
            return pagModel;
        }
    }

    /**
     * 查询用户所能使用的车辆类型
     *
     * @param user
     * @return
     */
    public List<VehicleType> getUserVehicleTypes(User user) {
        List<VehicleType> ruleList = ruleService.findRuleVehicleTypeByUserId(user.getId());
        if (ruleList == null || ruleList.size() == 0) {
            return Arrays.asList(VehicleType.values());
        } else {
            return ruleList;
        }
    }

    public PagModel queryBatchVehicleSchedule(User user, Long orderId, Integer currentPage, Integer numPerPage) {

        BusiOrder order = busiOrderService.findOrderByIdSimple(orderId);
        if (order == null) {
            throw new ServiceException(MessageCode.COMMON_NO_DATA, "订单不存在");
        }
        if (user == null) {
            throw new ServiceException(MessageCode.COMMON_NO_DATA, "用户不存在");
        }

        PagModel pageModel = getAvailableVehicles(user, orderId, currentPage, numPerPage);

        List<VehicleScheduleDto> dtoList = new ArrayList<>();

        List<VehicleModel> availableVehicles = pageModel.getResultList();
        for (VehicleModel vehicle : availableVehicles) {
            List<VehicleSchedule> vehicleSchedules = queryVehicleSchedule(vehicle.getId(),
                    DATE_FORMAT.format(order.getPlanStTime()));

            VehicleScheduleDto dto = new VehicleScheduleDto();
            dto.setSchedules(vehicleSchedules);
            dto.setVehicle(vehicle);
            dtoList.add(dto);
        }

        pageModel.setResultList(dtoList);
        return pageModel;
    }
}
