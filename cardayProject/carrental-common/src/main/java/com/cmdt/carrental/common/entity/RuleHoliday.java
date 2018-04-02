package com.cmdt.carrental.common.entity;

import java.io.Serializable;


public class RuleHoliday implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String holidayYear;
	private String holidayType;
	private String holidayTime;
	private String adjustHolidayTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getHolidayYear() {
		return holidayYear;
	}
	public void setHolidayYear(String holidayYear) {
		this.holidayYear = holidayYear;
	}
	public String getHolidayType() {
		return holidayType;
	}
	public void setHolidayType(String holidayType) {
		this.holidayType = holidayType;
	}
	public String getHolidayTime() {
		return holidayTime;
	}
	public void setHolidayTime(String holidayTime) {
		this.holidayTime = holidayTime;
	}
	public String getAdjustHolidayTime() {
		return adjustHolidayTime;
	}
	public void setAdjustHolidayTime(String adjustHolidayTime) {
		this.adjustHolidayTime = adjustHolidayTime;
	}
	
	
	
}
