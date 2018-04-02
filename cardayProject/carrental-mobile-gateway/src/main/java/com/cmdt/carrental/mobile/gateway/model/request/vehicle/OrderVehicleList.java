package com.cmdt.carrental.mobile.gateway.model.request.vehicle;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class OrderVehicleList {
	
	@NotNull(message="orgId不能为空")
	private Long orgId;
	@NotNull(message="orderId不能为空")
	private Long orderId;
	@Min(value=0)
	private int currentPage;
	@Min(value=1)
	private int numPerPage;
	@NotNull
	private boolean selfDept;
	@NotNull
	private boolean childDept;
	
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getNumPerPage() {
		return numPerPage;
	}
	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}
	public boolean isSelfDept() {
		return selfDept;
	}
	public void setSelfDept(boolean selfDept) {
		this.selfDept = selfDept;
	}
	public boolean isChildDept() {
		return childDept;
	}
	public void setChildDept(boolean childDept) {
		this.childDept = childDept;
	}
	
}
