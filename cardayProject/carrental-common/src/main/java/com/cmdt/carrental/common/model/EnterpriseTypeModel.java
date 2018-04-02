package com.cmdt.carrental.common.model;

public class EnterpriseTypeModel {

	private Long id; // 编号
	private String enterprisestype; // '0':租车公司 '1':普通企业
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEnterprisestype() {
		return enterprisestype;
	}
	public void setEnterprisestype(String enterprisestype) {
		this.enterprisestype = enterprisestype;
	}
	
}
