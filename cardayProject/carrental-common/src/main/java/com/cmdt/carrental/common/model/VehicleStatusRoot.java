package com.cmdt.carrental.common.model;

import java.util.List;

public class VehicleStatusRoot {
	private String text;
	private String iconCls;
	private String rowCls = "row-bgcolor-level1";
	private String viewType;
	private boolean expanded;
	//private boolean selectable;
	private String itemId;
	private String id;
	private List<VehicleTreeStatusModel> children;
	private boolean leaf;
	private String level = "1";
	public VehicleStatusRoot(String entName) {
		this.text=entName;
		this.iconCls="x-fa fa-home";
		this.rowCls="nav-tree-badge";
		this.viewType="admindashboard_AllVehs";
		this.expanded=true;
		//this.selectable=false;
		this.itemId="dashboard";
		this.id="dashboard";
		this.leaf=true;
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
	public List<VehicleTreeStatusModel> getChildren() {
		return children;
	}
	public void setChildren(List<VehicleTreeStatusModel> children) {
		this.children = children;
	}
	public boolean getLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	

}
