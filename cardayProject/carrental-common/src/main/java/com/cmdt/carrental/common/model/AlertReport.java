package com.cmdt.carrental.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AlertReport {

	private List<AlertJsonData> pieList;
	private List<AlertJsonData> columnarList;
	
	public List<AlertJsonData> getPieList() {
		return pieList;
	}
	public void setPieList(List<AlertJsonData> pieList) {
		this.pieList = pieList;
	}
	public List<AlertJsonData> getColumnarList() {
		return columnarList;
	}
	public void setColumnarList(List<AlertJsonData> columnarList) {
		this.columnarList = columnarList;
	}
	
}
