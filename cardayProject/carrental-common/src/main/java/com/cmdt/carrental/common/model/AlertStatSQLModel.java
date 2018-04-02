package com.cmdt.carrental.common.model;

import java.util.ArrayList;
import java.util.List;

public class AlertStatSQLModel {
	
	private Long parentId; 
	private Long orgId;
	private String tm;
	private Integer ct;
	private boolean isRootNode;
	private List<AlertStatSQLModel> children;
	
	
	public AlertStatSQLModel(){
		super();
	}
	
	public AlertStatSQLModel(String tm,int ct){
		this.tm = tm;
		this.ct = ct;
	}
	
	public String getTm() {
		return tm;
	}
	public void setTm(String tm) {
		this.tm = tm;
	}
	public Integer getCt() {
		return ct;
	}
	public void setCt(Integer ct) {
		this.ct = ct;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public List<AlertStatSQLModel> getChildren() {
		if(children == null){
			children = new ArrayList<>();
		}
		return children;
	}
	public void setChildren(List<AlertStatSQLModel> children) {
		this.children = children;
	}
	
	public boolean isRootNode() {
		return isRootNode;
	}
	public void setRootNode(boolean isRootNode) {
		this.isRootNode = isRootNode;
	}
	
	public int getcountToal() {
		if(isRootNode){
			return ct;
		}
		
		if(children != null && children.size() > 0){
			for(AlertStatSQLModel childVal : children){
				this.ct += childVal.getcountToal();//递归汇总所有子节点数据
			}
		}
		return ct;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	
}
