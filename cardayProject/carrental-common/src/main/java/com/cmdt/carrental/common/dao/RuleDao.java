package com.cmdt.carrental.common.dao;

import java.util.List;

import com.cmdt.carday.microservice.common.model.Order.enums.VehicleType;
import com.cmdt.carrental.common.entity.RuleAddress;
import com.cmdt.carrental.common.entity.RuleHoliday;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.RuleData;
import com.cmdt.carrental.common.model.RuleEditData;
import com.cmdt.carrental.common.model.UserBindingData;
import com.cmdt.carrental.common.model.VehicleRuleData;
import com.cmdt.carrental.common.model.VehicleRuleSQLModel;

public interface RuleDao {

	public RuleAddress createStation(RuleAddress ruleAddress);

	public PagModel findLocationByLocationName(String locationName, Long organizationId, String currentPage,String numPerPage);

	public RuleAddress updateLocation(RuleAddress ruleAddress);

	public RuleAddress findLocation(Long locationId);

	public void deleteStation(RuleAddress ruleAddress);
	
	/**
	 * 根据企业id查询规则列表
	 * @param orgId
	 * @return
	 */
	public List<VehicleRuleSQLModel> findRuleListByOrgId(Long orgId);

	/**
	 * 根据企业id查询自身及其祖先节点的用车规则
	 * @param orgIds
	 * @return
	 */
	List<VehicleRuleSQLModel> findRuleListByOrgIdList(List<Long> orgIds);
	/**
	 * 根据规则id查询上下车列表
	 * @param ruleIds
	 * @return
	 */
	public List<VehicleRuleSQLModel> findRuleAddressByRuleIds(List<Long> ruleIds);
	
	
	/**
	 * 根据规则id查询法定工作日/节假日
	 * @param ruleIds
	 * @return
	 */
	public List<VehicleRuleSQLModel> findRuleHolidayTimeRangeByRuleIds(List<Long> ruleIds);
	
	/**
	 * 根据规则id查询星期
	 * @param ruleIds
	 * @return
	 */
	public List<VehicleRuleSQLModel> findRuleWeekTimeRangeByRuleIds(List<Long> ruleIds);
	
	/**
	 * 根据规则id查询日期
	 * @param ruleIds
	 * @return
	 */
	public List<VehicleRuleSQLModel> findRuleDateTimeRangeByRuleIds(List<Long> ruleIds);

	/**
	 * 根据规则id删除规则
	 * @param orgId
	 * @return
	 */
	public void removeRule(Long ruleId);

	/**
	 * 根据规则id查询关联人数
	 * @param ruleIds
	 * @return
	 */
	public List<VehicleRuleSQLModel> findUsersByRuleIds(List<Long> ruleIds);

	
	/**
	 * 根据orgId查询该组织的上下车所有位置信息
	 * @param entId
	 * @return
	 */
	public List<VehicleRuleSQLModel> getVehicleRuleGetOnAndOffAddress(Long OrgId);

	/**
	 * 根据ruleId与actionType查询已经选中的地址id
	 * @param ruleId
	 * @param actionType
	 * @return
	 */
	public List<VehicleRuleSQLModel> getAddressRelationListByRuleIdAndActionType(Long ruleId, Integer actionType);

	public void addRule(RuleData ruleData,Long orgId);

	/**
	 * 根据rule id与org id查询规则
	 * @param ruleId
	 * @param orgId
	 * @return
	 */
	public RuleEditData findRuleById(Long ruleId, Long orgId);
	
	/**
	 * 根据rule id查询规则
	 * @param ruleId
	 * @param orgId
	 * @return
	 */
	public RuleEditData findRuleById(Long ruleId);

	/**
	 * 更新rule
	 * @param ruleData
	 */
	public void updateRule(RuleData ruleData);
	
	public PagModel getRuleHolidayListByYear(String  year,String currentPage,String numPerPage);
	
	public void createRuleHoliday(RuleHoliday ruleHoliday);
	
	public RuleHoliday findRuleHolidayById(Long id);
	
	public List<RuleHoliday> findRuleHolidayByYear(String year);
	
	public void updateRuleHoliday(RuleHoliday ruleHoliday);
	
	public void removeRuleHoliday(Long id);

	public List<VehicleRuleData> findRuleListByUserId(Long userId);
	
	public List<VehicleRuleSQLModel> findRuleListByIds(List<Long> ids);

	public void removeBindingRule(Long userId,Long ruleId);

	public void userBindingRule(UserBindingData userBindingData);

	public List<RuleAddress> findLocations(List<Long> locationIds);

    List<VehicleType> findRuleVehicleTypeByUserId(Long userId);
}
