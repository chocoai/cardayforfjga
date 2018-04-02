package com.cmdt.carrental.platform.service.model.request.rule;

/**
 * Created by zhengjun.jing on 6/6/2017.
 */
public class RuleWeeklyDto {
    private String weeklyType;
    private String startTime;
    private String endTime;

    public String getWeeklyType() {
        return weeklyType;
    }

    public void setWeeklyType(String weeklyType) {
        this.weeklyType = weeklyType;
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
