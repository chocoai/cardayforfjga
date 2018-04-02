package com.cmdt.carrental.common.entity;

import java.io.Serializable;
import java.util.Date;

public class VehicleMaintenance implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
    private Long vehicleId;//车辆ID
    private String vehicleNumber;//车牌号
    private String vehicleBrand;			 //车辆品牌
    private String vehicleModel;			 //车辆型号
    private String vehicleIdentification;//车架号
    private String deviceNumber;		     //设备号
    private String simNumber;		         //SIM卡
    private String vehicleSource;			//车辆来源
    private Long ownerOrgId;//所属部门(或企业)
    private String orgName;//部门(或企业)名称
    private Long ownerEntId;//车辆来源
    private Long headerLatestMileage;//当前(导入时最新)表头里程数
    private Long headerLastMileage;//上次保养表头里程数
    private Long headerMaintenanceMileage;//本次保养表头里程数
    private Long maintenanceMileage;//保养里程数
    private Long travelMileage;//已行驶里程 调用shouqi接口查询
    private Long remainingMileage;//剩余里程数=保养里程数-已行驶里程数
    private Date curTime;//本次保养时间
    private String curTimeF;//格式化本次保养时间yyyy-MM-dd hh24:mi:ss
    private int maintenanceTime;//维保时间（月）
    private int maintenanceRemainingTime;//保养剩余时间（天）
    private String maintenanceNextTime;//下次保养时间
    private Long alertMileage;//维保里程
    private int alertMileageWarn;//维保里程报警标识:0 未推送;1 推送即将到达维保里程;2 推送到达维保里程
    private int curTimeWarn;//保养时间报警标识:0 未推送;1 推送即将到期;2 推送到期 
    private Date maintenanceDueTime;//保养到期时间
    private String maintenanceDueTimeF;
    private int thresholdMonth;
    private Date maintenanceThresholdTime;
    private String maintenanceThresholdTimeF;
    private Date updateTime;
    private String updateTimeF;
    
    /**
     * 以下是非数据库操作属性,仅业务逻辑使用
     */
    private Integer gap;//比较导入时间和本次保养时间所得值
    private String message;//业务处理后的返回错误消息
    
    public VehicleMaintenance() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getVehicleBrand() {
		return vehicleBrand;
	}

	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public String getVehicleIdentification() {
		return vehicleIdentification;
	}

	public void setVehicleIdentification(String vehicleIdentification) {
		this.vehicleIdentification = vehicleIdentification;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public String getSimNumber() {
		return simNumber;
	}

	public void setSimNumber(String simNumber) {
		this.simNumber = simNumber;
	}

	public String getVehicleSource() {
		return vehicleSource;
	}

	public void setVehicleSource(String vehicleSource) {
		this.vehicleSource = vehicleSource;
	}

	public Long getOwnerOrgId() {
		return ownerOrgId;
	}

	public void setOwnerOrgId(Long ownerOrgId) {
		this.ownerOrgId = ownerOrgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Long getHeaderLatestMileage() {
		return headerLatestMileage;
	}

	public void setHeaderLatestMileage(Long headerLatestMileage) {
		this.headerLatestMileage = headerLatestMileage;
	}

	public Long getHeaderLastMileage() {
		return headerLastMileage;
	}

	public void setHeaderLastMileage(Long headerLastMileage) {
		this.headerLastMileage = headerLastMileage;
	}

	public Long getHeaderMaintenanceMileage() {
		return headerMaintenanceMileage;
	}

	public void setHeaderMaintenanceMileage(Long headerMaintenanceMileage) {
		this.headerMaintenanceMileage = headerMaintenanceMileage;
	}

	public Long getMaintenanceMileage() {
		return maintenanceMileage;
	}

	public void setMaintenanceMileage(Long maintenanceMileage) {
		this.maintenanceMileage = maintenanceMileage;
	}

	public Long getTravelMileage() {
		return travelMileage;
	}

	public void setTravelMileage(Long travelMileage) {
		this.travelMileage = travelMileage;
	}

	public Long getRemainingMileage() {
		return remainingMileage;
	}

	public void setRemainingMileage(Long remainingMileage) {
		this.remainingMileage = remainingMileage;
	}

	public Date getCurTime() {
		return curTime;
	}

	public void setCurTime(Date curTime) {
		this.curTime = curTime;
	}

	public String getCurTimeF() {
		return curTimeF;
	}

	public void setCurTimeF(String curTimeF) {
		this.curTimeF = curTimeF;
	}

	public Integer getGap() {
		return gap;
	}

	public void setGap(Integer gap) {
		this.gap = gap;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getMaintenanceTime() {
		return maintenanceTime;
	}

	public void setMaintenanceTime(int maintenanceTime) {
		this.maintenanceTime = maintenanceTime;
	}

	public int getMaintenanceRemainingTime() {
		return maintenanceRemainingTime;
	}

	public void setMaintenanceRemainingTime(int maintenanceRemainingTime) {
		this.maintenanceRemainingTime = maintenanceRemainingTime;
	}

	public String getMaintenanceNextTime() {
		return maintenanceNextTime;
	}

	public void setMaintenanceNextTime(String maintenanceNextTime) {
		this.maintenanceNextTime = maintenanceNextTime;
	}

	public Long getAlertMileage() {
		return alertMileage;
	}

	public void setAlertMileage(Long alertMileage) {
		this.alertMileage = alertMileage;
	}

	public int getAlertMileageWarn() {
		return alertMileageWarn;
	}

	public void setAlertMileageWarn(int alertMileageWarn) {
		this.alertMileageWarn = alertMileageWarn;
	}

	public int getCurTimeWarn() {
		return curTimeWarn;
	}

	public void setCurTimeWarn(int curTimeWarn) {
		this.curTimeWarn = curTimeWarn;
	}

	public Date getMaintenanceDueTime() {
		return maintenanceDueTime;
	}

	public void setMaintenanceDueTime(Date maintenanceDueTime) {
		this.maintenanceDueTime = maintenanceDueTime;
	}

	public Long getOwnerEntId() {
		return ownerEntId;
	}

	public void setOwnerEntId(Long ownerEntId) {
		this.ownerEntId = ownerEntId;
	}

	public String getMaintenanceDueTimeF() {
		return maintenanceDueTimeF;
	}

	public void setMaintenanceDueTimeF(String maintenanceDueTimeF) {
		this.maintenanceDueTimeF = maintenanceDueTimeF;
	}

	public int getThresholdMonth() {
		return thresholdMonth;
	}

	public void setThresholdMonth(int thresholdMonth) {
		this.thresholdMonth = thresholdMonth;
	}

	public Date getMaintenanceThresholdTime() {
		return maintenanceThresholdTime;
	}

	public void setMaintenanceThresholdTime(Date maintenanceThresholdTime) {
		this.maintenanceThresholdTime = maintenanceThresholdTime;
	}

	public String getMaintenanceThresholdTimeF() {
		return maintenanceThresholdTimeF;
	}

	public void setMaintenanceThresholdTimeF(String maintenanceThresholdTimeF) {
		this.maintenanceThresholdTimeF = maintenanceThresholdTimeF;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateTimeF() {
		return updateTimeF;
	}

	public void setUpdateTimeF(String updateTimeF) {
		this.updateTimeF = updateTimeF;
	}
}
