package com.cmdt.carday.microservice.biz.service;

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
import com.cmdt.carday.microservice.model.request.marker.MarkerAssignVehicleDto;
import com.cmdt.carday.microservice.model.request.marker.MarkerCreateDto;
import com.cmdt.carday.microservice.model.request.marker.MarkerPageDto;
import com.cmdt.carday.microservice.model.request.marker.MarkerUnassignVehicleDto;
import com.cmdt.carday.microservice.model.request.marker.MarkerUpdateDto;
import com.cmdt.carday.microservice.model.request.marker.MarkerVehiclePageDto;

@Service
public class PlatformMarkerService {
	
	private static final Logger LOG = LoggerFactory.getLogger(PlatformMarkerService.class);
	@Autowired
	private MarkerService markerService;
     
	@SuppressWarnings("unchecked")
	public PagModel listMarker(User user,MarkerPageDto markerPageDto) throws Exception{
		

		MarkerQueryDto markerQueryDto = new MarkerQueryDto();
		BeanUtils.copyProperties(markerQueryDto, markerPageDto);
		markerQueryDto.setOrganizationId(user.getOrganizationId());
		
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
	}
	
	
	public Marker createMarker(User user, MarkerCreateDto markerCreateDto) throws Exception{
			
		Marker marker = new Marker();
		BeanUtils.copyProperties(marker, markerCreateDto);
		
		if(user.getOrganizationId() != null)
			marker.setOrganizationId(user.getOrganizationId());
		 
		return markerService.createMarker(marker);
	}
	
	public Marker findMarkerById(Long id){
			
		return markerService.findMarker(id);
	}
	
	public Marker updateMarker(User user, MarkerUpdateDto markerUpdateDto){
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
		
	}
	
	public void deleteMarkerById(Long id){
		
		Marker marker = markerService.findMarker(id);
		markerService.deleteMarker(marker);
	}
	
	public PagModel findMarkerAvialiableVehicles(User loginUser, MarkerVehiclePageDto markerVehiclePageDto) throws Exception{
		MarkerQueryDto markerQueryDto=new MarkerQueryDto();
		
		BeanUtils.copyProperties(markerQueryDto, markerVehiclePageDto);
		markerQueryDto.setOrganizationId(loginUser.getOrganizationId());
		markerQueryDto.setIsEntAdmin(loginUser.isEntAdmin());
		markerQueryDto.setIsRentAdmin(loginUser.isRentAdmin());
		
		return markerService.findMarkerAvialiableVehicles(markerQueryDto);
	}
	
	
	public PagModel findMarkerAssignedVehicles(User loginUser, MarkerVehiclePageDto markerVehiclePageDto) throws Exception{
	
		MarkerQueryDto markerQueryDto=new MarkerQueryDto();
		BeanUtils.copyProperties(markerQueryDto, markerVehiclePageDto);
		
		markerQueryDto.setOrganizationId(loginUser.getOrganizationId());
		markerQueryDto.setIsEntAdmin(loginUser.isEntAdmin());
		markerQueryDto.setIsRentAdmin(loginUser.isRentAdmin());
		return markerService.findMarkerAssignedVehicles(markerQueryDto);
	}
	
	public void assignVehicles(MarkerAssignVehicleDto dto){
		
		markerService.assignVehicles(dto.getVehicleIds() , dto.getMarkerId().toString());
	}
	
	
	public void unassignVehicles(MarkerUnassignVehicleDto dto){
		
		markerService.unassignVehicles(dto.getVehicleId(), dto.getMarkerId().toString());
	}
}
