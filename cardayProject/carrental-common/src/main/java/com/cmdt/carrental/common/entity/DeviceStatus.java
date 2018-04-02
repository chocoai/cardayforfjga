package com.cmdt.carrental.common.entity;

import java.io.Serializable;
import java.sql.Date;

import org.apache.commons.lang3.StringUtils;

import com.cmdt.carrental.common.constants.Constants;

public class DeviceStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String deviceVendor;//商家(DBJ或其他)
	private String imeiNumber;//非DBJ就当这个为imei
	private String iccidNumber;//DBJ使用这个为imei
	private String snNumber; //斗海使用sn
	
	private String actualImei;//实际的imei
	private String licenseNumber;//licese号
	private String licenseStatus;//license状态
	
	public String getImeiNumber() {
		return imeiNumber;
	}
	public void setImeiNumber(String imeiNumber) {
		this.imeiNumber = imeiNumber;
	}
	public String getIccidNumber() {
		return iccidNumber;
	}
	public void setIccidNumber(String iccidNumber) {
		this.iccidNumber = iccidNumber;
	}
	public String getDeviceVendor() {
		return deviceVendor;
	}
	public void setDeviceVendor(String deviceVendor) {
		this.deviceVendor = deviceVendor;
	}
	
	public String getActualImei(){
		if(StringUtils.isEmpty(deviceVendor)){
			return "";
		}
		//return iccid for DBJ
		//return sn for dh
		//return imei for others
		if(Constants.DEVICE_VENFOR_DBJ.equals(deviceVendor)){
			if(StringUtils.isEmpty(iccidNumber)){
				return "";
			}
			return iccidNumber;
		}else if(Constants.DEVICE_VENFOR_DH.equals(deviceVendor)){
            if(StringUtils.isEmpty(snNumber)){
                return "";
            }
            return snNumber;
        }else{
			if(StringUtils.isEmpty(imeiNumber)){
				return "";
			}
			return imeiNumber;
		}
	}
	public String getLicenseStatus() {
		return licenseStatus;
	}
	public void setLicenseStatus(String licenseStatus) {
		this.licenseStatus = licenseStatus;
	}
	public String getLicenseNumber() {
		return licenseNumber;
	}
	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
    public String getSnNumber()
    {
        return snNumber;
    }
    public void setSnNumber(String snNumber)
    {
        this.snNumber = snNumber;
    }

}
