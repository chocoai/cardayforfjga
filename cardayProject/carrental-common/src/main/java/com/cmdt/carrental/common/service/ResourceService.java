package com.cmdt.carrental.common.service;

import java.util.List;
import java.util.Set;

import com.cmdt.carrental.common.entity.Resource;
import com.cmdt.carrental.common.util.TreeNode;
import com.cmdt.carrental.common.util.TreeNodeView;

public interface ResourceService {


    public Resource createResource(Resource resource);
    public Resource updateResource(Resource resource);
    public void deleteResource(Long resourceId);

    Resource findOne(Long resourceId);
    public List<Resource> findByResourceId(Long resourceId);
    List<Resource> findByResourceIds(String resourceIds);
    List<Resource> findAll();
    public List<String> list();
    public TreeNode formatList2Tree(List<Resource> list);
    public TreeNodeView formatList2TreeView(List<Resource> list);
    /**
     * 得到资源对应的权限字符串
     * @param resourceIds
     * @return
     */
    Set<String> findPermissions(Set<Long> resourceIds);

    /**
     * 根据用户权限得到菜单
     * @param permissions
     * @return
     */
    List<Resource> findMenus(Set<String> permissions);
    /**
     * 根据用户权限得到按钮
     * @param permissions
     * @return
     */
    public List<Resource> findButtons(Set<String> permissions);
    
    public List<Resource> findResourcesByRoleId(Long roleId);
}
