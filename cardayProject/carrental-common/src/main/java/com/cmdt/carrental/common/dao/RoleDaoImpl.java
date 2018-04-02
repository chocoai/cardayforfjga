package com.cmdt.carrental.common.dao;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.Pagination;
import com.cmdt.carrental.common.util.SqlUtil;
import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carrental.common.entity.Role;
import com.cmdt.carrental.common.entity.RoleTemplate;
import com.cmdt.carrental.common.model.OwnerRentModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RoleDaoImpl implements RoleDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public Role createRole(final Role role) {
    	final String sql = "insert into sys_role(template_id,organization_id,role, description, resource_ids, available) values(?,?,?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setLong(count++, role.getTemplateId());
                psst.setLong(count++, role.getOrganizationId());
                psst.setString(count++, role.getRole());
                psst.setString(count++, role.getDescription());
                psst.setString(count++, role.getResourceIds());
                psst.setBoolean(count, role.getAvailable());
                return psst;
            }
        }, keyHolder);
        role.setId(keyHolder.getKey().longValue());
        return role;
    }

    public Role updateRole(Role role) {
    	 final String sql = "update sys_role set template_id=?,organization_id=?,role=?, description=?, resource_ids=?, available=? where id=?";
         jdbcTemplate.update(
                 sql,
                 role.getTemplateId(),role.getOrganizationId(),role.getRole(), role.getDescription(), role.getResourceIds(), role.getAvailable(), role.getId());
         return role;
    }

    public void deleteRole(Long roleId) {
        final String sql = "delete from sys_role where id=?";
        jdbcTemplate.update(sql, roleId);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Role findOne(Long roleId) {
        final String sql = "select id,template_id as templateId,(select name from sys_role_template where id=r.template_id) as templateName,"
        		+ "organization_id as organizationId,(select name from sys_organization where id=r.organization_id) as organizationName,role, description, resource_ids as resourceIds, "
        		+ "array_to_string(array(SELECT name FROM sys_resource b WHERE ','||r.resource_ids||',' like '%,'||b.id||',%'),',') as resourceNames,available from sys_role r where id=?";
        List<Role> roleList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Role.class), roleId);
        if(roleList.size() == 0) {
            return null;
        }
        return roleList.get(0);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Role> findByCondition(String condition,List<Object> params) {
    	String sql = "select id, template_id as templateId,(select name from sys_role_template where id=r.template_id) as templateName,"
        		+ "organization_id as organizationId,(select name from sys_organization where id=r.organization_id) as organizationName,role, description, resource_ids as resourceIds, array_to_string(array(SELECT name FROM sys_resource b WHERE ','||r.resource_ids||',' like '%,'||b.id||',%'),',') as resourceNames,"
    			+ " available from sys_role r where 1=1";
    	if(StringUtils.isNotBlank(condition)){
    		sql+=condition;
    	}
        sql+=" order by id,template_id asc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Role.class),params.toArray());
    }
    
	@Override
	public PagModel findAllRole(Map<String, Object> jsonMap) {
		Integer currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
    	Integer numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
		List<Object> params = new ArrayList<Object>();
		String sql = "select id, template_id as templateId,(select name from sys_role_template where id=r.template_id) as templateName,"
        		+ "organization_id as organizationId,(select name from sys_organization where id=r.organization_id) as organizationName,role, description, resource_ids as resourceIds, array_to_string(array(SELECT name FROM sys_resource b WHERE ','||r.resource_ids||',' like '%,'||b.id||',%'),',') as resourceNames,"
    			+ " available from sys_role r where template_id>=-9";
        if (StringUtils.isNotBlank(TypeUtils.obj2String(jsonMap.get("roleName")))) {
			sql+=" and role like "+SqlUtil.processLikeInjectionStatement(TypeUtils.obj2String(jsonMap.get("roleName")));
		} 
        sql+=" order by id desc";
        Pagination page=new Pagination(sql, currentPage, numPerPage,Role.class,jdbcTemplate, params.toArray());
		PagModel pageModel = page.getResult();
		return pageModel;  
	}
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Role> findAll(String json) {
    	String sql = "select id, template_id as templateId,(select name from sys_role_template where id=r.template_id) as templateName,"
        		+ "organization_id as organizationId,(select name from sys_rent where id=r.organization_id) as organizationName,role, description, resource_ids as resourceIds, array_to_string(array(SELECT name FROM sys_resource b WHERE ','||r.resource_ids||',' like '%,'||b.id||',%'),',') as resourceNames,"
    			+ " available from sys_role r where template_id>=-9";
    	List<Object> params=new ArrayList<Object>();
    	if(StringUtils.isNotBlank(json)){
	    	Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Long templateId=TypeUtils.obj2Long(jsonMap.get("templateId"));
			if(templateId!=0l){
				sql+=" and template_id=?";
				params.add(templateId);
			}
			String role=TypeUtils.obj2String(jsonMap.get("role"));
	        if(StringUtils.isNotBlank(role)){
				sql+=" and role like "+SqlUtil.processLikeInjectionStatement(role);
			}
    	}
        sql+=" order by id,template_id asc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Role.class),params.toArray());
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Role> findAll(String templateIdStr, String roleStr) {
    	String sql = "select id, template_id as templateId,(select name from sys_role_template where id=r.template_id) as templateName,"
        		+ "organization_id as organizationId,(select name from sys_rent where id=r.organization_id) as organizationName,role, description, resource_ids as resourceIds, array_to_string(array(SELECT name FROM sys_resource b WHERE ','||r.resource_ids||',' like '%,'||b.id||',%'),',') as resourceNames,"
    			+ " available from sys_role r where template_id in (-9,-1,-2,2,11)";
    	List<Object> params=new ArrayList<Object>();
    	Long templateId=TypeUtils.obj2Long(templateIdStr);
    	if(templateId!=0l){
    		sql+=" and template_id=?";
    		params.add(templateId);
    	}
    	String role=TypeUtils.obj2String(roleStr);
    	if(StringUtils.isNotBlank(role)){
    		sql+=" and role like "+SqlUtil.processLikeInjectionStatement(role);
    	}
        sql+=" order by id,template_id asc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Role.class),params.toArray());
    }
    
    @SuppressWarnings({ "unchecked" })
	public List<String> list(String json) {
    	String sql = "select CONCAT_WS('#',coalesce(r.role,''),coalesce(rt.name,''),"
    			+ "coalesce((select name from sys_rent where id=r.organization_id)||(select name from sys_organization where id=r.organization_id),'通用'),"
    			+ "coalesce(r.resource_ids,''), coalesce(r.description,''))"
        		+ " from sys_role r,sys_role_template rt  where rt.id=r.template_id";
    	List<Object> params=new ArrayList<Object>();
    	if(StringUtils.isNotBlank(json)){
	    	Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			String ids=TypeUtils.obj2String(jsonMap.get("ids"));//批量导出时所选的ID
			if(StringUtils.isNotBlank(ids)){
				String[] idarr=ids.split(",");
				String preparams="";
				for(int i=0,num=idarr.length;i<num;i++){
					if(i==num-1){
						preparams+="?";
					}else{
						preparams+="?,";
					}
					params.add(TypeUtils.obj2Long(idarr[i]));
				}
				sql += " and r.id in ("+preparams+")";
			}
			Long templateId=TypeUtils.obj2Long(jsonMap.get("templateId"));
			if(templateId!=0l){
				sql+=" and template_id=?";
				params.add(templateId);
			}
			String role=TypeUtils.obj2String(jsonMap.get("role"));
	        if(StringUtils.isNotBlank(role)){
				sql+=" and role like "+SqlUtil.processLikeInjectionStatement(role);
			}
    	}
        sql+=" order by r.id,template_id asc";
        return jdbcTemplate.queryForList(sql, String.class, params.toArray());
    }
    
    @SuppressWarnings({ "unchecked" })
   	public List<String> list(String idsStr, String templateIdStr, String roleStr) {
       	String sql = "select CONCAT_WS('#',coalesce(r.role,''),coalesce(rt.name,''),"
       			+ "coalesce((select name from sys_rent where id=r.organization_id)||(select name from sys_organization where id=r.organization_id),'通用'),"
       			+ "coalesce(r.resource_ids,''), coalesce(r.description,''))"
           		+ " from sys_role r,sys_role_template rt  where r.template_id>0 and rt.id=r.template_id";
       	List<Object> params=new ArrayList<Object>();
       	String ids=TypeUtils.obj2String(idsStr);//批量导出时所选的ID
       	if(StringUtils.isNotBlank(ids)){
       		String[] idarr=ids.split(",");
       		String preparams="";
       		for(int i=0,num=idarr.length;i<num;i++){
       			if(i==num-1){
       				preparams+="?";
       			}else{
       				preparams+="?,";
       			}
       			params.add(TypeUtils.obj2Long(idarr[i]));
       		}
       		sql += " and r.id in ("+preparams+")";
       	}
       	Long templateId=TypeUtils.obj2Long(templateIdStr);
       	if(templateId!=0l){
       		sql+=" and template_id=?";
       		params.add(templateId);
       	}
       	String role=TypeUtils.obj2String(roleStr);
       	if(StringUtils.isNotBlank(role)){
       		sql+=" and role like "+SqlUtil.processLikeInjectionStatement(role);
       	}
       	sql+=" order by r.id,template_id asc";
       	return jdbcTemplate.queryForList(sql, String.class, params.toArray());
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<OwnerRentModel> findBySuperAdmin() {
    	String sql = "select id as rentId, name from sys_rent r order by id asc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(OwnerRentModel.class));
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<OwnerRentModel> findByRentAdmin(Long rentId) {
    	String sql = "select id as rentId, name from sys_rent r where id=? order by id asc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(OwnerRentModel.class),rentId);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public RoleTemplate findOneTemplate(Long templateId) {
    	final String sql = "select id,name templateName,resource_ids as resourceIds,array_to_string(array(SELECT name FROM sys_resource b WHERE ','||r.resource_ids||',' like '%,'||b.id||',%'),',') as resourceNames,"
        		+ "description from sys_role_template r where id=?";
        List<RoleTemplate> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(RoleTemplate.class), templateId);
        if(list.size() == 0) {
            return null;
        }
        return list.get(0);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<RoleTemplate> findAllTemplate() {
    	String sql = "select id, name, resource_ids as resourceIds, array_to_string(array(SELECT name FROM sys_resource b WHERE ','||r.resource_ids||',' like '%,'||b.id||',%'),',') as resourceNames,"
    			+ " description from sys_role_template r where id>=-9 order by id asc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(RoleTemplate.class));
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<RoleTemplate> findRentTemplate() {
    	String sql = "select id, name, resource_ids as resourceIds, array_to_string(array(SELECT name FROM sys_resource b WHERE ','||r.resource_ids||',' like '%,'||b.id||',%'),',') as resourceNames,"
    			+ " description from sys_role_template r where id=2 order by id asc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(RoleTemplate.class));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<RoleTemplate> findEntTemplate() {
    	String sql = "select id, name, resource_ids as resourceIds, array_to_string(array(SELECT name FROM sys_resource b WHERE ','||r.resource_ids||',' like '%,'||b.id||',%'),',') as resourceNames,"
    			+ " description from sys_role_template r where id in(3,4,12,13,14) order by id asc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(RoleTemplate.class));
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Role> findRentLevel(Long rentId,String json) {
    	String sql = "select id, template_id as templateId,(select name from sys_role_template where id=r.template_id) as templateName,"
        		+ "organization_id as organizationId,(select name from sys_rent where id=r.organization_id) as organizationName,role, description, resource_ids as resourceIds, array_to_string(array(SELECT name FROM sys_resource b WHERE ','||r.resource_ids||',' like '%,'||b.id||',%'),',') as resourceNames,"
    			//+ " available from sys_role r where  (organization_id=? and template_id in(2,3,4,5)) or organization_id=0";
        		+ " available from sys_role r where   template_id in(2,3,4,5)) and (organization_id=? or organization_id=0)";
    	List<Object> params=new ArrayList<Object>();
    	params.add(rentId);
    	if(StringUtils.isNotBlank(json)){
	    	Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Long templateId=TypeUtils.obj2Long(jsonMap.get("templateId"));
			if(templateId!=0l){
				sql+=" and template_id=?";
				params.add(templateId);
			}
			String role=TypeUtils.obj2String(jsonMap.get("role"));
	        if(StringUtils.isNotBlank(role)){
				sql+=" and role like "+SqlUtil.processLikeInjectionStatement(role);
			}
    	}
        sql+=" order by id,template_id asc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Role.class),params.toArray());
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Role> findRentLevel(Long rentId,String templateIdStr, String roleStr) {
    	String sql = "select id, template_id as templateId,(select name from sys_role_template where id=r.template_id) as templateName,"
        		+ "organization_id as organizationId,(select name from sys_rent where id=r.organization_id) as organizationName,role, description, resource_ids as resourceIds, array_to_string(array(SELECT name FROM sys_resource b WHERE ','||r.resource_ids||',' like '%,'||b.id||',%'),',') as resourceNames,"
    		//	+ " available from sys_role r where  (organization_id=? and template_id in(2,3,4,5)) or organization_id=0";
    			+ " available from sys_role r where   template_id in(2,3,4,5)) and (organization_id=? or organization_id=0)";
    	List<Object> params=new ArrayList<Object>();
    	params.add(rentId);
    	Long templateId=TypeUtils.obj2Long(templateIdStr);
    	if(templateId!=0l){
    		sql+=" and template_id=?";
    		params.add(templateId);
    	}
    	String role=TypeUtils.obj2String(roleStr);
    	if(StringUtils.isNotBlank(role)){
    		sql+=" and role like "+SqlUtil.processLikeInjectionStatement(role);
    	}
        sql+=" order by id,template_id asc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Role.class),params.toArray());
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Role> findEntpersieLevel(Long orgId,String json) {
    	String sql = "select id, template_id as templateId,(select name from sys_role_template where id=r.template_id) as templateName,"
        		+ "organization_id as organizationId,(select name from sys_rent where id=r.organization_id) as organizationName,role, description, resource_ids as resourceIds, "
    			+ "array_to_string(array(SELECT name FROM sys_resource b WHERE ','||r.resource_ids||',' like '%,'||b.id||',%'),',') as resourceNames,available from sys_role r "
    			//+ " where template_id in(3,4) and (organization_id in (select retid from sys_rent_org where orgid=?) or organization_id=0)";
    			+ " where template_id in (3,4,12,13,14) and (organization_id =? or organization_id=0)";
    	List<Object> params=new ArrayList<Object>();
    	params.add(orgId);
    	if(StringUtils.isNotBlank(json)){
	    	Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			String role=TypeUtils.obj2String(jsonMap.get("role"));
	        if(StringUtils.isNotBlank(role)){
				sql+=" and role like "+SqlUtil.processLikeInjectionStatement(role);
			}
    	}
        sql+=" order by id,template_id asc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Role.class),params.toArray());
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Role> findEntpersieLevel(String roleStr, Long orgId) {
    	String sql = "select id, template_id as templateId,(select name from sys_role_template where id=r.template_id) as templateName,"
        		+ "organization_id as organizationId,(select name from sys_rent where id=r.organization_id) as organizationName,role, description, resource_ids as resourceIds, "
    			+ "array_to_string(array(SELECT name FROM sys_resource b WHERE ','||r.resource_ids||',' like '%,'||b.id||',%'),',') as resourceNames,available from sys_role r "
    			//+ " where template_id in(3,4) and (organization_id in (select retid from sys_rent_org where orgid=?) or organization_id=0)";
    			+ " where template_id in (3,4) and (organization_id =? or organization_id=0)";
    	List<Object> params=new ArrayList<Object>();
    	params.add(orgId);
    	String role=TypeUtils.obj2String(roleStr);
    	if(StringUtils.isNotBlank(role)){
    		sql+=" and role like "+SqlUtil.processLikeInjectionStatement(role);
    	}
        sql+=" order by id,template_id asc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Role.class),params.toArray());
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Role> findDeptLevel(Long orgId,String json) {
    	String sql = "select id, template_id as templateId,(select name from sys_role_template where id=r.template_id) as templateName,"
        		+ "organization_id as organizationId,(select name from sys_rent where id=r.organization_id) as organizationName,role, description, resource_ids as resourceIds, "
    			+ "array_to_string(array(SELECT name FROM sys_resource b WHERE ','||r.resource_ids||',' like '%,'||b.id||',%'),',') as resourceNames,available from sys_role r "
    			//+ " where template_id in(3,4) and (organization_id in (select retid from sys_rent_org where orgid=(select parent_id from sys_organization where id=?))  or organization_id=0)";
    			+ " where template_id in(3,4,12,13,14) and (organization_id =?  or organization_id=0)";
    	List<Object> params=new ArrayList<Object>();
    	params.add(orgId);
    	if(StringUtils.isNotBlank(json)){
	    	Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			String role=TypeUtils.obj2String(jsonMap.get("role"));
	        if(StringUtils.isNotBlank(role)){
				sql+=" and role like "+SqlUtil.processLikeInjectionStatement(role);
			}
    	}
        sql+=" order by id,template_id asc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Role.class),params.toArray());
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Role> findDeptLevel(String roleStr, Long orgId) {
    	String sql = "select id, template_id as templateId,(select name from sys_role_template where id=r.template_id) as templateName,"
        		+ "organization_id as organizationId,(select name from sys_rent where id=r.organization_id) as organizationName,role, description, resource_ids as resourceIds, "
    			+ "array_to_string(array(SELECT name FROM sys_resource b WHERE ','||r.resource_ids||',' like '%,'||b.id||',%'),',') as resourceNames,available from sys_role r "
    			//+ " where template_id=4 and (organization_id in (select retid from sys_rent_org where orgid=(select parent_id from sys_organization where id=?))  or organization_id=0)";
    			+ " where template_id in(3,4) and (organization_id =?  or organization_id=0)";
    			List<Object> params=new ArrayList<Object>();
    	params.add(orgId);
    	String role=TypeUtils.obj2String(roleStr);
    	if(StringUtils.isNotBlank(role)){
    		sql+=" and role like "+SqlUtil.processLikeInjectionStatement(role);
    	}
        sql+=" order by id,template_id asc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Role.class),params.toArray());
    }

	@Override
	public List<Role> findAllRoleForAdmin(Long templateId, String roleStr,Long orgId) {
		String sql = "select id, template_id as templateId,(select name from sys_role_template where id=r.template_id) as templateName,"
        		+ "organization_id as organizationId,(select name from sys_rent where id=r.organization_id) as organizationName,role, description, resource_ids as resourceIds, array_to_string(array(SELECT name FROM sys_resource b WHERE ','||r.resource_ids||',' like '%,'||b.id||',%'),',') as resourceNames,"
    			+ " available from sys_role r where template_id in (-9,-1,-2,2,11) and (organization_id =?  or organization_id=0) ";
    	List<Object> params=new ArrayList<Object>();
    	params.add(orgId);
    	if(templateId!=0l){
    		sql+=" and template_id=?";
    		params.add(templateId);
    	}
    	if(StringUtils.isNotBlank(roleStr)){
    		sql+=" and role like "+SqlUtil.processLikeInjectionStatement(roleStr);
    	}
        sql+=" order by id,template_id asc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Role.class),params.toArray());
	}
}
