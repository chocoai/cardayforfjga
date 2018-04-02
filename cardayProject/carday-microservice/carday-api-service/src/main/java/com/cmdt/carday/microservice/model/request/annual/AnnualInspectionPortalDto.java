package com.cmdt.carday.microservice.model.request.annual;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
@ApiModel(value = "AnnualInspectionPortalDto", description = "信息描述")
public class AnnualInspectionPortalDto {

	@ApiModelProperty("用户ID")
	@Min(value=1)
	@NotNull(message="userId不能为空")
	private Long userId;
	
	@ApiModelProperty("企业ID")
	private Long entId;
	
	@ApiModelProperty("分页当前页")
	@Min(value=1)
	@NotNull(message="CurrentPage不能为空")
	private int currentPage;
	
	@ApiModelProperty("每页表示数据数")
	@Min(value=1)
	@NotNull(message="numPerPage不能为空")
	private int numPerPage;
	
	@ApiModelProperty("车牌号")
	private String vehicleNumber;
	
	@ApiModelProperty("部门ID")
	@Min(value=1)
	@NotNull(message="deptId不能为空")
	private Long deptId;
	
	@ApiModelProperty("部门id列表")
	private List<Long> deptIdList;
	
	@ApiModelProperty("保险状态(-1代表已过期, 1M代表一月后, 7或30代表间隔7天或30天)")
	private String insuranceStatus;
	@ApiModelProperty("年检状态(-1代表已过期, 1M代表一月后, 7或30代表间隔7天或30天)")
	private String inspectionStatus;
	
	@ApiModelProperty("本部门checkbox")
	private boolean selfDept;
	
	@ApiModelProperty("子部门checkbox")
	private boolean childDept;
	
	public Long getEntId() {
		return entId;
	}
	public void setEntId(Long entId) {
		this.entId = entId;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getNumPerPage() {
		return numPerPage;
	}
	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}
	public String getVehicleNumber() {
		return vehicleNumber;
	}
	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public String getInsuranceStatus() {
		return insuranceStatus;
	}
	public void setInsuranceStatus(String insuranceStatus) {
		this.insuranceStatus = insuranceStatus;
	}
	public String getInspectionStatus() {
		return inspectionStatus;
	}
	public void setInspectionStatus(String inspectionStatus) {
		this.inspectionStatus = inspectionStatus;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public boolean getSelfDept() {
		return selfDept;
	}
	public void setSelfDept(boolean selfDept) {
		this.selfDept = selfDept;
	}
	public boolean getChildDept() {
		return childDept;
	}
	public void setChildDept(boolean childDept) {
		this.childDept = childDept;
	}
	public List<Long> getDeptIdList() {
		return deptIdList;
	}
	public void setDeptIdList(List<Long> deptIdList) {
		this.deptIdList = deptIdList;
	}
}
