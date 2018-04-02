package com.cmdt.carrental.platform.service.biz.service;

import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.entity.Marker;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.model.MarkerQueryDto;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.service.MarkerService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.platform.service.model.request.marker.MarkerAssignVehicleDto;
import com.cmdt.carrental.platform.service.model.request.marker.MarkerCreateDto;
import com.cmdt.carrental.platform.service.model.request.marker.MarkerPageDto;
import com.cmdt.carrental.platform.service.model.request.marker.MarkerUnassignVehicleDto;
import com.cmdt.carrental.platform.service.model.request.marker.MarkerUpdateDto;
import com.cmdt.carrental.platform.service.model.request.marker.MarkerVehiclePageDto;

@Service
public class PlatformMarkerService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PlatformMarkerService.class);
	@Autowired
	private MarkerService markerService;
     
	@SuppressWarnings("unchecked")
	public PagModel listMarker(User user,MarkerPageDto markerPageDto){
		try{
			markerPageDto.setOrganizationId(user.getOrganizationId());
	
			MarkerQueryDto markerQueryDto = new MarkerQueryDto();
			BeanUtils.copyProperties(markerQueryDto, markerPageDto);
			
			PagModel markerList = markerService.findMarkerByName(markerQueryDto);
			
			if(markerList != null && !markerList.getResultList().isEmpty()){
				for(Marker marker : (List<Marker>)markerList.getResultList()){
					List<Vehicle> vehicles = markerService.findMarkerAssignedVehiclesSum(marker.getId().toString(), user.getOrganizationId(),user.isRentAdmin(),user.isEntAdmin());
					if(vehicles != null && !vehicles.isEmpty()){
						marker.setAssignedVehicleNumber(String.valueOf(vehicles.size()));
					}else{
						marker.setAssignedVehicleNumber("0");
					}
				}
			}
			return markerList;
			
		}catch(Exception e){
			LOG.error("PlatformMarkerService listMarker error, cause by: ", e);
        	throw new ServerException(Constants.API_MESSAGE_FAILURE); 
		}
	}
	
	
	public Marker createMarker(User user, MarkerCreateDto markerCreateDto){
		try{
			
			Marker marker = new Marker();
			BeanUtils.copyProperties(marker, markerCreateDto);
			
			if(user.getOrganizationId() != null)
				marker.setOrganizationId(user.getOrganizationId());
			 
			return markerService.createMarker(marker);
			
		}catch(Exception e){
			LOG.error("PlatformMarkerService createMarker error, cause by: ", e);
        	throw new ServerException(Constants.API_MESSAGE_FAILURE); 
		}
	}
	
	public Marker findMarkerById(Long id){
		try{
			return markerService.findMarker(id);
			
		}catch(Exception e){
			LOG.error("PlatformMarkerService findMarkerById error, cause by: ", e);
        	throw new ServerException(Constants.API_MESSAGE_FAILURE); 
		}
	}
	
	public Marker updateMarker(User user, MarkerUpdateDto markerUpdateDto){
		try{
			Marker marker = markerService.findMarker(markerUpdateDto.getId());
			if (marker != null) {
				if(markerUpdateDto.getMarkerName()!=null){
					marker.setMarkerName(markerUpdateDto.getMarkerName());
				}
				if(markerUpdateDto.getCity()!=null){
					marker.setCity(markerUpdateDto.getCity());
				}
				if(markerUpdateDto.getPosition()!=null){
					marker.setPosition(markerUpdateDto.getPosition());
				}
				if(markerUpdateDto.getType()!=null){
					marker.setType(markerUpdateDto.getType());
				}
				if(markerUpdateDto.getPattern()!=null){
					marker.setPattern(markerUpdateDto.getPattern());
				}
				if(markerUpdateDto.getLongitude()!=null){
					marker.setLongitude(markerUpdateDto.getLongitude());
				}
				if(markerUpdateDto.getLatitude()!=null){
					marker.setLatitude(markerUpdateDto.getLatitude());
				}
				if(markerUpdateDto.getRegionId()!=null){
					marker.setRegionId(markerUpdateDto.getRegionId());
				}
				
				if(user.getOrganizationId() != null)
					marker.setOrganizationId(user.getOrganizationId());
				
				return  markerService.updateMarker(marker);
			}
			return null;
		}catch(Exception e){
			LOG.error("PlatformMarkerService updateMarker error, cause by: ", e);
        	throw new ServerException(Constants.API_MESSAGE_FAILURE); 
		}
	}
	
	public void deleteMarkerById(Long id){
		try{
			Marker marker = markerService.findMarker(id);
			markerService.deleteMarker(marker);
		}catch(Exception e){
			LOG.error("PlatformMarkerService deleteMarkerById error, cause by: ", e);
        	throw new ServerException(Constants.API_MESSAGE_FAILURE); 
		}
	}
	
	public PagModel findMarkerAvialiableVehicles(User loginUser, MarkerVehiclePageDto markerVehiclePageDto){
		try{
			markerVehiclePageDto.setOrganizationId(loginUser.getOrganizationId());
			markerVehiclePageDto.setIsEntAdmin(loginUser.isEntAdmin());
			markerVehiclePageDto.setIsRentAdmin(loginUser.isRentAdmin());
			
			MarkerQueryDto markerQueryDto=new MarkerQueryDto();
			BeanUtils.copyProperties(markerQueryDto, markerVehiclePageDto);
			
			return markerService.findMarkerAvialiableVehicles(markerQueryDto);
		
		}catch(Exception e){
			LOG.error("PlatformMarkerService findMarkerAvialiableVehicles error, cause by: ", e);
        	throw new ServerException(Constants.API_MESSAGE_FAILURE); 
		}
	}
	
	
	public PagModel findMarkerAssignedVehicles(User loginUser, MarkerVehiclePageDto markerVehiclePageDto){
		try{
			markerVehiclePageDto.setOrganizationId(loginUser.getOrganizationId());
			markerVehiclePageDto.setIsEntAdmin(loginUser.isEntAdmin());
			markerVehiclePageDto.setIsRentAdmin(loginUser.isRentAdmin());
			
			MarkerQueryDto markerQueryDto=new MarkerQueryDto();
			BeanUtils.copyProperties(markerQueryDto, markerVehiclePageDto);
			return markerService.findMarkerAssignedVehicles(markerQueryDto);
		
		}catch(Exception e){
			LOG.error("PlatformMarkerService findMarkerAssignedVehicles error, cause by: ", e);
        	throw new ServerException(Constants.API_MESSAGE_FAILURE); 
		}
	}
	
	public void assignVehicles(MarkerAssignVehicleDto dto){
		try{
			markerService.assignVehicles(dto.getVehicleIds() , dto.getMarkerId().toString());
		}catch(Exception e){
			LOG.error("PlatformMarkerService assignVehicles error, cause by: ", e);
        	throw new ServerException(Constants.API_MESSAGE_FAILURE); 
		}
	}
	
	
	public void unassignVehicles(MarkerUnassignVehicleDto dto){
		try{
			markerService.unassignVehicles(dto.getVehicleId(), dto.getMarkerId().toString());
		}catch(Exception e){
			LOG.error("PlatformMarkerService unassignVehicles error, cause by: ", e);
        	throw new ServerException(Constants.API_MESSAGE_FAILURE); 
		}
	}
}
