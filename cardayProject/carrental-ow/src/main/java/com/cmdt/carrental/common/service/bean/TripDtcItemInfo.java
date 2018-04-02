package com.cmdt.carrental.common.service.bean;

public class TripDtcItemInfo implements java.io.Serializable {
	
	private static final long serialVersionUID = -8011688407253968703L;
	
	private Long id;
	private Long dtcId;
	private String dtcCode;
	private Integer times;
	
	public TripDtcItemInfo() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDtcId() {
		return dtcId;
	}

	public void setDtcId(Long dtcId) {
		this.dtcId = dtcId;
	}

	public String getDtcCode() {
		return dtcCode;
	}

	public void setDtcCode(String dtcCode) {
		this.dtcCode = dtcCode;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

}
