package com.cmdt.carrental.common.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleGetOnEditData {

	@JsonProperty("getOnType")
	private String getOnType;
	
	@JsonProperty("getOndata")
	private List<Long> getOndata;

	public String getGetOnType() {
		return getOnType;
	}

	public void setGetOnType(String getOnType) {
		this.getOnType = getOnType;
	}

	public List<Long> getGetOndata() {
		List<Long> retGetOndata = new ArrayList<Long>();
		if(getOndata != null && getOndata.size()>0){
			for(Long val : getOndata){
				if(val != -1){
					retGetOndata.add(val);
				}
			}
		}
		return retGetOndata;
	}

	public void setGetOndata(List<Long> getOndata) {
		this.getOndata = getOndata;
	}

	

	
	
}
