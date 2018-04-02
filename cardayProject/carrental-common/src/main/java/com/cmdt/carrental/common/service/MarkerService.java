package com.cmdt.carrental.common.service;

import java.util.List;

import com.cmdt.carrental.common.model.MarkerModel;
import com.cmdt.carrental.common.model.MarkerQueryDto;
import com.cmdt.carrental.common.entity.Marker;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.model.PagModel;

public interface MarkerService {
	/**
     * 创建Marker
     * @param user
     */
    public Marker createMarker(Marker marker);
    
    /**
     * 修改Marker
     * @param markerModel
     * @return
     */
    public Marker updateMarker(Marker marker);

    /**
     * 删除Marker
     * @param markerModelId
     */
	public void deleteMarker(Marker marker);

    /**
     * 根据ID查询Marker
     * @param markerModelId
     * @return
     */
    public Marker findMarker(Long markerId);
    
    /**
     * 查询所有Marker
     * @return
     */
    public List<MarkerModel> findAll();
    /**
     * 根据Marker名查询Marker
     * @param geofenceName
     * @return
     */
/*    public PagModel findMarkerByName(String geofenceName,String currentPage,String numPerPage);*/
    public PagModel findMarkerByName(String geofenceName, Long organizationId,String currentPage,String numPerPage);
    /**
     * 根据MarkerID查询已分配车辆
     * @param markerId,organizationId
     * @return
     */
	public PagModel findMarkerAssignedVehicles(String markerId, Long organizationId,String currentPage,String numPerPage,Boolean isRent,Boolean isEnt);
	
	
    /**
     * 根据MarkerID查询已分配车辆总数
     * @param markerId,organizationId
     * @return
     */
	public List<Vehicle> findMarkerAssignedVehiclesSum(String markerId, Long organizationId,Boolean isRent,Boolean isEnt);

	/**
	 * 根据MarkerID查询可分配车辆
	 * @param markerId,organizationId
	 * @return
	 */
	public PagModel findMarkerAvialiableVehicles(String markerId, Long organizationId,String currentPage,String numPerPage,Boolean isRent,Boolean isEnt);
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
     * 根据Marker名查询Marker
     * @param markerQueryDto
     * @return
     */
	 public PagModel findMarkerByName(MarkerQueryDto markerQueryDto);
	 
	 /**
	 * 根据Marker ID查询可分配车辆
	 * @param markerQueryDto
	 * @return
	 */
	public PagModel findMarkerAvialiableVehicles(MarkerQueryDto markerQueryDto);
	
	/**
     * 根据Marker ID查询已分配车辆
     * @param markerQueryDto
     * @return
     */
	public PagModel findMarkerAssignedVehicles(MarkerQueryDto markerQueryDto);
}
