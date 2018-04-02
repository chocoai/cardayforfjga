package com.cmdt.carrental.common.model;

import java.util.List;

public class UsageReportLineModel {

	String data1avgval;//元素1平均值
	String data2avgval;//元素2平均值
	String data1unit;//元素1单位
	String data2unit;//元素2单位
	
	List<UsageReportLineItemModel> dataList;

	public String getData1avgval() {
		return data1avgval;
	}

	public void setData1avgval(String data1avgval) {
		this.data1avgval = data1avgval;
	}

	public String getData2avgval() {
		return data2avgval;
	}

	public void setData2avgval(String data2avgval) {
		this.data2avgval = data2avgval;
	}

	public String getData1unit() {
		return data1unit;
	}

	public void setData1unit(String data1unit) {
		this.data1unit = data1unit;
	}

	public String getData2unit() {
		return data2unit;
	}

	public void setData2unit(String data2unit) {
		this.data2unit = data2unit;
	}

	public List<UsageReportLineItemModel> getDataList() {
		return dataList;
	}

	public void setDataList(List<UsageReportLineItemModel> dataList) {
		this.dataList = dataList;
	}
	
}
