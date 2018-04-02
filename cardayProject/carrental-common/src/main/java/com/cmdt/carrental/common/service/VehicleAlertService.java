package com.cmdt.carrental.common.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import com.cmdt.carrental.common.entity.EventConfig;
import com.cmdt.carrental.common.entity.Marker;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.VehicleAlert;
import com.cmdt.carrental.common.model.AlertCountModel;
import com.cmdt.carrental.common.model.AlertReport;
import com.cmdt.carrental.common.model.AlertStatSQLModel;
import com.cmdt.carrental.common.model.CountModel;
import com.cmdt.carrental.common.model.MarkerModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.QueryAlertInfoModel;
import com.cmdt.carrental.common.model.StationModel;
import com.cmdt.carrental.common.model.TripTraceModel;
import com.cmdt.carrental.common.model.VehicleAlertQueryDTO;
import com.cmdt.carrental.common.model.VehicleHistoryTrack;
import com.cmdt.carrental.common.model.VehicleModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public interface VehicleAlertService {

	/**
	 * 查询车辆超速报警
	 * 
	 * @param vehicleAlertQueryDTO
	 * @return 报警数据列表
	 */
	public List<VehicleAlert> findVehicleAlert(VehicleAlertQueryDTO vehicleAlertQueryDTO);

	/**
	 * 根据车牌号查找markerId
	 * 
	 * @param vehicleNumber
	 * @return
	 */
	public List<Marker> findMarker(String vehicleNumber);

	/**
	 * 根据markerid查找marker
	 * 
	 * @param markerModelId
	 * @return
	 */
	public MarkerModel findMarker(Long markerModelId);

	/**
	 * 根据车牌号查找车辆信息
	 * 
	 * @param vehicleNumber
	 * @return
	 */
	public VehicleModel getVehicleByVehicleNumber(Long entId,String vehicleNumber);

	/**
	 * 填充经纬度
	 * 
	 * @param obdNode
	 * @param list
	 */
	public void populatePoint(JsonNode obdNode, List<TripTraceModel> list) throws Exception;

	/**
	 * 填充tracetime,speed,以及百度address
	 * 
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public List<TripTraceModel> populateSpeedAndAddress(JsonNode obdNode, List<TripTraceModel> list) throws Exception;

	/**
	 * 查找车辆历史轨迹
	 * 
	 * @param imei
	 * @param starttime
	 * @param endtime
	 * @return
	 * @throws Exception
	 */
	public List<VehicleHistoryTrack> findTripTraceDataByTimeRange(String imei, String starttime, String endtime) throws Exception;

	 /**
	  * 根据车牌号查找Station
	  * @param vehicleNumber
	  * @return
	  */
	// public List<StationModel> findStation(String vehicleNumber);
	 
	 /**
	  * 根据stationId查找Station
	  * @param id
	  * @return
	  */
	public List<StationModel> findStation(String vehicleNumber);

	/**
	 * 分页查询告警
	 * @param currentPage
	 * @param numPerPage
	 * @param rentId
	 * @param isEnt
	 * @return
	 */
	public PagModel findAlertByPage(int currentPage, int numPerPage, Long rentId, boolean isEnt);
	
	/**
	 * 企业查询
	 * @param rentId
	 * @param isEnt
	 * @param isPre
	 * @return
	 */
	public List<VehicleAlert> findTodayAlert(Long orgId,boolean isPre);
	
	/**
	 * 
	 * @param currentPage
	 * @param numPerPage
	 * @param rentId
	 * @param isEnt
	 * @return
	 */
	public List<AlertCountModel> findAlertCountByType(Long orgId);
	
	/**
	 * 按天查询统计
	 * @param orgIdList
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<AlertStatSQLModel> getDayAlert(List<Organization> orgIdList, Date startDate, Date endDate,Long orgId, Boolean self);
	
	/**
	 * 查询部门统计
	 * @param orgId
	 * @param orgIdList
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public AlertStatSQLModel getDeptAlert(Long orgId,List<Organization> orgIdList, Date startDate, Date endDate,Boolean self);
	
	/**
	 * 
	 * @param orgId
	 * @param alertType
	 * @param fromDate
	 * @param toDate
	 * @param selfDept
	 * @param childDept
	 * @return
	 */
	public List<CountModel> statisticDailyAlertByTypeDepartmentTimeRanger(Long orgId , String alertType,String fromDate,String toDate,Boolean selfDept, Boolean childDept);
	
	/**
	 * 
	 * @param orgIdList
	 * @param alertType
	 * @param fromDate
	 * @param toDate
	 * @param currentPage
	 * @param numPerPage
	 * @return
	 */
	public PagModel getAlertByDepartmentAndTimeRange(List<Organization> orgIdList,Boolean self,Long orgId,String alertType,String fromDate,String toDate,int currentPage, int numPerPage);
	
	/**
	 * find top x
	 * @param startDay
	 * @param endDay
	 * @param orgId
	 * @param selfDept
	 * @param childDept
	 * @param topCount
	 * @return
	 */
	public AlertReport queryVehicleAlertStatisticsTopX(Date startDay, Date endDay,Long orgId,Boolean selfDept, Boolean childDept,int topCount);
	
	/**
	 * CR-1608:部门管理员查看所有下级部门的车辆报警信息
	 * @param model
	 * @return
	 */
	public PagModel findVehicleAlertInfo(QueryAlertInfoModel model);

	/**
	 * @param string
	 * @param alertType
	 * @param startDay
	 * @param endDay
	 * @param selfDept
	 * @param childDept
	 * @return
	 */
	public List<CountModel> statAlertByType(String string, String alertType, String startDay, String endDay,
			Boolean selfDept, Boolean childDept);
	
	/**
	 * 查询所有Alert
	 * @param vehicleAlertQueryDTO
	 * @return
	 */
	public List<VehicleAlert> findAllVehicleAlertData(VehicleAlertQueryDTO vehicleAlertQueryDTO);
	
	
	/**
	 * 查询车辆超速报警所有数据
	 * 
	 * @param vehicleAlertQueryDTO
	 * @return 报警数据列表
	 */
	public List<VehicleAlert> findAllVehicleAlert(QueryAlertInfoModel model);
	
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
	
	/**
	 * 根据车辆ID检查是否产生违规告警
	 * @param vehicleId
	 * @return
	 */
	public boolean voilateAlarmProcess(Long vehicleId);
	
}
