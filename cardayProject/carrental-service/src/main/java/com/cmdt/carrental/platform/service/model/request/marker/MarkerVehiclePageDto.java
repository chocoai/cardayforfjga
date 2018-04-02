package com.cmdt.carrental.platform.service.model.request.marker;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class MarkerVehiclePageDto {
	
	@NotNull(message="userId不能为空")
	private Long userId;
	
	@NotNull(message="markerId不能为空")
	private Long markerId;
	
	@NotNull(message="currentPage不能为空")
	@Digits(message="currentPage格式错误,最小:1,最大:"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
    private Integer currentPage;
    
	@NotNull(message="numPerPage不能为空")
    @Digits(message="numPerPage格式错误,最小1,最大:"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
    private Integer numPerPage;
	
	private Long organizationId;
	
	private Boolean isRentAdmin;
	
	private Boolean isEntAdmin;
	
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
	public Boolean getIsRentAdmin() {
		return isRentAdmin;
	}
	public void setIsRentAdmin(Boolean isRentAdmin) {
		this.isRentAdmin = isRentAdmin;
	}
	public Boolean getIsEntAdmin() {
		return isEntAdmin;
	}
	public void setIsEntAdmin(Boolean isEntAdmin) {
		this.isEntAdmin = isEntAdmin;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	public Long getMarkerId() {
		return markerId;
	}
	public void setMarkerId(Long markerId) {
		this.markerId = markerId;
	}

}
