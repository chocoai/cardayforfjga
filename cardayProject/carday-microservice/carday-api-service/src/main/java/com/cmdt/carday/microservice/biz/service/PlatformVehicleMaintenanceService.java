package com.cmdt.carday.microservice.biz.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cmdt.carrental.common.dao.VehicleMaintenanceDao;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.entity.VehicleMaintenance;
import com.cmdt.carday.microservice.common.model.response.MessageCode;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleDataCountModel;
import com.cmdt.carrental.common.model.VehicleMaintainInfoModel;
import com.cmdt.carrental.common.service.VehicleMaintenanceService;
import com.cmdt.carrental.common.util.CsvUtil;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.common.util.TypeUtils;
import com.cmdt.carday.microservice.common.model.response.exception.ServiceException;
import com.cmdt.carday.microservice.model.request.maintenance.MaintenanceCreateDto;
import com.cmdt.carday.microservice.model.request.maintenance.MaintenanceExportDto;
import com.cmdt.carday.microservice.model.request.maintenance.MaintenanceListDto;
import com.cmdt.carday.microservice.model.request.maintenance.MaintenanceResetDto;
import com.cmdt.carday.microservice.model.request.maintenance.MaintenanceThresholdDto;

@Service
public class PlatformVehicleMaintenanceService {

	private static final Logger LOG = LoggerFactory.getLogger(PlatformVehicleMaintenanceService.class);

	@Autowired
	private VehicleMaintenanceDao vmDao;
	
	@Autowired
	private VehicleMaintenanceService vmService;
	
	public PagModel listVehicleMaintencance(MaintenanceListDto listDto)  {
		VehicleMaintainInfoModel model = new VehicleMaintainInfoModel();
		try {
			BeanUtils.copyProperties(model, listDto);
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOG.error("PlatformVehicleMaintenanceService listVehicleMaintencance error : ", e);
		}
		return vmService.listPage(model);
	}
	
	public VehicleDataCountModel queryVehicleDataCountByHomePage(User loginUser){
		boolean isEnt=false;
    	//企业管理员
    	if(loginUser.isEntAdmin()){
    		isEnt=true;
    	}
    	//部门管理员
    	if(loginUser.isDeptAdmin()){
    		isEnt=false;
    	}
    	return vmService.queryVehicleDataCountByHomePage(loginUser.getOrganizationId(), isEnt);
	}
	
	public VehicleMaintenance createMaintenance(User loginUser, MaintenanceCreateDto dto) {
		dto.setCurTimeF(DateUtils.date2String(dto.getCurTime(),  DateUtils.FORMAT_YYYY_MM_DD));
		VehicleMaintenance vm = new VehicleMaintenance();
		try {
			BeanUtils.copyProperties(vm, dto);
			vm=vmService.calcMaintenance(vm);
		} catch (Exception e) {
			LOG.error("PlatformVehicleMaintenanceService createMaintenance error : ", e);
		}
		Long entId = loginUser.getOrganizationId();
		vm = saveOrUpdate(null,entId,vm);
		return vm;
	}
	
	public VehicleMaintenance saveOrUpdate(Integer num, Long entId,VehicleMaintenance vm) {
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
				vm=vmService.update(vmold);
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
	
	public Boolean reset(MaintenanceResetDto dto) {
		String condition=" and t.id=?";
		List<Object> params = new ArrayList<>();
		params.add(dto.getId());
		List<VehicleMaintenance> list=vmService.listBySql(condition, params);
		if(!list.isEmpty()){
			VehicleMaintenance vmold=list.get(0);
    		String starttime=DateUtils.date2String(dto.getCurTime(), DateUtils.FORMAT_YYYY_MM_DD);//前端传过来的是带时分秒的格式
    		int maintenanceTime = dto.getMaintenanceTime();
    		Long mantainMileage = dto.getMaintenanceMileage();
    		//如果本次保养时间大于或等于已存在记录的保养时间才更新
			if (starttime.compareTo(vmold.getCurTimeF()) >= 0) {
				// 本次保养表头里程数必须大于上次保养表头里程数
				Long headerMaintenanceMileage = dto.getHeaderMaintenanceMileage();
				if (headerMaintenanceMileage > vmold.getHeaderMaintenanceMileage()) {
					vmold.setHeaderLastMileage(vmold.getHeaderMaintenanceMileage());
					vmold.setHeaderMaintenanceMileage(headerMaintenanceMileage);
					vmold.setCurTime(dto.getCurTime());
					vmold.setMaintenanceDueTime(TimeUtils.countDateByMonth(vmold.getCurTime(), maintenanceTime));
					vmold.setAlertMileageWarn(0);
					vmold.setCurTimeWarn(0);
					vmold.setCurTimeF(starttime);
					vmold.setMaintenanceTime(maintenanceTime);
					try {
						vmold = vmService.calcMaintenance(vmold);
					} catch (Exception e) {
						LOG.error("PlatformVehicleMaintenanceService reset error : ", e);
					}
					VehicleMaintenance vMaintenance = list.get(0);
					int thresholdMonth = vMaintenance.getThresholdMonth();
					thresholdMonth = Integer.parseInt("-" + Math.abs(thresholdMonth));
					Date maintenanceThresholdTime = TimeUtils.countDateByMonth(vmold.getMaintenanceDueTime(),
							thresholdMonth);
					vmold.setMaintenanceThresholdTime(maintenanceThresholdTime);
					vmold.setMaintenanceMileage(mantainMileage);
					vmService.update(vmold);
				} else {	
					throw new ServiceException(MessageCode.HEAD_MAINTENANCE_MILEAGE_LESSTHAN_LASTTIME);	
				}
			} else {
				throw new ServiceException(MessageCode.MAINTENANCE_TIME_BEFORE_CURRENT_MAINTENANCE_TIME);
			}
		}
		return true;
	}
	
	public Boolean setThreshold(MaintenanceThresholdDto listDto) {
		VehicleMaintenance maintenance = new VehicleMaintenance();
		try {
			BeanUtils.copyProperties(maintenance, listDto);
		} catch (IllegalAccessException | InvocationTargetException e) {
			LOG.error("PlatformVehicleMaintenanceService setThreshold error : ", e);
		} 
		String dateTemp = String.valueOf(listDto.getMaintenanceDueTimeF());//下次保养时间
		Date maintenanceDueTime = DateUtils.string2Date(dateTemp, "yyyy-MM-dd");
		int monthNum = Integer.parseInt("-" + Math.abs(maintenance.getThresholdMonth()));
		Date maintenanceThresholdTime = TimeUtils.countDateByMonth(maintenanceDueTime, monthNum);
		maintenance.setMaintenanceThresholdTime(maintenanceThresholdTime);
		vmDao.setThreshold(maintenance);
		return true;
	}
	
	public String importVehicleMaintainFile(User loginUser, MultipartFile multiFile) throws Exception{
		String fileNa = org.springframework.util.StringUtils.getFilenameExtension(multiFile.getOriginalFilename());
		String suffix1=".xls";
    	String suffix2=".xlsx";
    	String suffix3=".csv";

    	if(!fileNa.endsWith(suffix1)&&!fileNa.endsWith(suffix2)&&!fileNa.endsWith(suffix3)){
	    	List<VehicleMaintenance> modelList = new ArrayList<>();
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
			StringBuilder msg= new StringBuilder();
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
				return "成功导入:"+sucNum+",失败:"+failNum+",详细如下:<br />"+msg.toString();
			} else {
				return "成功导入:"+sucNum+",失败:"+failNum;
			}
    	}  else {
			throw new ServiceException(MessageCode.COMMON_UPLOAD_FILE_EXTENSION_ERROR);
		}
	}
	
	public File generateVehicleMaintainExport(User loginUser,MaintenanceExportDto dto) throws Exception{
		if(loginUser.isEntAdmin()){
			Long entId=loginUser.getOrganizationId();
			List<VehicleMaintenance> resultList = vmDao.queryVehicleMaintenanceExportList(entId, dto.getVehicleNumber(), dto.getDeptId(), dto.getIds());
			if(resultList != null) {
				for(VehicleMaintenance vehicleMaintenance : resultList) {
					String orgName = vehicleMaintenance.getOrgName();
					if(StringUtils.isEmpty(orgName)) {
						vehicleMaintenance.setOrgName("未分配");
					}
					int maintenanceTime = vehicleMaintenance.getMaintenanceTime();
					Date startDate = vehicleMaintenance.getCurTime();
					if(startDate != null && maintenanceTime != 0) {
						Date maintenanceNextTime = TimeUtils.countDateByMonth(startDate, maintenanceTime);
						vehicleMaintenance.setMaintenanceNextTime(TimeUtils.dateToString(maintenanceNextTime));
						Date maintenanceThresholdTime = vehicleMaintenance.getMaintenanceThresholdTime();
						boolean status = DateUtils.compareTime(new Date(), maintenanceThresholdTime);
						int maintenanceRemainingTime = 0;
						if(status) {
							maintenanceRemainingTime = 2;
						}
						vehicleMaintenance.setMaintenanceRemainingTime(maintenanceRemainingTime);
					}else {
						vehicleMaintenance.setMaintenanceNextTime("");
						vehicleMaintenance.setMaintenanceRemainingTime(-9999);
					}
				}
			}
			return exprotExcel("maintenance_"+DateUtils.getNowDate()+".xls", resultList);
		}
		return null;
	}
	
	private File exprotExcel(String fileName, List<VehicleMaintenance> list) throws Exception {
		OutputStream fOut = null;
		HSSFWorkbook workbook = null;
		try{
			workbook = new HSSFWorkbook(); //NOSONAR workbook has been close in finally
			String excelName = fileName;
			File file =new File(excelName);
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
	        
			fOut = new FileOutputStream(file);      
	        workbook.write(fOut);
	        return file;
		}catch(Exception e){
			   throw new Exception("CSVUtilsView.exportExcel failure due to exception!", e);
		}finally{
			if (null != fOut) {
				fOut.flush();     
				fOut.close();
			}
			workbook.close();
		}
	}
}


