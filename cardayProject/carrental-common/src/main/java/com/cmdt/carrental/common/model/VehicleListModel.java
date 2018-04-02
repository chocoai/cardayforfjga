package com.cmdt.carrental.common.model;


public class VehicleListModel {
	
	private Long organizationId;  //当前登录用户所在的组织Id
	
	private Integer currentPage;
	
    private Integer numPerPage;
	
    private String vehicleNumber;
    
    private  String vehicleType;
    
    private Long arrangeEnt;
    
    private Long fromOrgId;
    
    private Long deptId; //组织机构树选中的部门节点Id
    
    private Boolean selfDept;  //本部门是否勾选
    
	private Boolean childDept; //子部门是否勾选
	
	//for fjga-req
    private Integer vehStatus;
    private Integer enableSecret;

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getNumPerPage() {
		return numPerPage;
	}

	public void setNumPerPage(Integer numPerPage) {
		this.numPerPage = numPerPage;
	}

	public String getVehicleNumber() {
		return vehicleNumber;
	}

	public void setVehicleNumber(String vehicleNumber) {
		this.vehicleNumber = vehicleNumber;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public Long getArrangeEnt() {
		return arrangeEnt;
	}

	public void setArrangeEnt(Long arrangeEnt) {
		this.arrangeEnt = arrangeEnt;
	}

	public Long getFromOrgId() {
		return fromOrgId;
	}

	public void setFromOrgId(Long fromOrgId) {
		this.fromOrgId = fromOrgId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Boolean getSelfDept() {
		return selfDept;
	}

	public void setSelfDept(Boolean selfDept) {
		this.selfDept = selfDept;
	}

	public Boolean getChildDept() {
		return childDept;
	}

	public void setChildDept(Boolean childDept) {
		this.childDept = childDept;
	}

    public Integer getVehStatus()
    {
        return vehStatus;
    }

    public void setVehStatus(Integer vehStatus)
    {
        this.vehStatus = vehStatus;
    }

    public Integer getEnableSecret()
    {
        return enableSecret;
    }

    public void setEnableSecret(Integer enableSecret)
    {
        this.enableSecret = enableSecret;
    }

}
