package com.cmdt.carrental.mobile.gateway.api.inf;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cmdt.carrental.common.model.BusiOrderDto;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;

@Produces(MediaType.APPLICATION_JSON)
public interface IBusiOrderService {
	public Response createOrder(@Valid @NotNull BusiOrderDto dto);
	public Response updateOrder(@Valid @NotNull BusiOrderDto dto);
	public Response viewOrder(@Valid @NotNull BusiOrderQueryDto dto);
	public Response deleteOrder(@Valid @NotNull BusiOrderDto dto);
	public Response recreateOrder(@Valid @NotNull BusiOrderDto dto);
	public Response recreateUpdate(@Valid @NotNull BusiOrderDto dto);
	public Response audit(@Valid @NotNull BusiOrderDto dto);
	public Response auditSendMsg(@Valid @NotNull BusiOrderDto dto);
	public Response auditHistory(@Valid @NotNull BusiOrderQueryDto dto);
	public Response getAvailableVehicles(@Valid @NotNull BusiOrderQueryDto dto);
	public Response getAvailableDrivers(@Valid @NotNull BusiOrderQueryDto dto);
	public Response allocate(@Valid @NotNull BusiOrderDto dto);
	public Response orderVehicleOut(@Valid @NotNull BusiOrderDto dto);
	public Response orderReachFromPlace(@Valid @NotNull BusiOrderDto dto);
	public Response orderOngoing(@Valid @NotNull BusiOrderDto dto);
	public Response orderWaiting(@Valid @NotNull BusiOrderDto dto);
	public Response orderFinish(@Valid @NotNull BusiOrderDto dto);
	public Response orderCancel(@Valid @NotNull BusiOrderDto dto);
	public Response orderList(@Valid @NotNull BusiOrderQueryDto dto);
	public Response auditList(@Valid @NotNull BusiOrderQueryDto dto);
	public Response allocateList(@Valid @NotNull BusiOrderQueryDto dto);
	public Response orderListCurrentForDriver(BusiOrderQueryDto dto);
	public Response orderListHistoryForDriver(BusiOrderQueryDto dto);
	public Response recreateOrderEmpList(@Valid @NotNull BusiOrderQueryDto dto);
	public Response recreateOrderDriverList(@Valid @NotNull BusiOrderQueryDto dto);
	public Response recreateOrderVehicleList(@Valid @NotNull BusiOrderQueryDto dto);
}
