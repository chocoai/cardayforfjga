package com.cmdt.carrental.common.service;

import java.util.List;

import com.cmdt.carrental.common.entity.Rent;
import com.cmdt.carrental.common.entity.RentOrg;

public interface RentService {

	 List<Rent> findAll();
	 Rent findOne(Long rentId);
	 List<RentOrg> findByRentOrg(Long rentId,Long orgId);
	 public List<Rent> findByName(String name);
}
