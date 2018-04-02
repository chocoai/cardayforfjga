package com.cmdt.carrental.common.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.dao.MarkerDao;
import com.cmdt.carrental.common.entity.Marker;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.model.MarkerModel;
import com.cmdt.carrental.common.model.MarkerQueryDto;
import com.cmdt.carrental.common.model.PagModel;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MarkerServiceImpl implements MarkerService {
	private static final Logger LOG = LoggerFactory.getLogger(MarkerServiceImpl.class);
	@Autowired
	private ShouqiService shouqiService;
	
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private MarkerDao markerDao;

	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Override
	public Marker createMarker(Marker marker) {	
		if(marker == null)
			return null;
		return markerDao.createMarker(marker);
	}

	@Override
	public Marker updateMarker(Marker marker) {
		if(marker == null)
			return null;
		return markerDao.updateMarker(marker);
	}

	@Override
	public void deleteMarker(Marker marker) {
		if(marker == null)
			return;
		markerDao.deleteMarker(marker);
	}

	@Override
	public Marker findMarker(Long markerId) {
		if(markerId == null)
			return null;
		return markerDao.findMarker(markerId);
	}

	@Override
	public List<MarkerModel> findAll() {
		return null;
	}

/*	@Override
	public PagModel findMarkerByName(String geofenceName, String currentPage, String numPerPage) {
		Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.SEARCHMARKER,
				new Object[] { geofenceName });
		try {
			if (result != null && result.get("status").equals("success") && result.get("data") != null) {
				JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
				if ("000".equals(jsonNode.get("status").asText())) {
					ArrayNode rows = (ArrayNode) jsonNode.get("result");
					if (rows != null && rows.size() > 0) {
						List<PageJsonModel> resultList = new ArrayList<PageJsonModel>();
						for (int i = 0; i < rows.size(); i++) {
							JsonNode rowNode = rows.get(i);
							MarkerModel markerModel = JsonUtils.json2Object(rowNode.toString(), MarkerModel.class);
							//只取多边形Marker
							if(markerModel != null && "polygon".equals(markerModel.getTypeArea()))
								resultList.add(markerModel);
						}
						
				        Collections.sort(resultList, new Comparator<PageJsonModel>() {
				            public int compare(PageJsonModel a, PageJsonModel b) {
				            	Long one = ((MarkerModel) a).getId();
				            	Long two = ((MarkerModel) b).getId();
				            	int intA =  Integer.parseInt(one.toString());;
				            	int intB =  Integer.parseInt(two.toString());;
				                return  intB - intA ;
				            }
				        });
				        
						//Collections.reverse(resultList);
						return new Pagination(resultList, currentPage, numPerPage).getResult();
					} else
						return null;
				}
				return null;
			} else
				return null;
		} catch (Exception e) {
			LOG.error("MarkerService findMarkerByName error, cause by:\n", e);
			return null;
		}
	}*/
	
	@Override
	public PagModel findMarkerByName(String geofenceName, Long organizationId,String currentPage,String numPerPage) {
		return markerDao.findMarkerByName(geofenceName,organizationId,currentPage,numPerPage);
	}

	@Override
	public PagModel findMarkerAssignedVehicles(String markerId,  Long organizationId,String currentPage,String numPerPage,Boolean isRent,Boolean isEnt) {
		if(StringUtils.isEmpty(markerId))
			return null;
		if(organizationId == null)
			return null;
		return markerDao.findMarkerAssignedVehicles(markerId,organizationId,currentPage,numPerPage,isRent,isEnt);
	}

	@Override
	public List<Vehicle> findMarkerAssignedVehiclesSum(String markerId, Long organizationId,Boolean isRent,Boolean isEnt) {
		if(StringUtils.isEmpty(markerId))
			return null;
		if(organizationId == null)
			return null;
		return markerDao.findMarkerAssignedVehiclesSum(markerId,organizationId,isRent,isEnt);
	}

	@Override
	public PagModel findMarkerAvialiableVehicles(String markerId,  Long organizationId,String currentPage,String numPerPage,Boolean isRent,Boolean isEnt) {
		if(StringUtils.isEmpty(markerId))
			return null;
		if(organizationId == null)
			return null;
		return markerDao.findMarkerAvialiableVehicles(markerId,organizationId,currentPage,numPerPage,isRent,isEnt);
	}

	@Override
	public void assignVehicles(String vehicleIds, String markerId) {
		if(StringUtils.isEmpty(vehicleIds)){
			return;
		}
		if(StringUtils.isEmpty(markerId)){
			return;
		}
		markerDao.assignVehicles(vehicleIds,markerId);
	}

	@Override
	public void unassignVehicles(String vehicleId, String markerId) {
		if(StringUtils.isEmpty(vehicleId)){
			return;
		}	
		if(StringUtils.isEmpty(markerId)){
			return;
		}
		markerDao.unassignVehicles(vehicleId,markerId);
	}

	@Override
	public PagModel findMarkerByName(MarkerQueryDto markerQueryDto) {
		
		return markerDao.findMarkerByName(markerQueryDto);
	}

	@Override
	public PagModel findMarkerAvialiableVehicles(MarkerQueryDto markerQueryDto) {
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(markerQueryDto.getOrganizationId());
		if (!orgList.isEmpty()) {
			List<Long> orgIds = new ArrayList<>();
			for (Organization org : orgList) {
				orgIds.add(org.getId());
			}
			return markerDao.findMarkerAvialiableVehicles(orgIds,markerQueryDto);
		}
		return null;
	}

	@Override
	public PagModel findMarkerAssignedVehicles(MarkerQueryDto markerQueryDto) {
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(markerQueryDto.getOrganizationId());
		if (!orgList.isEmpty()) {
			List<Long> orgIds = new ArrayList<>();
			for (Organization org : orgList) {
				orgIds.add(org.getId());
			}
			return markerDao.findMarkerAssignedVehicles(orgIds,markerQueryDto);
		}
		return null;
	}

}
