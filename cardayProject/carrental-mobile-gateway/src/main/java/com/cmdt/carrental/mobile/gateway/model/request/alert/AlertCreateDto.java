package com.cmdt.carrental.mobile.gateway.model.request.alert;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carrental.common.util.Patterns;
import com.cmdt.carrental.mobile.gateway.constraint.annotation.AlertType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AlertCreateDto {
	
	@NotNull(message="IMEI不能为空")
	private String imei;
	@NotNull(message="AlertType不能为空")
	@AlertType
	private String alertType;
	@NotNull(message="uploadCode不能为空")
	private String uploadCode;
	@Digits(message="speed格式错误,最大999", fraction = 0, integer = 3)
	@NotNull
	private Integer speed;
	@Digits(message="longitude格式错误,小数位数不能超过9位", fraction = 9, integer = 3)
	@NotNull
	private Double longitude;
	@Digits(message="latitude格式错误,小数位数不能超过9位", fraction = 9, integer = 3)
	@NotNull
	private Double latitude;
	@Digits(message="mileage格式错误", fraction = 0, integer = 10)
	private Double mileage;
	@NotNull(message="traceTime不能为空")
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS,message="To日期格式错误，应为yyyy-mm-dd hh:mm:ss!")
	private String traceTime;
	
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public String getUploadCode() {
		return uploadCode;
	}
	public void setUploadCode(String uploadCode) {
		this.uploadCode = uploadCode;
	}
	public Integer getSpeed() {
		return speed;
	}
	public void setSpeed(Integer speed) {
		this.speed = speed;
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
	public Double getMileage() {
		return mileage;
	}
	public void setMileage(Double mileage) {
		this.mileage = mileage;
	}
	public String getTraceTime() {
		return traceTime;
	}
	public void setTraceTime(String traceTime) {
		this.traceTime = traceTime;
	}
}
