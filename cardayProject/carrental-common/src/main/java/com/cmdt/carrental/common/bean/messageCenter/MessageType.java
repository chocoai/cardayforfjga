package com.cmdt.carrental.common.bean.messageCenter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageType {

	SYSTEM("system","系统消息"), TRIP("trip","行程消息"), ABNORMAL("abnormal","异常报警"), ALL("all","所有消息");

	private String type;
	
	private String info;

	MessageType(String type,String info) {
		this.type = type;
		this.info = info;
	}
	
	@JsonValue
	public String getType() {
		return type;
	}

	public String getInfo() {
		return info;
	}

	@JsonCreator
	public static MessageType getMessageType(String type) {
		for (MessageType value : values()) {
			if (value.getType().equals(type)) {
				return value;
			}
		}
		return null;
	}

}
