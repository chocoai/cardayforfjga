package com.cmdt.carday.microservice.model.response.alert;

import java.util.List;

import com.cmdt.carrental.common.model.AlertStatisticModel;

public class AlertOrgStatisticRetDto {
	
	private String type;
	private String from;
	private String to;
	private List<AlertStatisticModel> data;
	
	public AlertOrgStatisticRetDto( String type, String from, String to,List<AlertStatisticModel> data) {
		super();
		this.data = data;
		this.type = type;
		this.from = from;
		this.to = to;
	}
	public AlertOrgStatisticRetDto(){
		super();
	}
	
	public List<AlertStatisticModel> getData() {
		return data;
	}
	public void setData(List<AlertStatisticModel> data) {
		this.data = data;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}

}
