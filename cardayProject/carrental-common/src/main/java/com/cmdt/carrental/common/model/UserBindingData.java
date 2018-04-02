package com.cmdt.carrental.common.model;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserBindingData {
	
	@JsonProperty("userId")
	private Long userId;

	@JsonProperty("ruleList")
	private List<Long> ruleList;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Long> getRuleList() {
		List<Long> retRuleList = new ArrayList<Long>();
		if(ruleList != null && ruleList.size()>0){
			for(Long val : ruleList){
				if(val != -1){
					retRuleList.add(val);
				}
			}
		}
		return retRuleList;
	}

	public void setRuleList(List<Long> ruleList) {
		this.ruleList = ruleList;
	}
	
	
	
	
	
}
