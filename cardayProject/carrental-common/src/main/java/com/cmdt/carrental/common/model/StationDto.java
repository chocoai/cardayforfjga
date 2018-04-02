package com.cmdt.carrental.common.model;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by zhengjun.jing on 6/6/2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StationDto {
    private Long userId;
    private Long id; //编号
    private Long organizationId; //所属公司
    private Long markerId; //Marker
    private String stationName; //站点名
    private String province;//省/直辖市
    private Long provinceId;
    private String city; //城市
    private Long cityId;
    private String area;
    private Long areaId;
    private String position; //位置
    
    @NotNull(message="longitude不能为空")
    private String longitude; //经度
    
    @NotNull(message="latitude不能为空")
    private String latitude; //纬度
    
    @NotNull(message="radius不能为空")
    private String radius; //半径
    private String assignedVehicleNumber;//已分配车辆数
    private String carNumber;//停车位数

    private String startTime;//运营开始时间
    private String endTime;//运营结束时间

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Long getAreaId() {
        return areaId;
    }

    public void setAreaId(Long areaId) {
        this.areaId = areaId;
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

    @Override
    public String toString() {
        return "StationDto{" +
                "userId=" + userId +
                ", id=" + id +
                ", organizationId=" + organizationId +
                ", markerId=" + markerId +
                ", stationName='" + stationName + '\'' +
                ", province='" + province + '\'' +
                ", provinceId=" + provinceId +
                ", city='" + city + '\'' +
                ", cityId=" + cityId +
                ", area='" + area + '\'' +
                ", areaId=" + areaId +
                ", position='" + position + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", radius='" + radius + '\'' +
                ", assignedVehicleNumber='" + assignedVehicleNumber + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
