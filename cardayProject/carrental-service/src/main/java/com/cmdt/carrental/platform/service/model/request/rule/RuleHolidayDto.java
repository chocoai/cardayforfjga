package com.cmdt.carrental.platform.service.model.request.rule;

/**
 * Created by zhengjun.jing on 6/6/2017.
 */
public class RuleHolidayDto {
    private String holidayType;
    private String startTime;
    private String endTime;

    public String getHolidayType() {
        return holidayType;
    }

    public void setHolidayType(String holidayType) {
        this.holidayType = holidayType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
