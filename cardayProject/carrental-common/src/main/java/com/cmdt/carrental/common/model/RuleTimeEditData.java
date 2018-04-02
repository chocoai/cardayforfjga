package com.cmdt.carrental.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleTimeEditData {

	@JsonProperty("timeRangeType")
	private String timeRangeType;
	
	@JsonProperty("holidayData")
	private List<RuleTimeHolidayItemEditData> holidayData;
	
	@JsonProperty("weeklyData")
	private List<RuleTimeWeeklyItemEditData> weeklyData;
	
	@JsonProperty("dateData")
	private List<RuleTimeDateItemEditData> dateData;

	public String getTimeRangeType() {
		return timeRangeType;
	}

	public void setTimeRangeType(String timeRangeType) {
		this.timeRangeType = timeRangeType;
	}

	public List<RuleTimeHolidayItemEditData> getHolidayData() {
		return holidayData;
	}

	public void setHolidayData(List<RuleTimeHolidayItemEditData> holidayData) {
		this.holidayData = holidayData;
	}

	public List<RuleTimeWeeklyItemEditData> getWeeklyData() {
		return weeklyData;
	}

	public void setWeeklyData(List<RuleTimeWeeklyItemEditData> weeklyData) {
		this.weeklyData = weeklyData;
	}

	public List<RuleTimeDateItemEditData> getDateData() {
		return dateData;
	}

	public void setDateData(List<RuleTimeDateItemEditData> dateData) {
		this.dateData = dateData;
	}

	
	
}
