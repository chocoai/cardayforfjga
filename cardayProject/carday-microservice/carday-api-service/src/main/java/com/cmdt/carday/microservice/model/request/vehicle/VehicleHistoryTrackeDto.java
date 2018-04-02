package com.cmdt.carday.microservice.model.request.vehicle;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carday.microservice.common.Patterns;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

public class VehicleHistoryTrackeDto implements Serializable{
	
	private static final long serialVersionUID = -1217643942198017961L;

	@ApiModelProperty(value="车牌号",required=true)
	@NotNull(message="vehicleNumber不能为空")
	private String vehicleNumber;
	
	@ApiModelProperty(value="起始时间，日期格式错误，应为yyyy-mm-dd HH:mm:ss",required=true)
	@NotNull(message="starttime不能为空")
	@JsonFormat(pattern="yyyy-MM-dd HH:ss:mm")
	private String starttime;
	
	@ApiModelProperty(value="结束时间，日期格式错误，应为yyyy-mm-dd HH:mm:ss",required=true)
	@NotNull(message="endtime不能为空")
	@JsonFormat(pattern="yyyy-MM-dd HH:ss:mm")
	private String endtime;

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
}
