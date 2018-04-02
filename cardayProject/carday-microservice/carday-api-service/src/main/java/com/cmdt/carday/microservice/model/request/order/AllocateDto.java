package com.cmdt.carday.microservice.model.request.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: joe
 * @Date: 17-7-17 下午2:09.
 * @Description: 订单排车信息
 */
@ApiModel(value = "Allocate", description = "订单排车信息")
public class AllocateDto {

    @ApiModelProperty("登录用户ID")
    private Long loginUserId;

    @ApiModelProperty("订单ID")
    private Long orderId;

    @ApiModelProperty("车辆ID")
    private Long vehicleId;

    @ApiModelProperty("司机ID")
    private Long driverId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(Long loginUserId) {
        this.loginUserId = loginUserId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }
}
