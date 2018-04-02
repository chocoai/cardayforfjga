package com.cmdt.carrental.platform.service.api.portal;

import com.cmdt.carrental.common.entity.Station;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformStationService;
import com.cmdt.carrental.platform.service.model.request.station.StationCreateDto;
import com.cmdt.carrental.platform.service.model.request.station.StationPageDto;
import com.cmdt.carrental.platform.service.model.request.station.StationUnAssignVehicleDto;
import com.cmdt.carrental.platform.service.model.request.station.StationUpdateDto;
import com.cmdt.carrental.platform.service.model.request.station.StationVehicleDto;
import com.cmdt.carrental.platform.service.model.request.station.StationVehiclePageDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
public class StationApi extends BaseApi {

    private static final Logger LOG = LoggerFactory.getLogger(StationApi.class);

    @Autowired
    private PlatformStationService platformStationService;


    /**
     * 根据站点名查询站点
     *
     * @param stationPageDto
     * @return
     */
    @POST
    @Path("/findByStationName")
    @Consumes(MediaType.APPLICATION_JSON) 
    public PagModel findByStationName(@Valid @NotNull StationPageDto stationPageDto) {
    	
    	LOG.info("Enter StationApi findByStationName");
        return platformStationService.findStationsByName(super.getUserById(stationPageDto.getUserId()),stationPageDto);
    	
    }

    /**
     * 根据企业过滤查询站点用于下拉框选项
     *
     * @param userId
     * @return
     */
    @GET
    @Path("/{userId}/listByOrgId")
    public List<Station>  listByOrgId(@PathParam("userId") @NotNull Long userId) {
    	LOG.info("Enter StationApi listByOrgId");
    	return platformStationService.listByOrgId(super.getUserById(userId).getOrganizationId());
    }


    /**
     * 新增站点
     * @param stationDto
     * @param stationDto
     * @return
     */
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON) 
    public Station create(@Valid @NotNull StationCreateDto stationCreateDto) {
    	LOG.info("Enter StationApi create");
    	return  platformStationService.createStation(super.getUserById(stationCreateDto.getUserId()),stationCreateDto);
	    	
    }

    /**
     * 根据站点id查询站点
     *
     * @param id 站点id
     * @return
     */
    @GET
    @Path("/{id}/findById")
    public Station findById(@PathParam("id") @NotNull Long id) {
    	LOG.info("Enter StationApi findById");
    	return platformStationService.findById(id);
    }


    /**
     * 修改站点信息
     *
     * @param stationDto
     * @return
     */
    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON) 
    public Station update(@Valid @NotNull StationUpdateDto stationUpdateDto) {
        LOG.info("Enter StationApi update");
    	return platformStationService.updateStation(super.getUserById(stationUpdateDto.getUserId()),stationUpdateDto);
    }

    /**
     * 删除站点
     *
     * @param Id
     * @return
     */
    @GET
    @Path("/{id}/delete")
    public void delete(@PathParam("id") @NotNull Long Id) {
        LOG.info("Enter StationApi delete");
    	platformStationService.deleteStation(Id);
    }

    /**
     * 根据站点ID查询已分配车辆
     * @param stationQueryDto
     * @return
     */
    @POST
    @Path("/findStationAssignedVehicles")
    @Consumes(MediaType.APPLICATION_JSON)  
    public PagModel findStationAssignedVehicles(@Valid @NotNull StationVehiclePageDto stationVehiclePageDto) {
        LOG.info("Enter StationApi findStationAssignedVehicles");
    	return platformStationService.findStationAssignedVehicles(super.getUserById(stationVehiclePageDto.getUserId()),stationVehiclePageDto);
   
    }

    /**
     * 根据站点ID查询站点可分配车辆
     * @param stationQueryDto
     * @return
     */
    @POST
    @Path("/findStationAvailableVehicles")
    @Consumes(MediaType.APPLICATION_JSON)  
    public PagModel findStationAvailableVehicles(@Valid @NotNull StationVehiclePageDto stationVehiclePageDto){
        LOG.info("Enter StationApi findStationAvailableVehicles");
    	return platformStationService.findStationAvialiableVehicles(super.getUserById(stationVehiclePageDto.getUserId()),stationVehiclePageDto);
    
    }

    /**
     * 为站点分配车辆
     * @param stationVehicleDto
     * @return
     */
    @POST
    @Path("/assignVehicles")
    @Consumes(MediaType.APPLICATION_JSON)  
    public void assignVehicles(@Valid @NotNull StationVehicleDto stationVehicleDto){
        LOG.info("Enter StationApi assignVehicles");
    	platformStationService.assignVehicles(stationVehicleDto);
    }

    /**
     * 移除站点分配车辆
     * @param stationVehicleDto
     * @return
     */
    @POST
    @Path("/unassignVehicles")
    @Consumes(MediaType.APPLICATION_JSON)  
    public void unassignVehicles(@Valid @NotNull StationUnAssignVehicleDto stationUnAssignVehicleDto){
        LOG.info("Enter StationApi unassignVehicles");
    	platformStationService.unassignVehicles(stationUnAssignVehicleDto);
	   
    }
}
