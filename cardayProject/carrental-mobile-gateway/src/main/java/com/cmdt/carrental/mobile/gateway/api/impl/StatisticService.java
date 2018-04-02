package com.cmdt.carrental.mobile.gateway.api.impl;

import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.mobile.gateway.api.inf.IStatisticService;
import com.cmdt.carrental.mobile.gateway.common.Constants;
import com.cmdt.carrental.mobile.gateway.common.WsResponse;
import com.cmdt.carrental.mobile.gateway.model.DateDataModel;
import com.cmdt.carrental.mobile.gateway.model.DeptDataModel;
import com.cmdt.carrental.mobile.gateway.model.request.stat.DaysStatDto;
import com.cmdt.carrental.mobile.gateway.service.BusiStatisticsService;

public class StatisticService implements IStatisticService {

	private static final Logger LOG = LoggerFactory.getLogger(StatisticService.class);
	
	@Autowired
	private BusiStatisticsService busiStatisticsService;
	
	@Override
	@POST
	@Path("/load7DaysMileage/{username}")
	public Response load7DaysMileage(@PathParam("username")String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@POST
	@Path("/loadToYesMileage/{username}")
	public Response loadToYesMileage(@PathParam("username")String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@POST
	@Path("/loadTop3DMileage/{username}")
	public Response loadTop3DMileage(@PathParam("username")String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@POST
	@Path("/load7DaysFuel/{username}")
	public Response load7DaysFuel(@PathParam("username")String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@POST
	@Path("/loadToYesFuel/{username}")
	public Response loadToYesFuel(@PathParam("username")String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@POST
	@Path("/loadTop3DFuel/{username}")
	public Response loadTop3DFuel(@PathParam("username")String username) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	@GET
	@Path("/loadVehicleStat/{userId}")
	public Response loadVehicleStat(@PathParam("userId")String userId) {
		WsResponse<Map<String, Object>> wsResponse = new WsResponse<>();
		try{
			if(userId == null || "".equals(userId)){
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
				wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
			}else{
//				VehicleStatModel statModel = busiStatisticsService.loadVehicleStat(userId);
				Map<String, Object> statModel = busiStatisticsService.findVehicleStatus(userId);
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
				wsResponse.setResult(statModel);
			}
		}catch(Exception e){
			LOG.error("loadVehicleStat error",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
        return Response.ok( wsResponse ).build();
	}

	
	@Override
	@POST
	@Path("/load7DaysStat")
	public Response load7DaysStat(DaysStatDto data){
		WsResponse<List<DateDataModel>> wsResponse = new WsResponse<>();
		try{
			List<DateDataModel> statModel = busiStatisticsService.load7DaysStat(data);
			if(statModel == null){
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
				wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
			}else{
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
				wsResponse.setResult(statModel);
			}
		}catch(Exception e){
			LOG.error("load7DaysStat error",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
        return Response.ok( wsResponse ).build();
	}
	
	
	@Override
	@POST
	@Path("/loadTop3DStat")
	public Response loadTop3DStat(DaysStatDto data){
		WsResponse<List<DeptDataModel>> wsResponse = new WsResponse<>();
		try{
			List<DeptDataModel> statModel = busiStatisticsService.loadTop3DStat(data);
			if(statModel == null){
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
				wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
			}else{
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
				wsResponse.setResult(statModel);
			}
		}catch(Exception e){
			LOG.error("loadTop3DStat error",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
        return Response.ok( wsResponse ).build();
	}
	
}
