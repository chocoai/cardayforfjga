package com.cmdt.carday.microservice.common.model.Order.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Author: joe
 * @Date: 17-7-18 下午1:29.
 * @Description: 订单类型 <br/>
 *  0:企业订单 / 1:网约订单
 */
public enum OrderType {
    Enterprise(0),
    Online(1);

    private Integer type;

    OrderType(Integer type) {
        this.type = type;
    }

    @JsonValue
    public Integer getType() {
        return type;
    }

    @JsonCreator
    public static OrderType create(Integer type) {
        for (OrderType value : OrderType.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }

        return null;
    }
}
