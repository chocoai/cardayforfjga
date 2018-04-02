package com.cmdt.carrental.common.entity;

import java.io.Serializable;

public class Role implements Serializable {
	private static final long serialVersionUID = -3034702208326134711L;
	private Long id; //编号
    private Long templateId; //模板类型,0:超级管理员,1:租户管理员,2:企业管理员,3:部门管理员,4:员工,5:司机
    private String templateName;//角色类型名称,比如:超级管理员
    private Long organizationId;//租户ID
    private String organizationName;//租户名称
    private String role; //角色标识 程序中判断使用,如"admin"
    private String description; //角色描述,UI界面显示使用
    private String resourceIds; //拥有的资源
    private String resourceNames; //拥有的资源名称
    private Boolean available = Boolean.TRUE; //是否可用,如果不可用将不会添加给用户

    public Role() {
    }

    public Role(String role, String description, Boolean available) {
        this.role = role;
        this.description = description;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Long getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
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

	public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(String resourceIds) {
        this.resourceIds = resourceIds;
    }

    public String getResourceNames() {
		return resourceNames;
	}

	public void setResourceNames(String resourceNames) {
		this.resourceNames = resourceNames;
	}

	public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (id != null ? !id.equals(role.id) : role.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
    	StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		buffer.append("id:").append(id).append(",");
		buffer.append("role:").append(role).append(",");
		buffer.append("templateId:").append(templateId).append(",");
		buffer.append("organizationId:").append(organizationId).append(",");
		buffer.append("organizationName:").append(organizationName).append(",");
		buffer.append("resourceIds:").append(resourceIds).append(",");
		buffer.append("resourceNames:").append(resourceNames).append(",");
		buffer.append("description:").append(description).append(",");
		buffer.append("available:").append(available);
		buffer.append("}");
		return buffer.toString();
    }
}
