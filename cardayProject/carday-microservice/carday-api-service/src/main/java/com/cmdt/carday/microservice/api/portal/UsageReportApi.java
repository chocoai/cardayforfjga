package com.cmdt.carday.microservice.api.portal;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmdt.carday.microservice.api.BaseApi;
import com.cmdt.carday.microservice.biz.service.PlatformUsageReportService;
import com.cmdt.carday.microservice.model.request.organization.OrganizationByIdDto;
import com.cmdt.carday.microservice.model.request.usageReport.UsageReportDto;
import com.cmdt.carday.microservice.model.request.usageReport.UsageReportPageDto;
import com.cmdt.carday.microservice.model.request.usageReport.UsageReportVehDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleNumberQueryDto;
import com.cmdt.carrental.common.model.DrivingDetailedReportModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.UsageReportAllMileageAndFuleconsModel;
import com.cmdt.carrental.common.model.UsageReportLineModel;
import com.cmdt.carrental.common.model.UsageReportModel;
import com.cmdt.carrental.common.model.VehicleStatus;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@Api("/usage/report")
@SwaggerDefinition(info = @Info(contact = @Contact(name = "Wei Cheng", email = "wei.cheng@cm-dt.com"), title = "The usage report api for usageReport-management", version = "1.0.0"))
@Validated
@RestController
@RequestMapping("/usage/report")
public class UsageReportApi extends BaseApi {
	
	private static final Logger LOG = LoggerFactory.getLogger(UsageReportApi.class);
	
	@Autowired
	private PlatformUsageReportService platformUsageReportService;

	/**
	 * 车辆使用统计饼状图，柱状图
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="车辆使用统计_饼状图柱状图",response=UsageReportModel.class)
	@PostMapping(path="/getPieAndColumnarData",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public UsageReportModel getPieAndColumnarData(@ApiParam(value="饼状图，柱状图查询参数",required=true) 
			@RequestBody @Valid @NotNull UsageReportDto usageReportDto) throws Exception {
		LOG.info("Enter UsageReportApi getPieAndColumnarData");
		return platformUsageReportService.getPieAndColumnarData(usageReportDto);
	}
	
	/**
	 * 车辆使用统计曲线图
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="车辆使用统计_曲线图",response=UsageReportLineModel.class)
	@PostMapping(path="/getVehicleLinePropertyData",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public UsageReportLineModel getVehicleLinePropertyData(@ApiParam(value="曲线图查询参数",required=true) 
			@RequestBody @Valid @NotNull UsageReportDto usageReportDto) throws Exception {
		LOG.info("Enter UsageReportApi getVehicleLinePropertyData");
		return platformUsageReportService.getVehicleLinePropertyData(usageReportDto);
	}
	
	/**
	 * 车辆使用统计:车辆统计
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="车辆使用统计_车辆统计",response=PagModel.class)
	@PostMapping(path="/getVehiclePropertyData",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public PagModel getVehiclePropertyData(@ApiParam(value="车辆统计查询参数",required=true) 
			@RequestBody @Valid @NotNull UsageReportPageDto usageReportPageDto) throws Exception {
		LOG.info("Enter UsageReportApi getVehiclePropertyData");
		return platformUsageReportService.getVehiclePropertyData(usageReportPageDto);
	}
	
	/**
	 * 车辆使用统计:车辆统计(导出)
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="车辆使用统计_车辆统计导出",response=Resource.class)
	@GetMapping(path="/exportVehiclePropertyData",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource> exportVehiclePropertyData(@ApiParam(value="车辆统计导出查询参数",required=true) 
			@RequestBody @Valid @NotNull UsageReportDto usageReportDto) throws Exception {
		LOG.info("Enter UsageReportApi exportVehiclePropertyData");
		File file = platformUsageReportService.generateVehiclePropertyDataFile(usageReportDto);
	    Resource res = new UrlResource(file.toURI());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ URLEncoder.encode(file.getName(),"UTF-8")+"\"")
                .body(res);
	}
	
	/**
	 * 未使用车辆统计
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="未使用车辆统计",response=PagModel.class)
	@PostMapping(path="/getIdleVehicleList",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public PagModel getIdleVehicleList(@ApiParam(value="未使用车辆统计查询参数",required=true) 
			@RequestBody @Valid @NotNull UsageReportPageDto usageReportPageDto) throws Exception {
		LOG.info("Enter UsageReportApi getIdleVehicleList");
		return platformUsageReportService.getIdleVehicleList(usageReportPageDto);
	}
	
	/**
	 * 未使用车辆统计(导出)
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="未使用车辆统计导出",response=Resource.class)
	@GetMapping(path="/exportIdleVehicleList",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource> exportIdleVehicleList(@ApiParam(value="未使用车辆统计导出查询参数",required=true) 
			@RequestBody @Valid @NotNull UsageReportDto usageReportDto) throws Exception {
		LOG.info("Enter UsageReportApi exportIdleVehicleList");
		File file = platformUsageReportService.generateIdleVehicleListFile(usageReportDto);
	    Resource res = new UrlResource(file.toURI());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ URLEncoder.encode(file.getName(),"UTF-8")+"\"")
                .body(res);
	}
	
	
	
	/**
	 * 车辆行驶明细
	 * @param usageReportVehDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="车辆行驶明细",response=List.class)
	@PostMapping(path="/getDrivingDetailedReport",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public List<DrivingDetailedReportModel> getDrivingDetailedReport(@ApiParam(value="车辆行驶明细查询参数",required=true) 
			@RequestBody @Valid @NotNull UsageReportVehDto usageReportVehDto) throws Exception {
		LOG.info("Enter UsageReportApi getDrivingDetailedReport");
		return platformUsageReportService.getDrivingDetailedReport(usageReportVehDto);
	}
	
	/**
	 * 车辆行驶明细(导出)
	 * @param usageReportVehDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="车辆行驶明细导出",response=Resource.class)
	@GetMapping(path="/exportDrivingDetailedReport",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Resource> exportDrivingDetailedReport(@ApiParam(value="车辆行驶明细导出查询参数",required=true) 
			@RequestBody @Valid @NotNull UsageReportVehDto usageReportVehDto) throws Exception {
		LOG.info("Enter UsageReportApi exportDrivingDetailedReport");
		File file = platformUsageReportService.generateDrivingDetailedReportFile(usageReportVehDto);
	    Resource res = new UrlResource(file.toURI());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ URLEncoder.encode(file.getName(),"UTF-8")+"\"")
                .body(res);
	}
	
	/**
	 * 车辆信息汇总展示并10秒刷新
	 * @param userId
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="车辆信息汇总展示并10秒刷新")
	@PostMapping(path="/findVehicleStatusData",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public VehicleStatus findVehicleStatusData(@ApiParam(value="用户ID和部门ID",required=true) 
			@RequestBody @Valid @NotNull OrganizationByIdDto dto) throws Exception{
		return platformUsageReportService.findVehicleStatusData(super.getUserById(dto.getUserId()),dto.getId());
	}
	
	@ApiOperation(value="车辆数据统计",response = VehicleStatus.class)
	@PostMapping(path="/getVehicleAllMileageAndFuleconsListByVehNum",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public UsageReportAllMileageAndFuleconsModel getVehicleAllMileageAndFuleconsListByVehNum(@ApiParam(value="车牌号查询车辆信息",required=true) 
		@RequestBody @Valid @NotNull VehicleNumberQueryDto dto) throws Exception{
		return platformUsageReportService.getVehicleAllMileageAndFuleconsListByVehNum(super.getUserById(dto.getUserId()),dto.getVehicleNumber());
	}
	

}
