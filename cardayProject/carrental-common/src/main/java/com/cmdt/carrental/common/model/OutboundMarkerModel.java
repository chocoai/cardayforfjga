package com.cmdt.carrental.common.model;

import java.util.List;

import com.cmdt.carrental.common.model.MarkerModel;

public class OutboundMarkerModel{

	private String deviceNumber;
	private String vehicleNumber;
	private String startracetime;
	private String endtracetime;
	private List<MarkerModel> markers;
	
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
	public String getStartracetime() {
		return startracetime;
	}
	public void setStartracetime(String startracetime) {
		this.startracetime = startracetime;
	}
	public String getEndtracetime() {
		return endtracetime;
	}
	public void setEndtracetime(String endtracetime) {
		this.endtracetime = endtracetime;
	}
	public List<MarkerModel> getMarkers() {
		return markers;
	}
	public void setMarkers(List<MarkerModel> markers) {
		this.markers = markers;
	}
	
}
