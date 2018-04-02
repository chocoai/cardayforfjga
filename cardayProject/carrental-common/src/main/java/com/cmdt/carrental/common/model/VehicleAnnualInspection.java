package com.cmdt.carrental.common.model;

import java.util.Date;

public class VehicleAnnualInspection {
	
	private Long id;
	private Long vehicleId;
	private String vehicleNumber;
	private String arrangedOrgName;//所属部门
	private String vehicleFromName;//车辆来源
	private Date insuranceDueTime;//保险到期日期
	private String insuranceDueTimeF;
	private Date inspectionLastTime;//上次年检日期
	private String inspectionLastTimeF;
	private Date inspectionNextTime;//下次年检日期
	private String inspectionNextTimeF;
	private boolean insuranceDueTimeFlag;//保险日期是否即将或到期
	private boolean inspectionTimeFlag;//年检日期是否即将或到期
	private Long ownerEntId;//车辆来源
	private Long ownerOrgId;//所属部门
	private int insuranceFlag;
	private int inspectionFlag;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getArrangedOrgName() {
		return arrangedOrgName;
	}
	public void setArrangedOrgName(String arrangedOrgName) {
		this.arrangedOrgName = arrangedOrgName;
	}
	public String getVehicleFromName() {
		return vehicleFromName;
	}
	public void setVehicleFromName(String vehicleFromName) {
		this.vehicleFromName = vehicleFromName;
	}
	public Date getInsuranceDueTime() {
		return insuranceDueTime;
	}
	public void setInsuranceDueTime(Date insuranceDueTime) {
		this.insuranceDueTime = insuranceDueTime;
	}
	public String getInsuranceDueTimeF() {
		return insuranceDueTimeF;
	}
	public void setInsuranceDueTimeF(String insuranceDueTimeF) {
		this.insuranceDueTimeF = insuranceDueTimeF;
	}
	public Date getInspectionLastTime() {
		return inspectionLastTime;
	}
	public void setInspectionLastTime(Date inspectionLastTime) {
		this.inspectionLastTime = inspectionLastTime;
	}
	public String getInspectionLastTimeF() {
		return inspectionLastTimeF;
	}
	public void setInspectionLastTimeF(String inspectionLastTimeF) {
		this.inspectionLastTimeF = inspectionLastTimeF;
	}
	public Date getInspectionNextTime() {
		return inspectionNextTime;
	}
	public void setInspectionNextTime(Date inspectionNextTime) {
		this.inspectionNextTime = inspectionNextTime;
	}
	public String getInspectionNextTimeF() {
		return inspectionNextTimeF;
	}
	public void setInspectionNextTimeF(String inspectionNextTimeF) {
		this.inspectionNextTimeF = inspectionNextTimeF;
	}
	public boolean isInsuranceDueTimeFlag() {
		return insuranceDueTimeFlag;
	}
	public void setInsuranceDueTimeFlag(boolean insuranceDueTimeFlag) {
		this.insuranceDueTimeFlag = insuranceDueTimeFlag;
	}
	public boolean isInspectionTimeFlag() {
		return inspectionTimeFlag;
	}
	public void setInspectionTimeFlag(boolean inspectionTimeFlag) {
		this.inspectionTimeFlag = inspectionTimeFlag;
	}
	public Long getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	public Long getOwnerEntId() {
		return ownerEntId;
	}
	public void setOwnerEntId(Long ownerEntId) {
		this.ownerEntId = ownerEntId;
	}
	public Long getOwnerOrgId() {
		return ownerOrgId;
	}
	public void setOwnerOrgId(Long ownerOrgId) {
		this.ownerOrgId = ownerOrgId;
	}
	public int getInsuranceFlag() {
		return insuranceFlag;
	}
	public void setInsuranceFlag(int insuranceFlag) {
		this.insuranceFlag = insuranceFlag;
	}
	public int getInspectionFlag() {
		return inspectionFlag;
	}
	public void setInspectionFlag(int inspectionFlag) {
		this.inspectionFlag = inspectionFlag;
	}
}
