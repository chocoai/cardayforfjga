package com.cmdt.carrental.platform.service.model.request.alert;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class StationInfoByVehicleNum implements Serializable{
	
	private static final long serialVersionUID = 2065900105526260214L;

	@NotNull(message="vehicleNumber不能为空")
	private String vehicleNumber;

	@Digits(message="longitude格式错误,小数位数不能超过9位", fraction = 9, integer = 3)
	@NotNull
	private Double longitude;
	
	@Digits(message="latitude格式错误,小数位数不能超过9位", fraction = 9, integer = 3)
	@NotNull
	private Double latitude;

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
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
	
	
}
