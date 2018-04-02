package com.cmdt.carday.microservice.api.portal;

import com.cmdt.carday.microservice.api.BaseApi;
import com.cmdt.carday.microservice.biz.service.PlatformStationService;
import com.cmdt.carday.microservice.model.request.station.StationCreateDto;
import com.cmdt.carday.microservice.model.request.station.StationPageDto;
import com.cmdt.carday.microservice.model.request.station.StationUnAssignVehicleDto;
import com.cmdt.carday.microservice.model.request.station.StationUpdateDto;
import com.cmdt.carday.microservice.model.request.station.StationVehicleDto;
import com.cmdt.carday.microservice.model.request.station.StationVehiclePageDto;
import com.cmdt.carrental.common.entity.Station;
import com.cmdt.carrental.common.model.PagModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api("/station")
@SwaggerDefinition(info = @Info(contact = @Contact(name = "Sen Li", email = "sen.li@cm-dt.com"), title = "The station api for station-management", version = "1.0.0"))
@RestController
@Validated
@RequestMapping("/station")
public class StationApi extends BaseApi {

    private static final Logger LOG = LoggerFactory.getLogger(StationApi.class);

    @Autowired
    private PlatformStationService platformStationService;


    /**
     * 根据站点名查询站点
     *
     * @param stationPageDto
     * @return
     * @throws Exception 
     */
    @ApiOperation(value="根据站点名查询站点", response=PagModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(path="/findByStationName", consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
    public PagModel findByStationName(@ApiParam(value = "StationPageDto data parameters.", required = true)
    					@RequestBody @Valid @NotNull StationPageDto stationPageDto) throws Exception {
    	
    	LOG.info("Enter StationApi findByStationName");
        return platformStationService.findStationsByName(super.getUserById(stationPageDto.getUserId()),stationPageDto);
    	
    }

    /**
     * 根据企业过滤查询站点用于下拉框选项
     * @param userId
     * @return
     */
    @ApiOperation(value="根据企业过滤查询站点用于下拉框选项",response=List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping(path="/{userId}/listByOrgId",produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Station>  listByOrgId(@ApiParam(value="user id",required=true) @PathVariable("userId") @NotNull Long userId) {
    	LOG.info("Enter StationApi listByOrgId");
    	return platformStationService.listByOrgId(super.getUserById(userId).getOrganizationId());
    }


    /**
     * 新增站点
     * @param stationDto
     * @param stationDto
     * @return
     * @throws Exception 
     */
    @ApiOperation(value="新增站点",response=Station.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(path="/create",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
    public Station create(@ApiParam(value="StationCreateDto data parameters.",required=true)
    		@RequestBody @Valid @NotNull StationCreateDto stationCreateDto) throws Exception {
    	LOG.info("Enter StationApi create");
    	return  platformStationService.createStation(super.getUserById(stationCreateDto.getUserId()),stationCreateDto);
	    	
    }

    /**
     * 根据站点id查询站点
     *
     * @param id 站点id
     * @return
     */
    @ApiOperation(value="根据站点id查询站点",response=Station.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @GetMapping(path="/findStationById/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public Station findById(@ApiParam(value=" station id",required=true)
    		@PathVariable("id") @NotNull Long id) {
    	LOG.info("Enter StationApi findById");
    	return platformStationService.findById(id);
    }


    /**
     * 修改站点信息
     *
     * @param stationDto
     * @return
     */
    @ApiOperation(value="修改站点信息",response=Station.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PutMapping(path="/update",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
    public Station update(@ApiParam(value="stationUpdateDto data parameters.",required=true)
    		@RequestBody @Valid @NotNull StationUpdateDto stationUpdateDto) {
        LOG.info("Enter StationApi update");
    	return platformStationService.updateStation(super.getUserById(stationUpdateDto.getUserId()),stationUpdateDto);
    }

    /**
     * 删除站点
     *
     * @param Id
     * @return
     */
    @ApiOperation(value="删除站点")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @DeleteMapping(path="/delete/{id}",produces=MediaType.APPLICATION_JSON_VALUE)
    public Boolean delete(@ApiParam(value=" station id ",required=true)
    		@PathVariable("id") @NotNull Long Id) {
        LOG.info("Enter StationApi delete");
    	platformStationService.deleteStation(Id);
    	return true;
    }

    /**
     * 根据站点ID查询已分配车辆
     * @param stationQueryDto
     * @return
     * @throws Exception 
     */
    @ApiOperation(value="根据站点ID查询已分配车辆",response=PagModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(path="/findStationAssignedVehicles",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
    public PagModel findStationAssignedVehicles(@ApiParam(value="StationVehiclePageDto data parameters.",required=true)
    		@RequestBody @Valid @NotNull StationVehiclePageDto stationVehiclePageDto) throws Exception {
        LOG.info("Enter StationApi findStationAssignedVehicles");
    	return platformStationService.findStationAssignedVehicles(super.getUserById(stationVehiclePageDto.getUserId()),stationVehiclePageDto);
   
    }

    /**
     * 根据站点ID查询站点可分配车辆
     * @param stationQueryDto
     * @return
     * @throws Exception 
     */
    @ApiOperation(value="根据站点ID查询站点可分配车辆",response=PagModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(path="/findStationAvailableVehicles",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
    public PagModel findStationAvailableVehicles(@ApiParam(value="StationVehiclePageDto data parameters.",required=true)
    		@RequestBody @Valid @NotNull StationVehiclePageDto stationVehiclePageDto) throws Exception{
        LOG.info("Enter StationApi findStationAvailableVehicles");
    	return platformStationService.findStationAvialiableVehicles(super.getUserById(stationVehiclePageDto.getUserId()),stationVehiclePageDto);
    
    }

    /**
     * 为站点分配车辆
     * @param stationVehicleDto
     * @return
     */
    @ApiOperation(value="为站点分配车辆")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(path="/assignVehicles",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
    public Boolean assignVehicles(@ApiParam(value="StationVehicleDto data parameters.",required=true)
    		@RequestBody @Valid @NotNull StationVehicleDto stationVehicleDto){
        LOG.info("Enter StationApi assignVehicles");
    	platformStationService.assignVehicles(stationVehicleDto);
    	return true;
    }

    /**
     * 移除站点分配车辆
     * @param stationVehicleDto
     * @return
     */
    @ApiOperation(value=" 移除站点分配车辆")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(path="/unassignVehicles",consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
    public Boolean unassignVehicles(@ApiParam(value="StationUnAssignVehicleDto data parameters.",required=true)
    		@RequestBody @Valid @NotNull StationUnAssignVehicleDto stationUnAssignVehicleDto){
        LOG.info("Enter StationApi unassignVehicles");
    	platformStationService.unassignVehicles(stationUnAssignVehicleDto);
    	return true;
	   
    }
}
