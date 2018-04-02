package com.cmdt.carday.microservice.model.request.station;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class StationUpdateDto {
	
	@ApiModelProperty(value="站点ID",required=true)
	@NotNull(message="id不能为空")
	private Long id;
	
	@ApiModelProperty(value="登录用户ID",required=true)
	@NotNull(message="userId不能为空")
	private Long userId;
	
	@ApiModelProperty(value="站点名称")
	private String stationName; //站点名
	
	@ApiModelProperty(value="停车位数")
	@Digits(message="carNumber只能为数字",fraction=0,integer=Integer.MAX_VALUE)
	private String carNumber;//停车位数
	
	@ApiModelProperty(value="城市")
	private String city; //城市
	
	@ApiModelProperty(value="位置")
	private String position; //位置
	
	@ApiModelProperty(value="经度")
	@Digits(message="longitude只能为数字",fraction=6,integer=Integer.MAX_VALUE)
    private String longitude; //经度
    
	@ApiModelProperty(value="纬度")
    @Digits(message="latitude只能为数字",fraction=6,integer=Integer.MAX_VALUE)
    private String latitude; //纬度
    
	@ApiModelProperty(value="半径")
    private String radius; //半径
    
	@ApiModelProperty(value="所在区ID")
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
    
}