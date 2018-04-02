package com.cmdt.carrental.platform.service.model.response.alert;

import java.util.List;

import com.cmdt.carrental.common.model.VehicleAlertModel;

public class AlertOrgRetDto {
	
	private String type;
	private String from;
	private String to;
	private Integer pageSize;
	private Integer currentpage;
	private Integer total;
	private List<VehicleAlertModel> data;
	
	public AlertOrgRetDto(String type, String from, String to, Integer pageSize, Integer currentpage, Integer total,
			List<VehicleAlertModel> data) {
		super();
		this.type = type;
		this.from = from;
		this.to = to;
		this.pageSize = pageSize;
		this.currentpage = currentpage;
		this.total = total;
		this.data = data;
	}
	
	public AlertOrgRetDto(){
		
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
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getCurrentpage() {
		return currentpage;
	}
	public void setCurrentpage(Integer currentpage) {
		this.currentpage = currentpage;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public List<VehicleAlertModel> getData() {
		return data;
	}
	public void setData(List<VehicleAlertModel> data) {
		this.data = data;
	}
	
}
