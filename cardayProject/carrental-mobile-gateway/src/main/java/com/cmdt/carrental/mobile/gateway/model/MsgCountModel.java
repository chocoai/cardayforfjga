package com.cmdt.carrental.mobile.gateway.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class MsgCountModel {
	private String unReadTotal;
	private String abnormal;
	private String system;
	private String trip;
	private String maintain;
	
	public String getMaintain() {
		return maintain;
	}
	public void setMaintain(String maintain) {
		this.maintain = maintain;
	}
	public String getUnReadTotal() {
		return unReadTotal;
	}
	public void setUnReadTotal(String unReadTotal) {
		this.unReadTotal = unReadTotal;
	}
	public String getAbnormal() {
		return abnormal;
	}
	public void setAbnormal(String abnormal) {
		this.abnormal = abnormal;
	}
	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getTrip() {
		return trip;
	}
	public void setTrip(String trip) {
		this.trip = trip;
	}
	
}