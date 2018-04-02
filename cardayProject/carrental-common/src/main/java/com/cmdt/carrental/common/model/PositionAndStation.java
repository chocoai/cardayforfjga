package com.cmdt.carrental.common.model;

import java.util.List;

public class PositionAndStation {
	private String latitude;
	private String longitude;
	private List<StationModel> stationModels;
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public List<StationModel> getStationModels() {
		return stationModels;
	}
	public void setStationModels(List<StationModel> stationModels) {
		this.stationModels = stationModels;
	}
}
