package com.cmdt.carday.microservice.dto;

import java.util.List;


public class HelloPutDto {
	
	private List<HelloDto> dtos;

	public List<HelloDto> getDtos() {
		return dtos;
	}

	public void setDtos(List<HelloDto> dtos) {
		this.dtos = dtos;
	}

	@Override
	public String toString() {
		return "HelloPutDto [dtos=" + dtos + "]";
	}
	
}
