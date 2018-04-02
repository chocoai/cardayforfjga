package com.cmdt.carrental.common.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carday.microservice.common.model.Order.enums.VehicleType;
import com.cmdt.carrental.common.dao.RuleDao;
import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.Employee;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.RuleAddress;
import com.cmdt.carrental.common.entity.RuleHoliday;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.Point;
import com.cmdt.carrental.common.model.RuleData;
import com.cmdt.carrental.common.model.RuleEditData;
import com.cmdt.carrental.common.model.RuleGetOffEditData;
import com.cmdt.carrental.common.model.RuleGetOnEditData;
import com.cmdt.carrental.common.model.RuleTimeDateItemEditData;
import com.cmdt.carrental.common.model.RuleTimeEditData;
import com.cmdt.carrental.common.model.RuleTimeHolidayItemEditData;
import com.cmdt.carrental.common.model.RuleTimeWeeklyItemEditData;
import com.cmdt.carrental.common.model.UserBindingData;
import com.cmdt.carrental.common.model.VehicleRuleData;
import com.cmdt.carrental.common.model.VehicleRuleDisplayModel;
import com.cmdt.carrental.common.model.VehicleRuleGetOnAndOffDisplayModel;
import com.cmdt.carrental.common.model.VehicleRuleSQLModel;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.DistanceUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RuleServiceImpl implements RuleService{
	
	private static final Logger LOG = LoggerFactory.getLogger(RuleServiceImpl.class);

	private static final SimpleDateFormat SDF_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("MM.dd");
	private static final SimpleDateFormat SDF_TIME = new SimpleDateFormat("HH:mm");
	private static final SimpleDateFormat SDF_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Autowired
	private RuleDao ruleDao;
	
	@Autowired
	private ShouqiService shouqiService;
	
    // @Autowired
    // private BaiduApi baiduApi;
	
    @Autowired
    private UserService userService;

    @Autowired
	private OrganizationService organizationService;
	@Override
	public RuleAddress createLocation(RuleAddress ruleAddress){
		if(ruleAddress == null)
			return null;
		return ruleDao.createStation(ruleAddress);		
	}
	
	@Override
	public PagModel findLocationByLocationName(String locationName, Long organizationId, String currentPage,String numPerPage){
		return ruleDao.findLocationByLocationName(locationName,organizationId,currentPage,numPerPage);
	}
	
	@Override
	public RuleAddress findLocation(Long locationId){
		if(locationId == null)
			return null;
		return ruleDao.findLocation(locationId);
	}
	
	@Override
	public RuleAddress updateLocation(RuleAddress ruleAddress){
		if(ruleAddress == null)
			return null;
		return ruleDao.updateLocation(ruleAddress);
	}
	
	@Override
	public void deleteLocation(RuleAddress ruleAddress){
		if(ruleAddress == null)
			return;
		ruleDao.deleteStation(ruleAddress);
	}

	@Override
	public List<VehicleRuleDisplayModel> getRuleListByOrgId(Long orgId) {
		List<VehicleRuleDisplayModel> retList = new ArrayList<VehicleRuleDisplayModel>();
		Map<Long, VehicleRuleDisplayModel> retMap = new HashMap<Long, VehicleRuleDisplayModel>();

		//获得规则列表
//		+---------+----------------------+------------+------------+--------------+-------------+
//		| rule_id | rule_name            | usage_type | time_range | vehicle_type | usage_limit |
//		+---------+----------------------+------------+------------+--------------+-------------+
//		|       1 | 普通员工加班用车规则 |          0 |          1 | 0,1,2,3      |           1 |
//		|       2 | 普通员工出差用车规则 |          2 |          1 | 0,1,2,3      |           0 |
//		+---------+----------------------+------------+------------+--------------+-------------+
		
		List<VehicleRuleSQLModel> ruleList = ruleDao.findRuleListByOrgId(orgId);
		
		if(ruleList != null && ruleList.size() > 0){
			List<Long> ruleIds = new ArrayList<Long>();
			
			for(VehicleRuleSQLModel ruleVehicleRuleSQLModel : ruleList){
				Long ruleId = ruleVehicleRuleSQLModel.getRule_id();
				ruleIds.add(ruleId);
				VehicleRuleDisplayModel vehicleRuleDisplayModel = new VehicleRuleDisplayModel();
				//处理规则列表基础数据(用车类型,用车额度)
				populateRuleList(ruleVehicleRuleSQLModel,vehicleRuleDisplayModel);
				retMap.put(ruleId, vehicleRuleDisplayModel);
			}
			
			//获得上下车列表
//			+---------+--------------+-------------+
//			| rule_id | addressname  | action_type |
//			+---------+--------------+-------------+
//			|       2 | 中移德电     |           0 |
//			|       1 | 东方明珠塔   |           0 |
//			|       2 | 中部创意大厦 |           1 |
//			|       1 | 中部创意大厦 |           0 |
//			|       1 | 黄鹤楼       |           0 |
//			|       1 | 火车站       |           0 |
//			|       2 | 武汉站       |           0 |
//			|       2 | 光谷         |           1 |
//			+---------+--------------+-------------+
			List<VehicleRuleSQLModel> addressList = ruleDao.findRuleAddressByRuleIds(ruleIds);
			
			//处理上下车列表
			populateAddressList(retMap,addressList);
			
			//处理时间
			polulateTimeRange(retMap);
			
			List<VehicleRuleSQLModel> userList = ruleDao.findUsersByRuleIds(ruleIds);
			
			//处理员工人数
			populateEmployeeNum(retMap,userList);
		}
		
		if(retMap.size() > 0){
		  List<Map.Entry<Long, VehicleRuleDisplayModel>> list = new ArrayList<Map.Entry<Long, VehicleRuleDisplayModel>>(retMap.entrySet());
	        Collections.sort(list,new Comparator<Map.Entry<Long, VehicleRuleDisplayModel>>() {
	            //降序排序
	            public int compare(Entry<Long, VehicleRuleDisplayModel> o1,
	                    Entry<Long, VehicleRuleDisplayModel> o2) {
	                return o2.getKey().compareTo(o1.getKey());
	            }
	        });
		
	        for(Map.Entry<Long, VehicleRuleDisplayModel> mapping:list){ 
	        	retList.add(mapping.getValue());
	        }
		}
		return retList;
	}

	@Override
	public List<VehicleRuleDisplayModel> getRuleListByOrgIdAndParents(Long orgId) {
		List<VehicleRuleDisplayModel> retList = new ArrayList<VehicleRuleDisplayModel>();
		Map<Long, VehicleRuleDisplayModel> retMap = new HashMap<Long, VehicleRuleDisplayModel>();

		//获得规则列表
//		+---------+----------------------+------------+------------+--------------+-------------+
//		| rule_id | rule_name            | usage_type | time_range | vehicle_type | usage_limit |
//		+---------+----------------------+------------+------------+--------------+-------------+
//		|       1 | 普通员工加班用车规则 |          0 |          1 | 0,1,2,3      |           1 |
//		|       2 | 普通员工出差用车规则 |          2 |          1 | 0,1,2,3      |           0 |
//		+---------+----------------------+------------+------------+--------------+-------------+

		List<Organization> parents = organizationService.findUpOrganizationListByOrgId(orgId);
		List<Long> orgs = new ArrayList<>();
		for (Organization org : parents) {
			orgs.add(org.getId());
		}
		List<VehicleRuleSQLModel> ruleList = ruleDao.findRuleListByOrgIdList(orgs);

		if(ruleList != null && ruleList.size() > 0){
			List<Long> ruleIds = new ArrayList<Long>();

			for(VehicleRuleSQLModel ruleVehicleRuleSQLModel : ruleList){
				Long ruleId = ruleVehicleRuleSQLModel.getRule_id();
				ruleIds.add(ruleId);
				VehicleRuleDisplayModel vehicleRuleDisplayModel = new VehicleRuleDisplayModel();
				//处理规则列表基础数据(用车类型,用车额度)
				populateRuleList(ruleVehicleRuleSQLModel,vehicleRuleDisplayModel);
				retMap.put(ruleId, vehicleRuleDisplayModel);
			}

			//获得上下车列表
//			+---------+--------------+-------------+
//			| rule_id | addressname  | action_type |
//			+---------+--------------+-------------+
//			|       2 | 中移德电     |           0 |
//			|       1 | 东方明珠塔   |           0 |
//			|       2 | 中部创意大厦 |           1 |
//			|       1 | 中部创意大厦 |           0 |
//			|       1 | 黄鹤楼       |           0 |
//			|       1 | 火车站       |           0 |
//			|       2 | 武汉站       |           0 |
//			|       2 | 光谷         |           1 |
//			+---------+--------------+-------------+
			List<VehicleRuleSQLModel> addressList = ruleDao.findRuleAddressByRuleIds(ruleIds);

			//处理上下车列表
			populateAddressList(retMap,addressList);

			//处理时间
			polulateTimeRange(retMap);

			List<VehicleRuleSQLModel> userList = ruleDao.findUsersByRuleIds(ruleIds);

			//处理员工人数
			populateEmployeeNum(retMap,userList);
		}

		if(retMap.size() > 0){
			List<Map.Entry<Long, VehicleRuleDisplayModel>> list = new ArrayList<Map.Entry<Long, VehicleRuleDisplayModel>>(retMap.entrySet());
			Collections.sort(list,new Comparator<Map.Entry<Long, VehicleRuleDisplayModel>>() {
				//降序排序
				public int compare(Entry<Long, VehicleRuleDisplayModel> o1,
								   Entry<Long, VehicleRuleDisplayModel> o2) {
					return o2.getKey().compareTo(o1.getKey());
				}
			});

			for(Map.Entry<Long, VehicleRuleDisplayModel> mapping:list){
				retList.add(mapping.getValue());
			}
		}
		return retList;
	}

	private void populateEmployeeNum(Map<Long, VehicleRuleDisplayModel> retMap, List<VehicleRuleSQLModel> userList) {
		if(userList != null && userList.size() > 0){
			for(VehicleRuleSQLModel vehicleRuleSQLModel : userList){
				long ruleId = vehicleRuleSQLModel.getRule_id();
				VehicleRuleDisplayModel vehicleRuleDisplayModel = retMap.get(ruleId);
				vehicleRuleDisplayModel.setEmployeeNum(vehicleRuleSQLModel.getEmployee_num().intValue()+"人");
			}
		}else{//没有关联员工
			for (VehicleRuleDisplayModel vehicleRuleDisplayModel : retMap.values()) {
				vehicleRuleDisplayModel.setEmployeeNum("0人");
			}
		}
	}

	private void polulateTimeRange(Map<Long, VehicleRuleDisplayModel> retMap) {
		
		for (VehicleRuleDisplayModel vehicleRuleDisplayModel : retMap.values()) {  
			 
			if(vehicleRuleDisplayModel.getTimeRange().intValue() == 0){ //时间范围不限
				vehicleRuleDisplayModel.addTime("不限");
			}else{
				if(vehicleRuleDisplayModel.getTimeRange().intValue() == 1){
					polulateHolidayTimeRange(vehicleRuleDisplayModel);
				}
				if(vehicleRuleDisplayModel.getTimeRange().intValue() == 2){
					polulateWeekTimeRange(vehicleRuleDisplayModel);
				}
				if(vehicleRuleDisplayModel.getTimeRange().intValue() == 3){
					polulateDateTimeRange(vehicleRuleDisplayModel);
				}
			}
		}  
		
	}
	
	private void polulateHolidayTimeRange(VehicleRuleDisplayModel vehicleRuleDisplayModel) {
		List<Long> ruleIds = new ArrayList<Long>();
		ruleIds.add(vehicleRuleDisplayModel.getRuleId());
		List<VehicleRuleSQLModel> holidayList = ruleDao.findRuleHolidayTimeRangeByRuleIds(ruleIds);
//		+---------+--------------+------------+----------+
//		| rule_id | holiday_type | start_time | end_time |
//		+---------+--------------+------------+----------+
//		|       1 |            1 | 00:00      | 15:00    |
//		|       2 |            1 | 00:00      | 15:00    |
//		|       1 |            0 | 00:00      | 23:00    |
//		|       2 |            0 | 00:00      | 23:00    |
//		+---------+--------------+------------+----------+
		if(holidayList != null && holidayList.size() > 0){
			for(VehicleRuleSQLModel vehicleRuleSQLModel : holidayList){
				StringBuffer timeVal = new StringBuffer();
				int holidayType = vehicleRuleSQLModel.getHoliday_type().intValue();
				String startTime = vehicleRuleSQLModel.getStart_time();
				String endTime = vehicleRuleSQLModel.getEnd_time();
				if(holidayType == 0){//法定工作日
					timeVal.append("法定工作日");
				}else{//法定节假日
					timeVal.append("法定节假日");
				}
				
				timeVal.append(startTime).append("-");
				
				if("00:00".equals(endTime)){
					timeVal.append("次日 00:00");
				}else{
					timeVal.append(endTime);
				}
				vehicleRuleDisplayModel.addTime(timeVal.toString());
			}
		}
		
	}
	
	private void polulateWeekTimeRange(VehicleRuleDisplayModel vehicleRuleDisplayModel) {
		List<Long> ruleIds = new ArrayList<Long>();
		ruleIds.add(vehicleRuleDisplayModel.getRuleId());
		List<VehicleRuleSQLModel> weekList = ruleDao.findRuleWeekTimeRangeByRuleIds(ruleIds);
//		+---------+------------+------------+----------+
//		| rule_id | week_index | start_time | end_time |
//		+---------+------------+------------+----------+
//		|       2 |          0 | 00:00      | 00:00    |
//		|       1 |          0 | 00:00      | 00:00    |
//		|       2 |          1 | 00:00      | 00:00    |
//		|       1 |          1 | 00:00      | 00:00    |
//		|       2 |          2 | 00:00      | 00:00    |
//		|       1 |          2 | 00:00      | 00:00    |
//		|       2 |          3 | 00:00      | 00:00    |
//		|       1 |          3 | 00:00      | 00:00    |
//		|       2 |          4 | 00:00      | 00:00    |
//		|       1 |          4 | 00:00      | 00:00    |
//		|       2 |          5 | 00:00      | 00:00    |
//		|       1 |          5 | 00:00      | 00:00    |
//		|       2 |          6 | 00:00      | 00:00    |
//		|       1 |          6 | 00:00      | 00:00    |
//		+---------+------------+------------+----------+
		if(weekList != null && weekList.size() > 0){
			for(VehicleRuleSQLModel vehicleRuleSQLModel : weekList){
				StringBuffer timeVal = new StringBuffer();
				int weekIndex = vehicleRuleSQLModel.getWeek_index().intValue();
				String startTime = vehicleRuleSQLModel.getStart_time();
				String endTime = vehicleRuleSQLModel.getEnd_time();
				
				if(0 == weekIndex){
					timeVal.append("星期一");
				}else if(1 == weekIndex){
					timeVal.append("星期二");
				}else if(2 == weekIndex){
					timeVal.append("星期三");
				}else if(3 == weekIndex){
					timeVal.append("星期四");
				}else if(4 == weekIndex){
					timeVal.append("星期五");
				}else if(5 == weekIndex){
					timeVal.append("星期六");
				}else if(6 == weekIndex){
					timeVal.append("星期日");
				}else{
					timeVal.append("未知");
				}
					
				timeVal.append(startTime).append("-");
				
				if("00:00".equals(endTime)){
					timeVal.append("次日 00:00");
				}else{
					timeVal.append(endTime);
				}
				vehicleRuleDisplayModel.addTime(timeVal.toString());
			}
		}
	  }

	private void polulateDateTimeRange(VehicleRuleDisplayModel vehicleRuleDisplayModel) {
		List<Long> ruleIds = new ArrayList<Long>();
		ruleIds.add(vehicleRuleDisplayModel.getRuleId());
		List<VehicleRuleSQLModel> dateList = ruleDao.findRuleDateTimeRangeByRuleIds(ruleIds);
//		+---------+------------+------------+------------+----------+
//		| rule_id | start_day  | end_day    | start_time | end_time |
//		+---------+------------+------------+------------+----------+
//		|       2 | 2017-03-23 | 2017-05-23 | 00:00      | 00:00    |
//		|       1 | 2017-03-23 | 2017-05-23 | 00:00      | 00:00    |
//		|       2 | 2017-08-23 | 2017-09-23 | 00:00      | 00:00    |
//		|       1 | 2017-08-23 | 2017-09-23 | 00:00      | 00:00    |
//		+---------+------------+------------+------------+----------+
		if(dateList != null && dateList.size() > 0){
			for(VehicleRuleSQLModel vehicleRuleSQLModel : dateList){
				StringBuffer timeVal = new StringBuffer();
				String startDay = vehicleRuleSQLModel.getStart_day();
				String endDay = vehicleRuleSQLModel.getEnd_day();
				String startTime = vehicleRuleSQLModel.getStart_time();
				String endTime = vehicleRuleSQLModel.getEnd_time();
				
			    timeVal.append(startDay).append(" ").append(endDay).append("  ");
				timeVal.append(startTime).append("-");
				
				if("00:00".equals(endTime)){
					timeVal.append("次日 00:00");
				}else{
					timeVal.append(endTime);
				}
				vehicleRuleDisplayModel.addTime(timeVal.toString());
			}
		}
	}





	private void populateRuleList(VehicleRuleSQLModel ruleVehicleRuleSQLModel,
			VehicleRuleDisplayModel vehicleRuleDisplayModel) {
		vehicleRuleDisplayModel.setRuleId(ruleVehicleRuleSQLModel.getRule_id());
		vehicleRuleDisplayModel.setRuleName(ruleVehicleRuleSQLModel.getRule_name());
		
		//时间范围类型(后续处理时间范围类型处理)
		vehicleRuleDisplayModel.setTimeRange(ruleVehicleRuleSQLModel.getTime_range());
		
		//用车类型
		String vehicleTypeList = ruleVehicleRuleSQLModel.getVehicle_type();
		if(StringUtils.isNoneEmpty(vehicleTypeList)){
			String[] vehicleTypeListArr = vehicleTypeList.split(",");
			if(vehicleTypeListArr.length > 0){
				for(String vehicleType : vehicleTypeListArr){
					if("0".equals(vehicleType)){
						vehicleRuleDisplayModel.addVehicleType("经济型");
					}else if("1".equals(vehicleType)){
						vehicleRuleDisplayModel.addVehicleType("舒适型");
					}else if("2".equals(vehicleType)){
						vehicleRuleDisplayModel.addVehicleType("商务型");
					}else if("3".equals(vehicleType)){
						vehicleRuleDisplayModel.addVehicleType("豪华型");
					}
				}
			}
		}
		
		//用车额度
		if(ruleVehicleRuleSQLModel.getUsage_limit() == 0){
			vehicleRuleDisplayModel.setUseLimit("不占用");
		}else{
			vehicleRuleDisplayModel.setUseLimit("占用");
		}
	}

	private void populateAddressList(Map<Long, VehicleRuleDisplayModel> retMap, List<VehicleRuleSQLModel> addressList) {
		if(addressList != null && addressList.size() > 0){
			for(VehicleRuleSQLModel vehicleRuleSQLModel : addressList){
				long ruleId = vehicleRuleSQLModel.getRule_id();
				VehicleRuleDisplayModel vehicleRuleDisplayModel = retMap.get(ruleId);
				int actionType = vehicleRuleSQLModel.getAction_type();
				String addressname = vehicleRuleSQLModel.getAddressname();
				if(actionType == 0){//上车
					vehicleRuleDisplayModel.addGetOn(addressname);
				}else{//下车
					vehicleRuleDisplayModel.addGetOff(addressname);
				}
			}
		}
		
		//上下车位置不限
		for (VehicleRuleDisplayModel vehicleRuleDisplayModel : retMap.values()) {
			if(vehicleRuleDisplayModel.getGetOnList().size() == 0){
				vehicleRuleDisplayModel.addGetOn("不限");
			}
			
			if(vehicleRuleDisplayModel.getGetOffList().size() == 0){
				vehicleRuleDisplayModel.addGetOff("不限");
			}
			
		}
	}

	@Override
	public void removeRule(Long ruleId) {
		ruleDao.removeRule(ruleId);
	}

	@Override
	public List<VehicleRuleSQLModel> getVehicleRuleGetOnAndOffAddressByOrgId(Long OrgId) {
		List<VehicleRuleSQLModel> addressList = ruleDao.getVehicleRuleGetOnAndOffAddress(OrgId);
		return addressList;
	}

	@Override
	public List<VehicleRuleGetOnAndOffDisplayModel> getMergedVehicleRuleAddressByOrgIdAndRuleId(Long orgId, Long ruleId,
			Integer actionType) {
		
		List<VehicleRuleGetOnAndOffDisplayModel> retList = new ArrayList<VehicleRuleGetOnAndOffDisplayModel>();
		
		//根据OrgId获得所有的address
		List<VehicleRuleSQLModel> allAddressList = ruleDao.getVehicleRuleGetOnAndOffAddress(orgId);
		if(allAddressList != null && allAddressList.size() > 0){
			Map<Long,Integer> selectedMap = new HashMap<Long,Integer>();
			//根据actionType与ruleId查询已经选中的上车或下车地址
			List<VehicleRuleSQLModel> selectedAddressList = ruleDao.getAddressRelationListByRuleIdAndActionType(ruleId,actionType);
			if(selectedAddressList != null && selectedAddressList.size() > 0){
				for(VehicleRuleSQLModel checkedVal : selectedAddressList){
					selectedMap.put(checkedVal.getAddress_id(), 1);
				}
			}
			
			int selectedMapSize = selectedMap.size();
			for(VehicleRuleSQLModel nativeVal : allAddressList){
					
					VehicleRuleGetOnAndOffDisplayModel retModel = new VehicleRuleGetOnAndOffDisplayModel();
					if(actionType.intValue() == 0){//上车
						retModel.setName("getOnCheck");
					}else{//下车
						retModel.setName("getOffCheck");
					}
					retModel.setInputValue(nativeVal.getAddress_id());
					retModel.setBoxLabel(nativeVal.getAddress_name()+"("+nativeVal.getAddress_radius()+"km)");
					if(selectedMapSize > 0){//标注checked位置
						if(selectedMap.get(nativeVal.getAddress_id()) != null){
							retModel.setChecked(true);
						}else{
							retModel.setChecked(false);
						}
					}else{
						retModel.setChecked(false);
					}
					retList.add(retModel);
			}
		}
		return retList;
	}

	@Override
	public void addRule(RuleData ruleData,Long orgId) {
		ruleDao.addRule(ruleData,orgId);
	}

	@Override
	public RuleEditData findRuleById(Long ruleId, Long orgId) {
		return ruleDao.findRuleById(ruleId,orgId);
	}

	@Override
	public void updateRule(RuleData ruleData) {
		ruleDao.updateRule(ruleData);
	}

	@Override
	public PagModel getHolidayListListByYear(String year,String currentPage,String numPerPage) {
		return ruleDao.getRuleHolidayListByYear(year,currentPage,numPerPage);
	}

	@Override
	public void createRuleHoliday(RuleHoliday ruleHoliday) {
		ruleDao.createRuleHoliday(ruleHoliday);		
	}

	@Override
	public RuleHoliday findRuleHolidayById(Long id) {
		return ruleDao.findRuleHolidayById(id);
	}
	
	@Override
	public List<RuleHoliday> findRuleHolidayByYear(String year) {
		return ruleDao.findRuleHolidayByYear(year);
	}

	@Override
	public void updateRuleHoliday(RuleHoliday ruleHoliday) {
		ruleDao.updateRuleHoliday(ruleHoliday);
	}

	@Override
	public void removeRuleHoliday(Long id) {
		ruleDao.removeRuleHoliday(id);
	}

	@Override
	public String getOrderValidateResult(BusiOrder order,Long userId) {
		String msg = "";
		List<VehicleRuleData> list = ruleDao.findRuleListByUserId(userId);
		if(list != null && list.size() > 0){
			for(VehicleRuleData vehicleRuleData : list){
				Long ruleId = vehicleRuleData.getRuleId();
				RuleEditData ruleEditData = this.findRuleById(ruleId);
				msg = getValidateResultByEachRule(order,ruleEditData,userId);
				if(StringUtils.isEmpty(msg)){
					break;
				}
			}
		}
		return msg;
	}
	
	public String getValidateResultByEachRule(BusiOrder order,RuleEditData ruleEditData,Long userId){
		String msg = "";
		boolean failureFlag = false;
		try{
			msg = getBalanceValidateResult(order,ruleEditData,userId);//balance验证
			
			if(StringUtils.isEmpty(msg)){
				msg = getVehicleTypeValidateResult(order,ruleEditData);//车辆类型验证
			}else{
				return msg;
			}
			
			if(StringUtils.isEmpty(msg)){
				msg = getAddressValidateResult(order,ruleEditData);//上下车位置验证
			}else{
				return msg;
			}
			
			if(StringUtils.isEmpty(msg)){
				msg = getTimeValidateResult(order,ruleEditData);//时间范围验证
			}
		}catch(Exception e){
			failureFlag = true;
			LOG.error("rule validate error",e);
		}finally {
			if(failureFlag){
				msg = "规则验证流程出错,请稍后重试";
			}
		}
		return msg;
	}

	/**
	 * 时间范围验证
	 * @param order
	 * @return
	 */
	private String getTimeValidateResult(BusiOrder order,RuleEditData ruleEditData) {
		String msg = "";
		RuleTimeEditData ruleTimeEditData = ruleEditData.getTimeList();
		if(ruleTimeEditData != null){
			if("1".equals(ruleTimeEditData.getTimeRangeType())){
				msg = getHolidayTimeValidateResult(order,ruleTimeEditData.getHolidayData());
			}else if("2".equals(ruleTimeEditData.getTimeRangeType())){
				msg = getWeeklyTimeValidateResult(order,ruleTimeEditData.getWeeklyData());
			}else if("3".equals(ruleTimeEditData.getTimeRangeType())){
				msg = getDateTimeValidateResult(order,ruleTimeEditData.getDateData());
			}
		}else{
			msg = "规则流程查找时间范围出错";
		}
		return msg;
	}
	
	/**
	 * 按法定工作日/节假日验证
	 * @param order
	 * @param holidayData
	 * @throws Exception 
	 */
	private String getHolidayTimeValidateResult(BusiOrder order, List<RuleTimeHolidayItemEditData> holidayData) {
		LOG.debug("RuleService.getHolidayTimeValidateResult["+order+","+holidayData+"]");
		String respMsg = "";
		//get order time
		Date startDate = order.getPlanStTime();
		Date endDate = order.getPlanEdTime();
		try{
			//check whether data is valid
			if(startDate.compareTo(endDate)<=0
					&& holidayData != null && !holidayData.isEmpty()){
				//only check startDate
//				while(startDate.compareTo(endDate)<=0){
					boolean flag = false;
					//fetch years
					Calendar cal = Calendar.getInstance();
					cal.setTime(startDate);
					int year = cal.get(Calendar.YEAR);
					int startWeekType = DateUtils.getWeekOfDateForCarDay(startDate);
					//fetch holiday
					List<RuleHoliday> yearHolidays = findRuleHolidayByYear(String.valueOf(year));
					if(yearHolidays != null && !yearHolidays.isEmpty()){
						//check startdate must before enddate
						String startYearHolidayStr = "";
						String startYearAdjustStr = "";
						
						for(RuleHoliday yearHoliday : yearHolidays){
							String tmpHoliday = yearHoliday.getHolidayTime()==null?"":yearHoliday.getHolidayTime();
							String tmpAdjust = yearHoliday.getAdjustHolidayTime()==null?"":yearHoliday.getAdjustHolidayTime();
							startYearHolidayStr = startYearHolidayStr.concat(tmpHoliday).concat(",");
							startYearAdjustStr = startYearAdjustStr.concat(tmpAdjust).concat(",");
						}

						//set date and time string
						String dateStr = SDF_DATE.format(startDate);
						String timeStr = SDF_TIME.format(startDate);
						//check endtime logic
//							String endDStr = SDF_DATE.format(endDate);
//							String endTStr = SDF_TIME.format(endDate);
						//check week number
						
//						LOG.info("getHolidayTimeValidateResult:dateStr-"+dateStr);
//						LOG.info("getHolidayTimeValidateResult:timeStr-"+timeStr);
//						LOG.info("getHolidayTimeValidateResult:startWeekType-"+startWeekType);
//						LOG.info("getHolidayTimeValidateResult:startYearAdjustStr-"+startYearAdjustStr);
//						LOG.info("getHolidayTimeValidateResult:startYearHolidayStr-"+startYearHolidayStr);
//						LOG.info("getHolidayTimeValidateResult:check-"+((startWeekType>=5 && startYearAdjustStr.indexOf(dateStr) < 0) || (startYearHolidayStr != null && startYearHolidayStr.indexOf(dateStr) >= 0)));
						
						//check holiday validation
						//工作日节假日类型 0:法定工作日  1:法定节假日
						if(((startWeekType>=5 && startYearAdjustStr != null && !startYearAdjustStr.contains(dateStr)) || (startYearHolidayStr != null && startYearHolidayStr.contains(dateStr)))){
							for(RuleTimeHolidayItemEditData ruleData : holidayData){
								if("1".equals(ruleData.getHolidayType())){
									String starttime = ruleData.getStartTime();
									String endtime = ruleData.getEndTime();	
									
//									LOG.info("1getHolidayTimeValidateResult:starttime-"+starttime);
//									LOG.info("1getHolidayTimeValidateResult:endtime-"+endtime);
//									LOG.info("1getHolidayTimeValidateResult:checktime-"+(timeStr.compareTo(starttime) >= 0 && timeStr.compareTo(endtime) <= 0));
									
									if((timeStr.compareTo(starttime) >= 0 && (timeStr.compareTo(endtime) <= 0 || "00:00".equals(endtime)))){
										flag = true;
										break;
									}
								}
							}
						}
						//all working days
						else if(((startWeekType<=4 && startYearHolidayStr != null && !startYearHolidayStr.contains(dateStr)) || (startYearAdjustStr != null && startYearAdjustStr.contains(dateStr)))){
							for(RuleTimeHolidayItemEditData ruleData : holidayData){
								if("0".equals(ruleData.getHolidayType())){
									String starttime = ruleData.getStartTime();
									String endtime = ruleData.getEndTime();
									
//									LOG.info("0getHolidayTimeValidateResult:starttime-"+starttime);
//									LOG.info("0getHolidayTimeValidateResult:endtime-"+endtime);
//									LOG.info("0getHolidayTimeValidateResult:checktime-"+(timeStr.compareTo(starttime) >= 0 && timeStr.compareTo(endtime) <= 0));
									
									if((timeStr.compareTo(starttime) >= 0 && (timeStr.compareTo(endtime) <= 0 || "00:00".equals(endtime)))){
										flag = true;
										break;
									}
								}
							}
						}
					
						
					}
					
//					LOG.info("getHolidayTimeValidateResult:flag-"+flag);
					
					if(!flag){
						String datetimeStr = SDF_DATETIME.format(startDate);
						respMsg = datetimeStr + "超出规定用车时间！";
					}
					
//					if(startDate.compareTo(endDate)==0){
//						return respMsg;
//					}
					
//					startDate = getNextDay(startDate);
//					if(startDate.compareTo(endDate)>0){
//						startDate = endDate;
//					}
//				}
			}
		}catch(Exception e){
			respMsg = "用车假日校验失败！";
			LOG.error(respMsg,e);;
		}
		
		return respMsg;
	}
	
	/**
	 * move to next day
	 */
//	private Date getNextDay(Date dt) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(dt);
//        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
//        cal.set(Calendar.HOUR, 0);
//        cal.set(Calendar.MINUTE, 0);
//        cal.set(Calendar.SECOND, 0);
//        return cal.getTime();
//    }
//	public static List<RuleHoliday> findRuleHoliday(String year) {
//		List<RuleHoliday> yearHolidays = new ArrayList<RuleHoliday>();
//		if("2017".equals(year)){
//			RuleHoliday d = new RuleHoliday();
//			d.setHolidayTime("04.02,04.03");
//			d.setAdjustHolidayTime("04.01");
//			
//			RuleHoliday d2 = new RuleHoliday();
//			d2.setHolidayTime("01.02,01.03");
//			yearHolidays.add(d);
//			yearHolidays.add(d2);
//		}
//		return yearHolidays;
//	}
//	public static void main(String[] args){
//		BusiOrder order = new BusiOrder();
//		List<RuleTimeHolidayItemEditData> holidayData = new ArrayList<RuleTimeHolidayItemEditData>();
//		RuleTimeHolidayItemEditData d1 = new RuleTimeHolidayItemEditData();
//		
//		d1.setHolidayType("0");
//		d1.setStartTime("09:00");
//		d1.setEndTime("00:00");
//		
//		holidayData.add(d1);
//		
//		Calendar c = Calendar.getInstance();
//		c.set(2017, 3, 10, 8, 59, 01);
//		Date startDate = c.getTime();
//		
//		c.set(2017, 3, 10, 14, 00, 23);
//		Date endDate = c.getTime();
//		
//		order.setPlanStTime(startDate);
//		order.setPlanEdTime(endDate);
//		
//		String resMsg = getHolidayTimeValidateResult(order,holidayData);
//		System.out.println("resp:"+resMsg);
//	}
	
	
	/**
	 * 按星期定义验证
	 * @param order
	 * @param weeklyData
	 */
	private String getWeeklyTimeValidateResult(BusiOrder order, List<RuleTimeWeeklyItemEditData> weeklyData) {
//		String msg="按星期定义规则不符";
//		int weekType=DateUtils.getWeekOfDateForCarDay(order.getPlanStTime());
//		String stTime=order.getPlanStTimeF().substring(11);
//		String edTime=order.getPlanEdTimeF().substring(11);
//		if(!weeklyData.isEmpty()){
//			for(RuleTimeWeeklyItemEditData data:weeklyData){
//				if(data.getWeeklyType().equals(weekType+"")){
//					if(stTime.compareTo(data.getStartTime())>=0 && edTime.compareTo(data.getEndTime())<=0){
//						msg="";
//						break;
//					}
//				}
//			}
//		}else{
//			msg="";
//		}
//		return msg;
		
		String msg = "";
		Date orderStartDate = order.getPlanStTime();
		int startWeekType = DateUtils.getWeekOfDateForCarDay(orderStartDate);
		
		Map<Integer,RuleTimeWeeklyItemEditData> weekTypePool = new HashMap<Integer,RuleTimeWeeklyItemEditData>();
		if(weeklyData != null && weeklyData.size() > 0){
			for(RuleTimeWeeklyItemEditData ruleTimeWeeklyItemEditData : weeklyData){
				weekTypePool.put(Integer.valueOf(ruleTimeWeeklyItemEditData.getWeeklyType()), ruleTimeWeeklyItemEditData);
			}
		}
		
		if(weekTypePool.size() > 0){
			RuleTimeWeeklyItemEditData ruleTimeWeeklyItemEditData = weekTypePool.get(startWeekType);
			if(ruleTimeWeeklyItemEditData == null){//没有匹配星期
				msg = "订单所选星期与规则不匹配";
				return msg;
			}else{
				String ruleStartHHSS = ruleTimeWeeklyItemEditData.getStartTime();
				String ruleEndHHSS = ruleTimeWeeklyItemEditData.getEndTime();
				if("00:00".equals(ruleEndHHSS)){
					ruleEndHHSS = "24:00";//如果是00:00,转义为24:00
				}
				
				String orderStartHHSS = SDF_TIME.format(orderStartDate);
				
				if(orderStartHHSS.compareTo(ruleStartHHSS) >= 0 && orderStartHHSS.compareTo(ruleEndHHSS) <= 0){ //订单开始HHSS<规则HHSS
					msg = "";
					return msg;
				}else{
					msg = "订单所选星期与规则不匹配";
					return msg;
				}
			}
		}else{
			return "";
		}
	}
	
	/**
	 * 按日期定义验证
	 * @param order
	 * @param dateData
	 */
	private String getDateTimeValidateResult(BusiOrder order, List<RuleTimeDateItemEditData> dateData) {
//		String msg="按日期定义规则不符";
//		String startTime=order.getPlanStTimeF();
//		String endTime=order.getPlanEdTimeF();
//		if(!dateData.isEmpty()){
//			for(RuleTimeDateItemEditData data:dateData){
//				if(startTime.compareTo(data.getStartDay()+" "+data.getStartTime())>=0 && endTime.compareTo(data.getEndDay()+" "+data.getEndTime())<=0){
//					msg="";
//					break;
//				}
//			}
//		}else{
//			msg="";
//		}
//		return msg;
		
		String msg = "";
		Date orderStartDate = order.getPlanStTime();
		Date orderEndDate = order.getPlanEdTime();
		
		for(RuleTimeDateItemEditData ruleTimeDateItemEditData : dateData){
			msg = getDateTimeValidateResultByEachDate(orderStartDate,orderEndDate,ruleTimeDateItemEditData);
			if(StringUtils.isEmpty(msg)){
				break;
			}
		}
		return msg;
	}
	
    private String getDateTimeValidateResultByEachDate(Date orderStartDateVal,Date orderEndDateVal,RuleTimeDateItemEditData ruleTimeDateItemEditData) {
    	String msg = "";
    	//订单
    	String orderStartDay = SDF_YYYY_MM_DD.format(orderStartDateVal);
    	String orderStartTime = SDF_TIME.format(orderStartDateVal);
    	
    	//规则
		String ruleStartDay = ruleTimeDateItemEditData.getStartDay();
		String ruleStartTime = ruleTimeDateItemEditData.getStartTime();
		String ruleEndDay = ruleTimeDateItemEditData.getEndDay();
		String ruleEndTime = ruleTimeDateItemEditData.getEndTime();
		
		if(ruleStartDay.equals(ruleEndDay)){//规则是一天
			if(orderStartDay.equals(ruleStartDay)){//满足规则的day
				
				if("00:00".equals(ruleEndTime)){//如果结束时间为00:00,将结束HHSS设置为24:00
					ruleEndTime = "24:00";
				}
				
				if(orderStartTime.compareTo(ruleStartTime) >= 0 && orderStartTime.compareTo(ruleEndTime) <= 0){
					return "";
				}else{
					msg = "订单所选日期与规则不匹配";
					return msg;
				}
			}else{
				msg = "订单所选日期与规则不匹配";
				return msg;
			}
		}else{//规则是日期区间，也就是说每天的开始HHSS和结束HHSS是一样的
			if(orderStartDay.compareTo(ruleStartDay)>=0 && orderStartDay.compareTo(ruleEndDay)<=0){
				
				if("00:00".equals(ruleEndTime)){//如果结束时间为00:00,将结束HHSS设置为24:00
					ruleEndTime = "24:00";
				}
				
				if(orderStartTime.compareTo(ruleStartTime) >= 0 && orderStartTime.compareTo(ruleEndTime) <= 0){
					return "";
				}else{
					msg = "订单所选日期与规则不匹配";
					return msg;
				}
			}else{
				msg = "订单所选日期与规则不匹配";
				return msg;
			}
		}
	}

	/**
	 * 上下车位置验证
	 * @param order
	 * @return
	 */
	private String getAddressValidateResult(BusiOrder order,RuleEditData ruleEditData) {
		String msg = "";
		Double fromLat = order.getFromLat();
		Double fromLng = order.getFromLng();
		
		Double toLat = order.getToLat();
		Double toLng = order.getToLng();
		
		//上车位置验证
		msg = getGetOnAddressValidateResult(fromLat,fromLng,ruleEditData.getGetOnList());
		if(!StringUtils.isEmpty(msg)){
			return msg;
		}else{
			//下车位置验证
			msg = getGetOffAddressValidateResult(toLat,toLng,ruleEditData.getGetOffList());
		}
		return msg;
	}

	/**
	 * 上车位置验证
	 * @param getOnList
	 * @return
	 */
	private String getGetOnAddressValidateResult(Double lat, Double lnt,RuleGetOnEditData getOnList) {
		String msg = "";
		if(!"0".equals(getOnList.getGetOnType())){
			List<Long> getOndata = getOnList.getGetOndata();
			List<RuleAddress> ruleAddressList = findLocations(getOndata);
			msg = getAddressValidateResult(lat,lnt,ruleAddressList,"getOn");
		}
		return msg;
	}
	
	/**
	 * 下车位置验证
	 * @return
	 */
	private String getGetOffAddressValidateResult(Double lat, Double lnt, RuleGetOffEditData getOffList) {
		String msg = "";
		if(!"0".equals(getOffList.getGetOffType())){
			List<Long> getOffdata = getOffList.getGetOffdata();
			List<RuleAddress> ruleAddressList = findLocations(getOffdata);
			msg = getAddressValidateResult(lat,lnt,ruleAddressList,"getOff");
		}
		return msg;
	}

	/**
	 * 订单开始位置与规则地点的longitude,latitude,radius做比较
	 * @param ruleAddressList
	 * @return
	 */
	private String getAddressValidateResult(Double lat, Double lnt, List<RuleAddress> ruleAddressList,String addressType) {
		String msg = "";
		Point userPoint = new Point(lnt,lat);
		for(RuleAddress ruleAddress : ruleAddressList){
			Point rulePoint = new Point(Double.valueOf(ruleAddress.getLongitude()),Double.valueOf(ruleAddress.getLatitude()));//规则点
			//判断用户输入的地址对应的百度经纬度Point与rule Point的距离是否在对应的半径内
			if(DistanceUtil.getDistance(userPoint, rulePoint) <= new Double(ruleAddress.getRadius())){
				msg = "";
				return msg;
			}else{
				if("getOn".equals(addressType)){
					msg = "出发地位置超出规则范围";
				}else{
					msg = "目的地位置超出规则范围";
				}
			}
		}
		return msg;
	}
	
	public Point getPointByAddress(String address){
		Point point = null;
		Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.REVERSEADDRESS, new Object[]{address});
		if (result != null && result.get("status").equals("success") && result.get("data") != null && StringUtils.isNotEmpty(result.get("data").toString())) {
			JsonNode jsonNode;
			try {
				jsonNode = MAPPER.readTree(result.get("data").toString());
				if ("000".equals(jsonNode.get("status").asText())) {
					JsonNode resNd = jsonNode.get("result");
					if(resNd != null){
						if("0".equals(resNd.get("status").asText())){
							JsonNode resuNd = resNd.get("result");
							if(resuNd != null){
								JsonNode locNd = resuNd.get("location");
								if(locNd != null){
									point = new Point();
									double lng = locNd.get("lng").asDouble();
									double lat = locNd.get("lat").asDouble();
									point = new Point(lng,lat);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return point;
	}

	/**
	 * 车辆类型验证
	 * @param order
	 * @return
	 */
	private String getVehicleTypeValidateResult(BusiOrder order,RuleEditData ruleEditData) {
		String msg = "";
		
		String vehicleType = ruleEditData.getVehicleType();
		Map<String, Integer> vehicleTypePool = new HashMap<String, Integer>();
		
		if(!StringUtils.isEmpty(vehicleType)){
			
			String[] vehicleTypeArr = vehicleType.split(",");
			for(String vehicleTypeVal : vehicleTypeArr){
				vehicleTypePool.put(vehicleTypeVal, 1);
			}
			
			String vehicleTypeFromOrder = order.getVehicleType();//订单选项中的车辆类型
			
			if(!StringUtils.isEmpty(vehicleTypeFromOrder)){
				if(vehicleTypePool.get(vehicleTypeFromOrder) == null){
					msg = "用车类型不匹配";
				}
			}else{
				msg = "规则验证流程失败,用户没有选择用车类型";
			}
		}else{
			msg = "规则验证流程失败,用车类型规则没有找到";
		}
		
		return msg;
	}

	/**
	 * balance验证
	 * @param order
	 * @return
	 */
	private String getBalanceValidateResult(BusiOrder order,RuleEditData ruleEditData,Long userId) {
		String msg = "";
		Employee employee = userService.findEmployeeByUserId(userId);
		if(employee != null){
			double MonthLimitvalue = employee.getMonthLimitvalue().doubleValue(); 
			if(!(MonthLimitvalue == -1 || MonthLimitvalue == -1.0)){// 月累计限制额度(-1:不限额度)
				Double monthLimitLeft = employee.getMonthLimitLeft();
				if(monthLimitLeft != null){
					double monthLimitLeftVal = monthLimitLeft.doubleValue();
					if(monthLimitLeftVal < 0){
						msg = "用车剩余额度小于0";
					}
				}
			}
		}
		return msg;
	}

	@Override
	public RuleEditData findRuleById(Long ruleId) {
		return ruleDao.findRuleById(ruleId);
	}
	
	@Override
	public List<VehicleRuleDisplayModel> getRuleListByIds(List<Long> ids) {
		List<VehicleRuleDisplayModel> retList = new ArrayList<VehicleRuleDisplayModel>();
		Map<Long, VehicleRuleDisplayModel> retMap = new HashMap<Long, VehicleRuleDisplayModel>();

		//获得规则列表
//		+---------+----------------------+------------+------------+--------------+-------------+
//		| rule_id | rule_name            | usage_type | time_range | vehicle_type | usage_limit |
//		+---------+----------------------+------------+------------+--------------+-------------+
//		|       1 | 普通员工加班用车规则 |          0 |          1 | 0,1,2,3      |           1 |
//		|       2 | 普通员工出差用车规则 |          2 |          1 | 0,1,2,3      |           0 |
//		+---------+----------------------+------------+------------+--------------+-------------+
		
		List<VehicleRuleSQLModel> ruleList = ruleDao.findRuleListByIds(ids);
		
		if(ruleList != null && ruleList.size() > 0){
			List<Long> ruleIds = new ArrayList<Long>();
			
			for(VehicleRuleSQLModel ruleVehicleRuleSQLModel : ruleList){
				Long ruleId = ruleVehicleRuleSQLModel.getRule_id();
				ruleIds.add(ruleId);
				VehicleRuleDisplayModel vehicleRuleDisplayModel = new VehicleRuleDisplayModel();
				//处理规则列表基础数据(用车类型,用车额度)
				populateRuleList(ruleVehicleRuleSQLModel,vehicleRuleDisplayModel);
				retMap.put(ruleId, vehicleRuleDisplayModel);
			}
			
			//获得上下车列表
//			+---------+--------------+-------------+
//			| rule_id | addressname  | action_type |
//			+---------+--------------+-------------+
//			|       2 | 中移德电     |           0 |
//			|       1 | 东方明珠塔   |           0 |
//			|       2 | 中部创意大厦 |           1 |
//			|       1 | 中部创意大厦 |           0 |
//			|       1 | 黄鹤楼       |           0 |
//			|       1 | 火车站       |           0 |
//			|       2 | 武汉站       |           0 |
//			|       2 | 光谷         |           1 |
//			+---------+--------------+-------------+
			List<VehicleRuleSQLModel> addressList = ruleDao.findRuleAddressByRuleIds(ruleIds);
			
			//处理上下车列表
			populateAddressList(retMap,addressList);
			
			//处理时间
			polulateTimeRange(retMap);
			
			List<VehicleRuleSQLModel> userList = ruleDao.findUsersByRuleIds(ruleIds);
			
			//处理员工人数
			populateEmployeeNum(retMap,userList);
		}
		
		if(retMap.size() > 0){
		  List<Map.Entry<Long, VehicleRuleDisplayModel>> list = new ArrayList<Map.Entry<Long, VehicleRuleDisplayModel>>(retMap.entrySet());
	        Collections.sort(list,new Comparator<Map.Entry<Long, VehicleRuleDisplayModel>>() {
	            //降序排序
	            public int compare(Entry<Long, VehicleRuleDisplayModel> o1,
	                    Entry<Long, VehicleRuleDisplayModel> o2) {
	                return o2.getKey().compareTo(o1.getKey());
	            }
	        });
		
	        for(Map.Entry<Long, VehicleRuleDisplayModel> mapping:list){ 
	        	retList.add(mapping.getValue());
	        }
		}
		return retList;
	}

	@Override
	public List<VehicleRuleData> findRuleListByUserId(Long userId) {
		return ruleDao.findRuleListByUserId(userId);
	}

	@Override
	public List<VehicleRuleSQLModel> findRuleListByIds(List<Long> ids) {
		return ruleDao.findRuleListByIds(ids);
	}

	@Override
	public void removeBindingRule(Long userId,Long ruleId) {
		ruleDao.removeBindingRule(userId,ruleId);
	}

	@Override
	public void userBindingRule(UserBindingData userBindingData) {
		ruleDao.userBindingRule(userBindingData);
	}

	/**
	 * 查找用户可以使用的车辆列表，只查找 time_range = 0 表示 时间不限的 规则
	 * @param userId
	 * @return
	 */
	@Override
	public List<VehicleType> findRuleVehicleTypeByUserId(Long userId) {
		return ruleDao.findRuleVehicleTypeByUserId(userId);
	}

	@Override
	public List<RuleAddress> findLocations(List<Long> locationIds) {
		return ruleDao.findLocations(locationIds);
	}
}
