package com.cmdt.carrental.common.model;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleEditData {
	
	@JsonProperty("ruleId")
	private Long ruleId;

	@JsonProperty("ruleName")
	private String ruleName;
	
	@JsonProperty("ruleType")
	private String ruleType;
	
	@JsonProperty("getOnList")
	private RuleGetOnEditData getOnList;
	
	@JsonProperty("getOffList")
	private RuleGetOffEditData getOffList;
	
	@JsonProperty("vehicleType")
	private String vehicleType;
	
	@JsonProperty("useLimit")
	private String useLimit;
	
	@JsonProperty("timeList")
	private RuleTimeEditData timeList;
	
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

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public RuleGetOnEditData getGetOnList() {
		return getOnList;
	}

	public void setGetOnList(RuleGetOnEditData getOnList) {
		this.getOnList = getOnList;
	}

	public RuleGetOffEditData getGetOffList() {
		return getOffList;
	}

	public void setGetOffList(RuleGetOffEditData getOffList) {
		this.getOffList = getOffList;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getUseLimit() {
		return useLimit;
	}

	public void setUseLimit(String useLimit) {
		this.useLimit = useLimit;
	}

	public RuleTimeEditData getTimeList() {
		return timeList;
	}

	public void setTimeList(RuleTimeEditData timeList) {
		this.timeList = timeList;
	}
	
}
