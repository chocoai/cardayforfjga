package com.cmdt.carrental.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VehicleAlert implements Serializable {

	private static final long serialVersionUID = -5795063055772101467L;

	private String id;  	             //报警编号
	private String vehicleNumber;  	             //车牌号
	private String vehicleType;	                 //车辆类型
	private String alertType;                   //警报类型
	private Date alertTime;			             //警报时间
	private String alertSpeed;                  //速度
	private String overspeedPercent;            //超速百分比
	private String alertCity;                   //警报城市
	private String alertPosition;               //警报位置
	private String alertLongitude;              //经度
	private String alertLatitude;               //纬度
	private Date firstOutboundtime;             //越界发生时间
	private Date outboundReleasetime;           //越界解除时间
	private Double outboundKilos;               //越界里程
	private String outboundMinutes;             // 越界时间（分钟） 
	private Integer driverId;					 //司机Id
	private String driverName;					 //司机姓名	
	private String driverPhone;                 //司机电话
	private Integer currentuseOrgId;             //用车机构Id号
	private String currentuseOrgName;            //用车机构名称
	private Double backStationDistance;          //距离station的距离
	private Date createTime;                     //创建时间
	private String vehicleSource;                //车辆来源id
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public String getVehicleTypeName(String vehicleType){
		String retVal = "";
		switch (vehicleType) {
		case "0":
			retVal = "经济型";
		    break;
		case "1":
			retVal = "舒适型";
		    break;
		case "2":
			retVal = "商务型";
		    break;
		case "3":
			retVal = "豪华型";
		    break;
		default:
			retVal = "";
		}
		return retVal;
	}
	
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public Date getAlertTime() {
		return alertTime;
	}
	public void setAlertTime(Date alertTime) {
		this.alertTime = alertTime;
	}
	public String getAlertSpeed() {
		return alertSpeed;
	}
	public void setAlertSpeed(String alertSpeed) {
		this.alertSpeed = alertSpeed;
	}
	public String getOverspeedPercent() {
		return overspeedPercent;
	}
	public void setOverspeedPercent(String overspeedPercent) {
		this.overspeedPercent = overspeedPercent;
	}
	public String getAlertCity() {
		return alertCity;
	}
	public void setAlertCity(String alertCity) {
		this.alertCity = alertCity;
	}
	public String getAlertPosition() {
		return alertPosition;
	}
	public void setAlertPosition(String alertPosition) {
		this.alertPosition = alertPosition;
	}
	public String getAlertLongitude() {
		return alertLongitude;
	}
	public void setAlertLongitude(String alertLongitude) {
		this.alertLongitude = alertLongitude;
	}
	public String getAlertLatitude() {
		return alertLatitude;
	}
	public void setAlertLatitude(String alertLatitude) {
		this.alertLatitude = alertLatitude;
	}
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	public Date getFirstOutboundtime() {
		return firstOutboundtime;
	}
	public void setFirstOutboundtime(Date firstOutboundtime) {
		this.firstOutboundtime = firstOutboundtime;
	}
	public Date getOutboundReleasetime() {
		return outboundReleasetime;
	}
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	public void setOutboundReleasetime(Date outboundReleasetime) {
		this.outboundReleasetime = outboundReleasetime;
	}
	public Double getOutboundKilos() {
		return outboundKilos;
	}
	public void setOutboundKilos(Double outboundKilos) {
		this.outboundKilos = outboundKilos;
	}
	public String getOutboundMinutes() {
		return outboundMinutes;
	}
	public void setOutboundMinutes(String outboundMinutes) {
		this.outboundMinutes = outboundMinutes;
	}
	public Integer getDriverId() {
		return driverId;
	}
	public void setDriverId(Integer driverId) {
		this.driverId = driverId;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverPhone() {
		return driverPhone;
	}
	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}
	public Integer getCurrentuseOrgId() {
		return currentuseOrgId;
	}
	public void setCurrentuseOrgId(Integer currentuseOrgId) {
		this.currentuseOrgId = currentuseOrgId;
	}
	public String getCurrentuseOrgName() {
		return currentuseOrgName;
	}
	public void setCurrentuseOrgName(String currentuseOrgName) {
		this.currentuseOrgName = currentuseOrgName;
	}
	public Double getBackStationDistance() {
		return backStationDistance;
	}
	public void setBackStationDistance(Double backStationDistance) {
		this.backStationDistance = backStationDistance;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getVehicleSource() {
		return vehicleSource;
	}
	public void setVehicleSource(String vehicleSource) {
		this.vehicleSource = vehicleSource;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
