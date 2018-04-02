package com.cmdt.carday.microservice.model.request.usageReport;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carrental.common.util.Patterns;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

public class UsageReportVehDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="开始时间",required=true)
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD,message="开始时间日期格式错误，应为yyyy-mm-dd")
	private String starttime;
	
	@ApiModelProperty(value="结束时间",required=true)
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD,message="结束时间日期格式错误，应为yyyy-mm-dd")
	private String endtime;
	
	@ApiModelProperty(value="车牌号",required=true)
	@NotNull(message="车牌号不能为空")
	private String vehicleNumber;

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

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	
	

}
