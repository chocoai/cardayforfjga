package com.cmdt.carday.microservice.api.portal;

import java.io.File;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cmdt.carday.microservice.api.BaseApi;
import com.cmdt.carday.microservice.biz.service.PlatformFileService;
import com.cmdt.carday.microservice.biz.service.PlatformVehicleMaintenanceService;
import com.cmdt.carday.microservice.common.model.response.WsResponse;
import com.cmdt.carday.microservice.model.request.maintenance.MaintenanceCreateDto;
import com.cmdt.carday.microservice.model.request.maintenance.MaintenanceExportDto;
import com.cmdt.carday.microservice.model.request.maintenance.MaintenanceListDto;
import com.cmdt.carday.microservice.model.request.maintenance.MaintenanceResetDto;
import com.cmdt.carday.microservice.model.request.maintenance.MaintenanceThresholdDto;
import com.cmdt.carday.microservice.storage.StorageService;
import com.cmdt.carrental.common.entity.VehicleMaintenance;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleDataCountModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@Api("/vehiclemaintenance")
@SwaggerDefinition(info = @Info(contact = @Contact(name = "zhang ying",
        email = "ying.zhang@cm-dt.com"),
        title = "The vehiclemaintenance api for vehiclemaintenance-management", 
        version = "1.0.0"))
@Validated
@RestController
@RequestMapping("/vehiclemaintenance")
public class VehicleMaintenanceApi extends BaseApi{
    
	@Autowired
	PlatformVehicleMaintenanceService platformVehicleMaintenanceService;
	
	private final StorageService storageService;
	
	@Autowired
	private PlatformFileService fileService;
	
	@Autowired
	public VehicleMaintenanceApi(StorageService storageService) {
		this.storageService = storageService;
	}
	
	@ApiOperation(value = "查询车辆维保信息", response = PagModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PagModel list(@ApiParam(value = "车辆维保列表查询", required = true)
                                    @RequestBody @Valid @NotNull MaintenanceListDto dto){
        return platformVehicleMaintenanceService.listVehicleMaintencance(dto);
    }
	
	@ApiOperation(value = "（首页）显示次月内到期的保养、保险、年检车辆", response = VehicleDataCountModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping(value = "/{userId}/queryVehicleDataCount",produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleDataCountModel queryVehicleDataCount(@ApiParam(value = "登录用户id", required = true) @PathVariable("userId") Long userId){
        return platformVehicleMaintenanceService.queryVehicleDataCountByHomePage(getUserById(userId));
	}
	
	@ApiOperation(value = "新增保养记录", response = VehicleMaintenance.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleMaintenance create(@ApiParam(value = "新增保养记录", required = true)
                                    @RequestBody @Valid @NotNull MaintenanceCreateDto dto){
		return platformVehicleMaintenanceService.createMaintenance(getUserById(dto.getUserId()), dto);
    }
	
	@ApiOperation(value = "重置保养记录", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/reset", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean reset(@ApiParam(value = "新增保养记录", required = true)
                                    @RequestBody @Valid @NotNull MaintenanceResetDto dto){
		return platformVehicleMaintenanceService.reset(dto);
    }
	
	@ApiOperation(value = "设置阈值", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/setThreshold", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean setThreshold(@ApiParam(value = "新增保养记录", required = true)
                                    @RequestBody @Valid @NotNull MaintenanceThresholdDto dto){
		return platformVehicleMaintenanceService.setThreshold(dto);
    }
	
	@ApiOperation(value = "模板下载", response = WsResponse.class)
    @GetMapping("/loadTemplate")
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(HttpServletRequest request) {
		String relativePath = fileService.getTemplateFilePath("maintenance");
		Resource file = storageService.loadAsResource(request.getSession().getServletContext().getRealPath(relativePath));
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
				.body(file);
	}
	
	@PostMapping("/import")
	@ApiOperation(value = "批量导入车辆维保信息", response = WsResponse.class)
	public String importMaintenance(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "userId", required = true)
			@Digits(message="UserId格式错误", fraction = 0, integer = 10) Long userId) throws Exception {
		return platformVehicleMaintenanceService.importVehicleMaintainFile(getUserById(userId), file);
	}
	
	@ApiOperation(value = "导出车辆维保信息", response = Resource.class)
    @GetMapping(value = "/export")
    public ResponseEntity<Resource> exportVehicleMaintain(
    		@ApiParam(value = "登录用户ID", required = true) @RequestParam("userId")@Valid @NotNull Long userId,
            @ApiParam(value = "车牌号", required = true) @RequestParam(value = "vehicleNumber") String vehicleNumber,
            @ApiParam(value = "所属部门id", required = true) @RequestParam("deptId") Long deptId,
            @ApiParam(value = "ids", required = false) @RequestParam("ids") String ids) throws Exception {
		MaintenanceExportDto dto = new MaintenanceExportDto();
		dto.setUserId(userId);
		dto.setDeptId(deptId);
		dto.setVehicleNumber(vehicleNumber);
		dto.setIds(ids);
		File file = platformVehicleMaintenanceService.
				generateVehicleMaintainExport(getUserById(dto.getUserId()), dto);
        Resource res = new UrlResource(file.toURI());
        //文件名包含中文，必须进行编码转换，否则前端拿到是乱码
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ URLEncoder.encode(file.getName(),"UTF-8")+"\"")
                .body(res);
    }
    
}
