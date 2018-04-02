package com.cmdt.carrental.platform.service.model.request.device;

import java.sql.Date;

import org.apache.commons.lang3.StringUtils;

public class DeviceCreateDto {
	private Long id; // 编号
	private Long uerId; //用户id
	private String snNumber;
	private String imeiNumber;
	private String deviceType;
	private String deviceModel;
	private String deviceVendorNumber;
	private String deviceVendor;
	private String firmwareVersion;
	private String softwareVersion;
	private Date maintainExpireTime;
	private Date purchaseTime;
	private String iccidNumber;
	private String simNumber;
	private Long vehicleId;
	private String vehicleNumber;
	private Long rentId;					 //租户id(该车由哪个租户创建)
    private String rentName;                 //租户名称(该车由哪个租户创建)
    private Long entId;						 //企业id(该车由哪个企业创建)
    private String entName;                  //企业名称(该车由哪个企业创建)
	private String vehicleSource;
	private String vehicleIdentification;
	private Long deviceStatus; //设备状态   1：正常   2：未配置   3： 故障
	private String deviceBatch;
	private Float limitSpeed;
	private Float latestLimitSpeed;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUerId() {
		return uerId;
	}
	public void setUerId(Long uerId) {
		this.uerId = uerId;
	}
	public String getSnNumber() {
		return snNumber;
	}
	public void setSnNumber(String snNumber) {
		this.snNumber = snNumber;
	}
	public String getImeiNumber() {
		return imeiNumber;
	}
	public void setImeiNumber(String imeiNumber) {
		this.imeiNumber = imeiNumber;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	public String getDeviceVendorNumber() {
		return deviceVendorNumber;
	}
	public void setDeviceVendorNumber(String deviceVendorNumber) {
		this.deviceVendorNumber = deviceVendorNumber;
	}
	public String getDeviceVendor() {
		return deviceVendor;
	}
	public void setDeviceVendor(String deviceVendor) {
		this.deviceVendor = deviceVendor;
	}
	public String getFirmwareVersion() {
		return firmwareVersion;
	}
	public void setFirmwareVersion(String firmwareVersion) {
		this.firmwareVersion = firmwareVersion;
	}
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	public Date getMaintainExpireTime() {
		return maintainExpireTime;
	}
	public void setMaintainExpireTime(Date maintainExpireTime) {
		this.maintainExpireTime = maintainExpireTime;
	}
	public Date getPurchaseTime() {
		return purchaseTime;
	}
	public void setPurchaseTime(Date purchaseTime) {
		this.purchaseTime = purchaseTime;
	}
	public String getIccidNumber() {
		return iccidNumber;
	}
	public void setIccidNumber(String iccidNumber) {
		this.iccidNumber = iccidNumber;
	}
	public String getSimNumber() {
		return simNumber;
	}
	public void setSimNumber(String simNumber) {
		this.simNumber = simNumber;
	}
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public Long getRentId() {
		return rentId;
	}
	public void setRentId(Long rentId) {
		this.rentId = rentId;
	}
	public String getRentName() {
		return rentName;
	}
	public void setRentName(String rentName) {
		this.rentName = rentName;
	}
	public Long getEntId() {
		return entId;
	}
	public void setEntId(Long entId) {
		this.entId = entId;
	}
	public String getEntName() {
		return entName;
	}
	public void setEntName(String entName) {
		this.entName = entName;
	}
	public String getVehicleSource() {
		if(StringUtils.isNoneEmpty(this.rentName)){
			return this.vehicleSource = this.rentName;
		}else if(StringUtils.isNoneEmpty(this.entName)){
			return this.vehicleSource = this.entName;
		}else
			return this.vehicleSource;
	}
	public void setVehicleSource(String vehicleSource) {
		this.vehicleSource = vehicleSource;
	}
	public String getVehicleIdentification() {
		return vehicleIdentification;
	}
	public void setVehicleIdentification(String vehicleIdentification) {
		this.vehicleIdentification = vehicleIdentification;
	}
	public Long getDeviceStatus() {
		return deviceStatus;
	}
	public void setDeviceStatus(Long deviceStatus) {
		this.deviceStatus = deviceStatus;
	}
	public String getDeviceBatch() {
		return deviceBatch;
	}
	public void setDeviceBatch(String deviceBatch) {
		this.deviceBatch = deviceBatch;
	}
	public Float getLimitSpeed() {
		return limitSpeed;
	}
	public void setLimitSpeed(Float limitSpeed) {
		this.limitSpeed = limitSpeed;
	}
	public Float getLatestLimitSpeed() {
		return latestLimitSpeed;
	}
	public void setLatestLimitSpeed(Float latestLimitSpeed) {
		this.latestLimitSpeed = latestLimitSpeed;
	}
}
