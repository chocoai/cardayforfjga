package com.cmdt.carrental.portal.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.cmdt.carrental.common.cache.RedisService;
import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.entity.DevcieCommandConfigRecord;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.entity.VehicleAuthorized;
import com.cmdt.carrental.common.integration.DeviceCommandService;
import com.cmdt.carrental.common.integration.ServiceAdapter;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.AllocateDepModel;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.HomePageMapModel;
import com.cmdt.carrental.common.model.ObdStatusModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.Point;
import com.cmdt.carrental.common.model.ResponseTreeModel;
import com.cmdt.carrental.common.model.TimeRangeModel;
import com.cmdt.carrental.common.model.TransGPStoBaiduModel;
import com.cmdt.carrental.common.model.TripPropertyModel;
import com.cmdt.carrental.common.model.TripTraceModel;
import com.cmdt.carrental.common.model.VehicleAndOrderModel;
import com.cmdt.carrental.common.model.VehicleBrandModel;
import com.cmdt.carrental.common.model.VehicleCountModel;
import com.cmdt.carrental.common.model.VehicleEnterpriseModel;
import com.cmdt.carrental.common.model.VehicleHistoryTrack;
import com.cmdt.carrental.common.model.VehicleListForOrgDto;
import com.cmdt.carrental.common.model.VehicleListModel;
import com.cmdt.carrental.common.model.VehicleLocationModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleQueryDTO;
import com.cmdt.carrental.common.model.VehicleRoot;
import com.cmdt.carrental.common.model.VehicleStatusRoot;
import com.cmdt.carrental.common.model.VehicleTreeStatusModel;
import com.cmdt.carrental.common.service.BusiOrderService;
import com.cmdt.carrental.common.service.DeviceService;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.common.util.CsvUtil;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Controller
@RequestMapping("/vehicle")
public class VehicleController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(VehicleController.class);

	@Autowired
	private VehicleService vehicleService;

	@Autowired
	private OrganizationService orgService;
 
	@Autowired
	private BusiOrderService orderService;

	@Autowired
	private ShouqiService shouqiService;
	
	private static final String VEHICLE_PREFIX = "VEHICLE_";

	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	Map<String,Integer> provinceMap =new HashMap<String,Integer>();
	Map<Integer,List<Map<String, Integer>>> cityList =new HashMap<Integer,List<Map<String, Integer>>>();

	private static DecimalFormat df = new DecimalFormat("0.00");

	@Autowired
	private RedisService redisService;
	
	@Autowired
	private DeviceCommandService deviceCommandService;
	
	@Autowired
    private DeviceService deviceService;

	/**
	 * 方法已废弃
	 * [租户管理员]查询车辆(1.表格展示参见com.cmdt.carrental.portal.entity.Vehicle 2.模糊查询参数  Git Testtest for GIT
	 * vehicleNumber,vehicleType,fromOrgId,entId,city)
	 * [企业管理员]查询车辆(1.表格展示参见com.cmdt.carrental.portal.entity.Vehicle 2.模糊查询参数
	 * vehicleNumber,vehicleType,fromOrgId,entId,city)
	 * [部门管理员]查询车辆(1.表格展示参见com.cmdt.carrental.portal.entity.Vehicle 2.模糊查询参数
	 * vehicleNumber,vehicleType,fromOrgId)
	 * 
	 * @return
	 */
	 
	@RequiresPermissions("vehicle:list")
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			PagModel pagModel = new PagModel();
			// 企业管理员
			if (loginUser.isEntAdmin()) {
				Long entId = loginUser.getOrganizationId();
				pagModel = vehicleService.findPageListByEntAdminUsedByPortal(entId, json);
			}

			// 部门管理员
			if (loginUser.isDeptAdmin()) {
				Long deptId = loginUser.getOrganizationId();
				pagModel = vehicleService.findPageListByDeptAdmin(deptId, json);
			}

			map.put("data", pagModel);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.list error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	//@RequiresPermissions("vehicle:list")
	@RequestMapping(value = "/listVhicle", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listVhicle(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			VehicleListModel vModel= JsonUtils.json2Object(json, VehicleListModel.class);
			PagModel pagModel = new PagModel();
			vModel.setOrganizationId(loginUser.getOrganizationId());
			// 企业管理员
			if (loginUser.isEntAdmin()) {
				pagModel = vehicleService.getVehicleListByEnterAdmin(vModel);
			}

			// 部门管理员
			if (loginUser.isDeptAdmin()) {
				pagModel = vehicleService.getVehicleListByDeptAdmin(vModel);
			}

			map.put("data", pagModel);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.list error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	//维保中的自动补全功能
	@RequestMapping(value = "/vehicleListMantainance", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> vehicleListMantainance(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			VehicleQueryDTO dto=JsonUtils.json2Object(json,VehicleQueryDTO.class);
			List<Vehicle>  resultList = vehicleService.findVehicleListInMantainance(loginUser, dto);
			map.put("data", resultList);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.vehicleListMantainance error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	

//车辆维保中，根据车牌号自动匹配查询
	/*@RequiresPermissions("vehicle:list")
	@RequestMapping(value = "/vehicleListMantainance", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> vehicleListMantainance(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		List<Vehicle> resultList = new ArrayList<Vehicle>();

		try {
			// 企业管理员
			if (loginUser.isEntAdmin()) {
				Long entId = loginUser.getOrganizationId();
				resultList = vehicleService.findVehicleListInMantainance(entId, json);
			}

			map.put("data", resultList);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.vehicleListMantainance error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}*/

	@RequiresPermissions("vehicle:create")
	@RequestMapping(value = "/createVehicle", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> createVehicle(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Vehicle vehicle=JsonUtils.json2Object(json,Vehicle.class);
			VehicleCountModel vehicleCountModel = isValid(vehicle.getVehicleNumber(),vehicle.getVehicleIdentification(), null);

			if (vehicleCountModel.getValidflag() == 1) {
				map.put("status", "failure");
				map.put("error", vehicleCountModel.getMessage());
				return map;
			}
			// 企业管理员
			if (loginUser.isEntAdmin()) {
				vehicle.setEntId(loginUser.getOrganizationId());
				vehicle.setEntName(loginUser.getOrganizationName());
				vehicleService.createVehicleByEntAdmin(vehicle);
			}

			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.createVehicle error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 
	 * 
	 * 车辆来源下拉框list(查询页面) [租户管理员]显示租户以及租户下面的企业 [企业管理员]显示企业上面的租户(可能有多个租户)，以及本企业
	 * [部门管理员]显示部门上面的企业,以及企业上面的租户(可能有多个租户)
	 * 
	 * @param loginUser
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/listVehicleFrom", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listVehicleFrom(@CurrentUser User loginUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Long entId = loginUser.getOrganizationId();
			String entName = loginUser.getOrganizationName();
			// 企业管理员
			if (loginUser.isEntAdmin()) {
				List<VehicleEnterpriseModel> list = vehicleService.findAllVehiclefromByEnterId(entId, entName);
				map.put("data", list);
			}

			// 部门管理员
			if (loginUser.isDeptAdmin()) {
				List<VehicleEnterpriseModel> list = vehicleService.findListVehicleFromByDeptAdmin(entId);
				map.put("data", list);
			}

			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.listVehicleFrom error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 根据id查询车辆
	 * 
	 * @param id
	 * @return
	 */
//	@RequiresPermissions("vehicle:view")
	@RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> showUpdateForm(@PathVariable("id") Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Vehicle vehicle = vehicleService.findVehicleById(id);
			if (vehicle != null) {
				map.put("data", vehicle);
				map.put("status", "success");
			} else {
				map.put("status", "failure");
			}
		} catch (Exception e) {
			LOG.error("VehicleController.showUpdateForm error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 根据id查询车辆(定制化，包含车辆来源,所属部门)
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("vehicle:view")
	@RequestMapping(value = "/monitor/{id}/update", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> showMonitorVehicle(@PathVariable("id") Long id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			VehicleModel vehicleModel = vehicleService.findVehicleModelById(id);
			if (vehicleModel != null) {
				map.put("data", vehicleModel);
				map.put("status", "success");
			} else {
				map.put("status", "failure");
			}
		} catch (Exception e) {
			LOG.error("VehicleController.showMonitorVehicle error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 方法已废弃
	 * 分配车辆
	 * 
	 * @param vehicleId,orgId,orgName
	 * @return
	 */
	/*@RequiresPermissions("vehicle:allocation")
	@RequestMapping(value = "/vehicleAllocation", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> vehicleAllocation(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			// 企业管理员
			if (loginUser.isEntAdmin()) {
				Long entId = loginUser.getOrganizationId();
				String entName = loginUser.getOrganizationName();
				vehicleService.vehicleAllocationByEntAdmin(entId, entName, json);
			}

			// 租户管理员
			if (loginUser.isRentAdmin()) {
				Long rentId = loginUser.getOrganizationId();
				vehicleService.vehicleAllocationByRentAdmin(rentId, json);
			}
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.vehicleAllocation error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}*/

	/**
	 * 修改车辆信息
	 * 
	 * @return
	 */
	@RequiresPermissions("vehicle:update")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Vehicle vehModel =JsonUtils.json2Object(json, Vehicle.class);
			VehicleCountModel vehicleCountModel = isValid(vehModel.getVehicleNumber(),vehModel.getVehicleIdentification(), vehModel.getId());
			if (vehicleCountModel.getValidflag() == 1) {
				map.put("status", "failure");
				map.put("error", vehicleCountModel.getMessage());
				return map;
			}
			if (loginUser.isEntAdmin()) {
				// 企业管理员
				vehicleService.updateVehicleByEntAdmin(vehModel);
			}
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.update error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 删除车辆信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequiresPermissions("vehicle:delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Long id = Long.valueOf(String.valueOf(jsonMap.get("id")));
			Vehicle vehicle = vehicleService.findVehicleById(id);
			if (vehicle != null) {
				// 租户管理员,企业管理员,
				if (loginUser.isEntAdmin() || loginUser.isRentAdmin()) {
					vehicleService.deleteVehicleRelationById(id);
					map.put("status", "success");
				}
			} else {
				map.put("status", "failure");
				map.put("msg", "车辆不存在");
			}
		} catch (Exception e) {
			LOG.error("VehicleController.delete", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * [租户、企业管理员]导入车辆
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/importCSV", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public void batchImportCSV(@CurrentUser User loginUser, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		LOG.info("Inside batchImportCSV.");
		ResponseTreeModel<Vehicle> resp = new ResponseTreeModel<Vehicle>();
		provinceMap=vehicleService.getAllProvince();
		cityList=vehicleService.getAllProvinceAndCity();
		PrintWriter pWriter = response.getWriter();
		try {
			
			Long orgId = loginUser.getOrganizationId();
			String orgName = loginUser.getOrganizationName();
			request.setCharacterEncoding("UTF-8");
			ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
			CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
			MultipartHttpServletRequest multipartRequest = commonsMultipartResolver
					.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
			MultipartFile multiFile = multipartRequest.getFile("file"); // 与前端控件的name保持一致
			List<Object[]> dataList = CsvUtil.importFile(multiFile);
			List<Vehicle> modelList = new ArrayList<Vehicle>();
			for (int i = 0, num = dataList.size(); i < num; i++) {
				Object[] dataObj = dataList.get(i);
				// 过滤表头数据
				if (dataObj.length < 20)
					continue;
				Vehicle model = new Vehicle();
				String cell0 = TypeUtils.obj2String(dataObj[0]);// 车牌号
				String cell1 = TypeUtils.obj2String(dataObj[1]);// 车辆类型
				String cell2 = TypeUtils.obj2String(dataObj[2]);// 车辆品牌
				String cell3 = TypeUtils.obj2String(dataObj[3]);// 车辆型号
				String cell4 = TypeUtils.obj2String(dataObj[4]);// 车架号
				String cell5 = TypeUtils.obj2String(dataObj[5]);// 车辆颜色
				String cell6 = TypeUtils.obj2String(dataObj[6]);// 车辆座位数
				String cell7 = TypeUtils.obj2String(dataObj[7]);// 排量
				String cell8 = TypeUtils.obj2String(dataObj[8]);// 燃油号
				String cell9 = TypeUtils.obj2String(dataObj[9]);// 车辆所属省
				String cell10 = TypeUtils.obj2String(dataObj[10]);// 车辆所属城市
				String cell11 = TypeUtils.obj2String(dataObj[11]);// 购买时间
				String cell12 = TypeUtils.obj2String(dataObj[12]);// 理论油耗
				String cell13 = TypeUtils.obj2String(dataObj[13]);// 保险到期日
				String cell14 = TypeUtils.obj2String(dataObj[14]);// 车位信息
				String cell15 = TypeUtils.obj2String(dataObj[15]);// 车辆用途
				String cell16 = TypeUtils.obj2String(dataObj[16]);// 限速
				String cell17 = TypeUtils.obj2String(dataObj[17]);// 营业开始时间
				String cell18 = TypeUtils.obj2String(dataObj[18]);// 营业结束时间
				String cell19 = TypeUtils.obj2String(dataObj[19]);// 年检到期日
				String cell20 = TypeUtils.obj2String(dataObj[20]);// 部门ID

				model.setVehicleNumber(cell0);
				model.setVehicleType(cell1);
				model.setVehicleBrand(cell2);
				model.setVehicleModel(cell3);
				model.setVehicleIdentification(cell4);
				model.setVehicleColor(cell5);
				if (StringUtils.isNotBlank(cell6)) {
					model.setSeatNumber(TypeUtils.obj2Integer(cell6));
				}
				model.setVehicleOutput(cell7);
				model.setVehicleFuel(cell8);
				model.setProvince(cell9);
				model.setCity(cell10);
				if (StringUtils.isNotBlank(cell11)) {
					model.setVehicleBuyTime(new java.sql.Date(TypeUtils.obj2DateFormat(cell11).getTime()));
				}
				if (StringUtils.isNotBlank(cell12)) {
					model.setTheoreticalFuelCon(TypeUtils.obj2Double(cell12));
				}
				if (StringUtils.isNotBlank(cell13)) {
					model.setInsuranceExpiredate(new java.sql.Date(TypeUtils.obj2DateFormat(cell13).getTime()));
				}
				model.setParkingSpaceInfo(cell14);
				model.setVehiclePurpose(cell15);
				if (StringUtils.isNotBlank(cell16)) {
					model.setLimitSpeed(TypeUtils.obj2Integer(cell16));
				}
				model.setStartTime(cell17);
				model.setEndTime(cell18);
				if (StringUtils.isNotBlank(cell19)) {
					model.setInspectionExpiredate(new java.sql.Date(TypeUtils.obj2DateFormat(cell19).getTime()));
				}
				if(StringUtils.isNotBlank(cell20)){
					model.setOrganizationId(cell20);
				}
				model.setEntId(orgId);
				model.setEntName(orgName);
				modelList.add(model);
			}

			int sucNum = 0;// 成功导入几条数据
			int failNum = 0;// 失败几条数据
			int total = modelList.size();
			StringBuilder sb = new StringBuilder();
			if (modelList != null && !modelList.isEmpty()) {
				int num = 0;
				for (Vehicle model : modelList) {
					num++;
					String errMsg = validateData(loginUser,model, num);
					if (StringUtils.isNotBlank(errMsg)) {
						sb.append(errMsg).append("<br />");
						failNum++;
					} else {
						vehicleService.insertVehicleByEntAdmin(model);
					}
				}
				sucNum = total - failNum;
			}

			String failureMsgVal = sb.toString();
			if (StringUtils.isEmpty(failureMsgVal)) {
				resp.setMsg("成功导入:" + sucNum + ",失败:" + failNum);
			} else {
				resp.setMsg("成功导入:" + sucNum + ",失败:" + failNum + ",详细如下:<br />" + failureMsgVal);
			}

			pWriter.write(formatResponse(resp, null, null));
		} catch (UnsupportedEncodingException e) {
			LOG.error("Failed to importCSV due to UnsupportedEncoding error!", e);
			resp.setMsg("UnsupportedEncoding error");
			pWriter.write(formatResponse(resp, null, null));
		} catch (IOException e) {
			LOG.error("Failed to importCSV due to IO error!", e);
			resp.setMsg("IO error");
			pWriter.write(formatResponse(resp, null, null));
		} catch (Exception e) {
			LOG.error("Failed to importCSV due to unexpected error!", e);
			resp.setMsg("文件格式错误");
			pWriter.write(formatResponse(resp, null, null));
		} finally {
			pWriter.close();
		}
	}

	private String validateData(User loginUser,Vehicle vehicle, Integer num) {
		
		String msg = "";

		// 判断是否有为空的数据
		if (StringUtils.isBlank(vehicle.getVehicleNumber())) {
			msg = "第" + num + "条数据:车牌号为空,导入失败!";
			return msg;
		} else if (null != vehicleService.findVehicleByPlate(vehicle.getVehicleNumber())) {
			msg = "第" + num + "条数据:车牌号已被使用,导入失败!";
			return msg;
		} else if(!vehicle.getVehicleNumber().matches("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$")){
			msg = "第" + num + "条数据:车牌号格式输入错误,导入失败!";
			return msg;
		}
		if (StringUtils.isBlank(vehicle.getVehicleBrand())) {
			msg = "第" + num + "条数据:车辆品牌为空,导入失败!";
			return msg;
		}
		if (StringUtils.isBlank(vehicle.getVehicleIdentification())) {
			msg = "第" + num + "条数据:车架号为空,导入失败!";
			return msg;
		} else if (vehicleService.countByVehicleIdentification(vehicle.getVehicleIdentification(),null)!=null) {
			msg = "第" + num + "条数据:车架号已被使用,导入失败!";
			return msg;
		} else if(!vehicle.getVehicleIdentification().matches("^[A-Z0-9]{17}$")){
			msg = "第" + num + "条数据:车架号格式输入错误,导入失败!";
			return msg;
		}
		
		if(StringUtils.isBlank(vehicle.getProvince()) && StringUtils.isNotBlank(vehicle.getCity())){
			msg = "第" + num + "条数据:车辆所属省为空，但车辆所属市不为空,导入失败!";
			return msg;
		}else if(StringUtils.isNotBlank(vehicle.getProvince()) && StringUtils.isBlank(vehicle.getCity())){
			msg = "第" + num + "条数据:车辆所属省不为空，但车辆所属市为空,导入失败!";
			return msg;
		}else if(StringUtils.isNotBlank(vehicle.getProvince()) && StringUtils.isNotBlank(vehicle.getCity())){
			Integer id=provinceMap.get(vehicle.getProvince());
			if(id!=null){
				List<Map<String, Integer>> cList=cityList.get(id);
				Integer cityId=null;
				for(Map<String, Integer> map:cList){
					 cityId=map.get(vehicle.getCity());
					if(cityId!=null){
						break;
					}
				}
				if(cityId==null){
					msg = "第" + num + "条数据:车辆所属市不属于当前省,导入失败!";
					return msg;
				}
				vehicle.setCity(String.valueOf(cityId));		
			}else{
				msg = "第" + num + "条数据:车辆所属省不存在,导入失败!";
				return msg;
			}
		}
		if (StringUtils.isNotBlank(vehicle.getVehicleOutput())) {
			if (!vehicle.getVehicleOutput().matches("^\\d+(\\.\\d+)?(L|T)$")) {
				msg = "第" + num + "条数据:排量格式输入错误(L或者T),导入失败!";
				return msg;
			}
		}
		if (null == vehicle.getInsuranceExpiredate()) {
			msg = "第" + num + "条数据:保期到期日为空,导入失败!";
			return msg;
		}
		if (null == vehicle.getInspectionExpiredate()) {
			msg = "第" + num + "条数据:年检到期日为空,导入失败!";
			return msg;
		}
		if (null == vehicle.getLimitSpeed()) {
			msg = "第" + num + "条数据:限速为空,导入失败!";
			return msg;
		}
		if (StringUtils.isBlank(vehicle.getVehicleType())) {
			msg = "第" + num + "条数据:车辆类型为空,导入失败!";
			return msg;
		}
		if (StringUtils.isBlank(vehicle.getVehicleModel())) {
			msg = "第" + num + "条数据:车辆型号为空,导入失败!";
			return msg;
		}
		if (null == vehicle.getTheoreticalFuelCon()) {
			msg = "第" + num + "条数据:理论油耗为空,导入失败!";
			return msg;
		}
		if (null == vehicle.getVehicleFuel()) {
			msg = "第" + num + "条数据:燃油号为空,导入失败!";
			return msg;
		}
		if (StringUtils.isBlank(vehicle.getStartTime())) {
			//vehicle.setStartTime("09:00");
		}else if(!vehicle.getStartTime().matches("^([0][0-9]|[1][0-9]|2[0-3]):([0-5][0-9])$")){
			msg = "第" + num + "条数据:开始运营时间格式错误,导入失败!";
			return msg;
		}
		if (StringUtils.isBlank(vehicle.getEndTime())) {
			//vehicle.setStartTime("18:00");
		}else if (!vehicle.getEndTime().matches("^([0][0-9]|[1][0-9]|2[0-3]):([0-5][0-9])$")) {
			msg = "第" + num + "条数据:结束运营时间格式错误,导入失败!";
			return msg;
		}
		
		if(StringUtils.isNotBlank(vehicle.getVehiclePurpose())){
			if(vehicle.getVehiclePurpose().trim().equals("生产用车") || vehicle.getVehiclePurpose().trim().equals("营销用车") ||
					vehicle.getVehiclePurpose().trim().equals("接待用车") || vehicle.getVehiclePurpose().trim().equals("会议用车")){
			}else{
				msg = "第" + num + "条数据:车辆用途不存在,导入失败!";
				return msg;
			}
		}
		
		if(StringUtils.isNotBlank(vehicle.getOrganizationId())){
			Organization organization=	orgService.findCurrOrgBelongOrg(vehicle.getOrganizationId(),loginUser.getOrganizationId());
			if(organization==null){
				msg = "第" + num + "条数据:部门ID不属于当前企业,导入失败!";
				return msg;
			}else{
				vehicle.setCurrentuseOrgId(organization.getId());
				vehicle.setCurrentuseOrgName(organization.getName());
			}
		}else{
			vehicle.setCurrentuseOrgId(null);
			vehicle.setCurrentuseOrgName(null);
		}
		return msg;
	}
	/**
	 * 方法已废弃
	 * 查询OBD信息
	 * 
	 * @param deviceNumber
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryObdByImei", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryObdByImei(String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			String deviceNumber = String.valueOf(jsonMap.get("deviceNumber"));
			ObdStatusModel obdStatusModel = new ObdStatusModel();
			obdStatusModel.setDeviceNumber(deviceNumber);

			Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.QUERYOBDBYIMEI,
					new Object[] { deviceNumber });
			LOG.info("=============call service QueryObdByImei result:" + result);
			if (result == null || result.get("status").equals("failure")) {
				map.put("status", "failure");
				return map;
			} else if (result.get("data") != null) {
				JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
				if ("000".equals(jsonNode.get("status").asText())) {
					ArrayNode rows = (ArrayNode) jsonNode.get("result");
					if (rows != null && rows.size() > 0) {
						JsonNode obdNode = rows.get(0);
						obdStatusModel.setStatus(obdNode.get("status").asText());
					}
				} else {
					obdStatusModel.setStatus("NOTFOUND");
				}
			} else {
				obdStatusModel.setStatus("NOTFOUND");
			}
			map.put("status", "success");
			map.put("data", obdStatusModel);
		} catch (Exception e) {
			LOG.error("VehicleController.queryObdByImei", e);
			map.put("status", "failure");
		}
		return map;
	}

	// 模板下载
	@RequestMapping(value = "/down", method = RequestMethod.GET)
	public void down(HttpServletRequest request, HttpServletResponse res) {
		ServletContext cxf = request.getSession().getServletContext();
		String path = cxf.getRealPath("resources" + File.separator + "template" + File.separator + "template.csv");
		int nameIndex = path.lastIndexOf(File.separator);
		InputStream in = null;
		OutputStream out = null;
		try {
			String newFileName = URLEncoder.encode(path.substring(nameIndex + 1), "UTF-8");
			res.setContentType("application/x-msdownload");// 显示下载面板
			res.setHeader("Content-disposition", "attachment;fileName=" + newFileName);// 下载面板中
			// 产生输入流，读文件到内存
			in = new FileInputStream(path);
			// 产生输出流，用于把文件输出到客户端
			out = res.getOutputStream();
			byte[] b = new byte[1024];
			int len = 0;
			while ((len = in.read(b, 0, 1024)) != -1) {
				out.write(b, 0, len);
			}
		} catch (Exception e) {
			LOG.error("downLoad template error!", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					LOG.error("inputStream closed error!", e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					LOG.error("OutputString closed error!", e);
				}
			}
		}
	}

	/**
	 * 方法已废弃
	 * 查询多个OBD位置信息(或者说车辆位置信息)(redis调用)
	 * 
	 * @param deviceNumber
	 * @return
	 */
	@RequestMapping(value = "/queryObdLocationList", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryObdLocationList(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			List<VehicleLocationModel> obdLocationList = new ArrayList<VehicleLocationModel>();
			Long orgId = loginUser.getOrganizationId();

			List<VehicleModel> vehicleList = null;
			if (loginUser.isRentAdmin()) {// 监控租户创建的车辆
				vehicleList = vehicleService.findAllVehicleListByRentAdmin(orgId, json);
			}

			if (loginUser.isEntAdmin()) {// 企业下面部门的车辆
				vehicleList = vehicleService.findAllVehicleListByEntAdmin(orgId, json);
			}

			if (loginUser.isDeptAdmin()) {// 部门的车辆
				vehicleList = vehicleService.findAllVehicleListByDeptAdmin(orgId, json);
			}

			if (vehicleList != null && vehicleList.size() > 0) {
				for (VehicleModel vehicleModel : vehicleList) {
					// 从redis中取obd数据
					if(StringUtils.isEmpty(vehicleModel.getDeviceNumber())) {
						continue;
					}
					String obdRedisData = redisService.get(VEHICLE_PREFIX + vehicleModel.getDeviceNumber());
					if (StringUtils.isNotEmpty(obdRedisData)) {
						VehicleLocationModel vehicleLocationModel = MAPPER.readValue(obdRedisData,
								VehicleLocationModel.class);
						// GPS转百度坐标
						vehicleLocationModel = transGPStoBaidu(vehicleLocationModel);
						vehicleLocationModel.setId(vehicleModel.getId());
						vehicleLocationModel.setRealname(vehicleModel.getRealname());
						vehicleLocationModel.setPhone(vehicleModel.getPhone());
						// 过滤掉经纬度为0.0的值
						if (vehicleLocationModel.getLatitude() != 0.0 && vehicleLocationModel.getLongitude() != 0.0) {
							obdLocationList.add(vehicleLocationModel);
						}
					}
				}
			}
			map.put("status", "success");
			map.put("data", obdLocationList);
		} catch (Exception e) {
			LOG.error("VehicleController.queryObdLocationList", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 方法已废弃
	 * 查询多个OBD位置信息(或者说车辆位置信息)(redis调用)
	 * 
	 * @param vehicle
	 *            plate
	 * @return
	 */
	@RequestMapping(value = "/queryObdLocationByName", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryObdLocationByName(@CurrentUser User loginUser, String name) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		int totalVehs = 0;
		int onlineVehs = 0;
		int onTaskVehs = 0;
		int idleVehs = 0;
		try {
			Long orgId = loginUser.getOrganizationId();
			HomePageMapModel model = new HomePageMapModel();
			List<VehicleLocationModel> obdLocationList = new ArrayList<VehicleLocationModel>();
			model.setObdLocationList(obdLocationList);

			List<VehicleModel> vehicleList = null;
			if (name != null && !"".equals(name)) {
				// for home page
				if ("AllVehs".equals(name)) {
					if (loginUser.isEntAdmin()) {
						vehicleList = vehicleService.getVehicleModelByOrganization(orgId);
					} else if (loginUser.isDeptAdmin()) {
						vehicleList = vehicleService.getVehicleModelByDept(orgId);
					}
				} else {
					// check whether name equals vehicle plate
					VehicleModel vehicleModel = vehicleService.findVehicleByPlate(name);
					if (vehicleModel != null) {
						vehicleList = new ArrayList<VehicleModel>();
						vehicleList.add(vehicleModel);
					} else {
						// name should be dept name
						Organization org = null;
						if (loginUser.isEntAdmin()) {
							org = orgService.findByName(name, orgId);
						} else if (loginUser.isDeptAdmin()) {
							org = orgService.findByDeptName(name, orgId);

						}
						if (org != null) {
							vehicleList = vehicleService.getVehicleModelByDept(org.getId());
						}
					}
				}

				//过滤车辆绑定的license状态不正常的车辆
				if (vehicleList != null && !vehicleList.isEmpty()) {
					vehicleList = vehicleService.getInUsedVehicleListByDeviceNumber(vehicleList);
					LOG.info("vehicleList.size:" + vehicleList.size());
				}
				
				
				if (vehicleList != null && !vehicleList.isEmpty()) {
					// set total quantity
					totalVehs = vehicleList.size();

					for (VehicleModel vehicleModel : vehicleList) {
						// 从redis中取obd数据
						String obdRedisData = redisService.get(VEHICLE_PREFIX + vehicleModel.getDeviceNumber());
						if (StringUtils.isNotEmpty(obdRedisData)) {
							VehicleLocationModel vehicleLocationModel = MAPPER.readValue(obdRedisData,
									VehicleLocationModel.class);
							// GPS转百度坐标
							vehicleLocationModel = transGPStoBaidu(vehicleLocationModel);
							vehicleLocationModel.setId(vehicleModel.getId());
							// 过滤掉经纬度为0.0的值
							if (vehicleLocationModel.getLatitude() != 0.0
									&& vehicleLocationModel.getLongitude() != 0.0) {
								obdLocationList.add(vehicleLocationModel);
								
								//CR2399是否离线
								String tracetime = vehicleLocationModel.getTracetime();
								if(StringUtils.isNotEmpty(tracetime)){
									Date currentDate = new Date();
									long minutes = TimeUtils.timeBetween(TimeUtils.formatDate(tracetime),currentDate,Calendar.MINUTE);
									if(minutes > 10){//车辆最近一条数据与当前时间超过10分钟的，状态为离线
										vehicleLocationModel.setStatus(Constants.VEHICLE_RUNTIME_STATUS_OFFLINE);
									}
								}else{//无tracktime,无法判断上传时间，设置为离线
									vehicleLocationModel.setStatus(Constants.VEHICLE_RUNTIME_STATUS_OFFLINE);
								}
							}
						}

						// 确认车辆订单信息
						if (orderService.checkOnTask(vehicleModel.getId())) {
							onTaskVehs++;
						}
					}

					// set online quantity
					onlineVehs = obdLocationList.size();

					// set idle quantity
					idleVehs = totalVehs - onTaskVehs;
				}
			}

			// set vehs quantity
			model.setTotalVehs(totalVehs);
			model.setOnlineVehs(onlineVehs);
			model.setOnTaskVehs(onTaskVehs);
			model.setIdleVehs(idleVehs);

			// set JSON data body
			map.put("status", "success");
			map.put("data", model);
		} catch (Exception e) {
			LOG.error("VehicleController.queryObdLocationByName", e);
			map.put("status", "failure");
		}
		return map;
	}

	private VehicleLocationModel transGPStoBaidu(VehicleLocationModel vModel) {
		Point point = new Point();
		point.setLat(vModel.getLatitude());
		point.setLon(vModel.getLongitude());
		List<Point> points = new ArrayList<Point>();
		points.add(point);
		String result = shouqiService.transGPStoBaidu(points);
		TransGPStoBaiduModel transGPStoBaiduModel = JsonUtils.json2Object(result, TransGPStoBaiduModel.class);
		if (transGPStoBaiduModel.getResult().size() > 0) {
			vModel.setLatitude(transGPStoBaiduModel.getResult().get(0).getLat());
			vModel.setLongitude(transGPStoBaiduModel.getResult().get(0).getLon());
		}

		return vModel;
	}

	/**
	 * [部门管理员]查询部门的可用车辆(表格展示参见com.cmdt.carrental.portal.model.VehicleModel)
	 */
	@RequestMapping(value = "/{id}/listDeptVehicle", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listDeptVehicle(@PathVariable("id") Long orderUserOrgId, @CurrentUser User loginUser) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			List<VehicleModel> vehicleList = new ArrayList<>();
			//部门管理员
			if (loginUser.isDeptAdmin()) {
				vehicleList = vehicleService.listDeptVehicle(orderUserOrgId);
			}
			map.put("data", vehicleList);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.listDeptVehicle", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * [部门管理员]查询部门的可用的免审批车辆(表格展示参见com.cmdt.carrental.portal.model.VehicleModel)
	 */
	@RequestMapping(value = "/{id}/listDeptVehicle/NoNeedApprove", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listDeptVehicleNoNeedApprove(@PathVariable("id") Long orderUserOrgId, @CurrentUser User loginUser) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			List<VehicleModel> vehicleList = new ArrayList<>();
			//部门管理员
			if (loginUser.isDeptAdmin()) {
				vehicleList = vehicleService.listDeptVehicleNoNeedApprove(orderUserOrgId);
			}
			map.put("data", vehicleList);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.listDeptVehicleNoNeedApprove", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 查询单个OBD位置信息(或者说车辆位置信息)(redis调用)
	 * 
	 * @param deviceNumber
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryObdLocationByImei", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryObdLocationByImei(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			String deviceNumber = String.valueOf(jsonMap.get("deviceNumber"));
			String obdRedisData = redisService.get(VEHICLE_PREFIX + deviceNumber);
			if (StringUtils.isNotEmpty(obdRedisData)) {
				VehicleLocationModel vehicleLocationModel = MAPPER.readValue(obdRedisData, VehicleLocationModel.class);
				// GPS转百度坐标
				vehicleLocationModel = transGPStoBaidu(vehicleLocationModel);
				map.put("data", vehicleLocationModel);
			}
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.queryObdLocationByImei", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 查询单个车辆位置信息(或者说车辆位置信息)(redis调用)
	 * 
	 * @param vehicleId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/queryObdLocationByVehicleId", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryObdLocationByVehicleId(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Long vehicleId = Long.parseLong(String.valueOf(jsonMap.get("vehicleId")));
			Vehicle vehicle = vehicleService.findVehicleById(vehicleId);
			String deviceNumber = vehicle.getDeviceNumber();
			String obdRedisData = redisService.get(VEHICLE_PREFIX + deviceNumber);
			if (StringUtils.isNotEmpty(obdRedisData)) {
				VehicleLocationModel vehicleLocationModel = MAPPER.readValue(obdRedisData, VehicleLocationModel.class);
				map.put("data", vehicleLocationModel);
			}
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.queryObdLocationByVehicleId", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 方法已废弃
	 * 查询OBD轨迹信息
	 * 
	 * @param deviceNumber
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/monitor/findTripTrace", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findTripTraceDataByTimeRange(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			String imei = String.valueOf(jsonMap.get("imei"));
			String starttime = String.valueOf(jsonMap.get("starttime"));
			String endtime = String.valueOf(jsonMap.get("endtime"));
			List<TripTraceModel> tripTraceModelList = new ArrayList<TripTraceModel>();
			JsonNode obdNode = findTripTraceDataByTimeRange(imei, starttime, endtime);
			if (obdNode != null) {
				populatePoint(obdNode, tripTraceModelList);
				// populateSpeedAndAddress(obdNode,tripTraceModelList);
//				LOG.info("size+++1++++++" + tripTraceModelList.size());
				tripTraceModelList = populateSpeedAndAddressByGroup(obdNode, tripTraceModelList);
//				LOG.info("size+++2++++++" + tripTraceModelList.size());
			}

			map.put("status", "success");
			// 过滤掉经纬度为0.0的值
			List<TripTraceModel> resultList = new ArrayList<TripTraceModel>();
			if (tripTraceModelList != null && tripTraceModelList.size() > 0) {
				for (TripTraceModel tripTraceModelVal : tripTraceModelList) {
					if ((!"0.0".equals(tripTraceModelVal.getLatitude()))
							&& (!"0.0".equals(tripTraceModelVal.getLongitude()))) {
						resultList.add(tripTraceModelVal);
					}
				}
			}

			// 转换baidu坐标
			resultList = vehicleService.transformBaiduPoint(resultList);
			map.put("data", resultList);
		} catch (Exception e) {
			LOG.error("VehicleController.findTripTraceDataByTimeRange", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 首汽接口findTripTraceDataByTimeRange(车辆地理数据汇总)
	 * 
	 * @param imei
	 * @param starttime
	 * @param endtime
	 * @return
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public JsonNode findTripTraceDataByTimeRange(String imei, String starttime, String endtime) throws Exception {
		JsonNode obdNode = null;
		Map<String, Object> result = new ServiceAdapter(shouqiService)
				.doService(ActionName.FINDTRIPTRACEDATABYTIMERANGE, new Object[] { imei, starttime, endtime });
		if (result.get("data") != null) {
			JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
			if ("000".equals(jsonNode.get("status").asText())) {
				ArrayNode rows = (ArrayNode) jsonNode.get("result");
				if (rows != null && rows.size() > 0) {
					// 涉及到obd更换，只获得最新obd数据
					if (rows.size() == 1) {
						obdNode = rows.get(0);
					} else if (rows.size() == 2) {
						obdNode = rows.get(1);
					}
				}
			}
		}
		return obdNode;
	}

	/**
	 * 填充经纬度
	 * 
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public void populatePoint(JsonNode obdNode, List<TripTraceModel> list) throws Exception {
		JsonNode tracegeometryJsonNode = MAPPER.readTree(obdNode.get("tracegeometry").asText());
		JsonNode coordinatesNode = tracegeometryJsonNode.get("coordinates");
		if (coordinatesNode != null) {
			String coordinatesNodeTextVal = coordinatesNode.toString();
			if (StringUtils.isNotEmpty(coordinatesNodeTextVal)) {
				String[] coordinatesArr = coordinatesNodeTextVal.replace("],[", "#").replace("[", "").replace("]", "").split("#");
				if (coordinatesArr != null) {
					int eleLength = coordinatesArr.length;
					for (int i = 0; i < eleLength; i++) {
						String[] coordinatesValArr = coordinatesArr[i].split(",");
						TripTraceModel tripTraceModel = new TripTraceModel();
						tripTraceModel.setLongitude(coordinatesValArr[0]);
						tripTraceModel.setLatitude(coordinatesValArr[1]);
						list.add(tripTraceModel);
					}
				}
			}
		}
	}

	/**
	 * 填充tracetime,speed,以及百度address
	 * 
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	public void populateSpeedAndAddress(JsonNode obdNode, List<TripTraceModel> list) throws Exception {
		String idlistTextVal = obdNode.get("idlist").asText();
		String[] idlistArr = null;
		if (StringUtils.isNotEmpty(idlistTextVal)) {
			idlistArr = idlistTextVal.replace("},{", "#").replace("{", "").replace("[", "").replace("]", "")
					.replace("}", "").split("#");
			int idListArrLength = idlistArr.length;
			if (idListArrLength > 0) {
				Object[] objArr = new Object[idListArrLength];
				for (int i = 0; i < idListArrLength; i++) {
					objArr[i] = idlistArr[i];
				}
				Map<String, Object> result = new ServiceAdapter(shouqiService)
						.doService(ActionName.FINDREALTIMEGEOLISTDETAILBYID, objArr);
				if (result.get("data") != null) {
					JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
					if ("000".equals(jsonNode.get("status").asText())) {
						ArrayNode rows = (ArrayNode) jsonNode.get("result");
						if (rows != null) {
							int rowSize = rows.size();
							// 首汽trace接口返回数据与包装返回speed与address的接口返回数据size一致
							if (list.size() == rowSize) {
								for (int i = 0; i < rowSize; i++) {
									JsonNode speedAndAddressNode = rows.get(i);
									TripTraceModel tripTraceModel = list.get(i);
									tripTraceModel.setTracetime(speedAndAddressNode.get("tracetime").asText());
									tripTraceModel.setSpeed(speedAndAddressNode.get("speed").asText());
									tripTraceModel.setAddress(speedAndAddressNode.get("address").asText());
									if (Integer.valueOf(tripTraceModel.getSpeed()) > 0) {
										tripTraceModel.setStatus("运行中");
									} else {
										tripTraceModel.setStatus("停止");
									}
								}
							}
						}
					}
				}
			}
		}

	}

	private List<TripTraceModel> findrealTimegeoListDetailById(Object[] objArr, List<TripTraceModel> list) {
		try {
			Map<String, Object> result = new ServiceAdapter(shouqiService)
					.doService(ActionName.FINDREALTIMEGEOLISTDETAILBYID, objArr);
			if (result.get("data") != null) {
				JsonNode jsonNode;
				jsonNode = MAPPER.readTree(result.get("data").toString());
				if ("000".equals(jsonNode.get("status").asText())) {
					ArrayNode rows = (ArrayNode) jsonNode.get("result");
					if (rows != null) {
						int rowSize = rows.size();
						// 首汽trace接口返回数据与包装返回speed与address的接口返回数据size一致
						if (list.size() == rowSize) {
							for (int i = 0; i < rowSize; i++) {
								JsonNode speedAndAddressNode = rows.get(i);
								TripTraceModel tripTraceModel = list.get(i);
								tripTraceModel.setTracetime(speedAndAddressNode.get("tracetime").asText());
								tripTraceModel.setSpeed(speedAndAddressNode.get("speed").asText());
								tripTraceModel.setAddress(speedAndAddressNode.get("address").asText());
								if (Integer.valueOf(tripTraceModel.getSpeed()) > 0) {
									tripTraceModel.setStatus("运行中");
								} else {
									tripTraceModel.setStatus("停止");
								}
							}
						}
					}
				}
			}
		} catch (JsonProcessingException e) {
			LOG.error("VehicleController.findrealTimegeoListDetailById..", e);
		} catch (IOException e) {
			LOG.error("VehicleController.findrealTimegeoListDetailById..", e);
		}
		return list;
	}

	private List<TripTraceModel> populateSpeedAndAddressByGroup(JsonNode obdNode, List<TripTraceModel> list) {
		List<TripTraceModel> resultList = new ArrayList<TripTraceModel>();
		String idlistTextVal = obdNode.get("idlist").asText();
		String[] idlistArr = null;
		if (StringUtils.isNotEmpty(idlistTextVal)) {
			idlistArr = idlistTextVal.replace("},{", "#").replace("{", "").replace("[", "").replace("]", "").replace("}", "").split("#");
			int idListArrLength = idlistArr.length;
			if (idListArrLength > 0) {
				Object[] objArr = new Object[idListArrLength];
				for (int i = 0; i < idListArrLength; i++) {
					objArr[i] = idlistArr[i];
				}
				int length = list.size();
				for (int x = 0; x < (length / 300 + 1); x++) {
					int num = 300 * (x + 1);
					int objectLenth = 300;
					if (num > length) {
						num = length;
						objectLenth = num%300;
					}
//					LOG.info("num:" + num);
//					LOG.info("x:" + x);
//					LOG.info("objectLenth:" + objectLenth);
					Object[] objArrTemp = new Object[objectLenth];
					List<TripTraceModel> tempList = new ArrayList<TripTraceModel>();
					int z = 0;
					for (int y = 300 * x; y < num; y++) {
						objArrTemp[z] = objArr[y];
						tempList.add(list.get(y));
						z++;
					}
					resultList.addAll(findrealTimegeoListDetailById(objArrTemp, tempList));
				}
			}
		}
		return resultList;
	}
	
	/**
	 * 首汽接口findTripPropertyDataByTimeRange(车辆周期数据汇总)
	 * 
	 * @param imei
	 * @param starttime
	 * @param endtime
	 * @return
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/monitor/findTripPropertyDataByTimeRange", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findTripPropertyDataByTimeRange(String json) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			String imei = String.valueOf(jsonMap.get("imei"));
			String starttime = String.valueOf(jsonMap.get("starttime"));
			String endtime = String.valueOf(jsonMap.get("endtime"));

			TripPropertyModel tripPropertyModel = null;
			Map<String, Object> result = new ServiceAdapter(shouqiService)
					.doService(ActionName.FINDTRIPPROPERTYDATABYTIMERANGE, new Object[] { imei, starttime, endtime });
			if (result.get("data") != null) {
				JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
				if ("000".equals(jsonNode.get("status").asText())) {
					ArrayNode rows = (ArrayNode) jsonNode.get("result");
					if (rows != null && rows.size() > 0) {
						JsonNode dataNode = null;
						// 涉及到obd更换，只获得最新obd数据
						if (rows.size() == 1) {
							dataNode = rows.get(0);
						} else if (rows.size() == 2) {
							dataNode = rows.get(1);
						}
						tripPropertyModel = new TripPropertyModel();
						// 米转换为公里
						int mileage = dataNode.get("mileage").asInt();
						tripPropertyModel.setMileage(Double.valueOf(df.format((double) mileage / 1000)));
						double avgFule = dataNode.get("avgfuel").asDouble();
						if (avgFule == 0.0 || avgFule == 0) {
							// 理论油耗
							VehicleModel vehicleModel = vehicleService.findVehicleByImei(imei);
							if (vehicleModel != null) {
								tripPropertyModel.setFuel(Double.valueOf(
										df.format((double) mileage * vehicleModel.getTheoreticalFuelCon() / 100000)));
							} else {
								tripPropertyModel.setFuel(0.0);
							}
						} else {
							tripPropertyModel
									.setFuel(Double.valueOf(df.format(tripPropertyModel.getMileage() * avgFule / 100)));
						}
						tripPropertyModel.setDrivetime(dataNode.get("drivetime").asInt());
						tripPropertyModel.setStoptime(dataNode.get("stoptime").asInt());
					}
				}
			}
			map.put("status", "success");
			map.put("data", tripPropertyModel);
		} catch (Exception e) {
			LOG.error("VehicleController.findTripPropertyDataByTimeRange", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 方法已废弃
	 * [部门管理员]查询部门的可用车辆(表格展示参见com.cmdt.carrental.portal.model.VehicleModel)
	 * 
	 * @param vehicleType
	 *            车辆类型(0:经济 1:舒适 2:商务 3:豪华)
	 * @param planStTimeF
	 *            订单约车开始时间
	 * @param durationhours
	 *            订单持续时间
	 * @param currentPage
	 *            页数
	 * @param numPerPage
	 *            每页显示记录数
	 * @return
	 */
	@RequestMapping(value = "/listAvailableVehicle", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listAvailableVehicle(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			PagModel pagModel = vehicleService.listAvailableVehicleByDeptAdmin(loginUser, json);
			map.put("data", pagModel);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.listAvailableVehicle", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * [企业管理员、部门管理员]查询可用车辆(表格展示参见com.cmdt.carrental.portal.model.VehicleModel)
	 * 
	 * @param vehicleType
	 *            车辆类型(0:经济 1:舒适 2:商务 3:豪华)
	 * @param planStTimeF
	 *            订单约车开始时间
	 * @param durationhours
	 *            订单持续时间
	 * @param currentPage
	 *            页数
	 * @param numPerPage
	 *            每页显示记录数
	 * @return
	 */
	@RequestMapping(value = "/listAvailableVehicleByOrder", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listAvailableVehicleByOrder(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			BusiOrderQueryDto busiOrderModel = JsonUtils.json2Object(json, BusiOrderQueryDto.class);
			PagModel pagModel = vehicleService.listAvailableVehicleByAdmin(loginUser, busiOrderModel);
			map.put("data", pagModel);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.listAvailableVehicle", e);
			map.put("status", "failure");
		}
		return map;
	}
	/**
	 * 方法已废弃
	 * @param loginUser
	 * @return
	 */
	@RequestMapping(value = "/listVehicleTree", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listVehicleTree(@CurrentUser User loginUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		long entiId = loginUser.getOrganizationId();
		boolean isEnt = false;
		// 企业管理员
		if (loginUser.isEntAdmin()) {
			isEnt = true;
		}

		// 部门管理员
		if (loginUser.isDeptAdmin()) {
			isEnt = false;
		}
		List<VehicleRoot> root = vehicleService.queryVehicleTree(entiId, isEnt);
		map.put("children", root);
		return map;

	}

	/**
	 * 租车公司分配车辆的企业选择列表,企业须满足条件为:1.与租车公司有关联关系 2.企业的用车数大于实际用车数
	 * 
	 * @param loginUser
	 * @return
	 */
	@RequestMapping(value = "/listAvailableAssignedEnterprise", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listAvailableAssignedEnterprise(@CurrentUser User loginUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			List<VehicleEnterpriseModel> list = vehicleService
					.listAvailableAssignedEnterprise(loginUser.getOrganizationId());
			map.put("data", list);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.listAvailableAssignedEnterprise", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 分配车辆给企业或部门
	 * 
	 * @param vehicleId,orgid,orgname
	 * @return
	 */
	@RequestMapping(value = "/vehicleAssigne", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> vehicleAssigne(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Long orgid = loginUser.getOrganizationId();
			vehicleService.vehicleAssigne(orgid, json);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.vehicleAssigne", e);
			map.put("status", "failure");
		}
		return map;
	}

	/**
	 * 租车公司收回车辆
	 * 
	 * @param vehicleId
	 * @return
	 */
	@RequestMapping(value = "/vehicleRecover", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> vehicleRecover(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Long rentId = loginUser.getOrganizationId();
			vehicleService.vehicleRecover(rentId, json);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.vehicleRecover", e);
			map.put("status", "failure");
		}
		return map;
	}

	// 验证车牌号，车架号的唯一性
		private VehicleCountModel isValid(String vehicleNumber, String vehicleIdentification, Long id) {
			VehicleCountModel vehicleCountModel = new VehicleCountModel();
			if (!StringUtils.isBlank(vehicleNumber) && null != vehicleService.countByVehicleNumber(vehicleNumber, id)) {
				vehicleCountModel.setValidflag(1);
				vehicleCountModel.setMessage("该车牌号已被绑定，请重新输入！");
				return vehicleCountModel;
			}
			if (!StringUtils.isBlank(vehicleIdentification) && null!= vehicleService.countByVehicleIdentification(vehicleIdentification, id)) {
				vehicleCountModel.setValidflag(1);
				vehicleCountModel.setMessage("该车架号已被绑定，请重新输入！");
				return vehicleCountModel;
			}
			vehicleCountModel.setValidflag(0);
			return vehicleCountModel;
		}
	
	/**
	 * 查询车辆历史轨迹信息
	 * 
	 * @param deviceNumber
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/monitor/findVehicleHistoryTrack", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findVehicleHistoryTrack(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			String imei = String.valueOf(jsonMap.get("imei"));
			String starttime = String.valueOf(jsonMap.get("starttime"));
			String endtime = String.valueOf(jsonMap.get("endtime"));
			
			List<VehicleHistoryTrack> retList = new ArrayList<VehicleHistoryTrack>();

			Object[] params = new Object[] { imei, starttime, endtime };
			Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.FINDVEHICLEHISTORYTRACK, params);
			if (result.get("data") != null) {
				JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
				if ("000".equals(jsonNode.get("status").asText())) {
					ArrayNode rows = (ArrayNode) jsonNode.get("result");
					if (rows != null) {
						int rowSize = rows.size();
						if (rowSize > 0) {
							for (int i = 0; i < rowSize; i++) {
								JsonNode vehicleHistoryTrackNode = rows.get(i);
								VehicleHistoryTrack vehicleHistoryTrack = new VehicleHistoryTrack();
								vehicleHistoryTrack.setTracetime(vehicleHistoryTrackNode.get("tracetime").asText());
								vehicleHistoryTrack.setLongitude(vehicleHistoryTrackNode.get("longitude").asText());
								vehicleHistoryTrack.setLatitude(vehicleHistoryTrackNode.get("latitude").asText());
								vehicleHistoryTrack.setSpeed(vehicleHistoryTrackNode.get("speed").asText());
								vehicleHistoryTrack.setAddress(vehicleHistoryTrackNode.get("address").asText());
								vehicleHistoryTrack.setStatus(vehicleHistoryTrackNode.get("status").asText());
								retList.add(vehicleHistoryTrack);
							}
						}
						map.put("status", "success");
						map.put("data", retList);
					}else{
						map.put("status", "failure");
					}
				}else{
					 map.put("status", "failure");
				}
			}else{
				map.put("status", "failure");
			}
		} catch (Exception e) {
			LOG.error("VehicleController.findVehicleHistoryTrack", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * 查询车辆历史轨迹信息 new old:findVehicleHistoryTrack
	 * 
	 * @param deviceNumber
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/monitor/findVehicleHistoryTrackWithoutAddress", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findVehicleHistoryTrackWithoutAddress(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			String starttime = String.valueOf(jsonMap.get("starttime"));
			String endtime = String.valueOf(jsonMap.get("endtime"));
			String vehname = String.valueOf(jsonMap.get("vehname"));
			
			VehicleModel vehicleModel = vehicleService.findVehicleByPlate(vehname);
			if(vehicleModel == null){
				map.put("status", "failure");
				return map;
			}
			
			String imei = vehicleModel.getDeviceNumber();
			
			List<VehicleHistoryTrack> retList = new ArrayList<VehicleHistoryTrack>();
			
				List<TimeRangeModel> timeRangeModelList = TimeUtils.getSplitTimeList(starttime, endtime);//时间分割
				if(timeRangeModelList != null && timeRangeModelList.size() > 0){
					for(TimeRangeModel timeRangeModel : timeRangeModelList){
						String starttimeVal = timeRangeModel.getStarttime();
						String endtimeVal = timeRangeModel.getEndtime();
						if(StringUtils.isNotEmpty(starttimeVal) && StringUtils.isNotEmpty(endtimeVal)){
							Object[] params = new Object[] { imei,starttimeVal, endtimeVal};
							Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.FINDVEHICLEHISTORYTRACKWITHOUTADDRESS, params);
							if (result.get("data") != null) {
								JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
								if ("000".equals(jsonNode.get("status").asText())) {
									ArrayNode rows = (ArrayNode) jsonNode.get("result");
									if (rows != null) {
										int rowSize = rows.size();
										if (rowSize > 0) {
											for (int i = 0; i < rowSize; i++) {
												JsonNode vehicleHistoryTrackNode = rows.get(i);
												VehicleHistoryTrack vehicleHistoryTrack = new VehicleHistoryTrack();
												vehicleHistoryTrack.setTracetime(vehicleHistoryTrackNode.get("tracetime").asText());
												vehicleHistoryTrack.setLongitude(vehicleHistoryTrackNode.get("longitude").asText());
												vehicleHistoryTrack.setLatitude(vehicleHistoryTrackNode.get("latitude").asText());
												vehicleHistoryTrack.setSpeed(vehicleHistoryTrackNode.get("speed").asText());
												vehicleHistoryTrack.setStatus(vehicleHistoryTrackNode.get("status").asText());
												retList.add(vehicleHistoryTrack);
											}
										}
									}
								}
							}
						}
					}
				}
				
				if(retList != null && retList.size() > 0){
					map.put("status", "success");
					map.put("data", retList);
				}else{
					map.put("status", "failure");
				}
		} catch (Exception e) {
			LOG.error("VehicleController.findVehicleHistoryTrackWithoutAddress", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	
	/**
	 * 查询车辆可分配的司机
	 * 
	 * @param vehicleId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/findAvailableDriversByVehicleId", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findAvailableDriversByVehicleId(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
     		Long vehicleId = Long.valueOf(String.valueOf(jsonMap.get("vehicleId")));
     		List<DriverModel> driverList = vehicleService.findAvailableDriversByVehicleId(vehicleId);
			if (driverList!=null && !driverList.isEmpty()) {
				for (DriverModel driverModel: driverList) {
					String realnameAndPhone = driverModel.getRealname() + "  " + driverModel.getPhone();
					driverModel.setRealnameAndPhone(realnameAndPhone);
				}
			}
     		map.put("status", "success");
			map.put("data", driverList);
		} catch (Exception e) {
			LOG.error("VehicleController.findStationByVehicleId", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * 给车辆分配司机
	 * 
	 * @param vehicleId
	 * @return
	 */
	@RequestMapping(value = "/driverAllocate", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> driverAllocate(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
     		vehicleService.driverAllocate(json);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.driverAllocate", e);
			map.put("status", "failure");
		}
		return map;
	}
	/**
	 * 方法已废弃
	 * @param loginUser
	 * @return
	 */
	@RequestMapping(value = "/listVehicleStatusTree", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listVehicleStatusTree(@CurrentUser User loginUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		long orgId = loginUser.getOrganizationId();
		String entName = "";
		String deptName = "";
		boolean isEnt = false;
		// 企业管理员
		if (loginUser.isEntAdmin()) {
			isEnt = true;
			entName = loginUser.getOrganizationName();
		}

		// 部门管理员
		if (loginUser.isDeptAdmin()) {
			isEnt = false;
			deptName = loginUser.getOrganizationName();
		}
		List<VehicleStatusRoot> root = vehicleService.listVehicleStatusTree(orgId, isEnt,entName,deptName);
		map.put("children", root);
		return map;

	}
	
	/**
	 * 根据车牌号查询车辆信息
	 * @param loginUser
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/findVehicleInfoByVehicleNumber", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findVehicleByVehicleNumber(@CurrentUser User loginUser,String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			VehicleModel vehicleModel=null;
			if (StringUtils.isNotEmpty(json)) {
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				String vehicleNumber = TypeUtils.obj2String(jsonMap.get("vehicleNumber"));
				vehicleModel=vehicleService.getVehicleInfoByVehicleNumber(vehicleNumber,loginUser);
			}
			map.put("status", "success");
			map.put("data", vehicleModel);
		}catch(Exception e){
			LOG.error("Marker Controller find vehicle  error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * 下发车辆限速
	 * 
	 * @return
	 */
	@RequestMapping(value = "/updateSpeedLimit", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateSpeedLimit(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Vehicle vehModel =JsonUtils.json2Object(json, Vehicle.class);
			// 车辆信息
			VehicleModel vehicle = vehicleService.findByVehicleNumber(vehModel.getVehicleNumber());
			
			// 车辆绑定设备存在时,限制速度下发
			if(StringUtils.isNotBlank(vehicle.getDeviceNumber())) {
				// imei
				String imei = vehicle.getDeviceNumber();
				String result = deviceCommandService.setSpeedLimitCommand(imei, vehModel.getLimitSpeed(), 0);
				
				Map<String, Object> jsonMap = JsonUtils.json2Object(result, Map.class);
				String statusCode = "";
				if (jsonMap.get("status") != null) {
					statusCode = (String) jsonMap.get("status");
				}
				
				// 设备命令记录
	    		DevcieCommandConfigRecord devcieCommandConfigRecord = new DevcieCommandConfigRecord();
	    		devcieCommandConfigRecord.setDeviceNumber(imei);
	    		devcieCommandConfigRecord.setCommandType("SET_LIMIT_SPEED");
	    		devcieCommandConfigRecord.setCommandValue(String.valueOf(vehModel.getLimitSpeed()));
	    		java.util.Date utilDate = new java.util.Date();
	    		devcieCommandConfigRecord.setCommandSendTime(utilDate);
	    		devcieCommandConfigRecord.setUserId(loginUser.getId());
				
				if ("000".equals(statusCode)) {
					devcieCommandConfigRecord.setCommandExcuteStatus("excuting");
	    			devcieCommandConfigRecord.setCommandSendStatus(statusCode);
	    			map.put("status", "success");
				} else {
	    			devcieCommandConfigRecord.setCommandExcuteStatus("failure");
	    			devcieCommandConfigRecord.setCommandSendStatus(statusCode);
					map.put("msg", "下发限速失败");
					map.put("status", "failure");
				}
				// 插入 设备命令记录表
	    		deviceService.addDeviceCommandConfigRecord(devcieCommandConfigRecord);
			} else {
				map.put("msg", "车辆未绑定设备,下发限速失败");
				map.put("status", "failure");
			}
		} catch (Exception e) {
			LOG.error("VehicleController.updateSpeedLimit error, cause by:", e);
			map.put("msg", "下发限速失败");
			map.put("status", "failure");
		}
		return map;
	}
	
	 /**
     * 车辆行驶明显 车牌号自动匹配
     *
     */
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/listVehicleAutoComplete", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> findUnBindDeviceVehicle(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			String vehicleNumber = TypeUtils.obj2String(jsonMap.get("vehicleNumber"));
			List<VehicleModel> list= vehicleService.findVehicleByVehicleNumber(loginUser,vehicleNumber);
			map.put("status", "success");
			map.put("data", list);
		}catch(Exception e){
			LOG.error("Vehicle Controller listUnBindDeviceVehicle  error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
    
    
    /**
     * 方法已废弃
	 * 查询车辆历史轨迹信息
	 * 
	 * @param deviceNumber
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/monitor/findVehicleHistoryTrackByName", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findVehicleHistoryTrackByName(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			String vehname = String.valueOf(jsonMap.get("vehname"));
			String starttime = String.valueOf(jsonMap.get("starttime"));
			String endtime = String.valueOf(jsonMap.get("endtime"));
			
			VehicleModel vehicleModel = vehicleService.findVehicleByPlate(vehname);
			if(vehicleModel == null){
				map.put("status", "failure");
				return map;
			}
			String imei = vehicleModel.getDeviceNumber();
			
			List<VehicleHistoryTrack> retList = new ArrayList<VehicleHistoryTrack>();

			Object[] params = new Object[] { imei, starttime, endtime };
			Map<String, Object> result = new ServiceAdapter(shouqiService).doService(ActionName.FINDVEHICLEHISTORYTRACK, params);
			if (result.get("data") != null) {
				JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
				if ("000".equals(jsonNode.get("status").asText())) {
					ArrayNode rows = (ArrayNode) jsonNode.get("result");
					if (rows != null) {
						int rowSize = rows.size();
						if (rowSize > 0) {
							for (int i = 0; i < rowSize; i++) {
								JsonNode vehicleHistoryTrackNode = rows.get(i);
								VehicleHistoryTrack vehicleHistoryTrack = new VehicleHistoryTrack();
								vehicleHistoryTrack.setLongitude(vehicleHistoryTrackNode.get("longitude").asText());
								vehicleHistoryTrack.setLatitude(vehicleHistoryTrackNode.get("latitude").asText());
								//check null before set value
								if(vehicleHistoryTrackNode.get("tracetime") != null){
									vehicleHistoryTrack.setTracetime(vehicleHistoryTrackNode.get("tracetime").asText());
								}
								if(vehicleHistoryTrackNode.get("speed") != null){
									vehicleHistoryTrack.setSpeed(vehicleHistoryTrackNode.get("speed").asText());
								}
								if(vehicleHistoryTrackNode.get("address") != null){
									vehicleHistoryTrack.setAddress(vehicleHistoryTrackNode.get("address").asText());
								}
								if(vehicleHistoryTrackNode.get("status") != null){
									vehicleHistoryTrack.setStatus(vehicleHistoryTrackNode.get("status").asText());
								}
								retList.add(vehicleHistoryTrack);
							}
						}
						map.put("status", "success");
						map.put("data", retList);
					}else{
						map.put("status", "failure");
					}
				}else{
					 map.put("status", "failure");
				}
			}else{
				map.put("status", "failure");
			}
		} catch (Exception e) {
			LOG.error("VehicleController.findVehicleHistoryTrackByName", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * 首汽接口findTripPropertyDataByTimeRange(车辆周期数据汇总)
	 * 
	 * @param imei
	 * @param starttime
	 * @param endtime
	 * @return
	 * @throws IOException
	 * @throws JsonProcessingException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/monitor/findTripPropertyDataByTimeRangeByName", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findTripPropertyDataByTimeRangeByName(String json) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			String vehname = String.valueOf(jsonMap.get("vehname"));
			String starttime = String.valueOf(jsonMap.get("starttime"));
			String endtime = String.valueOf(jsonMap.get("endtime"));
			
			VehicleModel vehicle = vehicleService.findVehicleByPlate(vehname);
			if(vehicle == null){
				map.put("status", "failure");
				return map;
			}
			String imei = vehicle.getDeviceNumber();

			TripPropertyModel tripPropertyModel = null;
			Map<String, Object> result = new ServiceAdapter(shouqiService)
					.doService(ActionName.FINDTRIPPROPERTYDATABYTIMERANGE, new Object[] { imei, starttime, endtime });
			if (result.get("data") != null) {
				JsonNode jsonNode = MAPPER.readTree(result.get("data").toString());
				if ("000".equals(jsonNode.get("status").asText())) {
					ArrayNode rows = (ArrayNode) jsonNode.get("result");
					if (rows != null && rows.size() > 0) {
						JsonNode dataNode = null;
						// 涉及到obd更换，只获得最新obd数据
						if (rows.size() == 1) {
							dataNode = rows.get(0);
						} else if (rows.size() == 2) {
							dataNode = rows.get(1);
						}
						tripPropertyModel = new TripPropertyModel();
						// 米转换为公里
						int mileage = dataNode.get("mileage").asInt();
						tripPropertyModel.setMileage(Double.valueOf(df.format((double) mileage / 1000)));
						double avgFule = dataNode.get("avgfuel").asDouble();
						if (avgFule == 0.0 || avgFule == 0) {
							// 理论油耗
							VehicleModel vehicleModel = vehicleService.findVehicleByImei(imei);
							if (vehicleModel != null) {
								tripPropertyModel.setFuel(Double.valueOf(
										df.format((double) mileage * vehicleModel.getTheoreticalFuelCon() / 100000)));
							} else {
								tripPropertyModel.setFuel(0.0);
							}
						} else {
							tripPropertyModel
									.setFuel(Double.valueOf(df.format(tripPropertyModel.getMileage() * avgFule / 100)));
						}
						tripPropertyModel.setDrivetime(dataNode.get("drivetime").asInt());
						tripPropertyModel.setStoptime(dataNode.get("stoptime").asInt());
					}
				}
			}
			map.put("status", "success");
			map.put("data", tripPropertyModel);
		} catch (Exception e) {
			LOG.error("VehicleController.findTripPropertyDataByTimeRangeByName", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * 方法已废弃
	 * @param loginUser
	 * @return
	 */
		//新的监控首页树节点
	//原方法为listVehicleStatusTree
	@RequestMapping(value = "/listVehMoniStatusTree", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listVehMoniStatusTree(@CurrentUser User loginUser) {
		Map<String, Object> map = new HashMap<String, Object>();
		long orgId = loginUser.getOrganizationId();
		String entName = "";
		String deptName = "";
		boolean isEnt = false;
		// 企业管理员
		if (loginUser.isEntAdmin()) {
			isEnt = true;
			entName = loginUser.getOrganizationName();
		}

		// 部门管理员
		if (loginUser.isDeptAdmin()) {
			isEnt = false;
			deptName = loginUser.getOrganizationName();
		}
		List<VehicleStatusRoot> root = vehicleService.listVehMoniStatusTree(orgId, isEnt,entName,deptName);
		
		map.put("children", root);
		return map;

	}
	
	//新的监控首页树节点(最新版本 by kevin)
	//原方法为listVehicleStatusTree
	@RequestMapping(value = "/listVehMoniStatusTreeData", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listVehMoniStatusTreeData(@CurrentUser User loginUser,String vehicleNumber) {
		Map<String, Object> map = new HashMap<String, Object>();
		long orgId = loginUser.getOrganizationId();
		String orgName = loginUser.getOrganizationName();
		
		
		Organization organization = orgService.findById(orgId); 
		
		String type = "";
		String enterprisesType = organization.getEnterprisesType();//(0:租车公司，1:用车企业)
		if(StringUtils.isEmpty(enterprisesType)){
			type = "2";//部门及子部门
		}else{
			if("0".equals(enterprisesType)){
				type = "1";//租车公司
			}else{//企业
				type = "0";
			}
		}
		
		List<VehicleStatusRoot> root = vehicleService.listVehMoniStatusTreeData(orgId,orgName,type);
		
		if(!StringUtils.isEmpty(vehicleNumber)){
			//filter tree
			map.put("children", filterTreeWithVehicleNumber(root,vehicleNumber));
		}else{
			map.put("children", root);
		}
		return map;
	
	}
	
	
	@RequestMapping(value = "/listVehMoniStatusTreeByVehNum", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listVehMoniStatusTreeByVehNum(@CurrentUser User loginUser, String vehicleNumber) {
		Map<String, Object> map = new HashMap<String, Object>();
		long orgId = loginUser.getOrganizationId();
		String entName = "";
		String deptName = "";
		boolean isEnt = false;
		// 企业管理员
		if (loginUser.isEntAdmin()) {
			isEnt = true;
			entName = loginUser.getOrganizationName();
		}
	
		// 部门管理员
		if (loginUser.isDeptAdmin()) {
			isEnt = false;
			deptName = loginUser.getOrganizationName();
		}
		List<VehicleStatusRoot> root = vehicleService.listVehMoniStatusTree(orgId, isEnt,entName,deptName);
		
		//filter tree
		map.put("children", filterTreeWithVehicleNumber(root,vehicleNumber));
		return map;
	
	}
	
	
	/**判断当前部门是否有下级部门
	 * @param loginUser
	 * @return
	 */
	@RequestMapping(value="/checkCurrDeptHasChildDept")
	public Map checkCurrDeptIsHasChildDept(@CurrentUser User loginUser){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			Boolean checkFlag =false;
			// 部门管理员
			if (loginUser.isDeptAdmin()) {
				Long orgId = loginUser.getOrganizationId();
				List<Organization> orgList = orgService.findDownOrganizationListByOrgId(orgId);
				if(orgList!=null && orgList.size()==1){
					checkFlag=true;
				}
			}
			map.put("checkFlag", checkFlag);
			map.put("status", "success");
			
		}catch(Exception e){
			LOG.error("VehicleController.checkCurrentDeptIsHasChildDept", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**方法废弃无用 
	 * 获取组织管理界面的  现有车辆列表
	 * @param loginUser
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/organization/listVehicleForOrg", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listVehicleForOrg(String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			PagModel pagModel = new PagModel();
			VehicleListForOrgDto vehModel =JsonUtils.json2Object(json, VehicleListForOrgDto.class);
			if(vehModel!=null){
				if(vehModel.getDeptId()!=null){
					Organization org=orgService.findById(vehModel.getDeptId());
					if(org.getParentId()==0){
						vehModel.setEntId(vehModel.getDeptId());
						vehModel.setDeptId(null);
					}
				}
				pagModel=vehicleService.findVehicelPageListForOrg(vehModel);
			}

			map.put("data", pagModel);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.listVehicleForOrg error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/** 
	 * 获取组织管理界面的  可分配车辆列表
	 * @param loginUser
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/organization/listUnAssignedVehicle", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listUnAssignedVehicle(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			VehicleListForOrgDto vehModel =JsonUtils.json2Object(json, VehicleListForOrgDto.class);
			
			// 企业管理员
			if (loginUser.isEntAdmin()) {
				Long entId = loginUser.getOrganizationId();
				vehModel.setEntId(entId);
			}

			// 部门管理员
			if (loginUser.isDeptAdmin()) {
				Long deptId = loginUser.getOrganizationId();
				Organization org=orgService.findTopOrganization(deptId);
				vehModel.setEntId(org.getId());
			}
			
			PagModel pagModel=vehicleService.findUnAssignedVehicelListForOrg(vehModel);

			map.put("data", pagModel);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.listUnAssignedVehicle error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/** 
	 * 组织管理界面  添加车辆到本部门
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/organization/addVehicelToCurrOrg", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> addVehicelToCurrOrg(String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			if(json!=null){
				
				AllocateDepModel allocateDepModel =JsonUtils.json2Object(json, AllocateDepModel.class);
				Organization org=orgService.findById(allocateDepModel.getAllocateDepId());
				//如果分配id为企业，则不能分配给自己
				if(org!=null && org.getParentId()!=0){
					String vehicelIds = allocateDepModel.getIds();
					if(StringUtils.isNotBlank(vehicelIds)){
						String[] tempVehicelIds=vehicelIds.split(",");
						Long[] VehicelIdsArr= new Long[tempVehicelIds.length];
						for(int i=0;i<tempVehicelIds.length;i++){
							VehicelIdsArr[i]=TypeUtils.obj2Long(tempVehicelIds[i]);
						}
						allocateDepModel.setIdArray(VehicelIdsArr);
						
						allocateDepModel.setDeptName(org.getName());
						vehicleService.updateCurrOrgToVehicle(allocateDepModel);
					}
				}
			}
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.addVehicelToCurrOrg error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/** 
	 * 获取组织管理界面的  获取车辆列表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/organization/findVehicleListbyIds", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findVehicleListbyIds(String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			List<VehicleListForOrgDto> vehicleList=new ArrayList<VehicleListForOrgDto>();
			if(StringUtils.isNotBlank(json)){
				Map<String, Object> jsonMap=JsonUtils.json2Object(json, Map.class);
				String ids = String.valueOf(jsonMap.get("ids"));
				String[] tempIds=ids.split(",");
				Long [] VehicelIds= new Long[tempIds.length];
				for(int i=0;i<tempIds.length;i++){
					VehicelIds[i]= Long.parseLong(tempIds[i]);
				}
				vehicleList=vehicleService.findVehicleListbyIds(VehicelIds);
			}

			map.put("data", vehicleList);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.findVehicleListbyIds error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/** 
	 * 组织管理界面的  ,从部门移除车辆
	 * @param loginUser
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/organization/removeVehicleFromOrg", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> removeVehicleFromOrg(@CurrentUser User loginUser,String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			AllocateDepModel allocateDepModel =JsonUtils.json2Object(json, AllocateDepModel.class);
			if(allocateDepModel!=null){
			
				//如果当前为企业管理员,选中树节点也为企业，则不能移除车辆
				if(loginUser.isAdmin() && allocateDepModel.getAllocateDepId()!=null && allocateDepModel.getAllocateDepId().equals(loginUser.getOrganizationId())){
					map.put("status", "failure"); 
				}else{

					String ids=allocateDepModel.getIds();
					if(StringUtils.isNotBlank(ids)){
						String[] tempIds=ids.split(",");
						Long [] VehicelIds= new Long[tempIds.length];
						for(int i=0;i<tempIds.length;i++){
							VehicelIds[i]=Long.parseLong(tempIds[i]);
						}
						allocateDepModel.setIdArray(VehicelIds);			
						List<VehicleAndOrderModel> unRemoveVehicleList = vehicleService.findUnRemoveVehicleList(allocateDepModel);
						if(!unRemoveVehicleList.isEmpty()){
							map.put("data", unRemoveVehicleList);
							map.put("status", "failure"); 
						}else{
							map.put("status", "success"); 
						}
					}else{
						map.put("status", "failure"); 
					}
				}
			}
		} catch (Exception e) {
			LOG.error("VehicleController.removeVehicleFromOrg error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * 车辆品牌列表
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "/organization/listvehicleModel", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> listvehicleModel(String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			Long deptId = Long.valueOf(String.valueOf(jsonMap.get("deptId")));
			String tFlag = String.valueOf(jsonMap.get("tFlag"));
			Boolean isEnt=false;
			if(deptId!=null){
				Organization org=orgService.findById(deptId);
				if(org.getParentId()==0){
					isEnt=true;
				}
				List<VehicleBrandModel> vBrandList=	vehicleService.listvehicleModel(isEnt,deptId,tFlag);
				map.put("data", vBrandList);
			}
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.listvehicleModel error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * filterTreeWithVehicleNumber
	 * @param tree
	 * @param vehicleNumber
	 * @return
	 */
	private static List<VehicleStatusRoot> filterTreeWithVehicleNumber(List<VehicleStatusRoot> tree, String vehicleNumber){
		if(tree != null && StringUtils.isNotBlank(vehicleNumber)){
			try{
				//进行UTF转码
//				vehicleNumber = new String(vehicleNumber .getBytes("iso8859-1"),"utf-8");
//				vehicleNumber = URLDecoder.decode(vehicleNumber, "UTF-8");
				
				//过滤双引号
				vehicleNumber = vehicleNumber.replace("\"", "");
				
				//过滤单引号
				vehicleNumber = vehicleNumber.replace("\'", "");
				
				//进行统一大写处理
				vehicleNumber = vehicleNumber.toUpperCase();
				
				//root filter with vehicle number
				for(VehicleStatusRoot root : tree){
					filterChildrenNodes(root.getChildren(), vehicleNumber);
					filterNullChildrenNodes(root.getChildren());
				}
			}catch(Exception e){
				LOG.error("filter tree exception happened!", e);
			}
		}
		
		return tree;
	}
	
	
	/**
	 * filterChildrenNodes
	 * @param nodes
	 * @param vehicleNumber
	 * @return
	 */
	private static boolean filterChildrenNodes(List<VehicleTreeStatusModel> nodes, String vehicleNumber){
		boolean flag = false;
		if(nodes != null){
			for(int i = 0;i<nodes.size();i++){
				VehicleTreeStatusModel node = nodes.get(i);
				if(!node.isLeaf()){
					if(!filterChildrenNodes(node.getChildren(), vehicleNumber)){
//						nodes.remove(i);
						nodes.set(i, null);
//						System.out.println("not leaf:"+JsonUtils.object2Json(node));
					}else{
						flag = true;
					}
				}else{
					String viewType = node.getViewType();
					String[] tmps = viewType.split("_");
					if(tmps != null && tmps.length > 3){
						viewType = tmps[2];
					}
					if(!viewType.contains(vehicleNumber)){
//						nodes.remove(i);
						nodes.set(i, null);
//						System.out.println("leaf:"+JsonUtils.object2Json(node));
					}else{
						flag = true;
					}
				}
			}
		}
		return flag;
	}
	
	/**
	 * filterNullChildrenNodes
	 * @param nodes
	 */
	private static void filterNullChildrenNodes(List<VehicleTreeStatusModel> nodes){
		if(nodes != null && !nodes.isEmpty()){
			List<VehicleTreeStatusModel> nullTempProd = new ArrayList<VehicleTreeStatusModel>(1);  
			nullTempProd.add(null);
			nodes.removeAll(nullTempProd);
			
			for(VehicleTreeStatusModel node : nodes){
				if(!node.isLeaf() && node.getChildren() != null){
					filterNullChildrenNodes(node.getChildren());
				}
			}
		}
	}
	
	
	public static void main(String[] args){
		List<VehicleStatusRoot> root = new ArrayList<VehicleStatusRoot>();
		VehicleStatusRoot rt = new VehicleStatusRoot("test_root");
		root.add(rt);
		
		List<VehicleTreeStatusModel> nodesLV2 = new ArrayList<VehicleTreeStatusModel>();
		rt.setChildren(nodesLV2);
		
		VehicleTreeStatusModel model1 = new VehicleTreeStatusModel();
		model1.setLeaf(false);
		model1.setViewType("test_dept1");

		VehicleTreeStatusModel model2 = new VehicleTreeStatusModel();
		model2.setLeaf(true);
		model2.setViewType("7686867_鄂123456_678678687");
		
		nodesLV2.add(model1);
		nodesLV2.add(model2);
		
		
		List<VehicleTreeStatusModel> nodesLV3 = new ArrayList<VehicleTreeStatusModel>();
		model1.setChildren(nodesLV3);
		
		VehicleTreeStatusModel model3 = new VehicleTreeStatusModel();
		model3.setLeaf(true);
		model3.setViewType("123123123_鄂67890_123123123");

		VehicleTreeStatusModel model4 = new VehicleTreeStatusModel();
		model4.setLeaf(false);
		model4.setViewType("test_dept2");
		
		nodesLV3.add(model3);
		nodesLV3.add(model4);
		
		
		List<VehicleTreeStatusModel> nodesLV4 = new ArrayList<VehicleTreeStatusModel>();
		model4.setChildren(nodesLV4);
		
		VehicleTreeStatusModel model5 = new VehicleTreeStatusModel();
		model5.setLeaf(true);
		model5.setViewType("ssssss_鄂XXXXX_fffffff");
		
		nodesLV4.add(model5);
		
//		System.out.println("before:"+JsonUtils.object2Json(root));
		root = filterTreeWithVehicleNumber(root,"\'6\'");
		System.out.println("after:"+JsonUtils.object2Json(root));
	}
	
	/**
	 * 查询多个OBD位置信息(或者说车辆位置信息  by kevin new old:queryObdLocationByName)(redis调用)
	 * 
	 * @return
	 */
	@RequestMapping(value = "/queryObdLocation", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> queryObdLocation(@CurrentUser User loginUser,String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			HomePageMapModel model = new HomePageMapModel();
			List<VehicleLocationModel> obdLocationList = new ArrayList<VehicleLocationModel>();
			model.setObdLocationList(obdLocationList);
			
			Long orgId = 0l;
			String vehicleNumber = "";
			
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			if(jsonMap.get("orgId") != null){
				String orgIdVal = String.valueOf(jsonMap.get("orgId"));
				if(!StringUtils.isEmpty(orgIdVal)){
					orgId = Long.valueOf(orgIdVal);
				}
			}
			
			if(jsonMap.get("vehicleNumber") != null){
				String vehicleNumberVal = String.valueOf(jsonMap.get("vehicleNumber"));
				if(!StringUtils.isEmpty(vehicleNumberVal)){
					vehicleNumber = vehicleNumberVal;
				}
			}
			
			List<VehicleModel> vehicleList = new ArrayList<VehicleModel>();
			if(orgId.longValue() == 0 && "".equals(vehicleNumber)){//根节点查询
				orgId = loginUser.getOrganizationId();
				
				Organization organization = orgService.findById(orgId);
				String enterprisesType = organization.getEnterprisesType();//(0:租车公司，1:用车企业)
				if(StringUtils.isEmpty(enterprisesType)){
					vehicleList = vehicleService.queryVehicleListByDept(orgId);
				}else{
					vehicleList = vehicleService.queryVehicleListByEntAndRent(orgId,enterprisesType);
				}
			}else{
				if(orgId.longValue() != 0){//部门或子部门查询
					vehicleList = vehicleService.queryVehicleListByDept(orgId);
				}else{//按车牌查询
					VehicleModel vehicleModel = vehicleService.findVehicleByPlate(vehicleNumber);
					if(vehicleModel != null){
						vehicleList.add(vehicleModel);
					}
				}
			}

			//过滤车辆绑定的license状态不正常的车辆
			if (vehicleList != null && vehicleList.size() > 0) {
				vehicleList = vehicleService.getInUsedVehicleListByDeviceNumber(vehicleList);
			}
			
			if (vehicleList != null && !vehicleList.isEmpty()) {

				for (VehicleModel vehicleModel : vehicleList) {
				    
				    //filter when enableSecret = 1
				    if(vehicleModel.getEnableSecret() == 1){
				        continue;
				    } 
				    
					// 从redis中取obd数据
					String obdRedisData = redisService.get(VEHICLE_PREFIX + vehicleModel.getDeviceNumber());
					if (StringUtils.isNotEmpty(obdRedisData)) {
						VehicleLocationModel vehicleLocationModel = MAPPER.readValue(obdRedisData,
								VehicleLocationModel.class);
						// GPS转百度坐标
						vehicleLocationModel = transGPStoBaidu(vehicleLocationModel);
						vehicleLocationModel.setId(vehicleModel.getId());
						// 过滤掉经纬度为0.0的值
						if (vehicleLocationModel.getLatitude() != 0.0
								&& vehicleLocationModel.getLongitude() != 0.0) {
							obdLocationList.add(vehicleLocationModel);
							
							String tracetime = vehicleLocationModel.getTracetime();
							if(StringUtils.isNotEmpty(tracetime)){
								Date currentDate = new Date();
								long minutes = TimeUtils.timeBetween(TimeUtils.formatDate(tracetime),currentDate,Calendar.MINUTE);
								if(minutes > 10){//车辆最近一条数据与当前时间超过10分钟的，状态为离线
									vehicleLocationModel.setStatus(Constants.VEHICLE_RUNTIME_STATUS_OFFLINE);
								}
							}else{//无tracktime,无法判断上传时间，设置为离线
								vehicleLocationModel.setStatus(Constants.VEHICLE_RUNTIME_STATUS_OFFLINE);
							}
						}
					}

				}

			}

			map.put("status", "success");
			map.put("data", model);
		} catch (Exception e) {
			LOG.error("VehicleController.queryObdLocationByName", e);
			map.put("status", "failure");
		}
		return map;
	}
	

	/**
	 * 根据vehicle number 查询已分配marker
	 * @param loginUser
	 * @param json
	 * @return
	 */
	@RequestMapping(value="/findVehicleAssignedMarkers",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findVehicleAssignedMarkers(@CurrentUser User loginUser,String json){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			VehicleQueryDTO vehicleQueryDTO= JsonUtils.json2Object(json, VehicleQueryDTO.class);
			if(StringUtils.isNotBlank(json)){
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				String vehicleNumber =String.valueOf(jsonMap.get("vehicleNumber"));
				Integer currentPage =Integer.valueOf(String.valueOf(jsonMap.get("currentPage")));
				Integer numPerPage =Integer.valueOf(String.valueOf(jsonMap.get("numPerPage")));
				PagModel pagModel=vehicleService.findVehicleAssignedMarkers(vehicleNumber,currentPage,numPerPage);
				map.put("data", pagModel);
			}
			
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.list error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * 查询可分配marker
	 * @param loginUser
	 * @param json
	 * @return
	 */
	@RequestMapping(value="/findVehicleAvialiableMarkers",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> findVehicleAvialiableMarkers(@CurrentUser User loginUser,String json){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			if(StringUtils.isNotBlank(json)){
				VehicleQueryDTO vehicleQueryDto =JsonUtils.json2Object(json, VehicleQueryDTO.class);
				vehicleQueryDto.setOrganizationId(loginUser.getOrganizationId());
				PagModel pagModel=vehicleService.findVehicleAvialiableMarkers(vehicleQueryDto);
				map.put("data", pagModel);
			}
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.list error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * 为车辆分配marker
	 * @param loginUser
	 * @param json
	 * @return
	 */
	@RequestMapping(value="/assignMarkers",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> assignMarkers(@CurrentUser User loginUser,String json){

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			if (StringUtils.isNotEmpty(json)) {
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				Long vehicleId =Long.valueOf(String.valueOf(jsonMap.get("vehicleId")));
				String markerIds = String.valueOf(jsonMap.get("markerIds"));
				vehicleService.assignMarkers(vehicleId,markerIds);
			}
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("Marker Controller assignVehicles error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	/**
	 * 移除车辆分配的marker
	 * @param loginUser
	 * @param json
	 * @return
	 */
	@RequestMapping(value="/unassignMarkers",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> unassignMarkers(@CurrentUser User loginUser,String json){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			if (StringUtils.isNotEmpty(json)) {
				Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
				Long vehicleId =Long.valueOf(String.valueOf(jsonMap.get("vehicleId")));
				Long markerId = Long.valueOf(String.valueOf(jsonMap.get("markerId")));
				vehicleService.unassignMarkers(vehicleId,markerId);
			}
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("Marker Controller assignVehicles error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
	
	/**
     * 设置车辆涉密
     * 
     * @return
     */
    @RequestMapping(value = "/setVehSecret", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> setVehSecret(@CurrentUser User loginUser, String json) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", "");
        try {
            Vehicle vehModel =JsonUtils.json2Object(json, Vehicle.class);
            // 车辆信息
            VehicleModel vehicle = vehicleService.findByVehicleNumber(vehModel.getVehicleNumber());
            
            // 车辆绑定设备存在时,限制速度下发
            if(vehicle != null && vehicle.getId() != null) {
                vehicleService.setVehSecret(vehicle.getId(),vehModel.getEnableSecret(),vehicle.getDeviceNumber(),loginUser.getId());
                map.put("status", "success");
            } else {
                map.put("msg", "车牌号无效,设置涉密失败");
                map.put("status", "failure");
            }
        } catch (Exception e) {
            LOG.error("VehicleController.setVehSecret error, cause by:", e);
            map.put("msg", "设置涉密失败");
            map.put("status", "failure");
        }
        return map;
    }
    
    
    /**
     * 设置车辆涉密
     * 
     * @return
     */
    @RequestMapping(value = "/setTrafficPackage", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> setTrafficPackage(@CurrentUser User loginUser, String json) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", "");
        try {
            Vehicle vehModel =JsonUtils.json2Object(json, Vehicle.class);
            // 车辆信息
            VehicleModel vehicle = vehicleService.findByVehicleNumber(vehModel.getVehicleNumber());
            
            // 车辆绑定设备存在时,限制速度下发
            if(vehicle != null && vehicle.getId() != null) {
                vehicleService.setTrafficPackage(vehicle.getId(),vehModel.getEnableTrafficPkg(),vehicle.getDeviceNumber(),loginUser.getId());
                map.put("status", "success");
            } else {
                map.put("msg", "车牌号无效,设置M2M失败");
                map.put("status", "failure");
            }
        } catch (Exception e) {
            LOG.error("VehicleController.setVehSecret error, cause by:", e);
            map.put("msg", "设置M2M失败");
            map.put("status", "failure");
        }
        return map;
    }
    
    /**
     * 设置免审核车辆
     * 
     * @return
     */
    @RequestMapping(value = "/setNoNeedApprove", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> setNoNeedApprove(@CurrentUser User loginUser, String json) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", "");
        try {
            Vehicle vehModel =JsonUtils.json2Object(json, Vehicle.class);
            
            // 设置免审核车辆
            if (vehicleService.setNoNeedApprove(vehModel.getId())) {
            	map.put("status", "success");
            	
            } else {
            	map.put("msg", "车辆ID无效,设置免审核车辆失败");
            	map.put("status", "failure");
            }
            
        } catch (Exception e) {
            LOG.error("VehicleController.setNoNeedApprove error, cause by:", e);
            map.put("msg", "设置免审核车辆失败");
            map.put("status", "failure");
        }
        return map;
    }
    
    @RequestMapping(value = "/authorized/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String,Object>>  authorizedCreate(@CurrentUser User loginUser, HttpServletRequest request, HttpServletResponse response){
    	HttpHeaders headers = new HttpHeaders(); 
    	headers.setContentType(MediaType.TEXT_HTML);
    	Map<String,Object> map = new HashMap<>();
    	map.put("data", "");
    	try {
    		ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
			CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
			MultipartHttpServletRequest multipartRequest = commonsMultipartResolver
					.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
			List<MultipartFile> list= multipartRequest.getFiles("file");
			String attachName="";
			for (MultipartFile multipartFile : list) {
				if (!multipartFile.isEmpty()) {
						String savePath = "/opt/apache-tomcat/webapps/resources/upload/authorized";
						attachName=attachName+multipartFile.getOriginalFilename()+",";
						//String savePath = "D:/tmp";
						File dir=new File(savePath);
						if(!dir.exists()){
							dir.mkdirs();
						}
						File dest = new File(savePath + File.separator + multipartFile.getOriginalFilename());
						if (dest.exists()) {
							dest.delete();
						}
						multipartFile.transferTo(dest);
				}
				
			}	
			VehicleAuthorized vehicleAuthorized =new VehicleAuthorized();
			String docNo=DateUtils.getNowDate("yyyyMMdd") + subTimeStringforOrder(System.currentTimeMillis());//201504A000001
	    	vehicleAuthorized.setDocCode(docNo);
	    	vehicleAuthorized.setApplyTime(new Date());
	    	vehicleAuthorized.setStatus("0");
	    	vehicleAuthorized.setDeptId(TypeUtils.obj2Long(multipartRequest.getParameter("deptId")));
	    	vehicleAuthorized.setEmergencyVehAuthNum(TypeUtils.obj2Long(multipartRequest.getParameter("emergencyVehAuthNum")));
	    	vehicleAuthorized.setEmergencyVehRealNum(TypeUtils.obj2Long(multipartRequest.getParameter("emergencyVehRealNum")));
	    	vehicleAuthorized.setEnforcementVehAuthNum(TypeUtils.obj2Long(multipartRequest.getParameter("enforcementVehAuthNum")));
	    	vehicleAuthorized.setEnforcementVehRealNum(TypeUtils.obj2Long(multipartRequest.getParameter("enforcementVehRealNum")));
	    	vehicleAuthorized.setSpecialVehAuthNum(TypeUtils.obj2Long(multipartRequest.getParameter("specialVehAuthNum")));
	    	vehicleAuthorized.setSpecialVehRealNum(TypeUtils.obj2Long(multipartRequest.getParameter("specialVehRealNum")));
	    	vehicleAuthorized.setNormalVehAuthNum(TypeUtils.obj2Long(multipartRequest.getParameter("normalVehAuthNum")));
	    	vehicleAuthorized.setNormalVehRealNum(TypeUtils.obj2Long(multipartRequest.getParameter("normalVehRealNum")));
	    	vehicleAuthorized.setMajorVehAuthNum(TypeUtils.obj2Long(multipartRequest.getParameter("majorVehAuthNum")));
	    	vehicleAuthorized.setMajorVehRealNum(TypeUtils.obj2Long(multipartRequest.getParameter("majorVehRealNum")));
	    	vehicleAuthorized.setCause(TypeUtils.obj2String(multipartRequest.getParameter("cause")));
	    	vehicleAuthorized.setPoliceAdd(TypeUtils.obj2Long(multipartRequest.getParameter("policeAdd")));
	    	vehicleAuthorized.setEmergencyVehAddNum(TypeUtils.obj2Long(multipartRequest.getParameter("emergencyVehAddNum")));
	    	vehicleAuthorized.setEnforcementVehAddNum(TypeUtils.obj2Long(multipartRequest.getParameter("enforcementVehAddNum")));
	    	vehicleAuthorized.setSpecialVehAddNum(TypeUtils.obj2Long(multipartRequest.getParameter("specialVehAddNum")));
	    	vehicleAuthorized.setNormalVehAddNum(TypeUtils.obj2Long(multipartRequest.getParameter("normalVehAddNum")));
	    	vehicleAuthorized.setMajorVehAddNum(TypeUtils.obj2Long(multipartRequest.getParameter("majorVehAddNum")));
	    	if(StringUtils.isNotBlank(attachName)){
	    		attachName= attachName.substring(0, attachName.length()-1);
	    	}
	    	vehicleAuthorized.setAttachName(attachName);
	    	vehicleService.create(vehicleAuthorized);
	    	map.put("status", "success");
	    	map.put("success", true);
		} catch (Exception e) {
			 map.put("status", "failure");
    		 LOG.error("Failed to create due to unexpected error!", e);
		}
    	return new ResponseEntity<>(map,headers,HttpStatus.OK);
    }
    
    public Map<String, Object>  authorizedCreate(@CurrentUser User loginUser, String json){
    	Map<String, Object> map = new HashMap<String, Object>();
    	try{
    	
	    	VehicleAuthorized vehicleAuthorized =JsonUtils.json2Object(json, VehicleAuthorized.class);
	    	String docNo=DateUtils.getNowDate("yyyyMMdd") + subTimeStringforOrder(System.currentTimeMillis());//201504A000001
	    	vehicleAuthorized.setDocCode(docNo);
	    	vehicleAuthorized.setApplyTime(new Date());
	    	vehicleAuthorized.setStatus("0");
	    	//vehicleAuthorized.setDeptId(loginUser.getOrganizationId());
	    	vehicleService.create(vehicleAuthorized);
	    	map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("VehicleController.setNoNeedApprove error, cause by:", e);
            map.put("status", "failure");
    	}
    	return map;
    }
    
    
    
    /**
     * subTimeStringforOrder
     * @param timemillis
     * @return
     */
    private String subTimeStringforOrder(Long timemillis){
    	int len = String.valueOf(timemillis).length();
    	String timeStrNo = String.valueOf(timemillis);
    	if(len > 6){
    		timeStrNo = timeStrNo.substring(len - 6);
    	}
    	return timeStrNo;
    }
    
    
    /**
     * 上传附件
     * @param attachName
     * @param request
     * @param response
     */
	public boolean upload(String attachName, MultipartHttpServletRequest multipartRequest) {
		boolean boo = false;
		try {
			multipartRequest.setCharacterEncoding("UTF-8");
			MultipartFile multiFile = multipartRequest.getFile("authorizedAttach");
			if (!multiFile.isEmpty()) {
				String savePath = "/opt/apache-tomcat/webapps/resources/upload/authorized";
//				String savePath =multipartRequest.getServletContext().getRealPath("resources/upload/order");
				File dir=new File(savePath);
				if(!dir.exists()){
					dir.mkdirs();
				}
				File dest = new File(savePath + File.separator + attachName);
				if (dest.exists()) {
					dest.delete();
				}
				multiFile.transferTo(dest);
				boo = true;
			}
		} catch (IOException e) {
			LOG.error("Failed to upload due to IO error!", e);
			return boo;
		} catch (Exception e) {
			LOG.error("Failed to upload due to unexpected error!", e);
			return boo;
		}
		return boo;
	}
	
	
	@RequestMapping(value = "/authorized/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listAuthorized(@CurrentUser User loginUser, String json) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "");
		try {
			
			Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
			int currentPage = Integer.parseInt(String.valueOf(jsonMap.get("currentPage")));
			int numPerPage = Integer.parseInt(String.valueOf(jsonMap.get("numPerPage")));
			PagModel pagModel = vehicleService.listAuthorized(currentPage, numPerPage);

			map.put("data", pagModel);
			map.put("status", "success");
		} catch (Exception e) {
			LOG.error("VehicleController.list error, cause by:", e);
			map.put("status", "failure");
		}
		return map;
	}
	
}
