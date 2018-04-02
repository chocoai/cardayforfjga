package com.cmdt.carrental.mobile.gateway.model;

public class DateDataModel {
	private String date;
	private String data;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "DateDataModel [date=" + date + ", data=" + data + "]";
	}
	
}
