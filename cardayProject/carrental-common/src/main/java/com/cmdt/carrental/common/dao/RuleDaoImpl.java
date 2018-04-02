package com.cmdt.carrental.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.cmdt.carday.microservice.common.model.Order.enums.VehicleType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.entity.RuleAddress;
import com.cmdt.carrental.common.entity.RuleHoliday;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.RuleData;
import com.cmdt.carrental.common.model.RuleEditData;
import com.cmdt.carrental.common.model.RuleGetOffData;
import com.cmdt.carrental.common.model.RuleGetOffEditData;
import com.cmdt.carrental.common.model.RuleGetOnData;
import com.cmdt.carrental.common.model.RuleGetOnEditData;
import com.cmdt.carrental.common.model.RuleTimeData;
import com.cmdt.carrental.common.model.RuleTimeDateItemData;
import com.cmdt.carrental.common.model.RuleTimeDateItemEditData;
import com.cmdt.carrental.common.model.RuleTimeEditData;
import com.cmdt.carrental.common.model.RuleTimeHolidayItemData;
import com.cmdt.carrental.common.model.RuleTimeHolidayItemEditData;
import com.cmdt.carrental.common.model.RuleTimeWeeklyItemData;
import com.cmdt.carrental.common.model.RuleTimeWeeklyItemEditData;
import com.cmdt.carrental.common.model.UserBindingData;
import com.cmdt.carrental.common.model.VehicleRuleData;
import com.cmdt.carrental.common.model.VehicleRuleSQLModel;
import com.cmdt.carrental.common.util.Pagination;
import com.cmdt.carrental.common.util.SqlUtil;

@Repository
public class RuleDaoImpl implements RuleDao{
	private static final Logger LOG = LoggerFactory.getLogger(RuleDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public PagModel findLocationByLocationName(String locationName, Long organizationId, String currentPage,String numPerPage){
		int currentPageNum = 1;
    	int numPerPageNum = 10;
		if(!"".equals(currentPage)){
		  currentPageNum = Integer.valueOf(String.valueOf(currentPage));
		}
		if(!"".equals(numPerPage)){
	    	numPerPageNum = Integer.valueOf(String.valueOf(numPerPage));
		}
    	List<Object> params = new ArrayList<Object>();
    	String sql = "SELECT id, name as locationName, city, position, longitude, latitude, radius, org_id as organizationId from busi_rule_address ";
		if(StringUtils.isNotEmpty(locationName)){
			sql += "where name like "+SqlUtil.processLikeInjectionStatement(locationName)+" and org_id=? order by id desc";
			params.add(organizationId);
		}else{
			sql += "where org_id=? order by id desc";
			params.add(organizationId);
		}
		
		Pagination page=new Pagination(sql, currentPageNum, numPerPageNum,RuleAddress.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}
	
	@Override
	public RuleAddress findLocation(Long locationId){
		String sql = "SELECT id,name as locationName, city, position, longitude, latitude, radius, org_id as organizationId from busi_rule_address where id=?";
        List<RuleAddress> locationList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<RuleAddress>(RuleAddress.class), locationId);
        if(locationList.size() == 0) {
            return null;
        }
        return locationList.get(0);
	}

	@Override
	public RuleAddress createStation(final RuleAddress ruleAddress){
				
		try {
					final String sql = "INSERT INTO busi_rule_address(name, city, position, longitude, latitude, radius,org_id) VALUES (?, ?, ?,?, ?, ?, ?);";

			        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
			        jdbcTemplate.update(new PreparedStatementCreator() {
			            @Override
			            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
			                int count = 1;
			                
			                psst.setString(count++, ruleAddress.getLocationName());
			                psst.setString(count++, ruleAddress.getCity());
			                psst.setString(count++, ruleAddress.getPosition());
			                psst.setString(count++, ruleAddress.getLongitude());
			                psst.setString(count++, ruleAddress.getLatitude());
			                psst.setString(count++, ruleAddress.getRadius());
			                psst.setLong(count, ruleAddress.getOrganizationId());
			                return psst;
			            }
			        }, keyHolder);

			        ruleAddress.setId(keyHolder.getKey().longValue());		
			        return ruleAddress;
		} catch (Exception e) {
			LOG.error("RuleService createPosition error, cause by:\n", e);
			return null;
		}
		
	}
	
	@Override
	public RuleAddress updateLocation(RuleAddress ruleAddress){
	
		try {
			String sql = "UPDATE busi_rule_address SET name=?, city=?, position=?, longitude=?, latitude=?, radius=?, org_id=?  where id=?";
	        jdbcTemplate.update(
	                sql,
	                ruleAddress.getLocationName(),ruleAddress.getCity(),ruleAddress.getPosition(),ruleAddress.getLongitude(),ruleAddress.getLatitude(),ruleAddress.getRadius(),ruleAddress.getOrganizationId(),ruleAddress.getId());
	        return ruleAddress;
		} catch (Exception e) {
			LOG.error("RuleService updateLocation error, cause by:\n", e);
			return null;
		}
	}
	
	@Override
	public void deleteStation(RuleAddress ruleAddress){
		
		try {					  
			 String sql = "delete from busi_rule_address where id=?";
		     jdbcTemplate.update(sql,ruleAddress.getId());
		     
		     String busi_rule_address_relation_sql = "delete from busi_rule_address_relation where rule_address_id=?";
		     jdbcTemplate.update(busi_rule_address_relation_sql,ruleAddress.getId());

		} catch (Exception e) {
			LOG.error("RuleService deleteStation error, cause by:\n", e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<VehicleRuleSQLModel> findRuleListByOrgId(Long orgId) {
		String sql = "select id as rule_id,rule_name,usage_type,time_range,vehicle_type,usage_limit from busi_rule where org_id = ? order by id desc";
		List<VehicleRuleSQLModel> retList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleRuleSQLModel.class),orgId);
		return retList;
	}

	@Override
	public List<VehicleRuleSQLModel> findRuleListByOrgIdList(List<Long> orgIds) {
		StringBuilder sql = new StringBuilder("select id as rule_id,rule_name,usage_type,time_range,vehicle_type,usage_limit from busi_rule where org_id IN (");

		int i = 0;
		for (Long org : orgIds) {
			sql.append(org);
			if (i != (orgIds.size() - 1)) {
				sql.append(",");
			}

			i++;
		}
		sql.append(") order by id desc");
		List<VehicleRuleSQLModel> retList = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper(VehicleRuleSQLModel.class));
		return retList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<VehicleRuleSQLModel> findRuleAddressByRuleIds(List<Long> ruleIds) {
		
		StringBuffer buffer = new StringBuffer("");
		buffer.append("		select relation_val.rule_id,address.name as addressname,relation_val.action_type");
		buffer.append("		from");
		buffer.append("		(");
		buffer.append("			select rule_id,rule_address_id,action_type");
		buffer.append("			from");
		buffer.append("			busi_rule_address_relation");
		buffer.append("			where rule_id");
		buffer.append("			in(");
		
		int paramSize = ruleIds.size();
		for(int i = 0 ; i < paramSize ; i ++){
			if(i != paramSize - 1){
				buffer.append(ruleIds.get(i)).append(",");
			}else{
				buffer.append(ruleIds.get(i));
			}
		}
		buffer.append("			)");
		buffer.append("		) relation_val");
		buffer.append("		left join busi_rule_address address");
		buffer.append("		on relation_val.rule_address_id = address.id");
		return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehicleRuleSQLModel.class));
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<VehicleRuleSQLModel> findRuleHolidayTimeRangeByRuleIds(List<Long> ruleIds) {
		StringBuffer buffer = new StringBuffer("");
		buffer.append("		select relation_val.rule_id,holiday_val.holiday_type,holiday_val.start_time,holiday_val.end_time");
		buffer.append("		from");
		buffer.append("		(");
		buffer.append("			select rule_id,rule_timerange_holiday_id");
		buffer.append("			from");
		buffer.append("			busi_rule_timerange_holiday_relation");
		buffer.append("			where rule_id");
		buffer.append("			in(");
		
		int paramSize = ruleIds.size();
		for(int i = 0 ; i < paramSize ; i ++){
			if(i != paramSize - 1){
				buffer.append(ruleIds.get(i)).append(",");
			}else{
				buffer.append(ruleIds.get(i));
			}
		}
		
		buffer.append("			)");
		buffer.append("		) relation_val");
		buffer.append("		left join busi_rule_timerange_holiday holiday_val");
		buffer.append("		on relation_val.rule_timerange_holiday_id = holiday_val.id");
		return  jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehicleRuleSQLModel.class));
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<VehicleRuleSQLModel> findRuleWeekTimeRangeByRuleIds(List<Long> ruleIds) {
		StringBuffer buffer = new StringBuffer("");
		buffer.append("		select relation_val.rule_id,week_val.week_index,week_val.start_time,week_val.end_time");
		buffer.append("		from");
		buffer.append("		(");
		buffer.append("			select rule_id,rule_timerange_week_id");
		buffer.append("			from");
		buffer.append("			busi_rule_timerange_week_relation");
		buffer.append("			where rule_id");
		buffer.append("			in(");
		
		int paramSize = ruleIds.size();
		for(int i = 0 ; i < paramSize ; i ++){
			if(i != paramSize - 1){
				buffer.append(ruleIds.get(i)).append(",");
			}else{
				buffer.append(ruleIds.get(i));
			}
		}
		
		buffer.append("			)");
		buffer.append("		) relation_val");
		buffer.append("		left join busi_rule_timerange_week week_val");
		buffer.append("		on relation_val.rule_timerange_week_id = week_val.id");
		return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehicleRuleSQLModel.class));
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<VehicleRuleSQLModel> findRuleDateTimeRangeByRuleIds(List<Long> ruleIds) {
		StringBuffer buffer = new StringBuffer("");
		buffer.append("		select relation_val.rule_id,date_val.start_day,date_val.end_day,date_val.start_time,date_val.end_time");
		buffer.append("		from");
		buffer.append("		(");
		buffer.append("			select rule_id,rule_timerange_date_id");
		buffer.append("			from");
		buffer.append("			busi_rule_timerange_date_relation");
		buffer.append("			where rule_id");
		buffer.append("			in(");
		
		int paramSize = ruleIds.size();
		for(int i = 0 ; i < paramSize ; i ++){
			if(i != paramSize - 1){
				buffer.append(ruleIds.get(i)).append(",");
			}else{
				buffer.append(ruleIds.get(i));
			}
		}
		
		buffer.append("			)");
		buffer.append("		) relation_val");
		buffer.append("		left join busi_rule_timerange_date date_val");
		buffer.append("		on relation_val.rule_timerange_date_id = date_val.id");
		return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehicleRuleSQLModel.class));
	}

	@Override
	public void removeRule(Long ruleId) {
		
		//时间规则
		cleanHolidayRuleTimeDataAndRelation(ruleId);
		cleanWeeklyRuleTimeDataAndRelation(ruleId);
		cleanDateRuleTimeDataAndRelation(ruleId);
		
		//上下车位置规则
		String address_sql = "delete from busi_rule_address_relation where rule_id=?";
		jdbcTemplate.update(address_sql,ruleId);
		
		//删除关联用户
		String busi_rule_user_relation_sql = "delete from busi_rule_user_relation where rule_id = ?";
		jdbcTemplate.update(busi_rule_user_relation_sql,ruleId);
		
		//删除用车规则
		String ruleSql = "delete from busi_rule where id=?";
	    jdbcTemplate.update(ruleSql,ruleId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<VehicleRuleSQLModel> findUsersByRuleIds(List<Long> ruleIds) {
		
		StringBuffer buffer = new StringBuffer("");
		buffer.append("		select rule_id,count(user_id) as employee_num from busi_rule_user_relation where rule_id ");
		buffer.append("			in(");
		int paramSize = ruleIds.size();
		for(int i = 0 ; i < paramSize ; i ++){
			if(i != paramSize - 1){
				buffer.append(ruleIds.get(i)).append(",");
			}else{
				buffer.append(ruleIds.get(i));
			}
		}
		buffer.append("			)");
		buffer.append("		group by rule_id");
		return jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehicleRuleSQLModel.class));
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<VehicleRuleSQLModel> getVehicleRuleGetOnAndOffAddress(Long orgId) {
		List<VehicleRuleSQLModel> retList = jdbcTemplate.query("select id as address_id,name as address_name,radius as address_radius from busi_rule_address where org_id = ?", new BeanPropertyRowMapper(VehicleRuleSQLModel.class),orgId);
		return retList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<VehicleRuleSQLModel> getAddressRelationListByRuleIdAndActionType(Long ruleId, Integer actionType) {
		List<VehicleRuleSQLModel> retList = jdbcTemplate.query("select rule_address_id as address_id from busi_rule_address_relation where rule_id = ? and action_type = ?", new BeanPropertyRowMapper(VehicleRuleSQLModel.class),ruleId,actionType);
		return retList;
	}

	@Override
	public void addRule(final RuleData ruleData,final Long orgId) {
		//规则表
		final String busi_rule_sql = "INSERT INTO busi_rule(rule_name,usage_type,time_range,vehicle_type,usage_limit,org_id) VALUES (?, ?, ?,?, ?, ?);";
        GeneratedKeyHolder busi_rule_keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement psst = connection.prepareStatement(busi_rule_sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                
                psst.setString(count++,ruleData.getRuleName());
                psst.setInt(count++, Integer.valueOf(ruleData.getRuleType()));
                psst.setInt(count++, Integer.valueOf(ruleData.getTimeList().getTimeRangeType()));
                psst.setString(count++, ruleData.getVehicleType());
                psst.setInt(count++, Integer.valueOf(ruleData.getUseLimit()));
                psst.setLong(count, orgId);
                return psst;
            }
        }, busi_rule_keyHolder);
        
        Long ruleId = busi_rule_keyHolder.getKey().longValue();
        
        //时间范围
        RuleTimeData ruleTimeData = ruleData.getTimeList();
        addRuleTimeData(ruleTimeData,ruleId);
        
        //上车位置
        RuleGetOnData ruleGetOnData = ruleData.getGetOnList();
        addGetOnAddressRelation(ruleGetOnData,ruleId);
        
        //下车位置
        RuleGetOffData ruleGetOffData = ruleData.getGetOffList();
        addGetOffAddressRelation(ruleGetOffData,ruleId);
	}



	private void addRuleTimeData(RuleTimeData ruleTimeData, Long ruleId) {
		 String timeRangeType = ruleTimeData.getTimeRangeType();
	        if(!"0".equals(timeRangeType)){
	        	//法定工作日/节假日
	        	if("1".equals(timeRangeType)){
	        		addHolidayRuleTimeData(ruleTimeData.getHolidayData(),ruleId);
	        	}
	        	
	        	//星期
	        	if("2".equals(timeRangeType)){
	        		addWeeklyRuleTimeData(ruleTimeData.getWeeklyData(),ruleId);
	        	}
	        	
	        	//日期
	        	if("3".equals(timeRangeType)){
	        		addDateRuleTimeData(ruleTimeData.getDateData(),ruleId);
	        	}
	        }
	}
	
	private void addHolidayRuleTimeData(List<RuleTimeHolidayItemData> holidayData,Long ruleId) {
		List<Long> holidayIds = new ArrayList<Long>();
		if(holidayData != null && holidayData.size() > 0){
			 
			 GeneratedKeyHolder busi_rule_timerange_holiday_keyHolder = new GeneratedKeyHolder();
			 final String busi_rule_timerange_holiday_sql = "INSERT INTO busi_rule_timerange_holiday(holiday_type,start_time,end_time) VALUES (?, ?, ?);";
			 
			//table busi_rule_timerange_holiday
			for(final RuleTimeHolidayItemData ruleTimeHolidayItemData : holidayData){
		        jdbcTemplate.update(new PreparedStatementCreator() {
		            @Override
		            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		                PreparedStatement psst = connection.prepareStatement(busi_rule_timerange_holiday_sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
		                int count = 1;
		                psst.setInt(count++, Integer.valueOf(ruleTimeHolidayItemData.getHolidayType()));
		                psst.setString(count++, ruleTimeHolidayItemData.getStartTime());
		                psst.setString(count, ruleTimeHolidayItemData.getEndTime());
		                return psst;
		            }
		        }, busi_rule_timerange_holiday_keyHolder);
		        holidayIds.add(busi_rule_timerange_holiday_keyHolder.getKey().longValue());
			}
			
			//table busi_rule_timerange_holiday_relation
			if(holidayIds != null && holidayIds.size() > 0){
				final String busi_rule_timerange_holiday_relation_sql = "INSERT INTO busi_rule_timerange_holiday_relation(rule_id,rule_timerange_holiday_id) VALUES (?, ?);";
				for(Long holidayId : holidayIds){
					jdbcTemplate.update(busi_rule_timerange_holiday_relation_sql,ruleId,holidayId);
				}
			}
		}
	}
	
	private void addWeeklyRuleTimeData(List<RuleTimeWeeklyItemData> weekData,Long ruleId) {
		List<Long> weekIds = new ArrayList<Long>();
		if(weekData != null && weekData.size() > 0){
			 
			 GeneratedKeyHolder busi_rule_timerange_week_keyHolder = new GeneratedKeyHolder();
			 final String busi_rule_timerange_week_sql = "INSERT INTO busi_rule_timerange_week(week_index,start_time,end_time) VALUES (?, ?, ?);";
			 
			//table busi_rule_timerange_week
			for(final RuleTimeWeeklyItemData ruleTimeWeeklyItemData : weekData){
		        jdbcTemplate.update(new PreparedStatementCreator() {
		            @Override
		            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		                PreparedStatement psst = connection.prepareStatement(busi_rule_timerange_week_sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
		                int count = 1;
		                psst.setInt(count++, Integer.valueOf(ruleTimeWeeklyItemData.getWeeklyType()));
		                psst.setString(count++, ruleTimeWeeklyItemData.getStartTime());
		                psst.setString(count, ruleTimeWeeklyItemData.getEndTime());
		                return psst;
		            }
		        }, busi_rule_timerange_week_keyHolder);
		        weekIds.add(busi_rule_timerange_week_keyHolder.getKey().longValue());
			}
			
			//table busi_rule_timerange_week_relation
			if(weekIds != null && weekIds.size() > 0){
				final String busi_rule_timerange_week_relation_sql = "INSERT INTO busi_rule_timerange_week_relation(rule_id,rule_timerange_week_id) VALUES (?, ?);";
				for(Long weekId : weekIds){
					jdbcTemplate.update(busi_rule_timerange_week_relation_sql,ruleId,weekId);
				}
			}
		}
	}

	private void addDateRuleTimeData(List<RuleTimeDateItemData> dateData,Long ruleId) {
		List<Long> dateIds = new ArrayList<Long>();
		if(dateData != null && dateData.size() > 0){
			 
			 GeneratedKeyHolder busi_rule_timerange_date_keyHolder = new GeneratedKeyHolder();
			 final String busi_rule_timerange_date_sql = "INSERT INTO busi_rule_timerange_date(start_day,end_day,start_time,end_time) VALUES (?, ?, ?,?);";
			 
			//table busi_rule_timerange_week
			for(final RuleTimeDateItemData ruleTimeDateItemData : dateData){
		        jdbcTemplate.update(new PreparedStatementCreator() {
		            @Override
		            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		                PreparedStatement psst = connection.prepareStatement(busi_rule_timerange_date_sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
		                int count = 1;
		                psst.setString(count++, ruleTimeDateItemData.getStartDay());
		                psst.setString(count++, ruleTimeDateItemData.getEndDay());
		                psst.setString(count++, ruleTimeDateItemData.getStartTime());
		                psst.setString(count, ruleTimeDateItemData.getEndTime());
		                return psst;
		            }
		        }, busi_rule_timerange_date_keyHolder);
		        dateIds.add(busi_rule_timerange_date_keyHolder.getKey().longValue());
			}
			
			//table busi_rule_timerange_date_relation
			if(dateIds != null && dateIds.size() > 0){
				final String busi_rule_timerange_date_relation_sql = "INSERT INTO busi_rule_timerange_date_relation(rule_id,rule_timerange_date_id) VALUES (?, ?);";
				for(Long dateId : dateIds){
					jdbcTemplate.update(busi_rule_timerange_date_relation_sql,ruleId,dateId);
				}
			}
		}
	}

	private void addGetOnAddressRelation(RuleGetOnData ruleGetOnData,Long ruleId) {
        if("1".equals(ruleGetOnData.getGetOnType())){ //0:不限 1:存在上车位置
        	List<String> getOndataList = ruleGetOnData.getGetOndata();
        	if(getOndataList != null && getOndataList.size() > 0){
        		final String busi_rule_address_relation_sql = "INSERT INTO busi_rule_address_relation(rule_id,rule_address_id,action_type) VALUES (?, ?, ?);";
        		for(final String ruleAddressId : getOndataList){
        			jdbcTemplate.update(busi_rule_address_relation_sql,ruleId,Long.valueOf(ruleAddressId),0);
        		}
        	}
        }
	}
	
	private void addGetOffAddressRelation(RuleGetOffData ruleGetOffData,Long ruleId) {
		 if("1".equals(ruleGetOffData.getGetOffType())){ //0:不限 1:存在下车位置
	        	List<String> getOffdataList = ruleGetOffData.getGetOffdata();
	        	if(getOffdataList != null && getOffdataList.size() > 0){
	        		final String busi_rule_address_relation_sql = "INSERT INTO busi_rule_address_relation(rule_id,rule_address_id,action_type) VALUES (?, ?, ?);";
	        		for(final String ruleAddressId : getOffdataList){
	        			jdbcTemplate.update(busi_rule_address_relation_sql,ruleId,Long.valueOf(ruleAddressId),1);
	        		}
	        	}
	     }
	}

	@Override
	public RuleEditData findRuleById(Long ruleId, Long orgId) {
		RuleEditData ruleEditData = new RuleEditData();
		
		//查询规则表
		String sql = "select id as rule_id,rule_name,usage_type,time_range,vehicle_type,usage_limit from busi_rule where id=? and org_id = ?";
		List<VehicleRuleSQLModel> retList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleRuleSQLModel.class),ruleId,orgId);
		if(retList != null && retList.size() > 0){
			VehicleRuleSQLModel busi_rule_vehicleRuleSQLModel = retList.get(0);
			
			//填充基本信息
			ruleEditData.setRuleId(busi_rule_vehicleRuleSQLModel.getRule_id());
			ruleEditData.setRuleName(busi_rule_vehicleRuleSQLModel.getRule_name());
			ruleEditData.setRuleType(String.valueOf(busi_rule_vehicleRuleSQLModel.getUsage_type()));
			ruleEditData.setVehicleType(busi_rule_vehicleRuleSQLModel.getVehicle_type());
			ruleEditData.setUseLimit(String.valueOf(busi_rule_vehicleRuleSQLModel.getUsage_limit()));
			
			//填充时间范围类型
			RuleTimeEditData timeList = new RuleTimeEditData();
			timeList.setTimeRangeType(String.valueOf(busi_rule_vehicleRuleSQLModel.getTime_range()));
			ruleEditData.setTimeList(timeList);
			
			//填充时间
			populateTimeRange(ruleEditData,ruleId);
			//填充上车位置
			populateGetOnAddress(ruleEditData,ruleId);
			//填充下车位置
			populateGetOffAddress(ruleEditData,ruleId);
			return ruleEditData;
		}
		return null;
	}

	private void populateTimeRange(RuleEditData ruleEditData, Long ruleId) {
		RuleTimeEditData ruleTimeEditData = ruleEditData.getTimeList();
		 String timeRangeType = ruleTimeEditData.getTimeRangeType();
	        if(!"0".equals(timeRangeType)){
	        	//法定工作日/节假日
	        	if("1".equals(timeRangeType)){
	        		populateHolidayTimeRange(ruleTimeEditData,ruleId);
	        	}
	        	
	        	//星期
	        	if("2".equals(timeRangeType)){
	        		populateWeeklyTimeRange(ruleTimeEditData,ruleId);
	        	}
	        	
	        	//日期
	        	if("3".equals(timeRangeType)){
	        		populateDateTimeRange(ruleTimeEditData,ruleId);
	        	}
	        }
	}
	
	private void populateHolidayTimeRange(RuleTimeEditData ruleTimeEditData, Long ruleId) {
		List<RuleTimeHolidayItemEditData> holidayData = new ArrayList<RuleTimeHolidayItemEditData>();
		StringBuffer buffer = new StringBuffer();
		buffer.append("		select id as holiday_id,holiday_type,start_time,end_time");
		buffer.append("		from"); 
		buffer.append("		busi_rule_timerange_holiday");
		buffer.append("		where id");
		buffer.append("		in");
		buffer.append("		(");
		buffer.append("			select");
		buffer.append("			rule_timerange_holiday_id");
		buffer.append("			from");
		buffer.append("			busi_rule_timerange_holiday_relation");
		buffer.append("			where");
		buffer.append("			rule_id = ?");
		buffer.append("		)");
		List<VehicleRuleSQLModel> retList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehicleRuleSQLModel.class),ruleId);
		if(retList != null && retList.size() > 0){
			for(VehicleRuleSQLModel vehicleRuleSQLModel : retList){
				RuleTimeHolidayItemEditData itemEditData = new RuleTimeHolidayItemEditData();
				itemEditData.setHolidayId(vehicleRuleSQLModel.getHoliday_id());
				itemEditData.setHolidayType(String.valueOf(vehicleRuleSQLModel.getHoliday_type()));
				itemEditData.setStartTime(vehicleRuleSQLModel.getStart_time());
				itemEditData.setEndTime(vehicleRuleSQLModel.getEnd_time());
				holidayData.add(itemEditData);
			}
		}
		ruleTimeEditData.setHolidayData(holidayData);
	}
	
	private void populateWeeklyTimeRange(RuleTimeEditData ruleTimeEditData, Long ruleId) {
		List<RuleTimeWeeklyItemEditData> weeklyData = new ArrayList<RuleTimeWeeklyItemEditData>();
		StringBuffer buffer = new StringBuffer();
		buffer.append("		select id as weekly_id,week_index,start_time,end_time");
		buffer.append("		from"); 
		buffer.append("		busi_rule_timerange_week");
		buffer.append("		where id");
		buffer.append("		in");
		buffer.append("		(");
		buffer.append("			select");
		buffer.append("			rule_timerange_week_id");
		buffer.append("			from");
		buffer.append("			busi_rule_timerange_week_relation");
		buffer.append("			where");
		buffer.append("			rule_id = ?");
		buffer.append("		)");
		List<VehicleRuleSQLModel> retList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehicleRuleSQLModel.class),ruleId);
		if(retList != null && retList.size() > 0){
			for(VehicleRuleSQLModel vehicleRuleSQLModel : retList){
				RuleTimeWeeklyItemEditData itemEditData = new RuleTimeWeeklyItemEditData();
				itemEditData.setWeeklyId(vehicleRuleSQLModel.getWeekly_id());
				itemEditData.setWeeklyType(String.valueOf(vehicleRuleSQLModel.getWeek_index()));
				itemEditData.setStartTime(vehicleRuleSQLModel.getStart_time());
				itemEditData.setEndTime(vehicleRuleSQLModel.getEnd_time());
				weeklyData.add(itemEditData);
			}
		}
		ruleTimeEditData.setWeeklyData(weeklyData);
	}

	private void populateDateTimeRange(RuleTimeEditData ruleTimeEditData, Long ruleId) {
		List<RuleTimeDateItemEditData> dateData = new ArrayList<RuleTimeDateItemEditData>();
		StringBuffer buffer = new StringBuffer();
		buffer.append("		select id as date_id,start_day,end_day,start_time,end_time");
		buffer.append("		from"); 
		buffer.append("		busi_rule_timerange_date");
		buffer.append("		where id");
		buffer.append("		in");
		buffer.append("		(");
		buffer.append("			select");
		buffer.append("			rule_timerange_date_id");
		buffer.append("			from");
		buffer.append("			busi_rule_timerange_date_relation");
		buffer.append("			where");
		buffer.append("			rule_id = ?");
		buffer.append("		)");
		List<VehicleRuleSQLModel> retList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehicleRuleSQLModel.class),ruleId);
		if(retList != null && retList.size() > 0){
			for(VehicleRuleSQLModel vehicleRuleSQLModel : retList){
				RuleTimeDateItemEditData itemEditData = new RuleTimeDateItemEditData();
				itemEditData.setDateId(vehicleRuleSQLModel.getDate_id());
				itemEditData.setStartDay(vehicleRuleSQLModel.getStart_day());
				itemEditData.setEndDay(vehicleRuleSQLModel.getEnd_day());
				itemEditData.setStartTime(vehicleRuleSQLModel.getStart_time());
				itemEditData.setEndTime(vehicleRuleSQLModel.getEnd_time());
				dateData.add(itemEditData);
			}
		}
		ruleTimeEditData.setDateData(dateData);
	}

	private void populateGetOnAddress(RuleEditData ruleEditData, Long ruleId) {
		RuleGetOnEditData  getOnList = new RuleGetOnEditData();
		String sql = "select rule_address_id as address_id from busi_rule_address_relation where action_type = 0 and rule_id = ?";
		List<VehicleRuleSQLModel> retList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleRuleSQLModel.class),ruleId);
		if(retList != null && retList.size() > 0){
			List<Long> getOndata = new ArrayList<Long>();
			for(VehicleRuleSQLModel vehicleRuleSQLModel : retList){
				getOndata.add(vehicleRuleSQLModel.getAddress_id());
			}
			getOnList.setGetOndata(getOndata);
			getOnList.setGetOnType("1"); //限制上车位置
		}else{
			getOnList.setGetOnType("0"); //不限上车位置
		}
		ruleEditData.setGetOnList(getOnList);
	}
	
	private void populateGetOffAddress(RuleEditData ruleEditData, Long ruleId) {
		RuleGetOffEditData  getOffList = new RuleGetOffEditData();
		String sql = "select rule_address_id as address_id from busi_rule_address_relation where action_type = 1 and rule_id = ?";
		List<VehicleRuleSQLModel> retList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleRuleSQLModel.class),ruleId);
		if(retList != null && retList.size() > 0){
			List<Long> getOffdata = new ArrayList<Long>();
			for(VehicleRuleSQLModel vehicleRuleSQLModel : retList){
				getOffdata.add(vehicleRuleSQLModel.getAddress_id());
			}
			getOffList.setGetOffdata(getOffdata);
			getOffList.setGetOffType("1"); //限制下车位置
		}else{
			getOffList.setGetOffType("0"); //不限下车位置
		}
		ruleEditData.setGetOffList(getOffList);
	}

	@Override
	public void updateRule(RuleData ruleData) {
		
		Long ruleId = ruleData.getRuleId();
		//规则表
		final String busi_rule_sql = "UPDATE busi_rule set rule_name=?,usage_type=?,time_range=?,vehicle_type=?,usage_limit=? where id=?";
		jdbcTemplate.update(busi_rule_sql,
				ruleData.getRuleName(),
				Integer.valueOf(ruleData.getRuleType()),
				Integer.valueOf(ruleData.getTimeList().getTimeRangeType()),
				ruleData.getVehicleType(),
				Integer.valueOf(ruleData.getUseLimit()),
				ruleId
				);
        
        //时间范围
        RuleTimeData ruleTimeData = ruleData.getTimeList();
        updateRuleTimeData(ruleTimeData,ruleId);
        
        //上车位置
        RuleGetOnData ruleGetOnData = ruleData.getGetOnList();
        updateGetOnAddress(ruleGetOnData,ruleId);
        
        //下车位置
        RuleGetOffData ruleGetOffData = ruleData.getGetOffList();
        updateGetOffAddress(ruleGetOffData,ruleId);
	}
	
	private void updateRuleTimeData(RuleTimeData ruleTimeData, Long ruleId) {
		    String timeRangeType = ruleTimeData.getTimeRangeType();
	        if(!"0".equals(timeRangeType)){
	        	//法定工作日/节假日
	        	if("1".equals(timeRangeType)){
	        		cleanHolidayRuleTimeDataAndRelation(ruleId);
	        	}else if("2".equals(timeRangeType)){//星期
	        		cleanWeeklyRuleTimeDataAndRelation(ruleId);
	        	}else if("3".equals(timeRangeType)){//日期
	        		cleanDateRuleTimeDataAndRelation(ruleId);
	        	}
	        	addRuleTimeData(ruleTimeData,ruleId);
	        }else{
	        	cleanHolidayRuleTimeDataAndRelation(ruleId);
	        	cleanWeeklyRuleTimeDataAndRelation(ruleId);
	        	cleanDateRuleTimeDataAndRelation(ruleId);
	        }
	}
	
	/**
	 * 清除holiday数据以及与rule相关的表数据
	 * @param ruleId
	 */
	private void cleanHolidayRuleTimeDataAndRelation(Long ruleId) {
		StringBuffer holiday_sql = new StringBuffer();
		holiday_sql.append("		delete from busi_rule_timerange_holiday");
		holiday_sql.append("		where id"); 
		holiday_sql.append("		in");
		holiday_sql.append("		(");
		holiday_sql.append("			select rule_timerange_holiday_id"); 
		holiday_sql.append("			from busi_rule_timerange_holiday_relation"); 
		holiday_sql.append("			where rule_id = ?");
		holiday_sql.append("		)");
	    jdbcTemplate.update(holiday_sql.toString(),ruleId);
	     
	     String sql = "delete from busi_rule_timerange_holiday_relation where rule_id=?";
	     jdbcTemplate.update(sql,ruleId);
	}
	
	/**
	 * 清除week数据以及与rule相关的表数据
	 * @param ruleId
	 */
	private void cleanWeeklyRuleTimeDataAndRelation(Long ruleId) {
		StringBuffer holiday_sql = new StringBuffer();
		holiday_sql.append("		delete from busi_rule_timerange_week");
		holiday_sql.append("		where id"); 
		holiday_sql.append("		in");
		holiday_sql.append("		(");
		holiday_sql.append("			select rule_timerange_week_id"); 
		holiday_sql.append("			from busi_rule_timerange_week_relation"); 
		holiday_sql.append("			where rule_id = ?");
		holiday_sql.append("		)");
	    jdbcTemplate.update(holiday_sql.toString(),ruleId);
	     
	     String sql = "delete from busi_rule_timerange_week_relation where rule_id=?";
	     jdbcTemplate.update(sql,ruleId);
	}
	
	/**
	 * 清除date数据以及与rule相关的表数据
	 * @param ruleId
	 */
	private void cleanDateRuleTimeDataAndRelation(Long ruleId) {
		StringBuffer holiday_sql = new StringBuffer();
		holiday_sql.append("		delete from busi_rule_timerange_date");
		holiday_sql.append("		where id"); 
		holiday_sql.append("		in");
		holiday_sql.append("		(");
		holiday_sql.append("			select rule_timerange_date_id"); 
		holiday_sql.append("			from busi_rule_timerange_date_relation"); 
		holiday_sql.append("			where rule_id = ?");
		holiday_sql.append("		)");
	    jdbcTemplate.update(holiday_sql.toString(),ruleId);
	     
	     String sql = "delete from busi_rule_timerange_date_relation where rule_id=?";
	     jdbcTemplate.update(sql,ruleId);
	}
	
	
	private void updateGetOnAddress(RuleGetOnData ruleGetOnData, Long ruleId) {
		String sql = "delete from busi_rule_address_relation where rule_id=? and action_type = 0";
	    jdbcTemplate.update(sql,ruleId);
		if(!"0".equals(ruleGetOnData.getGetOnType())){
			addGetOnAddressRelation(ruleGetOnData,ruleId);
		}
	}
	
	private void updateGetOffAddress(RuleGetOffData ruleGetOffData, Long ruleId) {
		String sql = "delete from busi_rule_address_relation where rule_id=? and action_type = 1";
	    jdbcTemplate.update(sql,ruleId);
		if(!"0".equals(ruleGetOffData.getGetOffType())){//不限
			addGetOffAddressRelation(ruleGetOffData,ruleId);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public PagModel getRuleHolidayListByYear(String year,String currentPage,String numPerPage) {
		
		int currentPageNum = 1;
    	int numPerPageNum = 10;
		if(!"".equals(currentPage)){
		  currentPageNum = Integer.valueOf(String.valueOf(currentPage));
		}
		if(!"".equals(numPerPage)){
	    	numPerPageNum = Integer.valueOf(String.valueOf(numPerPage));
		}
		
		List<Object> params = new ArrayList<Object>();
		
    	String sql = "select id,holiday_year as holidayYear,holiday_type as holidayType,holiday_time as holidayTime,adjust_holiday_time as adjustHolidayTime from busi_rule_holiday ";
		if(StringUtils.isNotEmpty(year)){
			sql += "where holiday_year=?";
			params.add(year);
		}
		
		Pagination page=new Pagination(sql, currentPageNum, numPerPageNum,RuleHoliday.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}

	@Override
	public void createRuleHoliday(final RuleHoliday ruleHoliday) {
		final String sql =
	            "insert into busi_rule_holiday(holiday_year, holiday_type, holiday_time, adjust_holiday_time) "
	            + "values(?,?,?,?)";
	        
	        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	        jdbcTemplate.update(new PreparedStatementCreator()
	        {
	            @Override
	            public PreparedStatement createPreparedStatement(Connection connection)
	                throws SQLException
	            {
	                PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
	                int count = 1;
	                psst.setString(count++, ruleHoliday.getHolidayYear());
	                psst.setString(count++, ruleHoliday.getHolidayType());
	                psst.setString(count++, ruleHoliday.getHolidayTime());
	                psst.setString(count, ruleHoliday.getAdjustHolidayTime());
	                return psst;
	            }
	        }, keyHolder);
	}

	@Override
	@SuppressWarnings("unchecked")
	public RuleHoliday findRuleHolidayById(Long id) {
		final String sql = "select id,holiday_year, holiday_type, holiday_time, adjust_holiday_time from busi_rule_holiday where id=?";
		List<RuleHoliday> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(RuleHoliday.class),id);
        if (list.size() == 0)
        {
            return null;
        }
        return list.get(0);
	}

	@Override
	public void updateRuleHoliday(RuleHoliday ruleHoliday) {
		final String sql = "update busi_rule_holiday set holiday_year=?, holiday_type=?, holiday_time=?, adjust_holiday_time=? where id=?";
        jdbcTemplate.update(sql,
        		ruleHoliday.getHolidayYear(),
        		ruleHoliday.getHolidayType(),
        		ruleHoliday.getHolidayTime(),
        		ruleHoliday.getAdjustHolidayTime(),
        		ruleHoliday.getId()
            );
	}

	@Override
	public void removeRuleHoliday(Long id) {
		String sql = "delete from busi_rule_holiday where id=?";
		jdbcTemplate.update(sql,id);
	}

	@Override
	public List<VehicleRuleData> findRuleListByUserId(Long userId) {
		String sql = "select rule_id from busi_rule_user_relation where user_id=?";
		List<VehicleRuleData> retList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleRuleData.class),userId);
		if(retList != null && retList.size() > 0)
		{
			return retList;
		}
		return null;
	}

	@Override
	public RuleEditData findRuleById(Long ruleId) {
         RuleEditData ruleEditData = new RuleEditData();
		
		//查询规则表
		String sql = "select id as rule_id,rule_name,usage_type,time_range,vehicle_type,usage_limit from busi_rule where id=?";
		List<VehicleRuleSQLModel> retList = jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleRuleSQLModel.class),ruleId);
		if(retList != null && retList.size() > 0){
			VehicleRuleSQLModel busi_rule_vehicleRuleSQLModel = retList.get(0);
			
			//填充基本信息
			ruleEditData.setRuleId(busi_rule_vehicleRuleSQLModel.getRule_id());
			ruleEditData.setRuleName(busi_rule_vehicleRuleSQLModel.getRule_name());
			ruleEditData.setRuleType(String.valueOf(busi_rule_vehicleRuleSQLModel.getUsage_type()));
			ruleEditData.setVehicleType(busi_rule_vehicleRuleSQLModel.getVehicle_type());
			ruleEditData.setUseLimit(String.valueOf(busi_rule_vehicleRuleSQLModel.getUsage_limit()));
			
			//填充时间范围类型
			RuleTimeEditData timeList = new RuleTimeEditData();
			timeList.setTimeRangeType(String.valueOf(busi_rule_vehicleRuleSQLModel.getTime_range()));
			ruleEditData.setTimeList(timeList);
			
			//填充时间
			populateTimeRange(ruleEditData,ruleId);
			//填充上车位置
			populateGetOnAddress(ruleEditData,ruleId);
			//填充下车位置
			populateGetOffAddress(ruleEditData,ruleId);
			return ruleEditData;
		}
		return null;
	}
	
	@Override
	public List<VehicleRuleSQLModel> findRuleListByIds(List<Long> ids) {
		String sql = "select id as rule_id,rule_name,usage_type,time_range,vehicle_type,usage_limit from busi_rule where id in(";
		int paramSize = ids.size();
		for(int i = 0 ; i < paramSize ; i ++){
			if(i != paramSize - 1){
				sql += ids.get(i) + ",";
			}else{
				sql += ids.get(i);
			}
		}
		sql += ")";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleRuleSQLModel.class));
	}

	@Override
	public void removeBindingRule(Long userId,Long ruleId) {
	     String busi_rule_address_relation_sql = "delete from busi_rule_user_relation where user_id=? and rule_id=?";
	     jdbcTemplate.update(busi_rule_address_relation_sql,userId,ruleId);
	}

	@Override
	public void userBindingRule(UserBindingData userBindingData) {
		Long userId = userBindingData.getUserId();
		
		final String sql =
	            "insert into busi_rule_user_relation(rule_id, user_id) "
	            + "values(?,?)";
		List<Long> ruleIds = userBindingData.getRuleList();
		for(Long ruleId : ruleIds){
			jdbcTemplate.update(sql,ruleId,userId);
		}
	}

	@Override
	public List<RuleHoliday> findRuleHolidayByYear(String year) {
		final String sql = "select id,holiday_year, holiday_type, holiday_time, adjust_holiday_time from busi_rule_holiday where holiday_year=?";
		List<RuleHoliday> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(RuleHoliday.class),year);
        if (list.size() == 0)
        {
            return null;
        }
        return list;
	}

	@Override
	public List<RuleAddress> findLocations(List<Long> locationIds) {
		String sql = "SELECT id,name as locationName, city, position, longitude, latitude, radius, org_id as organizationId from busi_rule_address where id in(";
		
		int paramSize = locationIds.size();
		for(int i = 0 ; i < paramSize ; i ++){
			if(i != paramSize - 1){
				sql += locationIds.get(i) + ",";
			}else{
				sql += locationIds.get(i);
			}
		}
		
		sql += ")";
        List<RuleAddress> locationList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<RuleAddress>(RuleAddress.class));
        if(locationList != null && locationList.size() > 0) {
            return locationList;
        }
        return null;
	}

	/**
	 * 查找用户可以使用的车辆列表，只查找 time_range = 0 表示 时间不限的 规则
	 * @param userId
	 * @return
	 */
	@Override
	public List<VehicleType> findRuleVehicleTypeByUserId(Long userId) {
		String sql = "select r.vehicle_type from busi_rule r inner join " +
				"busi_rule_user_relation ur on r.id = ur.rule_id where ur.user_id = ? AND r.time_range = 0";

		Set<VehicleType> typeSet = new HashSet<>();

		List<List<VehicleType>> vehicleTypeList = jdbcTemplate.query(sql, new RowMapper<List<VehicleType>>() {
			@Override
			public List<VehicleType> mapRow(ResultSet resultSet, int i) throws SQLException {
				String[] types = resultSet.getString("vehicle_type").split(",");
				List<VehicleType> vehicleTypes = new ArrayList<>();
				for (String type : types) {
					Integer typeInt = Integer.valueOf(type);

					vehicleTypes.add(VehicleType.createVehicleType(typeInt));
				}
				return vehicleTypes;
			}
		}, userId);
		if (vehicleTypeList != null && vehicleTypeList.size() != 0) {
			for (List<VehicleType> vehicleTypes : vehicleTypeList) {
				typeSet.addAll(vehicleTypes);
			}
		}

		return new ArrayList(typeSet);
	}

}
