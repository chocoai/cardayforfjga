package com.cmdt.carrental.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OBDModel {
	private String imei;
	private Boolean inMarker;
	

	public OBDModel() {
		
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Boolean getInMarker() {
		return inMarker;
	}

	public void setInMarker(Boolean inMarker) {
		this.inMarker = inMarker;
	}

	
	
	

}
