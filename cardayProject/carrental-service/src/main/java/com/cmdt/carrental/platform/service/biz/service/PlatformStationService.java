package com.cmdt.carrental.platform.service.biz.service;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.entity.Station;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.StationQueryDto;
import com.cmdt.carrental.common.service.StationService;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.platform.service.model.request.station.StationCreateDto;
import com.cmdt.carrental.platform.service.model.request.station.StationPageDto;
import com.cmdt.carrental.platform.service.model.request.station.StationUnAssignVehicleDto;
import com.cmdt.carrental.platform.service.model.request.station.StationUpdateDto;
import com.cmdt.carrental.platform.service.model.request.station.StationVehicleDto;
import com.cmdt.carrental.platform.service.model.request.station.StationVehiclePageDto;

@Service
public class PlatformStationService {
	private static final Logger LOG = LoggerFactory.getLogger(PlatformStationService.class);

	@Autowired
	private StationService stationService;
	
	@Autowired
	private VehicleService vehicleService;

	public PagModel findStationsByName(User loginUser, StationPageDto stationPageDto) {
		try{
			stationPageDto.setOrganizationId(loginUser.getOrganizationId());
			StationQueryDto stationQueryDto=new StationQueryDto();
			BeanUtils.copyProperties(stationQueryDto, stationPageDto);
			PagModel stations = this.stationService.findStationsByName(stationQueryDto);
			
			if ((stations != null) && (!stations.getResultList().isEmpty())) {
				for (Station station : (List<Station>)stations.getResultList()) {
					List vehicles = this.stationService.findStationAssignedVehiclesSum(station.getId().toString(),
							loginUser.getOrganizationId(), Boolean.valueOf(loginUser.isRentAdmin()),
							Boolean.valueOf(loginUser.isEntAdmin()));
					if ((vehicles != null) && (!vehicles.isEmpty()))
						station.setAssignedVehicleNumber(Integer.toString(vehicles.size()));
					else {
						station.setAssignedVehicleNumber("0");
					}
				}
			}
			return stations;
		}catch(Exception e){
			LOG.error("PlatformStationService findStationsByName error, cause by: ", e);
        	throw new ServerException(Constants.API_MESSAGE_FAILURE); 
		}
	}

	public List<Station> listByOrgId(Long organizationId){
		try{
    		return stationService.findStationsByOrgId(String.valueOf(organizationId));
    		
    	}catch(Exception e){
    		LOG.error("PlatformStationService listByOrgId error : ",e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE); 
    	}
	}
	
	public Station createStation(User loginUser, StationCreateDto stationCreateDto) {
		try{
			Station station = new Station();
			BeanUtils.copyProperties(station, stationCreateDto);
			if(loginUser.getOrganizationId() != null){
				station.setOrganizationId(loginUser.getOrganizationId());
			}
			return this.stationService.createStation(station);
			
		}catch(Exception e){
    		LOG.error("PlatformStationService createStation error : ",e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE); 
    	}
	}

	public Station findById(Long id){
		try{
			return stationService.findStation(id);
		}catch(Exception e){
    		LOG.error("PlatformStationService findById error : ",e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE); 
    	}
	}
	
	
	public Station updateStation(User loginUser, StationUpdateDto stationUpdateDto) {
		try{
			Station station = this.stationService.findStation(stationUpdateDto.getId());
			if(stationUpdateDto.getStationName()!=null){
				station.setStationName(stationUpdateDto.getStationName());
			}
			
			if(stationUpdateDto.getCity()!=null){
				station.setCity(stationUpdateDto.getCity());
			}
			
			if(stationUpdateDto.getAreaId()!=null){
				station.setAreaId(stationUpdateDto.getAreaId());
			}
			if(stationUpdateDto.getPosition()!=null){
				station.setPosition(stationUpdateDto.getPosition());
			}
			if(stationUpdateDto.getLongitude()!=null){
				station.setLongitude(stationUpdateDto.getLongitude());
			}
			if(stationUpdateDto.getLatitude()!=null){
				station.setLatitude(stationUpdateDto.getLatitude());
			}
			if(stationUpdateDto.getRadius()!=null){
				station.setRadius(stationUpdateDto.getRadius());
			}
			if(stationUpdateDto.getCarNumber()!=null){
				station.setCarNumber(stationUpdateDto.getCarNumber());
			}
			if(loginUser.getOrganizationId() != null){
				station.setOrganizationId(loginUser.getOrganizationId());
			}
				
			return this.stationService.updateStation(station);
			
		}catch(Exception e){
    		LOG.error("PlatformStationService updateStation error : ",e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE); 
    	}
	}

	public void deleteStation(Long id) {
		try{
			Station station = this.stationService.findStation(id);
			this.stationService.deleteStation(station);
		}catch(Exception e){
    		LOG.error("PlatformStationService deleteStation error : ",e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE); 
    	}
	}

	public PagModel findStationAssignedVehicles(User loginUser, StationVehiclePageDto stationVehiclePageDto) {
		try{
			StationQueryDto stationQueryDto =new StationQueryDto();
			BeanUtils.copyProperties(stationQueryDto, stationVehiclePageDto);
			
			stationQueryDto.setOrganizationId(loginUser.getOrganizationId());
			stationQueryDto.setIsEnt(Boolean.valueOf(loginUser.isEntAdmin()));
			stationQueryDto.setIsRent(Boolean.valueOf(loginUser.isRentAdmin()));
			
			return this.stationService.findStationAssignedVehicles(stationQueryDto);
		}catch(Exception e){
    		LOG.error("PlatformStationService findStationAssignedVehicles error : ",e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE); 
    	}
	}

	public PagModel findStationAvialiableVehicles(User loginUser, StationVehiclePageDto stationVehiclePageDto) {
		try{
			StationQueryDto stationQueryDto =new StationQueryDto();
			BeanUtils.copyProperties(stationQueryDto, stationVehiclePageDto);
			
			stationQueryDto.setOrganizationId(loginUser.getOrganizationId());
			stationQueryDto.setIsEnt(Boolean.valueOf(loginUser.isEntAdmin()));
			stationQueryDto.setIsRent(Boolean.valueOf(loginUser.isRentAdmin()));
			
			return this.stationService.findStationAvialiableVehicles(stationQueryDto);
		}catch(Exception e){
    		LOG.error("PlatformStationService findStationAvialiableVehicles error : ",e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE); 
    	}
	}

	public void assignVehicles(StationVehicleDto stationVehicleDto) {
		try{
			this.stationService.assignVehicles(stationVehicleDto.getVehicleIds(), stationVehicleDto.getStationId());
			
		}catch(Exception e){
    		LOG.error("PlatformStationService assignVehicles error : ",e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE); 
    	}
	}

	public void unassignVehicles(StationUnAssignVehicleDto stationUnAssignVehicleDto) {
		try{
			this.stationService.unassignVehicles(stationUnAssignVehicleDto.getVehicleId(), stationUnAssignVehicleDto.getStationId());
			//车辆从站点移除，如果该车辆分配了司机，司机信息也相应的被移除掉
			vehicleService.unassignDriver(Long.valueOf(stationUnAssignVehicleDto.getVehicleId()));
		}catch(Exception e){
    		LOG.error("PlatformStationService unassignVehicles error : ",e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE); 
    	}
	}

}
