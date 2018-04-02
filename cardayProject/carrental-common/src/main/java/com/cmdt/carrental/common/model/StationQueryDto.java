package com.cmdt.carrental.common.model;

import java.io.Serializable;


public class StationQueryDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long userId;
	
	private String stationName;
	
	private Integer currentPage;
	
	private Integer numPerPage;

	private Long organizationId;

	private Long stationId;

	private Boolean isEnt;

	private Boolean isRent;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(Integer numPerPage) {
		this.numPerPage = numPerPage;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Boolean getIsEnt() {
		return isEnt;
	}

	public void setIsEnt(Boolean isEnt) {
		this.isEnt = isEnt;
	}

	public Boolean getIsRent() {
		return isRent;
	}

	public void setIsRent(Boolean isRent) {
		this.isRent = isRent;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

}
