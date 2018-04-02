package com.cmdt.carrental.common.model;

public class VehicleLocationModel {

	private Long id;
	
	private String imei;
	
	private String vehiclenumber;
	
	private String vehicleType;
	
	private double longitude;
	
	private double latitude;
	
	private int speed;
	
	private String status;
	
	private String tracetime;
	
	private String realname;
	
	private String phone;
	
	private int bearing;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getVehiclenumber() {
		return vehiclenumber;
	}
	public void setVehiclenumber(String vehiclenumber) {
		this.vehiclenumber = vehiclenumber;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTracetime() {
		return tracetime;
	}
	public void setTracetime(String tracetime) {
		this.tracetime = tracetime;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public int getBearing() {
		return bearing;
	}
	public void setBearing(int bearing) {
		this.bearing = bearing;
	}
	
}
