package com.cmdt.carrental.common.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleGetOffEditData {

	@JsonProperty("getOffType")
	private String getOffType;
	
	@JsonProperty("getOffdata")
	private List<Long> getOffdata;

	public String getGetOffType() {
		return getOffType;
	}

	public void setGetOffType(String getOffType) {
		this.getOffType = getOffType;
	}

	public List<Long> getGetOffdata() {
		List<Long> retGetOffdata = new ArrayList<Long>();
		if(getOffdata != null && getOffdata.size()>0){
			for(Long val : getOffdata){
				if(val != -1){
					retGetOffdata.add(val);
				}
			}
		}
		return retGetOffdata;
	}

	public void setGetOffdata(List<Long> getOffdata) {
		this.getOffdata = getOffdata;
	}
	
}
