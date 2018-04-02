package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class Area implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3543813076208751118L;
	private Integer regionId;
	private String regionName;
	private Integer parentId;
	private Integer level;
	
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getRegionId() {
		return regionId;
	}
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	

}
