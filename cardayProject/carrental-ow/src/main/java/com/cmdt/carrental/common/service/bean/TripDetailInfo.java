package com.cmdt.carrental.common.service.bean;

public class TripDetailInfo implements java.io.Serializable {
	
	private static final long serialVersionUID = -8011688407253968703L;
	
	private Long id;
	private Long oldId;
	private Long tripId;
	private String imei;
	private java.sql.Timestamp tripStartTime;
	private java.sql.Timestamp tripEndTime;
	private Integer fuelCons=0;
	private Integer mileage=0;
	private Double avgFuelCons=0.00;
	private java.sql.Timestamp createdTime;
	private Integer fuelConsGap=0;
	private Integer mileageGap=0;
	private Integer fuelConsMax=0;
	private Integer fuelConsMin=0;
	private Integer mileageMax=0;
	private Integer mileageMin=0;
	private String lasttimestatus;
	private Integer driveTime=0;
	private Integer idletime=0;
	private Integer enginetime=0;
	private Integer stoptime=0;
	private Double voltMax=0.00;
	private Double voltMin=0.00;
	private Double voltAvg=0.00;
	
	
	public TripDetailInfo() {
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOldId() {
		return oldId;
	}

	public void setOldId(Long oldId) {
		this.oldId = oldId;
	}

	public Long getTripId() {
		return tripId;
	}

	public void setTripId(Long tripId) {
		this.tripId = tripId;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public java.sql.Timestamp getTripStartTime() {
		return tripStartTime;
	}

	public void setTripStartTime(java.sql.Timestamp tripStartTime) {
		this.tripStartTime = tripStartTime;
	}

	public java.sql.Timestamp getTripEndTime() {
		return tripEndTime;
	}

	public void setTripEndTime(java.sql.Timestamp tripEndTime) {
		this.tripEndTime = tripEndTime;
	}

	public Integer getFuelCons() {
		return fuelCons;
	}

	public void setFuelCons(Integer fuelCons) {
		this.fuelCons = fuelCons;
	}

	public Integer getMileage() {
		return mileage;
	}

	public void setMileage(Integer mileage) {
		this.mileage = mileage;
	}

	public Double getAvgFuelCons() {
		return avgFuelCons;
	}

	public void setAvgFuelCons(Double avgFuelCons) {
		this.avgFuelCons = avgFuelCons;
	}

	public java.sql.Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(java.sql.Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getFuelConsGap() {
		return fuelConsGap;
	}

	public void setFuelConsGap(Integer fuelConsGap) {
		this.fuelConsGap = fuelConsGap;
	}

	public Integer getMileageGap() {
		return mileageGap;
	}

	public void setMileageGap(Integer mileageGap) {
		this.mileageGap = mileageGap;
	}

	public Integer getFuelConsMax() {
		return fuelConsMax;
	}

	public void setFuelConsMax(Integer fuelConsMax) {
		this.fuelConsMax = fuelConsMax;
	}

	public Integer getFuelConsMin() {
		return fuelConsMin;
	}

	public void setFuelConsMin(Integer fuelConsMin) {
		this.fuelConsMin = fuelConsMin;
	}

	public Integer getMileageMax() {
		return mileageMax;
	}

	public void setMileageMax(Integer mileageMax) {
		this.mileageMax = mileageMax;
	}

	public Integer getMileageMin() {
		return mileageMin;
	}

	public void setMileageMin(Integer mileageMin) {
		this.mileageMin = mileageMin;
	}

	public String getLasttimestatus() {
		return lasttimestatus;
	}

	public void setLasttimestatus(String lasttimestatus) {
		this.lasttimestatus = lasttimestatus;
	}

	public Integer getDriveTime() {
		return driveTime;
	}

	public void setDriveTime(Integer driveTime) {
		this.driveTime = driveTime;
	}

	public Integer getIdletime() {
		return idletime;
	}

	public void setIdletime(Integer idletime) {
		this.idletime = idletime;
	}

	public Integer getEnginetime() {
		return enginetime;
	}

	public void setEnginetime(Integer enginetime) {
		this.enginetime = enginetime;
	}

	public Integer getStoptime() {
		return stoptime;
	}

	public void setStoptime(Integer stoptime) {
		this.stoptime = stoptime;
	}

	public Double getVoltMax() {
		return voltMax;
	}

	public void setVoltMax(Double voltMax) {
		this.voltMax = voltMax;
	}

	public Double getVoltMin() {
		return voltMin;
	}

	public void setVoltMin(Double voltMin) {
		this.voltMin = voltMin;
	}

	public Double getVoltAvg() {
		return voltAvg;
	}

	public void setVoltAvg(Double voltAvg) {
		this.voltAvg = voltAvg;
	}
}
