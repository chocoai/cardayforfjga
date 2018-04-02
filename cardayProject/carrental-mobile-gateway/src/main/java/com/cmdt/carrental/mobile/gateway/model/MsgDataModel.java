package com.cmdt.carrental.mobile.gateway.model;

import java.util.Date;

import com.cmdt.carrental.common.entity.Message.MessageType;
public class MsgDataModel {
	private Long msgId;
	private MessageType type;
	private String carNo;
	private Date time;
	private String department;
	private String location;
	private boolean release;
	private boolean unread;
	private Long warningId;
	private String msg;
	private String orderNo;
	private String orderType;
	private String fromPlace;
	private String toPlace;
	private String userName;//乘车人姓名
	private String userPhone;//乘车人手机号
	private String driverName;//司机姓名
	private String driverPhone;//司机手机号
	private Date orderPlanTime;//订单预约时间
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
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
	public Date getOrderPlanTime() {
		return orderPlanTime;
	}
	public void setOrderPlanTime(Date orderPlanTime) {
		this.orderPlanTime = orderPlanTime;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	public String getCarNo() {
		return carNo;
	}
	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public boolean isRelease() {
		return release;
	}
	public void setRelease(boolean release) {
		this.release = release;
	}
	public boolean isUnread() {
		return unread;
	}
	public void setUnread(boolean unread) {
		this.unread = unread;
	}
	public Long getWarningId() {
		return warningId;
	}
	public void setWarningId(Long warningId) {
		this.warningId = warningId;
	}
	public Long getMsgId() {
		return msgId;
	}
	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

}
