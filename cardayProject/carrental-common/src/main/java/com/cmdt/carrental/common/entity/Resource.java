package com.cmdt.carrental.common.entity;

import java.io.Serializable;

public class Resource implements Serializable {
	private static final long serialVersionUID = -1965180098116835170L;
	private Long id; //编号
    private String name; //资源名称
//    private ResourceType type = ResourceType.menu; //资源类型
    private String type = "menu"; //资源类型,menu or button
    private String url; //资源路径
    private String permission; //权限字符串
    private Long parentId; //父编号
    private String parentIds; //父编号列表
    private Boolean available = Boolean.TRUE;
    private String description;
    public static enum ResourceType {
        menu("菜单"), button("按钮");

        private final String info;
        private ResourceType(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isRootNode() {
        return parentId == 0;
    }

    public String makeSelfAsParentIds() {
        return getParentIds() + "," + getId();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        if (id != null ? !id.equals(resource.id) : resource.id != null) return false;

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
		buffer.append("type:").append(type).append(",");
		buffer.append("permission:").append(permission).append(",");
		buffer.append("parentId:").append(parentId).append(",");
		buffer.append("parentIds:").append(parentIds).append(",");
		buffer.append("available:").append(available);
		buffer.append("}");
		return buffer.toString();
    }
}
