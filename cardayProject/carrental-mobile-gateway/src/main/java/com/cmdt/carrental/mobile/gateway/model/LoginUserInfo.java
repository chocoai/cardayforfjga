package com.cmdt.carrental.mobile.gateway.model;

public class LoginUserInfo {
	private Long userId;
	private String userName;
	private String realName;
	private String mobile;
	private String email;
	private Long userCategory;
	private String accessToken;
	private Long entId;
	private String entName;
	private Long deptId;
	private String deptName;
	private Double monthLimitvalue;
	private Double monthLimitLeft;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getUserCategory() {
		return userCategory;
	}

	public void setUserCategory(Long userCategory) {
		this.userCategory = userCategory;
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	
	public Long getEntId() {
		return entId;
	}

	public void setEntId(Long entId) {
		this.entId = entId;
	}

	public String getEntName() {
		return entName;
	}

	public void setEntName(String entName) {
		this.entName = entName;
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

    public Double getMonthLimitvalue()
    {
        return monthLimitvalue;
    }

    public void setMonthLimitvalue(Double monthLimitvalue)
    {
        this.monthLimitvalue = monthLimitvalue;
    }

    public Double getMonthLimitLeft()
    {
        return monthLimitLeft;
    }

    public void setMonthLimitLeft(Double monthLimitLeft)
    {
        this.monthLimitLeft = monthLimitLeft;
    }

}
