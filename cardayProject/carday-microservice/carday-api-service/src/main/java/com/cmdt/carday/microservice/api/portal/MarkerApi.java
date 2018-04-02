package com.cmdt.carday.microservice.api.portal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmdt.carday.microservice.api.BaseApi;
import com.cmdt.carday.microservice.biz.service.PlatformMarkerService;
import com.cmdt.carday.microservice.model.request.marker.MarkerAssignVehicleDto;
import com.cmdt.carday.microservice.model.request.marker.MarkerCreateDto;
import com.cmdt.carday.microservice.model.request.marker.MarkerPageDto;
import com.cmdt.carday.microservice.model.request.marker.MarkerUnassignVehicleDto;
import com.cmdt.carday.microservice.model.request.marker.MarkerUpdateDto;
import com.cmdt.carday.microservice.model.request.marker.MarkerVehiclePageDto;
import com.cmdt.carrental.common.entity.Marker;
import com.cmdt.carrental.common.model.PagModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;


@Api("/marker")
@SwaggerDefinition(info = @Info(contact = @Contact(name = "Sen Li", email = "sen.li@cm-dt.com"), title = "The marker api for marker-management", version = "1.0.0"))
@Validated
@RestController
@RequestMapping("/marker")
public class MarkerApi extends BaseApi{
	private static final Logger LOG = LoggerFactory.getLogger(MarkerApi.class);
	
	@Autowired
	private PlatformMarkerService platformMarkerService;
	
	/**
	 * 根据Marker名查询Marker
	 * @param markerPageDto
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value="根据Marker名查询Marker",response=PagModel.class)
	@ApiResponses(value = {
	            @ApiResponse(code = 200, message = "Success"),
	            @ApiResponse(code = 400, message = "Bad request"),
	            @ApiResponse(code = 500, message = "Internal Server Error")})
	@PostMapping(path="/findByName",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public PagModel listMarker(@ApiParam(value="marker分页查询参数",required=true) 
			@RequestBody @Valid @NotNull MarkerPageDto markerPageDto) throws Exception{
		LOG.info("Enter MarkerApi list");
		return platformMarkerService.listMarker(getUserById(markerPageDto.getUserId()),markerPageDto);
		
	}
	
	/**
	 * 新增Marker
	 * 
	 * @param markerCreateDto
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value="新增Marker",response=Marker.class)
	@ApiResponses(value = {
	            @ApiResponse(code = 200, message = "Success"),
	            @ApiResponse(code = 400, message = "Bad request"),
	            @ApiResponse(code = 500, message = "Internal Server Error")})
	@PostMapping(path="/create",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public Marker createMarker(@ApiParam(value="创建marker参数",required=true) 
			@RequestBody @Valid @NotNull MarkerCreateDto markerCreateDto) throws Exception{
		LOG.info("Enter MarkerApi createMarker");
		return platformMarkerService.createMarker(getUserById(markerCreateDto.getUserId()),markerCreateDto);
	}
	

	/**
	 * 根据id查询Marker
	 * 
	 * @param id
	 * @return
	 */
	@ApiOperation(value="根据id查询Marker",response=Marker.class)
	@ApiResponses(value = {
	            @ApiResponse(code = 200, message = "Success"),
	            @ApiResponse(code = 400, message = "Bad request"),
	            @ApiResponse(code = 500, message = "Internal Server Error")})
	@GetMapping(path="/findMarkerById/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
	public Marker findMarkerById(@ApiParam(value="marker id ",required=true) @PathVariable("id")  @NotNull Long id){
		LOG.info("Enter MarkerApi findById");
		return platformMarkerService.findMarkerById(id);
	}
	
	/**
	 * 修改Marker信息
	 * @param markerUpdateDto
	 * @return
	 */
	@ApiOperation(value="修改Marker信息",response=Marker.class)
	@ApiResponses(value = {
	            @ApiResponse(code = 200, message = "Success"),
	            @ApiResponse(code = 400, message = "Bad request"),
	            @ApiResponse(code = 500, message = "Internal Server Error")})
	@PutMapping(path="/update",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public Marker updateMarker(@ApiParam(value="更新marker参数",required=true) 
			@RequestBody @Valid @NotNull MarkerUpdateDto markerUpdateDto){
		LOG.info("Enter MarkerApi updateMarker");
		return platformMarkerService.updateMarker(getUserById(markerUpdateDto.getUserId()),markerUpdateDto);
	}
	

	/**
	 * 删除Marker
	 * @param id
	 * @return
	 */
	@ApiOperation(value="删除Marker",response=Marker.class)
	@ApiResponses(value = {
	            @ApiResponse(code = 200, message = "Success"),
	            @ApiResponse(code = 400, message = "Bad request"),
	            @ApiResponse(code = 500, message = "Internal Server Error")})
	@DeleteMapping(path="/delete/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
	public Boolean deleteMarker(@ApiParam(value="marker id",required=true) @PathVariable("id")  @NotNull Long id){
		LOG.info("Enter MarkerApi deleteMarker");
		platformMarkerService.deleteMarkerById(id);
		return true;
		
	}
	
	/**
	 * 根据Marker ID查询已分配车辆
	 * @param markerVehicleDto
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value="根据Marker ID查询已分配车辆",response=Marker.class)
	@ApiResponses(value = {
	            @ApiResponse(code = 200, message = "Success"),
	            @ApiResponse(code = 400, message = "Bad request"),
	            @ApiResponse(code = 500, message = "Internal Server Error")})
	@PostMapping(path="/findMarkerAvialiableVehicles",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public PagModel findMarkerAvialiableVehicles(@ApiParam(value="查询marker已分配车辆分页参数",required=true)
			@RequestBody @Valid @NotNull MarkerVehiclePageDto markerVehiclePageDto) throws Exception{
		LOG.info("Enter MarkerApi findMarkerAvialiableVehicles");
		return platformMarkerService.findMarkerAvialiableVehicles(getUserById(markerVehiclePageDto.getUserId()),markerVehiclePageDto);
		
	}
	

	/**
	 * 根据Marker ID查询Marker可分配车辆
	 * @param markerVehicleDto
	 * @return 
	 * @throws Exception 
	 */
	@ApiOperation(value="根据Marker ID查询Marker可分配车辆",response=Marker.class)
	@ApiResponses(value = {
	            @ApiResponse(code = 200, message = "Success"),
	            @ApiResponse(code = 400, message = "Bad request"),
	            @ApiResponse(code = 500, message = "Internal Server Error")})
	@PostMapping(path="/findMarkerAssignedVehicles",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public PagModel findMarkerAssignedVehicles(@ApiParam(value="查询marker可分配车辆分页参数",required=true) 
			@RequestBody @Valid @NotNull MarkerVehiclePageDto markerVehiclePageDto) throws Exception{
		LOG.info("Enter MarkerApi findMarkerAvialiableVehicles");
		return platformMarkerService.findMarkerAssignedVehicles(getUserById(markerVehiclePageDto.getUserId()),markerVehiclePageDto);
		
	}
	
	/**
	 * 为Marker分配车辆
	 * @param markerAssignVehicleDto
	 * @return 
	 */
	@ApiOperation(value="为Marker分配车辆",response=Marker.class)
	@ApiResponses(value = {
	            @ApiResponse(code = 200, message = "Success"),
	            @ApiResponse(code = 400, message = "Bad request"),
	            @ApiResponse(code = 500, message = "Internal Server Error")})
	@PostMapping(path="/assignVehicles",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public Boolean assignVehicles(@ApiParam(value="分配车辆参数",required=true) 
			@RequestBody @Valid @NotNull MarkerAssignVehicleDto markerAssignVehicleDto){
		LOG.info("Enter MarkerApi assignVehicles");
		platformMarkerService.assignVehicles(markerAssignVehicleDto);
		return true;
		
	}
	

	/**
	 * 移除Marker分配车辆
	 * @param markerUnassignVehicleDto
	 * @return 
	 */
	@ApiOperation(value="移除Marker分配车辆",response=Marker.class)
	@ApiResponses(value = {
	            @ApiResponse(code = 200, message = "Success"),
	            @ApiResponse(code = 400, message = "Bad request"),
	            @ApiResponse(code = 500, message = "Internal Server Error")})
	@PostMapping(path="/unassignVehicles",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	public Boolean unassignVehicles(@ApiParam(value="移除车辆参数",required=true)
			@RequestBody @Valid @NotNull MarkerUnassignVehicleDto markerUnassignVehicleDto){
		LOG.info("Enter MarkerApi unassignVehicles");
		platformMarkerService.unassignVehicles(markerUnassignVehicleDto);
		return true;
		
	}
   
}
