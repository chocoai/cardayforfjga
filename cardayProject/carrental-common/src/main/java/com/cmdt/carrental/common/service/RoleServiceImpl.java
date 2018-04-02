package com.cmdt.carrental.common.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.dao.RoleDao;
import com.cmdt.carrental.common.entity.Role;
import com.cmdt.carrental.common.entity.RoleTemplate;
import com.cmdt.carrental.common.model.OwnerRentModel;
import com.cmdt.carrental.common.model.PagModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private ResourceService resourceService;
    
    @Autowired OrganizationService organizationService;

    public Role createRole(Role role) {
    	String result=isValid(role);
    	if(StringUtils.isBlank(result)){
    		return roleDao.createRole(role);
    	}else{
    		return null;
    	}
    }

    public Role updateRole(Role role) {
    	String result=isValid(role);
    	if(StringUtils.isBlank(result)){
    		return roleDao.updateRole(role);
    	}else{
    		return null;
    	}
    }

    public void deleteRole(Long roleId) {
        roleDao.deleteRole(roleId);
    }

    @Override
	public String isValid(Role role) {
		String msg="";
		List<Object> params=new ArrayList<Object>();
		String condition=" and role=?";
		params.add(role.getRole());
		if(role.getId()!=null&&role.getId()!=0){
			condition+=" and id<>?";
			params.add(role.getId());
		}
		List<Role> list=roleDao.findByCondition(condition, params);
		if(!list.isEmpty()){
			msg="角色名称已经存在";
		}
		return msg;
	}

	public Role findOne(Long roleId) {
        return roleDao.findOne(roleId);
    }

    public List<Role> findAll(String json) {
        return roleDao.findAll(json);
    }
    
    public List<Role> findAll(String templateId, String role) {
        return roleDao.findAll(templateId, role);
    }
    
    @Override
	public List<String> list(String json) {
    	return roleDao.list(json);
	}
    
    @Override
	public List<String> list(String ids, String templateId, String role) {
    	return roleDao.list(ids, templateId, role);
	}

	public RoleTemplate findOneTemplate(Long templateId){
    	return roleDao.findOneTemplate(templateId);
    }
    
    public List<RoleTemplate> findAllTemplate() {
        return roleDao.findAllTemplate();
    }
    
    public List<RoleTemplate> findRentTemplate() {
        return roleDao.findRentTemplate();
    }

    public List<RoleTemplate> findEntTemplate() {
        return roleDao.findEntTemplate();
    }
    
    public List<OwnerRentModel> findBySuperAdmin() {
    	return roleDao.findBySuperAdmin();
    }
    public List<OwnerRentModel> findByRentAdmin(Long rentId) {
    	return roleDao.findByRentAdmin(rentId);
    }
    
    public List<Role> findRentLevel(Long rentId,String json) {
        return roleDao.findRentLevel(rentId,json);
    }
    
    public List<Role> findRentLevel(Long rentId,String templateId, String role) {
        return roleDao.findRentLevel(rentId, templateId, role);
    }
    
    public List<Role> findEntpersieLevel(Long orgId,String json) {
        return roleDao.findEntpersieLevel(orgId,json);
    }
    
    public List<Role> findEntpersieLevel(String role, Long orgId) {
        return roleDao.findEntpersieLevel(role, orgId);
    }
    
    public List<Role> findDeptLevel(Long orgId,String json) {
        return roleDao.findDeptLevel(organizationService.findTopOrganization(orgId).getId(),json);
    }
    
    public List<Role> findDeptLevel(String role, Long orgId) {
        return roleDao.findDeptLevel(role, organizationService.findTopOrganization(orgId).getId());
    }
    
    public Set<String> findRoles(Long... roleIds) {
        Set<String> roles = new HashSet<String>();
        for(Long roleId : roleIds) {
            Role role = findOne(roleId);
            if(role != null) {
                roles.add(role.getRole());
            }
        }
        return roles;
    }

    public Set<String> findPermissions(Long[] roleIds) {
        Set<Long> resourceIds = new HashSet<Long>();
        for(Long roleId : roleIds) {
            Role role = findOne(roleId);
            if(role != null) {
            	for(String rid:role.getResourceIds().split(","))
                resourceIds.add(Long.valueOf(rid));
            }
        }
        return resourceService.findPermissions(resourceIds);
    }

	@Override
	public PagModel findAllRole(Map<String, Object> jsonMap) {
		return roleDao.findAllRole(jsonMap);
	}

	@Override
	public List<Role> findAllRoleForAdmin(Long templateId,String roleStr,Long orgId) {
		
		return roleDao.findAllRoleForAdmin(templateId,roleStr,orgId);
	}
}
