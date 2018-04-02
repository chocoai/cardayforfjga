package com.cmdt.carrental.mobile.gateway.api.impl;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.entity.DevcieCommandConfigRecord;
import com.cmdt.carrental.common.entity.Device;
import com.cmdt.carrental.common.model.DevcieCommandConfigRecordModel;
import com.cmdt.carrental.common.service.DeviceService;
import com.cmdt.carrental.mobile.gateway.api.inf.IDeviceService;
import com.cmdt.carrental.mobile.gateway.common.Constants;
import com.cmdt.carrental.mobile.gateway.common.WsResponse;
import com.cmdt.carrental.mobile.gateway.model.BindDeviceAndVehicleDto;
import com.cmdt.carrental.mobile.gateway.model.LimitSpeedDto;
import com.cmdt.carrental.mobile.gateway.model.response.vehicle.VehicleRealtimeResult;
import com.cmdt.carrental.mobile.gateway.service.DeviceAppService;

public class DeviceServiceImpl implements IDeviceService {
	
	private static final Logger LOG = Logger.getLogger(DeviceServiceImpl.class);

	@Autowired
    private DeviceAppService deviceService;
	
	@Autowired
    private DeviceService commonDeviceService;
	
	@Override
	@GET
	@Path("/queryDeviceRealtimeData/{devNo}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response queryDeviceRealtimeData(@PathParam("devNo") String devNo) {
		LOG.debug("DeviceService.queryDeviceRealtimeData["+devNo+"]");
		WsResponse<VehicleRealtimeResult> wsResponse = new WsResponse<VehicleRealtimeResult>();
		try{
			VehicleRealtimeResult result = deviceService.getDeviceRuntimeInfo(devNo);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setResult(result);
		}catch(Exception e){
			LOG.error("queryDeviceRealtimeData error",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
        return Response.ok( wsResponse ).build();
	}

	@Override
	@GET
	@Path("/queryDeviceStaticData/{devNo}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response queryDeviceStaticData(@PathParam("devNo") String devNo) {
		LOG.debug("DeviceService.queryDeviceStaticData["+devNo+"]");
		WsResponse<Device> wsResponse = new WsResponse<Device>();
		try{
			Device result = deviceService.getDeviceStaticInfo(devNo);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setResult(result);
		}catch(Exception e){
			LOG.error("queryDeviceStaticData error",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
        return Response.ok( wsResponse ).build();
	}
	
	@Override
	@POST
	@Path("/setLimitSpeed")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response setLimitSpeed(LimitSpeedDto dto) {
		LOG.debug("DeviceService.setLimitSpeed["+dto.getImei()+"]");
		WsResponse<Device> wsResponse = new WsResponse<Device>();
		try{
			// 根据IMEI,查询该设备目前正在执行的下发命令记录,得到限速值
			DevcieCommandConfigRecordModel model = commonDeviceService.findLatestWaittingCommandByImei(dto.getImei());
			if (StringUtils.isNotBlank(model.getCommandValue())) {
				// 下发限速值
				int limitSpeed = Integer.valueOf(model.getCommandValue());
				// 更新最新下发限速
				commonDeviceService.updateLatestLimitSpeed(dto.getImei(), limitSpeed);
				
				// 更新下发命令状态
				DevcieCommandConfigRecord updateRecord = new DevcieCommandConfigRecord();
				updateRecord.setId(model.getId());
				updateRecord.setDeviceNumber(model.getDeviceNumber());
				updateRecord.setCommandType(model.getCommandType());
				updateRecord.setCommandValue(model.getCommandValue());
				updateRecord.setCommandSendTime(model.getCommandSendTime());
				updateRecord.setCommandSendStatus(model.getCommandSendStatus());
				updateRecord.setUserId(model.getUserId());
				
				updateRecord.setCommandExcuteStatus("success");
				java.util.Date utilDate = new java.util.Date();
	    		updateRecord.setCommandResponseTime(utilDate);
				
				commonDeviceService.updateDeviceCommandConfigRecord(updateRecord);
			}
			
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
				
		}catch(Exception e){
			LOG.error("setLimitSpeed error",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
        return Response.ok( wsResponse ).build();
	}

	/*
	 * 设备绑定车辆
	*/
	@Override
	@POST
	@Path("/bindDeviceAndVehicle")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response bindDeviceAndVehicle(BindDeviceAndVehicleDto bindDeviceAndVehicleDto) {
		LOG.debug("DeviceService.bindDeviceAndVehicle");
		WsResponse<Device> wsResponse = new WsResponse<>();
		try{
			String msg = deviceService.bindDeviceAndVehicle(bindDeviceAndVehicleDto);
			if (StringUtils.isBlank(msg)){
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			} else {
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
				wsResponse.getMessages().add(msg);
			}
		}catch(Exception e){
			LOG.error("bindDeviceAndVehicle error",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
        return Response.ok( wsResponse ).build();
	}

	/*
	 * 设备车辆解绑
	*/
	@Override
	@POST
	@Path("/unbindDeviceAndVehicle")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes({ MediaType.APPLICATION_JSON })
	public Response unbindDeviceAndVehicle(BindDeviceAndVehicleDto bindDeviceAndVehicleDto) {
		LOG.debug("DeviceService.unbindDeviceAndVehicle");
		WsResponse<Device> wsResponse = new WsResponse<>();
		try{
			String msg = deviceService.unbindDeviceAndVehicle(bindDeviceAndVehicleDto);
			if (StringUtils.isBlank(msg)){
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			} else {
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
				wsResponse.getMessages().add(msg);
			}
		}catch(Exception e){
			LOG.error("unbindDeviceAndVehicle error",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
        return Response.ok( wsResponse ).build();
	}	
}

