package com.cmdt.carrental.common.service.bean;

public class TripObdCache implements java.io.Serializable {
	
	private static final long serialVersionUID = -8011688407253968703L;
	
	private Long id;
	private String imei;
	private String vendor;
	private java.sql.Timestamp tripStartTime;
	private java.sql.Timestamp tripEndTime;
	private String recordOwner;
	//0:initial 1:processing 2:finished
	private String recordState;
	//current record thread name for debug
	private String recordComment;

	public TripObdCache() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
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

	public String getRecordOwner() {
		return recordOwner;
	}

	public void setRecordOwner(String recordOwner) {
		this.recordOwner = recordOwner;
	}

	public String getRecordState() {
		return recordState;
	}

	public void setRecordState(String recordState) {
		this.recordState = recordState;
	}

	public String getRecordComment() {
		return recordComment;
	}

	public void setRecordComment(String recordComment) {
		this.recordComment = recordComment;
	}

}
