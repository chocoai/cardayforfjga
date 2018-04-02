package com.cmdt.carrental.rt.data.database.dto;

import java.util.Date;

import javax.validation.constraints.Pattern;

import com.cmdt.carrental.rt.data.service.common.Patterns;


public class DtcDataDTO {

    private String imei;
    
    /**
     * yyyy-MM-dd
     */
    @Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS, message = "planTime format error!")
    private String traceTime;
    
    private Date createTime;
    
    //多个DTC中间以分号分隔 eg:P0001;P0002...
    private String dtc;
    
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getTraceTime() {
		return traceTime;
	}
	public void setTraceTime(String traceTime) {
		this.traceTime = traceTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getDtc() {
		return dtc;
	}
	public void setDtc(String dtc) {
		this.dtc = dtc;
	}
    
}
