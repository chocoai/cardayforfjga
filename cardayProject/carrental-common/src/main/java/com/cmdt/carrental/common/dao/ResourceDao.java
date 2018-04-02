package com.cmdt.carrental.common.dao;

import java.util.List;

import com.cmdt.carrental.common.entity.Resource;

public interface ResourceDao {

    public Resource createResource(Resource resource);
    public Resource updateResource(Resource resource);
    public void deleteResource(Long resourceId);

    Resource findOne(Long resourceId);
    public List<Resource> findByResourceId(Long resourceId);
    public List<Resource> findByResourceIds(String resourceIds);
    List<Resource> findAll();
    public List<String> list();
    public List<Resource> findResourcesByRoleId(Long roleId);
}
