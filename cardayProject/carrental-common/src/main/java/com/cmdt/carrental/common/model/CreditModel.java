package com.cmdt.carrental.common.model;

import com.cmdt.carrental.common.entity.CreditHistory;

public class CreditModel extends CreditHistory {

	private String operatorName; // 操作人的名字

	private String roleName; // 操作人的角色名称

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

}
