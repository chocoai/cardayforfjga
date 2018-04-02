package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class Region implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private Integer parentId;
	private Integer level;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	

}
