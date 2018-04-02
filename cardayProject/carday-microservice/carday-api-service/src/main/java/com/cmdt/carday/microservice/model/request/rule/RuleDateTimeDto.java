package com.cmdt.carday.microservice.model.request.rule;

/**
 * Created by zhengjun.jing on 6/6/2017.
 */
public class RuleDateTimeDto {
    private String startDay;
    private String endDay;
    private String startTime;
    private String endTime;

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
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
