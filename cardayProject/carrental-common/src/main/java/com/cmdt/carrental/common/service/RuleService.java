package com.cmdt.carrental.common.service;

import java.util.List;

import com.cmdt.carday.microservice.common.model.Order.enums.VehicleType;
import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.RuleAddress;
import com.cmdt.carrental.common.entity.RuleHoliday;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.RuleData;
import com.cmdt.carrental.common.model.RuleEditData;
import com.cmdt.carrental.common.model.UserBindingData;
import com.cmdt.carrental.common.model.VehicleRuleData;
import com.cmdt.carrental.common.model.VehicleRuleDisplayModel;
import com.cmdt.carrental.common.model.VehicleRuleGetOnAndOffDisplayModel;
import com.cmdt.carrental.common.model.VehicleRuleSQLModel;

public interface RuleService {

	public RuleAddress createLocation(RuleAddress ruleAddress);

	public PagModel findLocationByLocationName(String locationName, Long organizationId, String currentPage,String numPerPage);

	public RuleAddress findLocation(Long locationId);
	
	public List<RuleAddress> findLocations(List<Long> locationIds);

	public RuleAddress updateLocation(RuleAddress ruleAddress);

	public void deleteLocation(RuleAddress ruleAddress);

	public List<VehicleRuleDisplayModel> getRuleListByOrgId(Long orgId);

	/**
	 * 找到org及其祖先部门的所有用车规则
	 * @param orgId
	 * @return
	 */
	List<VehicleRuleDisplayModel> getRuleListByOrgIdAndParents(Long orgId);

	public void removeRule(Long ruleId);

	/**
	 * 根据orgId查询所有的位置
	 * @param orgId
	 * @return
	 */
	public List<VehicleRuleSQLModel> getVehicleRuleGetOnAndOffAddressByOrgId(Long orgId);
	
	
	/**
	 * 根据orgId以及ruleId,actionType(上车位置或下车位置) 来merge位置,已经选择的位置设置为Checked=true,没有选择的位置设置为Check=false
	 * @param orgId
	 * @return
	 */
	public List<VehicleRuleGetOnAndOffDisplayModel> getMergedVehicleRuleAddressByOrgIdAndRuleId(Long orgId,Long ruleId,Integer actionType);

	/**
	 * 增加规则
	 * @param ruleData
	 */
	public void addRule(RuleData ruleData,Long orgId);

	/**
	 * 根据id与orgId查询规则
	 * @param ruleId
	 * @param orgId
	 * @return
	 */
	public RuleEditData findRuleById(Long ruleId, Long orgId);
	
	/**
	 * 根据id查询规则
	 * @param ruleId
	 * @param orgId
	 * @return
	 */
	public RuleEditData findRuleById(Long ruleId);

	/**
	 * 更新规则
	 * @param ruleData
	 * @return
	 */
	public void updateRule(RuleData ruleData);

	/**
	 * 查询节假日
	 * @param string
	 * @return
	 */
	public PagModel getHolidayListListByYear(String year,String currentPage,String numPerPage);

	/**
	 * 创建节假日
	 * @param ruleHoliday
	 */
	public void createRuleHoliday(RuleHoliday ruleHoliday);

	/**
	 * 根据id查询节假日
	 * @param id
	 */
	public RuleHoliday findRuleHolidayById(Long id);

	/**
	 * 修改节假日
	 * @param ruleHoliday
	 */
	public void updateRuleHoliday(RuleHoliday ruleHoliday);

	/**
	 * 移除节假日
	 * @param id
	 */
	public void removeRuleHoliday(Long id);
	
	/**
	 * 验证用车人是否与规则match,follow订单返回接口设计,验证返回结果如下：
	 * 通过：返回 ""
	 * 不通过: 返回具体的不通过理由(中文)
	 * @return
	 */
	public String getOrderValidateResult(BusiOrder order,Long userId);
	
	
	/**
	 * 通过年份查询
	 * @param year
	 * @return
	 */
	public List<RuleHoliday> findRuleHolidayByYear(String year);
	
	
	/**
	 * 根据ruleIds查询已绑定rule list
	 * @param ids
	 * @return
	 */
    public List<VehicleRuleDisplayModel> getRuleListByIds(List<Long> ids);
	
	/**
	 * 查询用户已绑定规则
	 * @param userId
	 * @return
	 */
	public List<VehicleRuleData> findRuleListByUserId(Long userId);
	
	/**
	 * 根据规则ids查询规则列表
	 * @param ids
	 * @return
	 */
	public List<VehicleRuleSQLModel> findRuleListByIds(List<Long> ids);

	/**
	 * 用户规则解绑
	 * @param userId
	 */
	public void removeBindingRule(Long userId,Long ruleId);

	/**
	 * 用户绑定规则
	 * @param userBindingData
	 */
	public void userBindingRule(UserBindingData userBindingData);

	/**
	 * 找到用户所允许使用的车辆类型列表
	 * @param userId
	 * @return
	 */
	List<VehicleType> findRuleVehicleTypeByUserId(Long userId);
}
