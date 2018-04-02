package com.cmdt.carrental.common.service;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.service.bean.TripInfo;
import com.cmdt.carrental.common.service.bean.TripDetailInfo;
import com.cmdt.carrental.common.service.bean.TripDetailTimePointInfo;
import com.cmdt.carrental.common.service.bean.TripDtcDetailInfo;
import com.cmdt.carrental.common.service.bean.TripDtcItemInfo;
import com.cmdt.carrental.common.service.bean.TripTraceDetailInfo;
import com.cmdt.carrental.common.service.bean.TripObdCache;
import com.cmdt.carrental.common.util.CsvUtil;
import com.cmdt.carrental.common.util.TypeUtils;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.io.WKTReader;

@Repository
public class DataFeed_Trip {
	private static final Logger LOG = LoggerFactory.getLogger(DataFeed_Trip.class);
	@Autowired
    public JdbcTemplate jdbcTemplate;
	public static final String fileDir="/tmp/data2/";
//	public static final String fileDir="/opt/data2/";
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try{
			GenericXmlApplicationContext context = new GenericXmlApplicationContext();
	        context.setValidating(false);
	        context.load("classpath*:spring-config.xml");
	        context.refresh();
	        LOG.debug("trip data import start.....................");
			DataFeed_Trip feed=context.getBean(DataFeed_Trip.class);
			feed.importData2();
			LOG.debug("trip data import successfully!");
			context.close();
		} catch (Exception e) {
			LOG.error("failed in DataFeed_Trip import data:",e);
		}
	}
	
	/**
	 * about trip data
	 * @throws Exception
	 */
	public void importData2() throws Exception{
//		String trip_info_file = fileDir+"trip_info.csv";
		String trip_detail_info_file = fileDir+"trip_detail_info.csv";
		String trip_detail_time_point_info_file = fileDir+"trip_detail_time_point_info.csv";
		
		String trip_dtc_detail_info_file = fileDir+"trip_dtc_detail_info.csv";
		String trip_dtc_item_info_file = fileDir+"trip_dtc_item_info.csv";
		
		String trip_trace_detail_info_file = fileDir+"trip_trace_detail_info.csv";
		
		String trip_obd_cache_file = fileDir+"trip_obd_cache.csv";
//		Map<Long, Long> map = importTripInfo(trip_info_file);
		Map<String, Long> map =loadTripInfo();
		Map<Long, Long> map_trip_detail_info = importTripDetailInfo(trip_detail_info_file, map);
		importTripDetailTimePointInfo(trip_detail_time_point_info_file, map_trip_detail_info);
		Map<Long, Long> map_trip_dtc_detail_info = importTripDtcDetailInfo(trip_dtc_detail_info_file, map);
		importTripDtcItemInfo(trip_dtc_item_info_file, map_trip_dtc_detail_info);
		importTripTraceDetailInfo(trip_trace_detail_info_file, map);
		importTripObdCache(trip_obd_cache_file);
	}
	
	public Map<String, Long> loadTripInfo() throws Exception{
		Map<String, Long> map=new HashMap<String, Long>();
		String sql="select id,imei from trip_info";
		LOG.debug("cache trip_info data start++++++");
		List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
		if(!list.isEmpty()){
			for(Map<String, Object> objMap:list){
				String imei=TypeUtils.obj2String(objMap.get("imei"));
				Long id=TypeUtils.obj2Long(objMap.get("id"));
				map.put(imei,id);
			}
		}
		LOG.debug("trip_info data num:"+map.size());
		LOG.debug("cache trip_info data end++++++");
		return map;
	}
	
	//暂时不用
	public Map<Long ,Long> importTripInfo(String trip_info_file) throws Exception{
		Map<Long, Long> map = new HashMap<Long, Long>();
		LOG.debug(" load trip_info_file start,location:"+trip_info_file);
		List<Object[]> list = CsvUtil.importCsv(new File(trip_info_file));
		int total=list.size();
		LOG.debug("trip_info total:"+total);
		LOG.debug("load trip_info_file end.....");
		int count=0;
		for (Object[] obj : list) {
			if(obj.length<4)
				continue;
			TripInfo data = new TripInfo();
			data.setOldId(TypeUtils.obj2Long(obj[0]));
			data.setImei(TypeUtils.obj2String(obj[1]));
			data.setStartTime(TypeUtils.obj2Timestamp(obj[2]));
			data.setEndTime(TypeUtils.obj2Timestamp(obj[3]));
			data.setCreatedTime(TypeUtils.obj2Timestamp(obj[4]));
			data = insertTripInfo(data);
			map.put(data.getOldId(), data.getId());
			count++;
		}
		LOG.debug("trip_info import num:"+count);
		return map;
	}
	
	public Map<Long ,Long> importTripDetailInfo(String trip_detail_info_file,Map<String ,Long> map) throws Exception{
		Map<Long, Long> mapd = new HashMap<Long, Long>();
		File file=new File(trip_detail_info_file);
		if(file.exists()){
			LOG.debug(" load trip_detail_info_file start,location:"+trip_detail_info_file);
			List<Object[]> list = CsvUtil.importCsv(file);
			int total=list.size();
			LOG.debug("trip_detail_info total:"+total);
			LOG.debug("load trip_detail_info_file end.....");
			int count=0;
			for (Object[] obj : list) {
				if(obj.length<22)
					continue;
				TripDetailInfo data = new TripDetailInfo();
				String imei = TypeUtils.obj2String(obj[2]);
				Long tripId = map.get(imei);
				//找不到对应trip_info数据就不作处理
				if (tripId != null){
					data.setTripId(tripId);
					data.setOldId(TypeUtils.obj2Long(obj[0]));
					data.setImei(TypeUtils.obj2String(obj[2]));
					data.setTripStartTime(TypeUtils.obj2Timestamp(obj[3]));
					data.setTripEndTime(TypeUtils.obj2Timestamp(obj[4]));
					data.setFuelCons(TypeUtils.obj2Integer(obj[5]));
					data.setMileage(TypeUtils.obj2Integer(obj[6]));
					data.setAvgFuelCons(TypeUtils.obj2Double(obj[7]));
					data.setCreatedTime(TypeUtils.obj2Timestamp(obj[8]));
					data.setFuelConsGap(TypeUtils.obj2Integer(obj[9]));
					data.setMileageGap(TypeUtils.obj2Integer(obj[10]));
					data.setFuelConsMax(TypeUtils.obj2Integer(obj[11]));
					data.setFuelConsMin(TypeUtils.obj2Integer(obj[12]));
					data.setMileageMax(TypeUtils.obj2Integer(obj[13]));
					data.setMileageMin(TypeUtils.obj2Integer(obj[14]));
					data.setLasttimestatus(TypeUtils.obj2String(obj[15]));
					data.setDriveTime(TypeUtils.obj2Integer(obj[16]));
					data.setIdletime(TypeUtils.obj2Integer(obj[17]));
					data.setEnginetime(TypeUtils.obj2Integer(obj[18]));
					data.setStoptime(TypeUtils.obj2Integer(obj[19]));
					data.setVoltMax(TypeUtils.obj2Double(obj[20]));
					data.setVoltMin(TypeUtils.obj2Double(obj[21]));
					data.setVoltAvg(TypeUtils.obj2Double(obj[22]));
					data = insertTripDetailInfo(data);
					mapd.put(data.getOldId(), data.getId());
					count++;
					if(count%10000==0){
						LOG.debug("trip_detail_info completion percentage:......"+df.format(((double)count/(double)total)*100)+"%");
					}
				}
			}
			LOG.debug("trip_detail_info completion percentage:......100%");
			LOG.debug("trip_detail_info import num:"+count);
		}
		return mapd;
	}
	
	public void importTripDetailTimePointInfo(String trip_detail_time_point_info_file,Map<Long ,Long> map) throws Exception{
		File file=new File(trip_detail_time_point_info_file);
		if(file.exists()){
			LOG.debug(" load trip_detail_time_point_info_file start,location:"+trip_detail_time_point_info_file);
			List<Object[]> list = CsvUtil.importCsv(file);
			int total=list.size();
			LOG.debug("trip_detail_time_point_info total:"+total);
			LOG.debug("load trip_detail_time_point_info_file end.....");
			int count=0;
			for (Object[] obj : list) {
				if(obj.length<10)
					continue;
				TripDetailTimePointInfo data = new TripDetailTimePointInfo();
				Long keyid = TypeUtils.obj2Long(obj[1]);
				Long newid = map.get(keyid);
				//找不到对应trip_detail_info数据就不作处理
				if (newid != null){
					data.setDetailId(newid);
					data.setTimeStatus(TypeUtils.obj2String(obj[2]));
					data.setBeginPoint(TypeUtils.obj2String(obj[3]));
					data.setEndPoint(TypeUtils.obj2String(obj[4]));
					data.setBeginTime(TypeUtils.obj2Timestamp(obj[5]));
					data.setEndTime(TypeUtils.obj2Timestamp(obj[6]));
					data.setMergeBeginPoint(TypeUtils.obj2String(obj[7]));
					data.setMergeBeginTime(TypeUtils.obj2Timestamp(obj[8]));
					data.setMergeFlag(TypeUtils.obj2Integer(obj[9]));
					data.setSecondsSeq(TypeUtils.obj2Integer(obj[10]));
					data = insertTripDetailTimePointInfo(data);
					count++;
					if(count%10000==0){
						LOG.debug("trip_detail_time_point_info completion percentage:......"+df.format(((double)count/(double)total)*100)+"%");
					}
				}
			}
			LOG.debug("trip_detail_time_point_info completion percentage:......100%");
			LOG.debug("trip_detail_time_point_info import num:"+count);
		}
	}

	public Map<Long ,Long> importTripDtcDetailInfo(String trip_dtc_detail_info_file,Map<String ,Long> map) throws Exception{
		Map<Long, Long> mapd = new HashMap<Long, Long>();
		File file=new File(trip_dtc_detail_info_file);
		if(file.exists()){
			LOG.debug(" load trip_dtc_detail_info_file start,location:"+trip_dtc_detail_info_file);
			List<Object[]> list = CsvUtil.importCsv(file);
			int total=list.size();
			LOG.debug("trip_dtc_detail_info total:"+total);
			LOG.debug("load trip_dtc_detail_info_file end.....");
			int count=0;
			for (Object[] obj : list) {
				if(obj.length<6)
					continue;
				TripDtcDetailInfo data = new TripDtcDetailInfo();
				String imei = TypeUtils.obj2String(obj[2]);
				Long tripId = map.get(imei);
				//找不到对应trip_info数据就不作处理
				if (tripId != null){
					data.setTripId(tripId);
					data.setOldId(TypeUtils.obj2Long(obj[0]));
					data.setImei(TypeUtils.obj2String(obj[2]));
					data.setTripStartTime(TypeUtils.obj2Timestamp(obj[3]));
					data.setTripEndTime(TypeUtils.obj2Timestamp(obj[4]));
					data.setDtcList(TypeUtils.obj2String(obj[5]));
					data.setCreatedTime(TypeUtils.obj2Timestamp(obj[6]));
					data = insertTripDtcDetailInfo(data);
					mapd.put(data.getOldId(), data.getId());
					count++;
					if(count%10000==0){
						LOG.debug("trip_dtc_detail_info completion percentage:......"+df.format(((double)count/(double)total)*100)+"%");
					}
				}
			}
			LOG.debug("trip_dtc_detail_info completion percentage:......100%");
			LOG.debug("trip_dtc_detail_info import num:"+count);
		}
		return mapd;
	}
	
	public void importTripDtcItemInfo(String trip_dtc_item_info_file,Map<Long ,Long> map) throws Exception{
		File file=new File(trip_dtc_item_info_file);
		if(file.exists()){
			LOG.debug(" load trip_dtc_item_info_file start,location:"+trip_dtc_item_info_file);
			List<Object[]> list = CsvUtil.importCsv(file);
			int total=list.size();
			LOG.debug("trip_dtc_item_info total:"+total);
			LOG.debug("load trip_dtc_item_info_file end.....");
			int count=0;
			for (Object[] obj : list) {
				if(obj.length<3)
					continue;
				TripDtcItemInfo data = new TripDtcItemInfo();
				Long keyid = TypeUtils.obj2Long(obj[1]);
				Long newid = map.get(keyid);
				//找不到对应trip_dtc_detail_info数据就不作处理
				if (newid != null){
					data.setDtcId(newid);
					data.setDtcCode(TypeUtils.obj2String(obj[2]));
					data.setTimes(TypeUtils.obj2Integer(obj[3]));
					data = insertTripDtcItemInfo(data);
					count++;
					if(count%10000==0){
						LOG.debug("trip_dtc_item_info completion percentage:......"+df.format(((double)count/(double)total)*100)+"%");
					}
				}
			}
			LOG.debug("trip_dtc_item_info completion percentage:......100%");
			LOG.debug("trip_dtc_item_info import num:"+count);
		}
	}
	
	public void importTripTraceDetailInfo(String trip_trace_detail_info_file,Map<String ,Long> map) throws Exception{
		File file=new File(trip_trace_detail_info_file);
		if(file.exists()){
			LOG.debug(" load trip_trace_detail_info_file start,location:"+trip_trace_detail_info_file);
			List<Object[]> list = CsvUtil.importCsv(file);
			int total=list.size();
			LOG.debug("trip_trace_detail_info total:"+total);
			LOG.debug("load trip_trace_detail_info_file end.....");
			int count=0;
			for (Object[] obj : list) {
				if(obj.length<8)
					continue;
				TripTraceDetailInfo data = new TripTraceDetailInfo();
				String imei = TypeUtils.obj2String(obj[2]);
				Long tripId = map.get(imei);
				//找不到对应trip_info数据就不作处理
				if (tripId != null){
					data.setTripId(tripId);
					data.setImei(TypeUtils.obj2String(obj[2]));
					data.setTripStartTime(TypeUtils.obj2Timestamp(obj[3]));
					data.setTripEndTime(TypeUtils.obj2Timestamp(obj[4]));
					data.setTraceGeometry(transTraceInfoToGeometry(TypeUtils.obj2String(obj[5])));
					data.setCreatedTime(TypeUtils.obj2Timestamp(obj[6]));
					data.setIdList(TypeUtils.obj2String(obj[7]));
					data.setTraceList(TypeUtils.obj2String(obj[8]));
					data = insertTripTraceDetailInfo(data);
					count++;
					if(count%10000==0){
						LOG.debug("trip_trace_detail_info completion percentage:......"+df.format(((double)count/(double)total)*100)+"%");
					}
				}
			}
			LOG.debug("trip_trace_detail_info completion percentage:......100%");
			LOG.debug("trip_trace_detail_info import num:"+count);
		}
	}

	private final static int SRID = 4326;
	public Geometry transTraceInfoToGeometry(String traceInfo){
		WKTReader fromText = new WKTReader();
        MultiPoint geom = null;
        try {
            geom = (MultiPoint)fromText.read(traceInfo);
            geom.setSRID(SRID);
        } catch (Exception e) {
        	LOG.error("Failed in transTraceInfoToGeometry:"+ e);
        }
        return geom;
	}
	
	public void importTripObdCache(String trip_obd_cache_file) throws Exception{
		File file=new File(trip_obd_cache_file);
		if(file.exists()){
			LOG.debug(" load trip_obd_cache_file start,location:"+trip_obd_cache_file);
			List<Object[]> list = CsvUtil.importCsv(file);
			int total=list.size();
			LOG.debug("trip_obd_cache total:"+total);
			LOG.debug("load trip_obd_cache_file end.....");
			int count=0;
			for (Object[] obj : list) {
				if(obj.length<7)
					continue;
				TripObdCache data = new TripObdCache();
				data.setImei(TypeUtils.obj2String(obj[1]));
				data.setVendor(TypeUtils.obj2String(obj[2]));
				data.setTripStartTime(TypeUtils.obj2Timestamp(obj[3]));
				data.setTripEndTime(TypeUtils.obj2Timestamp(obj[4]));
				data.setRecordOwner(TypeUtils.obj2String(obj[5]));
				data.setRecordState(TypeUtils.obj2String(obj[6]));
				data.setRecordComment(TypeUtils.obj2String(obj[7]));
				data = insertTripObdCache(data);
				count++;
				if(count%10000==0){
					LOG.debug("trip_obd_cache completion percentage:......"+df.format(((double)count/(double)total)*100)+"%");
				}
			}
			LOG.debug("trip_obd_cache completion percentage:......100%");
			LOG.debug("trip_obd_cache import num:"+count);
		}
	}
	
	public TripInfo insertTripInfo(final TripInfo data)
    {
        final String sql = "insert into trip_info(imei, ord_start_time,ord_end_time,created_time) values (?,?,?,?)";
        
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator()
        {
            public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException
            {
                PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setString(count++, data.getImei());
                psst.setTimestamp(count++, data.getStartTime());
                psst.setTimestamp(count++, data.getEndTime());
                psst.setTimestamp(count, data.getCreatedTime());
                return psst;
            }
        }, keyHolder);
        data.setId(keyHolder.getKey().longValue());
        return data;
    }
	
	public TripDetailInfo insertTripDetailInfo(final TripDetailInfo data)
    {
        final String sql = "insert into trip_detail_info(trip_id, imei,trip_start_time,trip_end_time,fuel_cons,mileage,avg_fuel_cons,created_time,fuel_cons_gap,mileage_gap,"
        		+ "fuel_cons_max,fuel_cons_min,mileage_max,mileage_min,last_time_status,drive_time,idle_time,engine_time,stop_time,volt_max,volt_min,volt_avg)"
        		+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator()
        {
            public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException
            {
                PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setLong(count++, data.getTripId());
                psst.setString(count++, data.getImei());
                psst.setTimestamp(count++, data.getTripStartTime());
                psst.setTimestamp(count++, data.getTripEndTime());
                psst.setLong(count++, data.getFuelCons());
                psst.setLong(count++, data.getMileage());
                psst.setDouble(count++, data.getAvgFuelCons());
                psst.setTimestamp(count++, data.getCreatedTime());
                psst.setLong(count++, data.getFuelConsGap());
                psst.setLong(count++, data.getMileageGap());
                psst.setLong(count++, data.getFuelConsMax());
                psst.setLong(count++, data.getFuelConsMin());
                psst.setLong(count++, data.getMileageMax());
                psst.setLong(count++, data.getMileageMin());
                psst.setString(count++, data.getLasttimestatus());
                psst.setInt(count++, data.getDriveTime());
                psst.setInt(count++, data.getIdletime());
                psst.setInt(count++, data.getEnginetime());
                psst.setInt(count++, data.getStoptime());
                psst.setDouble(count++, data.getVoltMax());
                psst.setDouble(count++, data.getVoltMin());
                psst.setDouble(count, data.getVoltAvg());
                return psst;
            }
        }, keyHolder);
        data.setId(keyHolder.getKey().longValue());
        return data;
    }
	
	public TripDetailTimePointInfo insertTripDetailTimePointInfo(final TripDetailTimePointInfo data)
    {
        final String sql = "insert into trip_detail_time_point_info(detail_id, time_status,begin_point,end_point,begin_time,end_time,merge_begin_point,"
        		+ "merge_begin_time,merge_flag,seconds_seq) values (?,?,?,?,?,?,?,?,?,?)";
        
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator()
        {
            public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException
            {
                PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setLong(count++, data.getDetailId());
                psst.setString(count++, data.getTimeStatus());
                psst.setString(count++, data.getBeginPoint());
                psst.setString(count++, data.getEndPoint());
                psst.setTimestamp(count++, data.getBeginTime());
                psst.setTimestamp(count++, data.getEndTime());
                psst.setString(count++, data.getMergeBeginPoint());
                psst.setTimestamp(count++, data.getMergeBeginTime());
                psst.setInt(count++, data.getMergeFlag());
                psst.setInt(count, data.getSecondsSeq());
                return psst;
            }
        }, keyHolder);
        data.setId(keyHolder.getKey().longValue());
        return data;
    }
	
	public TripDtcDetailInfo insertTripDtcDetailInfo(final TripDtcDetailInfo data)
    {
        final String sql = "insert into trip_dtc_detail_info(trip_id, imei,trip_start_time,trip_end_time,dtc_list,created_time) values (?,?,?,?,?,?)";
        
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator()
        {
            public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException
            {
                PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setLong(count++, data.getTripId());
                psst.setString(count++, data.getImei());
                psst.setTimestamp(count++, data.getTripStartTime());
                psst.setTimestamp(count++, data.getTripEndTime());
                psst.setString(count++, data.getDtcList());
                psst.setTimestamp(count, data.getCreatedTime());
                return psst;
            }
        }, keyHolder);
        data.setId(keyHolder.getKey().longValue());
        return data;
    }
	
	public TripDtcItemInfo insertTripDtcItemInfo(final TripDtcItemInfo data)
    {
        final String sql = "insert into trip_dtc_item_info(dtc_id, dtc_code,times) values (?,?,?)";
        
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator()
        {
            public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException
            {
                PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setLong(count++, data.getDtcId());
                psst.setString(count++, data.getDtcCode());
                psst.setInt(count, data.getTimes());
                return psst;
            }
        }, keyHolder);
        data.setId(keyHolder.getKey().longValue());
        return data;
    }
	
	public TripTraceDetailInfo insertTripTraceDetailInfo(final TripTraceDetailInfo data)
    {
        final String sql = "insert into trip_trace_detail_info(trip_id, imei,trip_start_time,trip_end_time,trace_geometry,created_time,id_list,trace_list) values (?,?,?,?,?,?,?,?)";
        
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator()
        {
            public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException
            {
                PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setLong(count++, data.getTripId());
                psst.setString(count++, data.getImei());
                psst.setTimestamp(count++, data.getTripStartTime());
                psst.setTimestamp(count++, data.getTripEndTime());
                psst.setObject(count++, data.getTraceGeometry(),java.sql.Types.OTHER);
                psst.setTimestamp(count++, data.getCreatedTime());
                psst.setString(count++, data.getIdList());
                psst.setString(count, data.getTraceList());
                return psst;
            }
        }, keyHolder);
        data.setId(keyHolder.getKey().longValue());
        return data;
    }
	
	public TripObdCache insertTripObdCache(final TripObdCache data)
    {
        final String sql = "insert into trip_obd_cache( imei,vendor,trip_start_time,trip_end_time,record_owner,record_state,record_comment) values (?,?,?,?,?,?,?)";
        
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator()
        {
            public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException
            {
                PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setString(count++, data.getImei());
                psst.setString(count++, data.getVendor());
                psst.setTimestamp(count++, data.getTripStartTime());
                psst.setTimestamp(count++, data.getTripEndTime());
                psst.setString(count++, data.getRecordOwner());
                psst.setString(count++, data.getRecordState());
                psst.setString(count, data.getRecordComment());
                return psst;
            }
        }, keyHolder);
        data.setId(keyHolder.getKey().longValue());
        return data;
    }
	
}
