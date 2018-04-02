package com.cmdt.carrental.platform.service.model.request.marker;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MarkerPageDto {
	
	@NotNull(message="userId不能为空")
	private Long userId;
	
	private String geofenceName;
	
	@NotNull(message="currentPage不能为空")
	@Digits(message="currentPage格式错误,最小:1,最大:"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
    private Integer currentPage;
    
	@NotNull(message="numPerPage不能为空")
    @Digits(message="numPerPage格式错误,最小1,最大:"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
    private Integer numPerPage;
	
	private Long organizationId;
	
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public String getGeofenceName() {
		return geofenceName;
	}
	
	public void setGeofenceName(String geofenceName) {
		this.geofenceName = geofenceName;
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
	
	public Long getOrganizationId() {
		return organizationId;
	}
	
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
}
