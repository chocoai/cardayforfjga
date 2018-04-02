package com.cmdt.carrental.platform.service.model.request.driver;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DriverDelToDep {

	@NotNull(message="ids不能为空")
	private String ids; //需要分配部门的对象编号
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
}
