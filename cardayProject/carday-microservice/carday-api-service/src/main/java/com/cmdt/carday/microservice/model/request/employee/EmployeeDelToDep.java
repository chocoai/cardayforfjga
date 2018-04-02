package com.cmdt.carday.microservice.model.request.employee;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
@ApiModel(value = "EmployeeDelToDep", description = "信息描述")
public class EmployeeDelToDep {

	@ApiModelProperty("用户ID")
	@NotNull(message="userId不能为空")
	private Long userId;
	@ApiModelProperty("需要移除部门的员工ID")
	@NotNull(message="ids不能为空")
	private String ids; //需要分配部门的对象编号
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
