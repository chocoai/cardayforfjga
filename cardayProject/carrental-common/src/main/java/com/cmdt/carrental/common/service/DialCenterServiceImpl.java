package com.cmdt.carrental.common.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.dao.DialCenterDao;
import com.cmdt.carrental.common.entity.DialCenter;
import com.cmdt.carrental.common.entity.Marker;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.UserInfo;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.model.DialCenterQueryDTO;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.util.TypeUtils;

@Service
public class DialCenterServiceImpl implements DialCenterService {
	@Autowired
	private DialCenterDao dialCenterDao;

	@Override
	public PagModel findAllDialRecorder(Map<String, Object> jsonMap, User user) {
		Integer currentPage = Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
    	Integer numPerPage = Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
    	DialCenterQueryDTO dto=new DialCenterQueryDTO();
    	dto.setDialPhone(TypeUtils.obj2String(jsonMap.get("dialPhone")));
    	dto.setDialName(TypeUtils.obj2String(jsonMap.get("dialName")));
    	dto.setStartTime(TypeUtils.obj2Date(jsonMap.get("startTime")));
    	dto.setEndTime(TypeUtils.obj2Date(jsonMap.get("endTime")));
    	return dialCenterDao.findAllDialRecorder(dto,currentPage,numPerPage);
	}
	
	@Override
	public UserInfo findUserInfo(String dialPhone){
		return dialCenterDao.findUserInfo(dialPhone);
	}
	
	@Override
	public Vehicle findVehicleInfo(String orderNo){
		return dialCenterDao.findVehicleInfo(orderNo);
	}

	@Override
	public DialCenter addDialRecord(DialCenter dialCenter) {
		if(dialCenter == null)
			return null;
		return dialCenterDao.addDialRecord(dialCenter);
		
	}
	
	@Override
	public DialCenter findDialCenter(Long dialCenterId) {
		if(dialCenterId == null)
			return null;
		return dialCenterDao.findDialCenter(dialCenterId);
	}
	
	@Override
	public DialCenter updateDialCenter(DialCenter dialCenter) {
		if(dialCenter == null)
			return null;
		return dialCenterDao.updateDialCenter(dialCenter);
	}
	
	@Override
	public void deleteDialCenter(DialCenter dialCenter) {
		if(dialCenter == null)
			return;
		dialCenterDao.deleteDialCenter(dialCenter);
	}

}
