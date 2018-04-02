package com.cmdt.carrental.mobile.gateway.model.response.vehicle;

public class VehicleTraceResult {
	
	private String time;
	private String longitude;
	private String latitude;
	private String speed;
	private String driveStatus;
	private String address;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getDriveStatus() {
		return driveStatus;
	}
	public void setDriveStatus(String driveStatus) {
		this.driveStatus = driveStatus;
	}
}
