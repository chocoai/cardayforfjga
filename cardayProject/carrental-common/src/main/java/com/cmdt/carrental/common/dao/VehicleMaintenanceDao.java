package com.cmdt.carrental.common.dao;

import java.util.Date;
import java.util.List;

import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.entity.VehicleMaintenance;
import com.cmdt.carrental.common.model.MaintenanceListModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.UsageReportSQLModel;
import com.cmdt.carrental.common.model.VehicleDataCountModel;
import com.cmdt.carrental.common.model.VehicleMaintainInfoModel;

public interface VehicleMaintenanceDao {
	public VehicleMaintenance create(final VehicleMaintenance maintenance);
	public VehicleMaintenance update(VehicleMaintenance maintenance);
	PagModel listPage(VehicleMaintainInfoModel model,List<Long> list);
	public List<Vehicle> listVehicleBySql(String condition);
	public List<String> list(Long entId, String json);
	public List<VehicleMaintenance> listBySql(String condition,List<Object> params);
	public VehicleMaintenance setThreshold(VehicleMaintenance maintenance);
	public List<VehicleMaintenance> queryExportList(Long entId, String json);
	public List<VehicleMaintenance> queryMaintenanceMileageAlert();
	public List<VehicleMaintenance> queryMaintenanceTimeAlert();
	public boolean modifyJobStatus(String filedName, int filedValue, Long id);
	public List<VehicleMaintenance> querySysnTravelMileageVehicleList();
	public void modifyMaintenanceJobTime(Long travelMileage, Date updateTime, Long vehicleId);
	public VehicleDataCountModel queryVehicleDataCountByHomePage(String queryDate, String vehicleIdList);
	public List<Vehicle> listVehicleByEnt(Long orgId,List<Long> orgIds);
	public List<Vehicle> listVehicleByDep(List<Long> orgIds);
	public PagModel listVehicleMaintenancePage(MaintenanceListModel vModel);
	public List<VehicleMaintenance> queryVehicleMaintenanceExportList(Long entId, String vehicleNumber,Long deptId,String ids);
	public List<UsageReportSQLModel> getVehicleMileageStasticByImeiAndBeginTime(Date curTime, String deviceNumber, String vehicleNumber);
	public void modifyJobTravelMileageAndUpdateTime(Long travelMileage, Date updateTime, Long vehicleId);
}
