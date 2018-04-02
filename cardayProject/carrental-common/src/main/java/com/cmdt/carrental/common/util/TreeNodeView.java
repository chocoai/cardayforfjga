package com.cmdt.carrental.common.util;

import java.util.List;

/**
 * Tree model
 *
 */
public class TreeNodeView {

	private Long id;
	private String text;
	private List<TreeNodeView> children;
	private Boolean leaf = Boolean.TRUE;
	private Boolean expanded = Boolean.FALSE;
	
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
	public List<TreeNodeView> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNodeView> children) {
		this.children = children;
	}
	public Boolean getLeaf() {
		return leaf;
	}
	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}
	public Boolean getExpanded() {
		return expanded;
	}
	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("'id':").append(id).append(",");
		buffer.append("'text':").append("'"+text+"'").append(",");
		if(children != null){
			buffer.append("'children':").append(children).append(",");
		}
		buffer.append("'expanded':").append(expanded).append(",");
		buffer.append("'leaf':").append(leaf).append(",");
		buffer.append("}");
		return buffer.toString();
	}
	
}
