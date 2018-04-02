package com.cmdt.carrental.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TripTraceDisplayModelItem {
	
	private String imei;
	private String vin;
	private String startracetime;
	private String endtracetime;
	private String tracegeometry;
	private String idlist;
	private long totoalseconds;
	
	
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getStartracetime() {
		return startracetime;
	}
	public void setStartracetime(String startracetime) {
		this.startracetime = startracetime;
	}
	public String getEndtracetime() {
		return endtracetime;
	}
	public void setEndtracetime(String endtracetime) {
		this.endtracetime = endtracetime;
	}
	public String getTracegeometry() {
		return tracegeometry;
	}
	public void setTracegeometry(String tracegeometry) {
		this.tracegeometry = tracegeometry;
	}
	public String getIdlist() {
		return idlist;
	}
	public void setIdlist(String idlist) {
		this.idlist = idlist;
	}
	public long getTotoalseconds() {
		return totoalseconds;
	}
	public void setTotoalseconds(long totoalseconds) {
		this.totoalseconds = totoalseconds;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
    
}
