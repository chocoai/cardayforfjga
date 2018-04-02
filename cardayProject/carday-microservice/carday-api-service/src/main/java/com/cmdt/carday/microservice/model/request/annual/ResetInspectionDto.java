package com.cmdt.carday.microservice.model.request.annual;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
@ApiModel(value = "ResetInspectionDto", description = "信息描述")
public class ResetInspectionDto {
	
	@ApiModelProperty("用户ID")
	@Min(value=1)
	@NotNull(message="userId不能为空")
	private Long userId;
	
	@ApiModelProperty("年检记录ID")
	private Long id;
	
	@ApiModelProperty("年检时间")
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
