package com.cmdt.carrental.platform.service.model.request.user;

import com.cmdt.carrental.common.entity.Resource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: joe
 * @Date: 17-7-10 下午5:27.
 * @Description:
 */
@JsonIgnoreProperties(value = {"parentIds", "id"})
public class PermissionNode {

    private String permission;

    private String type;

    private List<Long> parentIds = new ArrayList<>();

    private Long id;

    private List<PermissionNode> childs;

//    private PermissionNode parent;

    public PermissionNode(Resource resource) {
        permission = resource.getPermission();
        type = resource.getType();
//        hierarchy = resource.getParentIds().split(",").length;
        id = resource.getId();

        for (String parentId : resource.getParentIds().split(",")) {
            parentIds.add(Long.valueOf(parentId));
        }
    }

    public void addChild(PermissionNode node) {
        if (childs == null) {
            childs = new ArrayList<>();
        }

        boolean hasAdd = false;
        // 找到正确的parent插入
        for (PermissionNode child : childs) {
            if (child.isAncestorOf(node)) {
                child.addChild(node);
                hasAdd = true;
            }
        }

        if (!hasAdd) {
            childs.add(node);
        }


    }

    public boolean isAncestorOf(PermissionNode node) {
        if (node.getParentIds().contains(id)) {
            return true;
        } else {
            return false;
        }
    }

//    public PermissionNode getParent() {
//        return parent;
//    }
//
//    public void setParent(PermissionNode parent) {
//        this.parent = parent;
//    }

    public List<Long> getParentIds() {
        return parentIds;
    }

    public void setParentIds(List<Long> parentIds) {
        this.parentIds = parentIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public Integer getHierarchy() {
//        return hierarchy;
//    }
//
//    public void setHierarchy(Integer hierarchy) {
//        this.hierarchy = hierarchy;
//    }

    public List<PermissionNode> getChilds() {
        return childs;
    }

    public void setChilds(List<PermissionNode> child) {
        this.childs = child;
    }
}
