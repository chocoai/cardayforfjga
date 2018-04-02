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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.service.bean.DtcData;
import com.cmdt.carrental.common.service.bean.EventData;
import com.cmdt.carrental.common.service.bean.RealtimeData;
import com.cmdt.carrental.common.util.CsvUtil;
import com.cmdt.carrental.common.util.TypeUtils;

@Repository
public class DataFeed_RealTimeData {
	private static final Logger LOG = LoggerFactory.getLogger(DataFeed_RealTimeData.class);
	@Autowired
    public JdbcTemplate jdbcTemplate;
	public static final String fileDir="/tmp/data1/";
//	public static final String fileDir="/opt/data1/";
	private static final DecimalFormat df = new DecimalFormat("0.00");
//	@Value("${datafeed.fileDir}")
//    private String datafeed_fileDir;
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		try{
			GenericXmlApplicationContext context = new GenericXmlApplicationContext();
	        context.setValidating(false);
	        context.load("classpath*:spring-config.xml");
	        context.refresh();
			LOG.debug("realtime_data data import start.....................");
			DataFeed_RealTimeData feed=context.getBean(DataFeed_RealTimeData.class);
			feed.importData1();
			LOG.debug("realtime_data data import successfully!");
			context.close();
		} catch (Exception e) {
			LOG.error("failed in DataFeed_RealTimeData import data:",e);
		}
	}
	
	/**
	 * about realtime data
	 * @throws Exception
	 */
	public void importData1() throws Exception{
		///tmp/data
		String realtime_data_file = fileDir+"realtime_data.csv";
		String event_data_file = fileDir+"event_data.csv";
		String dtc_data_file = fileDir+"dtc_data.csv";
		Map<Long, Long> map = importRealtimeData(realtime_data_file);
		importEventData(event_data_file, map);
		importDtcData(dtc_data_file, map);
	}
	
	public Map<Long ,Long> importRealtimeData(String realtime_data_file) throws Exception{
		Map<Long, Long> map = new HashMap<Long, Long>();
		LOG.debug(" load realtime_data_file start,location:"+realtime_data_file);
		List<Object[]> realtime_dataList = CsvUtil.importCsv(new File(realtime_data_file));
		int total=realtime_dataList.size();
		LOG.debug("realtime_data total:"+total);
		LOG.debug("load realtime_data_file end.....");
		int count=0;
		for (Object[] obj : realtime_dataList) {
			if(obj.length<26)
				continue;
			RealtimeData data = new RealtimeData();
			data.setOldId(TypeUtils.obj2Long(obj[0]));
			data.setImei(TypeUtils.obj2String(obj[1]));
			data.setVin(TypeUtils.obj2String(obj[2]));
			data.setTraceTime(TypeUtils.obj2Date(obj[3]));
			Boolean isset = null;
			if (obj[4].equals("t")) {
				isset = true;
			} else if (obj[4].equals("f")) {
				isset = false;
			}
			data.setIsSet(isset);
			data.setSatelliteNum(TypeUtils.obj2Integer(obj[5]));
			data.setLongitude(TypeUtils.obj2Double(obj[6]));
			data.setLatitude(TypeUtils.obj2Double(obj[7]));
			data.setAltitude(TypeUtils.obj2Integer(obj[8]));
			data.setBearing(TypeUtils.obj2Integer(obj[9]));
			data.setHdop(TypeUtils.obj2Double(obj[10]));
			data.setVdop(TypeUtils.obj2Double(obj[11]));
			data.setFuelCons(TypeUtils.obj2Long(obj[12]));
			data.setMileage(TypeUtils.obj2Long(obj[13]));
			data.setSpeed(TypeUtils.obj2Integer(obj[14]));
			data.setRpm(TypeUtils.obj2Integer(obj[15]));
			data.setTemperature(TypeUtils.obj2Integer(obj[16]));
			data.setStatus(TypeUtils.obj2String(obj[17]));
			data.setExtVol(TypeUtils.obj2Double(obj[18]));
			data.setIntVol(TypeUtils.obj2Double(obj[19]));
			data.setEngineTime(TypeUtils.obj2Long(obj[20]));
			data.setDeviceName(TypeUtils.obj2String(obj[21]));
			data.setFwVer(TypeUtils.obj2String(obj[22]));
			data.setHwVer(TypeUtils.obj2String(obj[23]));
			data.setCreateTime(TypeUtils.obj2Date(obj[24]));
			data.setDtcs(TypeUtils.obj2String(obj[25]));
			data.setUploadCode(TypeUtils.obj2String(obj[26]));
			data = insertRealtimeData(data);
			map.put(data.getOldId(), data.getId());
			count++;
			if(count%10000==0){
				LOG.debug("realtime_data completion percentage:......"+df.format(((double)count/(double)total)*100)+"%");
			}
		}
		LOG.debug("realtime_data completion percentage:......100%");
		LOG.debug("realtime_data import num:"+count);
		return map;
	}

	public void importEventData(String event_data_file,Map<Long ,Long> map) throws Exception{
		File eventfile=new File(event_data_file);
		if(eventfile.exists()){
			LOG.debug(" load event_data_file start,location:"+event_data_file);
			List<Object[]> event_dataList = CsvUtil.importCsv(eventfile);
			int total=event_dataList.size();
			LOG.debug("event_data total:"+total);
			LOG.debug("load event_data_file end.....");
			int count=0;
			for (Object[] obj : event_dataList) {
				if(obj.length<4)
					continue;
				EventData data = new EventData();
				data.setEventId(TypeUtils.obj2Integer(obj[1]));
				Boolean event_flag = null;
				if (obj[2].equals("t")) {
					event_flag = true;
				} else if (obj[2].equals("f")) {
					event_flag = false;
				}
				data.setEventFlag(event_flag);
				data.setEventData(TypeUtils.obj2String(obj[3]));
				Long keyid = TypeUtils.obj2Long(obj[4]);
				Long newrid = map.get(keyid);
				//找不到对应realtime_data数据就不作处理
				if (newrid != null){
					data.setRealTimeId(newrid);
					data = insertEventData(data);
					count++;
					if(count%10000==0){
						LOG.debug("event_data completion percentage:......"+df.format(((double)count/(double)total)*100)+"%");
					}
				}
			}
			LOG.debug("event_data completion percentage:......100%");
			LOG.debug("event_data import num:"+count);
		}
	}
	
	public void importDtcData(String dtc_data_file,Map<Long ,Long> map) throws Exception{
		File dtcfile=new File(dtc_data_file);
		if(dtcfile.exists()){
			LOG.debug(" load dtc_data_file start,location:"+dtc_data_file);
			List<Object[]> dtc_dataList = CsvUtil.importCsv(dtcfile);
			int total=dtc_dataList.size();
			LOG.debug("dtc_data total:"+total);
			LOG.debug("load dtc_data_file end.....");
			int count=0;
			for (Object[] obj : dtc_dataList) {
				if(obj.length<2)
					continue;
				DtcData data = new DtcData();
				data.setDtc(TypeUtils.obj2String(obj[1]));
				Long keyid = TypeUtils.obj2Long(obj[2]);
				Long newrid = map.get(keyid);
				//找不到对应realtime_data数据就不作处理
				if (newrid != null){
					data.setRealTimeId(newrid);
					data = insertDtcData(data);
					count++;
					if(count%10000==0){
						LOG.debug("dtc_data completion percentage:......"+df.format(((double)count/(double)total)*100)+"%");
					}
				}
			}
			LOG.debug("dtc_data completion percentage:......100%");
			LOG.debug("dtc_data import num:"+count);
		}
	}
	
	public RealtimeData insertRealtimeData(final RealtimeData data) throws Exception
    {
        String sequenSql = "SELECT nextval('realtime_data_id_seq')";
        Long id=null;
        List<Long> idlist=jdbcTemplate.queryForList(sequenSql, Long.class);
        if(!idlist.isEmpty()){
        	id=idlist.get(0);
        	final String sql =
                    "insert into realtime_data(id,imei,vin,trace_time, is_set,satellite_num,longitude,latitude,altitude,bearing,hdop,vdop,fuel_cons,mileage,speed,rpm,temperature,status,"
                        + "ext_vol,int_vol,engine_time,device_name,fw_ver,hw_ver,create_time,dtcs,upload_code)"
                        + " values("+id+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
	        jdbcTemplate.update(new PreparedStatementCreator()
	        {
	            public PreparedStatement createPreparedStatement(Connection connection)
	                throws SQLException
	            {
	                PreparedStatement psst = connection.prepareStatement(sql); //NOSONAR statement and conn already been closed in jdbcTemplate update method
	                int count = 1;
	                psst.setString(count++, data.getImei());
	                psst.setString(count++, data.getVin());
	                psst.setTimestamp(count++, new java.sql.Timestamp(data.getTraceTime().getTime()));
	                psst.setObject(count++, data.getIsSet());
	                psst.setInt(count++, data.getSatelliteNum());
	                psst.setDouble(count++, data.getLongitude());
	                psst.setDouble(count++, data.getLatitude());
	                psst.setInt(count++, data.getAltitude());
	                psst.setInt(count++, data.getBearing());
	                psst.setDouble(count++, data.getHdop());
	                psst.setDouble(count++, data.getVdop());
	                psst.setLong(count++, data.getFuelCons());
	                psst.setLong(count++, data.getMileage());
	                psst.setLong(count++, data.getSpeed());
	                psst.setInt(count++, data.getRpm());
	                psst.setInt(count++, data.getTemperature());
	                psst.setString(count++, data.getStatus());
	                psst.setDouble(count++, data.getExtVol());
	                psst.setDouble(count++, data.getIntVol());
	                psst.setLong(count++, data.getEngineTime());
	                psst.setString(count++, data.getDeviceName());
	                psst.setString(count++, data.getFwVer());
	                psst.setString(count++, data.getHwVer());
	                psst.setTimestamp(count++, new java.sql.Timestamp(data.getCreateTime().getTime()));
	                psst.setString(count++, data.getDtcs());
	                psst.setString(count, data.getUploadCode());
	                return psst;
	            }
	        }, keyHolder);
	        data.setId(id);
//	        data.setId(keyHolder.getKey().longValue());
        }else{
        	throw new Exception();
        }
        return data;
    }
	
	public EventData insertEventData(final EventData data)
    {
        final String sql = "insert into event_data(event_id,event_flag,event_data, realtime_id) values (?,?,?,?)";
        
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator()
        {
            public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException
            {
                PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setInt(count++, data.getEventId());
                psst.setObject(count++, data.getEventFlag());
                psst.setString(count++, data.getEventData());
                psst.setLong(count, data.getRealTimeId());
                return psst;
            }
        }, keyHolder);
        data.setId(keyHolder.getKey().longValue());
        return data;
    }
	
	public DtcData insertDtcData(final DtcData data)
    {
        final String sql = "insert into dtc_data(dtc, realtime_id) values (?,?)";
        
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator()
        {
            public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException
            {
                PreparedStatement psst = connection.prepareStatement(sql, new String[] {"id"}); //NOSONAR statement and conn already been closed in jdbcTemplate update method
                int count = 1;
                psst.setString(count++, data.getDtc());
                psst.setLong(count, data.getRealTimeId());
                return psst;
            }
        }, keyHolder);
        data.setId(keyHolder.getKey().longValue());
        return data;
    }
	
}
