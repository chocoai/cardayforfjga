package com.cmdt.carrental.mobile.gateway.model.request.vehicle;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.cmdt.carrental.common.util.Patterns;

public class VehicleImeiTraceDto {
	
	@NotNull(message="imei不能为空")
	private String imei;
	@NotEmpty(message="startTime不能为空")
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS,message="startTime日期格式错误，应为yyyy-mm-dd hh:mm:ss!")
	private String startTime;
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS,message="endTime日期格式错误，应为yyyy-mm-dd hh:mm:ss!")
	@NotEmpty(message="endTime不能为空")
	private String endTime;

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
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	
}
