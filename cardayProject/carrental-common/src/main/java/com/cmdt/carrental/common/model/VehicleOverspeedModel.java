package com.cmdt.carrental.common.model;

import com.cmdt.carrental.common.model.VehicleModel;

public class VehicleOverspeedModel {
	private String deviceNumber;
	private String vehicleNumber;
	private VehicleModel vehicleModel;
	
	public String getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public VehicleModel getVehicleModel() {
		return vehicleModel;
	}
	public void setVehicleModel(VehicleModel vehicleModel) {
		this.vehicleModel = vehicleModel;
	}
	
	@Override
	public String toString(){
		return deviceNumber+" "+vehicleNumber;
	}
	
}
