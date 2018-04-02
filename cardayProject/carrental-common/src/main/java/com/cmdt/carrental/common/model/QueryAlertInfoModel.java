package com.cmdt.carrental.common.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "QueryAlertInfoModel", description = "报警查询条件")
public class QueryAlertInfoModel implements Serializable{

	private static final long serialVersionUID = 2127915703485339381L;

	@ApiModelProperty("车辆号码")
	private String vehicleNumber;

	@ApiModelProperty("车辆类型 -1表示所有类型")
	private String vehicleType;

	@ApiModelProperty("车辆来源(所属企业), -1代表 所有来源")
	private Long fromOrgId;

	@ApiModelProperty("企业下的部门ID")
	private Long deptId;

	@ApiModelProperty("部门名称")
	private String deptName;

	@ApiModelProperty("查询结束时间")
	private Date endTime;

	@ApiModelProperty("查询开始时间")
	private Date startTime;

	@ApiModelProperty("当前页码， 1开始")
	private Integer currentPage;

	@ApiModelProperty("每页条数")
	private Integer numPerPage;

	@ApiModelProperty("报警类型: VEHICLEBACK / OVERSPEED / VEHICLEBACK / VOILATE")
	private String alertType;

	@ApiModelProperty("是否包含本部门")
	private Boolean includeSelf=Boolean.FALSE;

	@ApiModelProperty("是否包含子部门")
	private Boolean includeChild=Boolean.FALSE;

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

	public Long getFromOrgId() {
		return fromOrgId;
	}

	public void setFromOrgId(Long fromOrgId) {
		this.fromOrgId = fromOrgId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss") 
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss") 
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(Integer numPerPage) {
		this.numPerPage = numPerPage;
	}

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	public Boolean getIncludeSelf() {
		return includeSelf;
	}

	public void setIncludeSelf(Boolean includeSelf) {
		this.includeSelf = includeSelf;
	}

	public Boolean getIncludeChild() {
		return includeChild;
	}

	public void setIncludeChild(Boolean includeChild) {
		this.includeChild = includeChild;
	}


}
