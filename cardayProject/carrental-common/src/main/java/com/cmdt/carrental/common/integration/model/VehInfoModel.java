package com.cmdt.carrental.common.integration.model;

public class VehInfoModel {
	
	private String resultCode;
	private String resultMessage;
	private String terminalID;
	private String simNo;
	private String companyName;
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	public String getTerminalID() {
		return terminalID;
	}
	public void setTerminalID(String terminalID) {
		this.terminalID = terminalID;
	}
	public String getSimNo() {
		return simNo;
	}
	public void setSimNo(String simNo) {
		this.simNo = simNo;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	@Override
	public String toString() {
		return "VehInfoModel [resultCode=" + resultCode + ", resultMessage=" + resultMessage + ", terminalID="
				+ terminalID + ", simNo=" + simNo + ", companyName=" + companyName + "]";
	}

}
