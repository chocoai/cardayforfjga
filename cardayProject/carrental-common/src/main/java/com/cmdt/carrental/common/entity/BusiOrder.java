package com.cmdt.carrental.common.entity;

import java.io.Serializable;
import java.util.Date;

public class BusiOrder implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;//编号
    private String orderNo;//订单号，自动生成:yyyyMMdd+userId+timestamp
    private Date orderTime;//订单生成时间
    private String orderTimeF;//格式化后的订单时间yyyy-MM-dd hh24:mi:ss
    private Long orderUserid;//下单人ID
    private Long userCategory;//0:超级管理员  1:租户管理员 2:企业管理员 3.部门管理员  4：员工  5:司机
    private String orderUsername;
    private String orderUserphone;
    private String city;
    private String fromPlace;
    private String toPlace;
	private Date planStTime;//订单预约时间
    private String planStTimeF;//格式化后的订单预约时间yyyy-MM-dd hh24:mi:ss
    private Double durationTime;//预计行程时间，单位分钟
    private Double waitTime;//等待时长，单位分钟
	private Date planEdTime;//订单计划结束时间
    private String planEdTimeF;//格式化后的订单计划结束时间yyyy-MM-dd hh24:mi:ss
    private Date factStTime;//订单实际开始时间
    private String factStTimeF;//格式化后的实际开始时间yyyy-MM-dd hh24:mi:ss
	private Date factEdTime;//订单实际结束时间
    private String factEdTimeF;//格式化后的实际结束时间yyyy-MM-dd hh24:mi:ss
    private Double factDurationTime;//实际行程时间，单位分钟
    private Long stMileage;//起始读表里程数
    private Long edMileage;//结束读表里程数
    private Long factMileage;//里程数
    private String vehicleType;
    private Integer passengerNum;//乘车人数
    private String vehicleModel;	//车辆型号
    private String vehicleBrand;			 //车辆品牌
    private String orderReason;   //0:送文件,1:特殊警务,2:常规巡逻
    private Integer returnType;//0:是,1:否
    private String comments;
    private Integer status;//顺序->0:待审核,1:待排车,2:已排车,5:被驳回,6:已取消,11:已出车,12:已到达出发地,3:进行中/行程中,13:等待中,4:待支付,15:待评价,16:已完成 
    private String refuseComments;
    private Long vehicleId;
    private String vehicleNumber;//车牌号
    private String deviceNumber;//imei号
    private Long driverId;
    private String driverName;
    private String driverPhone;
    private Long organizationId;
    private String orgName;//部门名称
    private Integer orderType; //0:企业订单//1:网约订单
    private Double paymentCash;//订单支付金额
    private String comment;//评价信息
    private String comment_level;//评价等级
    private Double fromLat; //出发地lat
    private Double fromLng; //出发地lng
    private Double toLat;   //目的地lat
    private Double toLng;   //目的地lng
    private String stationName; //站定名称
    private Integer seatNumber; //站定名称
	// 特殊警务级别
	// 0: 未涉密  1: 机密 / 2: 绝密 / 3: 免审批
	private Integer secretLevel;
	
	private String paperOrderNo;//纸质排车单号，订单审核通过后自动生成
	private Integer vehicleUsage; //订单车辆用途，0:市区    1:省外
	private Integer drivingType; //驾驶类型，0:派遣驾驶员    1:民警自驾    2：临时驾驶员
	private String orderAttach;//附件，纸质排车单扫描件
	private Long dispatcherId;//调度员ID
    
	private String unitName;//单位名称
	
    public BusiOrder() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderTimeF() {
		return orderTimeF;
	}

	public void setOrderTimeF(String orderTimeF) {
		this.orderTimeF = orderTimeF;
	}

	public Long getOrderUserid() {
		return orderUserid;
	}

	public void setOrderUserid(Long orderUserid) {
		this.orderUserid = orderUserid;
	}

	public Long getUserCategory() {
		return userCategory;
	}

	public void setUserCategory(Long userCategory) {
		this.userCategory = userCategory;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFromPlace() {
		return fromPlace;
	}

	public void setFromPlace(String fromPlace) {
		this.fromPlace = fromPlace;
	}

	public String getToPlace() {
		return toPlace;
	}

	public void setToPlace(String toPlace) {
		this.toPlace = toPlace;
	}

	public Date getPlanStTime() {
		return planStTime;
	}

	public void setPlanStTime(Date planStTime) {
		this.planStTime = planStTime;
	}

	public String getPlanStTimeF() {
		return planStTimeF;
	}

	public void setPlanStTimeF(String planStTimeF) {
		this.planStTimeF = planStTimeF;
	}

	public Double getDurationTime() {
		return durationTime;
	}

	public void setDurationTime(Double durationTime) {
		this.durationTime = durationTime;
	}
	
	public Double getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(Double waitTime) {
		this.waitTime = waitTime;
	}

	public Date getPlanEdTime() {
		return planEdTime;
	}

	public void setPlanEdTime(Date planEdTime) {
		this.planEdTime = planEdTime;
	}

	public String getPlanEdTimeF() {
		return planEdTimeF;
	}

	public void setPlanEdTimeF(String planEdTimeF) {
		this.planEdTimeF = planEdTimeF;
	}

	public Date getFactStTime() {
		return factStTime;
	}

	public void setFactStTime(Date factStTime) {
		this.factStTime = factStTime;
	}

	public Date getFactEdTime() {
		return factEdTime;
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

	public void setFactEdTime(Date factEdTime) {
		this.factEdTime = factEdTime;
	}

	public Double getFactDurationTime() {
		return factDurationTime;
	}

	public void setFactDurationTime(Double factDurationTime) {
		this.factDurationTime = factDurationTime;
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

	public Long getFactMileage() {
		return factMileage;
	}

	public void setFactMileage(Long factMileage) {
		this.factMileage = factMileage;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public Integer getPassengerNum() {
		return passengerNum;
	}

	public void setPassengerNum(Integer passengerNum) {
		this.passengerNum = passengerNum;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public String getVehicleBrand() {
		return vehicleBrand;
	}

	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}

	public String getOrderReason() {
		return orderReason;
	}

	public void setOrderReason(String orderReason) {
		this.orderReason = orderReason;
	}

	public Integer getReturnType() {
		return returnType;
	}

	public void setReturnType(Integer returnType) {
		this.returnType = returnType;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRefuseComments() {
		return refuseComments;
	}

	public void setRefuseComments(String refuseComments) {
		this.refuseComments = refuseComments;
	}

	public Long getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getDeviceNumber() {
		return deviceNumber;
	}

	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Double getPaymentCash() {
		return paymentCash;
	}

	public void setPaymentCash(Double paymentCash) {
		this.paymentCash = paymentCash;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment_level() {
		return comment_level;
	}

	public void setComment_level(String comment_level) {
		this.comment_level = comment_level;
	}
	
	public Double getFromLat() {
		return fromLat;
	}

	public void setFromLat(Double fromLat) {
		this.fromLat = fromLat;
	}

	public Double getFromLng() {
		return fromLng;
	}

	public void setFromLng(Double fromLng) {
		this.fromLng = fromLng;
	}

	public Double getToLat() {
		return toLat;
	}

	public void setToLat(Double toLat) {
		this.toLat = toLat;
	}

	public Double getToLng() {
		return toLng;
	}

	public void setToLng(Double toLng) {
		this.toLng = toLng;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public Integer getSecretLevel() {
		return secretLevel;
	}

	public void setSecretLevel(Integer secretLevel) {
		this.secretLevel = secretLevel;
	}

	public String getPaperOrderNo() {
		return paperOrderNo;
	}

	public void setPaperOrderNo(String paperOrderNo) {
		this.paperOrderNo = paperOrderNo;
	}

	public Integer getVehicleUsage() {
		return vehicleUsage;
	}

	public void setVehicleUsage(Integer vehicleUsage) {
		this.vehicleUsage = vehicleUsage;
	}

	public Integer getDrivingType() {
		return drivingType;
	}

	public void setDrivingType(Integer drivingType) {
		this.drivingType = drivingType;
	}

	public String getOrderAttach() {
		return orderAttach;
	}

	public void setOrderAttach(String orderAttach) {
		this.orderAttach = orderAttach;
	}

	public Long getDispatcherId() {
		return dispatcherId;
	}

	public void setDispatcherId(Long dispatcherId) {
		this.dispatcherId = dispatcherId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Integer getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(Integer seatNumber) {
		this.seatNumber = seatNumber;
	}
	
}
