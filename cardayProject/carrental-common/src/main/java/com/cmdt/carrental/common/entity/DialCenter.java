package com.cmdt.carrental.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class DialCenter implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;            				//来电编号
	private Date dialTime;						//来电时间
	private String dialName;					//来电人姓名
	private String dialOrganization;			//来电人单位
	private String dialPhone;					//来电人电话号码
	private String dialType;					//投诉类型    (报障类，投诉类，其他)
	private String dialContent;					//来电内容
	private String vehicleNumber;				//车牌号
	private String orderNo;						//订单编号
	private String deviceNo;					//设别号
	private String dealResult;					//处理结果    （未核实，已核实，已处理，已回访）
	private String recorder;					//记录人姓名


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	public Date getDialTime() {
		return dialTime;
	}
	public void setDialTime(Date dialTime) {
		this.dialTime = dialTime;
	}
	public String getDialName() {
		return dialName;
	}
	public void setDialName(String dialName) {
		this.dialName = dialName;
	}
	public String getDialOrganization() {
		return dialOrganization;
	}
	public void setDialOrganization(String dialOrganization) {
		this.dialOrganization = dialOrganization;
	}
	public String getDialPhone() {
		return dialPhone;
	}
	public void setDialPhone(String dialPhone) {
		this.dialPhone = dialPhone;
	}
	public String getDialType() {
		return dialType;
	}
	public void setDialType(String dialType) {
		this.dialType = dialType;
	}
	public String getDialContent() {
		return dialContent;
	}
	public void setDialContent(String dialContent) {
		this.dialContent = dialContent;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	public String getDealResult() {
		return dealResult;
	}
	public void setDealResult(String dealResult) {
		this.dealResult = dealResult;
	}
	public String getRecorder() {
		return recorder;
	}
	public void setRecorder(String recorder) {
		this.recorder = recorder;
	}
	
	
}
