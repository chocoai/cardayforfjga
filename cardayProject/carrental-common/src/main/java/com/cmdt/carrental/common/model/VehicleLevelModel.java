package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class VehicleLevelModel implements Serializable{
	
	private static final long serialVersionUID = 6084746085173510668L;
	private Long vehicleId;                               //车辆id
    private String vehicleNumber;                         //车牌号
    private String deviceNumber;		                 //设备Imei号
	private String assignedFlag = "0";                   //"0":车辆在企业节点(在企业节点的车就是未分配的车辆)  "1":车辆在部门节点
	private String entName = "";                         //assignedFlag为"0"或"1"则有值
	private String deptName = "";                       //assignedFlag为"1"则有值
	
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
	public String getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public String getAssignedFlag() {
		return assignedFlag;
	}
	public void setAssignedFlag(String assignedFlag) {
		this.assignedFlag = assignedFlag;
	}
	public String getEntName() {
		return entName;
	}
	public void setEntName(String entName) {
		this.entName = entName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
}
