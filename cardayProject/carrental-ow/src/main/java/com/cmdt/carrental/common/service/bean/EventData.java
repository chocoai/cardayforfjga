package com.cmdt.carrental.common.service.bean;

public class EventData implements java.io.Serializable {

	private static final long serialVersionUID = -8011688407253968703L;

	private Long id;
	private Integer eventId;
	private Boolean eventFlag;
	private String eventData;
	private Long realTimeId;

	public EventData() {

	}

	public EventData(Long id, Integer eventId, Boolean eventFlag, String eventData, Long realTimeId) {
		this.id = id;
		this.eventId = eventId;
		this.eventFlag = eventFlag;
		this.eventData = eventData;
		this.realTimeId = realTimeId;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public void setEventFlag(Boolean eventFlag) {
		this.eventFlag = eventFlag;
	}

	public Boolean getEventFlag() {
		return eventFlag;
	}

	public String getEventData() {
		return eventData;
	}

	public void setEventData(String eventData) {
		this.eventData = eventData;
	}

	public Long getRealTimeId() {
		return realTimeId;
	}

	public void setRealTimeId(Long realTimeId) {
		this.realTimeId = realTimeId;
	}

}
