package com.cmdt.carrental.common.model;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import com.cmdt.carrental.common.util.Patterns;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "BusiOrderQuery", description = "订单查询信息")
public class BusiOrderQueryDto implements Serializable{
	
	private static final long serialVersionUID = 5384600884865441327L;
	
	@Min(value=1)
	@NotNull(message="loginUserId不能为空")
	@ApiModelProperty(value = "登录用户ID", required = true)
	private Long loginUserId;

	@ApiModelProperty("订单ID")
	private Long id;//订单ID

	@ApiModelProperty("组织机构ID")
	private Long organizationId;

	@ApiModelProperty("本部门是否勾选")
	private Boolean selfDept;  //本部门是否勾选

	@ApiModelProperty("子部门是否勾选")
	private Boolean childDept; //子部门是否勾选

	@ApiModelProperty("订单用户名")
	private String orderUsername;//real name

	@ApiModelProperty("订单号")
	private String orderNo;
	
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD,message="orderTime format error!")
	@ApiModelProperty("查询的订单日期")
	private String orderTime;

	@ApiModelProperty("订单类型，1:预约订单 / 2:补录订单")
	private Integer orderCat;//1:预约订单,2:补录订单

	@ApiModelProperty("订单状态  0:待审核,1:待排车,2:已排车,5:被驳回,6:已取消,11:已出车,12:已到达出发地,3:进行中/行程中,13:等待中,4:待支付,15:待评价,16:已完成 ")
	private Integer status;
	
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD,message="planTime format error!")
	@ApiModelProperty("订单预计开始时间")
	private String planTime;
	
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD,message="startTime format error!")
	@ApiModelProperty("订单开始日期")
	private String startTime;//用车开始日期
	
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD,message="endTime format error!")
	@ApiModelProperty("订单结束日期")
	private String endTime;//用车结束日期
	
//	@Min(value=1)
//	@NotNull(message="currentPage不能为空")
	@ApiModelProperty("分页，当前页码")
	private Integer currentPage;
	
//	@Min(value=1)
//	@NotNull(message="numPerPage不能为空")
	@ApiModelProperty("分页，每页条数")
	private Integer numPerPage;

	@ApiModelProperty("订单类型，0:企业订单 / 1:网约订单")
	private Integer orderType;

	@ApiModelProperty("混合查询字段")
	private String queryCol;//混合查询字段

	@ApiModelProperty("1: order by t.plan_st_time desc")
	private String colOrderBy;//1: order by t.plan_st_time desc
	//cr2215
	private String app;
	
	// 特殊警务级别
	// 1: 机密 / 2: 绝密 / 3: 免审批
	private Integer secretLevel;
	
	public Long getLoginUserId() {
		return loginUserId;
	}
	public void setLoginUserId(Long loginUserId) {
		this.loginUserId = loginUserId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
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
	public String getOrderUsername() {
		if(StringUtils.isBlank(orderUsername))
			orderUsername=null;
		return orderUsername;
	}
	public void setOrderUsername(String orderUsername) {
		this.orderUsername = orderUsername;
	}
	public String getOrderNo() {
		if(StringUtils.isBlank(orderNo))
			orderNo=null;
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderTime() {
		if(StringUtils.isBlank(orderTime))
			orderTime=null;
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPlanTime() {
		if(StringUtils.isBlank(planTime))
			planTime=null;
		return planTime;
	}
	public void setPlanTime(String planTime) {
		this.planTime = planTime;
	}
	public String getStartTime() {
		if(StringUtils.isBlank(startTime))
			startTime=null;
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		if(StringUtils.isBlank(endTime))
			endTime=null;
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
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
	public Integer getOrderCat() {
		return orderCat;
	}
	public void setOrderCat(Integer orderCat) {
		this.orderCat = orderCat;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public String getQueryCol() {
		if(StringUtils.isBlank(queryCol))
			queryCol=null;
		return queryCol;
	}
	public void setQueryCol(String queryCol) {
		this.queryCol = queryCol;
	}
	public String getColOrderBy() {
		if(StringUtils.isBlank(colOrderBy))
			colOrderBy=null;
		return colOrderBy;
	}
	public void setColOrderBy(String colOrderBy) {
		this.colOrderBy = colOrderBy;
	}
	public String getApp() {
		if(StringUtils.isBlank(app))
			app=null;
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public Integer getSecretLevel() {
		return secretLevel;
	}
	public void setSecretLevel(Integer secretLevel) {
		this.secretLevel = secretLevel;
	}
	
	
}
