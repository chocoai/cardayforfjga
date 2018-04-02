package com.cmdt.carrental.common.service.bean;

public class TripDetailTimePointInfo implements java.io.Serializable {
	
	private static final long serialVersionUID = -8011688407253968703L;
	
	private Long id;
	private Long detailId;
	private String timeStatus;
	private String beginPoint;
	private String endPoint;
	private java.sql.Timestamp beginTime;
	private java.sql.Timestamp endTime;
	private String mergeBeginPoint;
	private java.sql.Timestamp mergeBeginTime;
	private Integer mergeFlag;
	private Integer secondsSeq;
	
	public TripDetailTimePointInfo() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public String getTimeStatus() {
		return timeStatus;
	}

	public void setTimeStatus(String timeStatus) {
		this.timeStatus = timeStatus;
	}

	public String getBeginPoint() {
		return beginPoint;
	}

	public void setBeginPoint(String beginPoint) {
		this.beginPoint = beginPoint;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public java.sql.Timestamp getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(java.sql.Timestamp beginTime) {
		this.beginTime = beginTime;
	}

	public java.sql.Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(java.sql.Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getMergeBeginPoint() {
		return mergeBeginPoint;
	}

	public void setMergeBeginPoint(String mergeBeginPoint) {
		this.mergeBeginPoint = mergeBeginPoint;
	}

	public java.sql.Timestamp getMergeBeginTime() {
		return mergeBeginTime;
	}

	public void setMergeBeginTime(java.sql.Timestamp mergeBeginTime) {
		this.mergeBeginTime = mergeBeginTime;
	}

	public Integer getMergeFlag() {
		return mergeFlag;
	}

	public void setMergeFlag(Integer mergeFlag) {
		this.mergeFlag = mergeFlag;
	}

	public Integer getSecondsSeq() {
		return secondsSeq;
	}

	public void setSecondsSeq(Integer secondsSeq) {
		this.secondsSeq = secondsSeq;
	}
	
}
