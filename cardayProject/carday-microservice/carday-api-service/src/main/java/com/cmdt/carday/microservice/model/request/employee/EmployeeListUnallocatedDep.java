package com.cmdt.carday.microservice.model.request.employee;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@JsonInclude(Include.NON_NULL)
@ApiModel(value = "EmployeeListUnallocatedDep", description = "信息描述")
public class EmployeeListUnallocatedDep {
    
	@ApiModelProperty("用户id")
	@NotNull(message="userId不能为空")
	private Long userId;
	@ApiModelProperty("分页当前页")
	@NotNull(message="currentPage不能为空")
	@Digits(message="currentPage格式错误,最小1,最大:"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
	private Integer currentPage;
	@ApiModelProperty("每页表示数据数")
	@NotNull(message="numPerPage不能为空")
	@Digits(message="numPerPage格式错误,最小1,最大:"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
	private Integer numPerPage;
	@ApiModelProperty("手机号码")
	private String phone;
	@ApiModelProperty("员工姓名")
	private String realname;
	@ApiModelProperty("用户名")
	private String username;
	@ApiModelProperty("角色ID")
    private Long roleId;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
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
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}
