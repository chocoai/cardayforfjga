package com.cmdt.carrental.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleStasticModel;

@Repository
public class VehicleUsageReportDaoImpl implements VehicleUsageReportDao{
	
	private Logger LOG = Logger.getLogger(VehicleUsageReportDaoImpl.class);

	 @Autowired
	 private JdbcTemplate jdbcTemplate;

	 @SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public List<VehicleModel> findVehicleList() {
			List<VehicleModel> retList = new ArrayList<VehicleModel>();
			
			List<VehicleModel> retTmpList = jdbcTemplate.query("select vehicle_number,device_number,ent_id,ent_name,currentuse_org_id,currentuse_org_name,theoretical_fuel_con from busi_vehicle where device_number is not null", new BeanPropertyRowMapper(VehicleModel.class));
			if(retTmpList != null && retTmpList.size() > 0){
				retList.addAll(retTmpList);
			}
			return retList;
		}

	@Override
	@SuppressWarnings("unchecked")
	public void saveOrUpdateVehicleStastic(VehicleStasticModel vehicleStasticModel,String currentDateVal) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer buffer = new StringBuffer();
		buffer.append("		select id,vehicle_number,device_number,total_mileage,total_fuel_cons,total_driving_time");
		buffer.append("		from busi_vehicle_stastic");
		buffer.append("		where"); 
		buffer.append("		vehicle_number = ?");
		buffer.append("		and");
		buffer.append("		device_number = ?");
		buffer.append("		and");
		buffer.append("		to_char(last_updated_time,'yyyy-MM-dd') = ?");
		params.add(vehicleStasticModel.getVehicleNumber());
		params.add(vehicleStasticModel.getDeviceNumber());
		params.add(currentDateVal);
		List<VehicleStasticModel> retList = jdbcTemplate.query(buffer.toString(), new BeanPropertyRowMapper(VehicleStasticModel.class),params.toArray());
	    if(retList != null && retList.size() > 0){
	    	LOG.info("------------current day usage data is not null--------------," + vehicleStasticModel.getVehicleNumber() + "," + vehicleStasticModel.getDeviceNumber());
	    	//1.如果接口调用数据失败(失败定义:1.1接口调用失败，所有指标为0   1.2接口调用成功，但是mileage为0,所有指标设置为0),则vehicleStasticModel中的指标值均为0,此时不需要更新DB
	    	//2.如果接口调用数据成功，则vehicleStasticModel中的指标值均不为0，此时需要更新DB
	    	if(!(vehicleStasticModel.getTotalMileage() == 0 && 
	    			vehicleStasticModel.getTotalFuelCons() == 0.0 
	    			&& vehicleStasticModel.getTotalDrivingTime() == 0)){
	    		LOG.info("------------current day usage data is not null and new data is valid,so update current day usage data--------------," + vehicleStasticModel.getVehicleNumber() + "," + vehicleStasticModel.getDeviceNumber());
	    		Long updateId = retList.get(0).getId();
	    		updateVehicleStastic(vehicleStasticModel,updateId);
	    	}
	    }else{
	    	LOG.info("------------current day usage data is null ,so update current day usage data--------------," + vehicleStasticModel.getVehicleNumber() + "," + vehicleStasticModel.getDeviceNumber());
	    	//当天没有数据，执行新增操作
	    	saveVehicleStastic(vehicleStasticModel);
	    }
	}
	
	public void saveVehicleStastic(final VehicleStasticModel vehicleStasticModel){
		final String sql = "insert into busi_vehicle_stastic(vehicle_number,device_number,ent_id,ent_name,currentuse_org_id,currentuse_org_name,total_mileage,total_fuel_cons,total_driving_time,last_updated_time) values(?,?,?,?,?,?,?,?,?,?)";
		  GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	        jdbcTemplate.update(new PreparedStatementCreator() {
	          @Override
	          public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	              PreparedStatement psst = connection.prepareStatement(sql, new String[]{"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
	              int count = 1;
	              psst.setString(count++, vehicleStasticModel.getVehicleNumber());
	              psst.setString(count++, vehicleStasticModel.getDeviceNumber());
	              
	              if(vehicleStasticModel.getEntId() != null){
	            	  psst.setLong(count++, vehicleStasticModel.getEntId());
	              }else{
	            	  psst.setNull(count++, Types.INTEGER);
	              }
	              
	              if(vehicleStasticModel.getEntName() != null){
	            	  psst.setString(count++, vehicleStasticModel.getEntName());
	              }else{
	            	  psst.setNull(count++, Types.VARCHAR);
	              }
	              
	              if(vehicleStasticModel.getCurrentuseOrgId() != null){
	            	  psst.setLong(count++, vehicleStasticModel.getCurrentuseOrgId());
	              }else{
	            	  psst.setNull(count++, Types.INTEGER);
	              }
	              
	              if(vehicleStasticModel.getCurrentuseOrgName() != null){
	            	  psst.setString(count++, vehicleStasticModel.getCurrentuseOrgName());
	              }else{
	            	  psst.setNull(count++, Types.VARCHAR);
	              }
	              
	              psst.setInt(count++, vehicleStasticModel.getTotalMileage());
	              psst.setDouble(count++, vehicleStasticModel.getTotalFuelCons());
	              psst.setInt(count++, vehicleStasticModel.getTotalDrivingTime());
	              psst.setTimestamp(count, vehicleStasticModel.getLastUpdatedTime());
	              return psst;
	          }
	      }, keyHolder);
	}
	
	public void updateVehicleStastic(VehicleStasticModel vehicleStasticModel,Long id){
		String sql = "update busi_vehicle_stastic  set ent_id=?,ent_name=?,currentuse_org_id=?,currentuse_org_name=?,total_mileage=?,total_fuel_cons=?,total_driving_time=?,last_updated_time=? where id = ?";
		List<Object> params = new ArrayList<Object>();
		params.add(vehicleStasticModel.getEntId());
		params.add(vehicleStasticModel.getEntName());
		params.add(vehicleStasticModel.getCurrentuseOrgId());
		params.add(vehicleStasticModel.getCurrentuseOrgName());
		params.add(vehicleStasticModel.getTotalMileage());
		params.add(vehicleStasticModel.getTotalFuelCons());
		params.add(vehicleStasticModel.getTotalDrivingTime());
		params.add(vehicleStasticModel.getLastUpdatedTime());
		params.add(id);
		jdbcTemplate.update(sql,params.toArray());
	}

	@Override
	public void updateVehicleStasticCurrentOwner(Long currentuseOrgId, String currentuseOrgName, String vehicleNumber,
			String deviceNumber, String currentDateVal) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("		update busi_vehicle_stastic"); 
		buffer.append("		set currentuse_org_id = ?,currentuse_org_name = ?");
		buffer.append("		where"); 
		buffer.append("		vehicle_number = ? and device_number = ? and to_char(last_updated_time,'yyyy-MM-dd') = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(currentuseOrgId);
		params.add(currentuseOrgName);
		params.add(vehicleNumber);
		params.add(deviceNumber);
		params.add(currentDateVal);
		jdbcTemplate.update(buffer.toString(),params.toArray());
	}

	@Override
	public void cleanCurrentOwner(String vehicleNumber, String deviceNumber, String currentDateVal) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("		update busi_vehicle_stastic"); 
		buffer.append("		set currentuse_org_id = null,currentuse_org_name = null");
		buffer.append("		where"); 
		buffer.append("		vehicle_number = ? and device_number = ? and to_char(last_updated_time,'yyyy-MM-dd') = ?");
		List<Object> params = new ArrayList<Object>();
		params.add(vehicleNumber);
		params.add(deviceNumber);
		params.add(currentDateVal);
		jdbcTemplate.update(buffer.toString(),params.toArray());
	}
	
}
