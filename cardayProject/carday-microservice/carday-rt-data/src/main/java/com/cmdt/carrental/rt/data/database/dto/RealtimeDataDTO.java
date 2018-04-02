package com.cmdt.carrental.rt.data.database.dto;

import java.util.Date;

import javax.validation.constraints.Pattern;

import com.cmdt.carrental.rt.data.service.common.Patterns;


public class RealtimeDataDTO {

    private String imei;
    private String vin;
    
    @Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS, message = "planTime format error!")
    private String traceTime;
    
    private Date createTime;
    private Date updateTime;
    private Boolean isSet = false;
    private Integer satelliteNum;
    private Double longitude;
    private Double latitude;
    private Integer altitude; 
    private Integer bearing;
    private Double hdop;
    private Double vdop;
    private Long fuelCons;
    private Long mileage;
    private Integer speed;
    private Integer rpm;
    private Integer temperature;
    private String status;
    private Double extVol;
    private Double intVol;
    private Long engineTime;
    private String deviceName;
    private String fwVer;
    private String hwVer;
    private String dtcs;
    private String uploadCode;
    private Long factFuel;
	private Long factMileage;
    
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
	public String getTraceTime() {
		return traceTime;
	}
	public void setTraceTime(String traceTime) {
		this.traceTime = traceTime;
	}
	public Boolean getIsSet() {
		return isSet;
	}
	public void setIsSet(Boolean isSet) {
		this.isSet = isSet;
	}
	public Integer getSatelliteNum() {
		return satelliteNum;
	}
	public void setSatelliteNum(Integer satelliteNum) {
		this.satelliteNum = satelliteNum;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Integer getAltitude() {
		return altitude;
	}
	public void setAltitude(Integer altitude) {
		this.altitude = altitude;
	}
	public Integer getBearing() {
		return bearing;
	}
	public void setBearing(Integer bearing) {
		this.bearing = bearing;
	}
	public Double getHdop() {
		return hdop;
	}
	public void setHdop(Double hdop) {
		this.hdop = hdop;
	}
	public Double getVdop() {
		return vdop;
	}
	public void setVdop(Double vdop) {
		this.vdop = vdop;
	}
	public Long getFuelCons() {
		return fuelCons;
	}
	public void setFuelCons(Long fuelCons) {
		this.fuelCons = fuelCons;
	}
	public Long getMileage() {
		return mileage;
	}
	public void setMileage(Long mileage) {
		this.mileage = mileage;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public Long getEngineTime() {
		return engineTime;
	}
	public void setEngineTime(Long engineTime) {
		this.engineTime = engineTime;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getFwVer() {
		return fwVer;
	}
	public void setFwVer(String fwVer) {
		this.fwVer = fwVer;
	}
	public String getHwVer() {
		return hwVer;
	}
	public void setHwVer(String hwVer) {
		this.hwVer = hwVer;
	}
	public String getDtcs() {
		return dtcs;
	}
	public void setDtcs(String dtcs) {
		this.dtcs = dtcs;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUploadCode() {
		return uploadCode;
	}
	public void setUploadCode(String uploadCode) {
		this.uploadCode = uploadCode;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getFactFuel() {
		return factFuel;
	}
	public void setFactFuel(Long factFuel) {
		this.factFuel = factFuel;
	}
	public Long getFactMileage() {
		return factMileage;
	}
	public void setFactMileage(Long factMileage) {
		this.factMileage = factMileage;
	}
    
}
