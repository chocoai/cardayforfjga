package com.cmdt.carday.microservice.model.request.employee;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
@ApiModel(value = "EmployeeSetToDep", description = "信息描述")
public class EmployeeSetToDep {

	@ApiModelProperty("分配部门ID")
	@NotNull(message="allocateDepId不能为空")
	private Long allocateDepId; //分配部门ID
	@ApiModelProperty("需要分配部门的员工ID")
	@NotNull(message="ids不能为空")
	private String ids; //需要分配部门的对象编号
	
	public Long getAllocateDepId() {
		return allocateDepId;
	}
	public void setAllocateDepId(Long allocateDepId) {
		this.allocateDepId = allocateDepId;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
}
