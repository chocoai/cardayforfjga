package com.cmdt.carday.microservice.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.cmdt.carday.microservice.util.Patterns;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Hello", description = "信息描述")
public class HelloDto {
	
	@ApiModelProperty("顺序编号")
	@NotNull(message = "编号不能为空") 
	private Long id;
	
	
	@ApiModelProperty("用户姓名")
	@Size (min = 1, max = 20, message = "用户名长度必须位于1到20之间") 
	private String name;
	
	
	@ApiModelProperty("访问日期")
	@Pattern(regexp = Patterns.REG_DATE_YYYY_MM_DD_HH24_MI_SS,message="To日期格式错误，应为yyyy-mm-dd hh:mm:ss!")
    private String date;
	
	
	public HelloDto(){
		super();
	}
	
	public HelloDto(Long id, String name, String date){
		this.id = id;
		this.name = name;
		this.date = date;
	}
	
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "HelloDto [id=" + id + ", name=" + name + ", date=" + date + "]";
	}
	
}
