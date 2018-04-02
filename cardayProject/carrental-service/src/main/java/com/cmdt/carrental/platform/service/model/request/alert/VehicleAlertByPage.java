package com.cmdt.carrental.platform.service.model.request.alert;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class VehicleAlertByPage implements Serializable {

	private static final long serialVersionUID = -8093636367594042049L;
	
	@Min(value=1)
	@NotNull(message="userId不能为空")
	private Long userId;
	
	@Min(value=1)
	@NotNull(message="CurrentPage不能为空")
	private int currentPage;
	
	@Min(value=1)
	@NotNull(message="numPerPage不能为空")
	private int numPerPage;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}
}
