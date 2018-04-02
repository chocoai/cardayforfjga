package com.cmdt.carrental.portal.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleEnterpriseModel;
import com.cmdt.carrental.common.model.VehicleListModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.service.VehicleAlertService;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/vehicle")
public class VehicleController extends BaseController{
	private static final Logger LOG = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private VehicleAlertService vehicleAlertService;
    
    
    
    /**
     * [系统运营管理员]
     * [业务运营管理员]
     * 查询租车公司的车辆
     * 查询用车企业的车辆
     * currentPage(当前页),numPerPage（每页记录条数）,vehicleNumber(车牌号),
     * vehicleType(车辆类型，-1：全部类型),
     * fromOrgId(车辆来源,-1:全部来源)
     * entId(企业Id号)
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
	//@RequiresPermissions("vehicle:list")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> list(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
	    	PagModel pagModel= null;
			if (StringUtils.isNotBlank(json)) {
				Map<String,Object>	jsonMap = JsonUtils.json2Object(json, Map.class);
				pagModel= vehicleService.findPageListByEntAdminUsedByPortal(TypeUtils.obj2Long(jsonMap.get("entId")),json);
			//	pagModel= vehicleService.findPageListByEntAdmin(TypeUtils.obj2Long(jsonMap.get("entId")),json);
				map.put("data", pagModel);
			}
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("Vehicle Controller.list  error, cause by:", e);
    		map.put("status", "failure");
    	   }
        return map;
    }
    
    @RequestMapping(value = "/listVhicle", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listVhicle(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			VehicleListModel vModel= JsonUtils.json2Object(json, VehicleListModel.class);
			PagModel pagModel = new PagModel();
			vModel.setOrganizationId(vModel.getDeptId());
			pagModel = vehicleService.getVehicleListByEnterAdmin(vModel);
			map.put("data", pagModel);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.list error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
    /**
     * 根据车牌号查找车辆信息
     * 传入企业id号和车牌号
     * @param loginUser
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/findVehicleByVehicleNumber", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findVehicleByVehicleNumber(@CurrentUser User loginUser,String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			String vehicleNumber = TypeUtils.obj2String(jsonMap.get("vehicleNumber"));
			Long entId=TypeUtils.obj2Long(jsonMap.get("entId"));
			VehicleModel vehicleModel=vehicleAlertService.getVehicleByVehicleNumber(entId,vehicleNumber);
			map.put("status", "success");
			map.put("data", vehicleModel);
		}catch(Exception e){
			LOG.error("Vehicle Controller find findVehicleByVehicleNumber  error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
    /**
     * 车辆来源
     * 根据企业的id查找和该企业关联的公司（租车公司，和用车企业）
     * 传入企业id
     * @param loginUser
     * @param json
     * @return
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/listVehicleFrom", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findVehicleFrom(@CurrentUser User loginUser,String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Long entId=TypeUtils.obj2Long(jsonMap.get("entId"));
			String enterName=TypeUtils.obj2String(jsonMap.get("name"));
			List<VehicleEnterpriseModel> list=vehicleService.findAllVehiclefromByEnterId(entId,enterName);
			map.put("status", "success");
			map.put("data", list);
		}catch(Exception e){
			LOG.error("Vehicle Controller listVehicleFrom  error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
    
    /**
     * 管理员查询所有没有绑定设备的车辆
     *
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/listUnBindDeviceVehicle", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findUnBindDeviceVehicle(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			String vehicleNumber = TypeUtils.obj2String(jsonMap.get("vehicleNumber"));
			List<VehicleModel> list= vehicleService.findUnBindDeviceVehicle(vehicleNumber);
			map.put("status", "success");
			map.put("data", list);
		}catch(Exception e){
			LOG.error("Vehicle Controller listUnBindDeviceVehicle  error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
    /**
     * 根据id查询车辆(定制化，包含车辆来源,所属部门)
     * @param id
     * @return
     */
    //@RequiresPermissions("vehicle:view")
    @RequestMapping(value = "/monitor/{id}/update", method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> showMonitorVehicle(@PathVariable("id") Long id) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		VehicleModel vehicleModel = vehicleService.findVehicleModelById(id);
    		if(vehicleModel != null){
    			map.put("data", vehicleModel);
    			map.put("status", "success");
    		}else{
    			map.put("status", "failure");
    		}
    	}catch(Exception e){
    		LOG.error("Vehicle Controller showMonitorVehicle  error, cause by:", e);
    		 map.put("status", "failure");
    	}
        return map;
    }  
    
  
}
