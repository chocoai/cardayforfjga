package com.cmdt.carrental.common.dao;

import java.util.List;

import com.cmdt.carrental.common.model.AllowanceModel;

public interface AllowanceDao {

	public List<AllowanceModel> findAll();

	public void update(AllowanceModel allowanceModel);
	
	public List<AllowanceModel> findMileageAllowance();

}
