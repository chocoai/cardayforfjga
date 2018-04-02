package com.cmdt.carrental.common.model;

import java.util.ArrayList;
import java.util.List;


public class VehicleTreeStatusModel {
	private String text;
	private boolean expanded;
	private String iconCls;
	private boolean leaf;
	private List<VehicleTreeStatusModel> children;
	private String viewType;
	private Long vehicleId;//车辆id
	private Long deptId;//部门id
	private String level;
	
	//新增字段for多级节点
	private String vehicleNumber;
	private String deviceNumber;
	private Long nodeId;
	private String nodeName;
	private Long parentId;
	private String parentIds;
	private boolean isVehicleNode = false;//是否为车辆节点
	private String rowCls;
	
	public VehicleTreeStatusModel(){
		iconCls = "x-fa fa-car";
		leaf = true;
	}
	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}


	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}


	public List<VehicleTreeStatusModel> getChildren() {
		if(children == null){
			children = new ArrayList<VehicleTreeStatusModel>();
		}
		return children;
	}

	public void setChildren(List<VehicleTreeStatusModel> children) {
		this.children = children;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public Long getNodeId() {
		return nodeId;
	}
	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public boolean isVehicleNode() {
		return isVehicleNode;
	}
	public void setVehicleNode(boolean isVehicleNode) {
		this.isVehicleNode = isVehicleNode;
	}
	public String getRowCls() {
		return rowCls;
	}
	public void setRowCls(String rowCls) {
		this.rowCls = rowCls;
	}
	
	
}
