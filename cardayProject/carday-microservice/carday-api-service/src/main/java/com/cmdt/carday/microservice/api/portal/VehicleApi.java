package com.cmdt.carday.microservice.api.portal;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cmdt.carday.microservice.api.BaseApi;
import com.cmdt.carday.microservice.biz.service.PlatformVehicleService;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleAssignDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleCreateDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleHistoryTrackeDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleListDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleMarkerPageDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleNumberQueryDto;
import com.cmdt.carday.microservice.model.request.vehicle.VehicleUpdateDto;
import com.cmdt.carrental.common.entity.Marker;
import com.cmdt.carrental.common.entity.Vehicle;
import com.cmdt.carrental.common.model.HomePageMapModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehMoniTreeStatusNode;
import com.cmdt.carrental.common.model.VehicleHistoryTrack;
import com.cmdt.carrental.common.model.VehicleLocationModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carday.microservice.model.request.vehicle.LocationDto;
import com.cmdt.carday.microservice.model.request.vehicle.NavTreeForMainPageDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@Api("/vehicle")
@SwaggerDefinition(info = @Info(contact = @Contact(name = "Sen Li", email = "sen.li@cm-dt.com"), title = "The vehicle api for vehicle-management", version = "1.0.0"))
@Validated
@RestController
@RequestMapping("/vehicle")
public class VehicleApi extends BaseApi {
	
	private static final Logger LOG = LoggerFactory.getLogger(VehicleApi.class);
	
	@Autowired
	private PlatformVehicleService platformVehicleService;

	/**
	 * 根据搜索条件查询所有车辆列表信息
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="根据搜索条件查询所有车辆列表信息",response=PagModel.class)
	@PostMapping(path="/list",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public PagModel list(@ApiParam(value="车辆列表参数",required=true) 
			@RequestBody @Valid @NotNull VehicleListDto vehicleListDto) throws Exception {
		LOG.info("Enter VehicleApi list");
		return platformVehicleService.listVehicle(super.getUserById(vehicleListDto.getUserId()), vehicleListDto);
	}
	
	/**
	 * 企业管理员增加车辆
	 * 
	 * @param dto
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value="增加车辆",response=Vehicle.class)
	@PostMapping(path="/create",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public Vehicle create(@ApiParam(value="新增车辆参数",required=true)
			@RequestBody @Valid @NotNull VehicleCreateDto dto) throws Exception {
		LOG.info("Enter VehicleApi create vehicle");
		return platformVehicleService.createVehicle(super.getUserById(dto.getUserId()), dto);
		
	}
	
	/**
	 *查看车辆信息
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(value="查看车辆信息",response=Vehicle.class)
	@GetMapping(path="/findVehicleById/{id}")
	public Vehicle findVehicleById(@ApiParam(value="车辆id",required=true) @NotNull @PathVariable("id") Long id) {
		LOG.info("Enter VehicleApi findVehicleById");
		return platformVehicleService.getVehicleById(id);
	}
	
	/**
	 * 修改车辆
	 * 
	 * @param dto
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value="修改车辆",response=Boolean.class)
	@PutMapping(path="/update",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public Boolean update(@ApiParam(value="修改车辆参数",required=true)
			@RequestBody @Valid @NotNull VehicleUpdateDto dto) throws Exception {
		LOG.info("Enter VehicleApi update");
		platformVehicleService.updateVehicle(dto);
		return true;
		
	}
	
	/**
	 * 分配车辆至所属部门或收回
	 * 
	 * @param dto
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value="分配车辆至所属部门或收回",response=Boolean.class)
	@PostMapping(path="/vehicleAssigneOrg",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public boolean vehicleAssigneOrg(@ApiParam(value="车辆分配至所属部门或收回参数",required=true)
			@RequestBody @Valid @NotNull VehicleAssignDto dto) throws Exception {
		LOG.info("Enter VehicleApi vehicleAssigneOrg");
		return platformVehicleService.vehicleAssigne(dto) > 0 ? true : false;
		
	}
	
	/**
	 * 给车辆分配司机
	 * 
	 * @param dto
	 * @return
	 */
	@ApiOperation(value="分配司机",response=Boolean.class)
	@GetMapping(path="/driverAllocate/vehicleId/{vehicleId}/driverId/{driverId}",produces=MediaType.APPLICATION_JSON_VALUE)
	public boolean driverAllocate(@ApiParam(value="车辆Id",required=true) @PathVariable("vehicleId") Long vehicleId,
			@ApiParam(value="司机Id",required=true) @PathVariable("driverId") Long driverId) {
		LOG.info("Enter VehicleApi driverAllocate");
		return platformVehicleService.driverAllocate(vehicleId,driverId) > 0 ? true : false;
		
	}

	/**
	 * 根据vehicle number 查询已分配marker
	 * @param vehicleMarkerPageDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="根据vehicle number 查询已分配marker",response=PagModel.class)
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
	@PostMapping(path="/findVehicleAssignedMarkers",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public PagModel findVehicleAssignedMarkers(@ApiParam(value="车牌号",required=true) @NotNull @RequestParam("vehicleNumber") String vehicleNumber,
			@ApiParam(value="分页，当前页码",required=true) @NotNull @Min(1) @RequestParam("currentPage") Integer currentPage,
			@ApiParam(value="分页，每页条数",required=true) @NotNull @Min(1) @RequestParam("numPerPage") Integer numPerPage
			) throws Exception{
		LOG.info("Enter VehicleApi findVehicleAssignedMarkers");
		return platformVehicleService.findVehicleAssignedMarkers(vehicleNumber,currentPage,numPerPage);
	}
	
	/**
	 * 查询可分配marker
	 * @param vehicleMarkerPageDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="查询可分配marker",response=PagModel.class)
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
	@PostMapping(path="/findVehicleAvialiableMarkers",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public PagModel findVehicleAvialiableMarkers( @ApiParam(value="可分配marker参数",required=true)
				@RequestBody @Valid @NotNull  VehicleMarkerPageDto vehicleMarkerPageDto) throws Exception{
		return platformVehicleService.findVehicleAvialiableMarkers(super.getUserById(vehicleMarkerPageDto.getUserId()),vehicleMarkerPageDto);
	}
	
	/**
	 * 为车辆分配marker
	 * @param vehicleAssignMarkerDto
	 * @return
	 */
	@ApiOperation(value="为车辆分配marker",response=Marker.class)
	@ApiResponses(value = {
	            @ApiResponse(code = 200, message = "Success"),
	            @ApiResponse(code = 400, message = "Bad request"),
	            @ApiResponse(code = 500, message = "Internal Server Error")})
	@PostMapping(path="/assignMarkers",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public Boolean assignMarkers( @ApiParam(value="车辆ID",required=true) @NotNull @RequestParam("vehicleId") Long vehicleId,
			 @ApiParam(value="分配marker Ids，以逗号分隔",required=true) @NotNull @RequestParam("markerIds") String markerIds){
		LOG.info("Enter VehicleApi assignVehicles");
		platformVehicleService.assignMarkers(vehicleId,markerIds);
		return true;
		
	}
	

	/**
	 * 移除车辆分配的marker
	 * @param vehicleUnassignMarkerDto
	 * @return
	 */
	@ApiOperation(value="移除车辆分配的marker",response=Marker.class)
	@ApiResponses(value = {
	            @ApiResponse(code = 200, message = "Success"),
	            @ApiResponse(code = 400, message = "Bad request"),
	            @ApiResponse(code = 500, message = "Internal Server Error")})
	@PostMapping(path="/unassignMarkers",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public Boolean unassignVehicles(@ApiParam(value="车辆ID",required=true) @NotNull @RequestParam("vehicleId") Long vehicleId,
			 @ApiParam(value="移除的markerId",required=true) @NotNull @RequestParam("markerId") Long markerId){
		LOG.info("Enter VehicleApi unassignMarkers");
		platformVehicleService.unassignMarkers(vehicleId,markerId);
		return true;
		
	}
	
	/**
	 * 批量导入车辆
	 * @param file
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="批量导入车辆",response=String.class)
	@PostMapping(path="/batchImportVehicle")
	public String batchImportVehicle(@ApiParam(value="文件",required=true) @RequestParam("file") MultipartFile file,
				@ApiParam(value="登录用户Id",required=true) @NotNull @RequestParam("userId") Long userId) throws Exception{
		LOG.info("Enter VehicleApi batchImportVehicle");
		return platformVehicleService.batchImportVehicle(super.getUserById(userId),file);
	}
	
	/**
	 * 根据部门Id查询车辆
	 * @param orgId
	 * @return
	 */
	@ApiOperation(value="根据部门Id查询车辆")
	@GetMapping(path="/listDeptVehicle/{orgId}",produces=MediaType.APPLICATION_JSON_VALUE)
	public  List<VehicleModel> listDeptVehicles(@ApiParam(value="部门Id",required=true) @NotNull @PathVariable("orgId") Long orgId){
		LOG.info("Enter VehicleApi listDeptVehicles");
		return platformVehicleService.listDeptVehicles(orgId);
		
	}

	/**
	 * 根据车牌号查询车辆信息
	 * @param userId
	 * @param vehicleNumber
	 * @return
	 */
	@ApiOperation(value="根据车牌号查询车辆信息")
	@PostMapping(path="/findVehicleByVehicleNumber",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public VehicleModel findVehicleInfoByVehicleNumber(@ApiParam(value="车牌号查询车辆信息",required=true) 
				@RequestBody @Valid @NotNull VehicleNumberQueryDto dto){
		LOG.info("Enter VehicleApi findVehicleInfoByVehicleNumber");
		return platformVehicleService.findVehicleInfoByVehicleNumber(super.getUserById(dto.getUserId()),dto.getVehicleNumber());
		
	}

	/**
	 * 左侧导航多级组织架构及车辆展示
	 * @param userId
	 * @param vehicleNumber
	 * @return
	 */
	@ApiOperation(value="左侧导航多级组织架构及车辆展示",response=Map.class)
	@PostMapping(path="/listVehMoniStatusTree",produces=MediaType.APPLICATION_JSON_VALUE)
	public List<VehMoniTreeStatusNode> listVehMoniStatusTreeData(@ApiParam(value="导航树与车辆参数",required=true) 
			@RequestBody @Valid @NotNull NavTreeForMainPageDto dto) {
		LOG.info("Enter VehicleApi listVehMoniStatusTreeData");
		return platformVehicleService.listVehMoniStatusTreeData(super.getUserById(dto.getUserId()), dto.getVehicleNumber());
	}
	
	/**
	 * 查询多个OBD位置信息(或者说车辆位置信息)
	 * 
	 * @param dto
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value="实时定位",response = HomePageMapModel.class)
	@PostMapping(path="/queryObdLocation", consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public HomePageMapModel queryObdLocation(@ApiParam(value="实时定位参数",required=true) 
		@RequestBody @Valid @NotNull LocationDto dto) throws Exception {
		LOG.info("Enter VehicleApi queryObdLocation");
		return platformVehicleService.queryObdLocation(super.getUserById(dto.getUserId()), dto);
		
	}
	
	/**
	 * 查询车辆历史轨迹
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="历史轨迹",response = List.class)
	@PostMapping(path="/monitor/findVehicleHistoryTrack",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public List<VehicleHistoryTrack> findVehicleHistoryTrack(@ApiParam(value="查询轨迹参数",required=true)  
			@RequestBody @Valid @NotNull VehicleHistoryTrackeDto dto) throws Exception {
		LOG.info("Enter VehicleApi findVehicleHistoryTrack");
		return platformVehicleService.findVehicleHistoryTrack(dto);
		
	}
	
	/**
	 * 查询单个OBD位置信息(或者说车辆位置信息)(redis调用)
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value="查询单个OBD位置信息(或者说车辆位置信息)",response = HomePageMapModel.class)
	@GetMapping(path="/queryObdLocationByImei/{deviceNumber}", produces=MediaType.APPLICATION_JSON_VALUE)
	public VehicleLocationModel queryObdLocationByImei(@ApiParam(value="设备号",required=true) 
	 	@NotNull @PathVariable("deviceNumber") String deviceNumber ) throws Exception {
		LOG.info("Enter VehicleApi queryObdLocationByImei");
		return platformVehicleService.queryObdLocationByImei(deviceNumber);
		
	}
	
	/*@ApiOperation(value="查询单个OBD位置信息(或者说车辆位置信息)",response = HomePageMapModel.class)
	@PostMapping(path="/findTripPropertyDataByTimeRange", produces=MediaType.APPLICATION_JSON_VALUE)
	public VehicleLocationModel findTripPropertyDataByTimeRange(@ApiParam(value="设备号",required=true) ) throws Exception {
		LOG.info("Enter VehicleApi queryObdLocationByImei");
		return platformVehicleService.findTripPropertyDataByTimeRange(deviceNumber);
		
	}*/
	
	
}
