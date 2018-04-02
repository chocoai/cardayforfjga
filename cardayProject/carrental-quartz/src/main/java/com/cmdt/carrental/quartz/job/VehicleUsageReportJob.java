package com.cmdt.carrental.quartz.job;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleStasticModel;
import com.cmdt.carrental.quartz.service.VehicleUsageReportService;
import com.cmdt.carrental.quartz.spring.SpringUtils;
import com.cmdt.carrental.quartz.util.DateUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;


public class VehicleUsageReportJob extends QuartzJobBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Logger LOG = Logger.getLogger(VehicleUsageReportJob.class);
	
	@Autowired
	private VehicleUsageReportService vehicleUsageReportService;
	
	@Autowired
	private ShouqiService shouqiService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	private static DecimalFormat df = new DecimalFormat("0.00");
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		if(vehicleUsageReportService == null){
			vehicleUsageReportService = SpringUtils.getBean("vehicleUsageReportServiceImpl");
		}
		if(shouqiService == null){
			shouqiService = SpringUtils.getBean("shouqiService");
		}
		
		Date currentDate = new Date();
    	String currentDateVal = DateUtils.date2String(currentDate,"yyyy-MM-dd");
    	
    	
    	String starttime = currentDateVal + " 00:00:00";
    	String endtime = currentDateVal + " 23:59:59";
    	
		//获得所有的vehicle
		List<VehicleModel> vehicleList = vehicleUsageReportService.findVehicleList();
		if(vehicleList != null && vehicleList.size() > 0){
			int obdListSize = vehicleList.size();
			for(int i = 0 ; i < obdListSize ; i ++){
				VehicleModel tmpVehicleModel = vehicleList.get(i);
				//更新统计表中部门id与部门名(原因:之前存在企业管理员创建了车辆，未分配给部门，等过了一段时间再分配给部门)
				updateVehicleStasticCurrentOwner(tmpVehicleModel,currentDateVal);
				callFindTripPropertyDataByTimeRangeService(tmpVehicleModel,starttime,endtime,currentDate,currentDateVal);
			}
		}
	}
	
	private void updateVehicleStasticCurrentOwner(VehicleModel tmpVehicleModel,String currentDateVal) {
		Long currentuseOrgId = tmpVehicleModel.getCurrentuseOrgId();
		String currentuseOrgName = tmpVehicleModel.getCurrentuseOrgName();
		String vehicleNumber = tmpVehicleModel.getVehicleNumber();
    	String deviceNumber = tmpVehicleModel.getDeviceNumber();
	    if(StringUtils.isNotEmpty(currentuseOrgName)){
	    	vehicleUsageReportService.updateVehicleStasticCurrentOwner(currentuseOrgId,currentuseOrgName,vehicleNumber,deviceNumber,currentDateVal);
	    }else{//设置currentuseOrgId,currentuseOrgName为null
	    	vehicleUsageReportService.cleanCurrentOwner(vehicleNumber, deviceNumber, currentDateVal);
	    }
	}

	/**
	 * 车辆周期数据汇总
	 */
	public void callFindTripPropertyDataByTimeRangeService(VehicleModel vehicleModel,String starttime,String endtime,Date currentDate,String currentDateVal) {
		LOG.info("--------callFindTripPropertyDataByTimeRangeService start :" + vehicleModel.getVehicleNumber() + "," + vehicleModel.getDeviceNumber());
		
		if(vehicleModel.getDeviceNumber()==null||vehicleModel.getDeviceNumber().trim().length()==0){
			return;
		}
		
		try {
			VehicleStasticModel vehicleStasticModel = new VehicleStasticModel();
			vehicleStasticModel.setVehicleNumber(vehicleModel.getVehicleNumber());
			vehicleStasticModel.setDeviceNumber(vehicleModel.getDeviceNumber());
			vehicleStasticModel.setEntId(vehicleModel.getEntId());
			vehicleStasticModel.setEntName(vehicleModel.getEntName());
			vehicleStasticModel.setCurrentuseOrgId(vehicleModel.getCurrentuseOrgId());
			vehicleStasticModel.setCurrentuseOrgName(vehicleModel.getCurrentuseOrgName());
			vehicleStasticModel.setLastUpdatedTime(new java.sql.Timestamp(currentDate.getTime()));
			
			Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.FINDTRIPPROPERTYDATABYTIMERANGE, new Object[]{vehicleModel.getDeviceNumber(),starttime,endtime});
			if (result != null && result.get("status").equals("success") && result.get("data") != null && StringUtils.isNotEmpty(result.get("data").toString())) {
				JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
				if ("000".equals(jsonNode.get("status").asText())) {
					ArrayNode rows = (ArrayNode) jsonNode.get("result");
					if (rows != null && rows.size() > 0) {
						JsonNode resultNode = rows.get(rows.size()-1);
						//参数指标
						if(resultNode.get("mileage").asInt() != 0){
							LOG.info("--------callFindTripPropertyDataByTimeRangeService success :" + vehicleModel.getVehicleNumber() + "," + vehicleModel.getDeviceNumber());
							vehicleStasticModel.setTotalMileage(resultNode.get("mileage").asInt());
							vehicleStasticModel.setTotalFuelCons(Double.valueOf(df.format((double)vehicleStasticModel.getTotalMileage()*vehicleModel.getTheoreticalFuelCon()/100000)));//mileage*理论油耗=油耗
							vehicleStasticModel.setTotalDrivingTime(resultNode.get("drivetime").asInt());
						}else{
							LOG.info("--------callFindTripPropertyDataByTimeRangeService success,but data is not valid :" + vehicleModel.getVehicleNumber() + "," + vehicleModel.getDeviceNumber());
							//参数指标
							vehicleStasticModel.setTotalMileage(0);
							vehicleStasticModel.setTotalFuelCons(0.0);
							vehicleStasticModel.setTotalDrivingTime(0);
						}
					}
				}else{
					LOG.info("--------callFindTripPropertyDataByTimeRangeService failure(1)" + vehicleModel.getVehicleNumber() + "," + vehicleModel.getDeviceNumber());
					vehicleStasticModel.setTotalMileage(0);
					vehicleStasticModel.setTotalFuelCons(0.0);
					vehicleStasticModel.setTotalDrivingTime(0);
				}
			}else{
				LOG.info("--------callFindTripPropertyDataByTimeRangeService failure(2)" + vehicleModel.getVehicleNumber() + "," + vehicleModel.getDeviceNumber());
				//参数指标
				vehicleStasticModel.setTotalMileage(0);
				vehicleStasticModel.setTotalFuelCons(0.0);
				vehicleStasticModel.setTotalDrivingTime(0);
			}
			vehicleUsageReportService.saveOrUpdateVehicleStastic(vehicleStasticModel,currentDateVal);
		} catch (Exception e) {
			LOG.error("VehicleUsageReportJob callFindTripPropertyDataByTimeRangeService error, cause by\n",e);
		}

	}
	

	

}
