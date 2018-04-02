package com.cmdt.carrental.platform.service.model.request.vehicle;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class VehicleListDto implements Serializable{
	

	private static final long serialVersionUID = 4382516321774755725L;

	@Min(value=1)
	@NotNull(message="userId不能为空")
	private Long userId;
	
	@Min(value=1)
	@NotNull(message="PageSize不能为空")
	private Integer currentPage;
	
	@Min(value=1)
	@NotNull(message="CurrentPage不能为空")
    private Integer numPerPage;
	
    private String vehicleNumber;
    
    private Long vehicleType;
    
    private Long arrangeEnt;
    
    private Long fromOrgId;
    
    private Long deptId; //组织机构树选中的部门节点Id
    
    private Boolean selfDept=true;  //本部门是否勾选
    
	private Boolean childDept=true; //子部门是否勾选
	
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
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public Long getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(Long vehicleType) {
		this.vehicleType = vehicleType;
	}
	public Long getArrangeEnt() {
		return arrangeEnt;
	}
	public void setArrangeEnt(Long arrangeEnt) {
		this.arrangeEnt = arrangeEnt;
	}
	public Long getFromOrgId() {
		return fromOrgId;
	}
	public void setFromOrgId(Long fromOrgId) {
		this.fromOrgId = fromOrgId;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public Boolean getSelfDept() {
		return selfDept;
	}
	public void setSelfDept(Boolean selfDept) {
		this.selfDept = selfDept;
	}
	public Boolean getChildDept() {
		return childDept;
	}
	public void setChildDept(Boolean childDept) {
		this.childDept = childDept;
	}
    
}
