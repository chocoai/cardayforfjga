package com.cmdt.carrental.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OrgAndParentOrgByOrgIdModel {
	private Long id;
	
	private String organizationId;
	
	private String name; //组织机构名称
	
	private Integer level;//组织层级
	
	private Long parentId;//直接父机构ID
	/** 单位所属部门 */
	private String parentName;//直接父机构名称
	
//	private Boolean flag=Boolean.TRUE;   //false:部门级别和上级部门信息不可编辑，true:可以编辑

	//机构代码
	private String institutionCode;
	//机构性质
	private String institutionFeature;
	//机构级别
	private String institutionLevel;
	//是否是机构
	private Boolean isInstitution;
	/** 单位地址 */
	private String address;
	/** 车辆管理负责人 */
	private String vehicleAdministrator;
	/** 联系电话 */
	private String phone;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getInstitutionCode() {
		return institutionCode;
	}

	public void setInstitutionCode(String institutionCode) {
		this.institutionCode = institutionCode;
	}

	public String getInstitutionFeature() {
		return institutionFeature;
	}

	public void setInstitutionFeature(String institutionFeature) {
		this.institutionFeature = institutionFeature;
	}

	public String getInstitutionLevel() {
		return institutionLevel;
	}

	public void setInstitutionLevel(String institutionLevel) {
		this.institutionLevel = institutionLevel;
	}

	public Boolean getIsInstitution() {
		return isInstitution;
	}

	public void setIsInstitution(Boolean institution) {
		isInstitution = institution;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getVehicleAdministrator() {
		return vehicleAdministrator;
	}

	public void setVehicleAdministrator(String vehicleAdministrator) {
		this.vehicleAdministrator = vehicleAdministrator;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
//	@Override
//	public String toString() {
//		return "OrgAndParentOrgByOrgIdModel [id=" + id + ", organizationId=" + organizationId + ", name=" + name
//				+ ", level=" + level + ", parentId=" + parentId + ", parentName=" + parentName + "]";
//	}

	@Override
	public String toString() {
		return "OrgAndParentOrgByOrgIdModel[" +
				"id=" + id +
				", organizationId='" + organizationId + '\'' +
				", name='" + name + '\'' +
				", level=" + level +
				", parentId=" + parentId +
				", parentName='" + parentName + '\'' +
				", institutionCode='" + institutionCode + '\'' +
				", institutionFeature='" + institutionFeature + '\'' +
				", institutionLevel='" + institutionLevel + '\'' +
				", address='" + address + '\'' +
				", phone='" + phone + '\'' +
				", vehicleAdministrator='" + vehicleAdministrator + '\'' +
				", isInstitution=" + isInstitution +
				']';
	}





/*	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}*/


	
}
