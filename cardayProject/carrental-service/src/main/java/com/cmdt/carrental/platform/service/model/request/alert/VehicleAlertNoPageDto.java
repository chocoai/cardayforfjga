package com.cmdt.carrental.platform.service.model.request.alert;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class VehicleAlertNoPageDto implements Serializable{
	
	private static final long serialVersionUID = -2047980411187177705L;

	@Min(value=1)
	@NotNull(message="userId不能为空")
	private Long userId;
	
	@Min(value=1)
	@NotNull(message="isPre不能为空")
	private Boolean isPre;
	
	public Boolean getIsPre() {
		return isPre;
	}
	public void setIsPre(Boolean isPre) {
		this.isPre = isPre;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
