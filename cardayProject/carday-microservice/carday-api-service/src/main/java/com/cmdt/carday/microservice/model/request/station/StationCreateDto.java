package com.cmdt.carday.microservice.model.request.station;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class StationCreateDto {
	
	@ApiModelProperty(value="登录用户ID",required=true)
	@NotNull(message="userId不能为空")
	private Long userId;
	
	@ApiModelProperty(value="站点名称",required=true)
	@NotNull(message="stationName不能为空")
	private String stationName; //站点名
	
	@ApiModelProperty(value="停车位数")
	@Digits(message="carNumber只能为数字",fraction=0,integer=Integer.MAX_VALUE)
	private String carNumber;//停车位数
	
	@ApiModelProperty(value="城市",required=true)
	@NotNull(message="city不能为空")
	private String city; //城市
	
	@ApiModelProperty(value="位置",required=true)
	@NotNull(message="position不能为空")
	private String position; //位置
	
	@ApiModelProperty(value="经度",required=true)
	@NotNull(message="longitude不能为空")
    private String longitude; //经度
    
	@ApiModelProperty(value="纬度",required=true)
    @NotNull(message="latitude不能为空")
    private String latitude; //纬度
    
	@ApiModelProperty(value="半径",required=true)
    @NotNull(message="radius不能为空")
    private String radius; //半径
    
	@ApiModelProperty(value="所在区ID",required=true)
    @NotNull(message="areaId不能为空")
    private Long areaId;

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
}