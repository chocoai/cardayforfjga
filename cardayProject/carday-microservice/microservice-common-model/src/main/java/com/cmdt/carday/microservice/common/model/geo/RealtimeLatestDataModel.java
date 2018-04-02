package com.cmdt.carday.microservice.common.model.geo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RealtimeLatestDataModel {
	private String imei;
	private String vin;
	private double longitude;
	private double latitude;
	private Long mileage;
	private Long fuelCons;
	private Integer speed;
	private Integer rpm;
	private Integer temperature;
	private Double extVol;
	private Double intVol;
	private Date traceTime;
	private Integer bearing;
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
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
	public Long getMileage() {
		return mileage;
	}
	public void setMileage(Long mileage) {
		this.mileage = mileage;
	}
	public Long getFuelCons() {
		return fuelCons;
	}
	public void setFuelCons(Long fuelCons) {
		this.fuelCons = fuelCons;
	}
	public Integer getSpeed() {
		return speed;
	}
	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
	public Integer getRpm() {
		return rpm;
	}
	public void setRpm(Integer rpm) {
		this.rpm = rpm;
	}
	public Integer getTemperature() {
		return temperature;
	}
	public void setTemperature(Integer temperature) {
		this.temperature = temperature;
	}
	public Double getExtVol() {
		return extVol;
	}
	public void setExtVol(Double extVol) {
		this.extVol = extVol;
	}
	public Double getIntVol() {
		return intVol;
	}
	public void setIntVol(Double intVol) {
		this.intVol = intVol;
	}
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	public Date getTraceTime() {
		return traceTime;
	}
	public void setTraceTime(Date traceTime) {
		this.traceTime = traceTime;
	}
	public Integer getBearing() {
		return bearing;
	}
	public void setBearing(Integer bearing) {
		this.bearing = bearing;
	}
	
}
