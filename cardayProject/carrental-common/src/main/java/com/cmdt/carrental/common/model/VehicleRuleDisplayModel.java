package com.cmdt.carrental.common.model;

import java.util.ArrayList;
import java.util.List;

public class VehicleRuleDisplayModel {

	private Long ruleId;
	private String ruleName;
	private String employeeNum;
	private List<String> timeList = new ArrayList<>();
	private List<String> getOnList = new ArrayList<>();
	private List<String> getOffList = new ArrayList<>();
	private List<String> vehicleTypeList = new ArrayList<>();
	private String useLimit;
	private Integer timeRange;
	
	public void addTime(String time){
		timeList.add(time);
	}
	
	public void addGetOn(String getOnAddress){
		getOnList.add(getOnAddress);
	}
	
	public void addGetOff(String getOffAddress){
		getOffList.add(getOffAddress);
	}
	
	public void addVehicleType(String vehicleType){
		vehicleTypeList.add(vehicleType);
	}
	
	public Long getRuleId() {
		return ruleId;
	}
	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getEmployeeNum() {
		return employeeNum;
	}
	public void setEmployeeNum(String employeeNum) {
		this.employeeNum = employeeNum;
	}
	public List<String> getTimeList() {
		return timeList;
	}
	public void setTimeList(List<String> timeList) {
		this.timeList = timeList;
	}
	public List<String> getGetOnList() {
		return getOnList;
	}
	public void setGetOnList(List<String> getOnList) {
		this.getOnList = getOnList;
	}
	public List<String> getGetOffList() {
		return getOffList;
	}
	public void setGetOffList(List<String> getOffList) {
		this.getOffList = getOffList;
	}
	public List<String> getVehicleTypeList() {
		return vehicleTypeList;
	}
	public void setVehicleTypeList(List<String> vehicleTypeList) {
		this.vehicleTypeList = vehicleTypeList;
	}
	public String getUseLimit() {
		return useLimit;
	}
	public void setUseLimit(String useLimit) {
		this.useLimit = useLimit;
	}

	public Integer getTimeRange() {
		return timeRange;
	}

	public void setTimeRange(Integer timeRange) {
		this.timeRange = timeRange;
	}
	
	
}
