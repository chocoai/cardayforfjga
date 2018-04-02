package com.cmdt.carrental.common.model;

import java.io.Serializable;

/**
 * 租车公司所关联的企业列表,包含企业的计划用车数，实际用车数列表
 *
 */
public class VehicleEnterpriseModel implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private Integer vehiclenumber;
	private Integer actualVehiclenumber;
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getVehiclenumber() {
		return vehiclenumber;
	}
	public void setVehiclenumber(Integer vehiclenumber) {
		this.vehiclenumber = vehiclenumber;
	}
	public Integer getActualVehiclenumber() {
		return actualVehiclenumber;
	}
	public void setActualVehiclenumber(Integer actualVehiclenumber) {
		this.actualVehiclenumber = actualVehiclenumber;
	}
    
	
	
	
	

}
