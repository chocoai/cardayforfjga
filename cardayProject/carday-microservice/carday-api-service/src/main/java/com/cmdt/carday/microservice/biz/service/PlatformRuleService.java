package com.cmdt.carday.microservice.biz.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.entity.Employee;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.RuleAddress;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.RuleData;
import com.cmdt.carrental.common.model.RuleEditData;
import com.cmdt.carrental.common.model.RuleGetOffData;
import com.cmdt.carrental.common.model.RuleGetOnData;
import com.cmdt.carrental.common.model.RuleTimeData;
import com.cmdt.carrental.common.model.RuleTimeDateItemData;
import com.cmdt.carrental.common.model.RuleTimeHolidayItemData;
import com.cmdt.carrental.common.model.RuleTimeWeeklyItemData;
import com.cmdt.carrental.common.model.UserBindingData;
import com.cmdt.carrental.common.model.VehicleRuleData;
import com.cmdt.carrental.common.model.VehicleRuleDisplayModel;
import com.cmdt.carrental.common.model.VehicleRuleGetOnAndOffDisplayModel;
import com.cmdt.carrental.common.model.VehicleRuleSQLModel;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.RuleService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carday.microservice.common.Constants;
import com.cmdt.carday.microservice.model.request.rule.RuleAddressAddDto;
import com.cmdt.carday.microservice.model.request.rule.RuleAddressUpdateDto;
import com.cmdt.carday.microservice.model.request.rule.RuleBdDto;
import com.cmdt.carday.microservice.model.request.rule.RuleDateTimeDto;
import com.cmdt.carday.microservice.model.request.rule.RuleDeleteDto;
import com.cmdt.carday.microservice.model.request.rule.RuleDto;
import com.cmdt.carday.microservice.model.request.rule.RuleHolidayDto;
import com.cmdt.carday.microservice.model.request.rule.RulePageDto;
import com.cmdt.carday.microservice.model.request.rule.RuleWeeklyDto;
import com.cmdt.carday.microservice.model.request.rule.UserUnbindRuleDto;

@Service
public class PlatformRuleService {
	private static final Logger LOG = LoggerFactory.getLogger(PlatformRuleService.class);
	
	@Autowired
    private RuleService ruleService;
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private OrganizationService organizationService;
	
	public PagModel findLocationByLocationName(User loginUser, RulePageDto rulePageDto) {
		String locationName = rulePageDto.getLocationName();
		Long orgId = loginUser.getOrganizationId();
		String currentPage = String.valueOf(rulePageDto.getCurrentPage());
		String numPerPage = String .valueOf(rulePageDto.getNumPerPage());
		return ruleService.findLocationByLocationName(locationName, orgId, currentPage, numPerPage);
	}
	
	public RuleAddress createAddress(User loginUser, RuleAddressAddDto ruleDto) {
		RuleAddress ruleAddress = new RuleAddress();
		ruleAddress.setOrganizationId(loginUser.getOrganizationId());
		ruleAddress.setLocationName(ruleDto.getLocationName());
		ruleAddress.setCity(ruleDto.getCity());
		ruleAddress.setPosition(ruleDto.getPosition());
		ruleAddress.setLatitude(ruleDto.getLatitude());
		ruleAddress.setLongitude(ruleDto.getLongitude());
		ruleAddress.setRadius(ruleDto.getRadius());
		return ruleService.createLocation(ruleAddress);
	}
	
	public Boolean updateAddress(User loginUser, RuleAddressUpdateDto dto) {
        Boolean result = false;
		Long organizationId = loginUser.getOrganizationId();
        RuleAddress ruleAddress = new RuleAddress();
        ruleAddress = convertRuleAddressUpdateDtoToRuleAddress(ruleAddress, organizationId, dto);
        if (null != ruleService.updateLocation(ruleAddress)) {
        	result = true;
        }
		return result;
	}
	
	public void deleteLocation(long id) {
        RuleAddress ruleAddress = ruleService.findLocation(id);
        if (null != ruleAddress) {
        	ruleService.deleteLocation(ruleAddress);
        }
	}
	
	public List<VehicleRuleDisplayModel> getRuleListByOrgId(User loginUser) {
		List<VehicleRuleDisplayModel> ruleList = null;
		if (null!=loginUser && loginUser.isEntAdmin()) {
			Long organizationId = loginUser.getOrganizationId();
			ruleList = ruleService.getRuleListByOrgId(organizationId);
		}
		return ruleList;
	}
	
	public void addRule(User loginUser, RuleDto ruleDto) {
		if (null!=loginUser && loginUser.isEntAdmin()) {
			RuleData ruleData = convertRuleDtoToRuleData(ruleDto);
			Long entId = loginUser.getOrganizationId();
			ruleService.addRule(ruleData, entId);
		}
	}
	
	public void removeRule(User loginUser, Long ruleId) {
		if (null!=loginUser && loginUser.isEntAdmin()) {
			ruleService.removeRule(ruleId);
		}
	}
	
	public RuleEditData findRuleById(User loginUser, Long ruleId) {
		RuleEditData ruleEditData = null;
		if(null!=loginUser && loginUser.isEntAdmin()){
			Long entId = loginUser.getOrganizationId();
			ruleEditData = ruleService.findRuleById(ruleId, entId);
		}
		return ruleEditData;
	}
  
	public void updateRule(User loginUser, RuleDto ruleDto) {
		if(null!=loginUser && loginUser.isEntAdmin()){
			RuleData ruleData = convertRuleDtoToRuleData(ruleDto);
			ruleService.updateRule(ruleData);
		}
	}
	
	public List<VehicleRuleGetOnAndOffDisplayModel> getOnAddressListForAdd(User loginUser) {
		List<VehicleRuleSQLModel> ruleList = null;
		List<VehicleRuleGetOnAndOffDisplayModel> retList = new ArrayList<>();
		if(null!=loginUser && loginUser.isEntAdmin()){
			Long entId = loginUser.getOrganizationId();
			ruleList = ruleService.getVehicleRuleGetOnAndOffAddressByOrgId(entId);
			if(ruleList != null && !ruleList.isEmpty()){
				for(int i = 0 ; i < ruleList.size() ; i ++){
					VehicleRuleSQLModel vehicleRuleSQLModel = ruleList.get(i);
					VehicleRuleGetOnAndOffDisplayModel vehicleRuleGetOnAndOffDisplayModel = new VehicleRuleGetOnAndOffDisplayModel();
					vehicleRuleGetOnAndOffDisplayModel.setName("getOnCheck");
					vehicleRuleGetOnAndOffDisplayModel.setInputValue(vehicleRuleSQLModel.getAddress_id());
					vehicleRuleGetOnAndOffDisplayModel.setBoxLabel(vehicleRuleSQLModel.getAddress_name()+"("+vehicleRuleSQLModel.getAddress_radius()+"km)");
					if(i == 0){
						vehicleRuleGetOnAndOffDisplayModel.setChecked(true);
					}else {
						vehicleRuleGetOnAndOffDisplayModel.setChecked(false);
					}
					retList.add(vehicleRuleGetOnAndOffDisplayModel);
				}
			}
		}
		return retList;
	}
	
	public List<VehicleRuleGetOnAndOffDisplayModel> getOffAddressListForAdd(User loginUser) {
		List<VehicleRuleSQLModel> ruleList = null;
		List<VehicleRuleGetOnAndOffDisplayModel> retList = new ArrayList<>();
		if(null!=loginUser && loginUser.isEntAdmin()){
			Long entId = loginUser.getOrganizationId();
			ruleList = ruleService.getVehicleRuleGetOnAndOffAddressByOrgId(entId);
			if(ruleList != null && !ruleList.isEmpty()){
				for(int i = 0 ; i < ruleList.size() ; i ++){
					VehicleRuleSQLModel vehicleRuleSQLModel = ruleList.get(i);
					VehicleRuleGetOnAndOffDisplayModel vehicleRuleGetOnAndOffDisplayModel = new VehicleRuleGetOnAndOffDisplayModel();
					vehicleRuleGetOnAndOffDisplayModel.setName("getOffCheck");
					vehicleRuleGetOnAndOffDisplayModel.setInputValue(vehicleRuleSQLModel.getAddress_id());
					vehicleRuleGetOnAndOffDisplayModel.setBoxLabel(vehicleRuleSQLModel.getAddress_name()+"("+vehicleRuleSQLModel.getAddress_radius()+"km)");
					if(i == 0){
						vehicleRuleGetOnAndOffDisplayModel.setChecked(true);
					}else {
						vehicleRuleGetOnAndOffDisplayModel.setChecked(false);
					}
					retList.add(vehicleRuleGetOnAndOffDisplayModel);
				}
			}
		}
		return retList;
	}
	
	public List<VehicleRuleGetOnAndOffDisplayModel> getOnAddressListForEdit(User loginUser, Long ruleId) {
		List<VehicleRuleGetOnAndOffDisplayModel> retList = null;
		if(null!=loginUser && loginUser.isEntAdmin()){
			Long orgId = loginUser.getOrganizationId();
			retList = ruleService.getMergedVehicleRuleAddressByOrgIdAndRuleId(orgId, ruleId, 0);
		}
		return retList;
		
	}
	
	public List<VehicleRuleGetOnAndOffDisplayModel> getOffAddressListForEdit(User loginUser, Long ruleId) {
		List<VehicleRuleGetOnAndOffDisplayModel> retList = null;
		if(null!=loginUser && loginUser.isEntAdmin()){
			Long orgId = loginUser.getOrganizationId();
			retList = ruleService.getMergedVehicleRuleAddressByOrgIdAndRuleId(orgId, ruleId, 1);
		}
		return retList;
		
	}
	
	public List<VehicleRuleDisplayModel> ruleBindingList(User loginUser) {
		List<VehicleRuleDisplayModel> retList = null;
		if (null!=loginUser) {
			Long userId = loginUser.getId();
			List<VehicleRuleData> vehicleRuleDataList = ruleService.findRuleListByUserId(userId);
			if(vehicleRuleDataList != null && !vehicleRuleDataList.isEmpty()){
    			List<Long> ids = new ArrayList<>();
    			for(VehicleRuleData vehicleRuleData : vehicleRuleDataList){
    				ids.add(vehicleRuleData.getRuleId());
    			}
    			
    			if(!ids.isEmpty()){
    				retList = ruleService.getRuleListByIds(ids);
    			}
    		}
		}
		return retList;
	}
	
	public List<VehicleRuleDisplayModel> ruleNotBindingList(User loginUser, Long bindRuleUserId) {
		List<VehicleRuleDisplayModel> retList = null;
		if (null != loginUser) {
			Map<Long,Integer> bindingRuleIds = new HashMap<>();//已绑定规则池
    		List<VehicleRuleData> vehicleRuleDataList = ruleService.findRuleListByUserId(bindRuleUserId);//用户已经绑定
    		if(vehicleRuleDataList != null && !vehicleRuleDataList.isEmpty()){
    			for(VehicleRuleData vehicleRuleData : vehicleRuleDataList){
    				bindingRuleIds.put(vehicleRuleData.getRuleId(), 1);
    			}
    		}
    		
    		Long orgId = 0l;
    		//查询改员工对应的企业Id
    		User queryUser = userService.findById(bindRuleUserId);
    		if(queryUser.isEntAdmin()){//企业管理员
    			orgId = queryUser.getOrganizationId();
    		}else{
    			Organization orgOrderUser=organizationService.findOne(queryUser.getOrganizationId());//找到部门管理员对应的企业id
    			if(orgOrderUser.getParentId() == 0){//员工未分配，还在企业节点
    				orgId = queryUser.getOrganizationId();
    			}else{//在部门一级，查询部门的企业id
    				orgId = orgOrderUser.getParentId();
    			}
    		}
    		
    		List<VehicleRuleDisplayModel> filterList = new ArrayList<>();//过滤后的规则
    		List<VehicleRuleDisplayModel> ruleList = ruleService.getRuleListByOrgIdAndParents(orgId);//该企业下所有的规则
    		if(ruleList != null && !ruleList.isEmpty()){
    			if(bindingRuleIds.size() > 0){//需要过滤
    				for(VehicleRuleDisplayModel displayModel : ruleList){
    					if(bindingRuleIds.get(displayModel.getRuleId()) == null){
    						filterList.add(displayModel);
    					}
    				}
    				retList = filterList;
    			}else{//不需要过滤
    				retList = ruleList;
    			}
    		}
		}
		return retList;
	}
	
	public void userUnbindRule(User loginUser, UserUnbindRuleDto dto) {
		Long userId = dto.getUid();
		Long ruleId = dto.getRuleId();
		if (null != loginUser) {
			ruleService.removeBindingRule(userId, ruleId);
		}
	}
	
	public void userBindRule(User loginUser, RuleBdDto ruleBdDto) {
		if (null != loginUser) {
        	UserBindingData userBindingData = new UserBindingData();
        	userBindingData.setUserId(ruleBdDto.getUid());
        	userBindingData.setRuleList(ruleBdDto.getRuleList());
        	ruleService.userBindingRule(userBindingData);
        }
	}
	
	public String balanceCheck(User loginUser) {
		String msg = "";
		if (null != loginUser) {
			Employee employee = userService.findEmployeeByUserId(loginUser.getId());
    		if(employee != null){
    			double monthLimitvalue = employee.getMonthLimitvalue().doubleValue(); 
    			if(!(monthLimitvalue == -1 || monthLimitvalue == -1.0)){// 月累计限制额度(-1:不限额度)
    				Double monthLimitLeft = employee.getMonthLimitLeft();
    				if(monthLimitLeft != null){
    					double monthLimitLeftVal = monthLimitLeft.doubleValue();
    					if(monthLimitLeftVal < 0){
    						msg = "用车剩余额度小于0";
    					}
    				}
    			}
    		}
        }
		return msg;
	}
	
	
    /**
     * OR mapping from RUleDto to RuleData
     *
     * @param ruleDto
     * @return
     */

    private RuleData convertRuleDtoToRuleData(RuleDto ruleDto) {
        LOG.info("Enter RuleApi convertRuleDtoToRuleData");
        RuleData ruleData = new RuleData();
        ruleData.setRuleId(ruleDto.getId());
        ruleData.setRuleName(ruleDto.getName());
        ruleData.setRuleType(ruleDto.getType());
        ruleData.setUseLimit(ruleDto.getUseLimit());
        ruleData.setVehicleType(ruleDto.getVehicleType());
        RuleGetOffData offData = new RuleGetOffData();

        offData.setGetOffType(ruleDto.getGetOffType());
        if (ruleDto.getGetOffData() != null) {
            List<String> offDataList = new ArrayList<>();
            for (String str : ruleDto.getGetOffData()) {
                offDataList.add(str);
            }
            offData.setGetOffdata(offDataList);
        } else {
            offData.setGetOffdata(null);
        }

        ruleData.setGetOffList(offData);

        RuleGetOnData onData = new RuleGetOnData();

        onData.setGetOnType(ruleDto.getGetOnType());
        if (ruleDto.getGetOnData() != null) {
            List<String> onDataList = new ArrayList<>();
            for (String str : ruleDto.getGetOffData()) {
                onDataList.add(str);
            }
            onData.setGetOndata(onDataList);
        } else {
            onData.setGetOndata(null);
        }
        ruleData.setGetOnList(onData);

        RuleTimeData ruleTimeData = new RuleTimeData();
        ruleTimeData.setTimeRangeType(ruleDto.getTimeRangeType());
        ruleTimeData.setDateData(convertRuleTimeDateItemData(ruleDto.getDateTimeDto()));
        ruleTimeData.setHolidayData(convertRuleTimeHolidayItemData(ruleDto.getHolidayDto()));
        ruleTimeData.setWeeklyData(convertRuleTimeWeeklyItemData(ruleDto.getWeeklyDto()));

        ruleData.setTimeList(ruleTimeData);
        return ruleData;
    }
    
    /**
     * OR mapping from RuleWeeklyDto to RUleTimeWeeklyItemData
     *
     * @param weeklyDto
     * @return
     */

    private List<RuleTimeWeeklyItemData> convertRuleTimeWeeklyItemData(List<RuleWeeklyDto> weeklyDto) {
        LOG.info("Enter RuleApi convertRuleTimeWeeklyItemData");
        List<RuleTimeWeeklyItemData> list = null;
        if (weeklyDto != null) {
            list = new ArrayList<>();
            for (RuleWeeklyDto dto : weeklyDto) {
                RuleTimeWeeklyItemData data = new RuleTimeWeeklyItemData();
                data.setEndTime(dto.getEndTime());
                data.setStartTime(dto.getStartTime());
                data.setWeeklyType(dto.getWeeklyType());
                list.add(data);
            }
        }
        return list;
    }

    /**
     * OR mapping from RuleHolidayDto to RUleTimeHolidayItemData
     *
     * @param holidayDto
     * @return
     */

    private List<RuleTimeHolidayItemData> convertRuleTimeHolidayItemData(List<RuleHolidayDto> holidayDto) {
        LOG.info("Enter RuleApi convertRuleTimeHolidayItemData");
        List<RuleTimeHolidayItemData> list = null;
        if (holidayDto != null) {
            list = new ArrayList<>();
            for (RuleHolidayDto dto : holidayDto) {
                RuleTimeHolidayItemData data = new RuleTimeHolidayItemData();
                data.setEndTime(dto.getEndTime());
                data.setHolidayType(dto.getHolidayType());
                data.setStartTime(dto.getStartTime());
                list.add(data);
            }
        }
        return list;
    }

    /**
     * OR mapping from RUleDataTimeDto to RUleTimeDateTimeData
     *
     * @param timeDto
     * @return
     */

    private List<RuleTimeDateItemData> convertRuleTimeDateItemData(List<RuleDateTimeDto> timeDto) {
        LOG.info("Enter RuleApi convertRuleTimeDateItemData");
        List<RuleTimeDateItemData> list = null;
        if (timeDto != null) {
            list = new ArrayList<>();
            for (RuleDateTimeDto dto : timeDto) {
                RuleTimeDateItemData data = new RuleTimeDateItemData();
                data.setStartDay(dto.getStartDay());
                data.setEndDay(dto.getEndDay());
                data.setStartTime(dto.getStartTime());
                data.setEndTime(dto.getEndTime());
                list.add(data);
            }
        }
        return list;
    }

    /**
     * OR mapping from RuleAddressDto to RuleAddress
     *
     * @param ruleAddress
     * @param organizationId
     * @param ruleAddressDto
     * @return
     */

    private RuleAddress convertRuleAddressUpdateDtoToRuleAddress(RuleAddress ruleAddress, Long organizationId, RuleAddressUpdateDto dto) {
        LOG.info("Enter RuleApi convertRuleAddressUpdateDtoToRuleAddress");
        ruleAddress.setId(dto.getId());
        ruleAddress.setLocationName(dto.getLocationName());
        ruleAddress.setCity(dto.getCity());
        ruleAddress.setPosition(dto.getPosition());
        ruleAddress.setLongitude(dto.getLongitude());
        ruleAddress.setLatitude(dto.getLatitude());
        ruleAddress.setRadius(dto.getRadius());
        ruleAddress.setOrganizationId(organizationId);

        return ruleAddress;
    }
}
