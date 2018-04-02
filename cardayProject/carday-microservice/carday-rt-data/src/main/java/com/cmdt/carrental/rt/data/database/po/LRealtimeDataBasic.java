package com.cmdt.carrental.rt.data.database.po;

import java.util.Date;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(keyspace = "crt_tsp", name = "latest_rt_data",
caseSensitiveKeyspace = false,
caseSensitiveTable = false)
public class LRealtimeDataBasic {
	
	@PartitionKey(0)
	private String imei;
	
    private String vin;
    
    //private Date trace_time;
  	@PartitionKey(1)
  	@Column(name = "trace_time")
  	private Date traceTime;
  	
  	@Column(name = "is_set")
    private Boolean isSet;
  	
  	@Column(name = "satellite_num")
    private Integer satelliteNum;
  	
    private Double longitude;
    private Double latitude;
    private Integer altitude; 
    private Integer bearing;
    private Double hdop;
    private Double vdop;
    
    @Column(name = "fuel_cons")
    private Long fuelCons;
    private Long mileage;
    private Integer speed;
    private Integer rpm;
    private Integer temperature;
    private String status;
    
    @Column(name = "ext_vol")
    private Double extVol;
    
    @Column(name = "int_vol")
    private Double intVol;
    
    @Column(name = "engine_time")
    private Long engineTime;
    
    @Column(name = "device_name")
    private String deviceName;
    
    @Column(name = "fw_ver")
    private String fwVer;
    
    @Column(name = "hw_ver")
    private String hwVer;
    
    private String dtcs;
    
    @Column(name = "fact_fuel")
	private Long factFuel;
    
    @Column(name = "fact_mileage")
	private Long factMileage;
    
    @Column(name = "update_time")
    private Date updateTime;

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

	public Date getTraceTime() {
		return traceTime;
	}

	public void setTraceTime(Date traceTime) {
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

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
