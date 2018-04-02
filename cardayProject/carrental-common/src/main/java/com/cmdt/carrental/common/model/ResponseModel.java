package com.cmdt.carrental.common.model;

public class ResponseModel<T> {

	public static final String SERVICE_SUCCESS = "SERVICE_SUCCESS";
	public static final String SERVICE_FAILURE = "SERVICE_FAILURE";
	public static final String DUPLICATE_REC = "ALREADY_EXISTED_REC";
	public static final String JSON_FORMAT_FAILURE = "INVALID_FORMAT";
	public static final String INVALID_PARAMS = "INVALID_PARAM";
	public static final String ORDER_ALREADY_TAKEN = "ORDER_ALREADY_TAKEN";
	public static final String UPDATE_SUCCESS = "UPDATE_SUCCESS";
	public static final String CREATE_SUCCESS = "CREATE_SUCCESS";
	public static final String REC_NOT_FOUND = "REC_NOT_FOUND";


	protected T data;
	protected String status;
	protected Integer total;
	protected String failureMsg;
	protected boolean success;
	protected String msg;
	
	

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public String getFailureMsg() {
		return failureMsg;
	}

	public void setFailureMsg(String failureMsg) {
		this.failureMsg = failureMsg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
