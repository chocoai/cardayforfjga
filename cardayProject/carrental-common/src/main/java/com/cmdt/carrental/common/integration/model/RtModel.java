package com.cmdt.carrental.common.integration.model;

import java.io.Serializable;

public class RtModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String address;
	private Double lng;
	private Double lat;
	private Double speed;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getSpeed() {
		return speed;
	}
	public void setSpeed(Double speed) {
		this.speed = speed;
	}
	@Override
	public String toString() {
		return "RtModel [address=" + address + ", lng=" + lng + ", lat=" + lat + ", speed=" + speed + "]";
	}
	
}
