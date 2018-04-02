package com.cmdt.carday.microservice.model.request.department;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
@ApiModel(value = "DepartmentUpdateDto", description = "信息描述")
public class DepartmentUpdateDto {

	@ApiModelProperty("组织ID")
	@NotNull(message="id不能为空")
	private Long id;
	@ApiModelProperty("组织机构名称")
	@NotNull(message="name不能为空")
	private String name; //组织机构名称
	@ApiModelProperty("直接父机构ID")
	@NotNull(message="parentId不能为空")
	private Long parentId;//直接父机构ID

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
