package com.cmdt.carday.microservice.model.request.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: joe
 * @Date: 17-7-17 上午11:53.
 * @Description:
 * 订单审核DTO
 */
@ApiModel(value = "OrderAudit", description = "订单审核信息")
public class OrderAuditDto {

    /**
     * 订单ID
     */
    @ApiModelProperty("订单ID")
    private Long orderId;

    /**
     * 登录用户ID
     */
    @ApiModelProperty("登录用户ID")
    private Long loginUserId;

    /**
     * 订单审核后的状态
     */
    @ApiModelProperty("订单审核状态")
    private Integer status;

    /**
     * 拒绝原因
     */
    @ApiModelProperty("订单拒绝原因")
    private String refuseComments;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRefuseComments() {
        return refuseComments;
    }

    public void setRefuseComments(String refuseComments) {
        this.refuseComments = refuseComments;
    }
}
