package com.cmdt.carrental.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleTimeHolidayItemEditData {

	@JsonProperty("holidayId")
	private Long holidayId;
	
	@JsonProperty("holidayType")
	private String holidayType;  //工作日节假日类型 0:法定工作日  1:法定节假日
	
	@JsonProperty("startTime")
	private String startTime;
	
	@JsonProperty("endTime")
	private String endTime;
	
	public Long getHolidayId() {
		return holidayId;
	}

	public void setHolidayId(Long holidayId) {
		this.holidayId = holidayId;
	}

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
