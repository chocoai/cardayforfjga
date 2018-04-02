package com.cmdt.carday.microservice.model.request.department;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
@ApiModel(value = "DepartmentCreateDto", description = "信息描述")
public class DepartmentCreateDto {
	
	@ApiModelProperty("组织机构名称")
	@NotNull(message="name不能为空")
	private String name; //组织机构名称
	@ApiModelProperty("直接父机构ID")
	@NotNull(message="parentId不能为空")
	private Long parentId;//直接父机构ID

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

}
