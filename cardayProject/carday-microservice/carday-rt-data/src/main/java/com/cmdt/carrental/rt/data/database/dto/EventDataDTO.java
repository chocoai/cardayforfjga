package com.cmdt.carrental.rt.data.database.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Pattern;

import com.cmdt.carrental.rt.data.service.common.Patterns;


public class EventDataDTO {

    private String imei;
    
    /**
     * yyyy-MM-dd
     */
    @Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS, message = "planTime format error!")
    private String traceTime;
    
    private Date createTime;
    
    private List<Event> event = new ArrayList<Event>();
    
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
	public List<Event> getEvent() {
		return event;
	}
	public void setEvent(List<Event> event) {
		this.event = event;
	}
}
