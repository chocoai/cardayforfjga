package com.cmdt.carrental.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.dao.AllowanceDao;
import com.cmdt.carrental.common.model.AllowanceModel;

@Service
public class AllowanceServiceImpl implements AllowanceService{

	 @Autowired
	 private AllowanceDao allowanceDao;

	@Override
	public List<AllowanceModel> findAll() {
		return allowanceDao.findAll();
	}
	
	@Override
    public List<AllowanceModel> findMileageAllowance() {
        return allowanceDao.findMileageAllowance();
    }

	@Override
	public void update(AllowanceModel allowanceModel) {
		allowanceDao.update(allowanceModel);
	}
}
