package com.cmdt.carrental.common.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RuleGetOnData {

	@JsonProperty("getOnType")
	private String getOnType;
	
	@JsonProperty("getOndata")
	private List<String> getOndata;

	public String getGetOnType() {
		return getOnType;
	}

	public void setGetOnType(String getOnType) {
		this.getOnType = getOnType;
	}

	public List<String> getGetOndata() {
		List<String> retGetOndata = new ArrayList<String>();
		if(getOndata != null && getOndata.size()>0){
			for(String val : getOndata){
				if(!"-1".equals(val)){
					retGetOndata.add(val);
				}
			}
		}
		return retGetOndata;
	}

	public void setGetOndata(List<String> getOndata) {
		this.getOndata = getOndata;
	}
	
	
	
}
