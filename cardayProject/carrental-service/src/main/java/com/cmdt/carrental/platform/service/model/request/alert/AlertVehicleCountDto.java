package com.cmdt.carrental.platform.service.model.request.alert;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carrental.platform.service.common.Patterns;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AlertVehicleCountDto implements Serializable{

	private static final long serialVersionUID = -4099053487053208194L;

	private Long userId;
	
	@NotNull(message="vehicleNumber 不能为空")
	private String vehicleNumber;
	
	@NotNull(message="startTime 不能为空")
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS,message="startTime格式错误，应为yyyy-MM-dd HH:mm:ss")
	private String startTime;
	
	@NotNull(message="endTime不能为空")
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS,message="endTime格式错误，应为yyyy-MM-dd HH:mm:ss")
	private String endTime;
	
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
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
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
