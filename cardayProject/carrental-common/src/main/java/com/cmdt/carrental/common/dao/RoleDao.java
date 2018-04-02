package com.cmdt.carrental.common.dao;

import java.util.List;
import java.util.Map;

import com.cmdt.carrental.common.entity.Role;
import com.cmdt.carrental.common.entity.RoleTemplate;
import com.cmdt.carrental.common.model.OwnerRentModel;
import com.cmdt.carrental.common.model.PagModel;

public interface RoleDao {

    public Role createRole(Role role);
    public Role updateRole(Role role);
    public void deleteRole(Long roleId);
    public List<Role> findByCondition(String condition,List<Object> params);
    public Role findOne(Long roleId);
    public List<Role> findAll(String json);
    public List<Role> findAll(String templateId, String role);
    public List<String> list(String json);
    public List<String> list(String idsStr, String templateIdStr, String roleStr);
    public RoleTemplate findOneTemplate(Long templateId);
    public List<RoleTemplate> findAllTemplate();
    public List<RoleTemplate> findRentTemplate();
    public List<RoleTemplate> findEntTemplate();
    public List<OwnerRentModel> findBySuperAdmin();
    public List<OwnerRentModel> findByRentAdmin(Long rentId);
    public List<Role> findRentLevel(Long rentId,String json);
    public List<Role> findRentLevel(Long rentId,String templateId, String role);
    public List<Role> findEntpersieLevel(Long orgId,String json);
    public List<Role> findEntpersieLevel(String role, Long orgId);
    public List<Role> findDeptLevel(Long orgId,String json);
    public List<Role> findDeptLevel(String role, Long orgId);
    public PagModel findAllRole(Map<String, Object> jsonMap);
    public List<Role> findAllRoleForAdmin(Long templateId,String roleStr,Long orgId);
}
