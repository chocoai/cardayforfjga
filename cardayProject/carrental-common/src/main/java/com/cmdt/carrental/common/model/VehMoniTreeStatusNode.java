package com.cmdt.carrental.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Tree model
 *
 */
public class VehMoniTreeStatusNode implements Serializable {

	private static final long serialVersionUID = -4395857928419436611L;
	
	private Long id;
	
	private String text;
	
	private Long parentId;
	
	private List<VehMoniTreeStatusNode> children=new ArrayList<VehMoniTreeStatusNode>();
	
	private Boolean leaf = Boolean.TRUE;
	
	private Boolean checked = null;
	
	private Boolean expanded = Boolean.FALSE;
	
	private String level;
	
	private String status;  //车辆时显示状态 行驶 停止 离线
	
	private String type;  //DEPT:部门 VEHI:车辆

	private String parentIds;
	
	private String vehicleNumber;
	private String deviceNumber;
	private boolean isVehicleNode = false;//是否为车辆节点
	private Long vehicleId;//车辆id
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<VehMoniTreeStatusNode> getChildren() {
		return children;
	}
	public void setChildren(List<VehMoniTreeStatusNode> children) {
		this.children = children;
	}
	
	public Boolean getChecked() {
		return checked;
	}
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
	public Boolean getExpanded() {
		return expanded;
	}
	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}
	
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
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
	public boolean isVehicleNode() {
		return isVehicleNode;
	}
	public void setVehicleNode(boolean isVehicleNode) {
		this.isVehicleNode = isVehicleNode;
	}
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Boolean getLeaf() {
		return leaf;
	}
	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}
	
	
}
