package com.cmdt.carrental.common.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.dao.StationDao;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.Station;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.StationQueryDto;

@Service
public class StationServiceImpl implements StationService{

	@Autowired
    private StationDao stationDao;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Override
	public Station createStation(Station station) {
		if(station == null)
			return null;
		return stationDao.createStation(station);
	}

	@Override
	public Station updateStation(Station station) {
		if(station == null)
			return null;
		return stationDao.updateStation(station);
	}

	@Override
	public void deleteStation(Station station) {
		if(station == null)
			return;
		stationDao.deleteStation(station);
	}

	@Override
	public Station findStation(Long stationId) {
		if(stationId == null)
			return null;
		return stationDao.findStation(stationId);
	}

	@Override
	public List<Station> findAll() {
		return stationDao.findAll();
	}

	@Override
	public PagModel findStationsByName(String stationName, Long organizationId,String currentPage,String numPerPage) {
		return stationDao.findStationsByName(stationName,organizationId,currentPage,numPerPage);
	}

	@Override
	public List<Station> findStationsByOrgId(String organizationId) {
		if(organizationId == null)
			return null;
		return stationDao.findStationsByOrgId(organizationId);
	}

	@Override
	public List<Station> findByStationName(String stationName) {
		if(StringUtils.isBlank(stationName))
			return null;
		return stationDao.findByStationName(stationName);
	}

	@Override
	public List<Station> findByStationName(String stationName,Long entId) {
		if(StringUtils.isBlank(stationName) || entId==null)
			return null;
		return stationDao.findByStationName(stationName,entId);
	}
	
	@Override
	public PagModel findStationAssignedVehicles(String stationId, Long organizationId,String currentPage,String numPerPage,Boolean isRent,Boolean isEnt) {
		if(StringUtils.isEmpty(stationId))
			return null;
		if(organizationId == null)
			return null;
		
		return stationDao.findStationAssignedVehicles(stationId,organizationId,currentPage,numPerPage,isRent,isEnt);
		
	}
	
	@Override
	public List<Vehicle> findStationAssignedVehiclesSum(String stationId, Long organizationId,Boolean isRent,Boolean isEnt) {
		if(StringUtils.isEmpty(stationId))
			return null;
		if(organizationId == null)
			return null;
		return stationDao.findStationAssignedVehiclesSum(stationId,organizationId,isRent,isEnt);
	}

	@Override
	public PagModel findStationAvialiableVehicles(String stationId, Long organizationId,String currentPage,String numPerPage,Boolean isRent,Boolean isEnt) {
		if(StringUtils.isEmpty(stationId))
			return null;
		if(organizationId == null)
			return null;
		return stationDao.findStationAvialiableVehicles(stationId,organizationId,currentPage,numPerPage,isRent,isEnt);
	}
	
	@Override
	public void assignVehicles(String vehicleIds, String stationId) {
		if(StringUtils.isEmpty(vehicleIds)){
			return;
		}
		if(StringUtils.isEmpty(stationId)){
			return;
		}
		stationDao.assignVehicles(vehicleIds,stationId);
	}

	@Override
	public void unassignVehicles(String vehicleId, String stationId) {
		if(StringUtils.isEmpty(vehicleId)){
			return;
		}	
		if(StringUtils.isEmpty(stationId)){
			return;
		}
		stationDao.unassignVehicles(vehicleId,stationId);
	}

	@Override
	public PagModel findStationsByName(StationQueryDto stationQueryDto) {
		
		return stationDao.findStationsByName(stationQueryDto);
	}

	@Override
	public PagModel findStationAssignedVehicles(StationQueryDto stationQueryDto) {
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(stationQueryDto.getOrganizationId());
		if (!orgList.isEmpty()) {
			List<Long> orgIds = new ArrayList<>();
			for (Organization org : orgList) {
				orgIds.add(org.getId());
			}
			return stationDao.findStationAssignedVehicles(orgIds,stationQueryDto);
		}
		return null;
		
	}

	@Override
	public PagModel findStationAvialiableVehicles(StationQueryDto stationQueryDto) {
		
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(stationQueryDto.getOrganizationId());
		if (!orgList.isEmpty()) {
			List<Long> orgIds = new ArrayList<>();
			for (Organization org : orgList) {
				orgIds.add(org.getId());
			}
			return stationDao.findStationAvialiableVehicles(orgIds,stationQueryDto);
		}
		return null;
	}

}
