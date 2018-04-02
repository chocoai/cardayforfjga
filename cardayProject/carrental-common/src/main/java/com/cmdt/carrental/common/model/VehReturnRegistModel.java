package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class VehReturnRegistModel implements Serializable {

	private static final long serialVersionUID = -5597025809225217962L;

	/* 单位名称 */
	private String orgName;
	
	/* 单据编码 */
	private String orderNo;
	
	/* 纸质派车单号 */
	private String paperOrderNo;
	
	/* 申请日期 */
	private String orderTimeF;
	
	/* 车牌号 */
	private String vehicleNumber;
	
	/* 座位数 */
	private Integer seatNumber;
	
	/* 驾驶员 */
	private String driverName;
	
	/* 电话 */
	private String driverPhone;
	
	/* 出车前公里数 */
	private Long stMileage;
	
	/* 返回后公里数 */
	private Long edMileage;
	
	/* 实际出车时间-----调度员填写 */
	private String factStTimeF;
	
	/* 实际返回时间-----调度员填写 */
	private String factEdTimeF;
	
	/* 停放位置 */
	private String parkingSite;
	
	/* 用车人 */
	private String orderUsername;
	
	/* 用车人电话 */
	private String orderUserphone;
	
	/* 订单用途 0:市区 1:省外 */
	private Integer vehicleUsage;
	
	/* 事由: 0-送文件,1-特殊警务,2-常规巡逻 */
	private String orderReason;
	
	/* 目的地 */
	private String toPlace;
	
	/* 随车人数 */
	private Integer passengerNum;
	
	/* 负责人签批 */
	private String auditUserName;
	
	/* 上车地点 */
	private String fromPlace;
	
	/* 用车时间开始时间-----声请人填写 */
	private String planStTimeF;
	
	/* 用车时间结束时间-----声请人填写 */
	private String planEdTimeF;
	
	/* 审批意见 */
	private Integer auditStatus;
	
	/* 驾驶类型:0-派遣驾驶员,1-民警自驾,2-临时驾驶员 */
	private Integer drivingType;
	
	/* 纸质派车单扫描件 */
	private String orderAttah;
	
	/* 调度员签名 */
	private String dispatcherName;
	
	public VehReturnRegistModel() {
		super();
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPaperOrderNo() {
		return paperOrderNo;
	}

	public void setPaperOrderNo(String paperOrderNo) {
		this.paperOrderNo = paperOrderNo;
	}

	public String getOrderTimeF() {
		return orderTimeF;
	}

	public void setOrderTimeF(String orderTimeF) {
		this.orderTimeF = orderTimeF;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public Integer getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(Integer seatNumber) {
		this.seatNumber = seatNumber;
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

	public Long getStMileage() {
		return stMileage;
	}

	public void setStMileage(Long stMileage) {
		this.stMileage = stMileage;
	}

	public Long getEdMileage() {
		return edMileage;
	}

	public void setEdMileage(Long edMileage) {
		this.edMileage = edMileage;
	}

	public String getFactStTimeF() {
		return factStTimeF;
	}

	public void setFactStTimeF(String factStTimeF) {
		this.factStTimeF = factStTimeF;
	}

	public String getFactEdTimeF() {
		return factEdTimeF;
	}

	public void setFactEdTimeF(String factEdTimeF) {
		this.factEdTimeF = factEdTimeF;
	}


	public String getParkingSite() {
		return parkingSite;
	}

	public void setParkingSite(String parkingSite) {
		this.parkingSite = parkingSite;
	}

	public String getOrderUsername() {
		return orderUsername;
	}

	public void setOrderUsername(String orderUsername) {
		this.orderUsername = orderUsername;
	}

	public String getOrderUserphone() {
		return orderUserphone;
	}

	public void setOrderUserphone(String orderUserphone) {
		this.orderUserphone = orderUserphone;
	}

	public Integer getVehicleUsage() {
		return vehicleUsage;
	}

	public void setVehicleUsage(Integer vehicleUsage) {
		this.vehicleUsage = vehicleUsage;
	}

	public String getOrderReason() {
		return orderReason;
	}

	public void setOrderReason(String orderReason) {
		this.orderReason = orderReason;
	}

	public String getToPlace() {
		return toPlace;
	}

	public void setToPlace(String toPlace) {
		this.toPlace = toPlace;
	}

	public Integer getPassengerNum() {
		return passengerNum;
	}

	public void setPassengerNum(Integer passengerNum) {
		this.passengerNum = passengerNum;
	}

	public String getAuditUserName() {
		return auditUserName;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}

	public String getFromPlace() {
		return fromPlace;
	}

	public void setFromPlace(String fromPlace) {
		this.fromPlace = fromPlace;
	}

	public String getPlanStTimeF() {
		return planStTimeF;
	}

	public void setPlanStTimeF(String planStTimeF) {
		this.planStTimeF = planStTimeF;
	}

	public String getPlanEdTimeF() {
		return planEdTimeF;
	}

	public void setPlanEdTimeF(String planEdTimeF) {
		this.planEdTimeF = planEdTimeF;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Integer getDrivingType() {
		return drivingType;
	}

	public void setDrivingType(Integer drivingType) {
		this.drivingType = drivingType;
	}

	public String getOrderAttah() {
		return orderAttah;
	}

	public void setOrderAttah(String orderAttah) {
		this.orderAttah = orderAttah;
	}
	

	public String getDispatcherName() {
		return dispatcherName;
	}

	public void setDispatcherName(String dispatcherName) {
		this.dispatcherName = dispatcherName;
	}
	
}
