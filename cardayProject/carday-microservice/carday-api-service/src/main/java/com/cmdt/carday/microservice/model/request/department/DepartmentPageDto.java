package com.cmdt.carday.microservice.model.request.department;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
@ApiModel(value = "DepartmentPageDto", description = "信息描述")
public class DepartmentPageDto {

	@ApiModelProperty("用户ID")
	@NotNull(message="userId不能为空")
	private Long userId;
	@ApiModelProperty("分页当前页")
	@Digits(message="currentPage格式错误,最小1,最大:"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
	private Integer currentPage;
	@ApiModelProperty("每页表示数据数")
	@Digits(message="numPerPage格式错误,最小1,最大:"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
    private Integer numPerPage;
    
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public Integer getNumPerPage() {
		return numPerPage;
	}
	public void setNumPerPage(Integer numPerPage) {
		this.numPerPage = numPerPage;
	}

    
	
}
