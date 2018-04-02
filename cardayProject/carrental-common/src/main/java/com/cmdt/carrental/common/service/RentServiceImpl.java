package com.cmdt.carrental.common.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.dao.RentDao;
import com.cmdt.carrental.common.entity.Rent;
import com.cmdt.carrental.common.entity.RentOrg;

@Service
public class RentServiceImpl implements RentService{
	
	@Autowired
    private RentDao rentDao;

	@Override
	public List<Rent> findAll() {
		return rentDao.findAll();
	}

	@Override
	public Rent findOne(Long rentId) {
		return rentDao.findOne(rentId);
	}

	@Override
	public List<RentOrg> findByRentOrg(Long rentId,Long orgId) {
		return rentDao.findByRentOrg(rentId,orgId);
	}

	@Override
	public List<Rent> findByName(String name) {
		return rentDao.findByName(name);
	}
	
}
