package com.cmdt.carday.microservice.model.request.usageReport;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carrental.common.util.Patterns;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

public class UsageReportPageDto implements Serializable {
	
	private static final long serialVersionUID = -2190178091479461611L;
	
	@ApiModelProperty(value="开始时间",required=true)
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD,message="开始时间日期格式错误，应为yyyy-mm-dd")
	private String starttime;
	
	@ApiModelProperty(value="结束时间",required=true)
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD,message="结束时间日期格式错误，应为yyyy-mm-dd")
	private String endtime;
	
	@ApiModelProperty(value="组织id",required=true)
	@NotNull(message="orgId 不能为空")
	private Long orgId;
	
	@ApiModelProperty(value="本部门是否勾选，1代表勾选，0代表不勾选",required=true)
	@NotNull(message="selfDept 不能为空")
	private Boolean selfDept;
	
	@ApiModelProperty(value="子部门是否勾选，1代表勾选，0代表不勾选",required=true)
	@NotNull(message="childDept 不能为空")
	private Boolean childDept;
	
	@ApiModelProperty(value="分页当前页",required=true)
	@Digits(message="currentPage格式错误,最小1,最大:"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
	private Integer currentPage;
	
	@ApiModelProperty(value="每页表示数据数",required=true)
	@Digits(message="numPerPage格式错误,最小1,最大:"+ Integer.MAX_VALUE, fraction = 1, integer = Integer.MAX_VALUE)
	private Integer numPerPage;
	
	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
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

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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
