package com.cmdt.carrental.platform.service.api.portal;

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
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.entity.VehicleAlert;
import com.cmdt.carrental.common.model.AlertReport;
import com.cmdt.carrental.common.model.AlertStatisticModel;
import com.cmdt.carrental.common.model.CountModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.PositionAndStation;
import com.cmdt.carrental.common.model.VehicleOutBoundModel;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformVehicleAlertService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.platform.service.model.request.alert.AlertDataByTypeDto;
import com.cmdt.carrental.platform.service.model.request.alert.AlertExportAllDto;
import com.cmdt.carrental.platform.service.model.request.alert.AlertExportDto;
import com.cmdt.carrental.platform.service.model.request.alert.AlertMarkerVehicleDto;
import com.cmdt.carrental.platform.service.model.request.alert.AlertVehicleCountDto;
import com.cmdt.carrental.platform.service.model.request.alert.StationInfoByVehicleNum;
import com.cmdt.carrental.platform.service.model.request.alert.VehicleAlertByPage;
import com.cmdt.carrental.platform.service.model.request.alert.VehicleAlertListDto;
import com.cmdt.carrental.platform.service.model.request.alert.VehicleAlertNoPageDto;

@Produces(MediaType.APPLICATION_JSON)
public class VehicleAlertApi extends BaseApi {

	private static final Logger LOG = LoggerFactory.getLogger(VehicleAlertApi.class);

	@Autowired
	private PlatformVehicleAlertService alertService;

	/**
	 * 根据组织机构树以及其他条件查询车辆报警
	 * 
	 * @param dto
	 * @return
	 */
	@POST
	@Path("/list")
	@Consumes(MediaType.APPLICATION_JSON)
	public PagModel queryAlertByType(@Valid @NotNull VehicleAlertListDto dto) {
		try {
			return alertService.findAlertByType(getUserById(dto.getUserId()), dto);
		} catch (Exception e) {
			LOG.error("VehicleAlertApi list failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
	 * 导出车辆报警
	 * @param dto
	 * @return
	 */
	@POST
	@Path("/export")
	@Produces("application/vnd.ms-excel")
	public Response exportFile(@Valid @NotNull AlertExportDto dto) {
		/*try {
			File file = alertService.generateAlertExport(super.getUserById(dto.getUserId()), dto);
			String newFileName = URLEncoder.encode(file.getName(), "UTF-8");
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=" + newFileName);
			return response.build();
		} catch (Exception e) {
			LOG.error("VehicleAlertApi exportFile error, cause by: ", e);
			WsResponse<String> wsResponse = new WsResponse<>();
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
			return Response.ok(wsResponse).build();
		}*/
		return null;
	}
	
	/**
	 * 通过车牌号查询站点
	 * @param dto
	 * @return
	 */
	@POST
	@Path("/findStationByVehicleNumber")
	@Consumes(MediaType.APPLICATION_JSON)
	public PositionAndStation findStationByVehicleNumber(@Valid @NotNull StationInfoByVehicleNum dto) {
		return alertService.findStationByVehicleNumber(dto);
	}
	
	/**
	 * 根据车牌号查询该车辆的Marker和越界轨迹
	 * @param dto
	 * @return
	 */
	
	@POST
	@Path("/findMarkerAndTraceByVehicleNumber")
	@Consumes(MediaType.APPLICATION_JSON)
	public VehicleOutBoundModel findMarkerAndTraceByVehicleNumber(@Valid @NotNull AlertMarkerVehicleDto dto) {
		try {
			return alertService.findMarkerByVehicleNumber(super.getUserById(dto.getUserId()), dto);
		} catch (Exception e) {
			LOG.error("VehicleAlertApi findMarkerAndTraceByVehicleNumber failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
	 * 部门异常用车(列表)
	 * @param dto
	 * @return
	 */
	
	@POST
	@Path("/findAlertDataByType")
	@Consumes(MediaType.APPLICATION_JSON)
	public PagModel findAlertDataByType(@Valid @NotNull AlertDataByTypeDto dto) {
		return alertService.findAlertByTypeDepartmentAndTimeRange(super.getUserById(dto.getUserId()), dto);
	}

	/**
	 * 部门异常用车(曲线图)
	 * @param dto
	 * @return
	 */
	@POST
	@Path("/statAlertByType")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<CountModel> statAlertByTypeDepartmentAndTimeRange(@Valid @NotNull AlertDataByTypeDto dto) {
		return alertService.statAlertByTypeDepartmentAndTimeRange(super.getUserById(dto.getUserId()), dto);
	}

	/**
	 * 异常用车统计(饼图与柱状图)
	 * @param dto
	 * @return
	 */
	@POST
	@Path("/findVehicleAlertStatistics")
	@Consumes(MediaType.APPLICATION_JSON)
	public AlertReport findVehicleAlertStatistics(@Valid @NotNull AlertDataByTypeDto dto) {
		return alertService.findVehicleAlertStatistics(super.getUserById(dto.getUserId()), dto);
	}

	@POST
	@Path("/findVehicleAlertCountByDate")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<VehicleAlert> findVehicleAlertCountByDate(@Valid @NotNull AlertVehicleCountDto dto) {
		return alertService.findVehicleAlertCountByDate( dto);
	}
	
	@POST
	@Path("/findVehicleAlertNoPage")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<VehicleAlert> findVehicleAlertNoPage(@Valid @NotNull VehicleAlertNoPageDto dto) {
		return alertService.findVehicleAlert(super.getUserById(dto.getUserId()), dto);
	}
	
	@POST
	@Path("/findVehicleAlertByPage")
	@Consumes(MediaType.APPLICATION_JSON)
	public PagModel findVehicleAlertByPage(@Valid @NotNull VehicleAlertByPage dto) {
		return alertService.findVehicleAlertByPage(super.getUserById(dto.getUserId()), dto);
	}

	@GET
	@Path("/findVehicleAlertCount/{userId}")
	public List<AlertStatisticModel> findVehicleAlertCount(@PathParam("userId") Long userId) {
		return alertService.findVehicleAlertCount(super.getUserById(userId));
	}
	
	/**
	 * 异常用车统计(三种类型汇总导出,三个sheet)
	 * @param dto
	 * @return
	 */
	@POST
	@Path("/exportAll")
	@Produces("application/vnd.ms-excel")
	public Response exportFile(@Valid @NotNull AlertExportAllDto dto) {
		// TODO:
		return null;
		/*try {
			File file = alertService.exportAll(super.getUserById(dto.getUserId()), dto);
			String newFileName = URLEncoder.encode(file.getName(), "UTF-8");

			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=" + newFileName);
			return response.build();
		} catch (Exception e) {
			LOG.error("VehicleAlertApi exportFile error, cause by: ", e);
			WsResponse<String> wsResponse = new WsResponse<>();
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
			return Response.ok(wsResponse).build();
		}*/
	}

}
