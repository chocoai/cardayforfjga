package com.cmdt.carrental.common.service.bean;

import com.vividsolutions.jts.geom.Geometry;

public class TripTraceDetailInfo implements java.io.Serializable {
	
	private static final long serialVersionUID = -8011688407253968703L;
	
	private Long id;
	private Long tripId;
	private String imei;
	private java.sql.Timestamp tripStartTime;
	private java.sql.Timestamp tripEndTime;
	private Geometry traceGeometry;
	private java.sql.Timestamp createdTime;
	private String idList;
	private String traceList;
	
	public TripTraceDetailInfo() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Geometry getTraceGeometry() {
		return traceGeometry;
	}

	public void setTraceGeometry(Geometry traceGeometry) {
		this.traceGeometry = traceGeometry;
	}

	public java.sql.Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(java.sql.Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getIdList() {
		return idList;
	}

	public void setIdList(String idList) {
		this.idList = idList;
	}

	public String getTraceList() {
		return traceList;
	}

	public void setTraceList(String traceList) {
		this.traceList = traceList;
	}
	
}
