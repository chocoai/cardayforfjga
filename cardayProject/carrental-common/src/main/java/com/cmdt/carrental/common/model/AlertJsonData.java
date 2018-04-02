package com.cmdt.carrental.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AlertJsonData {

	private String alertType;
	private String averageNumber;
	private List<AlertMetaData> dataList;
	
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public String getAverageNumber() {
		return averageNumber;
	}
	public void setAverageNumber(String averageNumber) {
		this.averageNumber = averageNumber;
	}
	public List<AlertMetaData> getDataList() {
		return dataList;
	}
	public void setDataList(List<AlertMetaData> dataList) {
		this.dataList = dataList;
	}
	
}
