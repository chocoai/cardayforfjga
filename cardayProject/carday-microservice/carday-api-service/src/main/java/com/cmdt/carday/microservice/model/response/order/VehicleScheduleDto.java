package com.cmdt.carday.microservice.model.response.order;

import com.cmdt.carday.microservice.model.request.vehicle.VehicleModelDto;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleSchedule;

import java.util.List;

/**
 * @Author: joe
 * @Date: 17-7-24 下午3:55.
 * @Description: 每辆车在某一天的排车计划
 */
public class VehicleScheduleDto {

    private VehicleModel vehicle;

    private List<VehicleSchedule> schedules;

    public VehicleModel getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleModel vehicle) {
        this.vehicle = vehicle;
    }

    public List<VehicleSchedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<VehicleSchedule> schedules) {
        this.schedules = schedules;
    }
}
