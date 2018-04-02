package com.cmdt.carrental.common.model;


public class MarkerQueryDto	implements java.io.Serializable{
	
	private static final long serialVersionUID = 1582441921564798954L;

	private Long markerId ;			//战点名
	
	private String geofenceName; 	//站点名
	
	private Integer currentPage;
	 
	private Integer numPerPage;
	 
	private Long organizationId;   //组织id
	 
	private Boolean isRentAdmin;
	
	private Boolean isEntAdmin; 

	public String getGeofenceName() {
		return geofenceName;
	}

	public void setGeofenceName(String geofenceName) {
		this.geofenceName = geofenceName;
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

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public Boolean getIsRentAdmin() {
		return isRentAdmin;
	}

	public void setIsRentAdmin(Boolean isRentAdmin) {
		this.isRentAdmin = isRentAdmin;
	}

	public Boolean getIsEntAdmin() {
		return isEntAdmin;
	}

	public void setIsEntAdmin(Boolean isEntAdmin) {
		this.isEntAdmin = isEntAdmin;
	}

	public Long getMarkerId() {
		return markerId;
	}

	public void setMarkerId(Long markerId) {
		this.markerId = markerId;
	}
	
}
