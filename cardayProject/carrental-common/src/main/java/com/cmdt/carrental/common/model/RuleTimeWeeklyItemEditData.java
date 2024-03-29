package com.cmdt.carrental.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleTimeWeeklyItemEditData {

	@JsonProperty("weeklyId")
	private Long weeklyId;
	
	@JsonProperty("weeklyType")
	private String weeklyType;
	
	@JsonProperty("startTime")
	private String startTime;
	
	@JsonProperty("endTime")
	private String endTime;

	public Long getWeeklyId() {
		return weeklyId;
	}

	public void setWeeklyId(Long weeklyId) {
		this.weeklyId = weeklyId;
	}

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
