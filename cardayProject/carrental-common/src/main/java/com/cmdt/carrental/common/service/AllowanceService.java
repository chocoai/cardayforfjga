package com.cmdt.carrental.common.service;

import java.util.List;

import com.cmdt.carrental.common.model.AllowanceModel;

public interface AllowanceService {
	
	public List<AllowanceModel> findAll();
	
	public List<AllowanceModel> findMileageAllowance();
	
	public void update(AllowanceModel allowanceModel);

}
