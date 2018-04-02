package com.cmdt.carrental.common.dao;

import java.util.List;

import com.cmdt.carrental.common.entity.Marker;
import com.cmdt.carrental.common.entity.Station;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.model.MarkerQueryDto;
import com.cmdt.carrental.common.model.PagModel;

public interface MarkerDao {
	
	/**
     * 创建地理围栏
     * @param user
     */
    public Marker createMarker(Marker marker);
    	
    /**
     * 根据地理围栏名称查询地理围栏
     * @param markerName
     * @return
     */
    public PagModel findMarkerByName(String geofenceName, Long organizationId, String currentPage,String numPerPage);
    
	
	/**
     * 根据MarkerID查询已分配车辆
     * @param markerId,organizationId
     * @return
     */
	public PagModel findMarkerAssignedVehicles(String markerId, Long organizationId, String currentPage,String numPerPage, Boolean isRent, Boolean isEnt);	
	
    /**
     * 根据MarkerID查询已分配车辆总数
     * @param markerId,organizationId
     * @return
     */
	public List<Vehicle> findMarkerAssignedVehiclesSum(String markerId, Long organizationId, Boolean isRent,Boolean isEnt);

	/**
	 * 根据MarkerID查询可分配车辆
	 * @param markerId,organizationId
	 * @return
	 */
	public PagModel findMarkerAvialiableVehicles(String markerId, Long organizationId, String currentPage,String numPerPage, Boolean isRent, Boolean isEnt);
	/**
	 * 为Marker分配车辆
	 * @param vehicleIds
	 * @param markerId
	 */
	public void assignVehicles(String vehicleIds, String markerId);

	/**
	 * 移除Marker已分配车辆
	 * @param vehicleId
	 * @param markerId
	 */
	public void unassignVehicles(String vehicleId, String markerId);

    /**
     * 根据地理围栏Id查询地理围栏
     * @param markerName
     * @return
     */
	public Marker findMarker(Long markerId);
	
    /**
     * 修改地理围栏
     * @param marker
     * @return
     */
	public Marker updateMarker(Marker marker);
	
    /**
     * 删除地理围栏
     * @param marker
     * @return
     */
	public void deleteMarker(Marker marker);
	
	/**
     * 根据地理围栏名称查询地理围栏
     * @param markerQueryDto
     * @return
     */
	public PagModel findMarkerByName(MarkerQueryDto markerQueryDto);
	
	/**
	 * 根据Marker ID查询可分配车辆
	 * @param markerQueryDto
	 * @return
	 */
	public PagModel findMarkerAvialiableVehicles(List<Long> orgIds,MarkerQueryDto markerQueryDto);
	
	 /**
     * 根据Marker ID查询已分配车辆总数
     * @param markerQueryDto
     * @return
     */
	public PagModel findMarkerAssignedVehicles(List<Long> orgIds,MarkerQueryDto markerQueryDto);
}
