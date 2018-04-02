package com.cmdt.carrental.rt.data.database.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.rt.data.database.dao.sessionfactory.CassandraSessionFactory;
import com.cmdt.carrental.rt.data.database.dto.DtcDataDTO;
import com.cmdt.carrental.rt.data.database.dto.Event;
import com.cmdt.carrental.rt.data.database.dto.EventDataDTO;
import com.cmdt.carrental.rt.data.database.dto.RealtimeDataDTO;
import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.querybuilder.Batch;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;

/**
 * This DAO is supposed to be used to save all uploaded realtime data into database.
 * 
 * @author lisen
 *
 */
@Repository
public class RealtimeDataProcessDao {
	private static Logger LOG = Logger.getLogger(RealtimeDataProcessDao.class);
		
		@Resource(name="cassandraSessionFactory")
	    private CassandraSessionFactory sessionFactory;
		
		
		/**
		 * 新增设备网关发过来的最新的实时数据至数据库表中，且在latest表更新最新数据记录
		 * 
		 * @param dataPackage 解析后的车辆实时数据数据包
		 */
		public void saveRealtime(RealtimeDataDTO dataPackage) {
			LOG.debug("RealtimeDataProcessDao.saveRealtime");
			
			if (dataPackage == null) {
				return;
			}
		
			// 所有子表的数据创建时间保持一致
			Date createTime = Calendar.getInstance().getTime();
			dataPackage.setCreateTime(createTime);
			dataPackage.setUpdateTime(createTime);
			
			// 所有表的数据流水号保持一致
			//String tspSN = UUID.randomUUID().toString();
			//dataPackage.setTspSn(dataPackage.getTspSn());
			
			// 准备Insert statement，如果data package子对象不为空，则插入对应的数据库表
			// rt_basic
			Insert stmtInsertRealtimeBacis = getInsertRealtimeBasicStmt(dataPackage);
					
			List<Insert> batchInsertList = new ArrayList<Insert>();
			if (stmtInsertRealtimeBacis != null) {
				batchInsertList.add( stmtInsertRealtimeBacis);
			}
			
			
//			Update stmtUpdateLatestRtBasic = getUpdateLatestRtBasic(dataPackage);
//			
//			List<Update> batchUpdateList = new ArrayList<Update>();
//			if (stmtUpdateLatestRtBasic != null) {
//				batchUpdateList.add( stmtUpdateLatestRtBasic);
//			}
			
			ArrayList<RegularStatement> rtBatchList = new ArrayList<RegularStatement>();
			
			rtBatchList.addAll(batchInsertList);
//			rtBatchList.addAll(batchUpdateList);
			
			//Insert[] batchInsert =   batchList.toArray(new Insert[batchList.size()]);
			
			RegularStatement[] rtBatch =   rtBatchList.toArray(new RegularStatement[rtBatchList.size()]);
			Batch batch = QueryBuilder.batch(rtBatch);
			sessionFactory.getSession().execute(batch);
		}
		
		
		
		/**
		 * 保存设备事件数据
		 * @param dataPackage
		 */
		public void saveEventData(EventDataDTO dataPackage) {
			LOG.debug("RealtimeDataProcessDao.saveEventData");
			
			if (dataPackage == null) {
				return;
			}
		
			// 所有子表的数据创建时间保持一致
			Date createTime = Calendar.getInstance().getTime();
			dataPackage.setCreateTime(createTime);
			
			// 所有表的数据流水号保持一致
			//String tspSN = UUID.randomUUID().toString();
			//dataPackage.setTspSn(dataPackage.getTspSn());
			
			// 准备Insert statement，如果data package子对象不为空，则插入对应的数据库表
			// rt_basic
			List<Insert> stmtInsertRealtimeBacis = getInsertRealtimeEventStmt(dataPackage);
						
//			List<Insert> batchInsertList = new ArrayList<Insert>();
//			if (stmtInsertRealtimeBacis != null && !stmtInsertRealtimeBacis.isEmpty()) {
//				batchInsertList.addAll(stmtInsertRealtimeBacis);
//			}
			
			ArrayList<RegularStatement> rtBatchList = new ArrayList<RegularStatement>();
			if (stmtInsertRealtimeBacis != null && !stmtInsertRealtimeBacis.isEmpty()) {
				rtBatchList.addAll(stmtInsertRealtimeBacis);
			}
			
			//Insert[] batchInsert =   batchList.toArray(new Insert[batchList.size()]);
			
			RegularStatement[] rtBatch =   rtBatchList.toArray(new RegularStatement[rtBatchList.size()]);
			Batch batch = QueryBuilder.batch(rtBatch);
			sessionFactory.getSession().execute(batch);
		}
		
		
		/**
		 * 保存远程诊断-故障编码数据
		 * @param dataPackage
		 */
		public void saveDtcData(DtcDataDTO dataPackage) {
			LOG.debug("RealtimeDataProcessDao.saveDtcData");
			
			if (dataPackage == null) {
				return;
			}
		
			// 所有子表的数据创建时间保持一致
			Date createTime = Calendar.getInstance().getTime();
			dataPackage.setCreateTime(createTime);
			
			// 准备Insert statement，如果data package子对象不为空，则插入对应的数据库表
			// rt_basic
			List<Insert> stmtInsertRealtimeBacis = getInsertRealtimeDtcStmt(dataPackage);
						
//			List<Insert> batchInsertList = new ArrayList<Insert>();
//			if (stmtInsertRealtimeBacis != null && !stmtInsertRealtimeBacis.isEmpty()) {
//				batchInsertList.addAll(stmtInsertRealtimeBacis);
//			}
			
			ArrayList<RegularStatement> rtBatchList = new ArrayList<RegularStatement>();
			if (stmtInsertRealtimeBacis != null && !stmtInsertRealtimeBacis.isEmpty()) {
				rtBatchList.addAll(stmtInsertRealtimeBacis);
			}
			
			//Insert[] batchInsert =   batchList.toArray(new Insert[batchList.size()]);
			
			RegularStatement[] rtBatch =   rtBatchList.toArray(new RegularStatement[rtBatchList.size()]);
			Batch batch = QueryBuilder.batch(rtBatch);
			sessionFactory.getSession().execute(batch);
		}
		
		
		/**
		 * getInsertRealtimeEventStmt
		 * @param dataPackage
		 * @return
		 */
		private List<Insert> getInsertRealtimeEventStmt(EventDataDTO dataPackage)  {
			List<Insert> insertList = null;
			if (dataPackage == null || dataPackage.getImei() == null || dataPackage.getEvent() == null) {
				return insertList;
			}
			
			insertList = new ArrayList<Insert>();
			for(Event e : dataPackage.getEvent()){
				String[] names = new String[] { 
						"imei",
						"trace_time",
						"create_time",
						"event_id",
						"event_flag",
						"event_data"
						 };
				Object[] values = new Object[] {
						dataPackage.getImei(),
						DateUtils.string2Date(dataPackage.getTraceTime(),DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS), 
						dataPackage.getCreateTime(),
						e.getEventId(),
						e.getEventFlag(), 
						e.getEventData()
						};
				insertList.add(QueryBuilder.insertInto("rt_data_event").values(names, values));
			}
			
			return insertList;
		}
		
		
		/**
		 * getInsertRealtimeDtcStmt
		 * @param dataPackage
		 * @return
		 */
		private List<Insert> getInsertRealtimeDtcStmt(DtcDataDTO dataPackage)  {
			List<Insert> insertList = null;
			if (dataPackage == null || dataPackage.getImei() == null || StringUtils.isBlank(dataPackage.getDtc())) {
				return insertList;
			}
			
			insertList = new ArrayList<Insert>();
			//split dtcs by ; char
			String[] dtcs = dataPackage.getDtc().split(";");
			for(String dtcCode : dtcs){
				String[] names = new String[] { 
						"imei",
						"trace_time",
						"create_time",
						"dtc"
						 };
				Object[] values = new Object[] {
						dataPackage.getImei(),
						DateUtils.string2Date(dataPackage.getTraceTime(),DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS), 
						dataPackage.getCreateTime(),
						dtcCode
						};
				insertList.add(QueryBuilder.insertInto("rt_data_dtc").values(names, values));
			}
			return insertList;
		}
		
		
		/**
		 * getInsertRealtimeBasicStmt
		 * @param dataPackage
		 * @return
		 */
		private Insert getInsertRealtimeBasicStmt(RealtimeDataDTO dataPackage)  {
			if (dataPackage == null || dataPackage.getImei() == null ) {
				return null;
			}
			String[] names = new String[] { 
					"imei",
					"vin",
					"trace_time",
					"create_time",
					"mileage",
					"is_set",
					"satellite_num",
					"longitude",
					"latitude",
					"altitude",
					"bearing",
					"hdop",
					"vdop",
					"fuel_cons",
					"speed",
					"rpm",
					"temperature",
					"status",
					"ext_vol",
					"int_vol",
					"engine_time",
					"device_name",
					"fw_ver",
					"hw_ver",
					"dtcs",
					"upload_code"
					 };
			Object[] values = new Object[] {
					dataPackage.getImei(),
					dataPackage.getVin(),
					DateUtils.string2Date(dataPackage.getTraceTime(),DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS), 
					dataPackage.getCreateTime(),
					dataPackage.getMileage(),
					dataPackage.getIsSet(), 
					dataPackage.getSatelliteNum(),
					dataPackage.getLongitude(),
					dataPackage.getLatitude(),
					dataPackage.getAltitude(),
					dataPackage.getBearing(),
					dataPackage.getHdop(),
					dataPackage.getVdop(),
					dataPackage.getFuelCons(),
					dataPackage.getSpeed(),
					dataPackage.getRpm(),
					dataPackage.getTemperature(),
					dataPackage.getStatus(),
					dataPackage.getExtVol(),
					dataPackage.getIntVol(),
					dataPackage.getEngineTime(),
					dataPackage.getDeviceName(),
					dataPackage.getFwVer(),
					dataPackage.getHwVer(),
					dataPackage.getDtcs(),
					dataPackage.getUploadCode()
					};
			return QueryBuilder.insertInto("rt_data_basic").values(names, values);
		}
		
		
//		/**
//		 * getUpdateLatestRtBasic
//		 * @param dataPackage
//		 * @return
//		 */
//		private Update getUpdateLatestRtBasic(RealtimeDataDTO dataPackage) {
//			if (dataPackage == null || dataPackage.getImei() == null ) {
//				return null;
//			}
//			
//			// Update 时，set的value不能带主键
//			Update updateQuery = QueryBuilder.update("latest_rt_data");
//			updateQuery
//				.with(set("trace_time", DateUtils.string2Date(dataPackage.getTraceTime(),DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS)))
//				.and( set("update_time", dataPackage.getUpdateTime() ) )
//				.and( set("vin", dataPackage.getVin() ) )
//				.and( set("mileage", dataPackage.getMileage() ) )
//				.and( set("is_set", dataPackage.getIsSet() ) )
//				.and( set("satellite_num", dataPackage.getSatelliteNum() ) )
//				.and( set("longitude", dataPackage.getLongitude() ) )
//				.and( set("latitude", dataPackage.getLatitude() ) )
//				.and( set("altitude", dataPackage.getAltitude() ) )
//				.and( set("bearing", dataPackage.getBearing() ) )
//				.and( set("hdop", dataPackage.getHdop() ) )
//				.and( set("vdop", dataPackage.getVdop() ) )
//				.and( set("fuel_cons", dataPackage.getFuelCons() ) )
//				.and( set("speed", dataPackage.getSpeed() ) )
//				.and( set("rpm", dataPackage.getRpm() ) )
//				.and( set("temperature", dataPackage.getTemperature() ) )				
//				.and( set("status", dataPackage.getStatus() ) )
//				.and( set("ext_vol", dataPackage.getExtVol() ) )
//				.and( set("int_vol", dataPackage.getIntVol() ) )
//				.and( set("engine_time", dataPackage.getEngineTime() ) )
//				.and( set("device_name", dataPackage.getDeviceName() ) )
//				.and( set("fw_ver", dataPackage.getFwVer() ) )
//				.and( set("hw_ver", dataPackage.getHwVer() ) )
//				.and( set("dtcs", dataPackage.getDtcs() ) )
//				.and( set("upload_code", dataPackage.getUploadCode() ) )
//				.and( set("fact_fuel", dataPackage.getFactFuel() ) )
//				.and( set("fact_mileage", dataPackage.getFactMileage() ) )
//				.where(eq("imei", dataPackage.getImei()));
//			
//			return updateQuery;
//		}
}