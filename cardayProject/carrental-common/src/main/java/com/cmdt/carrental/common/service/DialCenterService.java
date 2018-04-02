package com.cmdt.carrental.common.service;

import java.util.Map;

import com.cmdt.carrental.common.entity.DialCenter;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.UserInfo;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.model.PagModel;

public interface DialCenterService {
	PagModel findAllDialRecorder(Map<String, Object> map,User user);

	DialCenter addDialRecord(DialCenter dialCenter);

	UserInfo findUserInfo(String dialPhone);

	Vehicle findVehicleInfo(String orderNo);

	DialCenter findDialCenter(Long dialCenterId);

	DialCenter updateDialCenter(DialCenter dialCenter);

	void deleteDialCenter(DialCenter dialCenter);
}
