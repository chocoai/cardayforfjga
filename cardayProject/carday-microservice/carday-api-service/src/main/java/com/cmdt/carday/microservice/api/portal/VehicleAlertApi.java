package com.cmdt.carday.microservice.api.portal;


import static com.cmdt.carday.microservice.common.Constants.Project_Version;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;

import com.cmdt.carday.microservice.api.BaseApi;
import com.cmdt.carday.microservice.biz.service.PlatformVehicleAlertService;
import com.cmdt.carday.microservice.model.request.alert.AlertDataReportDto;
import com.cmdt.carday.microservice.model.request.alert.AlertDataTypePageReportDto;
import com.cmdt.carday.microservice.model.request.alert.AlertDataTypeReportDto;
import com.cmdt.carday.microservice.model.request.alert.AlertExportDto;
import com.cmdt.carday.microservice.model.request.alert.AlertVehicleCountDto;
import com.cmdt.carrental.common.entity.VehicleAlert;
import com.cmdt.carrental.common.model.AlertReport;
import com.cmdt.carrental.common.model.CountModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.QueryAlertInfoModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

/**
 * @Author: joe
 * @Date: 17-7-20 上午11:44.
 * @Description:
 */
@Api("/vehicle-alert")
@SwaggerDefinition(info = @Info(title = "alert API", version = Project_Version))
@Validated
@RestController
@RequestMapping("/vehicle-alert")
public class VehicleAlertApi extends BaseApi {


    @Autowired
    private PlatformVehicleAlertService vehicleAlertService;

    @ApiOperation(value = "获取车辆报警信息", response = PagModel.class)
    @PostMapping(value = "/user/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PagModel findVehicleAlertInfo(@ApiParam(value = "登录用户ID", required = true)
                                         @PathVariable("userId") @NotNull Long loginUserId,
                                         @ApiParam(value = "报警查询信息", required = true)
                                         @RequestBody @Valid @NotNull QueryAlertInfoModel dto) throws Exception {
        return vehicleAlertService.findAlertByType(getUserById(loginUserId), dto);
    }

    @ApiOperation(value = "导出车辆报警信息", response = Resource.class)
    @GetMapping(value = "/export")
    public ResponseEntity<Resource> generateAlertExport(@ApiParam(value = "登录用户ID", required = true)
                                                        @RequestParam("userId")    @Valid @NotNull Long userId,
                                                        @ApiParam(value = "车辆号码", required = false)
                                                        @RequestParam(value = "vehicleNumber", required = false) String vehicleNumber,
                                                        @ApiParam(value = "车辆类型, -1代表所有类型", required = true)
                                                        @RequestParam("vehicleType") String vehicleType,
                                                        @ApiParam(value = "车辆来源, -1表示所有来源", required = true)
                                                        @RequestParam("fromOrgId") String fromOrgId,
                                                        @ApiParam(value = "部门ID", required = true)
                                                        @RequestParam("deptId") Integer deptId,
                                                        @ApiParam(value = "查询截止日期", required = true)
                                                        @RequestParam("endTime") String endTime,
                                                        @ApiParam(value = "查询开始日期", required = true)
                                                        @RequestParam("startTime") String startTime,
                                                        @ApiParam(value = "报警类型: VEHICLEBACK / OVERSPEED / VEHICLEBACK", required = true)
                                                        @RequestParam("alertType") String alertType) throws Exception {
        AlertExportDto dto = new AlertExportDto();
        dto.setAlertType(alertType);
        dto.setDeptId(deptId);
        dto.setEndTime(endTime);
        dto.setFromOrgId(fromOrgId);
        dto.setStartTime(startTime);
        dto.setUserId(userId);
        dto.setVehicleNumber(vehicleNumber);
        dto.setAlertType(alertType);
        dto.setVehicleType(vehicleType);

        File file = vehicleAlertService.generateAlertExport(getUserById(dto.getUserId()), dto);
        Resource res = new UrlResource(file.toURI());

        // 文件名包含中文，必须进行编码转换，否则前端拿到是乱码
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ URLEncoder.encode(file.getName(),"UTF-8")+"\"")
                .body(res);
    }

//    @ApiOperation(value = "导出车辆报警信息", response = Resource.class)
//    @PostMapping(value = "/export", produces = "application/vnd.ms-excel")
//    public ResponseEntity<Resource> generateAlertExport(@ApiParam(value = "报警导出设置信息", required = true)
//                                                        @RequestBody    @Valid @NotNull AlertExportDto dto) throws Exception {
//        File file = vehicleAlertService.generateAlertExport(getUserById(dto.getUserId()), dto);
//        Resource res = new UrlResource(file.toURI());
//        return ResponseEntity
//                .ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+res.getFilename()+"\"")
//                .body(res);
//    }
    
    
	/**
	 * 异常用车统计饼状图，柱状图
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value=" 异常用车统计_饼状图柱状图",response=AlertReport.class)
	@PostMapping(path="/getPieAndColumnarData",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public AlertReport getPieAndColumnarData(@ApiParam(value="饼状图，柱状图查询参数",required=true) 
			@RequestBody @Valid @NotNull AlertDataReportDto alertDataReportDto) throws Exception {
		return vehicleAlertService.queryVehicleAlertStatisticsTopX(alertDataReportDto);
	}

	/**
	 * 报警事件统计
	 * @param dto
	 * @return
	 */
    @ApiOperation(value="首页报警事件统计",response = List.class)
    @PostMapping(path="/findVehicleAlertCountByDate",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public List<VehicleAlert> findVehicleAlertCountByDate(@ApiParam(value="报警事件统计参数",required=true)  
			@RequestBody @Valid @NotNull AlertVehicleCountDto dto) {
		return vehicleAlertService.findVehicleAlertCountByDate( dto);
	}
    
    /**
	 * 部门异常用车统计_曲线图
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="部门异常用车统计_曲线图",response=List.class)
	@PostMapping(path="/statAlertByType",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public List<CountModel> statAlertByType(@ApiParam(value="曲线图查询参数",required=true) 
			@RequestBody @Valid @NotNull AlertDataTypeReportDto alertDataTypeReportDto) throws Exception {
		return vehicleAlertService.statAlertByType(alertDataTypeReportDto);
	}
	
    /**
	 * 部门异常用车统计_报警列表
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="部门异常用车统计_报警列表",response=PagModel.class)
	@PostMapping(path="/findAlertDataByType",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public PagModel findAlertDataByType(@ApiParam(value="报警列表查询参数",required=true) 
			@RequestBody @Valid @NotNull AlertDataTypePageReportDto alertDataTypePageReportDto) throws Exception {
		return vehicleAlertService.findAlertDataByType(alertDataTypePageReportDto);
	}

}
