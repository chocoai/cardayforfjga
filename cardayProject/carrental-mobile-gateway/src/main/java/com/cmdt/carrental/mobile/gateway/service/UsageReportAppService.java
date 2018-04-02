package com.cmdt.carrental.mobile.gateway.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.UsageReportAppColumnarModel;
import com.cmdt.carrental.common.model.UsageReportAppLineModel;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.UsageReportService;
import com.cmdt.carrental.common.util.DateUtils;
import com.cmdt.carrental.mobile.gateway.model.QueryDate;
import com.cmdt.carrental.mobile.gateway.model.UsageReportVehiclePropertyQueryDto;
import com.cmdt.carrental.mobile.gateway.model.request.usage.UsageDepartmentReportDto;

@Service
public class UsageReportAppService {
	
	@Autowired
	private UsageReportService usageReportService;
	
    @Autowired
    private OrganizationService organizationService;
	/**
	 * APP 用车情况(list展示)
	 * @param dto
	 * @return
	 */
	public PagModel getVehiclePropertyDataByDayRangeAndDeptId(UsageReportVehiclePropertyQueryDto dto) {
		String starttime = dto.getStarttime();
		String endtime = dto.getEndtime();
		Date startDate = DateUtils.string2Date(starttime + " "+DateUtils.FORMAT_TIME_HH_MI_SS_START,DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
		Date endDate = DateUtils.string2Date(endtime + " "+DateUtils.FORMAT_TIME_HH_MI_SS_END,DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
		int currentPage = 1;
    	int numPerPage = 10;
    	
    	if(dto.getCurrentPage() != null){
    		currentPage = dto.getCurrentPage().intValue();
    	}
    	
    	if(dto.getNumPerPage() != null){
    		numPerPage = dto.getNumPerPage().intValue();
    	}
    	return usageReportService.getVehiclePropertyData(startDate,endDate,dto.getOrgId(),dto.getSelfDept(),dto.getChildDept(),currentPage,numPerPage);
		
	}
	
	/**
	 * 整体走势图(曲线图展示)
	 */
	public UsageReportAppLineModel getTendencyChartByDayRange(UsageDepartmentReportDto dto) {
		String starttime = dto.getStartTime();
		String endtime = dto.getEndTime();
		String queryType = dto.getQueryType(); //0:mile 1:fuel 2:time
		
		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
			//如果不输入时间，取最近一个月时间作为开始和结束时间
			Date endDate = new Date();
	    	Date startDate = DateUtils.addDays(endDate,-29);
	    	starttime = DateUtils.date2String(startDate,DateUtils.FORMAT_YYYY_MM_DD);
	    	endtime = DateUtils.date2String(endDate,DateUtils.FORMAT_YYYY_MM_DD);
		}
		
		//按输入日期区间进行统计
		Date startDate = DateUtils.string2Date(starttime + " "+DateUtils.FORMAT_TIME_HH_MI_SS_START,DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
		Date endDate = DateUtils.string2Date(endtime + " "+DateUtils.FORMAT_TIME_HH_MI_SS_END,DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
		

		return usageReportService.getTendencyChartByDayRangeAndType(startDate,endDate,queryType,dto.getOrgId(),dto.getSelfDept(),dto.getChildDept());

	}

	public UsageReportAppColumnarModel getDepartmentColumnarChartByDayRange(UsageDepartmentReportDto dto) {
		String starttime = dto.getStartTime();
		String endtime = dto.getEndTime();
		String queryType = dto.getQueryType(); //0:mile 1:fuel 2:time
		
		if(StringUtils.isEmpty(starttime) || StringUtils.isEmpty(endtime)){
			//如果不输入时间，取最近一个月时间作为开始和结束时间
			Date endDate = new Date();
	    	Date startDate = DateUtils.addDays(endDate,-29);
	    	starttime = DateUtils.date2String(startDate,DateUtils.FORMAT_YYYY_MM_DD);
	    	endtime = DateUtils.date2String(endDate,DateUtils.FORMAT_YYYY_MM_DD);
		}
		
		//按输入日期区间进行统计
		Date startDate = DateUtils.string2Date(starttime + " "+DateUtils.FORMAT_TIME_HH_MI_SS_START,DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
		Date endDate = DateUtils.string2Date(endtime + " "+DateUtils.FORMAT_TIME_HH_MI_SS_END,DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
		
		return usageReportService.getDepartmentColumnarChartByDayRangeAndType(startDate,endDate,queryType,dto.getOrgId(),dto.getSelfDept(),dto.getChildDept());
	}
	
	public QueryDate getValidQueryDate(String starttime,String endtime){
		QueryDate queryDate = new QueryDate();
		String startDate = null;
		String endDate = null;
		
		if(StringUtils.isEmpty(starttime) && StringUtils.isEmpty(endtime)){ //如果不输入时间，取最近一个月时间作为开始和结束时间
			Date endDateTmp = new Date();
			Date startDateTmp = DateUtils.addDays(endDateTmp,-29);
			startDate = DateUtils.date2String(startDateTmp,DateUtils.FORMAT_YYYY_MM_DD);
			endDate = DateUtils.date2String(endDateTmp,DateUtils.FORMAT_YYYY_MM_DD);
		}else if((!StringUtils.isEmpty(starttime)) && (!StringUtils.isEmpty(endtime))){ //开始时间,结束时间都不为空
	    	startDate = starttime;
	    	endDate = endtime;
		}else if(StringUtils.isEmpty(starttime) && (!StringUtils.isEmpty(endtime))){ //开始时间为空,结束时间不为空
			Date endDateTmp = DateUtils.string2Date(endtime,DateUtils.FORMAT_YYYY_MM_DD);
			Date startDateTmp = DateUtils.addDays(endDateTmp,-29);
			startDate = DateUtils.date2String(startDateTmp,DateUtils.FORMAT_YYYY_MM_DD);
			endDate = DateUtils.date2String(endDateTmp,DateUtils.FORMAT_YYYY_MM_DD);
		}else if((!StringUtils.isEmpty(starttime)) && StringUtils.isEmpty(endtime)){ //开始时间不为空,结束时间为空
			Date startDateTmp = DateUtils.string2Date(starttime,DateUtils.FORMAT_YYYY_MM_DD);
			Date endDateTmp = DateUtils.addDays(startDateTmp,29);
			startDate = DateUtils.date2String(startDateTmp,DateUtils.FORMAT_YYYY_MM_DD);
			endDate = DateUtils.date2String(endDateTmp,DateUtils.FORMAT_YYYY_MM_DD);
		}
		
		if(DateUtils.string2Date(endDate,DateUtils.FORMAT_YYYY_MM_DD).compareTo(DateUtils.string2Date(startDate,DateUtils.FORMAT_YYYY_MM_DD)) < 0){
			return null;
		}else{
			queryDate.setStartDate(startDate);
			queryDate.setEndDate(endDate);
		}
		return queryDate;
	}
	
	/**
	 * 传入的deptId是否合法
	 * @param entId
	 * @param deptId
	 * @return
	 */
	public boolean isValidDeptId(long entId,long deptId){
		Organization org = organizationService.findById(deptId);
		if(entId == 0l){//部门管理员
			if(org != null){
				return true;
			}
			return false;
		}else{//企业管理员
			if(org != null){
				if(org.getParentId().longValue() == entId){
					return true;
				}
				return false;
			}
			return false;
		}
	}
}
