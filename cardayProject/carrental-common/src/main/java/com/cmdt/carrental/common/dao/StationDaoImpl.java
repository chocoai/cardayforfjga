package com.cmdt.carrental.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.MarkerModel;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.entity.Station;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.StationQueryDto;
import com.cmdt.carrental.common.util.Pagination;
import com.cmdt.carrental.common.util.SqlUtil;
import com.cmdt.carrental.common.util.TypeUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;

@Repository
public class StationDaoImpl implements StationDao{
	private static final Logger LOG = LoggerFactory.getLogger(StationDaoImpl.class);
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ShouqiService shouqiService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Override
	public Station createStation(final Station station) {
		
		//将数据同步到shouqi总marker表中
        String circleMarker = "{\"name\":\"";
        circleMarker +=  station.getStationName();
        circleMarker += "\",\"address\":\"";
        circleMarker +=  station.getPosition();
        circleMarker += "\",\"typeBusiness\":\"1\",\"mapMarkerPoint\":{\"lng\":";
        circleMarker +=  Double.valueOf(station.getLongitude()).doubleValue();
        circleMarker += ",\"lat\":";
        circleMarker +=  Double.valueOf(station.getLatitude()).doubleValue();
        circleMarker += "},\"typeArea\":\"circle\",\"pattern\":[[{\"lng\":";
        circleMarker +=  Double.valueOf(station.getLongitude()).doubleValue();
        circleMarker += ",\"lat\":";
        circleMarker +=  Double.valueOf(station.getLatitude()).doubleValue();
        circleMarker += "}]],\"radius\":";
        circleMarker += Double.valueOf(station.getRadius()).doubleValue()*1000 + "}";
        
        MarkerModel markerModel = JsonUtils.json2Object(circleMarker, MarkerModel.class);
		Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.CREATEMARKER,
				new Object[] { markerModel });
		
		try {
			if (result != null && result.get("status").equals("success") && result.get("data") != null) {
				JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
				if ("000".equals(jsonNode.get("status").asText())) {
					JsonNode resultNode = (JsonNode) jsonNode.get("result");
					String markerId = resultNode.get("markerId").asText();
					station.setMarkerId(Long.valueOf(markerId));
					final String sql = "INSERT INTO busi_station(station_name, city, position, longitude, latitude, radius, car_number, organization_id, marker_id,start_time,end_time,region_id) VALUES (?, ?, ?,?, ?, ?, ?, ?, ?,?,?, ?);";

			        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
			        jdbcTemplate.update(new PreparedStatementCreator() {
			            @Override
			            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
			                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
			                int count = 1;
			                
			                psst.setString(count++, station.getStationName());
			                psst.setString(count++, station.getCity());
			                psst.setString(count++, station.getPosition());
			                psst.setString(count++, station.getLongitude());
			                psst.setString(count++, station.getLatitude());
			                psst.setString(count++, station.getRadius());
			                psst.setString(count++, station.getCarNumber());
			                psst.setLong(count++, station.getOrganizationId());
			                psst.setLong(count++, station.getMarkerId());
			                psst.setString(count++, station.getStartTime());
			                psst.setString(count++, station.getEndTime());
			                psst.setLong(count, station.getAreaId());
			                return psst;
			            }
			        }, keyHolder);

			        station.setId(keyHolder.getKey().longValue());		
			        return station;
				} else
					return null;
			}else
				return null;
		} catch (Exception e) {
			LOG.error("MarkerService createMarker error, cause by:\n", e);
			return null;
		}
	}

	@Override
	public Station updateStation(Station station) {
		
		//将数据同步到shouqi总marker表中
		String circleMarker = "{\"id\":\"";
		circleMarker +=  String.valueOf(station.getMarkerId());
		circleMarker += "\",\"name\":\"";
        circleMarker +=  station.getStationName();
        circleMarker += "\",\"address\":\"";
        circleMarker +=  station.getPosition();
        circleMarker += "\",\"typeBusiness\":\"1\",\"mapMarkerPoint\":{\"lng\":";
        circleMarker +=  Double.valueOf(station.getLongitude()).doubleValue();
        circleMarker += ",\"lat\":";
        circleMarker +=  Double.valueOf(station.getLatitude()).doubleValue();
        circleMarker += "},\"typeArea\":\"circle\",\"pattern\":[[{\"lng\":";
        circleMarker +=  Double.valueOf(station.getLongitude()).doubleValue();
        circleMarker += ",\"lat\":";
        circleMarker +=  Double.valueOf(station.getLatitude()).doubleValue();
        circleMarker += "}]],\"radius\":";
        circleMarker += Double.valueOf(station.getRadius()).doubleValue()*1000 + "}";
        
        MarkerModel markerModel = JsonUtils.json2Object(circleMarker, MarkerModel.class);
		Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.UPDATEMARKER,
				new Object[] { markerModel });
		
		try {
			if (result != null && result.get("status").equals("success") && result.get("data") != null) {
				JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
				if ("000".equals(jsonNode.get("status").asText())) {
					BooleanNode resultNode = (BooleanNode) jsonNode.get("result");
					if(resultNode.asBoolean()){
						String sql = "UPDATE busi_station SET station_name=?, city=?, position=?, longitude=?, latitude=?, radius=?, car_number=?, organization_id=?, marker_id=?,start_time=?, end_time=?, region_id=? where id=?";
				        jdbcTemplate.update(
				                sql,
				                station.getStationName(),station.getCity(),station.getPosition(),station.getLongitude(),station.getLatitude(),station.getRadius(),station.getCarNumber(),station.getOrganizationId(),station.getMarkerId(),station.getStartTime(),station.getEndTime(),station.getAreaId(),station.getId());
				        return station;
					}else{
						return null;
					}
				} else
					return null;
			}else
				return null;
		} catch (Exception e) {
			LOG.error("MarkerService updateMarker error, cause by:\n", e);
			return null;
		}
	}

	@Override
	public void deleteStation(Station station) {
		
		Long markerId = station.getMarkerId();
		
		Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.DELETEMARKER,
				new Object[] { markerId });
		
		try {
			if (result != null && result.get("status").equals("success") && result.get("data") != null) {
				JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
				if ("000".equals(jsonNode.get("status").asText())) {					  
					 String sql = "delete from busi_station where id=?";
				     jdbcTemplate.update(sql,station.getId());
				     //删除station的同时删除station与车辆的关联
				     String sqldelete = "DELETE FROM busi_vehicle_station where station_id=?";
					    jdbcTemplate.update(sqldelete,station.getId());
				}
			}
		} catch (Exception e) {
			LOG.error("MarkerService deleteMarker error, cause by:\n", e);
		}
	}

	@Override
	public Station findStation(Long stationId) {
		String sql = "SELECT id,station_name as stationName, city, position, longitude, latitude, radius, car_number as carNumber, organization_id as organizationId,marker_id as markerId,start_time as startTime,end_time as endTime from busi_station where id=?";
        List<Station> stationList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Station>(Station.class), stationId);
        if(stationList.size() == 0) {
            return null;
        }
        return stationList.get(0);
	}

	@Override
	public List<Station> findByStationName(String stationName) {
		String sql = "SELECT id,station_name as stationName, city, position, longitude, latitude, radius, car_number as carNumber, organization_id as organizationId,marker_id as markerId,start_time as startTime,end_time as endTime from busi_station where station_name like "+SqlUtil.processLikeInjectionStatement(stationName);
        List<Station> stationList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Station>(Station.class));
		return stationList;
	}

	@Override
	public List<Station> findByStationName(String stationName,Long entId) {
		String sql = "SELECT id,station_name as stationName, city, position, longitude, latitude, radius, car_number as carNumber, organization_id as organizationId,marker_id as markerId,start_time as startTime,end_time as endTime from busi_station where station_name = ? and organization_id=?";
        List<Station> stationList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Station>(Station.class), stationName,entId);
		return stationList;
	}
	
	@Override
	public List<Station> findAll() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id, station_name as stationName, city, position, longitude, latitude, radius, car_number as carNumber, organization_id as organizationId,marker_id as markerId,start_time as startTime,end_time as endTime from busi_station");
		List<Station> stationList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<Station>(Station.class));
        
        if(stationList.size() == 0) {
            return null;
        }
        return stationList;
	}

	@Override
	public PagModel findStationsByName(String stationName, Long organizationId, String currentPage,String numPerPage) {
		int currentPageNum = 1;
    	int numPerPageNum = 10;
		if(!"".equals(currentPage)){
		  currentPageNum = Integer.valueOf(String.valueOf(currentPage));
		}
		if(!"".equals(numPerPage)){
	    	numPerPageNum = Integer.valueOf(String.valueOf(numPerPage));
		}
    	List<Object> params = new ArrayList<Object>();
//    	String sql = "SELECT id, station_name as stationName, city, position, longitude, latitude, radius, car_number as carNumber, organization_id as organizationId,marker_id as markerId,start_time as startTime,end_time as endTime from busi_station ";
    	String sql = "select t1.id,station_name,position,longitude,latitude,radius,car_number,organization_id,marker_id, "
    			   + " start_time,end_time,region_id areaId,t2.name area,t2.parentid cityId "
    			   + " from busi_station t1 left join region t2 on t1.region_id = t2.id ";
		if(StringUtils.isNotEmpty(stationName)){
//			sql += "where station_name like ? and organization_id=? order by id desc";
			sql += "where t1.station_name like "+SqlUtil.processLikeInjectionStatement(stationName)+" and t1.organization_id=? ";
			params.add(organizationId);
		}else{
//			sql += "where organization_id=? order by id desc";
			sql += "where t1.organization_id=? ";
			params.add(organizationId);
		}
		
		sql = "select ttt1.*,ttt2.name province,ttt1.parentid provinceId from ( "
			+ " select tt1.*,tt2.name city,tt2.parentid from ( "
		    + sql + " ) tt1 left join region tt2 "
		    + " on tt1.cityId = tt2.id) ttt1 left join region ttt2 "
		    + " on ttt1.parentid = ttt2.id order by ttt1.id desc";
		
		Pagination page=new Pagination(sql, currentPageNum, numPerPageNum,Station.class,jdbcTemplate, params.toArray());
		return page.getResult();
        
	}

	@Override
	public List<Station> findStationsByOrgId(String organizationId) {
		String sql = "SELECT id, station_name as stationName, city, position, longitude, latitude, radius, car_number as carNumber,organization_id as organizationId,marker_id as markerId,start_time as startTime,end_time as endTime from busi_station where organization_id=?";
    	List<Station> stationList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Station>(Station.class), new Long(organizationId));
        if(stationList.size() == 0) {
            return null;
        }
        return stationList;
	}

	@Override
	public PagModel findStationAssignedVehicles(String stationId, Long organizationId,String currentPage,String numPerPage,Boolean isRent,Boolean isEnt) {
		int currentPageNum = 1;
    	int numPerPageNum = 10;
		if(!("").equals(currentPage)){
		  currentPageNum = Integer.valueOf(String.valueOf(currentPage));
		}
		if(!("").equals(numPerPage)){
	    	numPerPageNum = Integer.valueOf(String.valueOf(numPerPage));
		}
		List<Object> params = new ArrayList<Object>();
		
		Pagination page = null;
		
		if(isRent){
	    	String sql = "SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type,c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city from busi_station a join busi_vehicle_station b on a.id = b.station_id ";
			if(StringUtils.isNotEmpty(stationId)){
				sql += " join busi_vehicle c on c.id = b.vehicle_id where a.id=? and c.rent_id=? order by c.id desc";
				params.add(Integer.valueOf(String.valueOf(stationId)));
				params.add(organizationId);
			}else{
				sql += " join busi_vehicle c on c.id = b.vehicle_id where c.rent_id=? order by c.id desc";
				params.add(organizationId);
			}
			page=new Pagination(sql, currentPageNum, numPerPageNum,Vehicle.class,jdbcTemplate, params.toArray());
		}
		
		if(isEnt){
		    	String sql = "SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type,c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city from busi_station a join busi_vehicle_station b on a.id = b.station_id ";
				if(StringUtils.isNotEmpty(stationId)){
					sql += " join busi_vehicle c on c.id = b.vehicle_id where a.id=? and (c.ent_id=? or c.currentuse_org_id=? or c.currentuse_org_id in (select id from sys_organization where parent_id =?)) order by c.id desc";
					params.add(Integer.valueOf(String.valueOf(stationId)));
					params.add(organizationId);
					params.add(organizationId);
					params.add(organizationId);
				}else{
					sql += " join busi_vehicle c on c.id = b.vehicle_id where (c.ent_id=? or c.currentuse_org_id=? or c.currentuse_org_id in (select id from sys_organization where parent_id =?)) order by c.id desc";
					params.add(organizationId);
					params.add(organizationId);
					params.add(organizationId);
				}	
				page=new Pagination(sql, currentPageNum, numPerPageNum,Vehicle.class,jdbcTemplate, params.toArray());
		}
		return page!=null ? page.getResult():null;
	}
	
	@Override
	public List<Vehicle> findStationAssignedVehiclesSum(String stationId, Long organizationId,Boolean isRent,Boolean isEnt) {
		StringBuffer sb = new StringBuffer();
		List<Vehicle> vehicleList = null;
		if(isRent){	
			sb.append("SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type,c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city ");
			sb.append(" from busi_station a join busi_vehicle_station b on a.id = b.station_id ");
			sb.append(" join busi_vehicle c on c.id = b.vehicle_id where a.id=? and c.rent_id=?");
			vehicleList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<Vehicle>(Vehicle.class), new Long(stationId), organizationId);
		}
		
		if(isEnt){	
			sb.append("SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type,c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city ");
			sb.append(" from busi_station a join busi_vehicle_station b on a.id = b.station_id ");
			sb.append(" join busi_vehicle c on c.id = b.vehicle_id where a.id=? and (c.ent_id=? or c.currentuse_org_id=? or c.currentuse_org_id in (select id from sys_organization where parent_id =?))");
			vehicleList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<Vehicle>(Vehicle.class), new Long(stationId), organizationId, organizationId, organizationId);
		}  	
        return vehicleList;
	}
	
	@Override
	public PagModel findStationAvialiableVehicles(String stationId, Long organizationId,String currentPage,String numPerPage,Boolean isRent,Boolean isEnt) {
		int currentPageNum = 1;
    	int numPerPageNum = 10;
		if(!"".equals(currentPage)){
		  currentPageNum = Integer.valueOf(String.valueOf(currentPage));
		}
		if(!"".equals(numPerPage)){
	    	numPerPageNum = Integer.valueOf(String.valueOf(numPerPage));
		}
		List<Object> params = new ArrayList<Object>();
		
		Pagination page = null;
		
		if(isRent){		
	    	String sql = "SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type, c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city from busi_vehicle c where c.id not in ( select b.vehicle_id from busi_station a join busi_vehicle_station b on a.id = b.station_id ";
			if(StringUtils.isNotEmpty(stationId)){
				//sql += "where a.id=? and a.organization_id=?) and c.currentuse_org_id=?";
				sql += "where a.id=? and a.organization_id=?) and c.rent_id=? order by c.id desc";
				params.add(Integer.valueOf(String.valueOf(stationId)));
				params.add(organizationId);
				params.add(organizationId);
			}else{
				sql += "where a.organization_id=?) and c.rent_id=? order by c.id desc";
				params.add(organizationId);
				params.add(organizationId);
			}			
			page=new Pagination(sql, currentPageNum, numPerPageNum,Vehicle.class,jdbcTemplate, params.toArray());
		}
		
		if(isEnt){
	    	String sql = "SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type, c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city from busi_vehicle c where c.id not in ( select b.vehicle_id from busi_station a join busi_vehicle_station b on a.id = b.station_id ";
			if(StringUtils.isNotEmpty(stationId)){
				//sql += "where a.id=? and a.organization_id=?) and c.currentuse_org_id=?";
				sql += "where a.id=? and a.organization_id=?) and (c.ent_id=? or c.currentuse_org_id=? or c.currentuse_org_id in (select id from sys_organization where parent_id =?)) order by c.id desc";
				params.add(Integer.valueOf(String.valueOf(stationId)));
				params.add(organizationId);
				params.add(organizationId);
				params.add(organizationId);
				params.add(organizationId);
			}else{
				sql += "where a.organization_id=?) and (c.ent_id=? or c.currentuse_org_id=? or c.currentuse_org_id in (select id from sys_organization where parent_id =?)) order by c.id desc";
				params.add(organizationId);
				params.add(organizationId);
				params.add(organizationId);
				params.add(organizationId);
			}			
			page=new Pagination(sql, currentPageNum, numPerPageNum,Vehicle.class,jdbcTemplate, params.toArray());
	    }
		
		
		
		return page!=null ? page.getResult():null;
	}

	@Override
	public void assignVehicles(String vehicleIds, String stationId) {
		String sql = "INSERT INTO busi_vehicle_station(vehicle_id, station_id)VALUES (?, ?)";
		if(vehicleIds.indexOf(",") != -1){
			String[] vehicleIdArray = vehicleIds.split(",");
			for(int i=0; i<vehicleIdArray.length;i++){
				jdbcTemplate.update(sql,new Long(vehicleIdArray[i]), new Long(stationId));
			}
		}else{
			 jdbcTemplate.update(sql,new Long(vehicleIds), new Long(stationId));
		}
	}

	@Override
	public void unassignVehicles(String vehicleId, String stationId) {
		String sql = "DELETE FROM busi_vehicle_station where vehicle_id=? and station_id=?";
	    jdbcTemplate.update(sql,new Long(vehicleId),new Long(stationId));
	}

	@Override
	public PagModel findStationsByName(StationQueryDto stationQueryDto) {
		
    	List<Object> params = new ArrayList<Object>();

		StringBuffer sql= new StringBuffer();
		sql.append(" select t1.id,station_name,position,longitude,latitude,radius,car_number,organization_id,marker_id,start_time,end_time, ");
		sql.append(" region_id areaId,t2.name area,t2.parentid cityId,tt2.name city,tt2.parentid ,ttt2.name province,tt2.parentid provinceId ");
		sql.append(" from busi_station t1 left join region t2 on t1.region_id = t2.id ");
		sql.append(" left join region tt2 on t2.parentid  = tt2.id");
		sql.append(" left join region ttt2  on tt2.parentid = ttt2.id where 1=1");
    	
    	if(StringUtils.isNotBlank(stationQueryDto.getStationName())){
			sql.append(" and t1.station_name like "+SqlUtil.processLikeInjectionStatement(stationQueryDto.getStationName())+" and t1.organization_id=? ");
			params.add(stationQueryDto.getOrganizationId());
		}else{
			sql.append(" and  t1.organization_id=? ");
			params.add(stationQueryDto.getOrganizationId());
		}
    	sql.append(" order by t1.id desc");
    	
		Pagination page=new Pagination(sql.toString(), stationQueryDto.getCurrentPage(), stationQueryDto.getNumPerPage(),Station.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}

	@Override
	public PagModel findStationAssignedVehicles(List<Long> orgIds,StationQueryDto stationQueryDto) {
		List<Object> params = new ArrayList<Object>();
		Pagination page = null;
		
		StringBuffer orgIdsStr = new StringBuffer();
	    for(Long id : orgIds){
	    	orgIdsStr.append(id+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
	    
		if(stationQueryDto.getIsRent()){
	    	String sql = "SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type,c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city from busi_station a join busi_vehicle_station b on a.id = b.station_id ";
			if(stationQueryDto.getStationId()!=null){
				sql += " join busi_vehicle c on c.id = b.vehicle_id where a.id=? and c.rent_id=? order by c.id desc";
				params.add(Integer.valueOf(String.valueOf(stationQueryDto.getStationId())));
				params.add(stationQueryDto.getOrganizationId());
			}else{
				sql += " join busi_vehicle c on c.id = b.vehicle_id where c.rent_id=? order by c.id desc";
				params.add(stationQueryDto.getOrganizationId());
			}
			page=new Pagination(sql, stationQueryDto.getCurrentPage(), stationQueryDto.getNumPerPage(),Vehicle.class,jdbcTemplate, params.toArray());
		}
		
		if(stationQueryDto.getIsEnt()){
		    	String sql = "SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type,c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city from busi_station a join busi_vehicle_station b on a.id = b.station_id ";
				if(stationQueryDto.getStationId()!=null){
					sql += " join busi_vehicle c on c.id = b.vehicle_id where a.id=? and (c.ent_id=? or c.currentuse_org_id in ("+orgIdsStr+")) order by c.id desc";
					params.add(Integer.valueOf(String.valueOf(stationQueryDto.getStationId())));
					params.add(stationQueryDto.getOrganizationId());
				}else{
					sql += " join busi_vehicle c on c.id = b.vehicle_id where (c.ent_id=? or c.currentuse_org_id  in ("+orgIdsStr+")) order by c.id desc";
					params.add(stationQueryDto.getOrganizationId());
				}	
				page=new Pagination(sql, stationQueryDto.getCurrentPage(), stationQueryDto.getNumPerPage(),Vehicle.class,jdbcTemplate, params.toArray());
		}
		return page!=null ? page.getResult():null;
	}

	@Override
	public PagModel findStationAvialiableVehicles(List<Long> orgIds,StationQueryDto stationQueryDto) {
		List<Object> params = new ArrayList<Object>();
		Pagination page = null;
		
		StringBuffer orgIdsStr = new StringBuffer();
	    for(Long id : orgIds){
	    	orgIdsStr.append(id+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
	    
		if(stationQueryDto.getIsRent()){		
	    	String sql = "SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type, c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city from busi_vehicle c where c.id not in ( select b.vehicle_id from busi_station a join busi_vehicle_station b on a.id = b.station_id ";
			if(stationQueryDto.getStationId()!=null){
				sql += "where a.id=? and a.organization_id=?) and c.rent_id=? order by c.id desc";
				params.add(Integer.valueOf(String.valueOf(stationQueryDto.getStationId())));
				params.add(stationQueryDto.getOrganizationId());
				params.add(stationQueryDto.getOrganizationId());
			}else{
				sql += "where a.organization_id=?) and c.rent_id=? order by c.id desc";
				params.add(stationQueryDto.getOrganizationId());
				params.add(stationQueryDto.getOrganizationId());
			}			
			page=new Pagination(sql, stationQueryDto.getCurrentPage(), stationQueryDto.getNumPerPage(),Vehicle.class,jdbcTemplate, params.toArray());
		}
		
		if(stationQueryDto.getIsEnt()){
	    	String sql = "SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type, c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city from busi_vehicle c where c.id not in ( select b.vehicle_id from busi_station a join busi_vehicle_station b on a.id = b.station_id ";
			if(stationQueryDto.getStationId()!=null){
				sql += "where a.id=? and a.organization_id=?) and (c.ent_id=? or c.currentuse_org_id in ("+orgIdsStr+")) order by c.id desc";
				params.add(Integer.valueOf(String.valueOf(stationQueryDto.getStationId())));
				params.add(stationQueryDto.getOrganizationId());
				params.add(stationQueryDto.getOrganizationId());
			}else{
				sql += "where a.organization_id=?) and (c.ent_id=? or c.currentuse_org_id in ("+orgIdsStr+")) order by c.id desc";
				params.add(stationQueryDto.getOrganizationId());
				params.add(stationQueryDto.getOrganizationId());
			}			
			page=new Pagination(sql, stationQueryDto.getCurrentPage(), stationQueryDto.getNumPerPage(),Vehicle.class,jdbcTemplate, params.toArray());
	    }
		
		return page!=null ? page.getResult():null;
	}
}
