package com.cmdt.carrental.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleTimeData {

	@JsonProperty("timeRangeType")
	private String timeRangeType;
	
	@JsonProperty("holidayData")
	private List<RuleTimeHolidayItemData> holidayData;
	
	@JsonProperty("weeklyData")
	private List<RuleTimeWeeklyItemData> weeklyData;
	
	@JsonProperty("dateData")
	private List<RuleTimeDateItemData> dateData;

	public String getTimeRangeType() {
		return timeRangeType;
	}

	public void setTimeRangeType(String timeRangeType) {
		this.timeRangeType = timeRangeType;
	}

	public List<RuleTimeHolidayItemData> getHolidayData() {
		return holidayData;
	}

	public void setHolidayData(List<RuleTimeHolidayItemData> holidayData) {
		this.holidayData = holidayData;
	}

	public List<RuleTimeWeeklyItemData> getWeeklyData() {
		return weeklyData;
	}

	public void setWeeklyData(List<RuleTimeWeeklyItemData> weeklyData) {
		this.weeklyData = weeklyData;
	}

	public List<RuleTimeDateItemData> getDateData() {
		return dateData;
	}

	public void setDateData(List<RuleTimeDateItemData> dateData) {
		this.dateData = dateData;
	}
	
}
