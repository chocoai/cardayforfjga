package com.cmdt.carrental.platform.service.api.portal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.entity.Marker;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformMarkerService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.common.WsResponse;
import com.cmdt.carrental.platform.service.model.request.marker.MarkerAssignVehicleDto;
import com.cmdt.carrental.platform.service.model.request.marker.MarkerCreateDto;
import com.cmdt.carrental.platform.service.model.request.marker.MarkerPageDto;
import com.cmdt.carrental.platform.service.model.request.marker.MarkerUnassignVehicleDto;
import com.cmdt.carrental.platform.service.model.request.marker.MarkerUpdateDto;
import com.cmdt.carrental.platform.service.model.request.marker.MarkerVehiclePageDto;

@Produces(MediaType.APPLICATION_JSON)
public class MarkerApi extends BaseApi{
	private static final Logger LOG = LoggerFactory.getLogger(MarkerApi.class);
	
	@Autowired
	private PlatformMarkerService platformMarkerService;
	
	/**
	 * 根据Marker名查询Marker
	 * @param markerPageDto
	 * @return
	 */
	@POST
	@Path("/findByName")
	@Consumes(MediaType.APPLICATION_JSON)  
	public PagModel listMarker(@Valid @NotNull MarkerPageDto markerPageDto){
		LOG.info("Enter MarkerApi list");
		return platformMarkerService.listMarker(getUserById(markerPageDto.getUserId()),markerPageDto);
		
	}
	
	/**
	 * 新增Marker
	 * 
	 * @param markerCreateDto
	 * @return
	 */
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)  
	public Marker createMarker(@Valid @NotNull MarkerCreateDto markerCreateDto){
		LOG.info("Enter MarkerApi createMarker");
		return platformMarkerService.createMarker(getUserById(markerCreateDto.getUserId()),markerCreateDto);
	}
	

	/**
	 * 根据id查询Marker
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("/find/{id}")
	public Marker findById(@PathParam("id")  @NotNull Long id){
		LOG.info("Enter MarkerApi findById");
		return platformMarkerService.findMarkerById(id);
	}
	
	/**
	 * 修改Marker信息
	 * @param markerUpdateDto
	 * @return
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)  
	public Marker updateMarker(@Valid @NotNull MarkerUpdateDto markerUpdateDto){
		LOG.info("Enter MarkerApi updateMarker");
		return platformMarkerService.updateMarker(getUserById(markerUpdateDto.getUserId()),markerUpdateDto);
	}
	

	/**
	 * 删除Marker
	 * @param id
	 * @return
	 */
	@GET
	@Path("/delete/{id}")
	public void deleteMarker(@PathParam("id")  @NotNull Long id){
		LOG.info("Enter MarkerApi deleteMarker");
		platformMarkerService.deleteMarkerById(id);
		
	}
	
	/**
	 * 根据Marker ID查询已分配车辆
	 * @param markerVehicleDto
	 * @return 
	 */
	@POST
	@Path("/findMarkerAvialiableVehicles")
	@Consumes(MediaType.APPLICATION_JSON)  
	public PagModel findMarkerAvialiableVehicles(@Valid @NotNull MarkerVehiclePageDto markerVehiclePageDto){
		LOG.info("Enter MarkerApi findMarkerAvialiableVehicles");
		return platformMarkerService.findMarkerAvialiableVehicles(getUserById(markerVehiclePageDto.getUserId()),markerVehiclePageDto);
		
	}
	

	/**
	 * 根据Marker ID查询Marker可分配车辆
	 * @param markerVehicleDto
	 * @return 
	 */
	@POST
	@Path("/findMarkerAssignedVehicles")
	@Consumes(MediaType.APPLICATION_JSON)  
	public PagModel findMarkerAssignedVehicles(@Valid @NotNull MarkerVehiclePageDto markerVehiclePageDto){
		LOG.info("Enter MarkerApi findMarkerAvialiableVehicles");
		return platformMarkerService.findMarkerAssignedVehicles(getUserById(markerVehiclePageDto.getUserId()),markerVehiclePageDto);
		
	}
	
	/**
	 * 为Marker分配车辆
	 * @param markerAssignVehicleDto
	 * @return 
	 */
	@POST
	@Path("/assignVehicles")
	@Consumes(MediaType.APPLICATION_JSON)  
	public void assignVehicles(@Valid @NotNull MarkerAssignVehicleDto markerAssignVehicleDto){
		LOG.info("Enter MarkerApi assignVehicles");
		platformMarkerService.assignVehicles(markerAssignVehicleDto);
		
	}
	

	/**
	 * 移除Marker分配车辆
	 * @param markerUnassignVehicleDto
	 * @return 
	 */
	@POST
	@Path("/unassignVehicles")
	@Consumes(MediaType.APPLICATION_JSON)  
	public void unassignVehicles(@Valid @NotNull MarkerUnassignVehicleDto markerUnassignVehicleDto){
		LOG.info("Enter MarkerApi unassignVehicles");
		platformMarkerService.unassignVehicles(markerUnassignVehicleDto);
		
	}
   
}
