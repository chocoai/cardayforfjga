package com.cmdt.carrental.rt.data.database.dao;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.rt.data.database.dao.sessionfactory.CassandraSessionFactory;
import com.cmdt.carrental.rt.data.database.dto.RealtimeDataDTO;
import com.cmdt.carrental.rt.data.database.dto.RealtimeQueryDTO;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;

@Repository
public class RealtimeDataQueryDao {
//	private static final Logger LOG = Logger.getLogger(RealtimeDataQueryDao.class);
    private static final Logger LOG = LogManager.getLogger(RealtimeDataQueryDao.class);

	@Resource(name="cassandraSessionFactory")
    private CassandraSessionFactory sessionFactory;
	
	public List<RealtimeDataDTO> getRealtimeData(RealtimeQueryDTO query) {
		LOG.debug("RealTimeDao.getRealtimeData");
		List<RealtimeDataDTO> list = new ArrayList<RealtimeDataDTO>();
		String imei = query.getImei();
		
		Statement select = QueryBuilder.select().all().from("rt_data_basic")
				.where(QueryBuilder.eq("imei",imei))
				.and(QueryBuilder.gte("trace_time",DateUtils.string2Date(query.getStartTime(), DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS)))
				.and(QueryBuilder.lte("trace_time",DateUtils.string2Date(query.getEndTime(), DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS)))
				.orderBy(QueryBuilder.asc("trace_time"));

		ResultSet resultSet = sessionFactory.getSession().execute(select);
		 for (Row row : resultSet) {
			 RealtimeDataDTO rl = new RealtimeDataDTO();
			 rl.setImei(row.getString("imei"));
			 rl.setVin(row.getString("vin"));
			 rl.setCreateTime(row.getTimestamp("create_time"));
			 rl.setTraceTime(DateUtils.date2String(row.getTimestamp("trace_time"),DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS));
			 rl.setMileage(row.getLong("mileage"));
			 rl.setLongitude(row.getDouble("longitude"));
			 rl.setLatitude(row.getDouble("latitude"));
			 rl.setUploadCode(row.getString("upload_code"));
			 
			 //TODO 补齐剩余fields
			 
			 list.add(rl);
			}
		return list;
	}
	
	
	
	public RealtimeDataDTO getLatestRealtimeData(RealtimeQueryDTO query) {
		LOG.debug("RealTimeDao.getLatestRealtimeData");
		RealtimeDataDTO rl = new RealtimeDataDTO();
		String imei = query.getImei();
		Statement select = QueryBuilder.select().all().from("latest_rt_data")
				.where(QueryBuilder.eq("imei",imei));
		ResultSet resultSet = sessionFactory.getSession().execute(select);
		if(resultSet != null && resultSet.iterator().hasNext()){
			Row row = resultSet.one();
			 rl.setImei(row.getString("imei"));
			 rl.setVin(row.getString("vin"));
			 rl.setUpdateTime(row.getTimestamp("update_time"));
			 rl.setTraceTime(DateUtils.date2String(row.getTimestamp("trace_time"),DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS));
			 rl.setMileage(row.getLong("mileage"));
			 rl.setLongitude(row.getDouble("longitude"));
			 rl.setLatitude(row.getDouble("latitude"));
			 rl.setUploadCode(row.getString("upload_code"));
			 
			 //TODO 补齐剩余fields
			 
			}
		return rl;
	}
}
