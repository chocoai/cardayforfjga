package com.cmdt.carday.microservice.model.request.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: joe
 * @Date: 17-7-17 下午1:09.
 * @Description:
 */
@ApiModel(value = "AuditSendMsg", description = "订单审核发送信息")
public class AuditSendMsgDto {

    @ApiModelProperty("登录用户ID")
    private Long loginUserId;

    @ApiModelProperty("接收手机号")
    private String phone;

    @ApiModelProperty("信息")
    private String msg;

    public Long getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(Long loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
