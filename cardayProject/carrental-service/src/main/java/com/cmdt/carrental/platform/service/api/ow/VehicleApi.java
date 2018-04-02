package com.cmdt.carrental.platform.service.api.ow;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleEnterpriseModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformVehicleService;
import com.cmdt.carrental.platform.service.model.request.vehicle.VehicleListDto;
import com.cmdt.carrental.platform.service.model.request.vehicle.VehicleQueryDto;

@Produces(MediaType.APPLICATION_JSON)
public class VehicleApi extends BaseApi{
	
	private final static Logger LOG = LoggerFactory.getLogger(VehicleApi.class);
	
	@Autowired
	private PlatformVehicleService platformVehicleService;

	/**
	 * 查询用车企业的车辆
	 * @param vehicleListDto
	 * @return
	 */
	@POST
	@Path("/listVhicle")
	@Consumes(MediaType.APPLICATION_JSON)
	public PagModel listVhicle(@Valid @NotNull VehicleListDto vehicleListDto){
		LOG.info("Enter OW VehicleApi listVhicle");
		return platformVehicleService.getVehicleListByEnterAdmin(super.getUserById(vehicleListDto.getUserId()),vehicleListDto);
	}
	
	 /**
     * 根据车牌号查找车辆信息
     * @param vehicleQueryDto
     * @return
     */
	@POST
	@Path("/findVehicleByVehicleNumber")
	@Consumes(MediaType.APPLICATION_JSON)
	public VehicleModel  findVehicleByVehicleNumber(@Valid @NotNull VehicleQueryDto vehicleQueryDto){
		LOG.info("Enter OW VehicleApi findVehicleByVehicleNumber");
		return platformVehicleService.findVehicleByVehicleNumber(vehicleQueryDto);
	}
	
	 /**
     * 车辆来源
     * 根据企业的id查找和该企业关联的公司（租车公司，和用车企业）
     * @param vehicleQueryDto
     * @return
     */
	@POST
	@Path("/listVehicleFrom")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<VehicleEnterpriseModel>  findVehicleFrom(@Valid @NotNull VehicleQueryDto vehicleQueryDto){
		LOG.info("Enter OW VehicleApi listVehicleFrom");
		return platformVehicleService.listVehicleFrom(vehicleQueryDto);
	}
	
	/**
	 * 管理员查询所有没有绑定设备的车辆
	 * @param vehicleNumber
	 * @return
	 */
	@GET
	@Path("/listUnBindDeviceVehicle/{vehicleNumber}")
	public List<VehicleModel> listUnBindDeviceVehicle(@PathParam("vehicleNumber")  @NotNull String vehicleNumber){
		LOG.info("Enter OW VehicleApi findVehicleByVehicleNumber");
		return platformVehicleService.findUnBindDeviceVehicle(vehicleNumber);
	}
	
	/**
	 * 根据id查询车辆(定制化，包含车辆来源,所属部门)
	 * @param id
	 * @return
	 */
	@GET
	@Path("/monitor/{id}/update")
	public VehicleModel showMonitorVehicle(@PathParam("id") @NotNull Long id){
		LOG.info("Enter OW VehicleApi showMonitorVehicle");
		return platformVehicleService.showMonitorVehicle(id);
	}
}
