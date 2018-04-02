package com.cmdt.carrental.platform.service.model.request.rule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengjun.jing on 6/6/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RuleBdDto {
	
	private Long userId;
	private Long uid;
	private List<Long> ruleList;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
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
