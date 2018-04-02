package com.cmdt.carrental.mobile.gateway.common;

import java.util.ArrayList;
import java.util.List;

public class WsResponse<T> {
	
	private String status;
	private List<String> messages;
	private T result;
	
	public WsResponse(){
		messages = new ArrayList<>();
	}
	
	public WsResponse(String status,T result){
		messages = new ArrayList<>();
		this.status = status;
		this.result = result;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
	
	@Override
	public String toString(){
	   return "error code:"+status+" result:"+result;
	}

}
