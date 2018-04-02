package com.cmdt.carrental.common.entity;

import java.io.Serializable;

public class RoleTemplate implements Serializable {
	private static final long serialVersionUID = 1222815966641045194L;
	private Long id; //编号
    private String name;//模板名称
    private String resourceIds; //拥有的资源
    private String resourceNames; //拥有的资源名称
    private String description; //模板备注描述

    public RoleTemplate() {
    }

    public RoleTemplate(String name, String resourceIds, String description) {
        this.name = name;
        this.resourceIds = resourceIds;
        this.description = description;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RoleTemplate role = (RoleTemplate) o;

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
		buffer.append("name:").append(name).append(",");
		buffer.append("resourceIds:").append(resourceIds).append(",");
		buffer.append("resourceNames:").append(resourceNames).append(",");
		buffer.append("description:").append(description);
		buffer.append("}");
		return buffer.toString();
    }
}
