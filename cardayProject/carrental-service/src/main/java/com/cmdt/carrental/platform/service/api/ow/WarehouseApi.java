package com.cmdt.carrental.platform.service.api.ow;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.api.portal.EmployeeApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformWarehouseService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.common.WsResponse;
import com.cmdt.carrental.platform.service.model.request.warehouse.WarehouseDto;

@Produces(MediaType.APPLICATION_JSON)
public class WarehouseApi extends BaseApi {
	private static final Logger LOG = LoggerFactory.getLogger(EmployeeApi.class);
	
	@Autowired
	private PlatformWarehouseService warehouseService;
	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)  
	public Response createWarehouse(@Valid @NotNull WarehouseDto dto){
		WsResponse<Object> wsResponse = new WsResponse<>();
		try{
			Map<String,String> map = warehouseService.createWarehouse(dto);
			wsResponse.getMessages().add(map.get(Constants.MSG_STR));
			wsResponse.setStatus(map.get(Constants.STATUS_STR));
		}catch(Exception e){
			LOG.error("EmployeeApi createEmployee error, cause by: : ",e);
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
		}
		return Response.ok( wsResponse ).build();
	}
}
