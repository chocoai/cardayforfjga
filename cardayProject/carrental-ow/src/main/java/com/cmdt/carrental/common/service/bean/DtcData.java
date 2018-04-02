package com.cmdt.carrental.common.service.bean;

public class DtcData implements java.io.Serializable {
	
	private static final long serialVersionUID = -8011688407253968703L;
	
	private Long id;
	private String dtc;
	private Long realTimeId;
	
	public DtcData(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDtc() {
		return dtc;
	}

	public void setDtc(String dtc) {
		this.dtc = dtc;
	}

	public Long getRealTimeId() {
		return realTimeId;
	}

	public void setRealTimeId(Long realTimeId) {
		this.realTimeId = realTimeId;
	}

}
