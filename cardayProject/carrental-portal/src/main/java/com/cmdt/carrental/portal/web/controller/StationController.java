package com.cmdt.carrental.portal.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmdt.carrental.common.entity.Station;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.StationQueryDto;
import com.cmdt.carrental.common.service.StationService;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/station")
public class StationController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(StationController.class);

	@Autowired
	private StationService stationService;
	
	@Autowired
	private VehicleService vehicleService;

	/**
	 * 根据站点名查询站点
	 * 
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("station:list")
	@RequestMapping(value = "/findByStationName",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			String stationName = "";
			String currentPage = "";
			String numPerPage = "";
			if (StringUtils.isNotEmpty(json)) {
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				stationName = String.valueOf(jsonMap.get("stationName"));
				currentPage = String.valueOf(jsonMap.get("currentPage"));
				numPerPage = String.valueOf(jsonMap.get("numPerPage"));
			}

			PagModel stations = null;
			stations = stationService.findStationsByName(stationName,loginUser.getOrganizationId(),currentPage,numPerPage);
			if(stations != null && stations.getResultList().size()>0){
				for(Station station : (List<Station>)stations.getResultList()){
					List<Vehicle> vehicles = stationService.findStationAssignedVehiclesSum(station.getId().toString(), loginUser.getOrganizationId(),loginUser.isRentAdmin(),loginUser.isEntAdmin());
					if(vehicles != null && !vehicles.isEmpty()){
						station.setAssignedVehicleNumber(vehicles.size()+"");
					}else{
						station.setAssignedVehicleNumber("0");
					}
				}
			}

			map.put("status", "success");
			map.put("data", stations);
		} catch (Exception e) {
			LOG.error("Station Controller find station by stationName error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 根据企业过滤查询站点
	 * 用于下拉框选项
	 * @return 
	 */
	@RequiresPermissions("station:list")
	@RequestMapping(value = "/listByOrgId",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listByOrgId(@CurrentUser User loginUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Long entId=loginUser.getOrganizationId();
			List<Station> stations = stationService.findStationsByOrgId(entId+"");
			map.put("status", "success");
			map.put("data", stations);
		} catch (Exception e) {
			LOG.error("Station Controller find station by orgId error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 新增站点
	 * 
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("station:create")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> create(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Station station = new Station();
			station.setStationName(String.valueOf(jsonMap.get("stationName")));
			station.setCarNumber(String.valueOf(jsonMap.get("carNumber")));
			station.setCity(String.valueOf(jsonMap.get("city")));
			station.setPosition(String.valueOf(jsonMap.get("position")));
			station.setLongitude(String.valueOf(jsonMap.get("longitude")));
			station.setLatitude(String.valueOf(jsonMap.get("latitude")));
			station.setRadius(String.valueOf(jsonMap.get("radius")));
			station.setAreaId(Long.valueOf(jsonMap.get("areaId").toString()));
//			station.setStartTime(String.valueOf(jsonMap.get("startTime")));
//			station.setEndTime(String.valueOf(jsonMap.get("endTime")));
			if(loginUser.getOrganizationId() != null)
				station.setOrganizationId(loginUser.getOrganizationId());
			 
			station = stationService.createStation(station);

			if (station != null) {
				map.put("data", station);
				map.put("status", "success");
			} else {
				map.put("status", "failure");
			}
		} catch (Exception e) {
			LOG.error("Station Controller create station error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 根据id查询站点
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("station:view")
	@RequestMapping(value = "/{id}/findById", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findById(@PathVariable("id") Long id, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Station station = stationService.findStation(id);
			if (station != null) {
				map.put("data", station);
				map.put("status", "success");
			} else {
				map.put("status", "failure");
			}
		} catch (Exception e) {
			LOG.error("Station Controller findById station error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 修改站点信息
	 * 
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("station:update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(@CurrentUser User loginUser,String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Station station = stationService.findStation(Long.valueOf(String.valueOf(jsonMap.get("id"))));
			if (station != null) {
				station.setId(Long.valueOf(jsonMap.get("id").toString()));
				station.setStationName(String.valueOf(jsonMap.get("stationName")));
				station.setCarNumber(String.valueOf(jsonMap.get("carNumber")));
				station.setCity(String.valueOf(jsonMap.get("city")));
				station.setPosition(String.valueOf(jsonMap.get("position")));
				station.setLongitude(String.valueOf(jsonMap.get("longitude")));
				station.setLatitude(String.valueOf(jsonMap.get("latitude")));
				station.setRadius(String.valueOf(jsonMap.get("radius")));
				station.setAreaId(Long.valueOf(jsonMap.get("areaId").toString()));
//				station.setStartTime(String.valueOf(jsonMap.get("startTime")));
//				station.setEndTime(String.valueOf(jsonMap.get("endTime")));
				if(loginUser.getOrganizationId() != null)
					station.setOrganizationId(loginUser.getOrganizationId());
			}
			Station updateStation = stationService.updateStation(station);
			map.put("data", updateStation);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("Station Controller update station error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 删除站点
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("station:delete")
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(@CurrentUser User loginUser, @PathVariable("id") Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Station station = stationService.findStation(id);
			stationService.deleteStation(station);
			map.put("status", "success");
			return map;
		} catch (Exception e) {
			LOG.error("Station Controller delete station error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * 根据站点ID查询已分配车辆
	 * 
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("station:list")
	@RequestMapping(value = "/findStationAssignedVehicles",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findStationAssignedVehicles(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			PagModel vehicles = null;
			if (StringUtils.isNotEmpty(json)) {
				StationQueryDto stationQueryDto =JsonUtils.json2Object(json, StationQueryDto.class);
				stationQueryDto.setOrganizationId(loginUser.getOrganizationId());
				stationQueryDto.setIsEnt(loginUser.isEntAdmin());
				stationQueryDto.setIsRent(loginUser.isRentAdmin());
				vehicles = stationService.findStationAssignedVehicles(stationQueryDto);
			}
			map.put("status", "success");
			map.put("data", vehicles);
		} catch (Exception e) {
			LOG.error("Station Controller findStationAssignedVehicles error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * 根据站点ID查询站点可分配车辆
	 * 
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("station:list")
	@RequestMapping(value = "/findStationAvialiableVehicles",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findStationAvialiableVehicles(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			PagModel vehicles = null;
			
			if (StringUtils.isNotEmpty(json)) {
				StationQueryDto stationQueryDto =JsonUtils.json2Object(json, StationQueryDto.class);
				stationQueryDto.setOrganizationId(loginUser.getOrganizationId());
				stationQueryDto.setIsEnt(loginUser.isEntAdmin());
				stationQueryDto.setIsRent(loginUser.isRentAdmin());
				vehicles = stationService.findStationAvialiableVehicles(stationQueryDto);
			}
			map.put("status", "success");
			map.put("data", vehicles);
		} catch (Exception e) {
			LOG.error("Station Controller findStationAvialiableVehicles error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * 为站点分配车辆
	 * 
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("station:list")
	@RequestMapping(value = "/assignVehicles",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> assignVehicles(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			String stationId = "";
			String vehicleIds = "";
			if (StringUtils.isNotEmpty(json)) {
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				stationId = String.valueOf(jsonMap.get("stationId"));
				vehicleIds = String.valueOf(jsonMap.get("vehicleIds"));
			}
			
			stationService.assignVehicles(vehicleIds , stationId);

			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("Station Controller assignVehicles error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * 移除站点分配车辆
	 * 
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("station:list")
	@RequestMapping(value = "/unassignVehicles",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> unassignVehicles(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			String stationId = "";
			String vehicleId = "";
			if (StringUtils.isNotEmpty(json)) {
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				stationId = String.valueOf(jsonMap.get("stationId"));
				vehicleId = String.valueOf(jsonMap.get("vehicleId"));
			}
			
			stationService.unassignVehicles(vehicleId , stationId);
			//车辆从站点移除，如果该车辆分配了司机，司机信息也相应的被移除掉
			vehicleService.unassignDriver(Long.valueOf(vehicleId));
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("Station Controller assignVehicles error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}

}
