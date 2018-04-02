package com.cmdt.carrental.common.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleGetOffData {

	@JsonProperty("getOffType")
	private String getOffType;
	
	@JsonProperty("getOffdata")
	private List<String> getOffdata;

	public String getGetOffType() {
		return getOffType;
	}

	public void setGetOffType(String getOffType) {
		this.getOffType = getOffType;
	}

	public List<String> getGetOffdata() {
		List<String> retGetOffdata = new ArrayList<String>();
		if(getOffdata != null && getOffdata.size()>0){
			for(String val : getOffdata){
				if(!"-1".equals(val)){
					retGetOffdata.add(val);
				}
			}
		}
		return retGetOffdata;
	}

	public void setGetOffdata(List<String> getOffdata) {
		this.getOffdata = getOffdata;
	}
	
}
