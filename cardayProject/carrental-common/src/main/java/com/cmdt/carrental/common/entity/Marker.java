package com.cmdt.carrental.common.entity;

import java.io.Serializable;

public class Marker implements Serializable {
	
	private static final long serialVersionUID = 5760869301006119800L;
	private Long id; //编号
    private Long organizationId; //所属公司
    private String markerName; //站点名
    private String province;//省
    private String city; //城市
    private Long cityId;
    private String position; //位置
    private String type; //绘制类型 ：0：行政划分 1：手动绘制
    private String pattern; //多边形点集合
    private String assignedVehicleNumber;//已分配车辆数
    private String longitude; //经度
    private String latitude; //纬度
    private Long provinceId;
    private Long regionId;
    
    private String radius; //半径
    
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
	public String getMarkerName() {
		return markerName;
	}
	public void setMarkerName(String markerName) {
		this.markerName = markerName;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getAssignedVehicleNumber() {
		return assignedVehicleNumber;
	}
	public void setAssignedVehicleNumber(String assignedVehicleNumber) {
		this.assignedVehicleNumber = assignedVehicleNumber;
	}
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
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public Long getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
	public Long getRegionId() {
		return regionId;
	}
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public String getRadius() {
		return radius;
	}
	public void setRadius(String radius) {
		this.radius = radius;
	}
	
}
