package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class DateCountModel implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String date;
	private Integer value;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public DateCountModel() {
		super();
	}
	public DateCountModel(String date, int value) {
		super();
		this.date = date;
		this.value = value;
	}

}
