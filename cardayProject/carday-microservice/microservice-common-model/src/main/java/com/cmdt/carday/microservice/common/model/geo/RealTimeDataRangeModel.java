package com.cmdt.carday.microservice.common.model.geo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class RealTimeDataRangeModel {
	
	private String imei;
	private String start;
	private String end;
	
	public RealTimeDataRangeModel(){
		super();
	}
	
	public RealTimeDataRangeModel(String imei, String start, String end) {
		super();
		this.imei = imei;
		this.start = start;
		this.end = end;
	}
	
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	
	@Override
	public String toString(){
		return "imei:"+imei+" start:"+start+" end:"+end;
	}

}
