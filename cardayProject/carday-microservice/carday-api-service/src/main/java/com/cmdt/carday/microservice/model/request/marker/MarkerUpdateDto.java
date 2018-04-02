package com.cmdt.carday.microservice.model.request.marker;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
public class MarkerUpdateDto {
	
	@ApiModelProperty(value="登录用户ID",required=true)
	@NotNull(message="userId不为空")
	private Long userId;
	
	@ApiModelProperty(value="地理围栏ID",required=true)
	@NotNull(message="id不为空")
	private Long id;
	
	@ApiModelProperty(value="所选城市ID")
	private Long regionId;
	
	@ApiModelProperty(value="地理围栏名称")
    private String markerName;
    
	@ApiModelProperty(value="城市名称")
    private String city;
    
	@ApiModelProperty(value="地理围栏地址")
    private String position;
    
	@ApiModelProperty(value="绘制类型：0: 行政区划分， 1：自主绘制")
    private String type;
	
	@ApiModelProperty(value="绘制地理围栏所得的地图的点集合，例如：[[{'lng':114.338467,'lat':30.543086},{'lng':114.366997,'lat':30.536928},{'lng':114.353199,'lat':30.528964},{'lng':114.341198,'lat':30.529898},{'lng':114.340839,'lat':30.52996},{'lng':114.332431,'lat':30.52996},{'lng':114.338467,'lat':30.543086}]]!")
    private String pattern;
    
	@ApiModelProperty(value="经度")
    private String longitude;
    
	@ApiModelProperty(value="纬度")
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
