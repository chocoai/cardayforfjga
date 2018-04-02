package com.cmdt.carrental.common.model;

import java.util.List;

public class UsageReportModel {

	//饼图
	List<UsageReportPieModel> pieList;
	//柱状图
	List<UsageReportColumnarModel> columnarList;
	
	public List<UsageReportPieModel> getPieList() {
		return pieList;
	}
	public void setPieList(List<UsageReportPieModel> pieList) {
		this.pieList = pieList;
	}
	public List<UsageReportColumnarModel> getColumnarList() {
		return columnarList;
	}
	public void setColumnarList(List<UsageReportColumnarModel> columnarList) {
		this.columnarList = columnarList;
	}
	
	
}
