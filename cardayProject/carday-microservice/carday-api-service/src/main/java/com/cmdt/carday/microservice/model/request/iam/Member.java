package com.cmdt.carday.microservice.model.request.iam;

import java.io.Serializable;

public class Member implements Serializable {

	private static final long serialVersionUID = -4193471571682227774L;

	private String display;

	private String value;

	
	
	public Member() {
		super();
	}

	public Member(String display, String value) {
		super();
		this.display = display;
		this.value = value;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
