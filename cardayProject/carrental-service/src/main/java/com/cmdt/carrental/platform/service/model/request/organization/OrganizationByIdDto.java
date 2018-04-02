package com.cmdt.carrental.platform.service.model.request.organization;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OrganizationByIdDto {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull(message="userId不能为空")
	private Long userId;
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
