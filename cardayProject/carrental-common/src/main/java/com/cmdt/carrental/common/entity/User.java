package com.cmdt.carrental.common.entity;


import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id; //编号
    private Long organizationId; //所属公司
    private String organizationName; //所属公司
    private String username; //用户名
    private String password; //密码
    private String salt; //加密密码的盐
    private Long roleId; //拥有的角色列表
    private String roleName;
    private Long userCategory;//0:超级管理员  1:租户管理员 2:企业管理员 3.部门管理员  4：员工  5:司机
    private String realname;
    private String phone;
    private String email;
    private Boolean locked = Boolean.FALSE;
    private String token;
    private String shortname; //用于登录登出设置
    private String iamId; //Iamid
    private String IDNumber;
    //机构代码
    private String institutionCode;
    //机构性质
    private String institutionFeature;
    //机构级别
    private String institutionLevel;
    //是否是机构
    private Boolean isInstitution;

    /** 是否拥有特殊警务权限 */
    private Boolean hasSpecialServicePerm;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

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

	public Long getUserCategory() {
		return userCategory;
	}

	public void setUserCategory(Long userCategory) {
		this.userCategory = userCategory;
	}

	public String getCredentialsSalt() {
        return username + salt;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

    public Boolean getHasSpecialServicePerm() {
        return hasSpecialServicePerm;
    }

    public void setHasSpecialServicePerm(Boolean hasSpecialServicePerm) {
        this.hasSpecialServicePerm = hasSpecialServicePerm;
    }

    public String getIamId() {
		return iamId;
	}

	public void setIamId(String iamId) {
		this.iamId = iamId;
	}

	//超级管理员
    public boolean isSuperAdmin(){
    	return (
    			Long.valueOf(-9).equals(userCategory) 
    			|| Long.valueOf(-1).equals(userCategory)
    			|| Long.valueOf(-2).equals(userCategory)
    			) ? true : false;
    }
    
    //租户管理员
    public boolean isRentAdmin(){
    	return Long.valueOf(1).equals(userCategory) ? true : false;
    }
    
    //TODO 企业管理员 [2 or 6]
    public boolean isEntAdmin(){
    	return (Long.valueOf(2).equals(userCategory) || Long.valueOf(6).equals(userCategory)
        || Long.valueOf(12).equals(userCategory)) ? true : false;
    }
    
    //部门管理员
    public boolean isDeptAdmin(){
    	return ( Long.valueOf(3).equals(userCategory) || Long.valueOf(12).equals(userCategory)||
                Long.valueOf(13).equals(userCategory) || Long.valueOf(14).equals(userCategory)) ? true : false;
    }
    
    //企业或部门管理员
    public boolean isAdmin(){
    	return Long.valueOf(2).equals(userCategory) || Long.valueOf(3).equals(userCategory) ? true : false;
    }
    
    //员工
    public boolean isEndUser(){
    	return Long.valueOf(4).equals(userCategory) ? true : false;
    }
    
    //司机
    public boolean isDriver(){
    	return Long.valueOf(5).equals(userCategory) ? true : false;
    }
    
    //租车公司管理员(特殊企业管理员)
    public boolean isRentEnterpriseAdmin(){
    	return Long.valueOf(6).equals(userCategory) ? true : false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", organizationId=" + organizationId + ", organizationName=" + organizationName
				+ ", username=" + username + ", password=" + password + ", salt=" + salt + ", roleId=" + roleId
				+ ", roleName=" + roleName + ", userCategory=" + userCategory + ", realname=" + realname + ", phone="
				+ phone + ", email=" + email + ", locked=" + locked + ", token=" + token + ", shortname=" + shortname
				+ ", iamId=" + iamId + ", IDNumber=" + IDNumber + ", institutionCode=" + institutionCode
				+ ", institutionFeature=" + institutionFeature + ", institutionLevel=" + institutionLevel
				+ ", isInstitution=" + isInstitution + ", hasSpecialServicePerm=" + hasSpecialServicePerm + "]";
	}

	public String getIDNumber() {
		return IDNumber;
	}

	public void setIDNumber(String iDNumber) {
		IDNumber = iDNumber;
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

	public void setIsInstitution(Boolean isInstitution) {
		this.isInstitution = isInstitution;
	}
	
	
	
	
}
