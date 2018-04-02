package com.cmdt.carrental.common.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.entity.EventConfig;
import com.cmdt.carrental.common.entity.Marker;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.Rent;
import com.cmdt.carrental.common.entity.VehicleAlert;
import com.cmdt.carrental.common.model.AlertCountModel;
import com.cmdt.carrental.common.model.AlertStatSQLModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.QueryAlertInfoModel;
import com.cmdt.carrental.common.model.StationModel;
import com.cmdt.carrental.common.model.VehicleAlertQueryDTO;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.util.Pagination;
import com.cmdt.carrental.common.util.SqlUtil;

@Repository
public class VehicleAlertDaoImpl implements VehicleAlertDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * 查找报警数据
	 */
	@Override
	public List<VehicleAlert> findVehicleAlert(VehicleAlertQueryDTO vehicleAlertQueryDTO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * from busi_vehicle_alert where 1=1 ");
		
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(vehicleAlertQueryDTO.getVehicleNumber())){
			sb.append(" and vehicle_number like "+SqlUtil.processLikeInjectionStatement(vehicleAlertQueryDTO.getVehicleNumber()));
		}
		if(StringUtils.isNotEmpty(vehicleAlertQueryDTO.getVehicleType())){
			sb.append(" and vehicle_type=?");
			params.add(vehicleAlertQueryDTO.getVehicleType());
		}
		if(StringUtils.isNotEmpty(vehicleAlertQueryDTO.getVehicleSource())){
			sb.append(" and vehicle_source=?");
			params.add(vehicleAlertQueryDTO.getVehicleSource());
		}
		if(StringUtils.isNotEmpty(vehicleAlertQueryDTO.getOrganizationName())){
			sb.append(" and organization_name=?");
			params.add(vehicleAlertQueryDTO.getOrganizationName());
		}
		if(vehicleAlertQueryDTO.getStartTime() != null){
			sb.append(" and alert_time>=?");
			params.add(new java.sql.Timestamp(vehicleAlertQueryDTO.getStartTime().getTime()));
		}
		if(vehicleAlertQueryDTO.getEndTime() != null){
			sb.append(" and alert_time<=?");
			params.add(new java.sql.Timestamp(vehicleAlertQueryDTO.getEndTime().getTime()));
		}
		if(StringUtils.isNotEmpty(vehicleAlertQueryDTO.getAlertType())){
			sb.append(" and alert_type=?");
			params.add(vehicleAlertQueryDTO.getAlertType());
		}
		sb.append(" order by alert_time desc ");
		List<VehicleAlert> alertList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<VehicleAlert>(VehicleAlert.class), params.toArray() );
        if(alertList == null || alertList.size() == 0) {
            return null;
        }
        return alertList;
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean isRentCompany(Long id) {
		List<Rent> rentList = jdbcTemplate.query("select id from sys_organization where id =? and enterprisestype='0'", new BeanPropertyRowMapper(Rent.class),id);
		if(rentList != null && rentList.size()>0){
			return true;
		}
		return false;
	}

	public List<Marker> findMarker(String vehicleNumber) {
		String sql = "SELECT a.* from busi_marker a JOIN busi_vehicle_marker d ON d.marker_id = a.id JOIN busi_vehicle c ON c.id = d.vehicle_id where c.vehicle_number = ?";
		List<Marker> markerList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Marker>(Marker.class),vehicleNumber);
        if(markerList == null || markerList.isEmpty()) {
            return null;
        }
        return markerList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public VehicleModel getVehicleByVehicleNumber(Long entId,String vehicleNumber) {
		List<VehicleModel> rentList = jdbcTemplate.query("select * from busi_vehicle where vehicle_number=?", new BeanPropertyRowMapper(VehicleModel.class),vehicleNumber);
		if(rentList != null && rentList.size() > 0){
			VehicleModel vehicleModel = rentList.get(0);
			
			//车辆来源
			if(vehicleModel.getRentId() != null){
				vehicleModel.setVehicleFromId(vehicleModel.getRentId());
				vehicleModel.setVehicleFromName(vehicleModel.getRentName());
			}else{
				vehicleModel.setVehicleFromId(vehicleModel.getEntId());
				vehicleModel.setVehicleFromName(vehicleModel.getEntName());
			}
			
			//所属部门
			//企业创建的车辆
			if(entId.equals(vehicleModel.getVehicleFromId())){
				//还没分配给部门
				if(vehicleModel.getCurrentuseOrgId() == null){
					vehicleModel.setArrangedOrgId(null);
					vehicleModel.setArrangedOrgName("未分配");
				}else{//已经分配给部门
					vehicleModel.setArrangedOrgId(vehicleModel.getCurrentuseOrgId());
					vehicleModel.setArrangedOrgName(vehicleModel.getCurrentuseOrgName());
				}
			}else{//租户创建的车辆
				if(entId.equals(vehicleModel.getCurrentuseOrgId())){//租户创建车辆，分配给了企业，企业还没有分配给部门
					vehicleModel.setArrangedOrgId(null);
					vehicleModel.setArrangedOrgName("未分配");
				}else{//已经分配给部门
					vehicleModel.setArrangedOrgId(vehicleModel.getCurrentuseOrgId());
					vehicleModel.setArrangedOrgName(vehicleModel.getCurrentuseOrgName());
				}
			}
			return vehicleModel;
		}
		return null;
	}

	@Override
	public List<StationModel> findStation(String vehicleNumber) {
		String sql="select c.* from busi_vehicle a join busi_vehicle_station b on a.id=b.vehicle_id join busi_station c on c.id=b.station_id where a.vehicle_number=?";
		List<StationModel> stationList= jdbcTemplate.query(sql, new BeanPropertyRowMapper<StationModel>(StationModel.class), vehicleNumber);
		return stationList;
	}
	
	@Override
	public PagModel findAlertByPage(int currentPage, int numPerPage, Long rentId, boolean isEnt) {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.*,b.name as currentuseOrgName from busi_vehicle_alert a ");
		sql.append(" join sys_organization b on a.currentuse_org_id=b.id ");
		sql.append(" where 1=1 and to_char(a.alert_time,'yyyy-MM-dd') = to_char(CURRENT_DATE,'yyyy-MM-dd') ");
		List<Object> params = new ArrayList<Object>();
		params.add(rentId);
		if(isEnt){
			//sql.append(" and a.currentuse_org_id in (select id from sys_organization where parent_id =?)");
			sql.append(" and b.parent_id =?");
		}else{
			sql.append(" and a.currentuse_org_id =?");
			
		}
		sql.append(" order by a.id desc");
		Pagination page=new Pagination(sql.toString(), currentPage, numPerPage,VehicleAlert.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}

	@Override
	public  List<VehicleAlert> findTodayAlert(Long orgId, List<Organization> orgList,boolean isPre) {
		

	    StringBuffer orgIdsStr = new StringBuffer();
	    
	    for(Organization org : orgList){
	    	orgIdsStr.append(org.getId()+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
		
		String sql = "select * from ( select *,(tmp.colType % 2) as type from ( ";
        sql += "select row_number() over(order by a.alert_time DESC) colType, a.*,b.name as currentuseOrgName from busi_vehicle_alert a  join sys_organization b on a.currentuse_org_id=b.id  where 1=1 and to_char(a.alert_time,'yyyy-MM-dd') = to_char(CURRENT_DATE,'yyyy-MM-dd') ";
 
        sql+=" and (a.currentuse_org_id in ("+orgIdsStr+") or (a.currentuse_org_id is null and a.ent_id = ?) or a.currentuse_org_id = ?)";
		
		List<VehicleAlert> vehicleAlertPre = null;
		List<VehicleAlert> vehicleAlertNext = null;
		if(isPre){
			sql += " ) tmp ) tmp_val where tmp_val.type = 1";
			vehicleAlertPre= jdbcTemplate.query(sql, new BeanPropertyRowMapper<VehicleAlert>(VehicleAlert.class),orgId,orgId);
			return vehicleAlertPre;
		}else{
			sql += " ) tmp ) tmp_val where tmp_val.type = 0";
			vehicleAlertNext= jdbcTemplate.query(sql, new BeanPropertyRowMapper<VehicleAlert>(VehicleAlert.class),orgId,orgId);
			return vehicleAlertNext;
		}
	}
	
	
	@Override
	public List<AlertCountModel> findAlertCountList(Long orgId,List<Organization> orgList) {
		
		 StringBuffer orgIdsStr = new StringBuffer();
		    
		    for(Organization org : orgList){
		    	orgIdsStr.append(org.getId()+",");
		    }
		    if(orgIdsStr!=null&&orgIdsStr.length()>0){
		    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
		    }
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.alert_type,count(id) as value from busi_vehicle_alert a  ");
		sql.append(" where 1=1  ");
		sql.append(" and ");
		sql.append(" (a.alert_type in ('OVERSPEED','OUTBOUND') and to_char(a.alert_time,'yyyy-MM-dd') = to_char(CURRENT_DATE,'yyyy-MM-dd') ");
		sql.append(" or (to_char(a.alert_time,'yyyy-MM-dd') = to_char(CURRENT_DATE - 1,'yyyy-MM-dd') and alert_type in ('VEHICLEBACK'))) ");
        sql.append(" and (a.currentuse_org_id in ("+orgIdsStr+") or (a.currentuse_org_id is null and a.ent_id = ?) or a.currentuse_org_id = ?)");
		sql.append(" group by a.alert_type");
		return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<AlertCountModel>(AlertCountModel.class),orgId,orgId);
	}
	

	public List<StationModel> queryStationNameByVehicleNumber(String vehicleNumber) {
		StringBuilder sb =new StringBuilder();
		sb.append("select station_name from busi_station a join busi_vehicle_station b on a.id=b.station_id ");
		sb.append("join busi_vehicle c on c.id=b.vehicle_id where vehicle_number=?");
		return jdbcTemplate.query(sb.toString(),new BeanPropertyRowMapper<StationModel>(StationModel.class), vehicleNumber);
	}

	@Override
	public List<AlertStatSQLModel> getDayAlertStat(Date startDate, Date endDate, List<Organization> orgList,Long orgId,Boolean self) {
		List<Object> params = new ArrayList<Object>();

		    StringBuffer orgIdsStr = new StringBuffer();
		    
		    for(Organization org : orgList){
		    	orgIdsStr.append(org.getId()+",");
		    }
		    if(orgIdsStr!=null&&orgIdsStr.length()>0){
		    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
		    }
		
		StringBuilder sql = new StringBuilder();
		sql.append("select to_char(alert_time, 'YYYY-MM-DD') as tm, count(*) as ct from busi_vehicle_alert where  1=1  ");
		if (self) {
			sql.append("  and ((ent_id=? and currentuse_org_id is null) or currentuse_org_id in (").append(orgIdsStr).append(") or currentuse_org_id=?)");
			params.add(orgId);
			params.add(orgId);
		}else{
			sql.append("  and currentuse_org_id in (").append(orgIdsStr).append(") ");
		}
		
		sql.append("and alert_time between ? and ?  ");
		sql.append("group by to_char(alert_time, 'YYYY-MM-DD') ORDER BY tm ");
		params.add(startDate);
		params.add(endDate);
		
		List<AlertStatSQLModel> alertList = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<AlertStatSQLModel>(AlertStatSQLModel.class), params.toArray() );
        if(alertList == null || alertList.size() == 0) {
            return null;
        }
		
        return alertList;
	}

	@Override
	public List<AlertStatSQLModel> getDeptAlertStat(Date startDate, Date endDate, List<Organization> orgList,Long orgId,Boolean self) {
		List<Object> params = new ArrayList<Object>();
		
		 StringBuffer orgIdsStr = new StringBuffer();
		    
		    for(Organization org : orgList){
		    	orgIdsStr.append(org.getId()+",");
		    }
		    if(orgIdsStr!=null&&orgIdsStr.length()>0){
		    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
		    }
		StringBuilder sql = new StringBuilder();
		sql.append("select so.parent_id,so.id as orgId,so.name as tm,COALESCE(yy.ct,0) as ct from sys_organization so ");
		sql.append("left join ");
		sql.append("(select a.currentuse_org_id as orgid, count(*) as ct from busi_vehicle_alert a where 1=1 ");
		if (self) {
			sql.append("  and ((a.ent_id=? and a.currentuse_org_id is null) or a.currentuse_org_id in (").append(orgIdsStr).append(") or a.currentuse_org_id = ?)");
			params.add(orgId);
			params.add(orgId);
		}else{
			sql.append("  and a.currentuse_org_id in (").append(orgIdsStr).append(") ");
		}
		sql.append("and a.alert_time between ? and ? ");
		sql.append("group by a.currentuse_org_id order by ct desc) yy ");
		sql.append("on so.id = yy.orgid ");
		sql.append("where so.id in ("+orgIdsStr+") ");
		sql.append("order by yy.ct ");
		params.add(startDate);
		params.add(endDate);
		
		List<AlertStatSQLModel> alertList = jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<AlertStatSQLModel>(AlertStatSQLModel.class), params.toArray() );
        if(alertList == null || alertList.size() == 0) {
            return null;
        }
		
        return alertList;
	}

	@Override
	public List<VehicleAlert> findAllVehicleAlert(QueryAlertInfoModel model,List<Organization> orgList) {
		 StringBuffer orgIdsStr = new StringBuffer();
		    
		    for(Organization org : orgList){
		    	orgIdsStr.append(org.getId()+",");
		    }
		    if(orgIdsStr!=null&&orgIdsStr.length()>0){
		    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
		    }
		
		    StringBuilder sb = new StringBuilder();
			sb.append("select a.*,b.id as driverId,c.realname as driverName,c.phone as driverPhone,d.name as currentuseOrgName,f.Name as vehicleSource from busi_vehicle_alert a");
			sb.append(" left join sys_driver b on a.driver_id=b.id left join sys_user c on c .id=b.id left join sys_organization d on d.id=a.currentuse_org_id LEFT JOIN sys_organization f ON f. ID = a .ent_id");
			sb.append(" where 1=1 ");
			List<Object> params = new ArrayList<Object>();
			if(StringUtils.isNotEmpty(model.getVehicleNumber())){
				sb.append(" and a.vehicle_number like "+SqlUtil.processLikeInjectionStatement(model.getVehicleNumber().toUpperCase()));
			}
			if (model.getVehicleType()!=null&&model.getVehicleType().length()>0&&!model.getVehicleType().equals("-1")) {
				sb.append(" and a.vehicle_type=?");
				params.add(model.getVehicleType());
			}
			//-1代表车辆类型为所有的
			if(StringUtils.isNotEmpty(model.getAlertType())){
				sb.append(" and a.alert_type=?");
				params.add(model.getAlertType());
			}
			
			//车辆来源
			//-1代表该企业下所有部门的车辆
			if (model.getFromOrgId()!=null&&model.getFromOrgId() != -1L) {
				sb.append(" and a.ent_id=?");
				params.add(model.getFromOrgId());
			}
			
			if (model.getIncludeSelf()) {
				sb.append("  and ((a.ent_id=? and a.currentuse_org_id is null) or a.currentuse_org_id in (").append(orgIdsStr).append(") or a.currentuse_org_id = ?)");
				params.add(model.getDeptId());
				params.add(model.getDeptId());
			}else{
				sb.append("  and a.currentuse_org_id in (").append(orgIdsStr).append(")");
			}
			
			if(model.getStartTime() != null){
				sb.append(" and a.alert_time>=?");
				params.add(new java.sql.Timestamp(model.getStartTime().getTime()));
			}
			if(model.getEndTime() != null){
				sb.append(" and a.alert_time<=?");
				params.add(new java.sql.Timestamp(model.getEndTime().getTime()));
			}
			sb.append(" ORDER BY id DESC");
		
		List<VehicleAlert> vehicleAlertList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<VehicleAlert>(VehicleAlert.class),params.toArray());
		if(vehicleAlertList != null && vehicleAlertList.size() > 0){
			return vehicleAlertList;
		}
		return null;
	}

	@Override
	public PagModel findVehicleAlertInfo(QueryAlertInfoModel model, List<Long> orgIdList) {
		StringBuilder sb = new StringBuilder();
		sb.append("select a.*,b.id as driverId,c.realname as driverName,c.phone as driverPhone,d.name as currentuseOrgName,f.Name as vehicleSource from busi_vehicle_alert a");
		sb.append(" left join sys_driver b on a.driver_id=b.id left join sys_user c on c .id=b.id left join sys_organization d on d.id=a.currentuse_org_id LEFT JOIN sys_organization f ON f. ID = a .ent_id");
		sb.append(" where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(model.getVehicleNumber())){
			sb.append(" and a.vehicle_number like "+SqlUtil.processLikeInjectionStatement(model.getVehicleNumber().toUpperCase()));
		}
		if (!model.getVehicleType().equals("-1")) {
			sb.append(" and a.vehicle_type=?");
			params.add(model.getVehicleType());
		}
		//-1代表车辆类型为所有的
		if(StringUtils.isNotEmpty(model.getAlertType())){
			sb.append(" and a.alert_type=?");
			params.add(model.getAlertType());
		}
		
		//车辆来源
		//-1代表该企业下所有部门的车辆
		if (model.getFromOrgId() != -1L) {
			sb.append(" and a.ent_id=?");
			params.add(model.getFromOrgId());
		}
		StringBuffer orgIdsStr = new StringBuffer();
	    for(Long id : orgIdList){
	    	orgIdsStr.append(id+",");
	    }
	    if(orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
		if (model.getIncludeSelf()) {
			sb.append("  and ((a.ent_id=? and a.currentuse_org_id is null) or a.currentuse_org_id in (").append(orgIdsStr).append(") or a.currentuse_org_id = ?)");
			params.add(model.getDeptId());
			params.add(model.getDeptId());
		}else{
			sb.append("  and a.currentuse_org_id in (").append(orgIdsStr).append(")");
		}
		
		if(model.getStartTime() != null){
			sb.append(" and a.alert_time>=?");
			params.add(new java.sql.Timestamp(model.getStartTime().getTime()));
		}
		if(model.getEndTime() != null){
			sb.append(" and a.alert_time<=?");
			params.add(new java.sql.Timestamp(model.getEndTime().getTime()));
		}
		sb.append(" ORDER BY (case when A.currentuse_org_id is null then 0 else d.orgindex end), d.orgindex ASC, a.alert_time DESC");
		Pagination page=new Pagination(sb.toString(), model.getCurrentPage(), model.getNumPerPage(),VehicleAlert.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}


	@Override
	public List<EventConfig> findAlarmConfig(Long entId) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ent_id,event_type,enable,update_time from busi_event_config where ent_id = ? and event_type in ('OVERSPEED','OUTBOUND','VEHICLEBACK','VIOLATE')");
		List<EventConfig> events = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<EventConfig>(EventConfig.class),entId);
		return events;
	}


	@Override
	public void updateAlarmConfig(Long entId, String eventType, Boolean isEnable) {
		StringBuilder sb = new StringBuilder();
		sb.append("update busi_event_config set enable = ? where ent_id = ? and event_type=?");
		jdbcTemplate.update(sb.toString(), isEnable,entId,eventType);
	}
}
