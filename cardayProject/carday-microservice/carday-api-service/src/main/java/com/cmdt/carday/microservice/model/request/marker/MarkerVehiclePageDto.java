package com.cmdt.carday.microservice.model.request.marker;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
public class MarkerVehiclePageDto {
	
	@ApiModelProperty(value="登录用户ID",required=true)
	@NotNull(message="userId不能为空")
	private Long userId;
	
	@ApiModelProperty(value="地理围栏ID",required=true)
	@NotNull(message="markerId不能为空")
	private Long markerId;
	
	@ApiModelProperty(value="分页，当前页码",required=true)
    @NotNull(message="currentPage不能为空")
    @Min(value=1)
    private Integer currentPage;
    
	@ApiModelProperty(value="分页，每页条数",required=true)
    @NotNull(message="numPerPage不能为空")
    @Min(value=1)
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
	
	public Long getMarkerId() {
		return markerId;
	}
	public void setMarkerId(Long markerId) {
		this.markerId = markerId;
	}

}
