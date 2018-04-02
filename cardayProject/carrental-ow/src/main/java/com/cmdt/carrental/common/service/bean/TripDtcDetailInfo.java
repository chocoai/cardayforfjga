package com.cmdt.carrental.common.service.bean;


public class TripDtcDetailInfo implements java.io.Serializable {
	
	private static final long serialVersionUID = -8011688407253968703L;
	
	private Long id;
	private Long oldId;
	private Long tripId;
	private String imei;
	private java.sql.Timestamp tripStartTime;
	private java.sql.Timestamp tripEndTime;
	private String dtcList;
	private java.sql.Timestamp createdTime;
	
	public TripDtcDetailInfo() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOldId() {
		return oldId;
	}

	public void setOldId(Long oldId) {
		this.oldId = oldId;
	}

	public Long getTripId() {
		return tripId;
	}

	public void setTripId(Long tripId) {
		this.tripId = tripId;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public java.sql.Timestamp getTripStartTime() {
		return tripStartTime;
	}

	public void setTripStartTime(java.sql.Timestamp tripStartTime) {
		this.tripStartTime = tripStartTime;
	}

	public java.sql.Timestamp getTripEndTime() {
		return tripEndTime;
	}

	public void setTripEndTime(java.sql.Timestamp tripEndTime) {
		this.tripEndTime = tripEndTime;
	}

	public String getDtcList() {
		return dtcList;
	}

	public void setDtcList(String dtcList) {
		this.dtcList = dtcList;
	}

	public java.sql.Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(java.sql.Timestamp createdTime) {
		this.createdTime = createdTime;
	}
}
