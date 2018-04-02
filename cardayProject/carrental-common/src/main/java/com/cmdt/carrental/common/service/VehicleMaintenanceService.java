package com.cmdt.carrental.common.service;

import java.util.Date;
import java.util.List;

import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.entity.VehicleMaintenance;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleDataCountModel;
import com.cmdt.carrental.common.model.VehicleMaintainInfoModel;

public interface VehicleMaintenanceService {
	public VehicleMaintenance calcMaintenance(VehicleMaintenance vm) throws Exception;
	public VehicleMaintenance create(VehicleMaintenance maintenance);
	public VehicleMaintenance update(VehicleMaintenance maintenance);
	PagModel listPage(VehicleMaintainInfoModel model);
	public List<VehicleMaintenance> list(Long entId, String json);
	public Vehicle getVehicleBySql(String condition);
	public List<VehicleMaintenance> listBySql(String condition,List<Object> params);
	public int setThreshold(String json);
	public List<VehicleMaintenance> queryMaintenanceMileageAlert();
	public List<VehicleMaintenance> queryMaintenanceTimeAlert();
	public boolean modifyJobStatus(String filedName, int filedValue, Long id);
	public List<VehicleMaintenance> querySysnTravelMileageVehicleList();
	public void modifyMaintenanceJobTime(Long travelMileage, Date updateTime, Long vehicleId);
	public VehicleDataCountModel queryVehicleDataCountByHomePage(Long orgId, boolean isEnt);
	public Integer getVehicleMileageStasticByImeiAndBeginTime(Date curTime, String deviceNumber,String vehicleNumber);
	public String validateVehicleInfoAndTime(Integer num, Vehicle vehicle, VehicleMaintenance vmNew);
	public String validateData(Integer num, VehicleMaintenance vmOld, VehicleMaintenance vmNew);
}
