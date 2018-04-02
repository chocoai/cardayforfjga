package com.cmdt.carrental.mobile.gateway.api.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.UsageReportAppColumnarModel;
import com.cmdt.carrental.common.model.UsageReportAppLineModel;
import com.cmdt.carrental.mobile.gateway.api.inf.IUsageReportService;
import com.cmdt.carrental.mobile.gateway.common.Constants;
import com.cmdt.carrental.mobile.gateway.common.WsResponse;
import com.cmdt.carrental.mobile.gateway.model.QueryDate;
import com.cmdt.carrental.mobile.gateway.model.UsageReportVehiclePropertyQueryDto;
import com.cmdt.carrental.mobile.gateway.model.request.usage.UsageDepartmentReportDto;
import com.cmdt.carrental.mobile.gateway.service.UsageReportAppService;

public class UsageReportServiceImpl implements IUsageReportService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UsageReportServiceImpl.class);
	
	@Autowired
	private UsageReportAppService usageReportAppService;
	
	/**
	 * 部门统计图(柱形图展示)
	 */
	@Override
	@POST
	@Path("/getDepartmentColumnarChartByDayRange")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getDepartmentColumnarChartByDayRange(UsageDepartmentReportDto dto) {
		WsResponse<UsageReportAppColumnarModel> wsResponse = new WsResponse<>();
		try{
				QueryDate queryDate =usageReportAppService.getValidQueryDate(dto.getStartTime(),dto.getEndTime());
				if(queryDate == null){
					wsResponse.setStatus(Constants.API_STATUS_FAILURE);
					wsResponse.getMessages().add(Constants.API_MESSAGE_PARAM_ILLEGAL);
				}else{
					dto.setStartTime(queryDate.getStartDate());
					dto.setEndTime(queryDate.getEndDate());
				}
				
				wsResponse.setResult(usageReportAppService.getDepartmentColumnarChartByDayRange(dto));
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
			
		}catch(Exception e){
			LOG.error("UsageReportServiceImpl.getDepartmentColumnarChartByDayRange error:",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
	
	/**
	 * 用车情况(list展示)
	 * @param dto
	 * @return
	 */
	@Override
	@POST
	@Path("/getVehiclePropertyDataByDayRangeAndDeptId")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getVehiclePropertyDataByDayRangeAndDeptId(UsageReportVehiclePropertyQueryDto dto) {
		WsResponse<PagModel> wsResponse = new WsResponse<>();
		try{
			
			QueryDate queryDate =usageReportAppService.getValidQueryDate(dto.getStarttime(),dto.getEndtime());
			if(queryDate == null){
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
				wsResponse.getMessages().add(Constants.API_MESSAGE_PARAM_ILLEGAL);
			}else{
				dto.setStarttime(queryDate.getStartDate());
				dto.setEndtime(queryDate.getEndDate());
			}
			
			wsResponse.setResult(usageReportAppService.getVehiclePropertyDataByDayRangeAndDeptId(dto));
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("UsageReportServiceImpl.getVehiclePropertyDataByDayRangeAndDeptId error:",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}

	/**
	 * 整体走势图(曲线图展示)
	 */
	@Override
	@POST
	@Path("/getTendencyChartByDayRange")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getTendencyChartByDayRange(UsageDepartmentReportDto dto) {
		WsResponse<UsageReportAppLineModel> wsResponse = new WsResponse<>();
		try{

				QueryDate queryDate =usageReportAppService.getValidQueryDate(dto.getStartTime(),dto.getEndTime());
				if(queryDate == null){
					wsResponse.setStatus(Constants.API_STATUS_FAILURE);
					wsResponse.getMessages().add(Constants.API_MESSAGE_PARAM_ILLEGAL);
				}else{
					dto.setStartTime(queryDate.getStartDate());
					dto.setEndTime(queryDate.getEndDate());
				}
				
				wsResponse.setResult(usageReportAppService.getTendencyChartByDayRange(dto));
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);

		}catch(Exception e){
			LOG.error("UsageReportServiceImpl.getTendencyChartByDayRange error:",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}

}
