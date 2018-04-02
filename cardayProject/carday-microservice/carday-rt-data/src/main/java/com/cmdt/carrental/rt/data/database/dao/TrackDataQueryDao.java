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
import com.cmdt.carrental.rt.data.database.dto.TrackDataDTO;
import com.cmdt.carrental.rt.data.database.dto.TrackDataQueryDTO;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;

@Repository
public class TrackDataQueryDao {

	private static final Logger LOG = LogManager.getLogger(TrackDataQueryDao.class);
	
	@Resource(name="cassandraSessionFactory")
    private CassandraSessionFactory sessionFactory;
	
	public List<TrackDataDTO> getTrackData(TrackDataQueryDTO trackDataQueryDTO){
		LOG.debug("TrackDataQueryDao.getTrackData");
		List<TrackDataDTO> list = new ArrayList<TrackDataDTO>();
		String imei = trackDataQueryDTO.getImei();
		
		Statement select = QueryBuilder.select().all().from("rt_data_basic")
				.where(QueryBuilder.eq("imei",imei))
				.and(QueryBuilder.gte("trace_time",DateUtils.string2Date(trackDataQueryDTO.getStartTime(), DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS)))
				.and(QueryBuilder.lte("trace_time",DateUtils.string2Date(trackDataQueryDTO.getEndTime(), DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS)))
				.orderBy(QueryBuilder.asc("trace_time"));

		ResultSet resultSet = sessionFactory.getSession().execute(select);
		 for (Row row : resultSet) {
			 TrackDataDTO trackDataDTO = new TrackDataDTO();
			 trackDataDTO.setTraceTime(DateUtils.date2String(row.getTimestamp("trace_time"),DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS));
			 trackDataDTO.setSpeed(row.getInt("speed"));
			 if(trackDataDTO.getSpeed() > 0){
				 trackDataDTO.setStatus("运行中");
			 }else{
				 trackDataDTO.setStatus("停止");
			 }
			 trackDataDTO.setLongitude(row.getDouble("longitude"));
			 trackDataDTO.setLatitude(row.getDouble("latitude"));
			 list.add(trackDataDTO);
			}
		return list;
	}
	
}
