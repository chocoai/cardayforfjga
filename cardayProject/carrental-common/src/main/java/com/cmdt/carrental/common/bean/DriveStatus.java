package com.cmdt.carrental.common.bean;

public enum DriveStatus {

	OFFLINE("离线", "0"), INTRANSIT("行驶中", "1"), STOP("停止", "2");

	private DriveStatus(String name, String index) {
		this.name = name;
		this.index = index;
	}

	private String name;
	private String index;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

}
