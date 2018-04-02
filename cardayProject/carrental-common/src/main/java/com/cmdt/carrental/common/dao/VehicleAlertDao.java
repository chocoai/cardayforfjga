package com.cmdt.carrental.common.dao;

import java.util.Date;
import java.util.List;

import com.cmdt.carrental.common.entity.EventConfig;
import com.cmdt.carrental.common.entity.Marker;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.VehicleAlert;
import com.cmdt.carrental.common.model.AlertCountModel;
import com.cmdt.carrental.common.model.AlertStatSQLModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.QueryAlertInfoModel;
import com.cmdt.carrental.common.model.StationModel;
import com.cmdt.carrental.common.model.VehicleAlertQueryDTO;
import com.cmdt.carrental.common.model.VehicleAlertStatistics;
import com.cmdt.carrental.common.model.VehicleModel;

public interface VehicleAlertDao {
	/**
	 * 查询车辆超速报警
	 * @param vehicleAlertQueryDTO
	 * @return 报警数据列表
	 */
	public List<VehicleAlert> findVehicleAlert(VehicleAlertQueryDTO vehicleAlertQueryDTO);
	
	/**
	 * 根据车牌号查询Marker
	 * @param vehicleNumber
	 * @return List<MarkerModel> 
	 */
	public List<Marker> findMarker(String vehicleNumber);

	/**
	 * 根据车牌号查询车辆信息
	 * @param vehicleNumber
	 * @return VehicleModel
	 */
	public VehicleModel getVehicleByVehicleNumber(Long entId,String vehicleNumber);

	/**
	 * 根据车牌号查询Station
	 * @param vehicleNumber
	 * @return
	 */
	//public List<StationModel> findStation(String vehicleNumber);
	/**
	 * 根据stationId查找对应的Station
	 * @param id
	 * @return
	 */
	public List<StationModel> findStation(String vehicleNumber);
	
     /**
      * 
      * @param currentPage
      * @param numPerPage
      * @param rentId
      * @param isEnt
      * @return
      */
	public PagModel findAlertByPage(int currentPage, int numPerPage, Long rentId, boolean isEnt);
	
	/**
	 * 
	 * @param orgId
	 * @param orgList
	 * @param isPre
	 * @return
	 */
	public List<VehicleAlert> findTodayAlert(Long orgId, List<Organization> orgList,boolean isPre);
	
	/**
	 * 
	 * @param orgId
	 * @param orgList
	 * @return
	 */
	public List<AlertCountModel> findAlertCountList(Long orgId, List<Organization> orgList);
	
	/**
	 * 
	 * @param vehicleNumber
	 * @return
	 */
	public List<StationModel> queryStationNameByVehicleNumber(String vehicleNumber);

	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param orgList
	 * @return
	 */
	public List<AlertStatSQLModel> getDayAlertStat(Date startDate, Date endDate, List<Organization> orgList,Long orgId, Boolean self);
	
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param orgList
	 * @return
	 */
	public List<AlertStatSQLModel> getDeptAlertStat(Date startDate, Date endDate, List<Organization> orgList,Long orgId,Boolean self);

    /**
     * 
     * @param isEnt
     * @param entId
     * @param vehicleAlertQueryDTO
     * @return
     */
	public List<VehicleAlert> findAllVehicleAlert(QueryAlertInfoModel model,List<Organization> orgList);

    /**
     * 
     * @param model
     * @param orgIdList
     * @return
     */
	public PagModel findVehicleAlertInfo(QueryAlertInfoModel model, List<Long> orgIdList);

    /**
     * 
     * @param orgList
     * @param vehicleAlertQueryDTO
     * @return
     */
	//public List<VehicleAlert> findAllVehicleAlertData(List<Long> orgList,VehicleAlertQueryDTO vehicleAlertQueryDTO);
	
	
	
	/**
	 * 根据企业ID获取告警相关配置
	 * @param entId
	 * @return
	 */
	public List<EventConfig> findAlarmConfig(Long entId);
	
	/**
	 * 开启/关闭告警
	 * @param entId
	 * @param eventType
	 * @param isEnable
	 */
	public void updateAlarmConfig(Long entId,String eventType,Boolean isEnable);
	
	
}
