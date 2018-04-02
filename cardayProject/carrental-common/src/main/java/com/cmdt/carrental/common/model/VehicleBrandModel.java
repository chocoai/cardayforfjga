package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class VehicleBrandModel implements Serializable {

	private static final long serialVersionUID = 9158425127007220846L;

	private Long id;
	
	private String vehicleModel;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

}
