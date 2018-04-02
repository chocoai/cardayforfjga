package com.cmdt.carrental.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.entity.VehicleMaintenance;
import com.cmdt.carrental.common.model.MaintenanceListModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.UsageReportSQLModel;
import com.cmdt.carrental.common.model.VehicleDataCountModel;
import com.cmdt.carrental.common.model.VehicleMaintainInfoModel;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.Pagination;
import com.cmdt.carrental.common.util.SqlUtil;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.common.util.TypeUtils;

@Repository
public class VehicleMaintenanceDaoImpl implements VehicleMaintenanceDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public VehicleMaintenance create(final VehicleMaintenance maintenance) {
		final String sql = "insert into busi_vehicle_maintenance(vehicle_id,header_last_mileage,header_maintenance_mileage,maintenance_mileage, cur_time, maintenance_time, maintenance_due_time, threshold_month, maintenance_threshold_time,update_time,travel_mileage) values(?,?,?,?,?,?,?,?,?,?,?)";

		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement psst = connection.prepareStatement(sql, new String[] { "id" }); //NOSONAR statement and conn already been closed in jdbcTemplate update method
				int count = 1;
				psst.setLong(count++, maintenance.getVehicleId());
//				psst.setLong(count++, maintenance.getHeaderLatestMileage());
				psst.setLong(count++, maintenance.getHeaderLastMileage());
				psst.setLong(count++, maintenance.getHeaderMaintenanceMileage());
				psst.setLong(count++, maintenance.getMaintenanceMileage());
				psst.setTimestamp(count++, new java.sql.Timestamp(maintenance.getCurTime().getTime()));
				psst.setInt(count++, maintenance.getMaintenanceTime());
				psst.setTimestamp(count++, new java.sql.Timestamp(maintenance.getMaintenanceDueTime().getTime()));
				psst.setInt(count++, maintenance.getThresholdMonth());
				psst.setTimestamp(count++, new java.sql.Timestamp(maintenance.getMaintenanceThresholdTime().getTime()));
				psst.setTimestamp(count++, new java.sql.Timestamp(maintenance.getUpdateTime().getTime()));
				psst.setLong(count, maintenance.getTravelMileage());
				return psst;
			}
		}, keyHolder);
		maintenance.setId(keyHolder.getKey().longValue());
		return maintenance;
	}

	public VehicleMaintenance update(VehicleMaintenance maintenance) {
		final String sql = "update busi_vehicle_maintenance set header_latest_mileage=?,header_last_mileage=?,header_maintenance_mileage=?, maintenance_mileage=?,"
				+ " cur_time=?, maintenance_time = ?, maintenance_due_time = ?, alert_mileage_warn = ?,cur_time_warn = ?, maintenance_threshold_time = ?, travel_mileage = ?,update_time = ?  where id=?";
		jdbcTemplate.update(sql, maintenance.getHeaderLatestMileage(), maintenance.getHeaderLastMileage(),maintenance.getHeaderMaintenanceMileage(),
				maintenance.getMaintenanceMileage(),new java.sql.Timestamp(maintenance.getCurTime().getTime()), maintenance.getMaintenanceTime(), new java.sql.Timestamp(maintenance.getMaintenanceDueTime().getTime()),
				maintenance.getAlertMileageWarn(), maintenance.getCurTimeWarn(), new java.sql.Timestamp(maintenance.getMaintenanceThresholdTime().getTime()), maintenance.getTravelMileage(), new java.sql.Timestamp(maintenance.getUpdateTime().getTime()), maintenance.getId());
		return maintenance;
	}
	
	@SuppressWarnings("unchecked")
	public PagModel listPage(VehicleMaintainInfoModel model,List<Long> orgIdList) {
		String sql="select t.id,t.vehicle_id,(t.header_maintenance_mileage+travel_mileage) header_latest_mileage, "
				+ "header_maintenance_mileage,maintenance_mileage,travel_mileage,cur_time,maintenance_time,alert_mileage,"
				+ "(t.maintenance_mileage-travel_mileage) remainingMileage,t.maintenance_threshold_time,"
				+ "to_char(cur_time,'yyyy-MM-dd') cur_time_f, t.threshold_month,to_char(maintenance_due_time,'yyyy-MM-dd') maintenanceDueTimeF, v.vehicle_number,v.vehicle_brand,v.vehicle_model,v.vehicle_identification,v.device_number,v.sim_number,"
				+ "v.ent_name vehicleSource,v.currentuse_org_id ownerOrgId, "
				+ " case when v.currentuse_org_id is NULL then v.ent_name "
				+ "     else (SELECT name from sys_organization where id = v.currentuse_org_id) "
				+ " end orgName "
				+ "from busi_vehicle_maintenance t "
				+ " left join busi_vehicle v on  t.vehicle_id=v.id "
				+ " left join sys_organization so on v.currentuse_org_id=so.id "
				+ " where 1=1";
		
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotBlank(model.getVehicleNumber())){
			sql += " and v.vehicle_number like "+SqlUtil.processLikeInjectionStatement( model.getVehicleNumber().toUpperCase());
		}
		StringBuffer orgIdsStr = new StringBuffer();
	    for(Long id : orgIdList){
	    	orgIdsStr.append(id+",");
	    }
	    if(orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
		if (model.getIncludeSelf()) {
			sql +="  and ((v.ent_id=? and v.currentuse_org_id is null) or v.currentuse_org_id in ("+orgIdsStr+"))";
			params.add(model.getDeptId());
		}else{
			sql +="  and v.currentuse_org_id in ("+orgIdsStr+")";
		}
		
		String searchScope=model.getSearchScope();
		
		if (StringUtils.isNotBlank(searchScope)) {
			if ("-1".equals(searchScope)) {
				// 已过期
				sql += " and Date(maintenance_due_time)-current_date < 0";
			} else if (searchScope.toUpperCase().endsWith("M")) {
				// 次月到期
				String nextMonth = searchScope.substring(0, searchScope.length()-1);
				sql += " and to_char(current_date::timestamp + '" + nextMonth + " month', 'yyyy-MM') = to_char(maintenance_due_time, 'yyyy-MM')";
			} else {
				// XX天内到期
				sql += " and Date(maintenance_due_time)-current_date <= " + searchScope + " and Date(maintenance_due_time)-current_date >= 0";
			}
		}
		sql += " order by (case when v.currentuse_org_id is null then 0 else so.orgindex end), so.orgindex ASC,t.id desc";
		Pagination page=new Pagination(sql, model.getCurrentPage(), model.getNumPerPage(),VehicleMaintenance.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Vehicle> listVehicleBySql(String condition) {
		String sql="select *  from busi_vehicle v  where  1=1"+condition+" order by id asc";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Vehicle.class));
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public List<String> list(Long entId, String json) {
		String sql="select CONCAT_WS(',',coalesce(v.vehicle_number,''),coalesce(v.vehicle_brand,''),coalesce(v.vehicle_model,'')," 
//	            + "coalesce(v.currentuse_org_name,''),"
	            + "coalesce( " 
                + "case when t1.currentuse_org_id is NULL then '未分配'  "
	            + "    else (SELECT name from sys_organization where id = t1.currentuse_org_id) "
                + " end arrangedOrgName "
	            + ",''),"
				+ "coalesce(t.header_latest_mileage,0),coalesce(t.header_last_mileage,0),coalesce(t.maintenance_mileage,0),coalesce((t.header_latest_mileage-t.header_maintenance_mileage),0),"
				+ "coalesce((t.maintenance_mileage-(t.header_latest_mileage-t.header_maintenance_mileage)),0),coalesce(to_char(cur_time,'yyyy-MM-dd hh24:mi:ss')),coalesce(t.maintenance_time,0),'')"
				+ "  from busi_vehicle_maintenance t,busi_vehicle v "
//				+ " where  t.vehicle_id=v.id and (v.ent_id=? or v.currentuse_org_id in(select id from sys_organization where parent_id=?))";
				+ " where  t.vehicle_id=v.id and v.ent_id=?";
		List<Object> params = new ArrayList<Object>();
		params.add(entId);
//		params.add(entId);
		Map<String, Object> jsonMap = null;
		if (StringUtils.isNotBlank(json)) {
			jsonMap = JsonUtils.json2Object(json, Map.class);
			String vehicleNumber = TypeUtils.obj2String(jsonMap.get("vehicleNumber"));
			Long deptId = TypeUtils.obj2Long(jsonMap.get("deptId"));
			String ids=TypeUtils.obj2String(jsonMap.get("ids"));//批量导出时所选的ID
			if(StringUtils.isNotBlank(ids)){
				String[] idarr=ids.split(",");
				String preparams="";
				for(int i=0,num=idarr.length;i<num;i++){
					if(i==num-1){
						preparams+="?";
					}else{
						preparams+="?,";
					}
					params.add(TypeUtils.obj2Long(idarr[i]));
				}
				sql += " and t.id in ("+preparams+")";
			}
			if(StringUtils.isNotBlank(vehicleNumber)){
				sql += " and v.vehicle_number=?";
				params.add(vehicleNumber);
			}
			if(deptId!=0){
				sql += " and v.currentuse_org_id=?";
				params.add(deptId);
			}
		}
		sql += " order by t.id asc";
		
		return jdbcTemplate.queryForList(sql, String.class, params.toArray());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<VehicleMaintenance> listBySql(String condition,List<Object> params) {
		String sql="select t.*,(t.header_latest_mileage-t.header_maintenance_mileage) oldMileage,(t.maintenance_mileage-(t.header_latest_mileage-t.header_maintenance_mileage)) remainingMileage,"
				+ "to_char(cur_time,'yyyy-MM-dd hh24:mi:ss') cur_time_f, v.vehicle_number,v.vehicle_brand,v.vehicle_model,v.vehicle_identification,v.device_number,v.sim_number,"
				+ "v.rent_name||v.ent_name vehicleSource,v.currentuse_org_id ownerOrgId,v.currentuse_org_name orgName from busi_vehicle_maintenance t,busi_vehicle v "
				+ " where 1=1 and t.vehicle_id = v.id ";
		if (StringUtils.isNotBlank(condition)) {
			sql +=condition;
		}
		sql += " order by t.id asc";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleMaintenance.class), params.toArray());
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public VehicleMaintenance setThreshold(VehicleMaintenance maintenance) {
		String sql = "update busi_vehicle_maintenance set alert_mileage = ?, threshold_month = ?, "
				+ " maintenance_threshold_time  = ? "
				+ " where id = ?";
		jdbcTemplate.update(sql, maintenance.getMaintenanceMileage(), maintenance.getThresholdMonth(), new java.sql.Timestamp(maintenance.getMaintenanceThresholdTime().getTime()) , maintenance.getId());
		return maintenance;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<VehicleMaintenance> queryExportList(Long entId, String json) {
		String sql="select t.id,t.vehicle_id,(t.header_maintenance_mileage+travel_mileage) header_latest_mileage, "
				+ "header_maintenance_mileage,maintenance_mileage,travel_mileage,cur_time,maintenance_time,"
				+ "(t.maintenance_mileage-travel_mileage) remainingMileage,t.maintenance_threshold_time,alert_mileage,"
				+ "to_char(cur_time,'yyyy-MM-dd') cur_time_f, v.vehicle_number,v.vehicle_brand,v.vehicle_model,v.vehicle_identification,v.device_number,v.sim_number,"
				+ "v.rent_name||v.ent_name vehicleSource,v.currentuse_org_id ownerOrgId,v.currentuse_org_name orgName from busi_vehicle_maintenance t,busi_vehicle v "
//				+ " where  t.vehicle_id=v.id and (v.ent_id=? or v.currentuse_org_id in(select id from sys_organization where parent_id=?))";
				+ " where  t.vehicle_id=v.id and v.ent_id=?";
		List<Object> params = new ArrayList<Object>();
		params.add(entId);
//		params.add(entId);
		Map<String, Object> jsonMap = null;
		if (StringUtils.isNotBlank(json)) {
			jsonMap = JsonUtils.json2Object(json, Map.class);
			String vehicleNumber = TypeUtils.obj2String(jsonMap.get("vehicleNumber"));
			Long deptId = TypeUtils.obj2Long(jsonMap.get("deptId"));
			String ids=TypeUtils.obj2String(jsonMap.get("ids"));//批量导出时所选的ID
			if(StringUtils.isNotBlank(ids)){
				String[] idarr=ids.split(",");
				String preparams="";
				for(int i=0,num=idarr.length;i<num;i++){
					if(i==num-1){
						preparams+="?";
					}else{
						preparams+="?,";
					}
					params.add(TypeUtils.obj2Long(idarr[i]));
				}
				sql += " and t.id in ("+preparams+")";
			}
			if(StringUtils.isNotBlank(vehicleNumber)){
				sql += " and v.vehicle_number=?";
				params.add(vehicleNumber);
			}
			if(deptId!=0){
				sql += " and v.currentuse_org_id=?";
				params.add(deptId);
			}
		}
		sql += " order by t.id desc";
		
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleMaintenance.class), params.toArray());
	}
	
	//CURTIMEWARN 0  即将达到;1 已经达到
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<VehicleMaintenance> queryMaintenanceMileageAlert() {
		StringBuilder sql = new StringBuilder();
//		sql.append("SELECT T.ID, (SELECT TT.VEHICLE_NUMBER FROM BUSI_VEHICLE TT WHERE TT.ID = T.VEHICLE_ID) VEHICLENUMBER, ")
//		   .append("(SELECT TT.ENT_ID FROM BUSI_VEHICLE TT WHERE TT.ID = T.VEHICLE_ID) OWNERORGID, ")
//		   .append("T.VEHICLE_ID,T.ALERT_MILEAGE_WARN,1 CURTIMEWARN, ")
//		   .append("T.MAINTENANCE_MILEAGE-(T.HEADER_LATEST_MILEAGE-T.HEADER_MAINTENANCE_MILEAGE) REMAININGMILEAGE ")
//		   .append("FROM BUSI_VEHICLE_MAINTENANCE T ")
//		   .append("WHERE (T.MAINTENANCE_MILEAGE-(T.HEADER_LATEST_MILEAGE-T.HEADER_MAINTENANCE_MILEAGE)) < T.ALERT_MILEAGE ")
//		   .append("AND (T.MAINTENANCE_MILEAGE-(T.HEADER_LATEST_MILEAGE-T.HEADER_MAINTENANCE_MILEAGE)) <=0 AND T.ALERT_MILEAGE_WARN <> 2")
//		   .append("UNION ")
//		   .append("SELECT T.ID, (SELECT TT.VEHICLE_NUMBER FROM BUSI_VEHICLE TT WHERE TT.ID = T.VEHICLE_ID) VEHICLENUMBER, ")
//		   .append("(SELECT TT.ENT_ID FROM BUSI_VEHICLE TT WHERE TT.ID = T.VEHICLE_ID) OWNERORGID, ")
//		   .append("T.VEHICLE_ID,T.ALERT_MILEAGE_WARN,0 CURTIMEWARN, ")
//		   .append("T.MAINTENANCE_MILEAGE-(T.HEADER_LATEST_MILEAGE-T.HEADER_MAINTENANCE_MILEAGE) REMAININGMILEAGE ")
//		   .append("FROM BUSI_VEHICLE_MAINTENANCE T ")
//		   .append("WHERE (T.MAINTENANCE_MILEAGE-(T.HEADER_LATEST_MILEAGE-T.HEADER_MAINTENANCE_MILEAGE)) <= T.ALERT_MILEAGE ")
//		   .append("AND (T.MAINTENANCE_MILEAGE-(T.HEADER_LATEST_MILEAGE-T.HEADER_MAINTENANCE_MILEAGE)) > 0 AND T.ALERT_MILEAGE_WARN = 0");
		
		sql.append("SELECT TT.VEHICLE_NUMBER,TT.ENT_ID OWNERENTID,T.VEHICLE_ID,TT.CURRENTUSE_ORG_ID OWNERORGID,  ")
		   .append("T.ALERT_MILEAGE_WARN,1 CURTIMEWARN, ")
		   .append("(T.maintenance_mileage-T.travel_mileage) REMAININGMILEAGE ")
		   .append("FROM BUSI_VEHICLE_MAINTENANCE T, BUSI_VEHICLE TT ")
		   .append("WHERE (T.maintenance_mileage-T.travel_mileage) < T.ALERT_MILEAGE ")
		   .append("AND (T.maintenance_mileage-T.travel_mileage) <=0 AND T.ALERT_MILEAGE_WARN <> 2")
		   .append("AND T.VEHICLE_ID = TT.ID ")
		   .append("UNION ")
		   .append("SELECT TT.VEHICLE_NUMBER,TT.ENT_ID OWNERENTID,T.VEHICLE_ID,TT.CURRENTUSE_ORG_ID OWNERORGID,  ")
		   .append("T.ALERT_MILEAGE_WARN,0 CURTIMEWARN, ")
		   .append("(T.maintenance_mileage-T.travel_mileage) REMAININGMILEAGE ")
		   .append("FROM BUSI_VEHICLE_MAINTENANCE T, BUSI_VEHICLE TT  ")
		   .append("WHERE (T.maintenance_mileage-T.travel_mileage) <= T.ALERT_MILEAGE ")
		   .append("AND (T.maintenance_mileage-T.travel_mileage) > 0 AND T.ALERT_MILEAGE_WARN = 0")
		   .append("AND T.VEHICLE_ID = TT.ID ");
		return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper(VehicleMaintenance.class));
	}
	
	//ALERTMILEAGEWARN 0  即将达到;1 已经达到
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<VehicleMaintenance> queryMaintenanceTimeAlert() {
		List<Object> params = new ArrayList<Object>();
		Date nextMonth = TimeUtils.countDateByMonth(new Date(), 1);
		params.add(nextMonth);
		
		StringBuilder sql = new StringBuilder();
//		sql.append("select T.ID, (select tt.vehicle_number from busi_vehicle tt where tt.id = t.vehicle_id) vehicleNumber, ")
//		   .append("       (select tt.ent_id from busi_vehicle tt where tt.id = t.vehicle_id) ownerOrgId,t.vehicle_id, ")
//		   .append("1 alertMileageWarn,cur_Time_Warn,t.maintenance_due_time ")
//		   .append("from busi_vehicle_maintenance T ")
//		   .append("where now() > t.maintenance_due_time and t.cur_time_warn <> 2 ")
//		   .append("UNION ")
//		   .append("select T.ID, (select tt.vehicle_number from busi_vehicle tt where tt.id = t.vehicle_id) vehicleNumber, ")
//		   .append("       (select tt.ent_id from busi_vehicle tt where tt.id = t.vehicle_id) ownerOrgId,t.vehicle_id, ")
//		   .append("0 alertMileageWarn,cur_Time_Warn,t.maintenance_due_time ")
//		   .append("from busi_vehicle_maintenance t ")
//		   .append("where ? >= t.maintenance_due_time and cur_time_warn = 0 ")
//		   .append("and now() <= t.maintenance_due_time ");
		   sql.append("SELECT TT.VEHICLE_NUMBER,TT.ENT_ID OWNERENTID,T.VEHICLE_ID,TT.CURRENTUSE_ORG_ID OWNERORGID,  ")
		   .append("1 alertMileageWarn,cur_Time_Warn,t.maintenance_due_time ")
		   .append("FROM BUSI_VEHICLE_MAINTENANCE T, BUSI_VEHICLE TT ")
		   .append("where now() > T.MAINTENANCE_DUE_TIME and t.cur_time_warn <> 2 ")
		   .append("AND T.VEHICLE_ID = TT.ID ")
		   .append("UNION ")
		   .append("SELECT TT.VEHICLE_NUMBER,TT.ENT_ID OWNERENTID,T.VEHICLE_ID,TT.CURRENTUSE_ORG_ID OWNERORGID,  ")
		   .append("0 alertMileageWarn,cur_Time_Warn,t.maintenance_due_time ")
		   .append("FROM BUSI_VEHICLE_MAINTENANCE T, BUSI_VEHICLE TT ")
		   .append("where now() >= T.MAINTENANCE_THRESHOLD_TIME and cur_time_warn = 0 ")
		   .append("and now() <= T.MAINTENANCE_DUE_TIME ")
		   .append("AND T.VEHICLE_ID = TT.ID ");
		return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper(VehicleMaintenance.class));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean modifyJobStatus(String filedName, int filedValue, Long id) {
		StringBuilder sql = new StringBuilder();
		sql.append("update busi_vehicle_maintenance set ")
		   .append(filedName + " = ? where vehicle_id = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(filedValue);
		params.add(id);
		jdbcTemplate.update(sql.toString(), params.toArray());
		return true;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<VehicleMaintenance> querySysnTravelMileageVehicleList() {
		StringBuilder sql = new StringBuilder();
		sql.append("select t1.vehicle_id,t1.cur_time,t2.vehicle_number,t2.device_number,to_char(t1.update_time, 'yyyy-mm-dd hh24:mi:ss') updateTimeF ")
		   .append(" from busi_vehicle_maintenance t1, busi_vehicle t2 ")
		   .append(" where t1.vehicle_id = t2.id ")
		   .append(" and t2.device_number is not null and t2.device_number <> '' ")
		   .append(" and t1.update_time < now() ");
		return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper(VehicleMaintenance.class));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void modifyMaintenanceJobTime(Long travelMileage,Date updateTime, Long vehicleId) {
		String sql = "update busi_vehicle_maintenance set update_time = ?, travel_mileage = travel_mileage+? where vehicle_id = ?";
		jdbcTemplate.update(sql, new java.sql.Timestamp(updateTime.getTime()), travelMileage, vehicleId);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Vehicle> listVehicleByEnt(Long orgId,List<Long> orgIds) {
		StringBuffer orgIdsStr = new StringBuffer();
	    for(Long id : orgIds){
	    	orgIdsStr.append(id+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
		List<Object> params = new ArrayList<Object>();
		String sql = "select t.* from busi_vehicle t "
                   + "where t.ent_id=? or t.currentuse_org_id in (" + orgIdsStr + ")";
		params.add(orgId);
		List<Vehicle> rList = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper(Vehicle.class),params.toArray());
		if(rList != null && rList.size() > 0) {
			return rList;
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Vehicle> listVehicleByDep(List<Long> orgIds) {
		StringBuffer orgIdsStr = new StringBuffer();
	    for(Long id : orgIds){
	    	orgIdsStr.append(id+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
		String sql = "select t.* from busi_vehicle t "
				   + "where t.currentuse_org_id in (" + orgIdsStr+ ")";
		List<Vehicle> rList = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper(Vehicle.class));
		if(rList != null && rList.size() > 0) {
			return rList;
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public VehicleDataCountModel queryVehicleDataCountByHomePage(String queryDate, String vehicleIdList) {
		String sql = "select "
				   + "(select count(0) from busi_vehicle_maintenance "
				   + "where to_char(maintenance_due_time,'yyyy-MM') = ? "
				   + "and vehicle_id in (" + vehicleIdList + ")) "
				   + "maintenance_count, "
				   + "(select count(0) from busi_vehicle "
				   + "where to_char( insurance_expiredate,'yyyy-MM') = ? and insurance_expiredate is not null " 
				   + "and id in (" + vehicleIdList + ")) "
				   + "insurance_count, "
				   + "(select count(0) from vehicle_annual_inspection "
				   + "where to_char(inspection_next_time,'yyyy-MM') = ? "
				   + "and vehicle_id in (" + vehicleIdList + ")) "
				   + "inspection_count";
		return (VehicleDataCountModel)jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(VehicleDataCountModel.class), queryDate, queryDate, queryDate);
	}
	
	@SuppressWarnings("unchecked")
	public PagModel listVehicleMaintenancePage(MaintenanceListModel vModel) {
			String sql="select t.id,t.vehicle_id,(t.header_maintenance_mileage+travel_mileage) header_latest_mileage, "
					+ "header_maintenance_mileage,maintenance_mileage,travel_mileage,cur_time,maintenance_time,alert_mileage,"
					+ "(t.maintenance_mileage-travel_mileage) remainingMileage,t.maintenance_threshold_time,"
					+ "to_char(cur_time,'yyyy-MM-dd') cur_time_f, t.threshold_month,to_char(maintenance_due_time,'yyyy-MM-dd') maintenanceDueTimeF, v.vehicle_number,v.vehicle_brand,v.vehicle_model,v.vehicle_identification,v.device_number,v.sim_number,"
					+ "v.rent_name||v.ent_name vehicleSource,v.currentuse_org_id ownerOrgId, "
					+ " case when v.currentuse_org_id is NULL then '未分配' "
					+ "     else (SELECT name from sys_organization where id = v.currentuse_org_id) "
					+ " end orgName "
					+ "from busi_vehicle_maintenance t,busi_vehicle v "
					+ " where  t.vehicle_id=v.id and v.ent_id=?";
			List<Object> params = new ArrayList<Object>();
			params.add(vModel.getOrganizationId());
			if(StringUtils.isNotBlank(vModel.getVehicleNumber())){
				sql += " and vehicle_number like "+SqlUtil.processLikeInjectionStatement( vModel.getVehicleNumber().toUpperCase());
			}
			if(vModel.getDeptId()!=0){
				sql += " and v.currentuse_org_id=?";
				params.add(vModel.getDeptId());
			}
			String searchScope = vModel.getSearchScope();
			if (StringUtils.isNotBlank(searchScope)) {
				if ("-1".equals(searchScope)) {
					// 已过期
					sql += " and Date(maintenance_due_time)-current_date < 0";
				} else if (searchScope.toUpperCase().endsWith("M")) {
					// 次月到期
					String nextMonth = searchScope.substring(0, searchScope.length()-1);
					sql += " and to_char(current_date::timestamp + '" + nextMonth + " month', 'yyyy-MM') = to_char(maintenance_due_time, 'yyyy-MM')";
				} else {
					// XX天内到期
					sql += " and Date(maintenance_due_time)-current_date <= " + searchScope + " and Date(maintenance_due_time)-current_date >= 0";
				}
			}
			sql += " order by t.id desc";
			Pagination page=new Pagination(sql, vModel.getCurrentPage(), vModel.getNumPerPage(),VehicleMaintenance.class,jdbcTemplate, params.toArray());
			return page.getResult();
	}

	@Override
	public List<VehicleMaintenance> queryVehicleMaintenanceExportList(Long entId, String vehicleNumber,Long deptId,String ids) {
		String sql="select t.id,t.vehicle_id,(t.header_maintenance_mileage+travel_mileage) header_latest_mileage, "
				+ "header_maintenance_mileage,maintenance_mileage,travel_mileage,cur_time,maintenance_time,"
				+ "(t.maintenance_mileage-travel_mileage) remainingMileage,t.maintenance_threshold_time,alert_mileage,"
				+ "to_char(cur_time,'yyyy-MM-dd') cur_time_f, v.vehicle_number,v.vehicle_brand,v.vehicle_model,v.vehicle_identification,v.device_number,v.sim_number,"
				+ "v.rent_name||v.ent_name vehicleSource,v.currentuse_org_id ownerOrgId,v.currentuse_org_name orgName from busi_vehicle_maintenance t,busi_vehicle v "
				+ " where  t.vehicle_id=v.id and v.ent_id=?";
		List<Object> params = new ArrayList<Object>();
		params.add(entId);
		if(StringUtils.isNotBlank(ids)){
			String[] idarr=ids.split(",");
			String preparams="";
			for(int i=0,num=idarr.length;i<num;i++){
				if(i==num-1){
					preparams+="?";
				}else{
					preparams+="?,";
				}
				params.add(TypeUtils.obj2Long(idarr[i]));
			}
			sql += " and t.id in ("+preparams+")";
		}
		if(StringUtils.isNotBlank(vehicleNumber)){
			sql += " and v.vehicle_number=?";
			params.add(vehicleNumber);
		}
		if(deptId!=0){
			sql += " and v.currentuse_org_id=?";
			params.add(deptId);
		}
		sql += " order by t.id desc";
		
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(VehicleMaintenance.class), params.toArray());
	}

	@Override
	public List<UsageReportSQLModel> getVehicleMileageStasticByImeiAndBeginTime(Date curTime, String deviceNumber, String vehicleNumber) {
		List<UsageReportSQLModel> retList = null;
		List<Object> params = new ArrayList<Object>();
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("     select sum(total_mileage) as total_mileage ");
	    buffer.append("		from busi_vehicle_stastic");
	    buffer.append("		where"); 
	    buffer.append("		last_updated_time >= ?");
	    buffer.append("		and device_number=? ");
	    buffer.append("		and vehicle_number=? ");
	    
	    params.add(curTime);
	    params.add(deviceNumber);
	    params.add(vehicleNumber);
	    retList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(UsageReportSQLModel.class),params.toArray());
		return retList;
		
	}

	@Override
	public void modifyJobTravelMileageAndUpdateTime(Long travelMileage, Date updateTime, Long vehicleId) {
		String sql = "update busi_vehicle_maintenance set update_time = ?, travel_mileage = ? where vehicle_id = ?";
		jdbcTemplate.update(sql, new java.sql.Timestamp(updateTime.getTime()), travelMileage, vehicleId);
	}
}
