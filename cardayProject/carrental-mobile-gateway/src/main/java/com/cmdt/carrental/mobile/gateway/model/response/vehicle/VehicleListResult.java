package com.cmdt.carrental.mobile.gateway.model.response.vehicle;

public class VehicleListResult {
	
	private Long carId;
	
	private String carNum;
	
	private String type;
	
	private String brand;
	
	private String carForm;
	
	private String department;
	
	private String drivieStatus;

	public Long getCarId() {
		return carId;
	}

	public void setCarId(Long carId) {
		this.carId = carId;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getCarForm() {
		return carForm;
	}

	public void setCarForm(String carForm) {
		this.carForm = carForm;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDrivieStatus() {
		return drivieStatus;
	}

	public void setDrivieStatus(String drivieStatus) {
		this.drivieStatus = drivieStatus;
	}
	
}
