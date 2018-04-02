package com.cmdt.carrental.platform.service.common;

import java.util.ArrayList;
import java.util.List;

import static com.cmdt.carrental.platform.service.common.Constants.API_STATUS_FAILURE;
import static com.cmdt.carrental.platform.service.common.Constants.API_STATUS_SUCCESS;

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

	public static WsResponse failure(String msg) {
		return failureStatus(API_STATUS_FAILURE, msg);
	}

	public static WsResponse failureStatus(String status, String msg) {
		WsResponse resp = new WsResponse();
		resp.status = status;
		resp.messages.add(msg);

		return resp;
	}

	public static WsResponse  success() {
		WsResponse resp = new WsResponse();
		resp.status = API_STATUS_SUCCESS;

		return resp;
	}

	public static <K> WsResponse<K>  success(K t) {
		WsResponse<K> resp = new WsResponse<>();
		resp.status = API_STATUS_SUCCESS;
		resp.result = t;

		return resp;
	}
}
