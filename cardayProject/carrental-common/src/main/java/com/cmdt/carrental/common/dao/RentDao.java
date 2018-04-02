package com.cmdt.carrental.common.dao;

import java.util.List;

import com.cmdt.carrental.common.entity.Rent;
import com.cmdt.carrental.common.entity.RentOrg;

public interface RentDao {

    Rent findOne(Long organizationId);
    List<Rent> findAll();
	List<RentOrg> findByRentOrg(Long rentId, Long orgId);
	List<RentOrg> findByRent(Long rentId);
	public List<Rent> findByName(String name);
}
