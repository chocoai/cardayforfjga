package com.cmdt.carday.microservice.model.request.user;

import java.io.Serializable;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public class UserPageDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String realName;
	
    @Digits(message="currentPage格式错误,最小:1,最大:"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
    @NotNull
    private int currentPage;
    
    @Digits(message="numPerPage格式错误,最小1,最大:"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
    @NotNull
    private int numPerPage;

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
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
    
}
