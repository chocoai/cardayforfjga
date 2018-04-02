package com.cmdt.carrental.platform.service.api.portal;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.entity.Rent;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformRentService;

@Produces(MediaType.APPLICATION_JSON)
public class RentApi extends BaseApi {
	
	private static final Logger LOG = LoggerFactory.getLogger(RentApi.class);
	
	@Autowired
	private PlatformRentService platformRentService;

	@GET
	@Path("/list/{userId}")
	public List<Rent> showRentList(@PathParam("userId")  @NotNull Long userId){
		
		return platformRentService.showRentList(super.getUserById(userId));
		
	}
	
	
}
