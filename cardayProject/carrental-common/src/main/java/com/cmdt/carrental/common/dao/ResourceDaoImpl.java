package com.cmdt.carrental.common.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.entity.Resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ResourceDaoImpl implements ResourceDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public Resource createResource(final Resource resource) {
        final String sql = "insert into sys_resource(name, type, url, permission, parent_id, parent_ids, available, description) values(?,?,?,?,?,?,?,?)";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setString(count++, resource.getName());
                psst.setString(count++, resource.getType());
                psst.setString(count++, resource.getUrl());
                psst.setString(count++, resource.getPermission());
                psst.setLong(count++, resource.getParentId());
                psst.setString(count++, resource.getParentIds());
                psst.setBoolean(count++, resource.getAvailable());
                psst.setString(count, resource.getDescription());
                return psst;
            }
        }, keyHolder);
        resource.setId(keyHolder.getKey().longValue());
        return resource;
    }

    public Resource updateResource(Resource resource) {
        final String sql = "update sys_resource set name=?, type=?, url=?, permission=?, parent_id=?, parent_ids=?, available=?, description=? where id=?";
        jdbcTemplate.update(
                sql,
                resource.getName(), resource.getType(), resource.getUrl(), resource.getPermission(), resource.getParentId(), resource.getParentIds(), resource.getAvailable(),resource.getDescription(), resource.getId());
        return resource;
    }

    public void deleteResource(Long resourceId) {
        Resource resource = findOne(resourceId);
        final String deleteSelfSql = "delete from sys_resource where id=?";
        jdbcTemplate.update(deleteSelfSql, resourceId);
        final String deleteDescendantsSql = "delete from sys_resource where parent_ids like ?";
        jdbcTemplate.update(deleteDescendantsSql, resource.makeSelfAsParentIds() + "%");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Resource findOne(Long resourceId) {
        final String sql = "select * from sys_resource where id=?";
        List<Resource> resourceList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Resource.class), resourceId);
        if(resourceList.size() == 0) {
            return null;
        }
        return resourceList.get(0);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Resource> findByResourceId(Long resourceId) {
        final String sql = "select * from sys_resource where id=?  or ','||parent_ids||',' like ? order by id asc";
        List<Resource> resourceList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Resource.class),resourceId,"%,"+resourceId+",%");
        if(resourceList.size() == 0) {
            return null;
        }
        return resourceList;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Resource> findByResourceIds(String resourceIds) {
        final String sql = "select * from sys_resource where ','||"+resourceIds+"||',' like '%,'||id||',%' order by id asc";
        List<Resource> resourceList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Resource.class));
        if(resourceList.size() == 0) {
            return null;
        }
        return resourceList;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Resource> findAll() {
        final String sql = "select * from sys_resource order by id asc";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Resource.class));
    }
    
    public List<String> list() {
        final String sql = "select CONCAT_WS(',',coalesce(id||'',''), coalesce(name,'')) from sys_resource order by id asc";
        return jdbcTemplate.queryForList(sql, String.class);
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Resource> findResourcesByRoleId(Long roleId) {
		List<Object> params = new ArrayList<Object>();
		params.add(roleId);
		final String sql = "select * from sys_resource r where EXISTS (select * from regexp_split_to_table((select resource_ids from sys_role where id = ?),',') "
				+ "as result where cast(result as  INT4)=r.id) order by id";
		
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Resource.class), params.toArray());
	}
}
