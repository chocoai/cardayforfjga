package com.cmdt.carrental.common.entity;

import java.util.Date;

public class CreditHistory {

	private Long id; // 主键

	private String operationType; // 操作名称

	private Long operatorId; // 操作人ID

	private Long operatorRole; // 操作人的角色ID

	private Long orgId;// 企业Id

	private Date operateTime; // 操作时间 yyyy-MM-dd HH:mm:ss

	private Integer creditValue; // 充值金额

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public Long getOperatorRole() {
		return operatorRole;
	}

	public void setOperatorRole(Long operatorRole) {
		this.operatorRole = operatorRole;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public Integer getCreditValue() {
		return creditValue;
	}

	public void setCreditValue(Integer creditValue) {
		this.creditValue = creditValue;
	}
}
