package com.cmdt.carday.microservice.common.model.Order.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @Author: joe
 * @Date: 17-7-18 下午1:29.
 * @Description: 车辆类型 <br/>
 * 0(经济), 1(舒适), 2(商务), 3(豪华)
 */
public enum VehicleType {
    Economy(0),
    Comfort(1),
    Business(2),
    Luxury(3);

    private Integer type;

    VehicleType(Integer type) {
        this.type = type;
    }

    @JsonValue
    public Integer getType() {
        return type;
    }

    @JsonCreator
    public static VehicleType createVehicleType(Integer type) {
        for (VehicleType value : VehicleType.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }

        return null;
    }
}
