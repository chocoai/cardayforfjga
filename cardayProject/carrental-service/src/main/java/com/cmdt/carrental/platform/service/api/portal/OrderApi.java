package com.cmdt.carrental.platform.service.api.portal;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.entity.BusiOrder;
import com.cmdt.carrental.common.entity.BusiOrderAuditRecord;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.EmployeeModel;
import com.cmdt.carrental.common.model.OrderSchedule;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleModel;
import com.cmdt.carrental.common.model.VehicleSchedule;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformOrderService;
import com.cmdt.carrental.common.model.BusiOrderDto;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;

@Produces(MediaType.APPLICATION_JSON)
public class OrderApi extends BaseApi{
	@Autowired
    private PlatformOrderService orderService;

	@POST
	@Path("/list")
	@Consumes(MediaType.APPLICATION_JSON)  
	public PagModel orderListForApp(@Valid @NotNull BusiOrderQueryDto dto){
		return orderService.orderListForApp(getUserById(dto.getLoginUserId()),dto);
	}
	
	@POST
	@Path("/admin/list")
	@Consumes(MediaType.APPLICATION_JSON)  
	public PagModel orderListForPortal(@Valid @NotNull BusiOrderQueryDto dto){
		return orderService.orderListForPortal(getUserById(dto.getLoginUserId()),dto);
	}
	
	@POST
	@Path("/auditList")
	@Consumes(MediaType.APPLICATION_JSON)  
	public PagModel auditList(@Valid @NotNull BusiOrderQueryDto dto){
		return orderService.auditList(getUserById(dto.getLoginUserId()),dto);
	}
	
	@POST
	@Path("/allocateList")
	@Consumes(MediaType.APPLICATION_JSON)  
	public PagModel allocateList(@Valid @NotNull BusiOrderQueryDto dto){
		return orderService.allocateList(getUserById(dto.getLoginUserId()),dto);
	}
	
    @POST
    @Path("/driver/currentList")
    public PagModel orderListCurrentForDriver(@Valid @NotNull BusiOrderQueryDto dto) {
    	return orderService.orderListCurrentForDriver(getUserById(dto.getLoginUserId()),dto);
    }
    
    @POST
    @Path("/driver/historyList")
    public PagModel orderListHistoryForDriver(@Valid @NotNull BusiOrderQueryDto dto) {
    	return orderService.orderListHistoryForDriver(getUserById(dto.getLoginUserId()),dto);
    }
    
    @POST
    @Path("/recreateOrderEmpList")
    public List<EmployeeModel> recreateOrderEmpList(@Valid @NotNull BusiOrderQueryDto dto) {
    	return orderService.recreateOrderEmpList(dto);
    }
    
    @POST
    @Path("/recreateOrderDriverList")
    public List<DriverModel> recreateOrderDriverList(@Valid @NotNull BusiOrderQueryDto dto) {
    	return orderService.recreateOrderDriverList(dto);
    }
    
    @POST
    @Path("/recreateOrderVehicleList")
    public List<VehicleModel> recreateOrderVehicleList(@Valid @NotNull BusiOrderQueryDto dto) {
    	return orderService.recreateOrderVehicleList(dto);
    }
	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)  
	public void create(@Valid @NotNull BusiOrderDto dto){
		orderService.createOrder(getUserById(dto.getLoginUserId()),dto);
	}
	
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)  
	public void update(@Valid @NotNull BusiOrderDto dto){
		orderService.updateOrder(getUserById(dto.getLoginUserId()),dto);
	}

	@POST
	@Path("/recreate")
	@Consumes(MediaType.APPLICATION_JSON)  
	public void recreate(@Valid @NotNull BusiOrderDto dto){
		orderService.recreateOrder(getUserById(dto.getLoginUserId()),dto);
	}
	
	@POST
	@Path("/recreateUpdate")
	@Consumes(MediaType.APPLICATION_JSON)  
	public void recreateUpdate(@Valid @NotNull BusiOrderDto dto){
		orderService.updateReCreateOrder(getUserById(dto.getLoginUserId()),dto);
	}
	
	@GET
	@Path("/delete/{userId}/{id}")
	@Consumes(MediaType.APPLICATION_JSON)  
	public void delete(@PathParam("userId") Long userId, @PathParam("id") Long orderId){
		orderService.deleteOrder(getUserById(userId),orderId);
	}
	
	@GET
	@Path("/{userId}/{orderId}/view")
	@Consumes(MediaType.APPLICATION_JSON)  
	public BusiOrder viewOrder(@PathParam("userId") Long userId, @PathParam("orderId") Long orderId){
		return orderService.viewOrder(userId,orderId);
	}
	
	@POST
	@Path("/audit")
	@Consumes(MediaType.APPLICATION_JSON)  
	public void audit(@Valid @NotNull BusiOrderDto dto){
		orderService.orderAudit(getUserById(dto.getLoginUserId()),dto);
	}
	
	@POST
	@Path("/auditSendMsg")
	@Consumes(MediaType.APPLICATION_JSON)  
	public void auditSendMsg(@Valid @NotNull BusiOrderDto dto){
		orderService.auditSendMsg(getUserById(dto.getLoginUserId()), dto);
	}
	
	@GET
	@Path("/auditHistory/{userId}/{orderId}")
	public List<BusiOrderAuditRecord> auditHistory(@PathParam("userId") Long userId, @PathParam("orderId") Long orderId){
		return orderService.auditHistory(getUserById(userId), orderId);
	}
	
	@POST
	@Path("/availableDrivers")
	@Consumes(MediaType.APPLICATION_JSON)  
	public PagModel getAvailableDrivers(@Valid @NotNull BusiOrderQueryDto dto){
		return orderService.getAvailableDrivers(getUserById(dto.getLoginUserId()), dto);
	}
	
	@GET
	@Path("/schedule/{vehicleId}/{orderId}")
	@Consumes(MediaType.APPLICATION_JSON)  
	public List<OrderSchedule> showOrderSchedule(@PathParam("vehicleId") Long vehicleId, @PathParam("orderId") Long orderId){
		return orderService.showOrderSchedule(vehicleId, orderId);
	}
	
	@POST
	@Path("/allocate")
	@Consumes(MediaType.APPLICATION_JSON)  
	public void allocate(@Valid @NotNull BusiOrderDto dto){
		orderService.orderAllocate(getUserById(dto.getLoginUserId()),dto);
	}
	
    @POST
    @Path("/vehicleOut")
    public void orderVehicleOut(@Valid @NotNull BusiOrderDto dto) {
    	orderService.orderVehicleOut(getUserById(dto.getLoginUserId()),dto);
    }
    
    @POST
    @Path("/reachFromPlace")
    public void orderReachFromPlace(@Valid @NotNull BusiOrderDto dto) {
    	orderService.orderReachFromPlace(getUserById(dto.getLoginUserId()),dto);
    }
    
    @POST
    @Path("/ongoing")
    public void orderOngoing(@Valid @NotNull BusiOrderDto dto) {
    	orderService.orderOngoing(getUserById(dto.getLoginUserId()),dto);
    }
    
    @POST
    @Path("/waiting")
    public void orderWaiting(@Valid @NotNull BusiOrderDto dto) {
    	orderService.orderWaiting(getUserById(dto.getLoginUserId()),dto);
    }
    
    @POST
    @Path("/finish")
    public void orderFinish(@Valid @NotNull BusiOrderDto dto) {
    	orderService.orderFinish(getUserById(dto.getLoginUserId()),dto);
    }
	
	@POST
	@Path("/cancel")
	@Consumes(MediaType.APPLICATION_JSON)  
	public void orderCancel(@Valid @NotNull BusiOrderDto dto){
		orderService.orderCancel(getUserById(dto.getLoginUserId()),dto.getId());
	}
	
	@GET
	@Path("/vehicleSchedule/{vehicleId}/{planStTime}")
	@Consumes(MediaType.APPLICATION_JSON)  
	public List<VehicleSchedule> queryVehicleSchedule(@PathParam("vehicleId") Long vehicleId, @PathParam("planStTime") String planStTimeF){
		return orderService.queryVehicleSchedule(vehicleId,planStTimeF);
	}
}
