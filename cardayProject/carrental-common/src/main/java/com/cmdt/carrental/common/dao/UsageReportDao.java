package com.cmdt.carrental.common.dao;

import java.util.Date;
import java.util.List;

import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.UsageIdleVehicleModel;
import com.cmdt.carrental.common.model.UsageReportAllMileageAndFuleconsModel;
import com.cmdt.carrental.common.model.UsageReportSQLModel;
import com.cmdt.carrental.common.model.VehicleModel;

public interface UsageReportDao {

	List<UsageReportSQLModel> getPieAndColumnarDataByDayRange(Long entId, Date startDate,Date endDate);

	PagModel getDeptVehiclePropertyDataByDayRangeAndEntAdmin(Date startDate, Date endDate, long deptId, int currentPage,
			int numPerPage);

	PagModel getAllDeptVehiclePropertyDataByDayRangeAndEntAdmin(Date startDate, Date endDate, long entId,
			int currentPage, int numPerPage);

	List<UsageReportSQLModel> getDeptVehicleLinePropertyDataByDayRange(Date startDate, Date endDate, long deptId);

	List<UsageReportSQLModel> getAllVehicleLinePropertyDataByDayRange(Date startDate, Date endDate, long entId);

	UsageReportAllMileageAndFuleconsModel getVehicleAllMileageAndFuleconsList(String vehicleNumber,
			String deviceNumber,String todayVal, String yesterdayVal,
			List<String> daysOFCurrentweekVal, String currentMonthVal);

	PagModel getDeptIdleVehicleListByDayRange(Date startDate, Date endDate, long deptId, int currentPage,
			int numPerPage);

	PagModel getAllIdleVehicleListByDayRange(Date startDate, Date endDate, long entId, int currentPage, int numPerPage);
	
	List<UsageReportSQLModel> getDeptIdleVehicleListByDayRangeForALL(Date startDate, Date endDate, long deptId);
	
	List<UsageReportSQLModel> getAllIdleVehicleListByDayRangeForALL(Date startDate, Date endDate, long deptId);

	VehicleModel findVehicleListByDeviceNumberAndImei(String vehicleNumber, String deviceNumber);

	VehicleModel findVehicleListByVehicleNumber(String vehicleNumber);

	List<UsageReportSQLModel> getTendencyChartByDayRangeAndTypeForDept(Date startDate, Date endDate, long deptId,
			String queryType);

	List<UsageReportSQLModel> getTendencyChartByDayRangeAndTypeForEnt(Date startDate, Date endDate,
			String queryType, List<Organization> orgList);

	List<UsageReportSQLModel> getDepartmentColumnarChartByDayRangeAndType(Date startDate, Date endDate,
			String queryType, List<Organization> orgList);
	
	List<UsageReportSQLModel> getDepartmentColumnarChartByDayRangeAndType(Date startDate, Date endDate,
			String queryType, String orgType,Long orgId,List<Organization> orgList);

	List<UsageReportSQLModel> getAllVehiclePropertyDataByDayRangeAndEntAdmin(Date startDate, Date endDate,
			long entId);

	List<UsageReportSQLModel> getAllVehiclePropertyDataByDayRangeAndDeptAdmin(Date startDate, Date endDate, long entId);

	List<UsageReportSQLModel> getRealDeptListByEntId(long entId);

	List<VehicleModel> findAllVehicleListByEntId(Long entId);

	List<VehicleModel> findAllVehicleListByDeptId(Long deptId);

	List<UsageReportSQLModel> getPieAndColumnarData(List<Organization> params, Date startDate,
			Date endDate);

	//new old:getDeptVehiclePropertyDataByDayRangeAndEntAdmin,getAllDeptVehiclePropertyDataByDayRangeAndEntAdmin
	PagModel getVehiclePropertyData(List<Organization> organizationList, Date startDate, Date endDate, int currentPage,
			int numPerPage);

	//new old:getDeptVehicleLinePropertyDataByDayRange,getAllVehicleLinePropertyDataByDayRange
	List<UsageReportSQLModel> getVehicleLinePropertyData(List<Organization> organizationList, Date startDate,
			Date endDate);

	//new old:getAllVehiclePropertyDataByDayRangeAndDeptAdmin,getAllVehiclePropertyDataByDayRangeAndEntAdmin
	List<UsageReportSQLModel> getAllVehiclePropertyData(List<Organization> organizationList, Date startDate,
			Date endDate);

	//new old:getDeptIdleVehicleListByDayRange,getAllIdleVehicleListByDayRange
	PagModel getIdleVehicleList(Date startDate, Date endDate, List<Organization> organizationList, int currentPage,
			int numPerPage);

	//new old:getDeptIdleVehicleListByDayRangeForALL,getAllIdleVehicleListByDayRangeForALL
	List<UsageReportSQLModel> getAllIdleVehicleList(Date startDate, Date endDate, List<Organization> organizationList);

	//new old:getVehiclePropertyData
	PagModel getVehiclePropertyDataDept(Boolean selfDept, Boolean childDept, Long orgId, List<Long> orgIdList,
			Date startDate, Date endDate, int currentPage, int numPerPage);

	//new old:getVehiclePropertyData
	PagModel getVehiclePropertyDataByEnt(Boolean selfDept, Boolean childDept, Long orgId, List<Long> orgIdList,
			Date startDate, Date endDate, int currentPage, int numPerPage);

	//new old:getVehiclePropertyData
	PagModel getVehiclePropertyDataByRent(Boolean selfDept, Boolean childDept, Long orgId, List<Long> orgIdList,
			Date startDate, Date endDate, int currentPage, int numPerPage);

	//new old:getVehicleLinePropertyData
	List<UsageReportSQLModel> getVehicleLinePropertyDataByDept(Boolean selfDept, Boolean childDept, Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate);

	//new old:getVehicleLinePropertyData
	List<UsageReportSQLModel> getVehicleLinePropertyDataByEnt(Boolean selfDept, Boolean childDept, Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate);

	//new old:getVehicleLinePropertyData
	List<UsageReportSQLModel> getVehicleLinePropertyDataByRent(Boolean selfDept, Boolean childDept, Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate);

	//new old:getPieAndColumnarData
	List<UsageReportSQLModel> getPieAndColumnarDataByDept(Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate);

	//new old:getPieAndColumnarData
	List<UsageReportSQLModel> getPieAndColumnarDataByEnt(Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate);

	//new old:getPieAndColumnarData
	List<UsageReportSQLModel> getPieAndColumnarDataByRent(Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate);

	//new old:getIdleVehicleList
	PagModel getIdleVehicleListByDept(Boolean selfDept, Boolean childDept, Long orgId, List<Long> orgIdList,
			Date startDate, Date endDate, int currentPage, int numPerPage);

	//new old:getIdleVehicleList
	PagModel getIdleVehicleListByEnt(Boolean selfDept, Boolean childDept, Long orgId, List<Long> orgIdList,
			Date startDate, Date endDate, int currentPage, int numPerPage);

	//new old:getIdleVehicleList
	PagModel getIdleVehicleListByRent(Boolean selfDept, Boolean childDept, Long orgId, List<Long> orgIdList,
			Date startDate, Date endDate, int currentPage, int numPerPage);

	//new old:getAllIdleVehicleList
	List<UsageReportSQLModel> getAllIdleVehicleListByDept(Boolean selfDept, Boolean childDept, Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate);

	//new old:getAllIdleVehicleList
	List<UsageReportSQLModel> getAllIdleVehicleListByEnt(Boolean selfDept, Boolean childDept, Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate);

	//new old:getAllIdleVehicleList
	List<UsageReportSQLModel> getAllIdleVehicleListByRent(Boolean selfDept, Boolean childDept, Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate);

	//new old:getAllVehiclePropertyData
	List<UsageReportSQLModel> getAllVehiclePropertyDataByDept(Boolean selfDept, Boolean childDept, Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate);

	//new old:getAllVehiclePropertyData
	List<UsageReportSQLModel> getAllVehiclePropertyDataByEnt(Boolean selfDept, Boolean childDept, Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate);

	//new old:getAllVehiclePropertyData
	List<UsageReportSQLModel> getAllVehiclePropertyDataByRent(Boolean selfDept, Boolean childDept, Long orgId,
			List<Long> orgIdList, Date startDate, Date endDate);

	List<VehicleModel> findAllVehicleListByDept(Long orgId, List<Long> orgIdList);

	List<VehicleModel> findAllVehicleListByEnt(Long orgId, List<Long> orgIdList);

	List<VehicleModel> findAllVehicleListByRent(Long orgId, List<Long> orgIdList);

	List<UsageReportSQLModel> findOrgList(List<Long> orgIdList);

	//new old:getTendencyChartByDayRangeAndTypeForEnt
	List<UsageReportSQLModel> getTendencyChartByDept(Date startDate, Date endDate,
			String queryType, Long orgId,List<Long> orgIdList,Boolean selfDept,Boolean childDept);

	//new old:getTendencyChartByDayRangeAndTypeForEnt
	List<UsageReportSQLModel> getTendencyChartByEnt(Date startDate, Date endDate, String queryType, long orgId,
			List<Long> orgIdList, boolean self, boolean child);

	//new old:getTendencyChartByDayRangeAndTypeForEnt
	List<UsageReportSQLModel> getTendencyChartByRent(Date startDate, Date endDate, String queryType, long orgId,
			List<Long> orgIdList, boolean self, boolean child);

	

}
