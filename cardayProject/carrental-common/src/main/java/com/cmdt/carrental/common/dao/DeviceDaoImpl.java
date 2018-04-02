package com.cmdt.carrental.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.entity.DevcieCommandConfigRecord;
import com.cmdt.carrental.common.entity.Device;
import com.cmdt.carrental.common.entity.DeviceStatus;
import com.cmdt.carrental.common.model.DevcieCommandConfigRecordModel;
import com.cmdt.carrental.common.model.DeviceModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.util.Pagination;
import com.cmdt.carrental.common.util.SqlUtil;
import com.cmdt.carrental.common.util.TypeUtils;

@Repository
public class DeviceDaoImpl implements DeviceDao {
	private static final Logger LOG = LoggerFactory.getLogger(DeviceDaoImpl.class);
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public Device createDevice(final Device device) {
		final String sql =
	            "insert into sys_device(sn_number, imei_number, device_type, device_model, device_vendor_number,device_vendor, firmware_version, software_version, "
	            + "purchase_time, maintain_expire_time, iccid_number, sim_number,vehicle_id,device_status, device_batch) "
	            + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	        
	        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	        jdbcTemplate.update(new PreparedStatementCreator()
	        {
	            @Override
	            public PreparedStatement createPreparedStatement(Connection connection)
	                throws SQLException
	            {
	                PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
	                int count = 1;
	                psst.setString(count++, device.getSnNumber());
	                psst.setString(count++, device.getImeiNumber());
	                psst.setString(count++, device.getDeviceType());
	                psst.setString(count++, device.getDeviceModel());
	                psst.setString(count++, device.getDeviceVendorNumber());
	                psst.setString(count++, device.getDeviceVendor());
	                psst.setString(count++, device.getFirmwareVersion());
	                psst.setString(count++, device.getSoftwareVersion());
//	                psst.setDate(count++, device.getPurchaseTime());
	                psst.setTimestamp(count++, new java.sql.Timestamp(device.getPurchaseTime().getTime()));
	                psst.setTimestamp(count++, new java.sql.Timestamp(device.getMaintainExpireTime().getTime()));
	                psst.setString(count++, device.getIccidNumber());
	                psst.setString(count++, device.getSimNumber());
	                if(device.getVehicleId() != null){
	                	psst.setLong(count++, device.getVehicleId());
	                }else{
	                	psst.setNull(count++, Types.INTEGER);
	                }
	                psst.setLong(count++, device.getDeviceStatus());
	                psst.setString(count, device.getDeviceBatch());
	                return psst;
	            }
	        }, keyHolder);
	        
	        device.setId(keyHolder.getKey().longValue());
	        return device;
	}

	@Override
	public Boolean imeiNumberIsValid(String imeiNumber) {
		List<Device> deviceList = jdbcTemplate.query("select id from sys_device where imei_number=?",
	            new BeanPropertyRowMapper<Device>(Device.class),
	            imeiNumber);
        if (deviceList != null && deviceList.size() > 0)
        {
            return false;
        }
        return true;
	}

	@Override
	public Boolean vehicleNumberIsValid(Long vehicleId) {
		List<Device> deviceList = jdbcTemplate.query("select id from sys_device where vehicle_id=?",
	            new BeanPropertyRowMapper<Device>(Device.class),
	            vehicleId);
        if (deviceList != null && deviceList.size() > 0)
        {
            return false;
        }
        return true;
	}
	
	@SuppressWarnings({ "unchecked"})
	@Override
	public PagModel findAll(Long orgId, DeviceModel device) {
		String sql = "select* from (select t.*,(select vehicle_number from busi_vehicle where id=t.vehicle_id) vehicle_number,"
				+ "(select vehicle_identification from busi_vehicle where id=t.vehicle_id) vehicle_identification,"
				+ "(select rent_id from busi_vehicle where id=t.vehicle_id) rent_id,"
				+ "(select rent_name from busi_vehicle where id=t.vehicle_id) rent_name,"
				+ "(select ent_id from busi_vehicle where id=t.vehicle_id) ent_id,"
				+ "(select limit_speed from busi_vehicle where id=t.vehicle_id) limit_speed,"
				+ "(select command_excute_status from device_command_config_record d "
					+ " where d.id = (select max(id) from device_command_config_record "
					+ " GROUP BY device_number,command_type,command_send_status "
					+ " HAVING device_number=t.imei_number  "
					+ "AND command_type = 'SET_LIMIT_SPEED' "
					+ "AND command_send_status = '000')) command_status,"
				+ "(select ent_name from busi_vehicle where id=t.vehicle_id) ent_name "
				+ "from sys_device t) st where 1=1";
		List<Object> params = new ArrayList<Object>();
		int currentPage = 1;
    	int numPerPage = 10;
    	
    	if(device.getCurrentPage() != 0){
    		currentPage = device.getCurrentPage();
    	}
    	if(device.getNumPerPage() != 0){
    		numPerPage = device.getNumPerPage();
    	}
    	//Long deptId=TypeUtils.obj2Long(jsonMap.get("organizationId"));
		String imeiNumber = device.getImeiNumber();
		if (StringUtils.isNotBlank(imeiNumber)) {
			sql += " and st.imei_number like "+SqlUtil.processLikeInjectionStatement(imeiNumber);
		}
		String vehicleNumber = device.getVehicleNumber();
		if (StringUtils.isNotBlank(vehicleNumber)) {
			sql += " and st.vehicle_number like "+SqlUtil.processLikeInjectionStatement(vehicleNumber);
		}
		String deviceVendor = device.getDeviceVendor();
		if (StringUtils.isNotBlank(deviceVendor)) {
			sql += " and st.device_vendor like "+SqlUtil.processLikeInjectionStatement(deviceVendor);
		}
		Long deviceStatus = device.getDeviceStatus();
		if (StringUtils.isNotBlank(TypeUtils.obj2String(deviceStatus))) {
			sql += " and st.device_status=?";
			params.add(deviceStatus);
		}
		sql += " order by id desc";
		Pagination page=new Pagination(sql, currentPage, numPerPage,DeviceModel.class,jdbcTemplate, params.toArray());
		return page.getResult();
//		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(BusiOrder.class), params.toArray());
	}

	@Override
	public Device updateDevice(Device device) {
		final String sql = "update sys_device set device_type=?, device_model=?, device_vendor_number=?, device_vendor=?, firmware_version=?, software_version=?, "
				+ "purchase_time=?, maintain_expire_time=?, iccid_number=?, sim_number=?,vehicle_id=?,device_status=?, device_batch=? where id=?";
	        jdbcTemplate.update(sql,
	        	device.getDeviceType(),
	        	device.getDeviceModel(),
	        	device.getDeviceVendorNumber(),
	        	device.getDeviceVendor(),
	        	device.getFirmwareVersion(),
	        	device.getSoftwareVersion(),
	        	new java.sql.Timestamp(device.getPurchaseTime().getTime()),
	        	new java.sql.Timestamp(device.getMaintainExpireTime().getTime()),
	        	device.getIccidNumber(),
	        	device.getSimNumber(),
	        	device.getVehicleId(),
	        	device.getDeviceStatus(),
	        	device.getDeviceBatch(),
	        	device.getId()
	            );
	        return device;
	}
	
	@Override
	public void updateLatestLimitSpeed(String imei, int latestLimitSpeed) {
		final String sql = "update sys_device set latest_limit_speed=? where imei_number=? or iccid_number=?";
	        jdbcTemplate.update(sql,  latestLimitSpeed, imei, imei);
	}

	@Override
	public Device findDeviceById(Long id) {
		final String sql = "select * from sys_device where id=?";
		List<Device> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Device.class),id);
        if (list.size() == 0)
        {
            return null;
        }
        return list.get(0);
	}

	@Override
	public Device updateDeviceLicesnse(Device device) {
        if (device.getStartTime()!=null && device.getEndTime()!=null) {
        	final String sql1 = "update sys_device set license_number=?, start_time=?, end_time=?,license_status=? where id=?";
        	jdbcTemplate.update(sql1,
            	device.getLicenseNumber(),
            	new java.sql.Timestamp(device.getStartTime().getTime()),
            	new java.sql.Timestamp(device.getEndTime().getTime()),
            	device.getLicenseStatus(),
            	device.getId()
             );
        } else {
        	final String sql2 = "update sys_device set license_number=?,start_time=?, end_time=?,license_status=? where id=?";
        	jdbcTemplate.update(sql2,
            	device.getLicenseNumber(),
            	device.getStartTime(),
            	device.getEndTime(),
            	device.getLicenseStatus(),
            	device.getId()
             );
        }
	   return device;
	}

	@Override
	public Device findDeviceByNo(String devNo) {
//		final String sql = "select * from sys_device where imei_number = ? or sn = ?";
		final String sql = "select * from sys_device where imei_number = ? or sn_number = ? or iccid_number = ?";
		List<Device> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(Device.class), devNo, devNo, devNo);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
	}
	
	@Override
	public List<Device> findDeviceBySimNumber(String simNumber) {
		final String sql = "select * from sys_device where sim_number = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Device.class), simNumber);
	}
	
	@Override
	public List<Device> findDeviceBySnNumber(String snNumber) {
		final String sql = "select * from sys_device where sn_number = ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper(Device.class), snNumber);
	}

	@Override
	public List<DeviceStatus> findLicenseListByImeiList(List<String> deviceNumberList) {
		
		StringBuffer paramBuffer = new StringBuffer();
		int deviceNumberListSize = deviceNumberList.size();
		for(int i = 0 ; i < deviceNumberListSize ; i ++){
			paramBuffer.append("'").append(deviceNumberList.get(i)).append("'");
			if(i !=  deviceNumberListSize - 1){
				paramBuffer.append(",");
			}
		}
		
		StringBuffer sqlBuffer = new StringBuffer("");
		sqlBuffer.append("		select license_number,sn_number,imei_number,iccid_number,device_vendor");
		sqlBuffer.append("		from"); 
		sqlBuffer.append("		sys_device");
		sqlBuffer.append("		where license_number is not null");
		sqlBuffer.append("		and");
		sqlBuffer.append("		(");
		sqlBuffer.append("			imei_number in");
		sqlBuffer.append("			(");
		sqlBuffer.append(paramBuffer.toString());
		sqlBuffer.append("			)");
		sqlBuffer.append("			or");
		sqlBuffer.append("			iccid_number in");
		sqlBuffer.append("			(");
		sqlBuffer.append(paramBuffer.toString());
		sqlBuffer.append("			)");
	    sqlBuffer.append("          or");
        sqlBuffer.append("          sn_number in");
        sqlBuffer.append("          (");
        sqlBuffer.append(paramBuffer.toString());
        sqlBuffer.append("          )");
		sqlBuffer.append("		)");
		
		List<DeviceStatus> retList = jdbcTemplate.query(sqlBuffer.toString(), new BeanPropertyRowMapper<DeviceStatus>(DeviceStatus.class));
		if(retList != null && retList.size() > 0){
			return retList;
		}
		return null;
	}

	@Override
	public DevcieCommandConfigRecord addDeviceCommandConfigRecord(final DevcieCommandConfigRecord deviceCommandConfigRecord) {
		final String sql =
	            "insert into device_command_config_record(device_number, command_type, command_value, command_send_time, command_response_time, command_send_status,command_excute_status, user_id) "
	            + "values(?,?,?,?,?,?,?,?)";
	        
	        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	        jdbcTemplate.update(new PreparedStatementCreator()
	        {
	            @Override
	            public PreparedStatement createPreparedStatement(Connection connection)
	                throws SQLException
	            {
	                PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
	                int count = 1;
	                psst.setString(count++, deviceCommandConfigRecord.getDeviceNumber());
	                psst.setString(count++, deviceCommandConfigRecord.getCommandType());
	                psst.setString(count++, deviceCommandConfigRecord.getCommandValue());
	                psst.setTimestamp(count++, new java.sql.Timestamp(deviceCommandConfigRecord.getCommandSendTime().getTime()));
	                if (null != deviceCommandConfigRecord.getCommandResponseTime()) {
	                	psst.setTimestamp(count++, new java.sql.Timestamp(deviceCommandConfigRecord.getCommandResponseTime().getTime()));
	                } else {
	                	psst.setTimestamp(count++, null);
	                }
	                psst.setString(count++, deviceCommandConfigRecord.getCommandSendStatus());
	                psst.setString(count++, deviceCommandConfigRecord.getCommandExcuteStatus());
	                psst.setLong(count, deviceCommandConfigRecord.getUserId());
	                return psst;
	            }
	        }, keyHolder);
	        
	        deviceCommandConfigRecord.setId(keyHolder.getKey().longValue());
	        return deviceCommandConfigRecord;
	}

	@Override
	public DevcieCommandConfigRecord updateDeviceCommandConfigRecord(DevcieCommandConfigRecord deviceCommandConfigRecord) {
		final String sql = "update device_command_config_record set device_number=?, command_type=?, command_value=?, command_send_time=?, command_response_time=?, "
				+ "command_send_status=?, command_excute_status=?, user_id=? where id=?";
	        jdbcTemplate.update(sql,
	        	deviceCommandConfigRecord.getDeviceNumber(),
	        	deviceCommandConfigRecord.getCommandType(),
	        	deviceCommandConfigRecord.getCommandValue(),
	        	new java.sql.Timestamp(deviceCommandConfigRecord.getCommandSendTime().getTime()),
	        	new java.sql.Timestamp(deviceCommandConfigRecord.getCommandResponseTime().getTime()),
	        	deviceCommandConfigRecord.getCommandSendStatus(),
	        	deviceCommandConfigRecord.getCommandExcuteStatus(),
	        	deviceCommandConfigRecord.getUserId(),
	        	deviceCommandConfigRecord.getId()
	            );
	        return deviceCommandConfigRecord;
	}

	@Override
	@SuppressWarnings("unchecked")
	public DevcieCommandConfigRecordModel findLatestWaittingCommandByImei(String imeiNumber) {
		final String sql = "select * from device_command_config_record where device_number = ? and upper(command_type) = 'SET_LIMIT_SPEED' and command_send_status = '000'"
						+ " and upper(command_excute_status) = 'EXCUTING' ORDER BY ID DESC limit 1 ";	
		List<DevcieCommandConfigRecordModel> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper(DevcieCommandConfigRecordModel.class),imeiNumber);
		if (list!=null && list.size()>0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Boolean iccidNumberIsExist(String iccidNumber) {
		List<Device> deviceList = jdbcTemplate.query("select id from sys_device where iccid_number=?",
	            new BeanPropertyRowMapper<Device>(Device.class), iccidNumber);
        if (deviceList != null && deviceList.size() > 0)
        {
            return true;
        }
        return false;
	}

	@Override
	public Device updateDeviceOfVehcleId(Device device) {
		try {
			final String sql = "update sys_device set vehicle_id=? where id=?";
			jdbcTemplate.update(sql,device.getVehicleId(),device.getId());
		    return device;
		} catch (Exception e) {
			LOG.error("DeviceService updateDeviceOfVehcleId error, cause by:\n", e);
			return null;
		}
	}
	
}
