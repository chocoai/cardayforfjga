package com.cmdt.carday.microservice.model.request.organization;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
@ApiModel(value = "OrganizationByIdDto", description = "信息描述")
public class OrganizationByIdDto {
	
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("用户id")
	@NotNull(message="userId不能为空")
	private Long userId;
	@ApiModelProperty("组织id")
	@NotNull(message="id不能为空")
	private Long id;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
