package com.cmdt.carrental.quartz.filter;

import com.cmdt.carrental.common.bean.AlertOperator;

public class Condition<T> {

	private String restrictName;
	
	private T restrictValue;
	
	private AlertOperator operator;

	public String getRestrictName() {
		return restrictName;
	}

	public void setRestrictName(String restrictName) {
		this.restrictName = restrictName;
	}

	public T getRestrictValue() {
		return restrictValue;
	}

	public void setRestrictValue(T restrictValue) {
		this.restrictValue = restrictValue;
	}

	public AlertOperator getOperator() {
		return operator;
	}

	public void setOperator(AlertOperator operator) {
		this.operator = operator;
	}
	
}
