package com.cmdt.carrental.platform.service.api.portal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformVehicleAnnualInspectionService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.platform.service.model.request.annual.AnnualInspectionPortalDto;
import com.cmdt.carrental.platform.service.model.request.annual.ResetInspectionDto;
import com.cmdt.carrental.platform.service.model.request.annual.ResetInsuranceDto;

@Produces(MediaType.APPLICATION_JSON)
public class VehicleAnnualInspectionApi extends BaseApi {

	private static final Logger LOG = LoggerFactory.getLogger(VehicleAnnualInspectionApi.class);

	@Autowired
	private PlatformVehicleAnnualInspectionService annualInspectionService;

	/**
	 * 根据组织机构树以及其他条件查询车辆年检
	 * 
	 * @param dto
	 * @return
	 */

	@POST
	@Path("/listPage")
	@Consumes(MediaType.APPLICATION_JSON)
	public PagModel listPage(@Valid @NotNull AnnualInspectionPortalDto dto) {
		try {
			return annualInspectionService.listPage(getUserById(dto.getUserId()), dto);
		} catch (Exception e) {
			LOG.error("VehicleAnnualInspectionApi list failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

	/**
	 * 重置保险时间
	 * 
	 * @param dto
	 * @return
	 */

	@POST
	@Path("/resetInsuranceTime")
	@Consumes(MediaType.APPLICATION_JSON)
	public String resetInsuranceTime(@Valid @NotNull ResetInsuranceDto dto) {
		String msg="failure";
		try {
			if (annualInspectionService.resetInsuranceTime(getUserById(dto.getUserId()), dto)) {
				msg="success";
			}
		} catch (Exception e) {
			LOG.error("VehicleAnnualInspectionApi resetInsuranceTime failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
		return msg;
	}
	
	/**
	 * 重置年检时间
	 * @param dto
	 * @return
	 */
	
	@POST
	@Path("/resetInspectionTime")
	@Consumes(MediaType.APPLICATION_JSON)
	public String resetInspectionTime(@Valid @NotNull ResetInspectionDto dto) {
		String msg="failure";
		try {
			if (!annualInspectionService.resetInspectionTime(getUserById(dto.getUserId()), dto)) {
				msg="success";
			}
		} catch (Exception e) {
			LOG.error("VehicleAnnualInspectionApi resetInspectionTime failed, error : ", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
		return msg;
	}
}
