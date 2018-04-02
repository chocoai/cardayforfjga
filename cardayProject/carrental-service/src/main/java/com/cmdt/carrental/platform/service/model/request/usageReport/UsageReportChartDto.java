package com.cmdt.carrental.platform.service.model.request.usageReport;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carrental.common.util.Patterns;

public class UsageReportChartDto implements Serializable {
	
	private static final long serialVersionUID = -2190178091479461611L;
	
	@NotNull(message = "userId不能为空")
	private Long userId;
	
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS, message = "starttime不能为空，应为yyyy-MM-dd HH:mm:ss")
	private Date starttime; //开始时间
	
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS, message = "endtime不能为空，应为yyyy-MM-dd HH:mm:ss")
	private Date endtime;  //结束时间
	
	@NotNull(message="orgId 不能为空")
	private Long orgId;  //组织id
	
	@NotNull(message="selfDept 不能为空")
	private Boolean selfDept; //本部门
	
	@NotNull(message="childDept 不能为空")
	private Boolean childDept; //子部门
	
	private Integer currentPage;
	
    private Integer numPerPage;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getStarttime() {
		return starttime;
	}

	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	public Date getEndtime() {
		return endtime;
	}

	public void setEndtime(Date endtime) {
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
