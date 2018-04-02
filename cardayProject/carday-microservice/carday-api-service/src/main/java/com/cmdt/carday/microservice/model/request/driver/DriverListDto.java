package com.cmdt.carday.microservice.model.request.driver;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
@ApiModel(value = "DriverListDto", description = "信息描述")
public class DriverListDto {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty("用户id")
	@NotNull(message="userId不能为空")
	private Long userId;
	@ApiModelProperty("所属公司")
	@NotNull(message="organizationId不能为空")
    private Long organizationId; //所属公司
	@ApiModelProperty("司机姓名")
    private String realname;
	@ApiModelProperty("手机号码")
    private String phone;
	@ApiModelProperty("邮箱")
    private String email;
	@ApiModelProperty("用户名")
    private String username;
	@ApiModelProperty("分页当前页")
    @NotNull(message="currentPage不能为空")
	@Digits(message="currentPage格式错误,最小1,最大:"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
	private int currentPage;
	@ApiModelProperty("每页表示数据数")
    @NotNull(message="numPerPage不能为空")
	@Digits(message="numPerPage格式错误,最小1,最大:"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
	private int numPerPage;
	@ApiModelProperty("本部门checkbox")
	private boolean selfDept;
	@ApiModelProperty("子部门checkbox")
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
