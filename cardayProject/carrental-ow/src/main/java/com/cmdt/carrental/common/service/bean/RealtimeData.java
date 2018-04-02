package com.cmdt.carrental.common.service.bean;

import java.util.Date;

public class RealtimeData implements java.io.Serializable
{
    
    private static final long serialVersionUID = -8011688407253968703L;
    
    private Long id;
    
    private Long oldId;
    
    private String imei;
    
    private String vin;
    
    private Date traceTime;
    
    private Boolean isSet = false;
    
    private Integer satelliteNum = 0;
    
    private Double longitude = 0.0;
    
    private Double latitude = 0.0;
    
    private Integer altitude = 0;
    
    private Integer bearing = 0;
    
    private Double hdop = 0.0;
    
    private Double vdop = 0.0;
    
    private Long fuelCons = 0l;
    
    private Long mileage = 0l;
    
    private Integer speed = 0;
    
    private Integer rpm = 0;
    
    private Integer temperature = 0;
    
    private String status;
    
    private Double extVol = 0.0;
    
    private Double intVol = 0.0;
    
    private Long engineTime = 0l;
    
    private String deviceName;
    
    private String fwVer;
    
    private String hwVer;
    
    private String dtcs;
    
    private Date createTime;
    
    private String uploadCode;
    
    /**
     * 以下针对DBJ里面的状态值添加的数据
     */
    /**
     * 充电状态
     */
    private Integer chargingStatus;
    
    /**
     * 电量
     */
    private Integer electricity;
    
    /**
     * GSM信号强度
     */
    private Integer gsmPower;
    
    /**
     * 信噪比
     */
    private Integer snr;
    
    public RealtimeData()
    {
        
    }
    
    public Long getId()
    {
        return this.id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public Long getOldId() {
		return oldId;
	}

	public void setOldId(Long oldId) {
		this.oldId = oldId;
	}

	public String getImei()
    {
        return this.imei;
    }
    
    public void setImei(String imei)
    {
        this.imei = imei;
    }
    
    public String getVin()
    {
        return this.vin;
    }
    
    public void setVin(String vin)
    {
        this.vin = vin;
    }
    
    public Date getTraceTime()
    {
        return this.traceTime;
    }
    
    public void setTraceTime(Date traceTime)
    {
        this.traceTime = traceTime;
    }
    
    public Boolean getIsSet()
    {
        return this.isSet;
    }
    
    public void setIsSet(Boolean isSet)
    {
        this.isSet = isSet;
    }
    
    public Integer getSatelliteNum()
    {
        return this.satelliteNum;
    }
    
    public void setSatelliteNum(Integer satelliteNum)
    {
        this.satelliteNum = satelliteNum;
    }
    
    public Double getLongitude()
    {
        return this.longitude;
    }
    
    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }
    
    public Double getLatitude()
    {
        return this.latitude;
    }
    
    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }
    
    public Integer getAltitude()
    {
        return this.altitude;
    }
    
    public void setAltitude(Integer altitude)
    {
        this.altitude = altitude;
    }
    
    public Integer getBearing()
    {
        return this.bearing;
    }
    
    public void setBearing(Integer bearing)
    {
        this.bearing = bearing;
    }
    
    public Double getHdop()
    {
        return this.hdop;
    }
    
    public void setHdop(Double hdop)
    {
        this.hdop = hdop;
    }
    
    public Double getVdop()
    {
        return this.vdop;
    }
    
    public void setVdop(Double vdop)
    {
        this.vdop = vdop;
    }
    
    public Long getFuelCons()
    {
        return this.fuelCons;
    }
    
    public void setFuelCons(Long fuelCons)
    {
        this.fuelCons = fuelCons;
    }
    
    public Long getMileage()
    {
        return this.mileage;
    }
    
    public void setMileage(Long mileage)
    {
        this.mileage = mileage;
    }
    
    public Integer getSpeed()
    {
        return this.speed;
    }
    
    public void setSpeed(Integer speed)
    {
        this.speed = speed;
    }
    
    public Integer getRpm()
    {
        if (null != this.rpm)
        {
            return this.rpm;
        }
        return 0;
    }
    
    public void setRpm(Integer rpm)
    {
        this.rpm = rpm;
    }
    
    public Integer getTemperature()
    {
        return this.temperature;
    }
    
    public void setTemperature(Integer temperature)
    {
        this.temperature = temperature;
    }
    
    public String getStatus()
    {
        return this.status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public Double getExtVol()
    {
        return extVol;
    }
    
    public void setExtVol(Double extVol)
    {
        this.extVol = extVol;
    }
    
    public Double getIntVol()
    {
        return intVol;
    }
    
    public void setIntVol(Double intVol)
    {
        this.intVol = intVol;
    }
    
    public Long getEngineTime()
    {
        if (null != engineTime)
        {
            return engineTime;
        }
        return 0l;
    }
    
    public void setEngineTime(Long engineTime)
    {
        this.engineTime = engineTime;
    }
    
    public String getDeviceName()
    {
        return deviceName;
    }
    
    public void setDeviceName(String deviceName)
    {
        this.deviceName = deviceName;
    }
    
    public String getFwVer()
    {
        return fwVer;
    }
    
    public void setFwVer(String fwVer)
    {
        this.fwVer = fwVer;
    }
    
    public String getHwVer()
    {
        return hwVer;
    }
    
    public void setHwVer(String hwVer)
    {
        this.hwVer = hwVer;
    }
    
    public String getDtcs()
    {
        return dtcs;
    }
    
    public void setDtcs(String dtcs)
    {
        this.dtcs = dtcs;
    }
    
    public Date getCreateTime()
    {
        return createTime;
    }
    
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
    
    public String getUploadCode()
    {
        return uploadCode;
    }
    
    public void setUploadCode(String uploadCode)
    {
        this.uploadCode = uploadCode;
    }
    
    public Integer getChargingStatus()
    {
        return chargingStatus;
    }
    
    public void setChargingStatus(Integer chargingStatus)
    {
        this.chargingStatus = chargingStatus;
    }
    
    public Integer getElectricity()
    {
        return electricity;
    }
    
    public void setElectricity(Integer electricity)
    {
        this.electricity = electricity;
    }
    
    public Integer getGsmPower()
    {
        return gsmPower;
    }
    
    public void setGsmPower(Integer gsmPower)
    {
        this.gsmPower = gsmPower;
    }
    
    public Integer getSnr()
    {
        return snr;
    }
    
    public void setSnr(Integer snr)
    {
        this.snr = snr;
    }
    
    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }
    
}
