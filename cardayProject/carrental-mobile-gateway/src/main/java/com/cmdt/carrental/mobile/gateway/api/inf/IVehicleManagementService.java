package com.cmdt.carrental.mobile.gateway.api.inf;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;

import com.cmdt.carrental.mobile.gateway.model.request.vehicle.OrderVehicleList;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.QueryVehicleListDto;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.VehicleImeiTraceDto;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.VehicleParamDto;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.VehicleStatusList;
import com.cmdt.carrental.mobile.gateway.model.request.vehicle.VehicleTraceDto;

public interface IVehicleManagementService {
	
	//查询车辆列表
	public Response queryVehicleList(QueryVehicleListDto dto); 
	
	public Response queryVehicleLocationList(@Valid @NotNull VehicleStatusList param);
	
	//查询可分配车辆列表
	public Response listAvailableVehicle(@Valid @NotNull OrderVehicleList dto);
	
	//根据ID查询车辆信息
	public Response queryVehicleInfoById(@Valid @NotNull VehicleParamDto vehicleParam);
	
	public Response queryAvailableVehicleById(@Valid @NotNull VehicleParamDto vehicleParam);
	
	
	
	//修改车辆信息
	
	//分配车辆
	
	//删除车辆
	
	//新增车辆
	
	//查询车辆保养信息列表
	
	//重置车辆保养信息
	
	//新增车辆保养信息
	
	//车辆实时地理分布
	public Response queryVehicleRealtimeDate(@Valid @NotNull VehicleParamDto vehicleParam);
	
	//车辆历史轨迹
	public Response findTripTrace(@Valid @NotNull VehicleTraceDto vehicleParam);
	
	//车辆历史轨迹(GPS经纬度)
	public Response findVehicleHistoryGPSTrack(@Valid @NotNull VehicleTraceDto vehicleParam);
	
	//获得所有的车辆类型列表
	public Response queryVehicleType();
	
	/**
	 * 更具IMEI查询历史轨迹
	 * @author KevinPei
	 * @param vehicleParam
	 * @return
	 */
	public Response findVehicleHistoryGPSTrackByImei(VehicleImeiTraceDto vehicleParam);

}
