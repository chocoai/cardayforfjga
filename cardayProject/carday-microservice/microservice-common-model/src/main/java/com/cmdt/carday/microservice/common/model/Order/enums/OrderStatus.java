package com.cmdt.carday.microservice.common.model.Order.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Author: joe
 * @Date: 17-7-18 下午1:29.
 * @Description: 订单状态 <br/>
 * 0:待审核,1:待排车,2:已排车,5:被驳回,6:已取消,11:已出车,12:已到达出发地,3:进行中/行程中,13:等待中,4:待支付,15:待评价,16:已完成
 */
public enum OrderStatus {
    Wait_Audit(0),
    Wait_Allocate(1),
    Allocated(2),
    Refused(5),
    Canceled(6),
    Vehicle_Out(11),
    Arrived_Departure(12),
    Processing(3),
    Round_Trip_Waiting(13),
    Wait_Payment(4),
    Wait_Comment(15),
    Finished(16);

    private Integer status;

    OrderStatus(Integer status) {
        this.status = status;
    }

    @JsonValue
    public Integer getStatus() {
        return status;
    }

    @JsonCreator
    public static OrderStatus create(Integer type) {
        for (OrderStatus value : OrderStatus.values()) {
            if (value.status.equals(type)) {
                return value;
            }
        }

        return null;
    }
}
