package com.cmdt.carday.microservice.model.request.device;

public class DeviceListDto {
	private Long userId;
	private String snNumber;
	private String imeiNumber;
	private String deviceVendor;
	private String iccidNumber;
	private String vehicleNumber;
	private Long deviceStatus; //设备状态   1：正常   2：未配置   3： 故障
	private int currentPage;
	private int numPerPage;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public String getDeviceVendor() {
		return deviceVendor;
	}
	public void setDeviceVendor(String deviceVendor) {
		this.deviceVendor = deviceVendor;
	}
	public String getIccidNumber() {
		return iccidNumber;
	}
	public void setIccidNumber(String iccidNumber) {
		this.iccidNumber = iccidNumber;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public Long getDeviceStatus() {
		return deviceStatus;
	}
	public void setDeviceStatus(Long deviceStatus) {
		this.deviceStatus = deviceStatus;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getNumPerPage() {
		return numPerPage;
	}
	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}
}
