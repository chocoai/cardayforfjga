package com.cmdt.carrental.rt.data.service.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.rt.data.database.dao.RealtimeDataQueryDao;
import com.cmdt.carrental.rt.data.database.dao.RealtimeValidDataDao;
import com.cmdt.carrental.rt.data.database.dto.RealtimeDataDTO;
import com.cmdt.carrental.rt.data.database.dto.RealtimeQueryDTO;
import com.cmdt.carrental.rt.data.database.po.LRealtimeDataBasic;

@Service
public class RealtimeValidDataService {

    @Autowired
    private RealtimeDataQueryDao realtimeDataQueryDao;
    
    @Autowired
    private RealtimeValidDataDao realtimeValidDataDao;
	
	public void processRealtimeVlaidData(RealtimeDataDTO realtime){
		
		RealtimeQueryDTO queryDto = new RealtimeQueryDTO();
		queryDto.setImei(realtime.getImei());
		RealtimeDataDTO latestData = realtimeDataQueryDao.getLatestRealtimeData(queryDto);
		
		if(null!=latestData){
			LRealtimeDataBasic data = new LRealtimeDataBasic();
			//todo:compose dto
			realtimeValidDataDao.save(data);
		}else{
			
		}
	}
	
}
