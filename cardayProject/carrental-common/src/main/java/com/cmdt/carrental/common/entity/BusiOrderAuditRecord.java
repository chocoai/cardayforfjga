package com.cmdt.carrental.common.entity;

import java.io.Serializable;
import java.util.Date;

public class BusiOrderAuditRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
    private Long orderId;//订单id
    private Long auditUserId;//审核人
    private String auditUserName;
    private String auditUserPhone;
    private Integer status;//1:审核通过(即:待排车),5:被驳回
    private String refuseComments;
//    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss", timezone = "GMT+8")
    private Date auditTime;//审核时间
    private String auditTimeF;//审核时间格式化yyyy-MM-dd HH24:mi:ss
    public BusiOrderAuditRecord() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(Long auditUserId) {
		this.auditUserId = auditUserId;
	}

	public String getAuditUserName() {
		return auditUserName;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}

	public String getAuditUserPhone() {
		return auditUserPhone;
	}

	public void setAuditUserPhone(String auditUserPhone) {
		this.auditUserPhone = auditUserPhone;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRefuseComments() {
		return refuseComments;
	}

	public void setRefuseComments(String refuseComments) {
		this.refuseComments = refuseComments;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getAuditTimeF() {
		return auditTimeF;
	}

	public void setAuditTimeF(String auditTimeF) {
		this.auditTimeF = auditTimeF;
	}

}
