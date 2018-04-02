package com.cmdt.carrental.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

import com.cmdt.carrental.common.entity.DialCenter;
import com.cmdt.carrental.common.entity.UserInfo;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.model.DialCenterQueryDTO;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.util.Pagination;
import com.cmdt.carrental.common.util.SqlUtil;

@Repository
public class DialCenterDaoImpl implements DialCenterDao {
	private static final Logger LOG = LoggerFactory.getLogger(DialCenterDaoImpl.class);
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public PagModel findAllDialRecorder(DialCenterQueryDTO dto, Integer currentPage, Integer numPerPage) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder sb=new StringBuilder();
		sb.append("select * from dial_center where 1=1");
		if (StringUtils.isNotBlank(dto.getDialPhone())) {
			sb.append(" and dial_phone like "+SqlUtil.processLikeInjectionStatement(dto.getDialPhone()));
		}
		if (StringUtils.isNotBlank(dto.getDialName())) {
			sb.append(" and dial_name like "+SqlUtil.processLikeInjectionStatement(dto.getDialName()));
		}
		if (null!=dto.getStartTime()) {
			sb.append(" and dial_time >=?");
			params.add(new java.sql.Timestamp(dto.getStartTime().getTime()));
		}
		if (null!=dto.getEndTime()) {
			sb.append(" and dial_time <=?");
			params.add(new java.sql.Timestamp(dto.getEndTime().getTime()));
		}
		sb.append(" order by id desc");
		Pagination page=new Pagination(sb.toString(), currentPage, numPerPage,DialCenter.class,jdbcTemplate, params.toArray());
		
		PagModel pageModel = page.getResult();
		return pageModel;
	}
		
	@Override
	public UserInfo findUserInfo(String dialPhone){
		String sql = "SELECT c.id, c.organization_id as organizationId,c.realname as realName, c.phone as phone, b.name as company from sys_user c join sys_organization b on b.id = c.organization_id where c.phone=?";
        List<UserInfo> userInfoList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<UserInfo>(UserInfo.class), dialPhone);
        if(userInfoList.size() == 0) {
            return null;
        }
        return userInfoList.get(0);
	}
	
	@Override
	public Vehicle findVehicleInfo(String orderNo){
		String sql = "SELECT c.id,c.vehicle_number as vehicleNumber,c.device_number as deviceNumber from busi_vehicle c join busi_order b on b.vehicle_id = c.id where b.order_no=?";
        List<Vehicle> vehicleInfoList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<Vehicle>(Vehicle.class), orderNo);
        if(vehicleInfoList.size() == 0) {
            return null;
        }
        return vehicleInfoList.get(0);
	}

	@Override
	public DialCenter addDialRecord(final DialCenter dialCenter) {
		try {
			final String sql = "insert into dial_center (dial_time,dial_name,dial_organization,dial_phone,dial_type,dial_content,vehicle_number,order_no,device_no,deal_result,recorder) values (?,?,?,?,?,?,?,?,?,?,?)";
			GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement psst = connection.prepareStatement(sql, new String[] { "id" }); //NOSONAR statement and conn already been closed in jdbcTemplate update method
					int count = 1;
					psst.setTimestamp(count++, new java.sql.Timestamp(dialCenter.getDialTime().getTime()));
					psst.setString(count++, dialCenter.getDialName());
					psst.setString(count++, dialCenter.getDialOrganization());
					psst.setString(count++, dialCenter.getDialPhone());
					psst.setString(count++, dialCenter.getDialType());
					psst.setString(count++, dialCenter.getDialContent());
					psst.setString(count++, dialCenter.getVehicleNumber());
					psst.setString(count++, dialCenter.getOrderNo());
					psst.setString(count++, dialCenter.getDeviceNo());
					psst.setString(count++, dialCenter.getDealResult());
					psst.setString(count, dialCenter.getRecorder());
					return psst;
				}
			}, keyHolder);
			dialCenter.setId(keyHolder.getKey().longValue());
			return dialCenter;
		} catch (Exception e) {
			LOG.error("DialCenterService addDialRecord error, cause by:\n", e);
			return null;
		}
	}
	
	@Override
	public DialCenter findDialCenter(Long dialCenterId) {
		String sql = "SELECT id,dial_time as dialTime, dial_name as dialName, dial_organization as dialOrganization, dial_phone as dialPhone, dial_type as dialType, dial_content as dialContent, vehicle_number as vehicleNumber, order_no as orderNo, device_no as deviceNo, deal_result as dealResult, recorder  from dial_center where id=?";
        List<DialCenter> dialCenterList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<DialCenter>(DialCenter.class), dialCenterId);
        if(dialCenterList.size() == 0) {
            return null;
        }
        return dialCenterList.get(0);
	}	
	
	public DialCenter updateDialCenter(DialCenter dialCenter){
		try {			
				String sqlUpdate = "UPDATE dial_center SET dial_time=?, dial_name=?, dial_organization=?, dial_phone=?, dial_type=?, dial_content=?,vehicle_number=?,order_no=?,device_no=?, deal_result=?,recorder=? where id=?";
		        jdbcTemplate.update(
		        		sqlUpdate,
		        		dialCenter.getDialTime(),dialCenter.getDialName(),dialCenter.getDialOrganization(),dialCenter.getDialPhone(),dialCenter.getDialType(),dialCenter.getDialContent(),dialCenter.getVehicleNumber(),dialCenter.getOrderNo(),dialCenter.getDeviceNo(),dialCenter.getDealResult(),dialCenter.getRecorder(),dialCenter.getId());
		        
		        return dialCenter;

		} catch (Exception e) {
			LOG.error("dialCenterService updateDialCenter error, cause by:\n", e);
			return null;
		}
	}
	
	@Override
	public void deleteDialCenter(DialCenter dialCenter) {
				
		try {	        
				 String sqlDelete = "delete from dial_center where id=?";
			     jdbcTemplate.update(sqlDelete,dialCenter.getId());

		} catch (Exception e) {
			LOG.error("dialCenterService deleteDialCenter error, cause by:\n", e);
		}
	}

}