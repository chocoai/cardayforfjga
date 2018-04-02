package com.cmdt.carrental.platform.service.model.request.marker;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MarkerUpdateDto {
	
	@NotNull(message="userId不为空")
	private Long userId;
	
	@NotNull(message="id不为空")
	private Long id;
	
	@Digits(message="regionId只能为数字",fraction=0,integer=Integer.MAX_VALUE)
	private Long regionId;
	
    private String markerName;
    
    private String city;
    
    private String position;
    
    @Digits(message="regionId只能为数字",fraction=0,integer=Integer.MAX_VALUE)
    private String type;
    private String pattern;
    
    @Digits(message="longitude只能为数字",fraction=6,integer=Integer.MAX_VALUE)
    private String longitude;
    
    @Digits(message="latitude只能为数字",fraction=6,integer=Integer.MAX_VALUE)
    private String latitude;
    
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
