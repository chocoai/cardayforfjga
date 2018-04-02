package com.cmdt.carrental.common.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.entity.Rent;
import com.cmdt.carrental.common.entity.RentOrg;
import com.cmdt.carrental.common.util.SqlUtil;

@Repository
public class RentDaoImpl implements RentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public Rent findOne(Long rentId) {
		final String sql = "select id, name,linkman, linkman_phone,linkman_email from sys_rent where id=?";
        List<Rent> rentList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Rent.class), rentId);
        if(rentList.size() == 0) {
            return null;
        }
        return rentList.get(0);
	}

	@Override
	public List<Rent> findAll() {
		 final String sql = "select id, name,linkman, linkman_phone,linkman_email from sys_rent";
	     return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Rent.class));
	}

	@Override
	public List<RentOrg> findByRentOrg(Long rentId, Long orgId) {
		final String sql = "select retid, orgid from sys_rent_org where retid=? and orgid=?";
	    return jdbcTemplate.query(sql, new BeanPropertyRowMapper(RentOrg.class),rentId,orgId);
	}

	@Override
	public List<RentOrg> findByRent(Long rentId) {
		final String sql = "select orgid from sys_rent_org where retid=?";
	    return jdbcTemplate.query(sql, new BeanPropertyRowMapper(RentOrg.class),rentId);
	}
    
	@Override
	public List<Rent> findByName(String name) {
		final String sql = "select orgid from sys_rent where name like "+SqlUtil.processLikeInjectionStatement(name);
	    return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Rent.class));
	}
}
