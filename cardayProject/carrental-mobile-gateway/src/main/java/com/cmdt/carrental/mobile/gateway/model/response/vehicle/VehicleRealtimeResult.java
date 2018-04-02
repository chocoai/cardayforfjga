package com.cmdt.carrental.mobile.gateway.model.response.vehicle;

public class VehicleRealtimeResult {
	private String imeiNumber;
	private String snNumber;
	private String iccidNumber;
	private String deviceType;
	private String deviceVendor;
	private String bindDeviceVin;//绑定车架号
	private String bindTime;
	private String bindLicenseNumber;
	private String bindLicenseStatus;
	private String vehicleNumber;
	private String time;//设备最后上报时间
	private String driveStatus;
	private double longitude;
	private double latitude;
	private double speed;
	private String address;
	private Long mileage;
	
	public String getImeiNumber() {
		return imeiNumber;
	}
	public void setImeiNumber(String imeiNumber) {
		this.imeiNumber = imeiNumber;
	}
	public String getSnNumber() {
		return snNumber;
	}
	public void setSnNumber(String snNumber) {
		this.snNumber = snNumber;
	}
	public String getIccidNumber() {
		return iccidNumber;
	}
	public void setIccidNumber(String iccidNumber) {
		this.iccidNumber = iccidNumber;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceVendor() {
		return deviceVendor;
	}
	public void setDeviceVendor(String deviceVendor) {
		this.deviceVendor = deviceVendor;
	}
	public String getBindDeviceVin() {
		return bindDeviceVin;
	}
	public void setBindDeviceVin(String bindDeviceVin) {
		this.bindDeviceVin = bindDeviceVin;
	}
	public String getBindTime() {
		return bindTime;
	}
	public void setBindTime(String bindTime) {
		this.bindTime = bindTime;
	}
	public String getBindLicenseNumber() {
		return bindLicenseNumber;
	}
	public void setBindLicenseNumber(String bindLicenseNumber) {
		this.bindLicenseNumber = bindLicenseNumber;
	}
	public String getBindLicenseStatus() {
		return bindLicenseStatus;
	}
	public void setBindLicenseStatus(String bindLicenseStatus) {
		this.bindLicenseStatus = bindLicenseStatus;
	}
	public Long getMileage() {
		return mileage;
	}
	public void setMileage(Long mileage) {
		this.mileage = mileage;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDriveStatus() {
		return driveStatus;
	}
	public void setDriveStatus(String driveStatus) {
		this.driveStatus = driveStatus;
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
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

}
