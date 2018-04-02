package com.cmdt.carrental.common.model;

import java.util.ArrayList;
import java.util.List;

public class AlertStatisticModel {
	
	private Long parentId;
	private Long id;
	private String name;
	private Integer value;
	private boolean isRootNode;
	private List<AlertStatisticModel> children;
	
	public AlertStatisticModel(){
		super();
	}
	
	public AlertStatisticModel(long id,String name,int value){
		this.id = id;
		this.name = name;
		this.value = value;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public List<AlertStatisticModel> getChildren() {
			if(children == null){
				children = new ArrayList<>();
			}
			return children;
	}
	public void setChildren(List<AlertStatisticModel> children) {
		this.children = children;
	}
	
	public boolean isRootNode() {
		return isRootNode;
	}

	public void setRootNode(boolean isRootNode) {
		this.isRootNode = isRootNode;
	}
	
	public int getValueToal() {
		if(isRootNode){
			return value;
		}
		
		if(children != null && children.size() > 0){
			for(AlertStatisticModel childVal : children){
				this.value += childVal.getValueToal();//递归汇总所有子节点数据
			}
		}
		return value;
	}
}
