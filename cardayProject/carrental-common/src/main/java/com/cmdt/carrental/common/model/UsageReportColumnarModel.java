package com.cmdt.carrental.common.model;

import java.util.List;

public class UsageReportColumnarModel {
	
	String storeId;//页面元素id
	String storeName;//饼图标题
	String unit;//单位
	String avgVal;//平均值
	List<UsageReportColumnarItemModel> dataList;
	public String getStoreId() {
		return storeId;
	}
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getAvgVal() {
		return avgVal;
	}
	public void setAvgVal(String avgVal) {
		this.avgVal = avgVal;
	}
	public List<UsageReportColumnarItemModel> getDataList() {
		return dataList;
	}
	public void setDataList(List<UsageReportColumnarItemModel> dataList) {
		this.dataList = dataList;
	}
	
	
	
	

	
}
