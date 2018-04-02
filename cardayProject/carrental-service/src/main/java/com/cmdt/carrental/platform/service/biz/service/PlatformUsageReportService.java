package com.cmdt.carrental.platform.service.biz.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.UsageReportLineModel;
import com.cmdt.carrental.common.model.UsageReportModel;
import com.cmdt.carrental.common.service.UsageReportService;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.model.request.usageReport.UsageReportChartDto;


@Service
public class PlatformUsageReportService {

	private static final Logger LOG = LoggerFactory.getLogger(PlatformUsageReportService.class);
	
	@Autowired
	UsageReportService usageReportService;

	/**
	 * 车辆使用统计: 饼状图，柱状图
	 * @param usageReportChartDto
	 * @return
	 */
	public UsageReportModel getPieAndColumnarData(UsageReportChartDto usageReportChartDto){
		Long orgId = usageReportChartDto.getOrgId();
		Boolean selfDept = usageReportChartDto.getSelfDept();
		Boolean childDept = usageReportChartDto.getChildDept();
		Date starttime = usageReportChartDto.getStarttime();
		Date endtime = usageReportChartDto.getEndtime();
		if(starttime==null || endtime==null){
			//如果不输入时间，取最近一个月时间作为开始和结束时间
			Date endDt = new Date();
	    	Date startDt = DateUtils.addDays(endDt,-29);
	    	String starttimeVal = DateUtils.date2String(startDt,Constants.API_DATE_YYYY_MM_DD);
	    	String endtimeVal = DateUtils.date2String(endDt,Constants.API_DATE_YYYY_MM_DD);
	    	//按输入日期区间进行统计
	    	starttime = DateUtils.string2Date(starttimeVal + Constants.API_TIME_MIN,Constants.API_DATE_YYYY_MM_DD_HH24_MI_SS);
	    	endtime = DateUtils.string2Date(endtimeVal + Constants.API_TIME_MAX,Constants.API_DATE_YYYY_MM_DD_HH24_MI_SS);
		}
		return  usageReportService.getPieAndColumnarData(orgId,selfDept,childDept,starttime,endtime);
	}
	
	/**
	 * 车辆使用统计:曲线图
	 * @param usageReportChartDto
	 * @return
	 */
	public UsageReportLineModel getVehicleLinePropertyData(UsageReportChartDto usageReportChartDto){
		Long orgId = usageReportChartDto.getOrgId();
		Boolean selfDept = usageReportChartDto.getSelfDept();
		Boolean childDept = usageReportChartDto.getChildDept();
		Date starttime = usageReportChartDto.getStarttime();
		Date endtime = usageReportChartDto.getEndtime();
		if(starttime==null || endtime==null){
			//如果不输入时间，取最近一个月时间作为开始和结束时间
			Date endDt = new Date();
	    	Date startDt = DateUtils.addDays(endDt,-29);
	    	String starttimeVal = DateUtils.date2String(startDt,Constants.API_DATE_YYYY_MM_DD);
	    	String endtimeVal = DateUtils.date2String(endDt,Constants.API_DATE_YYYY_MM_DD);
	    	//按输入日期区间进行统计
	    	starttime = DateUtils.string2Date(starttimeVal + Constants.API_TIME_MIN,Constants.API_DATE_YYYY_MM_DD_HH24_MI_SS);
	    	endtime = DateUtils.string2Date(endtimeVal + Constants.API_TIME_MAX,Constants.API_DATE_YYYY_MM_DD_HH24_MI_SS);
		}
		return  usageReportService.getVehicleLinePropertyData(starttime,endtime,orgId,selfDept,childDept);
	}
	
	/**
	 * 车辆使用统计:每辆车的总里程，总油耗，总行驶时长以及平均值,支持分页
	 * @param usageReportChartDto
	 * @return
	 */
	public PagModel getVehiclePropertyData(UsageReportChartDto usageReportChartDto){
		Long orgId = usageReportChartDto.getOrgId();
		Boolean selfDept = usageReportChartDto.getSelfDept();
		Boolean childDept = usageReportChartDto.getChildDept();
		Date starttime = usageReportChartDto.getStarttime();
		Date endtime = usageReportChartDto.getEndtime();
		if(starttime==null || endtime==null){
			//如果不输入时间，取最近一个月时间作为开始和结束时间
			Date endDt = new Date();
	    	Date startDt = DateUtils.addDays(endDt,-29);
	    	String starttimeVal = DateUtils.date2String(startDt,Constants.API_DATE_YYYY_MM_DD);
	    	String endtimeVal = DateUtils.date2String(endDt,Constants.API_DATE_YYYY_MM_DD);
	    	//按输入日期区间进行统计
	    	starttime = DateUtils.string2Date(starttimeVal + Constants.API_TIME_MIN,Constants.API_DATE_YYYY_MM_DD_HH24_MI_SS);
	    	endtime = DateUtils.string2Date(endtimeVal + Constants.API_TIME_MAX,Constants.API_DATE_YYYY_MM_DD_HH24_MI_SS);
		}
		
		int currentPage = 1;
    	int numPerPage = 10;
		
		if(usageReportChartDto.getCurrentPage() != null){
    		currentPage = usageReportChartDto.getCurrentPage();
    	}
    	if(usageReportChartDto.getNumPerPage() != null){
    		numPerPage = usageReportChartDto.getNumPerPage();
    	}
		return  usageReportService.getVehiclePropertyData(starttime,endtime,orgId,selfDept,childDept,currentPage,numPerPage);
	}
	
	/**
	 * 未使用车辆统计
	 * @param usageReportChartDto
	 * @return
	 */
	public PagModel getIdleVehicleList(UsageReportChartDto usageReportChartDto){
		Long orgId = usageReportChartDto.getOrgId();
		Boolean selfDept = usageReportChartDto.getSelfDept();
		Boolean childDept = usageReportChartDto.getChildDept();
		Date starttime = usageReportChartDto.getStarttime();
		Date endtime = usageReportChartDto.getEndtime();
		if(starttime==null || endtime==null){
			//如果不输入时间，取最近一个月时间作为开始和结束时间
			Date endDt = new Date();
	    	Date startDt = DateUtils.addDays(endDt,-29);
	    	String starttimeVal = DateUtils.date2String(startDt,Constants.API_DATE_YYYY_MM_DD);
	    	String endtimeVal = DateUtils.date2String(endDt,Constants.API_DATE_YYYY_MM_DD);
	    	//按输入日期区间进行统计
	    	starttime = DateUtils.string2Date(starttimeVal + Constants.API_TIME_MIN,Constants.API_DATE_YYYY_MM_DD_HH24_MI_SS);
	    	endtime = DateUtils.string2Date(endtimeVal + Constants.API_TIME_MAX,Constants.API_DATE_YYYY_MM_DD_HH24_MI_SS);
		}
		
		int currentPage = 1;
    	int numPerPage = 10;
		
		if(usageReportChartDto.getCurrentPage() != null){
    		currentPage = usageReportChartDto.getCurrentPage();
    	}
    	if(usageReportChartDto.getNumPerPage() != null){
    		numPerPage = usageReportChartDto.getNumPerPage();
    	}
		return  usageReportService.getIdleVehicleList(starttime,endtime,orgId,selfDept,childDept,currentPage,numPerPage);
	}
	
}
