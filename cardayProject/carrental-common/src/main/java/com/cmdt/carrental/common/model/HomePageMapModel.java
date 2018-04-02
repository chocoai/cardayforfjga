package com.cmdt.carrental.common.model;

import java.util.List;

public class HomePageMapModel {

	private List<VehicleLocationModel> obdLocationList;
	private Integer totalVehs;
	private Integer onlineVehs;
	private Integer onTaskVehs;
	private Integer idleVehs;
	
	public List<VehicleLocationModel> getObdLocationList() {
		return obdLocationList;
	}
	public void setObdLocationList(List<VehicleLocationModel> obdLocationList) {
		this.obdLocationList = obdLocationList;
	}
	public Integer getTotalVehs() {
		return totalVehs;
	}
	public void setTotalVehs(Integer totalVehs) {
		this.totalVehs = totalVehs;
	}
	public Integer getOnlineVehs() {
		return onlineVehs;
	}
	public void setOnlineVehs(Integer onlineVehs) {
		this.onlineVehs = onlineVehs;
	}
	public Integer getOnTaskVehs() {
		return onTaskVehs;
	}
	public void setOnTaskVehs(Integer onTaskVehs) {
		this.onTaskVehs = onTaskVehs;
	}
	public Integer getIdleVehs() {
		return idleVehs;
	}
	public void setIdleVehs(Integer idleVehs) {
		this.idleVehs = idleVehs;
	}	
}
