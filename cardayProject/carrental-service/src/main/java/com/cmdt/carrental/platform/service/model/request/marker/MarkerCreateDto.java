package com.cmdt.carrental.platform.service.model.request.marker;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MarkerCreateDto {
	
	@NotNull(message="userId不为空")
	private Long userId;
	
	@NotNull(message="regionId不为空")
	@Digits(message="regionId只能为数字",fraction=0,integer=Integer.MAX_VALUE)
	private Long regionId;
	
	@NotNull(message="markerName不为空")
    private String markerName;
	
	@NotNull(message="city不为空")
    private String city;
	
	@NotNull(message="position不为空")
    private String position;
	
	@NotNull(message="type不为空")
	@Digits(message="type只能为数字",fraction=0,integer=Integer.MAX_VALUE)
    private String type;  //0: 行政区划分， 1：自主绘制
	
	@NotNull(message="pattern不为空")
    private String pattern;
	
	@NotNull(message="longitude不为空")
	@Digits(message="longitude只能为数字",fraction=6,integer=Integer.MAX_VALUE)
    private String longitude;
	
	@NotNull(message="latitude不为空")
	@Digits(message="latitude只能为数字",fraction=6,integer=Integer.MAX_VALUE)
    private String latitude;
    
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getRegionId() {
		return regionId;
	}
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
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
