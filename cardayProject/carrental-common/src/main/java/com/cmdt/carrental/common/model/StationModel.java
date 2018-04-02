package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class StationModel implements Serializable {
	
	private static final long serialVersionUID = 3021666963271140132L;
	private Long id; //编号
    private Long organizationId; //所属公司
    private Long markerId; //Marker
    private String stationName; //站点名
    private String city; //城市
    private String position; //位置
    private String longitude; //经度
    private String latitude; //纬度
    private String radius; //半径
    private String assignedVehicleNumber;//已分配车辆数
    private String carNumber;//停车位数
    private String startTime;//运营开始时间
    private String endTime;//运营结束时间
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public Long getMarkerId() {
		return markerId;
	}
	public void setMarkerId(Long markerId) {
		this.markerId = markerId;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getRadius() {
		return radius;
	}
	public void setRadius(String radius) {
		this.radius = radius;
	}
	public String getAssignedVehicleNumber() {
		return assignedVehicleNumber;
	}
	public void setAssignedVehicleNumber(String assignedVehicleNumber) {
		this.assignedVehicleNumber = assignedVehicleNumber;
	}
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

}
