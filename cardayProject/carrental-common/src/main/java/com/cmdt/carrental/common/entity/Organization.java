package com.cmdt.carrental.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Organization implements Serializable {
	
	private static final long serialVersionUID = -2016704651464512508L;
	
	private Long id; //编号
	
	private String organizationId;//组织机构ID   如公司代码_部门级别代码_序号，例如：GEHUA_L1_001
	
	private String name; //组织机构名称
    
	private String shortname; //组织描述
   
	private String linkman;//企业联系人真实姓名
    
	private String linkmanPhone;//企业联系人电话
    
	private String linkmanEmail;//企业联系人邮箱
    
	private Long vehileNum;
    
	private String city;
   
	private Date startTime;
    
	private Date endTime;
    
	private String address;
   
	private String introduction;
   
	private Long parentId; //父编号
   
	private String parentIds; //父编号列表，如1/2
   
	private String status;//0:待审核  1:审核不通过 2:待服务开通 3：服务中 4:服务到期 5： 服务暂停
    //bind each comment together, like
    //20170101 Admin1 企业名称有误
    //20170102 Admin2 服务时间有误
    private String enterprisesType;   //(0:租车公司，1:用车企业)
  
    private String businessType;  //业务类型(自由车,长租车)
   
    private String comments;
    
    private Integer depth;
    
    private Boolean isValid;
    
    private Double availableCredit;
    
    private Double limitedCredit;

    //机构代码
    private String institutionCode;
    //机构性质
    private String institutionFeature;
    //机构级别
    private String institutionLevel;
    //是否是机构
    private Boolean isInstitution;

    public String getEnterprisesType() {
		return enterprisesType;
	}

	public void setEnterprisesType(String enterprisesType) {
		this.enterprisesType = enterprisesType;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
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

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
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

	public Long getVehileNum() {
		return vehileNum;
	}

	public void setVehileNum(Long vehileNum) {
		this.vehileNum = vehileNum;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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

	public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }
    
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	@JsonIgnore  
	public boolean isRootNode() {
        return parentId == 0;
    }
	@JsonIgnore  
    public String makeSelfAsParentIds() {
        return getParentIds() + "," + getId();
    }


	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}


	public Boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
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

	@Override
    @JsonIgnore  
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Double getAvailableCredit() {
		return availableCredit;
	}

	public void setAvailableCredit(Double availableCredit) {
		this.availableCredit = availableCredit;
	}

	public Double getLimitedCredit() {
		return limitedCredit;
	}

	public void setLimitedCredit(Double limitedCredit) {
		this.limitedCredit = limitedCredit;
	}
	
	
}
