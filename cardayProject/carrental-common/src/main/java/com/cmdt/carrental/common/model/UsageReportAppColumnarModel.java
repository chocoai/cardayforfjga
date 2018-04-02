package com.cmdt.carrental.common.model;

import java.util.List;

public class UsageReportAppColumnarModel {

	String dataAvgval;//元素平均值
	String dataUnit;//元素单位(单位:千米||单位:升||单位:小时)
	String dataFlag;//里程||耗油量||行驶时长
	
	List<UsageReportAppColumnarItemModel> dataList;

	public String getDataAvgval() {
		return dataAvgval;
	}

	public void setDataAvgval(String dataAvgval) {
		this.dataAvgval = dataAvgval;
	}

	public String getDataUnit() {
		return dataUnit;
	}

	public void setDataUnit(String dataUnit) {
		this.dataUnit = dataUnit;
	}

	public String getDataFlag() {
		return dataFlag;
	}

	public void setDataFlag(String dataFlag) {
		this.dataFlag = dataFlag;
	}

	public List<UsageReportAppColumnarItemModel> getDataList() {
		return dataList;
	}

	public void setDataList(List<UsageReportAppColumnarItemModel> dataList) {
		this.dataList = dataList;
	}
	
}
