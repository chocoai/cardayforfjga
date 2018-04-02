package com.cmdt.carday.microservice.common.model.Order.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Author: joe
 * @Date: 17-7-18 下午1:29.
 * @Description: 订单往返类型 <br/>
 * 0(往返双程), 1(单程)
 */
public enum OrderReturnType {
    Round_Trip(0),
    One_Way(1);

    private Integer type;

    OrderReturnType(Integer type) {
        this.type = type;
    }

    @JsonValue
    public Integer getType() {
        return type;
    }

    @JsonCreator
    public static OrderReturnType create(Integer type) {
        for (OrderReturnType value : OrderReturnType.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }

        return null;
    }
}
