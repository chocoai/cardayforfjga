package com.cmdt.carrental.platform.service.model.request.annual;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ResetInspectionDto {
	
	@Min(value=1)
	@NotNull(message="userId不能为空")
	private Long userId;
	
	private Long id;
	
	private String inspectionNextTime;
	
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
	public String getInspectionNextTime() {
		return inspectionNextTime;
	}
	public void setInspectionNextTime(String inspectionNextTime) {
		this.inspectionNextTime = inspectionNextTime;
	}

}
