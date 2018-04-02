package com.cmdt.carrental.common.model;

import java.io.Serializable;
import java.util.Date;

public class DevcieCommandConfigRecordModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String deviceNumber;
	private String commandType;
	private String commandValue;
	private Date commandSendTime;
	private Date commandResponseTime;
	private String commandSendStatus;
	private String commandExcuteStatus;
	private Long userId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public String getCommandType() {
		return commandType;
	}
	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}
	public String getCommandValue() {
		return commandValue;
	}
	public void setCommandValue(String commandValue) {
		this.commandValue = commandValue;
	}
	public Date getCommandSendTime() {
		return commandSendTime;
	}
	public void setCommandSendTime(Date commandSendTime) {
		this.commandSendTime = commandSendTime;
	}
	public Date getCommandResponseTime() {
		return commandResponseTime;
	}
	public void setCommandResponseTime(Date commandResponseTime) {
		this.commandResponseTime = commandResponseTime;
	}
	public String getCommandSendStatus() {
		return commandSendStatus;
	}
	public void setCommandSendStatus(String commandSendStatus) {
		this.commandSendStatus = commandSendStatus;
	}
	public String getCommandExcuteStatus() {
		return commandExcuteStatus;
	}
	public void setCommandExcuteStatus(String commandExcuteStatus) {
		this.commandExcuteStatus = commandExcuteStatus;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
