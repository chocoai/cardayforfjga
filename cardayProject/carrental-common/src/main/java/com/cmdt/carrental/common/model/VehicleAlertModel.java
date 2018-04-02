package com.cmdt.carrental.common.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class VehicleAlertModel implements Serializable {

	private static final long serialVersionUID = -5795063055772101467L;

	private Long id; //编号
	private Long driverId;
	private Long currentUseOrgId;
	private Long rentId;
	private Long entId;
	private String vehicleNumber;
	private String vehicleType;	
	private String alertType;
	private String alertSpeed;
	private String overspeedPercent;
	private String alertCity;
	private String alertPosition;
	private String alertLongitude;
	private String alertLatitude;
	private String outboundMinutes;
	private Double outboundKilos;
	private Double firstOutboundKilos;
	private Double backStationDistance;
	private String firstAlertLongitude;
	private String firstAlertLatitude;
	private String backStationIds;
	private Timestamp firstOutboundtime;
	private Timestamp outboundReleasetime;
	private Timestamp alertTime;
	private Timestamp createTime;
	
	private String orgName;
	private String driverName;
	private String driverMobile;
	private String vehicleBrand;

	private List<StationModel> stations;
	private List<MarkerModel> markers;
	
	private String deviceNumber;
	private String stationNames;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getDriverId() {
		return driverId;
	}
	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}
	public Long getCurrentUseOrgId() {
		return currentUseOrgId;
	}
	public void setCurrentUseOrgId(Long currentUseOrgId) {
		this.currentUseOrgId = currentUseOrgId;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getVehicleType() {
		return vehicleType;
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
	public String getOutboundMinutes() {
		return outboundMinutes;
	}
	public void setOutboundMinutes(String outboundMinutes) {
		this.outboundMinutes = outboundMinutes;
	}
	public Double getOutboundKilos() {
		return outboundKilos;
	}
	public void setOutboundKilos(Double outboundKilos) {
		this.outboundKilos = outboundKilos;
	}
	public Double getFirstOutboundKilos() {
		return firstOutboundKilos;
	}
	public void setFirstOutboundKilos(Double firstOutboundKilos) {
		this.firstOutboundKilos = firstOutboundKilos;
	}
	public Double getBackStationDistance() {
		return backStationDistance;
	}
	public void setBackStationDistance(Double backStationDistance) {
		this.backStationDistance = backStationDistance;
	}
	public Timestamp getFirstOutboundtime() {
		return firstOutboundtime;
	}
	public void setFirstOutboundtime(Timestamp firstOutboundtime) {
		this.firstOutboundtime = firstOutboundtime;
	}
	public Timestamp getOutboundReleasetime() {
		return outboundReleasetime;
	}
	public void setOutboundReleasetime(Timestamp outboundReleasetime) {
		this.outboundReleasetime = outboundReleasetime;
	}
	public Timestamp getAlertTime() {
		return alertTime;
	}
	public void setAlertTime(Timestamp alertTime) {
		this.alertTime = alertTime;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getRentId() {
		return rentId;
	}
	public void setRentId(Long rentId) {
		this.rentId = rentId;
	}
	public Long getEntId() {
		return entId;
	}
	public void setEntId(Long entId) {
		this.entId = entId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverMobile() {
		return driverMobile;
	}
	public void setDriverMobile(String driverMobile) {
		this.driverMobile = driverMobile;
	}
	public String getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public List<StationModel> getStations() {
		return stations;
	}
	public void setStations(List<StationModel> stations) {
		this.stations = stations;
	}
	public List<MarkerModel> getMarkers() {
		return markers;
	}
	public void setMarkers(List<MarkerModel> markers) {
		this.markers = markers;
	}
	public String getVehicleBrand() {
		return vehicleBrand;
	}
	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}
	public String getBackStationIds() {
		return backStationIds;
	}
	public void setBackStationIds(String backStationIds) {
		this.backStationIds = backStationIds;
	}
	public String getStationNames() {
		return stationNames;
	}
	public void setStationNames(String stationNames) {
		this.stationNames = stationNames;
	}
	public String getFirstAlertLongitude() {
		return firstAlertLongitude;
	}
	public void setFirstAlertLongitude(String firstAlertLongitude) {
		this.firstAlertLongitude = firstAlertLongitude;
	}
	public String getFirstAlertLatitude() {
		return firstAlertLatitude;
	}
	public void setFirstAlertLatitude(String firstAlertLatitude) {
		this.firstAlertLatitude = firstAlertLatitude;
	}
}
