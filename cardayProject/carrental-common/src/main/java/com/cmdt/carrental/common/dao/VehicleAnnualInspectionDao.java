package com.cmdt.carrental.common.dao;

import java.sql.Date;
import java.util.List;

import com.cmdt.carrental.common.entity.VehicleAnnualInspectionDto;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleAnnualInspection;

public interface VehicleAnnualInspectionDao {
	
	//保险年检列表
//	public PagModel listPage(Long entId, String json);
	
	public PagModel listPage(VehicleAnnualInspectionDto dto);
	
	//重置保险
	public boolean resetInsuranceTime(Long id, String insuranceDueTime);
	
	//重置年检
	public boolean resetInspectionTime(Long id, String inspectionNextTime);
	
	//保险提醒
	public List<VehicleAnnualInspection> queryInsuranceTimeAlert();
	
	//年检提醒
	public List<VehicleAnnualInspection> queryInspectionTimeAlert();

	public boolean modifyJobStatus(String filedName, int filedValue, Long id);
	
	//修改车辆的年检
	
	public int updateInspection(Date date,Long vehicleId);
	
}
