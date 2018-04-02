package com.cmdt.carrental.common.service;

import java.util.List;

import com.cmdt.carrental.common.entity.Station;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.StationQueryDto;

public interface StationService {
	
	/**
     * 创建站点
     * @param user
     */
    public Station createStation(Station station);
    
    /**
     * 修改站点
     * @param station
     * @return
     */
    public Station updateStation(Station station);

    /**
     * 删除站点
     * @param station
     */
    public void deleteStation(Station station);

    /**
     * 根据ID查询站点
     * @param stationId
     * @return
     */
    public Station findStation(Long stationId);
    
    /**
     * 查询所有站点
     * @return
     */
    public List<Station> findAll();
    /**
     * 根据站点名查询站点
     * @param stationName
     * @return
     */
    public PagModel findStationsByName(String stationName, Long organizationId,String currentPage,String numPerPage);
    
    /**
     * 根据组织ID查询Station
     * @param organizationId
     * @return
     */
    public List<Station> findStationsByOrgId(String organizationId);
    
    /**
     * 根据站点名称查询Station
     * @param stationName
     * @return
     */
    public List<Station> findByStationName(String stationName);
    public List<Station> findByStationName(String stationName,Long entId);
    /**
     * 根据站点ID查询已分配车辆
     * @param stationId,organizationId
     * @return
     */
	public PagModel findStationAssignedVehicles(String stationId, Long organizationId,String currentPage,String numPerPage,Boolean isRent,Boolean isEnt);
	
    /**
     * 根据站点ID查询已分配车辆总数
     * @param stationId,organizationId
     * @return
     */
	public List<Vehicle> findStationAssignedVehiclesSum(String stationId, Long organizationId,Boolean isRent,Boolean isEnt);
	
	/**
	 * 根据站点ID查询可分配车辆
	 * @param stationId,organizationId
	 * @return
	 */
	public PagModel findStationAvialiableVehicles(String stationId, Long organizationId,String currentPage,String numPerPage,Boolean isRent,Boolean isEnt);
	
	/**
	 * 为站点分配车辆
	 * @param vehicleIds
	 * @param stationId
	 */
	public void assignVehicles(String vehicleIds, String stationId);

	/**
	 * 移除站点已分配车辆
	 * @param vehicleId
	 * @param stationId
	 */
	public void unassignVehicles(String vehicleId, String stationId);
	
	 /**
     * 根据站点名查询站点
     * @param stationQueryDto
     * @return
     */
	public PagModel findStationsByName(StationQueryDto stationQueryDto);
	
	/**
	 * 根据站点ID查询可分配车辆
	 * @param stationQueryDto
	 * @return
	 */
	public PagModel findStationAssignedVehicles(StationQueryDto stationQueryDto);
	
	/**
	 * 根据站点ID查询可分配车辆
	 * @param stationQueryDto
	 * @return
	 */
	public PagModel findStationAvialiableVehicles(StationQueryDto stationQueryDto);
}
