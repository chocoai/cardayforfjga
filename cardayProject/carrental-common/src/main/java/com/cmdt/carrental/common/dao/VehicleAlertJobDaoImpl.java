package com.cmdt.carrental.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.bean.AlertType;
import com.cmdt.carrental.common.entity.EventConfig;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.model.AlertDiagramStatisticModel;
import com.cmdt.carrental.common.model.AlertStatisticModel;
import com.cmdt.carrental.common.model.CountModel;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.MarkerModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.StationModel;
import com.cmdt.carrental.common.model.StationsDurationModel;
import com.cmdt.carrental.common.model.VehicleAlertModel;
import com.cmdt.carrental.common.model.VehicleAlertStatistics;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleQueryDTO;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.Pagination;


@Repository
public class VehicleAlertJobDaoImpl implements VehicleAlertJobDao{
	
	@Autowired
	 private JdbcTemplate jdbcTemplate;

	@Override
	public VehicleAlertModel create(final VehicleAlertModel vehicleAlertModel) {
		final String sql = "INSERT INTO busi_vehicle_alert(driver_id, currentuse_org_id, ent_id, rent_id, vehicle_number, vehicle_type, alert_type,alert_speed, overspeed_percent, alert_city, alert_position, alert_longitude, alert_latitude, outbound_minutes,"
				         + "first_outbound_kilos, outbound_kilos,back_station_distance,back_station_ids,"
				         + "alert_time,first_outboundtime,outbound_releasetime,first_alert_longitude,first_alert_latitude,create_time)VALUES (?, ?, ? ,? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,now())";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                
                if(null!=vehicleAlertModel.getDriverId()){
                   psst.setLong(count++, vehicleAlertModel.getDriverId());
                }else{
                	psst.setNull(count++, java.sql.Types.INTEGER);
                }
                
                if(null!=vehicleAlertModel.getCurrentUseOrgId()){
                  psst.setLong(count++, vehicleAlertModel.getCurrentUseOrgId());
                }else{
                  psst.setNull(count++, java.sql.Types.INTEGER);
                }
                
                if(null!=vehicleAlertModel.getEntId()){
                    psst.setLong(count++, vehicleAlertModel.getEntId());
                  }else{
                    psst.setNull(count++, java.sql.Types.INTEGER);
                  }
                
                if(null!=vehicleAlertModel.getRentId()){
                    psst.setLong(count++, vehicleAlertModel.getRentId());
                  }else{
                    psst.setNull(count++, java.sql.Types.INTEGER);
                  }
                
                psst.setString(count++, vehicleAlertModel.getVehicleNumber());
                psst.setString(count++, vehicleAlertModel.getVehicleType());
                psst.setString(count++, vehicleAlertModel.getAlertType());
                psst.setString(count++, vehicleAlertModel.getAlertSpeed());
                psst.setString(count++, vehicleAlertModel.getOverspeedPercent());
                psst.setString(count++, vehicleAlertModel.getAlertCity());
                psst.setString(count++, vehicleAlertModel.getAlertPosition());
                psst.setString(count++, vehicleAlertModel.getAlertLongitude());
                psst.setString(count++, vehicleAlertModel.getAlertLatitude());
                psst.setString(count++, vehicleAlertModel.getOutboundMinutes());
                
                
                if(null!=vehicleAlertModel.getFirstOutboundKilos()){
                    psst.setDouble(count++, vehicleAlertModel.getFirstOutboundKilos());
                 }else{
                 	psst.setNull(count++, java.sql.Types.INTEGER);
                 }
                
                if(null!=vehicleAlertModel.getOutboundKilos()){
                    psst.setDouble(count++, vehicleAlertModel.getOutboundKilos());
                 }else{
                 	psst.setNull(count++, java.sql.Types.INTEGER);
                 }
                
                if(null!=vehicleAlertModel.getBackStationDistance()){
                    psst.setDouble(count++, vehicleAlertModel.getBackStationDistance());
                 }else{
                 	psst.setNull(count++, java.sql.Types.INTEGER);
                 }
                
                StringBuffer stationIdsBuffer = new StringBuffer();
                if(vehicleAlertModel.getStations()!=null&&vehicleAlertModel.getStations().size()>0){
	        		for(StationModel station : vehicleAlertModel.getStations()){
	        			stationIdsBuffer.append(station.getId()).append(",");

	        		}
	        		if(stationIdsBuffer.length()>0){
	        			stationIdsBuffer.deleteCharAt(stationIdsBuffer.length()-1);
	        		}
                }
                psst.setString(count++, stationIdsBuffer.length()>0?stationIdsBuffer.toString():null);
                                                                                                                                                          
                psst.setTimestamp(count++, vehicleAlertModel.getAlertTime());
                psst.setTimestamp(count++, vehicleAlertModel.getFirstOutboundtime());
                psst.setTimestamp(count++, vehicleAlertModel.getOutboundReleasetime());
                psst.setString(count++, vehicleAlertModel.getFirstAlertLongitude());
                psst.setString(count, vehicleAlertModel.getFirstAlertLatitude());
                return psst;
            }
        }, keyHolder);

        vehicleAlertModel.setId(keyHolder.getKey().longValue());		
        return vehicleAlertModel;
	}

	@Override
	public List<VehicleAlertModel> create(List<VehicleAlertModel> filteredData) {
		for(VehicleAlertModel vehicleAlertModel : filteredData){
			create(vehicleAlertModel);
		}
		return filteredData;
	}

	@Override
	public List<VehicleQueryDTO> findVehicleList() {
		List<VehicleQueryDTO> retList = new ArrayList<VehicleQueryDTO>();
		
		List<VehicleQueryDTO> retTmpList = jdbcTemplate.query("select vehicle_number,device_number from busi_vehicle", new BeanPropertyRowMapper<VehicleQueryDTO>(VehicleQueryDTO.class));
		if(retTmpList != null && retTmpList.size() > 0){
			retList.addAll(retTmpList);
		}
		return retList;
	}
	
	@Override
	public List<VehicleQueryDTO> findVehicleHasOrg(){
        List<VehicleQueryDTO> retList = new ArrayList<VehicleQueryDTO>();
		
		List<VehicleQueryDTO> retTmpList = jdbcTemplate.query("select vehicle_number,device_number from busi_vehicle where currentuse_org_id is not null and device_number is not null", new BeanPropertyRowMapper<VehicleQueryDTO>(VehicleQueryDTO.class));
		if(retTmpList != null && retTmpList.size() > 0){
			retList.addAll(retTmpList);
		}
		return retList;
	}
	

	@Override
	public List<MarkerModel> findMarker(String vehicleNumber) {
		StringBuilder sb =new StringBuilder();
		sb.append("select v.vehicle_number,mr.sq_marker_id as id from busi_vehicle_marker vm ");
		sb.append("left join busi_vehicle v on v.id = vm.vehicle_id ");
		sb.append("left join busi_marker m on vm.marker_id = m.id ");
		sb.append("left join busi_marker_relation mr on mr.cr_marker_id = vm.marker_id ");
		sb.append("where v.vehicle_number = ?");
		List<MarkerModel> markerList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<MarkerModel>(MarkerModel.class),vehicleNumber);
        if(markerList == null || markerList.isEmpty()) {
            return null;
        }
        return markerList;
	}

	@Override
	public List<StationModel> findStation(String vehicleNumber) {
    	StringBuilder sb =new StringBuilder();
    	sb.append("SELECT a.id, station_name as stationName, a.city, a.position, a.longitude, a.latitude, a.radius, a.car_number as carNumber,a.organization_id as organizationId,a.marker_id as markerId,a.start_time as startTime,a.end_time as endTime ");
		sb.append(" from busi_station a join busi_vehicle_station b on a.id = b.station_id ");
		sb.append(" join busi_vehicle c on c.id = b.vehicle_id where c.vehicle_number=? order by a.id desc");
    	List<StationModel> stationList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<StationModel>(StationModel.class),vehicleNumber);
        if(stationList == null || stationList.isEmpty()) {
            return null;
        }
        return stationList;
	}
	
	@Override
	public List<StationModel> findStation(Long vehicleId) {
    	StringBuilder sb =new StringBuilder();
    	sb.append("SELECT a.id, station_name as stationName, a.city, a.position, a.longitude, a.latitude, a.radius, a.car_number as carNumber,a.organization_id as organizationId,a.marker_id as markerId,a.start_time as startTime,a.end_time as endTime ");
		sb.append(" from busi_station a join busi_vehicle_station b on a.id = b.station_id ");
		sb.append(" join busi_vehicle c on c.id = b.vehicle_id where c.id=? order by a.id desc");
    	List<StationModel> stationList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<StationModel>(StationModel.class),vehicleId);
        if(stationList == null || stationList.isEmpty()) {
            return null;
        }
        return stationList;
	}
	
	@Override
	public List<StationModel> findStationByIds(String ids){
		StringBuilder sb =new StringBuilder();
    	sb.append("SELECT a.id, station_name as stationName, a.city, a.position, a.longitude, a.latitude, a.radius, a.car_number as carNumber,a.organization_id as organizationId,a.marker_id as markerId,a.start_time as startTime,a.end_time as endTime ");
		sb.append(" from busi_station a where a.id = any(string_to_array(?, ',')::int[])");
    	List<StationModel> stationList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<StationModel>(StationModel.class),ids);
        if(stationList == null || stationList.isEmpty()) {
            return null;
        }
        return stationList;
	}

	@Override
	public VehicleModel getVehicle(String vehicleNumber) {
		List<VehicleModel> vehicleList = jdbcTemplate.query("select * from busi_vehicle where vehicle_number=?", new BeanPropertyRowMapper<VehicleModel>(VehicleModel.class),vehicleNumber);
		if(vehicleList != null && vehicleList.size() > 0){
			return vehicleList.get(0);
		}
		return null;
	}
	
	@Override
	public VehicleModel getVehicleByImei(String imei){
		StringBuilder sb =new StringBuilder();
		sb.append("select bv.*,sd.latest_limit_speed from busi_vehicle bv,sys_device sd where ");
		sb.append("1 = 1  ");
		sb.append("AND (  ");
		sb.append(" (bv.device_number = sd.sn_number AND sd.device_vendor = 'DH' AND sd.sn_number = ?) OR ");
		sb.append(" (bv.device_number = sd.iccid_number AND sd.device_vendor = 'DBJ' AND sd.iccid_number = ?) OR");
		sb.append(" (bv.device_number = sd.imei_number AND sd.device_vendor = 'gosafe' AND sd.imei_number = ?) )");
		
		List<VehicleModel> vehicleList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<VehicleModel>(VehicleModel.class),imei,imei,imei);
		if(vehicleList != null && vehicleList.size() > 0){
			return vehicleList.get(0);
		}
		return null;
	}
	
	@Override
	public List<VehicleQueryDTO> findVehicleHasMarker(){
        List<VehicleQueryDTO> retList = new ArrayList<VehicleQueryDTO>();
        StringBuilder sb =new StringBuilder();
		 sb.append("select bv.vehicle_number,bv.device_number ");
		 sb.append("FROM busi_vehicle bv,busi_event_config bec ");
		 sb.append("where ");
		 sb.append("bv.currentuse_org_id is not null and bv.device_number is not null ");
		 sb.append("AND bec.ent_id = bv.ent_id AND bec.event_type = 'OUTBOUND' AND bec.enable = true ");
		 sb.append("and bv.id in (select distinct(vehicle_id) from busi_vehicle_marker)");
		List<VehicleQueryDTO> retTmpList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<VehicleQueryDTO>(VehicleQueryDTO.class));
		if(retTmpList != null && retTmpList.size() > 0){
			retList.addAll(retTmpList);
		}
		return retList;
	}
	
	public List<VehicleQueryDTO> findVehicleListHasStationAndTimeLimit(){
		 StringBuilder sb =new StringBuilder();
		 sb.append("SELECT bv.vehicle_number, bv.device_number, bv.start_time, bv.end_time ");
		 sb.append("FROM busi_vehicle bv,busi_event_config bec ");
		 sb.append("where ");
		 sb.append("bv.currentuse_org_id IS NOT NULL AND bv.start_time IS NOT NULL AND bv.end_time IS NOT NULL AND bv.device_number IS NOT NULL ");
		 sb.append("AND bec.ent_id = bv.ent_id AND bec.event_type = 'VEHICLEBACK' AND bec.enable = true ");
		 sb.append("AND bv.ID IN (SELECT DISTINCT(vehicle_id) FROM busi_vehicle_station)");
		 List<VehicleQueryDTO> retList = new ArrayList<VehicleQueryDTO>();
			List<VehicleQueryDTO> retTmpList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<VehicleQueryDTO>(VehicleQueryDTO.class));
			if(retTmpList != null && retTmpList.size() > 0){
				retList.addAll(retTmpList);
			}
			return retList;
	}

	@Override
	public DriverModel findDriver(String vehicleNumber, Date traceTime) {
		StringBuilder sb =new StringBuilder();
		sb.append("select c.* from busi_order a join busi_vehicle b on a.vehicle_id=b.id ")
		.append("join sys_user c on a.driver_id=c.id ")
		.append("where b.vehicle_number=? ")
		.append("and ((a.fact_st_time<= ? and a.fact_ed_time>=?) ")
        .append("or (a.fact_st_time<= ? and a.fact_ed_time is null))");
		List<DriverModel> driverList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<DriverModel>(DriverModel.class),vehicleNumber,traceTime,traceTime,traceTime);
		if(driverList != null && driverList.size() > 0){
			return driverList.get(0);
		}
		return null;
	}
	
	@Override
	public DriverModel findDefaultDriverByVehicleNumber(String vehicleNumber){
		StringBuilder sb =new StringBuilder();
		  sb.append("SELECT su.* FROM busi_vehicle bv ")
			.append("INNER JOIN busi_vehicle_driver bvd ON bv. ID = bvd.vehicle_id ")
			.append("INNER JOIN sys_user su ON su. ID = bvd.driver_id ")
			.append("WHERE ")
	        .append("bv.vehicle_number = ? ")
			.append("AND bvd.driver_id <> -1");
		List<DriverModel> driverList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<DriverModel>(DriverModel.class),vehicleNumber);
		if(driverList != null && driverList.size() > 0){
			return driverList.get(0);
		}
		return null;
	}
	
	@Override
	public StationsDurationModel findStationsDurationByVechileNumber(String vehicleNumber){
		StringBuilder sb =new StringBuilder();
		sb.append("select a.vehicle_number as vehicleNumber,min(c.start_time) as startTime,max(c.end_time) as endTime from ") 
                  .append("busi_vehicle a  inner join busi_vehicle_station b ON a.id = b.vehicle_id ")
                  .append("inner join busi_station c on b.station_id = c.id where a.vehicle_number=? GROUP BY a.vehicle_number");
		List<StationsDurationModel> stationDurationList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<StationsDurationModel>(StationsDurationModel.class),vehicleNumber);
		if(stationDurationList != null && stationDurationList.size() > 0){
			return stationDurationList.get(0);
		}
		return null;
	}

	/**
	 * 
	 */
	@Override
	public void relaseOutboundAlert(VehicleAlertModel vehicleAlertModel) {
		StringBuilder sb = new StringBuilder();
		sb.append("update busi_vehicle_alert set alert_city=?,outbound_minutes=?,outbound_kilos=?,alert_longitude=?,alert_latitude=?,alert_position=?,outbound_releasetime=? where alert_type=? and vehicle_number=? and outbound_releasetime is null");
		
		List<Object> params = new ArrayList<Object>();
		params.add(vehicleAlertModel.getAlertCity());
		params.add(vehicleAlertModel.getOutboundMinutes());
		params.add(vehicleAlertModel.getOutboundKilos());
		params.add(vehicleAlertModel.getAlertLongitude());
		params.add(vehicleAlertModel.getAlertLatitude());
		params.add(vehicleAlertModel.getAlertPosition());
		params.add(vehicleAlertModel.getOutboundReleasetime());
		params.add(AlertType.OUTBOUND.toString());
		params.add(vehicleAlertModel.getVehicleNumber());
		
		jdbcTemplate.update(sb.toString(),params.toArray());
	}
	
	@Override
	public void updateOutboundAlert(VehicleAlertModel vehicleAlertModel){
		StringBuilder sb = new StringBuilder();
		sb.append("update busi_vehicle_alert set alert_city=?,outbound_minutes=?,outbound_kilos=?,alert_longitude=?,alert_latitude=?,alert_position=? where alert_type=? and vehicle_number=? and outbound_releasetime is null");
		
		List<Object> params = new ArrayList<Object>();
		params.add(vehicleAlertModel.getAlertCity());
		params.add(vehicleAlertModel.getOutboundMinutes());
		params.add(vehicleAlertModel.getOutboundKilos());
		params.add(vehicleAlertModel.getAlertLongitude());
		params.add(vehicleAlertModel.getAlertLatitude());
		params.add(vehicleAlertModel.getAlertPosition());
		params.add(AlertType.OUTBOUND.toString());
		params.add(vehicleAlertModel.getVehicleNumber());
		
		jdbcTemplate.update(sb.toString(),params.toArray());
	}
	
	/**
	 * 根据车辆编号获取最近一次未解除报警的越界报警数据
	 */
	@Override
	public VehicleAlertModel getLatestOutboundData(VehicleModel vehicleModel) {
		String sql = "select *,currentuse_org_id as currentUseOrgId from busi_vehicle_alert where alert_type=? and vehicle_number=? and outbound_releasetime is null order by alert_time desc limit 1";
		List<VehicleAlertModel> outboundList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<VehicleAlertModel>(VehicleAlertModel.class),AlertType.OUTBOUND.toString(),vehicleModel.getVehicleNumber());
		if(outboundList != null && outboundList.size() > 0){
			return outboundList.get(0);
		}
		return null;
	}
	
	@Override
	public VehicleAlertModel getLastAlertDataByType(String vehicleNumber,String alertType){
		String sql = "select * from busi_vehicle_alert where alert_type=? and vehicle_number=? order by alert_time desc limit 1";
		List<VehicleAlertModel> alertList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<VehicleAlertModel>(VehicleAlertModel.class),alertType,vehicleNumber);
		if(alertList != null && alertList.size() > 0){
			return alertList.get(0);
		}
		return null;
	}
	
	@Override
	public List<VehicleAlertStatistics> doVehicleAlertStatistics(){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT A.alert_type,A.alert_day,COUNT (A.id) as alert_number,A.currentuse_org_id as org_id,A.ent_id");
		sb.append(" FROM (select C.id,C.alert_type,to_date(to_char(C.alert_time,'yyyy-MM-dd'), 'yyyy-MM-dd') as alert_day,C.currentuse_org_id,C.ent_id ");
		sb.append(" FROM busi_vehicle_alert C left join sys_organization D on D.id = C.currentuse_org_id");
		sb.append(" WHERE to_char(C .alert_time, 'yyyy-MM-dd') = to_char(now() - INTERVAL '1 day','yyyy-MM-dd')) A");
		sb.append(" group by A.alert_type,A.alert_day,A.currentuse_org_id,A.ent_id");
		List<VehicleAlertStatistics> outboundList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<VehicleAlertStatistics>(VehicleAlertStatistics.class));
		if(outboundList != null && outboundList.size() > 0){
			return outboundList;
		}
		return null;
	}
	
	@Override
	public List<VehicleAlertStatistics> doOutboundkiloStatistics(){
		StringBuffer sb = new StringBuffer();
		sb.append("select '"+AlertType.OUTBOUNDKILOS+"' as alert_type,a.alert_day,sum(a.outbound_kilos) as outbound_kilos,a.currentuse_org_id as org_id,a.ent_id");
		sb.append(" from (select c.id,c.alert_type,to_date(to_char(c.alert_time,'yyyy-MM-dd'), 'yyyy-MM-dd') as alert_day,c.outbound_kilos,c.currentuse_org_id,c.ent_id from busi_vehicle_alert c left join sys_organization d on d.id = c.currentuse_org_id where c.alert_type='OUTBOUND'");
		sb.append(" and to_char(c.alert_time, 'yyyy-MM-dd') = to_char(now() - INTERVAL '1 day','yyyy-MM-dd')) a ");
		sb.append(" group by a.alert_type,a.alert_day,a.currentuse_org_id,a.ent_id");
	
		List<VehicleAlertStatistics> outboundList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<VehicleAlertStatistics>(VehicleAlertStatistics.class));
		if(outboundList != null && outboundList.size() > 0){
			return outboundList;
		}
		return null;
	}

	@Override
	public VehicleAlertStatistics createStatistics(final VehicleAlertStatistics vehicleAlertStatistics) {
			final String sql = "INSERT INTO busi_vehicle_alert_statistics(alert_type, alert_day, alert_number,outbound_kilos, org_id, ent_id)VALUES (?,?, ?, ?, ?,?)";

	        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	        jdbcTemplate.update(new PreparedStatementCreator() {
	            @Override
	            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
	                int count = 1;
	                
	                psst.setString(count++, vehicleAlertStatistics.getAlertType());
	                
	                if(null!=vehicleAlertStatistics.getAlertDay()){
	                    psst.setDate(count++, vehicleAlertStatistics.getAlertDay());
	                 }else{
	                 	psst.setNull(count++, java.sql.Types.DATE);
	                 }
	               
	                if(null!=vehicleAlertStatistics.getAlertNumber()){
	                    psst.setInt(count++, vehicleAlertStatistics.getAlertNumber());
	                 }else{
	                 	psst.setNull(count++, java.sql.Types.INTEGER);
	                 }
	               
	                //psst.setString(count++, vehicleAlertStatistics.getAlertNumberAverage());
	                
	                if(null!=vehicleAlertStatistics.getOutboundKilos()){
	                    psst.setDouble(count++, vehicleAlertStatistics.getOutboundKilos());
	                 }else{
	                 	psst.setNull(count++, java.sql.Types.INTEGER);
	                 }
	                
	                if(null!=vehicleAlertStatistics.getOrgId()){
	                    psst.setDouble(count++, vehicleAlertStatistics.getOrgId());
	                 }else{
	                 	psst.setNull(count++, java.sql.Types.INTEGER);
	                 }
	                
	                if(null!=vehicleAlertStatistics.getEntId()){
	                    psst.setDouble(count++, vehicleAlertStatistics.getEntId());
	                 }else{
	                 	psst.setNull(count, java.sql.Types.INTEGER);
	                 }
	                return psst;
	            }
	        }, keyHolder);

	        vehicleAlertStatistics.setId(keyHolder.getKey().longValue());		
	        return vehicleAlertStatistics;
	}
	
	@Override
	public List<AlertStatisticModel> statisticAlertByTypeAndTimeRanger(List<Organization> orgList,Long orgId,Boolean selfDept,Boolean childDept,String alertType,String fromDate,String toDate){
		
		List<Object> params = new ArrayList<Object>();
		
		StringBuffer orgIdsStr = new StringBuffer();
	    for(Organization org : orgList){
	    	orgIdsStr.append(org.getId()+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
		
		StringBuffer sb = new StringBuffer();
		sb.append("select so.parent_id,so.id,so.name,COALESCE(st.value,0) as value from sys_organization so left join (select a.currentuse_org_id as id,b.name,count(a.id) as value from busi_vehicle_alert a left join sys_organization b on a.currentuse_org_id = b.id");
		sb.append(" where 1=1 ");
		if(selfDept){
			sb.append("and ((a.ent_id = ? and  a.currentuse_org_id is null ) or a.currentuse_org_id in ("+orgIdsStr+") or a.currentuse_org_id = ?) ");
			params.add(orgId);
			params.add(orgId);
		}else{
		sb.append("and a.currentuse_org_id in ("+orgIdsStr+") ");
		}
		sb.append("and a.alert_time >= ? and a.alert_time <= ? and a.alert_type = ? ");
		sb.append("GROUP BY a.currentuse_org_id,b.name) st on so.id = st.id where so.id in ("+orgIdsStr+")");
		
		params.add(new Timestamp(DateUtils.string2Date(fromDate, DateUtils.FORMAT_YYYYMMDD_HH_MI_SS).getTime()));
		params.add(new Timestamp(DateUtils.string2Date(toDate, DateUtils.FORMAT_YYYYMMDD_HH_MI_SS).getTime()));
		params.add(alertType);
	
		List<AlertStatisticModel> alertList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<AlertStatisticModel>(AlertStatisticModel.class),params.toArray());
		
		if(alertList != null && alertList.size() > 0){
			return alertList;
		}
		return null;
	}
	
	@Override
	public VehicleAlertModel getAlertById(Long alertId){
		String sql = "select a.*,b.realname as driverName,c.name as orgName,v.device_number as deviceNumber,b.phone as driverMobile "
				+ "from busi_vehicle_alert a "
				+ "left join sys_user b on a.driver_id = b.id "
				+ "left join sys_organization c on c.id = a.currentuse_org_id "
				+ "left join busi_vehicle v on v.vehicle_number = a.vehicle_number where a.id = ?";
		List<VehicleAlertModel> alertList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<VehicleAlertModel>(VehicleAlertModel.class),alertId);
		if(alertList != null && alertList.size() > 0){
			return alertList.get(0);
		}
		return null;
	}
	
	@Override
	public List<CountModel> statisticDailyAlertByTypeAndTimeRanger(List<Long> orgIdList,Long orgId,Boolean selfDept,String alertType,String fromDate,String toDate){
		List<Object> params = new ArrayList<Object>();
		
		StringBuffer orgIdsStr = new StringBuffer();
	    for(Long temOrgId : orgIdList){
	    	orgIdsStr.append(temOrgId+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
		
		StringBuffer sb = new StringBuffer();
		  sb.append(" select a.countDate,COALESCE(b.countValue,0) as countValue from  ")
			.append(" (select to_char(CURRENT_DATE + i, 'yyyy-mm-dd') as countDate from generate_series(to_date(?,'yyyymmdd')- CURRENT_DATE, to_date(?,'yyyymmdd') - CURRENT_DATE ) i) a ")
			.append(" LEFT JOIN  ")
			.append(" (select to_char(alert_time, 'yyyy-mm-dd') as alertDate,count(*) as countValue from busi_vehicle_alert ")
			.append(" where 1=1 ");
		    
			params.add(fromDate);
			params.add(toDate);
		  
			if(selfDept){
				sb.append("and ((ent_id = ? and  currentuse_org_id is null ) or currentuse_org_id in ("+orgIdsStr+") or currentuse_org_id = ?) ");
				params.add(orgId);
				params.add(orgId);
			}else{
			sb.append("and currentuse_org_id in ("+orgIdsStr+") ");
			}
			
		 sb.append(" and alert_type=? and alert_time >= ? and alert_time <= ? ");
		 sb.append(" group by to_char(alert_time, 'yyyy-mm-dd')) b ");
		 sb.append(" on a.countDate = b.alertDate");;
		 params.add(alertType);
		 params.add(new Timestamp(DateUtils.string2Date(fromDate+" 00:00:00", DateUtils.FORMAT_YYYYMMDD_HH_MI_SS).getTime()));
		 params.add(new Timestamp(DateUtils.string2Date(toDate+" 23:59:59", DateUtils.FORMAT_YYYYMMDD_HH_MI_SS).getTime()));

		List<CountModel> countList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<CountModel>(CountModel.class),params.toArray());
		if(countList != null && countList.size() > 0){
			return countList;
		}
		return null;
	}
	
	public PagModel queryAlertByOrgTypeAndTimeRange(String alertType,Boolean self,Long orgId,String fromDate,String toDate,List<Organization> orgList,int currentPage, int numPerPage){
		List<Object> params = new ArrayList<Object>();
		
		StringBuffer orgIdsStr = new StringBuffer();
	    for(Organization org : orgList){
	    	orgIdsStr.append(org.getId()+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
		
	    StringBuffer buffer = new StringBuffer();
	    buffer.append("	select a.*,b.realname as driverName,b.phone as driverMobile,c.name as orgName,v.device_number as deviceNumber,v.vehicle_brand as vehicleBrand, ");
	    buffer.append(" array_to_string(ARRAY(select station_name from busi_station where id = any(string_to_array(a.back_station_ids, ',')::int[])), ',') AS station_names");
	    buffer.append("	from busi_vehicle_alert a ");
	    buffer.append("	left join sys_user b on a.driver_id = b.id ");
	    buffer.append("	left join sys_organization c on c.id = a.currentuse_org_id ");
	    buffer.append(" left join busi_vehicle v on v.vehicle_number = a.vehicle_number ");
	    buffer.append("	where 1=1 ");
	    if(self){
	    	buffer.append("and ((a.ent_id = ? and  a.currentuse_org_id is null ) or a.currentuse_org_id in ("+orgIdsStr+") or a.currentuse_org_id = ?) ");
	    	params.add(orgId);
			params.add(orgId);
		}else{
			buffer.append("and a.currentuse_org_id in ("+orgIdsStr+") ");
		}
	    buffer.append("	and a.alert_time >= ?");
	    buffer.append("	and a.alert_time <= ?");
	    buffer.append("	and a.alert_type = ? order by c.orgindex, a.alert_time desc");

	    params.add(new Timestamp(DateUtils.string2Date(fromDate+" 00:00:00", DateUtils.FORMAT_YYYYMMDD_HH_MI_SS).getTime()));
	    params.add(new Timestamp(DateUtils.string2Date(toDate+" 23:59:59", DateUtils.FORMAT_YYYYMMDD_HH_MI_SS).getTime()));
	    params.add(alertType);
		
		Pagination page=new Pagination(buffer.toString(), currentPage, numPerPage,VehicleAlertModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}
	
	public List<AlertDiagramStatisticModel> queryVehicleAlertStatistics(Date startDay, Date endDay,List<Organization> orgList,String alertType,Long orgId,Boolean self){
		List<Object> params = new ArrayList<Object>();
		StringBuffer buffer = new StringBuffer();
		
		StringBuffer orgIdsStr = new StringBuffer();
	    for(Organization org : orgList){
	    	orgIdsStr.append(org.getId()+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
		
		params.add(alertType);
		params.add(new Timestamp(startDay.getTime()));
		params.add(new Timestamp(endDay.getTime()));
		
		buffer.append(" SELECT so.id as org_id,so.parent_id,so.name as org_name,COALESCE(stat.alert_total,0) as alert_total,COALESCE(stat.outbound_kilos_total,0) as outbound_kilos_total ");
		buffer.append(" from sys_organization so ");
		buffer.append(" left join ");
		buffer.append(" ( SELECT COUNT (bva. ID) AS alert_total, bva.alert_type,bva.currentuse_org_id,SUM (bva.outbound_kilos) AS outbound_kilos_total ");
		buffer.append("   FROM busi_vehicle_alert bva");
		buffer.append("    WHERE ");
		buffer.append("    bva.alert_type = ? ");
		buffer.append("    AND bva.alert_time >= ?::TIMESTAMP ");
		buffer.append("    AND bva.alert_time <= ?::TIMESTAMP ");
		
		if (self) {
			buffer.append("  and ((bva.ent_id=? and bva.currentuse_org_id is null) or bva.currentuse_org_id in (").append(orgIdsStr).append(") or bva.currentuse_org_id = ?)");
			params.add(orgId);
			params.add(orgId);
		}else{
			buffer.append("  and bva.currentuse_org_id in (").append(orgIdsStr).append(")");
		}
		
		buffer.append("    GROUP BY ");
		buffer.append("    currentuse_org_id,alert_type) stat ");
		buffer.append(" on so.id = stat.currentuse_org_id");
		buffer.append(" where so.id IN ("+orgIdsStr+")");
		buffer.append(" order by so.orgindex ");
		List<AlertDiagramStatisticModel> countList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper<AlertDiagramStatisticModel>(AlertDiagramStatisticModel.class),params.toArray());
		if(countList != null && countList.size() > 0){
			return countList;
		}
		return null;
	}

	@Override
	public EventConfig queryAlarmConfig(Long vehicleId,String eventType) {
		StringBuilder sb = new StringBuilder();
		sb.append("select bec.ent_id,bec.event_type,bec.enable,bec.update_time ");
		sb.append("from busi_vehicle bv,busi_event_config bec ");
		sb.append("where bv.device_number is not null ");
		sb.append("AND bec.ent_id = bv.ent_id AND bec.event_type = ? AND bec.enable = true and bv.id = ?");
		List<EventConfig> configs = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<EventConfig>(EventConfig.class),eventType,vehicleId);
		if(configs!=null&&configs.size()>0){
			return configs.get(0);
		}else{
			return null;
		}
	}

}
