package com.cmdt.carrental.rt.data.database.dto;

import javax.validation.constraints.Pattern;

import com.cmdt.carrental.rt.data.service.common.Patterns;

public class TrackDataDTO {
	
	 @Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS, message = "planTime format error!")
	 private String traceTime;
	 private Double longitude;
	 private Double latitude;
	 private Integer speed;
	 private String status;
	 
	public String getTraceTime() {
		return traceTime;
	}
	public void setTraceTime(String traceTime) {
		this.traceTime = traceTime;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Integer getSpeed() {
		return speed;
	}
	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
