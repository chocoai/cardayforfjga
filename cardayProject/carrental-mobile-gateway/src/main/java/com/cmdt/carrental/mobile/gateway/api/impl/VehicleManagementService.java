package com.cmdt.carrental.mobile.gateway.api.impl;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.mobile.gateway.api.inf.IVehicleManagementService;
import com.cmdt.carrental.mobile.gateway.common.Constants;
import com.cmdt.carrental.mobile.gateway.common.WsResponse;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.OrderVehicleList;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.QueryVehicleListDto;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.VehicleImeiTraceDto;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.VehicleParamDto;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.VehicleStatusList;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.VehicleTraceDto;
import com.cmdt.carrental.mobile.gateway.model.response.vehicle.VehicleTypeResult;
import com.cmdt.carrental.mobile.gateway.service.VehicleAppService;

public class VehicleManagementService implements IVehicleManagementService {
	
	private static final Logger LOG = Logger.getLogger(VehicleManagementService.class);

	@Autowired
    private VehicleAppService vehicleAppService;
	
	@Override
	@POST
	@Path("/queryVehicleList")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response queryVehicleList(QueryVehicleListDto dto) {
		WsResponse<PagModel> wsResponse = new WsResponse<PagModel>();
		wsResponse.setStatus(Constants.STATUS_SUCCESS);
    	try{
    		PagModel pagModel = vehicleAppService.queryVehicleList(dto);
	    	wsResponse.setResult(pagModel);
    	}catch(Exception e){
    		LOG.error("queryVehicleList failed!", e);
    		wsResponse.setStatus(Constants.STATUS_FAILURE);
   	   }
       return Response.ok( wsResponse ).build();
	}
	
	@Override
	@GET
	@Path("/queryVehicleType")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response queryVehicleType() {
		WsResponse<List<VehicleTypeResult>> wsResponse = new WsResponse<List<VehicleTypeResult>>();
		wsResponse.setStatus(Constants.STATUS_SUCCESS);
		List<VehicleTypeResult> list = vehicleAppService.queryVehicleType();
		wsResponse.getMessages().add(Constants.MESSAGE_SUCCESS);
		wsResponse.setResult(list);
		return Response.ok( wsResponse ).build();
	}
	
	@Override
	@POST
	@Path("/queryVehicleRealtimeDate")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response queryVehicleRealtimeDate(VehicleParamDto vehicleParam) {
		WsResponse<List<VehicleTypeResult>> wsResponse = vehicleAppService.queryObdLocationByImei(vehicleParam);
		return Response.ok( wsResponse ).build();
	}
	
	@Override
	@POST
	@Path("/findTripTrace")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response findTripTrace(VehicleTraceDto vehicleParam) {
		WsResponse wsResponse = vehicleAppService.findTripTraceDataByTimeRange(vehicleParam);
		return Response.ok( wsResponse ).build();
	}
	
	@Override
	@POST
	@Path("/queryVehicleLocationList")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response queryVehicleLocationList(VehicleStatusList dto) {
		WsResponse<PagModel> wsResponse = new WsResponse<>();
		try{
			wsResponse.setResult(vehicleAppService.queryObdLocationList(dto));
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("AlertServiceImpl.statByTypeAndTime failed, error : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
	
	@Override
	@POST
	@Path("/queryVehicleInfoById")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response queryVehicleInfoById(VehicleParamDto vehicleParam) {
		WsResponse wsResponse = vehicleAppService.queryVehicleInfoById(vehicleParam);
		return Response.ok( wsResponse ).build();
	}
	
	@Override
	@POST
	@Path("/listAvailableVehicle")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response listAvailableVehicle(OrderVehicleList dto) {
		WsResponse<PagModel> wsResponse = new WsResponse<>();
		try{
			wsResponse.setResult( vehicleAppService.listAvailableVehicle(dto));
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
		}catch(Exception e){
			LOG.error("AlertServiceImpl.statByTypeAndTime failed, error : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
		
	}
	
	@Override
	@POST
	@Path("/queryAvailableVehicleById")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response queryAvailableVehicleById(VehicleParamDto vehicleParam) {
		WsResponse wsResponse = vehicleAppService.queryAvailableVehicleById(vehicleParam);
		return Response.ok( wsResponse ).build();
	}

	@Override
	@POST
	@Path("/findVehicleHistoryGPSTrack")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response findVehicleHistoryGPSTrack(VehicleTraceDto vehicleParam) {
		WsResponse wsResponse = vehicleAppService.findVehicleHistoryGPSTrack(vehicleParam);
		return Response.ok( wsResponse ).build();
	}
	
	@Override
	@POST
	@Path("/findVehicleHistoryGPSTrackByImei")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response findVehicleHistoryGPSTrackByImei(VehicleImeiTraceDto vehicleParam) {
		WsResponse wsResponse = vehicleAppService.findVehicleHistoryGPSTrackByImei(vehicleParam);
		return Response.ok( wsResponse ).build();
	}
	
	
}
