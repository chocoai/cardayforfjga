package com.cmdt.carrental.platform.service.model.request.station;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class StationUpdateDto {
	
	@NotNull(message="id不能为空")
	private Long id;
	
	@NotNull(message="userId不能为空")
	private Long userId;
	
	private String stationName; //站点名
	
	@Digits(message="carNumber只能为数字",fraction=0,integer=Integer.MAX_VALUE)
	private String carNumber;//停车位数
	
	private String city; //城市
	
	private String position; //位置
	
	@Digits(message="longitude只能为数字",fraction=6,integer=Integer.MAX_VALUE)
    private String longitude; //经度
    
    @Digits(message="latitude只能为数字",fraction=6,integer=Integer.MAX_VALUE)
    private String latitude; //纬度
    
    private String radius; //半径
    
    private Long areaId;
    
    private Long organizationId; //所属公司

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getCarNumber() {
		return carNumber;
	}

	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
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

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    
}