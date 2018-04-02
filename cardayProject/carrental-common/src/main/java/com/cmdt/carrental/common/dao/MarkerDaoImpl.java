package com.cmdt.carrental.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.entity.Marker;
import com.cmdt.carrental.common.entity.MarkerId;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.MarkerModel;
import com.cmdt.carrental.common.model.MarkerQueryDto;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.PointModel;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.Pagination;
import com.cmdt.carrental.common.util.SqlUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class MarkerDaoImpl implements MarkerDao {
	private static final Logger LOG = LoggerFactory.getLogger(MarkerDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private ShouqiService shouqiService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Override
	public PagModel findMarkerByName(String geofenceName, Long organizationId, String currentPage,String numPerPage) {
		int currentPageNum = 1;
    	int numPerPageNum = 10;
		if(!"".equals(currentPage)){
		  currentPageNum = Integer.valueOf(String.valueOf(currentPage));
		}
		if(!"".equals(numPerPage)){
	    	numPerPageNum = Integer.valueOf(String.valueOf(numPerPage));
		}
    	List<Object> params = new ArrayList<Object>();
//    	String sql = "SELECT id, marker_name as markerName, province, city, position, organization_id as organizationId, type, pattern from busi_marker ";
    	String sql = "SELECT t1.id,t1.marker_name,t1.position,t1.type,t1.pattern,t1.organization_id,t1.region_id cityId,t1.longitude,t1.latitude,t1.radius,t2.name city,t2.parentid "
    		       + " from busi_marker t1 left join region t2 on t1.region_id = t2.id ";
		if(StringUtils.isNotEmpty(geofenceName)){
//			sql += "where marker_name like ? and organization_id=? order by id desc";
			sql += "where t1.marker_name like "+SqlUtil.processLikeInjectionStatement(geofenceName)+" and t1.organization_id=? ";
			params.add(organizationId);
		}else{
//			sql += "where organization_id=? order by id desc";
			sql += "where t1.organization_id=? ";
			params.add(organizationId);
		}
		
		sql  = "select tt1.*,tt2.name province,tt2.id provinceId from ("
			 + sql 
		     + " ) tt1 left join region tt2 on tt1.parentid = tt2.id "
		     + " order by tt1.id desc";
		Pagination page=new Pagination(sql, currentPageNum, numPerPageNum,Marker.class,jdbcTemplate, params.toArray());
		return page.getResult();
        
	}
	
	@Override
	public Marker createMarker(final Marker marker) {
		
		ArrayList<String>  markerIdArray = new ArrayList<String> ();
			
		String[] patternArray = marker.getPattern().split("!");
		
        for (int i = 0; i < patternArray.length; i++) {
            //System.out.println(patternArray[i]);
    		Map<String, Object> result = null;
    		//将数据同步到shouqi总marker表中
           /* String polygonMarker = "{\"name\":\"";
            polygonMarker +=  marker.getMarkerName();
            polygonMarker += "\",\"address\":\"";
            polygonMarker +=  marker.getPosition();
            polygonMarker += "\",\"typeBusiness\":\"1\",\"mapMarkerPoint\":{\"lng\":";
            polygonMarker +=  Double.valueOf(marker.getLongitude()).doubleValue();
            polygonMarker += ",\"lat\":";
            polygonMarker +=  Double.valueOf(marker.getLatitude()).doubleValue();
            polygonMarker += "},\"typeArea\":\"polygon\",\"pattern\":";
            polygonMarker +=  patternArray[i];
            polygonMarker += "}";*/
    		
    		MarkerModel markerModel= new MarkerModel();
            try{
	            markerModel.setName(marker.getMarkerName());
	            markerModel.setAddress(marker.getPosition());
	            markerModel.setTypeBusiness("1");
	            PointModel mapMarkerPoint = new PointModel();
	            mapMarkerPoint.setLng(Double.valueOf(marker.getLongitude()));
	            mapMarkerPoint.setLat(Double.valueOf(marker.getLatitude()));
	            markerModel.setMapMarkerPoint(mapMarkerPoint);
	            if(marker.getType().equals(Constants.MARKER_TYPE_CIRCLE)){
	            	markerModel.setTypeArea("circle");
	            	markerModel.setRadius(Double.valueOf(marker.getRadius())*1000);
	            	List<PointModel> ponintList=new ArrayList<PointModel>();
	    			PointModel point=new PointModel();
	    			point.setLng(Double.parseDouble(marker.getLongitude()));
	    			point.setLat(Double.parseDouble(marker.getLatitude()));
	    			ponintList.add(point);
	    			markerModel.setPattern(Arrays.asList(ponintList));
	            }else{
	            	markerModel.setTypeArea("polygon");
	            	List<List<PointModel>> pattern =MAPPER.readValue(patternArray[0],new TypeReference<List<List<PointModel>> >() { });
	            	 markerModel.setPattern(pattern);
	            }
            }catch(Exception e){
            	LOG.error("MarkerService createMarker error, cause by:\n", e);
    			return null;
            }
           
          //  MarkerModel markerModel = JsonUtils.json2Object(polygonMarker, MarkerModel.class);
    		result = new ServiceAdapter(shouqiService).doService(ActionName.CREATEMARKER,
    				new Object[] { markerModel });
    		
    		try {    		
				if (result != null && result.get("status").equals("success") && result.get("data") != null) {
					JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
					if ("000".equals(jsonNode.get("status").asText())) {
						JsonNode resultNode = (JsonNode) jsonNode.get("result");
						String markerId = resultNode.get("markerId").asText();
						markerIdArray.add(markerId);
					}
				}
			
    		} catch (Exception e) {
    			LOG.error("MarkerService createMarker error, cause by:\n", e);
    			return null;
    		}
				
        }
		
		
		try {
			if(markerIdArray.size() > 0){
				final String sql = "INSERT INTO busi_marker(marker_name, city, position, type, pattern, organization_id,region_id,longitude,latitude,radius) VALUES (?, ?, ?,?, ?, ?, ?, ?, ?, ?);";
		        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		        jdbcTemplate.update(new PreparedStatementCreator() {
		            @Override
		            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		                PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
		                int count = 1;
		                
		                psst.setString(count++, marker.getMarkerName());
		                psst.setString(count++, marker.getCity());
		                psst.setString(count++, marker.getPosition());
		                psst.setString(count++, marker.getType());
		                if(marker.getPattern()!=null){
		                	 psst.setString(count++, marker.getPattern());
		                }else{
		                	psst.setNull(count++, Types.VARCHAR);
		                }
		               
		                psst.setLong(count++, marker.getOrganizationId());
		                psst.setLong(count++, marker.getRegionId());
		                if(marker.getLongitude()!=null){
		                	psst.setString(count++, marker.getLongitude());
		                }else{
		                	psst.setNull(count++, Types.VARCHAR);
		                }
		                if(marker.getLatitude()!=null){
		                	psst.setString(count++, marker.getLatitude());
		                }else{
		                	psst.setNull(count++, Types.VARCHAR);
		                }
		                if(marker.getRadius()!=null){
		                	psst.setString(count, marker.getRadius());
		                }else{
		                	psst.setNull(count, Types.VARCHAR);
		                }
		                return psst;
		            }
		        }, keyHolder);

		        marker.setId(keyHolder.getKey().longValue());
		        for(String markerId:markerIdArray){
			        String sqlMarker = "INSERT INTO busi_marker_relation(cr_marker_id, sq_marker_id)VALUES (?, ?)";
			        jdbcTemplate.update(sqlMarker,keyHolder.getKey().longValue(),Long.valueOf(markerId).longValue());
		        }
		        return marker;
			}else{
				return null;
			}
		} catch (Exception e) {
			LOG.error("MarkerService createMarker error, cause by:\n", e);
			return null;
		}
	}
	
	
	@Override
	public Marker findMarker(Long markerId) {
		String sql = "SELECT id,marker_name as markerName, city, position, type, pattern, organization_id as organizationId, longitude,latitude,radius from busi_marker where id=?";
        List<Marker> markerList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Marker>(Marker.class), markerId);
        if(markerList.size() == 0) {
            return null;
        }
        return markerList.get(0);
	}
	
	@Override
	public Marker updateMarker(Marker marker) {
		
		ArrayList<String>  markerIdArray = new ArrayList<String> ();
		
		String[] patternArray = marker.getPattern().split("!");
		
        for (int i = 0; i < patternArray.length; i++) {
    		Map<String, Object> result = null;
    		//将数据同步到shouqi总marker表中
           /* String polygonMarker = "{\"name\":\"";
            polygonMarker +=  marker.getMarkerName();
            polygonMarker += "\",\"address\":\"";
            polygonMarker +=  marker.getPosition();
            polygonMarker += "\",\"typeBusiness\":\"1\",\"mapMarkerPoint\":{\"lng\":";
            polygonMarker +=  Double.valueOf(marker.getLongitude()).doubleValue();
            polygonMarker += ",\"lat\":";
            polygonMarker +=  Double.valueOf(marker.getLatitude()).doubleValue();
            polygonMarker += "},\"typeArea\":\"polygon\",\"pattern\":";
            polygonMarker +=  patternArray[i];
            polygonMarker += "}";
            
            MarkerModel markerModel = JsonUtils.json2Object(polygonMarker, MarkerModel.class);*/
            
            MarkerModel markerModel= new MarkerModel();
            try{
            markerModel.setName(marker.getMarkerName());
            markerModel.setAddress(marker.getPosition());
            markerModel.setTypeBusiness("1");
            PointModel mapMarkerPoint = new PointModel();
            mapMarkerPoint.setLng(Double.valueOf(marker.getLongitude()));
            mapMarkerPoint.setLat(Double.valueOf(marker.getLatitude()));
            markerModel.setMapMarkerPoint(mapMarkerPoint);
            if(marker.getType().equals(Constants.MARKER_TYPE_CIRCLE)){
            	markerModel.setTypeArea("circle");
            	markerModel.setRadius(Double.valueOf(marker.getRadius())*1000);
            	List<PointModel> ponintList=new ArrayList<PointModel>();
    			PointModel point=new PointModel();
    			point.setLng(Double.parseDouble(marker.getLongitude()));
    			point.setLat(Double.parseDouble(marker.getLatitude()));
    			ponintList.add(point);
    			markerModel.setPattern(Arrays.asList(ponintList));
            }else{
            	markerModel.setTypeArea("polygon");
            	List<List<PointModel>> pattern =MAPPER.readValue(patternArray[0],new TypeReference<List<List<PointModel>> >() { });
            	 markerModel.setPattern(pattern);
            }
            }catch(Exception e){
            	LOG.error("MarkerService createMarker error, cause by:\n", e);
    			return null;
            }
    		result = new ServiceAdapter(shouqiService).doService(ActionName.CREATEMARKER,
    				new Object[] { markerModel });
    		
    		try {    		
				if (result != null && result.get("status").equals("success") && result.get("data") != null) {
					JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
					if ("000".equals(jsonNode.get("status").asText())) {
						JsonNode resultNode = (JsonNode) jsonNode.get("result");
						String markerId = resultNode.get("markerId").asText();
						markerIdArray.add(markerId);
					}
				}
			
    		} catch (Exception e) {
    			LOG.error("MarkerService createMarker error, cause by:\n", e);
    			return null;
    		}
				
        }
        
        String sql = "select sq_marker_id as id from busi_marker_relation where cr_marker_id=?";
        List<MarkerId> markerList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<MarkerId>(MarkerId.class), marker.getId());
        
        if(!markerList.isEmpty()){        
	        for (int i = 0; i < markerList.size(); i++) {
	        	Long markerId = markerList.get(i).getId();
	    		Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.DELETEMARKER,
	    				new Object[] { markerId });
	    				
				if (result != null && result.get("status").equals("success") && result.get("data") != null) {
					LOG.info("MarkerService deleteMarker success");
				}else{
		        	return null;
				}
	        }
	        
		     String sqldelete = "DELETE FROM busi_marker_relation where cr_marker_id=?";
			    jdbcTemplate.update(sqldelete,marker.getId());
        }else{
        	return null;
        }
		
		
		try {			
			if(markerIdArray.size() > 0){
				String sqlUpdate = "UPDATE busi_marker SET marker_name=?, city=?, position=?, type=?, pattern=?, organization_id=?, region_id=? ,longitude=?, latitude=?,radius=? where id=?";
		        jdbcTemplate.update(
		        		sqlUpdate,
		                marker.getMarkerName(),marker.getCity(),marker.getPosition(),marker.getType(),marker.getPattern(),marker.getOrganizationId(),marker.getRegionId(),marker.getLongitude(),
		                marker.getLatitude(),marker.getRadius(),marker.getId());
		        
		        for(String markerId:markerIdArray){
			        String sqlMarker = "INSERT INTO busi_marker_relation(cr_marker_id, sq_marker_id)VALUES (?, ?)";
			        jdbcTemplate.update(sqlMarker,marker.getId(),Long.valueOf(markerId).longValue());
		        }
		        return marker;
			}else{
				return null;
			}
		} catch (Exception e) {
			LOG.error("MarkerService updateMarker error, cause by:\n", e);
			return null;
		}
	}
	
	@Override
	public void deleteMarker(Marker marker) {
				
		try {
	        String sql = "select sq_marker_id as id from busi_marker_relation where cr_marker_id=?";
	        List<MarkerId> markerList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<MarkerId>(MarkerId.class), marker.getId());
	        
	        if(!markerList.isEmpty()){        
		        for (int i = 0; i < markerList.size(); i++) {
		        	Long markerId = markerList.get(i).getId();
		    		Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.DELETEMARKER,
		    				new Object[] { markerId });
		    				
					if (result != null && result.get("status").equals("success") && result.get("data") != null) {
						LOG.info("MarkerService deleteMarker success");
					}
		        }
		        
			     String sqldelete = "DELETE FROM busi_marker_relation where cr_marker_id=?";
				    jdbcTemplate.update(sqldelete,marker.getId());
	        }
	        
				 String sqlDelete = "delete from busi_marker where id=?";
			     jdbcTemplate.update(sqlDelete,marker.getId());
			     //删除station的同时删除station与车辆的关联
			     String sqldeleteRelation = "DELETE FROM busi_vehicle_marker where marker_id=?";
				    jdbcTemplate.update(sqldeleteRelation,marker.getId());

		} catch (Exception e) {
			LOG.error("MarkerService deleteMarker error, cause by:\n", e);
		}
	}
	
	@Override
	public PagModel findMarkerAssignedVehicles(String markerId, Long organizationId,String currentPage,String numPerPage,Boolean isRent,Boolean isEnt) {		
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
	    	String sql = "SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type,c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city from busi_marker a join busi_vehicle_marker b on a.id = b.marker_id ";
			if(StringUtils.isNotEmpty(markerId)){
				sql += " join busi_vehicle c on c.id = b.vehicle_id where a.id=? and c.rent_id=? order by c.id desc";
				params.add(Integer.valueOf(String.valueOf(markerId)));
				params.add(organizationId);
			}else{
				sql += " join busi_vehicle c on c.id = b.vehicle_id where c.rent_id=? order by c.id desc";
				params.add(organizationId);
			}
			page=new Pagination(sql, currentPageNum, numPerPageNum,Vehicle.class,jdbcTemplate, params.toArray());
		}
		
		if(isEnt){
		    	String sql = "SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type,c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city from busi_marker a join busi_vehicle_marker b on a.id = b.marker_id ";
				if(StringUtils.isNotEmpty(markerId)){
					sql += " join busi_vehicle c on c.id = b.vehicle_id where a.id=? and (c.ent_id=? or c.currentuse_org_id=? or c.currentuse_org_id in (select id from sys_organization where parent_id =?)) order by c.id desc";
					params.add(Integer.valueOf(String.valueOf(markerId)));
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
		return page!=null ? page.getResult() :null;
	}

	@Override
	public List<Vehicle> findMarkerAssignedVehiclesSum(String markerId, Long organizationId,Boolean isRent,Boolean isEnt) {
       
		StringBuffer sb = new StringBuffer();
		List<Vehicle> vehicleList = null;
		if(isRent){	
			sb.append("SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type,c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city ");
			sb.append(" from busi_marker a join busi_vehicle_marker b on a.id = b.marker_id ");
			sb.append(" join busi_vehicle c on c.id = b.vehicle_id where a.id=? and c.rent_id=?");
			vehicleList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<Vehicle>(Vehicle.class), new Long(markerId), organizationId);
		}
		
		if(isEnt){	
			sb.append("SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type,c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city ");
			sb.append(" from busi_marker a join busi_vehicle_marker b on a.id = b.marker_id ");
			sb.append(" join busi_vehicle c on c.id = b.vehicle_id where a.id=? and (c.ent_id=? or c.currentuse_org_id=? or c.currentuse_org_id in (select id from sys_organization where parent_id =?))");
			vehicleList = jdbcTemplate.query(sb.toString(), new BeanPropertyRowMapper<Vehicle>(Vehicle.class), new Long(markerId), organizationId, organizationId, organizationId);
		}  	
        return vehicleList;
	}

	@Override
	public PagModel findMarkerAvialiableVehicles(String markerId, Long organizationId,String currentPage,String numPerPage,Boolean isRent,Boolean isEnt) {

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
	    	String sql = "SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type, c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city from busi_vehicle c where c.id not in ( select b.vehicle_id from busi_marker a join busi_vehicle_marker b on a.id = b.marker_id ";
//			if(StringUtils.isNotEmpty(markerId)){
				//sql += "where a.id=? and a.organization_id=?) and c.currentuse_org_id=?";
				sql += "where a.organization_id=?) and c.rent_id=? order by c.id desc";
				//params.add(Integer.valueOf(String.valueOf(markerId)));
				params.add(organizationId);
				params.add(organizationId);
			/*}else{
				sql += "where a.organization_id=?) and c.rent_id=? order by c.id desc";
				params.add(organizationId);
				params.add(organizationId);
			}	*/		
			page=new Pagination(sql, currentPageNum, numPerPageNum,Vehicle.class,jdbcTemplate, params.toArray());
		}
		
		if(isEnt){
	    	String sql = "SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type, c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city from busi_vehicle c where c.id not in ( select b.vehicle_id from busi_marker a join busi_vehicle_marker b on a.id = b.marker_id ";
			//if(StringUtils.isNotEmpty(markerId)){
				//sql += "where a.id=? and a.organization_id=?) and c.currentuse_org_id=?";
				sql += "where a.organization_id=?) and (c.ent_id=? or c.currentuse_org_id=? or c.currentuse_org_id in (select id from sys_organization where parent_id =?)) order by c.id desc";
				//params.add(Integer.valueOf(String.valueOf(markerId)));
				params.add(organizationId);
				params.add(organizationId);
				params.add(organizationId);
				params.add(organizationId);
			/*}else{
				sql += "where a.organization_id=?) and (c.ent_id=? or c.currentuse_org_id=? or c.currentuse_org_id in (select id from sys_organization where parent_id =?)) order by c.id desc";
				params.add(organizationId);
				params.add(organizationId);
				params.add(organizationId);
				params.add(organizationId);
			}	*/		
			page=new Pagination(sql, currentPageNum, numPerPageNum,Vehicle.class,jdbcTemplate, params.toArray());
	    }
		
		return page!=null ? page.getResult() : null;
	}

	@Override
	public void assignVehicles(String vehicleIds, String markerId) {
		String sql = "INSERT INTO busi_vehicle_marker(vehicle_id, marker_id)VALUES (?, ?)";
		if(vehicleIds.indexOf(",") != -1){
			String[] vehicleIdArray = vehicleIds.split(",");
			for(int i=0; i<vehicleIdArray.length;i++){
				jdbcTemplate.update(sql,new Long(vehicleIdArray[i]), new Long(markerId));
			}
		}else{
			 jdbcTemplate.update(sql,new Long(vehicleIds), new Long(markerId));
		}
	}

	@Override
	public void unassignVehicles(String vehicleId, String markerId) {
		String sql = "DELETE FROM busi_vehicle_marker where vehicle_id=? and marker_id=?";
	    jdbcTemplate.update(sql,new Long(vehicleId),new Long(markerId));
	}

	@Override
	public PagModel findMarkerByName(MarkerQueryDto markerQueryDto) {
    	List<Object> params = new ArrayList<Object>();
    	StringBuffer sql = new StringBuffer();
    	sql.append("SELECT t1.id,t1.marker_name,t1.position,t1.type,t1.pattern,t1.organization_id,t1.region_id cityId,t2.name city,t2.parentid,tt2.name province,tt2.id provinceId ");
    	sql.append(" from busi_marker t1 left join region t2 on t1.region_id = t2.id ");
    	sql.append(" left join region tt2 on t2.parentid = tt2.id where 1=1 ");
		if(StringUtils.isNotEmpty(markerQueryDto.getGeofenceName())){
			sql.append(" and t1.marker_name like "+SqlUtil.processLikeInjectionStatement(markerQueryDto.getGeofenceName())+" and t1.organization_id=? ");
			params.add(markerQueryDto.getOrganizationId());
		}else{
			sql.append(" and t1.organization_id=? ");
			params.add(markerQueryDto.getOrganizationId());
		}
		
		sql.append(" order by t1.id desc");
		Pagination page=new Pagination(sql.toString(), markerQueryDto.getCurrentPage(), markerQueryDto.getNumPerPage(),Marker.class,jdbcTemplate, params.toArray());
		return page.getResult();
	}

	@Override
	public PagModel findMarkerAvialiableVehicles(List<Long> orgIds,MarkerQueryDto markerQueryDto) {
		
		Pagination page = null;
		List<Object> params = new ArrayList<Object>();
		StringBuffer orgIdsStr = new StringBuffer();
	    for(Long id : orgIds){
	    	orgIdsStr.append(id+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
	    
		if(markerQueryDto.getIsRentAdmin()){		
	    	String sql = "SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type, c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city from busi_vehicle c where c.id not in ( select b.vehicle_id from busi_marker a join busi_vehicle_marker b on a.id = b.marker_id ";
				sql += "where a.organization_id=?) and c.rent_id=? order by c.id desc";
				params.add(markerQueryDto.getOrganizationId());
				params.add(markerQueryDto.getOrganizationId());
			page=new Pagination(sql, markerQueryDto.getCurrentPage(), markerQueryDto.getNumPerPage(),Vehicle.class,jdbcTemplate, params.toArray());
		}
		
		if(markerQueryDto.getIsEntAdmin()){
	    	String sql = "SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type, c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city from busi_vehicle c where c.id not in ( select b.vehicle_id from busi_marker a join busi_vehicle_marker b on a.id = b.marker_id ";
				sql += "where a.organization_id=?) and (c.ent_id=? or c.currentuse_org_id in ("+orgIdsStr +")) order by c.id desc";
				params.add(markerQueryDto.getOrganizationId());
				params.add(markerQueryDto.getOrganizationId());
			page=new Pagination(sql, markerQueryDto.getCurrentPage(), markerQueryDto.getNumPerPage(),Vehicle.class,jdbcTemplate, params.toArray());
	    }
		
		return page!=null ? page.getResult() : null;
	}

	@Override
	public PagModel findMarkerAssignedVehicles(List<Long> orgIds,MarkerQueryDto markerQueryDto) {
		List<Object> params = new ArrayList<Object>();
		Pagination page = null;
		StringBuffer orgIdsStr = new StringBuffer();
	    for(Long id : orgIds){
	    	orgIdsStr.append(id+",");
	    }
	    if(orgIdsStr!=null&&orgIdsStr.length()>0){
	    	orgIdsStr.replace(orgIdsStr.length()-1, orgIdsStr.length(), "");
	    }
		
		if(markerQueryDto.getIsRentAdmin()){
	    	String sql = "SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type,c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city from busi_marker a join busi_vehicle_marker b on a.id = b.marker_id ";
			if(markerQueryDto.getMarkerId()!=null){
				sql += " join busi_vehicle c on c.id = b.vehicle_id where a.id=? and c.rent_id=? order by c.id desc";
				params.add(markerQueryDto.getMarkerId());
				params.add(markerQueryDto.getOrganizationId());
			}else{
				sql += " join busi_vehicle c on c.id = b.vehicle_id where c.rent_id=? order by c.id desc";
				params.add(markerQueryDto.getOrganizationId());
			}
			page=new Pagination(sql, markerQueryDto.getCurrentPage(), markerQueryDto.getNumPerPage(),Vehicle.class,jdbcTemplate, params.toArray());
		}
		
		if(markerQueryDto.getIsEntAdmin()){
		    	String sql = "SELECT c.id, c.vehicle_number, c.vehicle_identification, c.vehicle_type, c.vehicle_brand, c.vehicle_model, c.seat_number, c.vehicle_color, c.vehicle_output, c.vehicle_fuel, c.vehicle_buy_time, c.license_type,c.rent_id,c.rent_name,c.ent_id,c.ent_name, c.currentuse_org_id,c.currentuse_org_name, c.city from busi_marker a join busi_vehicle_marker b on a.id = b.marker_id ";
				if(markerQueryDto.getMarkerId()!=null){
					sql += " join busi_vehicle c on c.id = b.vehicle_id where a.id=? and (c.ent_id=? or c.currentuse_org_id in ( "+orgIdsStr +" )) order by c.id desc";
					params.add(markerQueryDto.getMarkerId());
					params.add(markerQueryDto.getOrganizationId());
				}else{
					sql += " join busi_vehicle c on c.id = b.vehicle_id where (c.ent_id=? or c.currentuse_org_id in ( "+orgIdsStr +" )) order by c.id desc";
					params.add(markerQueryDto.getOrganizationId());
				}	
				page=new Pagination(sql,  markerQueryDto.getCurrentPage(), markerQueryDto.getNumPerPage(),Vehicle.class,jdbcTemplate, params.toArray());
		}
		return page!=null ? page.getResult() :null;
	}

}
