package com.cmdt.carrental.common.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.model.AllowanceModel;

@Repository
public class AllowanceDaoImpl implements AllowanceDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<AllowanceModel> findAll() {
		final String sql = "select id,allowance_name as allowanceName,allowance_value as allowanceValue from busi_emp_allowance";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(AllowanceModel.class));
	}
	
	@Override
    public List<AllowanceModel> findMileageAllowance() {
        final String sql = "select id,allowance_name as allowanceName,allowance_value as allowanceValue from busi_emp_allowance where allowance_name = '公里数补贴'";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper(AllowanceModel.class));
    }

	@Override
	public void update(AllowanceModel allowanceModel) {
        String sql ="update busi_emp_allowance set allowance_name=?,allowance_value=? where id=?";
        jdbcTemplate.update(sql,
        		allowanceModel.getAllowanceName(),
        		allowanceModel.getAllowanceValue(),
        		allowanceModel.getId());
	}

}
