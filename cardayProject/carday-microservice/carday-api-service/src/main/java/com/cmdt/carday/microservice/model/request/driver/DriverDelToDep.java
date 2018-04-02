package com.cmdt.carday.microservice.model.request.driver;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
@ApiModel(value = "DriverDelToDep", description = "信息描述")
public class DriverDelToDep {

	@ApiModelProperty("需要移除部门的对象编号")
	@NotNull(message="ids不能为空")
	private String ids; //需要移除部门的对象编号
	
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	
}
