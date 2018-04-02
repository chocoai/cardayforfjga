package com.cmdt.carrental.platform.service.model.request.vehicle;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carrental.platform.service.common.Patterns;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class VehicleTripTraceDto implements Serializable{
	
	private static final long serialVersionUID = 6794959673198783952L;

	@NotNull(message="imei不能为空")
	private String imei;
	
	@NotNull(message="starttime不能为空")
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS,message="starttime日期格式错误，应为yyyy-mm-dd HH:mm:ss")
	private String starttime;
	
	@NotNull(message="endtime不能为空")
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS,message="endtime日期格式错误，应为yyyy-mm-dd HH:mm:ss")
	private String endtime;
	
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
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
