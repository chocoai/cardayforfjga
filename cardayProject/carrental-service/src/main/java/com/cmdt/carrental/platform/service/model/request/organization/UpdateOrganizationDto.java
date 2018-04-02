package com.cmdt.carrental.platform.service.model.request.organization;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cmdt.carrental.platform.service.common.Patterns;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UpdateOrganizationDto {
	
	private static final long serialVersionUID = 1L;
	
	@NotNull(message="id不能为空")
	private Long id;
	@NotNull(message="name不能为空")
	private String name; //组织机构名称
	private String shortname; //组织描述
	@NotNull(message="address不能为空")
	private String address;
	private String introduction;
	@NotNull(message="linkman不能为空")
	private String linkman;//企业联系人真实姓名
	@NotNull(message="linkmanPhone不能为空")
	@Pattern(regexp = Patterns.REG_PHONE,message="phone格式错误，应为以13,15,17,18开头的11位数字")
	private String linkmanPhone;//企业联系人电话
	private String linkmanEmail;//企业联系人邮箱
	@NotNull(message="startTime不能为空")
	private String startTime;
	@NotNull(message="endTime不能为空")
	private String endTime;
	@NotNull(message="status不能为空")
	private String status;
	@NotNull(message="businessType不能为空")
	private List<String> businessType;
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
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getLinkmanPhone() {
		return linkmanPhone;
	}
	public void setLinkmanPhone(String linkmanPhone) {
		this.linkmanPhone = linkmanPhone;
	}
	public String getLinkmanEmail() {
		return linkmanEmail;
	}
	public void setLinkmanEmail(String linkmanEmail) {
		this.linkmanEmail = linkmanEmail;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<String> getBusinessType() {
		return businessType;
	}
	public void setBusinessType(List<String> businessType) {
		this.businessType = businessType;
	}
    
}
