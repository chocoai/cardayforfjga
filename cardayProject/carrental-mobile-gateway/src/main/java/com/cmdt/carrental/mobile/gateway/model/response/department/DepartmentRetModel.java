package com.cmdt.carrental.mobile.gateway.model.response.department;

public class DepartmentRetModel {
	
	private Integer deptLv;
	private Long deptPid;
	private Long deptid;
	private String deptName;
	
	public DepartmentRetModel(){
		super();
	};
	
	public DepartmentRetModel(Integer deptLv,Long deptPid,Long deptid,String deptName){
		this.setDeptid(deptid);
		this.setDeptLv(deptLv);
		this.setDeptName(deptName);
		this.setDeptPid(deptPid);
	}
	
	
	public Integer getDeptLv() {
		return deptLv;
	}
	public void setDeptLv(Integer deptLv) {
		this.deptLv = deptLv;
	}
	public Long getDeptPid() {
		return deptPid;
	}
	public void setDeptPid(Long deptPid) {
		this.deptPid = deptPid;
	}
	public Long getDeptid() {
		return deptid;
	}
	public void setDeptid(Long deptid) {
		this.deptid = deptid;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	

}
