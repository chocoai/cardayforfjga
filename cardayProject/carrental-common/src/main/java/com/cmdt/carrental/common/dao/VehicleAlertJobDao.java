package com.cmdt.carrental.common.dao;

import java.util.Date;
import java.util.List;

import com.cmdt.carrental.common.entity.EventConfig;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.model.AlertDiagramStatisticModel;
import com.cmdt.carrental.common.model.AlertStatisticModel;
import com.cmdt.carrental.common.model.CountModel;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.MarkerModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.StationModel;
import com.cmdt.carrental.common.model.StationsDurationModel;
import com.cmdt.carrental.common.model.VehicleAlertModel;
import com.cmdt.carrental.common.model.VehicleAlertStatistics;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleQueryDTO;

public interface VehicleAlertJobDao {

	public List<VehicleQueryDTO> findVehicleList();
	
	public VehicleAlertModel create(VehicleAlertModel vehicleAlertModel);

	public List<VehicleAlertModel> create(List<VehicleAlertModel> filteredData);

	public List<MarkerModel> findMarker(String vehicleNumber);

	public List<StationModel> findStation(String vehicleNumber);
	
	public List<StationModel> findStation(Long vehicleId);
	
	public List<StationModel> findStationByIds(String ids);

	public VehicleModel getVehicle(String vehicleNumber);

	public DriverModel findDriver(String vehicleNumber, Date traceTime);

	public void relaseOutboundAlert(VehicleAlertModel vehicleAlertModel);

	public VehicleAlertModel getLatestOutboundData(VehicleModel vehicleModel);

	public List<VehicleAlertStatistics> doVehicleAlertStatistics();

	public List<VehicleAlertStatistics> doOutboundkiloStatistics();
	
	public VehicleAlertStatistics createStatistics(VehicleAlertStatistics vehicleAlertStatistics);

	public List<VehicleQueryDTO> findVehicleHasMarker();
	
	public void updateOutboundAlert(VehicleAlertModel vehicleAlertModel);
	
    public VehicleAlertModel getLastAlertDataByType(String vehicleNumber,String alertType);
    
    public List<VehicleQueryDTO> findVehicleListHasStationAndTimeLimit();
    
    public List<VehicleQueryDTO> findVehicleHasOrg();
    
    public StationsDurationModel findStationsDurationByVechileNumber(String vehicleNumber);
    
    public List<AlertStatisticModel> statisticAlertByTypeAndTimeRanger(List<Organization> orgList,Long orgId,Boolean selfDept,Boolean childDept,String alertType,String fromDate,String toDate);
    
    public VehicleAlertModel getAlertById(Long alertId);
    
    public List<CountModel> statisticDailyAlertByTypeAndTimeRanger(List<Long> orgIdList,Long orgId,Boolean selfDept, String alertType,String fromDate,String toDate);
    
    public PagModel queryAlertByOrgTypeAndTimeRange(String alertType,Boolean self,Long orgId,String fromDate,String toDate,List<Organization> orgIdList,int currentPage, int numPerPage);
    
    public VehicleModel getVehicleByImei(String imei);
    
    public List<AlertDiagramStatisticModel> queryVehicleAlertStatistics(Date startDay, Date endDay,List<Organization> orgList,String alertType,Long orgId,Boolean self);
    
    public DriverModel findDefaultDriverByVehicleNumber(String vehicleNumber);
    
    public EventConfig queryAlarmConfig(Long vehicleId,String eventType);

}
