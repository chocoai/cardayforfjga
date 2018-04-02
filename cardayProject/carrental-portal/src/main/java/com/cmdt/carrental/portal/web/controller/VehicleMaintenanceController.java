package com.cmdt.carrental.portal.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
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

import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.entity.VehicleMaintenance;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.ResponseTreeModel;
import com.cmdt.carrental.common.model.VehicleDataCountModel;
import com.cmdt.carrental.common.model.VehicleMaintainInfoModel;
import com.cmdt.carrental.common.service.VehicleMaintenanceService;
import com.cmdt.carrental.common.util.CsvUtil;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carrental.portal.web.bind.annotation.CurrentUser;

@Controller
@RequestMapping("/maintenance")
public class VehicleMaintenanceController extends BaseController{
	private static final Logger LOG = LoggerFactory.getLogger(VehicleMaintenanceController.class);
	@Autowired
	private VehicleMaintenanceService vmService;
	
    /**
     * [企业管理员]查询所在企业的车辆保养列表
     * @return map
     */
    @RequiresPermissions(value={"maintenance:list"})
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> list(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		VehicleMaintainInfoModel model = JsonUtils.json2Object(json, VehicleMaintainInfoModel.class);
			PagModel pagModel = vmService.listPage(model);;
	    	map.put("status", "success");
	    	map.put("data", pagModel);
    	}catch(Exception e){
    		LOG.error("VehicleMaintenanceController.list",e);
    		map.put("status", "failure");
    	}
    	return map;
	}
    
    /**
     * [企业管理员/部门管理员]（首页）显示次月内到期的保养、保险、年检车辆
     */
    @RequestMapping(value = "/queryVehicleDataCount", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> queryVehicleDataCount(@CurrentUser User loginUser) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	boolean isEnt=false;
    	//企业管理员
    	if(loginUser.isEntAdmin()){
    		isEnt=true;
    	}
    	//部门管理员
    	if(loginUser.isDeptAdmin()){
    		isEnt=false;
    	}
    	VehicleDataCountModel vModel = vmService.queryVehicleDataCountByHomePage(loginUser.getOrganizationId(), isEnt);
    	map.put("status", "success");
    	map.put("data", vModel);
    	return map;
    }
    
    /**
     * [企业管理员]新增保养记录
     * @return map
     */
	@SuppressWarnings("unchecked")
	@RequiresPermissions(value={"maintenance:create"})
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> create(@CurrentUser User loginUser,String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		String msg="";
    		VehicleMaintenance vm=new VehicleMaintenance();
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		Long vehicleId=TypeUtils.obj2Long(jsonMap.get("vehicleId"));
    		vm.setVehicleId(vehicleId);
    		vm.setDeviceNumber(TypeUtils.obj2String(jsonMap.get("deviceNumber")));
    		String starttime=TypeUtils.obj2String(jsonMap.get("curTime"));//前端传过来的是带时分秒的格式
    		Long headerMaintenanceMileage=TypeUtils.obj2Long(jsonMap.get("headerMaintenanceMileage"));
    		vm.setHeaderMaintenanceMileage(headerMaintenanceMileage);
    		vm.setMaintenanceMileage(TypeUtils.obj2Long(jsonMap.get("maintenanceMileage")));
    		vm.setCurTime(DateUtils.string2Date(starttime,DateUtils.FORMAT_YYYY_MM_DD));
    		vm.setCurTimeF(starttime);
    		vm.setMaintenanceTime(TypeUtils.obj2Integer(jsonMap.get("maintenanceTime")));
    		vm=vmService.calcMaintenance(vm);
    		Long entId=loginUser.getOrganizationId();
    		vm=saveOrUpdate(null,entId,vm);
    		if(StringUtils.isNotBlank(vm.getMessage())){
    			msg=vm.getMessage();
    			map.put("data", msg);
    			map.put("status", "failure");
    		}else{
    			map.put("status", "success");
    		}
    	}catch(Exception e){
    		LOG.error("VehicleMaintenanceController.create error",e);
    		map.put("status", "failure");
    	}
        return map;
    }
	
	public VehicleMaintenance saveOrUpdate(Integer num, Long entId,VehicleMaintenance vm){
		//如果导入时间大于等于本次保养时间则进行操作,等于的时候只是替换数据
		if(vm.getGap()>=0){
			//检查当前车辆保养信息是否已经存在
			String condition=" and (v.ent_id=? or v.currentuse_org_id in(select id from sys_organization where parent_id=?)) and t.vehicle_id=?";
			List<Object> params = new ArrayList<Object>();
			params.add(entId);
			params.add(entId);
			params.add(vm.getVehicleId());
			List<VehicleMaintenance> list=vmService.listBySql(condition, params);
			vm.setMaintenanceDueTime(TimeUtils.countDateByMonth(vm.getCurTime(), vm.getMaintenanceTime()));
			if(!list.isEmpty()){
				VehicleMaintenance vmold=list.get(0);
				// check
				vm.setMessage(vmService.validateData(num, vmold, vm));
				if(StringUtils.isNotBlank(vm.getMessage())) {
					return vm;
				}
				vmold.setHeaderLatestMileage(vm.getHeaderLatestMileage());
				vmold.setHeaderLastMileage(vmold.getHeaderMaintenanceMileage());
				vmold.setHeaderMaintenanceMileage(vm.getHeaderMaintenanceMileage());
				vmold.setMaintenanceMileage(vm.getMaintenanceMileage());
				vmold.setTravelMileage(vm.getTravelMileage());
				vmold.setCurTime(vm.getCurTime());
				vmold.setMaintenanceTime(vm.getMaintenanceTime());
				vmold.setMaintenanceDueTime(vm.getMaintenanceDueTime());
				vmold.setAlertMileageWarn(0);
				vmold.setCurTimeWarn(0);
				vmold.setUpdateTime(vm.getUpdateTime());
				VehicleMaintenance vMaintenance = list.get(0);
				int thresholdMonth = vMaintenance.getThresholdMonth();
				thresholdMonth = Integer.parseInt("-" + Math.abs(thresholdMonth));
				Date maintenanceThresholdTime = TimeUtils.countDateByMonth(vm.getMaintenanceDueTime(),
						thresholdMonth);
				vmold.setMaintenanceThresholdTime(maintenanceThresholdTime);
				vm = vmService.update(vmold);
			}else{
				// check
				vm.setMessage(vmService.validateData(num, null, vm));
				if(StringUtils.isNotBlank(vm.getMessage())) {
					return vm;
				}
				
				vm.setHeaderLastMileage(0l);
				vm.setThresholdMonth(1);
				Date maintenanceThresholdTime = TimeUtils.countDateByMonth(vm.getMaintenanceDueTime(), -1);
				vm.setMaintenanceThresholdTime(maintenanceThresholdTime);
				vm=vmService.create(vm);
			}
		}
		return vm;
	}
	
    /**
     * [企业管理员]重置保养记录
     * @return map
     */
	@SuppressWarnings("unchecked")
	@RequiresPermissions(value={"maintenance:update"})
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> reset(String json) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		String msg="";
    		Map<String, Object> jsonMap = JsonUtils.json2Object(json, Map.class);
    		Long id=TypeUtils.obj2Long(jsonMap.get("id"));
    		String condition=" and t.id=?";
			List<Object> params = new ArrayList<Object>();
			params.add(id);
			List<VehicleMaintenance> list=vmService.listBySql(condition, params);
			if(!list.isEmpty()){
				VehicleMaintenance vmold=list.get(0);
	    		String starttime=TypeUtils.obj2String(jsonMap.get("curTime"));//前端传过来的是带时分秒的格式
	    		int maintenanceTime = TypeUtils.obj2Integer(jsonMap.get("maintenanceTime"));
	    		Long mantainMileage = TypeUtils.obj2Long(jsonMap.get("mantainMileage"));
	    		//如果本次保养时间大于或等于已存在记录的保养时间才更新
				if (starttime.compareTo(vmold.getCurTimeF()) >= 0) {
					// 本次保养表头里程数必须大于上次保养表头里程数
					Long headerMaintenanceMileage = TypeUtils.obj2Long(jsonMap.get("headerMaintenanceMileage"));
					if (headerMaintenanceMileage > vmold.getHeaderMaintenanceMileage()) {
						vmold.setHeaderLastMileage(vmold.getHeaderMaintenanceMileage());
						vmold.setHeaderMaintenanceMileage(headerMaintenanceMileage);
						vmold.setCurTime(DateUtils.string2Date(starttime, DateUtils.FORMAT_YYYY_MM_DD));
						vmold.setMaintenanceDueTime(TimeUtils.countDateByMonth(vmold.getCurTime(), maintenanceTime));
						vmold.setAlertMileageWarn(0);
						vmold.setCurTimeWarn(0);
						vmold.setCurTimeF(starttime);
						vmold.setMaintenanceTime(maintenanceTime);
						vmold = vmService.calcMaintenance(vmold);
						VehicleMaintenance vMaintenance = list.get(0);
						int thresholdMonth = vMaintenance.getThresholdMonth();
						thresholdMonth = Integer.parseInt("-" + Math.abs(thresholdMonth));
						Date maintenanceThresholdTime = TimeUtils.countDateByMonth(vmold.getMaintenanceDueTime(),
								thresholdMonth);
						vmold.setMaintenanceThresholdTime(maintenanceThresholdTime);
						vmold.setMaintenanceMileage(mantainMileage);
						vmService.update(vmold);
					} else {
						msg = "本次保养表头里程数小于上次保养表头里程数,未更新!";
						map.put("data", msg);
						map.put("status", "failure");
						return map;
					}
				} else {
					msg = "本次保养时间小于当前记录的保养时间,未更新!";
					map.put("data", msg);
					map.put("status", "failure");
					return map;
				}
			}
			map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("VehicleMaintenanceController.reset error",e);
    		map.put("status", "failure");
    	}
    	return map;
    }
	
	@RequiresPermissions(value={"maintenance:update"})
	@RequestMapping(value = "/setThreshold", method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> setThreshold(String json) {
		LOG.info("json:" + json);
		Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
		int status = vmService.setThreshold(json);
		if(status == 1) {
			map.put("status", "success");
		} else {
			map.put("status", "failure");
		}
		return map;
	}
	
	//模板下载
    @RequestMapping(value = "/loadTemplate", method = RequestMethod.GET)
	public void down(HttpServletRequest request, HttpServletResponse res){
		ServletContext cxf=request.getSession().getServletContext();
    	String path=cxf.getRealPath("resources"+File.separator+"template"+File.separator+"maintenance"+File.separator+"template.xls");
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
     * [企业管理员]excel导入保养记录
     * @param 
     * @return map
     */
	@RequiresPermissions(value={"maintenance:import"})
    @RequestMapping(value = "/import", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
	public void importData(@CurrentUser User loginUser, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResponseTreeModel<VehicleMaintenance> resp = new ResponseTreeModel<VehicleMaintenance>();
		PrintWriter pWriter = response.getWriter();
		try {
			request.setCharacterEncoding("UTF-8");
			ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
			CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
			MultipartHttpServletRequest multipartRequest = commonsMultipartResolver
					.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
			MultipartFile multiFile = multipartRequest.getFile("file");
			List<VehicleMaintenance> modelList = new ArrayList<VehicleMaintenance>();
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
					if(dataObj.length<8)
						continue;

					VehicleMaintenance model = new VehicleMaintenance();
					String cell0=TypeUtils.obj2String(dataObj[0]);//车牌号
					String cell1=TypeUtils.obj2String(dataObj[1]);//车架号
					String cell2=TypeUtils.obj2String(dataObj[2]);//设备号
					String cell3=TypeUtils.obj2String(dataObj[3]);//SIM卡号
					String cell4=TypeUtils.obj2String(dataObj[4]);//保养里程数
					String cell5=TypeUtils.obj2String(dataObj[5]);//本次保养表头里程数
					String cell6=TypeUtils.obj2String(dataObj[6]);//本次保养时间
					String cell7=TypeUtils.obj2String(dataObj[7]);//维保时间
					
					// 车牌号
					if (StringUtils.isNotBlank(cell0)) {
						model.setVehicleNumber(cell0);
					}
					// 车架号
					if (StringUtils.isNotBlank(cell1)) {
						model.setVehicleIdentification(cell1);
					}
					// 设备号
					if (StringUtils.isNotBlank(cell2)) {
						model.setDeviceNumber(cell2);
					}
					// SIM卡号
					if (StringUtils.isNotBlank(cell3)) {
						model.setSimNumber(cell3);
					}
					// 保养里程数(必填)
					if (StringUtils.isNotBlank(cell4)) {
						model.setMaintenanceMileage((TypeUtils.obj2Long(cell4)));
					}
					// 本次保养表头里程数(必填)
					if (StringUtils.isNotBlank(cell5)) {
						model.setHeaderMaintenanceMileage(TypeUtils.obj2Long(cell5));
					}
					// 本次保养时间(必填)
					if (StringUtils.isNotBlank(cell6)) {
						String curTimeF=TypeUtils.obj2String(cell6);
						model.setCurTime(DateUtils.string2Date(curTimeF,DateUtils.FORMAT_YYYY_MM_DD));
						model.setCurTimeF(curTimeF);
					}
					if (StringUtils.isNotBlank(cell7)) {
						model.setMaintenanceTime(Integer.parseInt(cell7));
					}
					modelList.add(model);
				}
			int sucNum=0;//成功导入几条数据
			int failNum=0;//失败几条数据
			int total=modelList.size();
			StringBuffer msg= new StringBuffer();
			if (modelList != null && total > 0) {
				int num = 0;
				for(VehicleMaintenance model:modelList){
					num++;
					String vehicleNumber=model.getVehicleNumber();
					String condition=" and v.vehicle_number='"+vehicleNumber+"'";
					Vehicle vehicle=vmService.getVehicleBySql(condition);
					//如果能找到车辆数据才做处理
					if(null!=vehicle){
						model.setVehicleId(vehicle.getId());
						//当前最新表头里程数，需要trip计算
						model=vmService.calcMaintenance(model);
						Long entId=loginUser.getOrganizationId();
						
						// 车辆信息Check
						model.setMessage(vmService.validateVehicleInfoAndTime(num, vehicle, model));
						// 其他信息check,以及保存,更新操作
						if(StringUtils.isBlank(model.getMessage())){
							model=saveOrUpdate(num,entId,model);//此处返回的内容里可能有错误信息，暂不处理
						}
						
						if(StringUtils.isBlank(model.getMessage())){
							sucNum++;
						} else {
							msg.append(model.getMessage() + "<br />");
						}
					} else {
						msg.append("第"+num+"条数据:车辆不存在,未更新!<br />");
					}
				}
			}
			failNum=total-sucNum;
			if (StringUtils.isNotBlank(msg.toString())) {
				resp.setMsg("成功导入:"+sucNum+",失败:"+failNum+",详细如下:<br />"+msg.toString());
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
		} catch (Exception e) {
			LOG.error("Failed to importData due to unexpected error!", e);
			resp.setMsg("文件格式错误");
			pWriter.write(formatResponse(resp, null, null));
		} finally {
			pWriter.close();
		}
	}
	
	/**
     * [企业管理员]excel导出保养记录
     * @param 
     * @return map
     */
	@RequiresPermissions(value={"maintenance:export"})
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ResponseBody
    public void exportData(@CurrentUser User loginUser,String json, HttpServletResponse response) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("data", "");
    	try{
    		//企业管理员
    		if(loginUser.isEntAdmin()){
    			Long entId=loginUser.getOrganizationId();
    			List<VehicleMaintenance> list=vmService.list(entId,json);
//    			Map<String, Object> model=new HashMap<String, Object>();
//    			model.put("filename", "maintenance_"+DateUtils.getNowDate()+".xls");
//    			model.put("sheet", DateUtils.getNowDate());
//    			model.put("header", "车牌号,车辆品牌,车辆型号,所属部门,当前表头里程数,上次保养表头里程数,保养里程数,已行驶里程数,剩余里程,本次保养时间");
//    			model.put("data", list);
//    			CsvUtil.exportExcel(model, response);
    			exprotExcel("maintenance_"+DateUtils.getNowDate()+".xls", list, response);
    		}
    		map.put("status", "success");
    	}catch(Exception e){
    		LOG.error("VehicleMaintenanceController.exportData error",e);
    		map.put("status", "failure");
    	}
    }
	
	
	private void exprotExcel(String fileName, List<VehicleMaintenance> list, HttpServletResponse response) throws Exception {
		OutputStream fOut = null;
		HSSFWorkbook workbook =null;
		try{
			String excelName = fileName;
			//设置response方式,使执行此controller时候自动出现下载页面,而非直接使用excel打开  
			response.setContentType("application/vnd.ms-excel");  
			response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(excelName, "UTF-8"));    
			
			workbook = new HSSFWorkbook(); //NOSONAR workbook has been close in finally
			//产生Excel表头  
			HSSFSheet sheet = workbook.createSheet(DateUtils.getNowDate());  
			HSSFRow header = sheet.createRow(0); // 第0行  
			//产生标题列  
			String headers = "车牌号,车辆品牌,车辆型号,所属部门,当前表头里程数,上次保养表头里程数,保养里程数,已行驶里程数,剩余里程,上次保养时间,下次保养时间";
			String[] hdCols = headers.split(",");
			for(int k=0;k<hdCols.length;k++){
				header.createCell(k).setCellValue(hdCols[k]);  
			}
			HSSFCellStyle style = workbook.createCellStyle(); 
	        //填充数据  
	        int rowNum = 1;
	        if(list != null){
	        	for(VehicleMaintenance vehicleMaintenance : list) {
	        		HSSFRow row = sheet.createRow(rowNum++);
	        		HSSFCell cell = row.createCell(0);
	        		cell.setCellValue(vehicleMaintenance.getVehicleNumber());
	        		cell = row.createCell(1);
	        		cell.setCellValue(vehicleMaintenance.getVehicleBrand());
	        		cell = row.createCell(2);
	        		cell.setCellValue(vehicleMaintenance.getVehicleModel());
	        		cell = row.createCell(3);
	        		cell.setCellValue(vehicleMaintenance.getOrgName());
	        		cell = row.createCell(4);
	        		cell.setCellValue(vehicleMaintenance.getHeaderLatestMileage());
	        		cell = row.createCell(5);
	        		cell.setCellValue(vehicleMaintenance.getHeaderMaintenanceMileage());
	        		cell = row.createCell(6);
	        		cell.setCellValue(vehicleMaintenance.getMaintenanceMileage());
	        		cell = row.createCell(7);
	        		cell.setCellValue(vehicleMaintenance.getTravelMileage());
	        		cell = row.createCell(8);
	        		cell.setCellValue(vehicleMaintenance.getRemainingMileage());
	        		if(!(vehicleMaintenance.getRemainingMileage() == null || 
	        				vehicleMaintenance.getAlertMileage() == null)) {
		        			if(vehicleMaintenance.getRemainingMileage() <= vehicleMaintenance.getAlertMileage()) {
		        				HSSFFont font=workbook.createFont();
		        				font.setColor(HSSFColor.RED.index);//HSSFColor.VIOLET.index //字体颜色\
		        				style.setFont(font);
		        				cell.setCellStyle(style);
		        			}
	        		}
	        		cell = row.createCell(9);
	        		cell.setCellValue(vehicleMaintenance.getCurTimeF());
	        		if(vehicleMaintenance.getMaintenanceRemainingTime() <= 1 &&
	        				vehicleMaintenance.getMaintenanceRemainingTime() != -9999) {
	        			HSSFFont font=workbook.createFont();
						font.setColor(HSSFColor.RED.index);//HSSFColor.VIOLET.index //字体颜色\
						style.setFont(font);
						cell.setCellStyle(style);
	        		}
	        		cell = row.createCell(10);
	        		if(vehicleMaintenance.getMaintenanceRemainingTime() <= 1 &&
	        				vehicleMaintenance.getMaintenanceRemainingTime() != -9999) {
	        			HSSFFont font=workbook.createFont();
						font.setColor(HSSFColor.RED.index);
						style.setFont(font);
						cell.setCellStyle(style);
	        		}
	        		cell.setCellValue(vehicleMaintenance.getMaintenanceNextTime());
	        	}
	        }
	        
			fOut = response.getOutputStream();     
	        workbook.write(fOut);
		}catch(Exception e){
			   LOG.error("VehicleMaintenanceController.exprotExcel error",e);
		}finally{
			if (null != fOut) {
				fOut.flush();     
				fOut.close();
			}
			if(null != workbook){
				workbook.close();
			}
		}
	}
	
}
