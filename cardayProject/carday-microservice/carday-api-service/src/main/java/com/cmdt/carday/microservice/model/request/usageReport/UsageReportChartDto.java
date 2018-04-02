package com.cmdt.carday.microservice.model.request.usageReport;

import com.cmdt.carrental.common.util.Patterns;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

public class UsageReportChartDto implements Serializable {
	
	private static final long serialVersionUID = -2190178091479461611L;
	
	@NotNull(message = "userId不能为空")
	private Long userId;
	
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS, message = "startDate不能为空，应为yyyy-MM-dd HH:mm:ss")
	private Date startDate; //开始时间
	
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS, message = "endDate不能为空，应为yyyy-MM-dd HH:mm:ss")
	private Date endDate;  //结束时间
	
	@NotNull(message="orgId 不能为空")
	private Long orgId;  //组织id
	
	@NotNull(message="selfDept 不能为空")
	private Boolean selfDept; //本部门
	
	@NotNull(message="childDept 不能为空")
	private Boolean childDept; //子部门

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	
}
