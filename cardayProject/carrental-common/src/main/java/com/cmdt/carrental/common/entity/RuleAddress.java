package com.cmdt.carrental.common.entity;

import java.io.Serializable;

public class RuleAddress implements Serializable {
	
	private static final long serialVersionUID = 5760869301006119800L;
	private Long id; //编号
    private Long organizationId; //所属公司
    private String locationName; //站点名
    private String city; //城市
    private String position; //位置
    private String longitude; //经度
    private String latitude; //纬度
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
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String positionName) {
		this.locationName = positionName;
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
