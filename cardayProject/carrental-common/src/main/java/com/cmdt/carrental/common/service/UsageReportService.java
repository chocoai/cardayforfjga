package com.cmdt.carrental.common.service;

import java.util.Date;
import java.util.List;

import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.UsageReportAllMileageAndFuleconsModel;
import com.cmdt.carrental.common.model.UsageReportAppColumnarModel;
import com.cmdt.carrental.common.model.UsageReportAppLineModel;
import com.cmdt.carrental.common.model.UsageReportLineModel;
import com.cmdt.carrental.common.model.UsageReportModel;
import com.cmdt.carrental.common.model.VehicleModel;

public interface UsageReportService {

	UsageReportModel getPieAndColumnarDataByDayRange(Long entId,Date startDate,Date endDate);

	PagModel getVehiclePropertyDataByDayRangeAndEntAdmin(Date startDate, Date endDate, long entId, long deptId,int currentPage,int numPerPage);

	UsageReportLineModel getVehicleLinePropertyDataByDayRange(Date startDate, Date endDate, long entId, long deptId);

	UsageReportAllMileageAndFuleconsModel getVehicleAllMileageAndFuleconsList(String vehicleNumber,
			String deviceNumber);

	PagModel getIdleVehicleListByDayRange(Date startDate, Date endDate, long entId, long deptId, int currentPage,
			int numPerPage);
	
	List<String> getIdleVehicleListByDayRangeALL(Date startDate, Date endDate, long entId, long deptId);
	
	VehicleModel findVehicleListByDeviceNumberAndImei(String vehicleNumber,String deviceNumber);

	VehicleModel findVehicleListByVehicleNumber(String vehicleNumber);

	UsageReportAppLineModel getTendencyChartByDayRangeAndType(Date startDate, Date endDate, String queryType,long orgId,boolean self,boolean child);

	UsageReportAppColumnarModel getDepartmentColumnarChartByDayRangeAndType(Date startDate, Date endDate,
			String queryType, long orgId,boolean self,boolean child);

	List<String> getAllVehiclePropertyDataByDayRange(Date startDate, Date endDate, long entId, long deptId);
	
	List<VehicleModel> findAllVehicleListByEntId(Long entId);
	
	List<VehicleModel> findAllVehicleListByDeptId(Long deptId);
	
	UsageReportModel getPieAndColumnarData(Long orgId,Boolean selfDept,Boolean childDept,Date startDate,Date endDate);

	//new old:getVehiclePropertyDataByDayRangeAndEntAdmin
	PagModel getVehiclePropertyData(Date startDate, Date endDate, Long orgId, Boolean selfDept, Boolean childDept,
			int currentPage, int numPerPage);

	//new old:getVehicleLinePropertyDataByDayRange
	UsageReportLineModel getVehicleLinePropertyData(Date startDate, Date endDate, Long orgId, Boolean selfDept,
			Boolean childDept);

	//new old:getAllVehiclePropertyDataByDayRange
	List<String> getAllVehiclePropertyData(Date startDate, Date endDate, Long orgId, Boolean selfDept,
			Boolean childDept);

	//new old:getIdleVehicleListByDayRange
	PagModel getIdleVehicleList(Date startDate, Date endDate, Long orgId, Boolean selfDept, Boolean childDept,
			int currentPage, int numPerPage);

	//new old:getIdleVehicleListByDayRangeALL
	List<String> getAllIdleVehicleList(Date startDate, Date endDate, Long orgId, Boolean selfDept, Boolean childDept);
	
	//new old:findAllVehicleListByDeptId
	List<VehicleModel> findAllVehicleListByOrgId(Long orgId);

	boolean getPrivilegeFlag(VehicleModel vehicleModel, Long orgId);
}
