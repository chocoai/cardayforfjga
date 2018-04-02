package com.cmdt.carday.microservice.model.request.device;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import com.cmdt.carrental.common.util.Patterns;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceCreateDto {
	private Long uerId; //用户id
	
	@NotNull(message = "snNumber can not be empty!")
    @ApiModelProperty("SN设备号")
	private String snNumber;
	
	@NotNull(message = "imeiNumber can not be empty!")
    @ApiModelProperty("设备IMEI号")
	private String imeiNumber;
	
	@NotNull(message = "deviceType can not be empty!")
    @ApiModelProperty("设备类型")
	private String deviceType;
	
	@NotNull(message = "deviceModel can not be empty!")
    @ApiModelProperty("设备型号")
	private String deviceModel;
	
	@NotNull(message = "deviceVendorNumber can not be empty!")
    @ApiModelProperty("设备供应商编号")
	private String deviceVendorNumber;
	
	@NotNull(message = "deviceVendor can not be empty!")
    @ApiModelProperty("设备厂家")
	private String deviceVendor;
	
	@NotNull(message = "firmwareVersion can not be empty!")
    @ApiModelProperty("固件版本")
	private String firmwareVersion;
	
	private String softwareVersion;
	
	@NotNull(message = "maintainExpireTime can not be empty!")
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD, message = "maintainExpireTime format error!")
    @ApiModelProperty("保修到期日")
	private String maintainExpireTime;
	
	@NotNull(message = "purchaseTime can not be empty!")
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD, message = "purchaseTime format error!")
    @ApiModelProperty("采购时间")
	private String purchaseTime;
	
	@NotNull(message = "iccidNumber can not be empty!")
    @ApiModelProperty("ICCID编号")
	private String iccidNumber;
	
    @ApiModelProperty("SIM卡号")
	private String simNumber;
	
	private Long vehicleId;
	
	@NotNull(message = "deviceStatus can not be empty!")
    @ApiModelProperty("设备状态")
	private Long deviceStatus; //设备状态   1：正常   2：未配置   3： 故障
	
	@NotNull(message = "deviceBatch can not be empty!")
    @ApiModelProperty("设备批次")
	private String deviceBatch;

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
	public String getMaintainExpireTime() {
		return maintainExpireTime;
	}
	public void setMaintainExpireTime(String maintainExpireTime) {
		this.maintainExpireTime = maintainExpireTime;
	}
	public String getPurchaseTime() {
		return purchaseTime;
	}
	public void setPurchaseTime(String purchaseTime) {
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
}
