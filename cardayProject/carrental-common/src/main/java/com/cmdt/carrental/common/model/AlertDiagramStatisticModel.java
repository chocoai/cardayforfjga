package com.cmdt.carrental.common.model;

import java.util.ArrayList;
import java.util.List;

public class AlertDiagramStatisticModel {
	
	private Long entId;
	private Long orgId;
	private String orgName;
	private String alertType;
	private double outboundKilosAvg;
	private double outboundKilosTotal;
	private int alertTotal;
	private double alertAvg;
	private Long parent_id;
	private boolean isRootNode;
	private List<AlertDiagramStatisticModel> children;
	
	public Long getEntId() {
		return entId;
	}
	public void setEntId(Long entId) {
		this.entId = entId;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public Double getOutboundKilosAvg() {
		return outboundKilosAvg;
	}
	public void setOutboundKilosAvg(Double outboundKilosAvg) {
		this.outboundKilosAvg = outboundKilosAvg;
	}
	public Double getOutboundKilosTotal() {
		if(isRootNode){
			return outboundKilosTotal;
		}
		
		double tempCount = outboundKilosTotal;
		if(children != null && children.size() > 0){
			for(AlertDiagramStatisticModel childVal : children){
				tempCount = this.outboundKilosTotal + childVal.getOutboundKilosTotal();//递归汇总所有子节点数据
			}
		}
		return tempCount;
	}
	public void setOutboundKilosTotal(Double outboundKilosTotal) {
		this.outboundKilosTotal = outboundKilosTotal;
	}
	public Integer getAlertTotal() {
		if(isRootNode){
			return alertTotal;
		}
		
		int tempCount = alertTotal;
		if(children != null && children.size() > 0){
			for(AlertDiagramStatisticModel childVal : children){
				tempCount += childVal.getAlertTotal();//递归汇总所有子节点数据
			}
		}
		return tempCount;
	}
	public void setAlertTotal(Integer alertTotal) {
		this.alertTotal = alertTotal;
	}
	public Double getAlertAvg() {
		return alertAvg;
	}
	public void setAlertAvg(Double alertAvg) {
		this.alertAvg = alertAvg;
	}
	public Long getParent_id() {
		return parent_id;
	}
	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
	}
	public boolean isRootNode() {
		return isRootNode;
	}
	public void setRootNode(boolean isRootNode) {
		this.isRootNode = isRootNode;
	}
	public List<AlertDiagramStatisticModel> getChildren() {
		if(children == null){
			children = new ArrayList<AlertDiagramStatisticModel>();
		}
		return children;
	}
	public void setChildren(List<AlertDiagramStatisticModel> children) {
		this.children = children;
	}

	
}
