package com.cmdt.carrental.common.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.dao.UsageReportDao;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.UsageIdleVehicleModel;
import com.cmdt.carrental.common.model.UsageReportAllMileageAndFuleconsModel;
import com.cmdt.carrental.common.model.UsageReportAppColumnarItemModel;
import com.cmdt.carrental.common.model.UsageReportAppColumnarModel;
import com.cmdt.carrental.common.model.UsageReportAppLineItemModel;
import com.cmdt.carrental.common.model.UsageReportAppLineModel;
import com.cmdt.carrental.common.model.UsageReportColumnarItemModel;
import com.cmdt.carrental.common.model.UsageReportColumnarModel;
import com.cmdt.carrental.common.model.UsageReportLineItemModel;
import com.cmdt.carrental.common.model.UsageReportLineModel;
import com.cmdt.carrental.common.model.UsageReportModel;
import com.cmdt.carrental.common.model.UsageReportPieItemModel;
import com.cmdt.carrental.common.model.UsageReportPieModel;
import com.cmdt.carrental.common.model.UsageReportSQLModel;
import com.cmdt.carrental.common.model.UsageVehiclePropertyModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.util.BeanSortUtils;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.common.util.TimeUtils;

@Service
public class UsageReportServiceImpl implements UsageReportService{

	@Autowired
	private UsageReportDao usageReportDao;
	
	private static DecimalFormat df = new DecimalFormat("0.00");
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private OrganizationService organizationService;

	@Override
	public UsageReportModel getPieAndColumnarDataByDayRange(Long entId,Date startDate,Date endDate) {
		List<UsageReportSQLModel> usageReportSQLModelList = usageReportDao.getPieAndColumnarDataByDayRange(entId,startDate,endDate);
		if(usageReportSQLModelList != null && usageReportSQLModelList.size() > 0){
			long durationDays = TimeUtils.timeBetween(startDate, endDate, Calendar.DATE);
			return processPieAndColumnarData(usageReportSQLModelList,durationDays);
		}
		return null;
	}

	/**
	 * 饼图与柱状图
	 * @param usageReportSQLModelList
	 * @return
	 */
	private UsageReportModel processPieAndColumnarData(List<UsageReportSQLModel> usageReportSQLModelList,long durationDays) {
		UsageReportModel usageReportModel = new UsageReportModel();
		Integer sumTotalMileage = 0;
		Double sumTotalFuleCons = 0.0;
		Integer sumTotalDrivingTime = 0;
		for(UsageReportSQLModel usageReportSQLModel : usageReportSQLModelList){
			sumTotalMileage += usageReportSQLModel.getTotal_mileage();
			sumTotalFuleCons += usageReportSQLModel.getTotal_fuel_cons();
			sumTotalDrivingTime += usageReportSQLModel.getTotal_driving_time();
		}
		//饼状图
		List<UsageReportPieModel> pieList = processPieData(usageReportSQLModelList,sumTotalMileage,sumTotalFuleCons,sumTotalDrivingTime);
		usageReportModel.setPieList(pieList);
		//柱状图(基于饼状图，只是将数据进行平均)
		List<UsageReportColumnarModel> columnarList = processColumnarData(pieList,sumTotalMileage,sumTotalFuleCons,sumTotalDrivingTime,durationDays);
		usageReportModel.setColumnarList(columnarList);
		
		return usageReportModel;
	}
	
	/**
	 * 饼图
	 * @param usageReportSQLModelList
	 * @return
	 */
	private List<UsageReportPieModel> processPieData(List<UsageReportSQLModel> usageReportSQLModelList,Integer sumTotalMileage,Double sumTotalFuleCons,Integer sumTotalDrivingTime) {
		List<UsageReportPieModel> retList = new ArrayList<UsageReportPieModel>();
		retList.add(processPieTotalMileageData(usageReportSQLModelList,sumTotalMileage));
		retList.add(processPieTotalFuleConsData(usageReportSQLModelList,sumTotalFuleCons));
		retList.add(processPieTotalDrivingTimeData(usageReportSQLModelList,sumTotalDrivingTime));
		return retList;
	}

	/**
	 * 总里程饼图
	 * @param usageReportSQLModelList
	 * @return
	 */
	private UsageReportPieModel processPieTotalMileageData(List<UsageReportSQLModel> usageReportSQLModelList,Integer sumTotalMileage) {
		UsageReportPieModel totalMilagePie = new UsageReportPieModel();
		totalMilagePie.setStoreId("totalMilagePie");
		totalMilagePie.setStoreName("总里程");
		totalMilagePie.setUnit("千米");
		List<UsageReportPieItemModel> retList = new ArrayList<UsageReportPieItemModel>();
		for(UsageReportSQLModel usageReportSQLModel : usageReportSQLModelList){
			UsageReportPieItemModel usageReportPieItemModel = new UsageReportPieItemModel();
			usageReportPieItemModel.setName(usageReportSQLModel.getCurrentuse_org_name());
			Integer totalMileage = usageReportSQLModel.getTotal_mileage();//米
			usageReportPieItemModel.setData(df.format((double)totalMileage/1000));//米转换为千米
			if(totalMileage.intValue() != 0 && sumTotalMileage.intValue() != 0){
				usageReportPieItemModel.setPrecent(df.format((double)totalMileage*100/sumTotalMileage) + "%");
			}else{
				usageReportPieItemModel.setPrecent("0%");
			}
			retList.add(usageReportPieItemModel);
		}
		totalMilagePie.setDataList(retList);
		return totalMilagePie;
	}

	/**
	 * 总油耗量饼图
	 * @param usageReportSQLModelList
	 * @return
	 */
	private UsageReportPieModel processPieTotalFuleConsData(List<UsageReportSQLModel> usageReportSQLModelList,Double sumTotalFuleCons) {
		UsageReportPieModel totalFuleConsPie = new UsageReportPieModel();
		totalFuleConsPie.setStoreId("totalFuleConsPie");
		totalFuleConsPie.setStoreName("总耗油量");
		totalFuleConsPie.setUnit("升");
		List<UsageReportPieItemModel> retList = new ArrayList<UsageReportPieItemModel>();
		for(UsageReportSQLModel usageReportSQLModel : usageReportSQLModelList){
			UsageReportPieItemModel usageReportPieItemModel = new UsageReportPieItemModel();
			usageReportPieItemModel.setName(usageReportSQLModel.getCurrentuse_org_name());
			Double totalFuleCons = usageReportSQLModel.getTotal_fuel_cons();
			usageReportPieItemModel.setData(df.format((double)totalFuleCons));
			if(totalFuleCons.doubleValue() != 0.0 && sumTotalFuleCons.doubleValue() != 0.0){
				usageReportPieItemModel.setPrecent(df.format((double)totalFuleCons*100/sumTotalFuleCons) + "%");
			}else{
				usageReportPieItemModel.setPrecent("0%");
			}
			retList.add(usageReportPieItemModel);
		}
		totalFuleConsPie.setDataList(retList);
		return totalFuleConsPie;
	}

	/**
	 * 总行驶时长饼图
	 * @param usageReportSQLModelList
	 * @return
	 */
	private UsageReportPieModel processPieTotalDrivingTimeData(List<UsageReportSQLModel> usageReportSQLModelList,Integer sumTotalDrivingTime) {
		UsageReportPieModel totalDrivingTimeMilagePie = new UsageReportPieModel();
		totalDrivingTimeMilagePie.setStoreId("totalDrivingTimeMilagePie");
		totalDrivingTimeMilagePie.setStoreName("总行驶时长");
		totalDrivingTimeMilagePie.setUnit("小时");
		List<UsageReportPieItemModel> retList = new ArrayList<UsageReportPieItemModel>();
		for(UsageReportSQLModel usageReportSQLModel : usageReportSQLModelList){
			UsageReportPieItemModel usageReportPieItemModel = new UsageReportPieItemModel();
			usageReportPieItemModel.setName(usageReportSQLModel.getCurrentuse_org_name());
			Integer totalDrivingTime = usageReportSQLModel.getTotal_driving_time();
			if(totalDrivingTime % 3600 == 0){
				usageReportPieItemModel.setData(String.valueOf(totalDrivingTime/3600));
			}else{
				usageReportPieItemModel.setData(df.format((double)totalDrivingTime/3600));
			}
			
			if(totalDrivingTime.intValue() != 0 && sumTotalDrivingTime.intValue() != 0){
				usageReportPieItemModel.setPrecent(df.format((double)totalDrivingTime*100/sumTotalDrivingTime) + "%");
			}else{
				usageReportPieItemModel.setPrecent("0%");
			}
			retList.add(usageReportPieItemModel);
		}
		totalDrivingTimeMilagePie.setDataList(retList);
		return totalDrivingTimeMilagePie;
	}
	
	/**
	 * 柱状图
	 * @param pieList
	 * @param sumTotalMileage
	 * @param sumTotalFuleCons
	 * @param sumTotalDrivingTime
	 * @return
	 */
	private List<UsageReportColumnarModel> processColumnarData(List<UsageReportPieModel> pieList,
			Integer sumTotalMileage, Double sumTotalFuleCons, Integer sumTotalDrivingTime,long durationDays) {
		List<UsageReportColumnarModel> retList = new ArrayList<UsageReportColumnarModel>();
		retList.add(populateMileageColumnarData(pieList.get(0),sumTotalMileage,durationDays,"avgDrivingTimeColumnar","平均里程","千米/天"));
		retList.add(populateFuelColumnarData(pieList.get(1),sumTotalFuleCons,durationDays,"avgFuleConsColumnar","平均耗油量","升/天"));
		retList.add(populateDrivingTimeColumnarData(pieList.get(2),sumTotalDrivingTime,durationDays,"avgDrivingTimeColumnar","平均时长","小时/天"));
		return retList;
	}
	
	/**
	 * 柱状图填充(平均时长)
	 * @param usageReportPieModel
	 * @param sumTotalDrivingTime
	 * @return
	 */
	private UsageReportColumnarModel populateDrivingTimeColumnarData(UsageReportPieModel usageReportPieModel,
			Integer totalVal,long durationDays,String storeId,String storeName,String unit) {
		//部门个数
		Integer deptNum = 0;
		UsageReportColumnarModel usageReportColumnarModel = new UsageReportColumnarModel();
		usageReportColumnarModel.setStoreId(storeId);
		usageReportColumnarModel.setStoreName(storeName);
		usageReportColumnarModel.setUnit(unit);
		List<UsageReportColumnarItemModel> retList = new ArrayList<UsageReportColumnarItemModel>();
		usageReportColumnarModel.setDataList(retList);
		
		//遍历饼图
		List<UsageReportPieItemModel> dataList = usageReportPieModel.getDataList();
		deptNum = dataList.size();
		for(UsageReportPieItemModel usageReportPieItemModel : dataList){
			UsageReportColumnarItemModel usageReportColumnarItemModel = new UsageReportColumnarItemModel();
			usageReportColumnarItemModel.setName(usageReportPieItemModel.getName());
			//平均值分母为查询天数
			usageReportColumnarItemModel.setData(df.format((double)Double.valueOf(usageReportPieItemModel.getData())/durationDays));
			retList.add(usageReportColumnarItemModel);
		}
		
		//平均值
		//平均值分母为(部门个数*查询天数)
		if(durationDays*deptNum != 0){
				usageReportColumnarModel.setAvgVal(df.format((double)totalVal/3600/(deptNum*durationDays)));
		}
		return usageReportColumnarModel;
	}
	
	/**
	 * 柱状图填充(平均里程)
	 * @param usageReportPieModel
	 * @param sumTotalDrivingTime
	 * @return
	 */
	private UsageReportColumnarModel populateMileageColumnarData(UsageReportPieModel usageReportPieModel,
			Integer totalVal,long durationDays,String storeId,String storeName,String unit) {
		//部门个数
		Integer deptNum = 0;
		UsageReportColumnarModel usageReportColumnarModel = new UsageReportColumnarModel();
		usageReportColumnarModel.setStoreId(storeId);
		usageReportColumnarModel.setStoreName(storeName);
		usageReportColumnarModel.setUnit(unit);
		List<UsageReportColumnarItemModel> retList = new ArrayList<UsageReportColumnarItemModel>();
		usageReportColumnarModel.setDataList(retList);
		
		//遍历饼图
		List<UsageReportPieItemModel> dataList = usageReportPieModel.getDataList();
		deptNum = dataList.size();
		for(UsageReportPieItemModel usageReportPieItemModel : dataList){
			UsageReportColumnarItemModel usageReportColumnarItemModel = new UsageReportColumnarItemModel();
			usageReportColumnarItemModel.setName(usageReportPieItemModel.getName());
			//平均值分母为查询天数
			usageReportColumnarItemModel.setData(df.format((double)Double.valueOf(usageReportPieItemModel.getData())/durationDays));
			retList.add(usageReportColumnarItemModel);
		}
		
		//平均值
		//平均值分母为(部门个数*查询天数)
		if(durationDays*deptNum != 0){
				usageReportColumnarModel.setAvgVal(df.format((double)totalVal/1000/(deptNum*durationDays)));//米换算为千米
		}
		return usageReportColumnarModel;
	}
	
	/**
	 * 柱状图填充(平均耗油量)
	 * @param usageReportPieModel
	 * @param sumTotalDrivingTime
	 * @return
	 */
	private UsageReportColumnarModel populateFuelColumnarData(UsageReportPieModel usageReportPieModel,
			Double totalVal,long durationDays,String storeId,String storeName,String unit) {
		//部门个数
		Integer deptNum = 0;
		UsageReportColumnarModel usageReportColumnarModel = new UsageReportColumnarModel();
		usageReportColumnarModel.setStoreId(storeId);
		usageReportColumnarModel.setStoreName(storeName);
		usageReportColumnarModel.setUnit(unit);
		List<UsageReportColumnarItemModel> retList = new ArrayList<UsageReportColumnarItemModel>();
		usageReportColumnarModel.setDataList(retList);
		
		//遍历饼图
		List<UsageReportPieItemModel> dataList = usageReportPieModel.getDataList();
		deptNum = dataList.size();
		for(UsageReportPieItemModel usageReportPieItemModel : dataList){
			UsageReportColumnarItemModel usageReportColumnarItemModel = new UsageReportColumnarItemModel();
			usageReportColumnarItemModel.setName(usageReportPieItemModel.getName());
			//平均值分母为查询天数
			usageReportColumnarItemModel.setData(df.format((double)Double.valueOf(usageReportPieItemModel.getData())/durationDays));//已经为升，不需要换算
			retList.add(usageReportColumnarItemModel);
		}
		
		//平均值
		//平均值分母为(部门个数*查询天数)
		if(durationDays*deptNum != 0){
				usageReportColumnarModel.setAvgVal(df.format((double)totalVal/(deptNum*durationDays)));//已经为升，不需要换算
		}
		return usageReportColumnarModel;
	}

	@Override
	public PagModel getVehiclePropertyDataByDayRangeAndEntAdmin(Date startDate, Date endDate, long entId, long deptId,int currentPage,int numPerPage) {
		PagModel pageModel = null;
		List<UsageVehiclePropertyModel> resultList = new ArrayList<UsageVehiclePropertyModel>();
		if(deptId != 0l){//按部门查询
			pageModel = usageReportDao.getDeptVehiclePropertyDataByDayRangeAndEntAdmin(startDate,endDate,deptId,currentPage,numPerPage);
		}else{//查询所有部门
			pageModel = usageReportDao.getAllDeptVehiclePropertyDataByDayRangeAndEntAdmin(startDate,endDate,entId,currentPage,numPerPage);
		}
		if(pageModel != null){
			List<UsageReportSQLModel> retList = pageModel.getResultList();
			long durationDays = TimeUtils.timeBetween(startDate, endDate, Calendar.DATE);
			
			for(UsageReportSQLModel usageReportSQLModel : retList){
				UsageVehiclePropertyModel usageVehiclePropertyModel = new UsageVehiclePropertyModel();
				usageVehiclePropertyModel.setId(usageReportSQLModel.getId());
				usageVehiclePropertyModel.setVehicleNumber(usageReportSQLModel.getVehicle_number());
				usageVehiclePropertyModel.setVehicleBrand(usageReportSQLModel.getVehicle_brand());
				usageVehiclePropertyModel.setVehicleOutput(usageReportSQLModel.getVehicle_output());
				usageVehiclePropertyModel.setVehicleFuel(usageReportSQLModel.getVehicle_fuel());
				usageVehiclePropertyModel.setCurrentuseOrgName(usageReportSQLModel.getCurrentuse_org_name());
				usageVehiclePropertyModel.setTotalMileage(df.format((double)usageReportSQLModel.getTotal_mileage()/1000));
				usageVehiclePropertyModel.setTotalFuelcons(df.format((double)usageReportSQLModel.getTotal_fuel_cons()));
				if(usageReportSQLModel.getTotal_driving_time() % 3600 == 0){
					usageVehiclePropertyModel.setTotalDrivingtime(String.valueOf(usageReportSQLModel.getTotal_driving_time()/3600));
				}else{
					usageVehiclePropertyModel.setTotalDrivingtime(df.format((double)usageReportSQLModel.getTotal_driving_time()/3600));
				}
				//使用率
				usageVehiclePropertyModel.setUsagePercent(df.format((double)usageReportSQLModel.getTotal_driving_time()/(durationDays*8*3600)));//每天按8小时算
				usageVehiclePropertyModel.setAvgMileage(df.format((double)usageReportSQLModel.getTotal_mileage()/1000/durationDays));
				usageVehiclePropertyModel.setAvgFuelcons(df.format((double)usageReportSQLModel.getTotal_fuel_cons()/durationDays));
				usageVehiclePropertyModel.setAvgDrivingtime(df.format((double)usageReportSQLModel.getTotal_driving_time()/(durationDays*3600)));
				resultList.add(usageVehiclePropertyModel);
			}
			pageModel.setResultList(resultList);
		}
		return pageModel;
	}

	@Override
	public UsageReportLineModel getVehicleLinePropertyDataByDayRange(Date startDate, Date endDate, long entId,long deptId) {
			
		UsageReportLineModel usageReportLineModel = new UsageReportLineModel();
		
		usageReportLineModel.setData1unit("里程(千米)");
		usageReportLineModel.setData2unit("耗油量(升)");
		
		//days
		List<UsageReportSQLModel> actualList = null;
		if(deptId != 0l){//按部门查询
			actualList = usageReportDao.getDeptVehicleLinePropertyDataByDayRange(startDate,endDate,deptId);
		}else{//查询所有部门
			actualList = usageReportDao.getAllVehicleLinePropertyDataByDayRange(startDate,endDate,entId);
		}
		
		if(actualList != null && actualList.size() > 0){
			
			Map<String, UsageReportSQLModel> actualMap = new HashMap<String, UsageReportSQLModel>();//实际存在日期的值
			for(UsageReportSQLModel usageReportSQLModel : actualList){
				actualMap.put(usageReportSQLModel.getDay(), usageReportSQLModel);
			}
			
			long durationDays = TimeUtils.timeBetween(startDate, endDate, Calendar.DATE);
			
			//补齐日期
			List<UsageReportSQLModel> retList = populateUsageReportSQLModelDays(startDate,Integer.valueOf(String.valueOf(durationDays)),actualMap);
			
			if(retList != null && retList.size() > 0){
				int totalData1 = 0;
				int totalData2 = 0;
				int retSize = retList.size();
				
				List<UsageReportLineItemModel> dataList = new ArrayList<UsageReportLineItemModel>();
				for(UsageReportSQLModel usageReportSQLModel : retList){
					UsageReportLineItemModel usageReportLineItemModel = new UsageReportLineItemModel();
					usageReportLineItemModel.setDay(usageReportSQLModel.getDay());
					totalData1 += usageReportSQLModel.getTotal_mileage();
					usageReportLineItemModel.setData1(df.format((double)usageReportSQLModel.getTotal_mileage()/1000));
					totalData2 += usageReportSQLModel.getTotal_fuel_cons();
					usageReportLineItemModel.setData2(df.format((double)usageReportSQLModel.getTotal_fuel_cons()));
					dataList.add(usageReportLineItemModel);
				}
				
				usageReportLineModel.setData1avgval(df.format((double)totalData1/(retSize*1000)));
				usageReportLineModel.setData2avgval(df.format((double)totalData2/retSize));
				
				usageReportLineModel.setDataList(dataList);
			}
		}
		

		return usageReportLineModel;
	}

	@Override
	public UsageReportAllMileageAndFuleconsModel getVehicleAllMileageAndFuleconsList(String vehicleNumber,
			String deviceNumber) {
		//当日
		Date today = new Date();
		//昨日
    	Date yesterday = DateUtils.addDays(today,-1);
    	
    	String todayVal = DateUtils.date2String(today,"yyyy-MM-dd");
    	String yesterdayVal = DateUtils.date2String(yesterday,"yyyy-MM-dd");
    	//本周截止到今天的日期
    	List<String> daysOFCurrentweekVal = DateUtils.getDaysOFCurrentWeek(today);
    	//本月
    	String currentMonthVal = DateUtils.date2String(today,"yyyy-MM");
    	UsageReportAllMileageAndFuleconsModel usageReportAllMileageAndFuleconsModel = usageReportDao.getVehicleAllMileageAndFuleconsList(vehicleNumber,deviceNumber,todayVal,yesterdayVal,daysOFCurrentweekVal,currentMonthVal);
	    
    	
    	//米转换为千米
    	String todayMileage = usageReportAllMileageAndFuleconsModel.getTodayMileage();
    	String yesterdayMileage = usageReportAllMileageAndFuleconsModel.getYesterdayMileage();
    	String currentweekMileage = usageReportAllMileageAndFuleconsModel.getCurrentweekMileage();
    	String currentMonthMileage = usageReportAllMileageAndFuleconsModel.getCurrentMonthMileage();
    	
    	if(!StringUtils.isEmpty(todayMileage)){
    		usageReportAllMileageAndFuleconsModel.setTodayMileage(df.format((double)Integer.valueOf(todayMileage)/1000));
    	}
    	
    	if(!StringUtils.isEmpty(yesterdayMileage)){
    		usageReportAllMileageAndFuleconsModel.setYesterdayMileage(df.format((double)Integer.valueOf(yesterdayMileage)/1000));
    	}
    	
    	if(!StringUtils.isEmpty(currentweekMileage)){
    		usageReportAllMileageAndFuleconsModel.setCurrentweekMileage(df.format((double)Integer.valueOf(currentweekMileage)/1000));
    	}
    	
    	if(!StringUtils.isEmpty(currentMonthMileage)){
    		usageReportAllMileageAndFuleconsModel.setCurrentMonthMileage(df.format((double)Integer.valueOf(currentMonthMileage)/1000));
    	}
    	
    	return usageReportAllMileageAndFuleconsModel;
	}

	@Override
	public PagModel getIdleVehicleListByDayRange(Date startDate, Date endDate, long entId, long deptId, int currentPage,
			int numPerPage) {
		PagModel pageModel = null;
		List<UsageIdleVehicleModel> resultList = new ArrayList<UsageIdleVehicleModel>();
		if(deptId != 0l){//按部门查询
			pageModel = usageReportDao.getDeptIdleVehicleListByDayRange(startDate,endDate,deptId,currentPage,numPerPage);
		}else{//查询所有部门
			pageModel = usageReportDao.getAllIdleVehicleListByDayRange(startDate,endDate,entId,currentPage,numPerPage);
		}
		if(pageModel != null){
			List<UsageReportSQLModel> retList = pageModel.getResultList();
			
			String starttime = DateUtils.date2String(startDate, "yyyy-MM-dd");
			String enttime = DateUtils.date2String(endDate, "yyyy-MM-dd");
			String idelInfo = starttime+"到"+enttime;
		
			for(UsageReportSQLModel usageReportSQLModel : retList){
				UsageIdleVehicleModel usageIdleVehicleModel = new UsageIdleVehicleModel();
				usageIdleVehicleModel.setId(usageReportSQLModel.getId());
				usageIdleVehicleModel.setVehicleNumber(usageReportSQLModel.getVehicle_number());
				usageIdleVehicleModel.setVehicleBrand(usageReportSQLModel.getVehicle_brand());
				usageIdleVehicleModel.setVehicleModel(usageReportSQLModel.getVehicle_model());
				usageIdleVehicleModel.setCurrentuseOrgName(usageReportSQLModel.getCurrentuse_org_name());
				usageIdleVehicleModel.setVehiclePurpose(usageReportSQLModel.getVehicle_purpose());
				usageIdleVehicleModel.setIdelInfo(idelInfo);
				resultList.add(usageIdleVehicleModel);
			}
			pageModel.setResultList(resultList);
		}
		return pageModel;
	}
	
	
	@Override
	public List<String> getIdleVehicleListByDayRangeALL(Date startDate, Date endDate, long entId, long deptId) {
		List<String> resultList = new ArrayList<String>();
		List<UsageReportSQLModel> searchList = null;
		if(deptId != 0l){//按部门查询
			searchList = usageReportDao.getDeptIdleVehicleListByDayRangeForALL(startDate,endDate,deptId);
		}else{//查询所有部门
			searchList = usageReportDao.getAllIdleVehicleListByDayRangeForALL(startDate,endDate,entId);
		}
		if(searchList != null){
			
			String starttime = DateUtils.date2String(startDate, "yyyy-MM-dd");
			String enttime = DateUtils.date2String(endDate, "yyyy-MM-dd");
			String idelInfo = starttime+"到"+enttime;
			
			for(UsageReportSQLModel usageReportSQLModel : searchList){
				String usageIdleVehicle = usageReportSQLModel.getVehicle_number() + ',';
				usageIdleVehicle += usageReportSQLModel.getVehicle_brand() + ',';
				usageIdleVehicle += usageReportSQLModel.getVehicle_model() + ',';
				usageIdleVehicle += usageReportSQLModel.getCurrentuse_org_name() + ',';
				usageIdleVehicle += usageReportSQLModel.getVehicle_purpose() + ',';
				usageIdleVehicle += idelInfo;

				resultList.add(usageIdleVehicle);
			}
		}
		return resultList;
	}

	@Override
	public VehicleModel findVehicleListByDeviceNumberAndImei(String vehicleNumber, String deviceNumber) {
		return usageReportDao.findVehicleListByDeviceNumberAndImei(vehicleNumber,deviceNumber);
	}

	@Override
	public VehicleModel findVehicleListByVehicleNumber(String vehicleNumber) {
		return usageReportDao.findVehicleListByVehicleNumber(vehicleNumber);
	}

	@Override
	public UsageReportAppLineModel getTendencyChartByDayRangeAndType(Date startDate, Date endDate, String queryType,
			long orgId,boolean self,boolean child) {
		UsageReportAppLineModel ret = new UsageReportAppLineModel();
		if(Constants.USAGE_TYPE_MILE.equals(queryType)){
			ret.setDataFlag(Constants.USAGE_TYPE_MILE_FLAG);
			ret.setDataUnit(Constants.USAGE_TYPE_MILE_UNIT);
		}else if(Constants.USAGE_TYPE_FUEL.equals(queryType)){
			ret.setDataFlag(Constants.USAGE_TYPE_FUEL_FLAG);
			ret.setDataUnit(Constants.USAGE_TYPE_FUEL_UNIT);
		}else if(Constants.USAGE_TYPE_TIME.equals(queryType)){
			ret.setDataFlag(Constants.USAGE_TYPE_TIME_FLAG);
			ret.setDataUnit(Constants.USAGE_TYPE_TIME_UNIT);
		}
		
        String type = "";
		
		Organization organization = organizationService.findById(orgId);
		
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
		
		List<Organization> organizationList = organizationService.findDownOrganizationListByOrgId(orgId,false,true);
		
		List<Long> orgIdList = new ArrayList<Long>();
		if(organizationList != null && organizationList.size() > 0){
			for(Organization org : organizationList){
				orgIdList.add(org.getId());
			}
		}
		
		
		List<UsageReportSQLModel> retList = new ArrayList<UsageReportSQLModel>();
		if("2".equals(type)){//部门及子部门
			retList = usageReportDao.getTendencyChartByDept(startDate,endDate,queryType,orgId,orgIdList,self,child);
		}else if("0".equals(type)){//企业
			retList = usageReportDao.getTendencyChartByEnt(startDate,endDate,queryType,orgId,orgIdList,self,child);
		}else if("1".equals(type)){//租车企业
			retList = usageReportDao.getTendencyChartByRent(startDate,endDate,queryType,orgId,orgIdList,self,child);
		}

		if(retList != null && retList.size() > 0){
			int totalMileageVal = 0;
			double totalFuleConsVal = 0.0;
			int totalDrivingTimeVal = 0;
			int retSize = retList.size();
			
			List<UsageReportAppLineItemModel> dataList = new ArrayList<UsageReportAppLineItemModel>();
			for(UsageReportSQLModel usageReportSQLModel : retList){
				UsageReportAppLineItemModel usageReportAppLineItemModel = new UsageReportAppLineItemModel();
				usageReportAppLineItemModel.setDay(usageReportSQLModel.getDay());
				if(Constants.USAGE_TYPE_MILE.equals(queryType)){
					Integer totalMileage = usageReportSQLModel.getTotal_mileage();//米
					usageReportAppLineItemModel.setData(df.format((double)totalMileage/1000));//米转换为千米
					totalMileageVal += totalMileage;
				}else if(Constants.USAGE_TYPE_FUEL.equals(queryType)){
					Double totalFuleCons = usageReportSQLModel.getTotal_fuel_cons();//升
					usageReportAppLineItemModel.setData(df.format((double)totalFuleCons));//不需要转换
					totalFuleConsVal += totalFuleCons;
				}else if(Constants.USAGE_TYPE_TIME.equals(queryType)){
					Integer totalDrivingTime = usageReportSQLModel.getTotal_driving_time();//秒
					if(totalDrivingTime % 3600 == 0){
						usageReportAppLineItemModel.setData(String.valueOf(totalDrivingTime/3600));//秒转换为小时
					}else{
						usageReportAppLineItemModel.setData(df.format((double)totalDrivingTime/3600));//秒转换为小时
					}
					totalDrivingTimeVal += totalDrivingTime;
				}
				dataList.add(usageReportAppLineItemModel);
			}
			
			if(Constants.USAGE_TYPE_MILE.equals(queryType)){
				ret.setDataAvgval(df.format((double)totalMileageVal/1000/retSize));//米转换为千米
			}else if(Constants.USAGE_TYPE_FUEL.equals(queryType)){
				ret.setDataAvgval(df.format((double)totalFuleConsVal/retSize));//不需要转换
			}else if(Constants.USAGE_TYPE_TIME.equals(queryType)){
			    ret.setDataAvgval(df.format((double)totalDrivingTimeVal/3600/retSize));//秒转换为小时
			}
			
			ret.setDataList(dataList);
		}
		return ret;
	}

	@Override
	public UsageReportAppColumnarModel getDepartmentColumnarChartByDayRangeAndType(Date startDate, Date endDate,
			String queryType, long deptId,boolean self,boolean child) {
		UsageReportAppColumnarModel reportModel = new UsageReportAppColumnarModel();
		
		if(Constants.USAGE_TYPE_MILE.equals(queryType)){
			reportModel.setDataFlag(Constants.USAGE_TYPE_MILE_FLAG);
			reportModel.setDataUnit(Constants.USAGE_TYPE_MILE_UNIT);
		}else if(Constants.USAGE_TYPE_FUEL.equals(queryType)){
			reportModel.setDataFlag(Constants.USAGE_TYPE_FUEL_FLAG);
			reportModel.setDataUnit(Constants.USAGE_TYPE_FUEL_UNIT);
		}else if(Constants.USAGE_TYPE_TIME.equals(queryType)){
			reportModel.setDataFlag(Constants.USAGE_TYPE_TIME_FLAG);
			reportModel.setDataUnit(Constants.USAGE_TYPE_TIME_UNIT);
		}
		
		
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(deptId, true , true);
		if(null!=orgList&&orgList.size()>0){
			
			String orgType = "";
			
			Organization organization = organizationService.findById(deptId);
			String enterprisesType = organization.getEnterprisesType();//(0:租车公司，1:用车企业)
			if(StringUtils.isEmpty(enterprisesType)){
				orgType = "2";//部门及子部门
			}else{
				if("0".equals(enterprisesType)){
					orgType = "1";//租车公司
				}else{//企业
					orgType = "0";
				}
			}
			
			
			List<UsageReportSQLModel> usageReportSQLModelList = usageReportDao.getDepartmentColumnarChartByDayRangeAndType(startDate,endDate,queryType,orgType,deptId,orgList);
			
			//构建树
			//首先筛选出根节点
			UsageReportSQLModel tree = null;
				for(UsageReportSQLModel usageReportSQLModel : usageReportSQLModelList){
						if(usageReportSQLModel.getCurrentuse_org_id().longValue() == deptId){
							tree = usageReportSQLModel;
							tree.setRootNode(true);
							break;
						}
				    }
			buildUsageReportTree(tree,usageReportSQLModelList);
	        //根据参数处理Report
			processUsageReportAppColumnar(reportModel, tree, orgList,self, child, queryType);
		}
		return reportModel;
	}
	
	private void processUsageReportAppColumnar(UsageReportAppColumnarModel reportModel,UsageReportSQLModel tree,List<Organization> orgList,boolean self,boolean child,String queryType){
		
		if(orgList!= null && orgList.size() > 0){
			
			int totalMileageVal = 0;
			int totalFuleConsVal = 0;
			int totalDrivingTimeVal = 0;
			
			int retSize = 0;
			
			List<UsageReportAppColumnarItemModel> dataList = new ArrayList<UsageReportAppColumnarItemModel>();
			
			//process node
			if(self){
		     retSize++;
			 generateUsageReportAppColumnar(dataList,tree,queryType);
			}
			if(child){
				//process children nodes
				List<UsageReportSQLModel> childrenNodes =  tree.getChildren();
				retSize+=childrenNodes.size();
				for(UsageReportSQLModel childNode : childrenNodes){
					generateUsageReportAppColumnar(dataList,childNode,queryType);
					
					if(Constants.USAGE_TYPE_MILE.equals(queryType)){
						totalMileageVal += childNode.getTotal_mileage();
					}else if(Constants.USAGE_TYPE_FUEL.equals(queryType)){
						totalFuleConsVal +=childNode.getTotal_fuel_cons();
					}else if(Constants.USAGE_TYPE_TIME.equals(queryType)){
						totalDrivingTimeVal +=childNode.getTotal_driving_time();
					}
					
				}
			}
			//getTotal 不会累加根节点的数据
			if(Constants.USAGE_TYPE_MILE.equals(queryType)){
				if(self){
				 totalMileageVal += tree.getTotal_mileage();
				}
				reportModel.setDataAvgval(df.format((double)totalMileageVal/1000/retSize));//米转换为千米
			}else if(Constants.USAGE_TYPE_FUEL.equals(queryType)){
				if(self){
				  totalFuleConsVal += tree.getTotal_fuel_cons();
				}
				reportModel.setDataAvgval(df.format((double)totalFuleConsVal/retSize));//不需要转换
			}else if(Constants.USAGE_TYPE_TIME.equals(queryType)){
				if(self){
				  totalDrivingTimeVal +=tree.getTotal_driving_time();
				}
				reportModel.setDataAvgval(df.format((double)totalDrivingTimeVal/3600/retSize));//秒转换为小时
			}
			
			reportModel.setDataList(dataList);
		}
	}
	
	public void generateUsageReportAppColumnar(List<UsageReportAppColumnarItemModel> dataList,UsageReportSQLModel usageReportSQLModel,String queryType){
		UsageReportAppColumnarItemModel usageReportAppColumnarItemModel = new UsageReportAppColumnarItemModel();
		usageReportAppColumnarItemModel.setDeptId(usageReportSQLModel.getCurrentuse_org_id());
		usageReportAppColumnarItemModel.setDeptName(usageReportSQLModel.getCurrentuse_org_name());
		
		if(Constants.USAGE_TYPE_MILE.equals(queryType)){
			Integer totalMileage = usageReportSQLModel.getTotal_mileage();//米
			usageReportAppColumnarItemModel.setData(df.format((double)totalMileage/1000));//米转换为千米
		}else if(Constants.USAGE_TYPE_FUEL.equals(queryType)){
			Double totalFuleCons = usageReportSQLModel.getTotal_fuel_cons();//升
			usageReportAppColumnarItemModel.setData(df.format((double)totalFuleCons));//不需要转换
		}else if(Constants.USAGE_TYPE_TIME.equals(queryType)){
			Integer totalDrivingTime = usageReportSQLModel.getTotal_driving_time();//秒
			if(totalDrivingTime % 3600 == 0){
				usageReportAppColumnarItemModel.setData(String.valueOf(totalDrivingTime/3600));//秒转换为小时
			}else{
				usageReportAppColumnarItemModel.setData(df.format((double)totalDrivingTime/3600));//秒转换为小时
			}
		}
		dataList.add(usageReportAppColumnarItemModel);
	}

	@Override
	public List<String> getAllVehiclePropertyDataByDayRange(Date startDate, Date endDate, long entId,
			long deptId) {
		
		List<String> retList = new ArrayList<String>();
		
		List<UsageReportSQLModel> usageReportSQLModelList = null;
		
		if(deptId != 0l){//按部门查询
			usageReportSQLModelList = usageReportDao.getAllVehiclePropertyDataByDayRangeAndDeptAdmin(startDate,endDate,deptId);
		}else{//查询所有部门
			usageReportSQLModelList = usageReportDao.getAllVehiclePropertyDataByDayRangeAndEntAdmin(startDate,endDate,entId);
		}
		
		if(usageReportSQLModelList != null && usageReportSQLModelList.size() > 0){
			long durationDays = TimeUtils.timeBetween(startDate, endDate, Calendar.DATE);
			
			for(UsageReportSQLModel usageReportSQLModel : usageReportSQLModelList){
				StringBuffer buffer = new StringBuffer();
				UsageVehiclePropertyModel usageVehiclePropertyModel = new UsageVehiclePropertyModel();
				usageVehiclePropertyModel.setId(usageReportSQLModel.getId());
				usageVehiclePropertyModel.setVehicleNumber(usageReportSQLModel.getVehicle_number());
				usageVehiclePropertyModel.setVehicleBrand(usageReportSQLModel.getVehicle_brand());
				usageVehiclePropertyModel.setVehicleOutput(usageReportSQLModel.getVehicle_output());
				usageVehiclePropertyModel.setVehicleFuel(usageReportSQLModel.getVehicle_fuel());
				usageVehiclePropertyModel.setCurrentuseOrgName(usageReportSQLModel.getCurrentuse_org_name());
				usageVehiclePropertyModel.setTotalMileage(df.format((double)usageReportSQLModel.getTotal_mileage()/1000));
				usageVehiclePropertyModel.setTotalFuelcons(df.format((double)usageReportSQLModel.getTotal_fuel_cons()));
				if(usageReportSQLModel.getTotal_driving_time() % 3600 == 0){
					usageVehiclePropertyModel.setTotalDrivingtime(String.valueOf(usageReportSQLModel.getTotal_driving_time()/3600));
				}else{
					usageVehiclePropertyModel.setTotalDrivingtime(df.format((double)usageReportSQLModel.getTotal_driving_time()/3600));
				}
				//使用率
				usageVehiclePropertyModel.setUsagePercent(df.format((double)usageReportSQLModel.getTotal_driving_time()/(durationDays*8*3600)));//每天按8小时算
				usageVehiclePropertyModel.setAvgMileage(df.format((double)usageReportSQLModel.getTotal_mileage()/1000/durationDays));
				usageVehiclePropertyModel.setAvgFuelcons(df.format((double)usageReportSQLModel.getTotal_fuel_cons()/durationDays));
				usageVehiclePropertyModel.setAvgDrivingtime(df.format((double)usageReportSQLModel.getTotal_driving_time()/(durationDays*3600)));
				
				buffer.append(usageVehiclePropertyModel.getId()).append(",");
				buffer.append(usageVehiclePropertyModel.getVehicleNumber()).append(",");
				buffer.append(usageVehiclePropertyModel.getVehicleBrand()).append(",");
				buffer.append(usageVehiclePropertyModel.getVehicleOutput()).append(",");
				buffer.append(usageVehiclePropertyModel.getVehicleFuel()).append(",");
				buffer.append(usageVehiclePropertyModel.getCurrentuseOrgName()).append(",");
				buffer.append(usageVehiclePropertyModel.getTotalMileage()).append(",");
				buffer.append(usageVehiclePropertyModel.getTotalFuelcons()).append(",");
				buffer.append(usageVehiclePropertyModel.getTotalDrivingtime()).append(",");
				buffer.append(usageVehiclePropertyModel.getUsagePercent()).append(",");
				buffer.append(usageVehiclePropertyModel.getAvgMileage()).append(",");
				buffer.append(usageVehiclePropertyModel.getAvgFuelcons()).append(",");
				buffer.append(usageVehiclePropertyModel.getAvgDrivingtime());
				
				retList.add(buffer.toString());
			}
		}
		
		if(retList != null && retList.size() > 0){
			return retList;
		}
		return null;
	}
	
	
	private static List<UsageReportSQLModel> populateUsageReportSQLModelDays(Date date,int duration,Map<String, UsageReportSQLModel> actualMap){
		List<UsageReportSQLModel> retList = new ArrayList<UsageReportSQLModel>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	Calendar calendar = null;
        Date dateVal = null;
        
        for(int i = 0; i < duration; i ++){
        	calendar = Calendar.getInstance();
        	calendar.setTime(date);
        	calendar.add(Calendar.DATE, i);
        	dateVal =calendar.getTime();
        	
        	String day = sdf.format(dateVal);
        	if(actualMap.containsKey(day)){
        		retList.add(actualMap.get(day));
        	}else{
        		UsageReportSQLModel usageReportSQLModel = new UsageReportSQLModel();
            	usageReportSQLModel.setDay(day);
            	usageReportSQLModel.setTotal_mileage(0);
            	usageReportSQLModel.setTotal_fuel_cons(0.0);
            	retList.add(usageReportSQLModel);
        	}
        }
        return retList;
    }

	@Override
	public List<VehicleModel> findAllVehicleListByEntId(Long entId) {
		List<VehicleModel> vehicleList = usageReportDao.findAllVehicleListByEntId(entId);
		List<VehicleModel> inUsedVehicleList = filterInUsedVehicleList(vehicleList);
		return inUsedVehicleList;
	}

	@Override
	public List<VehicleModel> findAllVehicleListByDeptId(Long deptId) {
		List<VehicleModel> vehicleList = usageReportDao.findAllVehicleListByDeptId(deptId);
		List<VehicleModel> inUsedVehicleList = filterInUsedVehicleList(vehicleList);
		return inUsedVehicleList;
	}
	
	public List<VehicleModel> filterInUsedVehicleList(List<VehicleModel> vehicleList){
        List<VehicleModel> inUsedVehicleList = new ArrayList<VehicleModel>();
		
		if(vehicleList != null && vehicleList.size() > 0){
			List<String> deviceNumberList = new ArrayList<String>();
			for(VehicleModel vehicleModel : vehicleList){
			    //filter enable secret
			    if(vehicleModel.getEnableSecret() == 1){
			        continue;
			    }
			    
				deviceNumberList.add(vehicleModel.getDeviceNumber());
			}
			Map<String,String> imeiLicenseStatusMap = deviceService.findDeviceStatusList(deviceNumberList);
			
			if(imeiLicenseStatusMap != null && imeiLicenseStatusMap.size() > 0){
				for(VehicleModel currentModel : vehicleList){
					if(imeiLicenseStatusMap.get(currentModel.getDeviceNumber()) != null){
						inUsedVehicleList.add(currentModel);
					}
				}
			}
		}
		return inUsedVehicleList;
	}

	/**
	 * new old:getPieAndColumnarDataByDayRange
	 */
	@Override
	public UsageReportModel getPieAndColumnarData(Long orgId, Boolean selfDept, Boolean childDept, Date startDate,Date endDate) {
		
		String type = "";
			
		Organization organization = organizationService.findById(orgId);
		if(organization == null){
			return new UsageReportModel();
		}
		
		String orgName = organization.getName();//根节点name
		
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
	   
		UsageReportSQLModel rootUsageReportSQLModel = new UsageReportSQLModel();
		rootUsageReportSQLModel.setCurrentuse_org_id(orgId);
		rootUsageReportSQLModel.setCurrentuse_org_name(orgName);
		rootUsageReportSQLModel.setParent_id(0l);
		rootUsageReportSQLModel.setRootNode(true);
		
		List<Organization> organizationList = organizationService.findDownOrganizationListByOrgId(orgId,false,true);
		
		List<Long> orgIdList = new ArrayList<Long>();
		if(organizationList != null && organizationList.size() > 0){
			for(Organization org : organizationList){
				orgIdList.add(org.getId());
			}
		}
		
		if(orgIdList.size() > 0){
			//从组织表中获得所有子节点数据
			List<UsageReportSQLModel> orgValList = usageReportDao.findOrgList(orgIdList);
			if(orgValList != null && orgValList.size() > 0){
				//根节点下有子节点,则使用根节点和子节点数据构建树,如果没有,就只有一个根节点,则不构建树
				buildUsageReportTree(rootUsageReportSQLModel,orgValList);
			}
		}
		
		Map<Long,UsageReportSQLModel> usageReportMap = buildUsageReportMap(type,orgId,orgIdList,startDate,endDate);
		if(usageReportMap.size() > 0){
			populateUsageReportTree(rootUsageReportSQLModel,usageReportMap);
		}

		long durationDays = TimeUtils.timeBetween(startDate, endDate, Calendar.DATE);
		return processPieAndColumna(rootUsageReportSQLModel,selfDept,childDept,durationDays);
	}
	

	private void populateUsageReportTree(UsageReportSQLModel rootUsageReportSQLModel,
			Map<Long, UsageReportSQLModel> usageReportMap) {
		for(UsageReportSQLModel usageReportSQLModelModel : rootUsageReportSQLModel.getChildren()){
			Long tmpId = usageReportSQLModelModel.getCurrentuse_org_id();
			if(usageReportMap.containsKey(tmpId)){
				UsageReportSQLModel tmpUsageReportSQLModel = usageReportMap.get(tmpId);
				usageReportSQLModelModel.setTotal_mileage(tmpUsageReportSQLModel.getTotal_mileage());
				usageReportSQLModelModel.setTotal_fuel_cons(tmpUsageReportSQLModel.getTotal_fuel_cons());
				usageReportSQLModelModel.setTotal_driving_time(tmpUsageReportSQLModel.getTotal_driving_time());
				usageReportMap.remove(tmpId);
			}
			populateUsageReportTree(usageReportSQLModelModel,usageReportMap);
		}
		
		Long currentTmpId = rootUsageReportSQLModel.getCurrentuse_org_id();
		if(usageReportMap.containsKey(currentTmpId)){
			UsageReportSQLModel currentTmpUsageReportSQLModel = usageReportMap.get(currentTmpId);
			rootUsageReportSQLModel.setTotal_mileage(currentTmpUsageReportSQLModel.getTotal_mileage());
			rootUsageReportSQLModel.setTotal_fuel_cons(currentTmpUsageReportSQLModel.getTotal_fuel_cons());
			rootUsageReportSQLModel.setTotal_driving_time(currentTmpUsageReportSQLModel.getTotal_driving_time());
			usageReportMap.remove(currentTmpId);
		}
		
	}

	private Map<Long, UsageReportSQLModel> buildUsageReportMap(String type,Long orgId, List<Long> orgIdList, Date startDate,
			Date endDate) {
		
		Map<Long,UsageReportSQLModel> retMap = new HashMap<Long,UsageReportSQLModel>();
		
		List<UsageReportSQLModel> usageReportSQLModelList = new ArrayList<UsageReportSQLModel>();
		if("2".equals(type)){//部门及子部门
			usageReportSQLModelList = usageReportDao.getPieAndColumnarDataByDept(orgId,orgIdList,startDate,endDate);
		}else if("0".equals(type)){//企业
			usageReportSQLModelList = usageReportDao.getPieAndColumnarDataByEnt(orgId,orgIdList,startDate,endDate);
		}else if("1".equals(type)){//租车企业
			usageReportSQLModelList = usageReportDao.getPieAndColumnarDataByRent(orgId,orgIdList,startDate,endDate);
		}
		
		if(usageReportSQLModelList != null && usageReportSQLModelList.size() > 0){
			for(UsageReportSQLModel usageReportSQLModel : usageReportSQLModelList){
				Long currentuseOrgId = usageReportSQLModel.getCurrentuse_org_id();
				retMap.put(currentuseOrgId, usageReportSQLModel);
			}
		}
		return retMap;
	}

	/**
	 * 饼图与柱状图  new  old:processPieAndColumnarData
	 * @param usageReportSQLModelList
	 * @return
	 */
	private UsageReportModel processPieAndColumna(UsageReportSQLModel rootUsageReportSQLModel, Boolean selfDept, Boolean childDept,long durationDays) {
		UsageReportModel usageReportModel = new UsageReportModel();
		//饼状图
		Map<String,Object> sumMap = new HashMap<String,Object>();//统计三项指标的总的值,用于柱状图求平均值
		List<UsageReportPieModel> pieList = processPie(rootUsageReportSQLModel,selfDept,childDept,sumMap);
		usageReportModel.setPieList(pieList);
		//柱状图(基于饼状图，只是将数据进行平均)
		List<UsageReportColumnarModel> columnarList = processColumnarData(pieList,(Integer)sumMap.get("sumTotalMileage"),(Double)sumMap.get("sumTotalFuleCons"),(Integer)sumMap.get("sumTotalDrivingTime"),durationDays);
		usageReportModel.setColumnarList(columnarList);
		return usageReportModel;
	}
	
	/**
	 * 饼图 new old processPieData
	 * @param usageReportSQLModelList
	 * @return
	 */
	private List<UsageReportPieModel> processPie(UsageReportSQLModel rootUsageReportSQLModel,Boolean selfDept, Boolean childDept,Map<String,Object> sumMap) {
		List<UsageReportPieModel> retList = new ArrayList<UsageReportPieModel>();
		retList.add(processPieTotalMileage(rootUsageReportSQLModel,selfDept,childDept,sumMap));
		retList.add(processPieTotalFuleCons(rootUsageReportSQLModel,selfDept,childDept,sumMap));
		retList.add(processPieTotalDrivingTime(rootUsageReportSQLModel,selfDept,childDept,sumMap));
		return retList;
	}
	
	/**
	 * 总里程饼图 new old processPieTotalMileageData
	 * @param usageReportSQLModelList
	 * @return
	 */
	private UsageReportPieModel processPieTotalMileage(UsageReportSQLModel rootNode,Boolean selfDept, Boolean childDept,Map<String,Object> sumMap) {
		UsageReportPieModel totalMilagePie = new UsageReportPieModel();
		totalMilagePie.setStoreId("totalMilagePie");
		totalMilagePie.setStoreName("总里程");
		totalMilagePie.setUnit("千米");
		List<UsageReportPieItemModel> retListVal = new ArrayList<UsageReportPieItemModel>();
		Integer sumTotalMileage = 0;
		
		if(selfDept){
			UsageReportPieItemModel currentCake = new UsageReportPieItemModel();
			currentCake.setName(rootNode.getCurrentuse_org_name());
			Integer totalMileage = rootNode.getTotal_mileage();//米
			currentCake.setTmpMileageData(totalMileage);
			currentCake.setData(df.format((double)totalMileage/1000));//米转换为千米
			retListVal.add(currentCake);
		}
		
		if(childDept){
			List<UsageReportSQLModel> children = rootNode.getChildren();
			for(UsageReportSQLModel child : children){
				UsageReportPieItemModel childCake = new UsageReportPieItemModel();
				childCake.setName(child.getCurrentuse_org_name());
				Integer totalMileage = child.getTotal_mileage();//米
				childCake.setTmpMileageData(totalMileage);
				childCake.setData(df.format((double)totalMileage/1000));//米转换为千米
				retListVal.add(childCake);
			}
		}
		
		//top5
		List<UsageReportPieItemModel> retList = BeanSortUtils.sort(retListVal, "tmpMileageData", false,5);
		for(UsageReportPieItemModel tmpVal : retList){
			sumTotalMileage += tmpVal.getTmpMileageData();
		}
		
	    //计算百分比
		for(UsageReportPieItemModel usageReportPieItemModel : retList){
			
			if (sumTotalMileage == 0) {
				usageReportPieItemModel.setPrecent("0%");
			}else{
				usageReportPieItemModel.setPrecent(df.format((double)usageReportPieItemModel.getTmpMileageData()*100/sumTotalMileage) + "%");
			}
			
			usageReportPieItemModel.setTmpMileageData(0);
		}
		
		sumMap.put("sumTotalMileage", sumTotalMileage);//统计总的mileage,用于柱状图取平均值计算
		
		totalMilagePie.setDataList(retList);
		return totalMilagePie;
	}
	
	/**
	 * 总油耗量饼图 new old processPieTotalFuleConsData
	 * @param usageReportSQLModelList
	 * @return
	 */
	private UsageReportPieModel processPieTotalFuleCons(UsageReportSQLModel rootNode,Boolean selfDept, Boolean childDept,Map<String,Object> sumMap) {
		UsageReportPieModel totalFuleConsPie = new UsageReportPieModel();
		totalFuleConsPie.setStoreId("totalFuleConsPie");
		totalFuleConsPie.setStoreName("总耗油量");
		totalFuleConsPie.setUnit("升");
		List<UsageReportPieItemModel> retListVal = new ArrayList<UsageReportPieItemModel>();
		Double sumTotalFuleCons = 0.0;
		
		if(selfDept){
			UsageReportPieItemModel currentCake = new UsageReportPieItemModel();
			currentCake.setName(rootNode.getCurrentuse_org_name());
			Double totalFuleCons = rootNode.getTotal_fuel_cons();
			currentCake.setTmpFuleCons(totalFuleCons);
			currentCake.setData(df.format((double)totalFuleCons));
			retListVal.add(currentCake);
		}
		
		if(childDept){
			List<UsageReportSQLModel> children = rootNode.getChildren();
			for(UsageReportSQLModel child : children){
				UsageReportPieItemModel childCake = new UsageReportPieItemModel();
				childCake.setName(child.getCurrentuse_org_name());
				Double totalFuleCons = child.getTotal_fuel_cons();
				childCake.setTmpFuleCons(totalFuleCons);
				childCake.setData(df.format((double)totalFuleCons));
				retListVal.add(childCake);
			}
		}
		
		//top5
		List<UsageReportPieItemModel> retList = BeanSortUtils.sort(retListVal, "tmpFuleCons", false,5);
		for(UsageReportPieItemModel tmpVal : retList){
			sumTotalFuleCons += tmpVal.getTmpFuleCons();
		}
		
		//计算百分比
		for(UsageReportPieItemModel usageReportPieItemModel : retList){
			
			if(sumTotalFuleCons == 0.0){
				usageReportPieItemModel.setPrecent("0%");
			}else{
				usageReportPieItemModel.setPrecent(df.format((double)usageReportPieItemModel.getTmpFuleCons()*100/sumTotalFuleCons) + "%");
			}
			usageReportPieItemModel.setTmpFuleCons(0.0);
		}
		
		sumMap.put("sumTotalFuleCons", sumTotalFuleCons);//统计总的fulecons用于柱状体计算平均值
		
		totalFuleConsPie.setDataList(retList);
		return totalFuleConsPie;
	}
	
	/**
	 * 总行驶时长饼图 new old:processPieTotalDrivingTimeData
	 * @param usageReportSQLModelList
	 * @return
	 */
	private UsageReportPieModel processPieTotalDrivingTime(UsageReportSQLModel rootNode,Boolean selfDept, Boolean childDept,Map<String,Object> sumMap) {
		UsageReportPieModel totalDrivingTimeMilagePie = new UsageReportPieModel();
		totalDrivingTimeMilagePie.setStoreId("totalDrivingTimeMilagePie");
		totalDrivingTimeMilagePie.setStoreName("总行驶时长");
		totalDrivingTimeMilagePie.setUnit("小时");
		List<UsageReportPieItemModel> retListVal = new ArrayList<UsageReportPieItemModel>();
		Integer sumTotalDrivingTime = 0;
		
		if(selfDept){
			UsageReportPieItemModel currentCake = new UsageReportPieItemModel();
			currentCake.setName(rootNode.getCurrentuse_org_name());
			Integer totalDrivingTime = rootNode.getTotal_driving_time();
			currentCake.setTmpDrivingTimeData(totalDrivingTime);
			if(totalDrivingTime % 3600 == 0){
				currentCake.setData(String.valueOf(totalDrivingTime/3600));
			}else{
				currentCake.setData(df.format((double)totalDrivingTime/3600));
			}
			retListVal.add(currentCake);
		}
		
		if(childDept){
			List<UsageReportSQLModel> children = rootNode.getChildren();
			for(UsageReportSQLModel child : children){
				UsageReportPieItemModel childCake = new UsageReportPieItemModel();
				childCake.setName(child.getCurrentuse_org_name());
				Integer totalDrivingTime = child.getTotal_driving_time();
				childCake.setTmpDrivingTimeData(totalDrivingTime);
				if(totalDrivingTime % 3600 == 0){
					childCake.setData(String.valueOf(totalDrivingTime/3600));
				}else{
					childCake.setData(df.format((double)totalDrivingTime/3600));
				}
				retListVal.add(childCake);
			}
		}
		
		//top5
		List<UsageReportPieItemModel> retList = BeanSortUtils.sort(retListVal, "tmpDrivingTimeData", false,5);
		for(UsageReportPieItemModel tmpVal : retList){
			sumTotalDrivingTime += tmpVal.getTmpDrivingTimeData();
		}
		
		//计算百分比
		for(UsageReportPieItemModel usageReportPieItemModel : retList){
			
			if(sumTotalDrivingTime == 0){
				usageReportPieItemModel.setPrecent("0%");
			}else{
				usageReportPieItemModel.setPrecent(df.format((double)usageReportPieItemModel.getTmpDrivingTimeData()*100/sumTotalDrivingTime) + "%");
			}
			usageReportPieItemModel.setTmpDrivingTimeData(0);
		}
		
		sumMap.put("sumTotalDrivingTime", sumTotalDrivingTime);//统计总的dringtime用于柱状图计算平均值

		totalDrivingTimeMilagePie.setDataList(retList);
		return totalDrivingTimeMilagePie;
	}
	
	/**
	 * 构建树
	 * @param node
	 * @param organizations
	 */
	private void buildUsageReportTree(UsageReportSQLModel rootNode, List<UsageReportSQLModel> usageReportSQLModelList)
    {
		UsageReportSQLModel currentNode = null;
        for (UsageReportSQLModel usageReportSQLModel : usageReportSQLModelList)
        {
            if (usageReportSQLModel.getParent_id().longValue() == rootNode.getCurrentuse_org_id().longValue())
            {
            	currentNode = usageReportSQLModel;
            	rootNode.getChildren().add(currentNode);
            	buildUsageReportTree(currentNode, usageReportSQLModelList);
            }
        }
    }

	//new old:getVehiclePropertyDataByDayRangeAndEntAdmin
	@Override
	public PagModel getVehiclePropertyData(Date startDate, Date endDate, Long orgId, Boolean selfDept,
			Boolean childDept, int currentPage, int numPerPage) {
		PagModel pageModel = new PagModel();
		List<UsageVehiclePropertyModel> resultList = new ArrayList<UsageVehiclePropertyModel>();
		
		String type = "";
		
		Organization organization = organizationService.findById(orgId);
		if(organization == null){
			return pageModel;
		}
		
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
		
		List<Organization> organizationList = organizationService.findDownOrganizationListByOrgId(orgId,false,true);
		
		List<Long> orgIdList = new ArrayList<Long>();
		if(organizationList != null && organizationList.size() > 0){
			for(Organization org : organizationList){
				orgIdList.add(org.getId());
			}
		}
		
		if("2".equals(type)){//部门及子部门
			pageModel = usageReportDao.getVehiclePropertyDataDept(selfDept,childDept,orgId,orgIdList,startDate,endDate,currentPage,numPerPage);
		}else if("0".equals(type)){//企业
			pageModel = usageReportDao.getVehiclePropertyDataByEnt(selfDept,childDept,orgId,orgIdList,startDate,endDate,currentPage,numPerPage);
		}else if("1".equals(type)){//租车企业
			pageModel = usageReportDao.getVehiclePropertyDataByRent(selfDept,childDept,orgId,orgIdList,startDate,endDate,currentPage,numPerPage);
		}
		
		if(pageModel != null){
			List<UsageReportSQLModel> retList = pageModel.getResultList();
			long durationDays = TimeUtils.timeBetween(startDate, endDate, Calendar.DATE);
			
			for(UsageReportSQLModel usageReportSQLModel : retList){
				UsageVehiclePropertyModel usageVehiclePropertyModel = new UsageVehiclePropertyModel();
				usageVehiclePropertyModel.setId(usageReportSQLModel.getId());
				usageVehiclePropertyModel.setVehicleNumber(usageReportSQLModel.getVehicle_number());
				usageVehiclePropertyModel.setVehicleBrand(usageReportSQLModel.getVehicle_brand());
				usageVehiclePropertyModel.setVehicleOutput(usageReportSQLModel.getVehicle_output());
				usageVehiclePropertyModel.setVehicleFuel(usageReportSQLModel.getVehicle_fuel());
				usageVehiclePropertyModel.setCurrentuseOrgName(usageReportSQLModel.getCurrentuse_org_name());
				usageVehiclePropertyModel.setTotalMileage(df.format((double)usageReportSQLModel.getTotal_mileage()/1000));
				usageVehiclePropertyModel.setTotalFuelcons(df.format((double)usageReportSQLModel.getTotal_fuel_cons()));
				if(usageReportSQLModel.getTotal_driving_time() % 3600 == 0){
					usageVehiclePropertyModel.setTotalDrivingtime(String.valueOf(usageReportSQLModel.getTotal_driving_time()/3600));
				}else{
					usageVehiclePropertyModel.setTotalDrivingtime(df.format((double)usageReportSQLModel.getTotal_driving_time()/3600));
				}
				//使用率
				usageVehiclePropertyModel.setUsagePercent(df.format((double)usageReportSQLModel.getTotal_driving_time()/(durationDays*8*3600)));//每天按8小时算
				usageVehiclePropertyModel.setAvgMileage(df.format((double)usageReportSQLModel.getTotal_mileage()/1000/durationDays));
				usageVehiclePropertyModel.setAvgFuelcons(df.format((double)usageReportSQLModel.getTotal_fuel_cons()/durationDays));
				usageVehiclePropertyModel.setAvgDrivingtime(df.format((double)usageReportSQLModel.getTotal_driving_time()/(durationDays*3600)));
				resultList.add(usageVehiclePropertyModel);
			}
			pageModel.setResultList(resultList);
		}
		return pageModel;
	}

	//new old:getVehicleLinePropertyDataByDayRange
	@Override
	public UsageReportLineModel getVehicleLinePropertyData(Date startDate, Date endDate, Long orgId, Boolean selfDept,
			Boolean childDept) {
		UsageReportLineModel usageReportLineModel = new UsageReportLineModel();
		
		usageReportLineModel.setData1unit("里程(千米)");
		usageReportLineModel.setData2unit("耗油量(升)");
		usageReportLineModel.setData1avgval("0");
		usageReportLineModel.setData2avgval("0");
		
        String type = "";
		
		Organization organization = organizationService.findById(orgId);
		if(organization == null){
			return usageReportLineModel;
		}
		
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
		
		List<Organization> organizationList = organizationService.findDownOrganizationListByOrgId(orgId,false,true);
		
		List<Long> orgIdList = new ArrayList<Long>();
		if(organizationList != null && organizationList.size() > 0){
			for(Organization org : organizationList){
				orgIdList.add(org.getId());
			}
		}
		
		//days
		List<UsageReportSQLModel> actualList = new ArrayList<UsageReportSQLModel>();
				
		if("2".equals(type)){//部门及子部门
			actualList = usageReportDao.getVehicleLinePropertyDataByDept(selfDept,childDept,orgId,orgIdList,startDate,endDate);
		}else if("0".equals(type)){//企业
			actualList = usageReportDao.getVehicleLinePropertyDataByEnt(selfDept,childDept,orgId,orgIdList,startDate,endDate);
		}else if("1".equals(type)){//租车企业
			actualList = usageReportDao.getVehicleLinePropertyDataByRent(selfDept,childDept,orgId,orgIdList,startDate,endDate);
		}		
		
		if(actualList != null && actualList.size() > 0){
			
			Map<String, UsageReportSQLModel> actualMap = new HashMap<String, UsageReportSQLModel>();//实际存在日期的值
			for(UsageReportSQLModel usageReportSQLModel : actualList){
				actualMap.put(usageReportSQLModel.getDay(), usageReportSQLModel);
			}
			
			long durationDays = TimeUtils.timeBetween(startDate, endDate, Calendar.DATE);
			
			//补齐日期
			List<UsageReportSQLModel> retList = populateUsageReportSQLModelDays(startDate,Integer.valueOf(String.valueOf(durationDays)),actualMap);
			
			if(retList != null && retList.size() > 0){
				int totalData1 = 0;
				double totalData2 = 0;
				int retSize = retList.size();
				
				List<UsageReportLineItemModel> dataList = new ArrayList<UsageReportLineItemModel>();
				for(UsageReportSQLModel usageReportSQLModel : retList){
					UsageReportLineItemModel usageReportLineItemModel = new UsageReportLineItemModel();
					usageReportLineItemModel.setDay(usageReportSQLModel.getDay());
					totalData1 += usageReportSQLModel.getTotal_mileage();
					usageReportLineItemModel.setData1(df.format((double)usageReportSQLModel.getTotal_mileage()/1000));
					totalData2 += usageReportSQLModel.getTotal_fuel_cons();
					usageReportLineItemModel.setData2(df.format((double)usageReportSQLModel.getTotal_fuel_cons()));
					dataList.add(usageReportLineItemModel);
				}
				
				usageReportLineModel.setData1avgval(df.format((double)totalData1/(retSize*1000)));
				usageReportLineModel.setData2avgval(df.format((double)totalData2/retSize));
				
				usageReportLineModel.setDataList(dataList);
			}
		}
		return usageReportLineModel;
	}

	@Override
	public List<String> getAllVehiclePropertyData(Date startDate, Date endDate, Long orgId, Boolean selfDept,
			Boolean childDept) {
		List<String> retList = new ArrayList<String>();
		
        String type = "";
		
		Organization organization = organizationService.findById(orgId);
		if(organization == null){
			return retList;
		}
		
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
		
		List<Organization> organizationList = organizationService.findDownOrganizationListByOrgId(orgId,false,true);
		
		List<Long> orgIdList = new ArrayList<Long>();
		if(organizationList != null && organizationList.size() > 0){
			for(Organization org : organizationList){
				orgIdList.add(org.getId());
			}
		}
		
		List<UsageReportSQLModel> usageReportSQLModelList = new ArrayList<UsageReportSQLModel>();
		
		if("2".equals(type)){//部门及子部门
			usageReportSQLModelList = usageReportDao.getAllVehiclePropertyDataByDept(selfDept,childDept,orgId,orgIdList,startDate,endDate);
		}else if("0".equals(type)){//企业
			usageReportSQLModelList = usageReportDao.getAllVehiclePropertyDataByEnt(selfDept,childDept,orgId,orgIdList,startDate,endDate);
		}else if("1".equals(type)){//租车企业
			usageReportSQLModelList = usageReportDao.getAllVehiclePropertyDataByRent(selfDept,childDept,orgId,orgIdList,startDate,endDate);
		}
		
		if(usageReportSQLModelList != null && usageReportSQLModelList.size() > 0){
			long durationDays = TimeUtils.timeBetween(startDate, endDate, Calendar.DATE);
			
			for(UsageReportSQLModel usageReportSQLModel : usageReportSQLModelList){
				StringBuffer buffer = new StringBuffer();
				UsageVehiclePropertyModel usageVehiclePropertyModel = new UsageVehiclePropertyModel();
				usageVehiclePropertyModel.setId(usageReportSQLModel.getId());
				usageVehiclePropertyModel.setVehicleNumber(usageReportSQLModel.getVehicle_number());
				usageVehiclePropertyModel.setVehicleBrand(usageReportSQLModel.getVehicle_brand());
				usageVehiclePropertyModel.setVehicleOutput(usageReportSQLModel.getVehicle_output());
				usageVehiclePropertyModel.setVehicleFuel(usageReportSQLModel.getVehicle_fuel());
				usageVehiclePropertyModel.setCurrentuseOrgName(usageReportSQLModel.getCurrentuse_org_name());
				usageVehiclePropertyModel.setTotalMileage(df.format((double)usageReportSQLModel.getTotal_mileage()/1000));
				usageVehiclePropertyModel.setTotalFuelcons(df.format((double)usageReportSQLModel.getTotal_fuel_cons()));
				if(usageReportSQLModel.getTotal_driving_time() % 3600 == 0){
					usageVehiclePropertyModel.setTotalDrivingtime(String.valueOf(usageReportSQLModel.getTotal_driving_time()/3600));
				}else{
					usageVehiclePropertyModel.setTotalDrivingtime(df.format((double)usageReportSQLModel.getTotal_driving_time()/3600));
				}
				//使用率
				usageVehiclePropertyModel.setUsagePercent(df.format((double)usageReportSQLModel.getTotal_driving_time()/(durationDays*8*3600)));//每天按8小时算
				usageVehiclePropertyModel.setAvgMileage(df.format((double)usageReportSQLModel.getTotal_mileage()/1000/durationDays));
				usageVehiclePropertyModel.setAvgFuelcons(df.format((double)usageReportSQLModel.getTotal_fuel_cons()/durationDays));
				usageVehiclePropertyModel.setAvgDrivingtime(df.format((double)usageReportSQLModel.getTotal_driving_time()/(durationDays*3600)));
				
				buffer.append(usageVehiclePropertyModel.getId()).append(",");
				buffer.append(usageVehiclePropertyModel.getVehicleNumber()).append(",");
				buffer.append(usageVehiclePropertyModel.getVehicleBrand()).append(",");
				buffer.append(usageVehiclePropertyModel.getVehicleOutput()).append(",");
				buffer.append(usageVehiclePropertyModel.getVehicleFuel()).append(",");
				buffer.append(usageVehiclePropertyModel.getCurrentuseOrgName()).append(",");
				buffer.append(usageVehiclePropertyModel.getTotalMileage()).append(",");
				buffer.append(usageVehiclePropertyModel.getTotalFuelcons()).append(",");
				buffer.append(usageVehiclePropertyModel.getTotalDrivingtime()).append(",");
				buffer.append(usageVehiclePropertyModel.getUsagePercent()).append(",");
				buffer.append(usageVehiclePropertyModel.getAvgMileage()).append(",");
				buffer.append(usageVehiclePropertyModel.getAvgFuelcons()).append(",");
				buffer.append(usageVehiclePropertyModel.getAvgDrivingtime());
				
				retList.add(buffer.toString());
			}
		}
		
		if(retList != null && retList.size() > 0){
			return retList;
		}
		return null;
	}

	//new old:getIdleVehicleListByDayRange
	@Override
	public PagModel getIdleVehicleList(Date startDate, Date endDate, Long orgId, Boolean selfDept, Boolean childDept,
			int currentPage, int numPerPage) {
		PagModel pageModel = new PagModel();
		List<UsageIdleVehicleModel> resultList = new ArrayList<UsageIdleVehicleModel>();
		
		String type = "";
		
		Organization organization = organizationService.findById(orgId);
		if(organization == null){
			return pageModel;
		}
		
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
		
		List<Organization> organizationList = organizationService.findDownOrganizationListByOrgId(orgId,false,true);
		
		List<Long> orgIdList = new ArrayList<Long>();
		if(organizationList != null && organizationList.size() > 0){
			for(Organization org : organizationList){
				orgIdList.add(org.getId());
			}
		}
		
		if("2".equals(type)){//部门及子部门
			pageModel = usageReportDao.getIdleVehicleListByDept(selfDept,childDept,orgId,orgIdList,startDate,endDate,currentPage,numPerPage);
		}else if("0".equals(type)){//企业
			pageModel = usageReportDao.getIdleVehicleListByEnt(selfDept,childDept,orgId,orgIdList,startDate,endDate,currentPage,numPerPage);
		}else if("1".equals(type)){//租车企业
			pageModel = usageReportDao.getIdleVehicleListByRent(selfDept,childDept,orgId,orgIdList,startDate,endDate,currentPage,numPerPage);
		}
		
		if(pageModel != null){
			List<UsageReportSQLModel> retList = pageModel.getResultList();
			
			String starttime = DateUtils.date2String(startDate, "yyyy-MM-dd");
			String enttime = DateUtils.date2String(endDate, "yyyy-MM-dd");
			String idelInfo = starttime+"到"+enttime;
		
			for(UsageReportSQLModel usageReportSQLModel : retList){
				UsageIdleVehicleModel usageIdleVehicleModel = new UsageIdleVehicleModel();
				usageIdleVehicleModel.setId(usageReportSQLModel.getId());
				usageIdleVehicleModel.setVehicleNumber(usageReportSQLModel.getVehicle_number());
				usageIdleVehicleModel.setVehicleBrand(usageReportSQLModel.getVehicle_brand());
				usageIdleVehicleModel.setVehicleModel(usageReportSQLModel.getVehicle_model());
				usageIdleVehicleModel.setCurrentuseOrgName(usageReportSQLModel.getCurrentuse_org_name());
				usageIdleVehicleModel.setVehiclePurpose(usageReportSQLModel.getVehicle_purpose());
				usageIdleVehicleModel.setIdelInfo(idelInfo);
				resultList.add(usageIdleVehicleModel);
			}
			pageModel.setResultList(resultList);
		}
		
		return pageModel;
	}

	//new old:getIdleVehicleListByDayRangeALL
	@Override
	public List<String> getAllIdleVehicleList(Date startDate, Date endDate, Long orgId, Boolean selfDept,
			Boolean childDept) {
		    List<String> resultList = new ArrayList<String>();
		
		    String type = "";
			
			Organization organization = organizationService.findById(orgId);
			if(organization == null){
				return resultList;
			}
			
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
			
			List<Organization> organizationList = organizationService.findDownOrganizationListByOrgId(orgId,false,true);
			
			List<Long> orgIdList = new ArrayList<Long>();
			if(organizationList != null && organizationList.size() > 0){
				for(Organization org : organizationList){
					orgIdList.add(org.getId());
				}
			}
			
			List<UsageReportSQLModel> searchList = new ArrayList<UsageReportSQLModel>();
			
			if("2".equals(type)){//部门及子部门
				searchList = usageReportDao.getAllIdleVehicleListByDept(selfDept,childDept,orgId,orgIdList,startDate,endDate);
			}else if("0".equals(type)){
				searchList = usageReportDao.getAllIdleVehicleListByEnt(selfDept,childDept,orgId,orgIdList,startDate,endDate);
			}else if("1".equals(type)){
				searchList = usageReportDao.getAllIdleVehicleListByRent(selfDept,childDept,orgId,orgIdList,startDate,endDate);
			}
					
			if(searchList != null && searchList.size() > 0){
				
				String starttime = DateUtils.date2String(startDate, "yyyy-MM-dd");
				String enttime = DateUtils.date2String(endDate, "yyyy-MM-dd");
				String idelInfo = starttime+"到"+enttime;
				
				for(UsageReportSQLModel usageReportSQLModel : searchList){
					String usageIdleVehicle = usageReportSQLModel.getVehicle_number() + ',';
					usageIdleVehicle += usageReportSQLModel.getVehicle_brand() + ',';
					usageIdleVehicle += usageReportSQLModel.getVehicle_model() + ',';
					usageIdleVehicle += usageReportSQLModel.getCurrentuse_org_name() + ',';
					usageIdleVehicle += usageReportSQLModel.getVehicle_purpose() + ',';
					usageIdleVehicle += idelInfo;

					resultList.add(usageIdleVehicle);
				}
			}
		
		return resultList;
	}

	//new old:findAllVehicleListByDeptId
	@Override
	public List<VehicleModel> findAllVehicleListByOrgId(Long orgId) {
         String type = "";
		
		Organization organization = organizationService.findById(orgId);
		if(organization == null){
			return null;
		}
		
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
		
		List<Organization> organizationList = organizationService.findDownOrganizationListByOrgId(orgId,false,true);
		
		List<Long> orgIdList = new ArrayList<Long>();
		if(organizationList != null && organizationList.size() > 0){
			for(Organization org : organizationList){
				orgIdList.add(org.getId());
			}
		}
		
		List<VehicleModel> vehicleList = new ArrayList<VehicleModel>();
		
		if("2".equals(type)){//部门及子部门
			vehicleList = usageReportDao.findAllVehicleListByDept(orgId,orgIdList);
		}else if("0".equals(type)){//企业
			vehicleList = usageReportDao.findAllVehicleListByEnt(orgId,orgIdList);
		}else if("1".equals(type)){//租车企业
			vehicleList = usageReportDao.findAllVehicleListByRent(orgId,orgIdList);
		}
		
		List<VehicleModel> inUsedVehicleList = filterInUsedVehicleList(vehicleList);
		return inUsedVehicleList;
	}

	@Override
	public boolean getPrivilegeFlag(VehicleModel vehicleModel, Long orgId) {
		
		boolean privilegeFlag = false;
		
		String type = "";
		Organization organization = organizationService.findById(orgId);
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
		
		List<Organization> organizationList = organizationService.findDownOrganizationListByOrgId(orgId,false,true);
		List<Long> orgIdList = new ArrayList<Long>();
		if(organizationList != null && organizationList.size() > 0){
			for(Organization org : organizationList){
				orgIdList.add(org.getId());
			}
		}
		
		if("2".equals(type)){//部门及子部门
			
			if(vehicleModel.getCurrentuseOrgId() == null){
				privilegeFlag = false;
				return privilegeFlag;
			}
			
			long currentuseOrgId = vehicleModel.getCurrentuseOrgId().longValue();
			
			List<Long> realOrgIdList = new ArrayList<Long>();
			realOrgIdList.add(orgId);
			
			if(orgIdList != null && orgIdList.size() > 0){
				realOrgIdList.addAll(orgIdList);
			}
			
			for(Long vehicleId : realOrgIdList){
				if(vehicleId.longValue() == currentuseOrgId){
					privilegeFlag = true;
					return privilegeFlag;
				}
			}
		}else if("0".equals(type)){//企业
			
			if(orgId.longValue() == vehicleModel.getEntId().longValue()){
				privilegeFlag = true;
				return privilegeFlag;
			}
			
			if(vehicleModel.getCurrentuseOrgId() == null){
				privilegeFlag = false;
				return privilegeFlag;
			}
			
			long currentuseOrgId = vehicleModel.getCurrentuseOrgId().longValue();
			
			if(orgIdList != null && orgIdList.size() > 0){
				for(Long vehicleId : orgIdList){
					if(vehicleId.longValue() == currentuseOrgId){
						privilegeFlag = true;
						return privilegeFlag;
					}
				}
			}
		}else if("1".equals(type)){//租车企业
			if(orgId.longValue() == vehicleModel.getEntId().longValue()){
				privilegeFlag = true;
				return privilegeFlag;
			}
			
			if(vehicleModel.getCurrentuseOrgId() == null){
				privilegeFlag = false;
				return privilegeFlag;
			}
			
			long currentuseOrgId = vehicleModel.getCurrentuseOrgId().longValue();
			orgIdList.add(orgId);
			
			for(Long vehicleId : orgIdList){
				if(vehicleId.longValue() == currentuseOrgId){
					privilegeFlag = true;
					return privilegeFlag;
				}
			}
		}
		
		return privilegeFlag;
	}
	
}
