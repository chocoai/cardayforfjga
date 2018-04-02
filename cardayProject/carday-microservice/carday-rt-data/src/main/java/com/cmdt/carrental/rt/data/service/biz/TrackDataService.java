package com.cmdt.carrental.rt.data.service.biz;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.rt.data.database.dao.TrackDataQueryDao;
import com.cmdt.carrental.rt.data.database.dto.TrackDataDTO;
import com.cmdt.carrental.rt.data.database.dto.TrackDataQueryDTO;

@Service
public class TrackDataService {
	
    private static final Logger LOG = LogManager.getLogger(TrackDataService.class);
    
    @Autowired
    private TrackDataQueryDao trackDataQueryDao;
    
    /**
     * getTrackData
     * @param dto
     * @return
     */
	public List<TrackDataDTO> getTrackData(TrackDataQueryDTO dto){
		LOG.debug("TrackDataService.getTrackData");
		if(StringUtils.isBlank(dto.getImei())){
			return null;
		}
		if(StringUtils.isBlank(dto.getStartTime())){
			return null;
		}
		if(StringUtils.isBlank(dto.getEndTime())){
			return null;
		}
		return trackDataQueryDao.getTrackData(dto);
	}
}	
	
	
