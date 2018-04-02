package com.cmdt.carday.microservice.model.request.warehouse;

import javax.validation.constraints.NotNull;

public class WarehouseDto {

	private Long id;
	@NotNull(message="warehouseName不能为空")
	private String warehouseName;
	@NotNull(message="warehouseAddress不能为空")
	private String warehouseAddress;
	@NotNull(message="warehousePhone不能为空")
	private String warehousePhone;
	@NotNull(message="charePerson不能为空")
	private String charePerson;
	@NotNull(message="charePersonPhone不能为空")
	private String charePersonPhone;
	private String comments;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public String getWarehouseAddress() {
		return warehouseAddress;
	}
	public void setWarehouseAddress(String warehouseAddress) {
		this.warehouseAddress = warehouseAddress;
	}
	public String getWarehousePhone() {
		return warehousePhone;
	}
	public void setWarehousePhone(String warehousePhone) {
		this.warehousePhone = warehousePhone;
	}
	public String getCharePerson() {
		return charePerson;
	}
	public void setCharePerson(String charePerson) {
		this.charePerson = charePerson;
	}
	public String getCharePersonPhone() {
		return charePersonPhone;
	}
	public void setCharePersonPhone(String charePersonPhone) {
		this.charePersonPhone = charePersonPhone;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
}
