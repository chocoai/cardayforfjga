package com.cmdt.carday.microservice.model.request.alert;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carrental.common.util.Patterns;

import io.swagger.annotations.ApiModelProperty;

public class AlertDataTypeReportDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="开始时间",required=true)
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD,message="开始时间日期格式错误，应为yyyy-mm-dd")
	@NotNull(message="开始时间不能为空")
	private String startDay;
	
	@ApiModelProperty(value="结束时间",required=true)
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD,message="结束时间日期格式错误，应为yyyy-mm-dd")
	@NotNull(message="结束时间不能为空")
	private String endDay;
	
	@ApiModelProperty(value="组织ID",required=true)
	@NotNull(message="组织ID不能为空")
	private Long orgId;
	
	@ApiModelProperty(value="本部门是否勾选，1代表勾选，0代表不勾选",required=true)
	@NotNull(message="selfDept 不能为空")
	private Boolean selfDept;
	
	@ApiModelProperty(value="子部门是否勾选，1代表勾选，0代表不勾选",required=true)
	@NotNull(message="childDept 不能为空")
	private Boolean childDept;
	
	@ApiModelProperty(value="报警类型",required=true)
	@NotNull(message="报警类型不能为空")
	private String alertType;

	public String getStartDay() {
		return startDay;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}

	public String getEndDay() {
		return endDay;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

}
