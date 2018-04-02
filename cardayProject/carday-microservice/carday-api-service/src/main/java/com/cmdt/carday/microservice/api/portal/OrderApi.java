package com.cmdt.carday.microservice.api.portal;

import com.cmdt.carday.microservice.api.BaseApi;
import com.cmdt.carday.microservice.biz.service.PlatformOrderService;
import com.cmdt.carday.microservice.model.request.order.AllocateDto;
import com.cmdt.carday.microservice.model.request.order.AuditSendMsgDto;
import com.cmdt.carday.microservice.model.request.order.OrderAuditDto;
import com.cmdt.carday.microservice.model.request.order.PageDto;
import com.cmdt.carday.microservice.common.model.Order.OrderDto;
import com.cmdt.carday.microservice.common.model.Order.enums.VehicleType;
import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.BusiOrderAuditRecord;
import com.cmdt.carday.microservice.common.model.response.MessageCode;
import com.cmdt.carrental.common.model.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.cmdt.carday.microservice.common.Constants.Project_Version;

@Api("/order")
@SwaggerDefinition(info = @Info(contact = @Contact(name = "Zhou YanGang",
        email = "yangang.zhou@cm-dt.com"),
        title = "The order api for order-management",
        version = Project_Version))
@Validated
@RestController
@RequestMapping("/order")
public class OrderApi extends BaseApi {
    @Autowired
    private PlatformOrderService orderService;


    @ApiOperation(value = "APP获取订单列表", response = PagModel.class)
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PagModel orderListForApp(@ApiParam(value = "订单查询参数", required = true)
                                    @RequestBody @Valid @NotNull BusiOrderQueryDto dto) {
        return orderService.orderListForApp(getUserById(dto.getLoginUserId()), dto);
    }

    @ApiOperation(value = "Portal获取订单列表", response = PagModel.class)
    @PostMapping(value = "/admin/list", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PagModel orderListForPortal(@ApiParam(value = "订单查询参数", required = true)
                                       @RequestBody @Valid @NotNull BusiOrderQueryDto dto) {
        return orderService.orderListForPortal(getUserById(), dto);
    }

    @ApiOperation(value = "获取订单审核列表", response = PagModel.class)
    @PostMapping(value = "/auditOrder/list", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PagModel auditList(@ApiParam(value = "订单查询参数", required = true)
                              @RequestBody @Valid @NotNull BusiOrderQueryDto dto) {
        return orderService.auditList(getUserById(dto.getLoginUserId()), dto);
    }


    @ApiOperation(value = "获取订单排车列表", response = PagModel.class)
    @PostMapping(value = "/allocate/list", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PagModel allocateList(@ApiParam(value = "订单查询参数", required = true)
                                 @RequestBody @Valid @NotNull BusiOrderQueryDto dto) {
        return orderService.allocateList(getUserById(dto.getLoginUserId()), dto);
    }


    @ApiOperation(value = "司机查看订单(当前任务)", response = PagModel.class)
    @PostMapping(value = "/driver/currentList", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PagModel orderListCurrentForDriver(@ApiParam(value = "登录用户ID") @RequestParam("loginUserId") Long loginUserId,
                                              @ApiParam(value = "当前页码, 从1开始") @RequestParam("currentPage") Integer currentPage,
                                              @ApiParam(value = "每页条数") @RequestParam("numPerPage") Integer numPerPage) {
        return orderService.orderListCurrentForDriver(getUserById(loginUserId), currentPage, numPerPage);
    }

    @ApiOperation(value = "司机查看订单(历史任务)", response = PagModel.class)
    @PostMapping(value = "/driver/historyList", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PagModel orderListHistoryForDriver(@ApiParam(value = "登录用户ID") @RequestParam("loginUserId") Long loginUserId,
                                              @ApiParam(value = "当前页码, 从1开始") @RequestParam("currentPage") Integer currentPage,
                                              @ApiParam(value = "每页条数") @RequestParam("numPerPage") Integer numPerPage) {
        return orderService.orderListHistoryForDriver(getUserById(loginUserId), currentPage, numPerPage);
    }


    @ApiOperation(value = "获取补录订单的用车人", response = EmployeeModel.class, responseContainer = "List")
    @PostMapping(value = "/recreateOrderEmpList", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EmployeeModel> recreateOrderEmpList(@ApiParam(value = "部门ID", required = true)
                                                    @Valid @NotNull @RequestParam("orgId") Long orgId) {
        return orderService.recreateOrderEmpList(orgId);
    }


    @ApiOperation(value = "获取补录订单的司机", response = DriverModel.class, responseContainer = "List")
    @PostMapping(value = "/recreateOrderDriverList", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DriverModel> recreateOrderDriverList(@ApiParam(value = "部门ID", required = true)
                                                     @Valid @NotNull @RequestParam("orgId") Long orgId) {
        return orderService.recreateOrderDriverList(orgId);
    }


    @ApiOperation(value = "获取补录订单的可用车辆", response = VehicleModel.class, responseContainer = "List")
    @PostMapping(value = "/recreateOrderVehicleList", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VehicleModel> recreateOrderVehicleList(@ApiParam(value = "部门ID", required = true)
                                                       @Valid @NotNull @RequestParam("orgId") Long orgId) {
        return orderService.recreateOrderVehicleList(orgId);
    }


    @ApiOperation(value = "新建订单", response = OrderDto.class)
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto create(@ApiParam(value = "订单信息", required = true)
                          @RequestBody @Valid @NotNull OrderDto dto) throws Exception {
        return orderService.createOrder(getUserById(dto.getLoginUserId()), dto);
    }


    @ApiOperation(value = "修改订单", response = Boolean.class)
    @PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean update(@ApiParam(value = "订单更新信息", required = true)
                          @RequestBody @Valid @NotNull OrderDto dto) {
        orderService.updateOrder(getUserById(dto.getLoginUserId()), dto);
        return true;
    }


    @ApiOperation(value = "补录订单", response = OrderDto.class)
    @PostMapping(value = "/recreate", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public OrderDto recreate(@ApiParam(value = "补录订单信息", required = true)
                            @RequestBody @Valid @NotNull OrderDto dto) {
        return orderService.recreateOrder(getUserById(dto.getLoginUserId()), dto);
    }


    @ApiOperation(value = "补录订单修改", response = Boolean.class)
    @PostMapping(value = "/recreateUpdate", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean recreateUpdate(@ApiParam(value = "订单修改信息", required = true)
                                  @RequestBody @Valid @NotNull OrderDto dto) {
        MessageCode result = orderService.updateReCreateOrder(getUserById(dto.getLoginUserId()), dto);
        if (result == MessageCode.COMMON_SUCCESS) {
            return true;
        } else {
            return false;
        }
    }


    @ApiOperation(value = "删除订单")
    @DeleteMapping(value = "{id}/delete")
    public void delete(@ApiParam(value = "用户ID", required = true) @RequestParam("loginUserId") Long userId,
                       @ApiParam(value = "用户ID", required = true) @PathVariable("id") Long orderId) {
        orderService.deleteOrder(getUserById(userId), orderId);
    }


    @ApiOperation(value = "根据订单ID查看订单详情", response = BusiOrder.class)
    @GetMapping(value = "/{orderId}/view", produces = MediaType.APPLICATION_JSON_VALUE)
    public BusiOrder viewOrder(@ApiParam(value = "用户ID", required = true) @RequestParam("userId") Long userId,
                               @ApiParam(value = "订单ID", required = true) @PathVariable("orderId") Long orderId) {
        return orderService.viewOrder(userId, orderId);
    }


    @ApiOperation(value = "审核订单", response = Boolean.class)
    @PostMapping(value = "/audit", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean audit(@ApiParam(value = "订单审核信息", required = true)
                         @RequestBody @Valid @NotNull OrderAuditDto dto) {
        orderService.orderAudit(getUserById(dto.getLoginUserId()), dto);
        return true;
    }


    @ApiOperation(value = "发送订单审核消息", response = Boolean.class)
    @PostMapping(value = "/audit/sendMsg", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean auditSendMsg(@ApiParam(value = "订单审核消息", required = true)
                                @Valid @NotNull @RequestBody AuditSendMsgDto dto) {
        MessageCode result = orderService.auditSendMsg(getUserById(dto.getLoginUserId()), dto);

        if (result == MessageCode.COMMON_SUCCESS) {
            return true;
        } else {
            return false;
        }
    }


    @ApiOperation(value = "获取订单审核历史信息", response = BusiOrderAuditRecord.class, responseContainer = "List")
    @GetMapping(value = "/auditHistory/{userId}/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BusiOrderAuditRecord> auditHistory(@ApiParam(value = "登录用户ID", required = true)
                                                   @PathVariable("userId") Long userId,
                                                   @ApiParam(value = "订单ID", required = true)
                                                   @PathVariable("orderId") Long orderId) {
        return orderService.auditHistory(getUserById(userId), orderId);
    }


    @ApiOperation(value = "获取可用的司机列表", response = PagModel.class)
    @PostMapping(value = "/{orderId}/availableDrivers",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PagModel getAvailableDrivers(@ApiParam(value = "订单ID", required = true) @PathVariable("orderId") @Valid @NotNull Long orderId,
                                        @ApiParam(value = "分页参数", required = true) @RequestBody PageDto pageDto) {
        return orderService.getAvailableDrivers(getUserById(pageDto.getLoginUserId()), orderId, pageDto.getCurrentPage(), pageDto.getNumPerPage());
    }

    @ApiOperation(value = "获取可用的车辆列表", response = PagModel.class)
    @PostMapping(value = "/{orderId}/availableVehicles",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PagModel getAvailableVehicles(@ApiParam(value = "订单ID", required = true) @PathVariable("orderId") @Valid @NotNull Long orderId,
                                         @ApiParam(value = "分页参数", required = true) @RequestBody PageDto pageDto) {
        return orderService.getAvailableVehicles(getUserById(pageDto.getLoginUserId()), orderId, pageDto.getCurrentPage(), pageDto.getNumPerPage());
    }




    @ApiOperation(value = "查看订单排车时间表", response = OrderSchedule.class, responseContainer = "List")
    @GetMapping(value = "/schedule/{vehicleId}/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OrderSchedule> showOrderSchedule(@ApiParam(value = "车辆ID") @PathVariable("vehicleId") Long vehicleId,
                                                 @ApiParam(value = "订单ID") @PathVariable("orderId") Long orderId) {
        return orderService.showOrderSchedule(vehicleId, orderId);
    }


    @ApiOperation(value = "订单排车", response = Boolean.class)
    @PostMapping(value = "/allocate", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean allocate(@ApiParam(value = "订单排车信息")
                            @RequestBody @Valid @NotNull AllocateDto dto) {
        MessageCode result = orderService.orderAllocate(getUserById(dto.getLoginUserId()), dto);
        if (result == MessageCode.COMMON_SUCCESS) {
            return true;
        } else {
            return false;
        }
    }


    @ApiOperation(value = "司机已出车", response = Boolean.class)
    @PostMapping(value = "/{orderId}/vehicleOut", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean orderVehicleOut(@ApiParam("订单ID") @PathVariable("orderId") @Valid @NotNull Long orderId,
                                   @ApiParam("登录用户ID") @RequestParam("loginUserId") Long userId) {
        MessageCode result = orderService.orderVehicleOut(getUserById(userId), orderId);
        if (result == MessageCode.COMMON_SUCCESS) {
            return true;
        } else {
            return false;
        }
    }


    @ApiOperation(value = "已到达出发地", response = Boolean.class)
    @PostMapping(value = "/{orderId}/reachFromPlace", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean orderReachFromPlace(@ApiParam("订单ID") @PathVariable("orderId") @Valid @NotNull Long orderId,
                                       @ApiParam("登录用户ID") @RequestParam("loginUserId") Long userId) {
        MessageCode result = orderService.orderReachFromPlace(getUserById(userId), orderId);
        if (result == MessageCode.COMMON_SUCCESS) {
            return true;
        } else {
            return false;
        }
    }


    @ApiOperation(value = "乘客在途", response = Boolean.class)
    @PostMapping(value = "/{orderId}/ongoing", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean orderOngoing(@ApiParam("订单ID") @PathVariable("orderId") @Valid @NotNull Long orderId,
                                @ApiParam("登录用户ID") @RequestParam("loginUserId") Long userId) throws Exception {
        MessageCode result = orderService.orderOngoing(getUserById(userId), orderId);
        if (result == MessageCode.COMMON_SUCCESS) {
            return true;
        } else {
            return false;
        }
    }


    @ApiOperation(value = "返途等待", response = Boolean.class)
    @PostMapping(value = "/{orderId}/waiting", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean orderWaiting(@ApiParam("订单ID") @PathVariable("orderId") @Valid @NotNull Long orderId,
                                @ApiParam("登录用户ID") @RequestParam("loginUserId") Long userId) {
        MessageCode result = orderService.orderWaiting(getUserById(userId), orderId);
        if (result == MessageCode.COMMON_SUCCESS) {
            return true;
        } else {
            return false;
        }
    }


    @ApiOperation(value = "完成订单", response = Boolean.class)
    @PostMapping(value = "/{orderId}/finish", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean orderFinish(@ApiParam("订单ID") @PathVariable("orderId") @Valid @NotNull Long orderId,
                               @ApiParam("登录用户ID") @RequestParam("loginUserId") Long userId) throws Exception {
        MessageCode result = orderService.orderFinish(getUserById(userId), orderId);
        if (result == MessageCode.COMMON_SUCCESS) {
            return true;
        } else {
            return false;
        }
    }


    @ApiOperation(value = "取消订单", response = Boolean.class)
    @PostMapping(value = "/{orderId}/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean orderCancel(@ApiParam("订单ID") @PathVariable("orderId") @Valid @NotNull Long orderId,
                               @ApiParam("登录用户ID") @RequestParam("loginUserId") Long userId) {
        MessageCode result = orderService.orderCancel(getUserById(userId), orderId);
        if (result == MessageCode.COMMON_SUCCESS) {
            return true;
        } else {
            return false;
        }
    }


    @ApiOperation(value = "查看订单排车时间表", response = VehicleSchedule.class, responseContainer = "List")
    @GetMapping(value = "/vehicleSchedule/{vehicleId}/{planStTime}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VehicleSchedule> queryVehicleSchedule(@PathVariable("vehicleId") Long vehicleId, @PathVariable("planStTime") String planStTimeF) {
        return orderService.queryVehicleSchedule(vehicleId, planStTimeF);
    }

    @ApiOperation(value = "批量查看订单排车时间表", response = VehicleSchedule.class, responseContainer = "List")
    @PostMapping(value = "/{orderId}/vehicleSchedule/batch", produces = MediaType.APPLICATION_JSON_VALUE)
    public PagModel queryBatchVehicleSchedule(@PathVariable("orderId") Long orderId,
                                              @RequestHeader("Authorization") @NotNull(message = "登录用户ID不能为空") Long loginUserId,
                                              @RequestBody PageDto pageDto) {
        return orderService.queryBatchVehicleSchedule(getUserById(loginUserId), orderId, pageDto.getCurrentPage(), pageDto.getNumPerPage());
    }

    @ApiOperation(value = "获取用户可以使用的车辆类型列表", response = VehicleType.class, responseContainer = "List")
    @GetMapping(value = "/vehicle-type", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VehicleType> getUserVehicleTypes(@RequestHeader("Authorization") @NotNull(message = "登录用户ID不能为空") Long loginUserId) {
        return orderService.getUserVehicleTypes(getUserById(loginUserId));
    }
}
