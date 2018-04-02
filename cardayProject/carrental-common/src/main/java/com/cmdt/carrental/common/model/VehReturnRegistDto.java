package com.cmdt.carrental.common.model;

import java.io.Serializable;

public class VehReturnRegistDto implements Serializable{

	private static final long serialVersionUID = 1877565731396205383L;
	
	/* 返回后公里数 */
	private Long edMileage;
	
	/* 实际返回时间-----调度员填写 */
	private String factEdTimeF;
	
	/* 订单Id */
	private Long id;

	public Long getEdMileage() {
		return edMileage;
	}

	public void setEdMileage(Long edMileage) {
		this.edMileage = edMileage;
	}

	public String getFactEdTimeF() {
		return factEdTimeF;
	}

	public void setFactEdTimeF(String factEdTimeF) {
		this.factEdTimeF = factEdTimeF;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
