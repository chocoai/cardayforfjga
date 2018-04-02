package com.cmdt.carday.microservice.model.request.vehicle;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
public class VehicleListDto implements Serializable{
	

	private static final long serialVersionUID = 4382516321774755725L;

	@ApiModelProperty(value="登录用户ID",required=true)
	@Min(value=1)
	@NotNull(message="userId不能为空")
	private Long userId;
	
	@ApiModelProperty(value="分页，当前页码",required=true)
	@Min(value=1)
	@NotNull(message="currentPage不能为空")
	private Integer currentPage;
	
	@ApiModelProperty(value="分页，每页条数",required=true)
	@Min(value=1)
	@NotNull(message="numPerPage不能为空")
    private Integer numPerPage;
	
	@ApiModelProperty(value="车牌号")
    private String vehicleNumber;
    
	@ApiModelProperty(value="车牌类型， -1：所有类型，0：经济型，1：舒适性,2:商务型，3：豪华型")
    private Long vehicleType; // -1：所有类型，0：经济型，1：舒适性,2:商务型，3：豪华型
    
	@ApiModelProperty(value="分配企业，对于租车公司，其值为企业id，默认-1代表所有")
    private Long arrangeEnt;
    
	@ApiModelProperty(value="车辆来源，创建车辆的企业id，默认-1代表所有")
    private Long fromOrgId;
    
	@ApiModelProperty(value="组织机构树选中的部门节点Id")
    private Long deptId; //组织机构树选中的部门节点Id
    
	@ApiModelProperty(value="本部门是否勾选，1代表勾选，0代表不勾选")
    private Boolean selfDept=true;  //本部门是否勾选
    
	@ApiModelProperty(value="子部门是否勾选，1代表勾选，0代表不勾选")
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
