package com.cmdt.carrental.rt.data.service.biz;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.rt.data.database.dao.RealtimeDataProcessDao;
import com.cmdt.carrental.rt.data.database.dao.RealtimeDataQueryDao;
import com.cmdt.carrental.rt.data.database.dto.DtcDataDTO;
import com.cmdt.carrental.rt.data.database.dto.EventDataDTO;
import com.cmdt.carrental.rt.data.database.dto.RealtimeDataDTO;
import com.cmdt.carrental.rt.data.database.dto.RealtimeQueryDTO;

@Service
public class RealtimeDataService {
	
//	private static final Logger LOG = LoggerFactory.getLogger(RealtimeDataService.class);
    private static final Logger LOG = LogManager.getLogger(RealtimeDataService.class);
    
    @Autowired
    private ObdHistoryRecordService obdHistoryRecordService;
    @Autowired
    private RealtimeValidDataService realtimeValidDataService;
    @Autowired
    private RealtimeDataQueryDao dataQueryDao;
    @Autowired
    private RealtimeDataProcessDao dataProcessDao;
    
    
    /**
     * queryOrderData
     * @param dto
     * @return
     */
	public List<RealtimeDataDTO> queryRealtimeData(RealtimeQueryDTO dto){
		LOG.debug("RealtimeDataService.queryRealtimeData");
		if(StringUtils.isBlank(dto.getImei())){
			return null;
		}
		//查询列表portal,app通用
		return dataQueryDao.getRealtimeData(dto);
	}
	
	
	/**
	 * queryLatestRealtime
	 * @param dto
	 * @return
	 */
	public RealtimeDataDTO queryLatestRealtime(RealtimeQueryDTO dto){
		LOG.debug("RealtimeDataService.queryLatestRealtime");
		if(StringUtils.isBlank(dto.getImei())){
			return null;
		}
		//查询列表portal,app通用
		return dataQueryDao.getLatestRealtimeData(dto);
	}
	
	
	/**
	 * persistRealtimeData
	 * @param dto
	 * @throws Exception
	 */
	public void persistRealtimeData(RealtimeDataDTO dto){
		LOG.debug("RealtimeDataService.persistRealtimeData");
		//保存实时数据
		dataProcessDao.saveRealtime(dto);
		//更新realtime_valid_data
		obdHistoryRecordService.processObdReplacement(dto);
		//更新OBD_history_record
		realtimeValidDataService.processRealtimeVlaidData(dto);
	}
	
	
	/**
	 * persistEventData
	 * @param dto
	 */
	public void persistEventData(EventDataDTO dto){
		LOG.debug("RealtimeDataService.persistEventData");
		//保存实时数据
		dataProcessDao.saveEventData(dto);
	}
	
	
	/**
	 * persistDtcData
	 * @param dto
	 */
	public void persistDtcData(DtcDataDTO dto){
		LOG.debug("RealtimeDataService.persistDtcData");
		//保存实时数据
		dataProcessDao.saveDtcData(dto);
	}
}
