package com.cmdt.carrental.platform.service.api.portal;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.HomePageMapModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.TripPropertyModel;
import com.cmdt.carrental.common.model.VehicleBrandModel;
import com.cmdt.carrental.common.model.VehicleEnterpriseModel;
import com.cmdt.carrental.common.model.VehicleHistoryTrack;
import com.cmdt.carrental.common.model.VehicleListForOrgDto;
import com.cmdt.carrental.common.model.VehicleLocationModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformVehicleService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.common.WsResponse;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.platform.service.model.request.vehicle.DriverAllocateDto;
import com.cmdt.carrental.platform.service.model.request.vehicle.LocationDto;
import com.cmdt.carrental.platform.service.model.request.vehicle.UnAssignedVehicleDto;
import com.cmdt.carrental.platform.service.model.request.vehicle.VehicelToCurrOrgDto;
import com.cmdt.carrental.platform.service.model.request.vehicle.VehicleAssignDto;
import com.cmdt.carrental.platform.service.model.request.vehicle.VehicleByVehicleNumberDto;
import com.cmdt.carrental.platform.service.model.request.vehicle.VehicleCreateDto;
import com.cmdt.carrental.platform.service.model.request.vehicle.VehicleHistoryTrackeDto;
import com.cmdt.carrental.platform.service.model.request.vehicle.VehicleListDto;
import com.cmdt.carrental.platform.service.model.request.vehicle.VehicleListMantainanceDto;
import com.cmdt.carrental.platform.service.model.request.vehicle.VehicleModelDto;
import com.cmdt.carrental.platform.service.model.request.vehicle.VehicleSpeedLimitDto;
import com.cmdt.carrental.platform.service.model.request.vehicle.VehicleTripTraceDto;
import com.cmdt.carrental.platform.service.model.request.vehicle.VehicleUpdateDto;

@Produces(MediaType.APPLICATION_JSON)
public class VehicleApi extends BaseApi {

	private static final Logger LOG = LoggerFactory.getLogger(VehicleApi.class);

	@Autowired
	private PlatformVehicleService vehicleService;

	/**
	 * 根据组织机构树查看车辆
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@POST
	@Path("/list")
	@Consumes(MediaType.APPLICATION_JSON)
	public PagModel list(@Valid @NotNull VehicleListDto dto) throws Exception {
		return vehicleService.listVehicle(super.getUserById(dto.getUserId()), dto);
	}

	/**
	 * 车辆维保中，自动匹配查询
	 * 
	 * @param dto
	 * @return
	 */
	@POST
	@Path("/vehicleListMantainance")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<Vehicle> vehicleListMantainance(@Valid @NotNull VehicleListMantainanceDto dto) {
		return vehicleService.vehicleListMantainance(super.getUserById(dto.getUserId()), dto);
	}

	/**
	 * 企业管理员增加车辆
	 * 
	 * @param dto
	 * @return
	 */
	@POST
	@Path("/addVehicle")
	@Consumes(MediaType.APPLICATION_JSON)
	public String addVehicle(@Valid @NotNull VehicleCreateDto dto) {
		try {
			return vehicleService.createVehicle(super.getUserById(dto.getUserId()), dto);
		} catch (Exception e) {
			LOG.error("VehicleAlertApi list failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * 车辆来源下拉框list(search条件)
	 * 
	 * @param userId
	 * @return
	 */
	@GET
	@Path("/listVehicleFrom/{userId}")
	public List<VehicleEnterpriseModel> listVehicleFrom(@PathParam("userId") Long userId) {
		try {
			return vehicleService.getVehicleOrigin(super.getUserById(userId));
		} catch (Exception e) {
			LOG.error("VehicleAlertApi listVehicleFrom failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * 根据车辆ID查询车辆
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/findVehicleById/{id}")
	public Vehicle showUpdateForm(@PathParam("id") Long id) {
		return vehicleService.getVehicleById(id);
	}

	/**
	 * 根据id查询车辆(定制化，包含车辆来源,所属部门)
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/monitor/findVehicleById/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public VehicleModel showMonitorVehicle(@PathParam("id") Long id) {
		return vehicleService.findVehicleModelById(id);
	}

	/**
	 * 修改车辆
	 * 
	 * @param dto
	 * @return
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(@Valid @NotNull VehicleUpdateDto dto) {
		try {
			vehicleService.updateVehicle(dto);
		} catch (Exception e) {
			LOG.error("VehicleApi update failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	@POST
	@Path("/importCSV")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response importCSV(List<Attachment> attachments,
			@Multipart(value = "userId", type = "text/plain", required = true) @Digits(message = "UserId格式错误", fraction = 0, integer = 10) Long userId) {
		WsResponse<Object> wsResponse = new WsResponse<>();
		// TODO try { business processor } catch {}

		return Response.ok(wsResponse).build();
	}

	@GET
	@Path("/listDeptVehicle/{orgId}")
	public List<VehicleModel> listDeptVehicle(@PathParam("orgId") Long orgId) {
		try {
			return vehicleService.listDeptVehicle(orgId);
		} catch (Exception e) {
			LOG.error("VehicleApi listDeptVehicle failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * 根据设备号查询位置数据
	 * 
	 * @param deviceNumber
	 * @return
	 */

	@GET
	@Path("/queryObdLocationByImei/{deviceNumber}")
	@Consumes(MediaType.APPLICATION_JSON)
	public VehicleLocationModel queryObdLocationByImei(@PathParam("deviceNumber") String deviceNumber) {
		try {
			return vehicleService.queryObdLocationByIme(deviceNumber);
		} catch (Exception e) {
			LOG.error("VehicleApi queryObdLocationByImei failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * 根据车辆Id查询该车的位置数据
	 * 
	 * @param dto
	 * @return
	 */

	@POST
	@Path("/queryObdLocationByVehicleId/{vehicleId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public VehicleLocationModel queryObdLocationByVehicleId(@PathParam("vehicleId") Long vehicleId) {
		try {
			return vehicleService.queryObdLocationByVehicleId(vehicleId);
		} catch (Exception e) {
			LOG.error("VehicleApi queryObdLocationByVehicleId failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	@POST
	@Path("/monitor/findTripPropertyDataByTimeRange")
	@Consumes(MediaType.APPLICATION_JSON)
	public TripPropertyModel findTripPropertyDataByTimeRange(@Valid @NotNull VehicleTripTraceDto dto) {
		try {
			return vehicleService.findTripPropertyDataByTimeRange(dto);
		} catch (Exception e) {
			LOG.error("VehicleApi queryObdLocationByVehicleId failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	@POST
	@Path("/listAvailableVehicleByOrder")
	@Consumes(MediaType.APPLICATION_JSON)
	public PagModel listAvailableVehicleByOrder(@Valid @NotNull BusiOrderQueryDto dto) {
		try {
			return vehicleService.listAvailableVehicle(super.getUserById(dto.getLoginUserId()), dto);
		} catch (Exception e) {
			LOG.error("VehicleApi listAvailableVehicleByOrder failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	@GET
	@Path("/listAvailableAssignedEnterprise/{userId}")
	public List<VehicleEnterpriseModel> listAvailableAssignedEnterprise(@PathParam("userId") Long userId) {
		try {
			return vehicleService.listAvailableAssignedEnterprise(super.getUserById(userId));
		} catch (Exception e) {
			LOG.error("VehicleApi listAvailableAssignedEnterprise failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * 车辆分配或收回
	 * 
	 * @param dto
	 * @return
	 */

	@POST
	@Path("/vehicleAssigne")
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean vehicleAssigne(@Valid @NotNull VehicleAssignDto dto) {
		try {
			return vehicleService.vehicleAssigne(dto) > 0 ? true : false;
		} catch (Exception e) {
			LOG.error("VehicleApi vehicleAssigne failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	@POST
	@Path("/monitor/findVehicleHistoryTrackWithoutAddress")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<VehicleHistoryTrack> findVehicleHistoryTrack(@Valid @NotNull VehicleHistoryTrackeDto dto) {
		try {
			return vehicleService.findVehicleHistoryTrack(dto);
		} catch (Exception e) {
			LOG.error("VehicleApi findVehicleHistoryTrack failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * 查询车辆可分配的司机
	 * 
	 * @param vehicleId
	 * @return
	 */
	@POST
	@Path("/findAvailableDriversByVehicleId/{vehicleId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<DriverModel> findAvailableDriversByVehicleId(@PathParam("vehicleId") Long vehicleId) {
		try {
			return vehicleService.findAvailableDriversByVehicleId(vehicleId);
		} catch (Exception e) {
			LOG.error("VehicleApi findAvailableDriversByVehicleId failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * 给车辆分配司机
	 * 
	 * @param dto
	 * @return
	 */

	@POST
	@Path("/driverAllocate")
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean driverAllocate(@Valid @NotNull DriverAllocateDto dto) {
		try {
			return vehicleService.driverAllocate(dto) > 0 ? true : false;
		} catch (Exception e) {
			LOG.error("VehicleApi vehicleAssigne failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	@POST
	@Path("/findVehicleByVehicleNumber")
	@Consumes(MediaType.APPLICATION_JSON)
	public VehicleModel findVehicleInfoByVehicleNumber(@Valid @NotNull VehicleByVehicleNumberDto dto) {
		try {
			return vehicleService.findVehicleInfoByVehicleNumber(super.getUserById(dto.getUserId()),
					dto.getVehicleNumber());
		} catch (Exception e) {
			LOG.error("VehicleApi findVehicleInfoByVehicleNumber failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	@POST
	@Path("/updateSpeedLimit")
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateSpeedLimit(@Valid @NotNull VehicleSpeedLimitDto dto) {
		try {
			return vehicleService.updateSpeedLimitgetUserById(getUserById(dto.getUserId()), dto);
		} catch (Exception e) {
			LOG.error("VehicleApi updateSpeedLimit failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	@POST
	@Path("/listVehicleAutoComplete")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<VehicleModel> listVehicleAutoComplete(@Valid @NotNull VehicleByVehicleNumberDto dto) {
		try {
			return vehicleService.listVehicleAutoComplete(getUserById(dto.getUserId()), dto.getVehicleNumber());
		} catch (Exception e) {
			LOG.error("VehicleApi listVehicleAutoComplete failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	@POST
	@Path("/monitor/findTripPropertyDataByTimeRangeByName")
	@Consumes(MediaType.APPLICATION_JSON)
	public TripPropertyModel findTripPropertyDataByTimeRangeByName(@Valid @NotNull VehicleHistoryTrackeDto dto) {
		try {
			return vehicleService.findTripPropertyDataByTimeRangeByName(dto);
		} catch (Exception e) {
			LOG.error("VehicleApi findTripPropertyDataByTimeRangeByName failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	@POST
	@Path("/listVehMoniStatusTree")
	@Consumes(MediaType.APPLICATION_JSON)
	public Map<String, Object> listVehMoniStatusTreeData(@Valid @NotNull VehicleListMantainanceDto dto) {
		try {
			return vehicleService.listVehMoniStatusTreeData(super.getUserById(dto.getUserId()), dto);
		} catch (Exception e) {
			LOG.error("VehicleApi listVehMoniStatusTreeData failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	@POST
	@Path("/organization/listUnAssignedVehicle")
	@Consumes(MediaType.APPLICATION_JSON)
	public PagModel listUnAssignedVehicle(@Valid @NotNull UnAssignedVehicleDto dto) {
		try {
			return vehicleService.listUnAssignedVehicle(super.getUserById(dto.getUserId()), dto);
		} catch (Exception e) {
			LOG.error("VehicleApi listUnAssignedVehicle failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * 组织管理界面 添加车辆到本部门
	 * 
	 * @param dto
	 * @return
	 */
	@POST
	@Path("/organization/addVehicelToCurrOrg")
	@Consumes(MediaType.APPLICATION_JSON)
	public boolean addVehicelToCurrOrg(@Valid @NotNull VehicelToCurrOrgDto dto) {
		try {
			return vehicleService.addVehicelToCurrOrg(dto);
		} catch (Exception e) {
			LOG.error("VehicleApi listUnAssignedVehicle failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * 获取组织管理界面的 获取车辆列表
	 * 
	 * @param dto
	 * @return
	 */
	@POST
	@Path("/organization/findVehicleListbyIds")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<VehicleListForOrgDto> findVehicleListbyIds(@Valid @NotNull VehicelToCurrOrgDto dto) {
		try {
			return vehicleService.findVehicleListbyIds(dto.getIds());
		} catch (Exception e) {
			LOG.error("VehicleApi findVehicleListbyIds failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * 组织管理界面的 ,从部门移除车辆
	 * 
	 * @param dto
	 * @return
	 */
	@POST
	@Path("/organization/removeVehicleFromOrgs")
	@Consumes(MediaType.APPLICATION_JSON)
	public Map<String, Object> removeVehicleFromOrg(@Valid @NotNull VehicelToCurrOrgDto dto) {
		try {
			return vehicleService.removeVehicleFromOrg(super.getUserById(dto.getUserId()), dto);
		} catch (Exception e) {
			LOG.error("VehicleApi removeVehicleFromOrg failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * 车辆品牌列表
	 * 
	 * @param dto
	 * @return
	 */
	@POST
	@Path("/organization/removeVehicleFromOrgs")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<VehicleBrandModel> listvehicleModel(@Valid @NotNull VehicleModelDto dto) {
		try {
			return vehicleService.listvehicleModel(dto);
		} catch (Exception e) {
			LOG.error("VehicleApi listvehicleModel failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * 查询多个OBD位置信息(或者说车辆位置信息
	 * 
	 * @param dto
	 * @return
	 */

	@POST
	@Path("/queryObdLocation")
	@Consumes(MediaType.APPLICATION_JSON)
	public HomePageMapModel queryObdLocation(@Valid @NotNull LocationDto dto) {
		try {
			return vehicleService.queryObdLocation(super.getUserById(dto.getUserId()), dto);
		} catch (Exception e) {
			LOG.error("VehicleApi listvehicleModel failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
}
