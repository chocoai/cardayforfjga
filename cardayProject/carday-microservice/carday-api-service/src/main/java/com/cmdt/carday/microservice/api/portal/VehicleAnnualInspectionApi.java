package com.cmdt.carday.microservice.api.portal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmdt.carday.microservice.api.BaseApi;
import com.cmdt.carday.microservice.biz.service.PlatformVehicleAnnualInspectionService;
import com.cmdt.carday.microservice.common.model.response.WsResponse;
import com.cmdt.carday.microservice.model.request.annual.AnnualInspectionPortalDto;
import com.cmdt.carday.microservice.model.request.annual.ResetInspectionDto;
import com.cmdt.carday.microservice.model.request.annual.ResetInsuranceDto;
import com.cmdt.carrental.common.model.PagModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@Api("/vehicleAnnualInspection")
@SwaggerDefinition(info = @Info(title = "vehicleAnnualInspection API", version = "1.0.0"))
@Validated
@RestController  
@RequestMapping("/vehicleAnnualInspection")
public class VehicleAnnualInspectionApi extends BaseApi {

	@Autowired
	private PlatformVehicleAnnualInspectionService annualInspectionService;

	/**
	 * 根据组织机构树以及其他条件查询车辆年检
	 * 
	 * @param dto
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/listPage")
	@ApiOperation(value = "根据组织机构树以及其他条件查询车辆年检", response = WsResponse.class)
	public PagModel listPage(@RequestBody @Valid @NotNull AnnualInspectionPortalDto dto) throws Exception {
		return annualInspectionService.listPage(getUserById(dto.getUserId()), dto);
	}

	/**
	 * 重置保险时间
	 * 
	 * @param dto
	 * @return
	 */

	@PutMapping("/resetInsuranceTime")
	@ApiOperation(value = "重置保险时间", response = WsResponse.class)
	public boolean resetInsuranceTime(@RequestBody @Valid @NotNull ResetInsuranceDto dto) {
		return annualInspectionService.resetInsuranceTime(getUserById(dto.getUserId()), dto);
	}
	
	/**
	 * 重置年检时间
	 * @param dto
	 * @return
	 */
	
	@PutMapping("/resetInspectionTime")
	@ApiOperation(value = "重置年检时间", response = WsResponse.class)
	public boolean resetInspectionTime(@RequestBody @Valid @NotNull ResetInspectionDto dto) {
		return annualInspectionService.resetInspectionTime(getUserById(dto.getUserId()), dto);
	}
}
