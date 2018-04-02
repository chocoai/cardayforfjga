package com.cmdt.carrental.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Tree model
 *
 */
public class TreeNode implements Serializable {

	private static final long serialVersionUID = -4395857928419436611L;
	
	private Long id;
	
	private String text;
	
	private Long parentId;
	
	private List<TreeNode> children=new ArrayList<TreeNode>();
	
	private Boolean leaf = Boolean.TRUE;
	
	private Boolean checked = null;
	
	private Boolean expanded = Boolean.FALSE;
	
	private Double availableCredit=0d;
	
	private Double limitedCredit=0d;
	
	private Integer level;
	
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
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	public Boolean getLeaf() {
		return leaf;
	}
	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
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
	public Double getAvailableCredit() {
		return availableCredit;
	}
	public void setAvailableCredit(Double availableCredit) {
		this.availableCredit = availableCredit;
	}
	public Double getLimitedCredit() {
		return limitedCredit;
	}
	public void setLimitedCredit(Double limitedCredit) {
		this.limitedCredit = limitedCredit;
	}
	
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
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
		buffer.append("'checked':").append(checked).append(",");
		buffer.append("'availableCredit':").append(availableCredit).append(",");
		buffer.append("'limitedCredit':").append(limitedCredit);
		buffer.append("}");
		return buffer.toString();
	}
	
	
	
}
