package com.cmdt.carrental.portal.web.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.cmdt.carrental.common.entity.DevcieCommandConfigRecord;
import com.cmdt.carrental.common.entity.Device;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.integration.DeviceCommandService;
import com.cmdt.carrental.common.integration.ShouqiService;
import com.cmdt.carrental.common.model.DeviceModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.ResponseTreeModel;
import com.cmdt.carrental.common.service.DeviceService;
import com.cmdt.carrental.common.service.VehicleService;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carrental.common.util.CsvUtil;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Controller
@RequestMapping("/device")
public class DeviceController extends BaseController {
	private static final Logger LOG = LoggerFactory.getLogger(DeviceController.class);
	
	
	private static final ObjectMapper MAPPER = new ObjectMapper();
	@Autowired
    private DeviceService deviceService;
	@Autowired
    private VehicleService vehicleService;
	@Autowired
    private ShouqiService shouqiService;
	@Autowired
	private DeviceCommandService deviceCommandService;
	
    /**
     * [系统管理员]查询设备列表
     * @param json{"imeiNumber":"IMEI1001","vehicleNumber":"鄂A88888","deviceVendor":"DBJ","vehicleSource":"12","deviceStatus":"1"}
     * @return
     */
    @RequiresPermissions(value={"device:list"})
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> deviceList(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		DeviceModel deviceModel = JsonUtils.json2Object(json, DeviceModel.class);
    		PagModel pageModel = deviceService.deviceList(loginUser, deviceModel);
	    	map.put("status", "success");
	    	map.put("data", pageModel);
    	}catch(Exception e){
    		map.put("status", "failure");
    		LOG.error("DeviceController deviceList error!", e);
    	}
    	return map;
	}	
	
	/**
     * 添加设备
     */
    @SuppressWarnings("unchecked")
    @RequiresPermissions("device:create")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> create(@CurrentUser User loginUser, String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		Device device = new Device();
    		Vehicle vehicle = new Vehicle();
    		
    		//验证sn_number是否已经存在
    		if (StringUtils.isNotEmpty(String.valueOf(jsonMap.get("snNumber")))) {//非空
    			String snNumber = String.valueOf(jsonMap.get("snNumber"));
    			if(!deviceService.snNumberIsValid(snNumber)) {
    				map.put("msg", "该SN设备号已经存在!");
					map.put("status", "failure");
					return map;
    			}
    		}
    		
    		//验证imei是否已经存在
    		String imeiNumber = String.valueOf(jsonMap.get("imeiNumber"));
    		if (!deviceService.imeiNumberIsValid(imeiNumber)) {
    			map.put("msg", "该设备已经存在!");
				map.put("status", "failure");
				return map;
    		} else {
    			device.setImeiNumber(String.valueOf(jsonMap.get("imeiNumber")));
    		}
    		
    		//验证车牌号是否已经绑定
    		if (StringUtils.isBlank(String.valueOf(jsonMap.get("vehicleId")))) {
    			device.setVehicleId(null);
    		} else {
    			Long vehicleId = Long.valueOf(String.valueOf(jsonMap.get("vehicleId")));
				if (!deviceService.vehicleNumberIsValid(vehicleId)) {
					map.put("msg", "该车牌号已经绑定!");
					map.put("status", "failure");
					return map;
				} else {
					device.setVehicleId(TypeUtils.obj2Long(jsonMap.get("vehicleId")));
					//更新车辆表的设备信息
					vehicle.setId(TypeUtils.obj2Long(jsonMap.get("vehicleId")));
					//DBJ的设备号是iccidNumber编号，DH设备号是snNumber,其它设备是imeiNumber
					if (String.valueOf(jsonMap.get("deviceVendor")).toUpperCase().equals("DBJ")) {
						vehicle.setDeviceNumber(TypeUtils.obj2String(jsonMap.get("iccidNumber")));
	    			} else if (String.valueOf(jsonMap.get("deviceVendor")).toUpperCase().equals("DH")){
	    				vehicle.setDeviceNumber(TypeUtils.obj2String(jsonMap.get("snNumber")));
	    			} else {
	    				vehicle.setDeviceNumber(TypeUtils.obj2String(jsonMap.get("imeiNumber")));
	    			}
					vehicle.setSimNumber(TypeUtils.obj2String(jsonMap.get("simNumber")));
				}	
    		}
    		
    		//验证SIM卡是否已经存在
    		if (StringUtils.isNotEmpty(String.valueOf(jsonMap.get("simNumber")))) {//非空
    			String simNumber = String.valueOf(jsonMap.get("simNumber"));
    			if(!deviceService.simNumberIsValid(simNumber)) {
    				map.put("msg", "该SIM卡已经存在!");
					map.put("status", "failure");
					return map;
    			}
    		}
    		
    		//验证iccidNumber是否已经存在
    		if (StringUtils.isNotBlank(String.valueOf(jsonMap.get("iccidNumber")))) {
    			String iccidNumber = String.valueOf(jsonMap.get("iccidNumber"));
    			if (deviceService.iccidNumberIsExist(iccidNumber)) {
    				map.put("msg", "该iccidNumber号已经存在!");
					map.put("status", "failure");
					return map;
    			}
    		}
    		
    		device.setSnNumber(String.valueOf(jsonMap.get("snNumber")));
    		device.setDeviceType(String.valueOf(jsonMap.get("deviceType")));
    		device.setDeviceModel(String.valueOf(jsonMap.get("deviceModel")));
    		device.setDeviceVendorNumber(String.valueOf(jsonMap.get("deviceVendorNumber")));
    		device.setDeviceVendor(String.valueOf(jsonMap.get("deviceVendor")));
    		device.setFirmwareVersion(String.valueOf(jsonMap.get("firmwareVersion")));
    		device.setSoftwareVersion(String.valueOf(jsonMap.get("softwareVersion")));
    		device.setPurchaseTime(Date.valueOf(jsonMap.get("purchaseTime").toString()));
    		device.setMaintainExpireTime(Date.valueOf(jsonMap.get("maintainExpireTime").toString()));
    		device.setIccidNumber(String.valueOf(jsonMap.get("iccidNumber")));
    		device.setSimNumber(String.valueOf(jsonMap.get("simNumber")));
    		device.setDeviceStatus(Long.valueOf(String.valueOf(jsonMap.get("deviceStatus"))));
    		device.setDeviceBatch(String.valueOf(jsonMap.get("deviceBatch")));
    		device = deviceService.createDevice(device);
    		if(device != null){
    			map.put("status", "success");
    			vehicleService.updateVichileDevice(vehicle);
    		}else{
    			map.put("status", "failure");
    			map.put("msg","新增设备失败！");
    		}
    		
    		String imei = device.getImeiNumber();
    		// DBJ设备 使用ICCID
    		if ("DBJ".equalsIgnoreCase(device.getDeviceVendor())) {
    			imei = device.getIccidNumber();
    		}
    		// 执行车辆限速下发
    		excuteSetLimitSpeed(map, String.valueOf(jsonMap.get("vehicleId")), imei,
    				String.valueOf(jsonMap.get("limitSpeed")), String.valueOf(jsonMap.get("latestLimitSpeed")),
    				loginUser.getId(), true);
    		
    	}catch(Exception e){
    		 map.put("status", "failure");
    		 map.put("msg","新增设备失败！");
    		 LOG.error("DeviceController create error!", e);
    	}
        return map;
    }
    
	/**
     * 更新设备
     */
    @SuppressWarnings("unchecked")
    @RequiresPermissions("device:update")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> update(@CurrentUser User loginUser, String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		Device device = new Device();
    		Long deviceId = TypeUtils.obj2Long(jsonMap.get("id"));
    		//车辆设备更新
    		Vehicle vehicle = new Vehicle();
    		Vehicle oldvehicle = new Vehicle();
    		Long newVehicleId = TypeUtils.obj2Long(jsonMap.get("vehicleId"));
    		Long oldVehicleId = deviceService.findDeviceById(deviceId).getVehicleId();
    		Device currentDevice = deviceService.findDeviceById(deviceId);
    		String oldLicenseNo = currentDevice.getLicenseNumber();
    		//验证车牌号是否已经绑定
    		if (StringUtils.isBlank(String.valueOf(jsonMap.get("vehicleId")))) {
    			device.setVehicleId(null);
    		} else {
    			Long vehicleId = Long.valueOf(String.valueOf(jsonMap.get("vehicleId")));
				if (!deviceService.vehicleNumberIsValid(vehicleId) && (!currentDevice.getVehicleId().equals(vehicleId))) {
					map.put("msg", "该车牌号已经绑定!");
					map.put("status", "failure");
					return map;
				} else {
					device.setVehicleId(TypeUtils.obj2Long(jsonMap.get("vehicleId")));
				}	
    		}

    		boolean simChanged = false;
    		//验证SIM卡是否已经绑定
    		if (StringUtils.isBlank(String.valueOf(jsonMap.get("simNumber")))) {
    			device.setSimNumber(null);
    		} else {
    			String simNumber = String.valueOf(jsonMap.get("simNumber"));
    			if (!deviceService.simNumberIsValid(simNumber) && (!currentDevice.getSimNumber().equals(simNumber))) {
    				map.put("msg", "该SIM卡已经绑定!");
					map.put("status", "failure");
					return map;
    			} else {
					if (!currentDevice.getSimNumber().equals(simNumber)) {
						simChanged = true;
					}
				}
    		}

    		device.setId(TypeUtils.obj2Long(jsonMap.get("id")));
    		device.setSnNumber(String.valueOf(jsonMap.get("snNumber")));
    		device.setImeiNumber(String.valueOf(jsonMap.get("imeiNumber")));
    		device.setDeviceType(String.valueOf(jsonMap.get("deviceType")));
    		device.setDeviceModel(String.valueOf(jsonMap.get("deviceModel")));
    		device.setDeviceVendorNumber(String.valueOf(jsonMap.get("deviceVendorNumber")));
    		device.setDeviceVendor(String.valueOf(jsonMap.get("deviceVendor")));
    		device.setFirmwareVersion(String.valueOf(jsonMap.get("firmwareVersion")));
    		device.setSoftwareVersion(String.valueOf(jsonMap.get("softwareVersion")));
    		device.setPurchaseTime(Date.valueOf(jsonMap.get("purchaseTime").toString()));
    		device.setMaintainExpireTime(Date.valueOf(jsonMap.get("maintainExpireTime").toString()));
    		device.setIccidNumber(String.valueOf(jsonMap.get("iccidNumber")));
    		device.setSimNumber(String.valueOf(jsonMap.get("simNumber")));
    		device.setDeviceStatus(Long.valueOf(String.valueOf(jsonMap.get("deviceStatus"))));
    		device.setDeviceBatch(String.valueOf(jsonMap.get("deviceBatch")));
    		device = deviceService.updateDevice(device);
    		//解绑，车辆设备清除
    		if (newVehicleId!=0 && oldVehicleId==null) {
    			vehicle.setId(newVehicleId);
    			//vehicle.setDeviceNumber(String.valueOf(jsonMap.get("imeiNumber")));
    			//DBJ的设备号是iccidNumber编号，DH设备号是snNumber,其它设备是imeiNumber
				if (String.valueOf(jsonMap.get("deviceVendor")).toUpperCase().equals("DBJ")) {
					vehicle.setDeviceNumber(TypeUtils.obj2String(jsonMap.get("iccidNumber")));
    			} else if (String.valueOf(jsonMap.get("deviceVendor")).toUpperCase().equals("DH")){
    				vehicle.setDeviceNumber(TypeUtils.obj2String(jsonMap.get("snNumber")));
    			} else {
    				vehicle.setDeviceNumber(TypeUtils.obj2String(jsonMap.get("imeiNumber")));
    			}
    			vehicle.setSimNumber(String.valueOf(jsonMap.get("simNumber")));
    			vehicleService.updateVichileDevice(vehicle);
    		 }else if (newVehicleId==0 && oldVehicleId!=null) {
    			vehicle.setId(oldVehicleId);
    			vehicle.setDeviceNumber(null);
    			vehicle.setSimNumber(null);
    			vehicleService.updateVichileDevice(vehicle);
    		} else if (newVehicleId!=0 && oldVehicleId!=null) {
    			if (!newVehicleId.equals(oldVehicleId)) {
	    			vehicle.setId(newVehicleId);
	    			//vehicle.setDeviceNumber(String.valueOf(jsonMap.get("imeiNumber")));
	    			if (String.valueOf(jsonMap.get("deviceVendor")).toUpperCase().equals("DBJ")) {
						vehicle.setDeviceNumber(TypeUtils.obj2String(jsonMap.get("iccidNumber")));
	    			} else if (String.valueOf(jsonMap.get("deviceVendor")).toUpperCase().equals("DH")){
	    				vehicle.setDeviceNumber(TypeUtils.obj2String(jsonMap.get("snNumber")));
	    			} else {
	    				vehicle.setDeviceNumber(TypeUtils.obj2String(jsonMap.get("imeiNumber")));
	    			}
	    			vehicle.setSimNumber(String.valueOf(jsonMap.get("simNumber")));
	    			vehicleService.updateVichileDevice(vehicle);
	    			//更新原来的vehicle的设备信息
	    			oldvehicle.setId(oldVehicleId);
	    			oldvehicle.setDeviceNumber(null);
	    			oldvehicle.setSimNumber(null);
	    			vehicleService.updateVichileDevice(oldvehicle);
    			}
    		}

    		if(device != null){
    			map.put("status", "success");

				if (simChanged) {
					String deviceNumber = null;
					if ("DBJ".equalsIgnoreCase(device.getDeviceVendor())) {
						deviceNumber = device.getIccidNumber();
					} else if ("DH".equalsIgnoreCase(device.getDeviceVendor())) {
						deviceNumber = device.getSnNumber();
					} else {
						deviceNumber = device.getImeiNumber();
					}

					// 挂起老的License
					suspendLicense(map, deviceId, oldLicenseNo, loginUser.getId().toString());

					// 绑定新的License
					if (map.get("status").equals("success")) {
						bindLicense(map, device.getId(), device.getDeviceVendor(), deviceNumber, loginUser.getId().toString());
					}
				}

			}else{
    			map.put("status", "failure");
    			map.put("msg", "更新设备失败！");
    		}
    		
    		String imei = device.getImeiNumber();
    		// DBJ设备 使用ICCID
    		if ("DBJ".equalsIgnoreCase(device.getDeviceVendor())) {
    			imei = device.getIccidNumber();
    		}
    		// 执行车辆限速下发
    		excuteSetLimitSpeed(map, String.valueOf(jsonMap.get("vehicleId")), imei,
    				String.valueOf(jsonMap.get("limitSpeed")), String.valueOf(jsonMap.get("latestLimitSpeed")),
    				loginUser.getId(), false);
    		
    	}catch(Exception e){
    		 map.put("status", "failure");
    		 map.put("msg", "更新设备失败！");
    		 LOG.error("DeviceController update error!", e);
    	}
        return map;
    }
    
    //模板下载
    @RequestMapping(value = "/loadTemplate", method = RequestMethod.GET)
	public void down(HttpServletRequest request, HttpServletResponse res){
		ServletContext cxf=request.getSession().getServletContext();
    	String path=cxf.getRealPath("resources"+File.separator+"template"+File.separator+"device"+File.separator+"template.xls");
    	int nameIndex=path.lastIndexOf(File.separator);
    	InputStream in=null;
    	OutputStream out=null;
    	try {
    		String newFileName = URLEncoder.encode(path.substring(nameIndex+1),"UTF-8");
    		res.setContentType("application/x-msdownload");//显示下载面板
    		res.setHeader("Content-disposition", "attachment;fileName="+newFileName);//下载面板中
    		//产生输入流，读文件到内存
    		in= new FileInputStream(path);
    		//产生输出流，用于把文件输出到客户端
    		out=res.getOutputStream();
    		byte[] b=new byte[1024];
    		int len=0;
    		while((len=in.read(b,0,1024))!=-1){
    			out.write(b,0,len);
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
     * excel导入终端设备信息信息
     * @param 
     * @return map
     */
    @RequestMapping(value = "/import", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
	public void importData(@CurrentUser User loginUser, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseTreeModel<Device> resp = new ResponseTreeModel<Device>();
		PrintWriter pWriter = response.getWriter();
		try {
			request.setCharacterEncoding("UTF-8");
			ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
			CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
			MultipartHttpServletRequest multipartRequest = commonsMultipartResolver
					.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
			MultipartFile multiFile = multipartRequest.getFile("file");
			List<Device> modelList = new ArrayList<Device>();
			String fileNa=multiFile.getOriginalFilename().toLowerCase();
			String suffix1=".xls";
	    	String suffix2=".xlsx";
	    	String suffix3=".csv";
	    	if(!fileNa.endsWith(suffix1)&&!fileNa.endsWith(suffix2)&&!fileNa.endsWith(suffix3)){
	    		throw new Exception();
	    	}
			List<Object[]> dataList=CsvUtil.importFile(multiFile);
			for (int i = 0,num=dataList.size(); i < num; i++) {
					Object[] dataObj=dataList.get(i);
					//过滤表头数据
					if(dataObj.length<11)
						continue;

					Device device = new Device();
					String cell0=TypeUtils.obj2String(dataObj[0]);//SN设备号，必填
					String cell1=TypeUtils.obj2String(dataObj[1]);//设备IMEI号，必填
					String cell2=TypeUtils.obj2String(dataObj[2]);//设备类型，必填
					String cell3=TypeUtils.obj2String(dataObj[3]);//设备型号，必填
					String cell4=TypeUtils.obj2String(dataObj[4]);//设备供应商编号，必填
					String cell5=TypeUtils.obj2String(dataObj[5]);//设备厂家，必填
					String cell6=TypeUtils.obj2String(dataObj[6]);//固件版本，必填
					String cell7=TypeUtils.obj2String(dataObj[7]);//软件版本，可选
					String cell8=TypeUtils.obj2String(dataObj[8]);//采购时间，必填
					String cell9=TypeUtils.obj2String(dataObj[9]);//保修到期日，必填
					String cell10=TypeUtils.obj2String(dataObj[10]);//批次，必填
					String cell11=TypeUtils.obj2String(dataObj[11]);//ICCID编号，必填
					String cell12=TypeUtils.obj2String(dataObj[12]);//sim卡号，（视情况而定，如果设备供应商负责安装SIM，则此项为必填）
					// SN设备号，必填
					if (StringUtils.isNotBlank(cell0)) {
						device.setSnNumber(cell0);
					}
					// 设备IMEI号，必填
					if (StringUtils.isNotBlank(cell1)) {
						device.setImeiNumber(cell1);
					}
					// 设备类型，必填
					if (StringUtils.isNotBlank(cell2)) {
						device.setDeviceType(cell2);
					}
					// 设备型号，必填
					if (StringUtils.isNotBlank(cell3)) {
						device.setDeviceModel(cell3);
					}
					//
					if (StringUtils.isNotBlank(cell4)) {
						device.setDeviceVendorNumber(cell4);
					}
 					// 设备厂家，必填
					if (StringUtils.isNotBlank(cell5)) {
						device.setDeviceVendor(cell5);
					}
					// 固件版本
					if (StringUtils.isNotBlank(cell6)) {
						device.setFirmwareVersion(cell6);
					}
					// 软件版本
					if (StringUtils.isNotBlank(cell7)) {
						device.setSoftwareVersion(cell7);
					}
					// 采购时间,必填
					if (StringUtils.isNotBlank(cell8)) {
						if (null==TypeUtils.obj2DateFormat(cell8)) {
							throw new ParseException("",1);
						}else{
							device.setPurchaseTime(new Date(TypeUtils.obj2DateFormat(cell8).getTime()));
						}
					}
					// 保修到期日,必填
					if (StringUtils.isNotBlank(cell9)) {
						if (null==TypeUtils.obj2DateFormat(cell9)) {
							throw new ParseException("",1);
						}else{
							device.setMaintainExpireTime(new Date(TypeUtils.obj2DateFormat(cell9).getTime()));
						}
					}
					// 批次
					if (StringUtils.isNotBlank(cell10)) {
						device.setDeviceBatch(cell10);
					}
					//ICCID编号
					if (StringUtils.isNotBlank(cell11)) {
						device.setIccidNumber(cell11);
					}
					// SIM卡号
					if (StringUtils.isNotBlank(cell12)) {
						device.setSimNumber(cell12);
					}
					//批量导入的设备状态默认为“正常”
					Long status = 1L;
					device.setDeviceStatus(status);
					modelList.add(device);
				}
			int sucNum=0;//成功导入几条数据
			int failNum=0;//失败几条数据
			int total=modelList.size();
			String msg="";
			if (modelList != null && total > 0) {
				int num = 0;
				for(Device device : modelList){
					num++;
					String errMsg=validateData(num,device);
					if (StringUtils.isNoneBlank(errMsg)) {
						msg += errMsg+"<br />";
						failNum++;
					} else {
						deviceService.createDevice(device);
						sucNum++;
					}
				}
			}

			if(StringUtils.isNotBlank(msg)) {
				resp.setMsg("成功导入:"+sucNum+",失败:"+failNum+",详细如下:<br />"+msg);
			} else {
				resp.setMsg("成功导入:"+sucNum+",失败:"+failNum);
			}
			pWriter.write(formatResponse(resp, null, null));
		} catch (UnsupportedEncodingException e) {
			LOG.error("Failed to importData due to UnsupportedEncoding error!", e);
			resp.setMsg("UnsupportedEncoding error");
			pWriter.write(formatResponse(resp, null, null));
		} catch (IOException e) {
			LOG.error("Failed to importData due to IO error!", e);
			resp.setMsg("IO error");
			pWriter.write(formatResponse(resp, null, null));
		} catch (ParseException e) {
			LOG.error("Failed to importData due to unexpected Date format!", e);
			resp.setMsg("日期格式错误");
			pWriter.write(formatResponse(resp, null, null));
		} catch(Exception e){
			LOG.error("Failed to importData due to unexpected error!", e);
			resp.setMsg("文件格式错误");
			pWriter.write(formatResponse(resp, null, null));
		} finally {
			pWriter.close();
		}
	}
    
    private String validateData(int num,Device model){
    	String msg="";
    	//判断是否有为空的数据
    	if(StringUtils.isBlank(model.getSnNumber())){
    		msg = "第"+num+"条数据:SN设备号为空,导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(model.getImeiNumber())){
    		msg = "第"+num+"条数据:设备IMEI号为空,导入失败!";
    		return msg;
    	}else if (!(model.getImeiNumber().matches("^([0-9]){15}$"))) {
    		msg = "第"+num+"条数据:IMEI设备编号输入错误,导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(model.getDeviceType())){
    		msg = "第"+num+"条数据:设备类型号为空,导入失败!";
    		return msg;
    	} else {
    		if (!model.getDeviceType().equals("OBD") && !model.getDeviceType().equals("GPS")) {
    			msg = "第"+num+"条数据:设备类型输入错误,导入失败!";
        		return msg;
    		}
    	}
    	if (StringUtils.isBlank(model.getDeviceVendorNumber())) {
    		msg = "第"+num+"条数据:设备供应商编号为空,导入失败!";
    		return msg;
    	} else if (!(model.getDeviceVendorNumber().matches("^[A-Za-z0-9]+$"))) {
    		msg = "第"+num+"条数据:设备供应商编号输入错误，导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(model.getDeviceModel())){
    		msg = "第"+num+"条数据:设备型号为空,导入失败!";
    		return msg;
    	}
    	
    	String deviceVendor =  model.getDeviceVendor().toUpperCase();
    	if(StringUtils.isBlank(model.getDeviceVendor())){
    		msg = "第"+num+"条数据:设备厂家为空,导入失败!";
    		return msg;
    	} else if (!deviceVendor.equals("DBJ")&&!deviceVendor.equals("GOSAFE")&&!deviceVendor.equals("LS_GENIUS")&&!deviceVendor.equals("DH")) {
    		msg = "第"+num+"条数据:设备厂家输入错误,导入失败!";
    		return msg;
    	}
    	
    	if(StringUtils.isBlank(model.getFirmwareVersion())){
    		msg = "第"+num+"条数据:设备固件版本为空,导入失败!";
    		return msg;
    	}
    	
    	Calendar calendar = Calendar.getInstance();
    	Date purchaseTime =  model.getPurchaseTime();
	    java.util.Date nowDate = calendar.getTime();
    	if(null == model.getPurchaseTime()){
    		msg = "第"+num+"条数据:采购时间为空,导入失败!";
    		return msg;
    	} else if (purchaseTime.after(nowDate)) { //采购日期不能大于当前日期
    		msg = "第"+num+"条数据:采购时间大于当前时间,导入失败!";
    		return msg;
    	}
    	if(null == model.getMaintainExpireTime()){
    		msg = "第"+num+"条数据:保修到期日为空,导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(model.getDeviceStatus().toString())){
    		msg = "第"+num+"条数据:设备状态为空,导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(model.getDeviceBatch())){
    		msg = "第"+num+"条数据:设备批次为空,导入失败!";
    		return msg;
    	}
    	if(StringUtils.isBlank(model.getIccidNumber())){
    		msg = "第"+num+"条数据:ICCID设备编号为空,导入失败!";
    		return msg;
    	} else if (!(model.getIccidNumber().matches("^([A-Za-z0-9]){20}$"))) {
    		msg = "第"+num+"条数据:ICCID设备编号输入错误,导入失败!";
    		return msg;
    	}
    	//如果输入车牌号，判断该车牌号在车辆表中是否已经存在
//    	if(StringUtils.isNotBlank(model.getVehicleNumber())&&vehicleService.vehicleNumberIsValid(model.getVehicleNumber())){
//			msg = "第"+num+"条数据:绑定的车牌号不存在,导入失败!";
//    		return msg;
//    	}
    	//判断设备IMEI号是否已经存在
    	if (!deviceService.imeiNumberIsValid(model.getImeiNumber())) {
			msg = "第"+num+"条数据:IMEI已经存在,导入失败!";
			return msg;
		}
    	//判断snNumber号是否存在
    	if (!deviceService.snNumberIsValid(model.getSnNumber())) {
			msg = "第"+num+"条数据:snNumber号已经存在,导入失败!";
			return msg;
		}
    	//如果sim卡号不为空，判断sim卡号是否已经存在
    	if (StringUtils.isNotBlank(model.getSimNumber())) {
    		if (!deviceService.simNumberIsValid(model.getImeiNumber())) {
    			msg = "第"+num+"条数据:SIM卡号已经存在,导入失败!";
    			return msg;
    		}
    	}
    	//判断iccidNumber编号是否存在
    	if (deviceService.iccidNumberIsExist(model.getIccidNumber())) {
    		msg = "第"+num+"条数据:iccidNumber号已经存在,导入失败!";
			return msg;
    	}
//		if (StringUtils.isNotBlank(model.getVehicleNumber())) {
//			VehicleModel vehicleModel = vehicleService.findByVehicleNumber(model.getVehicleNumber());
//			if (!deviceService.vehicleNumberIsValid(vehicleModel.getId())) {
//				msg = "第"+num+"条数据:绑定车牌号已经绑定,导入失败!";
//				return msg;
//			}
//		}
    	return msg;
    }
    
    /**
     * [系统管理员]License绑定
     * @param 
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/licenseBind", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> licenseBind(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		String userId = String.valueOf(loginUser.getId());
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		String imeiNumber = String.valueOf(jsonMap.get("imeiNumber"));
    		String iccidNumber = String.valueOf(jsonMap.get("iccidNumber"));
    		String snNumber = String.valueOf(jsonMap.get("snNumber"));
    		String devcieVendor = String.valueOf(jsonMap.get("deviceVendor"));
    		String deviceNumber = "";
    		if ("DBJ".equalsIgnoreCase(devcieVendor)) {
    			deviceNumber = iccidNumber;
    		} else if ("DH".equalsIgnoreCase(devcieVendor)) {
    			deviceNumber = snNumber;
    		} else {
    			deviceNumber = imeiNumber;
    		}

			bindLicense(map, TypeUtils.obj2Long(jsonMap.get("deviceId")), devcieVendor, deviceNumber, userId);
		}catch(Exception e){
    		map.put("status", "failure");
    		map.put("msg", "License绑定失败！");
    		LOG.error("DeviceController licenseBind error!,caused by:", e);
    	}
    	return map;
	}

	private void bindLicense(Map<String,Object> map, Long deviceId, String devcieVendor, String deviceNumber, String userId) throws IOException {
		//查询一条可用的license
		String license = shouqiService.queryLicense("NOT_IN_USE", "1");
		String licenseNo = "";
		Map<String, Object> licenseMap = JsonUtils.json2Object(license, Map.class);
		String status = String.valueOf(licenseMap.get("status"));
		if (status.equals("000")) {
			JsonNode jsonNode=MAPPER.readTree(license);
			ArrayNode arrayNodes = (ArrayNode)jsonNode.get("result");
			JsonNode node=arrayNodes.get(0);
			licenseNo=node.get("license_no").asText();
			//绑定license
			String bindRestult = shouqiService.bindLicense(licenseNo, deviceNumber, devcieVendor, userId);
			Map<String, Object> licenseBind = JsonUtils.json2Object(bindRestult, Map.class);
			if (licenseBind.get("status").equals("000")) {
				Device device = new Device();
				device.setId(deviceId);
				device.setStartTime(null);
				device.setEndTime(null);
				device.setLicenseNumber(licenseNo);
				device.setLicenseStatus(null);
				device = deviceService.updateDeviceLicesnse(device);
				map.put("status", "success");
				map.put("msg", "License绑定成功！");
			} else {
				map.put("status", "failure");
				//map.put("msg", licenseBind.get("message"));
				map.put("msg", "license绑定失败!"+"  错误状态：" + licenseBind.get("status"));
				LOG.error("DeviceController licenseBind error!,error status:",licenseBind.get("status"));
			}
		} else {
			map.put("status", "failure");
			map.put("msg", String.valueOf(licenseMap.get("message")));
		}
	}

	/**
     * [系统管理员]License激活
     * @param 
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/licenseActive", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> licenseActive(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		String licenseNo = String.valueOf(jsonMap.get("licenseNo"));
    		String startDate = String.valueOf(jsonMap.get("startDate"))+" 00:00:00";
    		String endDate = String.valueOf(jsonMap.get("endDate"))+" 23:59:59";
    		String userId = String.valueOf(jsonMap.get("userId"));
    		String activeRestult = shouqiService.activeLicense(licenseNo, startDate.toString(), endDate.toString(), userId);
    		Map<String, Object> licenseRet = JsonUtils.json2Object(activeRestult, Map.class);
    		String status = String.valueOf(licenseRet.get("status"));
    		if (status.equals("000")) {
    			//String licenseStatus = "IN_USE"; 		
        		Device device = new Device();
        		device.setLicenseNumber(licenseNo);
        		device.setId(TypeUtils.obj2Long(jsonMap.get("deviceId")));
        		device.setStartTime(Date.valueOf(jsonMap.get("startDate").toString()));
        		device.setEndTime(Date.valueOf(jsonMap.get("endDate").toString()));
        		device.setLicenseStatus(null);
        		device = deviceService.updateDeviceLicesnse(device);
        		map.put("status", "success");
        		map.put("msg", "License激活成功！");
    		} else {
    			map.put("status", "failure");
    			map.put("msg", "license激活失败!"+"  错误状态：" + licenseRet.get("status"));
    			LOG.error("DeviceController licenseActive error!,error status:",licenseRet.get("status"));
    		}
    	}catch(Exception e){
    		map.put("status", "failure");
    		map.put("msg", "License激活失败！");
    		LOG.error("DeviceController licenseActive error!,caused by:", e);
    	}
    	return map;
	}
    
    /**
     * [系统管理员]License挂起
     * @param 
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/licenseSuspend", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> licenseSuspend(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		String licenseNo = String.valueOf(jsonMap.get("licenseNo"));
    		String userId = String.valueOf(jsonMap.get("userId"));
    		suspendLicense(map, TypeUtils.obj2Long(jsonMap.get("deviceId")), licenseNo, userId);
    	}catch(Exception e){
    		map.put("status", "failure");
    		map.put("msg", "License挂起失败！");
    		LOG.error("DeviceController licenseSuspend error!,caused by:", e);
    	}
    	return map;
	}

	public void suspendLicense(Map<String, Object> map, Long deviceId, String licenseNo, String userId) {
		String suspendRestult = shouqiService.suspendLicense(licenseNo, userId);
		Map<String, Object> licenseRet = JsonUtils.json2Object(suspendRestult, Map.class);
		String status = String.valueOf(licenseRet.get("status"));
		if (status.equals("000")) {
			//String licenseStatus = "SUSPEND";
			Device device = new Device();
			device.setId(deviceId);
			device.setStartTime(null);
			device.setEndTime(null);
			device.setLicenseNumber(licenseNo);
			device.setLicenseStatus(null);
			device = deviceService.updateDeviceLicesnse(device);
			map.put("status", "success");
			map.put("msg", "License挂起成功！");
		} else {
			map.put("status", "failure");
			map.put("msg", "license挂起失败!"+"  错误状态：" + licenseRet.get("status"));
			LOG.error("DeviceController licenseSuspend error!,error status:",licenseRet.get("status"));
		}
	}
    
    /**
     * [系统管理员]License重新激活
     * @param 
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/licenseReactive", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> licenseReactive(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		String licenseNo = String.valueOf(jsonMap.get("licenseNo"));
    		String startDate = String.valueOf(jsonMap.get("startDate"))+" 00:00:00";
    		String endDate = String.valueOf(jsonMap.get("endDate"))+" 23:59:59";
    		String userId = String.valueOf(jsonMap.get("userId"));
    		String reactiveRestult = shouqiService.reactiveLicense(licenseNo, startDate.toString(), endDate.toString(), userId);
    		Map<String, Object> licenseRet = JsonUtils.json2Object(reactiveRestult, Map.class);
    		String status = String.valueOf(licenseRet.get("status"));
    		if (status.equals("000")) {
    			//String licenseStatus = "IN_USE"; 		
        		Device device = new Device();
        		device.setLicenseNumber(licenseNo);
        		device.setId(TypeUtils.obj2Long(jsonMap.get("deviceId")));
        		device.setStartTime(Date.valueOf(jsonMap.get("startDate").toString()));
        		device.setEndTime(Date.valueOf(jsonMap.get("endDate").toString()));
        		device.setLicenseStatus(null);
        		device = deviceService.updateDeviceLicesnse(device);
        		map.put("status", "success");
        		map.put("msg", "License重新激活成功！");
    		} else {
    			map.put("status", "failure");
    			map.put("msg", "license重新激活失败!"+"  错误状态：" + licenseRet.get("status"));
    			LOG.error("DeviceController licenseReactive error!,error status:",licenseRet.get("status"));
    		}
    	}catch(Exception e){
    		map.put("status", "failure");
    		map.put("msg", "License重新激活失败！");
    		LOG.error("DeviceController licenseReactive error!,caused by:", e);
    	}
    	return map;
	}
    
    /**
     * [系统管理员]License终止
     * @param 
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/licenseTerminated", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> licenseTerminated(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		String licenseNo = String.valueOf(jsonMap.get("licenseNo"));
    		String userId = String.valueOf(jsonMap.get("userId"));
    		String terminteRestult = shouqiService.terminatedLicense(licenseNo, userId);
    		Map<String, Object> licenseRet = JsonUtils.json2Object(terminteRestult, Map.class);
    		String status = String.valueOf(licenseRet.get("status"));
    		if (status.equals("000")) {
    			//String licenseStatus = "TERMINATED"; 		
        		Device device = new Device();
        		device.setId(TypeUtils.obj2Long(jsonMap.get("deviceId")));
        		device.setStartTime(null);
        		device.setEndTime(null);
        		device.setLicenseNumber(null);
        		device.setLicenseStatus(null);
        		device = deviceService.updateDeviceLicesnse(device);
        		map.put("status", "success");
        		map.put("msg", "License终止成功！");
    		} else {
    			map.put("status", "failure");
    			map.put("msg", "license终止失败!"+"  错误状态：" + licenseRet.get("status"));
    			LOG.error("DeviceController licenseTerminated error!,error status:",licenseRet.get("status"));
    		}
    	}catch(Exception e){
    		map.put("status", "failure");
    		map.put("msg", "License终止失败！");
    		LOG.error("DeviceController licenseTerminated error!,caused by:", e);
    	}
    	return map;
	}
    
    /**
     * [系统管理员]License解绑
     * @param 
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/licenseUnbind", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> licenseUnbind(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		String licenseNo = String.valueOf(jsonMap.get("licenseNo"));
    		String userId = String.valueOf(jsonMap.get("userId"));
    		String terminteRestult = shouqiService.unbindLicense(licenseNo, userId);
    		Map<String, Object> licenseRet = JsonUtils.json2Object(terminteRestult, Map.class);
    		String status = String.valueOf(licenseRet.get("status"));
    		if (status.equals("000")) {
        		Device device = new Device();
        		device.setId(TypeUtils.obj2Long(jsonMap.get("deviceId")));
        		device.setStartTime(null);
        		device.setEndTime(null);
        		device.setLicenseNumber(null);
        		device.setLicenseStatus(null);
        		device = deviceService.updateDeviceLicesnse(device);
        		map.put("status", "success");
        		map.put("msg", "License解绑成功！");
    		} else {
    			map.put("status", "failure");
    			map.put("msg", "license解绑失败!"+"  错误状态：" + licenseRet.get("status"));
    			LOG.error("DeviceController licenseUnbind error!,error status:",licenseRet.get("status"));
    		}
    	}catch(Exception e){
    		map.put("status", "failure");
    		map.put("msg", "License解绑失败！");
    		LOG.error("DeviceController licenseUnbind error!,caused by:", e);
    	}
    	return map;
	}
    
    /**
     * 执行车辆限速下发
     * 
     * @param map	状态map
     * @param vehicleId	车辆ID
     * @param imei	IMEI	
     * @param limitSpeed	车辆限速
     * @param latestLimitSpeed	最新下发限速
     * @param userId	操作用户ID
     * @return
     */
    private Map<String,Object> excuteSetLimitSpeed(Map<String,Object> map, String vehicleId, 
    		String imei, String limitSpeed, String latestLimitSpeed, Long userId, boolean isCreate) {
    	// 绑定设备的车辆存在,且车辆限速与最新下发设备速度不一致时,限制速度下发
    	if(StringUtils.isNotBlank(vehicleId)
    			&& StringUtils.isNotBlank(limitSpeed) && !limitSpeed.equals(latestLimitSpeed)) {
    		// 执行命令
    		String result = deviceCommandService.setSpeedLimitCommand(imei, Integer.valueOf(limitSpeed), 0);
    		
    		Map<String, Object> jsonRst = JsonUtils.json2Object(result, Map.class);
    		String statusCode = "";
    		if (jsonRst.get("status") != null) {
    			statusCode = (String) jsonRst.get("status");
    		}
    		
    		// 设备命令记录
    		DevcieCommandConfigRecord devcieCommandConfigRecord = new DevcieCommandConfigRecord();
    		devcieCommandConfigRecord.setDeviceNumber(imei);
    		devcieCommandConfigRecord.setCommandType("SET_LIMIT_SPEED");
    		devcieCommandConfigRecord.setCommandValue(limitSpeed);
    		java.util.Date utilDate = new java.util.Date();
    		devcieCommandConfigRecord.setCommandSendTime(utilDate);
    		devcieCommandConfigRecord.setUserId(userId);
    		
    		if ("000".equals(statusCode)) {
    			devcieCommandConfigRecord.setCommandExcuteStatus("excuting");
    			devcieCommandConfigRecord.setCommandSendStatus(statusCode);
    			map.put("status", "success");
    		} else {
    			devcieCommandConfigRecord.setCommandExcuteStatus("failure");
    			devcieCommandConfigRecord.setCommandSendStatus(statusCode);
    			map.put("status", "sendCmdFail");
    			if (true == isCreate) {
    				map.put("msg", "添加成功，下发限速失败！");
    			} else {
    				map.put("msg", "修改成功，下发限速失败！");
    			}
    		}
    		// 插入 设备命令记录表
    		deviceService.addDeviceCommandConfigRecord(devcieCommandConfigRecord);
    	}
    	return map;
    }
    
}
