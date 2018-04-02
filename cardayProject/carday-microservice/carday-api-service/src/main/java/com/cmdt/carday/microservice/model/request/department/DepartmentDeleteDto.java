package com.cmdt.carday.microservice.model.request.department;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
@ApiModel(value = "DepartmentDeleteDto", description = "信息描述")
public class DepartmentDeleteDto {

	@ApiModelProperty("组织ID")
	@NotNull(message="id不能为空")
	private Long id;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

    
	
}
