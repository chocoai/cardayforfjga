package com.cmdt.carrental.common.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class VehicleAuthorized {
	
	private Long id;
	
	private Long deptId;
	private String deptName;
	
	private String docCode;
	
	private String cause;
	
	private Date applyTime;
	
	private Long emergencyVehAuthNum;
	private Long emergencyVehRealNum;
	private Long emergencyVehAddNum;
	
	private Long enforcementVehAuthNum;
	private Long enforcementVehRealNum;
	private Long enforcementVehAddNum;
	
	private Long specialVehAuthNum;
	private Long specialVehRealNum;
	private Long specialVehAddNum;
	
	private Long normalVehAuthNum;
	private Long normalVehRealNum;
	private Long normalVehAddNum;
	
	private Long majorVehAuthNum;
	private Long majorVehRealNum;
	private Long majorVehAddNum;
	
	private Long policeAdd;
	
	private String status;
	
	private String attachName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	

	public String getDocCode() {
		return docCode;
	}

	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Long getEmergencyVehAuthNum() {
		return emergencyVehAuthNum;
	}

	public void setEmergencyVehAuthNum(Long emergencyVehAuthNum) {
		this.emergencyVehAuthNum = emergencyVehAuthNum;
	}

	public Long getEmergencyVehRealNum() {
		return emergencyVehRealNum;
	}

	public void setEmergencyVehRealNum(Long emergencyVehRealNum) {
		this.emergencyVehRealNum = emergencyVehRealNum;
	}

	public Long getEmergencyVehAddNum() {
		return emergencyVehAddNum;
	}

	public void setEmergencyVehAddNum(Long emergencyVehAddNum) {
		this.emergencyVehAddNum = emergencyVehAddNum;
	}

	public Long getEnforcementVehAuthNum() {
		return enforcementVehAuthNum;
	}

	public void setEnforcementVehAuthNum(Long enforcementVehAuthNum) {
		this.enforcementVehAuthNum = enforcementVehAuthNum;
	}

	public Long getEnforcementVehRealNum() {
		return enforcementVehRealNum;
	}

	public void setEnforcementVehRealNum(Long enforcementVehRealNum) {
		this.enforcementVehRealNum = enforcementVehRealNum;
	}

	public Long getEnforcementVehAddNum() {
		return enforcementVehAddNum;
	}

	public void setEnforcementVehAddNum(Long enforcementVehAddNum) {
		this.enforcementVehAddNum = enforcementVehAddNum;
	}

	public Long getSpecialVehAuthNum() {
		return specialVehAuthNum;
	}

	public void setSpecialVehAuthNum(Long specialVehAuthNum) {
		this.specialVehAuthNum = specialVehAuthNum;
	}

	public Long getSpecialVehRealNum() {
		return specialVehRealNum;
	}

	public void setSpecialVehRealNum(Long specialVehRealNum) {
		this.specialVehRealNum = specialVehRealNum;
	}

	public Long getSpecialVehAddNum() {
		return specialVehAddNum;
	}

	public void setSpecialVehAddNum(Long specialVehAddNum) {
		this.specialVehAddNum = specialVehAddNum;
	}

	public Long getNormalVehAuthNum() {
		return normalVehAuthNum;
	}

	public void setNormalVehAuthNum(Long normalVehAuthNum) {
		this.normalVehAuthNum = normalVehAuthNum;
	}

	public Long getNormalVehRealNum() {
		return normalVehRealNum;
	}

	public void setNormalVehRealNum(Long normalVehRealNum) {
		this.normalVehRealNum = normalVehRealNum;
	}

	public Long getNormalVehAddNum() {
		return normalVehAddNum;
	}

	public void setNormalVehAddNum(Long normalVehAddNum) {
		this.normalVehAddNum = normalVehAddNum;
	}

	public Long getMajorVehAuthNum() {
		return majorVehAuthNum;
	}

	public void setMajorVehAuthNum(Long majorVehAuthNum) {
		this.majorVehAuthNum = majorVehAuthNum;
	}

	public Long getMajorVehRealNum() {
		return majorVehRealNum;
	}

	public void setMajorVehRealNum(Long majorVehRealNum) {
		this.majorVehRealNum = majorVehRealNum;
	}

	public Long getMajorVehAddNum() {
		return majorVehAddNum;
	}

	public void setMajorVehAddNum(Long majorVehAddNum) {
		this.majorVehAddNum = majorVehAddNum;
	}

	public Long getPoliceAdd() {
		return policeAdd;
	}

	public void setPoliceAdd(Long policeAdd) {
		this.policeAdd = policeAdd;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAttachName() {
		return attachName;
	}

	public void setAttachName(String attachName) {
		this.attachName = attachName;
	}

	
	
}
