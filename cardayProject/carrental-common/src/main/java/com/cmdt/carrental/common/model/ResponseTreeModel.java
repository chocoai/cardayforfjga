package com.cmdt.carrental.common.model;

public class ResponseTreeModel<T> extends ResponseModel<T> {
	protected boolean expanded;
	protected T children;

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public T getChildren() {
		return children;
	}

	public void setChildren(T children) {
		this.children = children;
	}

}
