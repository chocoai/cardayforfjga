package com.cmdt.carrental.common.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.dao.VehicleMaintenanceDao;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.entity.VehicleMaintenance;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.UsageReportSQLModel;
import com.cmdt.carrental.common.model.VehicleDataCountModel;
import com.cmdt.carrental.common.model.VehicleMaintainInfoModel;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TimeUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class VehicleMaintenanceServiceImpl implements VehicleMaintenanceService {
	@Autowired
    private ShouqiService shouqiService;
	
    @Autowired
    private VehicleMaintenanceDao vmDao;
    
    @Autowired
    private OrganizationService organizationService;

    private static final ObjectMapper MAPPER = new ObjectMapper();
    
	@Override
	public VehicleMaintenance calcMaintenance(VehicleMaintenance vm) throws Exception {
		String imei=vm.getDeviceNumber();
		String starttime=vm.getCurTimeF();//前端传过来的是带时分秒的格式
		Long gapMileage=0l;
		//只有导入时间不为当天时间才进行计算
//		if(!starttime.equals(DateUtils.getNowDate()+" 00:00:00")){
			String endtime=DateUtils.getNowDate(DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
			Date updateTime = DateUtils.getCurrentTime();
			vm.setUpdateTime(updateTime);
			int gap=endtime.compareTo(starttime);
			vm.setGap(gap);
			if(StringUtils.isEmpty(imei)) {
				vm.setTravelMileage(0L);
				return vm;
			}
			if(gap>0){
	    		Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.FINDTRIPPROPERTYDATABYTIMERANGE, new Object[] { imei, starttime, endtime });
	    		if (result.get("data") != null) {
	    			JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
	    			if ("000".equals(jsonNode.get("status").asText())) {
	    				ArrayNode rows = (ArrayNode) jsonNode.get("result");
	    				if (rows != null && rows.size() > 0) {
	    					JsonNode dataNode = null;
	    					// 涉及到obd更换，只获得最新obd数据
	    					if (rows.size() == 1) {
	    						dataNode = rows.get(0);
	    					} else if (rows.size() == 2) {
	    						dataNode = rows.get(1);
	    					}
	    					gapMileage=dataNode.get("mileage").asLong();
	    					float temp  = (float)gapMileage/1000;
	    					gapMileage = Long.valueOf(Math.round(temp));
	    				}
	    			}
	    		}
			}
//		}else{
//			vm.setGap(0);
//		}
//		vm.setHeaderLatestMileage(vm.getHeaderMaintenanceMileage()+gapMileage);
		vm.setTravelMileage(gapMileage);
		return vm;
	}
	
	@Override
	public VehicleMaintenance update(VehicleMaintenance maintenance) {
		return vmDao.update(maintenance);
	}

	@Override
	public VehicleMaintenance create(VehicleMaintenance maintenance) {
		return vmDao.create(maintenance);
	}

	@Override
	public PagModel listPage(VehicleMaintainInfoModel model) {
		List<Organization> list=organizationService.findDownOrganizationListByOrgId(model.getDeptId(),model.getIncludeSelf(),model.getIncludeChild());
		List<Long> orgIdList=new ArrayList<Long>();
		for (Organization organization : list) {
			orgIdList.add(organization.getId());
		}
		if (orgIdList.isEmpty()) {
			return null;
		}
		PagModel pagModel= vmDao.listPage(model,orgIdList);
		List<VehicleMaintenance> resultList = pagModel.getResultList();
		if(resultList != null) {
			for(VehicleMaintenance vehicleMaintenance : resultList) {
				int maintenanceTime = vehicleMaintenance.getMaintenanceTime();
				Date startDate = vehicleMaintenance.getCurTime();
				if(startDate != null && maintenanceTime != 0) {
					vehicleMaintenance.setMaintenanceNextTime(vehicleMaintenance.getMaintenanceDueTimeF());
					Date maintenanceThresholdTime = vehicleMaintenance.getMaintenanceThresholdTime();
					boolean status = DateUtils.compareTime(new Date(), maintenanceThresholdTime);
					int maintenanceRemainingTime = 0;
					if(status) {
						maintenanceRemainingTime = 2;
					}
					vehicleMaintenance.setMaintenanceRemainingTime(maintenanceRemainingTime);
				}else {
					vehicleMaintenance.setMaintenanceNextTime("");
					vehicleMaintenance.setMaintenanceRemainingTime(-9999);
				}
			}
		}
		return pagModel;
	}

	@Override
	public List<VehicleMaintenance> list(Long entId, String json) {
//		List<String> list = vmDao.list(entId, json);
		List<VehicleMaintenance> resultList = vmDao.queryExportList(entId, json);
		if(resultList != null) {
			for(VehicleMaintenance vehicleMaintenance : resultList) {
				String orgName = vehicleMaintenance.getOrgName();
				if(StringUtils.isEmpty(orgName)) {
					vehicleMaintenance.setOrgName("未分配");
				}
				int maintenanceTime = vehicleMaintenance.getMaintenanceTime();
				Date startDate = vehicleMaintenance.getCurTime();
				if(startDate != null && maintenanceTime != 0) {
					Date maintenanceNextTime = TimeUtils.countDateByMonth(startDate, maintenanceTime);
					vehicleMaintenance.setMaintenanceNextTime(TimeUtils.dateToString(maintenanceNextTime));
					Date maintenanceThresholdTime = vehicleMaintenance.getMaintenanceThresholdTime();
					boolean status = DateUtils.compareTime(new Date(), maintenanceThresholdTime);
					int maintenanceRemainingTime = 0;
					if(status) {
						maintenanceRemainingTime = 2;
					}
					vehicleMaintenance.setMaintenanceRemainingTime(maintenanceRemainingTime);
				}else {
					vehicleMaintenance.setMaintenanceNextTime("");
					vehicleMaintenance.setMaintenanceRemainingTime(-9999);
				}
			}
		}
		return resultList;
	}

	@Override
	public List<VehicleMaintenance> listBySql(String condition, List<Object> params) {
		return vmDao.listBySql(condition, params);
	}

	@Override
	public Vehicle getVehicleBySql(String condition) {
		List<Vehicle> list=vmDao.listVehicleBySql(condition);
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public int setThreshold(String json) {
		int status = -1;
		VehicleMaintenance maintenance = new VehicleMaintenance();
		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
		maintenance.setId(Long.valueOf(String.valueOf(jsonMap.get("id"))));
		maintenance.setMaintenanceMileage(Long.valueOf(String.valueOf(jsonMap.get("alertMileage"))));//维保里程
		maintenance.setThresholdMonth(Integer.parseInt(String.valueOf(jsonMap.get("thresholdMonth"))));//维保时间
		String dateTemp = String.valueOf(jsonMap.get("maintenanceDueTimeF"));//下次保养时间
		Date maintenanceDueTime = DateUtils.string2Date(dateTemp, "yyyy-MM-dd");
//		String maintenanceDueTimeF =  String.valueOf(jsonMap.get("maintenanceDueTimeF"));
//		maintenance.setMaintenanceDueTimeF(maintenanceDueTimeF);
		int monthNum = Integer.parseInt("-" + Math.abs(maintenance.getThresholdMonth()));
		Date maintenanceThresholdTime = TimeUtils.countDateByMonth(maintenanceDueTime, monthNum);
		
		maintenance.setMaintenanceThresholdTime(maintenanceThresholdTime);
		maintenance = vmDao.setThreshold(maintenance);
		if(maintenance != null) {
			status = 1;
		}
		return status;
	}
	
	@Override
	public List<VehicleMaintenance> queryMaintenanceMileageAlert() {
		return vmDao.queryMaintenanceMileageAlert();
	}
	
	@Override
	public List<VehicleMaintenance> queryMaintenanceTimeAlert() {
		return vmDao.queryMaintenanceTimeAlert();
	}
	
	@Override
	public boolean modifyJobStatus(String filedName, int filedValue, Long id) {
		return vmDao.modifyJobStatus(filedName, filedValue, id);
	}
	
	@Override
	public List<VehicleMaintenance> querySysnTravelMileageVehicleList() {
		return vmDao.querySysnTravelMileageVehicleList();
	}
	
	@Override
	public void modifyMaintenanceJobTime(Long travelMileage, Date updateTime, Long vehicleId) {
		vmDao.modifyMaintenanceJobTime(travelMileage, updateTime, vehicleId);
	}
	
	@Override
	public VehicleDataCountModel queryVehicleDataCountByHomePage(Long orgId, boolean isEnt) {
		Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 2);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        String monthStr;
        if(month<10) {
        	monthStr = "0" + month;
        } else {
        	monthStr = "" + month;
        }
        String queryDate = year + "-" + monthStr; 
        List<Vehicle> vList = null;
        List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, true, true);
		if (!orgList.isEmpty()) {
			List<Long> orgIds = new ArrayList<>();
			for (Organization org : orgList) {
				orgIds.add(org.getId());
			}
			
	        if(isEnt) {
	        	vList = vmDao.listVehicleByEnt(orgId,orgIds);
	        } else {
	        	vList = vmDao.listVehicleByDep(orgIds);
	        }
	        
	        if(vList!=null&&vList.size()>0){
	          String vehicleIdListStr = listToString(vList, ',');
			  return vmDao.queryVehicleDataCountByHomePage(queryDate, vehicleIdListStr);
	        }else{
	          return new VehicleDataCountModel();
	        }
		}else{
			return new VehicleDataCountModel();
		}
        
	}
	
	private String listToString(List<Vehicle> list, char separator) {
		StringBuilder sb = new StringBuilder();    
		for (int i = 0; i < list.size(); i++) {        
			sb.append(list.get(i).getId()).append(separator);    
		}    
		return sb.toString().substring(0,sb.toString().length()-1);
	}

	@Override
	public Integer getVehicleMileageStasticByImeiAndBeginTime(Date curTime, String deviceNumber,String vehicleNumber) {
		List<UsageReportSQLModel> retList = vmDao.getVehicleMileageStasticByImeiAndBeginTime(curTime,deviceNumber,vehicleNumber);
		if(retList != null && retList.size() >0){
			UsageReportSQLModel usageReportSQLModel = retList.get(0);
			return usageReportSQLModel.getTotal_mileage();
		}
		return 0;
	}

	public void modifyJobTravelMileageAndUpdateTime(Long travelMileage, Date updateTime, Long vehicleId) {
		vmDao.modifyJobTravelMileageAndUpdateTime(travelMileage,updateTime,vehicleId);
	}
	
	@Override
	public String validateVehicleInfoAndTime(Integer num, Vehicle vehicle, VehicleMaintenance vmNew){
    	StringBuffer msg = new StringBuffer();
    	String numInfo = "第"+num+"条数据:";
    	//判断是否有为空的数据
    	if(StringUtils.isBlank(vmNew.getVehicleNumber())){
    		msg.append(numInfo).append("车牌号为空,未更新!");
    		return msg.toString();
    	}
    	if(StringUtils.isBlank(vmNew.getVehicleIdentification())){
    		msg.append(numInfo).append("车架号为空,未更新!");
    		return msg.toString();
    	}
    	if(StringUtils.isBlank(vmNew.getDeviceNumber())){
    		msg.append(numInfo).append("设备号为空,未更新!");
    		return msg.toString();
    	}
    	
    	// 车牌号是否一致
    	if (!vmNew.getVehicleNumber().equals(vehicle.getVehicleNumber())) {
    		msg.append(numInfo).append("车牌号不正确,未更新!");
    		return msg.toString();
    	}
    	// 车架号是否一致
    	if (!vmNew.getVehicleIdentification().equals(vehicle.getVehicleIdentification())) {
    		msg.append(numInfo).append("车架号不正确,未更新!");
    		return msg.toString();
    	}
    	// 设备号是否一致
    	if (!vmNew.getDeviceNumber().equals(vehicle.getDeviceNumber())) {
    		msg.append(numInfo).append("设备号不正确,未更新!");
    		return msg.toString();
    	}
    	// SIM是否一致
    	if (StringUtils.isNotBlank(vmNew.getSimNumber())
    			&& !vmNew.getSimNumber().equals(vehicle.getSimNumber())) {
    		msg.append(numInfo).append("SIM卡号不正确,未更新!");
    		return msg.toString();
    	}
    	
    	// 本次保养时间check
    	if(StringUtils.isBlank(vmNew.getCurTimeF())){
    		msg.append(numInfo).append("本次保养时间为空,未更新!");
    		return msg.toString();
    	} else {
        	Date date = DateUtils.string2Date(vmNew.getCurTimeF(),DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
        	// 时间格式判断
        	if (null == date) {
        		msg.append(numInfo).append("本次保养时间格式不正确,未更新!");
        		return msg.toString();
        	} else if (vmNew.getGap() < 0) {
        		// 本次保养时间 不能大于 导入时间
        		msg.append(numInfo).append("本次保养时间不能大于当前,未更新!");
        		return msg.toString();
        	}
    	}
    	
		return msg.toString();
	}
	
	@Override
	public String validateData(Integer num, VehicleMaintenance vmOld, VehicleMaintenance vmNew){
    	StringBuffer msg = new StringBuffer();
    	String numInfo = "";
    	if (null != num) {
    		numInfo = "第"+num+"条数据:";
    	}
    	//判断是否有为空的数据
    	if(null == vmNew.getMaintenanceMileage()){
    		msg.append(numInfo).append("保养里程数为空,未更新!");
    		return msg.toString();
    	}
    	if(null == vmNew.getHeaderMaintenanceMileage()){
    		msg.append(numInfo).append("本次保养表头里程数为空,未更新!");
    		return msg.toString();
    	}
    	
    	if (null != vmOld) {
    		// 如果本次保养时间小于已存在记录的保养时间,不更新提示失败
    		if (vmNew.getCurTimeF().compareTo(vmOld.getCurTimeF()) < 0){
    			msg.append(numInfo).append("本次保养时间小于当前记录的保养时间,未更新!");
    			return msg.toString();
    		}
    		// 本次保养表头里程数必须大于上次保养表头里程数
    		if (vmNew.getHeaderMaintenanceMileage() <= vmOld.getHeaderMaintenanceMileage()) {
    			msg.append(numInfo).append("本次保养表头里程数小于上次保养表头里程数,未更新!");
    			return msg.toString();
    		}
    	}
    	
    	//正整数判断
    	if(vmNew.getMaintenanceTime() < 0){
    		msg.append(numInfo).append("维保周期小于等于零,未更新!");
    		return msg.toString();
    	}
    	if(vmNew.getMaintenanceMileage() < 0) {
    		msg.append(numInfo).append("保养里程数小于等于零,未更新!");
    		return msg.toString();
    	}
    	
		return msg.toString();
	}
}
