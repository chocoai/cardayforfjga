package com.cmdt.carrental.platform.service.model.request.department;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DepartmentUpdateDto {

	@NotNull(message="id不能为空")
	private Long id;
	@NotNull(message="name不能为空")
	private String name; //组织机构名称
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
