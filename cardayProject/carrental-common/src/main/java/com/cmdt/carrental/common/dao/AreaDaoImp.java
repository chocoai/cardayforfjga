package com.cmdt.carrental.common.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.model.Area;
@Repository
public class AreaDaoImp implements AreaDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Area> findByParentId(Integer parentId) {
		List<Area> retList = jdbcTemplate.query("select id as regionId,name as regionName,parentid as parentId,level from region where parentid=?", new BeanPropertyRowMapper<Area>(Area.class),parentId);
		if(retList != null && retList.size() > 0){
			return retList;
		}
		return null;
	}

}
