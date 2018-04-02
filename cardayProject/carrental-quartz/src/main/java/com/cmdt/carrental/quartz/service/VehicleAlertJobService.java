package com.cmdt.carrental.quartz.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.MarkerModel;
import com.cmdt.carrental.common.model.OBDQueryDTO;
import com.cmdt.carrental.common.model.OutboundMarkerModel;
import com.cmdt.carrental.common.model.RealtimeLatestDataModel;
import com.cmdt.carrental.common.model.StationModel;
import com.cmdt.carrental.common.model.VehicleBackModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleOverspeedModel;
import com.cmdt.carrental.common.model.VehicleQueryDTO;
import com.cmdt.carrental.quartz.filter.Filter;

public interface VehicleAlertJobService<T> {

	/**
	 * 
	 * @return
	 */
	public List<VehicleQueryDTO> findVehicleList();
	
	/**
	 * 
	 * @param vehicleModelList
	 * @return
	 */
	public Map<String,String> toMap(List<VehicleQueryDTO> vehicleModelList);
	
	/**
	 * 
	 * @param vehicleModelList
	 * @return
	 */
	public List<OBDQueryDTO> toOBDList(List<VehicleQueryDTO> vehicleModelList);
	
	/**
	 * 生成过滤后的报警数据并保存.
	 * @param filter
	 */
	public void executeAlertData(Filter<T> filter);

	/**
	 * 
	 * @param vehicleBackModel
	 * @param obdDataMap
	 * @return
	 */
	public void limit(VehicleBackModel vehicleBackModel, Map<String, RealtimeLatestDataModel> obdDataMap);
	
	/**
	 * 
	 * @param outboundMarkerModel
	 * @param traceDataMap
	 * @return
	 */
	public void limit(OutboundMarkerModel outboundMarkerModel, Map<String, RealtimeLatestDataModel> obdDataMap);

	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public String getPosition(double latitude, double longitude);
	
	/**
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public String getCityByPosition(double latitude, double longitude);

	/**
	 * 
	 * @param vehicleNumber
	 * @return
	 */
	public VehicleModel getVehicle(String vehicleNumber);

	/**
	 * 
	 * @param vehicleNumber
	 * @param traceTime
	 * @return
	 */
	public DriverModel findDriver(String vehicleNumber, Date traceTime);

	/**
	 * 
	 * @param vehicleNumber
	 */
	public List<MarkerModel> findMarker(String vehicleNumber);

	/**
	 * 
	 * @param vehicleNumber
	 * @return
	 */
	public List<StationModel> findStation(String vehicleNumber);
	
	public List<VehicleQueryDTO> findVehicleListHasMarker();
	
	public List<VehicleQueryDTO> findVehicleListHasStationAndTimeLimit();
	
	public DriverModel findDefaultDriverByVehicleNumber(String vehicleNumber);

}
