package com.cmdt.carrental.platform.service.model.request.driver;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DriverListDto {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull(message="userId不能为空")
	private Long userId;
	@NotNull(message="organizationId不能为空")
    private Long organizationId; //所属公司
    private String realname;
    private String phone;
    private String email;
    private String username;
    @NotNull(message="currentPage不能为空")
	@Digits(message="currentPage格式错误,最小1,最大:"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
	private int currentPage;
    @NotNull(message="numPerPage不能为空")
	@Digits(message="numPerPage格式错误,最小1,最大:"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
	private int numPerPage;
	private boolean selfDept;
	private boolean childDept;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public boolean getSelfDept() {
		return selfDept;
	}
	public void setSelfDept(boolean selfDept) {
		this.selfDept = selfDept;
	}
	public boolean getChildDept() {
		return childDept;
	}
	public void setChildDept(boolean childDept) {
		this.childDept = childDept;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
