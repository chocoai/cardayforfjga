package com.cmdt.carrental.common.model;

import java.util.Date;

public class DialCenterQueryDTO {
	private String dialPhone;
	private String dialName;
	private Date startTime;
	private Date endTime;
	private String recorder;

	public String getRecorder() {
		return recorder;
	}
	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}
	public String getDialPhone() {
		return dialPhone;
	}
	public void setDialPhone(String dialPhone) {
		this.dialPhone = dialPhone;
	}
	public String getDialName() {
		return dialName;
	}
	public void setDialName(String dialName) {
		this.dialName = dialName;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	

}
