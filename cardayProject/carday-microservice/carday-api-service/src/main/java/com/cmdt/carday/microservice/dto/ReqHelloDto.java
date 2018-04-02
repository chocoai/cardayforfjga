package com.cmdt.carday.microservice.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.cmdt.carday.microservice.util.Patterns;


public class ReqHelloDto {
	 
	private String name;
	
	@NotNull
    @Min(1)
	private Integer limit;
	
	@NotNull
    @Max(1000)
	private Integer offset;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		return "ReqHelloDto [name=" + name + ", limit=" + limit + ", offset=" + offset + "]";
	}
	
}
