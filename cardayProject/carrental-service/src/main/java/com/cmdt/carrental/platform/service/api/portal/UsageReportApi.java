package com.cmdt.carrental.platform.service.api.portal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.model.UsageReportModel;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformUsageReportService;
import com.cmdt.carrental.platform.service.model.request.usageReport.UsageReportChartDto;

@Consumes(MediaType.APPLICATION_JSON)  
public class UsageReportApi extends BaseApi{
	
	private static final Logger LOG = LoggerFactory.getLogger(UsageReportApi.class);
	
	@Autowired
    private PlatformUsageReportService  platformUsageReportService;
	
	/**
	 * （多组织修改后，已经没有使用，可以删除）
	 * 
	 * 车辆使用统计(饼图以及柱状图,企业管理员查看)
	 * @param usageReportDto
	 * @param json
	 * @return
	 *//*
	@POST
	@Path("/getPieAndColumnarDataByDayRange")
	public UsageReportModel getPieAndColumnarDataByDayRange(@Valid @NotNull UsageReportDto usageReportDto){
		LOG.info("Enter UsageReportApi getPieAndColumnarDataByDayRange");
		return platformUsageReportService.getPieAndColumnarDataByDayRange(super.getUserById(usageReportDto.getUserId()),usageReportDto);
	}
	*/
	
	/**
	 * 车辆使用统计(饼图以及柱状图,企业管理员查看)
	 * @param usageReportDto
	 * @param json
	 * @return
	 */
	@POST
	@Path("/getPieAndColumnarData")
	public UsageReportModel getPieAndColumnarData(@Valid @NotNull UsageReportChartDto usageReportChartDto){
		LOG.info("Enter UsageReportApi getPieAndColumnarDataByDayRange");
		return platformUsageReportService.getPieAndColumnarData(usageReportChartDto);
	}
	
	
}
