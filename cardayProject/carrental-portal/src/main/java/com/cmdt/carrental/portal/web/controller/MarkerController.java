package com.cmdt.carrental.portal.web.controller;

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

import com.cmdt.carrental.common.entity.Marker;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.model.MarkerQueryDto;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.service.MarkerService;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/geofence")
public class MarkerController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(MarkerController.class);
	@Autowired
	private MarkerService markerService;

	/**
	 * 根据Marker名查询Marker
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("geofence:list")
	@RequestMapping(value = "/findByName", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(@CurrentUser User loginUser, String json) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			String geofenceName = "";
			String currentPage = "";
			String numPerPage = "";
			if (StringUtils.isNotEmpty(json)) {
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				geofenceName = String.valueOf(jsonMap.get("geofenceName"));
				currentPage = String.valueOf(jsonMap.get("currentPage"));
				numPerPage = String.valueOf(jsonMap.get("numPerPage"));
			}

			PagModel markerList = null;
			markerList = markerService.findMarkerByName(geofenceName,loginUser.getOrganizationId(),currentPage,numPerPage);
			if(markerList != null && markerList.getResultList().size()>0){
				for(Marker marker : (List<Marker>)markerList.getResultList()){
					List<Vehicle> vehicles = markerService.findMarkerAssignedVehiclesSum(marker.getId().toString(), loginUser.getOrganizationId(),loginUser.isRentAdmin(),loginUser.isEntAdmin());
					if(vehicles != null && !vehicles.isEmpty()){
						marker.setAssignedVehicleNumber(vehicles.size()+"");
					}else{
						marker.setAssignedVehicleNumber("0");
					}
				}
			}

			map.put("status", "success");
			map.put("data", markerList);
		} catch (Exception e) {
			LOG.error("Marker Controller find marker by markerName error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 新增Marker
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("geofence:create")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> create(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Marker marker = new Marker();
			marker.setMarkerName(String.valueOf(jsonMap.get("markerName")));
			marker.setCity(String.valueOf(jsonMap.get("city")));
			marker.setPosition(String.valueOf(jsonMap.get("position")));
			marker.setType(String.valueOf(jsonMap.get("type")));
			marker.setPattern(String.valueOf(jsonMap.get("pattern")));
			marker.setLongitude(String.valueOf(jsonMap.get("longitude")));
			marker.setLatitude(String.valueOf(jsonMap.get("latitude")));
			marker.setRegionId(Long.valueOf(String.valueOf(jsonMap.get("cityId"))));
			marker.setRadius(String.valueOf(jsonMap.get("radius")));
			if(loginUser.getOrganizationId() != null)
				marker.setOrganizationId(loginUser.getOrganizationId());
			 
			marker = markerService.createMarker(marker);

			if (marker != null) {
				map.put("data", marker);
				map.put("status", "success");
			} else {
				map.put("status", "failure");
			}
		} catch (Exception e) {
			LOG.error("Marker Controller create marker error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 根据id查询Marker
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("geofence:view")
	@RequestMapping(value = "/{id}/findById", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findById(@PathVariable("id") Long id, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		try {
			if (id != null) {
				Marker markerModel = markerService.findMarker(id);
				map.put("data", markerModel);
				map.put("status", "success");
			}else{
				map.put("status", "failure");
				map.put("data", "");
				LOG.error("Marker Controller find marker by id error, id is empty!");
			}
			
		} catch (Exception e) {
			LOG.error("Marker Controller find marker by id error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 修改Marker信息
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("geofence:update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(@CurrentUser User loginUser, String json) {		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Marker marker = markerService.findMarker(Long.valueOf(String.valueOf(jsonMap.get("id"))));
			if (marker != null) {
				marker.setId(Long.valueOf(jsonMap.get("id").toString()));
				marker.setMarkerName(String.valueOf(jsonMap.get("markerName")));
				marker.setCity(String.valueOf(jsonMap.get("city")));
				marker.setPosition(String.valueOf(jsonMap.get("position")));
				marker.setType(String.valueOf(jsonMap.get("type")));
				marker.setPattern(String.valueOf(jsonMap.get("pattern")));
				marker.setLongitude(String.valueOf(jsonMap.get("longitude")));
				marker.setLatitude(String.valueOf(jsonMap.get("latitude")));
				marker.setRegionId(Long.valueOf(jsonMap.get("cityId").toString()));
				marker.setRadius(String.valueOf(jsonMap.get("radius")));
				if(loginUser.getOrganizationId() != null)
					marker.setOrganizationId(loginUser.getOrganizationId());
			}
			Marker updateMarker = markerService.updateMarker(marker);
			map.put("data", updateMarker);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("Marker Controller update marker error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 删除Marker
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("geofence:delete")
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(@CurrentUser User loginUser, @PathVariable("id") Long id) {	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Marker marker = markerService.findMarker(id);
			markerService.deleteMarker(marker);
			map.put("status", "success");
			return map;
		} catch (Exception e) {
			LOG.error("Marker Controller delete marker error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 根据Marker ID查询已分配车辆
	 * 
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("geofence:list")
	@RequestMapping(value = "/findMarkerAssignedVehicles",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findMarkerAssignedVehicles(@CurrentUser User loginUser, String json) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			PagModel vehicles = null;
			if (StringUtils.isNotEmpty(json)) {
				MarkerQueryDto markerQueryDto =JsonUtils.json2Object(json, MarkerQueryDto.class);
				markerQueryDto.setOrganizationId(loginUser.getOrganizationId());
				markerQueryDto.setIsEntAdmin(loginUser.isEntAdmin());
				markerQueryDto.setIsRentAdmin(loginUser.isRentAdmin());
				vehicles=markerService.findMarkerAssignedVehicles(markerQueryDto);
			}

			map.put("status", "success");
			map.put("data", vehicles);
		} catch (Exception e) {
			LOG.error("Marker Controller findMarkerAssignedVehicles error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * 根据Marker ID查询Marker可分配车辆
	 * 
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("geofence:list")
	@RequestMapping(value = "/findMarkerAvialiableVehicles",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findMarkerAvialiableVehicles(@CurrentUser User loginUser, String json) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			PagModel vehicles = null;
			if (StringUtils.isNotEmpty(json)) {
				MarkerQueryDto markerQueryDto =JsonUtils.json2Object(json, MarkerQueryDto.class);
				markerQueryDto.setOrganizationId(loginUser.getOrganizationId());
				markerQueryDto.setIsEntAdmin(loginUser.isEntAdmin());
				markerQueryDto.setIsRentAdmin(loginUser.isRentAdmin());
				vehicles=markerService.findMarkerAvialiableVehicles(markerQueryDto);
			}

			map.put("status", "success");
			map.put("data", vehicles);
		} catch (Exception e) {
			LOG.error("Marker Controller findMarkerAvialiableVehicles error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * 为Marker分配车辆
	 * 
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("geofence:list")
	@RequestMapping(value = "/assignVehicles",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> assignVehicles(@CurrentUser User loginUser, String json) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			String markerId = "";
			String vehicleIds = "";
			if (StringUtils.isNotEmpty(json)) {
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				markerId = String.valueOf(jsonMap.get("markerId"));
				vehicleIds = String.valueOf(jsonMap.get("vehicleIds"));
			}
			
			markerService.assignVehicles(vehicleIds , markerId);

			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("Marker Controller assignVehicles error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * 移除Marker分配车辆
	 * 
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("geofence:list")
	@RequestMapping(value = "/unassignVehicles",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> unassignVehicles(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			String markerId = "";
			String vehicleId = "";
			if (StringUtils.isNotEmpty(json)) {
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				markerId = String.valueOf(jsonMap.get("markerId"));
				vehicleId = String.valueOf(jsonMap.get("vehicleId"));
			}
			
			markerService.unassignVehicles(vehicleId , markerId);

			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("Marker Controller assignVehicles error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
}
