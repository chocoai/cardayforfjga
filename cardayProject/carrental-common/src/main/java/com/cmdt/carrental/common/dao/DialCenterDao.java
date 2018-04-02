package com.cmdt.carrental.common.dao;

import com.cmdt.carrental.common.entity.DialCenter;
import com.cmdt.carrental.common.entity.UserInfo;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.model.DialCenterQueryDTO;
import com.cmdt.carrental.common.model.PagModel;

public interface DialCenterDao {
	PagModel findAllDialRecorder(DialCenterQueryDTO dto,Integer currentPage,Integer numPerPage);

	DialCenter addDialRecord(DialCenter dialCenter);

	UserInfo findUserInfo(String dialPhone);

	Vehicle findVehicleInfo(String orderNo);

	DialCenter findDialCenter(Long dialCenterId);

	DialCenter updateDialCenter(DialCenter dialCenter);

	void deleteDialCenter(DialCenter dialCenter);

}
