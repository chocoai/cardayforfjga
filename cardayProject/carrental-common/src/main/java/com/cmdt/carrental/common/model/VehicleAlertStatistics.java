package com.cmdt.carrental.common.model;

import java.io.Serializable;
import java.sql.Date;

public class VehicleAlertStatistics implements Serializable {

	private static final long serialVersionUID = -5795063055772101467L;

	private Long id;
	private String alertType;	//报警类型
	private Date alertDay;	//产生报警的时间--某一天
	private Integer alertNumber;	//报警数量
	private String alertNumberAverage;	//平均报警数
	private String alertNumberTotal;	//平均报警数
	private Double outboundKilos;	//越界里程
	private String outboundKilosAverage;	//平均越界里程
	private String outboundKilosTotal;	//越界里程
	private Long orgId;
	private Long entId;
	
	private String organizationName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public Date getAlertDay() {
		return alertDay;
	}
	public void setAlertDay(Date alertDay) {
		this.alertDay = alertDay;
	}
	public Integer getAlertNumber() {
		return alertNumber;
	}
	public void setAlertNumber(Integer alertNumber) {
		this.alertNumber = alertNumber;
	}
	public String getAlertNumberAverage() {
		return alertNumberAverage;
	}
	public void setAlertNumberAverage(String alertNumberAverage) {
		this.alertNumberAverage = alertNumberAverage;
	}
	public Double getOutboundKilos() {
		return outboundKilos;
	}
	public void setOutboundKilos(Double outboundKilos) {
		this.outboundKilos = outboundKilos;
	}
	public String getOutboundKilosAverage() {
		return outboundKilosAverage;
	}
	public void setOutboundKilosAverage(String outboundKilosAverage) {
		this.outboundKilosAverage = outboundKilosAverage;
	}
	public String getAlertNumberTotal() {
		return alertNumberTotal;
	}
	public void setAlertNumberTotal(String alertNumberTotal) {
		this.alertNumberTotal = alertNumberTotal;
	}
	public String getOutboundKilosTotal() {
		return outboundKilosTotal;
	}
	public void setOutboundKilosTotal(String outboundKilosTotal) {
		this.outboundKilosTotal = outboundKilosTotal;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public Long getEntId() {
		return entId;
	}
	public void setEntId(Long entId) {
		this.entId = entId;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
}
