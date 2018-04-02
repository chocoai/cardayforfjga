package com.cmdt.carday.microservice.model.request.rule;

public class UserUnbindRuleDto {
	private Long userId; //登录用户id
	private Long ruleId;
	private Long uid; //用于查询或删除的用户id,用于区别于userId
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getRuleId() {
		return ruleId;
	}
	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
}
