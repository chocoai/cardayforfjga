package com.cmdt.carday.microservice.model.request.rule;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by zhengjun.jing on 6/6/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RuleDto {
    private Long id;
    private String name;
    private String type;
    private String getOnType;
    private List<String> getOnData;
    private String getOffType;
    private List<String> getOffData;
    private String vehicleType;
    private String useLimit;
    private String timeRangeType;
    private List<RuleHolidayDto> holidayDto;
    private List<RuleWeeklyDto> weeklyDto;
    private List<RuleDateTimeDto> dateTimeDto;
    
    
    private Long userId;//登录用户id
    private Long ruleId;
    private Long uid;//用于查询或删除的用户id,用于区别于userId

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGetOnType() {
        return getOnType;
    }

    public void setGetOnType(String getOnType) {
        this.getOnType = getOnType;
    }

    public List<String> getGetOnData() {
        return getOnData;
    }

    public void setGetOnData(List<String> getOnData) {
        this.getOnData = getOnData;
    }

    public String getGetOffType() {
        return getOffType;
    }

    public void setGetOffType(String getOffType) {
        this.getOffType = getOffType;
    }

    public List<String> getGetOffData() {
        return getOffData;
    }

    public void setGetOffData(List<String> getOffData) {
        this.getOffData = getOffData;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getUseLimit() {
        return useLimit;
    }

    public void setUseLimit(String useLimit) {
        this.useLimit = useLimit;
    }

    public String getTimeRangeType() {
        return timeRangeType;
    }

    public void setTimeRangeType(String timeRangeType) {
        this.timeRangeType = timeRangeType;
    }

    public List<RuleHolidayDto> getHolidayDto() {
        return holidayDto;
    }

    public void setHolidayDto(List<RuleHolidayDto> holidayDto) {
        this.holidayDto = holidayDto;
    }

    public List<RuleWeeklyDto> getWeeklyDto() {
        return weeklyDto;
    }

    public void setWeeklyDto(List<RuleWeeklyDto> weeklyDto) {
        this.weeklyDto = weeklyDto;
    }

    public List<RuleDateTimeDto> getDateTimeDto() {
        return dateTimeDto;
    }

    public void setDateTimeDto(List<RuleDateTimeDto> dateTimeDto) {
        this.dateTimeDto = dateTimeDto;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}
    
}
