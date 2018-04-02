package com.cmdt.carrental.common.model;

import java.util.List;

public class VehicleRoot {
	private String text;
	private String iconCls;
	private String rowCls;
	private String viewType;
	private boolean expanded;
	//private boolean selectable;
	private String itemId;
	private String id;
	private List<VehicleTreeModel> children;
	private boolean leaf;
	public VehicleRoot() {
		this.text="首页";
		this.iconCls="x-fa fa-home";
		this.rowCls="nav-tree-badge";
		this.viewType="admindashboard_AllVehs";
		this.expanded=true;
		//this.selectable=false;
		this.itemId="dashboard";
		this.id="dashboard";
		this.leaf=false;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getRowCls() {
		return rowCls;
	}
	public void setRowCls(String rowCls) {
		this.rowCls = rowCls;
	}
	public String getViewType() {
		return viewType;
	}
	public void setViewType(String viewType) {
		this.viewType = viewType;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
/*	public boolean isSelectable() {
		return selectable;
	}
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}*/
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<VehicleTreeModel> getChildren() {
		return children;
	}
	public void setChildren(List<VehicleTreeModel> children) {
		this.children = children;
	}
	public boolean getLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	
	
	

}
