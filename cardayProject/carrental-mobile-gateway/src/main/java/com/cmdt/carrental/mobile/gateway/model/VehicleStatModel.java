package com.cmdt.carrental.mobile.gateway.model;

public class VehicleStatModel {
	private String totalCar;
	private String ownCar;
	private String onlineCar;
	private String drivingCar;
	private String stopCar;
	public String getTotalCar() {
		return totalCar;
	}
	public void setTotalCar(String totalCar) {
		this.totalCar = totalCar;
	}
	public String getOwnCar() {
		return ownCar;
	}
	public void setOwnCar(String ownCar) {
		this.ownCar = ownCar;
	}
	public String getOnlineCar() {
		return onlineCar;
	}
	public void setOnlineCar(String onlineCar) {
		this.onlineCar = onlineCar;
	}
	public String getDrivingCar() {
		return drivingCar;
	}
	public void setDrivingCar(String drivingCar) {
		this.drivingCar = drivingCar;
	}
	public String getStopCar() {
		return stopCar;
	}
	public void setStopCar(String stopCar) {
		this.stopCar = stopCar;
	}
	@Override
	public String toString() {
		return "VehicleStatModel [totalCar=" + totalCar + ", ownCar=" + ownCar + ", onlineCar=" + onlineCar
				+ ", drivingCar=" + drivingCar + ", stopCar=" + stopCar + "]";
	}
	
}
