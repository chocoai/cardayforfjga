package com.cmdt.carrental.platform.service.api.portal;

import com.cmdt.carrental.common.entity.VehicleMaintenance;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleDataCountModel;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformFileService;
import com.cmdt.carrental.platform.service.biz.service.PlatformVehicleMaintenanceService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.common.WsResponse;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.platform.service.model.request.maintenance.MaintenanceCreateDto;
import com.cmdt.carrental.platform.service.model.request.maintenance.MaintenanceExportDto;
import com.cmdt.carrental.platform.service.model.request.maintenance.MaintenanceListDto;
import com.cmdt.carrental.platform.service.model.request.maintenance.MaintenanceThresholdDto;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.phase.PhaseInterceptorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.activation.DataHandler;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.io.File;
import java.net.URLEncoder;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
public class VehicleMaintenanceApi extends BaseApi {

	private static final Logger LOG = LoggerFactory.getLogger(VehicleMaintenanceApi.class);

	@Autowired
	private PlatformVehicleMaintenanceService platformVehicleMaintenanceService;

	@Autowired
	private PlatformFileService fileService;

	/**
	 * 根据组织机构树以及其他条件查询车辆维保
	 * 
	 * @param listDto
	 * @return
	 */
	@POST
	@Path("/list")
	@Consumes(MediaType.APPLICATION_JSON)
	public PagModel list(@Valid @NotNull MaintenanceListDto listDto) {
		try {
			return platformVehicleMaintenanceService.listVehicleMaintencance(super.getUserById(listDto.getUserId()),
					listDto);
		} catch (Exception e) {
			LOG.error("VehicleMaintenanceApi list failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * （首页）显示次月内到期的保养、保险、年检车辆
	 * 
	 * @param userId
	 * @return
	 */
	@GET
	@Path("/{userId}/queryVehicleDataCount")
	public VehicleDataCountModel queryVehicleDataCount(@PathParam("userId") Long userId) {
		return platformVehicleMaintenanceService.queryVehicleDataCountByHomePage(super.getUserById(userId));
	}

	/**
	 * 新增保养记录
	 * 
	 * @param listDto
	 * @return
	 */
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public String create(@Valid @NotNull MaintenanceCreateDto listDto) {
		try {
			VehicleMaintenance maint = platformVehicleMaintenanceService.createMaintenance(super.getUserById(listDto.getUserId()), listDto);
			if (StringUtils.isEmpty(maint.getMessage())) {
				return null;
			} else {
				throw new ServerException(maint.getMessage());
			}
		} catch (ServerException e) {
			LOG.error("VehicleMaintenanceApi create, error : ", e);

			throw e;
		} catch(Exception e) {
			LOG.error("VehicleMaintenanceApi create, error : ", e);

			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * 重置保养记录
	 * 
	 * @param listDto
	 * @return
	 */
	@POST
	@Path("/reset")
	@Consumes(MediaType.APPLICATION_JSON)
	public String reset(@Valid @NotNull MaintenanceCreateDto listDto) {
		try {
			String msg = platformVehicleMaintenanceService.reset(listDto);
			if (StringUtils.isEmpty(msg)) {
				return null;
			} else {
				throw new ServerException(msg);
			}
		} catch (ServerException e) {
			LOG.error("VehicleMaintenanceApi create, error : ", e);

			throw e;
		} catch(Exception e) {
			LOG.error("VehicleMaintenanceApi create, error : ", e);

			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * 设置阈值
	 * 
	 * @param listDto
	 * @return
	 */
	@POST
	@Path("/setThreshold")
	@Consumes(MediaType.APPLICATION_JSON)
	public String setThreshold(@Valid @NotNull MaintenanceThresholdDto listDto) {
		String msg="failure";
		try {
			if (platformVehicleMaintenanceService.setThreshold(listDto) == 1) {
				msg = "success";
			} 
		} catch (Exception e) {
			LOG.error("VehicleMaintenanceApi reset, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
		return msg;
	}

	/**
	 * 模板下载
	 * 
	 * @return
	 */
	@GET
	@Path("/loadTemplate")
	@Produces("application/vnd.ms-excel")
	public Response downloadFile() {
		String relativePath = File.separator + "template" + File.separator + "maintenance" + File.separator
				+ "template.xls";
		try {
			String newFileName = URLEncoder.encode("template.xls", "UTF-8");
			File file = fileService.downloadTempalte(relativePath);
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename=" + newFileName);
			PhaseInterceptorChain.getCurrentMessage().getExchange().put(Constants.INTECEPTOR_SKIP_FLAG, Boolean.TRUE);
			return response.build();
		} catch (Exception e) {
			LOG.error("VehicleMaintenanceApi downloadFile error, cause by: ", e);
			WsResponse<String> wsResponse = new WsResponse<>();
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
			return Response.ok(wsResponse).build();
		}
	}

	/**
	 * 导入
	 * 
	 * @param attachments
	 * @param userId
	 * @return
	 */
	@POST
	@Path("/import")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON })
	public Response importVehicleMaintain(List<Attachment> attachments,
			@Multipart(value = "userId", type = "text/plain", required = true) @Digits(message = "UserId格式错误", fraction = 0, integer = 10) Long userId) {
		try {
			if (attachments.size() == 2) {
				String result = "";
				for (Attachment attach : attachments) {
					if (StringUtils.getFilenameExtension(attach.getDataHandler().getName()) != null && !"text/plain"
							.equals(StringUtils.getFilenameExtension(attach.getDataHandler().getContentType()))) {
						DataHandler handler = attach.getDataHandler();
						result = platformVehicleMaintenanceService.importVehicleMaintainFile(super.getUserById(userId),
								handler);
						break;
					}
				}
				WsResponse<String> wsResponse = new WsResponse<>();
				wsResponse.setResult(result);
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
				return Response.ok(wsResponse).build();
			} else {
				WsResponse<String> wsResponse = new WsResponse<>();
				wsResponse.getMessages().add("必须上传且只能上传一个文件！");
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
				wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
				return Response.ok(wsResponse).build();
			}
		} catch (Exception e) {
			LOG.error("VehicleMaintenanceApi importVehicleMaintain error, cause by: ", e);
			WsResponse<String> wsResponse = new WsResponse<>();
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
			return Response.ok(wsResponse).build();
		}

	}

	/**
	 * 导出
	 * 
	 * @param dto
	 * @return
	 */
	@POST
	@Path("/export")
	@Produces("application/vnd.ms-excel")
	public Response exportVehicleMaintain(@Valid @NotNull MaintenanceExportDto dto) {

		try {
			File file = platformVehicleMaintenanceService
					.generateVehicleMaintainExport(super.getUserById(dto.getUserId()), dto);
			if (file != null) {
				String newFileName = URLEncoder.encode(file.getName(), "UTF-8");

				ResponseBuilder response = Response.ok((Object) file);
				response.header("Content-Disposition", "attachment; filename=" + newFileName);
				return response.build();
			}
			WsResponse<Object> wsResponse = new WsResponse<>();
			wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
			wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
			return Response.ok(wsResponse).build();

		} catch (Exception e) {
			LOG.error("VehicleAlertApi exportFile error, cause by: ", e);
			WsResponse<String> wsResponse = new WsResponse<>();
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
			return Response.ok(wsResponse).build();
		}
	}
}
