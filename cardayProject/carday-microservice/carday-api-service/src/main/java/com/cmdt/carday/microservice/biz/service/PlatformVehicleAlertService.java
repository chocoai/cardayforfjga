package com.cmdt.carday.microservice.biz.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carday.microservice.common.Constants;
import com.cmdt.carday.microservice.common.model.response.MessageCode;
import com.cmdt.carday.microservice.common.model.response.exception.ServiceException;
import com.cmdt.carday.microservice.model.request.alert.AlertDataByTypeDto;
import com.cmdt.carday.microservice.model.request.alert.AlertDataReportDto;
import com.cmdt.carday.microservice.model.request.alert.AlertDataTypePageReportDto;
import com.cmdt.carday.microservice.model.request.alert.AlertDataTypeReportDto;
import com.cmdt.carday.microservice.model.request.alert.AlertExportAllDto;
import com.cmdt.carday.microservice.model.request.alert.AlertExportDto;
import com.cmdt.carday.microservice.model.request.alert.AlertMarkerVehicleDto;
import com.cmdt.carday.microservice.model.request.alert.AlertVehicleCountDto;
import com.cmdt.carday.microservice.model.request.alert.AlertVehicleQueryDto;
import com.cmdt.carday.microservice.model.request.alert.StationInfoByVehicleNum;
import com.cmdt.carday.microservice.model.request.alert.VehicleAlertByPage;
import com.cmdt.carday.microservice.model.request.alert.VehicleAlertNoPageDto;
import com.cmdt.carrental.common.bean.AlertType;
import com.cmdt.carrental.common.entity.Marker;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.VehicleAlert;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.model.AlertCountModel;
import com.cmdt.carrental.common.model.AlertReport;
import com.cmdt.carrental.common.model.AlertStatisticModel;
import com.cmdt.carrental.common.model.CountModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.Point;
import com.cmdt.carrental.common.model.PositionAndStation;
import com.cmdt.carrental.common.model.QueryAlertInfoModel;
import com.cmdt.carrental.common.model.StationModel;
import com.cmdt.carrental.common.model.TransGPStoBaiduModel;
import com.cmdt.carrental.common.model.VehicleAlertQueryDTO;
import com.cmdt.carrental.common.model.VehicleHistoryTrack;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleOutBoundModel;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.VehicleAlertService;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.common.util.CsvUtil;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.ExportFileBean;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.common.util.TypeUtils;

@Service
public class PlatformVehicleAlertService {

	private static final Logger LOG = LoggerFactory.getLogger(PlatformVehicleAlertService.class);
	@Autowired
	private VehicleAlertService vehicleAlertService;
	@Autowired
	private ShouqiService shouqiService;
	@Autowired
	private VehicleService vehicleService;
	@Autowired
	private OrganizationService organizationService;

	public PagModel findAlertByType(User loginUser, QueryAlertInfoModel dto) throws Exception {
		if (dto.getIncludeChild() == false && dto.getIncludeSelf() == false) {
			throw new ServiceException(MessageCode.COMMON_PARAMETER_ERROR, "本部门 / 子部门 至少选择一个");
		}
		return vehicleAlertService.findVehicleAlertInfo(dto);

	}

	public File generateAlertExport(User loginUser, AlertExportDto dto) throws Exception {
		if (dto.getAlertType().equals(AlertType.OVERSPEED.toString())) {
			return generateOverSpeedFile(loginUser, dto);
		} else if (dto.getAlertType().equals(AlertType.OUTBOUND.toString())) {
			return generateOutboundFile(loginUser, dto);
		} else if (dto.getAlertType().equals(AlertType.VEHICLEBACK.toString())) {
			return generateVehicleBackFile(loginUser, dto);
		}
		return null;
	}

	public File generateOverSpeedFile(User loginUser, AlertExportDto dto) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> retList = new ArrayList<String>();

		VehicleAlertQueryDTO vehicleAlertQueryDTO = new VehicleAlertQueryDTO();
		vehicleAlertQueryDTO.setVehicleNumber(dto.getVehicleNumber());
		vehicleAlertQueryDTO.setVehicleType(dto.getVehicleType());
		vehicleAlertQueryDTO.setVehicleSource(dto.getFromOrgId());
		vehicleAlertQueryDTO.setOrganizationId(dto.getDeptId());
		vehicleAlertQueryDTO.setStartTime(TypeUtils.obj2Date(dto.getStartTime()));
		vehicleAlertQueryDTO.setEndTime(TypeUtils.obj2Date(dto.getEndTime()));
		vehicleAlertQueryDTO.setAlertType(AlertType.OVERSPEED.toString());

		boolean isEnt = false;
		// 企业管理员
		if (loginUser.isEntAdmin()) {
			isEnt = true;
		}

		// 部门管理员
		if (loginUser.isDeptAdmin()) {
			isEnt = false;
		}
		List<VehicleAlert> alertList = vehicleAlertService.findAllVehicleAlertData(vehicleAlertQueryDTO);
		if (alertList != null && alertList.size() > 0) {
			for (VehicleAlert vehicleAlert : alertList) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(vehicleAlert.getVehicleNumber()).append(",");
				buffer.append(vehicleAlert.getVehicleTypeName(vehicleAlert.getVehicleType())).append(",");
				buffer.append("超速,");
				buffer.append(vehicleAlert.getAlertTime()).append(",");
				buffer.append(vehicleAlert.getAlertSpeed()).append("km/h,");
				buffer.append(vehicleAlert.getOverspeedPercent()).append(",");
				buffer.append(vehicleAlert.getAlertPosition()).append(",");
				buffer.append(vehicleAlert.getVehicleSource()).append(",");
				buffer.append(vehicleAlert.getCurrentuseOrgName()).append(",");
				buffer.append(vehicleAlert.getDriverName()).append(",");
				buffer.append(vehicleAlert.getDriverPhone());
				retList.add(buffer.toString());
			}
		}

//		map.put("status", "success");
		map.put("data", retList);
		map.put("filename", "超速报警列表.xls");
		map.put("sheet", "超速报警列表");
		map.put("header", "车牌号,车辆类型,异常类型,报警时间,实际速度,超速比例,超速位置,车辆来源,部门,司机姓名,司机电话");
		return CsvUtil.exportExcel(map);
	}

	public File generateOutboundFile(User loginUser, AlertExportDto dto) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> retList = new ArrayList<String>();

		VehicleAlertQueryDTO vehicleAlertQueryDTO = new VehicleAlertQueryDTO();
		vehicleAlertQueryDTO.setVehicleNumber(dto.getVehicleNumber());
		vehicleAlertQueryDTO.setVehicleType(dto.getVehicleType());
		vehicleAlertQueryDTO.setVehicleSource(dto.getFromOrgId());
		vehicleAlertQueryDTO.setOrganizationId(dto.getDeptId());
		vehicleAlertQueryDTO.setStartTime(TypeUtils.obj2Date(dto.getStartTime()));
		vehicleAlertQueryDTO.setEndTime(TypeUtils.obj2Date(dto.getEndTime()));
		vehicleAlertQueryDTO.setAlertType(AlertType.OUTBOUND.toString());

		boolean isEnt = false;
		// 企业管理员
		if (loginUser.isEntAdmin()) {
			isEnt = true;
		}

		// 部门管理员
		if (loginUser.isDeptAdmin()) {
			isEnt = false;
		}

		List<VehicleAlert> alertList = vehicleAlertService.findAllVehicleAlertData(vehicleAlertQueryDTO);

		if (alertList != null && alertList.size() > 0) {
			for (VehicleAlert vehicleAlert : alertList) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(vehicleAlert.getVehicleNumber()).append(",");
				buffer.append(vehicleAlert.getVehicleTypeName(vehicleAlert.getVehicleType())).append(",");
				buffer.append("越界,");
				buffer.append(vehicleAlert.getFirstOutboundtime()).append(",");
				buffer.append(vehicleAlert.getOutboundReleasetime()).append(",");
				buffer.append(vehicleAlert.getOutboundMinutes()).append(",");
				buffer.append(vehicleAlert.getOutboundKilos()).append(",");
				buffer.append(vehicleAlert.getVehicleSource()).append(",");
				buffer.append(vehicleAlert.getCurrentuseOrgName()).append(",");
				buffer.append(vehicleAlert.getDriverName()).append(",");
				buffer.append(vehicleAlert.getDriverPhone());
				retList.add(buffer.toString());
			}
		}

		map.put("status", "success");
		map.put("data", retList);
		map.put("filename", "越界报警列表.xls");
		map.put("sheet", "越界报警列表");
		map.put("header", "车牌号,车辆类型,异常类型,首次发生时间,解除时间,越界时长(分钟),越界里程(KM),车辆来源,部门,司机姓名,司机电话");
		return CsvUtil.exportExcel(map);
	}

	public File generateVehicleBackFile(User loginUser, AlertExportDto dto) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> retList = new ArrayList<String>();

		VehicleAlertQueryDTO vehicleAlertQueryDTO = new VehicleAlertQueryDTO();
		vehicleAlertQueryDTO.setVehicleNumber(dto.getVehicleNumber());
		vehicleAlertQueryDTO.setVehicleType(dto.getVehicleType());
		vehicleAlertQueryDTO.setVehicleSource(dto.getFromOrgId());
		vehicleAlertQueryDTO.setOrganizationId(dto.getDeptId());
		vehicleAlertQueryDTO.setStartTime(TypeUtils.obj2Date(dto.getStartTime()));
		vehicleAlertQueryDTO.setEndTime(TypeUtils.obj2Date(dto.getEndTime()));
		vehicleAlertQueryDTO.setAlertType(AlertType.VEHICLEBACK.toString());
		boolean isEnt = false;
		// 企业管理员
		if (loginUser.isEntAdmin()) {
			isEnt = true;
		}

		// 部门管理员
		if (loginUser.isDeptAdmin()) {
			isEnt = false;
		}

		List<VehicleAlert> alertList = vehicleAlertService.findAllVehicleAlertData(vehicleAlertQueryDTO);
		if (alertList != null && alertList.size() > 0) {
			for (VehicleAlert vehicleAlert : alertList) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(vehicleAlert.getVehicleNumber()).append(",");
				buffer.append(vehicleAlert.getVehicleTypeName(vehicleAlert.getVehicleType())).append(",");
				buffer.append("回车,");
				buffer.append(TimeUtils.formatDate(vehicleAlert.getAlertTime())).append(",");
				buffer.append(vehicleAlert.getAlertCity()).append(",");
				buffer.append(vehicleAlert.getAlertPosition()).append(",");
				buffer.append(vehicleAlert.getVehicleSource()).append(",");
				buffer.append(vehicleAlert.getCurrentuseOrgName()).append(",");
				buffer.append(vehicleAlert.getDriverName()).append(",");
				buffer.append(vehicleAlert.getDriverPhone());
				retList.add(buffer.toString());
			}
		}

		map.put("status", "success");
		map.put("data", retList);
		map.put("filename", "回车报警列表.xls");
		map.put("sheet", "回车报警列表");
		map.put("header", "车牌号,车辆类型,异常类型,报警时间,城市,实际停车位置,车辆来源,部门,司机姓名,司机电话");
		return CsvUtil.exportExcel(map);
	}

	public VehicleModel findVehicleByVehicleNumber(User loginUser, AlertVehicleQueryDto dto) {
		return vehicleAlertService.getVehicleByVehicleNumber(loginUser.getOrganizationId(), dto.getVehicleNumber());
	}

	public PositionAndStation findStationByVehicleNumber(StationInfoByVehicleNum dto) {
		// 查找车牌号对应的所有Station
		List<StationModel> stationModels = vehicleAlertService.findStation(dto.getVehicleNumber());
		StationModel sPosition = new StationModel(); // 车的目前位置
		sPosition.setLatitude(dto.getLatitude() + "");
		sPosition.setLongitude(dto.getLongitude() + "");
		sPosition = transGPStoBaidu(sPosition);

		PositionAndStation positionAndStation = new PositionAndStation();
		positionAndStation.setLatitude(sPosition.getLatitude());
		positionAndStation.setLongitude(sPosition.getLongitude());
		positionAndStation.setStationModels(stationModels);
		return positionAndStation;
	}

	public VehicleOutBoundModel findMarkerByVehicleNumber(User loginUser, AlertMarkerVehicleDto dto) throws Exception {
		Long entId = loginUser.getOrganizationId();
		VehicleOutBoundModel vehicleOutBoundModel = new VehicleOutBoundModel();

		String vehicleNumber = dto.getVehicleNumber();
		String startTime = dto.getStartTime();
		String endTime = dto.getEndTime();
		if ("".equals(endTime)) {
			Date currentDate = new Date();
			endTime = DateUtils.date2String(currentDate, DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
			startTime = DateUtils.date2String(DateUtils.addDays(currentDate, -1), DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
		}

		List<Marker> markerIdlist = vehicleAlertService.findMarker(vehicleNumber);

		// 根据车牌号查询设备号
		VehicleModel vehicleModel = vehicleAlertService.getVehicleByVehicleNumber(entId,vehicleNumber);
		// 查找车在某个时间段的轨迹
		List<VehicleHistoryTrack> retList  = vehicleAlertService.findTripTraceDataByTimeRange(vehicleModel.getDeviceNumber(),
				startTime, endTime);
		
		vehicleOutBoundModel.setMarkers(markerIdlist);
		vehicleOutBoundModel.setTraceModels(retList);
		return vehicleOutBoundModel;
	}

	public PagModel findAlertByTypeDepartmentAndTimeRange(User loginUser, AlertDataByTypeDto dto) {

		PagModel pagModel = null;

		Long orgId = loginUser.getOrganizationId();
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId,
				dto.getIncludeSelf().booleanValue(), dto.getIncludeChild().booleanValue());
		if (!orgList.isEmpty()) {
			pagModel = vehicleAlertService.getAlertByDepartmentAndTimeRange(orgList,dto.getIncludeSelf().booleanValue(),orgId,dto.getAlertType(),
					dto.getStartTime(), dto.getEndTime(), dto.getCurrentPage(), dto.getNumPerPage());
		}
		return pagModel;
	}

	public List<CountModel> statAlertByTypeDepartmentAndTimeRange(User loginUser, AlertDataByTypeDto dto) {
		long orgId = 0l;
		if (null != dto.getOrgId()) {
			orgId = dto.getOrgId().longValue();
		}
		return vehicleAlertService.statAlertByType(orgId + "", dto.getAlertType(), dto.getStartTime(), dto.getEndTime(),
				dto.getIncludeSelf().booleanValue(), dto.getIncludeChild().booleanValue());
	}

	public AlertReport findVehicleAlertStatistics(User loginUser, AlertDataByTypeDto dto) {
		long orgId = 0l;
		if (null != dto.getOrgId()) {
			orgId = dto.getOrgId().longValue();
		}
		// 按输入日期区间进行统计
		Date startDate = DateUtils.string2Date(dto.getStartTime() + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
		Date endDate = DateUtils.string2Date(dto.getEndTime() + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
		// 获取车辆报警数据报表
		return vehicleAlertService.queryVehicleAlertStatisticsTopX(startDate,endDate,orgId,true,true,5);
	}

	public List<VehicleAlert> findVehicleAlertCountByDate(AlertVehicleCountDto dto) {
		VehicleAlertQueryDTO vehicleAlertQueryDTO = new VehicleAlertQueryDTO();
		vehicleAlertQueryDTO.setVehicleNumber(dto.getVehicleNumber());
		vehicleAlertQueryDTO.setStartTime(TypeUtils.obj2Date(dto.getStartTime()));
		vehicleAlertQueryDTO.setEndTime(TypeUtils.obj2Date(dto.getEndTime()));
		return vehicleAlertService.findVehicleAlert(vehicleAlertQueryDTO);
	}

	public PagModel findVehicleAlertByPage(User loginUser, VehicleAlertByPage dto) {
		Long rentId = loginUser.getOrganizationId();
		boolean isEnt = false;
		// 企业管理员
		if (loginUser.isEntAdmin()) {
			isEnt = true;
		}
		// 部门管理员
		if (loginUser.isDeptAdmin()) {
			isEnt = false;
		}
		return vehicleAlertService.findAlertByPage(dto.getCurrentPage(), dto.getNumPerPage(), rentId, isEnt);
	}

	public List<AlertStatisticModel> findVehicleAlertCount(User loginUser) {
		List<AlertStatisticModel> alertCountList = new ArrayList<AlertStatisticModel>();
		int outboundCount = 0;
		int overspeedCount = 0;
		int vehiclebackCount = 0;
		Long orgId = loginUser.getOrganizationId();

		List<AlertCountModel> countList = vehicleAlertService.findAlertCountByType(orgId);
		//count alert
		for(AlertCountModel model : countList){
				String val = model.getAlertType();
				switch(val){
        			case "OUTBOUND":
        				outboundCount = model.getValue();
        				break;
        			case "OVERSPEED":
        				overspeedCount = model.getValue();
        				break;
        			case "VEHICLEBACK":
        				vehiclebackCount = model.getValue();
        				break;
        		}
		}

		AlertStatisticModel alert_ob = new AlertStatisticModel();
		AlertStatisticModel alert_os = new AlertStatisticModel();
		AlertStatisticModel alert_vb = new AlertStatisticModel();
		alert_ob.setName(AlertType.OUTBOUND.toString());
		alert_ob.setValue(outboundCount);
		alert_os.setName(AlertType.OVERSPEED.toString());
		alert_os.setValue(overspeedCount);
		alert_vb.setName(AlertType.VEHICLEBACK.toString());
		alert_vb.setValue(vehiclebackCount);
		alertCountList.add(alert_ob);
		alertCountList.add(alert_os);
		alertCountList.add(alert_vb);
		return alertCountList;
	}

	public File exportAll(User loginUser, AlertExportAllDto dto) throws Exception {
		List<ExportFileBean> exportFileBeanList = new ArrayList<ExportFileBean>();

		VehicleAlertQueryDTO vehicleAlertQueryDTO = new VehicleAlertQueryDTO();
		vehicleAlertQueryDTO.setOrganizationId(dto.getDeptId());
		vehicleAlertQueryDTO.setStartTime(TypeUtils.obj2Date(dto.getStartTime()));
		vehicleAlertQueryDTO.setEndTime(TypeUtils.obj2Date(dto.getEndTime()));
		boolean isEnt = false;
		// 企业管理员
		if (loginUser.isEntAdmin()) {
			isEnt = true;
		}

		// 部门管理员
		if (loginUser.isDeptAdmin()) {
			isEnt = false;
		}

		// 超速
		vehicleAlertQueryDTO.setAlertType(AlertType.OVERSPEED.toString());
		List<String> overspeedList = new ArrayList<String>();
		List<VehicleAlert> overspeedAlertList = vehicleAlertService.findAllVehicleAlertData(vehicleAlertQueryDTO);
		ExportFileBean overspeedExportFileBean = new ExportFileBean();

		if (overspeedAlertList != null && overspeedAlertList.size() > 0) {
			for (VehicleAlert vehicleAlert : overspeedAlertList) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(vehicleAlert.getVehicleNumber()).append(",");
				buffer.append(vehicleAlert.getVehicleTypeName(vehicleAlert.getVehicleType())).append(",");
				buffer.append("超速,");
				buffer.append(vehicleAlert.getAlertTime()).append(",");
				buffer.append(vehicleAlert.getAlertSpeed()).append("km/h,");
				buffer.append(vehicleAlert.getOverspeedPercent()).append(",");
				buffer.append(vehicleAlert.getAlertPosition()).append(",");
				buffer.append(vehicleAlert.getVehicleSource()).append(",");
				buffer.append(vehicleAlert.getCurrentuseOrgName()).append(",");
				buffer.append(vehicleAlert.getDriverName()).append(",");
				buffer.append(vehicleAlert.getDriverPhone());
				overspeedList.add(buffer.toString());
			}
		}

		overspeedExportFileBean.setSheet("超速报警列表");
		overspeedExportFileBean.setHeader("车牌号,车辆类型,异常类型,报警时间,实际速度,超速比例,超速位置,车辆来源,部门,司机姓名,司机电话");
		overspeedExportFileBean.setData(overspeedList);
		exportFileBeanList.add(overspeedExportFileBean);

		// 越界
		vehicleAlertQueryDTO.setAlertType(AlertType.OUTBOUND.toString());
		List<String> outboundList = new ArrayList<String>();
		List<VehicleAlert> outboundAlertList = vehicleAlertService.findAllVehicleAlertData(vehicleAlertQueryDTO);
		if (outboundAlertList != null && outboundAlertList.size() > 0) {
			ExportFileBean outboundExportFileBean = new ExportFileBean();
			for (VehicleAlert vehicleAlert : outboundAlertList) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(vehicleAlert.getVehicleNumber()).append(",");
				buffer.append(vehicleAlert.getVehicleTypeName(vehicleAlert.getVehicleType())).append(",");
				buffer.append("越界,");
				buffer.append(vehicleAlert.getFirstOutboundtime()).append(",");
				buffer.append(vehicleAlert.getOutboundReleasetime()).append(",");
				buffer.append(vehicleAlert.getOutboundMinutes()).append(",");
				buffer.append(vehicleAlert.getOutboundKilos()).append(",");
				buffer.append(vehicleAlert.getVehicleSource()).append(",");
				buffer.append(vehicleAlert.getCurrentuseOrgName()).append(",");
				buffer.append(vehicleAlert.getDriverName()).append(",");
				buffer.append(vehicleAlert.getDriverPhone());
				outboundList.add(buffer.toString());
			}

			outboundExportFileBean.setSheet("越界报警列表");
			outboundExportFileBean.setHeader("车牌号,车辆类型,异常类型,首次发生时间,解除时间,越界时长(分钟),越界里程(KM),车辆来源,部门,司机姓名,司机电话");
			outboundExportFileBean.setData(outboundList);
			exportFileBeanList.add(outboundExportFileBean);
		}

		// 回车
		vehicleAlertQueryDTO.setAlertType(AlertType.VEHICLEBACK.toString());
		List<String> backList = new ArrayList<String>();
		List<VehicleAlert> backAlertList = vehicleAlertService.findAllVehicleAlertData(vehicleAlertQueryDTO);
		if (backAlertList != null && backAlertList.size() > 0) {
			ExportFileBean backExportFileBean = new ExportFileBean();
			for (VehicleAlert vehicleAlert : backAlertList) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(vehicleAlert.getVehicleNumber()).append(",");
				buffer.append(vehicleAlert.getVehicleTypeName(vehicleAlert.getVehicleType())).append(",");
				buffer.append("回车,");
				buffer.append(vehicleAlert.getAlertTime()).append(",");
				buffer.append(vehicleAlert.getAlertCity()).append(",");
				buffer.append(vehicleAlert.getAlertPosition()).append(",");
				buffer.append(vehicleAlert.getVehicleSource()).append(",");
				buffer.append(vehicleAlert.getCurrentuseOrgName()).append(",");
				buffer.append(vehicleAlert.getDriverName()).append(",");
				buffer.append(vehicleAlert.getDriverPhone());
				backList.add(buffer.toString());
			}
			backExportFileBean.setSheet("回车报警列表");
			backExportFileBean.setHeader("车牌号,车辆类型,异常类型,报警时间,城市,实际停车位置,车辆来源,部门,司机姓名,司机电话");
			backExportFileBean.setData(backList);
			exportFileBeanList.add(backExportFileBean);
		}

		return CsvUtil.exportExcelWithMultiSheet("异常用车统计.xls", exportFileBeanList);
	}

	private StationModel transGPStoBaidu(StationModel stationModel) {
		Point point = new Point();
		point.setLat(Double.valueOf(stationModel.getLatitude()));
		point.setLon(Double.valueOf(stationModel.getLongitude()));
		List<Point> points = new ArrayList<Point>();
		points.add(point);
		String result = shouqiService.transGPStoBaidu(points);
		TransGPStoBaiduModel transGPStoBaiduModel = JsonUtils.json2Object(result, TransGPStoBaiduModel.class);
		if (transGPStoBaiduModel.getResult().size() > 0) {
			stationModel.setLatitude(String.valueOf(transGPStoBaiduModel.getResult().get(0).getLat()));
			stationModel.setLongitude(String.valueOf(transGPStoBaiduModel.getResult().get(0).getLon()));
		}
		return stationModel;
	}

	public List<VehicleAlert> findVehicleAlert(User loginUser, VehicleAlertNoPageDto dto) {
		Long orgId = loginUser.getOrganizationId();
		return vehicleAlertService.findTodayAlert(orgId, dto.getIsPre().booleanValue());
	}

	public AlertReport queryVehicleAlertStatisticsTopX(AlertDataReportDto alertDataReportDto) {
		Long orgId = alertDataReportDto.getOrgId();
		Boolean selfDept = alertDataReportDto.getSelfDept();
		Boolean childDept = alertDataReportDto.getChildDept();
		
		String starttime = alertDataReportDto.getStartDay();
		String endtime = alertDataReportDto.getEndDay();
		
		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
			//如果不输入时间，取最近一个月时间作为开始和结束时间
			Date endDate = new Date();
	    	Date startDate = DateUtils.addDays(endDate,-29);
	    	starttime = DateUtils.date2String(startDate,"yyyy-MM-dd");
	    	endtime = DateUtils.date2String(endDate,"yyyy-MM-dd");
		}
		
		Date startDate = DateUtils.string2Date(starttime + " 00:00:00","yyyy-MM-dd HH:mm:ss");
		Date endDate = DateUtils.string2Date(endtime + " 23:59:59","yyyy-MM-dd HH:mm:ss");
		return vehicleAlertService.queryVehicleAlertStatisticsTopX(startDate,endDate,orgId,selfDept,childDept,5);
	}

	public List<CountModel> statAlertByType(AlertDataTypeReportDto alertDataTypeReportDto) {
		Long orgId = alertDataTypeReportDto.getOrgId();
		Boolean selfDept = alertDataTypeReportDto.getSelfDept();
		Boolean childDept = alertDataTypeReportDto.getChildDept();
		
		String startDay = alertDataTypeReportDto.getStartDay().replace("-", "");
		String endDay = alertDataTypeReportDto.getEndDay().replace("-", "");
		String alertType = alertDataTypeReportDto.getAlertType();
		return vehicleAlertService.statisticDailyAlertByTypeDepartmentTimeRanger(orgId, alertType, startDay, endDay,selfDept,childDept);
	}

	public PagModel findAlertDataByType(AlertDataTypePageReportDto alertDataTypePageReportDto) {
		PagModel pagModel= new PagModel();
		Long orgId = alertDataTypePageReportDto.getOrgId();
		Boolean selfDept = alertDataTypePageReportDto.getSelfDept();
		Boolean childDept = alertDataTypePageReportDto.getChildDept();
		String startDay = alertDataTypePageReportDto.getStartDay().replace("-", "");
		String endDay = alertDataTypePageReportDto.getEndDay().replace("-", "");
		String alertType = alertDataTypePageReportDto.getAlertType();
		
		Integer currentPage = alertDataTypePageReportDto.getCurrentPage();
		Integer numPerPage = alertDataTypePageReportDto.getNumPerPage();
		if(currentPage == null || numPerPage == null){
			currentPage = 1;
			numPerPage = 10;
		}
		
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, selfDept , childDept);
		if(orgList!=null&&orgList.size()>0){
		  pagModel = vehicleAlertService.getAlertByDepartmentAndTimeRange(orgList,selfDept,orgId,alertType, startDay, endDay, currentPage, numPerPage);
		}
		return pagModel;
	}
}
