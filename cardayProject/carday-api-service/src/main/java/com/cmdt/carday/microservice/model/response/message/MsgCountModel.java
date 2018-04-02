package com.cmdt.carday.microservice.model.response.message;

import java.io.Serializable;

public class MsgCountModel implements Serializable {

	private static final long serialVersionUID = 1557998992395124283L;

	private String unReadTotal;

	private String abnormal;

	private String system;

	private String trip;

	private String maintain;

	public String getMaintain() {
		return maintain;
	}

	public void setMaintain(String maintain) {
		this.maintain = maintain;
	}

	public String getUnReadTotal() {
		return unReadTotal;
	}

	public void setUnReadTotal(String unReadTotal) {
		this.unReadTotal = unReadTotal;
	}

	public String getAbnormal() {
		return abnormal;
	}

	public void setAbnormal(String abnormal) {
		this.abnormal = abnormal;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getTrip() {
		return trip;
	}

	public void setTrip(String trip) {
		this.trip = trip;
	}

}