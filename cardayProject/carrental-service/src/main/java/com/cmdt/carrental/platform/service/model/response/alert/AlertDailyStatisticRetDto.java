package com.cmdt.carrental.platform.service.model.response.alert;

import java.util.List;

import com.cmdt.carrental.common.model.DateCountModel;

public class AlertDailyStatisticRetDto {
	
	private String type;
	private String from;
	private String to;
	
	private List<DateCountModel> data;
	//private Map<String,Integer> data;

	public String getType() {
		return type;
	}
	
	public AlertDailyStatisticRetDto(){
		super();
	}

	public AlertDailyStatisticRetDto(String type, String from, String to, List<DateCountModel> data) {
		super();
		this.type = type;
		this.from = from;
		this.to = to;
		this.data = data;
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

	public List<DateCountModel> getData() {
		return data;
	}

	public void setData(List<DateCountModel> data) {
		this.data = data;
	}

}
