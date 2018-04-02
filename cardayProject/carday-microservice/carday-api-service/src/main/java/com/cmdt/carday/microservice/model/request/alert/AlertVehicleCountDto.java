package com.cmdt.carday.microservice.model.request.alert;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carday.microservice.common.Patterns;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@JsonInclude(Include.NON_NULL)
public class AlertVehicleCountDto implements Serializable{

	private static final long serialVersionUID = -4099053487053208194L;
	
	@ApiModelProperty(value="车牌号",required=true)
	@NotNull(message="vehicleNumber 不能为空")
	private String vehicleNumber;
	
	@ApiModelProperty(value="开始时间， 格式为：yyyy-MM-dd HH:mm:ss",required=true)
	@NotNull(message="startTime 不能为空")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private String startTime;
	
	@ApiModelProperty(value="结束时间， 格式为：yyyy-MM-dd HH:mm:ss",required=true)
	@NotNull(message="endTime不能为空")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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
	
}
