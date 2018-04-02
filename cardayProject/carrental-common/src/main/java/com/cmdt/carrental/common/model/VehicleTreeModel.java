package com.cmdt.carrental.common.model;

import java.util.List;


public class VehicleTreeModel {
	private String text;
	private boolean expanded;
	private String iconCls;
	private boolean leaf;
	private List<VehicleTreeModel> children;
	private String viewType;
	public VehicleTreeModel(){
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


	public List<VehicleTreeModel> getChildren() {
		return children;
	}

	public void setChildren(List<VehicleTreeModel> children) {
		this.children = children;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

}
