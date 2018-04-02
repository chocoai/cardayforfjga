package com.cmdt.carrental.common.integration.model;

public class TrackModel {
	private Double lng;
	private Double lat;
	private String gPSTime;
	private Integer speed;
	private Integer direction;
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
	public String getgPSTime() {
		return gPSTime;
	}
	public void setgPSTime(String gPSTime) {
		this.gPSTime = gPSTime;
	}
	public Integer getSpeed() {
		return speed;
	}
	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
	public Integer getDirection() {
		return direction;
	}
	public void setDirection(Integer direction) {
		this.direction = direction;
	}
	@Override
	public String toString() {
		return "TrackModel [lng=" + lng + ", lat=" + lat + ", gPSTime=" + gPSTime + ", speed=" + speed + ", direction="
				+ direction + "]";
	}
	
}
