package com.cmdt.carrental.platform.service.model.request.vehicle;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carrental.platform.service.common.Patterns;

public class VehicleHistoryTrackeDto implements Serializable{
	
	private static final long serialVersionUID = -1217643942198017961L;

	@NotNull(message="vehicleNumber不能为空")
	private String vehicleNumber;
	
	@NotNull(message="starttime不能为空")
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS,message="starttime日期格式错误，应为yyyy-mm-dd HH:mm:ss")
	private String starttime;
	
	@NotNull(message="endtime不能为空")
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS,message="endtime日期格式错误，应为yyyy-mm-dd HH:mm:ss")
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
