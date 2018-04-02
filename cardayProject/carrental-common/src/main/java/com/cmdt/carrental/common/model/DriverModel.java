package com.cmdt.carrental.common.model;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

public class DriverModel implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long id; //编号
    private Long organizationId; //所属公司
    private String organizationName;//名称
    private Long depId;//所属部门
    private String depName;//所属部门名称
    private String sourceName;//司机来源
    private Long stationId;//所属站点
    private String stationName;//所属站点名称
    private String username; //用户名
    private String password; //密码
    private String salt; //加密密码的盐
    private Long roleId; //拥有的角色列表
    private String userCategory;//0:超级管理员  1:租户管理员 2:企业管理员 3.部门管理员  4：员工  5:司机
    private String realname;
    private String phone;
    private String realnameAndPhone;
    private String email;
    private String sex;//司机性别 0:男 1:女
    private String birthday;//司机生日 1983-12-16
    private Integer age;	//司机年龄
    private String licenseType;//驾照类型 C1 or C2
    private String licenseNumber;//驾照号码
    private String licenseBegintime;//初次领证时间  2015-09-12
    private String licenseExpiretime;//驾照到期时间 2011-09-12
    private Integer drivingYears;//驾龄
    private String licenseAttach;//附件保存路径
	private int currentPage;
	private int numPerPage;
	private boolean selfDept;
	private boolean childDept;
    private Boolean locked = Boolean.FALSE;
    
    //司机状态
    private Integer drvStatus;
    private Integer tripQuantity;
    private Long tripMileage;

	// fjga 司机 基本工资
	private Integer salary;

    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public Long getDepId() {
		return depId;
	}
	public void setDepId(Long depId) {
		this.depId = depId;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public String getSourceName() {
		if(StringUtils.isNotBlank(depName)){
			sourceName=this.depName;
		}else{
			sourceName=this.organizationName;
		}
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getUserCategory() {
		return userCategory;
	}
	public void setUserCategory(String userCategory) {
		this.userCategory = userCategory;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRealnameAndPhone() {
		return realnameAndPhone;
	}
	public void setRealnameAndPhone(String realnameAndPhone) {
		this.realnameAndPhone = realnameAndPhone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public String getLicenseNumber() {
		return licenseNumber;
	}
	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
	public String getLicenseBegintime() {
		return licenseBegintime;
	}
	public void setLicenseBegintime(String licenseBegintime) {
		this.licenseBegintime = licenseBegintime;
	}
	public String getLicenseExpiretime() {
		return licenseExpiretime;
	}
	public void setLicenseExpiretime(String licenseExpiretime) {
		this.licenseExpiretime = licenseExpiretime;
	}
	public Integer getDrivingYears() {
		return drivingYears;
	}
	public void setDrivingYears(Integer drivingYears) {
		this.drivingYears = drivingYears;
	}
	public String getLicenseAttach() {
		return licenseAttach;
	}
	public void setLicenseAttach(String licenseAttach) {
		this.licenseAttach = licenseAttach;
	}
	public Boolean getLocked() {
		return locked;
	}
	public void setLocked(Boolean locked) {
		this.locked = locked;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getNumPerPage() {
		return numPerPage;
	}
	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}
	public boolean getSelfDept() {
		return selfDept;
	}
	public void setSelfDept(boolean selfDept) {
		this.selfDept = selfDept;
	}
	public boolean getChildDept() {
		return childDept;
	}
	public void setChildDept(boolean childDept) {
		this.childDept = childDept;
	}
    public Integer getDrvStatus()
    {
        return drvStatus;
    }
    public void setDrvStatus(Integer drvStatus)
    {
        this.drvStatus = drvStatus;
    }
    public Integer getTripQuantity()
    {
        return tripQuantity;
    }
    public void setTripQuantity(Integer tripQuantity)
    {
        this.tripQuantity = tripQuantity;
    }
    public Long getTripMileage()
    {
        return tripMileage;
    }
    public void setTripMileage(Long tripMileage)
    {
        this.tripMileage = tripMileage;
    }

	public Integer getSalary() {
		return salary;
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}
}
